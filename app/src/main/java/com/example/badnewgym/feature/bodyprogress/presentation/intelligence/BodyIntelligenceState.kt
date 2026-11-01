package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.AnatomyView
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyDashboardAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyInsight
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyIntelligenceAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyIntelligenceInput
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyIntelligenceView
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyLedger
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyLedgerFactory
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyPrivacySettings
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyWidget
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyWidgetPriority
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.CompositionReading
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.PrivacyCategory
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.Exercise
import com.example.badnewgym.feature.bodyprogress.domain.model.FitnessTestKind
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup

/** Strength list muscle filter. */
enum class StrengthFilter(val label: String) {
    ALL("All"), CHEST("Chest"), BACK("Back"), SHOULDERS("Shoulder"),
    ARMS("Arms"), LEGS("Legs"), CORE("Core");

    fun matches(exercise: Exercise): Boolean {
        if (this == ALL) return true
        val groups = exercise.primaryMuscles.map { it.category() }
        return this in groups
    }

    private fun MuscleGroup.category(): StrengthFilter = when (this) {
        MuscleGroup.CHEST -> CHEST
        MuscleGroup.BACK, MuscleGroup.LATS, MuscleGroup.TRAPS, MuscleGroup.LOWER_BACK -> BACK
        MuscleGroup.SHOULDERS, MuscleGroup.REAR_DELTS -> SHOULDERS
        MuscleGroup.BICEPS, MuscleGroup.TRICEPS, MuscleGroup.FOREARMS -> ARMS
        MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.CALVES,
        MuscleGroup.HIP_FLEXORS -> LEGS
        MuscleGroup.CORE -> CORE
    }
}

/**
 * The eight Body tab sections, in chip order.
 *
 * The order is fixed on purpose: a chip row whose labels move between openings
 * is not navigable. Per-goal prioritisation is still applied *inside* each
 * section (see BodyWidgetPriority) where it does not cost the member their
 * spatial memory of the nav.
 */
enum class BodySection(val label: String, val shortLabel: String) {
    BODY_MAP("Body Map", "Map"),
    COMPOSITION("Body Composition", "Composition"),
    STRENGTH("Strength", "Strength"),
    FOCUS("Muscle & Focus", "Focus"),
    ACTIVITY("Activity & Endurance", "Activity"),
    ASSESSMENT("Fitness Assessment", "Assessment"),
    CONSISTENCY("Training Consistency", "Consistency"),
    INSIGHTS("AI Insights", "AI Insights")
}

/** Time window offered by the Activity trend control. */
enum class ActivityRange(val label: String) { DAY("D"), WEEK("W"), MONTH("M") }

/** Deterministic plan a footer action can open. */
enum class BodyPlanKind { NEXT_ACTIVITY, AI_PLAN, ASSESSMENT, REMINDER }

/** Everything a tap can open. */
sealed interface BodySheetTarget {
    data class MetricDetail(val metric: BodyMetric) : BodySheetTarget
    data class MuscleDetail(val muscle: MuscleGroup) : BodySheetTarget
    data class ExerciseDetail(val exercise: Exercise) : BodySheetTarget
    data class AssessmentDetail(val kind: FitnessTestKind) : BodySheetTarget
    data class InsightDetail(val insight: BodyInsight) : BodySheetTarget
    data class Report(val section: BodySection) : BodySheetTarget
    data class Plan(val kind: BodyPlanKind) : BodySheetTarget
    data object Privacy : BodySheetTarget
}

/**
 * Hoisted dashboard state.
 *
 * The privacy-filtered [view] is derived, so toggling one category immediately
 * removes it from every widget and every insight. Selection state lives here
 * (not inside each item) so returning from a sheet or switching tabs restores
 * the member's context.
 */
@Stable
class BodyIntelligenceState(val input: BodyIntelligenceInput) {

    var privacy by mutableStateOf(BodyPrivacySettings.Visible)

    var section by mutableStateOf(BodySection.BODY_MAP)
    var anatomyView by mutableStateOf(AnatomyView.FRONT)
    var selectedMuscle by mutableStateOf(MuscleGroup.CHEST)
    var selectedMetric by mutableStateOf<BodyMetric?>(null)
    var strengthFilter by mutableStateOf(StrengthFilter.ALL)
    var activityRange by mutableStateOf(ActivityRange.WEEK)
    var showAllExercises by mutableStateOf(false)
    var showAllAssessments by mutableStateOf(false)
    var showAllInsights by mutableStateOf(false)
    var sheet by mutableStateOf<BodySheetTarget?>(null)

    val view: BodyIntelligenceView by derivedStateOf {
        BodyIntelligenceAnalytics.viewOf(input, privacy)
    }

    val widgetOrder: List<BodyWidget> by derivedStateOf { BodyWidgetPriority.order(view) }

    /** The member's workout ledger, built once from their recorded days. */
    val ledger: BodyLedger by lazy { BodyLedgerFactory.from(input) }

    val focus: BodyDashboardAnalytics.MuscleFocusSummary? by derivedStateOf {
        BodyDashboardAnalytics.muscleFocus(input, ledger)
    }

    val activity: BodyDashboardAnalytics.ActivityDashboard? by derivedStateOf {
        BodyDashboardAnalytics.activity(input, ledger)
    }

    val assessment: BodyDashboardAnalytics.AssessmentDashboard by derivedStateOf {
        BodyDashboardAnalytics.assessment(input, ledger)
    }

    val consistency: BodyDashboardAnalytics.ConsistencyDashboard? by derivedStateOf {
        BodyDashboardAnalytics.consistency(input, ledger)
    }

    val insights: BodyDashboardAnalytics.InsightsDashboard by derivedStateOf {
        BodyDashboardAnalytics.insights(input, ledger, view)
    }

    /** The metric currently charted; falls back to the first visible reading. */
    val activeReading: CompositionReading? by derivedStateOf {
        view.readings.firstOrNull { it.metric == selectedMetric } ?: view.readings.firstOrNull()
    }

    fun selectSection(target: BodySection) {
        section = target
    }

    fun selectMetric(metric: BodyMetric) {
        selectedMetric = metric
    }

    fun togglePrivacy(category: PrivacyCategory) {
        privacy = privacy.toggle(category)
        // A hidden metric must not stay selected behind the scenes.
        if (!privacy.isVisible(category)) {
            val stillVisible = view.readings.any { it.metric == selectedMetric }
            if (!stillVisible) selectedMetric = null
        }
    }

    fun resetPrivacyToPrivateDefaults() {
        privacy = BodyPrivacySettings.PrivateByDefault
        if (view.readings.none { it.metric == selectedMetric }) selectedMetric = null
    }

    fun showAllPrivacy() {
        privacy = BodyPrivacySettings.Visible
    }

    fun openSheet(target: BodySheetTarget) {
        sheet = target
    }

    fun dismissSheet() {
        sheet = null
    }
}

@Composable
fun rememberBodyIntelligenceState(input: BodyIntelligenceInput): BodyIntelligenceState =
    remember(input) { BodyIntelligenceState(input) }
