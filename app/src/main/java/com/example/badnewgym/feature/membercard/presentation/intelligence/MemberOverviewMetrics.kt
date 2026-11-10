package com.example.badnewgym.feature.membercard.presentation.intelligence

import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import kotlin.math.roundToInt

/** Semantic tone for an overview action row. */
enum class OverviewTone { SUCCESS, WARNING, DANGER, ACCENT }

/** One derived "do this next" row. */
data class OverviewAction(
    val title: String,
    val detail: String,
    val tone: OverviewTone
)

/**
 * Cross-module summary derived from the same domain values the four tabs render.
 * Nothing here is invented: every number is a ratio or a field already shown in a
 * module, so the Overview can never disagree with Attendance / Workout / Payment / Body.
 */
data class MemberOverviewMetrics(
    val fitnessScore: Int,
    val band: String,
    val attendancePercent: Int,
    val workoutPercent: Int,
    val paymentPercent: Int,
    val activeDays: Int,
    val avgMinutes: Int,
    val daysLeft: Int,
    val weight: String,
    val bmi: String,
    val insight: String,
    val actions: List<OverviewAction>
) {
    companion object {

        fun from(data: MemberCardData, pass: BadGymMemberPass): MemberOverviewMetrics {
            val attendance = data.attendance.percent.coerceIn(0, 100)
            val activeDays = data.workoutTrend.daysWithWorkout.coerceAtLeast(0)
            val monthDays = data.attendance.totalDays.coerceAtLeast(1)
            val workoutPct = (activeDays * 100f / monthDays).roundToInt().coerceIn(0, 100)
            val paymentPct = when {
                pass.isOverdue -> 35
                data.paymentPlan.daysLeft <= 7 -> 70
                else -> 100
            }

            val score = (attendance * 0.40f + workoutPct * 0.35f + paymentPct * 0.25f)
                .roundToInt()
                .coerceIn(0, 100)

            val band = when {
                score >= 85 -> "Excellent"
                score >= 70 -> "Good"
                score >= 55 -> "Fair"
                else -> "Needs Work"
            }

            val weakest = minOf(attendance, workoutPct, paymentPct)
            val projected = if (attendance <= workoutPct && attendance <= paymentPct) {
                (90 * 0.40f + workoutPct * 0.35f + paymentPct * 0.25f).roundToInt()
            } else {
                score.coerceAtLeast(weakest)
            }
            val insight = when {
                attendance <= workoutPct && attendance <= paymentPct ->
                    "Attendance is the weakest input at $attendance%. Reaching 90% lifts the score to $projected."
                workoutPct <= paymentPct ->
                    "Consistency is the weakest input at $workoutPct%. Two extra sessions a week would lift the score to $projected."
                else ->
                    "Payment is the weakest input at $paymentPct%. Clearing the balance restores the full score."
            }

            val actions = buildList {
                add(
                    if (pass.isOverdue) {
                        OverviewAction(
                            title = "Clear the outstanding balance",
                            detail = "${pass.overdueDays} day(s) overdue",
                            tone = OverviewTone.DANGER
                        )
                    } else if (pass.daysLeft <= 8) {
                        OverviewAction(
                            title = "Renew membership",
                            detail = "${pass.daysLeft} days left on ${pass.planName}",
                            tone = OverviewTone.WARNING
                        )
                    } else {
                        OverviewAction(
                            title = "Membership is on track",
                            detail = "${pass.daysLeft} days remaining",
                            tone = OverviewTone.SUCCESS
                        )
                    }
                )
                add(
                    if (attendance < 90) {
                        OverviewAction(
                            title = "Lift attendance",
                            detail = "Target ${(attendance + 8).coerceAtMost(100)}% from $attendance%",
                            tone = OverviewTone.ACCENT
                        )
                    } else {
                        OverviewAction(
                            title = "Attendance is excellent",
                            detail = "$attendance% this month",
                            tone = OverviewTone.SUCCESS
                        )
                    }
                )
                add(
                    if (activeDays < 20) {
                        OverviewAction(
                            title = if (20 - activeDays == 1) "Add 1 workout day" else "Add ${20 - activeDays} workout days",
                            detail = "Aim for five sessions a week",
                            tone = OverviewTone.ACCENT
                        )
                    } else {
                        OverviewAction(
                            title = "Workout rhythm is solid",
                            detail = "$activeDays active days this month",
                            tone = OverviewTone.SUCCESS
                        )
                    }
                )
            }

            return MemberOverviewMetrics(
                fitnessScore = score,
                band = band,
                attendancePercent = attendance,
                workoutPercent = workoutPct,
                paymentPercent = paymentPct,
                activeDays = activeDays,
                avgMinutes = data.workoutTrend.avgMinutes,
                daysLeft = pass.daysLeft,
                weight = pass.weight,
                bmi = pass.bmi,
                insight = insight,
                actions = actions
            )
        }
    }
}
