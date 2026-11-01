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
fun BodyStatsSection(
    stats: List<BodyStatItem>,
    onDetailsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onDetailsClick)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MemberCardTokens.BrandEmerald),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
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
                contentDescription = "View Body Stats",
                tint = MemberCardTokens.DarkNavy,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 4 Metric Micro-Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stats.forEach { item ->
                BodyStatCard(
                    item = item,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BodyStatCard(
    item: BodyStatItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon Badge
            StatIconBadge(type = item.type)

            Spacer(modifier = Modifier.height(6.dp))

            // Main Value
            Text(
                text = item.value,
                color = MemberCardTokens.DarkNavy,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Delta or Status Subtext
            StatSubtext(item = item)
        }
    }
}

@Composable
private fun StatIconBadge(type: BodyStatType) {
    when (type) {
        BodyStatType.WEIGHT -> {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(Color(0xFF10B981)),
                contentAlignment = Alignment.Center
            ) {
                // Scale Icon
                Canvas(modifier = Modifier.size(14.dp)) {
                    val stroke = Stroke(width = 1.5.dp.toPx())
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(1.dp.toPx(), 1.dp.toPx()),
                        size = Size(size.width - 2.dp.toPx(), size.height - 2.dp.toPx()),
                        cornerRadius = CornerRadius(2.dp.toPx()),
                        style = stroke
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 2.5.dp.toPx(),
                        center = Offset(size.width / 2f, size.height * 0.45f),
                        style = stroke
                    )
                }
            }
        }
        BodyStatType.MUSCLE -> {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(Color(0xFF8B5CF6)),
                contentAlignment = Alignment.Center
            ) {
                // Barbell / Muscle Activity Icon
                Canvas(modifier = Modifier.size(14.dp)) {
                    val w = size.width
                    val h = size.height
                    // Left plate
                    drawLine(
                        color = Color.White,
                        start = Offset(w * 0.2f, h * 0.25f),
                        end = Offset(w * 0.2f, h * 0.75f),
                        strokeWidth = 2.dp.toPx()
                    )
                    // Bar
                    drawLine(
                        color = Color.White,
                        start = Offset(w * 0.15f, h * 0.5f),
                        end = Offset(w * 0.85f, h * 0.5f),
                        strokeWidth = 2.dp.toPx()
                    )
                    // Center grip
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(w * 0.4f, h * 0.2f),
                        size = Size(w * 0.2f, h * 0.6f),
                        cornerRadius = CornerRadius(1.dp.toPx())
                    )
                    // Right plate
                    drawLine(
                        color = Color.White,
                        start = Offset(w * 0.8f, h * 0.25f),
                        end = Offset(w * 0.8f, h * 0.75f),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
        }
        BodyStatType.BODY_FAT -> {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(Color(0xFFEF4444)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "%",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
        BodyStatType.BMI -> {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(Color(0xFF2563EB)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BMI",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
private fun StatSubtext(item: BodyStatItem) {
    when (item.type) {
        BodyStatType.WEIGHT -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "↓",
                    color = Color(0xFF10B981),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " kg ",
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "2.5",
                    color = Color(0xFF10B981),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        BodyStatType.MUSCLE -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "kg ",
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "↑ 1.4",
                    color = Color(0xFF10B981),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        BodyStatType.BODY_FAT -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "↓",
                    color = Color(0xFFEF4444),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "1.2",
                    color = Color(0xFFEF4444),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        BodyStatType.BMI -> {
            Text(
                text = "Normal",
                color = Color(0xFF10B981),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
