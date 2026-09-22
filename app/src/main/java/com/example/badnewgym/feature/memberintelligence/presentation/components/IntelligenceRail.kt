package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType

@Composable
fun IntelligenceRail(
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    onMenuSelected: (MenuType) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors
    val displayMenus = listOf(
        MenuType.HOME to "Home",
        MenuType.ATTENDANCE to "Attend",
        MenuType.PLAN to "Plan",
        MenuType.PAYMENT to "Pay",
        MenuType.TRAINER to "Trainer",
        MenuType.WORKOUT to "Workout",
        MenuType.SERVICES to "More"
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp))
            .background(colors.railBackground)
            .padding(horizontal = 5.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        RailBrandButton(
            onClick = { onMenuSelected(MenuType.HOME) }
        )

        Spacer(Modifier.height(2.dp))

        displayMenus.forEach { (type, label) ->
            val menu = menus.firstOrNull { it.id == type }
            val enabled = menu?.isEnabled != false && menu?.isLocked != true
            RailItem(
                type = type,
                label = label,
                selected = activeMenu == type,
                enabled = enabled,
                badge = menu?.badgeCount ?: 0,
                alert = menu?.hasAlert == true,
                onClick = { if (enabled) onMenuSelected(type) }
            )
        }
    }
}

@Composable
private fun RailBrandButton(onClick: () -> Unit) {
    val colors = BADGymTheme.colors
    Box(
        Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.linearGradient(listOf(colors.accent, colors.accentStrong)))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Rounded.Eco, "Home", tint = colors.textOnAccent, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun RailItem(
    type: MenuType,
    label: String,
    selected: Boolean,
    enabled: Boolean,
    badge: Int,
    alert: Boolean,
    onClick: () -> Unit
) {
    val colors = BADGymTheme.colors
    val background by animateColorAsState(
        if (selected) colors.railActiveBackground else Color.Transparent,
        label = "rail-background"
    )
    val iconColor by animateColorAsState(
        if (selected) colors.railActiveIcon else colors.railInactiveIcon,
        label = "rail-icon"
    )
    val scale by animateFloatAsState(
        if (selected) 1f else 0.94f,
        animationSpec = spring(stiffness = 700f),
        label = "rail-scale"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(background)
                .then(
                    if (selected) Modifier.border(1.dp, colors.accentStrong.copy(alpha = 0.45f), RoundedCornerShape(11.dp))
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                getRailIcon(type),
                contentDescription = label,
                tint = if (enabled) iconColor else colors.textMuted.copy(alpha = 0.45f),
                modifier = Modifier.size(18.dp)
            )

            if (alert || badge > 0) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .size(7.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(if (alert) colors.danger else colors.info)
                )
            }
        }

        Text(
            text = label,
            color = if (selected) colors.textPrimary else colors.textMuted,
            fontSize = 7.5.sp,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
            maxLines = 1
        )
    }
}

private fun getRailIcon(type: MenuType): ImageVector = when (type) {
    MenuType.HOME -> Icons.Rounded.Home
    MenuType.ATTENDANCE -> Icons.Rounded.CalendarToday
    MenuType.PLAN -> Icons.Rounded.CardMembership
    MenuType.PAYMENT -> Icons.Rounded.Payments
    MenuType.TRAINER -> Icons.Rounded.Person
    MenuType.WORKOUT -> Icons.Rounded.FitnessCenter
    MenuType.SERVICES -> Icons.Rounded.MoreHoriz
    else -> Icons.Rounded.MoreHoriz
}
