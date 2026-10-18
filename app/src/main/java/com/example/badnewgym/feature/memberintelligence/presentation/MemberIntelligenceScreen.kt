package com.example.badnewgym.feature.memberintelligence.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Member Intelligence host screen.
 *
 * The previous member intelligence card / dashboard presentation has been removed.
 * This screen is intentionally a blank white surface, kept so navigation and the
 * route contract stay intact while a new design is built from scratch.
 */
@Composable
fun MemberIntelligenceScreen(viewModel: MemberIntelligenceViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )
}