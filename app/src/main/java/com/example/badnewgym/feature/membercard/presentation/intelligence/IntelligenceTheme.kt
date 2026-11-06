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
    val brightAccent = primaryColor.luminance() > 0.5f
    var inkTone = darken(secondaryColor, 0.62f)
    if (inkTone.luminance() > 0.28f) inkTone = Color(0xFF14532D)
    return IntelligenceColors(
        accent = primaryColor,
        accentDeep = secondaryColor,
        accentWash = primaryColor.copy(alpha = 0.10f),
        accentSoft = primaryColor.copy(alpha = 0.22f),
        onAccent = if (brightAccent) darken(primaryColor, 0.45f) else Color.White,
        ink = inkTone,
        inkSoft = darken(secondaryColor, 0.85f),
        muted = Color(0xFF6B7F76),
        faint = Color(0xFF94A3B8),
        cardFill = Color.White,
        cardEdge = primaryColor.copy(alpha = 0.16f),
        good = Color(0xFF16A34A),
        goodDeep = Color(0xFF15803D),
        warn = Color(0xFFF59E0B),
        warnDeep = Color(0xFFB45309),
        bad = Color(0xFFDC2626),
        info = Color(0xFF0EA5E9),
        screenBrush = Brush.verticalGradient(
            listOf(
                Color.White,
                primaryColor.copy(alpha = 0.05f),
                primaryColor.copy(alpha = 0.12f)
            )
        ),
        activeTabBrush = Brush.verticalGradient(listOf(primaryColor, secondaryColor)),
        onActiveTab = if (brightAccent) darken(primaryColor, 0.45f) else Color.White
    )
}

@Composable
fun intelligenceColors(): IntelligenceColors {
    val theme = BadGymThemeManager.activeTheme
    return remember(theme) { theme.intelligenceColors() }
}

/** Draws the decorative leaf-swoosh curves used in the light header corners. */
fun DrawScope.drawIntelligenceSwooshes(accent: Color) {
    val swoosh = listOf(accent.copy(alpha = 0.30f), accent.copy(alpha = 0.14f))
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
    drawPath(rightSwoosh, accent.copy(alpha = 0.20f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
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
            .clip(RoundedCornerShape(18.dp))
            .background(c.cardFill)
            .border(1.2.dp, c.cardEdge, RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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

/** Icon + label filter chip; selected chips fill with the solid accent. */
@Composable
fun IntelligenceChip(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) c.accent else c.cardFill)
            .border(1.2.dp, if (selected) c.accent else c.cardEdge, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = if (selected) c.onAccent else c.accent, modifier = Modifier.size(12.dp))
        Text(
            label,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) c.onAccent else c.ink,
            letterSpacing = (-0.2).sp,
            maxLines = 1
        )
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
