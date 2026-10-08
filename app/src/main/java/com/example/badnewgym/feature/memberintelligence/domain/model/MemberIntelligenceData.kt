package com.example.badnewgym.feature.memberintelligence.domain.model

/**
 * V4 operational data model.
 *
 * Rules enforced by construction:
 *  - Every field is either real collected data or null. Never fabricated.
 *  - Absence of data is expressed as null / emptyList and drives honest empty states.
 *  - Nothing here depends on Android or Compose so it stays JVM-testable.
 */

// ------------------------------------------------------------------ Attendance

enum class AttendanceDayState {
    PRESENT,
    LATE,
    ABSENT,
    HOLIDAY,
    /** Day has not happened yet. Rendered faint, never counted in stats. */
    FUTURE
}

/**
 * One calendar day of a 30 day attendance window.
 * [epochDay] is days since epoch so days can be compared and grouped cheaply.
 */
data class AttendanceDay(
    val epochDay: Long,
    val dayOfMonth: Int,
    val state: AttendanceDayState,
    val visitMinutes: Int? = null
) {
    val isAttended: Boolean get() = state == AttendanceDayState.PRESENT || state == AttendanceDayState.LATE
}

/** 30 day attendance window, the primary representation required by spec section 19. */
data class AttendanceWindow(
    val year: Int,
    val month: Int,
    val days: List<AttendanceDay>
) {
    /** Only days that have happened. All ratios and streaks use this. */
    val elapsed: List<AttendanceDay> get() = days.filter { it.state != AttendanceDayState.FUTURE }
    val present: Int get() = days.count { it.state == AttendanceDayState.PRESENT }
    val late: Int get() = days.count { it.state == AttendanceDayState.LATE }
    val absent: Int get() = days.count { it.state == AttendanceDayState.ABSENT }
    val holiday: Int get() = days.count { it.state == AttendanceDayState.HOLIDAY }
    val future: Int get() = days.count { it.state == AttendanceDayState.FUTURE }
    val attended: Int get() = present + late
}

// ------------------------------------------------------------------ Workout

enum class WorkoutType { STRENGTH, CARDIO, MOBILITY, SPORTS, OTHER }

enum class MuscleGroup { CHEST, BACK, LEGS, SHOULDERS, ARMS, CORE, FULL_BODY }

data class WorkoutSession(
    val epochDay: Long,
    val dayOfMonth: Int,
    val minutes: Int,
    val type: WorkoutType? = null,
    val muscleGroups: List<MuscleGroup> = emptyList(),
    val calories: Int? = null
)

/** Monthly dataset. The compact card only renders a paged 7 day viewport of this. */
data class WorkoutMonth(
    val year: Int,
    val month: Int,
    val sessions: List<WorkoutSession>
) {
    fun sorted(): List<WorkoutSession> = sessions.sortedBy { it.epochDay }
    fun minutesOn(dayOfMonth: Int): Int = sessions.firstOrNull { it.dayOfMonth == dayOfMonth }?.minutes ?: 0
}

enum class WorkoutTrend { UP, DOWN, STABLE, MIXED, NONE }

// ------------------------------------------------------------------ Body goals

enum class GoalKind {
    WEIGHT_LOSS, WEIGHT_GAIN, MUSCLE_GAIN, BODY_FAT_REDUCTION,
    STRENGTH, RUNNING, ENDURANCE, PHYSIQUE, WAIST_REDUCTION, MOBILITY
}

enum class MetricKey(val label: String, val unit: String) {
    WEIGHT("Weight", "kg"),
    BODY_FAT("Body Fat", "%"),
    MUSCLE("Muscle", "kg"),
    WAIST("Waist", "in"),
    CHEST("Chest", "in"),
    BMI("BMI", ""),
    STRENGTH("Strength", "kg"),
    FIVE_K_TIME("5K Time", "min"),
    ENDURANCE_DISTANCE("Endurance", "km")
}

enum class GoalDirection { INCREASE, DECREASE, HOLD }

/** Progress interpretation is goal dependent: plus is not universally good. */
enum class ProgressVerdict { ON_TRACK, IMPROVING, REGRESSING, STABLE, OFF_TRACK, UNKNOWN }

data class BodyGoal(
    val id: String,
    val kind: GoalKind,
    val primaryMetric: MetricKey,
    val direction: GoalDirection,
    val startValue: Double,
    val currentValue: Double,
    val targetValue: Double,
    val startedAtEpoch: Long,
    val updatedAtEpoch: Long
) {
    val delta: Double get() = currentValue - startValue
    val toTarget: Double get() = targetValue - currentValue
}

data class MetricReading(
    val metric: MetricKey,
    val epochDay: Long,
    val value: Double
)

/**
 * Full measurement history for a metric so real up/down/plateau/recovery
 * shapes can be drawn instead of a decorative straight line.
 */
data class MetricHistory(
    val metric: MetricKey,
    val readings: List<MetricReading>
) {
    fun ordered(): List<MetricReading> = readings.sortedBy { it.epochDay }
}

enum class BodyTrend { UP, DOWN, STABLE, MIXED, NONE }

/** Privacy for sensitive body metrics (spec section 33 / 60). */
enum class PrivacyLevel { VISIBLE, HIDDEN, AUTHORIZED_STAFF_ONLY }

// ------------------------------------------------------------------ Progress photos

data class ProgressPhoto(
    val id: String,
    val url: String,
    val takenAtEpoch: Long,
    val monthLabel: String,
    val linkedWeight: Double? = null,
    val linkedBodyFat: Double? = null,
    val isNew: Boolean = false
)

// ------------------------------------------------------------------ Operational issues

enum class IssueState { REPORTED, IN_PROGRESS, RESOLVED }

data class MachineIssue(
    val id: String,
    val machineName: String,
    val description: String,
    val severity: IssueSeverity,
    val state: IssueState,
    val reportedAtEpoch: Long,
    val assignedStaff: String? = null,
    val affectedMemberIds: List<String> = emptyList()
)

data class StaffAlert(
    val id: String,
    val title: String,
    val detail: String,
    val minutesLate: Int,
    val affectedSessionAtEpoch: Long? = null
)

// ------------------------------------------------------------------ Trainer

data class TrainerSession(
    val trainerName: String,
    val trainerPhotoUrl: String?,
    val focus: String?,
    val scheduledAtEpoch: Long,
    val sessionsUsed: Int,
    val sessionsTotal: Int,
    val contactPhone: String? = null
)

// ------------------------------------------------------------------ Payment situation

sealed interface PaymentSituation {
    /** No outstanding balance - must NOT render a permanent payment widget (spec 12). */
    data object Settled : PaymentSituation

    data class Due(val amount: Double, val dueAtEpoch: Long) : PaymentSituation

    data class Overdue(val amount: Double, val overdueDays: Int, val dueAtEpoch: Long?) : PaymentSituation

    data class Partial(val received: Double, val remaining: Double, val dueAtEpoch: Long?) : PaymentSituation
}

enum class PaymentMethod { CASH, UPI, CARD, BANK_TRANSFER }

enum class ReminderChannel(val label: String, val hint: String) {
    WHATSAPP("WhatsApp", "Recommended"),
    SMS("SMS", "Auto fallback"),
    EMAIL("Email", "Optional")
}

enum class ReminderState { READY, SENDING, SENT, DELIVERED, SMS_FALLBACK, FAILED }

data class PaymentReceipt(
    val receiptId: String,
    val amount: Double,
    val method: PaymentMethod,
    val paidAtEpoch: Long
)

// ------------------------------------------------------------------ Entitlement

enum class MiFeature {
    MEMBER_CARD, ATTENDANCE, WORKOUT, BODY_GOALS, PROGRESS_PHOTOS,
    TRAINER, PAYMENT_COLLECTION, REMINDERS, MACHINE_ISSUES, PRIVACY_CONTROL
}

/**
 * Server entitlement is authoritative (spec 51). Local configuration can never
 * widen this set - it can only narrow it.
 */
data class MiEntitlement(
    val allowed: Set<MiFeature> = MiFeature.entries.toSet(),
    val planName: String = "Full Access"
) {
    fun permits(feature: MiFeature): Boolean = feature in allowed
}