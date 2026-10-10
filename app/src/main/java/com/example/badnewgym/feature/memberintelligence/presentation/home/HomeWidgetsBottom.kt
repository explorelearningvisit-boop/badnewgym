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
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricHistory
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricKey
import com.example.badnewgym.feature.memberintelligence.domain.model.ProgressVerdict
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutMonth

// ------------------------------------------------------------------ WORKOUT

/**
 * Ultra-compact workout card. Critical only: 7-day bars with exact minutes,
 * swipeable weeks, one-line month scope summary. Types, muscles and trends
 * live in the module sheet.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutWidget(
    month: WorkoutMonth?,
    weekIndex: Int,
    onWeekChange: (Int) -> Unit,
    onOpen: () -> Unit,
    range: WorkoutRange = WorkoutRange.D7,
    onRangeChange: (WorkoutRange) -> Unit = {}
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
    // Range-scoped days + totals: the summary always describes the visible
    // selector range, never a hidden month.
    val rangeDays = remember(month, range) { WorkoutAnalytics.rangeDays(month, range.days) }
    val rangeSessions = remember(month, range) {
        val newest = month.sorted().lastOrNull()?.epochDay ?: 0L
        val from = newest - range.days + 1
        month.sessions.filter { it.epochDay in from..newest }
    }
    val rangeSummary = remember(rangeSessions) { WorkoutAnalytics.summarize(rangeSessions) }

    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Workout", fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
            Spacer(Modifier.width(6.dp))
            WorkoutRange.entries.forEach { entry ->
                val selected = entry == range
                Box(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (selected) MiColors.Blue else MiColors.BlueSoft)
                        .clickable(onClick = { onRangeChange(entry) })
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        entry.short, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                        color = if (selected) Color.White else MiColors.Blue, maxLines = 1
                    )
                }
                Spacer(Modifier.width(3.dp))
            }
            Spacer(Modifier.weight(1f))
            Text(">", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary)
        }
        Spacer(Modifier.height(1.dp))
        if (range == WorkoutRange.D7) {
            Text(
                if (count > 1) "Wk ${pagerState.currentPage + 1}/$count \u00B7 swipe >" else "This week",
                fontSize = 9.5.sp, color = MiColors.TextSecondary, fontWeight = FontWeight.SemiBold, maxLines = 1
            )
            Spacer(Modifier.height(1.dp))
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { _ ->
                WeekChart(days = window?.days ?: emptyList())
            }
        } else {
            Text(
                "${WorkoutAnalytics.rangeLabel(month, range.days)} \u00B7 scroll >",
                fontSize = 9.5.sp, color = MiColors.TextSecondary, fontWeight = FontWeight.SemiBold, maxLines = 1
            )
            Spacer(Modifier.height(1.dp))
            RangeChart(days = rangeDays)
        }
        Spacer(Modifier.height(2.dp))
        Text(
            "${rangeSummary.activeDays} sess \u00B7 ${rangeSummary.totalMinutes} min \u00B7 " +
                "avg ${rangeSummary.averageMinutes} \u00B7 peak ${rangeSummary.peak?.minutes ?: "-"} \u00B7 " +
                "last ${range.days}d",
            fontSize = 9.5.sp, color = MiColors.TextSecondary, fontWeight = FontWeight.SemiBold, maxLines = 1
        )
    }
}

@Composable
private fun WeekChart(days: List<WorkoutAnalytics.WorkoutDay>) {
    val max = (days.maxOfOrNull { it.minutes } ?: 1).coerceAtLeast(1)
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            days.forEach { day ->
                Text(
                    if (day.minutes > 0) "${day.minutes}" else "0",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (day.minutes > 0) MiColors.TextPrimary else MiColors.TextTertiary,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(1.dp))
        Row(
            Modifier.fillMaxWidth().height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            days.forEach { day ->
                Box(
                    Modifier.weight(1f).padding(horizontal = 7.dp),
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
                            .width(20.dp)
                            .height(
                                if (day.minutes > 0) (7 + (day.minutes.toFloat() / max * 23)).dp else 5.dp
                            )
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .then(barFill)
                    )
                }
            }
        }
        Spacer(Modifier.height(1.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            days.forEach { day ->
                Text(
                    day.weekdayShort.take(3),
                    fontSize = 7.5.sp,
                    color = MiColors.TextSecondary,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Horizontally scrollable bar strip covering the whole selected range.
 * Starts at the most recent days; both ends are reachable by swipe.
 */
@Composable
private fun RangeChart(days: List<WorkoutAnalytics.WorkoutDay>) {
    if (days.isEmpty()) {
        EmptyWidgetLine("No workout data")
        return
    }
    val max = (days.maxOfOrNull { it.minutes } ?: 1).coerceAtLeast(1)
    val listState = androidx.compose.foundation.lazy.rememberLazyListState(
        initialFirstVisibleItemIndex = maxOf(0, days.size - 7)
    )
    androidx.compose.foundation.lazy.LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth().height(64.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(days.size) { i ->
            val day = days[i]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(34.dp)
            ) {
                Text(
                    if (day.minutes > 0) "${day.minutes}" else "0",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (day.minutes > 0) MiColors.TextPrimary else MiColors.TextTertiary,
                    maxLines = 1
                )
                Spacer(Modifier.height(1.dp))
                val miniFill: Modifier = if (day.minutes > 0) {
                    Modifier.background(
                        Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF2563EB)))
                    )
                } else {
                    Modifier.background(MiColors.Border)
                }
                Box(
                    Modifier
                        .width(20.dp)
                        .height(
                            if (day.minutes > 0) (7 + (day.minutes.toFloat() / max * 23)).dp else 5.dp
                        )
                        .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                        .then(miniFill),
                    contentAlignment = Alignment.BottomCenter
                ) {}
                Spacer(Modifier.height(1.dp))
                Text("${day.dayOfMonth}", fontSize = 7.5.sp, color = MiColors.TextSecondary, maxLines = 1)
            }
        }
    }
}

// ------------------------------------------------------------------ BODY

/**
 * Body widget slot on Home.
 *
 * The card itself is delegated to the Body Progress system so the compact
 * summary and the analytics screen always read from the same aggregation.
 * Muscle selection, ranges and consent live in the full screen.
 */
@Composable
fun BodyProgressWidget(
    goals: List<BodyGoal>,
    histories: List<MetricHistory>,
    onOpen: () -> Unit,
    summary: com.example.badnewgym.feature.bodyprogress.domain.BodyProgressSummary? = null,
    singleRow: Boolean = false
) {
    if (summary != null) {
        com.example.badnewgym.feature.bodyprogress.presentation.BodyProgressHomeWidget(
            summary = summary,
            onOpenDetails = onOpen
        )
        return
    }

    // Legacy path: goal-only records from Member Intelligence (kept so the card
    // still renders while the Body Progress dataset is loading).
    val visible = remember(goals) { goals.take(3) }
    if (visible.isEmpty()) {
        HomeCard {
            WidgetTitle("Body Progress")
            Spacer(Modifier.height(6.dp))
            EmptyWidgetLine("No recent body measurements")
        }
        return
    }
    val verdicts = remember(goals) { visible.associate { it.id to BodyGoalAnalytics.verdict(it) } }
    val improving = verdicts.values.count { it == ProgressVerdict.ON_TRACK || it == ProgressVerdict.IMPROVING }
    val onTrack = visible.isNotEmpty() && verdicts.values.all { it == ProgressVerdict.ON_TRACK }

    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Body", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
            Spacer(Modifier.width(4.dp))
            val goalName = visible.firstOrNull()?.kind?.name
                ?.lowercase()?.replaceFirstChar { it.uppercase() }?.replace('_', ' ')
            val rangeLabel = remember(histories) { bodyRangeLabel(histories) }
            Text(
                listOfNotNull(goalName, rangeLabel).joinToString(" \u00B7 "),
                fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MiColors.Purple,
                maxLines = 1, modifier = Modifier.weight(1f)
            )
            Pill(
                when {
                    onTrack -> "On Track"
                    improving == visible.size -> "Improving"
                    else -> "$improving/${visible.size}"
                },
                when {
                    onTrack -> Color(0xFF22C55E)
                    improving == visible.size -> Color(0xFF2F9E44)
                    else -> Color(0xFFE59A1A)
                },
                Color.White, 9
            )
            Spacer(Modifier.width(4.dp))
            Text(">", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MiColors.EmeraldDark)
        }
        Spacer(Modifier.height(3.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            visible.forEach { goal ->
                val verdict = verdicts[goal.id] ?: ProgressVerdict.UNKNOWN
                val good = verdict == ProgressVerdict.ON_TRACK || verdict == ProgressVerdict.IMPROVING
                val delta = goal.currentValue - goal.startValue
                val unit = goal.primaryMetric.unit.ifBlank { "" }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        metricLabel(goal.primaryMetric),
                        fontSize = 10.5.sp, color = MiColors.TextSecondary,
                        modifier = Modifier.weight(1f), maxLines = 1
                    )
                    Text(
                        "${formatOne(goal.startValue)}\u2192${formatOne(goal.currentValue)}",
                        fontSize = 11.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary,
                        maxLines = 1
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        (if (delta >= 0) "+" else "") + formatOne(delta) + (if (unit.isNotBlank()) " $unit" else ""),
                        fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold,
                        color = if (good) Color(0xFF16A34A) else Color(0xFFE54848),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/** Full body detail for the module sheet: sparklines, targets, verdicts. */
@Composable
fun BodyDetailContent(goals: List<BodyGoal>, histories: List<MetricHistory>) {
    val visible = goals.take(4)
    if (visible.isEmpty()) {
        HomeCard { EmptyWidgetLine("No recent body measurements") }
        return
    }
    val improving = visible.count {
        val v = BodyGoalAnalytics.verdict(it)
        v == ProgressVerdict.ON_TRACK || v == ProgressVerdict.IMPROVING
    }
    val onTrack = visible.all { BodyGoalAnalytics.verdict(it) == ProgressVerdict.ON_TRACK }
    HomeCard {
        WidgetTitle(
            "Body Progress",
            visible.firstOrNull()?.kind?.name?.lowercase()?.replaceFirstChar { it.uppercase() }
                ?.replace('_', ' ')?.let { "($it)" },
            MiColors.Purple
        )
        Spacer(Modifier.height(6.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            visible.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    row.forEach { goal ->
                        val history = histories.firstOrNull { it.metric == goal.primaryMetric }
                        MetricFullCard(goal, history, Modifier.weight(1f))
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
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
            when {
                onTrack -> Pill("On Track", Color(0xFF22C55E), Color.White, 10)
                improving == visible.size -> Pill("Improving", Color(0xFF2F9E44), Color.White, 10)
                else -> Pill("$improving / ${visible.size} Improving", Color(0xFFE59A1A), Color.White, 10)
            }
        }
    }
}

@Composable
private fun MetricFullCard(goal: BodyGoal, history: MetricHistory?, modifier: Modifier = Modifier) {
    val verdict = remember(goal) { BodyGoalAnalytics.verdict(goal) }
    val good = verdict == ProgressVerdict.ON_TRACK || verdict == ProgressVerdict.IMPROVING
    val delta = goal.currentValue - goal.startValue
    val unit = goal.primaryMetric.unit.ifBlank { "" }
    val lineColor = when {
        good -> Color(0xFF22C55E)
        verdict == ProgressVerdict.REGRESSING || verdict == ProgressVerdict.OFF_TRACK -> Color(0xFFE54848)
        else -> Color(0xFF367FE8)
    }
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MiColors.SurfaceSunken)
            .padding(9.dp)
    ) {
        Text(metricLabel(goal.primaryMetric), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MiColors.TextSecondary, maxLines = 1)
        Spacer(Modifier.height(2.dp))
        Text(
            "${formatOne(goal.startValue)} \u2192 ${formatOne(goal.currentValue)} $unit".trim(),
            fontSize = 13.5.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
        )
        Text(
            (if (delta >= 0) "+" else "") + formatOne(delta) + (if (unit.isNotBlank()) " $unit" else ""),
            fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold,
            color = if (good) Color(0xFF16A34A) else Color(0xFFE54848), maxLines = 1
        )
        Spacer(Modifier.height(2.dp))
        Sparkline(
            values = history?.ordered()?.map { it.value } ?: listOf(goal.startValue, goal.currentValue),
            lineColor = lineColor
        )
        Text(
            "Target ${formatOne(goal.targetValue)} $unit".trim(),
            fontSize = 9.5.sp, color = MiColors.TextTertiary, maxLines = 1
        )
        Text(
            if (good) "GOOD" else verdict.name.lowercase().replaceFirstChar { it.uppercase() }.replace('_', ' '),
            fontSize = 9.5.sp, fontWeight = FontWeight.Black,
            color = if (good) Color(0xFF16A34A) else MiColors.TextSecondary, maxLines = 1
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
