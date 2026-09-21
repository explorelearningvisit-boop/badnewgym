package com.example.badnewgym.feature.memberintelligence.data.repository

import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.repository.MemberIntelligenceRepository
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

class StubMemberRepositoryImpl : MemberIntelligenceRepository {
    private val seenKeys = mutableSetOf<String>()
    private val snapshots = MutableStateFlow<MemberSnapshot?>(null)

    override suspend fun getMemberSnapshot(memberId: String): Result<MemberSnapshot> {
        delay(100)
        val (snapshot, _) = when (memberId) {
            "1", "BG204" -> MemberScenarios.naturalFreshYash()
            "2", "BG105" -> MemberScenarios.futuristicNeonArjun()
            "3", "BG310" -> MemberScenarios.minimalDarkRiya()
            "4", "BG407" -> MemberScenarios.glassmorphismNeha()
            "5", "BG001" -> MemberScenarios.premium3dKabir()
            "6", "BG220" -> MemberScenarios.vibrantGradientAarav()
            "7", "BG330" -> MemberScenarios.beastModeRohan()
            "8", "BG502" -> MemberScenarios.purpleRoyalSimran()
            else -> MemberScenarios.naturalFreshYash()
        }
        snapshots.value = snapshot
        return Result.success(snapshot)
    }

    override fun observeMemberEvents(gymId: String): Flow<MemberEvent> = flow {
        while (true) {
            delay(12_000)
            emit(
                MemberEvent(
                    id = "event_${System.currentTimeMillis()}",
                    memberId = snapshots.value?.id ?: "1",
                    gymId = gymId,
                    eventType = EventType.CHECK_IN,
                    occurredAt = System.currentTimeMillis()
                )
            )
        }
    }

    override fun observeMemberUpdates(memberId: String): Flow<MemberSnapshot> = snapshots.filterNotNull()

    override suspend fun recordEvent(event: MemberEvent): Result<Unit> {
        val key = event.idempotencyKey
        if (key != null && !seenKeys.add(key)) {
            return Result.success(Unit)
        }
        delay(50)
        return Result.success(Unit)
    }

    override suspend fun refreshMember(memberId: String): Result<MemberSnapshot> = getMemberSnapshot(memberId)
}
