package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.BadGymDashboardTheme
import com.example.badnewgym.feature.membercard.design.BadGymThemeManager

/**
 * Light-canvas visual tokens for the Member Intelligence dashboard, derived
 * from the active [BadGymDashboardTheme]. Mirrors the approved pixel-perfect
 * reference: white canvas, theme-hued dark ink, solid accent fills, gold crowns.
 */
data class IntelligenceColors(
    val accent: Color,
    val accentDeep: Color,
    val accentWash: Color,
    val accentSoft: Color,
    val onAccent: Color,
    val ink: Color,
    val inkSoft: Color,
    val muted: Color,
    val faint: Color,
    val cardFill: Color,
    val cardEdge: Color,
    val good: Color,
    val goodDeep: Color,
    val warn: Color,
    val warnDeep: Color,
    val bad: Color,
    val info: Color,
    val screenBrush: Brush,
    val activeTabBrush: Brush,
    val onActiveTab: Color
)

/** Returns a darkened copy of [color] by multiplying each channel by [factor]. */
fun darken(color: Color, factor: Float): Color = Color(
    red = (color.red * factor).coerceIn(0f, 1f),
    green = (color.green * factor).coerceIn(0f, 1f),
    blue = (color.blue * factor).coerceIn(0f, 1f),
    alpha = color.alpha
)

/** Maps a pack theme onto the light-canvas token set. */
fun BadGymDashboardTheme.intelligenceColors(): IntelligenceColors {
    return when (this) {
        BadGymDashboardTheme.GAMING_RED -> {
            val main = Color(0xFFEF4444)
            val deep = Color(0xFFB91C1C)
            IntelligenceColors(
                accent = main,
                accentDeep = deep,
                accentWash = main.copy(alpha = 0.08f),
                accentSoft = main.copy(alpha = 0.18f),
                onAccent = Color.White,
                ink = Color(0xFF1F0707),
                inkSoft = Color(0xFF7F1D1D),
                muted = Color(0xFF78716C),
                faint = Color(0xFFA8A29E),
                cardFill = Color.White,
                cardEdge = main.copy(alpha = 0.18f),
                good = Color(0xFF10B981),
                goodDeep = Color(0xFF047857),
                warn = Color(0xFFF59E0B),
                warnDeep = Color(0xFFD97706),
                bad = Color(0xFFDC2626),
                info = Color(0xFF0284C7),
                screenBrush = Brush.verticalGradient(
                    0f to Color(0xFFFFF7F7),
                    0.18f to Color(0xFFFEE2E2).copy(alpha = 0.5f),
                    0.60f to Color(0xFFFFF7F7),
                    1f to Color(0xFFFECACA).copy(alpha = 0.6f)
                ),
                activeTabBrush = Brush.horizontalGradient(listOf(main, deep)),
                onActiveTab = Color.White
            )
        }
        BadGymDashboardTheme.SKY_BLUE -> {
            val main = Color(0xFF0284C7)
            val deep = Color(0xFF0369A1)
            IntelligenceColors(
                accent = main,
                accentDeep = deep,
                accentWash = main.copy(alpha = 0.08f),
                accentSoft = main.copy(alpha = 0.18f),
                onAccent = Color.White,
                ink = Color(0xFF082F49),
                inkSoft = Color(0xFF0C4A6E),
                muted = Color(0xFF64748B),
                faint = Color(0xFF94A3B8),
                cardFill = Color.White,
                cardEdge = main.copy(alpha = 0.18f),
                good = Color(0xFF10B981),
                goodDeep = Color(0xFF047857),
                warn = Color(0xFFF59E0B),
                warnDeep = Color(0xFFD97706),
                bad = Color(0xFFEF4444),
                info = Color(0xFF0EA5E9),
                screenBrush = Brush.verticalGradient(
                    0f to Color(0xFFF0F9FF),
                    0.18f to Color(0xFFE0F2FE),
                    0.60f to Color(0xFFF0F9FF),
                    1f to Color(0xFFBAE6FD).copy(alpha = 0.5f)
                ),
                activeTabBrush = Brush.horizontalGradient(listOf(main, deep)),
                onActiveTab = Color.White
            )
        }
        BadGymDashboardTheme.SMART_WATCH_GREEN -> {
            val main = Color(0xFF10B981)
            val deep = Color(0xFF047857)
            IntelligenceColors(
                accent = main,
                accentDeep = deep,
                accentWash = main.copy(alpha = 0.08f),
                accentSoft = main.copy(alpha = 0.18f),
                onAccent = Color.White,
                ink = Color(0xFF022C22),
                inkSoft = Color(0xFF064E3B),
                muted = Color(0xFF6B7280),
                faint = Color(0xFF9CA3AF),
                cardFill = Color.White,
                cardEdge = main.copy(alpha = 0.18f),
                good = Color(0xFF10B981),
                goodDeep = Color(0xFF047857),
                warn = Color(0xFFF59E0B),
                warnDeep = Color(0xFFD97706),
                bad = Color(0xFFEF4444),
                info = Color(0xFF06B6D4),
                screenBrush = Brush.verticalGradient(
                    0f to Color(0xFFF0FDF4),
                    0.18f to Color(0xFFDCFCE7),
                    0.60f to Color(0xFFF0FDF4),
                    1f to Color(0xFFBBF7D0).copy(alpha = 0.5f)
                ),
                activeTabBrush = Brush.horizontalGradient(listOf(main, deep)),
                onActiveTab = Color.White
            )
        }
        BadGymDashboardTheme.NEON_LIME -> {
            val main = Color(0xFF84CC16)
            val deep = Color(0xFF4D7C0F)
            IntelligenceColors(
                accent = main,
                accentDeep = deep,
                accentWash = main.copy(alpha = 0.10f),
                accentSoft = main.copy(alpha = 0.22f),
                onAccent = Color(0xFF0F172A),
                ink = Color(0xFF14532D),
                inkSoft = Color(0xFF166534),
                muted = Color(0xFF4B5563),
                faint = Color(0xFF9CA3AF),
                cardFill = Color.White,
                cardEdge = main.copy(alpha = 0.22f),
                good = Color(0xFF10B981),
                goodDeep = Color(0xFF047857),
                warn = Color(0xFFF59E0B),
                warnDeep = Color(0xFFD97706),
                bad = Color(0xFFEF4444),
                info = Color(0xFF06B6D4),
                screenBrush = Brush.verticalGradient(
                    0f to Color(0xFFF7FEE7),
                    0.18f to Color(0xFFECFCCB),
                    0.60f to Color(0xFFF7FEE7),
                    1f to Color(0xFFD9F99D).copy(alpha = 0.5f)
                ),
                activeTabBrush = Brush.horizontalGradient(listOf(Color(0xFFBEF264), Color(0xFFA3E635))),
                onActiveTab = Color(0xFF0F172A)
            )
        }
        BadGymDashboardTheme.GRILL_ORANGE, BadGymDashboardTheme.MUESLI_FRESH, BadGymDashboardTheme.SHAKE_YELLOW -> {
            val main = primaryColor
            val deep = secondaryColor
            IntelligenceColors(
                accent = main,
                accentDeep = deep,
                accentWash = main.copy(alpha = 0.08f),
                accentSoft = main.copy(alpha = 0.18f),
                onAccent = Color.White,
                ink = Color(0xFF1C1917),
                inkSoft = deep,
                muted = Color(0xFF78716C),
                faint = Color(0xFFA8A29E),
                cardFill = Color.White,
                cardEdge = main.copy(alpha = 0.18f),
                good = Color(0xFF10B981),
                goodDeep = Color(0xFF047857),
                warn = Color(0xFFF59E0B),
                warnDeep = Color(0xFFD97706),
                bad = Color(0xFFEF4444),
                info = Color(0xFF06B6D4),
                screenBrush = Brush.verticalGradient(
                    0f to Color(0xFFFFFBEB),
                    0.18f to Color(0xFFFEF3C7).copy(alpha = 0.5f),
                    0.60f to Color(0xFFFFFBEB),
                    1f to Color(0xFFFDE68A).copy(alpha = 0.4f)
                ),
                activeTabBrush = Brush.horizontalGradient(listOf(main, deep)),
                onActiveTab = Color.White
            )
        }
        BadGymDashboardTheme.PINK_AI -> {
            val main = Color(0xFFEC4899)
            val deep = Color(0xFFBE185D)
            IntelligenceColors(
                accent = main,
                accentDeep = deep,
                accentWash = main.copy(alpha = 0.08f),
                accentSoft = main.copy(alpha = 0.18f),
                onAccent = Color.White,
                ink = Color(0xFF500724),
                inkSoft = Color(0xFF831843),
                muted = Color(0xFF701A75).copy(alpha = 0.6f),
                faint = Color(0xFF9CA3AF),
                cardFill = Color.White,
                cardEdge = main.copy(alpha = 0.18f),
                good = Color(0xFF10B981),
                goodDeep = Color(0xFF047857),
                warn = Color(0xFFF59E0B),
                warnDeep = Color(0xFFD97706),
                bad = Color(0xFFEF4444),
                info = Color(0xFF06B6D4),
                screenBrush = Brush.verticalGradient(
                    0f to Color(0xFFFDF2F8),
                    0.18f to Color(0xFFFCE7F3),
                    0.60f to Color(0xFFFDF2F8),
                    1f to Color(0xFFFBCFE8).copy(alpha = 0.5f)
                ),
                activeTabBrush = Brush.horizontalGradient(listOf(main, deep)),
                onActiveTab = Color.White
            )
        }
        else -> { // BERRY_PURPLE & Default: Neo-Glass Lavender Luxe
            val inkTone = Color(0xFF1E1B4B)
            IntelligenceColors(
                accent = Color(0xFF8B5CF6),
                accentDeep = Color(0xFF6D28D9),
                accentWash = Color(0xFF8B5CF6).copy(alpha = 0.08f),
                accentSoft = Color(0xFF8B5CF6).copy(alpha = 0.18f),
                onAccent = Color.White,
                ink = inkTone,
                inkSoft = Color(0xFF4C1D95),
                muted = Color(0xFF6B7280),
                faint = Color(0xFF9CA3AF),
                cardFill = Color.White,
                cardEdge = Color(0xFF8B5CF6).copy(alpha = 0.18f),
                good = Color(0xFF10B981),
                goodDeep = Color(0xFF047857),
                warn = Color(0xFFF59E0B),
                warnDeep = Color(0xFFD97706),
                bad = Color(0xFFEF4444),
                info = Color(0xFF06B6D4),
                screenBrush = Brush.verticalGradient(
                    0f to Color(0xFFFAF8FF),
                    0.18f to Color(0xFFF5F3FF),
                    0.60f to Color(0xFFFAF8FF),
                    1f to Color(0xFFEDE9FE)
                ),
                activeTabBrush = Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))),
                onActiveTab = Color.White
            )
        }
    }
}

@Composable
fun intelligenceColors(): IntelligenceColors {
    val theme = BadGymThemeManager.activeTheme
    return remember(theme) { theme.intelligenceColors() }
}

/** Draws the decorative leaf-swoosh curves used in the light header corners. */
fun DrawScope.drawIntelligenceSwooshes(accent: Color) {
    val swoosh = listOf(accent.copy(alpha = 0.14f), accent.copy(alpha = 0.07f))
    val w = size.width
    val h = size.height
    swoosh.forEachIndexed { index, color ->
        val lift = index * h * 0.22f
        val path = Path().apply {
            moveTo(-w * 0.05f, h * 0.10f + lift)
            quadraticBezierTo(
                w * 0.38f, -h * 0.28f + lift,
                w * 0.96f, h * 0.34f + lift
            )
        }
        drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = (5f - index * 1.6f)))
    }
    val rightSwoosh = Path().apply {
        moveTo(w * 1.04f, h * 0.06f)
        quadraticBezierTo(w * 0.68f, -h * 0.30f, w * 0.42f, h * 0.30f)
    }
    drawPath(rightSwoosh, accent.copy(alpha = 0.10f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
}

/** Two soft radial blooms pinned to the canvas corners for a glass/neumorphic ambient wash. */
fun DrawScope.drawAmbientBlooms(accent: Color, secondary: Color) {
    val w = size.width
    val h = size.height
    val topRight = Offset(w * 0.88f, h * 0.10f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(accent.copy(alpha = 0.12f), Color.Transparent),
            center = topRight,
            radius = w * 0.62f
        ),
        radius = w * 0.62f,
        center = topRight
    )
    val midLeft = Offset(w * 0.06f, h * 0.52f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(secondary.copy(alpha = 0.08f), Color.Transparent),
            center = midLeft,
            radius = w * 0.72f
        ),
        radius = w * 0.72f,
        center = midLeft
    )
}

/** Shared white section card: solid accent icon tile, dark title, trailing slot. */
@Composable
fun IntelligenceSectionCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val c = intelligenceColors()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = c.accent.copy(alpha = 0.28f),
                spotColor = c.accent.copy(alpha = 0.34f)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.96f),
                        c.accentWash.copy(alpha = 0.35f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.9f), c.accentSoft)
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(c.accent),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = c.onAccent, modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = c.ink, letterSpacing = (-0.2).sp)
                if (subtitle != null) {
                    Text(subtitle, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = c.accentDeep)
                }
            }
            trailing?.invoke(this)
        }
        content()
    }
}

/** Outlined action/filter pill: white fill, accent border, dark accent text. */
@Composable
fun IntelligencePillOutlined(
    text: String,
    modifier: Modifier = Modifier,
    leading: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    val c = intelligenceColors()
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(c.cardFill)
            .border(1.2.dp, c.accentSoft, RoundedCornerShape(50))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (leading != null) {
            Icon(leading, contentDescription = null, tint = c.accent, modifier = Modifier.size(12.dp))
        }
        Text(text, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = c.ink, maxLines = 1)
    }
}

/** Icon + label filter chip with an optional live value line; selected chips fill with the solid accent. */
@Composable
fun IntelligenceChip(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    value: String? = null
) {
    val c = intelligenceColors()
    val onFill = Color(0xFF3B2A00)
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) c.warn else c.cardFill)
            .border(1.2.dp, if (selected) c.warn else c.cardEdge, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, tint = if (selected) onFill else c.accent, modifier = Modifier.size(12.dp))
            Text(
                label,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) onFill else c.ink,
                letterSpacing = (-0.2).sp,
                maxLines = 1
            )
        }
        if (value != null) {
            Text(
                value,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (selected) onFill else c.accentDeep,
                maxLines = 1
            )
        }
    }
}

/** Vertically stacked legend row: colored dot, label, right-aligned count. */
@Composable
fun IntelligenceLegendRow(color: Color, label: String, count: String, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(
            label,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = c.ink,
            modifier = Modifier.padding(start = 7.dp)
        )
        Text(
            count,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = c.ink,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

/** Light accent-wash info note with dark accent text (reference note bars). */
@Composable
fun IntelligenceNote(text: String, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(c.accentWash)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(Icons.Filled.Info, contentDescription = null, tint = c.accent, modifier = Modifier.size(12.dp))
        Text(text, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = c.ink, lineHeight = 12.sp)
    }
}

/**
 * Small vector crown used to mark a chart peak. Drawn as a path rather than an
 * emoji glyph so it renders identically on every device and font stack.
 */
fun DrawScope.drawCrown(
    centerX: Float,
    baseY: Float,
    width: Float = 11.dp.toPx(),
    height: Float = 8.dp.toPx(),
    color: Color = Color(0xFFF2A81D)
) {
    val path = Path().apply {
        moveTo(centerX - width / 2f, baseY)
        lineTo(centerX - width / 2f, baseY - height * 0.85f)
        lineTo(centerX - width / 4f, baseY - height * 0.35f)
        lineTo(centerX, baseY - height)
        lineTo(centerX + width / 4f, baseY - height * 0.35f)
        lineTo(centerX + width / 2f, baseY - height * 0.85f)
        lineTo(centerX + width / 2f, baseY)
        close()
    }
    drawPath(path, color)
}
