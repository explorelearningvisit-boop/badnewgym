package com.example.badnewgym.feature.memberintelligence.design.tokens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * BAD GYM Member Intelligence V4 centralized design tokens.
 *
 * Single source of truth for color, spacing, radius and type.
 * UI composables must not hardcode hex values or dp literals.
 */
object MiColors {
    // Brand - luminous on midnight navy.
    val Emerald = Color(0xFF00C98A)
    val EmeraldDark = Color(0xFF5EE8B5)
    val EmeraldLight = Color(0xFF7DF0C6)
    val Mint = Color(0xFF14417E)
    val Page = Color(0xFF071A3D)
    val PageGradientTop = Color(0xFF0A2B63)

    // Text - white and icy blue on navy.
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFA9C3E8)
    val TextTertiary = Color(0xFF7F9CCB)
    val Border = Color(0xFF2B5CA6)

    // Surfaces - layered navy glass.
    val Surface = Color(0xFF10336E)
    val SurfaceSunken = Color(0xFF0A2B63)
    val SurfaceRaised = Color(0xFF16407E)

    // Semantic - bright variants that read on navy.
    val Blue = Color(0xFF4D9BFF)
    val BlueSoft = Color(0xFF14386E)
    val Amber = Color(0xFFFFC85C)
    val AmberSoft = Color(0xFF4A3413)
    val Red = Color(0xFFF04452)
    val RedSoft = Color(0xFF4A1A22)
    val Purple = Color(0xFF8B5CF6)
    val PurpleSoft = Color(0xFF2E1F5C)
    val Neutral = Color(0xFF7E96B8)

    /** Soft black shadow for depth on dark surfaces. */
    val ShadowDark = Color(0x66000000)
    val ShadowLight = Color(0xFF1E5AA8)
}

object MiSpacing {
    val outer: Dp = 14.dp
    val widgetGap: Dp = 8.dp
    val widgetPadding: Dp = 11.dp
    val innerGap: Dp = 6.dp
    val tightGap: Dp = 4.dp
    val heroHeight: Dp = 64.dp
    val identityHeight: Dp = 86.dp
    val touchTarget: Dp = 48.dp
}

object MiRadius {
    val major: Dp = 20.dp
    val majorLarge: Dp = 24.dp
    val widget: Dp = 18.dp
    val small: Dp = 11.dp
    val pill: Dp = 40.dp
}

object MiType {
    val heroTitle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 0.1.sp)
    val heroValue = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Black)
    val memberName = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Black)
    val widgetTitle = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 0.1.sp)
    val primaryValue = TextStyle(fontSize = 21.sp, fontWeight = FontWeight.Black)
    val secondaryValue = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold)
    val body = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium)
    val label = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    val chartLabel = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold)

    /**
     * Smallest text allowed anywhere in the runtime card. Anything below this
     * is unreadable at arm's length on a gym floor, so widgets that used to
     * render 6-7sp captions now use this floor instead.
     */
    val micro = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
}

/** Hard mobile geometry budget from the V4 spec. */
object MiCard {
    const val TARGET_WIDTH_DP = 350
    const val MIN_WIDTH_DP = 320
    const val MAX_WIDTH_DP = 430
    const val PREFERRED_HEIGHT_DP = 520
    const val MAX_HEIGHT_DP = 600
}

fun Modifier.miWidgetSurface(
    shape: Shape = RoundedCornerShape(MiRadius.widget),
    fill: Color = MiColors.Surface,
    borderColor: Color = MiColors.Border,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(fill)
    .border(borderWidth, borderColor, shape)

/**
 * Dark Material3 scheme for dialogs, sheets, menus and inputs, which
 * otherwise render in light defaults. Covers everything the token system
 * does not paint directly.
 */
@Composable
fun MiDarkScheme(): androidx.compose.material3.ColorScheme =
    androidx.compose.material3.darkColorScheme(
        primary = MiColors.Emerald,
        onPrimary = Color(0xFF06281C),
        secondary = MiColors.Blue,
        tertiary = MiColors.Purple,
        background = MiColors.Page,
        onBackground = MiColors.TextPrimary,
        surface = MiColors.Surface,
        onSurface = MiColors.TextPrimary,
        surfaceVariant = MiColors.SurfaceSunken,
        onSurfaceVariant = MiColors.TextSecondary,
        outline = MiColors.Border,
        error = MiColors.Red,
        onError = Color.White
    )

/** Page atmosphere: midnight glow behind the shell and cards. */
val MiPageGradient: Brush
    get() = Brush.verticalGradient(listOf(MiColors.PageGradientTop, MiColors.Page))

/**
 * Restrained neumorphic surface: light fill, very low contrast border and a
 * soft raised treatment. Deliberately subtle - hierarchy over decoration.
 */
@Composable
fun MiNeumorphicSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(MiRadius.widget),
    fill: Color = MiColors.SurfaceRaised,
    content: @Composable () -> Unit
) {
    Column(
        modifier
            .clip(shape)
            .background(fill)
            .border(0.5.dp, MiColors.Border, shape)
            .padding(MiSpacing.widgetPadding)
    ) { content() }
}

@Composable
internal fun MiTokenReference() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(Modifier.fillMaxWidth()) { Text("MiTokens", style = MiType.widgetTitle, color = MiColors.TextPrimary) }
    }
}