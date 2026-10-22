package com.example.badnewgym.feature.membercard.domain.model

import androidx.compose.ui.graphics.Color

enum class MemberTier(
    val title: String,
    val durationText: String,
    val primaryColor: Color,
    val accentColor: Color,
    val badgeType: String // "crown", "diamond", "basic"
) {
    GOLD_6_MONTH("Gold Plan (6 Months)", "6 Months", Color(0xFF0070F3), Color(0xFFFFB800), "crown"),
    SPARK_DIAMOND("Spark Member", "1 Month", Color(0xFF00C48C), Color(0xFF00E5FF), "diamond"),
    BASIC_MEMBER("Basic Member", "1 Month", Color(0xFF10B981), Color(0xFF34D399), "basic")
}

data class MemberProfile(
    val greeting: String = "Good Morning 👋",
    val name: String,
    val memberCode: String,
    val age: Int,
    val gender: String,
    val tierName: String = "Spark Member",
    val tier: MemberTier = MemberTier.GOLD_6_MONTH,
    val isPrime: Boolean = true,
    val isActive: Boolean = true,
    val attendancePercent: Int = 75,
    val daysLeft: Int = 25,
    val attendedDays: Int = 25,
    val totalDays: Int = 31,
    val joinedDate: String = "01 Sep 2026",
    val goal: String = "Muscle Gain",
    val branch: String = "Varanasi",
    val unreadNotifications: Int = 3,
    val dateDisplay: String = "Tue\n22 Oct 2026"
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
    val delta: String,
    val label: String,
    val sparklinePoints: List<Float>,
    val lineColor: Color,
    val isFavorable: Boolean = true,
    val unit: String = "",
    val goalText: String = ""
)

data class MonthPaymentItem(
    val month: String,
    val amount: String = "--",
    val tierOrDiscount: String = "",
    val isCurrent: Boolean = false,
    val isOff: Boolean = false,
    val isDiscount: Boolean = false
)

data class PaymentPlanState(
    val planDuration: String = "Prime Plan (1 Month)",
    val planPeriod: String = "01 Sep – 30 Sep 2026",
    val amountPaid: String = "₹2,800",
    val paymentStatus: String = "PAID • ON TIME",
    val paidOnDate: String = "Paid on 01 Sep 2026",
    val nextDueDate: String = "30 Sep 2026",
    val daysLeft: Int = 25,
    val isPrime: Boolean = true,
    val lastPaymentAmount: String = "₹800",
    val lastPaymentDate: String = "01 Oct 2026",
    val monthlyHistory: List<MonthPaymentItem> = emptyList(),
    // New Canonical Rich Fields from user reference
    val dueAlertAmount: String = "₹2,800",
    val dueAlertText: String = "Due in 3 days ⚠️",
    val dueAlertDate: String = "16 Oct 2026",
    val planName: String = "Gold Plan (6 Months)",
    val planPeriodFormatted: String = "01 May 2026 – 31 Oct 2026",
    val planDaysUsed: Int = 135,
    val planTotalDays: Int = 180,
    val planDaysRemaining: Int = 45,
    val lastPaidAmount: String = "₹2,800",
    val lastPaidDate: String = "01 Sep 2026",
    val currentDueAmount: String = "₹2,800",
    val currentDueDate: String = "16 Oct 2026",
    val nextDueAmount: String = "₹2,800",
    val nextDueDateFormatted: String = "16 Nov 2026",
    val promoTitle: String = "Stay Fit Stay Active",
    val promoSubtitle: String = "Renew Now & Get 1 Month Free 🎁"
)

enum class DayAttendanceStatus {
    PRESENT,
    ABSENT,
    LATE,
    HOLIDAY
}

data class CalendarDay(
    val dayOfMonth: Int,
    val dayOfWeekIndex: Int, // 0 = Sunday .. 6 = Saturday
    val status: DayAttendanceStatus,
    val dayOfWeekInitial: String = ""
)

data class AttendanceSectionData(
    val monthLabel: String = "October 2026",
    val percent: Int = 77,
    val attendedDays: Int = 24,
    val totalDays: Int = 31,
    val presentCount: Int = 24,
    val absentCount: Int = 3,
    val lateCount: Int = 2,
    val holidayCount: Int = 1,
    val calendarDays: List<CalendarDay>
)

data class WorkoutTrendBar(
    val dayNumber: Int,
    val minutes: Int,
    val color: Color
)

data class WorkoutTrendData(
    val monthLabel: String = "October 2026",
    val bars: List<WorkoutTrendBar>,
    val avgPerDayMinutes: Int = 48,
    val peakMinutes: Int = 52,
    val peakDate: String = "10 Oct",
    val lowestMinutes: Int = 12,
    val lowestDate: String = "16 Oct",
    val trendVsLastMonth: String = "↗ +12% vs Sep",
    val daysWithWorkout: Int = 22,
    val daysNoWorkout: Int = 8,
    // Canonical KPI fields
    val avgMinutes: Int = 28,
    val avgTrendText: String = "↑ 12% vs prev 30 days",
    val peakDateText: String = "Peak (5 Oct)",
    val lowestDateText: String = "Lowest (9 Oct)",
    val highlightDayIndexes: Map<Int, String> = mapOf(8 to "52m", 26 to "48m")
)

data class MemberCardData(
    val profile: MemberProfile,
    val bodyStats: List<BodyStatItem>,
    val paymentPlan: PaymentPlanState,
    val attendance: AttendanceSectionData,
    val workoutTrend: WorkoutTrendData
) {
    companion object {
        fun sampleAmanTripathi(): MemberCardData {
            // Calendar Days (4 weeks × 7 columns = 28 days mapped to exact visual reference)
            val calendar = listOf(
                // Week 1 (Days 1 - 7)
                CalendarDay(1, 0, DayAttendanceStatus.PRESENT),
                CalendarDay(2, 1, DayAttendanceStatus.PRESENT),
                CalendarDay(3, 2, DayAttendanceStatus.PRESENT),
                CalendarDay(4, 3, DayAttendanceStatus.PRESENT),
                CalendarDay(5, 4, DayAttendanceStatus.PRESENT),
                CalendarDay(6, 5, DayAttendanceStatus.LATE),
                CalendarDay(7, 6, DayAttendanceStatus.LATE),
                // Week 2 (Days 8 - 14)
                CalendarDay(8, 0, DayAttendanceStatus.PRESENT),
                CalendarDay(9, 1, DayAttendanceStatus.PRESENT),
                CalendarDay(10, 2, DayAttendanceStatus.PRESENT),
                CalendarDay(11, 3, DayAttendanceStatus.PRESENT),
                CalendarDay(12, 4, DayAttendanceStatus.PRESENT),
                CalendarDay(13, 5, DayAttendanceStatus.PRESENT),
                CalendarDay(14, 6, DayAttendanceStatus.ABSENT),
                // Week 3 (Days 15 - 21)
                CalendarDay(15, 0, DayAttendanceStatus.PRESENT),
                CalendarDay(16, 1, DayAttendanceStatus.ABSENT),
                CalendarDay(17, 2, DayAttendanceStatus.PRESENT),
                CalendarDay(18, 3, DayAttendanceStatus.PRESENT),
                CalendarDay(19, 4, DayAttendanceStatus.PRESENT),
                CalendarDay(20, 5, DayAttendanceStatus.PRESENT),
                CalendarDay(21, 6, DayAttendanceStatus.PRESENT),
                // Week 4 (Days 22 - 28)
                CalendarDay(22, 0, DayAttendanceStatus.ABSENT),
                CalendarDay(23, 1, DayAttendanceStatus.PRESENT),
                CalendarDay(24, 2, DayAttendanceStatus.LATE),
                CalendarDay(25, 3, DayAttendanceStatus.PRESENT),
                CalendarDay(26, 4, DayAttendanceStatus.HOLIDAY),
                CalendarDay(27, 5, DayAttendanceStatus.PRESENT),
                CalendarDay(28, 6, DayAttendanceStatus.PRESENT),
                // Week 5 (Days 29 - 31)
                CalendarDay(29, 0, DayAttendanceStatus.PRESENT),
                CalendarDay(30, 1, DayAttendanceStatus.PRESENT),
                CalendarDay(31, 2, DayAttendanceStatus.PRESENT)
            )

            // Workout bars for 30 days with intensity colors
            val emeraldBar = Color(0xFF00E676)
            val amberBar = Color(0xFFFBBF24)
            val redBar = Color(0xFFF87171)
            val cyanBar = Color(0xFF06B6D4)

            val workoutBars = listOf(
                WorkoutTrendBar(1, 18, cyanBar),
                WorkoutTrendBar(2, 28, emeraldBar),
                WorkoutTrendBar(3, 40, emeraldBar),
                WorkoutTrendBar(4, 34, emeraldBar),
                WorkoutTrendBar(5, 38, emeraldBar),
                WorkoutTrendBar(6, 50, amberBar),
                WorkoutTrendBar(7, 30, cyanBar),
                WorkoutTrendBar(8, 32, cyanBar),
                WorkoutTrendBar(9, 20, redBar),
                WorkoutTrendBar(10, 22, redBar),
                WorkoutTrendBar(11, 32, amberBar),
                WorkoutTrendBar(12, 18, emeraldBar),
                WorkoutTrendBar(13, 24, emeraldBar),
                WorkoutTrendBar(14, 38, emeraldBar),
                WorkoutTrendBar(15, 45, emeraldBar),
                WorkoutTrendBar(16, 32, cyanBar),
                WorkoutTrendBar(17, 24, redBar),
                WorkoutTrendBar(18, 26, redBar),
                WorkoutTrendBar(19, 36, emeraldBar),
                WorkoutTrendBar(20, 52, emeraldBar),
                WorkoutTrendBar(21, 44, emeraldBar),
                WorkoutTrendBar(22, 38, emeraldBar),
                WorkoutTrendBar(23, 22, cyanBar),
                WorkoutTrendBar(24, 34, amberBar),
                WorkoutTrendBar(25, 30, amberBar),
                WorkoutTrendBar(26, 42, emeraldBar),
                WorkoutTrendBar(27, 36, emeraldBar),
                WorkoutTrendBar(28, 20, redBar),
                WorkoutTrendBar(29, 32, amberBar),
                WorkoutTrendBar(30, 48, emeraldBar),
                WorkoutTrendBar(31, 38, emeraldBar)
            )

            val monthlyHistory = listOf(
                MonthPaymentItem("Jan", "--"),
                MonthPaymentItem("Feb", "--"),
                MonthPaymentItem("Mar", "--"),
                MonthPaymentItem("Apr", "₹1.2K", "Gold"),
                MonthPaymentItem("May", "₹1.2K", "Gold"),
                MonthPaymentItem("Jun", "₹1.05K", "-₹150", isDiscount = true),
                MonthPaymentItem("Jul", "Off", isOff = true),
                MonthPaymentItem("Aug", "Off", isOff = true),
                MonthPaymentItem("Sep", "₹800", "Silver"),
                MonthPaymentItem("Oct", "₹600", "Spark", isCurrent = true),
                MonthPaymentItem("Nov", "--"),
                MonthPaymentItem("Dec", "--")
            )

            return MemberCardData(
                profile = MemberProfile(
                    greeting = "Good Morning 👋",
                    name = "Aman Tripathi",
                    memberCode = "BG305",
                    age = 23,
                    gender = "Male",
                    tierName = "Gold Plan (6 Months)",
                    tier = MemberTier.GOLD_6_MONTH,
                    isPrime = true,
                    isActive = true,
                    attendancePercent = 75,
                    daysLeft = 25,
                    joinedDate = "01 Sep 2026",
                    goal = "Muscle Gain",
                    branch = "Varanasi",
                    unreadNotifications = 3
                ),
                bodyStats = listOf(
                    BodyStatItem(
                        type = BodyStatType.WEIGHT,
                        value = "69.5 kg",
                        delta = "↓ 2.5 kg",
                        label = "Weight (kg)",
                        sparklinePoints = listOf(0.6f, 0.55f, 0.45f, 0.5f, 0.35f, 0.4f, 0.25f, 0.2f),
                        lineColor = Color(0xFF10B981),
                        isFavorable = true,
                        goalText = "Goal: 65 kg"
                    ),
                    BodyStatItem(
                        type = BodyStatType.MUSCLE,
                        value = "57.2 kg",
                        delta = "↑ 1.4 kg",
                        label = "Muscle (kg)",
                        sparklinePoints = listOf(0.2f, 0.3f, 0.25f, 0.4f, 0.5f, 0.45f, 0.6f, 0.75f),
                        lineColor = Color(0xFF8B5CF6),
                        isFavorable = true,
                        goalText = "Goal: 60 kg"
                    ),
                    BodyStatItem(
                        type = BodyStatType.BODY_FAT,
                        value = "17.6%",
                        delta = "↓ 1.2%",
                        label = "Body Fat (%)",
                        sparklinePoints = listOf(0.7f, 0.65f, 0.55f, 0.6f, 0.45f, 0.4f, 0.35f, 0.25f),
                        lineColor = Color(0xFFEF4444),
                        isFavorable = true,
                        goalText = "Goal: 15%"
                    ),
                    BodyStatItem(
                        type = BodyStatType.BMI,
                        value = "22.8",
                        delta = "Normal",
                        label = "BMI",
                        sparklinePoints = listOf(0.3f, 0.35f, 0.4f, 0.35f, 0.45f, 0.5f, 0.6f, 0.7f),
                        lineColor = Color(0xFF2563EB),
                        isFavorable = true,
                        goalText = "18.5 – 24.9"
                    )
                ),
                paymentPlan = PaymentPlanState(
                    planDuration = "Prime Plan (1 Month)",
                    planPeriod = "01 Sep – 30 Sep 2026",
                    amountPaid = "₹2,800",
                    paymentStatus = "PAID • ON TIME",
                    paidOnDate = "Paid on 01 Sep 2026",
                    nextDueDate = "30 Sep 2026",
                    daysLeft = 25,
                    isPrime = true,
                    lastPaymentAmount = "₹2,800",
                    lastPaymentDate = "01 Sep 2026",
                    monthlyHistory = monthlyHistory,
                    dueAlertAmount = "₹2,800",
                    dueAlertText = "Due in 3 days ⚠️",
                    dueAlertDate = "16 Oct 2026",
                    planName = "Gold Plan (6 Months)",
                    planPeriodFormatted = "01 May 2026 – 31 Oct 2026",
                    planDaysUsed = 135,
                    planTotalDays = 180,
                    planDaysRemaining = 45,
                    lastPaidAmount = "₹2,800",
                    lastPaidDate = "01 Sep 2026",
                    currentDueAmount = "₹2,800",
                    currentDueDate = "16 Oct 2026",
                    nextDueAmount = "₹2,800",
                    nextDueDateFormatted = "16 Nov 2026",
                    promoTitle = "Stay Fit Stay Active",
                    promoSubtitle = "Renew Now & Get 1 Month Free 🎁"
                ),
                attendance = AttendanceSectionData(
                    monthLabel = "October 2026",
                    percent = 77,
                    attendedDays = 24,
                    totalDays = 31,
                    presentCount = 24,
                    absentCount = 3,
                    lateCount = 2,
                    holidayCount = 1,
                    calendarDays = calendar
                ),
                workoutTrend = WorkoutTrendData(
                    monthLabel = "October 2026",
                    bars = workoutBars,
                    avgPerDayMinutes = 48,
                    peakMinutes = 52,
                    peakDate = "10 Oct",
                    lowestMinutes = 12,
                    lowestDate = "16 Oct",
                    avgMinutes = 28,
                    avgTrendText = "↑ 12% vs prev 30 days",
                    peakDateText = "Peak (5 Oct)",
                    lowestDateText = "Lowest (9 Oct)",
                    highlightDayIndexes = mapOf(8 to "52m", 26 to "48m")
                )
            )
        }

        fun sampleAshwiniSingh(): MemberCardData {
            // Full 31 days calendar mapped to Sunday(0) .. Saturday(6)
            // Oct 1 2026 was Thursday (index 4)
            val calendar = listOf(
                // Week 1 (Days 1 - 3)
                CalendarDay(1, 4, DayAttendanceStatus.PRESENT, "T"),
                CalendarDay(2, 5, DayAttendanceStatus.PRESENT, "F"),
                CalendarDay(3, 6, DayAttendanceStatus.PRESENT, "S"),
                // Week 2 (Days 4 - 10)
                CalendarDay(4, 0, DayAttendanceStatus.HOLIDAY, "S"),
                CalendarDay(5, 1, DayAttendanceStatus.PRESENT, "M"),
                CalendarDay(6, 2, DayAttendanceStatus.PRESENT, "T"),
                CalendarDay(7, 3, DayAttendanceStatus.PRESENT, "W"),
                CalendarDay(8, 4, DayAttendanceStatus.PRESENT, "T"),
                CalendarDay(9, 5, DayAttendanceStatus.PRESENT, "F"),
                CalendarDay(10, 6, DayAttendanceStatus.PRESENT, "S"),
                // Week 3 (Days 11 - 17)
                CalendarDay(11, 0, DayAttendanceStatus.HOLIDAY, "S"),
                CalendarDay(12, 1, DayAttendanceStatus.PRESENT, "M"),
                CalendarDay(13, 2, DayAttendanceStatus.PRESENT, "T"),
                CalendarDay(14, 3, DayAttendanceStatus.PRESENT, "W"),
                CalendarDay(15, 4, DayAttendanceStatus.PRESENT, "T"),
                CalendarDay(16, 5, DayAttendanceStatus.PRESENT, "F"),
                CalendarDay(17, 6, DayAttendanceStatus.ABSENT, "S"),
                // Week 4 (Days 18 - 24)
                CalendarDay(18, 0, DayAttendanceStatus.HOLIDAY, "S"),
                CalendarDay(19, 1, DayAttendanceStatus.PRESENT, "M"),
                CalendarDay(20, 2, DayAttendanceStatus.PRESENT, "T"),
                CalendarDay(21, 3, DayAttendanceStatus.PRESENT, "W"),
                CalendarDay(22, 4, DayAttendanceStatus.LATE, "T"),
                CalendarDay(23, 5, DayAttendanceStatus.PRESENT, "F"),
                CalendarDay(24, 6, DayAttendanceStatus.ABSENT, "S"),
                // Week 5 (Days 25 - 31)
                CalendarDay(25, 0, DayAttendanceStatus.HOLIDAY, "S"),
                CalendarDay(26, 1, DayAttendanceStatus.PRESENT, "M"),
                CalendarDay(27, 2, DayAttendanceStatus.PRESENT, "T"),
                CalendarDay(28, 3, DayAttendanceStatus.LATE, "W"),
                CalendarDay(29, 4, DayAttendanceStatus.PRESENT, "T"),
                CalendarDay(30, 5, DayAttendanceStatus.ABSENT, "F"),
                CalendarDay(31, 6, DayAttendanceStatus.PRESENT, "S")
            )

            val cyanBar = Color(0xFF0070F3)
            val emeraldBar = Color(0xFF00C48C)
            val amberBar = Color(0xFFFFB800)
            val redBar = Color(0xFFFF4D4F)
            val blueBar = Color(0xFF38BDF8)

            val workoutBars = (1..30).map { day ->
                val mins = when (day) {
                    8 -> 52 // Peak
                    15 -> 42 // Highlight orange
                    26 -> 48 // Secondary Peak
                    16 -> 12 // Lowest
                    else -> (22 + (day * 7) % 24)
                }
                val color = when (day) {
                    8 -> redBar
                    15 -> amberBar
                    26 -> cyanBar
                    else -> if (mins >= 35) emeraldBar else blueBar
                }
                WorkoutTrendBar(day, mins, color)
            }

            val monthlyHistory = listOf(
                MonthPaymentItem("Jan", "--"),
                MonthPaymentItem("Feb", "--"),
                MonthPaymentItem("Mar", "--"),
                MonthPaymentItem("Apr", "₹1.2K", "Gold"),
                MonthPaymentItem("May", "₹1.2K", "Gold"),
                MonthPaymentItem("Jun", "₹1.05K", "-₹150", isDiscount = true),
                MonthPaymentItem("Jul", "Off", isOff = true),
                MonthPaymentItem("Aug", "Off", isOff = true),
                MonthPaymentItem("Sep", "₹800", "Silver"),
                MonthPaymentItem("Oct", "₹600", "Spark", isCurrent = true),
                MonthPaymentItem("Nov", "--"),
                MonthPaymentItem("Dec", "--")
            )

            return MemberCardData(
                profile = MemberProfile(
                    greeting = "Good Morning 👋",
                    name = "Ashwini Singh",
                    memberCode = "BG4187",
                    age = 25,
                    gender = "Male",
                    tierName = "Spark Member",
                    tier = MemberTier.SPARK_DIAMOND,
                    isPrime = false,
                    isActive = true,
                    attendancePercent = 77,
                    daysLeft = 18,
                    attendedDays = 24,
                    totalDays = 30,
                    joinedDate = "14 Apr 2026",
                    goal = "Muscle Gain",
                    branch = "Main Branch",
                    unreadNotifications = 3,
                    dateDisplay = "Tue\n22 Oct 2026"
                ),
                bodyStats = listOf(
                    BodyStatItem(
                        type = BodyStatType.WEIGHT,
                        value = "69.5",
                        unit = "kg",
                        delta = "↓ 2.5 kg",
                        label = "Weight",
                        sparklinePoints = listOf(0.6f, 0.55f, 0.45f, 0.5f, 0.35f, 0.4f, 0.25f, 0.2f),
                        lineColor = Color(0xFF10B981),
                        isFavorable = true,
                        goalText = "Goal: 65 kg"
                    ),
                    BodyStatItem(
                        type = BodyStatType.MUSCLE,
                        value = "57.2",
                        unit = "kg",
                        delta = "↑ 1.4 kg",
                        label = "Muscle",
                        sparklinePoints = listOf(0.2f, 0.3f, 0.25f, 0.4f, 0.5f, 0.45f, 0.6f, 0.75f),
                        lineColor = Color(0xFF8B5CF6),
                        isFavorable = true,
                        goalText = "Goal: 60 kg"
                    ),
                    BodyStatItem(
                        type = BodyStatType.BODY_FAT,
                        value = "17.6%",
                        unit = "",
                        delta = "↓ 1.2%",
                        label = "Body Fat",
                        sparklinePoints = listOf(0.7f, 0.65f, 0.55f, 0.6f, 0.45f, 0.4f, 0.35f, 0.25f),
                        lineColor = Color(0xFFEF4444),
                        isFavorable = true,
                        goalText = "Goal: 15%"
                    ),
                    BodyStatItem(
                        type = BodyStatType.BMI,
                        value = "22.8",
                        unit = "",
                        delta = "Normal",
                        label = "BMI",
                        sparklinePoints = listOf(0.3f, 0.35f, 0.4f, 0.35f, 0.45f, 0.5f, 0.6f, 0.7f),
                        lineColor = Color(0xFF2563EB),
                        isFavorable = true,
                        goalText = "18.5 – 24.9"
                    )
                ),
                paymentPlan = PaymentPlanState(
                    planDuration = "Gold Plan (6 Months)",
                    planPeriod = "01 May – 31 Oct 2026",
                    amountPaid = "₹2,800",
                    paymentStatus = "PAID • ON TIME",
                    paidOnDate = "Paid on 01 Sep 2026",
                    nextDueDate = "31 Oct 2026",
                    daysLeft = 9,
                    isPrime = false,
                    lastPaymentAmount = "₹800",
                    lastPaymentDate = "01 Oct 2026",
                    monthlyHistory = monthlyHistory,
                    dueAlertAmount = "₹2,800",
                    dueAlertText = "Due in 3 days ⚠️",
                    dueAlertDate = "16 Oct 2026",
                    planName = "Gold Plan (6 Months)",
                    planPeriodFormatted = "01 May 2026 – 31 Oct 2026",
                    planDaysUsed = 135,
                    planTotalDays = 180,
                    planDaysRemaining = 45,
                    lastPaidAmount = "₹2,800",
                    lastPaidDate = "01 Sep 2026",
                    currentDueAmount = "₹2,800",
                    currentDueDate = "16 Oct 2026",
                    nextDueAmount = "₹2,800",
                    nextDueDateFormatted = "16 Nov 2026",
                    promoTitle = "Stay Fit Stay Active",
                    promoSubtitle = "Renew Now & Get 1 Month Free 🎁"
                ),
                attendance = AttendanceSectionData(
                    monthLabel = "Oct 2026",
                    percent = 77,
                    attendedDays = 24,
                    totalDays = 31,
                    presentCount = 24,
                    absentCount = 4,
                    lateCount = 2,
                    holidayCount = 1,
                    calendarDays = calendar
                ),
                workoutTrend = WorkoutTrendData(
                    monthLabel = "Oct 2026",
                    bars = workoutBars,
                    avgPerDayMinutes = 48,
                    peakMinutes = 52,
                    peakDate = "10 Oct",
                    lowestMinutes = 12,
                    lowestDate = "16 Oct",
                    trendVsLastMonth = "↗ +12% vs Sep",
                    daysWithWorkout = 22,
                    daysNoWorkout = 8,
                    avgMinutes = 28,
                    avgTrendText = "↑ 12% vs prev 30 days",
                    peakDateText = "Peak (5 Oct)",
                    lowestDateText = "Lowest (9 Oct)",
                    highlightDayIndexes = mapOf(8 to "52m", 26 to "48m")
                )
            )
        }
    }
}
