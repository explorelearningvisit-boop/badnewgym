package com.example.badnewgym.feature.memberintelligence.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.data.repository.StubTemporalIntelligenceRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.engine.MemberIntelligenceEngine
import com.example.badnewgym.feature.memberintelligence.domain.engine.MenuAvailabilityResolver
import com.example.badnewgym.feature.memberintelligence.domain.fixture.FixtureCardItem
import com.example.badnewgym.feature.memberintelligence.domain.fixture.MemberIntelligenceFixtureUniverse
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import com.example.badnewgym.feature.memberintelligence.domain.repository.MemberIntelligenceRepository
import com.example.badnewgym.feature.memberintelligence.domain.repository.TemporalIntelligenceRepository
import com.example.badnewgym.feature.memberintelligence.integration.MemberIntelligenceNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberIntelligenceViewModel(
    private val repository: MemberIntelligenceRepository = StubMemberRepositoryImpl(),
    private val temporalRepository: TemporalIntelligenceRepository = StubTemporalIntelligenceRepositoryImpl(),
    private val navigator: MemberIntelligenceNavigator = MemberIntelligenceNavigator.NoOp
) : ViewModel() {
    private val engine = MemberIntelligenceEngine()

    private val _state = MutableStateFlow<MemberIntelligenceUiState>(MemberIntelligenceUiState.Loading)
    val state: StateFlow<MemberIntelligenceUiState> = _state.asStateFlow()

    private var themeId: ThemeId = ThemeId.NATURAL_FRESH
    private var isDetailExpanded: Boolean = false
    private var currentTemporalRange: TemporalRange = TemporalRange.forCurrentMonth()

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
                activeMenu = resolveInitialMenu(selectedItem.snapshot, selectedItem.currentEvent, selectedItem.signals, menus),
                themeId = themeId,
                members = cardItems,
                selectedMemberIndex = targetIndex,
                isDetailExpanded = isDetailExpanded,
                temporalRange = currentTemporalRange
            )

            // On initial open, pre-fetch home temporal metrics without downloading entire raw history
            loadMenuTemporalData(MenuType.HOME, currentTemporalRange)
        }
    }

    fun selectMember(index: Int) {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        if (index !in current.members.indices) return
        val target = current.members[index]
        themeId = target.themeId
        val menus = MenuAvailabilityResolver.resolve(target.snapshot, target.signals)
        val nextActiveMenu = if (current.activeMenu == MenuType.HOME) {
            resolveInitialMenu(target.snapshot, target.currentEvent, target.signals, menus)
        } else {
            current.activeMenu.takeIf { active ->
                menus.any { it.id == active && it.isVisible && it.isEnabled }
            } ?: resolveInitialMenu(target.snapshot, target.currentEvent, target.signals, menus)
        }

        _state.value = current.copy(
            snapshot = target.snapshot,
            currentEvent = target.currentEvent,
            menus = menus,
            signals = target.signals,
            primarySignal = target.primarySignal,
            secondarySignals = target.secondarySignals,
            cta = target.cta,
            themeId = themeId,
            selectedMemberIndex = index,
            activeMenu = nextActiveMenu,
            isDetailExpanded = false
        )
        loadMenuTemporalData(current.activeMenu, currentTemporalRange)
    }

    /**
     * Member Intelligence is a single canonical card surface.
     * Selecting a member must never replace it with a second detail interface.
     * Contextual evidence belongs inside the canonical card/menu state.
     */
    fun openMemberDetail(index: Int? = null) {
        if (index != null) selectMember(index)
        isDetailExpanded = false
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = current.copy(isDetailExpanded = false)
    }

    fun closeMemberDetail() {
        isDetailExpanded = false
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = current.copy(isDetailExpanded = false)
    }

    fun selectMenu(menu: MenuType) {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        val target = current.menus.firstOrNull { it.id == menu } ?: return
        if (target.isLocked || !target.isEnabled) return
        _state.value = current.copy(activeMenu = menu)
        loadMenuTemporalData(menu, current.temporalRange)
    }

    fun updateTemporalRange(range: TemporalRange) {
        currentTemporalRange = range
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = current.copy(temporalRange = range)
        loadMenuTemporalData(current.activeMenu, range)
    }

    private fun resolveInitialMenu(
        snapshot: MemberSnapshot,
        currentEvent: MemberEvent,
        signals: List<IntelligenceSignal>,
        menus: List<MemberMenu>
    ): MenuType {
        val visible = menus.filter { it.isVisible && it.isEnabled }.map { it.id }.toSet()
        val urgentSource = signals.firstOrNull {
            it.priority == SignalPriority.P0_CRITICAL || it.priority == SignalPriority.P1_ACTION_REQUIRED
        }?.sourceMenu
        if (urgentSource != null && urgentSource in visible) return urgentSource

        return when (currentEvent.eventType) {
            EventType.PAYMENT_FAILED,
            EventType.PAYMENT_DUE,
            EventType.PAYMENT_OVERDUE,
            EventType.PAYMENT_PARTIAL -> MenuType.PAYMENT
            EventType.MEMBERSHIP_EXPIRED,
            EventType.MEMBERSHIP_CANCELLED -> MenuType.PLAN
            EventType.TRAINER_SESSION_SCHEDULED,
            EventType.TRAINER_SESSION_STARTED,
            EventType.TRAINER_SESSION_MISSED,
            EventType.TRAINER_SESSION_CANCELLED -> MenuType.TRAINER
            EventType.WORKOUT_STARTED,
            EventType.WORKOUT_COMPLETED,
            EventType.WORKOUT_SKIPPED,
            EventType.PR_ACHIEVED -> MenuType.WORKOUT
            EventType.SERVICE_ISSUE -> MenuType.SERVICES
            EventType.COMPLAINT,
            EventType.INCIDENT_REPORTED,
            EventType.MACHINE_FAULT -> MenuType.INSIGHT
            else -> MenuType.HOME
        }.takeIf { it in visible } ?: MenuType.HOME
    }

    fun loadMenuTemporalData(menu: MenuType, range: TemporalRange = currentTemporalRange) {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        val memberId = current.snapshot.id

        viewModelScope.launch {
            _state.value = current.copy(isTemporalLoading = true)

            when (menu) {
                MenuType.ATTENDANCE, MenuType.HOME -> {
                    val summaryRes = temporalRepository.getAttendanceSummary(memberId, range)
                    val eventsRes = temporalRepository.getAttendanceEvents(memberId, range)
                    val updated = _state.value as? MemberIntelligenceUiState.Success ?: return@launch
                    _state.value = updated.copy(
                        attendanceSummary = summaryRes.getOrNull(),
                        attendanceEvents = eventsRes.getOrNull()?.items.orEmpty(),
                        isFromCache = eventsRes.getOrNull()?.isFromCache ?: false,
                        isTemporalLoading = false
                    )
                }
                MenuType.PAYMENT -> {
                    val summaryRes = temporalRepository.getPaymentSummary(memberId, range)
                    val eventsRes = temporalRepository.getPaymentEvents(memberId, range)
                    val updated = _state.value as? MemberIntelligenceUiState.Success ?: return@launch
                    _state.value = updated.copy(
                        paymentSummary = summaryRes.getOrNull(),
                        paymentEvents = eventsRes.getOrNull()?.items.orEmpty(),
                        isFromCache = eventsRes.getOrNull()?.isFromCache ?: false,
                        isTemporalLoading = false
                    )
                }
                MenuType.WORKOUT -> {
                    val summaryRes = temporalRepository.getWorkoutSummary(memberId, range)
                    val eventsRes = temporalRepository.getWorkoutEvents(memberId, range)
                    val updated = _state.value as? MemberIntelligenceUiState.Success ?: return@launch
                    _state.value = updated.copy(
                        workoutSummary = summaryRes.getOrNull(),
                        workoutEvents = eventsRes.getOrNull()?.items.orEmpty(),
                        isFromCache = eventsRes.getOrNull()?.isFromCache ?: false,
                        isTemporalLoading = false
                    )
                }
                MenuType.MORE, MenuType.PLAN, MenuType.TRAINER, MenuType.SERVICES, MenuType.NUTRITION, MenuType.SUPPLEMENTS -> {
                    val summaryRes = temporalRepository.getHistorySummary(memberId, range)
                    val eventsRes = temporalRepository.getHistoryEvents(memberId, current.historyFilter, range)
                    val updated = _state.value as? MemberIntelligenceUiState.Success ?: return@launch
                    _state.value = updated.copy(
                        historySummary = summaryRes.getOrNull(),
                        historyEvents = eventsRes.getOrNull()?.items.orEmpty(),
                        isFromCache = eventsRes.getOrNull()?.isFromCache ?: false,
                        isTemporalLoading = false
                    )
                }
                else -> {
                    val updated = _state.value as? MemberIntelligenceUiState.Success ?: return@launch
                    _state.value = updated.copy(isTemporalLoading = false)
                }
            }
        }
    }

    fun openEventDetail(event: TemporalEventRecord) {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = current.copy(selectedEventDetail = event)
    }

    fun closeEventDetail() {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = current.copy(selectedEventDetail = null)
    }

    fun setHistoryFilter(filter: EventType?) {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = current.copy(historyFilter = filter)
        loadMenuTemporalData(current.activeMenu, current.temporalRange)
    }

    fun selectTheme(theme: ThemeId) {
        themeId = theme
        val current = _state.value as? MemberIntelligenceUiState.Success
        if (current == null) return
        _state.value = current.copy(themeId = theme)
    }

    fun openQaGallery() {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = current.copy(isQaGalleryOpen = true)
    }

    fun closeQaGallery() {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = current.copy(isQaGalleryOpen = false)
    }

    fun toggleQaGallery(open: Boolean) {
        val current = _state.value as? MemberIntelligenceUiState.Success ?: return
        _state.value = current.copy(isQaGalleryOpen = open)
    }

    fun loadFixture(fixture: FixtureCardItem) {
        val current = _state.value as? MemberIntelligenceUiState.Success
        val now = System.currentTimeMillis()
        val eval = engine.evaluate(fixture.snapshot, fixture.event, now)
        val cardTheme = themeForMember(fixture.snapshot.id, fixture.snapshot.identity.code)
        val cardItem = MemberCardItem(
            snapshot = fixture.snapshot,
            currentEvent = fixture.event,
            themeId = cardTheme,
            signals = eval.signals,
            primarySignal = eval.primary,
            secondarySignals = eval.secondary,
            cta = eval.cta
        )
        val menus = MenuAvailabilityResolver.resolve(fixture.snapshot, eval.signals)
        val existingMembers = current?.members?.toMutableList() ?: mutableListOf()
        val existingIndex = existingMembers.indexOfFirst { it.snapshot.id == fixture.snapshot.id }
        val targetIndex = if (existingIndex >= 0) {
            existingMembers[existingIndex] = cardItem
            existingIndex
        } else {
            existingMembers.add(0, cardItem)
            0
        }

        themeId = cardTheme
        _state.value = (current ?: MemberIntelligenceUiState.Success(
            snapshot = fixture.snapshot,
            currentEvent = fixture.event,
            menus = menus,
            signals = eval.signals,
            primarySignal = eval.primary,
            secondarySignals = eval.secondary,
            cta = eval.cta,
            activeMenu = MenuType.HOME,
            themeId = cardTheme,
            members = existingMembers,
            selectedMemberIndex = targetIndex
        )).copy(
            snapshot = fixture.snapshot,
            currentEvent = fixture.event,
            menus = menus,
            signals = eval.signals,
            primarySignal = eval.primary,
            secondarySignals = eval.secondary,
            cta = eval.cta,
            themeId = cardTheme,
            members = existingMembers,
            selectedMemberIndex = targetIndex,
            isQaGalleryOpen = false,
            activeFixtureId = fixture.id
        )
    }

    fun loadFixtureById(fixtureId: String) {
        val fixture = MemberIntelligenceFixtureUniverse.getById(fixtureId) ?: return
        loadFixture(fixture)
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
        fun provideFactory(
            repository: MemberIntelligenceRepository = StubMemberRepositoryImpl(),
            temporalRepository: TemporalIntelligenceRepository = StubTemporalIntelligenceRepositoryImpl()
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MemberIntelligenceViewModel(repository, temporalRepository) as T
                }
            }
    }
}
