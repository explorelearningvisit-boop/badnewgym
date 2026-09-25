package com.example.badnewgym.feature.memberintelligence.data.repository

import com.example.badnewgym.feature.memberintelligence.domain.model.*
import com.example.badnewgym.feature.memberintelligence.domain.repository.TemporalIntelligenceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

class StubTemporalIntelligenceRepositoryImpl(
    private val cache: TemporalMemoryCache = TemporalMemoryCache()
) : TemporalIntelligenceRepository {

    override suspend fun getAttendanceSummary(
        memberId: String,
        range: TemporalRange
    ): Result<AttendanceTemporalSummary> {
        val cacheKey = "att_summary_${memberId}_${range.startMillis}_${range.endMillis}_${range.granularity}"
        val cached = cache.getSummary<AttendanceTemporalSummary>(cacheKey)
        if (cached != null) {
            return Result.success(cached)
        }

        val startTime = System.currentTimeMillis()
        delay(40) // Simulate network/db query latency

        val daysCount = ((range.endMillis - range.startMillis) / (24 * 60 * 60 * 1000)).toInt().coerceAtLeast(1)
        val visits = (daysCount * 0.75).toInt().coerceAtLeast(1)
        val late = (visits * 0.1).toInt()
        val missed = (daysCount - visits).coerceAtLeast(0)

        val dailyMap = mutableMapOf<String, Int>()
        val cal = Calendar.getInstance().apply { timeInMillis = range.startMillis }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        while (cal.timeInMillis <= range.endMillis) {
            val dateStr = dateFormat.format(cal.time)
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            dailyMap[dateStr] = if (dayOfWeek != Calendar.SUNDAY) 1 else 0
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        val summary = AttendanceTemporalSummary(
            totalVisits = visits,
            lateVisits = late,
            missedVisits = missed,
            averageDurationMinutes = 68,
            peakWeekday = "Monday",
            peakHour = "07:30 - 08:45",
            attendanceRate = visits.toFloat() / daysCount,
            sourceBreakdown = mapOf("FACE" to (visits * 0.8).toInt(), "QR" to (visits * 0.15).toInt(), "STAFF" to (visits * 0.05).toInt()),
            dailyAttendanceMap = dailyMap
        )

        cache.putSummary(cacheKey, summary)
        cache.telemetry.recordMiss(bytes = 640L, latency = System.currentTimeMillis() - startTime)
        return Result.success(summary)
    }

    override suspend fun getAttendanceEvents(
        memberId: String,
        range: TemporalRange
    ): Result<TemporalPage<TemporalEventRecord>> {
        val cacheKey = "att_events_${memberId}_${range.startMillis}_${range.endMillis}_${range.cursor}"
        val cached = cache.getPage<TemporalPage<TemporalEventRecord>>(cacheKey)
        if (cached != null) {
            return Result.success(cached.copy(isFromCache = true))
        }

        val startTime = System.currentTimeMillis()
        delay(60)

        val events = generateAttendanceEventsForRange(memberId, range)
        val page = TemporalPage(
            items = events,
            range = range,
            nextCursor = if (events.size >= range.limit) "cursor_${System.currentTimeMillis()}" else null,
            totalCount = events.size,
            isFromCache = false,
            cachedAt = System.currentTimeMillis()
        )

        cache.putPage(cacheKey, page)
        cache.telemetry.recordMiss(bytes = (events.size * 220L).coerceAtLeast(400L), latency = System.currentTimeMillis() - startTime)
        return Result.success(page)
    }

    override suspend fun getPaymentSummary(
        memberId: String,
        range: TemporalRange
    ): Result<PaymentTemporalSummary> {
        val cacheKey = "pay_summary_${memberId}_${range.startMillis}_${range.endMillis}_${range.granularity}"
        val cached = cache.getSummary<PaymentTemporalSummary>(cacheKey)
        if (cached != null) {
            return Result.success(cached)
        }

        val startTime = System.currentTimeMillis()
        delay(40)

        val monthlyTotals = mapOf(
            "2026-07" to 15000.0,
            "2026-08" to 15000.0,
            "2026-09" to 15000.0
        )

        val isOverdueMember = memberId == "9" || memberId == "BG901"
        val summary = PaymentTemporalSummary(
            totalPaid = if (isOverdueMember) 28000.0 else 45000.0,
            totalDue = if (isOverdueMember) 3500.0 else 0.0,
            totalOverdue = if (isOverdueMember) 3500.0 else 0.0,
            transactionCount = 3,
            currency = "INR",
            lastPaymentAt = System.currentTimeMillis() - (12L * 24 * 60 * 60 * 1000),
            monthlyTotals = monthlyTotals
        )

        cache.putSummary(cacheKey, summary)
        cache.telemetry.recordMiss(bytes = 480L, latency = System.currentTimeMillis() - startTime)
        return Result.success(summary)
    }

    override suspend fun getPaymentEvents(
        memberId: String,
        range: TemporalRange
    ): Result<TemporalPage<TemporalEventRecord>> {
        val cacheKey = "pay_events_${memberId}_${range.startMillis}_${range.endMillis}"
        val cached = cache.getPage<TemporalPage<TemporalEventRecord>>(cacheKey)
        if (cached != null) {
            return Result.success(cached.copy(isFromCache = true))
        }

        val startTime = System.currentTimeMillis()
        delay(50)

        val isOverdue = memberId == "9" || memberId == "BG901"
        val events = listOf(
            TemporalEventRecord(
                eventId = "pay_001",
                memberId = memberId,
                eventType = EventType.PAYMENT,
                occurredAt = System.currentTimeMillis() - (12L * 24 * 60 * 60 * 1000) + 3600000L * 10,
                source = EventSource.PAYMENT,
                status = "SUCCESS",
                title = "Quarterly Elite Renewal",
                subtitle = "Paid via Razorpay UPI",
                amount = 15000.0,
                currency = "INR",
                referenceId = "TXN_RZP_98231908",
                invoiceNumber = "INV-2026-0902",
                paymentMethod = "UPI AutoPay",
                details = mapOf(
                    "Plan" to "Quarterly Elite",
                    "Gateway" to "Razorpay",
                    "GST Number" to "27AAAAA0000A1Z5",
                    "Tax Amount" to "₹2,288.14"
                )
            ),
            TemporalEventRecord(
                eventId = "pay_002",
                memberId = memberId,
                eventType = if (isOverdue) EventType.PAYMENT_FAILED else EventType.PAYMENT,
                occurredAt = System.currentTimeMillis() - (45L * 24 * 60 * 60 * 1000),
                source = EventSource.PAYMENT,
                status = if (isOverdue) "FAILED" else "SUCCESS",
                title = if (isOverdue) "Monthly Locker Fee" else "PT 10-Session Pack",
                subtitle = if (isOverdue) "Payment Declined by Bank" else "HDFC NetBanking",
                amount = if (isOverdue) 3500.0 else 8000.0,
                currency = "INR",
                referenceId = "TXN_HDFC_4719280",
                invoiceNumber = "INV-2026-0814",
                paymentMethod = if (isOverdue) "Auto-Debit" else "NetBanking",
                details = mapOf(
                    "Item" to if (isOverdue) "Locker Rental" else "PT Package",
                    "Bank Ref" to "HDFC889921",
                    "Failure Reason" to if (isOverdue) "Insufficient Funds" else "None"
                )
            )
        )

        val page = TemporalPage(
            items = events,
            range = range,
            nextCursor = null,
            totalCount = events.size,
            isFromCache = false,
            cachedAt = System.currentTimeMillis()
        )

        cache.putPage(cacheKey, page)
        cache.telemetry.recordMiss(bytes = 780L, latency = System.currentTimeMillis() - startTime)
        return Result.success(page)
    }

    override suspend fun getWorkoutSummary(
        memberId: String,
        range: TemporalRange
    ): Result<WorkoutTemporalSummary> {
        val cacheKey = "wo_summary_${memberId}_${range.startMillis}_${range.endMillis}"
        val cached = cache.getSummary<WorkoutTemporalSummary>(cacheKey)
        if (cached != null) return Result.success(cached)

        val startTime = System.currentTimeMillis()
        delay(40)

        val summary = WorkoutTemporalSummary(
            totalSessions = 14,
            totalSetsCompleted = 84,
            totalVolumeKg = 24650.0,
            averageDurationMinutes = 58,
            consistencyScore = 0.94f,
            topMuscles = listOf("Chest (32%)", "Back (28%)", "Shoulders (22%)", "Legs (18%)")
        )
        cache.putSummary(cacheKey, summary)
        cache.telemetry.recordMiss(bytes = 380L, latency = System.currentTimeMillis() - startTime)
        return Result.success(summary)
    }

    override suspend fun getWorkoutEvents(
        memberId: String,
        range: TemporalRange
    ): Result<TemporalPage<TemporalEventRecord>> {
        val cacheKey = "wo_events_${memberId}_${range.startMillis}_${range.endMillis}"
        val cached = cache.getPage<TemporalPage<TemporalEventRecord>>(cacheKey)
        if (cached != null) return Result.success(cached.copy(isFromCache = true))

        val startTime = System.currentTimeMillis()
        delay(50)

        val events = listOf(
            TemporalEventRecord(
                eventId = "wo_001",
                memberId = memberId,
                eventType = EventType.WORKOUT,
                occurredAt = System.currentTimeMillis() - (1L * 24 * 60 * 60 * 1000) + 3600000L * 8,
                source = EventSource.MEMBER,
                status = "COMPLETED",
                title = "Push Day A (Hypertrophy)",
                subtitle = "6 exercises • 18 sets • 62 mins",
                durationMinutes = 62,
                details = mapOf(
                    "Flat Dumbbell Press" to "4 sets x 10 reps @ 32kg",
                    "Incline Barbell Press" to "4 sets x 8 reps @ 75kg",
                    "Lateral Raises" to "4 sets x 15 reps @ 12kg",
                    "Tricep Rope Pushdown" to "3 sets x 12 reps @ 30kg",
                    "Overhead DB Extension" to "3 sets x 12 reps @ 24kg"
                )
            ),
            TemporalEventRecord(
                eventId = "wo_002",
                memberId = memberId,
                eventType = EventType.WORKOUT,
                occurredAt = System.currentTimeMillis() - (3L * 24 * 60 * 60 * 1000) + 3600000L * 9,
                source = EventSource.MEMBER,
                status = "COMPLETED",
                title = "Pull Day B (Strength)",
                subtitle = "5 exercises • 16 sets • 55 mins",
                durationMinutes = 55,
                details = mapOf(
                    "Conventional Deadlift" to "4 sets x 5 reps @ 150kg",
                    "Weighted Pull-ups" to "4 sets x 6 reps @ +15kg",
                    "Barbell Rows" to "4 sets x 8 reps @ 80kg",
                    "Incline DB Curls" to "4 sets x 10 reps @ 16kg"
                )
            )
        )

        val page = TemporalPage(events, range, null, events.size, false, System.currentTimeMillis())
        cache.putPage(cacheKey, page)
        cache.telemetry.recordMiss(bytes = 620L, latency = System.currentTimeMillis() - startTime)
        return Result.success(page)
    }

    override suspend fun getHistorySummary(
        memberId: String,
        range: TemporalRange
    ): Result<HistoryTemporalSummary> {
        val cacheKey = "hist_summary_${memberId}_${range.startMillis}_${range.endMillis}"
        val cached = cache.getSummary<HistoryTemporalSummary>(cacheKey)
        if (cached != null) return Result.success(cached)

        val startTime = System.currentTimeMillis()
        delay(40)

        val summary = HistoryTemporalSummary(
            totalEvents = 48,
            eventCountsByType = mapOf(
                EventType.CHECK_IN to 26,
                EventType.CHECK_OUT to 26,
                EventType.WORKOUT to 18,
                EventType.PAYMENT to 3,
                EventType.TRAINER_SESSION to 6
            ),
            firstEventAt = range.startMillis,
            lastEventAt = System.currentTimeMillis()
        )
        cache.putSummary(cacheKey, summary)
        cache.telemetry.recordMiss(bytes = 420L, latency = System.currentTimeMillis() - startTime)
        return Result.success(summary)
    }

    override suspend fun getHistoryEvents(
        memberId: String,
        typeFilter: EventType?,
        range: TemporalRange
    ): Result<TemporalPage<TemporalEventRecord>> {
        val cacheKey = "hist_events_${memberId}_${typeFilter}_${range.startMillis}_${range.endMillis}"
        val cached = cache.getPage<TemporalPage<TemporalEventRecord>>(cacheKey)
        if (cached != null) return Result.success(cached.copy(isFromCache = true))

        val startTime = System.currentTimeMillis()
        delay(60)

        val allEvents = mutableListOf<TemporalEventRecord>()

        // Check-in with FACE
        allEvents.add(
            TemporalEventRecord(
                eventId = "hist_001",
                memberId = memberId,
                eventType = EventType.CHECK_IN,
                occurredAt = System.currentTimeMillis() - 7200000L, // 2 hours ago
                source = EventSource.GATE,
                sourceMetadata = SourceVerificationMetadata("FACE", "Turnstile Gate 1", "DEV_CAM_01", "SUCCESS", 120L, 0.99f),
                title = "Check-in via Face Verification",
                subtitle = "Gate 1 • Latency: 120ms • Match: 99%"
            )
        )

        // Workout
        allEvents.add(
            TemporalEventRecord(
                eventId = "hist_002",
                memberId = memberId,
                eventType = EventType.WORKOUT,
                occurredAt = System.currentTimeMillis() - 5400000L,
                source = EventSource.MEMBER,
                title = "Completed Workout: Chest & Triceps",
                subtitle = "Logged via BAD GYM App • 55 mins"
            )
        )

        // Trainer session
        allEvents.add(
            TemporalEventRecord(
                eventId = "hist_003",
                memberId = memberId,
                eventType = EventType.TRAINER_SESSION,
                occurredAt = System.currentTimeMillis() - 86400000L,
                source = EventSource.TRAINER,
                actorName = "Coach Vikram",
                title = "PT Session with Coach Vikram",
                subtitle = "Focus: Form check on Deadlifts & Power Cleans"
            )
        )

        // Payment
        allEvents.add(
            TemporalEventRecord(
                eventId = "hist_004",
                memberId = memberId,
                eventType = EventType.PAYMENT,
                occurredAt = System.currentTimeMillis() - (12L * 86400000L),
                source = EventSource.PAYMENT,
                amount = 15000.0,
                currency = "INR",
                invoiceNumber = "INV-2026-0902",
                title = "Membership Fee Paid",
                subtitle = "₹15,000 via Razorpay UPI AutoPay"
            )
        )

        val filtered = if (typeFilter != null) allEvents.filter { it.eventType == typeFilter } else allEvents
        val page = TemporalPage(filtered, range, null, filtered.size, false, System.currentTimeMillis())

        cache.putPage(cacheKey, page)
        cache.telemetry.recordMiss(bytes = 950L, latency = System.currentTimeMillis() - startTime)
        return Result.success(page)
    }

    override suspend fun getEventDetail(eventId: String): Result<TemporalEventRecord> {
        delay(30)
        val event = TemporalEventRecord(
            eventId = eventId,
            memberId = "BG204",
            eventType = EventType.CHECK_IN,
            occurredAt = System.currentTimeMillis() - 7200000L,
            source = EventSource.GATE,
            sourceMetadata = SourceVerificationMetadata("FACE", "Main Gate Turnstile 1", "CAM_ENTRANCE_01", "SUCCESS", 95L, 0.992f),
            title = "Gate Check-In Verification",
            subtitle = "Biometric Face Recognition matched member profile",
            details = mapOf(
                "Verification Method" to "Face Liveness + Feature Vector Match",
                "Gate" to "Turnstile 1 (Main Entrance)",
                "Device Identifier" to "HIK_VISION_DS-K1T671MF",
                "Match Confidence" to "99.2%",
                "Processing Latency" to "95 ms",
                "Audit Status" to "VERIFIED_VALID"
            )
        )
        return Result.success(event)
    }

    override fun observeLiveAttendance(memberId: String): Flow<TemporalEventRecord> = flow {
        while (true) {
            delay(15_000)
            emit(
                TemporalEventRecord(
                    eventId = "live_att_${System.currentTimeMillis()}",
                    memberId = memberId,
                    eventType = EventType.CHECK_IN,
                    occurredAt = System.currentTimeMillis(),
                    source = EventSource.GATE,
                    sourceMetadata = SourceVerificationMetadata("FACE", "Turnstile 1", "CAM_01", "SUCCESS", 110L, 0.985f),
                    title = "Live Check-in Detected",
                    subtitle = "Face verified at Turnstile 1"
                )
            )
        }
    }

    override fun observeLiveWorkout(memberId: String): Flow<TemporalEventRecord> = flow {
        while (true) {
            delay(20_000)
            emit(
                TemporalEventRecord(
                    eventId = "live_wo_${System.currentTimeMillis()}",
                    memberId = memberId,
                    eventType = EventType.WORKOUT,
                    occurredAt = System.currentTimeMillis(),
                    source = EventSource.MEMBER,
                    title = "Live Workout Set Logged",
                    subtitle = "Bench Press: Set 3 @ 90kg"
                )
            )
        }
    }

    private fun generateAttendanceEventsForRange(
        memberId: String,
        range: TemporalRange
    ): List<TemporalEventRecord> {
        val events = mutableListOf<TemporalEventRecord>()
        val cal = Calendar.getInstance().apply { timeInMillis = range.startMillis }
        var counter = 1

        while (cal.timeInMillis <= range.endMillis && events.size < range.limit) {
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek != Calendar.SUNDAY) {
                // Check in event (with second precision, e.g. 07:42:18)
                val checkInCal = (cal.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, 7)
                    set(Calendar.MINUTE, 35 + (counter % 20))
                    set(Calendar.SECOND, 12 + (counter % 45))
                }
                events.add(
                    TemporalEventRecord(
                        eventId = "att_in_$counter",
                        memberId = memberId,
                        eventType = EventType.CHECK_IN,
                        occurredAt = checkInCal.timeInMillis,
                        source = EventSource.GATE,
                        sourceMetadata = SourceVerificationMetadata(
                            source = if (counter % 5 == 0) "QR" else "FACE",
                            gateName = "Gate 1",
                            deviceId = "GATE_CAM_01",
                            verificationResult = "SUCCESS",
                            confidenceScore = 0.98f
                        ),
                        title = "Check-in (${if (counter % 5 == 0) "QR" else "FACE"})",
                        subtitle = "Gate 1 • ${checkInCal.get(Calendar.HOUR_OF_DAY)}:${String.format("%02d", checkInCal.get(Calendar.MINUTE))}:${String.format("%02d", checkInCal.get(Calendar.SECOND))}"
                    )
                )

                // Check out event
                val checkOutCal = (checkInCal.clone() as Calendar).apply {
                    add(Calendar.MINUTE, 65 + (counter % 25))
                    add(Calendar.SECOND, 30)
                }
                events.add(
                    TemporalEventRecord(
                        eventId = "att_out_$counter",
                        memberId = memberId,
                        eventType = EventType.CHECK_OUT,
                        occurredAt = checkOutCal.timeInMillis,
                        source = EventSource.GATE,
                        sourceMetadata = SourceVerificationMetadata(
                            source = "FACE",
                            gateName = "Gate 1 Exit",
                            deviceId = "GATE_CAM_02",
                            verificationResult = "SUCCESS",
                            confidenceScore = 0.99f
                        ),
                        title = "Check-out (FACE)",
                        subtitle = "Duration: 68 mins"
                    )
                )
                counter++
            }
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return events.sortedByDescending { it.occurredAt }
    }
}
