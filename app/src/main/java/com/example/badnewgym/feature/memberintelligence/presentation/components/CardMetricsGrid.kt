package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSummary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CardMetricsGrid(
    attendance: AttendanceSummary?,
    payment: PaymentSummary?,
    workoutsCount: Int,
    theme: ThemeId,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Tile 1: Attendance with circular progress ring
        val visits = attendance?.visits ?: 16
        val target = attendance?.target ?: 26
        val attendancePercent = if (target > 0) ((visits.toFloat() / target) * 100).toInt() else 0
        MetricTileContainer(theme = theme, modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$visits/$target",
                        color = colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Attendance",
                        color = colors.textSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier.size(38.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { (attendancePercent / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxSize(),
                        color = when (theme) {
                            ThemeId.PREMIUM_3D -> Color(0xFFFFD700)
                            ThemeId.BEAST_MODE -> Color(0xFFFF4D5E)
                            ThemeId.PURPLE_ROYAL -> Color(0xFFC084FC)
                            ThemeId.FUTURISTIC_NEON -> Color(0xFF00E5FF)
                            ThemeId.VIBRANT_GRADIENT -> Color(0xFF10B981)
                            ThemeId.GLASSMORPHISM -> Color(0xFF0284C7)
                            ThemeId.MINIMAL_DARK -> Color(0xFFCBD5E1)
                            ThemeId.NATURAL_FRESH -> Color(0xFF16A34A)
                        },
                        strokeWidth = 4.dp,
                        trackColor = colors.surfaceMuted
                    )
                    Text(
                        text = "$attendancePercent%",
                        color = colors.textPrimary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // Tile 2: Payment Status / Due
        val totalDue = payment?.totalOutstanding ?: 4500.0
        val overdueDays = payment?.overdueDays ?: 3
        val isOverdue = totalDue > 0
        val formattedAmount = if (totalDue > 0) {
            val formatter = NumberFormat.getNumberInstance(Locale("en", "IN"))
            "₹" + formatter.format(totalDue.toInt())
        } else "₹0"

        MetricTileContainer(theme = theme, modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formattedAmount,
                        color = if (isOverdue) Color(0xFFDC2626) else colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Payment Due",
                        color = colors.textSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isOverdue) Color(0xFFFEE2E2)
                            else if (theme == ThemeId.MINIMAL_DARK || theme == ThemeId.PREMIUM_3D || theme == ThemeId.BEAST_MODE || theme == ThemeId.PURPLE_ROYAL) Color(0xFF064E3B)
                            else Color(0xFFDCFCE7)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isOverdue) "$overdueDays days" else "All Clear",
                        color = if (isOverdue) Color(0xFFDC2626) else Color(0xFF16A34A),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Tile 3: Workouts count with six-bar progression
        val countDisplay = if (workoutsCount > 0) workoutsCount else 12
        MetricTileContainer(theme = theme, modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$countDisplay",
                        color = colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Workouts",
                        color = colors.textSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Six-Bar Equalizer Progression
                Row(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.5.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    val barHeights = listOf(0.35f, 0.55f, 0.8f, 1.0f, 0.65f, 0.9f)
                    val barColor = when (theme) {
                        ThemeId.PREMIUM_3D -> Color(0xFFFFD700)
                        ThemeId.BEAST_MODE -> Color(0xFFFF1E27)
                        ThemeId.PURPLE_ROYAL -> Color(0xFFA855F7)
                        ThemeId.FUTURISTIC_NEON -> Color(0xFF00E5FF)
                        ThemeId.VIBRANT_GRADIENT -> Color(0xFF38BDF8)
                        ThemeId.GLASSMORPHISM -> Color(0xFF0284C7)
                        ThemeId.MINIMAL_DARK -> Color(0xFF9CA3AF)
                        ThemeId.NATURAL_FRESH -> Color(0xFF16A34A)
                    }

                    barHeights.forEach { h ->
                        Box(
                            modifier = Modifier
                                .width(3.5.dp)
                                .fillMaxHeight(h)
                                .clip(RoundedCornerShape(2.dp))
                                .background(barColor)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricTileContainer(
    theme: ThemeId,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .height(90.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFF1F1B12)
                    ThemeId.BEAST_MODE -> Color(0xFF1C0609)
                    ThemeId.PURPLE_ROYAL -> Color(0xFF22113B)
                    ThemeId.FUTURISTIC_NEON -> Color(0xFF0A1428)
                    ThemeId.VIBRANT_GRADIENT -> Color(0xFFF8FAFC)
                    ThemeId.GLASSMORPHISM -> Color(0xB0FFFFFF)
                    ThemeId.MINIMAL_DARK -> Color(0xFF181C22)
                    ThemeId.NATURAL_FRESH -> Color(0xFFF6FBF7)
                }
            )
            .border(
                width = 1.dp,
                color = when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFF4A3B1D)
                    ThemeId.BEAST_MODE -> Color(0x66FF1E27)
                    ThemeId.PURPLE_ROYAL -> Color(0x66A855F7)
                    ThemeId.FUTURISTIC_NEON -> Color(0x6600E5FF)
                    ThemeId.VIBRANT_GRADIENT -> Color(0xFFE2E8F0)
                    ThemeId.GLASSMORPHISM -> Color(0x80FFFFFF)
                    ThemeId.MINIMAL_DARK -> Color(0xFF28303C)
                    ThemeId.NATURAL_FRESH -> Color(0xFFD1E7D7)
                },
                shape = RoundedCornerShape(14.dp)
            )
            .padding(vertical = 8.dp, horizontal = 4.dp),
        content = content
    )
}
