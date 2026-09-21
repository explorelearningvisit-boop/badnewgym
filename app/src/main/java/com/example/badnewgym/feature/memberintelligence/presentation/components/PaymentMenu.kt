package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.engine.PaymentCalculator
import com.example.badnewgym.feature.memberintelligence.domain.format.MoneyFormat
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PaymentMenu(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    signals: List<IntelligenceSignal>,
    modifier: Modifier = Modifier
) {
    val payment = snapshot.payment
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Column(
        modifier = modifier
            .padding(BADGymTheme.dimensions.outerPadding)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("PAYMENTS", style = BADGymTheme.typography.kpiValue, color = BADGymTheme.colors.textPrimary)
        Spacer(modifier = Modifier.height(12.dp))

        if (payment == null) {
            Text("No payment history available", color = BADGymTheme.colors.textSecondary, style = BADGymTheme.typography.label)
            return
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(BADGymTheme.shapes.button)
                .background(
                    if (PaymentCalculator.isOverdue(payment)) BADGymTheme.colors.dangerSoft
                    else BADGymTheme.colors.surfaceMuted
                )
                .padding(14.dp)
        ) {
            Text("Outstanding", color = BADGymTheme.colors.danger, style = BADGymTheme.typography.label)
            Text(MoneyFormat.inr(payment.totalOutstanding), style = BADGymTheme.typography.kpiValue, color = BADGymTheme.colors.danger)
            if (payment.overdueDays > 0) {
                Text("Overdue: ${payment.overdueDays} days", color = BADGymTheme.colors.danger, style = BADGymTheme.typography.timestamp)
            }
        }

        if (payment.breakdown.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            payment.breakdown.forEach { line ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(line.label, color = BADGymTheme.colors.textSecondary, style = BADGymTheme.typography.label)
                    Text(MoneyFormat.inr(line.amount), color = BADGymTheme.colors.textPrimary, style = BADGymTheme.typography.label)
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        payment.lastPaymentAmount?.let { last ->
            Spacer(modifier = Modifier.height(12.dp))
            MetricTile(
                label = "Last payment ${payment.lastPaymentMethod ?: ""}",
                value = "${MoneyFormat.inr(last)} Â· ${payment.lastPaymentDate?.let { dateFormat.format(Date(it)) } ?: ""}",
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (payment.history.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("History", color = BADGymTheme.colors.textSecondary, style = BADGymTheme.typography.label)
            payment.history.forEach { tx ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(dateFormat.format(Date(tx.occurredAt)), color = BADGymTheme.colors.textSecondary, style = BADGymTheme.typography.timestamp)
                    Text(MoneyFormat.inr(tx.amount), color = BADGymTheme.colors.textPrimary, style = BADGymTheme.typography.label)
                    Text(tx.method, color = BADGymTheme.colors.success, style = BADGymTheme.typography.timestamp)
                }
            }
        }

        val collect = signals.firstOrNull { it.action?.actionType == "COLLECT_PAYMENT" }?.action
            ?: SignalAction("Collect ${MoneyFormat.inr(payment.totalOutstanding)}", "COLLECT_PAYMENT")
        Spacer(modifier = Modifier.height(16.dp))
        ContextualCta(action = collect, onClick = {})
    }
}


