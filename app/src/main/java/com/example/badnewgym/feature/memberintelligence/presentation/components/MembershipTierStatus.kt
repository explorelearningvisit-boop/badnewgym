package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.WorkspacePremium
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
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus

@Composable
fun MembershipTierStatus(
    membership: MembershipStatus?,
    theme: ThemeId,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors
    val planName = membership?.planName ?: "Gold Plan"
    val planType = membership?.planType ?: "12 Months"
    val daysRemaining = membership?.daysRemaining ?: 48
    val isActive = membership?.isActive ?: true

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Gold Plan Band
        val goldBg = colors.vipSoft
        val goldBorder = colors.vip.copy(alpha = 0.8f)
        val goldText = colors.vip
        val goldIcon = colors.vip


        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(goldBg)
                .border(1.dp, goldBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.WorkspacePremium,
                contentDescription = "Plan",
                tint = goldIcon,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Column {
                Text(
                    text = planName,
                    color = goldText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = planType,
                    color = goldText.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // 2. ACTIVE Status Band
        val activeBg = if (isActive) colors.successSoft.copy(alpha = 0.95f) else colors.dangerSoft.copy(alpha = 0.95f)
        val activeBorder = if (isActive) colors.success.copy(alpha = 0.5f) else colors.danger.copy(alpha = 0.5f)
        val activeTint = if (isActive) colors.success else colors.danger

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(activeBg)
                .border(1.dp, activeBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.CheckCircle,
                contentDescription = "Status",
                tint = activeTint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Column {
                Text(
                    text = if (isActive) "ACTIVE" else "INACTIVE",
                    color = activeTint,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "$daysRemaining Days Left",
                    color = activeTint.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
