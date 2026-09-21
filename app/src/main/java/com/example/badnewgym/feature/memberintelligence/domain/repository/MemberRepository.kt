package com.example.badnewgym.feature.memberintelligence.domain.repository

import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import kotlinx.coroutines.flow.Flow

interface MemberIntelligenceRepository {
    suspend fun getMemberSnapshot(memberId: String): Result<MemberSnapshot>
    fun observeMemberEvents(gymId: String): Flow<MemberEvent>
    fun observeMemberUpdates(memberId: String): Flow<MemberSnapshot>
    suspend fun recordEvent(event: MemberEvent): Result<Unit>
    suspend fun refreshMember(memberId: String): Result<MemberSnapshot>
}

@Deprecated("Use MemberIntelligenceRepository")
typealias MemberRepository = MemberIntelligenceRepository
