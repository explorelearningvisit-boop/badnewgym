package com.example.badnewgym.feature.bodyprogress.presentation

import androidx.compose.ui.graphics.Color

/**
 * Body Progress colour tokens.
 *
 * These used to live in Member Intelligence's design token file, which made this
 * feature depend on a package it has nothing to do with. They are now owned here
 * so Body Progress stands on its own and can be developed, tested and shipped
 * independently.
 *
 * Values are unchanged from the originals, so the feature looks identical.
 */
object MiColors {
    val TextPrimary = Color(0xFF102B26)
    val TextSecondary = Color(0xFF6C8580)
    val TextTertiary = Color(0xFF9AB0AB)
    val Border = Color(0xFFDCEDE7)

    val Surface = Color(0xFFFFFFFF)
    val SurfaceSunken = Color(0xFFF1F7F4)
    val Page = Color(0xFFF4FEFB)

    val Emerald = Color(0xFF14A46B)
    val EmeraldDark = Color(0xFF0E7A4D)
    val Blue = Color(0xFF367FE8)
    val Amber = Color(0xFFE59A1A)
    val Red = Color(0xFFE54848)
    val Neutral = Color(0xFF9AB0AB)
}