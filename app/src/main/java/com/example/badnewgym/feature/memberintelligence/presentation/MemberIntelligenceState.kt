package com.example.badnewgym.feature.memberintelligence.presentation

import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.GymLiveSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.GymOperationalEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.domain.model.SyncStatus

sealed class MemberIntelligenceUiState {
    data object Loading : MemberIntelligenceUiState()

    data class Success(
        val snapshot: MemberSnapshot,
        val currentEvent: MemberEvent,
        val menus: List<MemberMenu>,
        val signals: List<IntelligenceSignal>,
        val primarySignal: IntelligenceSignal?,
        val secondarySignals: List<IntelligenceSignal>,
        val cta: SignalAction?,
        val activeMenu: MenuType = MenuType.HOME,
        val themeId: ThemeId = ThemeId.NATURAL_FRESH,
        val syncStatus: SyncStatus = SyncStatus.SYNCED,
        val gymLive: GymLiveSnapshot = GymLiveSnapshot(),
        val gymEvents: List<GymOperationalEvent> = emptyList()
    ) : MemberIntelligenceUiState()

    data class Error(val message: String) : MemberIntelligenceUiState()
    data class Locked(val featureName: String, val planHint: String) : MemberIntelligenceUiState()
}

@Deprecated("Use MemberIntelligenceUiState")
typealias MemberIntelligenceState = MemberIntelligenceUiState
