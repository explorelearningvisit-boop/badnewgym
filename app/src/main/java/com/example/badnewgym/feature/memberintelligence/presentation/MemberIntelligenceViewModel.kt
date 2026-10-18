package com.example.badnewgym.feature.memberintelligence.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.engine.MemberIntelligenceEngine
import com.example.badnewgym.feature.memberintelligence.domain.engine.MenuAvailabilityResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.EventSource
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.domain.model.StubGymWidgetAccessRepository
import com.example.badnewgym.feature.memberintelligence.domain.model.GymWidgetAccessRepository
import com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetEntitlements
import com.example.badnewgym.feature.memberintelligence.domain.format.MoneyFormat
import com.example.badnewgym.feature.memberintelligence.domain.repository.MemberIntelligenceRepository
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.integration.MemberIntelligenceNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberIntelligenceViewModel(
    private val repository: MemberIntelligenceRepository = StubMemberRepositoryImpl(),
    private val navigator: MemberIntelligenceNavigator = MemberIntelligenceNavigator.NoOp,
    private val widgetAccessRepository: GymWidgetAccessRepository = StubGymWidgetAccessRepository()
) : ViewModel() {
    private val engine = MemberIntelligenceEngine()

    private val _state = MutableStateFlow<MemberIntelligenceUiState>(MemberIntelligenceUiState.Loading)
    val state: StateFlow<MemberIntelligenceUiState> = _state.asStateFlow()

    /** One-shot user notices (snackbar level) for actions that complete or are not connected. */
    private val _notices = MutableSharedFlow<String>(extraBufferCapacity = 4)
    val notices: SharedFlow<String> = _notices

    private var themeId: ThemeId = ThemeId.NATURAL_FRESH
    private var lastMemberId: String = "BG204"

    fun loadMemberData(memberId: String, gymId: String = "gym1") {
        // Never reload the business snapshot just because the visual theme changes.
        // This keeps theme transitions instant and prevents the old screen from
        // flashing/replacing member data while the user browses themes.
        lastMemberId = memberId
        viewModelScope.launch {
            _state.value = MemberIntelligenceUiState.Loading
            val snapshotResult = repository.getMemberSnapshot(memberId)
            if (snapshotResult.isFailure) {
                _state.value = MemberIntelligenceUiState.Error("Failed to load member snapshot")
                return@launch
            }

            val snapshot = snapshotResult.getOrThrow()
            val widgetEntitlements = widgetAccessRepository.getAccess(gymId)
            val event = snapshot.recentEvents.firstOrNull() ?: MemberEvent(
                id = "init_0",
                memberId = memberId,
                gymId = gymId,
                eventType = EventType.CHECK_IN,
                occurredAt = System.currentTimeMillis(),
                source = EventSource.SYSTEM
            )
            publishSuccess(snapshot, event, MenuType.HOME, widgetEntitlements)
        }
    }

    fun selectMenu(menu: MenuType) {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        val target = current.menus.firstOrNull { it.id == menu } ?: return
        if (target.isLocked || !target.isEnabled) return
        _state.value = current.copy(activeMenu = menu)
    }

    fun selectTheme(theme: ThemeId) {
        themeId = theme
        val current = _state.value as? MemberIntelligenceUiState.Success
        if (current == null) return

        // Theme is presentation state, not data state.
        // Preserve the loaded snapshot, active menu, intelligence signals and CTA.
        _state.value = current.copy(themeId = theme)
    }

    fun executeCta(action: SignalAction) {
        when (action.actionType) {
            "COLLECT_PAYMENT" -> recordOutstandingPayment()
            else -> _notices.tryEmit(
                "${action.label} belongs to another module and is not connected in this build yet."
            )
        }
    }

    /**
     * Records the member's full outstanding amount through the repository and
     * refreshes the snapshot so the payment panel reflects the real recorded state.
     */
    private fun recordOutstandingPayment() {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        val amount = current.snapshot.payment?.totalOutstanding ?: 0.0
        if (amount <= 0.0) {
            _notices.tryEmit("Nothing is due. The payment is already settled.")
            return
        }
        val event = MemberEvent(
            id = "collect_${System.currentTimeMillis()}",
            memberId = lastMemberId,
            gymId = "gym1",
            eventType = EventType.PAYMENT,
            occurredAt = System.currentTimeMillis(),
            source = EventSource.STAFF,
            metadata = mapOf("amount" to amount.toString()),
            idempotencyKey = "collect_${lastMemberId}_${System.currentTimeMillis()}"
        )
        viewModelScope.launch {
            val recorded = repository.recordEvent(event)
            if (recorded.isFailure) {
                _notices.tryEmit("Payment could not be recorded. The desk flow must be connected first.")
                return@launch
            }
            val refreshed = repository.refreshMember(lastMemberId)
            val refreshedSnapshot = refreshed.getOrNull()
            if (refreshedSnapshot == null) {
                _notices.tryEmit("Payment recorded, but the refreshed member data failed to load.")
                return@launch
            }
            publishSuccess(
                snapshot = refreshedSnapshot,
                event = current.currentEvent,
                menu = current.activeMenu,
                widgetEntitlements = current.visitWidgetEntitlements
            )
            _notices.tryEmit(
                "Recorded payment of ${MoneyFormat.inr(amount)} for ${refreshedSnapshot.identity.name}."
            )
        }
    }

    private fun publishSuccess(
        snapshot: com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot,
        event: MemberEvent,
        menu: MenuType,
        widgetEntitlements: VisitWidgetEntitlements
    ) {
        val result = engine.evaluate(snapshot, event, System.currentTimeMillis())
        val menus = MenuAvailabilityResolver.resolve(snapshot, result.signals)
        _state.value = MemberIntelligenceUiState.Success(
            snapshot = snapshot,
            currentEvent = event,
            menus = menus,
            signals = result.signals,
            primarySignal = result.primary,
            secondarySignals = result.secondary,
            cta = result.cta,
            activeMenu = menu,
            themeId = themeId,
            visitWidgetEntitlements = widgetEntitlements
        )
    }

    companion object {
        fun provideFactory(repository: MemberIntelligenceRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MemberIntelligenceViewModel(repository) as T
                }
            }
    }
}
