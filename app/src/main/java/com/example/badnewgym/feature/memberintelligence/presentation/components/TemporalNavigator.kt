package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.TemporalGranularity
import com.example.badnewgym.feature.memberintelligence.domain.model.TemporalRange
import java.util.Calendar

@Composable
fun TemporalNavigator(
    currentRange: TemporalRange,
    availableGranularities: List<TemporalGranularity> = listOf(
        TemporalGranularity.DAY,
        TemporalGranularity.WEEK,
        TemporalGranularity.MONTH,
        TemporalGranularity.YEAR
    ),
    isLive: Boolean = false,
    eventCount: Int? = null,
    onRangeChange: (TemporalRange) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colors.surfaceMuted.copy(alpha = 0.6f))
            .border(1.dp, colors.border.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Granularity Selector Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            availableGranularities.forEach { granularity ->
                val isSelected = currentRange.granularity == granularity
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) colors.accent else colors.surface)
                        .border(
                            1.dp,
                            if (isSelected) colors.accent else colors.border.copy(alpha = 0.4f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            val newRange = when (granularity) {
                                TemporalGranularity.LIVE -> currentRange.copy(granularity = granularity)
                                TemporalGranularity.DAY -> TemporalRange.forToday()
                                TemporalGranularity.WEEK -> TemporalRange.forCurrentWeek()
                                TemporalGranularity.MONTH -> TemporalRange.forCurrentMonth()
                                TemporalGranularity.YEAR -> TemporalRange.forCurrentYear()
                                else -> currentRange.copy(granularity = granularity)
                            }
                            onRangeChange(newRange)
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .semantics {
                            contentDescription = "Select ${granularity.displayName} time scale"
                        }
                ) {
                    Text(
                        text = granularity.displayName,
                        fontSize = 10.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) colors.textOnAccent else colors.textPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Live / Synced Status Pill
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isLive) colors.danger.copy(alpha = 0.15f) else colors.success.copy(alpha = 0.12f))
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.FiberManualRecord,
                    contentDescription = null,
                    tint = if (isLive) colors.danger else colors.success,
                    modifier = Modifier.size(7.dp)
                )
                Text(
                    text = if (isLive) "LIVE" else if (eventCount != null) "$eventCount events" else "SYNCED",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isLive) colors.danger else colors.success
                )
            }
        }

        // Stepper Navigation Row (Previous, Label, Next, Jump Today)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous Period Button
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(colors.surface)
                    .border(1.dp, colors.border.copy(alpha = 0.5f), CircleShape)
                    .clickable {
                        val prevRange = stepPeriod(currentRange, stepForward = false)
                        onRangeChange(prevRange)
                    }
                    .semantics { contentDescription = "Previous time period" },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = colors.textPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Current Period Label
            Text(
                text = currentRange.formatPeriodLabel(),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                modifier = Modifier.semantics {
                    contentDescription = "Current selected period: ${currentRange.formatPeriodLabel()}"
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Next Period Button
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(colors.surface)
                        .border(1.dp, colors.border.copy(alpha = 0.5f), CircleShape)
                        .clickable {
                            val nextRange = stepPeriod(currentRange, stepForward = true)
                            onRangeChange(nextRange)
                        }
                        .semantics { contentDescription = "Next time period" },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = colors.textPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }

                // Jump to Today
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colors.surface)
                        .border(1.dp, colors.border.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .clickable {
                            val todayRange = when (currentRange.granularity) {
                                TemporalGranularity.MONTH -> TemporalRange.forCurrentMonth()
                                TemporalGranularity.WEEK -> TemporalRange.forCurrentWeek()
                                TemporalGranularity.YEAR -> TemporalRange.forCurrentYear()
                                else -> TemporalRange.forToday()
                            }
                            onRangeChange(todayRange)
                        }
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                        .semantics { contentDescription = "Jump to Today" }
                ) {
                    Text(
                        text = "Today",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.accent
                    )
                }
            }
        }
    }
}

private fun stepPeriod(currentRange: TemporalRange, stepForward: Boolean): TemporalRange {
    val delta = if (stepForward) 1 else -1
    val cal = Calendar.getInstance().apply { timeInMillis = currentRange.startMillis }

    return when (currentRange.granularity) {
        TemporalGranularity.DAY, TemporalGranularity.SECOND, TemporalGranularity.MINUTE, TemporalGranularity.HOUR -> {
            cal.add(Calendar.DAY_OF_MONTH, delta)
            val start = cal.timeInMillis
            cal.add(Calendar.DAY_OF_MONTH, 1)
            val end = cal.timeInMillis - 1
            currentRange.copy(startMillis = start, endMillis = end)
        }
        TemporalGranularity.WEEK -> {
            cal.add(Calendar.WEEK_OF_YEAR, delta)
            val start = cal.timeInMillis
            cal.add(Calendar.DAY_OF_WEEK, 7)
            val end = cal.timeInMillis - 1
            currentRange.copy(startMillis = start, endMillis = end)
        }
        TemporalGranularity.MONTH -> {
            cal.add(Calendar.MONTH, delta)
            cal.set(Calendar.DAY_OF_MONTH, 1)
            val start = cal.timeInMillis
            cal.add(Calendar.MONTH, 1)
            val end = cal.timeInMillis - 1
            currentRange.copy(startMillis = start, endMillis = end)
        }
        TemporalGranularity.YEAR -> {
            cal.add(Calendar.YEAR, delta)
            cal.set(Calendar.DAY_OF_YEAR, 1)
            val start = cal.timeInMillis
            cal.add(Calendar.YEAR, 1)
            val end = cal.timeInMillis - 1
            currentRange.copy(startMillis = start, endMillis = end)
        }
        else -> currentRange
    }
}
