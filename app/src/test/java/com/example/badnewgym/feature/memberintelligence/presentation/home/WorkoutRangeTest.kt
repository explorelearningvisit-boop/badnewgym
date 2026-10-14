package com.example.badnewgym.feature.memberintelligence.presentation.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Contract for the workout range selector.
 *
 * The Home card presents one compact dropdown labelled with the spoken range
 * ("This Month"), while the module sheet keeps the dense chip form. Both
 * spellings have to survive a round trip because the debug bridge and the
 * stored state identify a range by string.
 */
class WorkoutRangeTest {

    @Test
    fun dashboardOffersExactlyTheRangesTheOldChipsDid() {
        assertEquals(
            listOf(WorkoutRange.THIS_MONTH, WorkoutRange.D7, WorkoutRange.D15, WorkoutRange.D30),
            WorkoutRange.dashboardOptions
        )
    }

    @Test
    fun dropdownLabelsAreSpelledOutNotAbbreviated() {
        assertEquals("This Month", WorkoutRange.THIS_MONTH.label)
        assertEquals("Last 7 Days", WorkoutRange.D7.label)
        assertEquals("Last 30 Days", WorkoutRange.D30.label)
        // A label that is just the abbreviation would make the dropdown look
        // identical to the chips it replaced.
        WorkoutRange.dashboardOptions.forEach {
            assertTrue("${it.label} is still abbreviated", it.label != it.short)
        }
    }

    @Test
    fun bothShortAndNameResolveBackToTheSameRange() {
        assertEquals(WorkoutRange.D7, WorkoutRange.fromId("D7"))
        assertEquals(WorkoutRange.THIS_MONTH, WorkoutRange.fromId("THIS_MONTH"))
    }

    @Test
    fun anUnknownRangeFallsBackToTheWholeMonthRatherThanFailing() {
        assertEquals(WorkoutRange.THIS_MONTH, WorkoutRange.fromId(null))
        assertEquals(WorkoutRange.THIS_MONTH, WorkoutRange.fromId("LAST_QUARTER"))
    }

    @Test
    fun onlyTheMonthRangeIsTheMonthRange() {
        assertTrue(WorkoutRange.THIS_MONTH.isMonth)
        assertFalse(WorkoutRange.D7.isMonth)
        assertFalse(WorkoutRange.D30.isMonth)
    }
}