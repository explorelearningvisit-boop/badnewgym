package com.example.badnewgym.feature.bodyprogress.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.domain.BodyProgressAggregator
import com.example.badnewgym.feature.bodyprogress.domain.BodyProgressSummary
import com.example.badnewgym.feature.bodyprogress.domain.GoalVerdict
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange
import com.example.badnewgym.feature.bodyprogress.domain.analytics.ConsistencyAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EnduranceAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.MetricSeries
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.EquipmentCategory
import com.example.badnewgym.feature.bodyprogress.domain.model.GoalMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import com.example.badnewgym.feature.bodyprogress.domain.model.ViewerRole
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Body Progress dashboard.
 *
 * Layout follows the approved dashboard design (header + stat strip + muscle
 * focus + top exercises + equipment + trend + consistency + insight grid),
 * but every number is derived from stored records: nothing here is a
 * hardcoded mock value. A tile with no records renders an explicit empty
 * state instead of a plausible-looking number.
 *
 * Filters re-run the aggregation in the ViewModel ([summarize]), so a
 * filtered view can never disagree with the totals.
 */
@Composable
fun BodyProgressAnalyticsScreen(
    summarize: (BodyRange, BodyMetric?, MuscleGroup?) -> BodyProgressSummary,
    initialRange: BodyRange = BodyRange.D30,
    viewerRole: ViewerRole = ViewerRole.SELF,
    onBack: () -> Unit,
    /** Deep-link section for external entry points ("top", "mid", "bottom"). */
    initialSection: String = "top"
) {
    var range by remember { mutableStateOf(initialRange) }
    var muscle by remember { mutableStateOf<MuscleGroup?>(null) }

    val summary = remember(range, muscle) { summarize(range, null, muscle) }
    val listState: LazyListState = rememberLazyListState()
    LaunchedEffect(range) {
        listState.scrollToItem(
            when (initialSection) {
                "mid" -> 3
                "bottom" -> 5
                else -> 0
            }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MiColors.Page)
            // enableEdgeToEdge means the window draws under the system bars, so
            // the header has to consume the insets itself.
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        DashboardHeader(summary = summary, range = range, onRange = { range = it }, onBack = onBack)

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { StatStrip(summary) }

            item { MuscleFocusDashboardCard(summary, muscle) { muscle = if (muscle == it) null else it } }

            item { TopExercisesDashboardCard(summary) }

            item { EquipmentDashboardCard(summary) }

            item { ProgressTrendDashboardCard(summary) }

            item { ConsistencyDashboardCard(summary) }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.weight(1f)) { BestProgressCard(summary) }
                    Box(Modifier.weight(1f)) { RecentActivityCard(summary) }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.weight(1f)) { KeyInsightCard(summary) }
                    Box(Modifier.weight(1f)) { NextFocusCard(summary) }
                }
            }

            item { DataConfidenceCard(summary) }
        }
    }
}

// ------------------------------------------------------------------ header

@Composable
private fun DashboardHeader(
    summary: BodyProgressSummary,
    range: BodyRange,
    onRange: (BodyRange) -> Unit,
    onBack: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .background(MiColors.Surface)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MiColors.SurfaceSunken)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) { Text("<", fontSize = 15.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary) }
        Spacer(Modifier.width(8.dp))
        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color(0xFF7C3AED)),
            contentAlignment = Alignment.Center
        ) { Text("B", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White) }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text("Body Progress", fontSize = 17.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
            Text(
                summary.goal?.let { "Goal: ${it.title} \u00B7 ${range.label}" } ?: "No active goal \u00B7 ${range.label}",
                fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 1
            )
        }
        BodyPill(
            "\u2197 ${summary.goalVerdict.label}",
            verdictBg(summary.goalVerdict),
            if (summary.goalVerdict == GoalVerdict.INSUFFICIENT_DATA) MiColors.TextSecondary else Color.White,
            10
        )
        Spacer(Modifier.width(6.dp))
        Box {
            Box(
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MiColors.SurfaceSunken)
                    .clickable { menuOpen = true }
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Text("${range.label} \u02C5", fontSize = 12.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
            }
            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                BodyRange.entries.forEach { entry ->
                    DropdownMenuItem(
                        text = { Text(entry.label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold) },
                        onClick = { menuOpen = false; onRange(entry) }
                    )
                }
            }
        }
    }
}

private fun verdictBg(verdict: GoalVerdict): Color = when (verdict) {
    GoalVerdict.ON_TRACK -> Color(0xFF16A34A)
    GoalVerdict.IMPROVING -> Color(0xFF0891B2)
    GoalVerdict.STABLE -> Color(0xFFE59A1A)
    GoalVerdict.REGRESSING -> Color(0xFFE54848)
    GoalVerdict.INSUFFICIENT_DATA -> MiColors.Border
}

// ------------------------------------------------------------------ stat strip

/** Horizontal strip: goal ring + one tile per recorded domain. No tile is shown for a domain with no records. */
@Composable
private fun StatStrip(summary: BodyProgressSummary) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item { GoalRingCard(summary) }
        item { WeightTile(summary) }
        item { MuscleTile(summary) }
        item { BodyFatTile(summary) }
        item { StaminaTile(summary) }
        item { StrengthTile(summary) }
    }
}

@Composable
private fun StripCard(width: Int, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Column(
        Modifier
            .width(width.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MiColors.Surface)
            .padding(10.dp)
    ) { content() }
}

@Composable
private fun TileHead(icon: String, tint: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(30.dp).clip(RoundedCornerShape(10.dp)).background(tint),
            contentAlignment = Alignment.Center
        ) { Text(icon, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White, maxLines = 1) }
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MiColors.TextPrimary, maxLines = 1)
    }
}

@Composable
private fun GoalRingCard(summary: BodyProgressSummary) {
    StripCard(128) {
        Spacer(Modifier.height(2.dp))
        if (summary.goalPercent != null) {
            BodyDonut(
                percent = summary.goalPercent,
                centerLabel = "${summary.goalPercent}%",
                caption = "Goal",
                color = BodyChartColors.weight,
                modifier = Modifier.size(88.dp).align(Alignment.CenterHorizontally),
                semanticsText = "${summary.goal?.title ?: "Goal"} is ${summary.goalPercent} percent toward target"
            )
        } else {
            Column(
                Modifier
                    .size(88.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MiColors.SurfaceSunken)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No target", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary, maxLines = 1)
                Text("set", fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 1)
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            summary.goal?.title ?: "No active goal",
            fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MiColors.TextSecondary,
            maxLines = 1, modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun WeightTile(summary: BodyProgressSummary) {
    val series = summary.compositionSeries(BodyMetric.WEIGHT)
    StripCard(150) {
        TileHead("W", BodyChartColors.weight, "Weight")
        Spacer(Modifier.height(4.dp))
        if (series == null || series.latest == null) {
            TileEmpty("No weight records")
            return@StripCard
        }
        val latest = series.latest!!.value
        if (!series.hasHistory) {
            // A single reading has no delta: showing "0 kg" would imply a
            // measured flat trend that does not exist.
            Text("${trim(latest)} kg", fontSize = 21.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
            Text(
                "One reading \u00B7 ${shortDay(series.latest!!.day)}",
                fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1
            )
            return@StripCard
        }
        val first = series.earliest!!.value
        val delta = latest - first
        Text("${trim(latest)} kg", fontSize = 21.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
        Text(
            deltaText(delta, "kg"),
            fontSize = 12.sp, fontWeight = FontWeight.Bold,
            color = deltaColor(summary, GoalMetric.WEIGHT, latest, delta, upIsGood = true),
            maxLines = 1
        )
        Spacer(Modifier.height(4.dp))
        BodyLineChart(
            values = series.points.map { it.value },
            labels = series.points.map { shortDay(it.day) },
            color = BodyChartColors.weight,
            unit = series.metric.unit,
            modifier = Modifier.height(36.dp).fillMaxWidth(),
            showAxis = false
        )
    }
}

@Composable
private fun MuscleTile(summary: BodyProgressSummary) {
    val series = summary.compositionSeries(BodyMetric.MUSCLE_MASS)
    StripCard(150) {
        TileHead("M", BodyChartColors.muscle, "Muscle")
        Spacer(Modifier.height(4.dp))
        if (series == null || series.latest == null) {
            TileEmpty("No muscle records")
            return@StripCard
        }
        val latest = series.latest!!.value
        if (!series.hasHistory) {
            Text("${trim(latest)} kg", fontSize = 21.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
            Text(
                "One reading \u00B7 ${shortDay(series.latest!!.day)}",
                fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1
            )
            return@StripCard
        }
        val delta = latest - series.earliest!!.value
        Text("${trim(latest)} kg", fontSize = 21.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
        Text(
            deltaText(delta, "kg"),
            fontSize = 12.sp, fontWeight = FontWeight.Bold,
            color = deltaColor(summary, GoalMetric.MUSCLE_MASS, latest, delta, upIsGood = true),
            maxLines = 1
        )
        Spacer(Modifier.height(4.dp))
        BodyLineChart(
            values = series.points.map { it.value },
            labels = series.points.map { shortDay(it.day) },
            color = BodyChartColors.muscle,
            unit = series.metric.unit,
            modifier = Modifier.height(36.dp).fillMaxWidth(),
            showAxis = false
        )
    }
}

@Composable
private fun BodyFatTile(summary: BodyProgressSummary) {
    val series = summary.compositionSeries(BodyMetric.BODY_FAT)
    StripCard(150) {
        TileHead("F", BodyChartColors.bodyFat, "Body Fat")
        Spacer(Modifier.height(4.dp))
        if (series == null || series.latest == null) {
            TileEmpty("No body-fat records")
            return@StripCard
        }
        val latest = series.latest!!.value
        if (!series.hasHistory) {
            Text("${trim(latest)}%", fontSize = 21.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
            Text(
                "One reading \u00B7 ${shortDay(series.latest!!.day)}",
                fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1
            )
            return@StripCard
        }
        val delta = latest - series.earliest!!.value
        Text("${trim(latest)}%", fontSize = 21.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
        Text(
            deltaText(delta, "%"),
            fontSize = 12.sp, fontWeight = FontWeight.Bold,
            color = deltaColor(summary, GoalMetric.BODY_FAT, latest, delta, upIsGood = false),
            maxLines = 1
        )
        Spacer(Modifier.height(4.dp))
        BodyLineChart(
            values = series.points.map { it.value },
            labels = series.points.map { shortDay(it.day) },
            color = BodyChartColors.bodyFat,
            unit = series.metric.unit,
            modifier = Modifier.height(36.dp).fillMaxWidth(),
            showAxis = false
        )
    }
}

@Composable
private fun StaminaTile(summary: BodyProgressSummary) {
    val endurance = summary.endurance
    StripCard(150) {
        TileHead("S", BodyChartColors.endurance, "Stamina")
        Spacer(Modifier.height(4.dp))
        val latest = endurance.latestPace
        if (latest?.paceSecPerKm == null) {
            TileEmpty("No cardio records")
            return@StripCard
        }
        Text(EnduranceAnalytics.formatPace(latest.paceSecPerKm), fontSize = 21.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
        Text(
            "${trim(latest.distanceKm)} km latest",
            fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 1
        )
        val change = endurance.paceChangeSecPerKm
        if (change != null) {
            // A negative pace change means faster running: good.
            Text(
                "${formatSecAsMs(abs(change))} ${if (change < 0) "\u2193" else "\u2191"}",
                fontSize = 12.sp, fontWeight = FontWeight.Bold,
                color = if (change <= 0) Color(0xFF16A34A) else Color(0xFFE54848),
                maxLines = 1
            )
        }
        val pacePoints = endurance.records.mapNotNull { it.paceSecPerKm }
        if (pacePoints.size >= 2) {
            Spacer(Modifier.height(4.dp))
            BodyLineChart(
                values = pacePoints,
                labels = endurance.records.filter { it.paceSecPerKm != null }.map { shortDay(it.day) },
                color = BodyChartColors.endurance,
                unit = "s/km",
                modifier = Modifier.height(36.dp).fillMaxWidth(),
                showAxis = false
            )
        }
    }
}

@Composable
private fun StrengthTile(summary: BodyProgressSummary) {
    val prog = summary.strength
    StripCard(150) {
        TileHead("+", BodyChartColors.strength, "Strength")
        Spacer(Modifier.height(4.dp))
        if (prog == null) {
            val top = summary.topExercise?.topLoadKg
            if (top == null) {
                TileEmpty("No strength records")
            } else {
                Text("${trim(top)} kg", fontSize = 21.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
                Text("Top recorded load", fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 1)
            }
            return@StripCard
        }
        val base = prog.previous.estimated1RmKg
        val pct = if (base > 0) ((prog.current.estimated1RmKg - base) / base) * 100.0 else null
        Text(
            pct?.let { "${if (it >= 0) "+" else ""}${it.roundToInt()}%" } ?: "-",
            fontSize = 21.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
        )
        Text("e1RM change", fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 1)
        Text(
            "${trim(prog.previous.loadKg)}\u2192${trim(prog.current.loadKg)} kg",
            fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MiColors.TextSecondary, maxLines = 1
        )
        Spacer(Modifier.height(4.dp))
        BodyLineChart(
            values = listOf(prog.previous.estimated1RmKg, prog.current.estimated1RmKg),
            labels = listOf(shortDay(prog.previous.day), shortDay(prog.current.day)),
            color = BodyChartColors.strength,
            unit = "kg",
            modifier = Modifier.height(36.dp).fillMaxWidth(),
            showAxis = false
        )
    }
}

@Composable
private fun TileEmpty(message: String) {
    Text(message, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MiColors.TextTertiary, maxLines = 2)
    Spacer(Modifier.height(2.dp))
    Text("No records in range", fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1)
}

/** Absolute delta with unit and direction arrow, e.g. "+1.2 kg \u2191". */
private fun deltaText(delta: Double, unit: String): String {
    if (abs(delta) < 0.0001) return "0 $unit \u2192"
    val sign = if (delta >= 0) "+" else ""
    val arrow = if (delta >= 0) "\u2191" else "\u2193"
    return "$sign${trim(delta)} $unit $arrow"
}

/**
 * Delta colour honours the member's own goal: when the tile metric is the
 * goal metric, "good" means moving toward the target (rising weight is good
 * for muscle gain, bad for fat loss). Otherwise a neutral per-metric default
 * applies.
 */
private fun deltaColor(
    summary: BodyProgressSummary,
    tile: GoalMetric,
    latestValue: Double,
    delta: Double,
    upIsGood: Boolean
): Color {
    if (abs(delta) < 0.0001) return MiColors.TextSecondary
    val goal = summary.goal
    val good = if (goal?.metric == tile) {
        BodyProgressAggregator.movesTowardGoal(goal, latestValue)
    } else {
        if (upIsGood) delta > 0 else delta < 0
    }
    return if (good) Color(0xFF16A34A) else Color(0xFFE54848)
}

// ------------------------------------------------------------------ muscle focus

@Composable
private fun MuscleFocusDashboardCard(
    summary: BodyProgressSummary,
    selected: MuscleGroup?,
    onSelect: (MuscleGroup) -> Unit
) {
    BodyCard {
        DashboardSectionHead(
            icon = "M", tint = BodyChartColors.muscle,
            title = "Muscle Focus",
            subtitle = "Last ${summary.range.days} days \u00B7 ${summary.muscleFocus.sessionCount} sessions",
            actionLabel = ">", onAction = null
        )
        Text(
            "Which muscles you train most",
            fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 1
        )
        Spacer(Modifier.height(8.dp))
        if (summary.muscleFocus.isEmpty) {
            BodyEmptyState("No workout sets recorded in this range")
            return@BodyCard
        }
        val shares = if (selected == null) {
            summary.muscleFocus.shares
        } else {
            summary.muscleFocus.shares.filter { it.muscle == selected }
        }
        shares.take(8).forEach { share ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                Box(Modifier.size(9.dp).clip(CircleShape).background(muscleColor(share.muscle)))
                Spacer(Modifier.width(7.dp))
                Text(
                    share.muscle.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                    color = MiColors.TextPrimary, modifier = Modifier.width(86.dp), maxLines = 1
                )
                Box(
                    Modifier.weight(1f).height(9.dp)
                        .clip(RoundedCornerShape(5.dp)).background(MiColors.Border)
                ) {
                    val top = (summary.muscleFocus.shares.firstOrNull()?.weightedSets ?: 1.0).coerceAtLeast(0.0001)
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(5.dp))
                            .background(muscleColor(share.muscle))
                            .fillMaxWidth((share.weightedSets / top).toFloat().coerceIn(0.04f, 1f))
                    )
                }
                Spacer(Modifier.width(7.dp))
                Text(
                    "${share.sharePercent}%", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                    color = MiColors.TextPrimary, maxLines = 1
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            items(summary.muscleFocus.shares.map { it.muscle }.distinct()) { m ->
                val active = m == selected
                Row(
                    Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (active) MiColors.Emerald else MiColors.SurfaceSunken)
                        .clickable { onSelect(m) }
                        .padding(horizontal = 9.dp, vertical = 5.dp)
                ) {
                    Text(
                        m.label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold,
                        color = if (active) Color.White else MiColors.TextSecondary, maxLines = 1
                    )
                }
            }
        }
        if (summary.muscleFocus.hasUnmappedExercises) {
            Spacer(Modifier.height(6.dp))
            Text(
                "Sets referencing unknown exercises are excluded from these shares.",
                fontSize = 10.sp, color = Color(0xFFE59A1A), maxLines = 2
            )
        }
    }
}

private fun muscleColor(muscle: MuscleGroup): Color = when (muscle) {
    MuscleGroup.CHEST -> Color(0xFFE54848)
    MuscleGroup.BACK, MuscleGroup.LATS, MuscleGroup.TRAPS -> Color(0xFF2563EB)
    MuscleGroup.BICEPS -> Color(0xFF7C3AED)
    MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.HIP_FLEXORS -> Color(0xFFF97316)
    MuscleGroup.SHOULDERS, MuscleGroup.REAR_DELTS -> Color(0xFFFBBF24)
    MuscleGroup.TRICEPS -> Color(0xFFF472B6)
    MuscleGroup.CORE, MuscleGroup.LOWER_BACK -> Color(0xFF16A34A)
    MuscleGroup.CALVES -> Color(0xFF0891B2)
    MuscleGroup.FOREARMS -> Color(0xFF94A3B8)
}

// ------------------------------------------------------------------ top exercises

@Composable
private fun TopExercisesDashboardCard(summary: BodyProgressSummary) {
    BodyCard {
        DashboardSectionHead(
            icon = "E", tint = Color(0xFF7C3AED),
            title = "Top Exercises",
            subtitle = "Sets \u00D7 Reps \u00D7 Weight \u00B7 ${summary.range.label}",
            actionLabel = ">", onAction = null
        )
        Text("By recorded volume", fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 1)
        Spacer(Modifier.height(8.dp))
        val ranked = summary.usage.byVolume()
        if (ranked.isEmpty()) {
            BodyEmptyState("No exercise sets recorded in this range")
            return@BodyCard
        }
        ranked.take(5).forEachIndexed { index, stat ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                Box(
                    Modifier.size(24.dp).clip(CircleShape).background(MiColors.SurfaceSunken),
                    contentAlignment = Alignment.Center
                ) { Text("${index + 1}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = MiColors.TextSecondary) }
                Spacer(Modifier.width(8.dp))
                Text(
                    stat.exercise.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                    color = MiColors.TextPrimary, modifier = Modifier.weight(1f), maxLines = 1
                )
                Text(
                    "${stat.sharePercent}%", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                    color = MiColors.TextPrimary, maxLines = 1
                )
            }
            Row(modifier = Modifier.padding(start = 32.dp, bottom = 4.dp)) {
                Box(
                    Modifier.fillMaxWidth().height(8.dp)
                        .clip(RoundedCornerShape(4.dp)).background(MiColors.Border)
                ) {
                    val top = (ranked.firstOrNull()?.volumeKg ?: 1.0).coerceAtLeast(0.0001)
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(topExerciseColor(index))
                            .fillMaxWidth((stat.volumeKg / top).toFloat().coerceIn(0.04f, 1f))
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "View All (${summary.usage.stats.size} exercises in range) >",
            fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MiColors.Blue, maxLines = 1
        )
        if (summary.usage.unmappedIds.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                "${summary.usage.unmappedIds.size} unknown exercise id(s) excluded.",
                fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 1
            )
        }
    }
}

private fun topExerciseColor(index: Int): Color = when (index % 5) {
    0 -> Color(0xFFE54848)
    1 -> Color(0xFF2563EB)
    2 -> Color(0xFF7C3AED)
    3 -> Color(0xFFF97316)
    else -> Color(0xFFFBBF24)
}

// ------------------------------------------------------------------ equipment

private data class EquipmentDisplay(val label: String, val sets: Int, val percent: Int, val color: Color)

@Composable
private fun EquipmentDashboardCard(summary: BodyProgressSummary) {
    BodyCard {
        DashboardSectionHead(
            icon = "Q", tint = Color(0xFF0891B2),
            title = "Equipment Usage",
            subtitle = "By recorded sets \u00B7 ${summary.range.label}",
            actionLabel = null, onAction = null
        )
        Spacer(Modifier.height(8.dp))
        val merged = remember(summary) { mergeEquipment(summary) }
        if (merged.isEmpty()) {
            BodyEmptyState("No equipment-linked sets in this range")
            return@BodyCard
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (summary.equipmentDominantPercent != null) {
                val top = merged.firstOrNull()
                BodyDonut(
                    percent = summary.equipmentDominantPercent,
                    centerLabel = "${summary.equipmentDominantPercent}%",
                    caption = top?.label ?: "by volume",
                    color = BodyChartColors.strength,
                    modifier = Modifier.size(96.dp),
                    semanticsText = "Dominant equipment ${top?.label} at ${summary.equipmentDominantPercent} percent of volume"
                )
                Spacer(Modifier.width(12.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                merged.forEach { row ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(9.dp).clip(CircleShape).background(row.color))
                        Spacer(Modifier.width(7.dp))
                        Text(row.label, fontSize = 11.sp, color = MiColors.TextSecondary, modifier = Modifier.weight(1f), maxLines = 1)
                        Text("${row.percent}%", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
                    }
                }
            }
        }
        if (summary.equipmentDominantPercent == null) {
            Spacer(Modifier.height(6.dp))
            Text(
                "Share is counted by sets; no comparable load data exists in this range.",
                fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 2
            )
        } else {
            // The ring and the list honestly measure different things (volume
            // vs set count), so the basis of each is stated outright.
            Spacer(Modifier.height(6.dp))
            Text(
                "Ring: volume share \u00B7 List: set-count share.",
                fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 1
            )
        }
    }
}

/** Categories sharing a display label (barbell/dumbbell/kettlebell) merge for display; totals are preserved. */
private fun mergeEquipment(summary: BodyProgressSummary): List<EquipmentDisplay> {
    val total = summary.equipment.shares.sumOf { it.setCount }
    if (total <= 0) return emptyList()
    return summary.equipment.shares
        .groupBy { it.category.label }
        .map { (label, rows) ->
            val sets = rows.sumOf { it.setCount }
            EquipmentDisplay(
                label = label, sets = sets,
                percent = ((sets.toDouble() / total) * 100).roundToInt(),
                color = equipmentColor(label)
            )
        }
        .sortedByDescending { it.sets }
}

private fun equipmentColor(label: String): Color = when (label) {
    "Free Weights" -> Color(0xFF16A34A)
    "Machines" -> Color(0xFF2563EB)
    "Cable" -> Color(0xFF7C3AED)
    "Bodyweight" -> Color(0xFFFBBF24)
    "Bands" -> Color(0xFF0891B2)
    else -> Color(0xFF94A3B8)
}

// ------------------------------------------------------------------ trend

private enum class TrendTab(val label: String) { WEIGHT("Weight"), MUSCLE("Muscle"), BODY_FAT("Body Fat"), STRENGTH("Strength") }

@Composable
private fun ProgressTrendDashboardCard(summary: BodyProgressSummary) {
    val tabs = remember(summary) { availableTrendTabs(summary) }
    var tab by remember(tabs) { mutableStateOf(tabs.firstOrNull() ?: TrendTab.WEIGHT) }
    BodyCard {
        DashboardSectionHead(
            icon = "T", tint = Color(0xFF7C3AED),
            title = "Progress Trend",
            subtitle = summary.range.label,
            actionLabel = null, onAction = null
        )
        Spacer(Modifier.height(8.dp))
        if (tabs.isEmpty()) {
            BodyEmptyState("No trendable records in this range")
            return@BodyCard
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(tabs) { entry ->
                val active = entry == tab
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) Color(0xFF7C3AED) else MiColors.SurfaceSunken)
                        .clickable { tab = entry }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        entry.label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = if (active) Color.White else MiColors.TextSecondary, maxLines = 1
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        when (tab) {
            TrendTab.STRENGTH -> StrengthTrend(summary)
            else -> CompositionTrend(summary, tab.metric())
        }
    }
}

private fun TrendTab.metric(): BodyMetric = when (this) {
    TrendTab.WEIGHT -> BodyMetric.WEIGHT
    TrendTab.MUSCLE -> BodyMetric.MUSCLE_MASS
    TrendTab.BODY_FAT -> BodyMetric.BODY_FAT
    TrendTab.STRENGTH -> BodyMetric.WEIGHT
}

private fun availableTrendTabs(summary: BodyProgressSummary): List<TrendTab> = buildList {
    if (summary.compositionSeries(BodyMetric.WEIGHT)?.points?.isNotEmpty() == true) add(TrendTab.WEIGHT)
    if (summary.compositionSeries(BodyMetric.MUSCLE_MASS)?.points?.isNotEmpty() == true) add(TrendTab.MUSCLE)
    if (summary.compositionSeries(BodyMetric.BODY_FAT)?.points?.isNotEmpty() == true) add(TrendTab.BODY_FAT)
    if (summary.strength != null) add(TrendTab.STRENGTH)
}

@Composable
private fun CompositionTrend(summary: BodyProgressSummary, metric: BodyMetric) {
    val series: MetricSeries? = summary.compositionSeries(metric)
    if (series == null || series.points.isEmpty()) {
        BodyEmptyState("No readings in this range", "Try a wider date range.")
        return
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        BodyPill(
            "${trim(series.latest?.value ?: 0.0)} ${metric.unit}",
            Color(0xFF7C3AED), Color.White, 11
        )
    }
    Spacer(Modifier.height(4.dp))
    BodyLineChart(
        values = series.points.map { it.value },
        labels = series.points.map { shortDay(it.day) },
        color = colorForMetric(series.metric.name),
        unit = series.metric.unit,
        modifier = Modifier.height(170.dp).fillMaxWidth()
    )
    Spacer(Modifier.height(6.dp))
    Text(
        "${series.metric.label} (${series.metric.unit}) \u00B7 " +
            "${shortDay(series.points.first().day)} \u2013 ${shortDay(series.points.last().day)}",
        fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 1
    )
    series.method?.let {
        Text("Method: $it", fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 1)
    }
}

@Composable
private fun StrengthTrend(summary: BodyProgressSummary) {
    val prog = summary.strength
    if (prog == null) {
        BodyEmptyState(
            "Not enough comparable records",
            "Two sets of the same variation are needed to compare."
        )
        return
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        BodyPill(
            "${trim(prog.current.loadKg)} kg \u00D7 ${prog.current.reps}",
            Color(0xFF16A34A), Color.White, 11
        )
    }
    Spacer(Modifier.height(4.dp))
    BodyLineChart(
        values = listOf(prog.previous.estimated1RmKg, prog.current.estimated1RmKg),
        labels = listOf(shortDay(prog.previous.day), shortDay(prog.current.day)),
        color = BodyChartColors.strength,
        unit = "kg",
        modifier = Modifier.height(170.dp).fillMaxWidth()
    )
    Spacer(Modifier.height(6.dp))
    InfoLine("Exercise", prog.exerciseName)
    InfoLine("From", "${trim(prog.previous.loadKg)} kg \u00D7 ${prog.previous.reps} (${shortDay(prog.previous.day)})")
    InfoLine("To", "${trim(prog.current.loadKg)} kg \u00D7 ${prog.current.reps} (${shortDay(prog.current.day)})")
    InfoLine("Est. 1RM", "${trim(prog.current.estimated1RmKg)} kg")
    Spacer(Modifier.height(4.dp))
    Text(
        "Comparisons only use records from the same exercise variation.",
        fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 2
    )
}

// ------------------------------------------------------------------ consistency

@Composable
private fun ConsistencyDashboardCard(summary: BodyProgressSummary) {
    val consistency = summary.consistency
    BodyCard {
        DashboardSectionHead(
            icon = "C", tint = Color(0xFF7C3AED),
            title = "Workout Consistency",
            subtitle = "Last ${consistency.totalDays} days",
            actionLabel = null, onAction = null
        )
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                "${consistency.trainedDays} / ${consistency.totalDays} days",
                fontSize = 16.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
            )
            Spacer(Modifier.width(10.dp))
            Text(
                consistency.consistencyPercent?.let { "$it%" } ?: "-",
                fontSize = 16.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
            )
        }
        Spacer(Modifier.height(8.dp))
        if (consistency.days.isEmpty()) {
            BodyEmptyState("No days in range")
            return@BodyCard
        }
        val weeks = consistency.days.chunked(7)
        weeks.forEachIndexed { index, week ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                Text(
                    "W${index + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = MiColors.TextSecondary, modifier = Modifier.width(28.dp), maxLines = 1
                )
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    week.forEach { (_, state) ->
                        Box(
                            Modifier.size(19.dp).clip(CircleShape).background(consistencyDot(state))
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ConsistencyLegend("Workout", Color(0xFF22C55E))
            ConsistencyLegend("Light", Color(0xFFFBBF24))
            ConsistencyLegend("Absent", Color(0xFFE54848))
            ConsistencyLegend("Rest", Color(0xFFCBD5E1))
        }
    }
}

@Composable
private fun ConsistencyLegend(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1)
    }
}

private fun consistencyDot(state: ConsistencyAnalytics.DayState): Color = when (state) {
    ConsistencyAnalytics.DayState.WORKOUT -> Color(0xFF22C55E)
    ConsistencyAnalytics.DayState.LIGHT -> Color(0xFFFBBF24)
    ConsistencyAnalytics.DayState.ABSENT -> Color(0xFFE54848)
    ConsistencyAnalytics.DayState.REST -> Color(0xFFCBD5E1)
}

// ------------------------------------------------------------------ bottom grid

@Composable
private fun BestProgressCard(summary: BodyProgressSummary) {
    val prog = summary.strength
    BodyCard {
        DashboardSectionHead(icon = "B", tint = Color(0xFFF59E0B), title = "Best Progress", subtitle = null, actionLabel = ">", onAction = null)
        Spacer(Modifier.height(6.dp))
        if (prog == null) {
            Text("No comparable strength records yet.", fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 2)
            return@BodyCard
        }
        Text(prog.exerciseName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MiColors.TextPrimary, maxLines = 1)
        Spacer(Modifier.height(2.dp))
        Text(
            "${trim(prog.previous.loadKg)} \u2192 ${trim(prog.current.loadKg)} kg",
            fontSize = 15.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
        )
        val delta = prog.loadDeltaKg ?: 0.0
        val base = prog.previous.loadKg
        val pct = if (base > 0) (delta / base) * 100.0 else null
        Text(
            "${if (delta >= 0) "+" else ""}${trim(delta)} kg" +
                (pct?.let { " (${if (it >= 0) "+" else ""}${it.roundToInt()}%)" } ?: ""),
            fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A), maxLines = 1
        )
        Spacer(Modifier.height(2.dp))
        Text(
            "${shortDay(prog.previous.day)} \u2192 ${shortDay(prog.current.day)} \u00B7 same variation",
            fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1
        )
    }
}

@Composable
private fun RecentActivityCard(summary: BodyProgressSummary) {
    BodyCard {
        DashboardSectionHead(icon = "R", tint = Color(0xFF2563EB), title = "Recent Activity", subtitle = null, actionLabel = ">", onAction = null)
        Spacer(Modifier.height(6.dp))
        val recent = summary.usage.stats.sortedByDescending { it.lastDay }.take(3)
        if (recent.isEmpty()) {
            Text("No recorded sessions.", fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 1)
            return@BodyCard
        }
        Text(
            "Latest \u00B7 ${shortDay(recent.first().lastDay)}",
            fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1
        )
        Spacer(Modifier.height(4.dp))
        recent.forEach { stat ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                val dot = stat.exercise.primaryMuscles.firstOrNull()?.let { muscleColor(it) } ?: MiColors.Blue
                Box(Modifier.size(8.dp).clip(CircleShape).background(dot))
                Spacer(Modifier.width(6.dp))
                Text(
                    stat.exercise.name, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                    color = MiColors.TextPrimary, modifier = Modifier.weight(1f), maxLines = 1
                )
            }
            Text(
                "${stat.setCount} sets \u00B7 ${shortDay(stat.lastDay)}",
                fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1,
                modifier = Modifier.padding(start = 14.dp)
            )
        }
    }
}

@Composable
private fun KeyInsightCard(summary: BodyProgressSummary) {
    BodyCard {
        DashboardSectionHead(icon = "I", tint = Color(0xFF7C3AED), title = "Insight", subtitle = null, actionLabel = ">", onAction = null)
        Spacer(Modifier.height(6.dp))
        val insight = summary.insights.firstOrNull()
        if (insight == null) {
            Text("Not enough records for an insight yet.", fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 2)
            return@BodyCard
        }
        Text(insight.text, fontSize = 11.sp, color = MiColors.TextPrimary, maxLines = 4)
        insight.evidence?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 2)
        }
    }
}

@Composable
private fun NextFocusCard(summary: BodyProgressSummary) {
    BodyCard {
        DashboardSectionHead(icon = "N", tint = Color(0xFFE54848), title = "Next Focus", subtitle = null, actionLabel = ">", onAction = null)
        Spacer(Modifier.height(6.dp))
        // "Least recorded" is a statement about the log, never a judgement
        // about the body: absence of a record is not evidence of weakness.
        val least = summary.muscleFocus.shares.minByOrNull { it.weightedSets }
        if (least == null) {
            Text("No training records to base a focus on.", fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 2)
            return@BodyCard
        }
        Text(
            "${least.muscle.label} ${least.sharePercent}%",
            fontSize = 14.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
        )
        Spacer(Modifier.height(2.dp))
        Text(
            "Least recorded of ${summary.muscleFocus.shares.size} trained muscles \u00B7 " +
                "${least.sessionCount} sessions in range.",
            fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 3
        )
    }
}

// ------------------------------------------------------------------ shared bits

@Composable
private fun DashboardSectionHead(
    icon: String,
    tint: Color,
    title: String,
    subtitle: String?,
    actionLabel: String?,
    onAction: (() -> Unit)?
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(30.dp).clip(RoundedCornerShape(10.dp)).background(tint),
            contentAlignment = Alignment.Center
        ) { Text(icon, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White, maxLines = 1) }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
            if (subtitle != null) {
                Text(subtitle, fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1)
            }
        }
        if (actionLabel != null) {
            val mod = if (onAction != null) Modifier.clickable(onClick = onAction) else Modifier
            Text(actionLabel, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MiColors.Blue, modifier = mod)
        }
    }
}

@Composable
private fun DataConfidenceCard(summary: BodyProgressSummary) {
    BodyCard {
        DashboardSectionHead(
            icon = "D", tint = MiColors.Neutral,
            title = "Data Sources & Confidence",
            subtitle = "What these numbers are based on",
            actionLabel = null, onAction = null
        )
        Spacer(Modifier.height(8.dp))
        InfoLine("Measurement method", summary.primarySeries?.method ?: "No measurement method recorded")
        InfoLine("Sessions in range", summary.muscleFocus.sessionCount.toString())
        InfoLine("Sets in range", summary.muscleFocus.totalSets.toString())
        InfoLine("Distinct exercises", summary.muscleFocus.exerciseCount.toString())
        if (summary.dataNotes.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            summary.dataNotes.forEach { note ->
                Row(verticalAlignment = Alignment.Top) {
                    Text("\u2022", fontSize = 10.sp, color = MiColors.Amber)
                    Spacer(Modifier.width(5.dp))
                    Text(note, fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 2)
                }
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 11.sp, color = MiColors.TextSecondary, modifier = Modifier.weight(1f), maxLines = 1)
        Text(value, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = MiColors.TextPrimary, maxLines = 1)
    }
}

private fun formatSecAsMs(totalSeconds: Double): String {
    val total = totalSeconds.roundToInt()
    return "%d:%02d".format(total / 60, total % 60)
}
