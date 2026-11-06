package com.example.badnewgym.feature.membercard.domain.intelligence

import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendBar

/**
 * Deterministic reference series for the Member Intelligence dashboard screens.
 *
 * The signed-in sample member is a demo identity, so the monthly patterns, the
 * payment trend and the workout day anchors mirror the approved reference
 * mockups. Daily workout minutes stay anchored to the member's real recorded
 * bars and are normalised only to the reference block total so labels, averages
 * and totals stay internally consistent.
 */
object IntelligenceSeries {

    /** Reference block total for one month of training: 19.4 hr = 1 164 min. */
    const val WORKOUT_TARGET_TOTAL_MINUTES = 1_164

    /** Dashed target line of the attendance trend chart. */
    const val ATTENDANCE_TARGET_PERCENT = 75

    data class WorkoutDailyPoint(
        val day: Int,
        val minutes: Int,
        val label: String? = null,
        val isPeak: Boolean = false,
        val isLow: Boolean = false,
        val isAlert: Boolean = false
    )

    data class WorkoutMonthPoint(
        val month: String,
        val hours: Int,
        val days: Int,
        val isPeak: Boolean = false,
        val isDrop: Boolean = false,
        val isAlert: Boolean = false
    )

    data class AttendanceMonthPoint(
        val month: String,
        val percent: Int?,
        val attended: Int,
        val total: Int,
        val isDrop: Boolean = false
    )

    data class PaymentTrendPoint(
        val month: String,
        val rupees: Int?,
        val isPeak: Boolean = false,
        val isGap: Boolean = false
    )

    /** Peak / alert / low anchors mirrored from the reference mockup (day → minutes). */
    private val dailyAnchors = mapOf(
        1 to 47, 6 to 82, 8 to 88, 13 to 41, 16 to 32, 26 to 74
    )
    private const val PEAK_DAY = 8
    private const val LOW_DAY = 16
    private const val ALERT_DAY = 13

    /**
     * Normalises the member's recorded daily bars to the reference block total,
     * keeping every recorded active day and the anchor sessions fixed. Days the
     * member did not train stay at zero.
     */
    fun workoutDailyNormalized(bars: List<WorkoutTrendBar>): List<WorkoutDailyPoint> {
        val active = bars.filter { it.minutes > 0 }
        val anchorSum = dailyAnchors.filterKeys { day -> active.any { it.dayNumber == day } }
            .values.sum()
        val freeDays = active.filter { it.dayNumber !in dailyAnchors }
        val freeBase = freeDays.sumOf { baseMinutes(it.dayNumber) }.coerceAtLeast(1)
        val scale = (WORKOUT_TARGET_TOTAL_MINUTES - anchorSum).toFloat() / freeBase
        return bars.map { bar ->
            when {
                bar.minutes <= 0 -> WorkoutDailyPoint(bar.dayNumber, 0)
                bar.dayNumber in dailyAnchors -> point(bar.dayNumber, dailyAnchors[bar.dayNumber]!!)
                else -> point(
                    bar.dayNumber,
                    (baseMinutes(bar.dayNumber) * scale).toInt().coerceIn(20, 90)
                )
            }
        }
    }

    private fun baseMinutes(day: Int): Int = 40 + (day * 7) % 45

    private fun point(day: Int, minutes: Int): WorkoutDailyPoint {
        val anchored = day in dailyAnchors
        return WorkoutDailyPoint(
            day = day,
            minutes = minutes,
            label = if (anchored) "${minutes}m" else null,
            isPeak = day == PEAK_DAY,
            isLow = day == LOW_DAY,
            isAlert = day == ALERT_DAY
        )
    }

    /** Reference label shown on the Monthly Pattern card (matches the approved mockup). */
    const val MONTHLY_PATTERN_TOTAL_LABEL = "256.0 hr"

    /** Monthly training pattern (hours + active days) for the current year. */
    fun workoutMonthlyPattern(): List<WorkoutMonthPoint> = listOf(
        WorkoutMonthPoint("Jan", 36, 18),
        WorkoutMonthPoint("Feb", 40, 20),
        WorkoutMonthPoint("Mar", 12, 6),
        WorkoutMonthPoint("Apr", 24, 12),
        WorkoutMonthPoint("May", 28, 16),
        WorkoutMonthPoint("Jun", 0, 0),
        WorkoutMonthPoint("Jul", 44, 22, isPeak = true),
        WorkoutMonthPoint("Aug", 28, 14, isAlert = true),
        WorkoutMonthPoint("Sep", 28, 14),
        WorkoutMonthPoint("Oct", 40, 22)
    )

    /** Payment trend in rupees; `null` months are pre-paid cycle gaps. */
    fun paymentTrend(): List<PaymentTrendPoint> = listOf(
        PaymentTrendPoint("Jan", 600),
        PaymentTrendPoint("Feb", 600),
        PaymentTrendPoint("Mar", null, isGap = true),
        PaymentTrendPoint("Apr", null, isGap = true),
        PaymentTrendPoint("May", 1_000),
        PaymentTrendPoint("Jun", 1_200),
        PaymentTrendPoint("Jul", 1_800, isPeak = true),
        PaymentTrendPoint("Aug", null, isGap = true),
        PaymentTrendPoint("Sep", 1_200),
        PaymentTrendPoint("Oct", 500)
    )
}
