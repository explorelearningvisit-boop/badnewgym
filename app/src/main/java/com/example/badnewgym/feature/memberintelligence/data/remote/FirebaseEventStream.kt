package com.example.badnewgym.feature.memberintelligence.data.remote

import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import kotlinx.coroutines.flow.Flow

/**
 * Defines the contract for observing real-time events from Firebase.
 * This ensures the repository is decoupled from the actual Firebase SDK.
 */
interface FirebaseEventStream {
    fun observeGymEvents(gymId: String): Flow<MemberEvent>
    suspend fun publishEvent(event: MemberEvent): Boolean
}
