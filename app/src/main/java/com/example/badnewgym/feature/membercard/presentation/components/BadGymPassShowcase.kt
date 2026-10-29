package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.BadGymPassTier
import com.example.badnewgym.feature.membercard.domain.model.MemberCardType
import com.example.badnewgym.feature.membercard.domain.model.PaymentStatus

/**
 * Interactive Showcase Carousel for all 9 BAD GYM Member Passes:
 * 1. Emerald Green
 * 2. Ocean Blue
 * 3. Royal Purple
 * 4. Golden Amber
 * 5. Crimson Alert (Overdue)
 * 6. Cyan Aqua
 * 7. White Mint Glass
 * 8. White Ocean Glass
 * 9. White Lavender Glass
 */
@Composable
fun BadGymPassShowcase(
    passes: List<BadGymMemberPass> = remember { BadGymMemberPass.getAll9Passes() },
    selectedIndex: Int = 0,
    showTierSelector: Boolean = false,
    cardType: MemberCardType = MemberCardType.MEMBER_PASS,
    paymentStatus: PaymentStatus? = null,
    onPassSelected: (Int) -> Unit = {},
    onAvatarClick: (BadGymMemberPass) -> Unit = {},
    onCardTypeCycle: () -> Unit = {},
    onPaymentStatusCycle: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentIndex by remember(selectedIndex) { mutableIntStateOf(selectedIndex) }
    val currentPass = passes.getOrElse(currentIndex) { passes.first() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (showTierSelector) {
            // Horizontal Tier Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                passes.forEachIndexed { index, pass ->
                    val isSelected = index == currentIndex
                    val tier = pass.tier
                    val chipColor = when (tier) {
                        BadGymPassTier.SPARK -> Color(0xFF059669)
                        BadGymPassTier.POWER -> Color(0xFF0284C7)
                        BadGymPassTier.PLATINUM -> Color(0xFF7C3AED)
                        BadGymPassTier.GOLD -> Color(0xFFD97706)
                        BadGymPassTier.BASIC -> Color(0xFFE11D48)
                        BadGymPassTier.CYAN -> Color(0xFF0D9488)
                        BadGymPassTier.WHITE_MINT -> Color(0xFF10B981)
                        BadGymPassTier.WHITE_OCEAN -> Color(0xFF0284C7)
                        BadGymPassTier.WHITE_LAVENDER -> Color(0xFF9333EA)
                        BadGymPassTier.SILVER -> Color(0xFF64748B)
                        BadGymPassTier.DIAMOND -> Color(0xFF0284C7)
                        BadGymPassTier.ELITE -> Color(0xFFEAB308)
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
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = when (tier) {
                                    BadGymPassTier.SPARK -> "🌱"
                                    BadGymPassTier.POWER -> "⚡"
                                    BadGymPassTier.PLATINUM -> "👑"
                                    BadGymPassTier.GOLD -> "👑"
                                    BadGymPassTier.BASIC -> "⏱️"
                                    BadGymPassTier.CYAN -> "💎"
                                    BadGymPassTier.WHITE_MINT -> "🌱"
                                    BadGymPassTier.WHITE_OCEAN -> "⚡"
                                    BadGymPassTier.WHITE_LAVENDER -> "👑"
                                    BadGymPassTier.SILVER -> "⭐"
                                    BadGymPassTier.DIAMOND -> "💎"
                                    BadGymPassTier.ELITE -> "👑"
                                },
                                fontSize = 11.5.sp
                            )
                            Text(
                                text = tier.title,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF334155)
                            )
                        }
                    }
                }
            }
        }

        // Active Member Pass Card (Luxury Crisp White Card Matching Reference)
        BadGymMemberPassCard(
            pass = currentPass,
            cardType = cardType,
            paymentStatus = paymentStatus,
            onAvatarClick = { onAvatarClick(currentPass) },
            onChevronClick = {
                // Cycle to next pass
                val nextIndex = (currentIndex + 1) % passes.size
                currentIndex = nextIndex
                onPassSelected(nextIndex)
            },
            onCardTypeCycle = onCardTypeCycle,
            onPaymentStatusCycle = onPaymentStatusCycle
        )
    }
}
