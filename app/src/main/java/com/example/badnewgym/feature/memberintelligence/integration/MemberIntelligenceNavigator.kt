package com.example.badnewgym.feature.memberintelligence.integration

import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction

fun interface MemberIntelligenceNavigator {
    fun onAction(action: SignalAction)

    companion object {
        val NoOp = MemberIntelligenceNavigator { }
    }
}
