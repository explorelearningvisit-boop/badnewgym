package com.example.badnewgym.feature.memberintelligence.domain.model

/**
 * Optional engagement data for the Member Intelligence card.
 * All collections default to empty so existing repositories remain source-compatible.
 * UI must render only records actually supplied by the backend/repository.
 */
data class MemberEngagementSummary(
    val statuses: List<MemberStatusUpdate> = emptyList(),
    val approvals: List<MemberApprovalRequest> = emptyList(),
    val recognitions: List<MemberRecognition> = emptyList(),
    val communications: List<MemberCommunication> = emptyList(),
    val leaderboard: List<GymRecognitionEntry> = emptyList(),
    val rewardsPoints: Int? = null,
    val rewardTier: String? = null,
    val unreadNotifications: Int = 0
)

data class MemberStatusUpdate(
    val id: String,
    val authorName: String,
    val text: String,
    val createdAt: Long,
    val expiresAt: Long? = null,
    val mediaUrl: String? = null,
    val reactions: Int = 0,
    val comments: Int = 0,
    val visibility: EngagementVisibility = EngagementVisibility.GYM
)

data class MemberApprovalRequest(
    val id: String,
    val channel: ApprovalChannel,
    val subject: String,
    val preview: String? = null,
    val requestedBy: String,
    val requestedAt: Long,
    val status: ApprovalStatus = ApprovalStatus.PENDING,
    val amount: Double? = null
)

data class MemberRecognition(
    val id: String,
    val type: RecognitionType,
    val title: String,
    val value: String,
    val occurredAt: Long,
    val highlightToGym: Boolean = true
)

data class GymRecognitionEntry(
    val memberId: String,
    val memberName: String,
    val rank: Int,
    val metric: String,
    val value: String,
    val reason: String? = null,
    val isCurrentMember: Boolean = false,
    val photoUrl: String? = null
)

data class MemberCommunication(
    val id: String,
    val channel: CommunicationChannel,
    val subject: String,
    val preview: String? = null,
    val occurredAt: Long,
    val status: CommunicationStatus = CommunicationStatus.SENT
)

enum class EngagementVisibility { MEMBER, STAFF, GYM }
enum class ApprovalChannel { EMAIL, WHATSAPP, APP, INTERNAL }
enum class ApprovalStatus { PENDING, APPROVED, REJECTED, EXPIRED }
enum class RecognitionType { ATTENDANCE, PAYMENT_ON_TIME, STREAK, WORKOUT, GOAL, REFERRAL, MILESTONE }
enum class CommunicationChannel { EMAIL, WHATSAPP, SMS, PUSH, IN_APP }
enum class CommunicationStatus { SENT, DELIVERED, READ, FAILED }
