package com.example.badnewgym.feature.membercard.domain.model

import androidx.compose.ui.graphics.Color
import com.example.badnewgym.R

/**
 * 8 Canonical BAD GYM Member Passes
 * Extracted directly from Master Reference Specification:
 * 1. Spark (2 Months) - Green
 * 2. Power (3 Months) - Cyan/Blue
 * 3. Silver (6 Months) - Silver/Slate
 * 4. Gold (12 Months) - Radiant Gold
 * 5. Diamond (18 Months) - Sky Diamond Blue
 * 6. Platinum (24 Months) - Royal Purple
 * 7. Elite (36 Months) - Obsidian & Royal Gold VIP
 * 8. Basic (1 Month) - Coral Red Overdue
 */
enum class BadGymPassTier(
    val id: String,
    val title: String,
    val durationText: String,
    val mottoLine1: String,
    val mottoLine2: String,
    val iconType: String
) {
    SPARK(
        id = "spark",
        title = "Spark",
        durationText = "2 Months",
        mottoLine1 = "FIT TODAY",
        mottoLine2 = "BETTER TOMORROW",
        iconType = "leaf"
    ),
    POWER(
        id = "power",
        title = "Power",
        durationText = "3 Months",
        mottoLine1 = "STRONGER",
        mottoLine2 = "EVERYDAY",
        iconType = "lightning"
    ),
    SILVER(
        id = "silver",
        title = "Silver",
        durationText = "6 Months",
        mottoLine1 = "DISCIPLINE",
        mottoLine2 = "BUILDS FREEDOM",
        iconType = "star"
    ),
    GOLD(
        id = "gold",
        title = "Gold",
        durationText = "12 Months",
        mottoLine1 = "CONSISTENCY",
        mottoLine2 = "CREATES RESULTS",
        iconType = "crown"
    ),
    DIAMOND(
        id = "diamond",
        title = "Diamond",
        durationText = "18 Months",
        mottoLine1 = "NO LIMITS",
        mottoLine2 = "JUST PROGRESS",
        iconType = "diamond"
    ),
    PLATINUM(
        id = "platinum",
        title = "Platinum",
        durationText = "24 Months",
        mottoLine1 = "HIGHER STRONGER",
        mottoLine2 = "HEALTHIER",
        iconType = "platinum"
    ),
    ELITE(
        id = "elite",
        title = "Elite",
        durationText = "36 Months",
        mottoLine1 = "ELITE",
        mottoLine2 = "DISCIPLINE FOCUSED RESULTS",
        iconType = "crest"
    ),
    BASIC(
        id = "basic",
        title = "Basic",
        durationText = "1 Month",
        mottoLine1 = "TIME TO",
        mottoLine2 = "GET BACK ON TRACK",
        iconType = "timer"
    )
}

data class BadGymMemberPass(
    val tier: BadGymPassTier,
    val memberId: String,
    val name: String,
    val age: Int,
    val gender: String,
    val quote: String,
    val quoteEmoji: String,
    val startDate: String,
    val endDate: String,
    val daysLeft: Int,
    val totalDays: Int,
    val isOverdue: Boolean = false,
    val overdueDays: Int = 0,
    val height: String,
    val weight: String,
    val bmi: String,
    val goal: String,
    val avatarRes: Int = R.drawable.avatar_ashwini,
    val isVip: Boolean = false
) {
    companion object {
        fun getAll8Passes(): List<BadGymMemberPass> = listOf(
            BadGymMemberPass(
                tier = BadGymPassTier.SPARK,
                memberId = "BG305",
                name = "Aman Tripathi",
                age = 23,
                gender = "Male",
                quote = "Better Than Yesterday",
                quoteEmoji = "\uD83D\uDCAA",
                startDate = "05 Oct 2026",
                endDate = "04 Dec 2026",
                daysLeft = 25,
                totalDays = 60,
                isOverdue = false,
                height = "5'8\"",
                weight = "69.5 kg",
                bmi = "22.8",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_aman
            ),
            BadGymMemberPass(
                tier = BadGymPassTier.POWER,
                memberId = "BG420",
                name = "Priya Sharma Verma",
                age = 26,
                gender = "Female",
                quote = "Stronger Everyday",
                quoteEmoji = "\uD83D\uDD25",
                startDate = "10 Sep 2026",
                endDate = "09 Dec 2026",
                daysLeft = 60,
                totalDays = 90,
                isOverdue = false,
                height = "5'4\"",
                weight = "58.2 kg",
                bmi = "21.3",
                goal = "Strength",
                avatarRes = R.drawable.avatar_ashwini
            ),
            BadGymMemberPass(
                tier = BadGymPassTier.SILVER,
                memberId = "BG215",
                name = "Rohit Kumar Singh",
                age = 28,
                gender = "Male",
                quote = "Discipline Builds Freedom",
                quoteEmoji = "",
                startDate = "01 Aug 2026",
                endDate = "31 Jan 2027",
                daysLeft = 92,
                totalDays = 180,
                isOverdue = false,
                height = "5'9\"",
                weight = "74.1 kg",
                bmi = "23.5",
                goal = "Fat Loss",
                avatarRes = R.drawable.avatar_rohit
            ),
            BadGymMemberPass(
                tier = BadGymPassTier.GOLD,
                memberId = "BG512",
                name = "Sneha Gupta",
                age = 24,
                gender = "Female",
                quote = "Fit Mind Fit Life",
                quoteEmoji = "✨",
                startDate = "05 Oct 2025",
                endDate = "04 Oct 2026",
                daysLeft = 320,
                totalDays = 365,
                isOverdue = false,
                height = "5'5\"",
                weight = "60.0 kg",
                bmi = "22.0",
                goal = "Body Toning",
                avatarRes = R.drawable.avatar_ashwini
            ),
            BadGymMemberPass(
                tier = BadGymPassTier.DIAMOND,
                memberId = "BG678",
                name = "Saurabh Rameshwar",
                age = 30,
                gender = "Male",
                quote = "No Limits",
                quoteEmoji = "∞",
                startDate = "15 Apr 2026",
                endDate = "14 Oct 2027",
                daysLeft = 540,
                totalDays = 550,
                isOverdue = false,
                height = "5'10\"",
                weight = "72.4 kg",
                bmi = "23.1",
                goal = "Strength",
                avatarRes = R.drawable.avatar_aman
            ),
            BadGymMemberPass(
                tier = BadGymPassTier.PLATINUM,
                memberId = "BG330",
                name = "Vikram Pratap Singh",
                age = 29,
                gender = "Male",
                quote = "Consistency Wins",
                quoteEmoji = "\uD83C\uDFC6",
                startDate = "10 Jun 2026",
                endDate = "09 Jun 2028",
                daysLeft = 700,
                totalDays = 730,
                isOverdue = false,
                height = "5'7\"",
                weight = "67.8 kg",
                bmi = "21.9",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_rohit
            ),
            BadGymMemberPass(
                tier = BadGymPassTier.ELITE,
                memberId = "BG882",
                name = "Karan Malhotra",
                age = 27,
                gender = "Male",
                quote = "Born To Be Elite",
                quoteEmoji = "\uD83D\uDC51",
                startDate = "01 Aug 2026",
                endDate = "31 Jul 2029",
                daysLeft = 980,
                totalDays = 1095,
                isOverdue = false,
                height = "5'8\"",
                weight = "71.2 kg",
                bmi = "23.6",
                goal = "Overall Fitness",
                avatarRes = R.drawable.avatar_ashwini,
                isVip = true
            ),
            BadGymMemberPass(
                tier = BadGymPassTier.BASIC,
                memberId = "BG910",
                name = "Neha Singh Chauhan",
                age = 25,
                gender = "Female",
                quote = "Come Back Stronger",
                quoteEmoji = "❤️",
                startDate = "01 Aug 2026",
                endDate = "31 Aug 2026",
                daysLeft = 0,
                totalDays = 30,
                isOverdue = true,
                overdueDays = 12,
                height = "5'3\"",
                weight = "56.0 kg",
                bmi = "21.5",
                goal = "General Fitness",
                avatarRes = R.drawable.avatar_ashwini
            )
        )
    }
}

