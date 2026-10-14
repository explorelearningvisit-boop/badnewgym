package com.example.badnewgym.feature.memberintelligence.presentation.home

import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios

/** One member the local dataset can actually load and show. */
data class DirectoryMember(
    val id: String,
    val name: String,
    val code: String?,
    val plan: String?
)

/**
 * The real search index behind the header search.
 *
 * It is built from the same member ids [HomeViewModel] can load, so a result
 * is always a member the dashboard can genuinely switch to. Nothing is
 * synthesised: if a member is not in the dataset, search cannot return it.
 */
private val DIRECTORY: Map<String, () -> MemberSnapshot> = linkedMapOf(
    "BG305" to { MemberScenarios.naturalFreshYash().first },
    "BG105" to { MemberScenarios.futuristicNeonArjun().first },
    "BG310" to { MemberScenarios.minimalDarkRiya().first },
    "BG407" to { MemberScenarios.glassmorphismNeha().first },
    "BG001" to { MemberScenarios.premium3dKabir().first },
    "BG220" to { MemberScenarios.vibrantGradientAarav().first },
    "BG330" to { MemberScenarios.beastModeRohan().first },
    "BG502" to { MemberScenarios.purpleRoyalSimran().first }
)

/** Built once: instantiating a scenario is far too heavy to do per keystroke. */
private val directory: List<DirectoryMember> by lazy {
    DIRECTORY.map { (id, build) ->
        val snapshot = build()
        DirectoryMember(
            id = id,
            name = snapshot.identity.name,
            code = snapshot.identity.code,
            plan = snapshot.membership?.planName
        )
    }
}

fun memberDirectory(): List<DirectoryMember> = directory

/**
 * Matches on name, member code and plan. A blank query returns the whole
 * directory; a query that matches nothing returns an empty list, which the UI
 * renders as "no member matches" rather than falling back to the full list.
 */
fun searchMembers(query: String): List<DirectoryMember> {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return directory
    return directory.filter { m ->
        m.name.lowercase().contains(q) ||
            m.code?.lowercase()?.contains(q) == true ||
            m.plan?.lowercase()?.contains(q) == true
    }
}