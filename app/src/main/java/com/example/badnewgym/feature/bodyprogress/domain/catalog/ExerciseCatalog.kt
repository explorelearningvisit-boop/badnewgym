package com.example.badnewgym.feature.bodyprogress.domain.catalog

import com.example.badnewgym.feature.bodyprogress.domain.model.EquipmentCategory
import com.example.badnewgym.feature.bodyprogress.domain.model.Exercise
import com.example.badnewgym.feature.bodyprogress.domain.model.MovementPattern
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup as M

/**
 * Exercise catalogue.
 *
 * Extensible by design: admins add or retire entries here (or through the
 * catalog API) and every analytic follows automatically - no new Home widget
 * is ever needed for a new exercise.
 *
 * Comparison rules:
 *  - `variationKey` = never compare across (e.g. dumbbell vs machine curl).
 *  - `progressionKey` = safe to compare within (same lift family).
 */
object ExerciseCatalog {

    val all: List<Exercise> = listOf(
        // ---- horizontal push
        Exercise(
            id = "bench_press_barbell", name = "Bench Press",
            aliases = listOf("Flat Barbell Bench Press"),
            progressionKey = "bench_press",
            primaryMuscles = listOf(M.CHEST), secondaryMuscles = listOf(M.TRICEPS, M.SHOULDERS),
            pattern = MovementPattern.HORIZONTAL_PUSH, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "bench_press_dumbbell", name = "Dumbbell Bench Press",
            progressionKey = "bench_press",
            primaryMuscles = listOf(M.CHEST), secondaryMuscles = listOf(M.TRICEPS),
            pattern = MovementPattern.HORIZONTAL_PUSH, equipment = EquipmentCategory.DUMBBELL, tracksLoad = true
        ),
        Exercise(
            id = "bench_press_machine", name = "Machine Bench Press",
            progressionKey = "bench_press",
            primaryMuscles = listOf(M.CHEST), secondaryMuscles = listOf(M.TRICEPS),
            pattern = MovementPattern.HORIZONTAL_PUSH, equipment = EquipmentCategory.MACHINE, tracksLoad = true
        ),
        Exercise(
            id = "incline_press_db", name = "Incline Dumbbell Press",
            primaryMuscles = listOf(M.CHEST, M.SHOULDERS), secondaryMuscles = listOf(M.TRICEPS),
            pattern = MovementPattern.HORIZONTAL_PUSH, equipment = EquipmentCategory.DUMBBELL, tracksLoad = true
        ),
        Exercise(
            id = "chest_fly_cable", name = "Chest Fly",
            primaryMuscles = listOf(M.CHEST), secondaryMuscles = listOf(M.SHOULDERS),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.CABLE, tracksLoad = true
        ),
        Exercise(
            id = "push_up", name = "Push-up",
            variationKey = "push_up_bodyweight",
            primaryMuscles = listOf(M.CHEST, M.TRICEPS), secondaryMuscles = listOf(M.CORE),
            pattern = MovementPattern.HORIZONTAL_PUSH, equipment = EquipmentCategory.BODYWEIGHT, tracksLoad = true
        ),
        Exercise(
            id = "dips", name = "Dips",
            primaryMuscles = listOf(M.CHEST, M.TRICEPS), secondaryMuscles = listOf(M.SHOULDERS),
            pattern = MovementPattern.VERTICAL_PUSH, equipment = EquipmentCategory.BODYWEIGHT, tracksLoad = true
        ),

        // ---- vertical push
        Exercise(
            id = "shoulder_press_db", name = "Shoulder Press",
            progressionKey = "shoulder_press",
            primaryMuscles = listOf(M.SHOULDERS), secondaryMuscles = listOf(M.TRICEPS, M.TRAPS),
            pattern = MovementPattern.VERTICAL_PUSH, equipment = EquipmentCategory.DUMBBELL, tracksLoad = true
        ),
        Exercise(
            id = "overhead_press_barbell", name = "Overhead Press",
            progressionKey = "shoulder_press",
            primaryMuscles = listOf(M.SHOULDERS), secondaryMuscles = listOf(M.TRICEPS, M.CORE),
            pattern = MovementPattern.VERTICAL_PUSH, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "lateral_raise", name = "Lateral Raise",
            primaryMuscles = listOf(M.SHOULDERS), secondaryMuscles = emptyList(),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.DUMBBELL, tracksLoad = true
        ),
        Exercise(
            id = "rear_delt_fly", name = "Rear Delt Fly",
            primaryMuscles = listOf(M.REAR_DELTS), secondaryMuscles = listOf(M.SHOULDERS),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.DUMBBELL, tracksLoad = true
        ),

        // ---- pull
        Exercise(
            id = "lat_pulldown", name = "Lat Pulldown",
            progressionKey = "vertical_pull",
            primaryMuscles = listOf(M.LATS), secondaryMuscles = listOf(M.BICEPS, M.REAR_DELTS),
            pattern = MovementPattern.VERTICAL_PULL, equipment = EquipmentCategory.CABLE, tracksLoad = true
        ),
        Exercise(
            id = "pull_up", name = "Pull-up",
            variationKey = "pull_up_bodyweight",
            primaryMuscles = listOf(M.LATS, M.BICEPS), secondaryMuscles = listOf(M.FOREARMS, M.CORE),
            pattern = MovementPattern.VERTICAL_PULL, equipment = EquipmentCategory.BODYWEIGHT, tracksLoad = true
        ),
        Exercise(
            id = "barbell_row", name = "Barbell Row",
            progressionKey = "horizontal_pull",
            primaryMuscles = listOf(M.BACK, M.LATS), secondaryMuscles = listOf(M.BICEPS, M.LOWER_BACK),
            pattern = MovementPattern.HORIZONTAL_PULL, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "seated_row_cable", name = "Seated Cable Row",
            progressionKey = "horizontal_pull",
            primaryMuscles = listOf(M.BACK), secondaryMuscles = listOf(M.LATS, M.BICEPS),
            pattern = MovementPattern.HORIZONTAL_PULL, equipment = EquipmentCategory.CABLE, tracksLoad = true
        ),
        Exercise(
            id = "face_pull", name = "Face Pull",
            primaryMuscles = listOf(M.REAR_DELTS, M.TRAPS), secondaryMuscles = emptyList(),
            pattern = MovementPattern.HORIZONTAL_PULL, equipment = EquipmentCategory.CABLE, tracksLoad = true
        ),

        // ---- arms
        Exercise(
            id = "biceps_curl_db", name = "Biceps Curl (DB)",
            variationKey = "curl_db",
            progressionKey = "curl",
            primaryMuscles = listOf(M.BICEPS), secondaryMuscles = listOf(M.FOREARMS),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.DUMBBELL, tracksLoad = true
        ),
        Exercise(
            id = "biceps_curl_barbell", name = "Barbell Curl",
            variationKey = "curl_barbell",
            progressionKey = "curl",
            primaryMuscles = listOf(M.BICEPS), secondaryMuscles = emptyList(),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "hammer_curl", name = "Hammer Curl",
            primaryMuscles = listOf(M.BICEPS, M.FOREARMS), secondaryMuscles = emptyList(),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.DUMBBELL, tracksLoad = true
        ),
        Exercise(
            id = "triceps_pushdown", name = "Triceps Pushdown",
            progressionKey = "triceps_extension",
            primaryMuscles = listOf(M.TRICEPS), secondaryMuscles = emptyList(),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.CABLE, tracksLoad = true
        ),
        Exercise(
            id = "skullcrusher", name = "Skullcrusher",
            variationKey = "triceps_extension_barbell",
            progressionKey = "triceps_extension",
            primaryMuscles = listOf(M.TRICEPS), secondaryMuscles = emptyList(),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "overhead_triceps_ext", name = "Overhead Triceps Extension",
            variationKey = "triceps_extension_cable",
            progressionKey = "triceps_extension",
            primaryMuscles = listOf(M.TRICEPS), secondaryMuscles = emptyList(),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.DUMBBELL, tracksLoad = true
        ),

        // ---- legs
        Exercise(
            id = "back_squat", name = "Back Squat",
            progressionKey = "squat",
            primaryMuscles = listOf(M.QUADS, M.GLUTES), secondaryMuscles = listOf(M.HAMSTRINGS, M.LOWER_BACK, M.CALVES),
            pattern = MovementPattern.SQUAT, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "leg_press", name = "Leg Press",
            progressionKey = "squat",
            primaryMuscles = listOf(M.QUADS, M.GLUTES), secondaryMuscles = listOf(M.HAMSTRINGS),
            pattern = MovementPattern.SQUAT, equipment = EquipmentCategory.MACHINE, tracksLoad = true
        ),
        Exercise(
            id = "front_squat", name = "Front Squat",
            primaryMuscles = listOf(M.QUADS), secondaryMuscles = listOf(M.GLUTES, M.CORE),
            pattern = MovementPattern.SQUAT, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "romanian_deadlift", name = "Romanian Deadlift",
            progressionKey = "hinge",
            primaryMuscles = listOf(M.HAMSTRINGS, M.GLUTES), secondaryMuscles = listOf(M.LOWER_BACK),
            pattern = MovementPattern.HINGE, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "deadlift", name = "Deadlift",
            primaryMuscles = listOf(M.GLUTES, M.HAMSTRINGS), secondaryMuscles = listOf(M.BACK, M.LOWER_BACK),
            pattern = MovementPattern.HINGE, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "lunge_db", name = "Dumbbell Lunge",
            primaryMuscles = listOf(M.QUADS, M.GLUTES), secondaryMuscles = listOf(M.HAMSTRINGS),
            pattern = MovementPattern.LUNGE, equipment = EquipmentCategory.DUMBBELL, tracksLoad = true
        ),
        Exercise(
            id = "hip_thrust", name = "Hip Thrust",
            primaryMuscles = listOf(M.GLUTES), secondaryMuscles = listOf(M.HAMSTRINGS),
            pattern = MovementPattern.HINGE, equipment = EquipmentCategory.BARBELL, tracksLoad = true
        ),
        Exercise(
            id = "leg_extension", name = "Leg Extension",
            primaryMuscles = listOf(M.QUADS), secondaryMuscles = emptyList(),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.MACHINE, tracksLoad = true
        ),
        Exercise(
            id = "leg_curl", name = "Leg Curl",
            primaryMuscles = listOf(M.HAMSTRINGS), secondaryMuscles = emptyList(),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.MACHINE, tracksLoad = true
        ),
        Exercise(
            id = "calf_raise", name = "Calf Raise",
            progressionKey = "calf_raise",
            primaryMuscles = listOf(M.CALVES), secondaryMuscles = emptyList(),
            pattern = MovementPattern.ISOLATION, equipment = EquipmentCategory.MACHINE, tracksLoad = true
        ),

        // ---- core
        Exercise(
            id = "plank", name = "Plank",
            primaryMuscles = listOf(M.CORE), secondaryMuscles = listOf(M.SHOULDERS),
            pattern = MovementPattern.CORE, equipment = EquipmentCategory.BODYWEIGHT,
            tracksDuration = true
        ),
        Exercise(
            id = "hanging_leg_raise", name = "Hanging Leg Raise",
            primaryMuscles = listOf(M.CORE), secondaryMuscles = listOf(M.HIP_FLEXORS),
            pattern = MovementPattern.CORE, equipment = EquipmentCategory.BODYWEIGHT, tracksLoad = true
        ),
        Exercise(
            id = "cable_crunch", name = "Cable Crunch",
            primaryMuscles = listOf(M.CORE), secondaryMuscles = emptyList(),
            pattern = MovementPattern.CORE, equipment = EquipmentCategory.CABLE, tracksLoad = true
        ),

        // ---- conditioning
        Exercise(
            id = "treadmill_run", name = "Running",
            primaryMuscles = listOf(M.CALVES), secondaryMuscles = listOf(M.QUADS, M.HAMSTRINGS, M.GLUTES),
            pattern = MovementPattern.CARDIO, equipment = EquipmentCategory.CARDIO_MACHINE,
            isCardio = true, tracksDistance = true, tracksDuration = true
        ),
        Exercise(
            id = "outdoor_run", name = "Outdoor Run",
            progressionKey = "running",
            primaryMuscles = listOf(M.CALVES), secondaryMuscles = listOf(M.QUADS, M.HAMSTRINGS),
            pattern = MovementPattern.CARDIO, equipment = EquipmentCategory.BODYWEIGHT,
            isCardio = true, tracksDistance = true, tracksDuration = true
        ),
        Exercise(
            id = "rowing_erg", name = "Rowing",
            primaryMuscles = listOf(M.BACK), secondaryMuscles = listOf(M.QUADS, M.CORE),
            pattern = MovementPattern.CARDIO, equipment = EquipmentCategory.CARDIO_MACHINE,
            isCardio = true, tracksDistance = true, tracksDuration = true
        ),
        Exercise(
            id = "cycling_erg", name = "Cycling",
            primaryMuscles = listOf(M.QUADS), secondaryMuscles = listOf(M.HAMSTRINGS, M.CALVES),
            pattern = MovementPattern.CARDIO, equipment = EquipmentCategory.CARDIO_MACHINE,
            isCardio = true, tracksDistance = true, tracksDuration = true
        ),
        Exercise(
            id = "jump_rope", name = "Jump Rope",
            primaryMuscles = listOf(M.CALVES), secondaryMuscles = listOf(M.CORE),
            pattern = MovementPattern.PLYOMETRIC, equipment = EquipmentCategory.BODYWEIGHT,
            isCardio = true, tracksDuration = true
        ),
        Exercise(
            id = "rowing_500m_test", name = "Row 500m Test",
            primaryMuscles = listOf(M.BACK), secondaryMuscles = listOf(M.QUADS),
            pattern = MovementPattern.CARDIO, equipment = EquipmentCategory.CARDIO_MACHINE,
            isCardio = true, tracksDuration = true
        )
    )

    private val byId: Map<String, Exercise> = all.associateBy { it.id }

    fun byId(id: String): Exercise? = byId[id]

    fun active(): List<Exercise> = all.filter { it.isActive }

    fun byProgressionKey(key: String): List<Exercise> = all.filter { it.progressionKey == key }

    /** Catalogue rows that admins may hide, or that simply do not exist yet. */
    fun retiredIds(): Set<String> = all.filter { !it.isActive }.map { it.id }.toSet()

    /**
     * Human label for an id. Unknown ids are surfaced verbatim rather than
     * silently mapped to something plausible.
     */
    fun label(id: String): String = byId[id]?.name ?: id
}
