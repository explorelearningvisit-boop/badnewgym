package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.BadGymPassTier

/**
 * Interactive Showcase Carousel for all 8 BAD GYM Member Passes:
 * Spark, Power, Silver, Gold, Diamond, Platinum, Elite, Basic (Overdue).
 */
@Composable
fun BadGymPassShowcase(
    passes: List<BadGymMemberPass> = remember { BadGymMemberPass.getAll8Passes() },
    selectedIndex: Int = 0,
    onPassSelected: (Int) -> Unit = {},
    onAvatarClick: (BadGymMemberPass) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableIntStateOf(selectedIndex) }
    val currentPass = passes.getOrElse(currentIndex) { passes.first() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Horizontal Tier Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            passes.forEachIndexed { index, pass ->
                val isSelected = index == currentIndex
                val tier = pass.tier
                val chipColor = when (tier) {
                    BadGymPassTier.SPARK -> Color(0xFF10B981)
                    BadGymPassTier.POWER -> Color(0xFF0284C7)
                    BadGymPassTier.SILVER -> Color(0xFF64748B)
                    BadGymPassTier.GOLD -> Color(0xFFF59E0B)
                    BadGymPassTier.DIAMOND -> Color(0xFF0284C7)
                    BadGymPassTier.PLATINUM -> Color(0xFF9333EA)
                    BadGymPassTier.ELITE -> Color(0xFFEAB308)
                    BadGymPassTier.BASIC -> Color(0xFFEF4444)
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = if (isSelected) chipColor else Color.White.copy(alpha = 0.9f),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) chipColor else Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier.clickable {
                        currentIndex = index
                        onPassSelected(index)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = when (tier) {
                                BadGymPassTier.SPARK -> "🌱"
                                BadGymPassTier.POWER -> "⚡"
                                BadGymPassTier.SILVER -> "⭐"
                                BadGymPassTier.GOLD -> "👑"
                                BadGymPassTier.DIAMOND -> "💎"
                                BadGymPassTier.PLATINUM -> "👑"
                                BadGymPassTier.ELITE -> "👑"
                                BadGymPassTier.BASIC -> "⏱️"
                            },
                            fontSize = 10.sp
                        )
                        Text(
                            text = tier.title,
                            fontSize = 10.5.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF334155)
                        )
                    }
                }
            }
        }

        // Active Member Pass Card
        BadGymMemberPassCard(
            pass = currentPass,
            onAvatarClick = { onAvatarClick(currentPass) },
            onChevronClick = {
                // Cycle to next pass
                val nextIndex = (currentIndex + 1) % passes.size
                currentIndex = nextIndex
                onPassSelected(nextIndex)
            }
        )
    }
}

