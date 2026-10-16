package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.compose.ui.graphics.Color
import com.example.badnewgym.feature.memberintelligence.domain.analytics.WorkoutAnalytics
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceWindow
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyGoal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricHistory
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutMonth
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.util.Date
import java.util.Locale

/**
 * BAD GYM Member Intelligence Home - single cross-domain summary.
 *
 * Home is NOW + MEMBER + MONEY + ATTENDANCE + WORKOUT + BODY.
 * Every field below is either a real record from [MemberSnapshot] /
 * MemberIntelligenceData or null. The UI renders honest empty states for null
 * and never invents values.
 */

// ------------------------------------------------------------------ widgets

/** Stable widget ids. Order here is the default Home order (spec 6). */
enum class HomeWidgetId(val priority: Int) {
    CHECK_IN(0),
    IDENTITY(1),
    PAYMENT(2),
    ATTENDANCE(3),
    WORKOUT(4),
    BODY(5),
    SUPPLEMENTS(6),
    SERVICES(7),
    HISTORY(8),
    INSIGHTS(9),
    MORE(10),
}

/**
 * Future admin drag/drop surface (spec 44). The renderer only reads
 * [visibleOrdered]; an admin console can later persist enable/order/resize
 * without touching widget code.
 */
data class HomeWidgetDef(
    val id: HomeWidgetId,
    val enabled: Boolean = true,
    val order: Int = id.priority
)

fun defaultWidgetDefs(): List<HomeWidgetDef> =
    HomeWidgetId.entries.map { HomeWidgetDef(it) }

fun visibleOrdered(defs: List<HomeWidgetDef>): List<HomeWidgetId> =
    defs.filter { it.enabled }.sortedWith(compareBy({ it.order }, { it.id.priority })).map { it.id }

// ------------------------------------------------------------------ check-in

data class HomeCheckIn(
    /** Null when nobody is checked in right now - Home shows CHECK-OUT state. */
    val liveStartMs: Long?,
    val methodLabel: String?,
    val visitNumberToday: Int?,
    val lastStartMs: Long?,
    val lastEndMs: Long?,
    val lastDurationMs: Long?
)

// ------------------------------------------------------------------ home data

data class HomeData(
    val snapshot: MemberSnapshot,
    val checkIn: HomeCheckIn,
    val attendance: AttendanceWindow?,
    val attendanceMonth: com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceMonth?,
    /** Previous-period comparison in percentage points; null when invalid. */
    val attendancePrevPoints: Int?,
    val workout: WorkoutMonth?,
    val bodyGoals: List<BodyGoal>,
    val bodyHistories: List<MetricHistory>,
    val paymentSituation: PaymentSituation,
    val widgetDefs: List<HomeWidgetDef> = defaultWidgetDefs()
)

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Ready(val data: HomeData) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

// ------------------------------------------------------------------ membership theme

/**
 * The plan owns the visual theme (spec 11). No separate Gold badge widget -
 * the Check-In card itself wears the member's metal.
 */
data class MembershipTheme(
    val surfaceTop: Color,
    val surfaceBottom: Color,
    val accent: Color,
    val accentText: Color,
    val pillBg: Color
)

fun themeForPlan(planName: String?): MembershipTheme {
    val p = (planName ?: "").lowercase(Locale.ENGLISH)
    return when {
        "gold" in p -> MembershipTheme(
            surfaceTop = Color(0xFFFFF8E1),
            surfaceBottom = Color(0xFFF3D27A),
            accent = Color(0xFF8A6D1B),
            accentText = Color(0xFF6B5410),
            pillBg = Color(0xFF2FA36B)
        )
        "silver" in p -> MembershipTheme(
            surfaceTop = Color(0xFFF4F8FB),
            surfaceBottom = Color(0xFFCFE0EA),
            accent = Color(0xFF4A6B8A),
            accentText = Color(0xFF38536B),
            pillBg = Color(0xFF2FA36B)
        )
        "diamond" in p -> MembershipTheme(
            surfaceTop = Color(0xFFEFF8FF),
            surfaceBottom = Color(0xFFBFE3F5),
            accent = Color(0xFF2B7CB0),
            accentText = Color(0xFF1F5E87),
            pillBg = Color(0xFF2FA36B)
        )
        "platinum" in p -> MembershipTheme(
            surfaceTop = Color(0xFFF4F1FA),
            surfaceBottom = Color(0xFFD5CBEA),
            accent = Color(0xFF5F54A8),
            accentText = Color(0xFF4A4285),
            pillBg = Color(0xFF2FA36B)
        )
        else -> MembershipTheme(
            surfaceTop = Color(0xFFEAF7F0),
            surfaceBottom = Color(0xFFBFE6D2),
            accent = Color(0xFF0E7A4D),
            accentText = Color(0xFF0B5F3D),
            pillBg = Color(0xFF2FA36B)
        )
    }
}

// ------------------------------------------------------------------ formatting (UI-side, locale-stable)

private val dateFmt = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
private val timeFmt = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
private val dayFmt = SimpleDateFormat("EEE, dd MMM", Locale.ENGLISH)

fun fmtDate(ms: Long?): String = if (ms == null || ms <= 0) "-" else dateFmt.format(Date(ms))
fun fmtTime(ms: Long?): String = if (ms == null || ms <= 0) "-" else timeFmt.format(Date(ms))
fun fmtDay(ms: Long?): String = if (ms == null || ms <= 0) "-" else dayFmt.format(Date(ms))

fun fmtRupees(amount: Double?): String {
    if (amount == null) return "-"
    val v = kotlin.math.round(amount).toLong()
    return "\u20B9" + String.format(Locale.ENGLISH, "%,d", v)
}

fun fmtDurationHms(ms: Long): String {
    val s = (ms / 1000).coerceAtLeast(0)
    return "%02d:%02d:%02d".format(s / 3600, (s % 3600) / 60, s % 60)
}

fun fmtDurationHm(ms: Long): String {
    // A sub-minute visit truncated to "00h 00m", which reads as "no duration"
    // even though time was genuinely recorded.
    if (ms in 1 until 60_000L) return "under 1 min"
    val m = (ms / 60_000).coerceAtLeast(0)
    return "%02dh %02dm".format(m / 60, m % 60)
}

/**
 * What a single day in the workout chart means.
 *
 * The approved reference separates a recorded session from a day with nothing
 * recorded. Collapsing "no record" into "rest day" would assert the member was
 * resting, which the records do not say, so future and untracked days are
 * reported as NO_RECORD.
 */
enum class WorkoutDayKind {
    /** A session was recorded on this day. */
    WORKOUT,
    /** The day has happened and is inside the tracked period, with no session. */
    REST_DAY,
    /** The day has not happened yet, so nothing can be said about it. */
    NO_RECORD
}

fun workoutDayKind(
    dayOfMonth: Int,
    minutes: Int,
    anchor: LocalDate,
    yearMonth: YearMonth
): WorkoutDayKind = when {
    minutes > 0 -> WorkoutDayKind.WORKOUT
    yearMonth == YearMonth.from(anchor) && dayOfMonth > anchor.dayOfMonth -> WorkoutDayKind.NO_RECORD
    yearMonth.isAfter(YearMonth.from(anchor)) -> WorkoutDayKind.NO_RECORD
    else -> WorkoutDayKind.REST_DAY
}

/** Counts of each day kind across a month. They must sum to the month length. */
data class WorkoutDayChips(val workout: Int, val rest: Int, val noRecord: Int) {
    val total: Int get() = workout + rest + noRecord
}

fun workoutDayChips(
    days: List<WorkoutAnalytics.WorkoutDay>,
    anchor: LocalDate,
    yearMonth: YearMonth
): WorkoutDayChips {
    var w = 0
    var r = 0
    var n = 0
    days.forEach { d ->
        when (workoutDayKind(d.dayOfMonth, d.minutes, anchor, yearMonth)) {
            WorkoutDayKind.WORKOUT -> w++
            WorkoutDayKind.REST_DAY -> r++
            WorkoutDayKind.NO_RECORD -> n++
        }
    }
    return WorkoutDayChips(w, r, n)
}

/**
 * Readable duration for chart axes and stat rows.
 *
 * Long sessions read as hours with one decimal, matching the reference's
 * "2.0 hr"; anything under an hour stays in whole minutes so a 20-minute
 * session never becomes "0.3 hr".
 */
fun fmtWorkoutDuration(min: Int): String =
    if (min >= 60) String.format(Locale.US, "%.1f hr", min / 60.0) else "$min min"

/**
 * The event the screen is currently asking the owner about.
 *
 * The approved reference replaces the usual product wordmark with the live
 * event, so this is derived purely from recorded state. Nothing here invents a
 * visit: [CHECK_OUT] appears only while a session is genuinely open.
 */
enum class HomeEventType(
    val label: String,
    val activeSubtitle: String,
    val idleSubtitle: String
) {
    CHECK_IN("Check-In", "Tap to end this visit", "Tap to check-in this member"),
    CHECK_OUT("Check-Out", "Visit in progress", "Tap to check-in this member")
}

/** A concrete event header, resolved from real check-in state. */
data class HomeEventHeader(
    val type: HomeEventType,
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

/**
 * Resolves the event header.
 *
 * [context] already distinguishes a live visit from a completed or absent one,
 * so the header reuses it instead of re-deriving live status independently.
 */
fun homeEventHeader(
    context: MemberContext?,
    icon: androidx.compose.ui.graphics.vector.ImageVector
): HomeEventHeader {
    val live = context == MemberContext.JUST_CHECKED_IN
    val type = if (live) HomeEventType.CHECK_OUT else HomeEventType.CHECK_IN
    return HomeEventHeader(
        type = type,
        title = type.label,
        subtitle = if (live) type.activeSubtitle else type.idleSubtitle,
        icon = icon
    )
}

/** Workout history ranges. THIS_MONTH shows the selected calendar month. */
/** [short] is the dense chip form; [label] is the dropdown/menu form. */
enum class WorkoutRange(val days: Int, val short: String, val label: String) {
    THIS_MONTH(-1, "Month", "This Month"),
    D7(7, "7D", "Last 7 Days"),
    D14(14, "14D", "Last 14 Days"),
    D15(15, "15D", "Last 15 Days"),
    D30(30, "30D", "Last 30 Days");

    val isMonth: Boolean get() = this == THIS_MONTH

    companion object {
        /** Ranges the Home dashboard offers, in menu order. */
        val dashboardOptions: List<WorkoutRange> = listOf(THIS_MONTH, D7, D15, D30)

        /** Parses a stored range id, defaulting to the full month. */
        fun fromId(id: String?): WorkoutRange =
            entries.firstOrNull { it.name == id } ?: THIS_MONTH
    }
}

/** "Today's Visit #1" - only when the count is real. */
fun visitOrdinal(n: Int): String = "Today's Visit #$n"

/**
 * Verified member context for the identity strip (master prompt 4B).
 *
 * It is derived purely from recorded visit timestamps. The LIVE state is
 * deliberately absent: the session hero already shows it prominently, so
 * returning null for an older live session keeps the strip non-duplicative.
 */
enum class MemberContext { JUST_CHECKED_IN, COMPLETED_VISIT, NO_ACTIVE_VISIT }

/** A check-in this recent reads as "just checked in" rather than "in session". */
const val JUST_CHECKED_IN_MS: Long = 30 * 60 * 1000L

fun memberContext(checkIn: HomeCheckIn, todayStartMs: Long, nowMs: Long): MemberContext? {
    val live = checkIn.liveStartMs
    if (live != null) {
        return if (nowMs - live <= JUST_CHECKED_IN_MS) MemberContext.JUST_CHECKED_IN else null
    }
    val startedToday = checkIn.lastStartMs?.let { it >= todayStartMs } == true
    return if (startedToday) MemberContext.COMPLETED_VISIT else MemberContext.NO_ACTIVE_VISIT
}

/** Display label for a configured payment method. */
fun paymentMethodLabel(method: com.example.badnewgym.feature.memberintelligence.domain.model.PaymentMethod): String =
    when (method) {
        com.example.badnewgym.feature.memberintelligence.domain.model.PaymentMethod.CASH -> "Cash"
        com.example.badnewgym.feature.memberintelligence.domain.model.PaymentMethod.UPI -> "UPI"
        com.example.badnewgym.feature.memberintelligence.domain.model.PaymentMethod.CARD -> "Card"
        com.example.badnewgym.feature.memberintelligence.domain.model.PaymentMethod.BANK_TRANSFER -> "Bank Transfer"
    }

/**
 * Pure collection guard so the rule is unit-testable and the ViewModel just
 * enforces it. Returns an error message, or null when collection may proceed.
 */
fun validateCollection(amount: Double, outstanding: Double): String? = when {
    outstanding <= 0 -> "Nothing outstanding on this account."
    amount <= 0 -> "Enter an amount greater than zero."
    amount > outstanding + 0.009 -> "Amount exceeds the outstanding ${fmtRupees(outstanding)}."
    else -> null
}

/**
 * Real measurement window across all histories, e.g. "Aug 1 – Oct 1".
 * Null when there is genuinely nothing to range over.
 */
fun bodyRangeLabel(histories: List<com.example.badnewgym.feature.memberintelligence.domain.model.MetricHistory>): String? {
    val days = histories.flatMap { it.readings }.map { it.epochDay }.distinct().sorted()
    if (days.size < 2) return null
    val fmt = SimpleDateFormat("MMM d", Locale.ENGLISH)
    val zone = java.time.ZoneId.systemDefault()
    fun day(ms: Long): String = fmt.format(Date(ms))
    val first = day(days.first() * 86_400_000L)
    val last = day(days.last() * 86_400_000L)
    return "$first – $last"
}

/** Human label for the actual check-in mechanism. Never invents a method. */
fun checkInMethodLabel(raw: String?): String? {
    if (raw.isNullOrBlank() || raw.equals("system", ignoreCase = true)) return null
    return raw.split('_', ' ', '-')
        .filter { it.isNotBlank() }
        .joinToString(" ") { w -> w.lowercase(Locale.ENGLISH).replaceFirstChar { it.uppercase() } }
}
