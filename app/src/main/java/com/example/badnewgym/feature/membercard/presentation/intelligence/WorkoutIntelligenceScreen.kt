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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
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
import java.util.Locale

/**
 * Workout Intelligence Screen
 * 100% 1:1 Pixel-Perfect Reproduction of Reference: media_1794217939597_36ee4831.jpg
 * - 19.4 hr Total Workout circular ring gauge (dual neon green / purple sweep)
 * - 4 Stat tiles (37 min Avg/Day, 88 min Best 👑, 32 min Lowest 🔻, 61% Consistency 🎯)
 * - Daily Workout Trend area spline with glowing vertical stem volume lines, 4 callout badges (47m, 88m 👑, 32m, 74m)
 * - 4 horizontal status chips (19/31 Active Days, 5 Days Current Streak, 18/30 Goal Days, 61% Consistency)
 * - Monthly Workout Pattern with 10 monthly bars, July 44h peak green bar with gold crown 👑, Total 256.0 hr
 * - Smart Insight [AI] card with athlete silhouette illustration and "View Plan ›" action
 */
@Composable
fun WorkoutIntelligenceScreen(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val measurer = rememberTextMeasurer()
    val points = IntelligenceSeries.workoutDailyNormalized(data.workoutTrend.bars)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Circular Ring Gauge (19.4 hr Total Workout)
                Box(
                    modifier = Modifier.size(98.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeW = 8.dp.toPx()
                        val arcSize = size.minDimension - strokeW
                        val topLeft = Offset(strokeW / 2f, strokeW / 2f)

                        // Track
                        drawArc(
                            color = Color(0xFFF1F5F9),
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
                                    Color(0xFF10B981),
                                    Color(0xFF8B5CF6),
                                    Color(0xFF7C3AED),
                                    Color(0xFF10B981)
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
                            "19.4 hr",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = c.ink,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            "Total Workout",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // 2x2 Grid of Stat Tiles
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        WorkoutStatTile(
                            icon = Icons.Filled.Schedule,
                            iconTint = Color(0xFF10B981),
                            value = "37 min",
                            label = "Avg / Day",
                            valueColor = c.ink,
                            modifier = Modifier.weight(1f)
                        )
                        WorkoutStatTile(
                            icon = Icons.Filled.EmojiEvents,
                            iconTint = Color(0xFFF59E0B),
                            value = "88 min",
                            label = "Best",
                            valueColor = c.ink,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        WorkoutStatTile(
                            icon = Icons.AutoMirrored.Filled.TrendingDown,
                            iconTint = Color(0xFFEF4444),
                            value = "32 min",
                            label = "Lowest",
                            valueColor = Color(0xFFEF4444),
                            modifier = Modifier.weight(1f)
                        )
                        WorkoutStatTile(
                            icon = Icons.Filled.TrackChanges,
                            iconTint = Color(0xFF10B981),
                            value = "61%",
                            label = "Consistency",
                            valueColor = c.ink,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle: Daily Workout Trend
            Text(
                "Daily Workout Trend",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = c.ink
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Daily Minutes Spline Chart with Vertical Stem Glow Volume
            DailyWorkoutTrendChart(points = points, measurer = measurer, c = c)

            Spacer(modifier = Modifier.height(8.dp))

            // 4 Horizontal Status Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                WorkoutStatusChip(
                    icon = Icons.AutoMirrored.Filled.DirectionsRun,
                    iconTint = Color(0xFF10B981),
                    bg = Color(0xFFDCFCE7),
                    value = "19 / 31",
                    label = "Active Days",
                    modifier = Modifier.weight(1f)
                )
                WorkoutStatusChip(
                    icon = Icons.Filled.LocalFireDepartment,
                    iconTint = Color(0xFFF59E0B),
                    bg = Color(0xFFFEF3C7),
                    value = "5 Days",
                    label = "Current Streak",
                    modifier = Modifier.weight(1f)
                )
                WorkoutStatusChip(
                    icon = Icons.Filled.CalendarMonth,
                    iconTint = Color(0xFF8B5CF6),
                    bg = Color(0xFFEDE9FE),
                    value = "18 / 30",
                    label = "Goal Days",
                    modifier = Modifier.weight(1f)
                )
                WorkoutStatusChip(
                    icon = Icons.Filled.TrackChanges,
                    iconTint = Color(0xFF10B981),
                    bg = Color(0xFFDCFCE7),
                    value = "61%",
                    label = "Consistency",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // CARD 2: Monthly Workout Pattern (This Year)
        IntelligenceSectionCard(
            title = "Monthly Workout Pattern",
            icon = Icons.Filled.BarChart,
            subtitle = "(This Year)",
            trailing = { IntelligencePillOutlined("⏱ Total: 256.0 hr") }
        ) {
            MonthlyWorkoutPatternChart(pattern = IntelligenceSeries.workoutMonthlyPattern(), measurer = measurer, c = c)
        }

        // CARD 3: Smart Insight [AI] Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color(0x188B5CF6), spotColor = Color(0x188B5CF6))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color(0x288B5CF6), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.FitnessCenter,
                            contentDescription = null,
                            tint = Color(0xFF8B5CF6),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                "Smart Insight",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = c.ink
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFEDE9FE))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    "AI",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF8B5CF6)
                                )
                            }
                        }
                        Text(
                            "Great consistency (61%)! 🔥 Maintain 5+ day streak to reach 44 hr+ next month.",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF475569),
                            lineHeight = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // "View Plan ›" Pill Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                        .border(1.dp, Color(0xFF8B5CF6), RoundedCornerShape(50))
                        .clickable(onClick = onClick)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            "View Plan ›",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B5CF6)
                        )
                    }
                }
            }
        }
    }
}

/** Stat tile inside the 2x2 grid matching reference. */
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
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(11.dp))
                Text(label, fontSize = 7.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B), maxLines = 1)
            }
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = valueColor, maxLines = 1)
        }
    }
}

/** Status chip below the Daily Workout Trend graph matching reference. */
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
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(horizontal = 4.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(11.dp))
            }
            Column {
                Text(value, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1B4B), maxLines = 1)
                Text(label, fontSize = 6.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B), maxLines = 1)
            }
        }
    }
}

/**
 * Daily Workout Trend Spline with Glowing Vertical Stems (Volume Drop Lines) and Callout Badges
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
            .height(130.dp)
    ) {
        val plotLeft = 28.dp.toPx()
        val plotRight = size.width - 6.dp.toPx()
        val plotTop = 32.dp.toPx()
        val plotBottom = size.height - 20.dp.toPx()
        val plotH = (plotBottom - plotTop).coerceAtLeast(1f)
        val span = plotRight - plotLeft
        val axisStyle = TextStyle(fontSize = 7.5.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)

        val xOf = { day: Int -> plotLeft + (day - 1) * span / 30f }
        val yOf = { minutes: Int -> plotBottom - (minutes / 90f) * plotH }

        // Y-axis gridlines & labels (90m, 68m, 45m, 23m, 0m)
        val yLabels = listOf("90m", "68m", "45m", "23m", "0m")
        yLabels.forEachIndexed { i, label ->
            val y = plotTop + i * plotH / 4f
            drawLine(
                color = Color(0xFFF1F5F9),
                start = Offset(plotLeft, y),
                end = Offset(plotRight, y),
                strokeWidth = 1f
            )
            drawText(measurer, label, Offset(0f, y - 5.dp.toPx()), style = axisStyle)
        }

        // X-axis labels (1, 5, 10, 15, 20, 25, 31)
        listOf(1, 5, 10, 15, 20, 25, 31).forEach { day ->
            val text = day.toString()
            val tw = measurer.measure(text, axisStyle).size.width
            val labelX = (xOf(day) - tw / 2f).coerceIn(plotLeft - tw, size.width - tw)
            drawText(measurer, text, Offset(labelX, plotBottom + 4.dp.toPx()), style = axisStyle)
        }

        // Draw Vertical Glowing Stems (volume lines) from x-axis to each data point
        points.forEach { p ->
            val x = xOf(p.day)
            val y = yOf(p.minutes)
            if (p.minutes > 0) {
                drawLine(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF10B981).copy(alpha = 0.5f),
                            Color(0xFF10B981).copy(alpha = 0.05f)
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
                    Color(0xFF10B981).copy(alpha = 0.20f),
                    Color(0xFF8B5CF6).copy(alpha = 0.08f),
                    Color(0xFF8B5CF6).copy(alpha = 0.00f)
                ),
                startY = plotTop,
                endY = plotBottom
            )
        )

        // Draw line stroke
        drawPath(
            path = linePath,
            brush = Brush.horizontalGradient(
                listOf(Color(0xFF8B5CF6), Color(0xFF10B981), Color(0xFF8B5CF6))
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
                    color = if (p.minutes >= 80) Color(0xFF10B981) else if (p.minutes <= 35) Color(0xFFEF4444) else Color(0xFF8B5CF6),
                    radius = 2.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }

        // Floating callout badges matching reference:
        // Day 2 (47m, purple), Day 8 (88m, green with gold crown 👑), Day 16 (32m, red), Day 28 (74m, purple)
        val badges = listOf(
            WorkoutBadgeData(day = 2, minutes = 47, label = "47m", bg = Color(0xFF8B5CF6), hasCrown = false),
            WorkoutBadgeData(day = 8, minutes = 88, label = "88m", bg = Color(0xFF10B981), hasCrown = true),
            WorkoutBadgeData(day = 16, minutes = 32, label = "32m", bg = Color(0xFFEF4444), hasCrown = false),
            WorkoutBadgeData(day = 28, minutes = 74, label = "74m", bg = Color(0xFF8B5CF6), hasCrown = false)
        )

        badges.forEach { b ->
            val x = xOf(b.day)
            val y = yOf(b.minutes)
            drawWorkoutCalloutBadge(
                text = b.label,
                x = x,
                y = y,
                bg = b.bg,
                hasCrown = b.hasCrown,
                measurer = measurer
            )
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

/** Callout badge on the Daily Workout Trend spline. */
private fun DrawScope.drawWorkoutCalloutBadge(
    text: String,
    x: Float,
    y: Float,
    bg: Color,
    hasCrown: Boolean,
    measurer: TextMeasurer
) {
    val style = TextStyle(fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
    val measured = measurer.measure(text, style)
    val padH = 5.dp.toPx()
    val padV = 2.dp.toPx()
    val boxW = measured.size.width + padH * 2f
    val boxH = measured.size.height + padV * 2f
    val gap = 6.dp.toPx()
    val top = y - boxH - gap
    val left = (x - boxW / 2f).coerceIn(2.dp.toPx(), size.width - boxW - 2.dp.toPx())

    if (hasCrown) {
        drawCrown(centerX = left + boxW / 2f, baseY = top - 2.dp.toPx())
    }

    drawRoundRect(
        color = bg,
        topLeft = Offset(left, top),
        size = Size(boxW, boxH),
        cornerRadius = CornerRadius(6.dp.toPx())
    )
    drawText(measurer, text, Offset(left + padH, top + padV), style = style)
}

/**
 * Monthly Workout Pattern Chart with 10 bars:
 * Jan (36h), Feb (40h), Mar (12h red), Apr (24h), May (28h), Jun (- dashed), Jul (44h green 👑), Aug (28h), Sep (28h), Oct (40h)
 */
@Composable
private fun MonthlyWorkoutPatternChart(
    pattern: List<IntelligenceSeries.WorkoutMonthPoint>,
    measurer: TextMeasurer,
    c: IntelligenceColors
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
    ) {
        val plotBottom = size.height - 16.dp.toPx()
        val topPad = 32.dp.toPx()
        val slotW = size.width / 10f
        val barW = slotW * 0.54f
        val maxHr = 44f

        val monthStyle = TextStyle(fontSize = 8.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
        val hourStyle = TextStyle(fontSize = 9.sp, color = Color(0xFF1E1B4B), fontWeight = FontWeight.ExtraBold)
        val redHourStyle = TextStyle(fontSize = 9.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.ExtraBold)

        val months = listOf(
            MonthBarItem("Jan", 36, false, false, false),
            MonthBarItem("Feb", 40, false, false, false),
            MonthBarItem("Mar", 12, false, true, false),
            MonthBarItem("Apr", 24, false, false, false),
            MonthBarItem("May", 28, false, false, false),
            MonthBarItem("Jun", 0, false, false, true), // Empty / hatched
            MonthBarItem("Jul", 44, true, false, false), // Peak with crown & green gradient
            MonthBarItem("Aug", 28, false, false, false),
            MonthBarItem("Sep", 28, false, false, false),
            MonthBarItem("Oct", 40, false, false, false)
        )

        months.forEachIndexed { i, m ->
            val cx = slotW * (i + 0.5f)
            val mw = measurer.measure(m.label, monthStyle).size.width
            drawText(measurer, m.label, Offset(cx - mw / 2f, plotBottom + 3.dp.toPx()), style = monthStyle)

            val left = cx - barW / 2f
            val right = left + barW

            if (m.isHatched) {
                // Dashed / hatched empty bar for June
                val barH = 26.dp.toPx()
                val barTop = plotBottom - barH
                drawRoundRect(
                    color = Color(0xFFE2E8F0),
                    topLeft = Offset(left, barTop),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                    style = Stroke(1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f)))
                )
                val dashText = "-"
                val dw = measurer.measure(dashText, monthStyle).size.width
                drawText(measurer, dashText, Offset(cx - dw / 2f, barTop - 14.dp.toPx()), style = monthStyle)
            } else {
                val barH = (m.hours / maxHr) * (plotBottom - topPad)
                val barTop = plotBottom - barH

                val barBrush = when {
                    m.isPeak -> Brush.verticalGradient(listOf(Color(0xFF10B981), Color(0xFF34D399)))
                    m.isAlert -> Brush.verticalGradient(listOf(Color(0xFFFCA5A5), Color(0xFFEF4444)))
                    else -> Brush.verticalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA)))
                }

                drawRoundRect(
                    brush = barBrush,
                    topLeft = Offset(left, barTop),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(5.dp.toPx())
                )

                // Label above bar
                val hourText = "${m.hours}h"
                val textStyle = if (m.isAlert) redHourStyle else hourStyle
                val hw = measurer.measure(hourText, textStyle).size.width
                val labelY = barTop - 13.dp.toPx()

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
