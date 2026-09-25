package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot

/**
 * Stage 7.6 — Compact Fusion Strip.
 *
 * Fixed-height information layer that merges the strongest visual ideas from
 * the concept boards without increasing the member-card footprint.
 * All values come from MemberSnapshot; unknown data stays unknown.
 */
@Composable
fun CompactIntelligenceStrip(
    snapshot: MemberSnapshot,
    activeMenu: MenuType,
    onAttendanceClick: () -> Unit = {},
    onTrainerClick: () -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors
    val attendance = snapshot.attendance
    val trainer = snapshot.trainer
    val workout = snapshot.workout
    val payment = snapshot.payment

    val cells: List<Triple<String, String, Color>> = when (activeMenu) {
        MenuType.ATTENDANCE -> listOf(
            Triple("VISITS", attendance?.visits?.toString() ?: "—", colors.accent),
            Triple("TARGET", attendance?.target?.toString() ?: "—", colors.info),
            Triple("AVG/WK", attendance?.avgVisitsPerWeek?.let { "%.1f".format(it) } ?: "—", colors.success),
            Triple("STREAK", attendance?.streakDays?.let { "${it}d" } ?: "—", colors.warning)
        )
        MenuType.PLAN -> listOf(
            Triple("DAYS", snapshot.membership?.daysRemaining?.toString() ?: "—", colors.accent),
            Triple("STATUS", if (snapshot.membership?.isActive == true) "ACTIVE" else if (snapshot.membership != null) "EXPIRED" else "—", if (snapshot.membership?.isActive == true) colors.success else colors.danger),
            Triple("RENEWALS", snapshot.membership?.renewalCount?.toString() ?: "—", colors.info),
            Triple("TIER", snapshot.identity.tier.name, colors.warning)
        )
        MenuType.PAYMENT -> listOf(
            Triple("DUE", payment?.totalOutstanding?.let(::formatCompactMoney) ?: "—", if ((payment?.totalOutstanding ?: 0.0) > 0) colors.danger else colors.success),
            Triple("PAID", payment?.lifetimePaid?.let(::formatCompactMoney) ?: "—", colors.success),
            Triple("OVERDUE", payment?.overdueDays?.let { "${it}d" } ?: "—", colors.warning),
            Triple("METHOD", payment?.lastPaymentMethod ?: "—", colors.info)
        )
        MenuType.TRAINER -> listOf(
            Triple("PT LEFT", trainer?.let { (it.sessionsTotal - it.sessionsUsed).coerceAtLeast(0).toString() } ?: "—", colors.accent),
            Triple("NEXT", trainer?.nextSessionDate?.let(::formatShortDate) ?: "—", colors.info),
            Triple("FOCUS", trainer?.focus ?: "—", colors.success),
            Triple("RATING", trainer?.rating?.toString() ?: "—", colors.warning)
        )
        MenuType.WORKOUT -> listOf(
            Triple("LAST", workout?.durationMinutes?.let { "${it}m" } ?: "—", colors.accent),
            Triple("ROUTINE", workout?.currentRoutine ?: "—", colors.info),
            Triple("CAL", workout?.calories?.toString() ?: "—", colors.warning),
            Triple("DATE", workout?.lastWorkoutDate?.let(::formatShortDate) ?: "—", colors.success)
        )
        MenuType.SUPPLEMENTS -> listOf(
            Triple("LAST BUY", snapshot.supplements?.lastPurchaseName ?: "—", colors.accent),
            Triple("BRAND", snapshot.supplements?.brand ?: "—", colors.info),
            Triple("DATE", snapshot.supplements?.lastPurchaseDate?.let(::formatShortDate) ?: "—", colors.success),
            Triple("PRICE", snapshot.supplements?.lastPurchasePrice?.let(::formatCompactMoney) ?: "—", colors.warning)
        )
        MenuType.NUTRITION -> listOf(
            Triple("PLAN", snapshot.nutrition?.planName ?: "—", colors.accent),
            Triple("STATUS", if (snapshot.nutrition?.isSubscribed == true) "ACTIVE" else "—", colors.success),
            Triple("RENEW", snapshot.nutrition?.renewalDate?.let(::formatShortDate) ?: "—", colors.info),
            Triple("PRICE", snapshot.nutrition?.monthlyPrice?.let(::formatCompactMoney) ?: "—", colors.warning)
        )
        MenuType.SERVICES -> {
            val services = snapshot.services.orEmpty()
            listOf(
                Triple("ACTIVE", services.count { it.isActive }.toString(), colors.success),
                Triple("TOTAL", services.size.toString(), colors.info),
                Triple("NEXT EXP", services.filter { it.isActive }.minByOrNull { it.expiryDate ?: Long.MAX_VALUE }?.expiryDate?.let(::formatShortDate) ?: "—", colors.warning),
                Triple("VALUE", services.sumOf { it.price ?: 0.0 }.let(::formatCompactMoney), colors.accent)
            )
        }
        MenuType.HISTORY -> listOf(
            Triple("EVENTS", snapshot.recentEvents.size.toString(), colors.accent),
            Triple("LAST", snapshot.recentEvents.firstOrNull()?.eventType?.displayLabel() ?: "—", colors.info),
            Triple("ISSUES", snapshot.issues.size.toString(), colors.danger),
            Triple("MEMBER", snapshot.identity.code ?: "—", colors.success)
        )
        MenuType.INSIGHT -> listOf(
            Triple("SIGNALS", snapshot.issues.size.toString(), colors.accent),
            Triple("CRITICAL", snapshot.issues.count { it.severity == IssueSeverity.CRITICAL }.toString(), colors.danger),
            Triple("DUE", payment?.totalOutstanding?.let(::formatCompactMoney) ?: "—", colors.warning),
            Triple("ATTEND", attendance?.visits?.let { "${it}/${attendance.target ?: "—"}" } ?: "—", colors.info)
        )
        MenuType.MORE -> listOf(
            Triple("TIER", snapshot.identity.tier.name, colors.accent),
            Triple("CODE", snapshot.identity.code ?: "—", colors.info),
            Triple("SINCE", formatShortDate(snapshot.identity.memberSince), colors.success),
            Triple("EVENTS", snapshot.recentEvents.size.toString(), colors.warning)
        )
        else -> emptyList()
    }

    Row(
        modifier = Modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(14.dp))
            .background(Brush.horizontalGradient(listOf(colors.surface.copy(alpha = 0.98f), colors.accentSoft.copy(alpha = 0.22f), colors.surface.copy(alpha = 0.98f))))
            .border(1.dp, colors.border.copy(alpha = 0.82f), RoundedCornerShape(14.dp)).padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        cells.forEachIndexed { index, cell ->
            if (index > 0) FusionDivider()
            FusionMetric(title = cell.first, value = cell.second, accent = cell.third)
        }
    }
}

@Composable
private fun RowScope.FusionMetric(
    title: String,
    value: String,
    accent: Color
) {
    val colors = BADGymTheme.colors
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(9.dp))
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, color = accent, fontSize = 6.5.sp, fontWeight = FontWeight.Black, maxLines = 1)
        Text(value, color = colors.textPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.Black, maxLines = 1)
        Text(caption, color = colors.textMuted, fontSize = 6.5.sp, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}

@Composable
private fun FusionDivider() {
    Box(
        Modifier
            .width(1.dp)
            .height(28.dp)
            .background(BADGymTheme.colors.border.copy(alpha = 0.55f))
    )
}

private fun formatShortDate(millis: Long): String = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(java.util.Date(millis))

private fun formatCompactMoney(value: Double): String {
    val rounded = value.toLong().coerceAtLeast(0L)
    return when {
        rounded >= 1000000L -> "₹" + (rounded / 1000000).toString() + "M"
        rounded >= 1000L -> "₹" + (rounded / 1000).toString() + "K"
        else -> "₹" + rounded.toString()
    }
}
