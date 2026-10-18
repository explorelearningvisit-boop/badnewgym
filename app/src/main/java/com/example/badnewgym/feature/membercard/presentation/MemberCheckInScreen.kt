package com.example.badnewgym.feature.membercard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

@Composable
fun MemberCheckInScreen(
    data: MemberCardData = remember { MemberCardData.sampleAmanTripathi() },
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MemberCardTokens.MintBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .drawBehind {
                // Subtle ambient emerald radial glow behind the card area
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x3000E676),
                            Color(0x1010B981),
                            Color.Transparent
                        ),
                        center = center,
                        radius = size.width * 0.75f
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f, fill = false))

            MemberCheckInCard(
                data = data,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f, fill = false))
        }
    }
}
