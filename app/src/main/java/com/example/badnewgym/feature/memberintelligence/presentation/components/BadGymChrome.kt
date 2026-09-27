package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.example.badnewgym.R
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme

/**
 * Global Member Intelligence chrome.
 *
 * Navigation deliberately does NOT live here. The member card owns its
 * context navigation so the screen has one information architecture rather
 * than competing top/bottom/rail menus.
 */
@Composable
fun BadGymTopBar(modifier: Modifier = Modifier) {
    val colors = BADGymTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .border(0.8.dp, colors.border.copy(alpha = 0.45f))
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_bad_gym_bolt),
            contentDescription = "BAD GYM",
            tint = Color.Unspecified,
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.width(9.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "BAD GYM",
                color = colors.textPrimary,
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                letterSpacing = 0.6.sp
            )
            Text(
                "Member Intelligence",
                color = colors.textSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Icon(
            Icons.Outlined.AutoAwesome,
            contentDescription = "Intelligence",
            tint = colors.accent,
            modifier = Modifier.size(18.dp)
        )
    }
}
