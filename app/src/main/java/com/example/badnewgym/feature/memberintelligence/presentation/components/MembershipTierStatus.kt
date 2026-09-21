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
fun MembershipTierStatus(membership: MembershipStatus?, theme: ThemeId, modifier: Modifier = Modifier) {
    val colors = BADGymTheme.colors
    val planName = membership?.planName ?: "Gold Plan"
    val planType = membership?.planType ?: "12 Months"
    val daysRemaining = membership?.daysRemaining ?: 48

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Rounded.WorkspacePremium, null,
                tint = when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFFFFD700)
                    ThemeId.PURPLE_ROYAL -> Color(0xFFC084FC)
                    ThemeId.NATURAL_FRESH -> Color(0xFFD97706)
                    else -> colors.accent
                },
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(5.dp))
            Text("$planName • $planType", color = colors.textSecondary, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold)
        }

        Row(
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                .background(colors.successSoft)
                .border(1.dp, colors.success.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
                .padding(horizontal = 7.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.CheckCircle, null, tint = colors.success, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(3.dp))
            Column {
                Text("ACTIVE", color = colors.success, fontSize = 8.5.sp, fontWeight = FontWeight.ExtraBold)
                Text("$daysRemaining Days Left", color = colors.success.copy(alpha = 0.85f), fontSize = 7.5.sp)
            }
        }
    }
}
