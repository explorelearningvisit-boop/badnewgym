package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
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
    val visibleMenus = menus.filter { it.isVisible && it.isEnabled }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
            .background(colors.railBackground.copy(alpha = .96f))
            .padding(vertical = 10.dp, horizontal = 2.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        visibleMenus.forEach { menu ->
            val isActive = activeMenu == menu.id
            val bg by animateColorAsState(
                if (isActive) colors.railActiveBackground else Color.Transparent,
                tween(180),
                label = "rail-bg"
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onMenuSelected(menu.id) }
                    .padding(vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isActive) 38.dp else 34.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(bg)
                        .then(
                            if (isActive) Modifier.border(
                                1.dp,
                                colors.border.copy(alpha = .6f),
                                RoundedCornerShape(11.dp)
                            ) else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        getRailIcon(menu.id),
                        contentDescription = menu.label,
                        tint = if (isActive) colors.railActiveIcon else colors.railInactiveIcon,
                        modifier = Modifier.size(if (isActive) 20.dp else 18.dp)
                    )
                    if (menu.hasAlert || menu.badgeCount > 0) {
                        Box(
                            Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-2).dp, y = 2.dp)
                                .size(7.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(colors.danger)
                        )
                    }
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = menu.label,
                    color = if (isActive) colors.textPrimary else colors.textMuted,
                    fontSize = if (isActive) 8.5.sp else 8.sp,
                    fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun RailIcon(
    icon: ImageVector,
    label: String,
    active: Boolean,
    colors: com.example.badnewgym.feature.memberintelligence.design.colors.BADGymColors,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(31.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(colors.surfaceMuted)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, label, tint = colors.railInactiveIcon, modifier = Modifier.size(18.dp))
    }
}

private fun getRailIcon(type: MenuType): ImageVector = when (type) {
    MenuType.HOME -> Icons.Rounded.Home
    MenuType.ATTENDANCE -> Icons.Rounded.CalendarToday
    MenuType.PLAN -> Icons.Rounded.Assignment
    MenuType.PAYMENT -> Icons.Rounded.CreditCard
    MenuType.TRAINER -> Icons.Rounded.PersonOutline
    MenuType.WORKOUT -> Icons.Rounded.FitnessCenter
    MenuType.SUPPLEMENTS -> Icons.Rounded.LocalDrink
    MenuType.NUTRITION -> Icons.Rounded.Restaurant
    MenuType.SERVICES -> Icons.Rounded.MiscellaneousServices
    MenuType.HISTORY -> Icons.Rounded.History
    MenuType.INSIGHT -> Icons.Rounded.AutoAwesome
    MenuType.MORE -> Icons.Rounded.MoreHoriz
}
