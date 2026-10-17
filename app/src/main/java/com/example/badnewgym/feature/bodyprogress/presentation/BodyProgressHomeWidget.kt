package com.example.badnewgym.feature.bodyprogress.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.domain.BodyProgressAggregator
import com.example.badnewgym.feature.bodyprogress.domain.BodyProgressSummary
import com.example.badnewgym.feature.bodyprogress.domain.GoalVerdict
import com.example.badnewgym.feature.bodyprogress.domain.analytics.MetricSeries
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.GoalMetric
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Body Progress widget for Home.
 *
 * Reference layout: violet header with verdict + View All, four metric tiles
 * with sparklines, then Top Exercise and Muscle Focus panels. Every number is
 * read from [summary]; a metric with no records simply has no tile.
 */
@Composable
fun BodyProgressHomeWidget(
    summary: BodyProgressSummary,
    onOpenDetails: () -> Unit,
    compact: Boolean = false
) {
    BodyCard(onClick = onOpenDetails, padding = 4.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(24.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF8849EF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.FitnessCenter, contentDescription = null,
                    tint = Color.White, modifier = Modifier.size(15.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(
                "Body Progress \u00B7 ${summary.range.label}",
                fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary,
                maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f)
            )
            BodyPill(
                verdictArrow(summary.goalVerdict) + " " + summary.goalVerdict.label,
                verdictBg(summary.goalVerdict),
                if (summary.goalVerdict == GoalVerdict.INSUFFICIENT_DATA) MiColors.TextSecondary else Color.White,
                9
            )
            Spacer(Modifier.width(5.dp))
            Text(
                "View All >", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MiColors.Blue,
                maxLines = 1, modifier = Modifier.clickable(onClick = onOpenDetails)
            )
        }

        Spacer(Modifier.height(3.dp))

        if (!summary.hasAnyData) {
            BodyEmptyState(
                "No recorded body or workout data",
                "Log a workout or measurement to start tracking."
            )
            return@BodyCard
        }

        // Compact mode: one row of tiles for the metrics that actually carry
        // records. Never fabricate a measurement the member never took.
        if (compact) {
            val metrics = listOf(BodyMetric.WEIGHT, BodyMetric.MUSCLE_MASS, BodyMetric.BODY_FAT, BodyMetric.BMI)
                .filter { summary.compositionSeries.containsKey(it) }
            if (metrics.isEmpty()) {
                BodyEmptyState(
                    "No recorded body or workout data",
                    "Log a workout or measurement to start tracking."
                )
                return@BodyCard
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                metrics.forEach { metric ->
                    BodyTile(
                        series = summary.compositionSeries[metric]!!,
                        summary = summary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            return@BodyCard
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            MetricTile(
                summary.compositionSeries(BodyMetric.WEIGHT),
                GoalMetric.WEIGHT, "W", BodyChartColors.weight, summary, Modifier.weight(1f)
            )
            MetricTile(
                summary.compositionSeries(BodyMetric.MUSCLE_MASS),
                GoalMetric.MUSCLE_MASS, "M", BodyChartColors.muscle, summary, Modifier.weight(1f)
            )
            MetricTile(
                summary.compositionSeries(BodyMetric.BODY_FAT),
                GoalMetric.BODY_FAT, "F", BodyChartColors.bodyFat, summary, Modifier.weight(1f)
            )
            FocusTile(summary, Modifier.weight(1f))
        }

        Spacer(Modifier.height(4.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            TopExercisePanel(summary, onOpenDetails, Modifier.weight(1.1f))
            MuscleFocusPanel(summary, onOpenDetails, Modifier.weight(1f))
        }
    }
}

/** One headline metric for the compact dashboard row. */
/**
 * One body metric tile for the dashboard row.
 *
 * Reference treatment: colored icon, metric name, latest value, the change
 * across the range with a direction glyph, a sparkline of the real readings,
 * and a plain-language status. The status honours the member's own goal, so
 * losing weight reads as good only when a loss was the goal.
 */
@Composable
private fun BodyTile(
    series: MetricSeries,
    summary: BodyProgressSummary,
    modifier: Modifier = Modifier
) {
    val latest = series.latest
    if (latest == null) return
    val value = latest.value
    val delta = if (series.hasHistory) value - series.earliest!!.value else 0.0
    val goalMetric = goalMetricFor(series.metric)
    val tint = colorForMetric(series.metric.name)
    val badge = statusBadge(series.metric, delta, summary.goal)

    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MiColors.SurfaceSunken)
            .padding(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(18.dp).clip(RoundedCornerShape(6.dp)).background(tint),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    series.metric.label.take(1),
                    fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White
                )
            }
            Spacer(Modifier.width(4.dp))
            Text(
                series.metric.label,
                fontSize = 8.5.sp, fontWeight = FontWeight.Medium,
                color = MiColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))
            badge?.let {
                Text(
                    it.text,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = it.fg,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(it.bg)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }
        Spacer(Modifier.height(1.dp))
        Text(
            valueText(value, series.metric.unit),
            fontSize = 11.5.sp, fontWeight = FontWeight.Black,
            color = MiColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        if (series.hasHistory) {
            Text(
                deltaText(delta, series.metric.unit),
                fontSize = 8.sp, fontWeight = FontWeight.Bold,
                color = tileDeltaColor(summary, goalMetric, value, delta),
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(1.dp))
            BodyLineChart(
                values = series.points.map { it.value },
                labels = series.points.map { shortDay(it.day) },
                color = tint,
                unit = series.metric.unit,
                modifier = Modifier.height(13.dp).fillMaxWidth(),
                showAxis = false
            )
        } else {
            // One reading and no dates: a sparkline or a trend line drawn from a
            // single point would be decoration pretending to be a trend.
            Text(
                "One reading",
                fontSize = 8.sp, color = MiColors.TextTertiary, maxLines = 1
            )
        }
    }
}

private fun valueText(value: Double, unit: String): String =
    if (unit == "%") "${trim(value)}%" else "${trim(value)} $unit".trim()

/** Maps a metric onto the goal enum so delta colour can respect the member's goal. */
private fun goalMetricFor(metric: BodyMetric): GoalMetric = when (metric) {
    BodyMetric.WEIGHT -> GoalMetric.WEIGHT
    BodyMetric.MUSCLE_MASS -> GoalMetric.MUSCLE_MASS
    BodyMetric.BODY_FAT -> GoalMetric.BODY_FAT
    else -> GoalMetric.WAIST
}

private data class StatusBadge(val text: String, val bg: Color, val fg: Color)

/**
 * Plain-language status for one tile.
 *
 * When the member has an explicit goal for this metric, the badge follows the
 * goal: dropping body fat reads as progress because the goal was to reduce it.
 * With no goal the badge stays neutral and only reports the direction of travel,
 * because "good" is not universal.
 */
private fun statusBadge(
    metric: BodyMetric,
    delta: Double,
    goal: com.example.badnewgym.feature.bodyprogress.domain.model.BodyGoalRecord?
): StatusBadge? {
    if (abs(delta) < 0.0001) {
        return StatusBadge("Stable", Color(0xFFEFF3F6), MiColors.TextSecondary)
    }
    val isGoalMetric = goal?.metric == goalMetricFor(metric)
    val rising = delta > 0
    val good = if (isGoalMetric) {
        BodyProgressAggregator.movesTowardGoal(goal!!, delta)
    } else {
        // No goal: report the direction without judging it.
        true
    }
    val label = if (isGoalMetric) {
        when {
            good && rising && metric == BodyMetric.MUSCLE_MASS -> "Building Muscle"
            good -> "Great Progress"
            else -> "Off Target"
        }
    } else {
        if (rising) "Rising" else "Falling"
    }
    val bg = if (good) Color(0xFFE5F6EC) else Color(0xFFFDECEC)
    val fg = if (good) Color(0xFF0E7A4D) else Color(0xFFC0392B)
    return StatusBadge(label, bg, fg)
}

@Composable
private fun MetricTile(
    series: MetricSeries?,
    goalMetric: GoalMetric,
    icon: String,
    tint: Color,
    summary: BodyProgressSummary,
    modifier: Modifier = Modifier
) {
    if (series == null || series.latest == null) {
        Column(
            modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MiColors.SurfaceSunken)
                .padding(7.dp)
        ) {
            Text("No data", fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1)
        }
        return
    }
    val latest = series.latest!!.value
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MiColors.SurfaceSunken)
            .padding(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(20.dp).clip(RoundedCornerShape(6.dp)).background(tint),
                contentAlignment = Alignment.Center
            ) { Text(icon, fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White) }
            Spacer(Modifier.width(4.dp))
            Text(series.metric.label, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = MiColors.TextSecondary, maxLines = 1)
        }
        Spacer(Modifier.height(1.dp))
        Text(
            if (series.metric.unit == "%") "${trim(latest)}%" else "${trim(latest)} ${series.metric.unit}".trim(),
            fontSize = 12.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
        )
        if (series.hasHistory) {
            val delta = latest - series.earliest!!.value
            Text(
                deltaText(delta, series.metric.unit),
                fontSize = 9.sp, fontWeight = FontWeight.Bold,
                color = tileDeltaColor(summary, goalMetric, latest, delta),
                maxLines = 1
            )
            Spacer(Modifier.height(1.dp))
            BodyLineChart(
                values = series.points.map { it.value },
                labels = series.points.map { shortDay(it.day) },
                color = tint,
                unit = series.metric.unit,
                modifier = Modifier.height(14.dp).fillMaxWidth(),
                showAxis = false
            )
        } else {
            // Single line, no date: the tile is ~60dp wide and the range +
            // exact date already travel in the card header and analytics.
            Text(
                "One reading",
                fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1
            )
        }
    }
}

@Composable
private fun FocusTile(summary: BodyProgressSummary, modifier: Modifier = Modifier) {
    val groups = BodyProgressAggregator.focusGroups(summary.muscleFocus)
    val top = groups.maxByOrNull { it.sharePercent }
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MiColors.SurfaceSunken)
            .padding(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(20.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFF8849EF)),
                contentAlignment = Alignment.Center
            ) { Text("C", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White) }
            Spacer(Modifier.width(4.dp))
            Text("Focus", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = MiColors.TextSecondary, maxLines = 1)
        }
        Spacer(Modifier.height(1.dp))
        if (top == null) {
            Text("No data", fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1)
            return@Column
        }
        Text(
            "${top.label} ${top.sharePercent}%",
            fontSize = 12.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
        )
        Spacer(Modifier.height(3.dp))
        Box(
            Modifier.fillMaxWidth().height(7.dp)
                .clip(RoundedCornerShape(4.dp)).background(MiColors.Border)
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth((top.sharePercent / 100f).coerceIn(0.04f, 1f))
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF8849EF))
            )
        }
    }
}

@Composable
private fun TopExercisePanel(summary: BodyProgressSummary, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    val prog = summary.strength
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MiColors.SurfaceSunken)
            .clickable(onClick = onOpen)
            .padding(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Top Exercise", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MiColors.TextPrimary, maxLines = 1, modifier = Modifier.weight(1f))
            Text(">", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MiColors.Blue)
        }
        Spacer(Modifier.height(3.dp))
        if (prog == null) {
            Text("No comparable records yet.", fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 2)
            return@Column
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(prog.exerciseName, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = MiColors.TextPrimary, maxLines = 1, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(2.dp))
        val delta = prog.loadDeltaKg ?: 0.0
        val base = prog.previous.loadKg
        val pct = if (base > 0) (delta / base) * 100 else null
        val pctColor = when (prog.verdict) {
            com.example.badnewgym.feature.bodyprogress.domain.analytics.StrengthAnalytics.StrengthVerdict.IMPROVING -> Color(0xFF16A34A)
            com.example.badnewgym.feature.bodyprogress.domain.analytics.StrengthAnalytics.StrengthVerdict.REGRESSING -> Color(0xFFE54848)
            else -> MiColors.TextSecondary
        }
        Text(
            androidx.compose.ui.text.buildAnnotatedString {
                append("${trim(prog.previous.loadKg)} \u2192 ${trim(prog.current.loadKg)} kg")
                if (pct != null) {
                    pushStyle(
                        androidx.compose.ui.text.SpanStyle(
                            color = pctColor, fontWeight = FontWeight.Bold
                        )
                    )
                    append(" (${if (pct >= 0) "+" else ""}${pct.roundToInt()}%)")
                    pop()
                }
            },
            fontSize = 12.5.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
        )
    }
}

@Composable
private fun MuscleFocusPanel(summary: BodyProgressSummary, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    val groups = BodyProgressAggregator.focusGroups(summary.muscleFocus)
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MiColors.SurfaceSunken)
            .clickable(onClick = onOpen)
            .padding(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Muscle Focus (Sets)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MiColors.TextPrimary, maxLines = 1, modifier = Modifier.weight(1f))
            Text(">", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MiColors.Blue)
        }
        Spacer(Modifier.height(3.dp))
        if (groups.isEmpty()) {
            Text("No sets recorded.", fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1)
            return@Column
        }
        groups.forEach { group ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(group.label, fontSize = 9.sp, color = MiColors.TextSecondary, modifier = Modifier.width(40.dp), maxLines = 1)
                Box(
                    Modifier.weight(1f).height(6.dp)
                        .clip(RoundedCornerShape(4.dp)).background(MiColors.Border)
                ) {
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((group.sharePercent / 100f).toFloat().coerceIn(0.03f, 1f))
                            .clip(RoundedCornerShape(4.dp))
                            .background(groupColor(group.label))
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text("${group.sharePercent}%", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
            }
        }
    }
}

private fun groupColor(label: String): Color = when (label) {
    "Chest" -> Color(0xFFE54848)
    "Back" -> Color(0xFF2563EB)
    "Arms" -> Color(0xFF16A34A)
    "Legs" -> Color(0xFFF97316)
    else -> Color(0xFF94A3B8)
}

/** Direction glyph for the verdict: movement toward/away from the goal, never bare color. */
private fun verdictArrow(verdict: GoalVerdict): String = when (verdict) {
    GoalVerdict.ON_TRACK, GoalVerdict.IMPROVING -> "\u2191"
    GoalVerdict.REGRESSING -> "\u2193"
    GoalVerdict.STABLE -> "\u2192"
    GoalVerdict.INSUFFICIENT_DATA -> "\u2022"
}

private fun verdictBg(verdict: GoalVerdict): Color = when (verdict) {
    GoalVerdict.ON_TRACK -> Color(0xFF16A34A)
    GoalVerdict.IMPROVING -> Color(0xFF22C55E)
    GoalVerdict.STABLE -> Color(0xFFE59A1A)
    GoalVerdict.REGRESSING -> Color(0xFFE54848)
    GoalVerdict.INSUFFICIENT_DATA -> MiColors.Border
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
 * goal metric, "good" means moving toward the target. Otherwise a neutral
 * per-metric default applies.
 */
private fun tileDeltaColor(
    summary: BodyProgressSummary,
    tile: GoalMetric,
    latestValue: Double,
    delta: Double,
    upIsGood: Boolean = tile != GoalMetric.BODY_FAT
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

internal fun colorForMetric(name: String): Color = when (name) {
    "WEIGHT" -> BodyChartColors.weight
    "MUSCLE_MASS" -> BodyChartColors.muscle
    "BODY_FAT", "FAT_MASS" -> BodyChartColors.bodyFat
    "WAIST" -> BodyChartColors.waist
    "BMI" -> BodyChartColors.endurance
    else -> BodyChartColors.strength
}

internal fun toneBg(tone: com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone): Color =
    when (tone) {
        com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone.POSITIVE -> Color(0xFFE8F8EF)
        com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone.ATTENTION -> Color(0xFFFDECEC)
        com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone.DATA -> Color(0xFFF1F5F3)
        else -> MiColors.SurfaceSunken
    }

internal fun toneFg(tone: com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone): Color =
    when (tone) {
        com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone.POSITIVE -> Color(0xFF0E7A4D)
        com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone.ATTENTION -> Color(0xFFB4231F)
        com.example.badnewgym.feature.bodyprogress.domain.insights.InsightTone.DATA -> MiColors.TextSecondary
        else -> MiColors.TextPrimary
    }

private val dayFmt = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)

internal fun shortDay(epochDay: Long): String =
    LocalDate.ofEpochDay(epochDay).format(dayFmt)

internal fun fullDay(epochDay: Long): String =
    Instant.ofEpochSecond(epochDay * 86_400).atZone(ZoneId.systemDefault()).toLocalDate()
        .format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy", Locale.ENGLISH))
