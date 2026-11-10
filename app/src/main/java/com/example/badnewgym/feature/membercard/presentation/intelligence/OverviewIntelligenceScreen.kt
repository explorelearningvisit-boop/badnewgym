package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendBar
import kotlin.math.roundToInt

/**
 * Overview Intelligence — the fifth tab. Everything on this screen is derived from the
 * same domain fields the four modules render, so it can never disagree with them.
 */
@Composable
fun OverviewIntelligenceScreen(
    data: MemberCardData,
    pass: BadGymMemberPass,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val metrics = remember(data, pass) { MemberOverviewMetrics.from(data, pass) }
    val scoreColor = when {
        metrics.fitnessScore >= 85 -> NeuPalette.green
        metrics.fitnessScore >= 70 -> NeuPalette.violet
        metrics.fitnessScore >= 55 -> NeuPalette.amber
        else -> NeuPalette.coral
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGap)
    ) {
        // ---------- Fitness score + the three inputs that build it ----------
        IntelligenceSectionCard(
            title = "Fitness Score",
            icon = Icons.Filled.AutoAwesome,
            subtitle = "Attendance + consistency + payment"
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGapLg)
            ) {
                ScoreGauge(score = metrics.fitnessScore, band = metrics.band, color = scoreColor)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGapSm)
                ) {
                    ScoreBar(
                        label = "Attendance",
                        percent = metrics.attendancePercent,
                        color = NeuPalette.green
                    )
                    ScoreBar(
                        label = "Consistency",
                        percent = metrics.workoutPercent,
                        color = NeuPalette.violet
                    )
                    ScoreBar(
                        label = "Payment",
                        percent = metrics.paymentPercent,
                        color = NeuPalette.amber
                    )
                }
            }
        }

        // ---------- Cross-module snapshot ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGapSm)
        ) {
            SnapshotTile(
                icon = Icons.Filled.CalendarMonth,
                label = "Attendance",
                value = "${metrics.attendancePercent}%",
                tint = NeuPalette.green,
                modifier = Modifier.weight(1f)
            )
            SnapshotTile(
                icon = Icons.Filled.FitnessCenter,
                label = "Avg / Day",
                value = "${metrics.avgMinutes}m",
                tint = NeuPalette.violet,
                modifier = Modifier.weight(1f)
            )
            SnapshotTile(
                icon = Icons.Filled.Straighten,
                label = "Weight",
                value = metrics.weight,
                tint = NeuPalette.violetDeep,
                modifier = Modifier.weight(1f)
            )
            SnapshotTile(
                icon = Icons.Filled.TrendingUp,
                label = "BMI",
                value = metrics.bmi,
                tint = NeuPalette.blue,
                modifier = Modifier.weight(1f)
            )
        }

        // ---------- Month activity strip (real bars from the domain model) ----------
        IntelligenceSectionCard(
            title = "Activity This Month",
            icon = Icons.Filled.BarChart,
            subtitle = "${metrics.activeDays} active days · ${metrics.avgMinutes} min avg"
        ) {
            ActivityStrip(bars = data.workoutTrend.bars)
        }

        // ---------- Next actions ----------
        IntelligenceSectionCard(
            title = "Next Actions",
            icon = Icons.Filled.Flag,
            subtitle = "Generated from this member's data"
        ) {
            metrics.actions.forEach { action ->
                ActionRow(action)
            }
        }

        // ---------- Derived coach insight ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .neuRaised(radius = IntelligenceDensity.radiusCard, depth = 5.dp, blur = 12.dp)
                .padding(horizontal = IntelligenceDensity.cardPaddingH, vertical = IntelligenceDensity.cardPaddingV),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGap)
        ) {
            Box(
                modifier = Modifier
                    .size(IntelligenceDensity.iconMedium)
                    .clip(RoundedCornerShape(IntelligenceDensity.radiusSmall))
                    .background(NeuPalette.violet),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(IntelligenceDensity.iconSmall)
                )
            }
            Text(
                text = metrics.insight,
                fontSize = IntelligenceTypography.sizeSmall,
                fontWeight = IntelligenceTypography.weightBody,
                color = NeuPalette.ink,
                lineHeight = 14.sp
            )
        }
    }
}

/** Donut gauge with the composite score in the middle. */
@Composable
private fun ScoreGauge(score: Int, band: String, color: Color) {
    Box(
        modifier = Modifier.size(96.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeW = 8.dp.toPx()
            val arcSize = size.minDimension - strokeW
            val topLeft = Offset(strokeW / 2f, strokeW / 2f)
            drawArc(
                color = NeuPalette.surfaceSunken,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(arcSize, arcSize),
                style = Stroke(strokeW, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(color, color.copy(alpha = 0.65f), color)),
                startAngle = -90f,
                sweepAngle = 360f * (score / 100f),
                useCenter = false,
                topLeft = topLeft,
                size = Size(arcSize, arcSize),
                style = Stroke(strokeW, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score",
                fontSize = IntelligenceTypography.sizeHero,
                fontWeight = IntelligenceTypography.weightValue,
                color = NeuPalette.ink
            )
            Text(
                text = band,
                fontSize = IntelligenceTypography.sizeMicro,
                fontWeight = IntelligenceTypography.weightValue,
                color = color
            )
        }
    }
}

/** Compact labelled progress line used for the three score inputs. */
@Composable
private fun ScoreBar(label: String, percent: Int, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = IntelligenceTypography.sizeMicro,
                fontWeight = IntelligenceTypography.weightBody,
                color = NeuPalette.inkSoft,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$percent%",
                fontSize = IntelligenceTypography.sizeSmall,
                fontWeight = IntelligenceTypography.weightValue,
                color = color
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(NeuPalette.surfaceSunken)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}

/** One quarter of the cross-module snapshot row. */
@Composable
private fun SnapshotTile(
    icon: ImageVector,
    label: String,
    value: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .neuRaised(radius = IntelligenceDensity.radiusChip, depth = 4.dp, blur = 10.dp)
            .padding(horizontal = 4.dp, vertical = IntelligenceDensity.widgetGap)
            .clip(RoundedCornerShape(IntelligenceDensity.radiusChip)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(IntelligenceDensity.iconSmall))
        Text(
            text = value,
            fontSize = IntelligenceTypography.sizeSmall,
            fontWeight = IntelligenceTypography.weightValue,
            color = NeuPalette.ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = label,
            fontSize = IntelligenceTypography.sizeMicro,
            fontWeight = IntelligenceTypography.weightBody,
            color = NeuPalette.muted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** One "do this next" row with a semantic tone. */
@Composable
private fun ActionRow(action: OverviewAction) {
    val (tint, wash) = when (action.tone) {
        OverviewTone.SUCCESS -> NeuPalette.green to NeuPalette.greenWash
        OverviewTone.WARNING -> NeuPalette.amber to NeuPalette.amberWash
        OverviewTone.DANGER -> NeuPalette.coral to NeuPalette.coralWash
        OverviewTone.ACCENT -> NeuPalette.violet to NeuPalette.violetWash
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGap)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(50))
                .background(tint)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = action.title,
                fontSize = IntelligenceTypography.sizeSmall,
                fontWeight = IntelligenceTypography.weightValue,
                color = NeuPalette.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = action.detail,
                fontSize = IntelligenceTypography.sizeMicro,
                fontWeight = IntelligenceTypography.weightBody,
                color = NeuPalette.inkSoft,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** 31-day micro bar strip built from the real workout bars. */
@Composable
private fun ActivityStrip(bars: List<WorkoutTrendBar>) {
    val peak = bars.maxOfOrNull { it.minutes }?.coerceAtLeast(1) ?: 1
    Column(verticalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGapSm)) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            if (bars.isEmpty()) return@Canvas
            val slot = size.width / bars.size
            val barW = (slot * 0.62f).coerceAtLeast(2f)
            val radius = androidx.compose.ui.geometry.CornerRadius(barW / 2f, barW / 2f)
            bars.forEachIndexed { index, bar ->
                val fraction = bar.minutes.toFloat() / peak
                val h = (size.height * fraction).coerceAtLeast(if (bar.minutes > 0) barW else barW * 0.6f)
                val left = index * slot + (slot - barW) / 2f
                drawRoundRect(
                    color = if (bar.minutes > 0) NeuPalette.violet else NeuPalette.surfaceSunken,
                    topLeft = Offset(left, size.height - h),
                    size = Size(barW, h),
                    cornerRadius = radius
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "1",
                fontSize = IntelligenceTypography.sizeMicro,
                fontWeight = IntelligenceTypography.weightBody,
                color = NeuPalette.muted
            )
            Text(
                text = "Peak ${peak} min",
                fontSize = IntelligenceTypography.sizeMicro,
                fontWeight = IntelligenceTypography.weightValue,
                color = NeuPalette.violetDeep
            )
            Text(
                text = "${bars.size}",
                fontSize = IntelligenceTypography.sizeMicro,
                fontWeight = IntelligenceTypography.weightBody,
                color = NeuPalette.muted
            )
        }
    }
}