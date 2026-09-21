package com.example.badnewgym.feature.memberintelligence.data.remote

/**
 * Defines the contract for fetching data from Supabase.
 * This ensures the repository is decoupled from the actual Supabase SDK.
 */
interface SupabaseClientProvider {
    suspend fun fetchMemberData(memberId: String): Map<String, Any>?
    suspend fun fetchMembershipData(memberId: String): Map<String, Any>?
    suspend fun fetchAttendanceData(memberId: String): Map<String, Any>?
    suspend fun fetchPaymentData(memberId: String): Map<String, Any>?
}
