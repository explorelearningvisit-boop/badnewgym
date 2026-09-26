package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*

/**
 * Data-gated engagement layer: social-style status, approval inbox,
 * recognition/rewards and gym leaderboard. No fake records are generated.
 */
@Composable
fun MemberEngagementHub(
    engagement: MemberEngagementSummary?,
    theme: ThemeId,
    onStatusClick: (MemberStatusUpdate) -> Unit = {},
    onApprovalAction: (MemberApprovalRequest, ApprovalStatus) -> Unit = { _, _ -> },
    onRecognitionClick: (MemberRecognition) -> Unit = {},
    onLeaderboardClick: (GymRecognitionEntry) -> Unit = {}
) {
    val data = engagement ?: return
    val statuses = data.statuses.take(8)
    val approvals = data.approvals.filter { it.status == ApprovalStatus.PENDING }.take(3)
    val recognitions = data.recognitions.filter { it.highlightToGym }.take(4)
    val leaderboard = data.leaderboard.sortedBy { it.rank }.take(5)

    if (statuses.isEmpty() && approvals.isEmpty() && recognitions.isEmpty() &&
        leaderboard.isEmpty() && data.rewardsPoints == null && data.unreadNotifications <= 0) return

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (statuses.isNotEmpty()) MemberStatusStories(statuses, theme, onStatusClick)
        if (approvals.isNotEmpty()) ApprovalInbox(approvals, theme, onApprovalAction)
        if (recognitions.isNotEmpty() || data.rewardsPoints != null) {
            RecognitionPulse(recognitions, data.rewardsPoints, data.rewardTier, theme, onRecognitionClick)
        }
        if (leaderboard.isNotEmpty()) GymRecognitionBoard(leaderboard, theme, onLeaderboardClick)
        if (data.unreadNotifications > 0) {
            InfoCard(theme) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Rounded.Notifications, null, tint = BADGymTheme.colors.accent, modifier = Modifier.size(15.dp))
                    Text("${data.unreadNotifications} unread member notifications", color = BADGymTheme.colors.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MemberStatusStories(items: List<MemberStatusUpdate>, theme: ThemeId, onClick: (MemberStatusUpdate) -> Unit) {
    val colors = BADGymTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        SectionTitle("MEMBER STATUS • LIVE STORIES", theme)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items.forEach { item ->
                Column(
                    modifier = Modifier.width(70.dp).clip(RoundedCornerShape(9.dp))
                        .background(colors.surfaceMuted.copy(alpha = 0.72f))
                        .border(0.8.dp, colors.border.copy(alpha = 0.55f), RoundedCornerShape(9.dp))
                        .clickable { onClick(item) }.padding(vertical = 5.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(CircleShape)
                            .background(colors.surface).border(2.dp, colors.accent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            item.authorName.trim().split(Regex("\\s+")).take(2)
                                .mapNotNull { it.firstOrNull()?.uppercase() }.joinToString(""),
                            color = colors.accent, fontSize = 10.sp, fontWeight = FontWeight.Black
                        )
                    }
                    Text(item.authorName, color = colors.textPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(item.text, color = colors.textMuted, fontSize = 7.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    if (item.reactions > 0 || item.comments > 0) {
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            if (item.reactions > 0) {
                                Icon(Icons.Rounded.Favorite, null, tint = colors.danger, modifier = Modifier.size(8.dp))
                                Text("${item.reactions}", color = colors.textMuted, fontSize = 6.5.sp)
                            }
                            if (item.comments > 0) {
                                Icon(Icons.Rounded.ChatBubbleOutline, null, tint = colors.textMuted, modifier = Modifier.size(8.dp))
                                Text("${item.comments}", color = colors.textMuted, fontSize = 6.5.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ApprovalInbox(
    items: List<MemberApprovalRequest>,
    theme: ThemeId,
    onAction: (MemberApprovalRequest, ApprovalStatus) -> Unit
) {
    val colors = BADGymTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            SectionTitle("APPROVAL INBOX", theme)
            Text("email / app workflow", color = colors.textMuted, fontSize = 7.5.sp)
        }
        items.forEach { item ->
            InfoCard(theme) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(26.dp).clip(CircleShape).background(colors.accentSoft), contentAlignment = Alignment.Center) {
                        Icon(approvalIcon(item.channel), null, tint = colors.accent, modifier = Modifier.size(14.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.subject, color = colors.textPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(listOfNotNull(item.requestedBy, item.preview).joinToString(" • "), color = colors.textMuted, fontSize = 8.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        item.amount?.let { Text(formatInr(it), color = colors.textPrimary, fontSize = 9.sp, fontWeight = FontWeight.Black) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        MiniAction("✓", colors.success) { onAction(item, ApprovalStatus.APPROVED) }
                        MiniAction("×", colors.danger) { onAction(item, ApprovalStatus.REJECTED) }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecognitionPulse(
    items: List<MemberRecognition>,
    points: Int?,
    tier: String?,
    theme: ThemeId,
    onClick: (MemberRecognition) -> Unit
) {
    val colors = BADGymTheme.colors
    InfoCard(theme) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Rounded.EmojiEvents, null, tint = colors.warning, modifier = Modifier.size(15.dp))
                    Text("RECOGNITION & REWARDS", color = colors.textPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.Black)
                }
                points?.let { Text("${it} pts${tier?.let { value -> " • $value" } ?: ""}", color = colors.accent, fontSize = 8.5.sp, fontWeight = FontWeight.Black) }
            }
            items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(7.dp))
                        .background(colors.surfaceMuted.copy(alpha = 0.72f)).clickable { onClick(item) }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(recognitionIcon(item.type), null, tint = recognitionColor(item.type, colors), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(5.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title, color = colors.textPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(item.value, color = colors.textMuted, fontSize = 8.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Icon(Icons.Rounded.ArrowForward, null, tint = colors.textMuted, modifier = Modifier.size(11.dp))
                }
            }
        }
    }
}

@Composable
private fun GymRecognitionBoard(items: List<GymRecognitionEntry>, theme: ThemeId, onClick: (GymRecognitionEntry) -> Unit) {
    val colors = BADGymTheme.colors
    InfoCard(theme) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Rounded.TrendingUp, null, tint = colors.accent, modifier = Modifier.size(14.dp))
                    Text("GYM RECOGNITION BOARD", color = colors.textPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.Black)
                }
                Text("real gym data", color = colors.textMuted, fontSize = 7.5.sp)
            }
            items.forEach { entry ->
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(7.dp))
                        .background(if (entry.isCurrentMember) colors.accentSoft else colors.surfaceMuted.copy(alpha = 0.7f))
                        .clickable { onClick(entry) }.padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(21.dp).clip(CircleShape).background(if (entry.isCurrentMember) colors.accent else colors.surface), contentAlignment = Alignment.Center) {
                        Text("#${entry.rank}", color = if (entry.isCurrentMember) colors.textOnAccent else colors.textPrimary, fontSize = 7.5.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(Modifier.width(5.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(entry.memberName, color = colors.textPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(entry.reason ?: entry.metric, color = colors.textMuted, fontSize = 7.5.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text(entry.value, color = colors.accent, fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun MiniAction(label: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(23.dp).clip(RoundedCornerShape(7.dp))
            .background(color.copy(alpha = 0.12f)).border(0.8.dp, color.copy(alpha = 0.35f), RoundedCornerShape(7.dp))
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Black)
    }
}

private fun approvalIcon(channel: ApprovalChannel): ImageVector = when (channel) {
    ApprovalChannel.EMAIL -> Icons.Rounded.Email
    ApprovalChannel.WHATSAPP -> Icons.Rounded.ChatBubbleOutline
    ApprovalChannel.APP -> Icons.Rounded.Notifications
    ApprovalChannel.INTERNAL -> Icons.Rounded.ReceiptLong
}

private fun recognitionIcon(type: RecognitionType): ImageVector = when (type) {
    RecognitionType.ATTENDANCE -> Icons.Rounded.CheckCircle
    RecognitionType.PAYMENT_ON_TIME -> Icons.Rounded.ReceiptLong
    RecognitionType.STREAK -> Icons.Rounded.TrendingUp
    RecognitionType.WORKOUT -> Icons.Rounded.FitnessCenter
    RecognitionType.GOAL -> Icons.Rounded.ThumbUp
    RecognitionType.REFERRAL -> Icons.Rounded.ChatBubbleOutline
    RecognitionType.MILESTONE -> Icons.Rounded.EmojiEvents
}

private fun recognitionColor(type: RecognitionType, colors: com.example.badnewgym.feature.memberintelligence.design.colors.BADGymColors): Color = when (type) {
    RecognitionType.PAYMENT_ON_TIME -> colors.success
    RecognitionType.ATTENDANCE, RecognitionType.STREAK -> colors.accent
    RecognitionType.WORKOUT, RecognitionType.GOAL -> colors.vip
    RecognitionType.REFERRAL, RecognitionType.MILESTONE -> colors.warning
}

private fun formatInr(value: Double): String =
    java.text.NumberFormat.getCurrencyInstance(java.util.Locale("en", "IN")).format(value)
