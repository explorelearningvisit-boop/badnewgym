package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Logout
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Contract for the event header that replaces the old product wordmark.
 *
 * The header states the event the screen is actually offering, so it must never
 * claim a visit is in progress unless a session was genuinely recorded.
 */
class HomeEventHeaderTest {

    private val runIcon = Icons.Filled.DirectionsRun
    private val outIcon = Icons.Filled.Logout

    @Test
    fun withNoRecordsTheHeaderOffersCheckIn() {
        val h = homeEventHeader(null, runIcon)
        assertEquals(HomeEventType.CHECK_IN, h.type)
        assertEquals("Check-In", h.title)
        assertEquals("Tap to check-in this member", h.subtitle)
    }

    @Test
    fun aCompletedVisitStillOffersCheckInRatherThanCheckOut() {
        // COMPLETED_VISIT means the visit already ended, so advertising
        // "Check-Out / Visit in progress" would be false.
        val h = homeEventHeader(MemberContext.COMPLETED_VISIT, runIcon)
        assertEquals(HomeEventType.CHECK_IN, h.type)
        assertEquals("Tap to check-in this member", h.subtitle)
    }

    @Test
    fun anOpenSessionSwitchesToCheckOut() {
        val h = homeEventHeader(MemberContext.JUST_CHECKED_IN, outIcon)
        assertEquals(HomeEventType.CHECK_OUT, h.type)
        assertEquals("Check-Out", h.title)
        // A state, not "tap to end": the header opens the check-in module, and
        // ending a visit is a separate authorised action, so promising a direct
        // end here would misrepresent what the tap does.
        assertEquals("Visit in progress", h.subtitle)
        assertEquals(outIcon, h.icon)
    }

    @Test
    fun noActiveVisitIsNotDescribedAsACompletedVisit() {
        val h = homeEventHeader(MemberContext.NO_ACTIVE_VISIT, runIcon)
        assertEquals(HomeEventType.CHECK_IN, h.type)
    }
}