package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.colors.ContrastResolver
import com.example.badnewgym.feature.memberintelligence.design.colors.ThemeResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventHeader(
    event: MemberEvent,
    modifier: Modifier = Modifier
) {
    val eventColor = ThemeResolver.resolveEventBadgeColor(event.eventType, BADGymTheme.colors)
    val textColor = ContrastResolver.contentColorFor(eventColor)
    
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(event.occurredAt))

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, eventColor, RoundedCornerShape(20.dp))
                .background(eventColor.copy(alpha = 0.2f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(eventColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = event.eventType.displayLabel().uppercase(),
                style = BADGymTheme.typography.eventLabel,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formattedTime,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Just now",
                color = BADGymTheme.colors.textSecondary,
                fontSize = 10.sp
            )
        }
    }
}
