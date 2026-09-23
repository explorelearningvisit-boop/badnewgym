package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badgym.feature.memberintelligence.design.ThemeId
import com.example.badgym.feature.memberintelligence.domain.model.MemberIdentity

@Composable
fun HeroMemberSection(
    identity: MemberIdentity,
    theme: ThemeId,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MemberPhoto(
                photoUrl = identity.photoUrl,
                tier = identity.tier,
                size = 76.dp,
                showVerified = identity.isVerified
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(identity.name, color = colors.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.5).sp, maxLines = 1)
                    Icon(Icons.Rounded.CheckCircle, "Verified", tint = Color(0xFF38BDF8), modifier = Modifier.size(14.dp))
                }
                Text(identity.code ?: "", color = colors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(
                    theme.motto,
                    color = colors.mottoColor,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = if (theme == ThemeId.NATURAL_FRESH || theme == ThemeId.GLASSMORPHISM || theme == ThemeId.PURPLE_ROYAL || theme == ThemeId.PREMIUM_3D) FontStyle.Italic else FontStyle.Normal,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
