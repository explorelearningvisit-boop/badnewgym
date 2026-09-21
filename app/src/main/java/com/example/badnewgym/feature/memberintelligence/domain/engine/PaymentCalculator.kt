package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSummary

object PaymentCalculator {
    fun resolve(payment: PaymentSummary?): PaymentLifecycle {
        if (payment == null) return PaymentLifecycle.PAID
        payment.lifecycle?.let { return it }
        return when {
            payment.totalOutstanding > 0 && payment.overdueDays > 0 -> PaymentLifecycle.OVERDUE
            payment.totalOutstanding > 0 -> PaymentLifecycle.DUE
            else -> PaymentLifecycle.PAID
        }
    }

    fun isOverdue(payment: PaymentSummary?): Boolean = resolve(payment) == PaymentLifecycle.OVERDUE

    fun outstanding(payment: PaymentSummary?): Double = payment?.totalOutstanding ?: 0.0
}
