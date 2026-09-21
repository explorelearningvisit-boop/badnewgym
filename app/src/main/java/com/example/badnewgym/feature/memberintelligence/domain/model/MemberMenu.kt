package com.example.badnewgym.feature.memberintelligence.domain.model

data class MemberMenu(
    val id: MenuType,
    val label: String,
    val priority: Int,
    val isVisible: Boolean,
    val isEnabled: Boolean,
    val isLocked: Boolean,
    val badgeCount: Int = 0,
    val hasAlert: Boolean = false,
    val severity: com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority? = null,
    val summary: String? = null
)

enum class MenuType(val defaultLabel: String) {
    HOME("Home"),
    ATTENDANCE("Attend"),
    PLAN("Plan"),
    PAYMENT("Pay"),
    TRAINER("Trainer"),
    WORKOUT("Workout"),
    SUPPLEMENTS("Supplements"),
    NUTRITION("Nutrition"),
    SERVICES("Services"),
    HISTORY("History"),
    INSIGHT("Insight")
}
