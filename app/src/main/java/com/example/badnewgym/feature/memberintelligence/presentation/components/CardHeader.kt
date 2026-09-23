package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.badnewgym.feature.memberintelligence.design.ThemeId

@Composable
fun CardHeader(
    theme: ThemeId,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.surfaceMuted.copy(alpha = .88f))
                .border(1.dp, colors.border.copy(alpha = .55f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.ChevronLeft,
                contentDescription = "Back",
                tint = colors.brandAccent,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(7.dp))

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(theme.brandBrush()),
            contentAlignment = Alignment.Center
        ) {
            Icon(theme.brandIcon(), "BAD GYM", tint = Color.White, modifier = Modifier.size(19.dp))
        }

        Spacer(Modifier.width(7.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                "BAD GYM",
                color = when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFFFFD700)
                    ThemeId.BEAST_MODE -> Color(0xFFFF4D5E)
                    else -> colors.textPrimary
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.4.sp
            )
            Text(
                theme.subtitle,
                color = colors.textSecondary,
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }

        Icon(
            Icons.Rounded.NotificationsNone,
            contentDescription = "Notifications",
            tint = colors.textPrimary,
            modifier = Modifier.size(19.dp)
        )

        Spacer(Modifier.width(7.dp))

        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(colors.surfaceElevated, colors.surfaceMuted)))
                .border(1.5.dp, colors.border.copy(alpha = .75f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("A", color = colors.textPrimary, fontSize = 9.sp, fontWeight = FontWeight.Black)
        }
    }
}

private fun ThemeId.brandBrush(): Brush = when (this) {
    ThemeId.NATURAL_FRESH -> Brush.linearGradient(listOf(Color(0xFF22C55E), Color(0xFF15803D)))
    ThemeId.FUTURISTIC_NEON -> Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF0369A1)))
    ThemeId.MINIMAL_DARK -> Brush.linearGradient(listOf(Color(0xFF4B5563), Color(0xFF1F2937)))
    ThemeId.GLASSMORPHISM -> Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
    ThemeId.PREMIUM_3D -> Brush.linearGradient(listOf(Color(0xFFE5B842), Color(0xFF926F15)))
    ThemeId.VIBRANT_GRADIENT -> Brush.linearGradient(listOf(Color(0xFFF97316), Color(0xFFEC4899)))
    ThemeId.BEAST_MODE -> Brush.linearGradient(listOf(Color(0xFFFF1E27), Color(0xFF660000)))
    ThemeId.PURPLE_ROYAL -> Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF4C1D95)))
}

private fun ThemeId.brandIcon(): ImageVector = when (this) {
    ThemeId.NATURAL_FRESH -> Icons.Rounded.Eco
    ThemeId.FUTURISTIC_NEON -> Icons.Rounded.Bolt
    ThemeId.MINIMAL_DARK -> Icons.Rounded.FitnessCenter
    ThemeId.GLASSMORPHISM -> Icons.Rounded.Favorite
    ThemeId.PREMIUM_3D -> Icons.Rounded.WorkspacePremium
    ThemeId.VIBRANT_GRADIENT -> Icons.Rounded.LocalFireDepartment
    ThemeId.BEAST_MODE -> Icons.Rounded.Pets
    ThemeId.PURPLE_ROYAL -> Icons.Rounded.Diamond
}
