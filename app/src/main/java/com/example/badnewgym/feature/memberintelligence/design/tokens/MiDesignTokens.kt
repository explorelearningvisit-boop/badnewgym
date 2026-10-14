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
import androidx.compose.ui.geometry.Offset
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
    // Brand
    val Emerald = Color(0xFF00B86B)
    val EmeraldDark = Color(0xFF05945A)
    val EmeraldLight = Color(0xFF3FCB8C)
    val Mint = Color(0xFFE6F8F0)
    val Page = Color(0xFFF4FBF8)

    // Text
    val TextPrimary = Color(0xFF102B26)
    val TextSecondary = Color(0xFF6C8580)
    val TextTertiary = Color(0xFF9AB0AB)
    val Border = Color(0xFFDCEDE7)

    // Surfaces
    val Surface = Color(0xFFFFFFFF)
    val SurfaceSunken = Color(0xFFF1F7F4)
    val SurfaceRaised = Color(0xFFFCFFFE)

    // Semantic
    val Blue = Color(0xFF367FE8)
    val BlueSoft = Color(0xFFEAF2FD)
    val Amber = Color(0xFFE59A1A)
    val AmberSoft = Color(0xFFFDF3E2)
    val Red = Color(0xFFE54848)
    val RedSoft = Color(0xFFFDECEC)
    val Purple = Color(0xFF7659E8)
    val PurpleSoft = Color(0xFFF0EDFD)
    val Neutral = Color(0xFF9AB0AB)

    /** Neutral light used as the dark half of a neumorphic shadow pair. */
    val ShadowDark = Color(0x14102B26)
    val ShadowLight = Color(0xFFFFFFFF)

    /**
     * Dark emerald chrome for the header and rail. The reference puts a deep
     * green frame around a light content canvas; these tokens are that frame.
     */
    val ChromeDeep = Color(0xFF05291B)
    val ChromeDark = Color(0xFF0A3A28)
    val ChromeMid = Color(0xFF0E4A33)
    val ChromeLine = Color(0xFF1B6547)
    val ChromeText = Color(0xFFE4F6EC)
    val ChromeTextDim = Color(0xFF8FC4AC)
    /** Saturated emerald used for the active rail pill and primary actions. */
    val ChromeGlow = Color(0xFF14C77C)
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

/** Deep-green frame gradient behind the header and rail. */
val ChromeGradient: Brush = Brush.verticalGradient(
    listOf(MiColors.ChromeMid, MiColors.ChromeDark, MiColors.ChromeDeep)
)

/**
 * Page canvas behind the widget stack.
 *
 * The reference floats white cards on a living emerald field rather than on flat
 * white. A single flat tint read as a dead page, so this is a mint-to-emerald
 * sweep with a deeper foot: every white card still sits well above the contrast
 * floor, but the page reads as one continuous surface.
 */
val PageGradient: Brush = Brush.verticalGradient(
    0.00f to Color(0xFFEAFBF3),
    0.30f to Color(0xFFD5F3E5),
    0.62f to Color(0xFFB8E9D2),
    1.00f to Color(0xFF93D9BC)
)

/**
 * Soft off-centre highlight laid over [PageGradient].
 *
 * The reference shows light bloom behind the upper widgets. Drawn as a radial
 * wash rather than a second solid layer so it reads as light, not as another
 * surface.
 */
val PageGlow: Brush = Brush.radialGradient(
    colors = listOf(Color(0x59FFFFFF), Color(0x00FFFFFF)),
    center = Offset(0.72f, 0.06f),
    radius = 900f
)

/** Active rail pill: a saturated emerald glow on the dark frame. */
val RailActiveGradient: Brush = Brush.verticalGradient(
    listOf(MiColors.ChromeGlow, MiColors.Emerald)
)

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