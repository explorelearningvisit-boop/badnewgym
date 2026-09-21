package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.format.MoneyFormat
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalCategory

object ContextualCtaEngine {
    fun resolve(primary: IntelligenceSignal?, snapshot: MemberSnapshot): SignalAction? {
        if (primary?.action != null) return primary.action
        return when (primary?.category) {
            SignalCategory.PAYMENT -> SignalAction(
                label = "Collect ${MoneyFormat.inr(PaymentCalculator.outstanding(snapshot.payment))}",
                actionType = "COLLECT_PAYMENT"
            )
            SignalCategory.MEMBERSHIP -> SignalAction("Renew Membership", "RENEW_PLAN")
            SignalCategory.ATTENDANCE -> SignalAction("Recover Attendance", "RECOVER_ATTENDANCE")
            SignalCategory.TRAINER -> SignalAction("Open Trainer Session", "VIEW_TRAINER")
            SignalCategory.COMPLAINT -> SignalAction("Resolve Issue", "RESOLVE_ISSUE")
            else -> null
        }
    }
}
