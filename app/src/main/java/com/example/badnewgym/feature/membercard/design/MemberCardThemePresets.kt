package com.example.badnewgym.feature.membercard.design

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 8 Canonical Theme Presets for BAD GYM Member Intelligence Card
 * Extracted directly from user reference mockup:
 * 01 - Emerald Glass Modern (Premium)
 * 02 - Clean White Minimal
 * 03 - 3D Glass Premium
 * 04 - Purple Premium
 * 05 - Red Gold Premium
 * 06 - Dark Mint Glass Pro
 * 07 - Gradient Card Style
 * 08 - Natural Soft Card
 */
enum class MemberCardTheme(
    val id: String,
    val title: String,
    val shortName: String,
    val badge: String
) {
    EMERALD_GLASS_MODERN("01", "Emerald Glass Modern (Premium)", "01 Emerald", "PREMIUM"),
    CLEAN_WHITE_MINIMAL("02", "Clean White Minimal", "02 White", "MINIMAL"),
    THREE_D_GLASS_PREMIUM("03", "3D Glass Premium", "03 3D Glass", "PREMIUM"),
    PURPLE_PREMIUM("04", "Purple Premium", "04 Purple", "PREMIUM"),
    RED_GOLD_PREMIUM("05", "Red Gold Premium", "05 Red Gold", "PREMIUM"),
    DARK_MINT_GLASS_PRO("06", "Dark Mint Glass Pro", "06 Dark Mint", "PRO"),
    GRADIENT_CARD_STYLE("07", "Gradient Card Style", "07 Gradient", "STYLE"),
    NATURAL_SOFT_CARD("08", "Natural Soft Card", "08 Natural", "SOFT")
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
    var activeTheme: MemberCardTheme by mutableStateOf(MemberCardTheme.EMERALD_GLASS_MODERN)

    fun getPalette(theme: MemberCardTheme = activeTheme): ThemeColorPalette {
        return when (theme) {
            // 01: Emerald Glass Modern (Premium) - Master Reference Frosted Aqua Glass
            MemberCardTheme.EMERALD_GLASS_MODERN -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.94f),
                        Color(0xFFF0FDF4).copy(alpha = 0.88f)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(
                        Color(0xFF6EE7B7).copy(alpha = 0.85f),
                        Color(0xFF34D399).copy(alpha = 0.50f),
                        Color(0xFFA7F3D0).copy(alpha = 0.70f)
                    )
                ),
                textPrimary = Color(0xFF0F172A),
                textSecondary = Color(0xFF475569),
                textMuted = Color(0xFF64748B),
                iconTint = Color(0xFF059669),
                headerPillBg = Color(0xFFDCFCE7),
                headerPillText = Color(0xFF15803D),
                statChipBg = Color.White.copy(alpha = 0.92f),
                statChipBorder = Color(0xFFE2E8F0),
                haloBrush = Brush.sweepGradient(
                    listOf(
                        Color(0xFF10B981),
                        Color(0xFF06B6D4),
                        Color(0xFF3B82F6),
                        Color(0xFF10B981)
                    )
                ),
                progressBarTrack = Color(0xFFE2E8F0),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF10B981), Color(0xFF06B6D4))
                ),
                progressPercentColor = Color(0xFF059669),
                // Bento card backgrounds & borders
                bentoAttendanceBg = Brush.verticalGradient(
                    listOf(Color(0xFFD1FAE5).copy(alpha = 0.90f), Color(0xFFECFDF5).copy(alpha = 0.95f))
                ),
                bentoAttendanceBorder = Color(0xFF6EE7B7).copy(alpha = 0.85f),
                bentoWorkoutBg = Brush.verticalGradient(
                    listOf(Color(0xFFBAE6FD).copy(alpha = 0.90f), Color(0xFFF0F9FF).copy(alpha = 0.95f))
                ),
                bentoWorkoutBorder = Color(0xFF7DD3FC).copy(alpha = 0.85f),
                bentoPaymentBg = Brush.verticalGradient(
                    listOf(Color(0xFFFED7AA).copy(alpha = 0.90f), Color(0xFFFFFBEB).copy(alpha = 0.95f))
                ),
                bentoPaymentBorder = Color(0xFFFDBA74).copy(alpha = 0.85f),
                bentoBodyBg = Brush.verticalGradient(
                    listOf(Color(0xFFF5D0FE).copy(alpha = 0.90f), Color(0xFFFDF4FF).copy(alpha = 0.95f))
                ),
                bentoBodyBorder = Color(0xFFF0ABFC).copy(alpha = 0.85f),
                // Chart node/line colors
                chartAttendanceColor = Color(0xFF10B981),
                chartWorkoutColor = Color(0xFF0284C7),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFF59E0B), Color(0xFFD97706))),
                chartBodyColor = Color(0xFFA855F7),
                themeAccent = Color(0xFF10B981),
                stats = ThemeQuickStats(
                    weight = "69.5 kg",
                    weightTarget = "Target 68",
                    height = "178 cm",
                    heightLabel = "Height",
                    goal = "Lean Muscle",
                    goalLabel = "Goal",
                    daysThisWeek = "4 Days",
                    weekLabel = "This Week",
                    totalVisits = "18",
                    visitsLabel = "Total Visits"
                )
            )
            // 02: Clean White Minimal
            MemberCardTheme.CLEAN_WHITE_MINIMAL -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF8FAFC),
                        Color(0xFFF1F5F9)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1), Color(0xFFE2E8F0))
                ),
                textPrimary = Color(0xFF0F172A),
                textSecondary = Color(0xFF475569),
                textMuted = Color(0xFF64748B),
                iconTint = Color(0xFF0F172A),
                headerPillBg = Color(0xFF0F172A),
                headerPillText = Color.White,
                statChipBg = Color.White,
                statChipBorder = Color(0xFFE2E8F0),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFF0EA5E9), Color(0xFF38BDF8), Color(0xFF60A5FA), Color(0xFF0EA5E9))
                ),
                progressBarTrack = Color(0xFFE2E8F0),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF0EA5E9), Color(0xFF38BDF8))
                ),
                progressPercentColor = Color(0xFF0284C7),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF0FDF4))),
                bentoAttendanceBorder = Color(0xFF86EFAC),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFFFBEB))),
                bentoPaymentBorder = Color(0xFFFDE68A),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAF5FF))),
                bentoBodyBorder = Color(0xFFE9D5FF),
                chartAttendanceColor = Color(0xFF10B981),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFFA855F7),
                themeAccent = Color(0xFF0EA5E9),
                stats = ThemeQuickStats(
                    weight = "68 kg",
                    weightTarget = "Target 65",
                    height = "5'8\"",
                    goal = "Fat Loss",
                    daysThisWeek = "3 Days",
                    totalVisits = "28"
                )
            )

            // 03: 3D Glass Premium
            MemberCardTheme.THREE_D_GLASS_PREMIUM -> ThemeColorPalette(
                isDark = true,
                cardBackground = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0C3B68),
                        Color(0xFF0E4C86),
                        Color(0xFF145999),
                        Color(0xFF0A2B4C)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(
                        Color(0xFF38BDF8).copy(alpha = 0.9f),
                        Color(0xFF818CF8).copy(alpha = 0.6f),
                        Color(0xFF06B6D4).copy(alpha = 0.8f)
                    )
                ),
                textPrimary = Color.White,
                textSecondary = Color(0xFFBAE6FD),
                textMuted = Color(0xFF7DD3FC),
                iconTint = Color(0xFF38BDF8),
                headerPillBg = Color(0xFF0369A1).copy(alpha = 0.85f),
                headerPillText = Color.White,
                statChipBg = Color(0xFF082F54).copy(alpha = 0.70f),
                statChipBorder = Color(0xFF0284C7).copy(alpha = 0.45f),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFF00E5FF), Color(0xFF38BDF8), Color(0xFF818CF8), Color(0xFF00E5FF))
                ),
                progressBarTrack = Color.White.copy(alpha = 0.15f),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF00E5FF), Color(0xFF38BDF8), Color(0xFF818CF8))
                ),
                progressPercentColor = Color(0xFF38BDF8),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF047857))),
                bentoAttendanceBorder = Color(0xFF34D399).copy(alpha = 0.5f),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7))),
                bentoPaymentBorder = Color(0xFFF59E0B).copy(alpha = 0.5f),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF))),
                bentoBodyBorder = Color(0xFFA855F7).copy(alpha = 0.5f),
                chartAttendanceColor = Color(0xFF34D399),
                chartWorkoutColor = Color(0xFF38BDF8),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFFA855F7),
                themeAccent = Color(0xFF38BDF8),
                stats = ThemeQuickStats(
                    weight = "68 kg",
                    weightTarget = "Target 65",
                    height = "5'8\"",
                    goal = "Strength",
                    daysThisWeek = "5 Days",
                    totalVisits = "45"
                )
            )

            // 04: Purple Premium
            MemberCardTheme.PURPLE_PREMIUM -> ThemeColorPalette(
                isDark = true,
                cardBackground = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF4A0E78),
                        Color(0xFF5E1B96),
                        Color(0xFF3B0B61),
                        Color(0xFF2C0749)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(
                        Color(0xFFC084FC).copy(alpha = 0.9f),
                        Color(0xFFE879F9).copy(alpha = 0.6f),
                        Color(0xFFA855F7).copy(alpha = 0.8f)
                    )
                ),
                textPrimary = Color.White,
                textSecondary = Color(0xFFE9D5FF),
                textMuted = Color(0xFFD8B4FE),
                iconTint = Color(0xFFC084FC),
                headerPillBg = Color(0xFF6B21A8).copy(alpha = 0.85f),
                headerPillText = Color.White,
                statChipBg = Color(0xFF3B0764).copy(alpha = 0.70f),
                statChipBorder = Color(0xFFA855F7).copy(alpha = 0.45f),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFFA855F7), Color(0xFFC084FC), Color(0xFFE879F9), Color(0xFFA855F7))
                ),
                progressBarTrack = Color.White.copy(alpha = 0.15f),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFFA855F7), Color(0xFFC084FC), Color(0xFFE879F9))
                ),
                progressPercentColor = Color(0xFFC084FC),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF047857))),
                bentoAttendanceBorder = Color(0xFF34D399).copy(alpha = 0.5f),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7))),
                bentoPaymentBorder = Color(0xFFF59E0B).copy(alpha = 0.5f),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF))),
                bentoBodyBorder = Color(0xFFA855F7).copy(alpha = 0.5f),
                chartAttendanceColor = Color(0xFF34D399),
                chartWorkoutColor = Color(0xFF38BDF8),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFFA855F7),
                themeAccent = Color(0xFFA855F7),
                stats = ThemeQuickStats(
                    weight = "68 kg",
                    weightTarget = "Target 65",
                    height = "5'8\"",
                    goal = "Lean Muscle",
                    daysThisWeek = "4 Days",
                    totalVisits = "37"
                )
            )

            // 05: Red Gold Premium
            MemberCardTheme.RED_GOLD_PREMIUM -> ThemeColorPalette(
                isDark = true,
                cardBackground = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF6B1111),
                        Color(0xFF881919),
                        Color(0xFF500B0B),
                        Color(0xFF3A0606)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(
                        Color(0xFFF87171).copy(alpha = 0.9f),
                        Color(0xFFFBBF24).copy(alpha = 0.6f),
                        Color(0xFFEF4444).copy(alpha = 0.8f)
                    )
                ),
                textPrimary = Color.White,
                textSecondary = Color(0xFFFECACA),
                textMuted = Color(0xFFFCA5A5),
                iconTint = Color(0xFFFBBF24),
                headerPillBg = Color(0xFF991B1B).copy(alpha = 0.85f),
                headerPillText = Color.White,
                statChipBg = Color(0xFF450A0A).copy(alpha = 0.70f),
                statChipBorder = Color(0xFFDC2626).copy(alpha = 0.45f),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFFEF4444), Color(0xFFFBBF24), Color(0xFFF87171), Color(0xFFEF4444))
                ),
                progressBarTrack = Color.White.copy(alpha = 0.15f),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFFEF4444), Color(0xFFFBBF24))
                ),
                progressPercentColor = Color(0xFFFBBF24),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF047857))),
                bentoAttendanceBorder = Color(0xFF34D399).copy(alpha = 0.5f),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7))),
                bentoPaymentBorder = Color(0xFFF59E0B).copy(alpha = 0.5f),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF))),
                bentoBodyBorder = Color(0xFFA855F7).copy(alpha = 0.5f),
                chartAttendanceColor = Color(0xFF34D399),
                chartWorkoutColor = Color(0xFF38BDF8),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFFA855F7),
                themeAccent = Color(0xFFEF4444),
                stats = ThemeQuickStats(
                    weight = "68 kg",
                    weightTarget = "Target 65",
                    height = "5'8\"",
                    goal = "Fat Loss",
                    daysThisWeek = "3 Days",
                    totalVisits = "29"
                )
            )

            // 06: Dark Mint Glass Pro
            MemberCardTheme.DARK_MINT_GLASS_PRO -> ThemeColorPalette(
                isDark = true,
                cardBackground = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF071B16),
                        Color(0xFF0B2821),
                        Color(0xFF051511),
                        Color(0xFF020907)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(
                        Color(0xFF10B981).copy(alpha = 0.85f),
                        Color(0xFF00E5FF).copy(alpha = 0.50f),
                        Color(0xFF059669).copy(alpha = 0.75f)
                    )
                ),
                textPrimary = Color.White,
                textSecondary = Color(0xFF6EE7B7),
                textMuted = Color(0xFF34D399).copy(alpha = 0.85f),
                iconTint = Color(0xFF10B981),
                headerPillBg = Color(0xFF064E3B).copy(alpha = 0.90f),
                headerPillText = Color.White,
                statChipBg = Color(0xFF04201A).copy(alpha = 0.80f),
                statChipBorder = Color(0xFF059669).copy(alpha = 0.40f),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFF00E5FF), Color(0xFF10B981), Color(0xFF34D399), Color(0xFF00E5FF))
                ),
                progressBarTrack = Color.White.copy(alpha = 0.12f),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF00E5FF), Color(0xFF10B981), Color(0xFF34D399))
                ),
                progressPercentColor = Color(0xFF10B981),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFF064E3B), Color(0xFF022C22))),
                bentoAttendanceBorder = Color(0xFF10B981).copy(alpha = 0.45f),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF075985), Color(0xFF0C4A6E))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.45f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFF78350F).copy(alpha = 0.6f), Color(0xFF451A03).copy(alpha = 0.7f))),
                bentoPaymentBorder = Color(0xFFF59E0B).copy(alpha = 0.45f),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFF581C87).copy(alpha = 0.6f), Color(0xFF3B0764).copy(alpha = 0.7f))),
                bentoBodyBorder = Color(0xFFA855F7).copy(alpha = 0.45f),
                chartAttendanceColor = Color(0xFF10B981),
                chartWorkoutColor = Color(0xFF38BDF8),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFF59E0B), Color(0xFFD97706))),
                chartBodyColor = Color(0xFFA855F7),
                themeAccent = Color(0xFF00E5FF),
                stats = ThemeQuickStats(
                    weight = "68 kg",
                    weightTarget = "Target 65",
                    height = "5'8\"",
                    goal = "Muscle Gain",
                    daysThisWeek = "5 Days",
                    totalVisits = "42"
                )
            )

            // 07: Gradient Card Style
            MemberCardTheme.GRADIENT_CARD_STYLE -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFEDE9FE),
                        Color(0xFFFCE7F3),
                        Color(0xFFE0E7FF),
                        Color(0xFFE0F2FE)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(
                        Color(0xFFA855F7).copy(alpha = 0.5f),
                        Color(0xFFEC4899).copy(alpha = 0.5f),
                        Color(0xFF38BDF8).copy(alpha = 0.5f)
                    )
                ),
                textPrimary = Color(0xFF1E1B4B),
                textSecondary = Color(0xFF4338CA),
                textMuted = Color(0xFF6366F1),
                iconTint = Color(0xFF7C3AED),
                headerPillBg = Color(0xFF6366F1),
                headerPillText = Color.White,
                statChipBg = Color.White.copy(alpha = 0.75f),
                statChipBorder = Color(0xFFC7D2FE),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFF8B5CF6), Color(0xFFEC4899), Color(0xFF38BDF8), Color(0xFF8B5CF6))
                ),
                progressBarTrack = Color(0xFFC7D2FE).copy(alpha = 0.6f),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF8B5CF6), Color(0xFFEC4899))
                ),
                progressPercentColor = Color(0xFF7C3AED),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF0FDF4))),
                bentoAttendanceBorder = Color(0xFF86EFAC),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFFFBEB))),
                bentoPaymentBorder = Color(0xFFFDE68A),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAF5FF))),
                bentoBodyBorder = Color(0xFFE9D5FF),
                chartAttendanceColor = Color(0xFF10B981),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFFA855F7),
                themeAccent = Color(0xFF8B5CF6),
                stats = ThemeQuickStats(
                    weight = "68 kg",
                    weightTarget = "Target 65",
                    height = "5'8\"",
                    goal = "Endurance",
                    daysThisWeek = "4 Days",
                    totalVisits = "34"
                )
            )

            // 08: Natural Soft Card
            MemberCardTheme.NATURAL_SOFT_CARD -> ThemeColorPalette(
                isDark = false,
                cardBackground = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF8F6F0),
                        Color(0xFFF0ECE1),
                        Color(0xFFEAE4D5),
                        Color(0xFFE2DBCB)
                    )
                ),
                cardBorder = Brush.linearGradient(
                    listOf(Color(0xFFD6CEBE), Color(0xFFC7BCAB), Color(0xFFD6CEBE))
                ),
                textPrimary = Color(0xFF292524),
                textSecondary = Color(0xFF57534E),
                textMuted = Color(0xFF78716C),
                iconTint = Color(0xFF44403C),
                headerPillBg = Color(0xFF292524),
                headerPillText = Color(0xFFF5F5F4),
                statChipBg = Color.White.copy(alpha = 0.70f),
                statChipBorder = Color(0xFFD6CEBE),
                haloBrush = Brush.sweepGradient(
                    listOf(Color(0xFF10B981), Color(0xFF059669), Color(0xFF34D399), Color(0xFF10B981))
                ),
                progressBarTrack = Color(0xFFD6CEBE).copy(alpha = 0.5f),
                progressBarFill = Brush.horizontalGradient(
                    listOf(Color(0xFF059669), Color(0xFF10B981))
                ),
                progressPercentColor = Color(0xFF059669),
                bentoAttendanceBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF0FDF4))),
                bentoAttendanceBorder = Color(0xFF86EFAC),
                bentoWorkoutBg = Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))),
                bentoWorkoutBorder = Color(0xFF38BDF8).copy(alpha = 0.5f),
                bentoPaymentBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFFFBEB))),
                bentoPaymentBorder = Color(0xFFFDE68A),
                bentoBodyBg = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAF5FF))),
                bentoBodyBorder = Color(0xFFE9D5FF),
                chartAttendanceColor = Color(0xFF10B981),
                chartWorkoutColor = Color(0xFFFFFFFF),
                chartPaymentBarBrush = Brush.verticalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEA580C))),
                chartBodyColor = Color(0xFFA855F7),
                themeAccent = Color(0xFF059669),
                stats = ThemeQuickStats(
                    weight = "68 kg",
                    weightTarget = "Target 65",
                    height = "5'8\"",
                    goal = "General",
                    daysThisWeek = "3 Days",
                    totalVisits = "26"
                )
            )
        }
    }
}
