package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.badnewgym.R

/**
 * Typeface for the neumorphic dashboard.
 *
 * The style guides ask for Gilroy / SF Pro / SF UI Display. Neither Gilroy nor
 * SF can be redistributed in an APK, so the closest open geometric-humanist
 * equivalent is bundled instead: Plus Jakarta Sans (SIL OFL). It keeps the tall
 * x-height, round terminals and tight apertures that give the guides their voice.
 */
val BadGymSans = FontFamily(
    Font(R.font.plus_jakarta, FontWeight.Normal),
    Font(R.font.plus_jakarta, FontWeight.Medium),
    Font(R.font.plus_jakarta, FontWeight.SemiBold),
    Font(R.font.plus_jakarta, FontWeight.Bold),
    Font(R.font.plus_jakarta, FontWeight.ExtraBold),
    Font(R.font.plus_jakarta, FontWeight.Black)
)
