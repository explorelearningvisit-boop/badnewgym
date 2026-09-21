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
    val textColor = ContrastResolver.contentColorFor(eventColor)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                .border(1.dp, eventColor.copy(alpha = 0.65f), RoundedCornerShape(10.dp))
                .background(eventColor.copy(alpha = 0.16f))
                .padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(6.dp).clip(CircleShape).background(eventColor))
            Spacer(Modifier.width(5.dp))
            Text(event.eventType.displayLabel(), color = textColor, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.4.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(theme.timeText, color = BADGymTheme.colors.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(theme.timeRelative, color = BADGymTheme.colors.textMuted, fontSize = 8.sp)
        }
    }
}
