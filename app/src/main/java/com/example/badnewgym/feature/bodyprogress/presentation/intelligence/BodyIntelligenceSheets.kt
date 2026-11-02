package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.domain.catalog.ExerciseCatalog
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.AnatomyRegions
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyDashboardAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyInsight
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyIntelligenceAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.InsightTone
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.PrivacyCategory
import com.example.badnewgym.feature.bodyprogress.domain.model.FitnessTestKind
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup

/**
 * Drill-down surface for every Body Intelligence widget.
 *
 * Every sheet answers with the same structure - latest value, previous value,
 * net change, chart/timeline, source - so a member never lands on a generic
 * page after tapping a specific metric.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyIntelligenceSheetHost(state: BodyIntelligenceState) {
    val target = state.sheet ?: return
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { state.dismissSheet() },
        sheetState = sheetState,
        containerColor = BodyIntelPalette.Card,
        contentColor = BodyIntelPalette.Ink,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 620.dp)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 14.dp)
                .padding(top = 14.dp, bottom = 18.dp)
        ) {
            when (target) {
                is BodySheetTarget.MetricDetail -> MetricHistoryContent(state, target)
                is BodySheetTarget.MuscleDetail -> MuscleHistoryContent(state, target)
                is BodySheetTarget.ExerciseDetail -> ExerciseHistoryContent(target)
                is BodySheetTarget.AssessmentDetail -> AssessmentHistoryContent(target)
                is BodySheetTarget.InsightDetail -> InsightContent(target.insight)
                is BodySheetTarget.Report -> ReportContent(state, target.section)
                is BodySheetTarget.Plan -> PlanContent(state, target.kind)
                BodySheetTarget.Privacy -> PrivacyContent(state)
            }
        }
    }
}

// ------------------------------------------------------------------- chrome

@Composable
private fun SheetHeader(eyebrow: String, title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .width(34.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(50))
                .background(BodyIntelPalette.HairlineStrong)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = eyebrow.uppercase(),
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = BodyIntelPalette.Emerald
        )
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            color = BodyIntelPalette.Ink
        )
        Text(
            text = subtitle,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Medium,
            color = BodyIntelPalette.Muted
        )
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 10.5.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 0.8.sp,
        color = BodyIntelPalette.Faint
    )
}

@Composable
private fun MetricRow(label: String, value: String, emphasise: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = BodyIntelPalette.Muted,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = if (emphasise) 15.sp else 12.sp,
            fontWeight = if (emphasise) FontWeight.Black else FontWeight.Bold,
            color = BodyIntelPalette.Ink
        )
    }
}

// ---------------------------------------------------------- composition drill

@Composable
private fun MetricHistoryContent(state: BodyIntelligenceState, target: BodySheetTarget.MetricDetail) {
    val reading = state.view.readings.firstOrNull { it.metric == target.metric }

    if (reading == null) {
        SheetHeader(
            eyebrow = "Body composition",
            title = target.metric.label,
            subtitle = "This metric is currently private."
        )
        BodyIntelEmpty(
            title = "${target.metric.label} is hidden",
            body = "The record still exists. It is excluded from the dashboard and from insights until the member " +
                "makes this category visible again.",
            action = "Manage privacy",
            onAction = { state.openSheet(BodySheetTarget.Privacy) }
        )
        return
    }

    SheetHeader(
        eyebrow = "Body composition",
        title = reading.label,
        subtitle = "Latest stored value with the recorded change."
    )

    MetricRow("Latest value", reading.valueLabel, emphasise = true)
    MetricRow("Recorded change", reading.deltaLabel ?: "No change recorded")
    MetricRow("Goal", reading.goalLabel ?: "No goal set")
    MetricRow("Readings", "${reading.trend.size}")
    Spacer(Modifier.height(10.dp))
    TrendShapeChart(
        points = reading.trend,
        highlight = BodyIntelPalette.Emerald,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    )
    Spacer(Modifier.height(12.dp))
    SectionLabel("Recorded readings")
    Spacer(Modifier.height(6.dp))
    reading.trend.forEachIndexed { index, point ->
        val isLatest = index == reading.trend.lastIndex
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isLatest) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.HairlineStrong)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (isLatest) "Latest reading" else "Reading ${index + 1}",
                fontSize = 11.sp,
                fontWeight = if (isLatest) FontWeight.Bold else FontWeight.Medium,
                color = BodyIntelPalette.Ink
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "recorded",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium,
                color = BodyIntelPalette.Faint
            )
        }
    }
    Spacer(Modifier.height(10.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        SourceLabel(reading.sourceLabel)
        Spacer(Modifier.weight(1f))
        PrivacyBadge(reading.privacy)
    }
    Spacer(Modifier.height(8.dp))
    BodyIntelInfo(
        "Reading order is preserved exactly as stored. The dashboard never draws a continuous line across a gap " +
            "where no reading exists."
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = "Hide ${reading.label.lowercase()}",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = BodyIntelPalette.Rose,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(BodyIntelPalette.RoseWash)
            .clickable { state.togglePrivacy(reading.privacy) }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    )
}

// -------------------------------------------------------------- muscle drill

@Composable
private fun MuscleHistoryContent(state: BodyIntelligenceState, target: BodySheetTarget.MuscleDetail) {
    val muscle: MuscleGroup = target.muscle
    val related = ExerciseCatalog.active().filter { it.primaryMuscles.contains(muscle) }

    SheetHeader(
        eyebrow = "Human body",
        title = muscle.label,
        subtitle = "Muscle measurement, strength and volume are separate records."
    )

    MetricRow("Circumference", "No measurement recorded")
    MetricRow("Previous measurement", "—")
    MetricRow("Net change", "—")
    Spacer(Modifier.height(6.dp))
    SectionLabel("Measurement timeline")
    Spacer(Modifier.height(6.dp))
    BodyIntelEmpty(
        title = "No circumference record for this muscle",
        body = "A tape measurement appears here once recorded. It is never inferred from training volume or from " +
            "exercise performance."
    )
    Spacer(Modifier.height(12.dp))
    SectionLabel("Related exercises")
    Spacer(Modifier.height(4.dp))
    if (related.isEmpty()) {
        Text(
            text = "No catalogue exercise lists this muscle as a primary target.",
            fontSize = 10.5.sp,
            color = BodyIntelPalette.Muted
        )
    } else {
        related.forEachIndexed { index, exercise ->
            if (index > 0) Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { state.openSheet(BodySheetTarget.ExerciseDetail(exercise)) }
                    .padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BodyIntelPalette.Ink
                    )
                    Text(
                        text = exercise.equipment.label,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = BodyIntelPalette.Muted
                    )
                }
                StatusPill("Not started", BodyIntelPalette.Amber, BodyIntelPalette.AmberWash)
            }
        }
    }
    Spacer(Modifier.height(10.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        SourceLabel("Catalogue")
        Spacer(Modifier.weight(1f))
        PrivacyBadge(PrivacyCategory.ARMS)
    }
}

// ------------------------------------------------------------ exercise drill

@Composable
private fun ExerciseHistoryContent(target: BodySheetTarget.ExerciseDetail) {
    val exercise = target.exercise

    SheetHeader(
        eyebrow = "Strength intelligence",
        title = exercise.name,
        subtitle = "${exercise.primaryMuscles.joinToString(", ") { it.label }} · ${exercise.equipment.label}"
    )

    MetricRow("Status", "Not started")
    MetricRow("Latest performance", "No set recorded")
    MetricRow("Personal best", "No set recorded")
    MetricRow("Last performed", "—")
    MetricRow("Movement pattern", exercise.pattern.label)
    Spacer(Modifier.height(10.dp))
    BodyIntelEmpty(
        title = "No performance record yet",
        body = "Set-by-set load and reps appear here after the first logged set. Until then this exercise reads " +
            "\"Not started\" rather than 0 kg × 0 reps."
    )
    Spacer(Modifier.height(10.dp))
    BodyIntelInfo(
        "Estimated 1RM is calculated only from a real load and rep pair, and only within the same exercise " +
            "variation."
    )
}

// ---------------------------------------------------------- assessment drill

@Composable
private fun AssessmentHistoryContent(target: BodySheetTarget.AssessmentDetail) {
    val kind: FitnessTestKind = target.kind

    SheetHeader(
        eyebrow = "Fitness assessment",
        title = kind.label,
        subtitle = "Result unit ${kind.unit} · higher is ${if (kind.higherIsBetter) "better" else "not always better"}"
    )

    MetricRow("Latest result", "Not tested")
    MetricRow("Previous result", "Not tested")
    MetricRow("Personal best", "Not tested")
    MetricRow("Protocol", "Not recorded")
    Spacer(Modifier.height(10.dp))
    BodyIntelEmpty(
        title = "This test has not been performed",
        body = "The test stays visible so it can be scheduled. A missing result is never displayed as a zero score."
    )
    Spacer(Modifier.height(10.dp))
    BodyIntelInfo(
        "Results are only compared when the protocol matches. No universal score is applied across ages, sexes or " +
            "experience levels."
    )
}

// -------------------------------------------------------------- insight drill

@Composable
private fun InsightContent(insight: BodyInsight) {
    SheetHeader(
        eyebrow = "AI insight",
        title = insight.title,
        subtitle = "Derived observation from stored records."
    )
    Text(
        text = insight.detail,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        fontWeight = FontWeight.Medium,
        color = BodyIntelPalette.InkSoft
    )
    Spacer(Modifier.height(12.dp))
    SectionLabel("Supporting evidence")
    Spacer(Modifier.height(5.dp))
    Text(
        text = insight.evidence,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BodyIntelPalette.Ink,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BodyIntelPalette.CardInset)
            .padding(10.dp)
    )
    Spacer(Modifier.height(10.dp))
    BodyIntelInfo(
        "Insights describe recorded change only. They are not a medical assessment and do not claim causation, " +
            "guaranteed fat loss or muscle growth."
    )
    if (insight.tone == InsightTone.DATA) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Confidence · insufficient data for a trend claim",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BodyIntelPalette.Violet
        )
    }
}

// ------------------------------------------------------------------ privacy

@Composable
private fun PrivacyContent(state: BodyIntelligenceState) {
    val settings = state.privacy

    SheetHeader(
        eyebrow = "Privacy",
        title = "Body data visibility",
        subtitle = "Every member controls their own body data. Hidden records are kept, not deleted."
    )

    PrivacyCategory.entries.forEach { category ->
        val visible = settings.isVisible(category)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (visible) Icons.Filled.LockOpen else Icons.Filled.Lock,
                contentDescription = null,
                tint = if (visible) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.Muted,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BodyIntelPalette.Ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (category.privateByDefault) "Private by default" else "User controlled",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = BodyIntelPalette.Faint
                )
            }
            Switch(
                checked = visible,
                onCheckedChange = { state.togglePrivacy(category) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = BodyIntelPalette.EmeraldDeep,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = BodyIntelPalette.HairlineStrong
                )
            )
        }
    }

    Spacer(Modifier.height(10.dp))
    BodyIntelInfo(
        "Visibility is applied before aggregation, so a hidden value cannot appear in a chart, a summary, an " +
            "insight or a notification."
    )
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Private defaults",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = BodyIntelPalette.InkSoft,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(BodyIntelPalette.CardSoft)
                .clickable { state.resetPrivacyToPrivateDefaults() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
        Text(
            text = "Show all",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(BodyIntelPalette.EmeraldDeep)
                .clickable { state.showAllPrivacy() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
    Spacer(Modifier.height(6.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = BodyIntelPalette.Emerald,
            modifier = Modifier.size(12.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "${settings.hidden.size} of ${PrivacyCategory.entries.size} categories hidden",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Medium,
            color = BodyIntelPalette.Muted
        )
    }
    Spacer(Modifier.height(4.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(BodyIntelPalette.Hairline)
            .border(0.dp, Color.Transparent)
    )
}

// ------------------------------------------------------------------- reports

/**
 * Full-section report.
 *
 * The report repeats the dashboard numbers on purpose: a member who taps
 * "Detailed Report" wants the same figures plus the rows the card had to
 * truncate, not a second, differently-derived set of numbers.
 */
@Composable
private fun ReportContent(state: BodyIntelligenceState, section: BodySection) {
    val focus = state.focus
    val activity = state.activity
    val assessment = state.assessment
    val consistency = state.consistency
    val insights = state.insights

    SheetHeader(
        eyebrow = "Detailed report",
        title = section.label,
        subtitle = state.view.input.memberName + " · " + state.view.goal.label
    )
    Spacer(Modifier.height(12.dp))

    when (section) {
        BodySection.BODY_MAP -> {
            MetricRow("Selected muscle", state.selectedMuscle.label, emphasise = true)
            MetricRow("View", state.anatomyView.label)
            MetricRow("Muscle groups on this view", AnatomyRegions.groups(state.anatomyView).size.toString())
            SectionLabel("Training volume")
            focus?.muscles?.forEach { bar ->
                MetricRow("${bar.label} (${bar.percent}%)", "${bar.sets} sets")
            } ?: MetricRow("Volume", "No sessions recorded")
            SectionLabel("Body measurements")
            state.view.readings.forEach { reading ->
                MetricRow(reading.metric.label, reading.valueLabel)
            }
        }

        BodySection.COMPOSITION -> {
            state.view.readings.forEach { reading ->
                MetricRow(reading.metric.label, reading.valueLabel, emphasise = true)
                MetricRow("  Goal", reading.goalLabel ?: "Not set")
                reading.deltaLabel?.let { MetricRow("  Change", it) }
                MetricRow("  Source", reading.sourceLabel)
                Spacer(Modifier.height(6.dp))
            }
            if (state.view.readings.isEmpty()) {
                BodyIntelInfo("No composition metric is currently visible. Check privacy settings.")
            }
        }

        BodySection.STRENGTH -> {
            val ledger = state.ledger
            MetricRow("Training days", ledger.trainingDays.toString(), emphasise = true)
            MetricRow("Working sets", ledger.totalSets.toString())
            MetricRow("Total volume", BodyDashboardAnalytics.volumeLabel(ledger.totalVolumeKg))
            MetricRow("Total time", BodyIntelligenceAnalytics.minutesLabel(ledger.totalMinutes))
            SectionLabel("Session split")
            BodyDashboardAnalytics.equipmentMix(ledger).forEach { (category, count) ->
                MetricRow(category.label, "$count sets")
            }
        }

        BodySection.FOCUS -> {
            if (focus == null) {
                BodyIntelInfo("No training is recorded for this block.")
            } else {
                MetricRow("Working sets", focus.totalSets.toString(), emphasise = true)
                MetricRow("Training days", focus.trainingDays.toString())
                MetricRow("Average session", "${focus.averageMinutes} min")
                MetricRow("Total volume", BodyDashboardAnalytics.volumeLabel(focus.totalVolumeKg))
                MetricRow("Plan completion", "${focus.planCompletionPercent}%")
                SectionLabel("Muscle volume")
                focus.muscles.forEach { bar ->
                    MetricRow("${bar.label} · ${bar.percent}%", "${bar.sets} sets")
                }
                SectionLabel("Movement coverage")
                focus.coverage.forEach { pattern ->
                    MetricRow(pattern.label, if (pattern.covered) "${pattern.sets} sets" else "Not trained")
                }
                SectionLabel("Observations")
                focus.insights.forEach { line -> BulletLine(line, null); Spacer(Modifier.height(4.dp)) }
            }
        }

        BodySection.ACTIVITY -> {
            if (activity == null) {
                BodyIntelInfo("No activity is recorded for this block.")
            } else {
                MetricRow("Steps (estimated)", activity.totalSteps.toString(), emphasise = true)
                MetricRow("Distance", BodyDashboardAnalytics.fmt(activity.totalDistanceKm) + " km")
                MetricRow("Active time", BodyIntelligenceAnalytics.minutesLabel(activity.totalMinutes))
                MetricRow("Calories (estimated)", "${activity.calories} kcal")
                MetricRow("Active days", "${activity.activeDays} / ${activity.windowDays}")
                MetricRow(
                    "Average pace",
                    activity.averagePaceSecPerKm?.let { "%d:%02d /km".format(it / 60, it % 60) }
                        ?: "Not recorded"
                )
                MetricRow("Average heart rate", activity.averageHeartRate?.let { "$it bpm" } ?: "Not recorded")
                MetricRow("VO2 max", activity.vo2Max?.let { BodyDashboardAnalytics.fmt(it) } ?: "Not recorded")
                SectionLabel("Activity mix")
                activity.types.forEach { type ->
                    MetricRow(type.label, "${type.valueLabel} · ${type.sessions} sessions")
                }
                SectionLabel("Weekly distance")
                activity.weekly.forEach { week ->
                    MetricRow(week.label, BodyDashboardAnalytics.fmt(week.value) + " km")
                }
                BodyIntelInfo(
                    "Steps and calories are derived from recorded duration. They are estimates and are " +
                        "labelled as such everywhere they appear."
                )
            }
        }

        BodySection.ASSESSMENT -> {
            MetricRow("Overall score", "${assessment.score}% · ${assessment.scoreLabel}", emphasise = true)
            SectionLabel("Categories")
            assessment.categories.forEach { category ->
                MetricRow(category.label, "${category.percent}%")
            }
            SectionLabel("Tests")
            assessment.tests.forEach { test ->
                MetricRow(
                    test.label,
                    if (!test.recorded) "Not tested"
                    else test.valueLabel + (test.changePercent?.let { " (${if (it >= 0) "+" else ""}$it%)" } ?: "")
                )
            }
            SectionLabel("History")
            assessment.history.forEach { row ->
                MetricRow(row.dateLabel, "${row.score}%")
            }
            MetricRow("Next assessment", "${assessment.nextDateLabel} · in ${assessment.daysToNext} days")
        }

        BodySection.CONSISTENCY -> {
            if (consistency == null) {
                BodyIntelInfo("No attendance is recorded for this block.")
            } else {
                MetricRow("Current streak", "${consistency.streakDays} days", emphasise = true)
                MetricRow("Completed", "${consistency.completed} of ${consistency.planned}")
                MetricRow("Completion", "${consistency.completionPercent}%")
                MetricRow("Average per week", BodyDashboardAnalytics.fmt(consistency.averagePerWeek) + " sessions")
                MetricRow("Average session", "${consistency.averageSessionMinutes} min")
                SectionLabel("Weekly summary")
                consistency.weekly.forEach { week ->
                    MetricRow(week.label, "${week.done} / ${week.planned} (${week.percent}%)")
                }
                SectionLabel("Plan vs completed")
                consistency.planRows.forEach { row ->
                    MetricRow(row.label, "${row.done} / ${row.planned}")
                }
                SectionLabel("Recent sessions")
                consistency.recentSessions.forEach { session ->
                    MetricRow(session.label, "${session.minutes} min")
                }
            }
        }

        BodySection.INSIGHTS -> {
            MetricRow("AI fitness score", "${insights.score}% · ${insights.scoreLabel}", emphasise = true)
            SectionLabel("Category scores")
            insights.overall.forEach { category ->
                MetricRow(category.label, "${category.percent}%")
            }
            SectionLabel("Analysis")
            Text(
                text = insights.summary,
                fontSize = 10.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Medium,
                color = BodyIntelPalette.InkSoft
            )
            Spacer(Modifier.height(8.dp))
            SectionLabel("Recommendations")
            insights.recommendations.forEach { line ->
                BulletLine(line, null)
                Spacer(Modifier.height(4.dp))
            }
            SectionLabel("Comparison vs previous block")
            insights.comparison.forEach { row ->
                MetricRow(row.label, (if (row.changePercent >= 0) "+" else "") + "${row.changePercent}%")
            }
            SectionLabel("Next actions")
            insights.nextActions.forEach { action ->
                MetricRow(action.label, action.detail)
            }
            BodyIntelInfo(
                "The score is a weighted summary of recorded categories. A category with no record is " +
                    "excluded rather than scored as zero."
            )
        }
    }

    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Privacy",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = BodyIntelPalette.InkSoft,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(BodyIntelPalette.CardSoft)
                .clickable { state.openSheet(BodySheetTarget.Privacy) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
        Text(
            text = "Close",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(BodyIntelPalette.EmeraldDeep)
                .clickable { state.dismissSheet() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

/**
 * Deterministic next-step plan.
 *
 * Every suggestion is derived from what the member actually did - the least
 * trained split, the untested assessments, the sessions they have completed.
 * It is a proposal to take to a trainer, not an automated programme.
 */
@Composable
private fun PlanContent(state: BodyIntelligenceState, kind: BodyPlanKind) {
    val focus = state.focus
    val activity = state.activity
    val assessment = state.assessment
    val consistency = state.consistency

    val title = when (kind) {
        BodyPlanKind.NEXT_ACTIVITY -> "Plan Next Activity"
        BodyPlanKind.AI_PLAN -> "AI Fitness Plan"
        BodyPlanKind.ASSESSMENT -> "Book Assessment"
        BodyPlanKind.REMINDER -> "Set Reminder"
    }
    SheetHeader(
        eyebrow = "Suggested next step",
        title = title,
        subtitle = "Built from this member's recorded work"
    )
    Spacer(Modifier.height(12.dp))

    when (kind) {
        BodyPlanKind.NEXT_ACTIVITY -> {
            val splits = focus?.split.orEmpty()
            val weakest = splits.minByOrNull { it.sessions }
            val target = weakest?.label ?: "Push"
            MetricRow("Suggested session", target, emphasise = true)
            MetricRow("Duration", "${focus?.averageMinutes ?: 45} min")
            SectionLabel("Why this session")
            BulletLine(
                if (weakest == null) "No split history yet - start with a full-body session."
                else "$target has ${weakest.sessions} recorded sessions, the lowest of your splits.",
                weakest == null
            )
            Spacer(Modifier.height(4.dp))
            val missingPatterns = focus?.coverage?.filterNot { it.covered }?.map { it.label }.orEmpty()
            BulletLine(
                if (missingPatterns.isEmpty()) "All movement patterns are already covered."
                else "Adds missing coverage: ${missingPatterns.joinToString(", ")}.",
                missingPatterns.isEmpty()
            )
            Spacer(Modifier.height(4.dp))
            BulletLine(
                "Stop 1-2 reps short of failure and log the load so the next plan can use it.",
                null
            )
            SectionLabel("Suggested template")
            when (target) {
                "Push" -> listOf("Bench Press 3x8", "Incline DB Press 3x10", "Shoulder Press 3x10", "Triceps Pushdown 3x12")
                "Pull" -> listOf("Lat Pulldown 3x10", "Barbell Row 3x8", "Face Pull 3x15", "DB Curl 3x12")
                "Legs" -> listOf("Back Squat 3x8", "Romanian Deadlift 3x10", "Leg Press 3x12", "Calf Raise 3x15")
                else -> listOf("Full body circuit 3 rounds", "Squat / Row / Press", "Core finisher 2x10 min")
            }.forEach { line -> MetricRow(line, "") }
            Spacer(Modifier.height(6.dp))
            BodyIntelInfo("Template only - confirm load and variation with the floor trainer before starting.")
        }

        BodyPlanKind.AI_PLAN -> {
            MetricRow("Goal", state.view.goal.label, emphasise = true)
            MetricRow("Current frequency", "${focus?.sessionsThisWeek ?: 0} sessions this week")
            MetricRow("Recommended frequency", "4 sessions / week")
            SectionLabel("Weekly structure")
            listOf(
                "Day 1 - Push: press strength, 3-4 working sets per lift.",
                "Day 2 - Pull: vertical and horizontal pull.",
                "Day 3 - Legs: squat, hinge and unilateral work.",
                "Day 4 - Cardio + core: 25-30 min easy pace, then core.",
                "Rest days: walking only, no added load."
            ).forEach { line -> BulletLine(line, null); Spacer(Modifier.height(4.dp)) }
            SectionLabel("Targets this block")
            MetricRow("Strength", assessment.categories.first { it.label == "Strength" }.percent.let { "${it}% → ${(it + 5).coerceAtMost(100)}%" })
            MetricRow(
                "Cardio",
                activity?.let { "${BodyDashboardAnalytics.fmt(it.totalDistanceKm)} km → ${BodyDashboardAnalytics.fmt(it.weeklyGoalKm * 4)} km" }
                    ?: "No cardio recorded"
            )
            MetricRow("Consistency", "${consistency?.completionPercent ?: 0}% → 85%")
            Spacer(Modifier.height(6.dp))
            BodyIntelInfo("Targets are derived from your own current values, not from a generic programme.")
        }

        BodyPlanKind.ASSESSMENT -> {
            val pending = assessment.tests.filterNot { it.recorded }
            MetricRow("Proposed date", assessment.nextDateLabel, emphasise = true)
            MetricRow("Tests pending", pending.size.toString())
            SectionLabel("Tests to book")
            if (pending.isEmpty()) {
                BulletLine("Every supported test already has a recorded result.", true)
            } else {
                pending.forEach { test -> BulletLine(test.label, false); Spacer(Modifier.height(3.dp)) }
            }
            SectionLabel("Before the test")
            listOf(
                "No heavy lower-body work for 24 hours before a run or jump test.",
                "Bring the same shoes and surface used for the previous test.",
                "Record the protocol used so results stay comparable."
            ).forEach { line -> BulletLine(line, null); Spacer(Modifier.height(3.dp)) }
            Spacer(Modifier.height(6.dp))
            BodyIntelInfo("The assessment slot is a proposal shown in the app; the front desk confirms the booking.")
        }

        BodyPlanKind.REMINDER -> {
            MetricRow("Current streak", "${consistency?.streakDays ?: 0} days", emphasise = true)
            MetricRow("Average per week", BodyDashboardAnalytics.fmt(consistency?.averagePerWeek ?: 0.0))
            SectionLabel("Suggested reminders")
            BulletLine("Evening before each planned session.", null)
            Spacer(Modifier.height(3.dp))
            BulletLine("If a session is missed, the next reminder repeats the same split.", null)
            Spacer(Modifier.height(3.dp))
            BulletLine("Weekly check on Sunday for the plan-versus-completed gap.", null)
            Spacer(Modifier.height(6.dp))
            BodyIntelInfo(
                "Reminders are local to this device. No message is sent anywhere until the member confirms it."
            )
        }
    }

    Spacer(Modifier.height(10.dp))
    Text(
        text = "Close",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(BodyIntelPalette.EmeraldDeep)
            .clickable { state.dismissSheet() }
            .padding(horizontal = 14.dp, vertical = 9.dp)
    )
}
