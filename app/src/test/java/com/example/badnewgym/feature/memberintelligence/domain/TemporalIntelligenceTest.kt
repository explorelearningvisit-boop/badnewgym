package com.example.badnewgym.feature.memberintelligence.domain

import com.example.badnewgym.feature.memberintelligence.data.repository.StubTemporalIntelligenceRepositoryImpl
import com.example.badnewgym.feature.memberintelligence.data.repository.TemporalMemoryCache
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class TemporalIntelligenceTest {

    private lateinit var cache: TemporalMemoryCache
    private lateinit var repository: StubTemporalIntelligenceRepositoryImpl

    @Before
    fun setUp() {
        cache = TemporalMemoryCache()
        repository = StubTemporalIntelligenceRepositoryImpl(cache)
    }

    @Test
    fun testTemporalGranularityProperties() {
        assertTrue(TemporalGranularity.LIVE.isRealtime())
        assertFalse(TemporalGranularity.MONTH.isRealtime())
        assertFalse(TemporalGranularity.DAY.isRealtime())
        assertEquals("Month", TemporalGranularity.MONTH.displayName)
        assertEquals("MO", TemporalGranularity.MONTH.shortLabel)
    }

    @Test
    fun testTemporalRangeFormatting() {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2026)
            set(Calendar.MONTH, Calendar.SEPTEMBER)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val start = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        val end = cal.timeInMillis - 1

        val range = TemporalRange(start, end, TemporalGranularity.MONTH)
        assertTrue(range.formatPeriodLabel().contains("September 2026"))

        val dayRange = TemporalRange(start, start + 86400000L - 1, TemporalGranularity.DAY)
        assertTrue(dayRange.formatPeriodLabel().contains("Sep 2026"))
    }

    @Test
    fun testTemporalCacheHitAndTelemetry() = runBlocking {
        val range = TemporalRange.forCurrentMonth()
        
        // Initial request -> Cache Miss
        val res1 = repository.getAttendanceSummary("BG204", range)
        assertTrue(res1.isSuccess)
        assertEquals(1, cache.telemetry.totalRequests)
        assertEquals(0, cache.telemetry.cacheHits)

        // Second request -> Cache Hit
        val res2 = repository.getAttendanceSummary("BG204", range)
        assertTrue(res2.isSuccess)
        assertEquals(2, cache.telemetry.totalRequests)
        assertEquals(1, cache.telemetry.cacheHits)
        assertEquals(50f, cache.telemetry.hitRatePercent, 0.01f)
    }

    @Test
    fun testAttendanceEventsSecondPrecisionAndSource() = runBlocking {
        val range = TemporalRange.forPast7Days()
        val eventsResult = repository.getAttendanceEvents("BG204", range)
        assertTrue(eventsResult.isSuccess)
        val events = eventsResult.getOrThrow().items
        assertTrue(events.isNotEmpty())

        val checkIn = events.firstOrNull { it.eventType == EventType.CHECK_IN }
        assertNotNull(checkIn)
        assertNotNull(checkIn?.sourceMetadata)
        assertTrue(checkIn?.sourceMetadata?.source in listOf("FACE", "QR", "STAFF"))
        assertTrue(checkIn?.formatTimeOnly(withSeconds = true)?.matches(Regex("\\d{2}:\\d{2}:\\d{2}")) == true)
    }

    @Test
    fun testPaymentSummaryVsEventsOnDemand() = runBlocking {
        val range = TemporalRange.forCurrentMonth()
        val summaryRes = repository.getPaymentSummary("BG204", range)
        assertTrue(summaryRes.isSuccess)
        val summary = summaryRes.getOrThrow()
        assertTrue(summary.totalPaid > 0.0)

        val eventsRes = repository.getPaymentEvents("BG204", range)
        assertTrue(eventsRes.isSuccess)
        val events = eventsRes.getOrThrow().items
        assertTrue(events.isNotEmpty())
        assertEquals(EventType.PAYMENT, events[0].eventType)
        assertNotNull(events[0].invoiceNumber)
    }

    @Test
    fun testHistoryEventsFilter() = runBlocking {
        val range = TemporalRange.forCurrentMonth()
        val allEventsRes = repository.getHistoryEvents("BG204", null, range)
        assertTrue(allEventsRes.isSuccess)
        val allEvents = allEventsRes.getOrThrow().items

        val paymentEventsRes = repository.getHistoryEvents("BG204", EventType.PAYMENT, range)
        assertTrue(paymentEventsRes.isSuccess)
        val paymentEvents = paymentEventsRes.getOrThrow().items

        assertTrue(paymentEvents.all { it.eventType == EventType.PAYMENT })
        assertTrue(allEvents.size >= paymentEvents.size)
    }

    @Test
    fun testDataRetentionPolicyDefaults() {
        val policy = DataRetentionPolicy(
            category = "ATTENDANCE_LOGS",
            hotRetentionDays = 90,
            warmRetentionDays = 365,
            coldArchiveDays = 1825
        )
        assertEquals(90, policy.hotRetentionDays)
        assertEquals(365, policy.warmRetentionDays)
        assertEquals(1825, policy.coldArchiveDays)
        assertTrue(policy.requiresExplicitAuditRecord)
    }
}
