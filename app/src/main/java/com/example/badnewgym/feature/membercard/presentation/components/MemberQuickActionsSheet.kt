package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Small, honest action surface. It exposes only actions that are implemented
 * in this build. The old avatar opened a theme picker that no longer changed
 * this screen, and the menu opened a developer-only Tailscale panel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberQuickActionsSheet(
    onDismissRequest: () -> Unit,
    onSearch: () -> Unit,
    onNotifications: () -> Unit,
    onReceipt: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = InfographicTokens.Hairline)
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp)
                .padding(bottom = 18.dp)
        ) {
            Text(
                text = "Quick actions",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = InfographicTokens.Ink
            )
            Text(
                text = "Member workspace",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = InfographicTokens.Muted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            QuickActionRow(
                icon = Icons.Default.Search,
                title = "Search records",
                subtitle = "Check-ins, payments, workouts",
                onClick = onSearch
            )
            Spacer(modifier = Modifier.height(7.dp))
            QuickActionRow(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Gym updates and reminders",
                onClick = onNotifications
            )
            Spacer(modifier = Modifier.height(7.dp))
            QuickActionRow(
                icon = Icons.Default.ReceiptLong,
                title = "Receipts",
                subtitle = "Your latest payment receipt",
                onClick = onReceipt
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "BAD GYM member workspace",
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = InfographicTokens.Muted,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun QuickActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = InfographicTokens.SurfaceMint,
        border = BorderStroke(1.dp, InfographicTokens.Hairline),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .padding(0.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = InfographicTokens.BrandSoft,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = InfographicTokens.Brand,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = InfographicTokens.Ink
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = InfographicTokens.Muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = InfographicTokens.Muted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
