package com.example.badnewgym.feature.memberintelligence.domain.model

/**
 * Visit Based Card v1.
 * Every information block is a widget. Visibility is resolved from the
 * current visit/event plus the server entitlement.
 */
enum class VisitWidgetId {
    MEMBER_IDENTITY, CURRENT_VISIT, PAYMENT_ALERT, ATTENDANCE, PLAN,
    GYM_TIME, WORKOUT, TRAINER, BODY_PROGRESS, SERVICES, OFFERS, HISTORY, INSIGHT
}

enum class VisitWidgetSize(val widthUnits: Int, val heightUnits: Int) {
    XS(1, 1), S(2, 1), M(2, 2), L(3, 2), XL(4, 2)
}

data class VisitWidgetDefinition(
    val id: VisitWidgetId,
    val title: String,
    val size: VisitWidgetSize,
    val feature: FeatureKey? = null
)

data class VisitWidgetLayout(
    val orderedIds: List<VisitWidgetId> = VisitWidgetId.entries.toList(),
    val locallyDisabled: Set<VisitWidgetId> = emptySet()
)

data class VisitWidgetEntitlements(
    val serverEnabledFeatures: Set<FeatureKey> = FeatureKey.entries.toSet(),
    val subscriptionPlan: String = "Full Access",
    val subscriptionActive: Boolean = true,
    val paymentRequired: Boolean = false
) {
    fun allows(feature: FeatureKey?): Boolean =
        feature == null || (subscriptionActive && feature in serverEnabledFeatures)
}

object VisitWidgetCatalog {
    val definitions = listOf(
        VisitWidgetDefinition(VisitWidgetId.MEMBER_IDENTITY, "Member", VisitWidgetSize.XL),
        VisitWidgetDefinition(VisitWidgetId.CURRENT_VISIT, "Current Visit", VisitWidgetSize.XL),
        VisitWidgetDefinition(VisitWidgetId.PAYMENT_ALERT, "Payment", VisitWidgetSize.L, FeatureKey.PAYMENT_AUTOMATION),
        VisitWidgetDefinition(VisitWidgetId.ATTENDANCE, "Attendance", VisitWidgetSize.M, FeatureKey.ADVANCED_INSIGHTS),
        VisitWidgetDefinition(VisitWidgetId.PLAN, "Plan", VisitWidgetSize.L),
        VisitWidgetDefinition(VisitWidgetId.GYM_TIME, "Gym Time", VisitWidgetSize.M, FeatureKey.ANALYTICS),
        VisitWidgetDefinition(VisitWidgetId.WORKOUT, "Workout", VisitWidgetSize.M, FeatureKey.ADVANCED_INSIGHTS),
        VisitWidgetDefinition(VisitWidgetId.TRAINER, "Trainer / PT", VisitWidgetSize.M, FeatureKey.TRAINER_MANAGEMENT),
        VisitWidgetDefinition(VisitWidgetId.BODY_PROGRESS, "Body Progress", VisitWidgetSize.M, FeatureKey.ADVANCED_INSIGHTS),
        VisitWidgetDefinition(VisitWidgetId.SERVICES, "Services", VisitWidgetSize.M),
        VisitWidgetDefinition(VisitWidgetId.OFFERS, "Offers", VisitWidgetSize.M, FeatureKey.MARKETING),
        VisitWidgetDefinition(VisitWidgetId.HISTORY, "History", VisitWidgetSize.S, FeatureKey.ANALYTICS),
        VisitWidgetDefinition(VisitWidgetId.INSIGHT, "Insight", VisitWidgetSize.S, FeatureKey.ADVANCED_INSIGHTS)
    )

    fun definition(id: VisitWidgetId) = definitions.first { it.id == id }
}

object VisitWidgetConditionResolver {
    fun visible(
        id: VisitWidgetId,
        event: MemberEvent,
        snapshot: MemberSnapshot,
        entitlements: VisitWidgetEntitlements,
        layout: VisitWidgetLayout
    ): Boolean {
        val definition = VisitWidgetCatalog.definition(id)
        if (id in layout.locallyDisabled || id !in layout.orderedIds) return false
        if (!entitlements.allows(definition.feature)) return false

        val paymentDue = (snapshot.payment?.totalOutstanding ?: 0.0) > 0.0
        val hasTrainer = snapshot.trainer != null
        val hasWorkout = snapshot.workout != null
        val hasServices = snapshot.services.orEmpty().isNotEmpty()

        val base = when (event.eventType) {
            EventType.CHECK_IN, EventType.MEMBERSHIP_STARTED, EventType.NEW_MEMBER ->
                setOf(VisitWidgetId.MEMBER_IDENTITY, VisitWidgetId.CURRENT_VISIT,
                    VisitWidgetId.PAYMENT_ALERT, VisitWidgetId.ATTENDANCE, VisitWidgetId.PLAN,
                    VisitWidgetId.TRAINER, VisitWidgetId.WORKOUT, VisitWidgetId.SERVICES,
                    VisitWidgetId.OFFERS, VisitWidgetId.INSIGHT)

            EventType.CHECK_OUT ->
                setOf(VisitWidgetId.MEMBER_IDENTITY, VisitWidgetId.CURRENT_VISIT,
                    VisitWidgetId.PAYMENT_ALERT, VisitWidgetId.ATTENDANCE, VisitWidgetId.PLAN,
                    VisitWidgetId.GYM_TIME, VisitWidgetId.WORKOUT, VisitWidgetId.BODY_PROGRESS,
                    VisitWidgetId.TRAINER, VisitWidgetId.HISTORY, VisitWidgetId.INSIGHT)

            EventType.PAYMENT, EventType.PAYMENT_SUCCESS, EventType.PAYMENT_DUE,
            EventType.PAYMENT_OVERDUE, EventType.PAYMENT_PARTIAL, EventType.REFUND ->
                setOf(VisitWidgetId.MEMBER_IDENTITY, VisitWidgetId.PAYMENT_ALERT,
                    VisitWidgetId.PLAN, VisitWidgetId.HISTORY, VisitWidgetId.INSIGHT)

            EventType.WALK_IN, EventType.TRIAL_STARTED, EventType.TRIAL_CONVERTED,
            EventType.TRIAL_EXPIRED ->
                setOf(VisitWidgetId.MEMBER_IDENTITY, VisitWidgetId.CURRENT_VISIT,
                    VisitWidgetId.PLAN, VisitWidgetId.OFFERS, VisitWidgetId.ATTENDANCE,
                    VisitWidgetId.HISTORY)

            EventType.WORKOUT_STARTED, EventType.WORKOUT_COMPLETED,
            EventType.WORKOUT_SKIPPED, EventType.PR_ACHIEVED,
            EventType.BODY_MEASUREMENT_UPDATED ->
                setOf(VisitWidgetId.MEMBER_IDENTITY, VisitWidgetId.CURRENT_VISIT,
                    VisitWidgetId.WORKOUT, VisitWidgetId.BODY_PROGRESS,
                    VisitWidgetId.ATTENDANCE, VisitWidgetId.TRAINER,
                    VisitWidgetId.PLAN, VisitWidgetId.HISTORY)

            EventType.TRAINER_SESSION, EventType.TRAINER_SESSION_SCHEDULED,
            EventType.TRAINER_SESSION_STARTED, EventType.TRAINER_SESSION_COMPLETED,
            EventType.TRAINER_SESSION_MISSED, EventType.TRAINER_SESSION_CANCELLED,
            EventType.TRAINER_ASSIGNED ->
                setOf(VisitWidgetId.MEMBER_IDENTITY, VisitWidgetId.CURRENT_VISIT,
                    VisitWidgetId.TRAINER, VisitWidgetId.WORKOUT, VisitWidgetId.PLAN,
                    VisitWidgetId.ATTENDANCE, VisitWidgetId.HISTORY)

            EventType.SERVICE_PURCHASE, EventType.SERVICE_ACTIVATED,
            EventType.SERVICE_DEACTIVATED, EventType.SERVICE_BOOKED,
            EventType.SERVICE_USED, EventType.SERVICE_EXPIRED,
            EventType.SERVICE_ISSUE, EventType.SUPPLEMENT_PURCHASE, EventType.NUTRITION ->
                setOf(VisitWidgetId.MEMBER_IDENTITY, VisitWidgetId.CURRENT_VISIT,
                    VisitWidgetId.SERVICES, VisitWidgetId.PAYMENT_ALERT,
                    VisitWidgetId.PLAN, VisitWidgetId.HISTORY)

            else ->
                setOf(VisitWidgetId.MEMBER_IDENTITY, VisitWidgetId.CURRENT_VISIT,
                    VisitWidgetId.PAYMENT_ALERT, VisitWidgetId.PLAN,
                    VisitWidgetId.HISTORY, VisitWidgetId.INSIGHT)
        }

        return id in base &&
            when (id) {
                VisitWidgetId.PAYMENT_ALERT -> paymentDue
                VisitWidgetId.TRAINER -> hasTrainer
                VisitWidgetId.WORKOUT -> hasWorkout
                VisitWidgetId.SERVICES -> hasServices
                else -> true
            }
    }

    fun resolve(
        event: MemberEvent,
        snapshot: MemberSnapshot,
        entitlements: VisitWidgetEntitlements,
        layout: VisitWidgetLayout
    ): List<VisitWidgetDefinition> =
        layout.orderedIds.map { VisitWidgetCatalog.definition(it) }
            .filter { visible(it.id, event, snapshot, entitlements, layout) }
}
