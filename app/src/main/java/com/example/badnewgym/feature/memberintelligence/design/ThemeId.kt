package com.example.badnewgym.feature.memberintelligence.design

import com.example.badnewgym.feature.memberintelligence.design.colors.*

enum class ThemeId(
    val title: String,
    val isDark: Boolean,
    val category: String,
    val subtitle: String,
    val motto: String,
    val footer: String,
    val timeText: String,
    val timeRelative: String,
    val actionChipText: String,
    val ctaText: String,
    val defaultMemberId: String
) {
    NATURAL_FRESH(
        title = "Natural Fresh",
        isDark = false,
        category = "Wellness • Clean • Friendly",
        subtitle = "Healthy People\nHappier Lives",
        motto = "Good Fitness\nBrighter You",
        footer = "Small Steps\nBig Results",
        timeText = "4:03 PM",
        timeRelative = "Just now",
        actionChipText = "+ CHECK-IN",
        ctaText = "Collect Payment",
        defaultMemberId = "BG204"
    ),
    FUTURISTIC_NEON(
        title = "Futuristic Neon",
        isDark = true,
        category = "Bold • Energetic • High Tech",
        subtitle = "BEYOND LIMITS",
        motto = "STRONGER\nEVERYDAY",
        footer = "DISCIPLINE TODAY\nA STRONGER TOMORROW  → → →",
        timeText = "6:15 PM",
        timeRelative = "Today",
        actionChipText = "+ WORKOUT",
        ctaText = "View Workout",
        defaultMemberId = "BG105"
    ),
    MINIMAL_DARK(
        title = "Minimal Dark",
        isDark = true,
        category = "Simple • Elegant • Focused",
        subtitle = "FOCUS • TRAIN • GROW",
        motto = "CONSISTENCY\nCREATES\nCHANGE",
        footer = "LESS EXCUSES\nMORE RESULTS",
        timeText = "7:00 AM",
        timeRelative = "Tomorrow",
        actionChipText = "+ TRAINER",
        ctaText = "View Trainer Session",
        defaultMemberId = "BG310"
    ),
    GLASSMORPHISM(
        title = "Glassmorphism",
        isDark = false,
        category = "Translucent • Modern • Elegant",
        subtitle = "Mind • Body • Community",
        motto = "More Than\nA Gym  ↗",
        footer = "Better People\nBetter Communities",
        timeText = "8:20 AM",
        timeRelative = "Today",
        actionChipText = "+ NUTRITION",
        ctaText = "View Nutrition Plan",
        defaultMemberId = "BG407"
    ),
    PREMIUM_3D(
        title = "Premium 3D",
        isDark = true,
        category = "Luxury • Stylish • Premium",
        subtitle = "ELITE FITNESS CLUB",
        motto = "\"A Better You\nEveryday\"",
        footer = "EXCLUSIVE MEMBERSHIP\nPREMIUM LIFE",
        timeText = "5:45 PM",
        timeRelative = "Today",
        actionChipText = "+ VIP MEMBER",
        ctaText = "View Elite Services",
        defaultMemberId = "BG001"
    ),
    VIBRANT_GRADIENT(
        title = "Vibrant Gradient",
        isDark = false,
        category = "Youthful • Dynamic • Colorful",
        subtitle = "FITNESS FOR A BRIGHTER YOU",
        motto = "Train\nEat\nRepeat",
        footer = "GOOD ENERGY\nEVERYDAY",
        timeText = "11:30 AM",
        timeRelative = "Today",
        actionChipText = "+ PAYMENT",
        ctaText = "Pay Now",
        defaultMemberId = "BG220"
    ),
    BEAST_MODE(
        title = "Gym Beast Mode",
        isDark = true,
        category = "Powerful • Intense • Motivational",
        subtitle = "BEAST MODE ON",
        motto = "NO\nPAIN\nNO\nGAIN",
        footer = "BE A BETTER YOU",
        timeText = "9:10 PM",
        timeRelative = "Today",
        actionChipText = "+ CHECK-IN",
        ctaText = "Take Action",
        defaultMemberId = "BG330"
    ),
    PURPLE_ROYAL(
        title = "Purple Royal",
        isDark = true,
        category = "Elegant • Royal • Exclusive",
        subtitle = "FITNESS ROYALTY",
        motto = "Stronger\nFitter\nHappier",
        footer = "FITNESS TODAY\nA STRONGER YOU TOMORROW",
        timeText = "10:15 AM",
        timeRelative = "Today",
        actionChipText = "+ SERVICE",
        ctaText = "View Services",
        defaultMemberId = "BG502"
    );

    fun colors(): BADGymColors = when (this) {
        NATURAL_FRESH -> NaturalFreshColors
        FUTURISTIC_NEON -> FuturisticNeonColors
        MINIMAL_DARK -> MinimalDarkColors
        GLASSMORPHISM -> GlassmorphismColors
        PREMIUM_3D -> Premium3DColors
        VIBRANT_GRADIENT -> VibrantGradientColors
        BEAST_MODE -> BeastModeColors
        PURPLE_ROYAL -> PurpleRoyalColors
    }
}
