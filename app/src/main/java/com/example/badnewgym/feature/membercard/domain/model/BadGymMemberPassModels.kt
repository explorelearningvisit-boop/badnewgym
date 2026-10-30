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
        title = "Emerald",
        durationText = "2 Months",
        mottoLine1 = "FIT TODAY",
        mottoLine2 = "BETTER TOMORROW",
        iconType = "leaf"
    ),
    POWER(
        id = "power",
        title = "Ocean",
        durationText = "3 Months",
        mottoLine1 = "STRONGER",
        mottoLine2 = "EVERYDAY",
        iconType = "lightning"
    ),
    PLATINUM(
        id = "platinum",
        title = "Purple",
        durationText = "24 Months",
        mottoLine1 = "HIGHER STRONGER",
        mottoLine2 = "HEALTHIER",
        iconType = "crown"
    ),
    GOLD(
        id = "gold",
        title = "Gold",
        durationText = "12 Months",
        mottoLine1 = "CONSISTENCY",
        mottoLine2 = "CREATES RESULTS",
        iconType = "crown"
    ),
    BASIC(
        id = "basic",
        title = "Overdue",
        durationText = "1 Month",
        mottoLine1 = "TIME TO",
        mottoLine2 = "GET BACK ON TRACK",
        iconType = "alert"
    ),
    CYAN(
        id = "cyan",
        title = "Cyan",
        durationText = "3 Months",
        mottoLine1 = "NO LIMITS",
        mottoLine2 = "JUST PROGRESS",
        iconType = "diamond"
    ),
    WHITE_MINT(
        id = "white_mint",
        title = "White Mint",
        durationText = "6 Months",
        mottoLine1 = "DISCIPLINE",
        mottoLine2 = "BUILDS FREEDOM",
        iconType = "star"
    ),
    WHITE_OCEAN(
        id = "white_ocean",
        title = "White Ocean",
        durationText = "12 Months",
        mottoLine1 = "CLEAR MIND",
        mottoLine2 = "STRONG BODY",
        iconType = "ocean"
    ),
    WHITE_LAVENDER(
        id = "white_lavender",
        title = "White Lavender",
        durationText = "24 Months",
        mottoLine1 = "BORN TO BE",
        mottoLine2 = "ELITE",
        iconType = "crest"
    ),
    // Preserved for backward compatibility
    SILVER(
        id = "silver",
        title = "Silver",
        durationText = "6 Months",
        mottoLine1 = "DISCIPLINE",
        mottoLine2 = "BUILDS FREEDOM",
        iconType = "star"
    ),
    DIAMOND(
        id = "diamond",
        title = "Diamond",
        durationText = "18 Months",
        mottoLine1 = "NO LIMITS",
        mottoLine2 = "JUST PROGRESS",
        iconType = "diamond"
    ),
    ELITE(
        id = "elite",
        title = "Elite",
        durationText = "36 Months",
        mottoLine1 = "ELITE",
        mottoLine2 = "DISCIPLINE FOCUSED RESULTS",
        iconType = "crest"
    )
}

data class BadGymMemberPass(
    val tier: BadGymPassTier,
    val memberId: String = "BG305",
    val name: String = "Aman Tripathi",
    val age: Int = 23,
    val gender: String = "Male",
    val quote: String = "Better Than Yesterday",
    val quoteEmoji: String = "\uD83D\uDCAA",
    val startDate: String = "05 Oct 2026",
    val endDate: String = "04 Nov 2026",
    val daysLeft: Int = 25,
    val totalDays: Int = 30,
    val isOverdue: Boolean = false,
    val overdueDays: Int = 0,
    val height: String = "5'8\"",
    val weight: String = "69.5 kg",
    val bmi: String = "22.8",
    val goal: String = "Muscle Gain",
    val avatarRes: Int = R.drawable.avatar_aman,
    val isVip: Boolean = false,
    val planName: String = "Prime Plan",
    val planPrice: String = "₹1,200 / month",
    val branchName: String = "Varanasi",
    val isVerified: Boolean = true
) {
    companion object {
        fun sampleAshwaniKumarSingh(): BadGymMemberPass = BadGymMemberPass(
            tier = BadGymPassTier.SILVER,
            memberId = "BG215",
            name = "Ashwani Kumar Singh Srivastav",
            age = 28,
            gender = "Male",
            quote = "Discipline Builds Freedom",
            quoteEmoji = "",
            startDate = "09 Oct 2026",
            endDate = "07 Nov 2026",
            daysLeft = 8,
            totalDays = 30,
            isOverdue = false,
            height = "5'9\"",
            weight = "69.5 kg",
            bmi = "22.8",
            goal = "Fat Loss",
            avatarRes = R.drawable.avatar_ashwini,
            planName = "Starter Plan SILVER",
            planPrice = "30 Days Plan",
            branchName = "Varanasi",
            isVerified = true
        )

        fun sampleRohitKumarSingh(): BadGymMemberPass = BadGymMemberPass(
            tier = BadGymPassTier.SILVER,
            memberId = "BG215",
            name = "Rohit Kumar Singh",
            age = 28,
            gender = "Male",
            quote = "Discipline Builds Freedom",
            quoteEmoji = "",
            startDate = "09 Oct 2026",
            endDate = "31 Jan 2027",
            daysLeft = 92,
            totalDays = 180,
            isOverdue = false,
            height = "5'9\"",
            weight = "69.5 kg",
            bmi = "22.8",
            goal = "Fat Loss",
            avatarRes = R.drawable.avatar_rohit,
            planName = "Silver",
            planPrice = "6 Months",
            branchName = "Varanasi",
            isVerified = true
        )

        fun getAll8Passes(): List<BadGymMemberPass> = getAll9Passes()

        fun getAll9Passes(): List<BadGymMemberPass> = listOf(
            // 01: Canonical Master Ashwani Kumar Singh Srivastav (Infographic Blueprint 1:1)
            sampleAshwaniKumarSingh(),
            // 02: Canonical Master Rohit Kumar Singh (Silver 6 Months Luxury White)
            sampleRohitKumarSingh(),
            // 02: Emerald Green (Saturated Glass)
            BadGymMemberPass(
                tier = BadGymPassTier.SPARK,
                memberId = "BG215",
                name = "Rohit Kumar Singh",
                age = 28,
                gender = "Male",
                quote = "Discipline Builds Freedom",
                quoteEmoji = "",
                startDate = "09 Oct 2026",
                endDate = "31 Jan 2027",
                daysLeft = 92,
                totalDays = 180,
                isOverdue = false,
                height = "5'9\"",
                weight = "74.1 kg",
                bmi = "23.5",
                goal = "Fat Loss",
                avatarRes = R.drawable.avatar_rohit,
                planName = "Emerald",
                planPrice = "6 Months",
                branchName = "Varanasi",
                isVerified = true
            ),
            // 02: Ocean Blue (Saturated Glass)
            BadGymMemberPass(
                tier = BadGymPassTier.POWER,
                memberId = "BG305",
                name = "Aman Tripathi",
                age = 23,
                gender = "Male",
                quote = "Better Than Yesterday",
                quoteEmoji = "\uD83D\uDCAA",
                startDate = "05 Oct 2026",
                endDate = "04 Nov 2026",
                daysLeft = 25,
                totalDays = 30,
                isOverdue = false,
                height = "5'8\"",
                weight = "69.5 kg",
                bmi = "22.8",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_aman,
                planName = "Prime Plan",
                planPrice = "₹1,200 / month",
                branchName = "Varanasi",
                isVerified = true
            ),
            // 03: Royal Purple (Saturated Glass)
            BadGymMemberPass(
                tier = BadGymPassTier.PLATINUM,
                memberId = "BG305",
                name = "Aman Tripathi",
                age = 23,
                gender = "Male",
                quote = "Better Than Yesterday",
                quoteEmoji = "\uD83D\uDCAA",
                startDate = "05 Oct 2026",
                endDate = "04 Nov 2026",
                daysLeft = 25,
                totalDays = 30,
                isOverdue = false,
                height = "5'8\"",
                weight = "69.5 kg",
                bmi = "22.8",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_aman,
                planName = "Prime Plan",
                planPrice = "₹1,200 / month",
                branchName = "Varanasi",
                isVerified = true
            ),
            // 04: Golden Amber (Warm Gold Saturated Glass)
            BadGymMemberPass(
                tier = BadGymPassTier.GOLD,
                memberId = "BG305",
                name = "Aman Tripathi",
                age = 23,
                gender = "Male",
                quote = "Better Than Yesterday",
                quoteEmoji = "\uD83D\uDCAA",
                startDate = "05 Oct 2026",
                endDate = "04 Nov 2026",
                daysLeft = 25,
                totalDays = 30,
                isOverdue = false,
                height = "5'8\"",
                weight = "69.5 kg",
                bmi = "22.8",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_aman,
                planName = "Prime Plan",
                planPrice = "₹1,200 / month",
                branchName = "Varanasi",
                isVerified = true
            ),
            // 05: Crimson Alert / Overdue (Saturated Glass)
            BadGymMemberPass(
                tier = BadGymPassTier.BASIC,
                memberId = "BG305",
                name = "Aman Tripathi",
                age = 23,
                gender = "Male",
                quote = "Better Than Yesterday",
                quoteEmoji = "\uD83D\uDCAA",
                startDate = "05 Oct 2026",
                endDate = "04 Nov 2026",
                daysLeft = 0,
                totalDays = 30,
                isOverdue = true,
                overdueDays = 5,
                height = "5'8\"",
                weight = "69.5 kg",
                bmi = "22.8",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_aman,
                planName = "Prime Plan",
                planPrice = "₹1,200 / month",
                branchName = "Varanasi",
                isVerified = true
            ),
            // 06: Cyan Aqua (Bright Saturated Glass)
            BadGymMemberPass(
                tier = BadGymPassTier.CYAN,
                memberId = "BG305",
                name = "Aman Tripathi",
                age = 23,
                gender = "Male",
                quote = "Better Than Yesterday",
                quoteEmoji = "\uD83D\uDCAA",
                startDate = "05 Oct 2026",
                endDate = "04 Nov 2026",
                daysLeft = 25,
                totalDays = 30,
                isOverdue = false,
                height = "5'8\"",
                weight = "69.5 kg",
                bmi = "22.8",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_aman,
                planName = "Prime Plan",
                planPrice = "₹1,200 / month",
                branchName = "Varanasi",
                isVerified = true
            ),
            // 07: White Mint Glass (Minimal Frosted White Glass)
            BadGymMemberPass(
                tier = BadGymPassTier.WHITE_MINT,
                memberId = "BG305",
                name = "Aman Tripathi",
                age = 23,
                gender = "Male",
                quote = "Better Than Yesterday",
                quoteEmoji = "\uD83D\uDCAA",
                startDate = "05 Oct 2026",
                endDate = "04 Nov 2026",
                daysLeft = 25,
                totalDays = 30,
                isOverdue = false,
                height = "5'8\"",
                weight = "69.5 kg",
                bmi = "22.8",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_aman,
                planName = "Prime Plan",
                planPrice = "₹1,200 / month",
                branchName = "Varanasi",
                isVerified = true
            ),
            // 08: White Ocean Glass (Minimal Frosted White Glass)
            BadGymMemberPass(
                tier = BadGymPassTier.WHITE_OCEAN,
                memberId = "BG305",
                name = "Aman Tripathi",
                age = 23,
                gender = "Male",
                quote = "Better Than Yesterday",
                quoteEmoji = "\uD83D\uDCAA",
                startDate = "05 Oct 2026",
                endDate = "04 Nov 2026",
                daysLeft = 25,
                totalDays = 30,
                isOverdue = false,
                height = "5'8\"",
                weight = "69.5 kg",
                bmi = "22.8",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_aman,
                planName = "Prime Plan",
                planPrice = "₹1,200 / month",
                branchName = "Varanasi",
                isVerified = true
            ),
            // 09: White Lavender Glass (Minimal Frosted White Glass)
            BadGymMemberPass(
                tier = BadGymPassTier.WHITE_LAVENDER,
                memberId = "BG305",
                name = "Aman Tripathi",
                age = 23,
                gender = "Male",
                quote = "Better Than Yesterday",
                quoteEmoji = "\uD83D\uDCAA",
                startDate = "05 Oct 2026",
                endDate = "04 Nov 2026",
                daysLeft = 25,
                totalDays = 30,
                isOverdue = false,
                height = "5'8\"",
                weight = "69.5 kg",
                bmi = "22.8",
                goal = "Muscle Gain",
                avatarRes = R.drawable.avatar_aman,
                planName = "Prime Plan",
                planPrice = "₹1,200 / month",
                branchName = "Varanasi",
                isVerified = true
            )
        )
    }
}

enum class MemberCardType(
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val primaryColor: Color,
    val badgeBg: Color,
    val badgeBorder: Color,
    val badgeText: Color
) {
    CHECK_IN(
        title = "CHECK-IN",
        subtitle = "MEMBER CHECK-IN",
        iconEmoji = "✓",
        primaryColor = Color(0xFF059669),
        badgeBg = Color(0xFFDCFCE7),
        badgeBorder = Color(0xFF86EFAC),
        badgeText = Color(0xFF065F46)
    ),
    CHECK_OUT(
        title = "CHECK-OUT",
        subtitle = "MEMBER CHECK-OUT",
        iconEmoji = "➔",
        primaryColor = Color(0xFF0284C7),
        badgeBg = Color(0xFFE0F2FE),
        badgeBorder = Color(0xFF7DD3FC),
        badgeText = Color(0xFF075985)
    ),
    PAYMENT(
        title = "PAYMENT",
        subtitle = "PAYMENT & DUES",
        iconEmoji = "₹",
        primaryColor = Color(0xFFE11D48),
        badgeBg = Color(0xFFFFE4E6),
        badgeBorder = Color(0xFFFDA4AF),
        badgeText = Color(0xFF9F1239)
    ),
    TRAINER_CHECK_IN(
        title = "TRAINER",
        subtitle = "TRAINER CHECK-IN",
        iconEmoji = "★",
        primaryColor = Color(0xFF7C3AED),
        badgeBg = Color(0xFFF3E8FF),
        badgeBorder = Color(0xFFD8B4FE),
        badgeText = Color(0xFF5B21B6)
    ),
    MACHINE_FAULT(
        title = "FAULT",
        subtitle = "MACHINE FAULT",
        iconEmoji = "⚠",
        primaryColor = Color(0xFFDC2626),
        badgeBg = Color(0xFFFEE2E2),
        badgeBorder = Color(0xFFFCA5A5),
        badgeText = Color(0xFF991B1B)
    ),
    MEMBER_PASS(
        title = "PASS",
        subtitle = "MEMBER PASS",
        iconEmoji = "★",
        primaryColor = Color(0xFF059669),
        badgeBg = Color(0xFFF1F5F9),
        badgeBorder = Color(0xFFCBD5E1),
        badgeText = Color(0xFF475569)
    )
}

enum class PaymentStatus(
    val label: String,
    val color: Color,
    val bgColor: Color,
    val borderColor: Color
) {
    PENDING("₹800 Pending", Color(0xFFD97706), Color(0xFFFEF3C7), Color(0xFFFCD34D)),
    DONE("Paid", Color(0xFF059669), Color(0xFFDCFCE7), Color(0xFF86EFAC)),
    FAILED("Failed", Color(0xFFDC2626), Color(0xFFFEE2E2), Color(0xFFFCA5A5)),
    OVERDUE("Overdue", Color(0xFFB91C1C), Color(0xFFFEE2E2), Color(0xFFEF4444))
}

