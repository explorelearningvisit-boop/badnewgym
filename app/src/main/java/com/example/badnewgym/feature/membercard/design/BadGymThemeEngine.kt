package com.example.badnewgym.feature.membercard.design

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * BAD GYM — Complete 10 Themes Engine
 * Direct 1:1 Implementation of the "10 Premium Dashboard Backgrounds / Themes (4K) Complete Design Asset Pack":
 * 01 - Neon Lime Fitness
 * 02 - Gaming Red
 * 03 - Smart Watch Green
 * 04 - Berry Purple (Default Signature Cosmic Splash)
 * 05 - Grill Orange
 * 06 - Muesli Fresh
 * 07 - Shake Yellow
 * 08 - Energy Green
 * 09 - 3D Blue / Sky Clouds
 * 10 - Pink AI Fitness
 */
enum class BadGymDashboardTheme(
    val code: String,
    val title: String,
    val subtitle: String,
    val primaryColor: Color,
    val secondaryColor: Color
) {
    NEON_LIME(
        code = "01",
        title = "Neon Lime",
        subtitle = "Cyberpunk High-Volt",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    ),
    GAMING_RED(
        code = "02",
        title = "Gaming Red",
        subtitle = "Crimson Intensity",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    ),
    SMART_WATCH_GREEN(
        code = "03",
        title = "Smart Watch",
        subtitle = "Ultra Mint Tech",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    ),
    BERRY_PURPLE(
        code = "04",
        title = "Berry Purple",
        subtitle = "Cosmic Splash Signature",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    ),
    GRILL_ORANGE(
        code = "05",
        title = "Grill Orange",
        subtitle = "Flame Smoke Energy",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    ),
    MUESLI_FRESH(
        code = "06",
        title = "Muesli Fresh",
        subtitle = "Honey Toasted Amber",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    ),
    SHAKE_YELLOW(
        code = "07",
        title = "Shake Yellow",
        subtitle = "Vibrant Mango Gold",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    ),
    ENERGY_GREEN(
        code = "08",
        title = "Energy Green",
        subtitle = "Clean Pure Botanical",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    ),
    SKY_BLUE(
        code = "09",
        title = "3D Blue Sky",
        subtitle = "Cloud Sky & Warm Orange",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    ),
    PINK_AI(
        code = "10",
        title = "Pink AI",
        subtitle = "Futuristic Cyber Glow",
        primaryColor = Color(0xFF7B81BE),
        secondaryColor = Color(0xFF5E649E)
    );

    fun getCanvasGradient(): Brush {
        return when (this) {
            NEON_LIME -> Brush.verticalGradient(
                0.00f to Color(0xFF041406),
                0.08f to Color(0xFF0E3812),
                0.16f to Color(0xFF16531D),
                0.22f to Color(0xFFEAF3EF),
                0.40f to Color(0xFFEDF0F4),
                1.00f to Color(0xFFFFFFFF)
            )
            GAMING_RED -> Brush.verticalGradient(
                0.00f to Color(0xFF170404),
                0.08f to Color(0xFF450A0A),
                0.16f to Color(0xFF8E3A3A),
                0.22f to Color(0xFFFFF1F2),
                0.40f to Color(0xFFEDF0F4),
                1.00f to Color(0xFFFFFFFF)
            )
            SMART_WATCH_GREEN -> Brush.verticalGradient(
                0.00f to Color(0xFF021711),
                0.08f to Color(0xFF343434),
                0.16f to Color(0xFF178A66),
                0.22f to Color(0xFFECFDF5),
                0.40f to Color(0xFFEDF0F4),
                1.00f to Color(0xFFFFFFFF)
            )
            BERRY_PURPLE -> Brush.verticalGradient(
                0.00f to Color(0xFF09041A),
                0.07f to Color(0xFF1E0736),
                0.14f to Color(0xFF380854),
                0.20f to Color(0xFFE9EDF0),
                0.38f to Color(0xFFEDF0F4),
                1.00f to Color(0xFFFFFFFF)
            )
            GRILL_ORANGE -> Brush.verticalGradient(
                0.00f to Color(0xFF1C0A03),
                0.08f to Color(0xFF431407),
                0.16f to Color(0xFF7C2D12),
                0.22f to Color(0xFFFFF7ED),
                0.40f to Color(0xFFEDF0F4),
                1.00f to Color(0xFFFFFFFF)
            )
            MUESLI_FRESH -> Brush.verticalGradient(
                0.00f to Color(0xFF1A1204),
                0.08f to Color(0xFF451A03),
                0.16f to Color(0xFF78350F),
                0.22f to Color(0xFFFBEFD4),
                0.40f to Color(0xFFEDF0F4),
                1.00f to Color(0xFFFFFFFF)
            )
            SHAKE_YELLOW -> Brush.verticalGradient(
                0.00f to Color(0xFF1A1603),
                0.08f to Color(0xFF422006),
                0.16f to Color(0xFF713F12),
                0.22f to Color(0xFFFEF9C3),
                0.40f to Color(0xFFEDF0F4),
                1.00f to Color(0xFFFFFFFF)
            )
            ENERGY_GREEN -> Brush.verticalGradient(
                0.00f to Color(0xFF031A0B),
                0.08f to Color(0xFF052E16),
                0.16f to Color(0xFF343434),
                0.22f to Color(0xFFDAEFE6),
                0.40f to Color(0xFFEDF0F4),
                1.00f to Color(0xFFFFFFFF)
            )
            SKY_BLUE -> Brush.verticalGradient(
                0.00f to Color(0xFF3C7E9C),
                0.08f to Color(0xFF489CC1),
                0.16f to Color(0xFF38BDF8),
                0.24f to Color(0xFFE2EAF4),
                0.45f to Color(0xFFE9EDF0),
                1.00f to Color(0xFFFFFFFF)
            )
            PINK_AI -> Brush.verticalGradient(
                0.00f to Color(0xFF180312),
                0.08f to Color(0xFF3A2630),
                0.16f to Color(0xFF6B3348),
                0.22f to Color(0xFFE9EDF0),
                0.40f to Color(0xFFEDF0F4),
                1.00f to Color(0xFFFFFFFF)
            )
        }
    }

    fun getOuterBorder(): Brush {
        return when (this) {
            NEON_LIME -> Brush.linearGradient(
                listOf(Color(0xFFA9C25A), Color(0xFF4ADE80), Color(0xFFFFBB0D), Color(0xFFA9C25A))
            )
            GAMING_RED -> Brush.linearGradient(
                listOf(Color(0xFFFF7272), Color(0xFFC24B4B), Color(0xFFFF9090), Color(0xFFFF7272))
            )
            SMART_WATCH_GREEN -> Brush.linearGradient(
                listOf(Color(0xFF21A87D), Color(0xFF178A66), Color(0xFF42CD8C), Color(0xFF21A87D))
            )
            BERRY_PURPLE -> Brush.linearGradient(
                listOf(
                    Color(0xFFFF007A),
                    Color(0xFF8B91CC),
                    Color(0xFF38BDF8),
                    Color(0xFFFBBF24),
                    Color(0xFFFF007A)
                )
            )
            GRILL_ORANGE -> Brush.linearGradient(
                listOf(Color(0xFFF97316), Color(0xFFEA580C), Color(0xFFFBBF24), Color(0xFFF97316))
            )
            MUESLI_FRESH -> Brush.linearGradient(
                listOf(Color(0xFFD99F0B), Color(0xFF8A6410), Color(0xFFFCD34D), Color(0xFFD99F0B))
            )
            SHAKE_YELLOW -> Brush.linearGradient(
                listOf(Color(0xFFFFBB0D), Color(0xFFEAB308), Color(0xFFFEF08A), Color(0xFFFFBB0D))
            )
            ENERGY_GREEN -> Brush.linearGradient(
                listOf(Color(0xFF22C55E), Color(0xFF21A87D), Color(0xFF9BD9C2), Color(0xFF22C55E))
            )
            SKY_BLUE -> Brush.linearGradient(
                listOf(Color(0xFF38BDF8), Color(0xFF489CC1), Color(0xFFF97316), Color(0xFF38BDF8))
            )
            PINK_AI -> Brush.linearGradient(
                listOf(Color(0xFFF88AB0), Color(0xFFC026D3), Color(0xFFF472B6), Color(0xFFF88AB0))
            )
        }
    }

    fun getActiveTabBrush(): Brush {
        // One palette for every pack: the neumorphic accent, never a per-theme gradient.
        return Brush.horizontalGradient(listOf(Color(0xFF7B81BE), Color(0xFF5E649E)))
    }

    fun getActiveTabTextColor(): Color = Color.White

    fun getActiveTabBorder(): Color = Color(0xFF5E649E)

    fun getAccentPillColor(): Color {
        return when (this) {
            NEON_LIME -> Color(0xFF8FAF42)
            GAMING_RED -> Color(0xFFFF7272)
            SMART_WATCH_GREEN -> Color(0xFF21A87D)
            BERRY_PURPLE -> Color(0xFFC026D3)
            GRILL_ORANGE -> Color(0xFFF97316)
            MUESLI_FRESH -> Color(0xFFD99F0B)
            SHAKE_YELLOW -> Color(0xFFEAB308)
            ENERGY_GREEN -> Color(0xFF22C55E)
            SKY_BLUE -> Color(0xFF489CC1)
            PINK_AI -> Color(0xFFF88AB0)
        }
    }

    fun getGlowColor(): Color {
        return primaryColor.copy(alpha = 0.28f)
    }

    val isCloudSky: Boolean get() = this == SKY_BLUE
    val isStarryCosmic: Boolean get() = this == BERRY_PURPLE || this == PINK_AI
}

/**
 * Singleton state manager for the active BAD GYM dashboard theme.
 */
object BadGymThemeManager {
    var activeTheme by mutableStateOf(BadGymDashboardTheme.BERRY_PURPLE)

    fun setTheme(theme: BadGymDashboardTheme) {
        activeTheme = theme
    }

    fun setThemeByCode(code: String) {
        val found = BadGymDashboardTheme.values().firstOrNull {
            it.code == code || it.name.equals(code, ignoreCase = true) || it.title.contains(code, ignoreCase = true)
        }
        if (found != null) {
            activeTheme = found
        }
    }

    fun nextTheme() {
        val themes = BadGymDashboardTheme.values()
        val nextIdx = (themes.indexOf(activeTheme) + 1) % themes.size
        activeTheme = themes[nextIdx]
    }
}

/**
 * Interactive Horizontal Theme Quick Switcher Strip
 * Allows seamless switching across all 10 verified themes.
 */
@Composable
fun BadGymThemeSelectorStrip(
    selectedTheme: BadGymDashboardTheme = BadGymThemeManager.activeTheme,
    onThemeSelect: (BadGymDashboardTheme) -> Unit = { BadGymThemeManager.setTheme(it) },
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp),
        color = Color(0xF7FFFFFF),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0x140F172A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 6.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = "Themes",
                tint = Color(0xFFA1AEBF),
                modifier = Modifier.size(16.dp)
            )

            BadGymDashboardTheme.values().forEach { theme ->
                val isSelected = theme == selectedTheme
                val bg = if (isSelected) theme.primaryColor else Color(0x0A0F172A)
                val textColor = if (isSelected) {
                    if (theme == BadGymDashboardTheme.GAMING_RED ||
                        theme == BadGymDashboardTheme.SKY_BLUE ||
                        theme == BadGymDashboardTheme.PINK_AI
                    ) Color.White else Color(0xFF343434)
                } else Color(0xFF6E7887)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(bg)
                        .border(
                            1.dp,
                            if (isSelected) theme.primaryColor else Color(0x140F172A),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onThemeSelect(theme) }
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(theme.primaryColor)
                        )
                        Text(
                            text = "${theme.code} ${theme.title}",
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

