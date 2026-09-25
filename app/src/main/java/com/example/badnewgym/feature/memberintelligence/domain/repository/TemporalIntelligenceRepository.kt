package com.example.badnewgym.feature.memberintelligence.domain.repository

import com.example.badnewgym.feature.memberintelligence.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * On-demand temporal intelligence repository contract.
 * Allows capability-scoped queries, cursor pagination, and low-bandwidth summary-first retrieval.
 */
interface TemporalIntelligenceRepository {
    suspend fun getAttendanceSummary(memberId: String, range: TemporalRange): Result<AttendanceTemporalSummary>
    suspend fun getAttendanceEvents(memberId: String, range: TemporalRange): Result<TemporalPage<TemporalEventRecord>>
    
    suspend fun getPaymentSummary(memberId: String, range: TemporalRange): Result<PaymentTemporalSummary>
    suspend fun getPaymentEvents(memberId: String, range: TemporalRange): Result<TemporalPage<TemporalEventRecord>>

    suspend fun getWorkoutSummary(memberId: String, range: TemporalRange): Result<WorkoutTemporalSummary>
    suspend fun getWorkoutEvents(memberId: String, range: TemporalRange): Result<TemporalPage<TemporalEventRecord>>

    suspend fun getHistorySummary(memberId: String, range: TemporalRange): Result<HistoryTemporalSummary>
    suspend fun getHistoryEvents(
        memberId: String,
        typeFilter: EventType? = null,
        range: TemporalRange
    ): Result<TemporalPage<TemporalEventRecord>>

    suspend fun getEventDetail(eventId: String): Result<TemporalEventRecord>

    fun observeLiveAttendance(memberId: String): Flow<TemporalEventRecord>
    fun observeLiveWorkout(memberId: String): Flow<TemporalEventRecord>
}
