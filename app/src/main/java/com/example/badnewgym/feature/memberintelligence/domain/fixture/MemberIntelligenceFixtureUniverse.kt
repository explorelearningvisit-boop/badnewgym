package com.example.badnewgym.feature.memberintelligence.domain.fixture

import com.example.badnewgym.feature.memberintelligence.design.MediaAssets
import com.example.badnewgym.feature.memberintelligence.domain.model.*

/**
 * BAD GYM Stage 7.9 — Deterministic Synthetic Fixture Universe.
 *
 * Provides typed synthetic fixture data for EVERY supported EventType (all 68 + UNKNOWN)
 * and all 16 EventCardKind archetypes, plus comprehensive edge cases.
 *
 * CRITICAL RULE: All data here is explicitly synthetic/demo data for QA and testing.
 * It is never mistaken for live member or production records.
 */
enum class TemporalBucket {
    NOW,
    PAST,
    FUTURE
}

data class FixtureCardItem(
    val id: String,
    val eventType: EventType,
    val cardKind: EventCardKind,
    val temporalBucket: TemporalBucket,
    val label: String,
    val description: String,
    val snapshot: MemberSnapshot,
    val event: MemberEvent,
    val spec: EventCardSpec = EventCardCatalog.forEvent(eventType),
    val isEdgeCase: Boolean = false,
    val edgeCaseNote: String? = null,
    val storyLink: String? = null
)

object MemberIntelligenceFixtureUniverse {
    const val DAY_MS: Long = 86_400_000L
    const val HOUR_MS: Long = 3_600_000L
    const val BASE_TIME: Long = 1_774_620_000_000L // Deterministic baseline reference

    val allEvents: List<FixtureCardItem> by lazy { buildTaxonomyFixtures() }
    val edgeCases: List<FixtureCardItem> by lazy { buildEdgeCaseFixtures() }
    val allFixtures: List<FixtureCardItem> by lazy { allEvents + edgeCases }

    fun forEventType(type: EventType): FixtureCardItem =
        allEvents.firstOrNull { it.eventType == type }
            ?: createDefaultFixture(type)

    fun forCardKind(kind: EventCardKind): List<FixtureCardItem> =
        allFixtures.filter { it.cardKind == kind }

    fun forTemporalBucket(bucket: TemporalBucket): List<FixtureCardItem> =
        allFixtures.filter { it.temporalBucket == bucket }

    fun getById(id: String): FixtureCardItem? =
        allFixtures.firstOrNull { it.id == id }

    // --- Synthetic Fixture Factory ---

    private fun buildTaxonomyFixtures(): List<FixtureCardItem> {
        val list = mutableListOf<FixtureCardItem>()

        // 1. ACCESS / ATTENDANCE
        list += createFixture(
            id = "fix_check_in",
            type = EventType.CHECK_IN,
            bucket = TemporalBucket.NOW,
            label = "Check-in: Clean Attendance",
            desc = "Member arrived at turnstile, valid membership, no overdue balance.",
            name = "Aarav Sharma (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_AARAV,
            meta = mapOf("gate" to "Turnstile 01", "latenessMinutes" to "0", "status" to "On time"),
            weeklyPattern = listOf(1, 1, 0, 1, 1, 0, 1)
        )
        list += createFixture(
            id = "fix_check_out",
            type = EventType.CHECK_OUT,
            bucket = TemporalBucket.PAST,
            label = "Check-out: Completed Visit",
            desc = "Member completed 72-minute session and exited through gate.",
            name = "Riya Patel (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_RIYA,
            meta = mapOf("sessionDurationMinutes" to "72", "gate" to "Turnstile 02")
        )
        list += createFixture(
            id = "fix_walk_in",
            type = EventType.WALK_IN,
            bucket = TemporalBucket.NOW,
            label = "Walk-in Lead: Consultation",
            desc = "Prospective member walk-in inquiring about annual strength coaching.",
            name = "Kabir Deshmukh (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_KABIR,
            meta = mapOf("walkInAt" to "26 Sep 10:15", "source" to "Walk-in Front Desk", "trialDays" to "3", "staff" to "Reception", "conversionStatus" to "Lead Registered"),
            storyLink = "WALK-IN → TRIAL → MEMBER"
        )

        // 2. TRIAL LIFECYCLE
        list += createFixture(
            id = "fix_trial_started",
            type = EventType.TRIAL_STARTED,
            bucket = TemporalBucket.NOW,
            label = "Trial Started: Active 3-Day Pass",
            desc = "3-day guest trial pass activated with trainer orientation scheduled.",
            name = "Neha Joshi (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_NEHA,
            meta = mapOf("startedAt" to "25 Sep", "expiresAt" to "28 Sep", "trialDays" to "3", "source" to "Front Desk Walk-in"),
            storyLink = "WALK-IN → TRIAL ACTIVE"
        )
        list += createFixture(
            id = "fix_trial_converted",
            type = EventType.TRIAL_CONVERTED,
            bucket = TemporalBucket.PAST,
            label = "Trial Converted: Gold Annual",
            desc = "Trialist converted to full 12-month membership following PT orientation.",
            name = "Vikram Sengupta (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_VIKRAM,
            meta = mapOf("walkInAt" to "20 Sep", "trialStartedAt" to "21 Sep", "convertedAt" to "24 Sep", "planName" to "Gold Annual 12M"),
            storyLink = "TRIAL → CONVERTED"
        )
        list += createFixture(
            id = "fix_trial_expired",
            type = EventType.TRIAL_EXPIRED,
            bucket = TemporalBucket.PAST,
            label = "Trial Expired: Follow-up Pending",
            desc = "Trial expired 2 days ago without conversion; sales follow-up triggered.",
            name = "Simran Kaur (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_SIMRAN,
            meta = mapOf("startedAt" to "18 Sep", "expiredAt" to "21 Sep", "daysSinceExpiry" to "2", "conversionStatus" to "Follow-up Required"),
            storyLink = "TRIAL → EXPIRED"
        )

        // 3. FREEZE & RESTRICTIONS
        list += createFixture(
            id = "fix_freeze_started",
            type = EventType.FREEZE_STARTED,
            bucket = TemporalBucket.NOW,
            label = "Membership Frozen: Medical Request",
            desc = "Member requested 30-day medical freeze; recurring billing paused.",
            name = "Rohan Malhotra (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("freezeStart" to "15 Sep", "freezeEnd" to "15 Oct", "reason" to "Knee surgery rehabilitation", "remainingDays" to "19")
        )
        list += createFixture(
            id = "fix_freeze_ended",
            type = EventType.FREEZE_ENDED,
            bucket = TemporalBucket.PAST,
            label = "Freeze Ended: Auto-Reactivated",
            desc = "Scheduled freeze period ended, member access reactivated.",
            name = "Rohan Malhotra (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("freezeStart" to "15 Aug", "freezeEnd" to "15 Sep", "reason" to "Medical freeze complete")
        )
        list += createFixture(
            id = "fix_banned",
            type = EventType.BANNED,
            bucket = TemporalBucket.NOW,
            label = "Member Banned: Code of Conduct",
            desc = "Access blocked by management due to repeated facility rule violations.",
            name = "Demo Subject X",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("banAt" to "22 Sep", "reason" to "Severe conduct violation (Facility safety)", "actor" to "General Manager", "status" to "ACTIVE RESTRICTION")
        )
        list += createFixture(
            id = "fix_ban_lifted",
            type = EventType.BAN_LIFTED,
            bucket = TemporalBucket.PAST,
            label = "Ban Lifted: Reinstated Access",
            desc = "Executive review completed, probationary turnstile access restored.",
            name = "Demo Subject Y",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("banAt" to "01 Sep", "liftedAt" to "25 Sep", "reason" to "Review board resolution approved", "actor" to "Operations Lead")
        )

        // 4. MEMBERSHIP LIFECYCLE
        list += createFixture(
            id = "fix_new_member",
            type = EventType.NEW_MEMBER,
            bucket = TemporalBucket.NOW,
            label = "New Member Onboarding",
            desc = "New member profile created, welcome packet and induction pending.",
            name = "Priya Nair (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_NEHA,
            meta = mapOf("joinedAt" to "26 Sep", "plan" to "Quarterly Standard")
        )
        list += createFixture(
            id = "fix_member_created",
            type = EventType.MEMBER_CREATED,
            bucket = TemporalBucket.PAST,
            label = "Member Created: System Record",
            desc = "System identity established in database.",
            name = "Aditya Rao (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_ARJUN,
            meta = mapOf("createdBy" to "Admin Portal", "code" to "BG-701")
        )
        list += createFixture(
            id = "fix_member_updated",
            type = EventType.MEMBER_UPDATED,
            bucket = TemporalBucket.PAST,
            label = "Member Profile Updated",
            desc = "Emergency contact and medical disclosure verified.",
            name = "Aditya Rao (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_ARJUN,
            meta = mapOf("updatedField" to "Emergency Contact")
        )
        list += createFixture(
            id = "fix_membership_started",
            type = EventType.MEMBERSHIP_STARTED,
            bucket = TemporalBucket.NOW,
            label = "Membership Started: Gold Tier",
            desc = "Annual plan term active with 365 days validity.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("plan" to "Gold Plan", "duration" to "12 Months")
        )
        list += createFixture(
            id = "fix_renewal",
            type = EventType.RENEWAL,
            bucket = TemporalBucket.PAST,
            label = "Membership Renewed: Early Incentive",
            desc = "Renewed 30 days prior to expiry with loyalty credit applied.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("renewalDate" to "20 Sep", "term" to "+12 Months")
        )
        list += createFixture(
            id = "fix_membership_renewed",
            type = EventType.MEMBERSHIP_RENEWED,
            bucket = TemporalBucket.PAST,
            label = "Membership Renewed: Standard",
            desc = "Annual plan successfully extended for second year.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("renewedTerm" to "Annual")
        )
        list += createFixture(
            id = "fix_expired",
            type = EventType.EXPIRED,
            bucket = TemporalBucket.PAST,
            label = "Membership Expired: Renewal Pending",
            desc = "Subscription expired 5 days ago; access restricted at turnstile.",
            name = "Vikram Khanna (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_KABIR,
            meta = mapOf("expiredAt" to "21 Sep", "daysPast" to "5", "plan" to "Silver 6M")
        )
        list += createFixture(
            id = "fix_membership_expired",
            type = EventType.MEMBERSHIP_EXPIRED,
            bucket = TemporalBucket.PAST,
            label = "Plan Term Expired",
            desc = "Formal expiry recorded; member in grace period for renewal.",
            name = "Vikram Khanna (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_KABIR,
            meta = mapOf("expiredDays" to "5")
        )
        list += createFixture(
            id = "fix_freeze",
            type = EventType.FREEZE,
            bucket = TemporalBucket.NOW,
            label = "General Freeze Status",
            desc = "Account freeze active.",
            name = "Rohan Verma (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("freezeType" to "Standard Freeze")
        )
        list += createFixture(
            id = "fix_reactivation",
            type = EventType.REACTIVATION,
            bucket = TemporalBucket.NOW,
            label = "Account Reactivation",
            desc = "Account restored from lapsed status.",
            name = "Neha Joshi (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_NEHA,
            meta = mapOf("reactivatedAt" to "26 Sep")
        )
        list += createFixture(
            id = "fix_membership_cancelled",
            type = EventType.MEMBERSHIP_CANCELLED,
            bucket = TemporalBucket.PAST,
            label = "Membership Cancelled",
            desc = "Member relocated out of state; contract settled.",
            name = "Arjun Kapoor (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_ARJUN,
            meta = mapOf("reason" to "Relocation", "cancelledAt" to "10 Sep")
        )

        // 5. FINANCE / PAYMENTS
        list += createFixture(
            id = "fix_payment",
            type = EventType.PAYMENT,
            bucket = TemporalBucket.PAST,
            label = "Payment Received",
            desc = "Subscription payment recorded.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("amount" to "₹24,000", "method" to "UPI")
        )
        list += createFixture(
            id = "fix_payment_success",
            type = EventType.PAYMENT_SUCCESS,
            bucket = TemporalBucket.PAST,
            label = "Payment Success: UPI Auto-debit",
            desc = "Monthly membership subscription of ₹3,500 successfully collected.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("amount" to "₹3,500", "method" to "UPI Autopay", "ref" to "TXN_882914")
        )
        list += createFixture(
            id = "fix_payment_due",
            type = EventType.PAYMENT_DUE,
            bucket = TemporalBucket.FUTURE,
            label = "Payment Due: In 3 Days",
            desc = "Quarterly instalment of ₹6,000 due on 29 Sep.",
            name = "Aarav Sharma (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_AARAV,
            meta = mapOf("amount" to "₹6,000", "dueDate" to "29 Sep", "method" to "Card on File")
        )
        list += createFixture(
            id = "fix_payment_overdue",
            type = EventType.PAYMENT_OVERDUE,
            bucket = TemporalBucket.NOW,
            label = "Payment Overdue: 7 Days",
            desc = "Outstanding balance ₹4,500 overdue by 7 days. Action required.",
            name = "Kavita Reddy (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_RIYA,
            meta = mapOf("amount" to "₹4,500", "dueAt" to "19 Sep", "overdueDays" to "7", "lastAttemptAt" to "25 Sep", "method" to "Net Banking")
        )
        list += createFixture(
            id = "fix_payment_failed",
            type = EventType.PAYMENT_FAILED,
            bucket = TemporalBucket.NOW,
            label = "Payment Failed: Insufficient Balance",
            desc = "Recurring mandate failed (Bank code: E-04). Retrying in 24h.",
            name = "Kavita Reddy (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_RIYA,
            meta = mapOf("amount" to "₹3,500", "dueAt" to "25 Sep", "lastAttemptAt" to "26 Sep 06:00", "method" to "UPI Mandate", "failReason" to "Declined by bank")
        )
        list += createFixture(
            id = "fix_payment_partial",
            type = EventType.PAYMENT_PARTIAL,
            bucket = TemporalBucket.PAST,
            label = "Partial Payment Recorded",
            desc = "₹5,000 paid towards ₹12,000 total balance; ₹7,000 remaining.",
            name = "Rahul Saxena (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("paid" to "₹5,000", "remaining" to "₹7,000", "total" to "₹12,000")
        )
        list += createFixture(
            id = "fix_refund",
            type = EventType.REFUND,
            bucket = TemporalBucket.PAST,
            label = "Refund Processed: Supplement Return",
            desc = "₹2,499 refunded to original payment method for unopened return.",
            name = "Simran Kaur (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_SIMRAN,
            meta = mapOf("amount" to "₹2,499", "item" to "MuscleBlaze Protein (Damaged seal)", "auth" to "Manager Approved")
        )
        list += createFixture(
            id = "fix_invoice_created",
            type = EventType.INVOICE_CREATED,
            bucket = TemporalBucket.PAST,
            label = "Tax Invoice Generated",
            desc = "GST compliant invoice INV-2026-9041 generated for annual dues.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("invoiceNo" to "INV-2026-9041", "gst" to "₹4,320")
        )
        list += createFixture(
            id = "fix_package_purchased",
            type = EventType.PACKAGE_PURCHASED,
            bucket = TemporalBucket.PAST,
            label = "Package Purchased: 20 PT Sessions",
            desc = "Elite personal training package credited to member account.",
            name = "Kabir Deshmukh (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_KABIR,
            meta = mapOf("package" to "20x 1-on-1 PT Coaching", "price" to "₹25,000")
        )

        // 6. TRAINER / COACHING
        list += createFixture(
            id = "fix_trainer_session",
            type = EventType.TRAINER_SESSION,
            bucket = TemporalBucket.NOW,
            label = "PT Session Event",
            desc = "General trainer session touchpoint.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("trainer" to "Vikas Yadav", "type" to "Hypertrophy")
        )
        list += createFixture(
            id = "fix_trainer_assigned",
            type = EventType.TRAINER_ASSIGNED,
            bucket = TemporalBucket.PAST,
            label = "Head Coach Assigned",
            desc = "Coach Vikas Yadav assigned as primary fitness mentor.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("coach" to "Vikas Yadav", "specialty" to "Strength & Conditioning")
        )
        list += createFixture(
            id = "fix_trainer_scheduled",
            type = EventType.TRAINER_SESSION_SCHEDULED,
            bucket = TemporalBucket.FUTURE,
            label = "PT Session Scheduled: Tomorrow 07:00",
            desc = "Session #8 of 12 scheduled with Vikas Yadav (Chest & Triceps focus).",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("trainer" to "Vikas Yadav", "scheduledAt" to "Tomorrow 07:00 AM", "sessionNo" to "8/12", "focus" to "Upper Body Push")
        )
        list += createFixture(
            id = "fix_trainer_started",
            type = EventType.TRAINER_SESSION_STARTED,
            bucket = TemporalBucket.NOW,
            label = "PT Session Live: In Progress",
            desc = "Session started at 07:05 AM in Weightlifting Bay 2.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("trainer" to "Vikas Yadav", "startedAt" to "07:05 AM", "location" to "Power Rack 3", "duration" to "45 min elapsed")
        )
        list += createFixture(
            id = "fix_trainer_completed",
            type = EventType.TRAINER_SESSION_COMPLETED,
            bucket = TemporalBucket.PAST,
            label = "PT Session Completed: Logged",
            desc = "60-minute session logged: 14 sets completed, RPE 8.5 recorded.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("trainer" to "Vikas Yadav", "startedAt" to "07:00", "duration" to "60 min", "rpe" to "8.5", "volumeKg" to "14,200")
        )
        list += createFixture(
            id = "fix_trainer_missed",
            type = EventType.TRAINER_SESSION_MISSED,
            bucket = TemporalBucket.PAST,
            label = "PT Session Missed: No-Show",
            desc = "Member did not attend scheduled 08:00 AM session without notice.",
            name = "Aditya Rao (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_ARJUN,
            meta = mapOf("trainer" to "Coach Priya", "scheduledAt" to "Yesterday 08:00 AM", "policy" to "Deduction Applied")
        )
        list += createFixture(
            id = "fix_trainer_cancelled",
            type = EventType.TRAINER_SESSION_CANCELLED,
            bucket = TemporalBucket.PAST,
            label = "PT Session Cancelled: Weather",
            desc = "Session cancelled with >12h notice; credit retained.",
            name = "Aditya Rao (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_ARJUN,
            meta = mapOf("trainer" to "Coach Priya", "cancelledBy" to "Member (Timely Notice)")
        )

        // 7. WORKOUT & PROGRESS
        list += createFixture(
            id = "fix_workout",
            type = EventType.WORKOUT,
            bucket = TemporalBucket.NOW,
            label = "General Workout Activity",
            desc = "Workout logged in member diary.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("routine" to "Push Protocol", "duration" to "60 min")
        )
        list += createFixture(
            id = "fix_workout_started",
            type = EventType.WORKOUT_STARTED,
            bucket = TemporalBucket.NOW,
            label = "Workout Started: Legs & Core",
            desc = "Member scanned into Functional Fitness Zone.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("routine" to "Heavy Leg Day", "startedAt" to "18:30")
        )
        list += createFixture(
            id = "fix_workout_completed",
            type = EventType.WORKOUT_COMPLETED,
            bucket = TemporalBucket.PAST,
            label = "Workout Completed: 75 min",
            desc = "Full routine completed: Squats, RDLs, Walking Lunges, Abs.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("routine" to "Heavy Leg Day", "duration" to "75 min", "exercises" to "6", "calories" to "540 kcal")
        )
        list += createFixture(
            id = "fix_workout_skipped",
            type = EventType.WORKOUT_SKIPPED,
            bucket = TemporalBucket.PAST,
            label = "Workout Skipped: 3 Day Gap",
            desc = "Habit alert: 3 consecutive planned training days missed.",
            name = "Neha Joshi (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_NEHA,
            meta = mapOf("routine" to "Upper Body Hypertrophy", "gapDays" to "3")
        )
        list += createFixture(
            id = "fix_pr_achieved",
            type = EventType.PR_ACHIEVED,
            bucket = TemporalBucket.PAST,
            label = "New PR: Deadlift 180kg",
            desc = "+10kg lifetime milestone achieved on Barbell Deadlift.",
            name = "Rohan Verma (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("exercise" to "Barbell Deadlift", "weight" to "180 kg", "previous" to "170 kg", "improvement" to "+10 kg")
        )
        list += createFixture(
            id = "fix_goal_updated",
            type = EventType.GOAL_UPDATED,
            bucket = TemporalBucket.PAST,
            label = "Goal Updated: Target 12% BF",
            desc = "Member revised target to lean hypertrophy before summer.",
            name = "Rohan Verma (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("goal" to "Body Recomposition", "targetBf" to "12%")
        )
        list += createFixture(
            id = "fix_body_measurement",
            type = EventType.BODY_MEASUREMENT_UPDATED,
            bucket = TemporalBucket.PAST,
            label = "InBody Scan Logged: 74.2 kg",
            desc = "Bio-impedance scan recorded: Skeletal muscle mass +0.8kg.",
            name = "Rohan Verma (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("weight" to "74.2 kg", "smm" to "35.1 kg", "fatPercent" to "13.4%")
        )

        // 8. SERVICES & NUTRITION
        list += createFixture(
            id = "fix_supplement_purchase",
            type = EventType.SUPPLEMENT_PURCHASE,
            bucket = TemporalBucket.PAST,
            label = "Supplement: Whey Protein 2kg",
            desc = "Purchased MuscleBlaze Biozyme Whey (Rich Chocolate).",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("product" to "MuscleBlaze Biozyme Whey 2kg", "price" to "₹4,249")
        )
        list += createFixture(
            id = "fix_nutrition",
            type = EventType.NUTRITION,
            bucket = TemporalBucket.NOW,
            label = "Nutrition Plan: Macro Adjusted",
            desc = "Daily target: 2,600 kcal (170g Protein, 280g Carbs, 65g Fats).",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("calories" to "2,600 kcal", "protein" to "170g", "plan" to "Lean Bulk Protocol")
        )
        list += createFixture(
            id = "fix_service_purchase",
            type = EventType.SERVICE_PURCHASE,
            bucket = TemporalBucket.PAST,
            label = "Service Purchased: Locker #12",
            desc = "Annual premium locker rental attached to account.",
            name = "Yash Singh (Demo)",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            meta = mapOf("service" to "Private Locker L12", "cost" to "₹3,000/yr")
        )
        list += createFixture(
            id = "fix_service_activated",
            type = EventType.SERVICE_ACTIVATED,
            bucket = TemporalBucket.NOW,
            label = "Service Activated: Sauna & Spa",
            desc = "Unlimited recovery suite access enabled on RFID wristband.",
            name = "Kabir Deshmukh (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_KABIR,
            meta = mapOf("service" to "Spa & Recovery Suite", "status" to "Active")
        )
        list += createFixture(
            id = "fix_service_deactivated",
            type = EventType.SERVICE_DEACTIVATED,
            bucket = TemporalBucket.PAST,
            label = "Service Deactivated: Towel Tier",
            desc = "Towel service subscription lapsed at end of term.",
            name = "Aarav Sharma (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_AARAV,
            meta = mapOf("service" to "Towel Valet", "status" to "Inactive")
        )
        list += createFixture(
            id = "fix_service_booked",
            type = EventType.SERVICE_BOOKED,
            bucket = TemporalBucket.FUTURE,
            label = "Service Booked: Cryotherapy",
            desc = "15-minute cryo chamber session booked for Friday 16:00.",
            name = "Kabir Deshmukh (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_KABIR,
            meta = mapOf("service" to "Cryotherapy Chamber", "time" to "Fri 16:00")
        )
        list += createFixture(
            id = "fix_service_used",
            type = EventType.SERVICE_USED,
            bucket = TemporalBucket.PAST,
            label = "Service Checked: Ice Bath",
            desc = "Checked in for cold plunge therapy session.",
            name = "Rohan Verma (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("service" to "Cold Plunge Recovery", "duration" to "10 min")
        )
        list += createFixture(
            id = "fix_service_expired",
            type = EventType.SERVICE_EXPIRED,
            bucket = TemporalBucket.PAST,
            label = "Service Expired: Nutrition Add-on",
            desc = "60-day dietitian check-in cycle completed.",
            name = "Simran Kaur (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_SIMRAN,
            meta = mapOf("service" to "Dietitian Consults", "expiredDays" to "3")
        )
        list += createFixture(
            id = "fix_service_issue",
            type = EventType.SERVICE_ISSUE,
            bucket = TemporalBucket.NOW,
            label = "Service Issue: Locker Jam",
            desc = "Digital lock error on Locker #44. Staff dispatched.",
            name = "Aarav Sharma (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_AARAV,
            meta = mapOf("asset" to "Locker #44", "reportedAt" to "10:15 AM", "severity" to "Medium", "assignee" to "Floor Staff")
        )

        // 9. FACILITY & OPERATIONS
        list += createFixture(
            id = "fix_maintenance",
            type = EventType.MAINTENANCE,
            bucket = TemporalBucket.NOW,
            label = "Facility Maintenance Event",
            desc = "Scheduled preventative maintenance on fitness floor.",
            name = "Facility Operations (Demo)",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("area" to "Cardio Zone", "status" to "In Progress")
        )
        list += createFixture(
            id = "fix_machine_fault",
            type = EventType.MACHINE_FAULT,
            bucket = TemporalBucket.NOW,
            label = "Machine Fault: Treadmill #4",
            desc = "Motor control board error (E-02). Machine tagged out.",
            name = "Facility Equipment (Demo)",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("asset" to "Treadmill Matrix T7xi (#4)", "reportedAt" to "08:15 AM", "severity" to "High", "assignee" to "Tech Support")
        )
        list += createFixture(
            id = "fix_machine_reported",
            type = EventType.MACHINE_REPORTED,
            bucket = TemporalBucket.NOW,
            label = "Machine Reported: Cable Crossover",
            desc = "Member reported frayed cable sheath on left pulley tower.",
            name = "Facility Equipment (Demo)",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("asset" to "Dual Cable Cross (Bay 3)", "reportedAt" to "09:30 AM", "severity" to "Medium", "assignee" to "Floor Tech")
        )
        list += createFixture(
            id = "fix_machine_fixed",
            type = EventType.MACHINE_FIXED,
            bucket = TemporalBucket.PAST,
            label = "Machine Fixed: Cable Replaced",
            desc = "Aircraft grade cable replaced and safety tested. Returned to service.",
            name = "Facility Equipment (Demo)",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("asset" to "Dual Cable Cross (Bay 3)", "resolvedAt" to "11:45 AM", "technician" to "Ramesh (Certified)")
        )
        list += createFixture(
            id = "fix_maint_started",
            type = EventType.MAINTENANCE_STARTED,
            bucket = TemporalBucket.NOW,
            label = "HVAC Filter Replacement",
            desc = "Quarterly HEPA air filtration service underway in Studio A.",
            name = "Facility Maintenance (Demo)",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("area" to "Studio A HVAC", "startedAt" to "06:00 AM", "estDuration" to "3h")
        )
        list += createFixture(
            id = "fix_maint_completed",
            type = EventType.MAINTENANCE_COMPLETED,
            bucket = TemporalBucket.PAST,
            label = "HVAC Maintenance Complete",
            desc = "Air filtration service completed; airflow certified at 450 CFM.",
            name = "Facility Maintenance (Demo)",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("area" to "Studio A HVAC", "completedAt" to "09:00 AM")
        )
        list += createFixture(
            id = "fix_clean_started",
            type = EventType.CLEANING_STARTED,
            bucket = TemporalBucket.NOW,
            label = "Deep Sanitation: Free Weights",
            desc = "Midday antimicrobial wipe-down of all dumbbell & barbell racks.",
            name = "Housekeeping (Demo)",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("zone" to "Dumbbell Zone", "startedAt" to "13:00")
        )
        list += createFixture(
            id = "fix_clean_completed",
            type = EventType.CLEANING_COMPLETED,
            bucket = TemporalBucket.PAST,
            label = "Sanitation Complete",
            desc = "Weight floor sanitized and inspected.",
            name = "Housekeeping (Demo)",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("zone" to "Dumbbell Zone", "completedAt" to "13:40")
        )
        list += createFixture(
            id = "fix_stock_low",
            type = EventType.STOCK_LOW,
            bucket = TemporalBucket.NOW,
            label = "Inventory Low: MB Whey 1kg",
            desc = "Stock level below threshold: 2 units remaining in reception fridge.",
            name = "Inventory Manager (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.WHEY_PRODUCT,
            meta = mapOf("sku" to "MB-WHEY-1KG", "qtyRemaining" to "2", "reorderThreshold" to "5")
        )

        // 10. COMMUNICATIONS, COMPLAINTS & INCIDENTS
        list += createFixture(
            id = "fix_complaint",
            type = EventType.COMPLAINT,
            bucket = TemporalBucket.NOW,
            label = "Member Complaint: Shower Temp",
            desc = "Member reported lukewarm water in Men's Locker Room shower #3.",
            name = "Rohan Verma (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("reportedAt" to "Yesterday 19:40", "category" to "Facility Plumbing", "assignee" to "Duty Manager")
        )
        list += createFixture(
            id = "fix_complaint_resolved",
            type = EventType.COMPLAINT_RESOLVED,
            bucket = TemporalBucket.PAST,
            label = "Complaint Resolved: Water Heater",
            desc = "Thermostat reset; water temperature verified at 42°C. Member notified.",
            name = "Rohan Verma (Demo)",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_ROHAN,
            meta = mapOf("resolvedAt" to "Today 08:30", "action" to "Commercial boiler element reset")
        )
        list += createFixture(
            id = "fix_incident_reported",
            type = EventType.INCIDENT_REPORTED,
            bucket = TemporalBucket.NOW,
            label = "Safety Incident: Ankle Twist",
            desc = "Minor slip during plyometric jump box drill. Ice pack administered.",
            name = "Neha Joshi (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_NEHA,
            meta = mapOf("reportedAt" to "11:20 AM", "location" to "Functional Turf", "firstAid" to "Ice + Compression", "severity" to "Mild")
        )
        list += createFixture(
            id = "fix_incident_resolved",
            type = EventType.INCIDENT_RESOLVED,
            bucket = TemporalBucket.PAST,
            label = "Incident Closed: Follow-up Good",
            desc = "Member checked by physical therapist; cleared for light activity.",
            name = "Neha Joshi (Demo)",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_NEHA,
            meta = mapOf("resolvedAt" to "Yesterday 16:00", "followUp" to "Full Recovery")
        )
        list += createFixture(
            id = "fix_announcement",
            type = EventType.ANNOUNCEMENT,
            bucket = TemporalBucket.FUTURE,
            label = "Gym Notice: Diwal Fest Hours",
            desc = "Special festival hours: 06:00 - 13:00 on festive weekend.",
            name = "BAD GYM Management",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("effectiveDate" to "30 Oct", "topic" to "Holiday Schedule")
        )
        list += createFixture(
            id = "fix_offer",
            type = EventType.OFFER,
            bucket = TemporalBucket.FUTURE,
            label = "Exclusive Offer: +3 Months Free",
            desc = "Special anniversary renewal offer for loyal members.",
            name = "BAD GYM Membership Club",
            tier = MembershipTier.PREMIUM,
            photo = null,
            meta = mapOf("promoCode" to "BAD-ANNIV-26", "validTill" to "15 Oct")
        )

        // 11. SENTINEL FALLBACK
        list += createFixture(
            id = "fix_unknown",
            type = EventType.UNKNOWN,
            bucket = TemporalBucket.NOW,
            label = "Unknown Event Sentinel",
            desc = "Fallback graceful card for unmapped or forward-compatible event signals.",
            name = "Unassigned Event Sentinel",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("info" to "Graceful fallback archetype")
        )

        return list
    }

    private fun buildEdgeCaseFixtures(): List<FixtureCardItem> {
        val list = mutableListOf<FixtureCardItem>()

        // 1. Long member name
        list += createFixture(
            id = "edge_long_name",
            type = EventType.CHECK_IN,
            bucket = TemporalBucket.NOW,
            label = "Edge Case: Ultra Long Member Name",
            desc = "Tests text wrapping and typography containment for long names.",
            name = "Dr. Bartholomew Alexander Montgomery-Choudhury III",
            tier = MembershipTier.VIP,
            photo = MediaAssets.MEMBER_KABIR,
            meta = mapOf("status" to "VIP Arrival"),
            isEdgeCase = true,
            edgeCaseNote = "Verify 2-line ellipsis containment and font scaling"
        )

        // 2. Missing member photo
        list += createFixture(
            id = "edge_missing_photo",
            type = EventType.CHECK_IN,
            bucket = TemporalBucket.NOW,
            label = "Edge Case: Missing Member Photo (Null URL)",
            desc = "Verifies deterministic initials avatar fallback when photoUrl is null.",
            name = "Siddharth Nair",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf("status" to "No Photo on File"),
            isEdgeCase = true,
            edgeCaseNote = "Verifies initials fallback avatar 'SN'"
        )

        // 3. Missing payment summary
        list += createFixture(
            id = "edge_missing_payment",
            type = EventType.PAYMENT,
            bucket = TemporalBucket.NOW,
            label = "Edge Case: Null Payment Record",
            desc = "Verifies card handles null PaymentSummary gracefully without crashing.",
            name = "Meera Krishnan",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_RIYA,
            customPayment = null,
            isEdgeCase = true,
            edgeCaseNote = "Null PaymentSummary handling"
        )

        // 4. Zero outstanding balance
        list += createFixture(
            id = "edge_zero_outstanding",
            type = EventType.PAYMENT_SUCCESS,
            bucket = TemporalBucket.PAST,
            label = "Edge Case: Zero Outstanding Dues",
            desc = "Member has zero balance due; verifies clean status presentation.",
            name = "Ananya Desai",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_NEHA,
            customPayment = PaymentSummary(
                totalOutstanding = 0.0,
                overdueDays = 0,
                dueDate = null,
                lastPaymentAmount = 18_000.0,
                lastPaymentDate = BASE_TIME - DAY_MS * 5,
                lastPaymentMethod = "Credit Card",
                lifetimePaid = 54_000.0,
                lifecycle = PaymentLifecycle.PAID
            ),
            isEdgeCase = true,
            edgeCaseNote = "Zero dues: verifies ₹0 display and active paid status"
        )

        // 5. Incomplete attendance weekly pattern (< 7 days)
        list += createFixture(
            id = "edge_incomplete_pattern",
            type = EventType.CHECK_IN,
            bucket = TemporalBucket.NOW,
            label = "Edge Case: Incomplete Pattern (< 7 Days)",
            desc = "Weekly pattern has only 3 recorded days; pulse graph must NOT render incomplete misleading 7-day bars.",
            name = "Tarun Mehra",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_AARAV,
            weeklyPattern = listOf(1, 0, 1),
            isEdgeCase = true,
            edgeCaseNote = "Attendance pulse must suppress graph if pattern.size < 7"
        )

        // 6. Empty weekly pattern (0 days)
        list += createFixture(
            id = "edge_empty_pattern",
            type = EventType.CHECK_IN,
            bucket = TemporalBucket.NOW,
            label = "Edge Case: Empty Pattern (0 Days)",
            desc = "Member has zero recorded pattern values.",
            name = "Varun Gupta",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_ROHAN,
            weeklyPattern = emptyList(),
            isEdgeCase = true,
            edgeCaseNote = "Weekly pattern emptyList() handling"
        )

        // 7. Exactly 7 days attendance pattern
        list += createFixture(
            id = "edge_7_days_pattern",
            type = EventType.CHECK_IN,
            bucket = TemporalBucket.NOW,
            label = "Edge Case: Exactly 7 Days Pattern",
            desc = "Standard 7-day attendance pulse with active visit distribution.",
            name = "Yash Singh",
            tier = MembershipTier.PREMIUM,
            photo = MediaAssets.MEMBER_YASH,
            weeklyPattern = listOf(1, 1, 0, 1, 1, 0, 1),
            isEdgeCase = true,
            edgeCaseNote = "Verifies 7-day attendance pulse bar reveal"
        )

        // 8. No active services
        list += createFixture(
            id = "edge_no_service",
            type = EventType.SERVICE_ACTIVATED,
            bucket = TemporalBucket.NOW,
            label = "Edge Case: Zero Active Services",
            desc = "Member has empty services list; must show 'NO ACTIVE SERVICE' informational pill.",
            name = "Devansh Bhatia",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_ARJUN,
            customServices = emptyList(),
            isEdgeCase = true,
            edgeCaseNote = "Verifies 'NO ACTIVE SERVICE' pill and graceful empty text"
        )

        // 9. No trainer assigned
        list += createFixture(
            id = "edge_no_trainer",
            type = EventType.TRAINER_SESSION,
            bucket = TemporalBucket.NOW,
            label = "Edge Case: Null Trainer",
            desc = "Member has no trainer summary; verifies null-safety.",
            name = "Gaurav Malhotra",
            tier = MembershipTier.NORMAL,
            photo = MediaAssets.MEMBER_KABIR,
            customTrainer = null,
            isEdgeCase = true,
            edgeCaseNote = "Verifies '—' fallback for missing coach"
        )

        // 10. Long issue description
        list += createFixture(
            id = "edge_long_description",
            type = EventType.INCIDENT_REPORTED,
            bucket = TemporalBucket.NOW,
            label = "Edge Case: Ultra Long Issue Text",
            desc = "Verifies text overflow and vertical scrolling inside issue card facts.",
            name = "Operations Admin",
            tier = MembershipTier.NORMAL,
            photo = null,
            meta = mapOf(
                "asset" to "HVAC Secondary Chiller Compressor Tower B2",
                "reason" to "Intermittent high-pressure cut-off triggered during peak afternoon demand cycle; thermal expansion valve suspected faulty, replacement parts ordered with expedited freight.",
                "reportedAt" to "Today 14:15",
                "assignee" to "Senior HVAC Systems Engineer"
            ),
            isEdgeCase = true,
            edgeCaseNote = "Multi-line text wrapping without clipping container"
        )

        return list
    }

    private fun createFixture(
        id: String,
        type: EventType,
        bucket: TemporalBucket,
        label: String,
        desc: String,
        name: String,
        tier: MembershipTier,
        photo: String?,
        meta: Map<String, String> = emptyMap(),
        weeklyPattern: List<Int> = listOf(1, 0, 1, 1, 0, 1, 0),
        customPayment: PaymentSummary? = defaultPayment(type),
        customTrainer: TrainerSummary? = defaultTrainer(type),
        customServices: List<ServiceSummary>? = defaultServices(type),
        isEdgeCase: Boolean = false,
        edgeCaseNote: String? = null,
        storyLink: String? = null
    ): FixtureCardItem {
        val now = BASE_TIME
        val identity = MemberIdentity(
            name = name,
            photoUrl = photo,
            tier = tier,
            memberSince = now - DAY_MS * 180,
            code = "SYN-${id.takeLast(6).uppercase()}",
            isVerified = true
        )
        val membership = MembershipStatus(
            planName = if (tier == MembershipTier.VIP) "Platinum Elite 12M" else if (tier == MembershipTier.PREMIUM) "Gold Annual 12M" else "Silver Standard 6M",
            planType = "Annual Recurring",
            isActive = type != EventType.EXPIRED && type != EventType.MEMBERSHIP_EXPIRED && type != EventType.MEMBERSHIP_CANCELLED && type != EventType.BANNED,
            daysRemaining = if (type == EventType.EXPIRED || type == EventType.MEMBERSHIP_EXPIRED) -5 else 185,
            startDate = now - DAY_MS * 180,
            expiryDate = if (type == EventType.EXPIRED || type == EventType.MEMBERSHIP_EXPIRED) now - DAY_MS * 5 else now + DAY_MS * 185,
            currentCost = 18_000.0,
            renewalCount = 1
        )
        val attendance = AttendanceSummary(
            visits = 24,
            target = 26,
            periodName = "Current Month",
            lifetimeVisits = 142,
            streakDays = 3,
            avgVisitsPerWeek = 3.5,
            weeklyPattern = weeklyPattern,
            lastVisitAt = now - HOUR_MS * 2
        )
        val snapshot = MemberSnapshot(
            id = id,
            gymId = "gym_qa_synthetic",
            identity = identity,
            membership = membership,
            attendance = attendance,
            payment = customPayment,
            trainer = customTrainer,
            workout = WorkoutSummary(now - DAY_MS, "Functional Strength", 60),
            supplements = SupplementSummary(true, "Biozyme Whey", now - DAY_MS * 10, 4249.0, "MuscleBlaze", MediaAssets.WHEY_PRODUCT),
            nutrition = NutritionSummary(true, "Custom Macros", now + DAY_MS * 30, 2400.0),
            services = customServices ?: listOf(ServiceSummary("Locker L12", true, now + DAY_MS * 90, 500.0)),
            recentEvents = listOf(
                MemberEvent(
                    id = "${id}_ev_hist",
                    memberId = id,
                    gymId = "gym_qa_synthetic",
                    eventType = type,
                    occurredAt = now - HOUR_MS * 3,
                    source = EventSource.SYSTEM,
                    metadata = meta
                )
            ),
            issues = if (type == EventType.PAYMENT_OVERDUE || type == EventType.PAYMENT_FAILED) {
                listOf(MemberIssue("iss_pay", "Payment overdue by ${meta["overdueDays"] ?: "7"} days", IssueSeverity.HIGH))
            } else if (type == EventType.MACHINE_FAULT || type == EventType.SERVICE_ISSUE) {
                listOf(MemberIssue("iss_mach", meta["asset"] ?: "Operational equipment report", IssueSeverity.MEDIUM))
            } else emptyList()
        )

        val eventTime = when (bucket) {
            TemporalBucket.NOW -> now
            TemporalBucket.PAST -> now - HOUR_MS * 4
            TemporalBucket.FUTURE -> now + DAY_MS * 1
        }
        val event = MemberEvent(
            id = "${id}_current",
            memberId = id,
            gymId = "gym_qa_synthetic",
            eventType = type,
            occurredAt = eventTime,
            source = EventSource.SYSTEM,
            metadata = meta
        )

        return FixtureCardItem(
            id = id,
            eventType = type,
            cardKind = EventCardCatalog.forEvent(type).kind,
            temporalBucket = bucket,
            label = label,
            description = desc,
            snapshot = snapshot,
            event = event,
            isEdgeCase = isEdgeCase,
            edgeCaseNote = edgeCaseNote,
            storyLink = storyLink
        )
    }

    private fun createDefaultFixture(type: EventType): FixtureCardItem =
        createFixture(
            id = "fix_auto_${type.name.lowercase()}",
            type = type,
            bucket = TemporalBucket.NOW,
            label = type.displayLabel(),
            desc = "Synthetic fixture representation of ${type.name}",
            name = "Synthetic ${type.displayLabel()} User",
            tier = MembershipTier.NORMAL,
            photo = null
        )

    private fun defaultPayment(type: EventType): PaymentSummary =
        when (type) {
            EventType.PAYMENT_OVERDUE -> PaymentSummary(
                totalOutstanding = 4500.0,
                overdueDays = 7,
                dueDate = BASE_TIME - DAY_MS * 7,
                lastPaymentAmount = 1400.0,
                lastPaymentDate = BASE_TIME - DAY_MS * 30,
                lastPaymentMethod = "UPI",
                lifetimePaid = 18_000.0,
                lifecycle = PaymentLifecycle.OVERDUE
            )
            EventType.PAYMENT_FAILED -> PaymentSummary(
                totalOutstanding = 3500.0,
                overdueDays = 1,
                dueDate = BASE_TIME - DAY_MS * 1,
                lastPaymentAmount = 3500.0,
                lastPaymentDate = BASE_TIME - DAY_MS * 32,
                lastPaymentMethod = "UPI Mandate",
                lifetimePaid = 21_000.0,
                lifecycle = PaymentLifecycle.OVERDUE
            )
            else -> PaymentSummary(
                totalOutstanding = 0.0,
                overdueDays = 0,
                dueDate = BASE_TIME + DAY_MS * 15,
                lastPaymentAmount = 3500.0,
                lastPaymentDate = BASE_TIME - DAY_MS * 15,
                lastPaymentMethod = "UPI",
                lifetimePaid = 28_000.0,
                lifecycle = PaymentLifecycle.PAID
            )
        }

    private fun defaultTrainer(type: EventType): TrainerSummary =
        TrainerSummary(
            trainerName = "Vikas Yadav",
            trainerPhotoUrl = MediaAssets.TRAINER_VIKAS,
            sessionsTotal = 12,
            sessionsUsed = 7,
            nextSessionDate = BASE_TIME + DAY_MS,
            focus = "Strength & Form",
            lastSessionDate = BASE_TIME - DAY_MS * 2
        )

    private fun defaultServices(type: EventType): List<ServiceSummary> =
        listOf(
            ServiceSummary("Locker L12", true, BASE_TIME + DAY_MS * 90, 500.0),
            ServiceSummary("Spa Pass", true, BASE_TIME + DAY_MS * 30, 1500.0)
        )
}
