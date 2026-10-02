package com.example.badnewgym.feature.memberintelligence.domain.model

/**
 * Server-side subscription/entitlement contract.
 *
 * Production implementations load this from the gym subscription/entitlement
 * record (for example Supabase). A server revocation always wins over the
 * gym owner's local widget arrangement.
 */
interface GymWidgetAccessRepository {
    suspend fun getAccess(gymId: String): VisitWidgetEntitlements
}

class StubGymWidgetAccessRepository : GymWidgetAccessRepository {
    override suspend fun getAccess(gymId: String): VisitWidgetEntitlements =
        VisitWidgetEntitlements(
            serverEnabledFeatures = FeatureKey.entries.toSet(),
            subscriptionPlan = "Full Access",
            subscriptionActive = true,
            paymentRequired = false
        )
}
