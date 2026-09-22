package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.getValue
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CardHeader(
    theme: ThemeId,
    memberPhotoUrl: String?,
    eventTime: Long,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors
    val time = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(eventTime))
    val ring by animateColorAsState(colors.accent, label = "header-ring")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(theme.brandBrush()),
                contentAlignment = Alignment.Center
            ) {
                Icon(theme.brandIcon(), null, tint = Color.White, modifier = Modifier.size(23.dp))
            }
            Spacer(Modifier.width(9.dp))
            Column {
                Text(
                    "BAD GYM",
                    color = colors.textPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.3.sp
                )
                Text(theme.subtitle, color = colors.textSecondary, fontSize = 8.5.sp, fontWeight = FontWeight.Medium, lineHeight = 10.sp)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            Box(
                Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceMuted)
                    .border(1.dp, colors.border.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.NotificationsNone, "Notifications", tint = colors.textSecondary, modifier = Modifier.size(18.dp))
            }

            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.surface)
                    .border(2.dp, ring, CircleShape)
                    .padding(2.dp)
            ) {
                AvatarImage(
                    model = memberPhotoUrl,
                    size = 34.dp,
                    shape = CircleShape,
                    placeholderTint = colors.surfaceMuted,
                    modifier = Modifier.clip(CircleShape)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(time, color = colors.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                Text("Just now", color = colors.textMuted, fontSize = 8.sp)
            }
        }
    }
}

private fun ThemeId.brandBrush(): Brush = when (this) {
    ThemeId.NATURAL_FRESH -> Brush.linearGradient(listOf(Color(0xFF22C55E), Color(0xFF15803D)))
    ThemeId.FUTURISTIC_NEON -> Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF0369A1)))
    ThemeId.MINIMAL_DARK -> Brush.linearGradient(listOf(Color(0xFF64748B), Color(0xFF1F2937)))
    ThemeId.GLASSMORPHISM -> Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
    ThemeId.PREMIUM_3D -> Brush.linearGradient(listOf(Color(0xFFE5B842), Color(0xFF8B6516)))
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
