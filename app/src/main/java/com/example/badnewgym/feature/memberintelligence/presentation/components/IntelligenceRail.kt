package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    Column(
        modifier = modifier
            .width(54.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
            .background(colors.railBackground)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Top collapse / arrow icon
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.surfaceMuted)
                .clickable { /* Toggle expand/collapse */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = "Collapse Rail",
                tint = colors.railInactiveIcon,
                modifier = Modifier.size(18.dp)
            )
        }

        // Main Nav Items: Home, Attend, Plan, Pay, Trainer, Workout, More
        val displayMenus = listOf(
            MenuType.HOME to "Home",
            MenuType.ATTENDANCE to "Attend",
            MenuType.PLAN to "Plan",
            MenuType.PAYMENT to "Pay",
            MenuType.TRAINER to "Trainer",
            MenuType.WORKOUT to "Workout",
            MenuType.SERVICES to "More"
        )

        displayMenus.forEach { (type, label) ->
            val isActive = activeMenu == type
            val iconBgColor by animateColorAsState(
                targetValue = if (isActive) colors.railActiveBackground else Color.Transparent,
                animationSpec = tween(180),
                label = "railItemBg"
            )
            val iconColor = if (isActive) colors.railActiveIcon else colors.railInactiveIcon
            val textColor = if (isActive) colors.textPrimary else colors.textMuted

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onMenuSelected(type) }
                    .padding(vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconBgColor)
                        .then(
                            if (isActive) Modifier.border(0.5.dp, colors.border.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getRailIcon(type),
                        contentDescription = label,
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = label,
                    color = textColor,
                    fontSize = 8.5.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}

private fun getRailIcon(type: MenuType): ImageVector {
    return when (type) {
        MenuType.HOME -> Icons.Rounded.Home
        MenuType.ATTENDANCE -> Icons.Rounded.CalendarToday
        MenuType.PLAN -> Icons.Rounded.Assignment
        MenuType.PAYMENT -> Icons.Rounded.CreditCard
        MenuType.TRAINER -> Icons.Rounded.PersonOutline
        MenuType.WORKOUT -> Icons.Rounded.FitnessCenter
        MenuType.SERVICES -> Icons.Rounded.Settings
        else -> Icons.Rounded.Apps
    }
}
