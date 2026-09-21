package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*

@Composable
fun MemberDashboard(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    signals: List<IntelligenceSignal>,
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    onMenuSelected: (MenuType) -> Unit,
    modifier: Modifier = Modifier,
    theme: ThemeId = ThemeId.NATURAL_FRESH,
    primarySignal: IntelligenceSignal? = null,
    secondarySignals: List<IntelligenceSignal> = emptyList(),
    cta: SignalAction? = null,
    onCta: (SignalAction) -> Unit = {}
) {
    MemberIntelligenceCard(
        snapshot = snapshot,
        currentEvent = currentEvent,
        signals = signals,
        primarySignal = primarySignal,
        secondarySignals = secondarySignals,
        cta = cta,
        theme = theme,
        menus = menus,
        activeMenu = activeMenu,
        onMenuSelected = onMenuSelected,
        onCtaClick = {
            if (cta != null) onCta(cta)
        },
        modifier = modifier
    )
}
