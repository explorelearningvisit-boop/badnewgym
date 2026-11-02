package com.example.badnewgym.feature.bodyprogress.domain.intelligence

import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.bodyprogress.domain.model.FitnessTestKind
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup

/**
 * BAD GYM - Body Intelligence contracts.
 *
 * These types are the single canonical description of what the Body tab is
 * allowed to show. Two rules are enforced here rather than in the UI:
 *
 *  1. A value only exists when a record exists. [CompositionReading] is built
 *     from a stored member value; nothing is interpolated, averaged or invented
 *     to fill a chart.
 *  2. Privacy is a property of the *data*, not of a composable. A hidden
 *     category is removed from [BodyIntelligenceView.readings] before any
 *     widget, summary or insight can read it, so hiding a value cannot be
 *     bypassed by a shortcut widget.
 */

// --------------------------------------------------------------------- goal

/** The member's explicitly selected primary goal. Drives widget priority. */
enum class TrainingGoal(val label: String) {
    FAT_LOSS("Fat Loss"),
    MUSCLE_GAIN("Muscle Gain"),
    MAINTENANCE("Muscle Maintenance"),
    STRENGTH("Strength"),
    ENDURANCE("Endurance"),
    GENERAL("General Fitness");

    companion object {
        /** Maps a free-text profile goal onto a supported priority profile. */
        fun fromLabel(raw: String?): TrainingGoal {
            val text = raw?.trim()?.lowercase().orEmpty()
            return when {
                text.isEmpty() -> GENERAL
                text.contains("fat") || text.contains("weight loss") || text.contains("cut") -> FAT_LOSS
                text.contains("maintain") -> MAINTENANCE
                text.contains("strength") || text.contains("power") -> STRENGTH
                text.contains("endur") || text.contains("stamina") || text.contains("run") -> ENDURANCE
                text.contains("muscle") || text.contains("bulk") || text.contains("gain") -> MUSCLE_GAIN
                else -> GENERAL
            }
        }
    }
}

// ------------------------------------------------------------------- widgets

/**
 * The eight Body Intelligence widgets. Their order is adaptive but the set is
 * fixed: no widget may be dropped, only re-prioritised.
 */
enum class BodyWidget(val ordinal1: Int, val title: String, val subtitle: String) {
    HUMAN_BODY(1, "Human Body", "Front · back · select a muscle"),
    COMPOSITION(2, "Body Composition", "One metric at a time"),
    STRENGTH(3, "Strength Intelligence", "Every exercise, recorded or not"),
    MUSCLE_FOCUS(4, "Muscle & Training Focus", "Where the training effort went"),
    ACTIVITY(5, "Activity & Endurance", "Recorded session time"),
    ASSESSMENT(6, "Fitness Assessment", "Tests and protocols"),
    CONSISTENCY(7, "Training Consistency", "Check-ins vs completed work"),
    INSIGHTS(8, "AI Insights", "Evidence-linked observations")
}

// ------------------------------------------------------------------- privacy

/**
 * Per-category visibility. [privateByDefault] is the starting state for a new
 * member; an existing member who has already consented keeps their own choice.
 */
enum class PrivacyCategory(val label: String, val privateByDefault: Boolean) {
    WEIGHT("Weight", false),
    HEIGHT("Height", false),
    BMI("BMI", false),
    BODY_FAT("Body Fat", true),
    MUSCLE_MASS("Muscle Mass", true),
    FAT_MASS("Fat Mass", true),
    CHEST("Chest Measurement", true),
    WAIST("Waist Measurement", true),
    HIP("Hip Measurement", true),
    ARMS("Biceps / Triceps", false),
    LEGS("Leg Measurements", false),
    STRENGTH_PERFORMANCE("Strength Performance", false),
    WORKOUT_HISTORY("Workout History", false);

    companion object {
        val privateDefaults: Set<PrivacyCategory> = entries.filter { it.privateByDefault }.toSet()
    }
}

/** Hiding a category never deletes the record - it only stops being rendered. */
data class BodyPrivacySettings(val hidden: Set<PrivacyCategory> = emptySet()) {

    fun isVisible(category: PrivacyCategory): Boolean = category !in hidden

    fun toggle(category: PrivacyCategory): BodyPrivacySettings = copy(
        hidden = if (category in hidden) hidden - category else hidden + category
    )

    fun hideAll(categories: Collection<PrivacyCategory>): BodyPrivacySettings =
        copy(hidden = hidden + categories)

    companion object {
        /** Everything visible: an existing member's recorded consent. */
        val Visible: BodyPrivacySettings = BodyPrivacySettings()
        /** Conservative starting point for a member with no consent on record. */
        val PrivateByDefault: BodyPrivacySettings = BodyPrivacySettings(PrivacyCategory.privateDefaults)
    }
}

// ------------------------------------------------------------------- records

/** One stored body-composition value plus the trend shape recorded with it. */
data class CompositionReading(
    val metric: BodyMetric,
    val value: Double,
    val unit: String,
    /** Recorded net change, e.g. "-2.5 kg". Null when no change is on record. */
    val deltaLabel: String? = null,
    val deltaIsFavourable: Boolean = true,
    /** Normalised 0..1 history shape from the member's records, oldest first. */
    val trend: List<Float> = emptyList(),
    /**
     * Dated values exactly as stored, oldest first. Where [trend] only carries a
     * shape, this carries the numbers, so a chart may label its points and its
     * axis. Empty when no dated history exists - never filled in by interpolation.
     */
    val history: List<Pair<String, Double>> = emptyList(),
    val goalLabel: String? = null,
    val sourceLabel: String = "Member app",
    val privacy: PrivacyCategory
) {
    val label: String get() = metric.label
    val valueLabel: String
        get() {
            val rounded = if (value == value.toLong().toDouble()) value.toLong().toString()
            else String.format("%.1f", value)
            return "$rounded $unit".trim()
        }
    val hasTrend: Boolean get() = trend.size >= 2

    /** True when enough dated records exist to label a chart with real numbers. */
    val hasHistory: Boolean get() = history.size >= 2
}

/** A recorded training/activity day with its duration. */
data class ActivityDay(val day: Int, val minutes: Int, val verified: Boolean = false)

/**
 * One completed set as the member or trainer recorded it.
 *
 * This is the only path by which the Body tab may print a load and rep count. A
 * [loadKg] of null means a bodyweight or timed movement, where kilograms are not
 * the right unit.
 */
data class StrengthSetRecord(
    val exerciseId: String,
    val day: Int,
    val loadKg: Double?,
    val reps: Int,
    val setNumber: Int = 1
)

/** A gym check-in. Deliberately separate from a completed workout. */
data class CheckInRecord(
    val day: Int,
    val present: Boolean,
    val late: Boolean = false,
    val holiday: Boolean = false,
    /** 0 = Sunday .. 6 = Saturday, or -1 when unknown. */
    val dayOfWeekIndex: Int = -1
)

/**
 * The signed-in member's pass identity.
 *
 * Shown once at the top of the Body tab so every number below it is attributable
 * to the person standing in the gym, not to an anonymous device.
 */
data class BodyMemberProfile(
    val name: String,
    val memberId: String,
    val age: String? = null,
    val gender: String? = null,
    val status: String? = null,
    val plan: String? = null,
    val goalLabel: String? = null,
    val dueAmount: String? = null,
    val dueDate: String? = null
) {
    /** "28 Yrs · Male · Active" - blanks are omitted, never printed as "null". */
    val factsLabel: String
        get() = listOfNotNull(
            age?.takeIf { it.isNotBlank() }?.let { "$it Yrs" },
            gender?.takeIf { it.isNotBlank() },
            status?.takeIf { it.isNotBlank() }
        ).joinToString(" · ")
}

/** Everything the Body tab is allowed to read, for one member. */
data class BodyIntelligenceInput(
    val memberId: String,
    val memberName: String,
    val profile: BodyMemberProfile? = null,
    val goal: TrainingGoal = TrainingGoal.GENERAL,
    val readings: List<CompositionReading> = emptyList(),
    val activityDays: List<ActivityDay> = emptyList(),
    val checkInDays: List<CheckInRecord> = emptyList(),
    val monthLabel: String = "",
    val anchorDay: Int = 0,
    val heightLabel: String? = null,
    /**
     * Sets the member actually logged this cycle. Empty until a set is recorded,
     * which is what keeps the strength table reading "Not started" instead of
     * inventing loads from a session template.
     */
    val strengthSets: List<StrengthSetRecord> = emptyList(),
    /**
     * True only when set-level strength records exist. Until they do, strength
     * rows must read "Not started" rather than "0 kg x 0 reps".
     */
    val hasSetLevelRecords: Boolean = false
) {
    val hasAnyRecord: Boolean
        get() = readings.isNotEmpty() || activityDays.isNotEmpty() || checkInDays.any { it.present }
}

// ------------------------------------------------------------------ summaries

data class ActivitySummary(
    val totalMinutes: Int = 0,
    val sessionDays: Int = 0,
    val averageMinutes: Int = 0,
    val longestMinutes: Int = 0,
    val longestDay: Int? = null,
    val dailyMinutes: List<Pair<Int, Int>> = emptyList()
) {
    val hasData: Boolean get() = sessionDays > 0
}

enum class DayMarker { TRAINED, CHECKED_IN_ONLY, REST }

data class ConsistencySummary(
    val trainedDays: Int = 0,
    val checkInDays: Int = 0,
    /** Check-ins with no recorded workout - attendance is not training. */
    val checkInWithoutWorkout: Int = 0,
    val updatedAtDay: Int = 0,
    val markers: List<Pair<Int, DayMarker>> = emptyList()
) {
    val hasData: Boolean get() = trainedDays > 0 || checkInDays > 0

    /** Days since the most recent training day inside the recorded window. */
    val currentStreak: Int
        get() {
            var streak = 0
            for ((_, marker) in markers.reversed()) {
                if (marker == DayMarker.TRAINED) streak++ else break
            }
            return streak
        }
}

enum class InsightTone { POSITIVE, NEUTRAL, ATTENTION, DATA }

data class BodyInsight(
    val title: String,
    val detail: String,
    val evidence: String,
    val tone: InsightTone = InsightTone.NEUTRAL,
    val metricKey: String? = null
)

/** The privacy-filtered projection the UI renders. */
data class BodyIntelligenceView(
    val input: BodyIntelligenceInput,
    val privacy: BodyPrivacySettings,
    val readings: List<CompositionReading>,
    val activity: ActivitySummary,
    val consistency: ConsistencySummary,
    val insights: List<BodyInsight>,
    val hiddenCategories: List<PrivacyCategory>
) {
    val goal: TrainingGoal get() = input.goal
    val hasAnyRecord: Boolean get() = input.hasAnyRecord

    fun isVisible(category: PrivacyCategory): Boolean = privacy.isVisible(category)

    fun reading(metric: BodyMetric): CompositionReading? = readings.firstOrNull { it.metric == metric }
}

// ------------------------------------------------------------- muscle mapping

/** The muscle groups a practical body map exposes (launch scope). */
enum class AnatomyView(val label: String) { FRONT("Front"), BACK("Back") }

data class AnatomyRegion(
    val id: String,
    val label: String,
    val muscle: MuscleGroup,
    val view: AnatomyView,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    fun contains(x: Float, y: Float): Boolean =
        x in left..right && y in top..bottom
}

object AnatomyRegions {

    val front: List<AnatomyRegion> = listOf(
        AnatomyRegion("chest", "Chest", MuscleGroup.CHEST, AnatomyView.FRONT, 0.40f, 0.19f, 0.60f, 0.30f),
        AnatomyRegion("shoulders", "Shoulders", MuscleGroup.SHOULDERS, AnatomyView.FRONT, 0.30f, 0.155f, 0.40f, 0.23f),
        AnatomyRegion("shoulders_r", "Shoulders", MuscleGroup.SHOULDERS, AnatomyView.FRONT, 0.60f, 0.155f, 0.70f, 0.23f),
        AnatomyRegion("biceps", "Biceps", MuscleGroup.BICEPS, AnatomyView.FRONT, 0.30f, 0.25f, 0.375f, 0.36f),
        AnatomyRegion("biceps_r", "Biceps", MuscleGroup.BICEPS, AnatomyView.FRONT, 0.625f, 0.25f, 0.70f, 0.36f),
        AnatomyRegion("forearms", "Forearms", MuscleGroup.FOREARMS, AnatomyView.FRONT, 0.295f, 0.37f, 0.36f, 0.51f),
        AnatomyRegion("forearms_r", "Forearms", MuscleGroup.FOREARMS, AnatomyView.FRONT, 0.64f, 0.37f, 0.705f, 0.51f),
        AnatomyRegion("core", "Abs / Core", MuscleGroup.CORE, AnatomyView.FRONT, 0.425f, 0.31f, 0.575f, 0.44f),
        AnatomyRegion("quads", "Quads", MuscleGroup.QUADS, AnatomyView.FRONT, 0.385f, 0.52f, 0.49f, 0.73f),
        AnatomyRegion("quads_r", "Quads", MuscleGroup.QUADS, AnatomyView.FRONT, 0.51f, 0.52f, 0.615f, 0.73f),
        AnatomyRegion("calves", "Calves", MuscleGroup.CALVES, AnatomyView.FRONT, 0.40f, 0.75f, 0.475f, 0.93f),
        AnatomyRegion("calves_r", "Calves", MuscleGroup.CALVES, AnatomyView.FRONT, 0.525f, 0.75f, 0.60f, 0.93f)
    )

    val back: List<AnatomyRegion> = listOf(
        AnatomyRegion("traps", "Traps", MuscleGroup.TRAPS, AnatomyView.BACK, 0.43f, 0.16f, 0.57f, 0.225f),
        AnatomyRegion("lats", "Lats", MuscleGroup.LATS, AnatomyView.BACK, 0.385f, 0.24f, 0.49f, 0.39f),
        AnatomyRegion("lats_r", "Lats", MuscleGroup.LATS, AnatomyView.BACK, 0.51f, 0.24f, 0.615f, 0.39f),
        AnatomyRegion("triceps", "Triceps", MuscleGroup.TRICEPS, AnatomyView.BACK, 0.29f, 0.25f, 0.365f, 0.37f),
        AnatomyRegion("triceps_r", "Triceps", MuscleGroup.TRICEPS, AnatomyView.BACK, 0.635f, 0.25f, 0.71f, 0.37f),
        AnatomyRegion("lower_back", "Lower Back", MuscleGroup.LOWER_BACK, AnatomyView.BACK, 0.44f, 0.39f, 0.56f, 0.46f),
        AnatomyRegion("glutes", "Glutes", MuscleGroup.GLUTES, AnatomyView.BACK, 0.405f, 0.465f, 0.595f, 0.53f),
        AnatomyRegion("hamstrings", "Hamstrings", MuscleGroup.HAMSTRINGS, AnatomyView.BACK, 0.385f, 0.56f, 0.49f, 0.73f),
        AnatomyRegion("hamstrings_r", "Hamstrings", MuscleGroup.HAMSTRINGS, AnatomyView.BACK, 0.51f, 0.56f, 0.615f, 0.73f),
        AnatomyRegion("calves_b", "Calves", MuscleGroup.CALVES, AnatomyView.BACK, 0.40f, 0.75f, 0.475f, 0.93f),
        AnatomyRegion("calves_br", "Calves", MuscleGroup.CALVES, AnatomyView.BACK, 0.525f, 0.75f, 0.60f, 0.93f)
    )

    fun of(view: AnatomyView): List<AnatomyRegion> = if (view == AnatomyView.FRONT) front else back

    /** Accessible, deduplicated list of selectable muscle groups for a view. */
    fun groups(view: AnatomyView): List<MuscleGroup> =
        of(view).map { it.muscle }.distinct()

    fun regionsFor(view: AnatomyView, muscle: MuscleGroup): List<AnatomyRegion> =
        of(view).filter { it.muscle == muscle }
}

/** Assessments always discoverable, even before any has been performed. */
val BODY_ASSESSMENTS: List<FitnessTestKind> = listOf(
    FitnessTestKind.PUSH_UPS,
    FitnessTestKind.PLANK_HOLD,
    FitnessTestKind.FIVE_K_TIME,
    FitnessTestKind.RUN_DISTANCE,
    FitnessTestKind.RUN_PACE,
    FitnessTestKind.CYCLING_DISTANCE,
    FitnessTestKind.ROW_500M,
    FitnessTestKind.GRIP_STRENGTH,
    FitnessTestKind.VO2_MAX
)

// =============================================================================
// MODULAR BODY INTELLIGENCE ARCHITECTURE CONTRACTS
// =============================================================================

/** Primary navigation modules for Body Intelligence workspace. */
enum class BodyPrimaryTab(val label: String) {
    OVERVIEW("Overview"),
    COMPOSITION("Composition"),
    MEASUREMENTS("Measurements"),
    PROGRESS("Progress"),
    GALLERY("Gallery"),
    GOALS("Goals"),
    HEALTH("Health"),
    MORE("More")
}

/** Contextual secondary sub-tabs for each primary module. */
enum class OverviewSubTab(val label: String) {
    SUMMARY("Summary"),
    TRENDS("Trends"),
    ACTIVITY("Activity")
}

enum class CompositionSubTab(val label: String) {
    OVERVIEW("Overview"),
    FAT_MASS("Fat"),
    LEAN_MASS("Lean"),
    SEGMENTAL("Segmental")
}

enum class MeasurementsSubTab(val label: String) {
    ALL("All"),
    UPPER("Upper"),
    CORE("Core"),
    LOWER("Lower"),
    RATIOS("Ratios")
}

enum class ProgressSubTab(val label: String) {
    WEIGHT("Weight"),
    BODY_FAT("Body Fat"),
    MUSCLE("Muscle"),
    SIZE("Size")
}

enum class GallerySubTab(val label: String) {
    COMPARE("Compare"),
    FRONT("Front"),
    SIDE("Side"),
    BACK("Back"),
    TIMELINE("Timeline")
}

enum class GoalsSubTab(val label: String) {
    ACTIVE("Active"),
    MILESTONES("Milestones"),
    HISTORY("History")
}

enum class HealthSubTab(val label: String) {
    VITALS("Vitals"),
    RECOVERY("Recovery"),
    LIFESTYLE("Lifestyle")
}

enum class MoreSubTab(val label: String) {
    ASSESSMENTS("Assessments"),
    REPORTS("Reports"),
    DEVICE_SYNC("Device Sync"),
    TRAINER_NOTES("Trainer Notes"),
    SETTINGS("Settings")
}

// ---------------------------------------------------------------- Data Models

/** Truthful body composition breakdown (Total Weight = Fat Mass + Fat-Free Mass). */
data class BodyCompositionRecord(
    val totalWeightKg: Double = 69.5,
    val fatMassKg: Double = 12.9,
    val fatFreeMassKg: Double = 56.6, // 12.9 + 56.6 = 69.5 exactly
    val bodyFatPercent: Double = 18.5,
    val skeletalMuscleKg: Double = 53.2, // Subset of fatFreeMassKg
    val bodyWaterKg: Double = 41.8,      // Overlaps fatFreeMassKg
    val boneMineralKg: Double = 3.1,     // Overlaps fatFreeMassKg
    val visceralFatGrade: Int = 5,       // Grade 5 (Normal 1-9)
    val bmrKcal: Int = 1612,             // Labeled "Estimated"
    val bmi: Double = 22.7,              // from 175cm / 69.5kg
    val assessmentDate: String = "30 Jun 2025",
    val sourceDevice: String = "InBody 570",
    val recordedBy: String = "Certified Trainer"
)

data class SegmentalComposition(
    val leftArmKg: Double = 3.2,
    val leftArmPct: Double = 18.4,
    val rightArmKg: Double = 3.3,
    val rightArmPct: Double = 18.7,
    val trunkKg: Double = 28.6,
    val trunkPct: Double = 81.2,
    val leftLegKg: Double = 9.7,
    val leftLegPct: Double = 19.0,
    val rightLegKg: Double = 9.8,
    val rightLegPct: Double = 19.2
)

enum class MeasurementCategory { UPPER, CORE, LOWER }

data class CircumferenceRecord(
    val name: String,
    val category: MeasurementCategory,
    val currentInches: Double,
    val changeInches: Double,
    val previousInches: Double = currentInches - changeInches
) {
    fun currentCm(): Double = currentInches * 2.54
    fun changeCm(): Double = changeInches * 2.54
}

data class BodyRatios(
    val waistToHeight: Double = 0.48, // 32.0 / 66.9
    val waistToHeightLabel: String = "Healthy",
    val waistToHip: Double = 0.88,    // 32.0 / 36.5
    val waistToHipLabel: String = "Normal"
)

data class ProgressHistoryPoint(
    val dateLabel: String,
    val weightKg: Double,
    val bodyFatPct: Double,
    val muscleKg: Double,
    val chestIn: Double
)

data class MilestoneItem(
    val title: String,
    val date: String,
    val completed: Boolean,
    val note: String? = null
)

data class TransformationGoal(
    val title: String = "Fat Loss Goal",
    val status: String = "In Progress",
    val startKg: Double = 72.8,
    val currentKg: Double = 69.5,
    val targetKg: Double = 65.0,
    val progressPercent: Int = 56,
    val milestones: List<MilestoneItem> = listOf(
        MilestoneItem("Initial Assessment", "01 Apr 2025", true, "72.8 kg • Starting point"),
        MilestoneItem("30-Day Review", "01 May 2025", true, "71.2 kg • -1.6 kg"),
        MilestoneItem("60-Day Review", "01 Jun 2025", true, "70.1 kg • -1.1 kg"),
        MilestoneItem("90-Day Review", "01 Jul 2025", false, "Goal: 69.0 kg • In progress")
    )
)

data class HealthVitalsRecord(
    val restingHeartRateBpm: Int = 62,
    val bloodPressureSys: Int = 118,
    val bloodPressureDia: Int = 76,
    val sleepDuration: String = "7 hr 32 min",
    val dailySteps: Int = 8420,
    val hydrationLiters: Double = 2.8,
    val recoveryStatus: String = "Good"
)

data class TransformationPhotoData(
    val beforeDate: String = "01 Apr 2025",
    val afterDate: String = "30 Jun 2025",
    val beforeWeightKg: Double = 74.5,
    val afterWeightKg: Double = 69.5,
    val beforeFatPct: Double = 22.0,
    val afterFatPct: Double = 18.5
)

