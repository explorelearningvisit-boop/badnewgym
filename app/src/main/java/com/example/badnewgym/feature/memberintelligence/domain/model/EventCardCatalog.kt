package com.example.badnewgym.feature.memberintelligence.domain.model

/**
 * BAD GYM Stage 7.8 — event-card variety registry.
 *
 * Important: card variety means semantic presentation variety, not 50 unrelated
 * Composables. The renderer should select one reusable archetype and configure
 * its evidence, story, state, media and actions from this registry.
 */
enum class EventCardKind {
    HOME_DECISION,
    CHECK_IN,
    CHECK_OUT,
    WALK_IN,
    TRIAL,
    FREEZE,
    BAN,
    MEMBERSHIP,
    PAYMENT,
    TRAINER,
    WORKOUT,
    SERVICE,
    FACILITY_OPERATION,
    ISSUE_RESOLUTION,
    COMMUNICATION,
    INSIGHT
}

enum class CardMediaMode {
    NONE,
    MEMBER_AVATAR,
    MEMBER_PHOTO,
    SERVICE_IMAGE,
    EQUIPMENT_IMAGE,
    STAFF_AVATAR,
    ATTACHMENT_THUMBNAIL,
    PHOTO_TIMELINE,
    PHOTO_OR_VIDEO_STATUS
}

enum class CardDecision {
    NONE,
    OPEN_ATTENDANCE,
    START_TRIAL,
    CONVERT_TRIAL,
    EXTEND_TRIAL,
    RECORD_PAYMENT,
    COLLECT_PAYMENT,
    RENEW_PLAN,
    UNFREEZE,
    VIEW_BAN_REASON,
    LIFT_BAN,
    OPEN_TRAINER,
    START_SESSION,
    OPEN_WORKOUT,
    OPEN_SERVICE,
    RESOLVE_ISSUE,
    ASSIGN_STAFF,
    OPEN_HISTORY,
    OPEN_DETAIL
}

data class EventCardSpec(
    val eventType: EventType,
    val kind: EventCardKind,
    val defaultMenu: MenuType,
    val priority: SignalPriority,
    val title: String,
    val storyLabel: String,
    val primaryDecision: CardDecision,
    val mediaMode: CardMediaMode = CardMediaMode.NONE,
    val showTimeline: Boolean = true,
    val showNowPastFuture: Boolean = true,
    val evidenceKeys: List<String> = emptyList(),
    val actionLabel: String? = null
)

/**
 * One registry entry per event variant. UI stays reusable while the information
 * hierarchy remains event-specific.
 */
object EventCardCatalog {
    val all: List<EventCardSpec> = listOf(
        // ACCESS / JOURNEY
        spec(EventType.CHECK_IN, EventCardKind.CHECK_IN, MenuType.ATTENDANCE, SignalPriority.P3_BACKGROUND, "Checked in", "ACCESS", CardDecision.OPEN_ATTENDANCE, CardMediaMode.MEMBER_AVATAR, "Open attendance"),
        spec(EventType.CHECK_OUT, EventCardKind.CHECK_OUT, MenuType.ATTENDANCE, SignalPriority.P3_BACKGROUND, "Checked out", "ACCESS", CardDecision.OPEN_HISTORY, CardMediaMode.MEMBER_AVATAR, "View session"),
        spec(EventType.WALK_IN, EventCardKind.WALK_IN, MenuType.ATTENDANCE, SignalPriority.P2_IMPORTANT, "Walk-in visit", "LEAD → VISIT", CardDecision.START_TRIAL, CardMediaMode.MEMBER_PHOTO, "Start trial"),
        spec(EventType.TRIAL_STARTED, EventCardKind.TRIAL, MenuType.PLAN, SignalPriority.P2_IMPORTANT, "Trial started", "WALK-IN → TRIAL", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_PHOTO, "View trial"),
        spec(EventType.TRIAL_CONVERTED, EventCardKind.TRIAL, MenuType.PLAN, SignalPriority.P2_IMPORTANT, "Trial converted", "TRIAL → MEMBER", CardDecision.RENEW_PLAN, CardMediaMode.MEMBER_PHOTO, "Activate plan"),
        spec(EventType.TRIAL_EXPIRED, EventCardKind.TRIAL, MenuType.PLAN, SignalPriority.P1_ACTION_REQUIRED, "Trial expired", "TRIAL → EXPIRED", CardDecision.CONVERT_TRIAL, CardMediaMode.MEMBER_PHOTO, "Convert now"),
        spec(EventType.FREEZE_STARTED, EventCardKind.FREEZE, MenuType.PLAN, SignalPriority.P2_IMPORTANT, "Membership frozen", "ACTIVE → FROZEN", CardDecision.UNFREEZE, CardMediaMode.MEMBER_AVATAR, "View freeze"),
        spec(EventType.FREEZE_ENDED, EventCardKind.FREEZE, MenuType.PLAN, SignalPriority.P3_BACKGROUND, "Membership active", "FROZEN → ACTIVE", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_AVATAR, "View plan"),
        spec(EventType.BANNED, EventCardKind.BAN, MenuType.PLAN, SignalPriority.P0_CRITICAL, "Member access banned", "ACTIVE → BANNED", CardDecision.VIEW_BAN_REASON, CardMediaMode.MEMBER_PHOTO, "View reason"),
        spec(EventType.BAN_LIFTED, EventCardKind.BAN, MenuType.PLAN, SignalPriority.P2_IMPORTANT, "Ban lifted", "BANNED → ACTIVE", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_PHOTO, "View access"),

        // MEMBERSHIP
        spec(EventType.MEMBER_CREATED, EventCardKind.MEMBERSHIP, MenuType.PLAN, SignalPriority.P2_IMPORTANT, "New member", "LEAD → MEMBER", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_PHOTO, "Open member"),
        spec(EventType.MEMBER_UPDATED, EventCardKind.MEMBERSHIP, MenuType.MORE, SignalPriority.P3_BACKGROUND, "Member updated", "PROFILE", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_AVATAR, "View profile"),
        spec(EventType.MEMBERSHIP_STARTED, EventCardKind.MEMBERSHIP, MenuType.PLAN, SignalPriority.P2_IMPORTANT, "Membership started", "PLAN START", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_AVATAR, "View plan"),
        spec(EventType.MEMBERSHIP_RENEWED, EventCardKind.MEMBERSHIP, MenuType.PLAN, SignalPriority.P2_IMPORTANT, "Membership renewed", "RENEWED", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_AVATAR, "View renewal"),
        spec(EventType.MEMBERSHIP_EXPIRED, EventCardKind.MEMBERSHIP, MenuType.PLAN, SignalPriority.P1_ACTION_REQUIRED, "Membership expired", "ACTIVE → EXPIRED", CardDecision.RENEW_PLAN, CardMediaMode.MEMBER_PHOTO, "Renew plan"),
        spec(EventType.MEMBERSHIP_CANCELLED, EventCardKind.MEMBERSHIP, MenuType.PLAN, SignalPriority.P1_ACTION_REQUIRED, "Membership cancelled", "ACTIVE → CANCELLED", CardDecision.OPEN_HISTORY, CardMediaMode.MEMBER_AVATAR, "View history"),

        // FINANCE
        spec(EventType.PAYMENT_SUCCESS, EventCardKind.PAYMENT, MenuType.PAYMENT, SignalPriority.P3_BACKGROUND, "Payment received", "PAYMENT", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_AVATAR, "View payment"),
        spec(EventType.PAYMENT_FAILED, EventCardKind.PAYMENT, MenuType.PAYMENT, SignalPriority.P1_ACTION_REQUIRED, "Payment failed", "PAYMENT → FAILED", CardDecision.COLLECT_PAYMENT, CardMediaMode.MEMBER_AVATAR, "Collect payment"),
        spec(EventType.PAYMENT_DUE, EventCardKind.PAYMENT, MenuType.PAYMENT, SignalPriority.P1_ACTION_REQUIRED, "Payment due", "DUE", CardDecision.COLLECT_PAYMENT, CardMediaMode.MEMBER_AVATAR, "Collect payment"),
        spec(EventType.PAYMENT_OVERDUE, EventCardKind.PAYMENT, MenuType.PAYMENT, SignalPriority.P1_ACTION_REQUIRED, "Payment overdue", "DUE → OVERDUE", CardDecision.COLLECT_PAYMENT, CardMediaMode.MEMBER_PHOTO, "Collect now"),
        spec(EventType.PAYMENT_PARTIAL, EventCardKind.PAYMENT, MenuType.PAYMENT, SignalPriority.P2_IMPORTANT, "Partial payment", "PARTIAL", CardDecision.COLLECT_PAYMENT, CardMediaMode.MEMBER_AVATAR, "Collect balance"),
        spec(EventType.REFUND, EventCardKind.PAYMENT, MenuType.PAYMENT, SignalPriority.P2_IMPORTANT, "Refund recorded", "REFUND", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_AVATAR, "View refund"),
        spec(EventType.INVOICE_CREATED, EventCardKind.PAYMENT, MenuType.PAYMENT, SignalPriority.P3_BACKGROUND, "Invoice created", "INVOICE", CardDecision.OPEN_DETAIL, CardMediaMode.NONE, "View invoice"),
        spec(EventType.PACKAGE_PURCHASED, EventCardKind.PAYMENT, MenuType.PAYMENT, SignalPriority.P2_IMPORTANT, "Package purchased", "PURCHASE", CardDecision.OPEN_DETAIL, CardMediaMode.MEMBER_AVATAR, "View package"),

        // TRAINER
        spec(EventType.TRAINER_ASSIGNED, EventCardKind.TRAINER, MenuType.TRAINER, SignalPriority.P2_IMPORTANT, "Trainer assigned", "COACHING", CardDecision.OPEN_TRAINER, CardMediaMode.STAFF_AVATAR, "Open trainer"),
        spec(EventType.TRAINER_SESSION_SCHEDULED, EventCardKind.TRAINER, MenuType.TRAINER, SignalPriority.P2_IMPORTANT, "PT session scheduled", "UPCOMING", CardDecision.START_SESSION, CardMediaMode.STAFF_AVATAR, "Open session"),
        spec(EventType.TRAINER_SESSION_STARTED, EventCardKind.TRAINER, MenuType.TRAINER, SignalPriority.P2_IMPORTANT, "PT session started", "LIVE SESSION", CardDecision.OPEN_TRAINER, CardMediaMode.STAFF_AVATAR, "View session"),
        spec(EventType.TRAINER_SESSION_COMPLETED, EventCardKind.TRAINER, MenuType.TRAINER, SignalPriority.P3_BACKGROUND, "PT session completed", "COMPLETED", CardDecision.OPEN_HISTORY, CardMediaMode.STAFF_AVATAR, "View session"),
        spec(EventType.TRAINER_SESSION_MISSED, EventCardKind.TRAINER, MenuType.TRAINER, SignalPriority.P1_ACTION_REQUIRED, "PT session missed", "MISSED", CardDecision.OPEN_TRAINER, CardMediaMode.STAFF_AVATAR, "Reschedule"),
        spec(EventType.TRAINER_SESSION_CANCELLED, EventCardKind.TRAINER, MenuType.TRAINER, SignalPriority.P2_IMPORTANT, "PT session cancelled", "CANCELLED", CardDecision.OPEN_HISTORY, CardMediaMode.STAFF_AVATAR, "View schedule"),

        // WORKOUT
        spec(EventType.WORKOUT_STARTED, EventCardKind.WORKOUT, MenuType.WORKOUT, SignalPriority.P3_BACKGROUND, "Workout started", "WORKOUT", CardDecision.OPEN_WORKOUT, CardMediaMode.MEMBER_AVATAR, "Open workout"),
        spec(EventType.WORKOUT_COMPLETED, EventCardKind.WORKOUT, MenuType.WORKOUT, SignalPriority.P3_BACKGROUND, "Workout completed", "COMPLETED", CardDecision.OPEN_WORKOUT, CardMediaMode.MEMBER_AVATAR, "View workout"),
        spec(EventType.WORKOUT_SKIPPED, EventCardKind.WORKOUT, MenuType.WORKOUT, SignalPriority.P2_IMPORTANT, "Workout skipped", "SKIPPED", CardDecision.OPEN_WORKOUT, CardMediaMode.MEMBER_AVATAR, "View pattern"),
        spec(EventType.PR_ACHIEVED, EventCardKind.WORKOUT, MenuType.WORKOUT, SignalPriority.P2_IMPORTANT, "Personal record", "PROGRESS", CardDecision.OPEN_WORKOUT, CardMediaMode.PHOTO_TIMELINE, "View PR"),
        spec(EventType.GOAL_UPDATED, EventCardKind.WORKOUT, MenuType.WORKOUT, SignalPriority.P2_IMPORTANT, "Goal updated", "GOAL", CardDecision.OPEN_WORKOUT, CardMediaMode.MEMBER_AVATAR, "View goal"),
        spec(EventType.BODY_MEASUREMENT_UPDATED, EventCardKind.WORKOUT, MenuType.WORKOUT, SignalPriority.P2_IMPORTANT, "Body measurement updated", "PROGRESS", CardDecision.OPEN_WORKOUT, CardMediaMode.PHOTO_TIMELINE, "View progress"),

        // SERVICES
        spec(EventType.SERVICE_ACTIVATED, EventCardKind.SERVICE, MenuType.SERVICES, SignalPriority.P3_BACKGROUND, "Service activated", "SERVICE", CardDecision.OPEN_SERVICE, CardMediaMode.SERVICE_IMAGE, "View service"),
        spec(EventType.SERVICE_DEACTIVATED, EventCardKind.SERVICE, MenuType.SERVICES, SignalPriority.P2_IMPORTANT, "Service inactive", "SERVICE → INACTIVE", CardDecision.OPEN_SERVICE, CardMediaMode.SERVICE_IMAGE, "View service"),
        spec(EventType.SERVICE_BOOKED, EventCardKind.SERVICE, MenuType.SERVICES, SignalPriority.P2_IMPORTANT, "Service booked", "UPCOMING", CardDecision.OPEN_SERVICE, CardMediaMode.SERVICE_IMAGE, "Open booking"),
        spec(EventType.SERVICE_USED, EventCardKind.SERVICE, MenuType.SERVICES, SignalPriority.P3_BACKGROUND, "Service used", "USAGE", CardDecision.OPEN_SERVICE, CardMediaMode.SERVICE_IMAGE, "View usage"),
        spec(EventType.SERVICE_EXPIRED, EventCardKind.SERVICE, MenuType.SERVICES, SignalPriority.P1_ACTION_REQUIRED, "Service expired", "EXPIRED", CardDecision.OPEN_SERVICE, CardMediaMode.SERVICE_IMAGE, "View expiry"),
        spec(EventType.SERVICE_ISSUE, EventCardKind.ISSUE_RESOLUTION, MenuType.SERVICES, SignalPriority.P1_ACTION_REQUIRED, "Service issue", "ISSUE", CardDecision.RESOLVE_ISSUE, CardMediaMode.SERVICE_IMAGE, "Resolve issue"),

        // FACILITY / OPERATIONS
        spec(EventType.MACHINE_FAULT, EventCardKind.FACILITY_OPERATION, MenuType.SERVICES, SignalPriority.P0_CRITICAL, "Machine fault", "FACILITY", CardDecision.RESOLVE_ISSUE, CardMediaMode.EQUIPMENT_IMAGE, "Resolve fault"),
        spec(EventType.MACHINE_REPORTED, EventCardKind.FACILITY_OPERATION, MenuType.SERVICES, SignalPriority.P1_ACTION_REQUIRED, "Machine reported", "FACILITY", CardDecision.ASSIGN_STAFF, CardMediaMode.EQUIPMENT_IMAGE, "Assign staff"),
        spec(EventType.MACHINE_FIXED, EventCardKind.FACILITY_OPERATION, MenuType.SERVICES, SignalPriority.P3_BACKGROUND, "Machine fixed", "RESOLVED", CardDecision.OPEN_HISTORY, CardMediaMode.EQUIPMENT_IMAGE, "View repair"),
        spec(EventType.MAINTENANCE_STARTED, EventCardKind.FACILITY_OPERATION, MenuType.SERVICES, SignalPriority.P2_IMPORTANT, "Maintenance started", "OPERATIONS", CardDecision.OPEN_DETAIL, CardMediaMode.EQUIPMENT_IMAGE, "View maintenance"),
        spec(EventType.MAINTENANCE_COMPLETED, EventCardKind.FACILITY_OPERATION, MenuType.SERVICES, SignalPriority.P3_BACKGROUND, "Maintenance completed", "RESOLVED", CardDecision.OPEN_HISTORY, CardMediaMode.EQUIPMENT_IMAGE, "View record"),
        spec(EventType.CLEANING_STARTED, EventCardKind.FACILITY_OPERATION, MenuType.SERVICES, SignalPriority.P2_IMPORTANT, "Cleaning started", "OPERATIONS", CardDecision.OPEN_DETAIL, CardMediaMode.NONE, "View cleaning"),
        spec(EventType.CLEANING_COMPLETED, EventCardKind.FACILITY_OPERATION, MenuType.SERVICES, SignalPriority.P3_BACKGROUND, "Cleaning completed", "RESOLVED", CardDecision.OPEN_HISTORY, CardMediaMode.NONE, "View record"),
        spec(EventType.STOCK_LOW, EventCardKind.ISSUE_RESOLUTION, MenuType.SERVICES, SignalPriority.P1_ACTION_REQUIRED, "Stock low", "INVENTORY", CardDecision.ASSIGN_STAFF, CardMediaMode.SERVICE_IMAGE, "Restock"),

        // COMMUNICATION / ISSUES
        spec(EventType.COMPLAINT, EventCardKind.ISSUE_RESOLUTION, MenuType.HISTORY, SignalPriority.P1_ACTION_REQUIRED, "Member complaint", "ISSUE", CardDecision.RESOLVE_ISSUE, CardMediaMode.MEMBER_PHOTO, "Resolve"),
        spec(EventType.COMPLAINT_RESOLVED, EventCardKind.ISSUE_RESOLUTION, MenuType.HISTORY, SignalPriority.P3_BACKGROUND, "Complaint resolved", "RESOLVED", CardDecision.OPEN_HISTORY, CardMediaMode.MEMBER_PHOTO, "View resolution"),
        spec(EventType.INCIDENT_REPORTED, EventCardKind.ISSUE_RESOLUTION, MenuType.SERVICES, SignalPriority.P0_CRITICAL, "Incident reported", "INCIDENT", CardDecision.RESOLVE_ISSUE, CardMediaMode.ATTACHMENT_THUMBNAIL, "Open incident"),
        spec(EventType.INCIDENT_RESOLVED, EventCardKind.ISSUE_RESOLUTION, MenuType.HISTORY, SignalPriority.P3_BACKGROUND, "Incident resolved", "RESOLVED", CardDecision.OPEN_HISTORY, CardMediaMode.ATTACHMENT_THUMBNAIL, "View incident"),
        spec(EventType.ANNOUNCEMENT, EventCardKind.COMMUNICATION, MenuType.HOME, SignalPriority.P3_BACKGROUND, "Announcement", "NOTICE", CardDecision.OPEN_DETAIL, CardMediaMode.NONE, "Read"),
        spec(EventType.OFFER, EventCardKind.COMMUNICATION, MenuType.HOME, SignalPriority.P3_BACKGROUND, "Offer", "PROMOTION", CardDecision.OPEN_DETAIL, CardMediaMode.SERVICE_IMAGE, "View offer")
    )

    private fun spec(
        eventType: EventType,
        kind: EventCardKind,
        menu: MenuType,
        priority: SignalPriority,
        title: String,
        storyLabel: String,
        decision: CardDecision,
        media: CardMediaMode,
        actionLabel: String
    ): EventCardSpec = EventCardSpec(
        eventType = eventType,
        kind = kind,
        defaultMenu = menu,
        priority = priority,
        title = title,
        storyLabel = storyLabel,
        primaryDecision = decision,
        mediaMode = media,
        showTimeline = eventType in setOf(
            EventType.WALK_IN,
            EventType.TRIAL_STARTED,
            EventType.TRIAL_CONVERTED,
            EventType.TRIAL_EXPIRED,
            EventType.FREEZE_STARTED,
            EventType.FREEZE_ENDED,
            EventType.BANNED,
            EventType.BAN_LIFTED,
            EventType.MEMBERSHIP_STARTED,
            EventType.MEMBERSHIP_RENEWED,
            EventType.MEMBERSHIP_EXPIRED,
            EventType.MEMBERSHIP_CANCELLED,
            EventType.PAYMENT_SUCCESS,
            EventType.PAYMENT_FAILED,
            EventType.PAYMENT_OVERDUE,
            EventType.TRAINER_SESSION_SCHEDULED,
            EventType.TRAINER_SESSION_STARTED,
            EventType.TRAINER_SESSION_COMPLETED,
            EventType.SERVICE_ISSUE,
            EventType.MACHINE_FAULT,
            EventType.MAINTENANCE_STARTED,
            EventType.INCIDENT_REPORTED
        ),
        showNowPastFuture = eventType !in setOf(
            EventType.PAYMENT_SUCCESS,
            EventType.CHECK_IN,
            EventType.CHECK_OUT
        ),
        evidenceKeys = evidenceKeysFor(eventType),
        actionLabel = actionLabel
    )

    private fun evidenceKeysFor(event: EventType): List<String> = when (event) {
        EventType.WALK_IN -> listOf("walkInAt", "source", "trialDays", "staff", "conversionStatus")
        EventType.TRIAL_STARTED -> listOf("startedAt", "expiresAt", "trialDays", "source")
        EventType.TRIAL_CONVERTED -> listOf("walkInAt", "trialStartedAt", "convertedAt", "planName")
        EventType.TRIAL_EXPIRED -> listOf("startedAt", "expiredAt", "daysSinceExpiry", "conversionStatus")
        EventType.FREEZE_STARTED, EventType.FREEZE_ENDED -> listOf("freezeStart", "freezeEnd", "reason", "remainingDays")
        EventType.BANNED, EventType.BAN_LIFTED -> listOf("banAt", "reason", "actor", "liftedAt")
        EventType.PAYMENT_OVERDUE, EventType.PAYMENT_FAILED -> listOf("amount", "dueAt", "lastAttemptAt", "method")
        EventType.TRAINER_SESSION_STARTED, EventType.TRAINER_SESSION_COMPLETED -> listOf("trainer", "scheduledAt", "startedAt", "duration")
        EventType.MACHINE_FAULT, EventType.SERVICE_ISSUE -> listOf("asset", "reportedAt", "severity", "assignee", "resolvedAt")
        else -> listOf("occurredAt")
    }

    fun forEvent(eventType: EventType): EventCardSpec =
        all.firstOrNull { it.eventType == eventType } ?: EventCardSpec(
            eventType = eventType,
            kind = EventCardKind.HOME_DECISION,
            defaultMenu = MenuType.HOME,
            priority = SignalPriority.P3_BACKGROUND,
            title = eventType.displayLabel(),
            storyLabel = "EVENT",
            primaryDecision = CardDecision.OPEN_DETAIL
        )
}

/**
 * Deterministic routing with state-aware overrides. The event type alone is
 * insufficient for a check-in: an overdue payment or expired membership wins.
 */
object EventCardRouter {
    fun route(event: MemberEvent, snapshot: MemberSnapshot): EventCardSpec {
        if (event.eventType == EventType.CHECK_IN) {
            val paymentOverdue = snapshot.issues.any { it.description.contains("overdue", ignoreCase = true) } ||
                snapshot.recentEvents.any { it.eventType == EventType.PAYMENT_OVERDUE }
            if (paymentOverdue) return EventCardCatalog.forEvent(EventType.PAYMENT_OVERDUE)

            val expired = snapshot.recentEvents.any { it.eventType == EventType.MEMBERSHIP_EXPIRED }
            if (expired) return EventCardCatalog.forEvent(EventType.MEMBERSHIP_EXPIRED)

            val trainerScheduled = snapshot.recentEvents.any { it.eventType == EventType.TRAINER_SESSION_SCHEDULED }
            if (trainerScheduled) return EventCardCatalog.forEvent(EventType.TRAINER_SESSION_SCHEDULED)
        }
        return EventCardCatalog.forEvent(event.eventType)
    }
}
