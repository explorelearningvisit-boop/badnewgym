package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = theme.footer,
                        color = Color(0xFFD4AF37),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.BEAST_MODE -> {
                    Text(
                        text = "/// ",
                        color = Color(0xFFFF1E27),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = theme.footer,
                        color = Color(0xFFFF4D5E),
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = " ///",
                        color = Color(0xFFFF1E27),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                ThemeId.PURPLE_ROYAL -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = theme.footer,
                            color = Color(0xFFC084FC),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.4.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Better Together ♡",
                            color = Color(0xFFE9D5FF),
                            fontSize = 9.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                ThemeId.MINIMAL_DARK -> {
                    Icon(
                        imageVector = Icons.Rounded.FitnessCenter,
                        contentDescription = "Gym",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = theme.footer,
                        color = Color(0xFF9CA3AF),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.4.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.FUTURISTIC_NEON -> {
                    Text(
                        text = theme.footer,
                        color = Color(0xFF00E5FF),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.VIBRANT_GRADIENT -> {
                    Text(
                        text = "⚡ " + theme.footer,
                        color = Color(0xFF6B21A8),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.GLASSMORPHISM -> {
                    Text(
                        text = "〰 " + theme.footer,
                        color = Color(0xFF0284C7),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.3.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ThemeId.NATURAL_FRESH -> {
                    Text(
                        text = "🌿 " + theme.footer + " 🌿",
                        color = Color(0xFF2D6A4F),
                        fontSize = 9.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
