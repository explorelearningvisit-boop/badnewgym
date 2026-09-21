package com.example.badnewgym.feature.memberintelligence.domain.model

data class PaymentTransaction(
    val id: String,
    val amount: Double,
    val occurredAt: Long,
    val method: String,
    val purpose: String? = null,
    val invoiceId: String? = null
)

data class PaymentBreakdownLine(
    val label: String,
    val amount: Double
)

data class PromotionSlot(
    val id: String,
    val title: String,
    val subtitle: String?,
    val badge: String?,
    val imageUrl: String?,
    val priority: Int = 0
)
