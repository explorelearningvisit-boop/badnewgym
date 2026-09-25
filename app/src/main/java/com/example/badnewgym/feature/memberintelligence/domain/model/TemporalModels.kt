package com.example.badnewgym.feature.memberintelligence.domain.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Temporal granularity scales supported by Member Intelligence.
 */
enum class TemporalGranularity(val displayName: String, val shortLabel: String) {
    LIVE("Live", "LIVE"),
    SECOND("Second", "SEC"),
    MINUTE("Minute", "MIN"),
    HOUR("Hour", "HR"),
    DAY("Day", "DAY"),
    WEEK("Week", "WK"),
    MONTH("Month", "MO"),
    QUARTER("Quarter", "QTR"),
    HALF_YEAR("6 Months", "6M"),
    YEAR("Year", "YR"),
    CUSTOM("Custom", "CUST");

    fun isRealtime(): Boolean = this == LIVE
}

/**
 * Common temporal range descriptor for on-demand queries.
 */
data class TemporalRange(
    val startMillis: Long,
    val endMillis: Long,
    val granularity: TemporalGranularity = TemporalGranularity.MONTH,
    val timeZone: String = "UTC",
    val cursor: String? = null,
    val limit: Int = 20,
    val hasMore: Boolean = false
) {
    fun formatPeriodLabel(): String {
        val startDate = Date(startMillis)
        val endDate = Date(endMillis)
        val cal = Calendar.getInstance().apply { time = startDate }
        
        return when (granularity) {
            TemporalGranularity.LIVE -> "Live (Realtime)"
            TemporalGranularity.SECOND, TemporalGranularity.MINUTE, TemporalGranularity.HOUR, TemporalGranularity.DAY -> {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(startDate)
            }
            TemporalGranularity.WEEK -> {
                val weekNum = cal.get(Calendar.WEEK_OF_YEAR)
                val startStr = SimpleDateFormat("dd MMM", Locale.getDefault()).format(startDate)
                val endStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(endDate)
                "Week $weekNum ($startStr - $endStr)"
            }
            TemporalGranularity.MONTH -> {
                SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(startDate)
            }
            TemporalGranularity.QUARTER -> {
                val q = (cal.get(Calendar.MONTH) / 3) + 1
                "Q$q ${cal.get(Calendar.YEAR)}"
            }
            TemporalGranularity.HALF_YEAR -> {
                val h = if (cal.get(Calendar.MONTH) < 6) "H1" else "H2"
                "$h ${cal.get(Calendar.YEAR)}"
            }
            TemporalGranularity.YEAR -> {
                SimpleDateFormat("yyyy", Locale.getDefault()).format(startDate)
            }
            TemporalGranularity.CUSTOM -> {
                val s = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(startDate)
                val e = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(endDate)
                "$s - $e"
            }
        }
    }

    companion object {
        fun forCurrentMonth(now: Long = System.currentTimeMillis()): TemporalRange {
            val cal = Calendar.getInstance().apply {
                timeInMillis = now
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val start = cal.timeInMillis
            cal.add(Calendar.MONTH, 1)
            val end = cal.timeInMillis - 1
            return TemporalRange(start, end, TemporalGranularity.MONTH)
        }

        fun forToday(now: Long = System.currentTimeMillis()): TemporalRange {
            val cal = Calendar.getInstance().apply {
                timeInMillis = now
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val start = cal.timeInMillis
            cal.add(Calendar.DAY_OF_MONTH, 1)
            val end = cal.timeInMillis - 1
            return TemporalRange(start, end, TemporalGranularity.DAY)
        }

        fun forPast7Days(now: Long = System.currentTimeMillis()): TemporalRange {
            val end = now
            val start = now - (7L * 24 * 60 * 60 * 1000)
            return TemporalRange(start, end, TemporalGranularity.DAY)
        }

        fun forPast30Days(now: Long = System.currentTimeMillis()): TemporalRange {
            val end = now
            val start = now - (30L * 24 * 60 * 60 * 1000)
            return TemporalRange(start, end, TemporalGranularity.DAY)
        }

        fun forCurrentWeek(now: Long = System.currentTimeMillis()): TemporalRange {
            val cal = Calendar.getInstance().apply {
                timeInMillis = now
                set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val start = cal.timeInMillis
            cal.add(Calendar.DAY_OF_WEEK, 7)
            val end = cal.timeInMillis - 1
            return TemporalRange(start, end, TemporalGranularity.WEEK)
        }

        fun forCurrentYear(now: Long = System.currentTimeMillis()): TemporalRange {
            val cal = Calendar.getInstance().apply {
                timeInMillis = now
                set(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val start = cal.timeInMillis
            cal.add(Calendar.YEAR, 1)
            val end = cal.timeInMillis - 1
            return TemporalRange(start, end, TemporalGranularity.YEAR)
        }
    }
}

/**
 * Attendance source verification metadata.
 */
data class SourceVerificationMetadata(
    val source: String, // FACE, CAMERA, QR, NFC, RFID, STAFF, APP, SYSTEM
    val gateName: String? = null,
    val deviceId: String? = null,
    val verificationResult: String = "SUCCESS",
    val latencyMs: Long? = null,
    val confidenceScore: Float? = null
)

/**
 * Canonical temporal event record for on-demand retrieval.
 */
data class TemporalEventRecord(
    val eventId: String,
    val memberId: String,
    val eventType: EventType,
    val occurredAt: Long,
    val recordedAt: Long = occurredAt,
    val source: EventSource = EventSource.SYSTEM,
    val sourceMetadata: SourceVerificationMetadata? = null,
    val actorName: String? = null,
    val status: String = "COMPLETED",
    val title: String,
    val subtitle: String? = null,
    val amount: Double? = null,
    val currency: String = "INR",
    val referenceId: String? = null,
    val invoiceNumber: String? = null,
    val paymentMethod: String? = null,
    val durationMinutes: Int? = null,
    val details: Map<String, String> = emptyMap(),
    val mediaThumbnailUrl: String? = null,
    val mediaFullUrl: String? = null
) {
    fun formatTimestamp(withSeconds: Boolean = false): String {
        val pattern = if (withSeconds) "dd MMM yyyy • HH:mm:ss" else "dd MMM yyyy • HH:mm"
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(occurredAt))
    }

    fun formatTimeOnly(withSeconds: Boolean = true): String {
        val pattern = if (withSeconds) "HH:mm:ss" else "HH:mm"
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(occurredAt))
    }
}

/**
 * Generic paginated temporal result.
 */
data class TemporalPage<T>(
    val items: List<T>,
    val range: TemporalRange,
    val nextCursor: String? = null,
    val totalCount: Int = items.size,
    val isFromCache: Boolean = false,
    val cachedAt: Long? = null
)

/**
 * Aggregated temporal summaries for low-bandwidth overview.
 */
data class AttendanceTemporalSummary(
    val totalVisits: Int,
    val lateVisits: Int = 0,
    val missedVisits: Int = 0,
    val averageDurationMinutes: Int = 65,
    val peakWeekday: String = "Monday",
    val peakHour: String = "07:00 - 08:30",
    val attendanceRate: Float = 0.85f,
    val sourceBreakdown: Map<String, Int> = mapOf("FACE" to 12, "QR" to 4),
    val dailyAttendanceMap: Map<String, Int> = emptyMap() // "yyyy-MM-dd" -> count
)

data class PaymentTemporalSummary(
    val totalPaid: Double,
    val totalDue: Double = 0.0,
    val totalOverdue: Double = 0.0,
    val transactionCount: Int,
    val currency: String = "INR",
    val lastPaymentAt: Long? = null,
    val monthlyTotals: Map<String, Double> = emptyMap() // "yyyy-MM" -> amount
)

data class WorkoutTemporalSummary(
    val totalSessions: Int,
    val totalSetsCompleted: Int,
    val totalVolumeKg: Double = 0.0,
    val averageDurationMinutes: Int = 55,
    val consistencyScore: Float = 0.92f,
    val topMuscles: List<String> = listOf("Chest", "Back", "Legs")
)

data class HistoryTemporalSummary(
    val totalEvents: Int,
    val eventCountsByType: Map<EventType, Int> = emptyMap(),
    val firstEventAt: Long? = null,
    val lastEventAt: Long? = null
)

/**
 * Data retention policy descriptor.
 */
data class DataRetentionPolicy(
    val category: String,
    val hotRetentionDays: Int = 90,
    val warmRetentionDays: Int = 365,
    val coldArchiveDays: Int = 1825,
    val requiresExplicitAuditRecord: Boolean = true
)
