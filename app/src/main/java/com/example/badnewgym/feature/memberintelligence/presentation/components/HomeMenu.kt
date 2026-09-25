package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.AttendanceRing
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.DueBlock
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.MetricPanel
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.PromoBanner
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.ShortcutTriple
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.attendancePercent

@Composable
fun HomeMenu(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal? = signals.firstOrNull(),
    secondarySignals: List<IntelligenceSignal> = signals.drop(1).take(3),
    cta: SignalAction? = primarySignal?.action,
    onCta: (SignalAction) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(BADGymTheme.dimensions.outerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val insightSignal = primarySignal ?: IntelligenceSignal(
            id = "insight_demo",
            title = "Payment Follow-up",
            subtitle = "Active member with outstanding dues.",
            priority = SignalPriority.P1_ACTION_REQUIRED,
            category = com.example.badnewgym.feature.memberintelligence.domain.model.SignalCategory.PAYMENT,
            value = null,
            sourceMenu = null,
            action = null,
            timestamp = 0L,
            validUntil = null
        )

        // Row 1: Metrics (Attendance, Payment, Services)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricPanel(title = "Attendance", modifier = Modifier.weight(1f).height(110.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AttendanceRing(percent = snapshot.attendancePercent(), size = 48.dp, stroke = 4.dp)
                }
            }
            
            DueBlock(
                amount = snapshot.payment?.totalOutstanding ?: 0.0,
                overdueDays = snapshot.payment?.overdueDays ?: 0,
                modifier = Modifier.weight(1f).height(110.dp),
                compact = true
            )

            MetricPanel(title = "Plan", modifier = Modifier.weight(1f).height(110.dp)) {
                val daysLeft = snapshot.membership?.daysRemaining ?: 0
                androidx.compose.material3.Text(
                    text = "$daysLeft\nDays",
                    color = BADGymTheme.colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                androidx.compose.material3.Text(
                    text = snapshot.membership?.planName ?: "None",
                    color = BADGymTheme.colors.textSecondary,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }

        // Row 2: Shortcuts
        ShortcutTriple(
            trainer = snapshot.trainer?.trainerName ?: "Unassigned",
            workout = snapshot.workout?.currentRoutine ?: "Rest Day",
            services = "${snapshot.services?.count { it.isActive } ?: 0} Active"
        )

        // Row 3: Primary Intelligence Signal
        IntelligenceSignalCard(
            signal = insightSignal,
            emphasized = false
        )

        // Row 4: Primary CTA
        ContextualCta(action = cta, onClick = { cta?.let(onCta) })
    }
}

