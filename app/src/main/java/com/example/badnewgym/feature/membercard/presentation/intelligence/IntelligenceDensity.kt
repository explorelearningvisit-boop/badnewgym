package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

/**
 * Ultra-compact layout constants. Vertically optimized so all widgets and cards
 * consume less vertical space, eliminating unnecessary gaps and massive empty whitespace.
 */
object IntelligenceDensity {

    // Card outer padding (tightened to save vertical canvas)
    val cardPaddingH = 8.dp
    val cardPaddingV = 6.dp

    // Gap between widgets inside a card
    val widgetGap = 4.dp
    val widgetGapSm = 2.dp
    val widgetGapLg = 5.dp

    // Gap between cards / sections
    val sectionGap = 4.dp

    // Icon sizes (bold, high-visibility icons)
    val iconMicro = 12.dp
    val iconSmall = 14.dp
    val iconMedium = 16.dp
    val iconLarge = 18.dp

    // KPI tile height (compact 38dp instead of bulky 44-50dp)
    val kpiTileHeight = 38.dp

    // Radius
    val radiusCard = 14.dp
    val radiusPill = 50.dp
    val radiusChip = 8.dp
    val radiusSmall = 6.dp

    // Common arrangements
    val spacedRow = Arrangement.spacedBy(widgetGap)
    val spacedCol = Arrangement.spacedBy(widgetGap)
    val spacedCenter = Arrangement.Center
    val endArrangement = Arrangement.End
    val spaceBetween = Arrangement.SpaceBetween

    // Common alignments
    val centerVert = Alignment.CenterVertically
    val centerHoriz = Alignment.CenterHorizontally
    val center = Alignment.Center
    val topStart = Alignment.TopStart
}
