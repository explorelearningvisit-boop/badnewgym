package com.example.badnewgym.feature.memberintelligence.preview.scenarios

import com.example.badnewgym.feature.memberintelligence.design.MediaAssets
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.EventSource
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.IssueSeverity
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberIdentity
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberIssue
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier
import com.example.badnewgym.feature.memberintelligence.domain.model.NutritionSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentBreakdownLine
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTransaction
import com.example.badnewgym.feature.memberintelligence.domain.model.ServiceSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.SupplementSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.TrainerSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutSummary

object MemberScenarios {
    private const val DAY = 86_400_000L

    fun getScenarioForTheme(
        theme: ThemeId,
        now: Long = System.currentTimeMillis()
    ): Pair<MemberSnapshot, MemberEvent> {
        return when (theme) {
            ThemeId.NATURAL_FRESH -> naturalFreshYash(now)
            ThemeId.FUTURISTIC_NEON -> futuristicNeonArjun(now)
            ThemeId.MINIMAL_DARK -> minimalDarkRiya(now)
            ThemeId.GLASSMORPHISM -> glassmorphismNeha(now)
            ThemeId.PREMIUM_3D -> premium3dKabir(now)
            ThemeId.VIBRANT_GRADIENT -> vibrantGradientAarav(now)
            ThemeId.BEAST_MODE -> beastModeRohan(now)
            ThemeId.PURPLE_ROYAL -> purpleRoyalSimran(now)
        }
    }

    // 1. Yash Singh (BG204) - NATURAL FRESH
    fun naturalFreshYash(now: Long = System.currentTimeMillis()): Pair<MemberSnapshot, MemberEvent> {
        val snapshot = MemberSnapshot(
            id = "1",
            gymId = "gym1",
            identity = MemberIdentity(
                name = "Yash Singh",
                photoUrl = MediaAssets.MEMBER_YASH,
                tier = MembershipTier.PREMIUM,
                memberSince = now - DAY * 365,
                code = "BG204",
                isVerified = true
            ),
            membership = MembershipStatus(
                planName = "Gold Plan",
                planType = "12 Months",
                isActive = true,
                daysRemaining = 48,
                startDate = now - DAY * 317,
                expiryDate = now + DAY * 48,
                currentCost = 24_000.0,
                renewalCount = 1
            ),
            attendance = AttendanceSummary(
                visits = 16,
                target = 26,
                periodName = "Current Month",
                lifetimeVisits = 214,
                streakDays = 4,
                avgVisitsPerWeek = 3.2,
                weeklyPattern = listOf(1, 1, 0, 1, 1, 0, 1),
                lastVisitAt = now
            ),
            payment = PaymentSummary(
                totalOutstanding = 4500.0,
                overdueDays = 3,
                dueDate = now - DAY * 3,
                lastPaymentAmount = 1400.0,
                lastPaymentDate = now - DAY * 28,
                lastPaymentMethod = "UPI",
                lifetimePaid = 22_200.0,
                lifecycle = PaymentLifecycle.OVERDUE,
                breakdown = listOf(PaymentBreakdownLine("Gold Plan Due", 4500.0)),
                history = listOf(PaymentTransaction("p1", 1400.0, now - DAY * 28, "UPI", "Plan"))
            ),
            trainer = TrainerSummary("Vikas Yadav", MediaAssets.TRAINER_VIKAS, 12, 7, now + DAY, "Chest & Triceps", now - DAY * 2),
            workout = WorkoutSummary(now - DAY, "Functional Fitness", 60),
            supplements = SupplementSummary(true, "MuscleBlaze Whey 2kg", now - DAY * 10, 4249.0, "MuscleBlaze", MediaAssets.WHEY_PRODUCT),
            nutrition = NutritionSummary(true, "BAD GYM Nutrition", now + DAY * 20, 2000.0),
            services = listOf(ServiceSummary("Locker L12", true, now + DAY * 45, 500.0)),
            recentEvents = listOf(MemberEvent("e1", "1", "gym1", EventType.CHECK_IN, now, EventSource.GATE)),
            issues = listOf(MemberIssue("i1", "Payment Due: ₹4,500 (3 days overdue)", IssueSeverity.HIGH))
        )
        val event = MemberEvent("live_1", "1", "gym1", EventType.CHECK_IN, now, EventSource.GATE)
        return snapshot to event
    }

    // 2. Arjun Mehta (BG105) - FUTURISTIC NEON
    fun futuristicNeonArjun(now: Long = System.currentTimeMillis()): Pair<MemberSnapshot, MemberEvent> {
        val snapshot = MemberSnapshot(
            id = "2",
            gymId = "gym1",
            identity = MemberIdentity(
                name = "Arjun Mehta",
                photoUrl = MediaAssets.MEMBER_ARJUN,
                tier = MembershipTier.PREMIUM,
                memberSince = now - DAY * 400,
                code = "BG105",
                isVerified = true
            ),
            membership = MembershipStatus(
                planName = "Premium Plan",
                planType = "12 Months",
                isActive = true,
                daysRemaining = 102,
                startDate = now - DAY * 263,
                expiryDate = now + DAY * 102,
                currentCost = 28_000.0,
                renewalCount = 2
            ),
            attendance = AttendanceSummary(
                visits = 22,
                target = 26,
                periodName = "Current Month",
                lifetimeVisits = 310,
                streakDays = 8,
                avgVisitsPerWeek = 4.5,
                weeklyPattern = listOf(1, 1, 1, 1, 1, 0, 1),
                lastVisitAt = now
            ),
            payment = PaymentSummary(
                totalOutstanding = 0.0,
                overdueDays = 0,
                dueDate = now + DAY * 102,
                lastPaymentAmount = 28000.0,
                lastPaymentDate = now - DAY * 263,
                lastPaymentMethod = "UPI",
                lifetimePaid = 56_000.0,
                lifecycle = PaymentLifecycle.PAID,
                breakdown = emptyList(),
                history = emptyList()
            ),
            trainer = TrainerSummary("Vikas Yadav", MediaAssets.TRAINER_VIKAS, 20, 18, now + DAY, "Hypertrophy Push", now - DAY),
            workout = WorkoutSummary(now, "Cyber High-Intensity Chest", 75),
            supplements = SupplementSummary(true, "Iso-Whey Zero", now - DAY * 5, 5999.0, "BioTech", MediaAssets.WHEY_PRODUCT),
            nutrition = NutritionSummary(true, "Neon Pro Diet", now + DAY * 30, 2500.0),
            services = listOf(ServiceSummary("VIP Locker", true, now + DAY * 102, 1000.0)),
            recentEvents = listOf(MemberEvent("e2", "2", "gym1", EventType.WORKOUT, now, EventSource.MEMBER)),
            issues = emptyList()
        )
        val event = MemberEvent("live_2", "2", "gym1", EventType.WORKOUT, now, EventSource.MEMBER)
        return snapshot to event
    }

    // 3. Riya Kapoor (BG310) - MINIMAL DARK
    fun minimalDarkRiya(now: Long = System.currentTimeMillis()): Pair<MemberSnapshot, MemberEvent> {
        val snapshot = MemberSnapshot(
            id = "3",
            gymId = "gym1",
            identity = MemberIdentity(
                name = "Riya Kapoor",
                photoUrl = MediaAssets.MEMBER_RIYA,
                tier = MembershipTier.NORMAL,
                memberSince = now - DAY * 180,
                code = "BG310",
                isVerified = true
            ),
            membership = MembershipStatus(
                planName = "Silver Plan",
                planType = "6 Months",
                isActive = true,
                daysRemaining = 28,
                startDate = now - DAY * 152,
                expiryDate = now + DAY * 28,
                currentCost = 15_000.0,
                renewalCount = 0
            ),
            attendance = AttendanceSummary(
                visits = 12,
                target = 18,
                periodName = "Current Month",
                lifetimeVisits = 95,
                streakDays = 3,
                avgVisitsPerWeek = 2.8,
                weeklyPattern = listOf(1, 0, 1, 0, 1, 0, 1),
                lastVisitAt = now
            ),
            payment = PaymentSummary(
                totalOutstanding = 0.0,
                overdueDays = 0,
                dueDate = now + DAY * 28,
                lastPaymentAmount = 15000.0,
                lastPaymentDate = now - DAY * 152,
                lastPaymentMethod = "Card",
                lifetimePaid = 15_000.0,
                lifecycle = PaymentLifecycle.PAID,
                breakdown = emptyList(),
                history = emptyList()
            ),
            trainer = TrainerSummary("Ananya Roy", null, 10, 8, now + DAY, "Pilates & Core", now - DAY * 3),
            workout = WorkoutSummary(now - DAY, "Pilates Core Routine", 45),
            supplements = SupplementSummary(false, "", 0L, 0.0, "", null),
            nutrition = NutritionSummary(false, "", 0L, 0.0),
            services = emptyList(),
            recentEvents = listOf(MemberEvent("e3", "3", "gym1", EventType.TRAINER_SESSION, now, EventSource.TRAINER)),
            issues = emptyList()
        )
        val event = MemberEvent("live_3", "3", "gym1", EventType.TRAINER_SESSION, now, EventSource.TRAINER)
        return snapshot to event
    }

    // 4. Neha Sharma (BG407) - GLASSMORPHISM
    fun glassmorphismNeha(now: Long = System.currentTimeMillis()): Pair<MemberSnapshot, MemberEvent> {
        val snapshot = MemberSnapshot(
            id = "4",
            gymId = "gym1",
            identity = MemberIdentity(
                name = "Neha Sharma",
                photoUrl = MediaAssets.MEMBER_NEHA,
                tier = MembershipTier.PREMIUM,
                memberSince = now - DAY * 290,
                code = "BG407",
                isVerified = true
            ),
            membership = MembershipStatus(
                planName = "Premium Plan",
                planType = "12 Months",
                isActive = true,
                daysRemaining = 75,
                startDate = now - DAY * 290,
                expiryDate = now + DAY * 75,
                currentCost = 26_000.0,
                renewalCount = 1
            ),
            attendance = AttendanceSummary(
                visits = 18,
                target = 26,
                periodName = "Current Month",
                lifetimeVisits = 180,
                streakDays = 5,
                avgVisitsPerWeek = 3.8,
                weeklyPattern = listOf(1, 1, 1, 0, 1, 1, 0),
                lastVisitAt = now
            ),
            payment = PaymentSummary(
                totalOutstanding = 1200.0,
                overdueDays = 0,
                dueDate = now + DAY * 18,
                lastPaymentAmount = 2400.0,
                lastPaymentDate = now - DAY * 45,
                lastPaymentMethod = "UPI",
                lifetimePaid = 28_400.0,
                lifecycle = PaymentLifecycle.DUE,
                breakdown = listOf(PaymentBreakdownLine("Nutrition Installment", 1200.0)),
                history = emptyList()
            ),
            trainer = TrainerSummary("Sameer Joshi", null, 15, 12, now + DAY * 2, "Nutrition & Cardio", now - DAY),
            workout = WorkoutSummary(now, "Mind & Body Flow", 50),
            supplements = SupplementSummary(true, "Plant Protein Vanilla", now - DAY * 15, 2899.0, "Oziva", null),
            nutrition = NutritionSummary(true, "Holistic Wellness Diet", now + DAY * 18, 1200.0),
            services = listOf(ServiceSummary("Steam & Sauna Access", true, now + DAY * 75, 1200.0)),
            recentEvents = listOf(MemberEvent("e4", "4", "gym1", EventType.CHECK_IN, now, EventSource.GATE)),
            issues = emptyList()
        )
        val event = MemberEvent("live_4", "4", "gym1", EventType.CHECK_IN, now, EventSource.GATE)
        return snapshot to event
    }

    // 5. Kabir Khan (BG001) - PREMIUM 3D
    fun premium3dKabir(now: Long = System.currentTimeMillis()): Pair<MemberSnapshot, MemberEvent> {
        val snapshot = MemberSnapshot(
            id = "5",
            gymId = "gym1",
            identity = MemberIdentity(
                name = "Kabir Khan",
                photoUrl = MediaAssets.MEMBER_KABIR,
                tier = MembershipTier.VIP,
                memberSince = now - DAY * 730,
                code = "BG001",
                isVerified = true
            ),
            membership = MembershipStatus(
                planName = "VIP Plan",
                planType = "12 Months",
                isActive = true,
                daysRemaining = 240,
                startDate = now - DAY * 125,
                expiryDate = now + DAY * 240,
                currentCost = 60_000.0,
                renewalCount = 3
            ),
            attendance = AttendanceSummary(
                visits = 24,
                target = 26,
                periodName = "Current Month",
                lifetimeVisits = 540,
                streakDays = 12,
                avgVisitsPerWeek = 5.2,
                weeklyPattern = listOf(1, 1, 1, 1, 1, 1, 0),
                lastVisitAt = now
            ),
            payment = PaymentSummary(
                totalOutstanding = 0.0,
                overdueDays = 0,
                dueDate = now + DAY * 240,
                lastPaymentAmount = 60000.0,
                lastPaymentDate = now - DAY * 125,
                lastPaymentMethod = "Amex",
                lifetimePaid = 180_000.0,
                lifecycle = PaymentLifecycle.PAID,
                breakdown = emptyList(),
                history = emptyList()
            ),
            trainer = TrainerSummary("Master Coach Vikram", null, 50, 42, now + DAY, "VIP Elite Conditioning", now),
            workout = WorkoutSummary(now, "Olympic Strength & Power", 90),
            supplements = SupplementSummary(true, "Gold Standard 100% Isolate", now - DAY * 2, 7999.0, "ON", null),
            nutrition = NutritionSummary(true, "Custom Chef Macro Plan", now + DAY * 30, 8000.0),
            services = listOf(
                ServiceSummary("Executive Gold Locker", true, now + DAY * 240, 5000.0),
                ServiceSummary("Private Valet & Lounge", true, now + DAY * 240, 10000.0)
            ),
            recentEvents = listOf(MemberEvent("e5", "5", "gym1", EventType.CHECK_IN, now, EventSource.GATE)),
            issues = emptyList()
        )
        val event = MemberEvent("live_5", "5", "gym1", EventType.CHECK_IN, now, EventSource.GATE)
        return snapshot to event
    }

    // 6. Aarav Patel (BG220) - VIBRANT GRADIENT
    fun vibrantGradientAarav(now: Long = System.currentTimeMillis()): Pair<MemberSnapshot, MemberEvent> {
        val snapshot = MemberSnapshot(
            id = "6",
            gymId = "gym1",
            identity = MemberIdentity(
                name = "Aarav Patel",
                photoUrl = MediaAssets.MEMBER_AARAV,
                tier = MembershipTier.NORMAL,
                memberSince = now - DAY * 60,
                code = "BG220",
                isVerified = true
            ),
            membership = MembershipStatus(
                planName = "Normal Plan",
                planType = "3 Months",
                isActive = true,
                daysRemaining = 15,
                startDate = now - DAY * 75,
                expiryDate = now + DAY * 15,
                currentCost = 6_000.0,
                renewalCount = 0
            ),
            attendance = AttendanceSummary(
                visits = 10,
                target = 12,
                periodName = "Current Month",
                lifetimeVisits = 35,
                streakDays = 2,
                avgVisitsPerWeek = 2.4,
                weeklyPattern = listOf(0, 1, 0, 1, 0, 1, 0),
                lastVisitAt = now
            ),
            payment = PaymentSummary(
                totalOutstanding = 2000.0,
                overdueDays = 2,
                dueDate = now - DAY * 2,
                lastPaymentAmount = 2000.0,
                lastPaymentDate = now - DAY * 30,
                lastPaymentMethod = "UPI",
                lifetimePaid = 4_000.0,
                lifecycle = PaymentLifecycle.OVERDUE,
                breakdown = listOf(PaymentBreakdownLine("Monthly Due", 2000.0)),
                history = emptyList()
            ),
            trainer = TrainerSummary("Rohan Dev", null, 6, 4, now + DAY * 2, "Full Body Circuit", now - DAY * 2),
            workout = WorkoutSummary(now, "Dynamic Calisthenics", 55),
            supplements = SupplementSummary(false, "", 0L, 0.0, "", null),
            nutrition = NutritionSummary(false, "", 0L, 0.0),
            services = emptyList(),
            recentEvents = listOf(MemberEvent("e6", "6", "gym1", EventType.PAYMENT, now - DAY * 2, EventSource.PAYMENT)),
            issues = listOf(MemberIssue("i6", "Payment Due: ₹2,000 (2 days)", IssueSeverity.HIGH))
        )
        val event = MemberEvent("live_6", "6", "gym1", EventType.PAYMENT, now - DAY * 2, EventSource.PAYMENT)
        return snapshot to event
    }

    // 7. Rohan Verma (BG330) - GYM BEAST MODE
    fun beastModeRohan(now: Long = System.currentTimeMillis()): Pair<MemberSnapshot, MemberEvent> {
        val snapshot = MemberSnapshot(
            id = "7",
            gymId = "gym1",
            identity = MemberIdentity(
                name = "Rohan Verma",
                photoUrl = MediaAssets.MEMBER_ROHAN,
                tier = MembershipTier.PREMIUM,
                memberSince = now - DAY * 500,
                code = "BG330",
                isVerified = true
            ),
            membership = MembershipStatus(
                planName = "Premium Plan",
                planType = "12 Months",
                isActive = true,
                daysRemaining = 65,
                startDate = now - DAY * 300,
                expiryDate = now + DAY * 65,
                currentCost = 30_000.0,
                renewalCount = 2
            ),
            attendance = AttendanceSummary(
                visits = 20,
                target = 26,
                periodName = "Current Month",
                lifetimeVisits = 410,
                streakDays = 9,
                avgVisitsPerWeek = 4.8,
                weeklyPattern = listOf(1, 1, 1, 1, 1, 0, 1),
                lastVisitAt = now
            ),
            payment = PaymentSummary(
                totalOutstanding = 500.0,
                overdueDays = 5,
                dueDate = now - DAY * 5,
                lastPaymentAmount = 2500.0,
                lastPaymentDate = now - DAY * 25,
                lastPaymentMethod = "UPI",
                lifetimePaid = 60_000.0,
                lifecycle = PaymentLifecycle.OVERDUE,
                breakdown = listOf(PaymentBreakdownLine("Locker Fee Due", 500.0)),
                history = emptyList()
            ),
            trainer = TrainerSummary("Hardcore Tarun", null, 24, 20, now + DAY, "Heavy Squat & Deadlift", now - DAY),
            workout = WorkoutSummary(now, "Beast Chest & Heavy Back", 85),
            supplements = SupplementSummary(true, "Creatine Monohydrate & Pre-Workout", now - DAY * 4, 3499.0, "GNC", null),
            nutrition = NutritionSummary(true, "Beast Bulking Diet", now + DAY * 15, 3000.0),
            services = listOf(ServiceSummary("Heavy Duty Locker", true, now + DAY * 65, 500.0)),
            recentEvents = listOf(MemberEvent("e7", "7", "gym1", EventType.CHECK_IN, now, EventSource.GATE)),
            issues = listOf(MemberIssue("i7", "Payment Due: ₹500 (5 days)", IssueSeverity.MEDIUM))
        )
        val event = MemberEvent("live_7", "7", "gym1", EventType.CHECK_IN, now, EventSource.GATE)
        return snapshot to event
    }

    // 8. Simran Kaur (BG502) - PURPLE ROYAL
    fun purpleRoyalSimran(now: Long = System.currentTimeMillis()): Pair<MemberSnapshot, MemberEvent> {
        val snapshot = MemberSnapshot(
            id = "8",
            gymId = "gym1",
            identity = MemberIdentity(
                name = "Simran Kaur",
                photoUrl = MediaAssets.MEMBER_SIMRAN,
                tier = MembershipTier.PREMIUM,
                memberSince = now - DAY * 320,
                code = "BG502",
                isVerified = true
            ),
            membership = MembershipStatus(
                planName = "Premium Plan",
                planType = "12 Months",
                isActive = true,
                daysRemaining = 95,
                startDate = now - DAY * 270,
                expiryDate = now + DAY * 95,
                currentCost = 32_000.0,
                renewalCount = 1
            ),
            attendance = AttendanceSummary(
                visits = 14,
                target = 20,
                periodName = "Current Month",
                lifetimeVisits = 190,
                streakDays = 6,
                avgVisitsPerWeek = 3.5,
                weeklyPattern = listOf(1, 1, 0, 1, 1, 0, 1),
                lastVisitAt = now
            ),
            payment = PaymentSummary(
                totalOutstanding = 0.0,
                overdueDays = 0,
                dueDate = now + DAY * 95,
                lastPaymentAmount = 32000.0,
                lastPaymentDate = now - DAY * 270,
                lastPaymentMethod = "UPI",
                lifetimePaid = 32_000.0,
                lifecycle = PaymentLifecycle.PAID,
                breakdown = emptyList(),
                history = emptyList()
            ),
            trainer = TrainerSummary("Elena Smith", null, 16, 14, now + DAY * 2, "Royal Glute & Flexibility", now - DAY),
            workout = WorkoutSummary(now, "Royal Sculpt & Tone", 65),
            supplements = SupplementSummary(true, "Collagen & BCAA Complex", now - DAY * 8, 3999.0, "Vital Proteins", null),
            nutrition = NutritionSummary(true, "Clean Glow Nutrition", now + DAY * 25, 2800.0),
            services = listOf(ServiceSummary("Royal Spa Suite", true, now + DAY * 95, 2000.0)),
            recentEvents = listOf(MemberEvent("e8", "8", "gym1", EventType.TRAINER_SESSION, now, EventSource.TRAINER)),
            issues = emptyList()
        )
        val event = MemberEvent("live_8", "8", "gym1", EventType.TRAINER_SESSION, now, EventSource.TRAINER)
        return snapshot to event
    }

    // 9. Vikram Rathore (BG901) - EXPIRED & OVERDUE
    fun expiredVikram(now: Long = System.currentTimeMillis()): Pair<MemberSnapshot, MemberEvent> {
        val snapshot = MemberSnapshot(
            id = "9",
            gymId = "gym1",
            identity = MemberIdentity(
                name = "Vikram Rathore",
                photoUrl = null,
                tier = MembershipTier.NORMAL,
                memberSince = now - DAY * 420,
                code = "BG901",
                isVerified = false
            ),
            membership = MembershipStatus(
                planName = "Gold Plan",
                planType = "12 Months",
                isActive = false,
                daysRemaining = 0,
                startDate = now - DAY * 370,
                expiryDate = now - DAY * 5,
                currentCost = 22_000.0,
                renewalCount = 1,
                lifecycle = MembershipLifecycle.EXPIRED
            ),
            attendance = AttendanceSummary(
                visits = 18,
                target = 26,
                periodName = "Previous Month",
                lifetimeVisits = 142,
                streakDays = 0,
                avgVisitsPerWeek = 2.1,
                weeklyPattern = listOf(0, 0, 0, 0, 0, 0, 0),
                lastVisitAt = now - DAY * 5
            ),
            payment = PaymentSummary(
                totalOutstanding = 22000.0,
                overdueDays = 5,
                dueDate = now - DAY * 5,
                lastPaymentAmount = 22000.0,
                lastPaymentDate = now - DAY * 370,
                lastPaymentMethod = "UPI",
                lifetimePaid = 22_000.0,
                lifecycle = PaymentLifecycle.OVERDUE,
                breakdown = listOf(PaymentBreakdownLine("Gold Plan Renewal Due", 22000.0)),
                history = listOf(PaymentTransaction("p9", 22000.0, now - DAY * 370, "UPI", "Plan"))
            ),
            trainer = null,
            workout = WorkoutSummary(now - DAY * 5, "Powerlifting Basics", 50),
            supplements = SupplementSummary(false, "", 0L, 0.0, "", null),
            nutrition = NutritionSummary(false, "", 0L, 0.0),
            services = emptyList(),
            recentEvents = listOf(MemberEvent("e9", "9", "gym1", EventType.CHECK_IN, now - DAY * 5, EventSource.GATE)),
            issues = listOf(MemberIssue("i9", "Membership Expired: Renewal Required", IssueSeverity.CRITICAL))
        )
        val event = MemberEvent("live_9", "9", "gym1", EventType.CHECK_IN, now - DAY * 5, EventSource.GATE)
        return snapshot to event
    }

    // Default scenario for backwards compatibility
    fun overdueActiveMember(
        memberId: String = "1",
        now: Long = System.currentTimeMillis()
    ): Pair<MemberSnapshot, MemberEvent> = naturalFreshYash(now)
}
