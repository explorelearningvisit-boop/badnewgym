package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.compose.ui.graphics.Color
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceWindow
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyGoal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricHistory
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutMonth
import java.text.SimpleDateFormat
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

/**
 * Hero surface for the check-in card.
 *
 * The hero always wears the electric-blue identity (the reference's hero is
 * blue, not plan-tinted); plan identity lives in the pills and badges.
 */
fun heroTheme(): MembershipTheme = MembershipTheme(
    surfaceTop = Color(0xFF0E5CC8),
    surfaceBottom = Color(0xFF08306E),
    accent = Color(0xFF00D4FF),
    accentText = Color(0xFFFFFFFF),
    pillBg = Color(0xFF00C98A)
)

fun themeForPlan(planName: String?): MembershipTheme {
    val p = (planName ?: "").lowercase(Locale.ENGLISH)
    return when {
        "gold" in p -> MembershipTheme(
            surfaceTop = Color(0xFF5A4517),
            surfaceBottom = Color(0xFF241C08),
            accent = Color(0xFFE8B93C),
            accentText = Color(0xFFFFE9B8),
            pillBg = Color(0xFF00C98A)
        )
        "silver" in p -> MembershipTheme(
            surfaceTop = Color(0xFF3D5A78),
            surfaceBottom = Color(0xFF16222E),
            accent = Color(0xFF9FC3E8),
            accentText = Color(0xFFD6E9FA),
            pillBg = Color(0xFF00C98A)
        )
        "diamond" in p -> MembershipTheme(
            surfaceTop = Color(0xFF1F6A9E),
            surfaceBottom = Color(0xFF0A2A40),
            accent = Color(0xFF5CC8FF),
            accentText = Color(0xFFD2F0FF),
            pillBg = Color(0xFF00C98A)
        )
        "platinum" in p -> MembershipTheme(
            surfaceTop = Color(0xFF4A4390),
            surfaceBottom = Color(0xFF1B1840),
            accent = Color(0xFFA99BFF),
            accentText = Color(0xFFE4E0FF),
            pillBg = Color(0xFF00C98A)
        )
        else -> MembershipTheme(
            surfaceTop = Color(0xFF0B5A40),
            surfaceBottom = Color(0xFF062A1E),
            accent = Color(0xFF00C98A),
            accentText = Color(0xFFC8F5E2),
            pillBg = Color(0xFF00C98A)
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
    val m = (ms / 60_000).coerceAtLeast(0)
    return "%02dh %02dm".format(m / 60, m % 60)
}

/** Workout history ranges. THIS_MONTH shows the selected calendar month. */
enum class WorkoutRange(val days: Int, val short: String) {
    THIS_MONTH(-1, "Month"),
    D7(7, "7D"),
    D14(14, "14D"),
    D15(15, "15D"),
    D30(30, "30D");

    val isMonth: Boolean get() = this == THIS_MONTH
}

/** "Today's Visit #1" - only when the count is real. */
fun visitOrdinal(n: Int): String = "Today's Visit #$n"

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
