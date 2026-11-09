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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
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


/**
 * Neumorphic design system reconstructed from the three supplied style guides.
 *
 * Surfaces sit on the same value as the canvas and are separated only by two
 * opposing shadows: a white highlight from the top-left and a cool #A6B4C8
 * shade toward the bottom-right. Accents are used as fills, never as washes,
 * so no card changes colour when a tab is selected.
 */
object NeuPalette {
    // Canvas + surfaces (style guide 1: E9EDF0 / E6E9EF)
    val canvas = Color(0xFFE6EAEE)
    val canvasDeep = Color(0xFFE0E5EA)
    val surface = Color(0xFFEDF0F4)
    val surfaceRaised = Color(0xFFF5F7FA)
    val surfaceSunken = Color(0xFFDCE2E8)
    val shadow = Color(0xFFA6B4C8)
    val shadowSoft = Color(0xFFD3DBE6)
    val highlight = Color(0xFFFFFFFF)

    // Ink (style guide 2: 343434 / A1AEBF / 8C94A0)
    val ink = Color(0xFF343434)
    val inkSoft = Color(0xFF6E7887)
    val muted = Color(0xFF8C94A0)
    val faint = Color(0xFFA1AEBF)

    // Accents (style guides 1-3, deduplicated)
    val violet = Color(0xFF7B81BE)
    val violetDeep = Color(0xFF5E649E)
    val blue = Color(0xFF6B92E5)
    val sky = Color(0xFF489CC1)
    val green = Color(0xFF21A87D)
    val greenBright = Color(0xFF42CD8C)
    val greenDeep = Color(0xFF178A66)
    val coral = Color(0xFFFF7272)
    val coralDeep = Color(0xFFE05C5C)
    val amber = Color(0xFFFFBB0D)
    val amberDeep = Color(0xFFD99F0B)
    val pink = Color(0xFFF88AB0)
}

/**
 * Extruded neumorphic surface. Draws the dark shade first, the white highlight
 * second, then fills flat so the interior matches the canvas exactly.
 */
fun Modifier.neuRaised(
    radius: Dp = 24.dp,
    color: Color = NeuPalette.surface,
    depth: Dp = 6.dp,
    blur: Dp = 14.dp
): Modifier = this
    .drawBehind {
        val r = radius.toPx().coerceAtMost(minOf(size.width, size.height) / 2f)
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint()
            paint.isAntiAlias = true
            paint.color = color.toArgb()
            paint.setShadowLayer(blur.toPx(), depth.toPx(), depth.toPx(), NeuPalette.shadow.toArgb())
            canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, r, r, paint)
            paint.setShadowLayer(blur.toPx(), -depth.toPx(), -depth.toPx(), Color.White.copy(alpha = 0.62f).toArgb())
            canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, r, r, paint)
        }
    }
    .clip(RoundedCornerShape(radius))
    .background(color)

/** Pressed / sunken well: inner shade at the top edge, inner glow at the bottom. */
fun Modifier.neuSunken(
    radius: Dp = 18.dp,
    color: Color = NeuPalette.surfaceSunken
): Modifier = this
    .clip(RoundedCornerShape(radius))
    .background(color)
    .drawWithContent {
        drawContent()
        val edge = 14.dp.toPx()
        drawRect(
            brush = Brush.verticalGradient(
                listOf(NeuPalette.shadow.copy(alpha = 0.42f), Color.Transparent),
                startY = 0f,
                endY = edge
            )
        )
    }

/** Accent-filled neumorphic pill: solid colour with a soft self-coloured glow. */
fun Modifier.neuAccent(
    color: Color,
    radius: Dp = 20.dp,
    depth: Dp = 5.dp,
    blur: Dp = 12.dp
): Modifier = this
    .drawBehind {
        val r = radius.toPx().coerceAtMost(minOf(size.width, size.height) / 2f)
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint()
            paint.isAntiAlias = true
            paint.color = color.toArgb()
            paint.setShadowLayer(blur.toPx(), depth.toPx(), depth.toPx(), color.copy(alpha = 0.45f).toArgb())
            canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, r, r, paint)
        }
    }
    .clip(RoundedCornerShape(radius))
    .background(Brush.verticalGradient(listOf(color, darken(color, 0.9f))))

/** The single token set the whole dashboard reads from. Pack themes no longer recolour it. */
fun neoIntelligenceColors(): IntelligenceColors = IntelligenceColors(
    accent = NeuPalette.violet,
    accentDeep = NeuPalette.violetDeep,
    accentWash = NeuPalette.violet.copy(alpha = 0.10f),
    accentSoft = NeuPalette.violet.copy(alpha = 0.22f),
    onAccent = Color.White,
    ink = NeuPalette.ink,
    inkSoft = NeuPalette.inkSoft,
    muted = NeuPalette.muted,
    faint = NeuPalette.faint,
    cardFill = NeuPalette.surface,
    cardEdge = NeuPalette.shadowSoft,
    good = NeuPalette.green,
    goodDeep = NeuPalette.greenDeep,
    warn = NeuPalette.amber,
    warnDeep = NeuPalette.amberDeep,
    bad = NeuPalette.coral,
    info = NeuPalette.sky,
    screenBrush = Brush.linearGradient(listOf(NeuPalette.canvas, NeuPalette.canvas)),
    activeTabBrush = Brush.horizontalGradient(listOf(NeuPalette.violet, NeuPalette.violetDeep)),
    onActiveTab = Color.White
)

@Composable
fun intelligenceColors(): IntelligenceColors = remember { neoIntelligenceColors() }

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
            .neuRaised(radius = 22.dp, depth = 6.dp, blur = 14.dp)
            .padding(horizontal = 11.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(c.accent),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = c.onAccent, modifier = Modifier.size(14.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = c.ink, letterSpacing = (-0.2).sp)
                if (subtitle != null) {
                    Text(subtitle, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = c.accentDeep)
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
            .neuRaised(radius = 50.dp, color = NeuPalette.surfaceRaised, depth = 2.dp, blur = 5.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        if (leading != null) {
            Icon(leading, contentDescription = null, tint = c.accent, modifier = Modifier.size(11.dp))
        }
        Text(text, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = c.ink, maxLines = 1)
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
    color: Color = Color(0xFFFFBB0D)
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
