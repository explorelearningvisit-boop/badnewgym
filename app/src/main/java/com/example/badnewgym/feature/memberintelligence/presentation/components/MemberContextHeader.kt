package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
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
import com.example.badnewgym.feature.memberintelligence.design.colors.ThemeResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@Composable
fun MemberContextHeader(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    modifier: Modifier = Modifier
) {
    val identity = snapshot.identity
    val membership = snapshot.membership

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Row 1: Check-in button and timestamp
        val eventColor = ThemeResolver.resolveEventBadgeColor(currentEvent.eventType, BADGymTheme.colors)
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(Date(currentEvent.occurredAt))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .border(1.dp, eventColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(eventColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentEvent.eventType.displayLabel().uppercase(),
                    color = eventColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formattedTime,
                    color = BADGymTheme.colors.textPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Just now",
                    color = BADGymTheme.colors.textSecondary,
                    fontSize = 9.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 2: Photo and Theme Text
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MemberPhoto(
                photoUrl = identity.photoUrl,
                tier = identity.tier,
                showVerified = false,
                size = 72.dp // Made slightly smaller to fit better
            )
            
            // Theme specific motivational text (simulated)
            Text(
                text = "Good Fitness\nBrighter You",
                color = BADGymTheme.colors.textSecondary.copy(alpha = 0.5f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp,
                lineHeight = 18.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Right
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row 3: Name
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = identity.name,
                style = BADGymTheme.typography.memberName,
                color = BADGymTheme.colors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            if (identity.isVerified) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Rounded.Verified,
                    contentDescription = "Verified member",
                    tint = BADGymTheme.colors.accent,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        // Row 4: Member ID
        identity.code?.let {
            Text(
                text = it,
                color = BADGymTheme.colors.textSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Row 5: Membership Plan Card
        val planName = membership?.planName ?: "Silver Plan"
        val daysLeft = membership?.daysRemaining ?: 0
        
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(BADGymTheme.colors.surfaceElevated)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Verified, // Standardizing icon
                contentDescription = null,
                tint = BADGymTheme.colors.textSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$planName • 6 Months",
                color = BADGymTheme.colors.textPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        // Row 6: Status Chip
        val statusBgColor = if (daysLeft >= 0) BADGymTheme.colors.success else BADGymTheme.colors.danger
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(statusBgColor.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(statusBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (daysLeft >= 0) androidx.compose.material.icons.Icons.Rounded.Check else androidx.compose.material.icons.Icons.Rounded.Close,
                    contentDescription = null,
                    tint = BADGymTheme.colors.surface,
                    modifier = Modifier.size(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (daysLeft >= 0) "ACTIVE • $daysLeft Days Left" else "EXPIRED • ${abs(daysLeft)} Days Ago",
                color = if (daysLeft >= 0) BADGymTheme.colors.success else BADGymTheme.colors.danger,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
