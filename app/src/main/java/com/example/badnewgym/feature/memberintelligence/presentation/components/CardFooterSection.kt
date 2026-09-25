package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId

@Composable
fun CardFooterSection(
    theme: ThemeId,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            when (theme) {
                ThemeId.PREMIUM_3D -> {
                    Icon(
                        imageVector = Icons.Rounded.WorkspacePremium,
                        contentDescription = "Crown",
                        tint = colors.accent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = theme.footer,
                        color = colors.accent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.BEAST_MODE -> {
                    Text(
                        text = "/// ",
                        color = colors.danger,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = theme.footer,
                        color = colors.accent,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = " ///",
                        color = colors.danger,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                ThemeId.PURPLE_ROYAL -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = theme.footer,
                            color = colors.accent,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.4.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Better Together ♡",
                            color = colors.textSecondary,
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                ThemeId.MINIMAL_DARK -> {
                    Icon(
                        imageVector = Icons.Rounded.FitnessCenter,
                        contentDescription = "Gym",
                        tint = colors.textMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = theme.footer,
                        color = colors.textSecondary,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.4.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.FUTURISTIC_NEON -> {
                    Text(
                        text = theme.footer,
                        color = colors.accent,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.VIBRANT_GRADIENT -> {
                    Text(
                        text = "⚡ " + theme.footer,
                        color = colors.textSecondary,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.GLASSMORPHISM -> {
                    Text(
                        text = "〰 " + theme.footer,
                        color = colors.accent,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.3.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.NATURAL_FRESH -> {
                    Text(
                        text = "🌿 " + theme.footer + " 🌿",
                        color = colors.textSecondary,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
