package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendData

@Composable
fun WorkoutTrendDetailedSection(
    data: WorkoutTrendData,
    onPrevMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Icon + "Workout Trend" + Month Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(14.dp)) {
                            val w = size.width
                            val h = size.height
                            drawLine(color = Color.White, start = Offset(w * 0.25f, h * 0.2f), end = Offset(w * 0.25f, h * 0.8f), strokeWidth = 2.dp.toPx())
                            drawLine(color = Color.White, start = Offset(w * 0.2f, h * 0.5f), end = Offset(w * 0.8f, h * 0.5f), strokeWidth = 1.5.dp.toPx())
                            drawLine(color = Color.White, start = Offset(w * 0.75f, h * 0.2f), end = Offset(w * 0.75f, h * 0.8f), strokeWidth = 2.dp.toPx())
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Workout Trend",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Month Picker: < October 2026 >
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Month",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(onClick = onPrevMonth)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = data.monthLabel,
                        color = Color(0xFF2563EB),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Month",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(onClick = onNextMonth)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 30-Day Multi-Color Intensity Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
            ) {
                // Y-Axis labels
                Column(
                    modifier = Modifier.matchParentSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "60m", color = MemberCardTokens.SlateMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text(text = "30m", color = MemberCardTokens.SlateMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text(text = "0m", color = MemberCardTokens.SlateMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }

                // Chart Canvas
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(start = 24.dp)
                ) {
                    val chartWidth = size.width
                    val chartHeight = size.height

                    val gridY60 = 2.dp.toPx()
                    val gridY30 = chartHeight * 0.5f
                    val gridY0 = chartHeight - 2.dp.toPx()

                    // Guide Lines
                    drawLine(color = Color(0xFFE2E8F0), start = Offset(0f, gridY60), end = Offset(chartWidth, gridY60), strokeWidth = 1.dp.toPx())
                    drawLine(color = Color(0xFFE2E8F0), start = Offset(0f, gridY30), end = Offset(chartWidth, gridY30), strokeWidth = 1.dp.toPx())
                    drawLine(color = Color(0xFFCBD5E1), start = Offset(0f, gridY0), end = Offset(chartWidth, gridY0), strokeWidth = 1.dp.toPx())

                    // Bars
                    val totalBars = data.bars.size
                    if (totalBars > 0) {
                        val step = chartWidth / totalBars
                        val barWidth = (step * 0.7f).coerceAtMost(6.5.dp.toPx()).coerceAtLeast(3.5f)

                        data.bars.forEachIndexed { index, entry ->
                            val barCenterX = (index + 0.5f) * step
                            val barLeft = barCenterX - barWidth / 2f
                            val maxMinutes = 60f
                            val barHeight = (entry.minutes / maxMinutes) * (chartHeight - 6.dp.toPx())
                            val barTop = gridY0 - barHeight

                            drawRoundRect(
                                color = entry.color,
                                topLeft = Offset(barLeft, barTop),
                                size = Size(barWidth, barHeight.coerceAtLeast(barWidth)),
                                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                            )
                        }
                    }
                }
            }

            // X-Axis Date Intervals (1, 5, 10, 15, 20, 25, 30)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("1", "5", "10", "15", "20", "25", "30").forEach { tick ->
                    Text(
                        text = tick,
                        color = MemberCardTokens.SlateMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3 Stat Highlights (Avg/Day, Peak, Lowest)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Avg / Day
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(12.dp)) {
                                drawCircle(color = Color.White, style = Stroke(1.2.dp.toPx()))
                                drawLine(color = Color.White, start = Offset(size.width / 2f, size.height / 2f), end = Offset(size.width / 2f, size.height * 0.25f), strokeWidth = 1.2.dp.toPx())
                                drawLine(color = Color.White, start = Offset(size.width / 2f, size.height / 2f), end = Offset(size.width * 0.75f, size.height / 2f), strokeWidth = 1.2.dp.toPx())
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = "${data.avgPerDayMinutes}m", color = MemberCardTokens.DarkNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Avg / Day", color = MemberCardTokens.SlateMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                // Peak
                Box(
                    modifier = Modifier
                        .weight(1.15f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(12.dp)) {
                                val w = size.width
                                val h = size.height
                                drawLine(color = Color.White, start = Offset(w * 0.2f, h * 0.8f), end = Offset(w * 0.8f, h * 0.2f), strokeWidth = 1.8.dp.toPx())
                                drawLine(color = Color.White, start = Offset(w * 0.45f, h * 0.2f), end = Offset(w * 0.8f, h * 0.2f), strokeWidth = 1.8.dp.toPx())
                                drawLine(color = Color.White, start = Offset(w * 0.8f, h * 0.55f), end = Offset(w * 0.8f, h * 0.2f), strokeWidth = 1.8.dp.toPx())
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = "${data.peakMinutes}m", color = MemberCardTokens.DarkNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Peak (${data.peakDate})", color = MemberCardTokens.SlateMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                        }
                    }
                }

                // Lowest
                Box(
                    modifier = Modifier
                        .weight(1.25f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(12.dp)) {
                                val w = size.width
                                val h = size.height
                                val path = Path().apply {
                                    moveTo(w / 2f, h * 0.85f)
                                    lineTo(w * 0.85f, h * 0.45f)
                                    lineTo(w * 0.60f, h * 0.45f)
                                    lineTo(w * 0.60f, h * 0.15f)
                                    lineTo(w * 0.40f, h * 0.15f)
                                    lineTo(w * 0.40f, h * 0.45f)
                                    lineTo(w * 0.15f, h * 0.45f)
                                    close()
                                }
                                drawPath(path, Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = "${data.lowestMinutes}m", color = MemberCardTokens.DarkNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Lowest (${data.lowestDate})", color = MemberCardTokens.SlateMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}
