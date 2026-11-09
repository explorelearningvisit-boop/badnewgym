package com.example.badnewgym.feature.membercard.design

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.BadGymPassTier

/**
 * 8 Canonical BAD GYM Member Pass Themes & Presets
 * Directly representing the 8 Passes from the Master Mockup:
 * 01 - Spark (2 Months) - Green
 * 02 - Power (3 Months) - Cyan/Blue
 * 03 - Silver (6 Months) - Silver Metallic
 * 04 - Gold (12 Months) - Radiant Warm Gold
 * 05 - Diamond (18 Months) - Sky Diamond Blue
 * 06 - Platinum (24 Months) - Amethyst Purple
 * 07 - Elite (36 Months) - Dark Luxury VIP
 * 08 - Basic (1 Month) - Coral Red Overdue
 */
enum class MemberCardTheme(
    val id: String,
    val title: String,
    val shortName: String,
    val badge: String,
    val tier: BadGymPassTier
) {
    SPARK_2_MONTH("01", "Spark (2 Months)", "01 Spark", "SPARK", BadGymPassTier.SPARK),
    POWER_3_MONTH("02", "Power (3 Months)", "02 Power", "POWER", BadGymPassTier.POWER),
    SILVER_6_MONTH("03", "Silver (6 Months)", "03 Silver", "SILVER", BadGymPassTier.SILVER),
    GOLD_12_MONTH("04", "Gold (12 Months)", "04 Gold", "GOLD", BadGymPassTier.GOLD),
    DIAMOND_18_MONTH("05", "Diamond (18 Months)", "05 Diamond", "DIAMOND", BadGymPassTier.DIAMOND),
    PLATINUM_24_MONTH("06", "Platinum (24 Months)", "06 Platinum", "PLATINUM", BadGymPassTier.PLATINUM),
    ELITE_36_MONTH("07", "Elite (36 Months)", "07 Elite", "ELITE", BadGymPassTier.ELITE),
    BASIC_OVERDUE("08", "Basic (1 Month - Overdue)", "08 Basic", "OVERDUE", BadGymPassTier.BASIC),
    CYAN_AQUA("09", "Cyan Aqua", "09 Cyan", "CYAN", BadGymPassTier.CYAN),
    WHITE_MINT("10", "White Mint Glass", "10 Mint", "MINT", BadGymPassTier.WHITE_MINT),
    WHITE_OCEAN("11", "White Ocean Glass", "11 Ocean", "OCEAN", BadGymPassTier.WHITE_OCEAN),
    WHITE_LAVENDER("12", "White Lavender Glass", "12 Lavender", "LAVENDER", BadGymPassTier.WHITE_LAVENDER);

    companion object {
        // Compatibility Aliases for seamless integration with existing callers:
        val EMERALD_GLASS_MODERN = SPARK_2_MONTH
        val CLEAN_WHITE_MINIMAL = POWER_3_MONTH
        val THREE_D_GLASS_PREMIUM = SILVER_6_MONTH
        val PURPLE_PREMIUM = PLATINUM_24_MONTH
        val RED_GOLD_PREMIUM = ELITE_36_MONTH
        val DARK_MINT_GLASS_PRO = DIAMOND_18_MONTH
        val GRADIENT_CARD_STYLE = GOLD_12_MONTH
        val NATURAL_SOFT_CARD = BASIC_OVERDUE
    }

    fun toPass(): BadGymMemberPass {
        return BadGymMemberPass.getAll8Passes().firstOrNull { it.tier == this.tier }
            ?: BadGymMemberPass.getAll8Passes().first()
    }
}

data class ThemeQuickStats(
    val weight: String = "68 kg",
    val weightTarget: String = "Target 65",
    val height: String = "5'8\"",
    val heightLabel: String = "Height",
    val goal: String = "Muscle Gain",
    val goalLabel: String = "Goal",
    val daysThisWeek: String = "4 Days",
    val weekLabel: String = "This Week",
    val totalVisits: String = "32",
    val visitsLabel: String = "Total Visits"
)

data class ThemeColorPalette(
    val isDark: Boolean,
    val cardBackground: Brush,
    val cardBorder: Brush,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val iconTint: Color,
    val headerPillBg: Color,
    val headerPillText: Color,
    val statChipBg: Color,
    val statChipBorder: Color,
    val haloBrush: Brush,
    val progressBarTrack: Color,
    val progressBarFill: Brush,
    val progressPercentColor: Color,
    // Bento card backgrounds & borders
    val bentoAttendanceBg: Brush,
    val bentoAttendanceBorder: Color,
    val bentoWorkoutBg: Brush,
    val bentoWorkoutBorder: Color,
    val bentoPaymentBg: Brush,
    val bentoPaymentBorder: Color,
    val bentoBodyBg: Brush,
    val bentoBodyBorder: Color,
    // Chart node/line colors
    val chartAttendanceColor: Color,
    val chartWorkoutColor: Color,
    val chartPaymentBarBrush: Brush,
    val chartBodyColor: Color,
    val themeAccent: Color,
    val stats: ThemeQuickStats
)

object MemberCardThemeManager {
    /**
     * Globally reactive active theme for the Member Intelligence Card.
     * Recomposes all observers immediately when modified.
     */
    var activeTheme: MemberCardTheme by mutableStateOf(MemberCardTheme.SILVER_6_MONTH)

    fun getPalette(theme: MemberCardTheme = activeTheme): ThemeColorPalette {
        return when (theme) {
            // 01: Spark (2 Months) - Green
            MemberCardTheme.SPARK_2_MONTH -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.verticalGradient(
                    listOf(
                        Color(0xFFE8FDF0),
                        Color(0xFFDAEFE6),
                        Color(0xFFEAF3EF)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(
                        Color(0xFF6EE7B7),
                        Color(0xFF42CD8C),
                        Color(0xFFB7DFD1)
                    )
                ),
                textPrimary = Color(0xFF343434),
                textSecondary = Color(0xFF6E7887),
                textMuted = Color(0xFF8C94A0),
                iconTint = Color(0xFF178A66),
                headerPillBg = Color(0xFFDAEFE6),
                headerPillText = Color(0xFF15803D),
                statChipBg = Color.White.copy(alpha = 0.92f),
                statChipBorder = Color(0xFFC9E8DC),
                haloBrush = Brush.sweepGradient(
                    listOf(
                        Color(0xFF21A87D),
                        Color(0xFF42CD8C),
                        Color(0xFF178A66),
                        Color(0xFF21A87D)
                    )
                ),
                progressBarTrack = Color(0xFFDCDFE6),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF21A87D), Color(0xFF178A66))
                ),
                progressPercentColor = Color(0xFF178A66),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFEAF3EF))),
                bentoAttendanceBorder = Color(0xFF9BD9C2),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF489CC1), Color(0xFF3C7E9C))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF6F0E2))),
                bentoPaymentBorder = Color(0xFFF7E3B2),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE9EDF0))),
                bentoBodyBorder = Color(0xFFDCD9EE),
                chartAttendanceColor = Color(0xFF21A87D),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFFFBB0D), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFF8B91CC),
                themeAccent = Color(0xFF21A87D),
                stats = ThemeQuickStats(
                    weight = "69.5 kg",
                    weightTarget = "Target 67",
                    height = "5'8\"",
                    goal = "Muscle Gain",
                    daysThisWeek = "4 Days",
                    totalVisits = "25"
                )
            )

            // 02: Power (3 Months) - Cyan/Blue
            MemberCardTheme.POWER_3_MONTH -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.verticalGradient(
                    listOf(
                        Color(0xFFE2EAF4),
                        Color(0xFFD3E3F0),
                        Color(0xFFE9EDF0)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(Color(0xFF7DD3FC), Color(0xFF38BDF8))
                ),
                textPrimary = Color(0xFF343434),
                textSecondary = Color(0xFF6E7887),
                textMuted = Color(0xFF8C94A0),
                iconTint = Color(0xFF489CC1),
                headerPillBg = Color(0xFFE2EAF4),
                headerPillText = Color(0xFF3C7E9C),
                statChipBg = Color.White.copy(alpha = 0.90f),
                statChipBorder = Color(0xFFD3E3F0),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFF489CC1), Color(0xFF38BDF8), Color(0xFF3C7E9C), Color(0xFF489CC1))
                ),
                progressBarTrack = Color(0xFFDCDFE6),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF489CC1), Color(0xFF00A3FF))
                ),
                progressPercentColor = Color(0xFF489CC1),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFEAF3EF))),
                bentoAttendanceBorder = Color(0xFF9BD9C2),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF489CC1), Color(0xFF3C7E9C))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF6F0E2))),
                bentoPaymentBorder = Color(0xFFF7E3B2),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE9EDF0))),
                bentoBodyBorder = Color(0xFFDCD9EE),
                chartAttendanceColor = Color(0xFF21A87D),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFFFBB0D), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFF8B91CC),
                themeAccent = Color(0xFF489CC1),
                stats = ThemeQuickStats(
                    weight = "58.2 kg",
                    weightTarget = "Target 56",
                    height = "5'4\"",
                    goal = "Strength",
                    daysThisWeek = "5 Days",
                    totalVisits = "60"
                )
            )

            // 03: Silver (6 Months) - Silver Metallic
            MemberCardTheme.SILVER_6_MONTH -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.verticalGradient(
                    listOf(
                        Color(0xFFEDF0F4),
                        Color(0xFFDCDFE6),
                        Color(0xFFE6E9EF)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(Color(0xFFD3DBE6), Color(0xFFA1AEBF))
                ),
                textPrimary = Color(0xFF343434),
                textSecondary = Color(0xFF5C6673),
                textMuted = Color(0xFF8C94A0),
                iconTint = Color(0xFF6E7887),
                headerPillBg = Color(0xFFE6E9EF),
                headerPillText = Color(0xFF5C6673),
                statChipBg = Color.White.copy(alpha = 0.90f),
                statChipBorder = Color(0xFFDCDFE6),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFFA1AEBF), Color(0xFFD3DBE6), Color(0xFF8C94A0), Color(0xFFA1AEBF))
                ),
                progressBarTrack = Color(0xFFDCDFE6),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF8C94A0), Color(0xFF6E7887))
                ),
                progressPercentColor = Color(0xFF6E7887),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFEAF3EF))),
                bentoAttendanceBorder = Color(0xFF9BD9C2),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF489CC1), Color(0xFF3C7E9C))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF6F0E2))),
                bentoPaymentBorder = Color(0xFFF7E3B2),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE9EDF0))),
                bentoBodyBorder = Color(0xFFDCD9EE),
                chartAttendanceColor = Color(0xFF21A87D),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFFFBB0D), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFF8B91CC),
                themeAccent = Color(0xFF8C94A0),
                stats = ThemeQuickStats(
                    weight = "74.1 kg",
                    weightTarget = "Target 70",
                    height = "5'9\"",
                    goal = "Fat Loss",
                    daysThisWeek = "4 Days",
                    totalVisits = "92"
                )
            )

            // 04: Gold (12 Months) - Radiant Warm Gold
            MemberCardTheme.GOLD_12_MONTH -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.verticalGradient(
                    listOf(
                        Color(0xFFF6F0E2),
                        Color(0xFFFBEFD4),
                        Color(0xFFF7E3B2).copy(alpha = 0.7f)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(Color(0xFFFCD34D), Color(0xFFFFBB0D))
                ),
                textPrimary = Color(0xFF343434),
                textSecondary = Color(0xFF78350F),
                textMuted = Color(0xFFB5850A),
                iconTint = Color(0xFFD99F0B),
                headerPillBg = Color(0xFFFBEFD4),
                headerPillText = Color(0xFFB5850A),
                statChipBg = Color.White.copy(alpha = 0.90f),
                statChipBorder = Color(0xFFF7E3B2),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFFFFBB0D), Color(0xFFFDE047), Color(0xFFD99F0B), Color(0xFFFFBB0D))
                ),
                progressBarTrack = Color(0xFFFBEFD4),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFFFFBB0D), Color(0xFFD99F0B))
                ),
                progressPercentColor = Color(0xFFD99F0B),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFEAF3EF))),
                bentoAttendanceBorder = Color(0xFF9BD9C2),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF489CC1), Color(0xFF3C7E9C))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF6F0E2))),
                bentoPaymentBorder = Color(0xFFF7E3B2),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE9EDF0))),
                bentoBodyBorder = Color(0xFFDCD9EE),
                chartAttendanceColor = Color(0xFF21A87D),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFFFBB0D), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFF8B91CC),
                themeAccent = Color(0xFFFFBB0D),
                stats = ThemeQuickStats(
                    weight = "60.0 kg",
                    weightTarget = "Target 58",
                    height = "5'5\"",
                    goal = "Body Toning",
                    daysThisWeek = "5 Days",
                    totalVisits = "320"
                )
            )

            // 05: Diamond (18 Months) - Sky Diamond Blue
            MemberCardTheme.DIAMOND_18_MONTH -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.verticalGradient(
                    listOf(
                        Color(0xFFE9EDF0),
                        Color(0xFFD3E3F0),
                        Color(0xFFE2EAF4)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(Color(0xFF38BDF8), Color(0xFF489CC1))
                ),
                textPrimary = Color(0xFF343434),
                textSecondary = Color(0xFF3C7E9C),
                textMuted = Color(0xFF489CC1),
                iconTint = Color(0xFF489CC1),
                headerPillBg = Color(0xFFE2EAF4),
                headerPillText = Color(0xFF3C7E9C),
                statChipBg = Color.White.copy(alpha = 0.90f),
                statChipBorder = Color(0xFFD3E3F0),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFF38BDF8), Color(0xFF489CC1), Color(0xFF67E8F9), Color(0xFF38BDF8))
                ),
                progressBarTrack = Color(0xFFE2EAF4),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF489CC1), Color(0xFF3C7E9C))
                ),
                progressPercentColor = Color(0xFF489CC1),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFEAF3EF))),
                bentoAttendanceBorder = Color(0xFF9BD9C2),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF489CC1), Color(0xFF3C7E9C))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF6F0E2))),
                bentoPaymentBorder = Color(0xFFF7E3B2),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE9EDF0))),
                bentoBodyBorder = Color(0xFFDCD9EE),
                chartAttendanceColor = Color(0xFF21A87D),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFFFBB0D), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFF8B91CC),
                themeAccent = Color(0xFF489CC1),
                stats = ThemeQuickStats(
                    weight = "72.4 kg",
                    weightTarget = "Target 70",
                    height = "5'10\"",
                    goal = "Strength",
                    daysThisWeek = "6 Days",
                    totalVisits = "540"
                )
            )

            // 06: Platinum (24 Months) - Amethyst Purple
            MemberCardTheme.PLATINUM_24_MONTH -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.verticalGradient(
                    listOf(
                        Color(0xFFE9EDF0),
                        Color(0xFFF3E8FF),
                        Color(0xFFDCD9EE)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(Color(0xFFA9AEE3), Color(0xFF9333EA))
                ),
                textPrimary = Color(0xFF343434),
                textSecondary = Color(0xFF581C87),
                textMuted = Color(0xFF6A70A8),
                iconTint = Color(0xFF9333EA),
                headerPillBg = Color(0xFFF3E8FF),
                headerPillText = Color(0xFF6A70A8),
                statChipBg = Color.White.copy(alpha = 0.90f),
                statChipBorder = Color(0xFFD8DBEE),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFF8B91CC), Color(0xFFA9AEE3), Color(0xFF6A70A8), Color(0xFF8B91CC))
                ),
                progressBarTrack = Color(0xFFF3E8FF),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF9333EA), Color(0xFF6A70A8))
                ),
                progressPercentColor = Color(0xFF9333EA),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFEAF3EF))),
                bentoAttendanceBorder = Color(0xFF9BD9C2),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF489CC1), Color(0xFF3C7E9C))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF6F0E2))),
                bentoPaymentBorder = Color(0xFFF7E3B2),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE9EDF0))),
                bentoBodyBorder = Color(0xFFDCD9EE),
                chartAttendanceColor = Color(0xFF21A87D),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFFFBB0D), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFF8B91CC),
                themeAccent = Color(0xFF9333EA),
                stats = ThemeQuickStats(
                    weight = "67.8 kg",
                    weightTarget = "Target 65",
                    height = "5'7\"",
                    goal = "Muscle Gain",
                    daysThisWeek = "5 Days",
                    totalVisits = "700"
                )
            )

            // 07: Elite (36 Months) - Obsidian & Royal Gold VIP
            MemberCardTheme.ELITE_36_MONTH -> ThemeColorPalette(
                isDark = true,
                cardBackground = Brush.verticalGradient(
                    listOf(
                        Color(0xFF26211C),
                        Color(0xFF181512),
                        Color(0xFF0F0D0C)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(Color(0xFFFFBB0D), Color(0xFFCA8A04), Color(0xFFEAB308))
                ),
                textPrimary = Color.White,
                textSecondary = Color(0xFFFDE047),
                textMuted = Color(0xFFCA8A04),
                iconTint = Color(0xFFEAB308),
                headerPillBg = Color(0xFF332A1D),
                headerPillText = Color(0xFFFDE047),
                statChipBg = Color(0xFF221E19),
                statChipBorder = Color(0xFF523E15),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFFFFBB0D), Color(0xFFCA8A04), Color(0xFFEAB308), Color(0xFFFFBB0D))
                ),
                progressBarTrack = Color(0xFF2B251F),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFFEAB308), Color(0xFFB5850A))
                ),
                progressPercentColor = Color(0xFFFDE047),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFF221E19), Color(0xFF181512))),
                bentoAttendanceBorder = Color(0xFF523E15),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF343434))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFF221E19), Color(0xFF181512))),
                bentoPaymentBorder = Color(0xFF856218),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFF221E19), Color(0xFF181512))),
                bentoBodyBorder = Color(0xFF523E15),
                chartAttendanceColor = Color(0xFFFFBB0D),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFEAB308), Color(0xFFCA8A04))),
                chartBodyColor = Color(0xFFFDE047),
                themeAccent = Color(0xFFEAB308),
                stats = ThemeQuickStats(
                    weight = "71.2 kg",
                    weightTarget = "Target 70",
                    height = "5'8\"",
                    goal = "Overall Fitness",
                    daysThisWeek = "6 Days",
                    totalVisits = "980"
                )
            )

            // 08: Basic (1 Month) - Coral Red Overdue
            MemberCardTheme.BASIC_OVERDUE -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF1F2),
                        Color(0xFFFFE4E6),
                        Color(0xFFFECDD3).copy(alpha = 0.5f)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(Color(0xFFFDA4AF), Color(0xFFF43F5E))
                ),
                textPrimary = Color(0xFF343434),
                textSecondary = Color(0xFF9F1239),
                textMuted = Color(0xFFBE123C),
                iconTint = Color(0xFFE11D48),
                headerPillBg = Color(0xFFFFE4E6),
                headerPillText = Color(0xFFBE123C),
                statChipBg = Color.White.copy(alpha = 0.90f),
                statChipBorder = Color(0xFFFECDD3),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFFF43F5E), Color(0xFFFDA4AF), Color(0xFFE11D48), Color(0xFFF43F5E))
                ),
                progressBarTrack = Color(0xFFFFE4E6),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFFFDA4AF), Color(0xFFF43F5E))
                ),
                progressPercentColor = Color(0xFFE11D48),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFEAF3EF))),
                bentoAttendanceBorder = Color(0xFF9BD9C2),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF489CC1), Color(0xFF3C7E9C))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF6F0E2))),
                bentoPaymentBorder = Color(0xFFF7E3B2),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE9EDF0))),
                bentoBodyBorder = Color(0xFFDCD9EE),
                chartAttendanceColor = Color(0xFF21A87D),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFFFBB0D), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFF8B91CC),
                themeAccent = Color(0xFFE11D48),
                stats = ThemeQuickStats(
                    weight = "56.0 kg",
                    weightTarget = "Target 55",
                    height = "5'3\"",
                    goal = "General Fitness",
                    daysThisWeek = "1 Days",
                    totalVisits = "18"
                )
            )

            // 09: Cyan Aqua
            MemberCardTheme.CYAN_AQUA -> getPalette(MemberCardTheme.POWER_3_MONTH).copy(
                themeAccent = Color(0xFF0D9488)
            )

            // 10: White Mint Glass
            MemberCardTheme.WHITE_MINT -> getPalette(MemberCardTheme.SPARK_2_MONTH).copy(
                themeAccent = Color(0xFF178A66)
            )

            // 11: White Ocean Glass
            MemberCardTheme.WHITE_OCEAN -> getPalette(MemberCardTheme.POWER_3_MONTH).copy(
                themeAccent = Color(0xFF489CC1)
            )

            // 12: White Lavender Glass
            MemberCardTheme.WHITE_LAVENDER -> getPalette(MemberCardTheme.PLATINUM_24_MONTH).copy(
                themeAccent = Color(0xFF9333EA)
            )
        }
    }
}
