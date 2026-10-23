package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.domain.engine.DashboardMath
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot

/**
 * Design-verification harness for the compact member card.
 *
 * Renders a single component at a time so each can be captured in isolation. It is a
 * debug-only surface: it never ships in a release build and is only reachable by
 * launching MainActivity with `--ei gallery_index <n>`.
 *
 * Indices:
 * 0 full card · 1 brand strip · 2 identity + membership ring ·
 * 3 body stats · 4 payment & plan · 5 attendance & workout
 */
object CardGallery {
    val titles = listOf(
        "FULL CARD · 200x320 DESIGN AT 1.8x",
        "1 · BRAND STRIP",
        "2 · MEMBER IDENTITY + MEMBERSHIP RING",
        "3 · BODY STATS",
        "4 · PAYMENT & PLAN",
        "5 · ATTENDANCE & WORKOUT"
    )

    const val COUNT = 6
}

@Composable
fun CompactCardComponentGallery(
    snapshot: MemberSnapshot,
    index: Int,
    now: Long
) {
    val monthKey = remember(now) { DashboardMath.monthKey(now) }
    val title = CardGallery.titles.getOrElse(index) { "UNKNOWN COMPONENT" }

    Column(
        Modifier
            .fillMaxSize()
            .background(CardPalette.Page)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            color = CardPalette.Cobalt,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "BAD GYM · Member Intelligence component gallery",
            color = CardPalette.Muted,
            fontSize = 8.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(14.dp))

        // The card lays its sections out inside a 200dp-wide, 6dp-padded design
        // container that is then scaled to the screen. Reproducing both the container
        // and the scale keeps every isolated component identical to the live card.
        when (index) {
            // The full card supplies its own padding and scaling.
            0 -> CompactMemberIntelligenceCard(snapshot = snapshot, now = now)
            else -> CardScaleHost {
                Box(
                    Modifier
                        .width(DESIGN_WIDTH)
                        .background(CardPalette.Card)
                        .padding(horizontal = 6.dp, vertical = 5.dp)
                ) {
                when (index) {
                    1 -> CardBrandStrip(onOverflow = {})
                    2 -> MemberIdentityBlock(snapshot, now)
                    3 -> CardSectionBox {
                        CardSectionHeader(
                            icon = Icons.Rounded.Person,
                            iconTint = CardPalette.Mint,
                            iconPale = CardPalette.MintPale,
                            title = "Body Stats",
                            onClick = {}
                        )
                        BodyStatsRow(snapshot)
                    }
                    4 -> CardSectionBox {
                        CardSectionHeader(
                            icon = Icons.Rounded.AccountBalanceWallet,
                            iconTint = CardPalette.Coral,
                            iconPale = CardPalette.CoralPale,
                            title = "Payment & Plan",
                            onClick = {}
                        )
                        PaymentPlanRow(snapshot, now)
                    }
                    5 -> CardSectionBox {
                        CardSectionHeader(
                            icon = Icons.Rounded.FitnessCenter,
                            iconTint = CardPalette.Cobalt,
                            iconPale = CardPalette.CobaltPale,
                            title = "Attendance & Workout",
                            trailing = DashboardMath.monthLabelForKey(monthKey),
                            onClick = {}
                        )
                        ActivityBlock(snapshot, monthKey)
                    }
                }
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Box(Modifier.fillMaxWidth(0.7f).height(1.dp).background(CardPalette.Hairline))
        Spacer(Modifier.height(6.dp))
        Text(
            "index $index of ${CardGallery.COUNT - 1}",
            color = CardPalette.Muted,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
