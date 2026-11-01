package com.example.badnewgym.feature.membercard.design

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * BAD GYM — MEMBER CARD ACCENT VARIANTS
 * The canonical member card renders in four accent skins (per the master
 * mockup): Emerald, Blue, Violet and Amber. One accent drives the check-in
 * stadium, the avatar ring, the "Active" pill, the membership progress rail,
 * the "Days Left" capsule and the card wash, so the whole card re-skins
 * atomically when the accent changes.
 */
enum class MemberCardAccent(val title: String, val color: Color) {
    EMERALD("Emerald", Color(0xFF10B981)),
    BLUE("Blue", Color(0xFF2979FF)),
    VIOLET("Violet", Color(0xFF8B2FE8)),
    AMBER("Amber", Color(0xFFF57F17))
}

/**
 * Globally reactive accent for the unified member card.
 * Recomposes every band that reads [activeAccent] the moment it changes.
 */
object MemberCardAccentManager {
    var activeAccent: MemberCardAccent by mutableStateOf(MemberCardAccent.EMERALD)

    val all: List<MemberCardAccent> = MemberCardAccent.entries.toList()

    fun cycle() {
        val next = (all.indexOf(activeAccent) + 1) % all.size
        activeAccent = all[next]
    }

    fun select(accent: MemberCardAccent) {
        activeAccent = accent
    }
}

/**
 * Shade math for accent-derived surfaces. Every derived tone comes from the
 * single accent so a variant switch never mixes stale hues.
 */
object MemberCardAccentTones {
    /** Accent-tinted band background (mockup: clearly tinted, still light). */
    fun wash(accent: Color): Color = blend(Color.White, accent, 0.28f)

    /** Soft tint for capsules and icon beds. */
    fun soft(accent: Color): Color = blend(Color.White, accent, 0.55f)

    /** Slightly deeper tint used for hairline borders. */
    fun edge(accent: Color): Color = blend(Color.White, accent, 0.75f)

    /** Darker stop of the glossy stadium gradient. */
    fun deep(accent: Color): Color = blend(Color.Black, accent, 0.72f)

    /** Lighter stop of the glossy stadium gradient. */
    fun bright(accent: Color): Color = blend(accent, Color.White, 0.18f)

    fun stadiumBrush(accent: Color): Brush = Brush.horizontalGradient(
        listOf(bright(accent), accent, deep(accent))
    )

    fun verticalBrush(accent: Color): Brush = Brush.verticalGradient(
        listOf(bright(accent), accent)
    )

    /** Card fill: white crown fading into the accent wash. */
    fun cardWash(accent: Color): Brush = Brush.verticalGradient(
        0.00f to Color(0xFFFFFFFF),
        0.45f to blend(Color.White, accent, 0.05f),
        0.75f to blend(Color.White, accent, 0.10f),
        1.00f to blend(Color.White, accent, 0.12f)
    )

    /** Lit edge gradient that frames the card in the accent. */
    fun cardEdge(accent: Color): Brush = Brush.verticalGradient(
        0.00f to Color(0xFFFFFFFF),
        0.50f to edge(accent),
        1.00f to deep(accent).copy(alpha = 0.35f)
    )

    // [weight] is the share kept from [from]; the rest comes from [to].
    private fun blend(from: Color, to: Color, weight: Float): Color = Color(
        red = from.red * (1f - weight) + to.red * weight,
        green = from.green * (1f - weight) + to.green * weight,
        blue = from.blue * (1f - weight) + to.blue * weight,
        alpha = from.alpha
    )
}
