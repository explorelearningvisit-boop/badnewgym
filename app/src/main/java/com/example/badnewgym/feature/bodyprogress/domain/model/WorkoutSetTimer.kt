package com.example.badnewgym.feature.bodyprogress.domain.model

/**
 * Monotonic, process-independent timing contract for workout set capture.
 *
 * Persist the returned epoch timestamps alongside this state in the repository.
 * Use a monotonic clock (SystemClock.elapsedRealtime on Android) for elapsed
 * duration so wall-clock changes cannot make a set negative or longer.
 *
 * The timer intentionally does not subtract a guessed 3–6 second interaction
 * delay. Stop and Save are separate events; the UI may display their interval
 * as save interaction delay without pretending it was exercise time.
 */
data class WorkoutSetTiming(
    val startedAtEpochMillis: Long? = null,
    val stoppedAtEpochMillis: Long? = null,
    val savedAtEpochMillis: Long? = null,
    val startedAtMonotonicMillis: Long? = null,
    val stoppedAtMonotonicMillis: Long? = null
) {
    val isStarted: Boolean get() = startedAtMonotonicMillis != null
    val isStopped: Boolean get() = stoppedAtMonotonicMillis != null
    val isSaved: Boolean get() = savedAtEpochMillis != null

    /** Recorded interval from Start to Stop; null until a valid stop exists. */
    val recordedDurationMillis: Long?
        get() {
            val start = startedAtMonotonicMillis ?: return null
            val stop = stoppedAtMonotonicMillis ?: return null
            return (stop - start).takeIf { it >= 0L }
        }

    /** Delay between Stop and Save, not subtracted from recorded duration. */
    val saveInteractionDelayMillis: Long?
        get() {
            val stop = stoppedAtEpochMillis ?: return null
            val save = savedAtEpochMillis ?: return null
            return (save - stop).takeIf { it >= 0L }
        }

    fun start(epochMillis: Long, monotonicMillis: Long): WorkoutSetTiming {
        require(!isStarted) { "Set timer has already started." }
        require(epochMillis >= 0L && monotonicMillis >= 0L)
        return copy(
            startedAtEpochMillis = epochMillis,
            startedAtMonotonicMillis = monotonicMillis
        )
    }

    fun stop(epochMillis: Long, monotonicMillis: Long): WorkoutSetTiming {
        require(isStarted) { "Cannot stop a set before it starts." }
        require(!isStopped) { "Set timer has already stopped." }
        require(epochMillis >= (startedAtEpochMillis ?: 0L)) {
            "Stop wall-clock time cannot precede start time."
        }
        require(monotonicMillis >= (startedAtMonotonicMillis ?: 0L)) {
            "Stop monotonic time cannot precede start time."
        }
        return copy(
            stoppedAtEpochMillis = epochMillis,
            stoppedAtMonotonicMillis = monotonicMillis
        )
    }

    fun save(epochMillis: Long): WorkoutSetTiming {
        require(isStopped) { "Cannot save a set before it stops." }
        require(!isSaved) { "Set timing has already been saved." }
        require(epochMillis >= (stoppedAtEpochMillis ?: 0L)) {
            "Save time cannot precede stop time."
        }
        return copy(savedAtEpochMillis = epochMillis)
    }
}
