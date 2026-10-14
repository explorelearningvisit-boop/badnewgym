package com.example.badnewgym.feature.memberintelligence.presentation.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * The identity context strip must describe a verified visit state and must
 * never simply repeat the LIVE badge the session hero already carries.
 */
class MemberContextTest {

    private val now = 1_800_000_000_000L
    private val todayStart = now - 5 * 60 * 60 * 1000L // 05:00 this morning

    private fun checkIn(
        liveStartMs: Long? = null,
        lastStartMs: Long? = null,
        lastEndMs: Long? = null,
        lastDurationMs: Long? = null,
        visitNumberToday: Int? = null
    ) = HomeCheckIn(
        liveStartMs = liveStartMs,
        methodLabel = null,
        visitNumberToday = visitNumberToday,
        lastStartMs = lastStartMs,
        lastEndMs = lastEndMs,
        lastDurationMs = lastDurationMs
    )

    @Test
    fun aFreshCheckInReadsAsJustCheckedIn() {
        val ctx = memberContext(
            checkIn(liveStartMs = now - 5 * 60 * 1000L, visitNumberToday = 1),
            todayStart, now
        )
        assertEquals(MemberContext.JUST_CHECKED_IN, ctx)
    }

    @Test
    fun anOlderLiveSessionIsNotDuplicatedInTheStrip() {
        // The hero already shows LIVE prominently; the strip stays silent.
        val ctx = memberContext(
            checkIn(liveStartMs = now - 90 * 60 * 1000L, visitNumberToday = 1),
            todayStart, now
        )
        assertNull(ctx)
    }

    @Test
    fun aFinishedVisitTodayReadsAsCompleted() {
        val ctx = memberContext(
            checkIn(lastStartMs = todayStart + 60 * 60 * 1000L, lastEndMs = now - 60 * 60 * 1000L, lastDurationMs = 45 * 60 * 1000L),
            todayStart, now
        )
        assertEquals(MemberContext.COMPLETED_VISIT, ctx)
    }

    @Test
    fun anOldVisitIsNoActiveVisit() {
        val yesterday = todayStart - 12 * 60 * 60 * 1000L
        val ctx = memberContext(
            checkIn(lastStartMs = yesterday, lastEndMs = yesterday + 30 * 60 * 1000L, lastDurationMs = 30 * 60 * 1000L),
            todayStart, now
        )
        assertEquals(MemberContext.NO_ACTIVE_VISIT, ctx)
    }

    @Test
    fun aMemberWithNoRecordsAtAllIsNoActiveVisit() {
        assertEquals(MemberContext.NO_ACTIVE_VISIT, memberContext(checkIn(), todayStart, now))
    }
}
