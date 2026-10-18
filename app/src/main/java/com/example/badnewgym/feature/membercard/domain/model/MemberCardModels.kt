package com.example.badnewgym.feature.membercard.domain.model

data class MemberProfile(
    val name: String,
    val memberCode: String,
    val age: Int,
    val gender: String,
    val isPrime: Boolean = true,
    val isActive: Boolean = true,
    val attendancePercent: Int = 75,
    val attendedDays: Int = 25,
    val totalDays: Int = 31
)

enum class BodyStatType {
    WEIGHT,
    MUSCLE,
    BODY_FAT,
    BMI
}

data class BodyStatItem(
    val type: BodyStatType,
    val value: String,
    val subText: String,
    val isFavorable: Boolean = true
)

data class PaymentPlanState(
    val amountPaid: String,
    val paymentStatus: String,
    val nextDueDate: String,
    val daysLeft: Int,
    val isPrime: Boolean = true
)

enum class ActivityDayType {
    PRESENT,
    ABSENT,
    OFF,
    LATE
}

data class DailyWorkoutEntry(
    val dayNumber: Int,
    val minutes: Int,
    val type: ActivityDayType
)

data class AttendanceWorkoutSummary(
    val monthLabel: String,
    val dailyEntries: List<DailyWorkoutEntry>,
    val avgPerDayMinutes: Int,
    val peakMinutes: Int,
    val peakDate: String,
    val lowestMinutes: Int,
    val lowestDate: String,
    val presentCount: Int,
    val presentPercent: Int,
    val absentCount: Int,
    val absentPercent: Int,
    val offCount: Int,
    val offPercent: Int,
    val lateCount: Int,
    val latePercent: Int
)

data class MemberCardData(
    val profile: MemberProfile,
    val bodyStats: List<BodyStatItem>,
    val paymentPlan: PaymentPlanState,
    val attendanceWorkout: AttendanceWorkoutSummary
) {
    companion object {
        fun sampleAmanTripathi(): MemberCardData {
            val days = mutableListOf<DailyWorkoutEntry>()
            // 31 days data modeled directly on reference visual chart
            val workoutPattern = listOf(
                DailyWorkoutEntry(1, 18, ActivityDayType.PRESENT),
                DailyWorkoutEntry(2, 42, ActivityDayType.PRESENT),
                DailyWorkoutEntry(3, 35, ActivityDayType.PRESENT),
                DailyWorkoutEntry(4, 28, ActivityDayType.PRESENT),
                DailyWorkoutEntry(5, 15, ActivityDayType.ABSENT),
                DailyWorkoutEntry(6, 0, ActivityDayType.OFF),
                DailyWorkoutEntry(7, 22, ActivityDayType.PRESENT),
                DailyWorkoutEntry(8, 32, ActivityDayType.PRESENT),
                DailyWorkoutEntry(9, 48, ActivityDayType.PRESENT),
                DailyWorkoutEntry(10, 50, ActivityDayType.PRESENT),
                DailyWorkoutEntry(11, 30, ActivityDayType.PRESENT),
                DailyWorkoutEntry(12, 28, ActivityDayType.PRESENT),
                DailyWorkoutEntry(13, 20, ActivityDayType.PRESENT),
                DailyWorkoutEntry(14, 0, ActivityDayType.OFF),
                DailyWorkoutEntry(15, 0, ActivityDayType.OFF),
                DailyWorkoutEntry(16, 12, ActivityDayType.PRESENT),
                DailyWorkoutEntry(17, 20, ActivityDayType.PRESENT),
                DailyWorkoutEntry(18, 28, ActivityDayType.PRESENT),
                DailyWorkoutEntry(19, 58, ActivityDayType.PRESENT),
                DailyWorkoutEntry(20, 52, ActivityDayType.PRESENT),
                DailyWorkoutEntry(21, 42, ActivityDayType.PRESENT),
                DailyWorkoutEntry(22, 25, ActivityDayType.PRESENT),
                DailyWorkoutEntry(23, 0, ActivityDayType.OFF),
                DailyWorkoutEntry(24, 0, ActivityDayType.OFF),
                DailyWorkoutEntry(25, 18, ActivityDayType.PRESENT),
                DailyWorkoutEntry(26, 28, ActivityDayType.PRESENT),
                DailyWorkoutEntry(27, 24, ActivityDayType.PRESENT),
                DailyWorkoutEntry(28, 45, ActivityDayType.PRESENT),
                DailyWorkoutEntry(29, 20, ActivityDayType.PRESENT),
                DailyWorkoutEntry(30, 30, ActivityDayType.PRESENT),
                DailyWorkoutEntry(31, 48, ActivityDayType.PRESENT)
            )
            days.addAll(workoutPattern)

            return MemberCardData(
                profile = MemberProfile(
                    name = "Aman Tripathi",
                    memberCode = "BG305",
                    age = 23,
                    gender = "Male",
                    isPrime = true,
                    isActive = true,
                    attendancePercent = 75,
                    attendedDays = 25,
                    totalDays = 31
                ),
                bodyStats = listOf(
                    BodyStatItem(
                        type = BodyStatType.WEIGHT,
                        value = "69.5",
                        subText = "↓ kg 2.5",
                        isFavorable = true
                    ),
                    BodyStatItem(
                        type = BodyStatType.MUSCLE,
                        value = "57.2",
                        subText = "kg ↑ 1.4",
                        isFavorable = true
                    ),
                    BodyStatItem(
                        type = BodyStatType.BODY_FAT,
                        value = "17.6%",
                        subText = "↓ 1.2",
                        isFavorable = true
                    ),
                    BodyStatItem(
                        type = BodyStatType.BMI,
                        value = "22.8",
                        subText = "Normal",
                        isFavorable = true
                    )
                ),
                paymentPlan = PaymentPlanState(
                    amountPaid = "₹2,800",
                    paymentStatus = "Paid • On Time",
                    nextDueDate = "30 Sep 2026",
                    daysLeft = 25,
                    isPrime = true
                ),
                attendanceWorkout = AttendanceWorkoutSummary(
                    monthLabel = "Oct 2026",
                    dailyEntries = days,
                    avgPerDayMinutes = 48,
                    peakMinutes = 65,
                    peakDate = "10 Oct",
                    lowestMinutes = 12,
                    lowestDate = "16 Oct",
                    presentCount = 24,
                    presentPercent = 77,
                    absentCount = 3,
                    absentPercent = 10,
                    offCount = 2,
                    offPercent = 6,
                    lateCount = 2,
                    latePercent = 6
                )
            )
        }
    }
}
