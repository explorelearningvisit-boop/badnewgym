package com.example.badnewgym.feature.memberintelligence.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.domain.engine.MemberIntelligenceEngine
import com.example.badnewgym.feature.memberintelligence.domain.engine.MenuAvailabilityResolver
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberDashboard
import com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios

@Preview(name = "360x500 light", widthDp = 360, heightDp = 640)
@Composable
fun OverdueMemberPreview() {
    val (snapshot, event) = MemberScenarios.overdueActiveMember()
    val result = MemberIntelligenceEngine().evaluate(snapshot, event, event.occurredAt)
    val theme = ThemeId.NATURAL_FRESH
    BADGymTheme(
        colors = theme.colors(),
        shapes = theme.shapes(),
        motion = theme.motion(),
        elevation = theme.elevation()
    ) {
        MemberDashboard(
            snapshot = snapshot,
            currentEvent = event,
            signals = result.signals,
            menus = MenuAvailabilityResolver.resolve(snapshot, result.signals),
            activeMenu = com.example.badnewgym.feature.memberintelligence.domain.model.MenuType.HOME,
            onMenuSelected = {},
            primarySignal = result.primary,
            secondarySignals = result.secondary,
            cta = result.cta
        )
    }
}
