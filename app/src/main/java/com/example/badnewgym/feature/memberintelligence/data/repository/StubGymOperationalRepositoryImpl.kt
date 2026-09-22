package com.example.badnewgym.feature.memberintelligence.data.repository

import com.example.badnewgym.feature.memberintelligence.domain.model.GymLiveSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.GymOperationalCategory
import com.example.badnewgym.feature.memberintelligence.domain.model.GymOperationalEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.GymOperationalStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority
import com.example.badnewgym.feature.memberintelligence.domain.repository.GymOperationalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StubGymOperationalRepositoryImpl : GymOperationalRepository {
    override suspend fun getLiveSnapshot(gymId: String): Result<GymLiveSnapshot> {
        delay(40)
        return Result.success(
            GymLiveSnapshot(
                activeMembers = 34,
                checkInsToday = 61,
                checkOutsToday = 27,
                openMaintenance = 2,
                activeTrainers = 5,
                powerStatus = "ONLINE",
                todayRevenue = 38250.0,
                todayPayments = 17,
                unresolvedAlerts = 3
            )
        )
    }

    override suspend fun getRecentEvents(gymId: String, limit: Int): Result<List<GymOperationalEvent>> {
        delay(40)
        return Result.success(sampleEvents(System.currentTimeMillis()).take(limit.coerceAtLeast(1)))
    }

    override fun observeEvents(gymId: String): Flow<GymOperationalEvent> = flow {
        // Demo stream. The production adapter should map Supabase Realtime
        // rows into the same immutable event contract.
        delay(10_000)
        emit(
            GymOperationalEvent(
                id = "power-restored",
                gymId = gymId,
                category = GymOperationalCategory.POWER,
                status = GymOperationalStatus.SUCCESS,
                severity = SignalPriority.P2_IMPORTANT,
                title = "Power restored",
                detail = "Gym power is back online",
                location = "Main Branch",
                occurredAt = System.currentTimeMillis()
            )
        )
    }

    private fun sampleEvents(now: Long): List<GymOperationalEvent> = listOf(
        GymOperationalEvent(
            id = "member-checkin",
            gymId = "gym1",
            category = GymOperationalCategory.ACCESS,
            status = GymOperationalStatus.LIVE,
            severity = SignalPriority.P2_IMPORTANT,
            title = "Yash checked in",
            detail = "Gate A • member access verified",
            location = "Main Gate",
            actor = "Gate scanner",
            occurredAt = now - 2 * 60_000,
            relatedMemberId = "BG204"
        ),
        GymOperationalEvent(
            id = "payment-success",
            gymId = "gym1",
            category = GymOperationalCategory.PAYMENT,
            status = GymOperationalStatus.SUCCESS,
            severity = SignalPriority.P2_IMPORTANT,
            title = "Payment received",
            detail = "₹4,500 membership payment",
            location = "Front Desk",
            actor = "Staff",
            occurredAt = now - 7 * 60_000,
            relatedMemberId = "BG204",
            amount = 4500.0
        ),
        GymOperationalEvent(
            id = "trainer-scheduled",
            gymId = "gym1",
            category = GymOperationalCategory.TRAINER,
            status = GymOperationalStatus.SCHEDULED,
            severity = SignalPriority.P2_IMPORTANT,
            title = "PT session scheduled",
            detail = "Vikas Yadav • 7:00 AM tomorrow",
            location = "Studio 1",
            actor = "Trainer desk",
            occurredAt = now - 13 * 60_000,
            relatedMemberId = "BG204"
        ),
        GymOperationalEvent(
            id = "treadmill-fault",
            gymId = "gym1",
            category = GymOperationalCategory.EQUIPMENT,
            status = GymOperationalStatus.IN_PROGRESS,
            severity = SignalPriority.P1_ACTION_REQUIRED,
            title = "Treadmill #04 fault",
            detail = "Incline sensor error • technician assigned",
            location = "Cardio Zone",
            actor = "Maintenance",
            occurredAt = now - 19 * 60_000,
            relatedAssetId = "TM-04"
        ),
        GymOperationalEvent(
            id = "cleaning",
            gymId = "gym1",
            category = GymOperationalCategory.CLEANING,
            status = GymOperationalStatus.RESOLVED,
            severity = SignalPriority.P3_BACKGROUND,
            title = "Locker room cleaned",
            detail = "Sanitation checklist completed",
            location = "Locker Room",
            actor = "Housekeeping",
            occurredAt = now - 27 * 60_000
        ),
        GymOperationalEvent(
            id = "power-cut",
            gymId = "gym1",
            category = GymOperationalCategory.POWER,
            status = GymOperationalStatus.RESOLVED,
            severity = SignalPriority.P1_ACTION_REQUIRED,
            title = "Power interruption",
            detail = "Backup supply carried the load for 4 min",
            location = "Main Branch",
            actor = "IoT power monitor",
            occurredAt = now - 41 * 60_000
        ),
        GymOperationalEvent(
            id = "mirror-install",
            gymId = "gym1",
            category = GymOperationalCategory.FACILITY,
            status = GymOperationalStatus.IN_PROGRESS,
            severity = SignalPriority.P2_IMPORTANT,
            title = "Studio mirror installation",
            detail = "₹18,500 budget • 60% completed",
            location = "Functional Studio",
            actor = "Vendor",
            occurredAt = now - 55 * 60_000,
            amount = 18500.0
        ),
        GymOperationalEvent(
            id = "electricity-bill",
            gymId = "gym1",
            category = GymOperationalCategory.FINANCE,
            status = GymOperationalStatus.SUCCESS,
            severity = SignalPriority.P2_IMPORTANT,
            title = "Electricity bill paid",
            detail = "₹12,840 • GST/tax recorded in ledger",
            location = "Utility Account",
            actor = "Owner",
            occurredAt = now - 75 * 60_000,
            amount = 12840.0
        )
    )
}
