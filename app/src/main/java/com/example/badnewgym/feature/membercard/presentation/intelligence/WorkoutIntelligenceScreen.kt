package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.intelligence.IntelligenceSeries
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import kotlin.math.roundToInt

/**
 * Neumorphic Workout Intelligence Screen:
 * - All headings, data, names, metrics highlighted & extra-bold.
 * - Monthly Workout Pattern features tactile Neumorphic Capsule Bars (Images 2 & 5).
 * - Ultra-compact layout with tactile 3D buttons and cards.
 */
@Composable
fun WorkoutIntelligenceScreen(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val measurer = rememberTextMeasurer()
    // Consistency is derived from real active days, so the tab cannot disagree with Overview.
    val monthDays = data.attendance.totalDays.coerceAtLeast(1)
    val consistency = (data.workoutTrend.daysWithWorkout * 100f / monthDays).roundToInt().coerceIn(0, 100)

    val points = IntelligenceSeries.workoutDailyNormalized(data.workoutTrend.bars)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // CARD 1: Workout Intelligence (Gauge + Stat Tiles + Daily Trend + 4 Chips)
        IntelligenceSectionCard(
            title = "Workout Intelligence",
            icon = Icons.Filled.FitnessCenter,
            trailing = { IntelligencePillOutlined("Oct 2026 ▾") }
        ) {
            // TOP ROW: Circular Gauge + 4 Stat Tiles in 2x2 grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Circular Ring Gauge (19.4 hr Total Workout) - Compact 70dp
                Box(
                    modifier = Modifier.size(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeW = 6.dp.toPx()
                        val arcSize = size.minDimension - strokeW
                        val topLeft = Offset(strokeW / 2f, strokeW / 2f)

                        // Track
                        drawArc(
                            color = NeuPalette.shadowSoft,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(arcSize, arcSize),
                            style = Stroke(strokeW, cap = StrokeCap.Round)
                        )

                        // Dual sweep gradient: neon green to purple
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(
                                    NeuPalette.green,
                                    NeuPalette.violet,
                                    NeuPalette.violetDeep,
                                    NeuPalette.green
                                )
                            ),
                            startAngle = -90f,
                            sweepAngle = 360f * 0.72f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(arcSize, arcSize),
                            style = Stroke(strokeW, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "19.4h",
                            fontSize = IntelligenceTypography.sizeMetric,
                            fontWeight = IntelligenceTypography.weightValue,
                            color = c.ink,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            "Workout",
                            fontSize = IntelligenceTypography.sizeSmall,
                            fontWeight = IntelligenceTypography.weightValue,
                            color = NeuPalette.violet
                        )
                    }
                }

                // 2x2 Grid of Stat Tiles - Highlighted & bold
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        WorkoutStatTile(
                            icon = Icons.Filled.Schedule,
                            iconTint = NeuPalette.green,
                            value = "${data.workoutTrend.avgPerDayMinutes} min",
                            label = "Avg / Day",
                            valueColor = c.ink,
                            modifier = Modifier.weight(1f)
                        )
                        WorkoutStatTile(
                            icon = Icons.Filled.EmojiEvents,
                            iconTint = Color(0xFFF59E0B),
                            value = "${data.workoutTrend.peakMinutes} min",
                            label = "Best 👑",
                            valueColor = Color(0xFFD97706),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        WorkoutStatTile(
                            icon = Icons.AutoMirrored.Filled.TrendingDown,
                            iconTint = Color(0xFFDC2626),
                            value = "${data.workoutTrend.lowestMinutes} min",
                            label = "Lowest 🔻",
                            valueColor = Color(0xFFDC2626),
                            modifier = Modifier.weight(1f)
                        )
                        WorkoutStatTile(
                            icon = Icons.Filled.TrackChanges,
                            iconTint = NeuPalette.violet,
                            value = "$consistency%",
                            label = "Consistency",
                            valueColor = NeuPalette.violet,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            // Subtitle: Daily Workout Trend
            Text(
                "Daily Workout Trend",
                fontSize = IntelligenceTypography.sizeCardTitle,
                fontWeight = IntelligenceTypography.weightValue,
                color = c.ink
            )

            // Daily Minutes Spline Chart with Vertical Stem Glow Volume - Compact 76dp
            DailyWorkoutTrendChart(points = points, measurer = measurer, c = c)

            Spacer(modifier = Modifier.height(3.dp))

            // 4 Horizontal Status Chips Row - Big bold readable values
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                WorkoutStatusChip(
                    icon = Icons.AutoMirrored.Filled.DirectionsRun,
                    iconTint = NeuPalette.green,
                    bg = Color(0xFFD1FAE5),
                    value = "19/31",
                    label = "Active Days",
                    modifier = Modifier.weight(1f)
                )
                WorkoutStatusChip(
                    icon = Icons.Filled.LocalFireDepartment,
                    iconTint = Color(0xFFD97706),
                    bg = Color(0xFFFEF3C7),
                    value = "5 Days",
                    label = "Streak",
                    modifier = Modifier.weight(1f)
                )
                WorkoutStatusChip(
                    icon = Icons.Filled.CalendarMonth,
                    iconTint = NeuPalette.violet,
                    bg = Color(0xFFEDE9FE),
                    value = "18/30",
                    label = "Goal",
                    modifier = Modifier.weight(1f)
                )
                WorkoutStatusChip(
                    icon = Icons.Filled.TrackChanges,
                    iconTint = NeuPalette.green,
                    bg = Color(0xFFD1FAE5),
                    value = "$consistency%",
                    label = "Steady",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // CARD 2: Monthly Workout Pattern (This Year) - Featuring Neumorphic Capsule Bars
        IntelligenceSectionCard(
            title = "Monthly Workout Pattern",
            icon = Icons.Filled.BarChart,
            subtitle = "(This Year)",
            trailing = { IntelligencePillOutlined("Total: 256.0 hr") }
        ) {
            MonthlyWorkoutCapsuleBarChart(measurer = measurer, c = c)
        }

        // CARD 3: Smart Insight [AI] Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .neuRaised(radius = 12.dp, depth = 3.dp, blur = 8.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.FitnessCenter,
                            contentDescription = null,
                            tint = NeuPalette.violet,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                "Smart Insight",
                                fontSize = IntelligenceTypography.sizeSmall,
                                fontWeight = IntelligenceTypography.weightValue,
                                color = c.ink
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NeuPalette.violet)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    "AI",
                                    fontSize = IntelligenceTypography.sizeMicro,
                                    fontWeight = IntelligenceTypography.weightValue,
                                    color = Color.White
                                )
                            }
                        }
                        Text(
                            "Consistency is $consistency% this month. Keep a 5-day streak to pass 44 hr next month.",
                            fontSize = IntelligenceTypography.sizeSmall,
                            fontWeight = IntelligenceTypography.weightBody,
                            color = NeuPalette.inkSoft,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // "View Plan ›" 3D Neumorphic Pill Button (matching Image 1)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF8B5CF6), NeuPalette.violetDeep)
                            )
                        )
                        .clickable(onClick = onClick)
                        .padding(horizontal = 11.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "View Plan ›",
                        fontSize = IntelligenceTypography.sizeSmall,
                        fontWeight = IntelligenceTypography.weightValue,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/** Stat tile for Workout section. All labels & values are highlighted, bold, and readable. */
@Composable
private fun WorkoutStatTile(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .neuRaised(radius = 8.dp, depth = 2.dp, blur = 4.dp)
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(13.dp))
                Text(label, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft, maxLines = 1)
            }
            Text(value, fontSize = IntelligenceTypography.sizeCardTitle, fontWeight = IntelligenceTypography.weightValue, color = valueColor, maxLines = 1)
        }
    }
}

/** Status chip below the Daily Workout Trend graph. */
@Composable
private fun WorkoutStatusChip(
    icon: ImageVector,
    iconTint: Color,
    bg: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .neuRaised(radius = 8.dp, depth = 2.dp, blur = 4.dp)
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(12.dp))
            }
            Column {
                Text(value, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = NeuPalette.ink, maxLines = 1)
                Text(label, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft, maxLines = 1)
            }
        }
    }
}

/**
 * Daily Workout Trend Spline with Glowing Vertical Stems and Callout Badges
 */
@Composable
private fun DailyWorkoutTrendChart(
    points: List<IntelligenceSeries.WorkoutDailyPoint>,
    measurer: TextMeasurer,
    c: IntelligenceColors
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
    ) {
        val plotLeft = 28.dp.toPx()
        val plotRight = size.width - 20.dp.toPx()
        val plotTop = 22.dp.toPx()
        val plotBottom = size.height - 18.dp.toPx()
        val plotH = (plotBottom - plotTop).coerceAtLeast(1f)
        val span = plotRight - plotLeft
        val axisStyle = TextStyle(fontSize = IntelligenceTypography.sizeSmall, color = NeuPalette.inkSoft, fontWeight = IntelligenceTypography.weightBody)

        val xOf = { day: Int -> plotLeft + (day - 1) * span / 30f }
        val yOf = { minutes: Int -> plotBottom - (minutes / 90f) * plotH }

        // Y-axis gridlines & labels (90m, 45m, 0m)
        val yLabels = listOf("90m", "45m", "0m")
        yLabels.forEachIndexed { i, label ->
            val y = plotTop + i * plotH / 2f
            drawLine(
                color = NeuPalette.shadowSoft,
                start = Offset(plotLeft, y),
                end = Offset(plotRight, y),
                strokeWidth = 1f
            )
            val lw = measurer.measure(label, axisStyle).size.width
            drawText(measurer, label, Offset(plotLeft - lw - 3.dp.toPx(), y - 6.dp.toPx()), style = axisStyle)
        }

        // X-axis labels (1, 5, 10, 15, 20, 25, 31)
        listOf(1, 5, 10, 15, 20, 25, 31).forEach { day ->
            val text = day.toString()
            val tw = measurer.measure(text, axisStyle).size.width
            val labelX = (xOf(day) - tw / 2f).coerceIn(plotLeft - tw, size.width - tw)
            drawText(measurer, text, Offset(labelX, plotBottom + 2.dp.toPx()), style = axisStyle)
        }

        // Draw Vertical Glowing Stems from x-axis to each data point
        points.forEach { p ->
            val x = xOf(p.day)
            val y = yOf(p.minutes)
            if (p.minutes > 0) {
                drawLine(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NeuPalette.green.copy(alpha = 0.5f),
                            NeuPalette.green.copy(alpha = 0.05f)
                        ),
                        startY = y,
                        endY = plotBottom
                    ),
                    start = Offset(x, y),
                    end = Offset(x, plotBottom),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // Smooth Spline Line
        val linePath = Path().apply {
            if (points.isNotEmpty()) {
                moveTo(xOf(points[0].day), yOf(points[0].minutes))
                for (i in 0 until points.size - 1) {
                    val p0 = points[i]
                    val p1 = points[i + 1]
                    val x0 = xOf(p0.day)
                    val y0 = yOf(p0.minutes)
                    val x1 = xOf(p1.day)
                    val y1 = yOf(p1.minutes)
                    val midX = (x0 + x1) / 2f
                    cubicTo(midX, y0, midX, y1, x1, y1)
                }
            }
        }

        // Spline Area Fill
        val areaPath = Path().apply {
            if (points.isNotEmpty()) {
                moveTo(xOf(points[0].day), plotBottom)
                lineTo(xOf(points[0].day), yOf(points[0].minutes))
                for (i in 0 until points.size - 1) {
                    val p0 = points[i]
                    val p1 = points[i + 1]
                    val x0 = xOf(p0.day)
                    val y0 = yOf(p0.minutes)
                    val x1 = xOf(p1.day)
                    val y1 = yOf(p1.minutes)
                    val midX = (x0 + x1) / 2f
                    cubicTo(midX, y0, midX, y1, x1, y1)
                }
                lineTo(xOf(points.last().day), plotBottom)
                close()
            }
        }

        // Draw area fill
        drawPath(
            path = areaPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    NeuPalette.green.copy(alpha = 0.20f),
                    NeuPalette.violet.copy(alpha = 0.08f),
                    NeuPalette.violet.copy(alpha = 0.00f)
                ),
                startY = plotTop,
                endY = plotBottom
            )
        )

        // Draw line stroke
        drawPath(
            path = linePath,
            brush = Brush.horizontalGradient(
                listOf(NeuPalette.violet, NeuPalette.green, NeuPalette.violet)
            ),
            style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Data point circles
        points.forEach { p ->
            val x = xOf(p.day)
            val y = yOf(p.minutes)
            if (p.minutes > 0) {
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = Offset(x, y)
                )
                drawCircle(
                    color = if (p.minutes >= 80) NeuPalette.green else if (p.minutes <= 35) Color(0xFFEF4444) else NeuPalette.violet,
                    radius = 2.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }

        // Floating callout badges matching peak points:
        val badges = listOf(
            WorkoutBadgeData(day = 2, minutes = 47, label = "47m", bg = NeuPalette.violet, hasCrown = false),
            WorkoutBadgeData(day = 8, minutes = 88, label = "88m", bg = NeuPalette.green, hasCrown = true),
            WorkoutBadgeData(day = 16, minutes = 32, label = "32m", bg = Color(0xFFDC2626), hasCrown = false),
            WorkoutBadgeData(day = 28, minutes = 74, label = "74m", bg = NeuPalette.violet, hasCrown = false)
        )

        badges.forEach { b ->
            val bx = xOf(b.day)
            val by = yOf(b.minutes)

            val badgeStyle = TextStyle(
                fontSize = IntelligenceTypography.sizeMicro,
                color = Color.White,
                fontWeight = IntelligenceTypography.weightValue
            )
            val textLayout = measurer.measure(b.label, badgeStyle)
            val padH = 4.dp.toPx()
            val padV = 2.dp.toPx()
            val boxW = textLayout.size.width + padH * 2
            val boxH = textLayout.size.height + padV * 2
            val boxLeft = (bx - boxW / 2f).coerceIn(plotLeft, plotRight - boxW)
            val boxTop = by - boxH - 6.dp.toPx()

            if (b.hasCrown) {
                drawCrown(centerX = bx, baseY = boxTop - 2.dp.toPx())
            }

            drawRoundRect(
                color = b.bg,
                topLeft = Offset(boxLeft, boxTop),
                size = Size(boxW, boxH),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
            drawText(measurer, b.label, Offset(boxLeft + padH, boxTop + padV), style = badgeStyle)
        }
    }
}

private data class WorkoutBadgeData(
    val day: Int,
    val minutes: Int,
    val label: String,
    val bg: Color,
    val hasCrown: Boolean
)

/**
 * Neumorphic Capsule Bar Chart (100% matched to reference Images 2 & 5):
 * - Sunken vertical pill tracks with rounded capsule ends.
 * - Liquid gradient capsule fills rising from the baseline.
 * - Peak July with Gold Crown 👑 and emerald gradient fill.
 * - High-contrast bold numbers and month labels.
 */
@Composable
private fun MonthlyWorkoutCapsuleBarChart(
    measurer: TextMeasurer,
    c: IntelligenceColors
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
    ) {
        val plotBottom = size.height - 18.dp.toPx()
        val topPad = 20.dp.toPx()
        val totalTrackH = plotBottom - topPad
        val slotW = size.width / 10f
        val barW = (slotW * 0.48f).coerceAtLeast(10.dp.toPx())
        val capsuleRadius = CornerRadius(barW / 2f, barW / 2f)
        val maxHr = 44f

        val monthStyle = TextStyle(fontSize = IntelligenceTypography.sizeSmall, color = NeuPalette.inkSoft, fontWeight = IntelligenceTypography.weightValue)
        val hourStyle = TextStyle(fontSize = IntelligenceTypography.sizeSmall, color = NeuPalette.ink, fontWeight = IntelligenceTypography.weightValue)
        val redHourStyle = TextStyle(fontSize = IntelligenceTypography.sizeSmall, color = Color(0xFFDC2626), fontWeight = IntelligenceTypography.weightValue)

        val months = listOf(
            MonthBarItem("Jan", 36, false, false, false),
            MonthBarItem("Feb", 40, false, false, false),
            MonthBarItem("Mar", 12, false, true, false),
            MonthBarItem("Apr", 24, false, false, false),
            MonthBarItem("May", 28, false, false, false),
            MonthBarItem("Jun", 0, false, false, true),
            MonthBarItem("Jul", 44, true, false, false),
            MonthBarItem("Aug", 28, false, false, false),
            MonthBarItem("Sep", 28, false, false, false),
            MonthBarItem("Oct", 40, false, false, false)
        )

        months.forEachIndexed { i, m ->
            val cx = slotW * (i + 0.5f)
            val mw = measurer.measure(m.label, monthStyle).size.width
            drawText(measurer, m.label, Offset(cx - mw / 2f, plotBottom + 3.dp.toPx()), style = monthStyle)

            val left = cx - barW / 2f

            // 1. Draw the Neumorphic Sunken Capsule Track (Images 2 & 5)
            drawRoundRect(
                color = Color(0xFFEDE9FE),
                topLeft = Offset(left, topPad),
                size = Size(barW, totalTrackH),
                cornerRadius = capsuleRadius
            )
            // Inner track subtle border
            drawRoundRect(
                color = Color(0xFFDDD6FE),
                topLeft = Offset(left, topPad),
                size = Size(barW, totalTrackH),
                cornerRadius = capsuleRadius,
                style = Stroke(width = 0.8.dp.toPx())
            )

            if (m.isHatched) {
                // June: empty / dashed pill track
                val dashText = "-"
                val dw = measurer.measure(dashText, monthStyle).size.width
                drawText(measurer, dashText, Offset(cx - dw / 2f, topPad + totalTrackH / 2f - 7.dp.toPx()), style = monthStyle)
            } else {
                // 2. Liquid Capsule Fill inside the sunken track
                val fillH = ((m.hours / maxHr) * totalTrackH).coerceAtLeast(barW)
                val fillTop = plotBottom - fillH

                val barBrush = when {
                    m.isPeak -> Brush.verticalGradient(
                        listOf(Color(0xFF00E5FF), NeuPalette.green, NeuPalette.green)
                    )
                    m.isAlert -> Brush.verticalGradient(
                        listOf(Color(0xFFFF8A80), Color(0xFFEF4444), Color(0xFFDC2626))
                    )
                    else -> Brush.verticalGradient(
                        listOf(Color(0xFF38BDF8), NeuPalette.violet, NeuPalette.violetDeep)
                    )
                }

                drawRoundRect(
                    brush = barBrush,
                    topLeft = Offset(left, fillTop),
                    size = Size(barW, fillH),
                    cornerRadius = capsuleRadius
                )

                // 3. Highlighted bold hour text above the capsule
                val hourText = "${m.hours}h"
                val textStyle = if (m.isAlert) redHourStyle else hourStyle
                val hw = measurer.measure(hourText, textStyle).size.width
                val labelY = topPad - 13.dp.toPx()

                if (m.isPeak) {
                    drawCrown(centerX = cx, baseY = labelY - 2.dp.toPx())
                }
                drawText(measurer, hourText, Offset(cx - hw / 2f, labelY), style = textStyle)
            }
        }
    }
}

private data class MonthBarItem(
    val label: String,
    val hours: Int,
    val isPeak: Boolean,
    val isAlert: Boolean,
    val isHatched: Boolean
)
