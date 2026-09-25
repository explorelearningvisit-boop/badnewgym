package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme

@Composable
fun StatusChip(
    text: String,
    modifier: Modifier = Modifier,
    danger: Boolean = text.contains("EXPIRED") || text.contains("OVERDUE")
) {
    val bg = if (danger) BADGymTheme.colors.dangerSoft else BADGymTheme.colors.successSoft
    val fg = if (danger) BADGymTheme.colors.danger else BADGymTheme.colors.success
    Box(
        modifier = modifier
            .background(bg, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, color = fg, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}
