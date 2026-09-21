package com.example.badnewgym.feature.memberintelligence.presentation.components

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
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalCategory

@Composable
fun TrainerMenu(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    signals: List<IntelligenceSignal>,
    modifier: Modifier = Modifier
) {
    val trainerSignals = signals.filter { it.category == SignalCategory.TRAINER }
    val trainer = snapshot.trainer

    Column(
        modifier = modifier
            .padding(BADGymTheme.dimensions.outerPadding)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "PERSONAL TRAINER",
            style = BADGymTheme.typography.kpiValue,
            color = BADGymTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        if (trainer != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricTile(
                    label = "TRAINER",
                    value = trainer.trainerName,
                    modifier = Modifier.weight(1f)
                )
                MetricTile(
                    label = "SESSIONS LEFT",
                    value = "${trainer.sessionsTotal - trainer.sessionsUsed} / ${trainer.sessionsTotal}",
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Text(
                text = "No active PT plan",
                style = BADGymTheme.typography.label,
                color = BADGymTheme.colors.textSecondary
            )
        }

        if (trainerSignals.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            trainerSignals.forEach { signal ->
                IntelligenceSignalCard(signal = signal)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


