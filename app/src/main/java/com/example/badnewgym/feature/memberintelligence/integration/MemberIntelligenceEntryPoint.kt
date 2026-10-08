package com.example.badnewgym.feature.memberintelligence.integration

import androidx.compose.runtime.Composable
import com.example.badnewgym.feature.memberintelligence.presentation.home.HomeViewModel
import com.example.badnewgym.feature.memberintelligence.presentation.home.MemberIntelligenceHome

object MemberIntelligenceEntryPoint {
    const val ROUTE = "member_intelligence/{memberId}"

    fun route(memberId: String, eventId: String? = null): String {
        val base = "member_intelligence/$memberId"
        return if (eventId.isNullOrBlank()) base else "$base?eventId=$eventId"
    }

    @Composable
    fun MemberIntelligenceRoute(
        viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
            factory = HomeViewModel.Factory()
        )
    ) {
        MemberIntelligenceHome(vm = viewModel)
    }
}
