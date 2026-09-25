package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.colors.ContrastResolver
import com.example.badnewgym.feature.memberintelligence.design.colors.ThemeResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent

@Composable
fun EventHeader(event: MemberEvent, theme: ThemeId, modifier: Modifier = Modifier) {
    val eventColor = ThemeResolver.resolveEventBadgeColor(event.eventType, BADGymTheme.colors)
    val isNatural = theme == ThemeId.NATURAL_FRESH
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(if (isNatural) BADGymTheme.colors.success else eventColor)
                .border(
                    1.dp,
                    if (isNatural) BADGymTheme.colors.success.copy(alpha = 0.8f) else eventColor.copy(alpha = 0.8f),
                    RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "+ " + event.eventType.displayLabel(),
                color = BADGymTheme.colors.textOnAccent,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = theme.timeText,
                color = BADGymTheme.colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = theme.timeRelative,
                color = BADGymTheme.colors.textMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
