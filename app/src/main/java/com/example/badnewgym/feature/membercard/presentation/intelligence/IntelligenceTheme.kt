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
 * Live palette. Every token resolves through [NeuStyle.theme], so switching a
 * theme re-skins the whole dashboard without touching a single screen.
 */
object NeuPalette {
    private val t: NeuTheme get() = NeuStyle.theme

    val canvas get() = t.canvas
    val canvasDeep get() = t.canvasDeep
    val surface get() = t.surface
    val surfaceRaised get() = t.surfaceRaised
    val surfaceSunken get() = t.surfaceSunken
    val shadow get() = t.shadow
    val shadowSoft get() = t.shadowSoft
    val highlight get() = t.highlight
    val highlightAlpha get() = t.highlightAlpha

    val ink get() = t.ink
    val inkSoft get() = t.inkSoft
    val muted get() = t.muted
    val faint get() = t.faint

    val violet get() = t.violet
    val violetDeep get() = t.violetDeep
    val violetWash get() = t.violetWash
    val green get() = t.green
    val greenDeep get() = t.greenDeep
    val greenBright get() = t.greenBright
    val greenWash get() = t.greenWash
    val coral get() = t.coral
    val coralDeep get() = t.coralDeep
    val coralWash get() = t.coralWash
    val amber get() = t.amber
    val amberDeep get() = t.amberDeep
    val amberWash get() = t.amberWash
    val blue get() = t.blue
    val sky get() = t.sky
    val pink get() = t.pink
    val onAccent get() = t.onAccent
    val isDark get() = t.dark
}

/**
 * Extruded neumorphic surface. Draws the dark shade first, the highlight
 * second, then fills flat. Radius and depth follow the appearance sliders.
 */
fun Modifier.neuRaised(
    radius: Dp = 24.dp,
    color: Color = NeuPalette.surface,
    depth: Dp = 6.dp,
    blur: Dp = 14.dp
): Modifier {
    val r = radius * NeuStyle.radiusScale
    val d = depth * NeuStyle.depthScale
    val b = blur * NeuStyle.depthScale
    val shade = NeuPalette.shadow
    val glint = NeuPalette.highlight.copy(alpha = NeuPalette.highlightAlpha)
    return this
        .drawBehind {
            val radiusPx = r.toPx().coerceAtMost(minOf(size.width, size.height) / 2f)
            drawIntoCanvas { canvas ->
                val paint = android.graphics.Paint()
                paint.isAntiAlias = true
                paint.color = color.toArgb()
                if (b > 0.dp) {
                    paint.setShadowLayer(b.toPx(), d.toPx(), d.toPx(), shade.toArgb())
                    canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radiusPx, radiusPx, paint)
                    paint.setShadowLayer(b.toPx(), -d.toPx(), -d.toPx(), glint.toArgb())
                    canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radiusPx, radiusPx, paint)
                } else {
                    paint.clearShadowLayer()
                    canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radiusPx, radiusPx, paint)
                }
            }
        }
        .clip(RoundedCornerShape(r))
        .background(color)
}

/** Pressed / sunken well. */
fun Modifier.neuSunken(
    radius: Dp = 18.dp,
    color: Color = NeuPalette.surfaceSunken
): Modifier {
    val r = radius * NeuStyle.radiusScale
    val shade = NeuPalette.shadow
    val strength = 0.42f * NeuStyle.depthScale.coerceAtMost(1.4f)
    return this
        .clip(RoundedCornerShape(r))
        .background(color)
        .drawWithContent {
            drawContent()
            if (strength > 0f) {
                val edge = 14.dp.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        listOf(shade.copy(alpha = strength), Color.Transparent),
                        startY = 0f,
                        endY = edge
                    )
                )
            }
        }
}

/** Accent-filled neumorphic pill. */
fun Modifier.neuAccent(
    color: Color,
    radius: Dp = 20.dp,
    depth: Dp = 5.dp,
    blur: Dp = 12.dp
): Modifier {
    val r = radius * NeuStyle.radiusScale
    val d = depth * NeuStyle.depthScale
    val b = blur * NeuStyle.depthScale
    return this
        .drawBehind {
            val radiusPx = r.toPx().coerceAtMost(minOf(size.width, size.height) / 2f)
            drawIntoCanvas { canvas ->
                val paint = android.graphics.Paint()
                paint.isAntiAlias = true
                paint.color = color.toArgb()
                if (b > 0.dp) {
                    paint.setShadowLayer(b.toPx(), d.toPx(), d.toPx(), color.copy(alpha = 0.45f).toArgb())
                } else {
                    paint.clearShadowLayer()
                }
                canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radiusPx, radiusPx, paint)
            }
        }
        .clip(RoundedCornerShape(r))
        .background(Brush.verticalGradient(listOf(color, darken(color, 0.9f))))
}

/**
 * Tactile claymorphic surface with gentle diffuse blue-tinted drop shadow
 * and subtle clean hairline border.
 */
fun Modifier.clayCard(
    radius: Dp = 18.dp,
    elevation: Dp = 4.dp,
    border: Color = Color(0xFFF1F5F9),
    backgroundColor: Color = Color.White
): Modifier = this
    .shadow(
        elevation = elevation,
        shape = RoundedCornerShape(radius),
        spotColor = Color(0x181E3A8A),
        ambientColor = Color(0x0C1E3A8A)
    )
    .border(1.dp, border, RoundedCornerShape(radius))
    .clip(RoundedCornerShape(radius))
    .background(backgroundColor)

/** The single token set the whole dashboard reads from. */
fun neoIntelligenceColors(): IntelligenceColors = IntelligenceColors(
    accent = NeuPalette.violet,
    accentDeep = NeuPalette.violetDeep,
    accentWash = NeuPalette.violet.copy(alpha = 0.10f),
    accentSoft = NeuPalette.violet.copy(alpha = 0.22f),
    onAccent = NeuPalette.onAccent,
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
    screenBrush = Brush.verticalGradient(listOf(NeuPalette.canvas, NeuPalette.canvasDeep)),
    activeTabBrush = Brush.verticalGradient(listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))),
    onActiveTab = NeuPalette.onAccent
)

@Composable
fun intelligenceColors(): IntelligenceColors = remember(NeuStyle.themeId) { neoIntelligenceColors() }

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
            .neuRaised(radius = IntelligenceDensity.radiusCard, depth = 6.dp, blur = 14.dp)
            .padding(horizontal = IntelligenceDensity.cardPaddingH, vertical = IntelligenceDensity.cardPaddingV),
        verticalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGapSm)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGapSm)) {
            Box(
                modifier = Modifier
                    .size(IntelligenceDensity.iconMedium)
                    .clip(RoundedCornerShape(IntelligenceDensity.radiusSmall))
                    .background(c.accent),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = c.onAccent, modifier = Modifier.size(IntelligenceDensity.iconSmall))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = IntelligenceTypography.sizeCardTitle, fontWeight = IntelligenceTypography.weightTitle, color = c.ink, letterSpacing = (-0.2).sp)
                if (subtitle != null) {
                    Text(subtitle, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = c.accentDeep)
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
            .neuRaised(radius = IntelligenceDensity.radiusPill, color = NeuPalette.surfaceRaised, depth = 2.dp, blur = 5.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        if (leading != null) {
            Icon(leading, contentDescription = null, tint = c.accent, modifier = Modifier.size(IntelligenceDensity.iconMicro))
        }
        Text(text, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = c.ink, maxLines = 1)
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
            .clip(RoundedCornerShape(IntelligenceDensity.radiusChip))
            .background(if (selected) c.warn else c.cardFill)
            .border(1.2.dp, if (selected) c.warn else c.cardEdge, RoundedCornerShape(IntelligenceDensity.radiusChip))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, tint = if (selected) onFill else c.accent, modifier = Modifier.size(IntelligenceDensity.iconMicro))
            Text(
                label,
                fontSize = IntelligenceTypography.sizeSmall,
                fontWeight = IntelligenceTypography.weightBody,
                color = if (selected) onFill else c.ink,
                letterSpacing = (-0.2).sp,
                maxLines = 1
            )
        }
        if (value != null) {
            Text(
                value,
                fontSize = IntelligenceTypography.sizeSmall,
                fontWeight = IntelligenceTypography.weightValue,
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
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Text(
            label,
            fontSize = IntelligenceTypography.sizeSmall,
            fontWeight = IntelligenceTypography.weightBody,
            color = c.ink,
            modifier = Modifier.padding(start = 8.dp)
        )
        Text(
            count,
            fontSize = IntelligenceTypography.sizeSmall,
            fontWeight = IntelligenceTypography.weightValue,
            color = c.ink,
            modifier = Modifier.padding(start = 7.dp)
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
            .clip(RoundedCornerShape(IntelligenceDensity.radiusPill))
            .background(c.accentWash)
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Icon(Icons.Filled.Info, contentDescription = null, tint = c.accent, modifier = Modifier.size(IntelligenceDensity.iconSmall))
        Text(text, fontSize = IntelligenceTypography.sizeBody, fontWeight = IntelligenceTypography.weightBody, color = c.ink, lineHeight = 14.sp)
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
