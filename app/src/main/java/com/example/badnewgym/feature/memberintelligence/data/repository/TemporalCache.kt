package com.example.badnewgym.feature.memberintelligence.data.repository

import java.util.concurrent.ConcurrentHashMap

/**
 * Cache entry with freshness metadata.
 */
data class CacheEntry<T>(
    val data: T,
    val timestamp: Long = System.currentTimeMillis(),
    val ttlMillis: Long = 60_000L // 1 minute default TTL
) {
    fun isFresh(now: Long = System.currentTimeMillis()): Boolean = (now - timestamp) < ttlMillis
}

/**
 * Query and bandwidth telemetry.
 */
data class TemporalQueryTelemetry(
    var totalRequests: Int = 0,
    var cacheHits: Int = 0,
    var estimatedBytesTransferred: Long = 0,
    var totalLatencyMs: Long = 0
) {
    fun recordHit() {
        totalRequests++
        cacheHits++
    }

    fun recordMiss(bytes: Long, latency: Long) {
        totalRequests++
        estimatedBytesTransferred += bytes
        totalLatencyMs += latency
    }

    val hitRatePercent: Float
        get() = if (totalRequests == 0) 0f else (cacheHits.toFloat() / totalRequests) * 100f

    val averageLatencyMs: Long
        get() = if (totalRequests == 0) 0L else totalLatencyMs / (totalRequests - cacheHits).coerceAtLeast(1)
}

/**
 * In-memory L1 cache layer for temporal queries.
 */
class TemporalMemoryCache {
    private val summaries = ConcurrentHashMap<String, CacheEntry<Any>>()
    private val eventPages = ConcurrentHashMap<String, CacheEntry<Any>>()
    val telemetry = TemporalQueryTelemetry()

    @Suppress("UNCHECKED_CAST")
    fun <T> getSummary(key: String): T? {
        val entry = summaries[key] as? CacheEntry<T> ?: return null
        if (entry.isFresh()) {
            telemetry.recordHit()
            return entry.data
        }
        return null
    }

    fun <T : Any> putSummary(key: String, data: T, ttlMillis: Long = 60_000L) {
        summaries[key] = CacheEntry(data, ttlMillis = ttlMillis)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getPage(key: String): T? {
        val entry = eventPages[key] as? CacheEntry<T> ?: return null
        if (entry.isFresh()) {
            telemetry.recordHit()
            return entry.data
        }
        return null
    }

    fun <T : Any> putPage(key: String, data: T, ttlMillis: Long = 60_000L) {
        eventPages[key] = CacheEntry(data, ttlMillis = ttlMillis)
    }

    fun invalidateMember(memberId: String) {
        summaries.keys.removeIf { it.startsWith(memberId) }
        eventPages.keys.removeIf { it.startsWith(memberId) }
    }

    fun clear() {
        summaries.clear()
        eventPages.clear()
    }
}
