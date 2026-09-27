package com.example.badnewgym.feature.memberintelligence.data.repository

import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.repository.MemberIntelligenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Safe release fallback.
 *
 * The feature must never ship demo members as production data. Until the real
 * authenticated remote repository is injected, release builds surface an
 * explicit unavailable state instead of silently using fixtures.
 */
class UnavailableMemberRepositoryImpl : MemberIntelligenceRepository {
    private val error = IllegalStateException(
        "Member Intelligence data source is not configured for this build."
    )

    override suspend fun getMemberSnapshot(memberId: String): Result<MemberSnapshot> =
        Result.failure(error)

    override suspend fun getAllMembers(gymId: String): Result<List<Pair<MemberSnapshot, MemberEvent>>> =
        Result.failure(error)

    override fun observeMemberEvents(gymId: String): Flow<MemberEvent> = emptyFlow()

    override fun observeMemberUpdates(memberId: String): Flow<MemberSnapshot> = emptyFlow()

    override suspend fun recordEvent(event: MemberEvent): Result<Unit> =
        Result.failure(error)

    override suspend fun refreshMember(memberId: String): Result<MemberSnapshot> =
        Result.failure(error)
}
