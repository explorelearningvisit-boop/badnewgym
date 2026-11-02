package com.example.badnewgym.feature.membercard.data

import com.example.badnewgym.feature.bodyprogress.domain.intelligence.ActivityDay
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyIntelligenceInput
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyMemberProfile
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.CheckInRecord
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.CompositionReading
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.PrivacyCategory
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.StrengthSetRecord
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.TrainingGoal
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.MeasurementEntry
import com.example.badnewgym.feature.membercard.domain.model.MeasurementSite
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import kotlin.math.roundToInt

/**
 * Adapts the signed-in member's own records into the Body Intelligence input.
 *
 * This is the single place where the Body tab reads member data. Values are
 * copied, never derived: a metric that the member has not recorded simply does
 * not appear, and no series is reconstructed from another metric.
 */
object MemberBodyIntelligenceMapper {

    fun from(data: MemberCardData): BodyIntelligenceInput {
        val calendar = data.attendance.calendarDays
        val anchorDay = calendar.maxOfOrNull { it.dayOfMonth } ?: 0

        return BodyIntelligenceInput(
            memberId = data.profile.memberCode,
            memberName = data.profile.name,
            profile = BodyMemberProfile(
                name = data.profile.name,
                memberId = data.profile.memberCode,
                age = data.profile.age.takeIf { it > 0 }?.toString(),
                gender = data.profile.gender.takeIf { it.isNotBlank() },
                status = if (data.profile.isActive) "Active" else "Inactive",
                plan = data.paymentPlan.planName.substringBefore('(').trim()
                    .ifBlank { data.profile.tierName },
                goalLabel = data.profile.goal.takeIf { it.isNotBlank() },
                dueAmount = data.paymentPlan.currentDueAmount
                    .ifBlank { data.paymentPlan.dueAlertAmount }
                    .takeIf { it.isNotBlank() },
                dueDate = data.paymentPlan.currentDueDate
                    .ifBlank { data.paymentPlan.dueAlertDate }
                    .takeIf { it.isNotBlank() }
            ),
            goal = TrainingGoal.fromLabel(data.profile.goal),
            readings = data.bodyStats.mapNotNull { it.toReading() } +
                data.measurements.mapNotNull { it.toReading() },
            activityDays = data.workoutTrend.bars.map { ActivityDay(day = it.dayNumber, minutes = it.minutes) },
            checkInDays = calendar.map { day ->
                CheckInRecord(
                    day = day.dayOfMonth,
                    present = day.status == DayAttendanceStatus.PRESENT || day.status == DayAttendanceStatus.LATE,
                    late = day.status == DayAttendanceStatus.LATE,
                    holiday = day.status == DayAttendanceStatus.HOLIDAY,
                    dayOfWeekIndex = day.dayOfWeekIndex
                )
            },
            monthLabel = data.attendance.monthLabel,
            anchorDay = anchorDay,
            heightLabel = null,
            // Logged sets are the only thing that can turn a strength row from
            // "Not started" into a load and a rep count.
            strengthSets = data.strengthSets.map {
                StrengthSetRecord(
                    exerciseId = it.exerciseId,
                    day = it.day,
                    loadKg = it.loadKg,
                    reps = it.reps,
                    setNumber = it.setNumber
                )
            },
            hasSetLevelRecords = data.strengthSets.isNotEmpty()
        )
    }

    private fun BodyStatItem.toReading(): CompositionReading? {
        val metric = type.toMetric() ?: return null
        val parsed = numericValue() ?: return null
        return CompositionReading(
            metric = metric,
            value = parsed,
            unit = unit.ifBlank { metric.unit },
            deltaLabel = delta.takeIf { it.isNotBlank() },
            deltaIsFavourable = isFavorable,
            trend = sparklinePoints,
            history = history.map { it.monthLabel to it.value },
            goalLabel = goalText.takeIf { it.isNotBlank() },
            sourceLabel = "Member app",
            privacy = type.toPrivacy()
        )
    }

    /** "17.6%" -> 17.6, "69.5" -> 69.5. Returns null for a non-numeric card. */
    private fun BodyStatItem.numericValue(): Double? =
        value.replace("%", "")
            .replace(",", "")
            .replace(" ", "")
            .toDoubleOrNull()
            ?.let { (it * 10).roundToInt() / 10.0 }

    private fun BodyStatType.toMetric(): BodyMetric? = when (this) {
        BodyStatType.WEIGHT -> BodyMetric.WEIGHT
        BodyStatType.MUSCLE -> BodyMetric.MUSCLE_MASS
        BodyStatType.BODY_FAT -> BodyMetric.BODY_FAT
        BodyStatType.BMI -> BodyMetric.BMI
    }

    private fun BodyStatType.toPrivacy(): PrivacyCategory = when (this) {
        BodyStatType.WEIGHT -> PrivacyCategory.WEIGHT
        BodyStatType.MUSCLE -> PrivacyCategory.MUSCLE_MASS
        BodyStatType.BODY_FAT -> PrivacyCategory.BODY_FAT
        BodyStatType.BMI -> PrivacyCategory.BMI
    }

    /**
     * Tape measurements enter the same reading stream as composition so privacy
     * filtering applies to them identically, but they stay out of [BodyStatType]:
     * centimetres must not be plotted by the widgets that assume kilograms.
     */
    private fun MeasurementEntry.toReading(): CompositionReading? {
        val metric = site.toMetric() ?: return null
        return CompositionReading(
            metric = metric,
            value = value,
            unit = unit,
            deltaLabel = delta.takeIf { it.isNotBlank() },
            deltaIsFavourable = isFavorable,
            trend = sparklinePoints,
            goalLabel = goalText.takeIf { it.isNotBlank() },
            sourceLabel = "Trainer assessment",
            privacy = site.toPrivacy()
        )
    }

    private fun MeasurementSite.toMetric(): BodyMetric? = when (this) {
        MeasurementSite.CHEST -> BodyMetric.CHEST
        MeasurementSite.WAIST -> BodyMetric.WAIST
        MeasurementSite.ARM -> BodyMetric.ARM
        MeasurementSite.THIGH, MeasurementSite.CALF, MeasurementSite.NECK -> null
    }

    private fun MeasurementSite.toPrivacy(): PrivacyCategory = when (this) {
        MeasurementSite.CHEST -> PrivacyCategory.CHEST
        MeasurementSite.WAIST -> PrivacyCategory.WAIST
        MeasurementSite.ARM -> PrivacyCategory.ARMS
        MeasurementSite.THIGH, MeasurementSite.CALF, MeasurementSite.NECK -> PrivacyCategory.LEGS
    }
}
