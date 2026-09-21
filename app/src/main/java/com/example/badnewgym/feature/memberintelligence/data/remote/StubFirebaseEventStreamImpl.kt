package com.example.badnewgym.feature.memberintelligence.data.remote

import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * A stub implementation of [FirebaseEventStream] for development and testing.
 * Does not connect to actual Firebase.
 */
class StubFirebaseEventStreamImpl : FirebaseEventStream {
    override fun observeGymEvents(gymId: String): Flow<MemberEvent> {
        // In a real implementation, this would connect to Firebase Realtime Database
        // or Firestore and emit events as they happen.
        return emptyFlow()
    }

    override suspend fun publishEvent(event: MemberEvent): Boolean {
        // Simulate a successful publish
        return true
    }
}
