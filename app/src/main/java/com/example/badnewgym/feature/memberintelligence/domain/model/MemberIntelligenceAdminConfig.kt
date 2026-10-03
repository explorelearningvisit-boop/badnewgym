package com.example.badnewgym.feature.memberintelligence.domain.model

/**
 * V2 product-admin command-center state.
 *
 * Draft configuration is editable by the main BAD GYM software admin.
 * Published configuration is the active account/default configuration.
 * Runtime/member state must never be used to grant features.
 */
data class MemberIntelligenceAdminConfig(
    val configId: String = "member-intelligence-v2",
    val version: Long = 1L,
    val draft: VisitWidgetLayout = VisitWidgetLayout(),
    val published: VisitWidgetLayout = VisitWidgetLayout(),
    val allowedFeatures: Set<FeatureKey> = FeatureKey.entries.toSet(),
    val updatedAt: Long = 0L,
    val updatedBy: String? = null
) {
    fun beginDraft(): MemberIntelligenceAdminConfig =
        copy(draft = published)

    fun publish(now: Long, adminId: String): MemberIntelligenceAdminConfig =
        copy(
            version = version + 1L,
            published = draft,
            updatedAt = now,
            updatedBy = adminId
        )

    fun resetDraft(): MemberIntelligenceAdminConfig =
        copy(draft = published)

    fun canConfigure(feature: FeatureKey?): Boolean =
        feature == null || feature in allowedFeatures
}
