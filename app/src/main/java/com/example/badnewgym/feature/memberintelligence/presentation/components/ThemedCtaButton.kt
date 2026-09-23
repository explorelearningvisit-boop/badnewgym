package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId

@Composable
fun ThemedCtaButton(
    theme: ThemeId,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null
) {
    val colors = BADGymTheme.colors
    val displayText = label ?: "${theme.ctaText} →"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                when (theme) {
                    ThemeId.PREMIUM_3D -> Brush.horizontalGradient(listOf(Color(0xFFFFD700), Color(0xFFE5B842), Color(0xFFB8860B)))
                    ThemeId.BEAST_MODE -> Brush.horizontalGradient(listOf(Color(0xFFFF1E27), Color(0xFFCC0000)))
                    ThemeId.PURPLE_ROYAL -> Brush.horizontalGradient(listOf(Color(0xFFA855F7), Color(0xFF7E22CE)))
                    ThemeId.FUTURISTIC_NEON -> Brush.horizontalGradient(listOf(Color(0xFF00E5FF), Color(0xFF0284C7)))
                    ThemeId.VIBRANT_GRADIENT -> Brush.horizontalGradient(listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6), Color(0xFFEC4899)))
                    ThemeId.GLASSMORPHISM -> Brush.horizontalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
                    ThemeId.MINIMAL_DARK -> Brush.horizontalGradient(listOf(Color(0xFF475569), Color(0xFF334155)))
                    ThemeId.NATURAL_FRESH -> Brush.horizontalGradient(listOf(Color(0xFF16A34A), Color(0xFF15803D)))
                }
            )
            .border(
                width = if (theme == ThemeId.MINIMAL_DARK) 1.dp else 0.5.dp,
                color = when (theme) {
                    ThemeId.MINIMAL_DARK -> Color(0xFF94A3B8)
                    ThemeId.PREMIUM_3D -> Color(0xFFFFFBEB)
                    ThemeId.FUTURISTIC_NEON -> Color(0xFFE0F2FE)
                    else -> Color.White.copy(alpha = 0.3f)
                },
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = displayText,
                color = if (theme == ThemeId.PREMIUM_3D) Color(0xFF1C1304) else Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.4.sp
            )
        }
    }
}
