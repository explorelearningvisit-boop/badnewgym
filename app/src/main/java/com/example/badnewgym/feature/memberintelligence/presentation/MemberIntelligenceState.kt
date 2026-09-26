package com.example.badnewgym.feature.memberintelligence.presentation

import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*

/**
 * Representation of a single member item in the browse carousel.
 */
data class MemberCardItem(
    val snapshot: MemberSnapshot,
    val currentEvent: MemberEvent,
    val themeId: ThemeId = ThemeId.NATURAL_FRESH,
    val signals: List<IntelligenceSignal> = emptyList(),
    val primarySignal: IntelligenceSignal? = null,
    val secondarySignals: List<IntelligenceSignal> = emptyList(),
    val cta: SignalAction? = null
)

sealed class MemberIntelligenceUiState {
    data object Loading : MemberIntelligenceUiState()

    data class Success(
        val snapshot: MemberSnapshot,
        val currentEvent: MemberEvent,
        val menus: List<MemberMenu>,
        val signals: List<IntelligenceSignal>,
        val primarySignal: IntelligenceSignal?,
        val secondarySignals: List<IntelligenceSignal>,
        val cta: SignalAction?,
        val activeMenu: MenuType = MenuType.HOME,
        val themeId: ThemeId = ThemeId.NATURAL_FRESH,
        val syncStatus: SyncStatus = SyncStatus.SYNCED,
        val members: List<MemberCardItem> = emptyList(),
        val selectedMemberIndex: Int = 0,
        val isDetailExpanded: Boolean = false,
        // Stage 7.4 Temporal State
        val temporalRange: TemporalRange = TemporalRange.forCurrentMonth(),
        val selectedEventDetail: TemporalEventRecord? = null,
        val attendanceSummary: AttendanceTemporalSummary? = null,
        val attendanceEvents: List<TemporalEventRecord> = emptyList(),
        val paymentSummary: PaymentTemporalSummary? = null,
        val paymentEvents: List<TemporalEventRecord> = emptyList(),
        val workoutSummary: WorkoutTemporalSummary? = null,
        val workoutEvents: List<TemporalEventRecord> = emptyList(),
        val historySummary: HistoryTemporalSummary? = null,
        val historyEvents: List<TemporalEventRecord> = emptyList(),
        val historyFilter: EventType? = null,
        val isTemporalLoading: Boolean = false,
        val isFromCache: Boolean = false,
        // Stage 7.9 QA Fixture Universe
        val isQaGalleryOpen: Boolean = false,
        val activeFixtureId: String? = null
    ) : MemberIntelligenceUiState()

    data class Error(val message: String) : MemberIntelligenceUiState()
    data class Locked(val featureName: String, val planHint: String) : MemberIntelligenceUiState()
}

@Deprecated("Use MemberIntelligenceUiState")
typealias MemberIntelligenceState = MemberIntelligenceUiState
