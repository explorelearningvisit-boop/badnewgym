package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction

@Composable
fun ContextualCta(
    action: SignalAction?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (action == null) return
    
    val adaptiveContext = BADGymTheme.adaptiveContext
    val bg = adaptiveContext?.activeSignalColor ?: BADGymTheme.colors.accent
    val fg = com.example.badnewgym.feature.memberintelligence.design.colors.ContrastResolver.contentColorFor(bg)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(BADGymTheme.shapes.button)
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = action.label.uppercase(),
            style = BADGymTheme.typography.cta,
            color = fg
        )
    }
}
