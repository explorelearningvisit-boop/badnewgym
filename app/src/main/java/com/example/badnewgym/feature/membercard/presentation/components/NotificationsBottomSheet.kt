package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        scrimColor = Color(0x66021C17),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        GlassSheetSurface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                GlassSheetHeader(
                    title = "Notifications",
                    subtitle = "Gym updates and reminders",
                    badge = "3 NEW",
                    badgeTint = InfographicTokens.RedAlert,
                    onClose = onDismissRequest
                )

                Spacer(modifier = Modifier.height(14.dp))

                NotificationTile(
                    icon = Icons.Default.Payment,
                    tint = InfographicTokens.BrandDeep,
                    title = "Payment Successful \u2022 \u20B92,800",
                    description = "Prime Annual Plan renewed on-time. Next renewal due on 30 Sep 2026.",
                    timeAgo = "2h ago"
                )
                Spacer(modifier = Modifier.height(10.dp))
                NotificationTile(
                    icon = Icons.Default.EmojiEvents,
                    tint = InfographicTokens.Amber,
                    title = "Milestone Unlocked: 12-Day Streak",
                    description = "You have reached the top 5% consistency tier at Bandra West.",
                    timeAgo = "1d ago"
                )
                Spacer(modifier = Modifier.height(10.dp))
                NotificationTile(
                    icon = Icons.Default.Info,
                    tint = InfographicTokens.Blue,
                    title = "Facility Update: New Platforms",
                    description = "New Olympic lifting platforms and calibrated bumper plates are open in Zone 2.",
                    timeAgo = "3d ago"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Primary action — gradient capsule with a lit rim.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(7.dp, RoundedCornerShape(50.dp), ambientColor = Color(0xFF10B981), spotColor = Color(0xFF10B981))
                        .clip(RoundedCornerShape(50.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFF19C48A), Color(0xFF0FA875), Color(0xFF07956A))))
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(50.dp))
                        .clickable { onDismissRequest() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mark all as read",
                        fontSize = 15.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.2.sp
                    )
                }

                GlassSheetFooterPad(height = 18)
            }
        }
    }
}

@Composable
private fun NotificationTile(
    icon: ImageVector,
    tint: Color,
    title: String,
    description: String,
    timeAgo: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(18.dp), ambientColor = Color(0xFF9DB2C8), spotColor = tint.copy(alpha = 0.28f))
            .clip(RoundedCornerShape(18.dp))
            .background(GlassPaneFill)
            .border(1.dp, Color.White, RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Brush.verticalGradient(listOf(tint.copy(alpha = 0.9f), tint.copy(alpha = 0.72f))))
                .border(0.9.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(11.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = GlassInk,
                    fontSize = 13.5.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF0F7F5))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = timeAgo,
                        color = GlassMuted,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = description,
                color = GlassMuted,
                fontSize = 12.5.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
