package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.MediaAssets
import com.example.badnewgym.feature.memberintelligence.design.ThemeId

@Composable
fun CardHeader(
    theme: ThemeId,
    photoUrl: String? = null,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Brand icon + BAD GYM + Subtitle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Theme Brand Icon in decorative badge
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        when (theme) {
                            ThemeId.PREMIUM_3D -> Brush.linearGradient(listOf(Color(0xFFE5B842), Color(0xFF926F15)))
                            ThemeId.BEAST_MODE -> Brush.linearGradient(listOf(Color(0xFFFF1E27), Color(0xFF660000)))
                            ThemeId.PURPLE_ROYAL -> Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF4C1D95)))
                            ThemeId.FUTURISTIC_NEON -> Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF0369A1)))
                            ThemeId.VIBRANT_GRADIENT -> Brush.linearGradient(listOf(Color(0xFFF97316), Color(0xFFEC4899)))
                            ThemeId.GLASSMORPHISM -> Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
                            ThemeId.MINIMAL_DARK -> Brush.linearGradient(listOf(Color(0xFF374151), Color(0xFF1F2937)))
                            ThemeId.NATURAL_FRESH -> Brush.linearGradient(listOf(Color(0xFF22C55E), Color(0xFF15803D)))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getThemeBrandIcon(theme),
                    contentDescription = "Brand Icon",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = "BAD GYM",
                    color = if (theme == ThemeId.PREMIUM_3D) Color(0xFFFFD700)
                    else if (theme == ThemeId.BEAST_MODE) Color(0xFFFF4D5E)
                    else colors.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = theme.subtitle,
                    color = colors.textSecondary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 11.sp
                )
            }
        }

        // Right: Notification Bell, Profile Avatar with badge, and Time
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Notification Bell
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceMuted),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = colors.textSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Small circle avatar with notification border
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, colors.accent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AvatarImage(
                    model = MediaAssets.ADMIN_AVATAR,
                    size = 30.dp,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Time & relative stamp
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = theme.timeText,
                    color = colors.textPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = theme.timeRelative,
                    color = colors.textMuted,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

private fun getThemeBrandIcon(theme: ThemeId): ImageVector {
    return when (theme) {
        ThemeId.NATURAL_FRESH -> Icons.Rounded.Eco
        ThemeId.FUTURISTIC_NEON -> Icons.Rounded.Bolt
        ThemeId.MINIMAL_DARK -> Icons.Rounded.FitnessCenter
        ThemeId.GLASSMORPHISM -> Icons.Rounded.Favorite
        ThemeId.PREMIUM_3D -> Icons.Rounded.WorkspacePremium
        ThemeId.VIBRANT_GRADIENT -> Icons.Rounded.LocalFireDepartment
        ThemeId.BEAST_MODE -> Icons.Rounded.Pets
        ThemeId.PURPLE_ROYAL -> Icons.Rounded.Diamond
    }
}
