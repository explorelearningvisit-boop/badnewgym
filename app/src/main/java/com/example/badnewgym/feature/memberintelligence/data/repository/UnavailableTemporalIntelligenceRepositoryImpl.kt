package com.example.badnewgym.feature.memberintelligence.data.repository

import com.example.badnewgym.feature.memberintelligence.domain.model.TemporalEventRecord
import com.example.badnewgym.feature.memberintelligence.domain.model.TemporalPage
import com.example.badnewgym.feature.memberintelligence.domain.model.TemporalRange
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceTemporalSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTemporalSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutTemporalSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.HistoryTemporalSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.repository.TemporalIntelligenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Safe release fallback. Never synthesizes temporal metrics.
 */
class UnavailableTemporalIntelligenceRepositoryImpl : TemporalIntelligenceRepository {
    private val error = IllegalStateException(
        "Member Intelligence temporal data source is not configured for this build."
    )

    override suspend fun getAttendanceSummary(memberId: String, range: TemporalRange): Result<AttendanceTemporalSummary> = Result.failure(error)
    override suspend fun getAttendanceEvents(memberId: String, range: TemporalRange): Result<TemporalPage<TemporalEventRecord>> = Result.failure(error)
    override suspend fun getPaymentSummary(memberId: String, range: TemporalRange): Result<PaymentTemporalSummary> = Result.failure(error)
    override suspend fun getPaymentEvents(memberId: String, range: TemporalRange): Result<TemporalPage<TemporalEventRecord>> = Result.failure(error)
    override suspend fun getWorkoutSummary(memberId: String, range: TemporalRange): Result<WorkoutTemporalSummary> = Result.failure(error)
    override suspend fun getWorkoutEvents(memberId: String, range: TemporalRange): Result<TemporalPage<TemporalEventRecord>> = Result.failure(error)
    override suspend fun getHistorySummary(memberId: String, range: TemporalRange): Result<HistoryTemporalSummary> = Result.failure(error)
    override suspend fun getHistoryEvents(memberId: String, typeFilter: EventType?, range: TemporalRange): Result<TemporalPage<TemporalEventRecord>> = Result.failure(error)
    override suspend fun getEventDetail(eventId: String): Result<TemporalEventRecord> = Result.failure(error)
    override fun observeLiveAttendance(memberId: String): Flow<TemporalEventRecord> = emptyFlow()
    override fun observeLiveWorkout(memberId: String): Flow<TemporalEventRecord> = emptyFlow()
}
