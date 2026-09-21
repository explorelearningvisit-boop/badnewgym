package com.example.badnewgym.feature.memberintelligence.design

import com.example.badnewgym.feature.memberintelligence.design.colors.*

/**
 * Eight visual skins for the same Member Intelligence experience.
 * Business facts stay identical; only visual personality changes.
 */
enum class ThemeId(
    val title: String,
    val isDark: Boolean,
    val category: String,
    val subtitle: String,
    val headerTag: String,
    val motto: String,
    val footer: String,
    val timeText: String,
    val timeRelative: String,
    val actionChipText: String,
    val ctaText: String,
    val defaultMemberId: String
) {
    NATURAL_FRESH("Natural Fresh", false, "Wellness • Clean • Friendly", "Healthy People\nHappier Lives", "Healthy People\nHappier Lives", "Good Fitness\nBrighter You", "Small Steps\nBig Results", "4:03 PM", "Just now", "+ CHECK-IN", "Collect Payment", "BG204"),
    FUTURISTIC_NEON("Futuristic Neon", true, "Bold • Energetic • High Tech", "BEYOND LIMITS", "Discipline Today\nA Stronger Tomorrow", "STRONGER\nEVERY DAY", "DISCIPLINE TODAY\nA STRONGER TOMORROW", "4:03 PM", "Just now", "+ CHECK-IN", "Collect Payment", "BG204"),
    MINIMAL_DARK("Minimal Dark", true, "Simple • Elegant • Focused", "FOCUS • TRAIN • GROW", "Less Excuses\nMore Results", "CONSISTENCY\nCREATES CHANGE", "LESS EXCUSES\nMORE RESULTS", "4:03 PM", "Just now", "+ CHECK-IN", "Collect Payment", "BG204"),
    GLASSMORPHISM("Glassmorphism", false, "Translucent • Modern • Elegant", "MIND • BODY • COMMUNITY", "More Than Gym\nA Better You", "More Than\nA Gym ↗", "Better People\nBetter Communities", "4:03 PM", "Just now", "+ CHECK-IN", "Collect Payment", "BG204"),
    PREMIUM_3D("Premium 3D", true, "Luxury • Stylish • Premium", "ELITE FITNESS CLUB", "A Premium You\nA Stronger Tomorrow", "Make Fitness\nA Lifestyle", "EXCLUSIVE MEMBERS\nEXTRAORDINARY RESULTS", "4:03 PM", "Just now", "+ CHECK-IN", "Collect Payment", "BG204"),
    VIBRANT_GRADIENT("Vibrant Gradient", false, "Youthful • Dynamic • Colorful", "FITNESS FOR A BRIGHTER YOU", "Good Energy\nEveryday", "Train\nEat\nRepeat", "GOOD ENERGY\nEVERYDAY", "4:03 PM", "Just now", "+ CHECK-IN", "Collect Payment", "BG204"),
    BEAST_MODE("Gym Beast Mode", true, "Powerful • Intense • Motivational", "BEAST MODE ON", "Train Harder\nBe Stronger", "NO PAIN\nNO GAIN", "BEAST MODE\nIS A CHOICE", "4:03 PM", "Just now", "+ CHECK-IN", "Collect Payment", "BG204"),
    PURPLE_ROYAL("Purple Royal", true, "Elegant • Royal • Exclusive", "FITNESS ROYALTY", "Elevate Your\nFitness Journey", "Stronger\nFitter Happier", "FITNESS TODAY\nA STRONGER YOU TOMORROW", "4:03 PM", "Just now", "+ CHECK-IN", "Collect Payment", "BG204");

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
