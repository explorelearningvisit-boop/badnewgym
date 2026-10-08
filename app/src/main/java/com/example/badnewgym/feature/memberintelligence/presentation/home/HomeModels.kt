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
    val m = (ms / 60_000).coerceAtLeast(0)
    return "%02dh %02dm".format(m / 60, m % 60)
}

/** "1st Visit Today" / "2nd Visit Today" - only when the count is real. */
fun visitOrdinal(n: Int): String = when {
    n % 100 in 11..13 -> "${n}th Visit Today"
    n % 10 == 1 -> "${n}st Visit Today"
    n % 10 == 2 -> "${n}nd Visit Today"
    n % 10 == 3 -> "${n}rd Visit Today"
    else -> "${n}th Visit Today"
}

/** Human label for the actual check-in mechanism. Never invents a method. */
fun checkInMethodLabel(raw: String?): String? {
    if (raw.isNullOrBlank() || raw.equals("system", ignoreCase = true)) return null
    return raw.split('_', ' ', '-')
        .filter { it.isNotBlank() }
        .joinToString(" ") { w -> w.lowercase(Locale.ENGLISH).replaceFirstChar { it.uppercase() } }
}
