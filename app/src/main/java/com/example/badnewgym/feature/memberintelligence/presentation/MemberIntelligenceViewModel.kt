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
    private var isDetailExpanded: Boolean = false

    fun loadMemberData(memberId: String = "BG204", gymId: String = "gym1") {
        viewModelScope.launch {
            _state.value = MemberIntelligenceUiState.Loading
            val allMembersResult = repository.getAllMembers(gymId)
            val allPairs = allMembersResult.getOrNull().orEmpty()

            val memberPairs = if (allPairs.isNotEmpty()) {
                allPairs
            } else {
                val singleSnapshotResult = repository.getMemberSnapshot(memberId)
                if (singleSnapshotResult.isFailure) {
                    _state.value = MemberIntelligenceUiState.Error("Failed to load member snapshot")
                    return@launch
                }
                val snap = singleSnapshotResult.getOrThrow()
                val ev = snap.recentEvents.firstOrNull() ?: MemberEvent(
                    id = "init_0",
                    memberId = memberId,
                    gymId = gymId,
                    eventType = EventType.CHECK_IN,
                    occurredAt = System.currentTimeMillis(),
                    source = EventSource.SYSTEM
                )
                listOf(snap to ev)
            }

            val now = System.currentTimeMillis()
            val cardItems = memberPairs.map { (snap, ev) ->
                val eval = engine.evaluate(snap, ev, now)
                val cardTheme = themeForMember(snap.id, snap.identity.code)
                MemberCardItem(
                    snapshot = snap,
                    currentEvent = ev,
                    themeId = cardTheme,
                    signals = eval.signals,
                    primarySignal = eval.primary,
                    secondarySignals = eval.secondary,
                    cta = eval.cta
                )
            }

            // Find matching index or default to 0
            val targetIndex = memberPairs.indexOfFirst {
                it.first.id == memberId || it.first.identity.code == memberId
            }.takeIf { it >= 0 } ?: 0

            val selectedItem = cardItems[targetIndex]
            themeId = selectedItem.themeId

            val menus = MenuAvailabilityResolver.resolve(selectedItem.snapshot, selectedItem.signals)

            _state.value = MemberIntelligenceUiState.Success(
                snapshot = selectedItem.snapshot,
                currentEvent = selectedItem.currentEvent,
                menus = menus,
                signals = selectedItem.signals,
                primarySignal = selectedItem.primarySignal,
                secondarySignals = selectedItem.secondarySignals,
                cta = selectedItem.cta,
                activeMenu = MenuType.HOME,
                themeId = themeId,
                members = cardItems,
                selectedMemberIndex = targetIndex,
                isDetailExpanded = isDetailExpanded
            )
        }
    }

    fun selectMember(index: Int) {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        if (index !in current.members.indices) return
        val target = current.members[index]
        themeId = target.themeId
        val menus = MenuAvailabilityResolver.resolve(target.snapshot, target.signals)
        _state.value = current.copy(
            snapshot = target.snapshot,
            currentEvent = target.currentEvent,
            menus = menus,
            signals = target.signals,
            primarySignal = target.primarySignal,
            secondarySignals = target.secondarySignals,
            cta = target.cta,
            themeId = themeId,
            selectedMemberIndex = index
        )
    }

    fun openMemberDetail(index: Int? = null) {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        if (index != null && index in current.members.indices) {
            selectMember(index)
        }
        isDetailExpanded = true
        val updated = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = updated.copy(isDetailExpanded = true)
    }

    fun closeMemberDetail() {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        isDetailExpanded = false
        _state.value = current.copy(isDetailExpanded = false)
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
        _state.value = current.copy(themeId = theme)
    }

    fun executeCta(action: SignalAction) {
        navigator.onAction(action)
    }

    private fun themeForMember(id: String, code: String?): ThemeId = when {
        code == "BG204" || id == "1" -> ThemeId.NATURAL_FRESH
        code == "BG105" || id == "2" -> ThemeId.FUTURISTIC_NEON
        code == "BG310" || id == "3" -> ThemeId.MINIMAL_DARK
        code == "BG407" || id == "4" -> ThemeId.GLASSMORPHISM
        code == "BG001" || id == "5" -> ThemeId.PREMIUM_3D
        code == "BG220" || id == "6" -> ThemeId.VIBRANT_GRADIENT
        code == "BG330" || id == "7" -> ThemeId.BEAST_MODE
        code == "BG502" || id == "8" -> ThemeId.PURPLE_ROYAL
        code == "BG901" || id == "9" -> ThemeId.MINIMAL_DARK
        else -> ThemeId.NATURAL_FRESH
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
