package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun PlaceholderMenu(
    title: String,
    currentEvent: MemberEvent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(BADGymTheme.dimensions.outerPadding)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title.uppercase(),
            style = BADGymTheme.typography.kpiValue,
            color = BADGymTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Module under construction",
            style = BADGymTheme.typography.label,
            color = BADGymTheme.colors.textSecondary
        )
    }
}


