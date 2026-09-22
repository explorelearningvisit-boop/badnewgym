package com.example.badnewgym.feature.memberintelligence.domain.repository

import com.example.badnewgym.feature.memberintelligence.domain.model.GymLiveSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.GymOperationalEvent
import kotlinx.coroutines.flow.Flow

interface GymOperationalRepository {
    suspend fun getLiveSnapshot(gymId: String): Result<GymLiveSnapshot>
    suspend fun getRecentEvents(gymId: String, limit: Int = 20): Result<List<GymOperationalEvent>>
    fun observeEvents(gymId: String): Flow<GymOperationalEvent>
}