package com.example.badnewgym.feature.memberintelligence.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.BuildConfig

const val MI_VERSION_TAG = "MI-V5"
val BUILD_GIT_SHA: String = BuildConfig.GIT_SHA

/**
 * DEBUG-only runtime identity marker required by Stage 1 specification:
 * MI-V5
 * BUILD <short Git SHA>
 * DEBUG
 *
 * Automatically stripped/hidden in release builds via BuildConfig.DEBUG check.
 */
@Composable
fun DebugRuntimeIdentityBadge(
    modifier: Modifier = Modifier,
    gitSha: String = BUILD_GIT_SHA
) {
    if (!BuildConfig.DEBUG) return

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xD01E293B))
            .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$MI_VERSION_TAG • BUILD $gitSha • DEBUG",
            color = Color(0xFF22C55E),
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}
