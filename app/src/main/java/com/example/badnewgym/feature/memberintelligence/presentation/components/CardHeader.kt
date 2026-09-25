package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
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
                .border(1.dp, colors.border.copy(alpha = .55f), RoundedCornerShape(10.dp))
                .clickable(onClick = onBack),
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
                .background(Brush.linearGradient(colors.ctaGradient)),
            contentAlignment = Alignment.Center
        ) {
            Icon(theme.brandIcon(), "BAD GYM", tint = com.example.badnewgym.feature.memberintelligence.design.colors.ContrastResolver.contentColorFor(colors.ctaGradient.first()), modifier = Modifier.size(19.dp))
        }

        Spacer(Modifier.width(7.dp))

        Box(modifier = Modifier.weight(1f)) {
            Column {
                Text(
                    "BAD GYM",
                    color = colors.brandAccent,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.4.sp
                )
                Text(
                    theme.subtitle.replace("\n", " • "),
                    color = colors.textSecondary,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            if (theme == ThemeId.NATURAL_FRESH) {
                Image(
                    painter = painterResource(R.drawable.badgym_leaf_pair_header),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .size(30.dp)
                )
            }
        }

        Icon(
            Icons.Rounded.NotificationsNone,
            contentDescription = "Notifications",
            tint = colors.textPrimary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(colors.surfaceElevated, colors.surfaceMuted)))
                .border(1.5.dp, colors.border.copy(alpha = .75f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("A", color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
    }
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
