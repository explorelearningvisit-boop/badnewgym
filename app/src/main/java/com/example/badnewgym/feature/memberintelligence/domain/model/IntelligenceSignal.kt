package com.example.badnewgym.feature.memberintelligence.domain.model

data class IntelligenceSignal(
    val id: String,
    val category: SignalCategory,
    val priority: SignalPriority,
    val title: String,
    val subtitle: String?,
    val value: String?,
    val sourceMenu: MenuType?,
    val action: SignalAction?,
    val timestamp: Long,
    val validUntil: Long?,
    val metadata: Map<String, String> = emptyMap(),
    val evidence: List<String> = emptyList()
)

enum class SignalPriority {
    P0_CRITICAL,
    P1_ACTION_REQUIRED,
    P2_IMPORTANT,
    P3_BACKGROUND
}

enum class SignalCategory {
    PAYMENT,
    MEMBERSHIP,
    ATTENDANCE,
    TRAINER,
    SAFETY,
    ENGAGEMENT,
    GENERAL,
    COMPLAINT,
    UPSELL
}

data class SignalAction(
    val label: String,
    val actionType: String,
    val payload: String? = null
)

data class IntelligenceResult(
    val signals: List<IntelligenceSignal>,
    val primary: IntelligenceSignal?,
    val secondary: List<IntelligenceSignal>,
    val cta: SignalAction?
)
