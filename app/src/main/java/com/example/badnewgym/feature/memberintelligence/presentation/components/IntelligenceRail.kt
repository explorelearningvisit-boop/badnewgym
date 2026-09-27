package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType

/**
 * Single left context rail inside the member card.
 * No right rail and no competing global bottom navigation.
 * Labels live in the content header; the rail stays icon-first.
 */
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
            .clip(RoundedCornerShape(18.dp))
            .background(colors.railBackground.copy(alpha = 0.96f))
            .border(0.8.dp, colors.border.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            .padding(vertical = 6.dp, horizontal = 3.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        visibleMenus.forEach { item ->
            val selected = item.id == activeMenu
            val background by animateColorAsState(
                targetValue = if (selected) colors.railActiveBackground else Color.Transparent,
                animationSpec = tween(160),
                label = "member-rail-bg"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(40.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(background)
                    .then(
                        if (selected) Modifier.border(
                            1.dp,
                            colors.accent.copy(alpha = 0.35f),
                            RoundedCornerShape(11.dp)
                        ) else Modifier
                    )
                    .clickable(
                        onClickLabel = "Open ${item.label}",
                        onClick = { onMenuSelected(item.id) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = menuIcon(item.id),
                    contentDescription = item.label,
                    tint = if (selected) colors.railActiveIcon else colors.railInactiveIcon,
                    modifier = Modifier.size(if (selected) 20.dp else 18.dp)
                )
                if (item.hasAlert || item.badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 3.dp, end = 3.dp)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (item.hasAlert) colors.danger else colors.accent)
                    )
                }
            }
        }
    }
}

private fun menuIcon(type: MenuType): ImageVector = when (type) {
    MenuType.HOME -> Icons.Rounded.Home
    MenuType.ATTENDANCE -> Icons.Rounded.CalendarToday
    MenuType.PLAN -> Icons.Rounded.Assignment
    MenuType.PAYMENT -> Icons.Rounded.CreditCard
    MenuType.TRAINER -> Icons.Rounded.PersonOutline
    MenuType.WORKOUT -> Icons.Rounded.FitnessCenter
    MenuType.SUPPLEMENTS -> Icons.Rounded.LocalDrink
    MenuType.NUTRITION -> Icons.Rounded.Restaurant
    MenuType.SERVICES -> Icons.Rounded.MiscellaneousServices
    MenuType.ADVERTISEMENT -> Icons.Rounded.Campaign
    MenuType.HISTORY -> Icons.Rounded.History
    MenuType.INSIGHT -> Icons.Rounded.AutoAwesome
    MenuType.MORE -> Icons.Rounded.MoreHoriz
}
