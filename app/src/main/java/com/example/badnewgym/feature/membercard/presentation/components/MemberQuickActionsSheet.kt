package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardAccent
import com.example.badnewgym.feature.membercard.design.MemberCardAccentManager

/**
 * Small, honest action surface. It exposes only actions that are implemented
 * in this build: search, notifications, receipts, and the four canonical
 * member-card colour variants from the master mockup.
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
                    title = "Quick actions",
                    subtitle = "Member workspace",
                    onClose = onDismissRequest
                )

                Spacer(modifier = Modifier.height(14.dp))

                GlassActionTile(
                    icon = Icons.Default.Search,
                    title = "Search records",
                    subtitle = "Check-ins, payments, workouts",
                    iconTint = InfographicTokens.Brand,
                    onClick = onSearch
                )
                Spacer(modifier = Modifier.height(9.dp))
                GlassActionTile(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Gym updates and reminders",
                    iconTint = InfographicTokens.Purple,
                    onClick = onNotifications
                )
                Spacer(modifier = Modifier.height(9.dp))
                GlassActionTile(
                    icon = Icons.Default.ReceiptLong,
                    title = "Receipts",
                    subtitle = "Your latest payment receipt",
                    iconTint = InfographicTokens.Orange,
                    onClick = onReceipt
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Card colour variants — re-skins the whole member card accent.
                GlassSectionLabel(text = "Card colour")
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    MemberCardAccentManager.all.forEach { accent ->
                        AccentSwatch(
                            accent = accent,
                            isSelected = MemberCardAccentManager.activeAccent == accent,
                            onClick = { MemberCardAccentManager.select(accent) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "BAD GYM member workspace",
                    fontSize = 11.5.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GlassFaint,
                    modifier = Modifier.fillMaxWidth()
                )

                GlassSheetFooterPad(height = 14)
            }
        }
    }
}

@Composable
private fun AccentSwatch(
    accent: MemberCardAccent,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "swatchScale"
    )
    val rim by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = "swatchRim"
    )

    Column(
        modifier = modifier
            .scale(scale)
            .clickable(interactionSource = interaction, indication = null) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .shadow(
                    elevation = (3f + rim * 5f).dp,
                    shape = CircleShape,
                    ambientColor = accent.color.copy(alpha = 0.55f),
                    spotColor = accent.color.copy(alpha = 0.65f)
                )
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            accent.color.copy(alpha = 0.92f),
                            accent.color
                        )
                    )
                )
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.45f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = accent.title,
                    tint = Color.White,
                    modifier = Modifier.size(21.dp)
                )
            }
        }
        Text(
            text = accent.title,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
            color = if (isSelected) GlassInk else GlassMuted,
            maxLines = 1
        )
    }
}
