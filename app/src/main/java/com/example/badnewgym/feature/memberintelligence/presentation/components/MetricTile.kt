package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme

@Composable
fun MetricTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(BADGymTheme.shapes.button)
            .border(1.dp, BADGymTheme.colors.border, BADGymTheme.shapes.button)
            .padding(16.dp)
    ) {
        Text(
            text = value,
            style = BADGymTheme.typography.kpiValue,
            color = BADGymTheme.colors.textPrimary
        )
        Text(
            text = label.uppercase(),
            style = BADGymTheme.typography.label,
            color = BADGymTheme.colors.textSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
