package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens

@Composable
fun DetailedAnalyticsButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0x402563EB),
                ambientColor = Color(0x302563EB)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(MemberCardTokens.AnalyticsButtonGradient)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Analytics Bar Chart Icon
            Canvas(modifier = Modifier.size(18.dp)) {
                val w = size.width
                val h = size.height
                drawLine(color = Color.White, start = Offset(w * 0.25f, h * 0.85f), end = Offset(w * 0.25f, h * 0.45f), strokeWidth = 2.5.dp.toPx())
                drawLine(color = Color.White, start = Offset(w * 0.50f, h * 0.85f), end = Offset(w * 0.50f, h * 0.25f), strokeWidth = 2.5.dp.toPx())
                drawLine(color = Color.White, start = Offset(w * 0.75f, h * 0.85f), end = Offset(w * 0.75f, h * 0.10f), strokeWidth = 2.5.dp.toPx())
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "View Detailed Analytics  →",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
