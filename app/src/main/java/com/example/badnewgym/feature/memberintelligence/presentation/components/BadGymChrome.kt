package com.example.badnewgym.feature.memberintelligence.presentation.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme

@Composable
fun BadGymTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BADGymTheme.colors.surface)
            .border(width = 0.8.dp, color = BADGymTheme.colors.border.copy(alpha = 0.4f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_bad_gym_bolt),
            contentDescription = "BAD GYM",
            tint = Color.Unspecified,
            modifier = Modifier.size(34.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "BAD GYM",
                color = BADGymTheme.colors.textPrimary,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                letterSpacing = 0.6.sp
            )
            Text(
                text = "Your Gym's Operating Brain",
                color = BADGymTheme.colors.textSecondary,
                fontSize = 13.sp
            )
        }
        Text(
            text = "Member Intelligence",
            color = BADGymTheme.colors.accent,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val selected: Boolean = false
)

@Composable
fun BadGymBottomBar(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit = {}
) {
    val items = listOf(
        BottomNavItem("Members", Icons.Outlined.Groups, selected = true),
        BottomNavItem("Payments", Icons.Outlined.CreditCard),
        BottomNavItem("Trainers", Icons.Outlined.FitnessCenter),
        BottomNavItem("Equipment", Icons.Outlined.Build),
        BottomNavItem("Marketing", Icons.Outlined.Campaign),
        BottomNavItem("Analytics", Icons.Outlined.Analytics),
        BottomNavItem("Market", Icons.Outlined.Storefront),
        BottomNavItem("More", Icons.Outlined.MoreHoriz)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BADGymTheme.colors.surface)
            .border(width = 0.8.dp, color = BADGymTheme.colors.border.copy(alpha = 0.5f))
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onItemClick(item.label) }
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (item.selected) BADGymTheme.colors.accent else BADGymTheme.colors.textMuted,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.label,
                    color = if (item.selected) BADGymTheme.colors.accent else BADGymTheme.colors.textMuted,
                    fontSize = 11.sp,
                    fontWeight = if (item.selected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}
