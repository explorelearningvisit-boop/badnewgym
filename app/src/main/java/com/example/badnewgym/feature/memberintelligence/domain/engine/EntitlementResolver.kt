package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.FeatureEntitlement
import com.example.badnewgym.feature.memberintelligence.domain.model.FeatureKey

object EntitlementResolver {
    fun allows(entitlements: FeatureEntitlement, key: FeatureKey): Boolean = entitlements.allows(key)

    fun default(): FeatureEntitlement = FeatureEntitlement()
}
