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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import com.example.badnewgym.feature.memberintelligence.domain.analytics.BodyGoalAnalytics
import com.example.badnewgym.feature.memberintelligence.domain.analytics.WorkoutAnalytics
import com.example.badnewgym.feature.memberintelligence.domain.analytics.WorkoutBand
import com.example.badnewgym.feature.memberintelligence.domain.analytics.WorkoutIntensity
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
    range: WorkoutRange = WorkoutRange.THIS_MONTH,
    onRangeChange: (WorkoutRange) -> Unit = {},
    workoutYearMonth: java.time.YearMonth = java.time.YearMonth.now(),
    onWorkoutMonthChange: (java.time.YearMonth) -> Unit = {}
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
    // selector range, never a hidden month. Month mode covers every calendar
    // day of the selected month instead of a trailing N-day window.
    val isMonth = range.isMonth
    val monthDays = remember(month, workoutYearMonth, isMonth) {
        if (isMonth && month != null) WorkoutAnalytics.monthDays(month, workoutYearMonth) else emptyList()
    }
    val rangeDays = remember(month, range) {
        if (isMonth) emptyList() else WorkoutAnalytics.rangeDays(month, range.days)
    }
    val rangeSessions = remember(month, range) {
        if (isMonth) emptyList() else {
            val newest = month.sorted().lastOrNull()?.epochDay ?: 0L
            val from = newest - range.days + 1
            month.sessions.filter { it.epochDay in from..newest }
        }
    }
    val chartDays = if (isMonth) monthDays else rangeDays
    val rangeSummary = remember(rangeSessions, monthDays, isMonth) {
        if (isMonth) WorkoutAnalytics.summarizeDays(monthDays)
        else WorkoutAnalytics.summarize(rangeSessions)
    }
    val monthTitle = remember(workoutYearMonth) {
        workoutYearMonth.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() } +
            " " + workoutYearMonth.year
    }

    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(28.dp).clip(RoundedCornerShape(9.dp)).background(MiColors.Blue),
                contentAlignment = Alignment.Center
            ) { Text("W", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White) }
            Spacer(Modifier.width(6.dp))
            Text(
                if (isMonth) "Workout ($monthTitle)" else "Workout (Last ${range.days} Days)",
                fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1
            )
            Spacer(Modifier.weight(1f))
            ViewAllChip(onOpen, small = true)
        }
        Spacer(Modifier.height(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            listOf(WorkoutRange.THIS_MONTH, WorkoutRange.D7, WorkoutRange.D15, WorkoutRange.D30).forEach { entry ->
                val selected = entry == range
                Box(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (selected) MiColors.Blue else MiColors.BlueSoft)
                        .clickable(onClick = { onRangeChange(entry) })
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        entry.short, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        color = if (selected) Color.White else MiColors.Blue, maxLines = 1
                    )
                }
                Spacer(Modifier.width(4.dp))
            }
            Spacer(Modifier.weight(1f))
            if (isMonth) {
                val nowYm = remember { java.time.YearMonth.now() }
                MonthNavArrows(workoutYearMonth, nowYm, onWorkoutMonthChange)
            } else {
                Text(">", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary,
                    modifier = Modifier.clickable(onClick = onOpen))
            }
        }
        Spacer(Modifier.height(2.dp))
        if (isMonth) {
            val weeks = remember(month, workoutYearMonth) {
                WorkoutAnalytics.weeksOfMonth(month, workoutYearMonth)
            }
            val wow = remember(month, workoutYearMonth) {
                WorkoutAnalytics.weekOverWeek(month, workoutYearMonth)
            }
            if (weeks.size >= 2) {
                WeeklyTrendRow(weeks, wow)
            }
        }
        if (range == WorkoutRange.D7) {
            Text(
                if (count > 1) "Wk ${pagerState.currentPage + 1}/$count \u00B7 swipe >" else "This week",
                fontSize = 9.sp, color = MiColors.TextSecondary, fontWeight = FontWeight.SemiBold, maxLines = 1
            )
            Spacer(Modifier.height(1.dp))
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { _ ->
                WeekChart(days = window?.days ?: emptyList())
            }
        } else if (isMonth) {
            // The month view must show the complete month, so every day of the
            // selected calendar month is on screen at once - no hidden paging.
            MonthChart(days = chartDays)
        } else {
            RangeChart(days = chartDays)
        }
        if (isMonth) {
            Spacer(Modifier.height(3.dp))
            IntensityLegend(days = chartDays)
        }
        Spacer(Modifier.height(3.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            StatChip("Sessions", "${rangeSummary.activeDays}", MiColors.TextPrimary, Modifier.weight(1f), dense = true)
            StatChip("Total", "${rangeSummary.totalMinutes}m", MiColors.TextPrimary, Modifier.weight(1f), dense = true)
            StatChip("Avg", "${rangeSummary.averageMinutes}m", MiColors.TextPrimary, Modifier.weight(1f), dense = true)
            StatChip("Peak", "${rangeSummary.peak?.minutes ?: 0}m", MiColors.TextPrimary, Modifier.weight(1f), dense = true)
        }
    }
}

/**
 * Bar fill encodes the recorded session, documented in the chart subtitle:
 * blue = strength, green = cardio, gray = no session. Color never invents a
 * workout; a gray stub is an explicit "no record" state.
 */
private fun barFillFor(day: WorkoutAnalytics.WorkoutDay): Modifier = when {
    day.minutes <= 0 -> Modifier.background(MiColors.Border)
    day.cardio -> Modifier.background(
        Brush.verticalGradient(listOf(Color(0xFF4ADE80), Color(0xFF16A34A)))
    )
    else -> Modifier.background(
        Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF2563EB)))
    )
}

@Composable
private fun WeekChart(days: List<WorkoutAnalytics.WorkoutDay>) {
    val max = (days.maxOfOrNull { it.minutes } ?: 1).coerceAtLeast(1)
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            days.forEach { day ->
                Text(
                    if (day.minutes > 0) "${day.minutes}" else "0",
                    fontSize = 9.sp,
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
            Modifier.fillMaxWidth().height(34.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            days.forEach { day ->
                Box(
                    Modifier.weight(1f).padding(horizontal = 7.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        Modifier
                            .width(20.dp)
                            .height(
                                if (day.minutes > 0) (7 + (day.minutes.toFloat() / max * 23)).dp else 5.dp
                            )
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .then(barFillFor(day))
                    )
                }
            }
        }
        Spacer(Modifier.height(1.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            days.forEach { day ->
                Text(
                    day.weekdayShort.take(3),
                    fontSize = 9.sp,
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
private fun MonthChart(days: List<WorkoutAnalytics.WorkoutDay>) {
    if (days.isEmpty()) {
        EmptyWidgetLine("No workout data")
        return
    }
    val max = (days.maxOfOrNull { it.minutes } ?: 1).coerceAtLeast(1)
    val labelEvery = if (days.size > 20) 3 else 2
    Row(
        Modifier.fillMaxWidth().height(58.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEachIndexed { i, day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        contentDescription = "${day.dayOfMonth} ${day.monthShort}: " +
                            if (day.minutes > 0) "${day.minutes} minutes" else "no workout"
                    }
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(if (day.minutes > 0) (4 + (day.minutes.toFloat() / max * 34)).dp else 3.dp)
                        .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                        .then(barFillFor(day))
                )
                Spacer(Modifier.height(1.dp))
                Text(
                    if (i % labelEvery == 0 || i == days.lastIndex) "${day.dayOfMonth}" else "",
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = MiColors.TextTertiary,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Duration-band legend for the month view. Each band is a real count of days,
 * so the row reconciles with the bars above it.
 */
@Composable
private fun IntensityLegend(days: List<WorkoutAnalytics.WorkoutDay>) {
    val counts = remember(days) { WorkoutIntensity.counts(days) }
    if (counts.values.all { it == 0 }) return
    data class BandStyle(val label: String, val color: Color, val band: WorkoutBand)
    val styles = listOf(
        BandStyle("High (${WorkoutIntensity.HIGH_MIN}m+)", Color(0xFF16A34A), WorkoutBand.HIGH),
        BandStyle(
            "Med (${WorkoutIntensity.MEDIUM_MIN}-${WorkoutIntensity.HIGH_MIN - 1}m)",
            Color(0xFF2563EB), WorkoutBand.MEDIUM
        ),
        BandStyle("Low (<${WorkoutIntensity.MEDIUM_MIN}m)", Color(0xFFE59A1A), WorkoutBand.LOW),
        BandStyle("No workout", MiColors.Neutral, WorkoutBand.NONE)
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        styles.forEach { s ->
            val n = counts[s.band] ?: 0
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(7.dp).clip(RoundedCornerShape(2.dp)).background(s.color))
                Spacer(Modifier.width(3.dp))
                Column {
                    Text(
                        s.label,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        color = MiColors.TextSecondary,
                        maxLines = 1
                    )
                    Text(
                        "$n day${if (n == 1) "" else "s"}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = MiColors.TextPrimary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun RangeChart(days: List<WorkoutAnalytics.WorkoutDay>) {
    if (days.isEmpty()) {
        EmptyWidgetLine("No workout data")
        return
    }
    val max = (days.maxOfOrNull { it.minutes } ?: 1).coerceAtLeast(1)
    // Open on the most recent recorded training, not on empty future days:
    // a month view would otherwise greet the member with a week of zeros.
    val lastActive = days.indexOfLast { it.minutes > 0 }
    val listState = androidx.compose.foundation.lazy.rememberLazyListState(
        initialFirstVisibleItemIndex = maxOf(0, (if (lastActive >= 0) lastActive else days.size - 1) - 6)
    )
    androidx.compose.foundation.lazy.LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(days.size) { i ->
            val day = days[i]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(36.dp)
            ) {
                Text(
                    if (day.minutes > 0) "${day.minutes}" else "0",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (day.minutes > 0) MiColors.TextPrimary else MiColors.TextTertiary,
                    maxLines = 1
                )
                Spacer(Modifier.height(1.dp))
                Box(
                    Modifier
                        .width(20.dp)
                        .height(
                            if (day.minutes > 0) (4 + (day.minutes.toFloat() / max * 9)).dp else 4.dp
                        )
                        .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                        .then(barFillFor(day)),
                    contentAlignment = Alignment.BottomCenter
                ) {}
                Spacer(Modifier.height(1.dp))
                Text("${day.dayOfMonth}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MiColors.TextSecondary, maxLines = 1)
                Text(day.monthShort, fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1)
            }
        }
    }
}

/**
 * Compact weekly-volume trend beside the full-month daily chart.
 *
 * Every bar is the recorded minutes of one Monday-start week of the selected
 * month, so the strip always describes the same period as the chart beneath
 * it. The right-hand figure is the latest recorded week against the week
 * before it; it is omitted entirely when there is no honest comparator.
 */
@Composable
private fun WeeklyTrendRow(
    weeks: List<WorkoutAnalytics.WeekBucket>,
    delta: WorkoutAnalytics.WeekDelta?
) {
    val max = (weeks.maxOfOrNull { it.totalMinutes } ?: 1).coerceAtLeast(1)
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            "Weekly", fontSize = 9.sp, fontWeight = FontWeight.Bold,
            color = MiColors.TextSecondary, maxLines = 1
        )
        Spacer(Modifier.width(6.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.semantics {
                contentDescription = "Weekly recorded minutes: " +
                    weeks.joinToString(", ") { "${it.label} ${it.totalMinutes}" }
            }
        ) {
            weeks.forEach { w ->
                Box(
                    Modifier
                        .width(12.dp)
                        .height(
                            if (w.totalMinutes > 0) (4 + (w.totalMinutes.toFloat() / max * 14)).dp
                            else 3.dp
                        )
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(if (w.totalMinutes > 0) MiColors.Emerald else MiColors.Border)
                )
            }
        }
        Spacer(Modifier.weight(1f))
        if (delta != null) {
            val up = delta.deltaPercent > 0
            val flat = delta.deltaPercent == 0
            Text(
                (if (flat) "\u25AC flat" else if (up) "\u25B2 +${delta.deltaPercent}%" else "\u25BC ${delta.deltaPercent}%") +
                    " wk/wk",
                fontSize = 9.sp, fontWeight = FontWeight.Black,
                color = if (flat) MiColors.TextTertiary else if (up) MiColors.EmeraldDark else MiColors.Red,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun MonthNavArrows(
    selected: java.time.YearMonth,
    now: java.time.YearMonth,
    onChange: (java.time.YearMonth) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MonthArrowMini("<", enabled = true) { onChange(selected.minusMonths(1)) }
        Spacer(Modifier.width(4.dp))
        MonthArrowMini(">", enabled = selected < now) {
            if (selected < now) onChange(selected.plusMonths(1))
        }
    }
}

@Composable
private fun MonthArrowMini(label: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (enabled) MiColors.BlueSoft else MiColors.SurfaceSunken)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Black, color = if (enabled) MiColors.Blue else MiColors.TextTertiary)
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

    // Dataset still loading: an honest loading state, never stale goal rows.
    HomeCard {
        WidgetTitle("Body Progress")
        Spacer(Modifier.height(6.dp))
        EmptyWidgetLine("Loading body progress\u2026")
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
