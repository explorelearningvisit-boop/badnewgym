package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot

@Composable
fun InsightMenu(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    signals: List<IntelligenceSignal>,
    modifier: Modifier = Modifier
) {
    val topIssue = snapshot.issues.firstOrNull()?.description ?: "No active issues"
    val severity = snapshot.issues.firstOrNull()?.severity?.name ?: "--"

    Column(
        modifier = modifier.padding(BADGymTheme.dimensions.outerPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(style = BADGymTheme.typography.kpiValue, color = BADGymTheme.colors.textPrimary, text = "Actionable Insights")
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            MetricTile(
                label = "Primary Issue",
                value = topIssue,
                modifier = Modifier.weight(1f)
            )
            MetricTile(
                label = "Severity",
                value = severity,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


