package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.badnewgym.feature.memberintelligence.data.repository.MemberIntelligenceV4DataSource
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.domain.model.EventSource
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

/**
 * Home ViewModel. Loads the real snapshot + operational datasets once and
 * derives every widget summary from records. Fixtures live in the data layer
 * (V4DataSource / MemberScenarios) - never as literals in UI code.
 */
class HomeViewModel(
    private val repository: StubMemberRepositoryImpl = StubMemberRepositoryImpl(),
    private val dataSource: MemberIntelligenceV4DataSource = MemberIntelligenceV4DataSource()
) : ViewModel() {

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state

    private val _workoutWeek = MutableStateFlow<Int>(Int.MAX_VALUE)
    val workoutWeek: StateFlow<Int> = _workoutWeek

    /** Which module sheet is open, null when Home shows the summary only. */
    private val _openModule = MutableStateFlow<HomeWidgetId?>(null)
    val openModule: StateFlow<HomeWidgetId?> = _openModule

    fun openModule(id: HomeWidgetId) { _openModule.value = id }
    fun closeModule() { _openModule.value = null }

    fun setWorkoutWeek(index: Int) { _workoutWeek.value = index }

    fun load(memberId: String = DEFAULT_MEMBER_ID) {
        viewModelScope.launch {
            _state.value = HomeUiState.Loading
            val result = repository.getMemberSnapshot(memberId)
            val snapshot = result.getOrNull()
            if (snapshot == null) {
                _state.value = HomeUiState.Error("Member not found")
                return@launch
            }
            val anchor = LocalDate.now()
            val attendance = try { dataSource.attendanceWindow(anchor) } catch (t: Throwable) { null }
            val workout = try { dataSource.workoutMonth(anchor) } catch (t: Throwable) { null }
            val goals = try { dataSource.bodyGoals(anchor) } catch (t: Throwable) { emptyList() }
            val histories = try { dataSource.metricHistories(goals, anchor) } catch (t: Throwable) { emptyList() }
            val payment = snapshot.payment
            val situation = dataSource.paymentSituation(
                outstanding = payment?.totalOutstanding ?: 0.0,
                overdueDays = payment?.overdueDays ?: 0,
                dueAtEpoch = payment?.dueDate
            )
            _state.value = HomeUiState.Ready(
                HomeData(
                    snapshot = snapshot,
                    checkIn = resolveCheckIn(snapshot.recentEvents),
                    attendance = attendance,
                    workout = workout,
                    bodyGoals = goals,
                    bodyHistories = histories,
                    paymentSituation = situation
                )
            )
        }
    }

    /** Ends the live session. The card flips to the CHECK-OUT completed state. */
    fun endSession() {
        val current = (_state.value as? HomeUiState.Ready)?.data ?: return
        val checkIn = current.checkIn
        val start = checkIn.liveStartMs ?: return
        val now = System.currentTimeMillis()
        _state.value = HomeUiState.Ready(
            current.copy(
                checkIn = checkIn.copy(
                    liveStartMs = null,
                    lastStartMs = start,
                    lastEndMs = now,
                    lastDurationMs = now - start
                )
            )
        )
    }

    // ------------------------------------------------------------ internals

    private fun resolveCheckIn(events: List<MemberEvent>): HomeCheckIn {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        fun isToday(ms: Long): Boolean =
            LocalDate.ofInstant(java.time.Instant.ofEpochMilli(ms), zone) == today

        val todaysCheckIns = events
            .filter { it.eventType == EventType.CHECK_IN && isToday(it.occurredAt) }
            .sortedBy { it.occurredAt }
        val todaysCheckOuts = events
            .filter { it.eventType == EventType.CHECK_OUT && isToday(it.occurredAt) }
            .sortedBy { it.occurredAt }

        val lastIn = todaysCheckIns.lastOrNull()
        val lastOut = todaysCheckOuts.lastOrNull()
        val live = lastIn != null && (lastOut == null || lastIn.occurredAt > lastOut.occurredAt)

        if (live && lastIn != null) {
            return HomeCheckIn(
                liveStartMs = lastIn.occurredAt,
                methodLabel = methodOf(lastIn),
                visitNumberToday = todaysCheckIns.size.takeIf { it > 0 },
                lastStartMs = lastIn.occurredAt,
                lastEndMs = null,
                lastDurationMs = null
            )
        }
        // Honest checked-out state: last real session if one exists, else idle.
        val lastSessionStart = todaysCheckIns.lastOrNull()?.occurredAt
            ?: events.filter { it.eventType == EventType.CHECK_IN }.maxOfOrNull { it.occurredAt }
        val lastSessionEnd = todaysCheckOuts.lastOrNull()?.occurredAt
        return HomeCheckIn(
            liveStartMs = null,
            methodLabel = null,
            visitNumberToday = todaysCheckIns.size.takeIf { it > 0 },
            lastStartMs = lastSessionStart,
            lastEndMs = lastSessionEnd,
            lastDurationMs = if (lastSessionStart != null && lastSessionEnd != null && lastSessionEnd > lastSessionStart)
                lastSessionEnd - lastSessionStart else null
        )
    }

    private fun methodOf(event: MemberEvent): String? {
        // Explicit metadata wins, then the real event source. Gate/system/import
        // entries describe where the event was recorded, not the mechanism the
        // member used, so they stay hidden rather than becoming a fake method.
        event.metadata["method"]?.takeIf { it.isNotBlank() }?.let { return checkInMethodLabel(it) }
        event.metadata["checkInMethod"]?.takeIf { it.isNotBlank() }?.let { return checkInMethodLabel(it) }
        return when (event.source) {
            EventSource.STAFF -> "Staff"
            EventSource.MEMBER -> "Member App"
            EventSource.TRAINER -> "Trainer"
            else -> null
        }
    }

    companion object {
        const val DEFAULT_MEMBER_ID = "BG305"

        /** Deterministic demo session anchor (today 09:42) used only by previews. */
        fun demoSessionStart(): Long {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 9)
            cal.set(Calendar.MINUTE, 42)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel() as T
    }
}
