package com.example.badnewgym.feature.memberintelligence.domain.engine

import com.example.badnewgym.feature.memberintelligence.domain.model.FeatureEntitlement
import com.example.badnewgym.feature.memberintelligence.domain.model.FeatureKey
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberIdentity
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.SupplementSummary
import com.example.badnewgym.feature.memberintelligence.domain.model.TrainerSummary
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MenuAvailabilityResolverTest {

    private fun base(): MemberSnapshot = MemberSnapshot(
        id = "m1",
        gymId = "g1",
        identity = MemberIdentity("A", null, MembershipTier.NORMAL, 0, "BG1"),
        membership = null,
        attendance = null,
        payment = null,
        trainer = null,
        workout = null,
        supplements = null,
        nutrition = null,
        services = null
    )

    @Test
    fun trainerHiddenWhenAbsent() {
        val menus = MenuAvailabilityResolver.resolve(base(), emptyList())
        assertFalse(menus.any { it.id == MenuType.TRAINER })
    }

    @Test
    fun trainerVisibleWhenAssigned() {
        val snapshot = base().copy(
            trainer = TrainerSummary("Vikas", null, 12, 4, null, "Hypertrophy")
        )
        val menus = MenuAvailabilityResolver.resolve(snapshot, emptyList())
        assertTrue(menus.any { it.id == MenuType.TRAINER && it.isVisible })
    }

    @Test
    fun supplementsHiddenWithoutHistory() {
        val menus = MenuAvailabilityResolver.resolve(base(), emptyList())
        assertFalse(menus.any { it.id == MenuType.SUPPLEMENTS })
    }

    @Test
    fun supplementsVisibleWithHistory() {
        val snapshot = base().copy(
            supplements = SupplementSummary(true, "Whey", 1L)
        )
        val menus = MenuAvailabilityResolver.resolve(snapshot, emptyList())
        assertTrue(menus.any { it.id == MenuType.SUPPLEMENTS })
    }

    @Test
    fun insightLockedWithoutEntitlement() {
        val menus = MenuAvailabilityResolver.resolve(
            base(),
            emptyList(),
            FeatureEntitlement(setOf(FeatureKey.MEMBER_INTELLIGENCE))
        )
        val insight = menus.first { it.id == MenuType.INSIGHT }
        assertTrue(insight.isLocked)
    }
}
