package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Icon
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.colors.ThemeResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal

@Composable
fun IntelligenceSignalCard(
    signal: IntelligenceSignal,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false
) {
    val adaptiveContext = BADGymTheme.adaptiveContext
    val isCritical = signal.priority == com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority.P1_ACTION_REQUIRED
    val overrideColor = if (!isCritical) adaptiveContext?.activeSignalColor else null
    
    val signalColor = overrideColor ?: ThemeResolver.resolveSignalColor(signal, BADGymTheme.colors)
    val signalSoftColor = overrideColor?.copy(alpha = 0.1f) ?: ThemeResolver.resolveSignalSoftColor(signal, BADGymTheme.colors)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(BADGymTheme.shapes.button)
            .background(signalSoftColor)
            .border(
                width = if (emphasized) 2.dp else 1.dp,
                color = signalColor,
                shape = BADGymTheme.shapes.button
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(signalColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = null,
                tint = signalColor,
                modifier = Modifier.size(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = signal.title,
                color = signalColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            if (signal.subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = signal.subtitle,
                    color = signalColor.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = signalColor,
            modifier = Modifier.size(24.dp)
        )
    }
}
