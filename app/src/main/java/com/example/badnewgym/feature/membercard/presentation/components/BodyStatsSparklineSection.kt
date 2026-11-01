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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType

@Composable
fun BodyStatsSparklineSection(
    stats: List<BodyStatItem>,
    onDetailsClick: () -> Unit = {},
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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onDetailsClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF8B5CF6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Body Stats",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Details",
                    tint = MemberCardTokens.DarkNavy,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Micro-Cards with Sparklines
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stats.forEach { item ->
                    SparklineStatCard(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SparklineStatCard(
    item: BodyStatItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top Icon Badge
            StatIconBadge(type = item.type)

            Spacer(modifier = Modifier.height(6.dp))

            // Main Value
            Text(
                text = item.value,
                color = MemberCardTokens.DarkNavy,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            // Delta
            val deltaColor = if (item.type == BodyStatType.BODY_FAT) Color(0xFFEF4444) else Color(0xFF10B981)
            Text(
                text = item.delta,
                color = deltaColor,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Canvas Sparkline Wave
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                if (item.sparklinePoints.isNotEmpty()) {
                    val points = item.sparklinePoints
                    val stepX = size.width / (points.size - 1)
                    val path = Path()
                    val fillPath = Path()

                    val startX = 0f
                    val startY = size.height * (1f - points[0])
                    path.moveTo(startX, startY)
                    fillPath.moveTo(startX, size.height)
                    fillPath.lineTo(startX, startY)

                    for (i in 1 until points.size) {
                        val prevX = (i - 1) * stepX
                        val prevY = size.height * (1f - points[i - 1])
                        val curX = i * stepX
                        val curY = size.height * (1f - points[i])
                        val midX = (prevX + curX) / 2f

                        path.cubicTo(midX, prevY, midX, curY, curX, curY)
                        fillPath.cubicTo(midX, prevY, midX, curY, curX, curY)
                    }

                    fillPath.lineTo(size.width, size.height)
                    fillPath.close()

                    // Subtle gradient fill under wave
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(item.lineColor.copy(alpha = 0.25f), Color.Transparent),
                            startY = 0f,
                            endY = size.height
                        )
                    )

                    // Stroke wave line
                    drawPath(
                        path = path,
                        color = item.lineColor,
                        style = Stroke(width = 1.8.dp.toPx())
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Bottom Label
            Text(
                text = item.label,
                color = MemberCardTokens.SlateMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun StatIconBadge(type: BodyStatType) {
    when (type) {
        BodyStatType.WEIGHT -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF10B981)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(12.dp)) {
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(0f, 0f),
                        size = size,
                        cornerRadius = CornerRadius(2.dp.toPx()),
                        style = Stroke(1.2.dp.toPx())
                    )
                    drawCircle(color = Color.White, radius = 2.dp.toPx(), center = Offset(size.width / 2f, size.height * 0.4f))
                }
            }
        }
        BodyStatType.MUSCLE -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF8B5CF6)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(12.dp)) {
                    drawLine(color = Color.White, start = Offset(size.width * 0.2f, size.height * 0.2f), end = Offset(size.width * 0.2f, size.height * 0.8f), strokeWidth = 1.8.dp.toPx())
                    drawLine(color = Color.White, start = Offset(size.width * 0.15f, size.height * 0.5f), end = Offset(size.width * 0.85f, size.height * 0.5f), strokeWidth = 1.5.dp.toPx())
                    drawLine(color = Color.White, start = Offset(size.width * 0.8f, size.height * 0.2f), end = Offset(size.width * 0.8f, size.height * 0.8f), strokeWidth = 1.8.dp.toPx())
                }
            }
        }
        BodyStatType.BODY_FAT -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFEF4444)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "%", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black)
            }
        }
        BodyStatType.BMI -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF2563EB)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "BMI", color = Color.White, fontSize = 10.5.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}
