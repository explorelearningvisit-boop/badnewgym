package com.example.badnewgym.feature.memberintelligence.domain.model

data class GymConfiguration(
    val gymId: String,
    val gymName: String,
    val timezone: String = "Asia/Kolkata",
    val attendanceTargetDefault: Int = 26
)

data class FeatureEntitlement(
    val features: Set<FeatureKey> = FeatureKey.entries.toSet()
) {
    fun allows(key: FeatureKey): Boolean = key in features
}

data class GymSubscription(
    val planName: String,
    val entitlements: FeatureEntitlement
)
