package com.example.badnewgym.feature.memberintelligence.domain.variants

import androidx.compose.ui.graphics.Color
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus

/**
 * Home widget variant engines (Home Screen spec panels 4, 7, 9).
 *
 * Every variant is derived from real records. When the data needed for a
 * variant does not exist the engine returns UNKNOWN and the UI omits the
 * badge instead of fabricating one. Pure Kotlin, fully unit tested.
 */
object CheckInVariants {

    enum class Lateness { JUST_NOW, ON_TIME, LATE, VERY_LATE, UNKNOWN }

    /** Arrival within this many minutes of schedule still counts as on time. */
    const val ON_TIME_GRACE_MIN = 15

    /** Delay beyond this many minutes is "very late" rather than "late". */
    const val VERY_LATE_MIN = 120

    fun resolve(
        arrivedAtEpoch: Long,
        scheduledAtEpoch: Long?,
        nowEpoch: Long
    ): Lateness {
        if ((nowEpoch - arrivedAtEpoch) < 15 * 60_000L) return Lateness.JUST_NOW
        if (scheduledAtEpoch == null) return Lateness.UNKNOWN
        val delayMin = ((arrivedAtEpoch - scheduledAtEpoch) / 60_000L).toInt()
        return when {
            delayMin <= ON_TIME_GRACE_MIN -> Lateness.ON_TIME
            delayMin <= VERY_LATE_MIN -> Lateness.LATE
            else -> Lateness.VERY_LATE
        }
    }

    /** e.g. "+2h 15m late". Only called when a schedule exists. */
    fun lateLabel(arrivedAtEpoch: Long, scheduledAtEpoch: Long): String {
        val mins = ((arrivedAtEpoch - scheduledAtEpoch) / 60_000L).toInt().coerceAtLeast(0)
        return if (mins < 60) "+${mins}m late"
        else "+${mins / 60}h ${(mins % 60).pad2()} late"
    }

    private fun Int.pad2() = toString().padStart(2, '0')
}

object PlanVariants {

    enum class Variant { ACTIVE, EXPIRING, EXPIRED, TRIAL, FROZEN, UPGRADE }

    /** Days remaining at or below this threshold reads as expiring. */
    const val EXPIRING_THRESHOLD_DAYS = 7

    fun resolve(
        membership: MembershipStatus?,
        isTrial: Boolean = false,
        premiumLocked: Boolean = false
    ): Variant {
        if (membership == null || premiumLocked) return Variant.UPGRADE
        if (membership.lifecycle == MembershipLifecycle.FROZEN) return Variant.FROZEN
        if (isTrial || membership.planType.contains("trial", ignoreCase = true)) return Variant.TRIAL
        if (!membership.isActive ||
            membership.lifecycle == MembershipLifecycle.EXPIRED ||
            membership.daysRemaining <= 0
        ) return Variant.EXPIRED
        if (membership.lifecycle == MembershipLifecycle.EXPIRING ||
            membership.daysRemaining <= EXPIRING_THRESHOLD_DAYS
        ) return Variant.EXPIRING
        return Variant.ACTIVE
    }

    fun headline(variant: Variant, membership: MembershipStatus?): String = when (variant) {
        Variant.ACTIVE -> "${membership?.daysRemaining ?: 0} Days Left"
        Variant.EXPIRING -> "${membership?.daysRemaining ?: 0} Days Left"
        Variant.EXPIRED -> "Expired"
        Variant.TRIAL -> "${membership?.daysRemaining ?: 0} Days Left"
        Variant.FROZEN -> "Frozen"
        Variant.UPGRADE -> "Upgrade"
    }

    fun subline(variant: Variant, membership: MembershipStatus?): String = when (variant) {
        Variant.ACTIVE -> "${membership?.planName ?: ""}"
        Variant.EXPIRING -> "Renew soon"
        Variant.EXPIRED -> "Plan ended"
        Variant.TRIAL -> "Trial plan"
        Variant.FROZEN -> membership?.let {
            "Until " + java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
                .format(java.util.Date(it.expiryDate))
        } ?: ""
        Variant.UPGRADE -> "Premium locked"
    }
}

object AttendanceBands {

    enum class Band { GOOD, AVERAGE, RISK, LOW }

    fun resolve(completion: Float): Band = when {
        completion >= 0.80f -> Band.GOOD
        completion >= 0.60f -> Band.AVERAGE
        completion >= 0.40f -> Band.RISK
        else -> Band.LOW
    }

    /** Donut color per band. Low is red, never grey-on-grey. */
    fun color(band: Band): Color = when (band) {
        Band.GOOD -> Color(0xFF00B86B)
        Band.AVERAGE -> Color(0xFF367FE8)
        Band.RISK -> Color(0xFFE59A1A)
        Band.LOW -> Color(0xFFE54848)
    }
}

object PaymentCaptions {

    fun partial(received: Double, remaining: Double): String =
        "${received.toMoney()} of ${(received + remaining).toMoney()}"

    fun paidToday(amount: Double, method: String?): String =
        "Paid today · ${amount.toMoney()}" + (method?.let { " · $it" } ?: "")

    private fun Double.toMoney(): String =
        "₹" + String.format(java.util.Locale.getDefault(), "%,.0f", this)
}

/** Text and icon scaling tiers (Home Screen spec panel 7). */
enum class TextScale(val label: String, val factor: Float) {
    COMPACT("Compact", 0.85f),
    DEFAULT("Default", 1.0f),
    COMFORT("Comfort", 1.12f),
    LARGE("Large", 1.25f)
}
