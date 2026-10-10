package com.example.badnewgym.feature.bodyprogress.domain.insights

import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyCompositionAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.BodyRange
import com.example.badnewgym.feature.bodyprogress.domain.analytics.ConsistencyAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EnduranceAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.EquipmentUsageAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.ExerciseUsageAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.MetricSeries
import com.example.badnewgym.feature.bodyprogress.domain.analytics.MuscleFocusAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.analytics.StrengthAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyProgressDataset
import kotlin.math.abs

/**
 * Evidence-backed insight rules.
 *
 * Every sentence below is derived from stored records and names the period it
 * came from. No causal claims, no medical language, no invented trends: when
 * the records are insufficient the insight says exactly that.
 */

enum class InsightTone { POSITIVE, NEUTRAL, ATTENTION, DATA }

data class Insight(
    val text: String,
    val tone: InsightTone,
    val metricKey: String? = null,
    val evidence: String? = null
)

object InsightEngine {

    private const val MIN_SESSIONS_FOR_CLAIM = 2

    fun generate(dataset: BodyProgressDataset, range: BodyRange = BodyRange.D30): List<Insight> {
        val insights = mutableListOf<Insight>()
        insights += strengthInsights(dataset, range)
        insights += bodyCompositionInsights(dataset, range)
        insights += muscleFocusInsights(dataset, range)
        insights += usageInsights(dataset, range)
        insights += equipmentInsights(dataset, range)
        insights += enduranceInsights(dataset, range)
        insights += consistencyInsight(dataset, range)
        insights += dataQualityInsight(dataset, range)
        return insights
    }

    // ---------------------------------------------------------------- strength

    private fun strengthInsights(dataset: BodyProgressDataset, range: BodyRange): List<Insight> {
        val usage = ExerciseUsageAnalytics.usage(dataset, range)
        if (usage.isEmpty) return emptyList()
        // Compare within a lift family so dumbbell vs barbell never mixes.
        val best = usage.stats
            .filter { it.exercise.tracksLoad && it.setCount >= MIN_SESSIONS_FOR_CLAIM }
            .mapNotNull { stat ->
                StrengthAnalytics.progression(dataset, stat.exercise.id, range)
                    ?.let { prog -> Pair(stat.exercise.name, prog) }
            }
            .maxByOrNull { it.second.current.estimated1RmKg }

        val insight = best ?: return emptyList()
        val (name, prog) = best
        val prev = prog.previous
        val cur = prog.current
        return when (prog.verdict) {
            StrengthAnalytics.StrengthVerdict.IMPROVING -> listOf(
                Insight(
                    text = "$name: best comparable set changed from " +
                        "${fmt(prog.previous.loadKg)} kg x ${prog.previous.reps} reps to " +
                        "${fmt(prog.current.loadKg)} kg x ${prog.current.reps} reps.",
                    tone = InsightTone.POSITIVE,
                    metricKey = "strength",
                    evidence = "Estimated 1RM ${fmt(cur.estimated1RmKg)} kg " +
                        "(was ${fmt(prev.estimated1RmKg)} kg)."
                )
            )
            StrengthAnalytics.StrengthVerdict.REGRESSING -> listOf(
                Insight(
                    text = "$name: estimated 1RM is down from ${fmt(prev.estimated1RmKg)} kg to " +
                        "${fmt(cur.estimated1RmKg)} kg over the selected period.",
                    tone = InsightTone.ATTENTION,
                    metricKey = "strength"
                )
            )
            StrengthAnalytics.StrengthVerdict.STABLE -> listOf(
                Insight(
                    text = "$name: estimated 1RM held at about ${fmt(cur.estimated1RmKg)} kg in this period.",
                    tone = InsightTone.NEUTRAL,
                    metricKey = "strength"
                )
            )
            else -> emptyList()
        }.also { _ -> name }
    }

    // ------------------------------------------------------- body composition

    private fun bodyCompositionInsights(dataset: BodyProgressDataset, range: BodyRange): List<Insight> {
        val available = BodyCompositionAnalytics.availableMetrics(dataset)
        if (available.isEmpty()) {
            return listOf(
                Insight(
                    text = "No recent body measurement is available to assess body-composition change.",
                    tone = InsightTone.DATA
                )
            )
        }
        return available.mapNotNull { metric ->
            val series = BodyCompositionAnalytics.series(dataset, metric, range)
            if (!series.hasHistory) {
                return@mapNotNull Insight(
                    text = "${metric.label}: only one ${metric.unit} reading on record, so no trend can be assessed.",
                    tone = InsightTone.DATA,
                    metricKey = metric.name
                )
            }
            val change = series.changePercent() ?: return@mapNotNull null
            val dir = if (change >= 0) "+" else ""
            Insight(
                text = "${metric.label} moved ${dir}${"%.1f".format(change)}% over the selected range " +
                    "(${fmt(series.earliest!!.value)} to ${fmt(series.latest!!.value)} ${metric.unit}).",
                tone = InsightTone.NEUTRAL,
                metricKey = metric.name,
                evidence = series.method?.let { "Measured by $it" }
            )
        }
    }

    // ------------------------------------------------------------ muscle focus

    private fun muscleFocusInsights(dataset: BodyProgressDataset, range: BodyRange): List<Insight> {
        val focus = MuscleFocusAnalytics.focus(dataset, range)
        if (focus.isEmpty) return emptyList()
        val top = focus.top
        if (top.isEmpty()) return emptyList()
        val names = top.joinToString(" and ") { it.muscle.label }
        val evidence = top.joinToString(", ") { "${it.muscle.label} ${it.sharePercent}%" }
        return listOf(
            Insight(
                text = "$names received the most recorded training focus in this period.",
                tone = InsightTone.NEUTRAL,
                metricKey = "muscle_focus",
                evidence = "$evidence across ${focus.sessionCount} sessions and ${focus.totalSets} sets."
            )
        )
    }

    // --------------------------------------------------------------- usage

    private fun usageInsights(dataset: BodyProgressDataset, range: BodyRange): List<Insight> {
        val usage = ExerciseUsageAnalytics.usage(dataset, range)
        if (usage.isEmpty) return emptyList()
        val top = usage.byFrequency().firstOrNull() ?: return emptyList()
        val body = top.exercise.name
        val muscles = top.exercise.primaryMuscles.joinToString(", ") { it.label }
        return listOf(
            Insight(
                text = "$body was the most frequently recorded exercise (${top.setCount} sets " +
                    "across ${top.sessionCount} sessions).",
                tone = InsightTone.NEUTRAL,
                metricKey = "exercises",
                evidence = "Primary muscles: $muscles."
            )
        )
    }

    private fun equipmentInsights(dataset: BodyProgressDataset, range: BodyRange): List<Insight> {
        val usage = EquipmentUsageAnalytics.usage(dataset, range)
        if (usage.shares.isEmpty()) return emptyList()
        val top = usage.shares.first()
        return listOf(
            Insight(
                text = "${top.category.label} accounted for ${top.sharePercent}% of recorded sets in this period.",
                tone = InsightTone.NEUTRAL,
                metricKey = "equipment"
            )
        ) + if (usage.volumeBasedDominantPercent == null) {
            listOf(
                Insight(
                    text = "Equipment share is reported by set count; no compatible volume data exists for this period.",
                    tone = InsightTone.DATA,
                    metricKey = "equipment"
                )
            )
        } else {
            emptyList()
        }
    }

    // --------------------------------------------------------------- endurance

    private fun enduranceInsights(dataset: BodyProgressDataset, range: BodyRange): List<Insight> {
        val endurance = EnduranceAnalytics.summary(dataset, range)
        if (endurance.isEmpty) return emptyList()
        if (!endurance.hasComparison) {
            return listOf(
                Insight(
                    text = "One cardio session is on record; pace change needs at least two comparable sessions.",
                    tone = InsightTone.DATA,
                    metricKey = "endurance"
                )
            )
        }
        val change = endurance.paceChangeSecPerKm!!
        val from = endurance.records.first { it.paceSecPerKm != null }
        val to = endurance.latestPace!!
        val faster = change < 0
        return listOf(
            Insight(
                text = "Pace changed from ${EnduranceAnalytics.formatPace(from.paceSecPerKm)}/km to " +
                    "${EnduranceAnalytics.formatPace(to.paceSecPerKm)}/km under comparable recorded conditions.",
                tone = if (faster) InsightTone.POSITIVE else InsightTone.NEUTRAL,
                metricKey = "endurance",
                evidence = "Total distance ${"%.1f".format(endurance.totalDistanceKm)} km in the selected range."
            )
        )
    }

    private fun consistencyInsight(dataset: BodyProgressDataset, range: BodyRange): List<Insight> {
        val consistency = ConsistencyAnalytics.consistency(dataset, range)
        if (consistency.trainedDays == 0) return emptyList()
        val pct = consistency.consistencyPercent ?: return emptyList()
        return listOf(
            Insight(
                text = "${consistency.trainedDays} of the last ${consistency.totalDays} days had recorded training " +
                    "($pct% including light days).",
                tone = InsightTone.NEUTRAL,
                metricKey = "consistency",
                evidence = "${consistency.lightDays} light day(s), ${consistency.absentDays} day(s) with no record."
            )
        )
    }

    private fun dataQualityInsight(dataset: BodyProgressDataset, range: BodyRange): List<Insight> {
        val focus = MuscleFocusAnalytics.focus(dataset, range)
        val usage = ExerciseUsageAnalytics.usage(dataset, range)
        val unmapped = focus.hasUnmappedExercises || usage.unmappedIds.isNotEmpty()
        if (!unmapped) return emptyList()
        val ids = usage.unmappedIds.take(3).joinToString(", ")
        return listOf(
            Insight(
                text = "Some logged sets reference exercises missing from the catalogue" +
                    (if (ids.isNotBlank()) " ($ids)" else "") +
                    ", so those sets are excluded from muscle and equipment share.",
                tone = InsightTone.DATA
            )
        )
    }

    private fun fmt(v: Double): String =
        if (abs(v - Math.rint(v)) < 0.001) v.toInt().toString() else "%.1f".format(v)
}

/** True when the dataset simply has no evidence to interpret. */
fun BodyProgressDataset.hasEvidence(): Boolean =
    sessions.any { it.effectiveSets.isNotEmpty() } ||
        measurements.isNotEmpty() ||
        fitnessTests.isNotEmpty()

/** Human label for a series' latest value with unit. */
fun MetricSeries.latestLabel(): String {
    val point = latest ?: return "-"
    return "${point.value.clean()} ${metric.unit}".trim()
}

private fun Double.clean(): String =
    if (abs(this - Math.rint(this)) < 0.001) toInt().toString() else "%.1f".format(this)
