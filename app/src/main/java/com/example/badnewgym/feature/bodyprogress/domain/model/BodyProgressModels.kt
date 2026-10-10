package com.example.badnewgym.feature.bodyprogress.domain.model

import java.time.LocalDate

/**
 * Body Progress Intelligence - data contracts.
 *
 * Design rules enforced by construction:
 *  - Body composition, strength, muscular endurance, cardio endurance and
 *    training volume are separate types. Nothing merges them into one score.
 *  - Every measurement carries unit, timestamp, source and verification state.
 *  - Absence is `null` / empty collections, never a synthesised number.
 */

// ------------------------------------------------------------------ muscles

enum class MuscleGroup(val label: String, val region: BodyRegion) {
    CHEST("Chest", BodyRegion.UPPER_PUSH),
    BACK("Back", BodyRegion.UPPER_PULL),
    LATS("Lats", BodyRegion.UPPER_PULL),
    TRAPS("Traps", BodyRegion.UPPER_PULL),
    SHOULDERS("Shoulders", BodyRegion.UPPER_PUSH),
    REAR_DELTS("Rear Delts", BodyRegion.UPPER_PUSH),
    BICEPS("Biceps", BodyRegion.UPPER_PULL),
    TRICEPS("Triceps", BodyRegion.UPPER_PUSH),
    FOREARMS("Forearms", BodyRegion.UPPER_PULL),
    QUADS("Quads", BodyRegion.LOWER),
    HAMSTRINGS("Hamstrings", BodyRegion.LOWER),
    GLUTES("Glutes", BodyRegion.LOWER),
    CALVES("Calves", BodyRegion.LOWER),
    CORE("Abs/Core", BodyRegion.CORE),
    LOWER_BACK("Lower Back", BodyRegion.CORE),
    HIP_FLEXORS("Hip Flexors", BodyRegion.LOWER)
}

enum class BodyRegion(val label: String) {
    UPPER_PUSH("Push"), UPPER_PULL("Pull"), LOWER("Legs"), CORE("Core"), FULL_BODY("Full Body")
}

/** How a movement is performed - drives cross-pattern comparison. */
enum class MovementPattern(val label: String) {
    HORIZONTAL_PUSH("Horizontal Push"),
    VERTICAL_PUSH("Vertical Push"),
    HORIZONTAL_PULL("Horizontal Pull"),
    VERTICAL_PULL("Vertical Pull"),
    SQUAT("Squat"),
    HINGE("Hinge"),
    LUNGE("Lunge"),
    ISOLATION("Isolation"),
    CORE("Core"),
    CARDIO("Cardio"),
    PLYOMETRIC("Plyometric"),
    MOBILITY("Mobility")
}

enum class EquipmentCategory(val label: String) {
    BARBELL("Free Weights"),
    DUMBBELL("Free Weights"),
    KETTLEBELL("Free Weights"),
    MACHINE("Machines"),
    CABLE("Cable"),
    BODYWEIGHT("Bodyweight"),
    BAND("Bands"),
    CARDIO_MACHINE("Cardio")
}

enum class LoadUnit(val label: String, val toKg: Double) {
    KG("kg", 1.0),
    LB("lb", 0.45359237),
    PER_DUMBBELL("kg/db", 1.0)
}

enum class VerificationState {
    /** Recorded by a member without staff confirmation. */
    SELF_REPORTED,
    /** Confirmed by staff or a trusted device. */
    VERIFIED,
    /** Produced by a machine estimate (body fat, muscle mass). */
    ESTIMATED
}

enum class DataSourceKind(val label: String) {
    STAFF_ENTRY("Staff entry"),
    MEMBER_ENTRY("Member app"),
    SCALE("Smart scale"),
    INCLINOMETER("Inclinometer"),
    GYM_MACHINE("Gym machine"),
    IMPORT("Import"),
    CAMERA_ESTIMATE("Camera estimate")
}

/** How a body-composition number was obtained - never conflate the two. */
enum class MeasurementMethod(val label: String) {
    DIRECT_SCALE("Digital scale"),
    BIA("Bio-impedance"),
    DEXA("DEXA scan"),
    TAPE("Tape measure"),
    ANTHROPOMETRIC("Anthropometric"),
    ESTIMATED("Estimated")
}

// ------------------------------------------------------------------ catalogue

/**
 * Stable exercise identity. [variationKey] separates variants that must never
 * be compared (e.g. grip vs machine), while [progressionKey] groups variants
 * that may legitimately be compared.
 */
data class Exercise(
    val id: String,
    val name: String,
    val aliases: List<String> = emptyList(),
    val variationKey: String = id,
    val progressionKey: String = variationKey,
    val primaryMuscles: List<MuscleGroup> = emptyList(),
    val secondaryMuscles: List<MuscleGroup> = emptyList(),
    val pattern: MovementPattern = MovementPattern.ISOLATION,
    val equipment: EquipmentCategory = EquipmentCategory.MACHINE,
    val isCardio: Boolean = false,
    val tracksLoad: Boolean = false,
    val tracksDuration: Boolean = false,
    val tracksDistance: Boolean = false,
    val isActive: Boolean = true
) {
    fun allMuscles(): List<MuscleGroup> = primaryMuscles + secondaryMuscles
}

// ------------------------------------------------------------------ records

data class WorkoutSetRecord(
    val id: String,
    val sessionId: String,
    val exerciseId: String,
    val setIndex: Int,
    val load: Double? = null,
    val unit: LoadUnit? = null,
    val reps: Int? = null,
    val holdSeconds: Int? = null,
    val distanceMeters: Double? = null,
    val durationSeconds: Int? = null,
    val recordedAtEpoch: Long,
    val verification: VerificationState = VerificationState.SELF_REPORTED,
    val source: DataSourceKind = DataSourceKind.MEMBER_ENTRY,
    /** Set when a record was corrected, pointing at the superseded set. */
    val correctsSetId: String? = null
) {
    /** True when this record supersedes an earlier one. */
    val isCorrection: Boolean get() = correctsSetId != null
    fun loadKg(): Double? {
        val raw = load ?: return null
        val unitValue = unit ?: return null
        return when (unitValue) {
            // A dumbbell load is stored per dumbbell; total load doubles it so
            // volume maths compares like with like.
            LoadUnit.PER_DUMBBELL -> raw * 2.0
            else -> raw * unitValue.toKg
        }
    }
}

data class WorkoutSessionRecord(
    val id: String,
    val memberId: String,
    val epochDay: Long,
    val startedAtEpoch: Long,
    val sets: List<WorkoutSetRecord> = emptyList(),
    val isActive: Boolean = false
) {
    /** Every set ever recorded, corrections included - used for audit views. */
    val allSets: List<WorkoutSetRecord> get() = sets

    /**
     * Sets that count toward analytics.
     *
     * A correcting record carries [WorkoutSetRecord.correctsSetId] pointing at
     * the set it replaces, so the SUPERSEDED id - not the correcting record -
     * is what gets filtered out. Keeping the correcting record is the whole
     * point of an audit trail.
     */
    val effectiveSets: List<WorkoutSetRecord>
        get() {
            val superseded = sets.mapNotNull { it.correctsSetId }.toSet()
            return sets.filter { it.id !in superseded }
        }
}

data class BodyMeasurement(
    val id: String,
    val memberId: String,
    val metric: BodyMetric,
    val value: Double,
    val unit: String,
    val measuredAtEpoch: Long,
    val method: MeasurementMethod,
    val source: DataSourceKind,
    val verification: VerificationState,
    val notes: String? = null
) {
    val epochDay: Long get() = measuredAtEpoch / 86_400_000L
}

enum class BodyMetric(val label: String, val unit: String, val higherIsBetterDefault: Boolean) {
    WEIGHT("Weight", "kg", false),
    BODY_FAT("Body Fat", "%", false),
    MUSCLE_MASS("Muscle Mass", "kg", true),
    FAT_MASS("Fat Mass", "kg", false),
    WAIST("Waist", "in", false),
    CHEST("Chest", "in", true),
    ARM("Arm", "in", true),
    BMI("BMI", "", false)
}

data class FitnessTestRecord(
    val id: String,
    val memberId: String,
    val kind: FitnessTestKind,
    val value: Double,
    val unit: String,
    val performedAtEpoch: Long,
    val verification: VerificationState = VerificationState.SELF_REPORTED,
    val source: DataSourceKind = DataSourceKind.MEMBER_ENTRY,
    /** Protocol must match before two tests may be compared. */
    val protocol: String? = null
) {
    val epochDay: Long get() = performedAtEpoch / 86_400_000L
}

enum class FitnessTestKind(val label: String, val unit: String, val higherIsBetter: Boolean) {
    FIVE_K_TIME("5K Time", "min", false),
    RUN_DISTANCE("Run Distance", "km", true),
    RUN_PACE("Run Pace", "min/km", false),
    CYCLING_DISTANCE("Cycling Distance", "km", true),
    ROW_500M("Row 500m", "sec", false),
    PUSH_UPS("Push-ups", "reps", true),
    PLANK_HOLD("Plank Hold", "sec", true),
    VO2_MAX("VO2 Max", "ml/kg/min", true),
    GRIP_STRENGTH("Grip Strength", "kg", true)
}

// ------------------------------------------------------------------ goals

/**
 * A goal is measurable by construction: [targetValue] and [startValue] make a
 * completion percentage legitimate. Goals without a numeric target (for
 * example "general fitness") report progress as null instead of a fake ring.
 */
data class BodyGoalRecord(
    val id: String,
    val memberId: String,
    val title: String,
    val metric: GoalMetric,
    val startValue: Double,
    val targetValue: Double?,
    val startedAtEpoch: Long,
    val active: Boolean = true,
    /**
     * Which way this goal wants the metric to move. Stored per goal rather than
     * inferred from the metric: the same metric legitimately goes UP for muscle
     * gain and DOWN for fat loss, and only the goal knows which.
     */
    val direction: GoalDirection = GoalDirection.fromTarget(metric, startValue, targetValue),
    /** Null when the goal is qualitative and has no measurable denominator. */
    val note: String? = null
) {
    val higherIsBetter: Boolean get() = direction == GoalDirection.INCREASE
}

enum class GoalDirection { INCREASE, DECREASE, HOLD;

    companion object {
        /** Inferred from start vs target when the caller does not say. */
        fun fromTarget(metric: GoalMetric, start: Double, target: Double?): GoalDirection {
            if (target == null) return GoalDirection.HOLD
            return if (target > start) GoalDirection.INCREASE else GoalDirection.DECREASE
        }
    }
}

enum class GoalMetric(val label: String, val unit: String, val higherIsBetter: Boolean) {
    WEIGHT("Weight", "kg", false),
    BODY_FAT("Body Fat", "%", false),
    MUSCLE_MASS("Muscle Mass", "kg", true),
    STRENGTH("Strength", "kg", true),
    WAIST("Waist", "in", false),
    FIVE_K_TIME("5K Time", "min", false),
    RUN_DISTANCE("Run Distance", "km", true),
    ENDURANCE("Endurance", "", true)
}

// ------------------------------------------------------------------ privacy

/** Consent governs optional body data; hiding a field is not authorization. */
data class BodyDataConsent(
    val memberId: String,
    val allowsBodyComposition: Boolean = true,
    val allowsProgressPhotos: Boolean = true,
    val visibleTo: Set<ViewerRole> = setOf(ViewerRole.SELF, ViewerRole.OWNER, ViewerRole.STAFF)
) {
    fun permits(role: ViewerRole, metric: BodyMetric): Boolean = when (metric) {
        BodyMetric.WEIGHT, BodyMetric.BMI -> true
        else -> allowsBodyComposition && role in visibleTo
    }
}

enum class ViewerRole { SELF, OWNER, STAFF, PUBLIC }

// ------------------------------------------------------------------ aggregates

/** Everything the analytics layer needs for one member, already filtered. */
data class BodyProgressDataset(
    val memberId: String,
    val sessions: List<WorkoutSessionRecord>,
    val measurements: List<BodyMeasurement>,
    val fitnessTests: List<FitnessTestRecord>,
    val goals: List<BodyGoalRecord>,
    val consent: BodyDataConsent,
    val anchor: LocalDate = LocalDate.now()
) {
    fun effectiveSets(): List<WorkoutSetRecord> = sessions.flatMap { it.effectiveSets }
}
