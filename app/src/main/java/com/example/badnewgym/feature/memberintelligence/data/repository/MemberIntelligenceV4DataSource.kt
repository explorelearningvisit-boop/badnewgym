package com.example.badnewgym.feature.memberintelligence.data.repository

import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceWindow
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyGoal
import com.example.badnewgym.feature.memberintelligence.domain.model.GoalDirection
import com.example.badnewgym.feature.memberintelligence.domain.model.GoalKind
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricHistory
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricKey
import com.example.badnewgym.feature.memberintelligence.domain.model.MetricReading
import com.example.badnewgym.feature.memberintelligence.domain.model.MuscleGroup
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation
import com.example.badnewgym.feature.memberintelligence.domain.model.ProgressPhoto
import com.example.badnewgym.feature.memberintelligence.domain.model.TrainerSession
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutMonth
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutSession
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutType
import java.time.LocalDate


/**
 * V4 operational datasets.
 *
 * This is the data layer. The UI consumes these records and derives charts from
 * them - it never invents values. In production these come from Firestore /
 * the gym backend; here they are produced deterministically so the app has a
 * real dataset to render instead of UI-level literals.
 *
 * Everything is generated from a seeded pattern so screenshots are stable and
 * tests are reproducible.
 */
class MemberIntelligenceV4DataSource {

    private val today: LocalDate get() = LocalDate.now()

    /**
     * Rolling 30 day attendance window ending at [anchor].
     *
     * A calendar month would leave the widget nearly empty early in the month
     * (only 5 days on the 5th), so the window always carries a full month of
     * real observations. Every day in the window is elapsed; the card renders
     * them as two fixed rows of 15 so nothing scrolls horizontally or
     * vertically. Cells keep their true day-of-month number.
     */
    fun attendanceWindow(anchor: LocalDate = today, seed: Int = 7): AttendanceWindow {
        val days = (29 downTo 0).map { back ->
            val date = anchor.minusDays(back.toLong())
            val state = when {
                date.dayOfWeek.value == 1 -> AttendanceDayState.HOLIDAY          // Monday closed
                (date.toEpochDay().toInt() + seed) % 9 == 0 -> AttendanceDayState.ABSENT
                (date.toEpochDay().toInt() + seed) % 7 == 3 -> AttendanceDayState.LATE
                else -> AttendanceDayState.PRESENT
            }
            AttendanceDay(
                epochDay = date.toEpochDay(),
                dayOfMonth = date.dayOfMonth,
                state = state,
                visitMinutes = if (state == AttendanceDayState.PRESENT || state == AttendanceDayState.LATE)
                    45 + ((kotlin.math.abs(date.toEpochDay().toInt()) * 7 + seed) % 55) else null
            )
        }
        return AttendanceWindow(anchor.year, anchor.monthValue, days)
    }

    /** Real rolling 30 day workout dataset for paging and monthly summary. */
    fun workoutMonth(anchor: LocalDate = today, seed: Int = 5): WorkoutMonth {
        val sessions = (29 downTo 0).mapNotNull { back ->
            val date = anchor.minusDays(back.toLong())
            if (date.dayOfWeek.value == 1) return@mapNotNull null          // rest day
            val mix = (date.toEpochDay() + seed).toInt()
            if (mix % 6 == 0) return@mapNotNull null                       // skipped day
            val minutes = 20 + (kotlin.math.abs(mix * 17 + seed * 5) % 105)
            WorkoutSession(
                epochDay = date.toEpochDay(),
                dayOfMonth = date.dayOfMonth,
                minutes = minutes,
                type = if (mix % 3 == 0) WorkoutType.CARDIO else WorkoutType.STRENGTH,
                muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.ARMS).take(if (mix % 2 == 0) 2 else 1),
                calories = if (mix % 4 == 0) 320 + (kotlin.math.abs(mix) % 120) else null
            )
        }
        return WorkoutMonth(anchor.year, anchor.monthValue, sessions)
    }

    /**
     * Multi-goal member. Direction is explicit so "plus" is never assumed good.
     */
    fun bodyGoals(anchor: LocalDate = today, seed: Int = 3): List<BodyGoal> {
        val start = anchor.minusDays(60).toEpochDay() * 86_400_000L
        val now = anchor.atTime(9, 30).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        return listOf(
            BodyGoal(
                id = "g_weight", kind = GoalKind.MUSCLE_GAIN,
                primaryMetric = MetricKey.WEIGHT, direction = GoalDirection.INCREASE,
                startValue = 67.0, currentValue = 69.5, targetValue = 72.0,
                startedAtEpoch = start, updatedAtEpoch = now
            ),
            BodyGoal(
                id = "g_bodyfat", kind = GoalKind.BODY_FAT_REDUCTION,
                primaryMetric = MetricKey.BODY_FAT, direction = GoalDirection.DECREASE,
                startValue = 24.0, currentValue = 21.0, targetValue = 18.0,
                startedAtEpoch = start, updatedAtEpoch = now - 2 * 86_400_000L
            ),
            BodyGoal(
                id = "g_waist", kind = GoalKind.WAIST_REDUCTION,
                primaryMetric = MetricKey.WAIST, direction = GoalDirection.DECREASE,
                startValue = 36.0, currentValue = 33.5, targetValue = 32.0,
                startedAtEpoch = start, updatedAtEpoch = now - 5 * 86_400_000L
            )
        ).take(if (seed > 2) 3 else 2)
    }

    /**
     * Real measurement history with genuine non-monotonic movement so trend
     * detection has something honest to work with.
     */
    fun metricHistories(goals: List<BodyGoal>, anchor: LocalDate = today): List<MetricHistory> =
        goals.map { goal ->
            val shape = when (goal.primaryMetric) {
                MetricKey.WEIGHT -> listOf(67.0, 67.4, 67.2, 68.1, 68.0, 69.0, 68.8, 69.5)
                MetricKey.BODY_FAT -> listOf(24.0, 23.6, 23.8, 23.0, 22.4, 21.8, 21.3, 21.0)
                MetricKey.WAIST -> listOf(36.0, 35.6, 35.5, 34.8, 34.5, 34.0, 33.8, 33.5)
                else -> listOf(goal.startValue, (goal.startValue + goal.currentValue) / 2, goal.currentValue)
            }
            val startDay = anchor.minusDays(150).toEpochDay()
            MetricHistory(
                metric = goal.primaryMetric,
                readings = shape.mapIndexed { i, v -> MetricReading(goal.primaryMetric, startDay + i * 19, v) }
            )
        }

    /** Progress photos only exist where they were actually captured. */
    fun progressPhotos(anchor: LocalDate = today): List<ProgressPhoto> {
        val photos = listOf(
            0L to 72.0, 30L to 70.8, 60L to 69.9, 90L to 70.1, 120L to 69.2, 150L to 68.0
        )
        return photos.mapIndexed { i, (daysAgo, weight) ->
            val date = anchor.minusDays(daysAgo)
            ProgressPhoto(
                id = "photo_$i",
                url = PHOTO_ASSETS[i % PHOTO_ASSETS.size],
                takenAtEpoch = date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
                monthLabel = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                linkedWeight = weight,
                linkedBodyFat = if (i % 2 == 0) 24.0 - (i * 0.6) else null,
                isNew = i == photos.lastIndex
            )
        }
    }

    fun trainerSession(anchor: LocalDate = today): TrainerSession? {
        val scheduled = anchor.atTime(18, 0).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        return TrainerSession(
            trainerName = "Rahul Verma",
            trainerPhotoUrl = TRAINER_ASSET,
            focus = "Strength Session",
            scheduledAtEpoch = scheduled,
            sessionsUsed = 6,
            sessionsTotal = 12,
            contactPhone = "+91 98765 43210"
        )
    }

    /** Payment situation is derived, never declared. */
    fun paymentSituation(
        outstanding: Double,
        overdueDays: Int,
        dueAtEpoch: Long?,
        partialReceived: Double = 0.0
    ): PaymentSituation = when {
        partialReceived > 0 && partialReceived < outstanding ->
            PaymentSituation.Partial(partialReceived, outstanding - partialReceived, dueAtEpoch)
        overdueDays > 0 -> PaymentSituation.Overdue(outstanding, overdueDays, dueAtEpoch)
        outstanding > 0 && dueAtEpoch != null -> PaymentSituation.Due(outstanding, dueAtEpoch)
        else -> PaymentSituation.Settled
    }

    private companion object {
        val PHOTO_ASSETS = listOf(
            "file:///android_asset/media/member_yash.jpg",
            "file:///android_asset/media/workout_chest.jpg",
            "file:///android_asset/media/member_arjun.jpg",
            "file:///android_asset/media/trainer_vikas.jpg",
            "file:///android_asset/media/member_rohan.jpg",
            "file:///android_asset/media/gym_bg.jpg"
        )
        const val TRAINER_ASSET = "file:///android_asset/media/trainer_vikas.jpg"
    }
}
