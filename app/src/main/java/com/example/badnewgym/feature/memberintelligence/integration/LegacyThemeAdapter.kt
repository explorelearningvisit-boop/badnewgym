package com.example.badnewgym.feature.memberintelligence.integration

import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.colors.BADGymColors

fun interface MemberIntelligenceThemeAdapter {
    fun resolve(themeId: ThemeId): BADGymColors
}

object LegacyThemeAdapter : MemberIntelligenceThemeAdapter {
    override fun resolve(themeId: ThemeId): BADGymColors = themeId.colors()
}
