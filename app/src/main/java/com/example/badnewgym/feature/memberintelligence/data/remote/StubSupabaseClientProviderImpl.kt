package com.example.badnewgym.feature.memberintelligence.data.remote

/**
 * A stub implementation of [SupabaseClientProvider] for development and testing.
 * Does not connect to actual Supabase.
 */
class StubSupabaseClientProviderImpl : SupabaseClientProvider {
    override suspend fun fetchMemberData(memberId: String): Map<String, Any>? {
        return mapOf(
            "id" to memberId,
            "name" to "Test User",
            "tier" to "ELITE"
        )
    }

    override suspend fun fetchMembershipData(memberId: String): Map<String, Any>? {
        return mapOf(
            "member_id" to memberId,
            "status" to "ACTIVE"
        )
    }

    override suspend fun fetchAttendanceData(memberId: String): Map<String, Any>? {
        return mapOf(
            "member_id" to memberId,
            "total_visits" to 15
        )
    }

    override suspend fun fetchPaymentData(memberId: String): Map<String, Any>? {
        return mapOf(
            "member_id" to memberId,
            "amount_due" to 0.0
        )
    }
}
