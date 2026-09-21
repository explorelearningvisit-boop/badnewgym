package com.example.badnewgym.feature.memberintelligence.integration

object FeatureFlagManager {
    const val MEMBER_INTELLIGENCE_V2 = "memberIntelligenceV2"

    var memberIntelligenceV2Enabled: Boolean = true

    fun isV2Enabled(): Boolean = memberIntelligenceV2Enabled
}
