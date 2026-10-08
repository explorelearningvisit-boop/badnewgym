package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import com.example.badnewgym.feature.memberintelligence.domain.analytics.BodyGoalAnalytics
import com.example.badnewgym.feature.memberintelligence.domain.analytics.WorkoutAnalytics
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyGoal
import com.example.badnewgym.feature.memberintelligence.domain.model.GoalDirection
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricHistory
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricKey
import com.example.badnewgym.feature.memberintelligence.domain.model.ProgressVerdict
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutMonth

// ------------------------------------------------------------------ WORKOUT

/**
 * "HOW DID THE MEMBER TRAIN?" Sessions, duration, consistency, volume.
 * No attendance status here. Exact minutes above every bar.
 * Swipe pages across real calendar weeks of the 30-day dataset.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutWidget(
    month: WorkoutMonth?,
    weekIndex: Int,
    onWeekChange: (Int) -> Unit,
    onOpen: () -> Unit
) {
    if (month == null || month.sessions.isEmpty()) {
        HomeCard {
            WidgetTitle("Workout", "(Last 7 Days)")
            Spacer(Modifier.height(6.dp))
            EmptyWidgetLine("No workout data")
        }
        return
    }
    val count = remember(month) { WorkoutAnalytics.windowCount(month) }
    val page = weekIndex.coerceIn(0, count - 1)
    val pagerState = rememberPagerState(initialPage = page.coerceIn(0, count - 1), pageCount = { count })
    LaunchedEffect(page) {
        if (pagerState.currentPage != page) pagerState.scrollToPage(page)
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { onWeekChange(it) }
    }
    val window = remember(month, pagerState.currentPage) {
        WorkoutAnalytics.window(month, pagerState.currentPage)
    }
    val summary = remember(month) { WorkoutAnalytics.summary(month) }

    HomeCard(onClick = onOpen) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            WidgetTitle("Workout", "(Last 7 Days)")
        }
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (count > 1) "Week ${pagerState.currentPage + 1} of $count - swipe >" else "This week",
                fontSize = 11.sp, color = MiColors.TextSecondary, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            ViewAllChip(onOpen)
        }
        Spacer(Modifier.height(4.dp))
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { _ ->
            val days = window?.days ?: emptyList()
            val max = (days.maxOfOrNull { it.minutes } ?: 1).coerceAtLeast(1)
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    days.forEach { day ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                if (day.minutes > 0) "${day.minutes}" else "0",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (day.minutes > 0) MiColors.TextPrimary else MiColors.TextTertiary,
                                maxLines = 1
                            )
                            Text("min", fontSize = 8.5.sp, color = MiColors.TextTertiary, maxLines = 1)
                        }
                    }
                }
                Spacer(Modifier.height(2.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(88.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    days.forEach { day ->
                        Box(
                            Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            val barFill: Modifier = if (day.minutes > 0) {
                                Modifier.background(
                                    Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF2563EB)))
                                )
                            } else {
                                Modifier.background(MiColors.Border)
                            }
                            Box(
                                Modifier
                                    .width(26.dp)
                                    .height(
                                        if (day.minutes > 0) (14 + (day.minutes.toFloat() / max * 66)).dp else 8.dp
                                    )
                                    .clip(RoundedCornerShape(topStart = 7.dp, topEnd = 7.dp))
                                    .then(barFill)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(3.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    days.forEach { day ->
                        Text(
                            day.weekdayShort.take(3),
                            fontSize = 9.5.sp,
                            color = MiColors.TextSecondary,
                            maxLines = 1,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            StatChip("Sessions", "${summary.activeDays}", modifier = Modifier.weight(1f), dense = true)
            StatChip("Total", "${summary.totalMinutes} min", modifier = Modifier.weight(1f), dense = true)
            StatChip("Avg", "${summary.averageMinutes} min", modifier = Modifier.weight(1f), dense = true)
        }
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            StatChip("Peak", summary.peak?.let { "${it.minutes} min" } ?: "-", MiColors.EmeraldDark, Modifier.weight(1f), dense = true)
            StatChip(
                "Lowest",
                summary.lowest?.let { "${it.minutes} min" } ?: "No Session",
                MiColors.TextPrimary,
                Modifier.weight(1f),
                dense = true
            )
        }
    }
}

// ------------------------------------------------------------------ BODY

/**
 * Goal-driven body progress. No fake 0-100% ring: each metric shows
 * start -> current with a real sparkline, and GOOD / Needs Attention is
 * decided by the goal direction, never by assuming up = good.
 */
@Composable
fun BodyProgressWidget(
    goals: List<BodyGoal>,
    histories: List<MetricHistory>,
    onOpen: () -> Unit,
    singleRow: Boolean = false
) {
    val visible = remember(goals) { goals.take(if (singleRow) 3 else 4) }
    if (visible.isEmpty()) {
        HomeCard {
            WidgetTitle("Body Progress")
            Spacer(Modifier.height(6.dp))
            EmptyWidgetLine("No recent body measurements")
        }
        return
    }
    val improving = remember(goals) {
        goals.count {
            val v = BodyGoalAnalytics.verdict(it)
            v == ProgressVerdict.ON_TRACK || v == ProgressVerdict.IMPROVING
        }
    }
    val headlineGoal = remember(goals) { goals.firstOrNull()?.kind?.name }
    HomeCard(onClick = onOpen) {
        WidgetTitle(
            "Body Progress",
            headlineGoal?.lowercase()?.replaceFirstChar { it.uppercase() }?.replace('_', ' ')
                ?.let { "($it)" },
            MiColors.Purple,
            trailing = { ViewAllChip(onOpen) }
        )
        Spacer(Modifier.height(6.dp))
        if (singleRow) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                visible.forEach { goal ->
                    val history = histories.firstOrNull { it.metric == goal.primaryMetric }
                    MetricCard(goal, history, Modifier.weight(1f), compact = true)
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                visible.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEach { goal ->
                            val history = histories.firstOrNull { it.metric == goal.primaryMetric }
                            MetricCard(goal, history, Modifier.weight(1f))
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            val first = visible.first()
            Pill(
                "Goal: ${first.kind.name.lowercase().replaceFirstChar { it.uppercase() }.replace('_', ' ')}",
                MiColors.PurpleSoft, MiColors.Purple, 10
            )
            val allGood = improving == visible.size
            Pill(
                if (allGood) "On Track" else "$improving / ${visible.size} Improving",
                if (allGood) Color(0xFF22C55E) else Color(0xFFE59A1A),
                Color.White, 10
            )
        }
    }
}

@Composable
private fun MetricCard(
    goal: BodyGoal,
    history: MetricHistory?,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val verdict = remember(goal) { BodyGoalAnalytics.verdict(goal) }
    val good = verdict == ProgressVerdict.ON_TRACK || verdict == ProgressVerdict.IMPROVING
    val delta = goal.currentValue - goal.startValue
    val unit = goal.primaryMetric.unit.ifBlank { "" }
    val deltaText = buildString {
        append(if (delta >= 0) "+" else "")
        append(formatOne(delta))
        if (unit.isNotBlank()) append(" $unit")
    }
    val lineColor = when {
        good -> Color(0xFF22C55E)
        verdict == ProgressVerdict.REGRESSING || verdict == ProgressVerdict.OFF_TRACK -> Color(0xFFE54848)
        else -> Color(0xFF367FE8)
    }
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MiColors.SurfaceSunken)
            .padding(if (compact) 7.dp else 9.dp)
    ) {
        Text(
            metricLabel(goal.primaryMetric),
            fontSize = if (compact) 10.sp else 11.sp, fontWeight = FontWeight.Bold, color = MiColors.TextSecondary, maxLines = 1
        )
        Spacer(Modifier.height(2.dp))
        Text(
            "${formatOne(goal.startValue)} \u2192 ${formatOne(goal.currentValue)} $unit".trim(),
            fontSize = if (compact) 12.sp else 13.5.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
        )
        Text(
            deltaText,
            fontSize = if (compact) 10.5.sp else 11.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (good) Color(0xFF16A34A) else Color(0xFFE54848),
            maxLines = 1
        )
        Spacer(Modifier.height(2.dp))
        Sparkline(
            values = history?.ordered()?.map { it.value } ?: listOf(goal.startValue, goal.currentValue),
            lineColor = lineColor,
            modifier = Modifier.height(if (compact) 22.dp else 30.dp).fillMaxWidth()
        )
        Text(
            if (good) "GOOD" else verdict.name.lowercase().replaceFirstChar { it.uppercase() }.replace('_', ' '),
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Black,
            color = if (good) Color(0xFF16A34A) else MiColors.TextSecondary,
            maxLines = 1
        )
    }
}

private fun metricLabel(key: MetricKey): String = when (key) {
    MetricKey.WEIGHT -> "Weight"
    MetricKey.BODY_FAT -> "Body Fat"
    MetricKey.MUSCLE -> "Muscle Mass"
    MetricKey.WAIST -> "Waist"
    MetricKey.CHEST -> "Chest"
    MetricKey.BMI -> "BMI"
    MetricKey.STRENGTH -> "Strength"
    MetricKey.FIVE_K_TIME -> "5K Time"
    MetricKey.ENDURANCE_DISTANCE -> "Endurance"
}

private fun formatOne(v: Double): String {
    val r = kotlin.math.round(v * 10) / 10.0
    return if (r == kotlin.math.floor(r)) r.toInt().toString() else r.toString()
}

@Suppress("unused")
private fun directionHint(direction: GoalDirection): String = when (direction) {
    GoalDirection.INCREASE -> "goal: gain"
    GoalDirection.DECREASE -> "goal: reduce"
    GoalDirection.HOLD -> "goal: hold"
}
