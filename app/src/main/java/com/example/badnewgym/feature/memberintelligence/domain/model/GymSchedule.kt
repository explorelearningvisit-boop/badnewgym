package com.example.badnewgym.feature.memberintelligence.domain.model

/**
 * Gym weekly opening schedule.
 *
 * Closure is *never* inferred from the weekday itself: Sunday is an ordinary
 * operating day unless the gym's configured schedule says otherwise. A day is
 * closed only when the schedule lists its [Calendar.dayOfWeek] as closed, or
 * when an explicit closure record exists for that date.
 */
data class GymWeeklySchedule(
    /** [Calendar.DAY_OF_WEEK] values (1=Sunday .. 7=Saturday) that the gym is closed. */
    val closedWeekdays: Set<Int> = emptySet()
) {
    fun isClosedOnWeekday(dayOfWeek: Int): Boolean = dayOfWeek in closedWeekdays

    companion object {
        /** No configured closures: every weekday is an operating day, Sunday included. */
        val OPEN_EVERY_DAY = GymWeeklySchedule(emptySet())
    }
}