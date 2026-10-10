package com.example.badnewgym.feature.bodyprogress.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.domain.BodyProgressSummary
import com.example.badnewgym.feature.bodyprogress.domain.GoalVerdict
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EnduranceAnalytics
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Compact Body Progress widget for Home (360dp target).
 *
 * Contents are priority-ranked for the member's goal:
 *  1. goal + period + verdict (ring only when a real denominator exists)
 *  2. primary metric chart
 *  3. up to three supporting metrics
 *  4. muscle focus, top exercise, and one evidence-backed reading
 *
 * Anything that would not stay legible moves to the analytics screen.
 */
@Composable
fun BodyProgressHomeWidget(
    summary: BodyProgressSummary,
    onOpenDetails: () -> Unit
) {
    BodyCard(onClick = onOpenDetails, padding = 8.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(26.dp).clip(RoundedCornerShape(8.dp)).background(BodyChartColors.weight),
                contentAlignment = Alignment.Center
            ) { Text("B", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White) }
            Spacer(Modifier.width(7.dp))
            Column(Modifier.weight(1f)) {
                Text("Body Progress", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
                val goalText = summary.goal?.let { "${it.title} \u00B7 ${summary.range.label}" } ?: summary.range.label
                Text(goalText, fontSize = 9.5.sp, color = MiColors.TextSecondary, maxLines = 1)
                // The trend chart may cover a wider window than the selected
                // range; its own period is always stated so the two agree.
                summary.primarySeries?.takeIf { it.points.size >= 2 }?.let { series ->
                    Text(
                        "${series.metric.label} \u00B7 ${shortDay(series.points.first().day)}" +
                            " - ${shortDay(series.points.last().day)}",
                        fontSize = 8.5.sp, color = MiColors.TextTertiary, maxLines = 1
                    )
                }
            }
            VerdictPill(summary.goalVerdict)
            Spacer(Modifier.width(4.dp))
            Text(">", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary)
        }

        Spacer(Modifier.height(4.dp))

        if (!summary.hasAnyData) {
            BodyEmptyState(
                "No recorded body or workout data",
                "Log a workout or measurement to start tracking."
            )
            return@BodyCard
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Goal ring is rendered only when start/target give a denominator.
            if (summary.goalPercent != null) {
                BodyDonut(
                    percent = summary.goalPercent,
                    centerLabel = "${summary.goalPercent}%",
                    caption = summary.primaryMetric?.label?.take(6) ?: "Goal",
                    color = BodyChartColors.weight,
                    modifier = Modifier.size(58.dp),
                    semanticsText = "Goal ${summary.goalPercent} percent complete for ${summary.goal?.title ?: "selected goal"}"
                )
                Spacer(Modifier.width(8.dp))
            } else if (summary.isQualitativeGoal) {
                Column(
                    Modifier
                        .width(58.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MiColors.SurfaceSunken)
                        .padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No target", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary, maxLines = 1)
                    Text("set", fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1)
                }
                Spacer(Modifier.width(8.dp))
            }

            Column(Modifier.weight(1f)) {
                val series = summary.primarySeries
                if (series != null && series.points.isNotEmpty()) {
                    val labels = series.points.map { shortDay(it.day) }
                    BodyLineChart(
                        values = series.points.map { it.value },
                        labels = labels,
                        color = colorForMetric(series.metric.name),
                        unit = series.metric.unit,
                        modifier = Modifier.height(52.dp).fillMaxWidth(),
                        showAxis = false
                    )
                } else {
                    // No body data: fall back to the strongest exercise record.
                    val strength = summary.strength
                    if (strength != null) {
                        Text(
                            "${strength.exerciseName}: ${trim(strength.current.loadKg)} kg x ${strength.current.reps}",
                            fontSize = 12.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
                        )
                        Text(
                            "Best comparable set \u00B7 e1RM ${trim(strength.current.estimated1RmKg)} kg",
                            fontSize = 9.5.sp, color = MiColors.TextSecondary, maxLines = 1
                        )
                    } else {
                        Text(
                            "Workout records only - no body measurements yet",
                            fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MiColors.TextSecondary
                        )
                    }
                }
            }
        }

        // Home has a fixed height budget, so the supporting facts collapse into
        // one compact evidence line rather than tiles that would be squeezed to
        // the point of being unreadable. Full metrics, focus breakdown and
        // insight reasoning live on the analytics screen.
        val supporting = summary.supportingMetrics.take(2)
        val focusTop = summary.muscleFocus.top.firstOrNull()
        val facts = buildList {
            // Supporting metrics come from the selected range while the primary
            // chart spans a wider window; reporting the chart's window delta next
            // to a short-range value would read as "no change" and be wrong.
            supporting.forEach { series ->
                val inRange = series.points.size >= 2
                val delta = if (inRange) {
                    series.changePercent()?.let { (if (it >= 0) "+" else "") + trim(it) + "%" }
                } else null
                add(
                    series.metric.label + " " +
                        trim(series.latest?.value ?: 0.0) + series.metric.unit +
                        (delta?.let { " (" + it + ")" } ?: "")
                )
            }
            focusTop?.let { add(it.muscle.label + " " + it.sharePercent + "% focus") }
            summary.topExercise?.let { add(it.exercise.name + " top set") }
        }
        if (facts.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                facts.joinToString("  \u00B7  "),
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = MiColors.TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun VerdictPill(verdict: GoalVerdict) {
    val (bg, fg) = when (verdict) {
        GoalVerdict.ON_TRACK -> Color(0xFF16A34A) to Color.White
        GoalVerdict.IMPROVING -> Color(0xFF0891B2) to Color.White
        GoalVerdict.STABLE -> Color(0xFFE59A1A) to Color.White
        GoalVerdict.REGRESSING -> Color(0xFFE54848) to Color.White
        GoalVerdict.INSUFFICIENT_DATA -> MiColors.Border to MiColors.TextSecondary
    }
    BodyPill(verdict.label, bg, fg, 9)
}

@Composable
private fun EvidenceLine(text: String, evidence: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        Box(Modifier.size(4.dp).clip(CircleShape).background(MiColors.Emerald))
        Spacer(Modifier.width(6.dp))
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MiColors.TextPrimary, maxLines = 1)
        if (evidence != null) {
            Spacer(Modifier.width(4.dp))
            Text(evidence, fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1)
        }
    }
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
