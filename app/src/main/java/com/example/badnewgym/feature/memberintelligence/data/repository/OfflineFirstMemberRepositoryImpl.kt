package com.example.badnewgym.feature.memberintelligence.data.repository

import com.example.badnewgym.feature.memberintelligence.data.local.MemberDao
import com.example.badnewgym.feature.memberintelligence.data.local.entities.toDomain
import com.example.badnewgym.feature.memberintelligence.data.local.entities.toEntity
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.repository.MemberIntelligenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext

class OfflineFirstMemberRepositoryImpl(
    private val memberDao: MemberDao,
    private val remote: MemberIntelligenceRepository = StubMemberRepositoryImpl()
) : MemberIntelligenceRepository {

    override suspend fun getMemberSnapshot(memberId: String): Result<MemberSnapshot> = withContext(Dispatchers.IO) {
        try {
            val localEntity = memberDao.getMemberById(memberId)
            if (localEntity != null) {
                Result.success(localEntity.toDomain())
            } else {
                val remoteResult = remote.getMemberSnapshot(memberId)
                if (remoteResult.isSuccess) {
                    val remoteData = remoteResult.getOrNull()
                    if (remoteData != null) {
                        memberDao.insertMember(remoteData.toEntity())
                        Result.success(remoteData)
                    } else {
                        Result.failure(Exception("Remote data is null"))
                    }
                } else {
                    Result.failure(remoteResult.exceptionOrNull() ?: Exception("Unknown remote error"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllMembers(gymId: String): Result<List<Pair<MemberSnapshot, MemberEvent>>> =
        remote.getAllMembers(gymId)

    override fun observeMemberEvents(gymId: String): Flow<MemberEvent> = remote.observeMemberEvents(gymId)

    override fun observeMemberUpdates(memberId: String): Flow<MemberSnapshot> = emptyFlow()

    override suspend fun recordEvent(event: MemberEvent): Result<Unit> = remote.recordEvent(event)

    override suspend fun refreshMember(memberId: String): Result<MemberSnapshot> = withContext(Dispatchers.IO) {
        val remoteResult = remote.refreshMember(memberId)
        remoteResult.getOrNull()?.let { memberDao.insertMember(it.toEntity()) }
        remoteResult
    }
}
