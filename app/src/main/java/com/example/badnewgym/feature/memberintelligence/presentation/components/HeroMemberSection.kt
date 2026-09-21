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
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberIdentity

@Composable
fun HeroMemberSection(
    identity: MemberIdentity,
    theme: ThemeId,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Top Action Pill (e.g. + CHECK-IN / + WORKOUT / + TRAINER)
        Row {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when (theme) {
                            ThemeId.PREMIUM_3D -> Brush.horizontalGradient(listOf(Color(0xFFE5B842), Color(0xFF926F15)))
                            ThemeId.BEAST_MODE -> Brush.horizontalGradient(listOf(Color(0xFF00E5FF), Color(0xFF007799)))
                            ThemeId.PURPLE_ROYAL -> Brush.horizontalGradient(listOf(Color(0xFFA855F7), Color(0xFF7E22CE)))
                            ThemeId.FUTURISTIC_NEON -> Brush.horizontalGradient(listOf(Color(0xFF00E5FF), Color(0xFF0284C7)))
                            ThemeId.VIBRANT_GRADIENT -> Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFEC4899)))
                            ThemeId.GLASSMORPHISM -> Brush.horizontalGradient(listOf(Color(0xFF22C55E), Color(0xFF16A34A)))
                            ThemeId.MINIMAL_DARK -> Brush.horizontalGradient(listOf(Color(0xFF262C36), Color(0xFF1F242D)))
                            ThemeId.NATURAL_FRESH -> Brush.horizontalGradient(listOf(Color(0xFF16A34A), Color(0xFF15803D)))
                        }
                    )
                    .border(
                        1.dp,
                        if (theme == ThemeId.MINIMAL_DARK) Color(0xFF3B4452)
                        else Color.White.copy(alpha = 0.25f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = theme.actionChipText,
                    color = Color.White,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Photo + Name + ID + Motto Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Member Photo with Border & Verified Badge
            MemberPhoto(
                photoUrl = identity.photoUrl,
                tier = identity.tier,
                size = 72.dp,
                showVerified = identity.isVerified
            )

            // Name, ID Code and Motto Script
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = identity.name,
                        color = colors.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "Verified Badge",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = identity.code ?: "",
                    color = colors.textSecondary,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Signature Theme Motto
                Text(
                    text = theme.motto,
                    color = colors.mottoColor,
                    fontSize = when (theme) {
                        ThemeId.FUTURISTIC_NEON, ThemeId.BEAST_MODE -> 11.5.sp
                        ThemeId.PREMIUM_3D -> 11.sp
                        else -> 10.5.sp
                    },
                    fontWeight = when (theme) {
                        ThemeId.FUTURISTIC_NEON, ThemeId.BEAST_MODE -> FontWeight.Black
                        ThemeId.MINIMAL_DARK -> FontWeight.Medium
                        else -> FontWeight.Bold
                    },
                    fontStyle = if (theme == ThemeId.NATURAL_FRESH || theme == ThemeId.GLASSMORPHISM || theme == ThemeId.PURPLE_ROYAL || theme == ThemeId.PREMIUM_3D) FontStyle.Italic else FontStyle.Normal,
                    lineHeight = 13.sp
                )
            }
        }
    }
}
