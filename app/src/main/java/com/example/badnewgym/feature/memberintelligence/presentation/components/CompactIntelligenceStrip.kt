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
    onAttendanceClick: () -> Unit = {},
    onTrainerClick: () -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors
    val attendance = snapshot.attendance
    val trainer = snapshot.trainer
    val workout = snapshot.workout
    val visits = attendance?.visits
    val target = attendance?.target
    val progress = if (visits != null && target != null && target > 0) {
        (visits.toFloat() / target.toFloat()).coerceIn(0f, 1f)
    } else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(420),
        label = "compact-intelligence-progress"
    )
    val sessionsLeft = trainer?.let { (it.sessionsTotal - it.sessionsUsed).coerceAtLeast(0) }
    val weekly = attendance?.weeklyPattern?.map { it.coerceAtLeast(0) } ?: emptyList()
    val maxWeekly = (weekly.maxOrNull() ?: 1).coerceAtLeast(1)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        colors.surface.copy(alpha = 0.98f),
                        colors.accentSoft.copy(alpha = 0.28f),
                        colors.surface.copy(alpha = 0.98f)
                    )
                )
            )
            .border(1.dp, colors.border.copy(alpha = 0.82f), RoundedCornerShape(14.dp))
            .padding(horizontal = 7.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AttendanceMini(progress, visits, target, weekly, maxWeekly, onAttendanceClick)
        FusionDivider()
        FusionMetric(
            icon = Icons.Rounded.FitnessCenter,
            title = "PT",
            value = sessionsLeft?.toString() ?: "—",
            caption = if (sessionsLeft != null) "sessions left" else "not recorded",
            accent = colors.accent,
            onClick = onTrainerClick
        )
        FusionDivider()
        FusionMetric(
            icon = Icons.Rounded.CalendarMonth,
            title = "WORKOUT",
            value = workout?.durationMinutes?.let { "\${it}m" } ?: "—",
            caption = "last recorded",
            accent = colors.info,
            onClick = onWorkoutClick
        )
        FusionDivider()
        val due = snapshot.payment?.totalOutstanding
        FusionMetric(
            icon = Icons.Rounded.Payments,
            title = "DUE",
            value = due?.let { formatCompactMoney(it) } ?: "—",
            caption = when {
                due == null -> "not recorded"
                due > 0 -> "action"
                else -> "clear"
            },
            accent = if (due != null && due > 0) colors.error else colors.success,
            onClick = onTrainerClick
        )
    }
}

@Composable
private fun AttendanceMini(
    progress: Float,
    visits: Int?,
    target: Int?,
    weekly: List<Int>,
    maxWeekly: Int,
    onClick: () -> Unit
) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier
            .width(104.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(39.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val stroke = 4.dp.toPx()
                drawArc(colors.surfaceMuted, -90f, 360f, false, style = Stroke(stroke, cap = StrokeCap.Round))
                drawArc(
                    Brush.sweepGradient(listOf(colors.accent, colors.info, colors.accent)),
                    -90f,
                    360f * progress,
                    false,
                    style = Stroke(stroke, cap = StrokeCap.Round)
                )
            }
            Text(
                text = if (target != null && target > 0) "\${(progress * 100).toInt()}%" else "—",
                color = colors.textPrimary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(Modifier.width(5.dp))
        Column(Modifier.weight(1f)) {
            Text("ATTEND", color = colors.textMuted, fontSize = 7.sp, fontWeight = FontWeight.Black)
            Text(
                text = if (visits != null && target != null) "\${visits}/\${target}" else "—",
                color = colors.textPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black
            )
            if (weekly.isNotEmpty()) {
                Row(
                    Modifier.fillMaxWidth().height(9.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    weekly.take(7).forEach { value ->
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxHeight((value.toFloat() / maxWeekly).coerceIn(0.12f, 1f))
                                .clip(RoundedCornerShape(2.dp))
                                .background(colors.accent.copy(alpha = 0.72f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FusionMetric(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    caption: String,
    accent: Color,
    onClick: () -> Unit
) {
    val colors = BADGymTheme.colors
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(9.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(10.dp))
            Spacer(Modifier.width(2.dp))
            Text(title, color = colors.textMuted, fontSize = 6.5.sp, fontWeight = FontWeight.Black)
        }
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

private fun formatCompactMoney(value: Double): String {
    val rounded = value.toLong().coerceAtLeast(0L)
    return when {
        rounded >= 1000000L -> "₹\${rounded / 1000000}M"
        rounded >= 1000L -> "₹\${rounded / 1000}K"
        else -> "₹\${rounded}"
    }
}
