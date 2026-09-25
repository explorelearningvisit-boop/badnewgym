package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CalendarMonth
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
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import kotlin.math.max

@Composable
fun HomeEnergyPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onAttendanceClick: () -> Unit,
    onWorkoutClick: () -> Unit
) {
    val colors = BADGymTheme.colors
    val pattern = snapshot.attendance?.weeklyPattern?.map { it.coerceAtLeast(0) } ?: emptyList()
    val maxValue = max(1, pattern.maxOrNull() ?: 1)
    val attendance = snapshot.attendance
    val attendancePercent = if (attendance?.target != null && attendance.target > 0) ((attendance.visits.toFloat() / attendance.target) * 100f).coerceIn(0f, 100f) else null
    val progress by animateFloatAsState((attendancePercent ?: 0f) / 100f, tween(520), label = "member-attendance-progress")

    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(colors.surface, colors.accentSoft.copy(alpha = 0.56f), colors.surface)))
            .border(1.dp, colors.border, RoundedCornerShape(18.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(82.dp).clip(CircleShape).background(colors.surface.copy(alpha = 0.96f))
                .border(2.dp, colors.accent.copy(alpha = 0.18f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(Modifier.fillMaxSize().padding(8.dp)) {
                val stroke = 8.dp.toPx()
                drawArc(colors.surfaceMuted, -90f, 360f, false, style = Stroke(stroke, cap = StrokeCap.Round))
                drawArc(Brush.sweepGradient(listOf(colors.accent, colors.info, colors.accent)), -90f, 360f * progress, false, style = Stroke(stroke, cap = StrokeCap.Round))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(attendancePercent?.let { "${it.toInt()}%" } ?: "—", color = colors.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Black)
                Text("goal", color = colors.textMuted, fontSize = 8.sp)
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Bolt, null, tint = colors.accent, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("MEMBER PULSE", color = colors.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
            }
            Text(
                when {
                    pattern.isNotEmpty() -> "Recorded attendance rhythm"
                    attendance?.lastVisitAt != null -> "Latest recorded visit is available"
                    else -> "Live summary • detail loads on demand"
                },
                color = colors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(6.dp))
            if (pattern.isNotEmpty()) {
                Row(
                    Modifier.fillMaxWidth().height(42.dp).clickable(onClick = onAttendanceClick),
                    horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.Bottom
                ) {
                    pattern.forEachIndexed { index, value ->
                        val h by animateFloatAsState((value.toFloat() / maxValue).coerceIn(0.08f, 1f), tween(360 + index * 35), label = "attendance-bar-$index")
                        Box(
                            Modifier.weight(1f).fillMaxHeight(h).clip(RoundedCornerShape(5.dp))
                                .background(Brush.verticalGradient(listOf(colors.info, colors.accent)))
                        )
                    }
                }
            } else {
                Box(
                    Modifier.fillMaxWidth().height(42.dp).clip(RoundedCornerShape(10.dp))
                        .background(colors.surfaceMuted.copy(alpha = 0.65f)).border(1.dp, colors.border, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Weekly pattern not recorded", color = colors.textMuted, fontSize = 9.sp)
                }
            }
            Spacer(Modifier.height(5.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                EnergyPill(attendance?.periodName ?: "Current period", attendance?.visits?.let { "${it} visits" } ?: "Not recorded", Icons.Rounded.CalendarMonth, colors.info, onAttendanceClick)
                EnergyPill("Workout", snapshot.workout?.durationMinutes?.let { "${it}m last" } ?: "Not recorded", Icons.Rounded.Bolt, colors.accent, onWorkoutClick)
            }
        }
    }
}

@Composable
private fun EnergyPill(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    Row(
        Modifier.clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.18f), RoundedCornerShape(10.dp)).clickable(onClick = onClick)
            .padding(horizontal = 7.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(12.dp))
        Spacer(Modifier.width(4.dp))
        Column {
            Text(label, color = BADGymTheme.colors.textMuted, fontSize = 7.sp, fontWeight = FontWeight.Bold)
            Text(value, color = BADGymTheme.colors.textPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        }
    }
}