package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Bold-first, high-legibility typography scale for the Member Intelligence dashboard.
 *
 * Every weight is Bold or Black — no thin or low-contrast text anywhere — so that
 * all information is effortlessly readable for human eyes of all ages, including users
 * with low vision or older age eyesight challenges.
 *
 * Font sizes are carefully balanced:
 * - Minimum readable caption/chip size: 11sp (Bold)
 * - Standard body / subtitles: 12sp - 13sp (Bold)
 * - Section / Card headings: 15sp (ExtraBold)
 * - Prominent metrics: 18sp - 22sp (Black)
 * - Hero numbers: 24sp - 26sp (Black)
 */
object IntelligenceTypography {

    // ---------- Font weights (all bold / extra bold / black) ----------
    val weightLabel = FontWeight.Bold         // chip labels, axis labels, meta
    val weightBody  = FontWeight.Bold         // body text, section subtitles
    val weightTitle = FontWeight.ExtraBold    // card titles, screen headings
    val weightValue = FontWeight.Black        // big key numbers (81%, ₹14,400, 19.4 hr)

    // ---------- Accessible Font sizes (sp) ----------
    // Minimum caption size: pill labels, chip subtext, axis ticks (never < 11sp)
    val sizeMicro = 11.sp
    // Small: card subtitles, secondary metrics, "Avg / Day" captions
    val sizeSmall = 12.sp
    // Base body: readable paragraph text, insight copy
    val sizeBody = 13.sp
    // Section titles inside cards
    val sizeCardTitle = 15.sp
    // Big key metrics: "81%", "69.5 kg", "₹14,400"
    val sizeMetric = 19.sp
    // Largest: screen headings, hero numbers
    val sizeHero = 24.sp
    // Wordmark / brand
    val sizeWordmark = 17.sp
}
