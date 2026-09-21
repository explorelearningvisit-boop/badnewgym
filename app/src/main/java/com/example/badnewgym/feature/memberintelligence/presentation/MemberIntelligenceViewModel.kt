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
import com.example.badnewgym.feature.memberintelligence.domain.repository.MemberIntelligenceRepository
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.integration.MemberIntelligenceNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberIntelligenceViewModel(
    private val repository: MemberIntelligenceRepository = StubMemberRepositoryImpl(),
    private val navigator: MemberIntelligenceNavigator = MemberIntelligenceNavigator.NoOp
) : ViewModel() {
    private val engine = MemberIntelligenceEngine()

    private val _state = MutableStateFlow<MemberIntelligenceUiState>(MemberIntelligenceUiState.Loading)
    val state: StateFlow<MemberIntelligenceUiState> = _state.asStateFlow()

    private var themeId: ThemeId = ThemeId.NATURAL_FRESH

    fun loadMemberData(memberId: String, gymId: String = "gym1") {
        viewModelScope.launch {
            _state.value = MemberIntelligenceUiState.Loading
            val snapshotResult = repository.getMemberSnapshot(memberId)
            if (snapshotResult.isFailure) {
                _state.value = MemberIntelligenceUiState.Error("Failed to load member snapshot")
                return@launch
            }
            val snapshot = snapshotResult.getOrThrow()
            val event = snapshot.recentEvents.firstOrNull() ?: MemberEvent(
                id = "init_0",
                memberId = memberId,
                gymId = gymId,
                eventType = EventType.CHECK_IN,
                occurredAt = System.currentTimeMillis(),
                source = EventSource.SYSTEM
            )
            publishSuccess(snapshot, event, MenuType.HOME)
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
        loadMemberData(theme.defaultMemberId)
    }

    fun executeCta(action: SignalAction) {
        navigator.onAction(action)
    }

    private fun publishSuccess(
        snapshot: com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot,
        event: MemberEvent,
        menu: MenuType
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
            themeId = themeId
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
