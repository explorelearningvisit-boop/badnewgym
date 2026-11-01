package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =============================================================================
// BAD GYM — GLASS SHEET CHROME
// One shared shell for every menu / dialog surface so the frosted-pane language
// that the dashboard cards use continues seamlessly into the overlays.
// =============================================================================

/** Soft ice-mint canvas the overlays float above. */
val GlassSheetCanvas = Brush.verticalGradient(
    0.00f to Color(0xFFF2FBF8),
    0.30f to Color(0xFFF5FBFF),
    0.66f to Color(0xFFF1FAF5),
    1.00f to Color(0xFFEAF6F1)
)

/** Frosted pane fill: lit at the crown, cooling toward the base. */
val GlassPaneFill = Brush.verticalGradient(
    0.00f to Color(0xFFFFFFFF),
    0.52f to Color(0xFFFCFEFF),
    1.00f to Color(0xFFF4FAF8)
)

/** White-lit edge that makes a frosted pane read as glass rather than paint. */
val GlassPaneEdge = BorderStroke(1.dp, Color.White)

val GlassShape: Shape = RoundedCornerShape(20.dp)

val GlassInk = Color(0xFF081A15)
val GlassInkSoft = Color(0xFF2C4741)
val GlassMuted = Color(0xFF52706A)
val GlassFaint = Color(0xFF7A938D)
val GlassHairline = Color(0xFFDCEBE6)

/**
 * Wraps a modal sheet body in the mint canvas + drag handle so the container can
 * stay transparent and the gradient shows through behind rounded corners.
 */
@Composable
fun GlassSheetSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(GlassSheetCanvas)
    ) {
        // Ambient blooms give the pane a light source to refract.
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(220.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0x3322C3E6), Color(0x00000000))
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(240.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0x2E10B981), Color(0x00000000))
                    )
                )
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            SheetGrip()
            content()
        }
    }
}

/** Capsule grip with a lit top edge. */
@Composable
fun SheetGrip(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(46.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFC6DED6))))
                .border(0.8.dp, Color(0xFFB7D3CA), RoundedCornerShape(50.dp))
        )
    }
}

/**
 * Standard sheet masthead: title, optional subtitle and badge, neumorphic close.
 */
@Composable
fun GlassSheetHeader(
    title: String,
    subtitle: String? = null,
    badge: String? = null,
    badgeTint: Color = InfographicTokens.RedAlert,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = GlassInk,
                    letterSpacing = (-0.4).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (badge != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Brush.verticalGradient(listOf(badgeTint, badgeTint.copy(alpha = 0.82f))))
                            .border(0.8.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(50.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.3.sp,
                            maxLines = 1
                        )
                    }
                }
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.5.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = GlassMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        GlassCloseButton(onClick = onClose)
    }
}

/** Raised circular dismiss control — neumorphic dish with a hairline rim. */
@Composable
fun GlassCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = CircleShape
    Box(
        modifier = modifier
            .size(36.dp)
            .shadow(5.dp, shape, ambientColor = Color(0xFF9DB2C8), spotColor = Color(0xFFB9C7D6))
            .clip(shape)
            .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFF1F7F5))))
            .border(1.dp, Color.White, shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = GlassInkSoft,
            modifier = Modifier.size(17.dp)
        )
    }
}

/**
 * Frosted action tile: gradient pane, white rim, coloured ambient shadow and a
 * press-scale response so taps feel physical.
 */
@Composable
fun GlassActionTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = InfographicTokens.Brand,
    chevron: Boolean = true
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.972f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "glassTileScale"
    )

    Surface(
        shape = GlassShape,
        color = Color.Transparent,
        border = GlassPaneEdge,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (pressed) 2.dp else 7.dp,
                shape = GlassShape,
                ambientColor = Color(0xFF9DB2C8),
                spotColor = iconTint.copy(alpha = 0.35f)
            )
            .clip(GlassShape)
            .background(GlassPaneFill)
            .clickable(interactionSource = interaction, indication = null) { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .shadow(4.dp, RoundedCornerShape(14.dp), ambientColor = iconTint.copy(alpha = 0.5f), spotColor = iconTint.copy(alpha = 0.55f))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.verticalGradient(listOf(iconTint.copy(alpha = 0.95f), iconTint.copy(alpha = 0.78f))))
                    .border(0.9.dp, Color.White.copy(alpha = 0.55f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(21.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlassInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 12.5.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = GlassMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (chevron) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F7F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\u203A",
                        fontSize = 17.sp,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = GlassMuted
                    )
                }
            }
        }
    }
}

/** Pill chip with an animated selected state. */
@Composable
fun GlassChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = InfographicTokens.Brand
) {
    val shape = RoundedCornerShape(50.dp)
    val fill by androidx.compose.animation.animateColorAsState(
        targetValue = if (selected) accent else Color.White,
        animationSpec = tween(200, easing = FastOutSlowInEasing),
        label = "glassChipFill"
    )
    val ink by androidx.compose.animation.animateColorAsState(
        targetValue = if (selected) Color.White else GlassInkSoft,
        animationSpec = tween(200, easing = FastOutSlowInEasing),
        label = "glassChipInk"
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(fill)
            .border(
                width = 1.dp,
                color = if (selected) Color.White.copy(alpha = 0.5f) else GlassHairline,
                shape = shape
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.5.sp,
            lineHeight = 16.sp,
            fontWeight = if (selected) FontWeight.Black else FontWeight.Bold,
            color = ink,
            maxLines = 1
        )
    }
}

/** Small section caption used between tiles. */
@Composable
fun GlassSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Black,
        color = GlassFaint,
        letterSpacing = 0.9.sp,
        modifier = modifier
    )
}

/** Bottom padding that clears the system navigation bar on the last child. */
@Composable
fun GlassSheetFooterPad(height: Int = 18) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .navigationBarsPadding()
    )
}

// =============================================================================
// NEUMORPHIC DEPTH — one modifier so every surface lifts off the mint canvas
// =============================================================================

/**
 * Coloured ambient + spot shadow tuned for the mint canvas. A neutral slate
 * ambient keeps the lift soft, while [tint] bleeds the section hue into the
 * contact shadow so the card belongs to its own palette.
 */
fun Modifier.neumorphicLift(
    shape: Shape,
    tint: Color = InfographicTokens.Brand,
    elevation: androidx.compose.ui.unit.Dp = 7.dp
): Modifier = this.shadow(
    elevation = elevation,
    shape = shape,
    clip = false,
    ambientColor = Color(0xFF9DB2C8),
    spotColor = tint.copy(alpha = 0.42f)
)
