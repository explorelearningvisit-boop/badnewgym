package com.example.badnewgym.feature.bodyprogress.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.badnewgym.feature.bodyprogress.domain.BodyProgressSummary
import com.example.badnewgym.feature.bodyprogress.domain.GoalVerdict
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyCompositionAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange
import com.example.badnewgym.feature.bodyprogress.domain.analytics.ConsistencyAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EnduranceAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EquipmentUsageAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.ExerciseUsageAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.MuscleFocusAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.StrengthAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.catalog.ExerciseCatalog
import com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import com.example.badnewgym.feature.bodyprogress.domain.model.ViewerRole
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors

/**
 * Full Body Progress analytics screen.
 *
 * Every section is derived from stored records. Filters (range, metric,
 * muscle) re-run the aggregations rather than filtering the rendered result,
 * so a filtered view can never disagree with the totals.
 */
@Composable
fun BodyProgressAnalyticsScreen(
    summarize: (BodyRange, BodyMetric?, MuscleGroup?) -> BodyProgressSummary,
    initialRange: BodyRange = BodyRange.D30,
    viewerRole: ViewerRole = ViewerRole.SELF,
    onBack: () -> Unit
) {
    var range by remember { mutableStateOf(initialRange) }
    var metric by remember { mutableStateOf<BodyMetric?>(null) }
    var muscle by remember { mutableStateOf<MuscleGroup?>(null) }

    val summary = remember(range, metric, muscle) { summarize(range, metric, muscle) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MiColors.Page)
            // enableEdgeToEdge means the window draws under the system bars, so
            // the header has to consume the insets itself.
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
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
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Body Progress", fontSize = 16.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
                Text(
                    summary.goal?.let { "${it.title} \u00B7 ${range.label}" } ?: "No active goal \u00B7 ${range.label}",
                    fontSize = 11.sp, color = MiColors.TextSecondary, maxLines = 1
                )
            }
            if (summary.goalPercent != null) {
                BodyPill("${summary.goalPercent}%", BodyChartColors.weight, Color.White, 11)
            }
        }

        LazyColumn(
            Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { RangeSelector(range) { range = it } }

            item {
                GoalOverviewCard(summary)
            }

            if (summary.availableMetrics.isNotEmpty()) {
                item {
                    MetricSelector(
                        metrics = summary.availableMetrics,
                        selected = metric,
                        onSelect = { metric = if (metric == it) null else it }
                    )
                }
                item {
                    CompositionCard(
                        summary = summary,
                        metric = metric,
                        range = range,
                        viewerRole = viewerRole
                    )
                }
            } else {
                item {
                    BodyCard {
                        BodySectionHeader("Body Composition", "No measurements on record", iconTint = BodyChartColors.bodyFat)
                        Spacer(Modifier.height(6.dp))
                        BodyEmptyState(
                            "No recent body measurements",
                            "This member has workout records only."
                        )
                    }
                }
            }

            item {
                MuscleFocusCard(summary, muscle) { muscle = if (muscle == it) null else it }
            }

            item { ExerciseUsageCard(summary) }

            item { EquipmentCard(summary) }

            item { StrengthCard(summary) }

            item { EnduranceCard(summary) }

            item { ConsistencyCard(summary) }

            item { InsightsCard(summary) }

            item { DataConfidenceCard(summary) }
        }
    }
}

// ------------------------------------------------------------------ selectors

@Composable
private fun RangeSelector(selected: BodyRange, onSelect: (BodyRange) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(BodyRange.entries.toList()) { entry ->
            val active = entry == selected
            Box(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (active) MiColors.Emerald else MiColors.Surface)
                    .clickable { onSelect(entry) }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    entry.label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (active) Color.White else MiColors.TextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun MetricSelector(metrics: List<BodyMetric>, selected: BodyMetric?, onSelect: (BodyMetric) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(metrics) { m ->
            val active = m == selected
            Row(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (active) colorForMetric(m.name) else MiColors.Surface)
                    .clickable { onSelect(m) }
                    .padding(horizontal = 11.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    m.label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = if (active) Color.White else MiColors.TextSecondary, maxLines = 1
                )
            }
        }
    }
}

// ------------------------------------------------------------------ cards

@Composable
private fun GoalOverviewCard(summary: BodyProgressSummary) {
    BodyCard {
        BodySectionHeader(
            "Goal Overview",
            summary.goal?.note ?: "Measured against the configured target",
            iconTint = BodyChartColors.weight
        )
        Spacer(Modifier.height(8.dp))
        val goal = summary.goal
        if (goal == null) {
            BodyEmptyState("No active goal", "Set a goal to track measurable progress.")
            return@BodyCard
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (summary.goalPercent != null) {
                BodyDonut(
                    percent = summary.goalPercent,
                    centerLabel = "${summary.goalPercent}%",
                    caption = "of target",
                    color = BodyChartColors.weight,
                    modifier = Modifier.size(78.dp),
                    semanticsText = "${goal.title} is ${summary.goalPercent} percent toward target"
                )
                Spacer(Modifier.width(12.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(goal.title, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
                    Spacer(Modifier.width(6.dp))
                    BodyPill(
                        summary.goalVerdict.label,
                        when (summary.goalVerdict) {
                            GoalVerdict.ON_TRACK -> Color(0xFF16A34A)
                            GoalVerdict.IMPROVING -> Color(0xFF0891B2)
                            GoalVerdict.STABLE -> Color(0xFFE59A1A)
                            GoalVerdict.REGRESSING -> Color(0xFFE54848)
                            GoalVerdict.INSUFFICIENT_DATA -> MiColors.Border
                        },
                        if (summary.goalVerdict == GoalVerdict.INSUFFICIENT_DATA) MiColors.TextSecondary else Color.White,
                        10
                    )
                }
                InfoLine("Metric", "${goal.metric.label} (${goal.metric.unit})")
                InfoLine("Start", "${trim(goal.startValue)} ${goal.metric.unit}")
                InfoLine(
                    "Target",
                    goal.targetValue?.let { "${trim(it)} ${goal.metric.unit}" } ?: "Not set (qualitative goal)"
                )
                InfoLine("Started", shortDay(goal.startedAtEpoch / 86_400_000L))
                // Direction is explicit, so a rising weight is never misread as
                // a failed fat-loss goal (and vice versa).
                InfoLine("Direction", goal.direction.name.lowercase().replaceFirstChar { it.uppercase() })
            }
        }
    }
}

@Composable
private fun CompositionCard(
    summary: BodyProgressSummary,
    metric: BodyMetric?,
    range: BodyRange,
    viewerRole: ViewerRole
) {
    BodyCard {
        BodySectionHeader(
            "Body Composition",
            metric?.let { "${it.label} (${it.unit}) \u00B7 ${range.label}" } ?: "All recorded metrics \u00B7 ${range.label}",
            iconTint = BodyChartColors.weight
        )
        Spacer(Modifier.height(8.dp))
        val series = if (metric != null) {
            summary.compositionSeries(metric)
        } else {
            summary.primarySeries
        }
        if (series == null || series.points.isEmpty()) {
            BodyEmptyState("No readings in this range", "Try a wider date range.")
            return@BodyCard
        }
        BodyLineChart(
            values = series.points.map { it.value },
            labels = series.points.map { shortDay(it.day) },
            color = colorForMetric(series.metric.name),
            unit = series.metric.unit,
            modifier = Modifier.height(140.dp).fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BodyMetricTile(
                "First", trim(series.earliest?.value ?: 0.0) + " " + series.metric.unit, null,
                colorForMetric(series.metric.name), Modifier.weight(1f)
            )
            BodyMetricTile(
                "Latest", trim(series.latest?.value ?: 0.0) + " " + series.metric.unit, null,
                colorForMetric(series.metric.name), Modifier.weight(1f)
            )
            BodyMetricTile(
                "Change",
                series.changePercent()?.let { trim(it) + "%" } ?: "-",
                null, colorForMetric(series.metric.name), Modifier.weight(1f)
            )
        }
        series.method?.let {
            Spacer(Modifier.height(6.dp))
            Text("Method: $it", fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 1)
        }
    }
}

@Composable
private fun MuscleFocusCard(summary: BodyProgressSummary, selected: MuscleGroup?, onSelect: (MuscleGroup) -> Unit) {
    BodyCard {
        BodySectionHeader(
            "Muscle Focus",
            "Last ${summary.range.days} days \u00B7 ${summary.muscleFocus.sessionCount} sessions",
            iconTint = BodyChartColors.muscle
        )
        Spacer(Modifier.height(8.dp))
        if (summary.muscleFocus.isEmpty) {
            BodyEmptyState("No workout sets recorded in this range")
            return@BodyCard
        }
        // Bars use the shares already computed for this range, so the widget and
        // this screen can never disagree.
        val shares = if (selected == null) {
            summary.muscleFocus.shares
        } else {
            summary.muscleFocus.shares.filter { it.muscle == selected }
        }
        BodyRankedBars(
            rows = shares.map {
                RankedBar(
                    label = it.muscle.label,
                    value = it.weightedSets,
                    percent = it.sharePercent,
                    accent = colorForMetric("MUSCLE")
                )
            },
            barColor = BodyChartColors.muscle
        )
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

@Composable
private fun ExerciseUsageCard(summary: BodyProgressSummary) {
    BodyCard {
        BodySectionHeader(
            "Top Exercises",
            "By recorded sets \u00B7 ${summary.range.label}",
            iconTint = Color(0xFF7C3AED)
        )
        Spacer(Modifier.height(8.dp))
        val ranked = summary.exercisesByFrequency()
        if (ranked.isEmpty()) {
            BodyEmptyState("No exercise sets recorded in this range")
            return@BodyCard
        }
        BodyRankedBars(
            rows = ranked.take(6).map {
                RankedBar(
                    label = it.exercise.name,
                    value = it.setCount.toDouble(),
                    percent = it.frequencySharePercent,
                    accent = MiColors.TextPrimary
                )
            },
            barColor = Color(0xFF7C3AED)
        )
        Spacer(Modifier.height(6.dp))
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

@Composable
private fun EquipmentCard(summary: BodyProgressSummary) {
    BodyCard {
        BodySectionHeader("Equipment Usage", "By recorded set count \u00B7 ${summary.range.label}", iconTint = Color(0xFF0891B2))
        Spacer(Modifier.height(8.dp))
        val shares = summary.equipment.shares
        if (shares.isEmpty()) {
            BodyEmptyState("No equipment-linked sets in this range")
            return@BodyCard
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Ring appears only when a comparable volume total exists.
            if (summary.equipmentDominantPercent != null) {
                BodyDonut(
                    percent = summary.equipmentDominantPercent,
                    centerLabel = "${summary.equipmentDominantPercent}%",
                    caption = "by volume",
                    color = BodyChartColors.muscle,
                    modifier = Modifier.size(80.dp),
                    semanticsText = "Dominant equipment ${shares.firstOrNull()?.category?.label} at ${summary.equipmentDominantPercent} percent of volume"
                )
                Spacer(Modifier.width(12.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                shares.forEach { share ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(9.dp).clip(CircleShapeAlias).background(equipmentColor(share.category)))
                        Spacer(Modifier.width(7.dp))
                        Text(share.category.label, fontSize = 11.sp, color = MiColors.TextSecondary, modifier = Modifier.weight(1f), maxLines = 1)
                        Text("${share.sharePercent}%", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
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
        }
    }
}

@Composable
private fun StrengthCard(summary: BodyProgressSummary) {
    BodyCard {
        BodySectionHeader("Exercise Performance", "Best comparable set \u00B7 ${summary.range.label}", iconTint = Color(0xFF16A34A))
        Spacer(Modifier.height(8.dp))
        val progression = summary.strength
        if (progression == null) {
            BodyEmptyState(
                "Not enough comparable records",
                "Two sets of the same variation are needed to compare."
            )
            return@BodyCard
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(progression.exerciseName, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
                Spacer(Modifier.height(4.dp))
                InfoLine("From", "${trim(progression.previous.loadKg)} kg x ${progression.previous.reps} (${shortDay(progression.previous.day)})")
                InfoLine("To", "${trim(progression.current.loadKg)} kg x ${progression.current.reps} (${shortDay(progression.current.day)})")
                progression.loadDeltaKg?.let {
                    InfoLine(
                        "Load change",
                        (if (it >= 0) "+" else "") + trim(it) + " kg" +
                            if (progression.repDelta != 0) "  \u00B7  reps ${if (progression.repDelta > 0) "+" else ""}${progression.repDelta}" else ""
                    )
                }
                InfoLine("Est. 1RM", "${trim(progression.current.estimated1RmKg)} kg")
            }
            BodyPill(
                progression.verdict.name.replace('_', ' '),
                when (progression.verdict) {
                    StrengthAnalytics.StrengthVerdict.IMPROVING -> Color(0xFF16A34A)
                    StrengthAnalytics.StrengthVerdict.REGRESSING -> Color(0xFFE54848)
                    StrengthAnalytics.StrengthVerdict.STABLE -> Color(0xFFE59A1A)
                    StrengthAnalytics.StrengthVerdict.INSUFFICIENT_DATA -> MiColors.Border
                },
                if (progression.verdict == StrengthAnalytics.StrengthVerdict.INSUFFICIENT_DATA) MiColors.TextSecondary else Color.White,
                10
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "Comparisons only use records from the same exercise variation.",
            fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 2
        )
    }
}

@Composable
private fun EnduranceCard(summary: BodyProgressSummary) {
    val endurance = summary.endurance
    BodyCard {
        BodySectionHeader("Cardio Endurance", "Recorded cardio sessions \u00B7 ${summary.range.label}", iconTint = BodyChartColors.endurance)
        Spacer(Modifier.height(8.dp))
        if (endurance.isEmpty) {
            BodyEmptyState("No cardio records in this range")
            return@BodyCard
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BodyMetricTile(
                "Total distance", trim(endurance.totalDistanceKm) + " km", null,
                BodyChartColors.endurance, Modifier.weight(1f)
            )
            BodyMetricTile(
                "Best pace", EnduranceAnalytics.formatPace(endurance.bestPace?.paceSecPerKm) + "/km", null,
                BodyChartColors.endurance, Modifier.weight(1f)
            )
            BodyMetricTile(
                "Change", endurance.paceChangeSecPerKm?.let { trim(it) + "s/km" } ?: "-", null,
                BodyChartColors.endurance, Modifier.weight(1f)
            )
        }
        if (!endurance.hasComparison) {
            Spacer(Modifier.height(6.dp))
            Text(
                "One session on record; pace change needs two comparable sessions.",
                fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 2
            )
        }
    }
}

@Composable
private fun ConsistencyCard(summary: BodyProgressSummary) {
    val consistency = summary.consistency
    BodyCard {
        BodySectionHeader("Workout Consistency", "Last ${consistency.totalDays} days", iconTint = Color(0xFF7C3AED))
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            BodyCalendar(
                cells = consistency.days.map { (day, state) ->
                    day to when (state) {
                        ConsistencyAnalytics.DayState.WORKOUT -> ConsistencyCell.WORKOUT
                        ConsistencyAnalytics.DayState.LIGHT -> ConsistencyCell.LIGHT
                        ConsistencyAnalytics.DayState.ABSENT -> ConsistencyCell.ABSENT
                        ConsistencyAnalytics.DayState.REST -> ConsistencyCell.REST
                    }
                }
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            BodyMetricTile("Trained", "${consistency.trainedDays}d", null, Color(0xFF16A34A), Modifier.weight(1f))
            BodyMetricTile("Light", "${consistency.lightDays}d", null, Color(0xFFFBBF24), Modifier.weight(1f))
            BodyMetricTile("No record", "${consistency.absentDays}d", null, Color(0xFFCBD5E1), Modifier.weight(1f))
            BodyMetricTile(
                "Consistency",
                consistency.consistencyPercent?.let { "$it%" } ?: "-",
                null, Color(0xFF7C3AED), Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun InsightsCard(summary: BodyProgressSummary) {
    BodyCard {
        BodySectionHeader("Insights", "Derived from recorded evidence", iconTint = Color(0xFF0891B2))
        Spacer(Modifier.height(8.dp))
        if (summary.insights.isEmpty()) {
            BodyEmptyState("No insights available", "Not enough records in this range.")
            return@BodyCard
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            summary.insights.forEach { insight ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(toneBg(insight.tone))
                        .padding(9.dp)
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(insight.text, fontSize = 11.sp, color = toneFg(insight.tone), maxLines = 3)
                        insight.evidence?.let {
                            Spacer(Modifier.height(2.dp))
                            Text(it, fontSize = 9.5.sp, color = MiColors.TextTertiary, maxLines = 2)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DataConfidenceCard(summary: BodyProgressSummary) {
    BodyCard {
        BodySectionHeader("Data Sources & Confidence", "What these numbers are based on", iconTint = MiColors.Neutral)
        Spacer(Modifier.height(8.dp))
        InfoLine("Measurement method", summary.primarySeries?.method ?: "No measurement method recorded")
        InfoLine("Sessions in range", summary.muscleFocus.sessionCount.toString())
        InfoLine("Sets in range", summary.muscleFocus.totalSets.toString())
        InfoLine("Dist Exercises", summary.muscleFocus.exerciseCount.toString())
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

// ------------------------------------------------------------------ helpers

@Composable
private fun InfoLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 11.sp, color = MiColors.TextSecondary, modifier = Modifier.weight(1f), maxLines = 1)
        Text(value, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = MiColors.TextPrimary, maxLines = 1)
    }
}

private val CircleShapeAlias = androidx.compose.foundation.shape.CircleShape

private fun equipmentColor(category: com.example.badnewgym.feature.bodyprogress.domain.model.EquipmentCategory): Color =
    when (category.label) {
        "Free Weights" -> Color(0xFF16A34A)
        "Machines" -> Color(0xFF2563EB)
        "Cable" -> Color(0xFF7C3AED)
        "Bodyweight" -> Color(0xFFFBBF24)
        "Bands" -> Color(0xFF0891B2)
        else -> Color(0xFF94A3B8)
    }

internal fun EquipmentUsageAnalytics.EquipmentUsage.dominantLabel(): String? =
    shares.firstOrNull()?.category?.label
