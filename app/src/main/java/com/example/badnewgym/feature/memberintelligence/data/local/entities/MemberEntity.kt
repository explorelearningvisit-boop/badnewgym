package com.example.badnewgym.feature.memberintelligence.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.badnewgym.feature.memberintelligence.domain.model.*

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey val id: String,
    val gymId: String,
    val identity: MemberIdentity,
    val membership: MembershipStatus?,
    val attendance: AttendanceSummary?,
    val payment: PaymentSummary?,
    val trainer: TrainerSummary?,
    val workout: WorkoutSummary?,
    val supplements: SupplementSummary?,
    val nutrition: NutritionSummary?,
    val services: List<ServiceSummary>?,
    val recentEvents: List<MemberEvent>,
    val issues: List<MemberIssue>
)

fun MemberEntity.toDomain(): MemberSnapshot {
    return MemberSnapshot(
        id = id,
        gymId = gymId,
        identity = identity,
        membership = membership,
        attendance = attendance,
        payment = payment,
        trainer = trainer,
        workout = workout,
        supplements = supplements,
        nutrition = nutrition,
        services = services,
        recentEvents = recentEvents,
        issues = issues
    )
}

fun MemberSnapshot.toEntity(): MemberEntity {
    return MemberEntity(
        id = id,
        gymId = gymId,
        identity = identity,
        membership = membership,
        attendance = attendance,
        payment = payment,
        trainer = trainer,
        workout = workout,
        supplements = supplements,
        nutrition = nutrition,
        services = services,
        recentEvents = recentEvents,
        issues = issues
    )
}
