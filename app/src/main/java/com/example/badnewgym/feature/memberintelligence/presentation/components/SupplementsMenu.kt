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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SupplementsMenu(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    signals: List<IntelligenceSignal>,
    modifier: Modifier = Modifier
) {
    val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    val lastPurchaseName = snapshot.supplements?.lastPurchaseName ?: "None"
    val lastDateStr = snapshot.supplements?.lastPurchaseDate?.let { formatter.format(Date(it)) } ?: "--"

    Column(
        modifier = modifier.padding(BADGymTheme.dimensions.outerPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(style = BADGymTheme.typography.kpiValue, color = BADGymTheme.colors.textPrimary, text = "Supplements")
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            MetricTile(
                label = "Last Purchase",
                value = lastPurchaseName,
                modifier = Modifier.weight(1f)
            )
            MetricTile(
                label = "Date",
                value = lastDateStr,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


