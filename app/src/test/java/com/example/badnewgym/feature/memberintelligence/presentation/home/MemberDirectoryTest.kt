package com.example.badnewgym.feature.memberintelligence.presentation.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Header search must resolve to real members the repository can load. An
 * unmatched query has to return nothing rather than quietly falling back to
 * the whole list, which would make the search look broken-or-fake.
 */
class MemberDirectoryTest {

    @Test
    fun directoryIsNotEmptyAndCodesAreUnique() {
        val all = memberDirectory()
        assertTrue("directory should hold the members the repo can load", all.isNotEmpty())
        assertEquals(all.size, all.map { it.id }.distinct().size)
        assertTrue(all.all { it.name.isNotBlank() })
    }

    @Test
    fun blankQueryReturnsEverything() {
        assertEquals(memberDirectory().size, searchMembers("").size)
        assertEquals(memberDirectory().size, searchMembers("   ").size)
    }

    @Test
    fun matchesByNameCodeOrPlan() {
        val target = memberDirectory().first()
        assertTrue(searchMembers(target.name).any { it.id == target.id })
        assertTrue(searchMembers(target.id).any { it.id == target.id })
        val planHit = memberDirectory().firstOrNull { !it.plan.isNullOrBlank() }
        if (planHit != null) {
            assertTrue(searchMembers(planHit.plan!!).any { it.id == planHit.id })
        }
    }

    @Test
    fun unmatchedQueryReturnsEmpty() {
        assertEquals(emptyList<DirectoryMember>(), searchMembers("zzzz-no-such-member"))
    }
}