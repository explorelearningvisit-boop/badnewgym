package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.badnewgym.feature.memberintelligence.data.repository.MemberIntelligenceV4DataSource
import com.example.badnewgym.feature.memberintelligence.data.repository.StubMemberRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.domain.model.EventSource
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentMethod
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentReceipt
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTransaction
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

    private val _workoutRange = MutableStateFlow(WorkoutRange.THIS_MONTH)
    val workoutRange: StateFlow<WorkoutRange> = _workoutRange

    /** Selected attendance month. Defaults to the current month. */
    private val _attendanceMonth = MutableStateFlow(java.time.YearMonth.now())
    val attendanceMonth: StateFlow<java.time.YearMonth> = _attendanceMonth

    /** Selected workout month. Defaults to the current month. */
    private val _workoutMonth = MutableStateFlow(java.time.YearMonth.now())
    val workoutMonth: StateFlow<java.time.YearMonth> = _workoutMonth

    /** Honest unavailable-feature notice (no dead buttons, no fake screens). */
    private val _notice = MutableStateFlow<String?>(null)
    val notice: StateFlow<String?> = _notice

    /** Which module sheet is open, null when Home shows the summary only. */
    private val _openModule = MutableStateFlow<HomeWidgetId?>(null)
    val openModule: StateFlow<HomeWidgetId?> = _openModule

    /** Last recorded receipt this session. Drives the SUCCESSFUL card. */
    private val _lastReceipt = MutableStateFlow<PaymentReceipt?>(null)
    val lastReceipt: StateFlow<PaymentReceipt?> = _lastReceipt

    private val _paymentBusy = MutableStateFlow(false)
    val paymentBusy: StateFlow<Boolean> = _paymentBusy

    private val _paymentError = MutableStateFlow<String?>(null)
    val paymentError: StateFlow<String?> = _paymentError

    fun openModule(id: HomeWidgetId) { _openModule.value = id }
    fun closeModule() { _openModule.value = null }

    fun setWorkoutWeek(index: Int) { _workoutWeek.value = index }
    fun setWorkoutRange(range: WorkoutRange) { _workoutRange.value = range }
    fun clearPaymentError() { _paymentError.value = null }

    fun showNotice(message: String) { _notice.value = message }
    fun clearNotice() { _notice.value = null }

    fun setAttendanceMonth(month: java.time.YearMonth) {
        _attendanceMonth.value = month
        refreshMonth()
    }

    fun setWorkoutMonth(month: java.time.YearMonth) {
        _workoutMonth.value = month
    }

    fun load(memberId: String = DEFAULT_MEMBER_ID) {
        viewModelScope.launch {
            _state.value = HomeUiState.Loading
            _lastReceipt.value = null
            _paymentError.value = null
            val result = repository.getMemberSnapshot(memberId)
            val snapshot = result.getOrNull()
            if (snapshot == null) {
                _state.value = HomeUiState.Error("Member not found")
                return@launch
            }
            val anchor = LocalDate.now()
            monthAnchor = anchor
            monthPaymentDays = snapshot.payment?.history?.map { it.occurredAt / 86_400_000L }?.toSet()
                ?: emptySet()
            _attendanceMonth.value = java.time.YearMonth.from(anchor)
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
            val month = attendanceMonthNow()
            _state.value = HomeUiState.Ready(
                HomeData(
                    snapshot = snapshot,
                    checkIn = resolveCheckIn(snapshot.recentEvents),
                    attendance = attendance,
                    attendanceMonth = month.first,
                    attendancePrevPoints = month.second,
                    workout = workout,
                    bodyGoals = goals,
                    bodyHistories = histories,
                    paymentSituation = situation
                )
            )
        }
    }

    private var monthAnchor: LocalDate = LocalDate.now()
    private var monthPaymentDays: Set<Long> = emptySet()

    /** Selected month plus the previous-period comparison (null when invalid). */
    private fun attendanceMonthNow(): Pair<
        com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceMonth, Int?> {
        val ym = _attendanceMonth.value
        val current = dataSource.attendanceMonth(ym.year, ym.monthValue, monthAnchor, paymentDays = monthPaymentDays)
        val prevYm = ym.minusMonths(1)
        val prev = try {
            dataSource.attendanceMonth(prevYm.year, prevYm.monthValue, monthAnchor, paymentDays = monthPaymentDays)
        } catch (t: Throwable) {
            null
        }
        val delta = com.example.badnewgym.feature.memberintelligence.domain.analytics.compareMonthPoints(
            current, prev, monthAnchor
        )
        return current to delta
    }

    /** Rebuilds the selected month (month navigation) without reloading records. */
    private fun refreshMonth() {
        val current = (_state.value as? HomeUiState.Ready)?.data ?: return
        val month = try {
            attendanceMonthNow()
        } catch (t: Throwable) {
            return
        }
        _state.value = HomeUiState.Ready(
            current.copy(attendanceMonth = month.first, attendancePrevPoints = month.second)
        )
    }

    /**
     * Ends the live session and persists the checkout event.
     *
     * The card only flips to CHECK-OUT after the event is recorded; a failed
     * write surfaces a notice instead of a fake completed state. Duplicate
     * taps are ignored (nothing live to end).
     */
    fun endSession() {
        val current = (_state.value as? HomeUiState.Ready)?.data ?: return
        val checkIn = current.checkIn
        val start = checkIn.liveStartMs ?: return // duplicate-tap guard: nothing live
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val recorded = try {
                repository.recordEvent(
                    MemberEvent(
                        id = "checkout_${start}",
                        memberId = current.snapshot.id,
                        gymId = current.snapshot.gymId,
                        eventType = EventType.CHECK_OUT,
                        occurredAt = now,
                        source = EventSource.GATE,
                        metadata = mapOf("durationMinutes" to ((now - start) / 60_000L).toString()),
                        idempotencyKey = "checkout_${start}"
                    )
                ).isSuccess
            } catch (t: Throwable) {
                false
            }
            if (!recorded) {
                _notice.value = "Could not save checkout. Please retry."
                return@launch
            }
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
    }

    /**
     * Records a collection against the outstanding balance.
     *
     * Real state machine, not a button animation: invalid input is rejected
     * with an error, double submits are ignored while recording, and the Home
     * card only flips to SUCCESSFUL after the snapshot (and receipt) update.
     * A short payment keeps the Partial state with the true remainder.
     */
    fun collectPayment(amount: Double, method: PaymentMethod) {
        if (_paymentBusy.value) return // duplicate-submission guard
        val current = (_state.value as? HomeUiState.Ready)?.data ?: return
        val payment = current.snapshot.payment ?: run {
            _paymentError.value = "No billing account on this member."
            return
        }
        val outstanding = payment.totalOutstanding
        validateCollection(amount, outstanding)?.let {
            _paymentError.value = it
            return
        }
        _paymentBusy.value = true
        _paymentError.value = null
        viewModelScope.launch {
            try {
                val now = System.currentTimeMillis()
                repository.recordEvent(
                    MemberEvent(
                        id = "pay_${now}",
                        memberId = current.snapshot.id,
                        gymId = current.snapshot.gymId,
                        eventType = EventType.PAYMENT,
                        occurredAt = now,
                        source = EventSource.PAYMENT,
                        metadata = mapOf(
                            "amount" to amount.toString(),
                            "method" to paymentMethodLabel(method)
                        ),
                        idempotencyKey = "pay_${now}_$amount"
                    )
                )
                val receipt = PaymentReceipt(
                    receiptId = "RCP-${current.snapshot.identity.code ?: "BG"}-$now",
                    amount = amount,
                    method = method,
                    paidAtEpoch = now
                )
                val remaining = (outstanding - amount).coerceAtLeast(0.0)
                val settled = remaining <= 0.009
                val updated = payment.copy(
                    totalOutstanding = remaining,
                    overdueDays = if (settled) 0 else payment.overdueDays,
                    lastPaymentAmount = amount,
                    lastPaymentDate = now,
                    lastPaymentMethod = paymentMethodLabel(method),
                    lifetimePaid = (payment.lifetimePaid ?: 0.0) + amount,
                    history = payment.history + PaymentTransaction(
                        id = receipt.receiptId,
                        amount = amount,
                        occurredAt = now,
                        method = paymentMethodLabel(method),
                        purpose = "Collection"
                    )
                )
                val situation = when {
                    settled -> com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation.Settled
                    amount < outstanding -> com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation.Partial(
                        received = amount,
                        remaining = remaining,
                        dueAtEpoch = payment.dueDate
                    )
                    else -> dataSource.paymentSituation(remaining, updated.overdueDays, payment.dueDate)
                }
                _state.value = HomeUiState.Ready(
                    current.copy(
                        snapshot = current.snapshot.copy(payment = updated),
                        paymentSituation = situation
                    )
                )
                _lastReceipt.value = receipt
            } catch (t: Throwable) {
                _paymentError.value = "Could not record payment. Please retry."
            } finally {
                _paymentBusy.value = false
            }
        }
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
