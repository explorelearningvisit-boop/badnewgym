package com.example.badnewgym.feature.membercard.presentation.intelligence

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * A complete neumorphic skin. Every surface, shadow, ink level and accent lives
 * here, so switching a theme re-skins the whole dashboard with no per-screen work.
 */
data class NeuTheme(
    val id: String,
    val label: String,
    val dark: Boolean,
    val canvas: Color,
    val canvasDeep: Color,
    val surface: Color,
    val surfaceRaised: Color,
    val surfaceSunken: Color,
    val shadow: Color,
    val shadowSoft: Color,
    val highlight: Color,
    val highlightAlpha: Float,
    val ink: Color,
    val inkSoft: Color,
    val muted: Color,
    val faint: Color,
    val violet: Color,
    val violetDeep: Color,
    val violetWash: Color,
    val green: Color,
    val greenDeep: Color,
    val greenBright: Color,
    val greenWash: Color,
    val coral: Color,
    val coralDeep: Color,
    val coralWash: Color,
    val amber: Color,
    val amberDeep: Color,
    val amberWash: Color,
    val blue: Color,
    val sky: Color,
    val pink: Color,
    val onAccent: Color
) {
    /** A representative swatch used by the picker. */
    val swatch: Color get() = violet
    val swatchBackdrop: Color get() = canvas
}

private fun theme(
    id: String,
    label: String,
    dark: Boolean = false,
    canvas: Long,
    canvasDeep: Long,
    surface: Long,
    surfaceRaised: Long,
    surfaceSunken: Long,
    shadow: Long,
    shadowSoft: Long,
    highlight: Long = 0xFFFFFFFF,
    highlightAlpha: Float = 0.62f,
    ink: Long,
    inkSoft: Long,
    muted: Long,
    faint: Long,
    violet: Long,
    violetDeep: Long,
    violetWash: Long,
    green: Long,
    greenDeep: Long,
    greenBright: Long,
    greenWash: Long,
    coral: Long,
    coralDeep: Long,
    coralWash: Long,
    amber: Long,
    amberDeep: Long,
    amberWash: Long,
    blue: Long,
    sky: Long,
    pink: Long,
    onAccent: Long = 0xFFFFFFFF
) = NeuTheme(
    id = id,
    label = label,
    dark = dark,
    canvas = Color(canvas),
    canvasDeep = Color(canvasDeep),
    surface = Color(surface),
    surfaceRaised = Color(surfaceRaised),
    surfaceSunken = Color(surfaceSunken),
    shadow = Color(shadow),
    shadowSoft = Color(shadowSoft),
    highlight = Color(highlight),
    highlightAlpha = highlightAlpha,
    ink = Color(ink),
    inkSoft = Color(inkSoft),
    muted = Color(muted),
    faint = Color(faint),
    violet = Color(violet),
    violetDeep = Color(violetDeep),
    violetWash = Color(violetWash),
    green = Color(green),
    greenDeep = Color(greenDeep),
    greenBright = Color(greenBright),
    greenWash = Color(greenWash),
    coral = Color(coral),
    coralDeep = Color(coralDeep),
    coralWash = Color(coralWash),
    amber = Color(amber),
    amberDeep = Color(amberDeep),
    amberWash = Color(amberWash),
    blue = Color(blue),
    sky = Color(sky),
    pink = Color(pink),
    onAccent = Color(onAccent)
)

object NeuThemes {

    /**
     * Flagship skin: Neo-Cloud Sky-Blue & Royal Electric Blue claymorphism.
     * Soft dreamy sky-blue canvas, pure white claymorphic surfaces, cool blue ambient shadows,
     * vivid royal electric blue primary accents and crisp multi-color semantic pills.
     */
    val NeoSky = theme(
        id = "neosky", label = "Neo Sky",
        canvas = 0xFFEBF3FE, canvasDeep = 0xFFDCE8FD, surface = 0xFFFFFFFF,
        surfaceRaised = 0xFFFFFFFF, surfaceSunken = 0xFFE0ECFD,
        shadow = 0xFFB4C8E6, shadowSoft = 0xFFD8E5F7,
        highlight = 0xFFFFFFFF, highlightAlpha = 0.85f,
        ink = 0xFF0F172A, inkSoft = 0xFF1E293B, muted = 0xFF475569, faint = 0xFF94A3B8,
        violet = 0xFF2563EB, violetDeep = 0xFF1D4ED8, violetWash = 0xFFE0EDFE,
        green = 0xFF16A34A, greenDeep = 0xFF15803D, greenBright = 0xFF22C55E, greenWash = 0xFFDCFCE7,
        coral = 0xFFEF4444, coralDeep = 0xFFDC2626, coralWash = 0xFFFEE2E2,
        amber = 0xFFD97706, amberDeep = 0xFFB45309, amberWash = 0xFFFEF3C7,
        blue = 0xFF3B82F6, sky = 0xFF38BDF8, pink = 0xFFEC4899,
        onAccent = 0xFFFFFFFF
    )

    /**
     * Primary skin: premium white/lavender canvas, saturated violet primary and
     * the spec's semantic trio (success #079B70, danger #E34E58, warning #E9AA35).
     */
    val Lavender = theme(
        id = "lavender", label = "Lavender",
        canvas = 0xFFF5F3F8, canvasDeep = 0xFFEFEBF6, surface = 0xFFFFFFFF,
        surfaceRaised = 0xFFFFFFFF, surfaceSunken = 0xFFEDE8F6,
        shadow = 0xFFC9C0DC, shadowSoft = 0xFFE4DEF0,
        ink = 0xFF2B2340, inkSoft = 0xFF5F5674, muted = 0xFF8B82A0, faint = 0xFFA79FBA,
        violet = 0xFF8239E8, violetDeep = 0xFF6424C8, violetWash = 0xFFF0E8FA,
        green = 0xFF079B70, greenDeep = 0xFF057A58, greenBright = 0xFF2FC79A, greenWash = 0xFFDDF6EC,
        coral = 0xFFE34E58, coralDeep = 0xFFBE3B45, coralWash = 0xFFFCE6E8,
        amber = 0xFFE9AA35, amberDeep = 0xFFC68A1E, amberWash = 0xFFFFF2D4,
        blue = 0xFF5B8DEF, sky = 0xFF489CC1, pink = 0xFFF88AB0
    )

    /** The original guide palette: cool grey canvas, violet accent. */
    val Snow = theme(
        id = "snow", label = "Snow",
        canvas = 0xFFE6EAEE, canvasDeep = 0xFFE0E5EA, surface = 0xFFEDF0F4,
        surfaceRaised = 0xFFF5F7FA, surfaceSunken = 0xFFDCE2E8,
        shadow = 0xFFA6B4C8, shadowSoft = 0xFFD3DBE6,
        ink = 0xFF343434, inkSoft = 0xFF6E7887, muted = 0xFF8C94A0, faint = 0xFFA1AEBF,
        violet = 0xFF7B81BE, violetDeep = 0xFF5E649E, violetWash = 0xFFE0E3F2,
        green = 0xFF21A87D, greenDeep = 0xFF178A66, greenBright = 0xFF42CD8C, greenWash = 0xFFDAEFE6,
        coral = 0xFFFF7272, coralDeep = 0xFFE05C5C, coralWash = 0xFFFFE3E3,
        amber = 0xFFFFBB0D, amberDeep = 0xFFD99F0B, amberWash = 0xFFFBEFD4,
        blue = 0xFF6B92E5, sky = 0xFF489CC1, pink = 0xFFF88AB0
    )

    /** Cool mint green, same light grey family. */
    val Mint = theme(
        id = "mint", label = "Mint",
        canvas = 0xFFE2EDE8, canvasDeep = 0xFFDCE8E2, surface = 0xFFE9F3EE,
        surfaceRaised = 0xFFF2FAF6, surfaceSunken = 0xFFD7E5DE,
        shadow = 0xFFA2BDB2, shadowSoft = 0xFFCFE0D8,
        ink = 0xFF22312B, inkSoft = 0xFF5C6F67, muted = 0xFF7C8F87, faint = 0xFF9CAFA6,
        violet = 0xFF2F9E7A, violetDeep = 0xFF1F7C5F, violetWash = 0xFFD6EBE2,
        green = 0xFF21A87D, greenDeep = 0xFF15705A, greenBright = 0xFF42CD8C, greenWash = 0xFFD8EFE4,
        coral = 0xFFFF7A6B, coralDeep = 0xFFD95B4D, coralWash = 0xFFFFE4DF,
        amber = 0xFFFFC22E, amberDeep = 0xFFD9A015, amberWash = 0xFFFBF0D3,
        blue = 0xFF4E9BC4, sky = 0xFF3D8EA8, pink = 0xFFE88BA8
    )

    /** Warm sand / parchment. */
    val Sand = theme(
        id = "sand", label = "Sand",
        canvas = 0xFFEDE7DE, canvasDeep = 0xFFE7E0D6, surface = 0xFFF3EEE5,
        surfaceRaised = 0xFFFBF7EF, surfaceSunken = 0xFFE2DACE,
        shadow = 0xFFC0B4A0, shadowSoft = 0xFFE0D7C8,
        ink = 0xFF35302A, inkSoft = 0xFF6E6559, muted = 0xFF8E8578, faint = 0xFFA9A093,
        violet = 0xFFB07B4A, violetDeep = 0xFF8C5D34, violetWash = 0xFFF0E3D4,
        green = 0xFF5F8F4E, greenDeep = 0xFF46703A, greenBright = 0xFF7FB267, greenWash = 0xFFE2EDD8,
        coral = 0xFFD9705A, coralDeep = 0xFFB4553F, coralWash = 0xFFF7E2DB,
        amber = 0xFFE2A233, amberDeep = 0xFFB77F1E, amberWash = 0xFFF8ECD3,
        blue = 0xFF5C86B0, sky = 0xFF4F88A0, pink = 0xFFC97F97
    )

    /** Cool steel blue. */
    val Steel = theme(
        id = "steel", label = "Steel",
        canvas = 0xFFE3E9F1, canvasDeep = 0xFFDCE3EC, surface = 0xFFEAF0F7,
        surfaceRaised = 0xFFF4F8FD, surfaceSunken = 0xFFD8E0EA,
        shadow = 0xFFA3B1C6, shadowSoft = 0xFFD0DAE6,
        ink = 0xFF28313D, inkSoft = 0xFF5D6B7C, muted = 0xFF7E8C9E, faint = 0xFF9EABBC,
        violet = 0xFF4F7CC4, violetDeep = 0xFF3A60A0, violetWash = 0xFFDCE7F7,
        green = 0xFF2A9B7E, greenDeep = 0xFF1B7A60, greenBright = 0xFF4CC79F, greenWash = 0xFFD9EDE6,
        coral = 0xFFF5736F, coralDeep = 0xFFCE5754, coralWash = 0xFFFFE3E1,
        amber = 0xFFF2B32C, amberDeep = 0xFFC79112, amberWash = 0xFFF9EDD2,
        blue = 0xFF4F7CC4, sky = 0xFF3F87B0, pink = 0xFFD97FA5
    )

    /** Soft rose. */
    val Rose = theme(
        id = "rose", label = "Rose",
        canvas = 0xFFF0E6EC, canvasDeep = 0xFFEAE0E6, surface = 0xFFF6EDF2,
        surfaceRaised = 0xFFFDF6FA, surfaceSunken = 0xFFE5D9E0,
        shadow = 0xFFC2AEB9, shadowSoft = 0xFFE2D3DB,
        ink = 0xFF38292F, inkSoft = 0xFF6F5C65, muted = 0xFF8F7C85, faint = 0xFFAB98A1,
        violet = 0xFFB4638C, violetDeep = 0xFF8E4A6E, violetWash = 0xFFF1DDE7,
        green = 0xFF3F9C7C, greenDeep = 0xFF2B7A5E, greenBright = 0xFF5FC6A3, greenWash = 0xFFDCEEE7,
        coral = 0xFFE86A6A, coralDeep = 0xFFC24E4E, coralWash = 0xFFF9E0E0,
        amber = 0xFFE9A93A, amberDeep = 0xFFC08A1E, amberWash = 0xFFF8EBD5,
        blue = 0xFF6A82C0, sky = 0xFF4F87A8, pink = 0xFFE2789F
    )

    /** Dark neumorphism: the same geometry with an inverted shadow pair. */
    val Midnight = theme(
        id = "midnight", label = "Midnight", dark = true,
        canvas = 0xFF23272F, canvasDeep = 0xFF1E222A, surface = 0xFF2A2F38,
        surfaceRaised = 0xFF313742, surfaceSunken = 0xFF1D2229,
        shadow = 0xFF14171C, shadowSoft = 0xFF3A414D,
        highlight = 0xFF6E7A8E, highlightAlpha = 0.45f,
        ink = 0xFFF2F5F9, inkSoft = 0xFFB3BCC9, muted = 0xFF8B95A3, faint = 0xFF6F7986,
        violet = 0xFF9AA0E0, violetDeep = 0xFF767DC4, violetWash = 0xFF343A4E,
        green = 0xFF3FCB9B, greenDeep = 0xFF2AA07A, greenBright = 0xFF6FE0BA, greenWash = 0xFF253C36,
        coral = 0xFFFF8A8A, coralDeep = 0xFFD96A6A, coralWash = 0xFF402A2C,
        amber = 0xFFFFC93C, amberDeep = 0xFFD9A51F, amberWash = 0xFF3E3423,
        blue = 0xFF8AAAE8, sky = 0xFF6FB6D8, pink = 0xFFF7A0C0,
        onAccent = 0xFF1B1F26
    )

    val all = listOf(Lavender, NeoSky, Snow, Mint, Sand, Steel, Rose, Midnight)

    fun byId(id: String): NeuTheme = all.firstOrNull { it.id == id } ?: Lavender
}

/**
 * Live appearance state. Held in a singleton so the picker (inside a bottom
 * sheet) and the dashboard read the same values without threading callbacks.
 */
object NeuStyle {

    private const val PREFS = "badgym.neu.style"

    private var prefs: android.content.SharedPreferences? = null

    var themeId by mutableStateOf(NeuThemes.Lavender.id)
        private set
    /** Multiplies every text size only. */
    var textScale by mutableFloatStateOf(1f)
        private set
    /** Multiplies icons, buttons, paddings and component boxes. */
    var componentScale by mutableFloatStateOf(1f)
        private set
    /** Multiplies every neumorphic corner radius. */
    var radiusScale by mutableFloatStateOf(1f)
        private set
    /** Multiplies shadow offset + blur. 0 removes the effect entirely. */
    var depthScale by mutableFloatStateOf(1f)
        private set

    val theme: NeuTheme get() = NeuThemes.byId(themeId)

    fun attach(context: Context) {
        if (prefs != null) return
        val p = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs = p
        themeId = NeuThemes.byId(p.getString("theme", NeuThemes.Lavender.id) ?: NeuThemes.Lavender.id).id
        textScale = p.getFloat("text", 1f).coerceIn(TEXT_MIN, TEXT_MAX)
        componentScale = p.getFloat("component", 1f).coerceIn(COMPONENT_MIN, COMPONENT_MAX)
        radiusScale = p.getFloat("radius", 1f).coerceIn(RADIUS_MIN, RADIUS_MAX)
        depthScale = p.getFloat("depth", 1f).coerceIn(DEPTH_MIN, DEPTH_MAX)
    }

    fun selectTheme(id: String) {
        themeId = NeuThemes.byId(id).id
        save()
    }

    fun updateTextScale(value: Float) {
        textScale = value.coerceIn(TEXT_MIN, TEXT_MAX)
        save()
    }

    fun updateComponentScale(value: Float) {
        componentScale = value.coerceIn(COMPONENT_MIN, COMPONENT_MAX)
        save()
    }

    fun updateRadiusScale(value: Float) {
        radiusScale = value.coerceIn(RADIUS_MIN, RADIUS_MAX)
        save()
    }

    fun updateDepthScale(value: Float) {
        depthScale = value.coerceIn(DEPTH_MIN, DEPTH_MAX)
        save()
    }

    fun reset() {
        themeId = NeuThemes.Lavender.id
        textScale = 1f
        componentScale = 1f
        radiusScale = 1f
        depthScale = 1f
        save()
    }

    private fun save() {
        prefs?.edit()
            ?.putString("theme", themeId)
            ?.putFloat("text", textScale)
            ?.putFloat("component", componentScale)
            ?.putFloat("radius", radiusScale)
            ?.putFloat("depth", depthScale)
            ?.apply()
    }

    const val TEXT_MIN = 0.85f
    const val TEXT_MAX = 1.30f
    const val COMPONENT_MIN = 0.85f
    const val COMPONENT_MAX = 1.25f
    const val RADIUS_MIN = 0.4f
    const val RADIUS_MAX = 1.6f
    const val DEPTH_MIN = 0f
    const val DEPTH_MAX = 1.8f
}
