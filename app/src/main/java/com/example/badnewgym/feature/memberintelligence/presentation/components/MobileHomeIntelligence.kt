package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.formatEventTime
import java.text.NumberFormat
import java.util.Locale

/**
 * Compact Home for the existing single-member operational card.
 * Home is a member briefing: identity + one decision + small cross-domain pulse
 * + live context + one curiosity hook + one action. Detailed evidence stays in menus.
 */
@Composable
fun MobileHomeIntelligence(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    theme: ThemeId,
    expanded: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onCta: (SignalAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = BADGymTheme.colors

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(c.surface)
            .border(1.dp, c.border.copy(alpha = .6f), RoundedCornerShape(18.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        CompactMemberHeader(snapshot, currentEvent)

        DecisionCard(
            snapshot = snapshot,
            primarySignal = primarySignal ?: signals.firstOrNull()
        )

        DomainPulse(snapshot)
        LiveContext(snapshot, currentEvent)

        CuriosityRow(
            snapshot = snapshot,
            secondarySignals = secondarySignals,
            onMore = onExpand
        )

        CompactPrimaryAction(snapshot, cta, onCta)
    }
}

@Composable
private fun CompactMemberHeader(snapshot: MemberSnapshot, event: MemberEvent?) {
    val c = BADGymTheme.colors
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        MemberPhoto(
            photoUrl = snapshot.identity.photoUrl,
            tier = snapshot.identity.tier,
            showVerified = snapshot.identity.isVerified,
            size = 62.dp
        )
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    snapshot.identity.name,
                    color = c.textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (snapshot.identity.isVerified) {
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Rounded.CheckCircle, null, tint = c.info, modifier = Modifier.size(13.dp))
                }
            }
            val code = snapshot.identity.code ?: "MEMBER"
            val plan = snapshot.membership?.planName ?: "No active plan"
            Text(
                code + " • " + plan,
                color = c.textSecondary,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val membership = snapshot.membership
            Text(
                if (membership?.isActive == true) "ACTIVE • " + membership.daysRemaining + " days left" else "MEMBERSHIP ATTENTION",
                color = if (membership?.isActive == true) c.success else c.danger,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black
            )
        }
        EventPill(event)
    }
}

@Composable
private fun EventPill(event: MemberEvent?) {
    val c = BADGymTheme.colors
    Box(
        Modifier
            .clip(RoundedCornerShape(9.dp))
            .background(c.accentSoft)
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Text(
            event?.let(::shortEventLabel) ?: "HOME",
            color = c.accentStrong,
            fontSize = 7.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun DecisionCard(
    snapshot: MemberSnapshot,
    primarySignal: IntelligenceSignal?
) {
    val c = BADGymTheme.colors
    val due = snapshot.payment?.totalOutstanding ?: 0.0
    val days = snapshot.payment?.overdueDays ?: 0

    val title: String
    val detail: String

    when {
        due > 0 -> {
            title = "PAYMENT ATTENTION"
            detail = money(due) + " overdue" + if (days > 0) " • " + days + " days" else ""
        }
        snapshot.membership?.daysRemaining?.let { it in 0..14 } == true -> {
            title = "RENEWAL OPPORTUNITY"
            detail = (snapshot.membership?.daysRemaining ?: 0).toString() + " days left • start renewal"
        }
        snapshot.trainer?.let { it.sessionsTotal - it.sessionsUsed > 0 } == true -> {
            title = "PT OPPORTUNITY"
            detail = (snapshot.trainer!!.sessionsTotal - snapshot.trainer.sessionsUsed).toString() + " sessions unused"
        }
        snapshot.attendance?.avgVisitsPerWeek?.let { it < 2.0 } == true -> {
            title = "ATTENDANCE SIGNAL"
            detail = "Visit consistency needs attention"
        }
        primarySignal != null -> {
            title = primarySignal.title.uppercase(Locale.getDefault())
            detail = primarySignal.subtitle ?: "Member intelligence signal"
        }
        else -> {
            title = "MEMBER ON TRACK"
            detail = "No urgent action • keep relationship active"
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(if (due > 0) c.danger.copy(alpha = .08f) else c.accentSoft.copy(alpha = .55f))
            .border(
                1.dp,
                if (due > 0) c.danger.copy(alpha = .24f) else c.border.copy(alpha = .55f),
                RoundedCornerShape(13.dp)
            )
            .padding(horizontal = 9.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(25.dp).clip(CircleShape).background(if (due > 0) c.danger else c.accent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (due > 0) Icons.Rounded.CreditCard else Icons.Rounded.AutoAwesome,
                null,
                tint = Color.White,
                modifier = Modifier.size(13.dp)
            )
        }
        Spacer(Modifier.width(7.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = c.textPrimary, fontSize = 8.sp, fontWeight = FontWeight.Black)
            Text(detail, color = if (due > 0) c.danger else c.textSecondary, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text("→", color = if (due > 0) c.danger else c.accentStrong, fontSize = 16.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun DomainPulse(snapshot: MemberSnapshot) {
    val c = BADGymTheme.colors
    val attendance = snapshot.attendance
    val payment = snapshot.payment
    val trainer = snapshot.trainer
    val nutrition = snapshot.nutrition
    val services = snapshot.services.orEmpty()

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Pulse("ATTEND", attendance?.let { it.visits.toString() + "/" + (it.target?.toString() ?: "—") } ?: "—", attendance?.streakDays?.let { it.toString() + "d" } ?: "—", c.success, Modifier.weight(1f))
        Pulse("PAY", if ((payment?.totalOutstanding ?: 0.0) > 0) money(payment?.totalOutstanding ?: 0.0) else "PAID", if ((payment?.overdueDays ?: 0) > 0) (payment?.overdueDays.toString() + "d late") else "clear", if ((payment?.totalOutstanding ?: 0.0) > 0) c.danger else c.success, Modifier.weight(1f))
        Pulse("PT", trainer?.let { (it.sessionsTotal - it.sessionsUsed).toString() } ?: "—", "left", Color(0xFF8B5CF6), Modifier.weight(1f))
        Pulse("NUTRI", if (nutrition?.isSubscribed == true) "ON" else "OFF", nutrition?.planName ?: "plan", c.info, Modifier.weight(1f))
        Pulse("SERVICE", services.count { it.isActive }.toString(), "active", c.warning, Modifier.weight(1f))
    }
}

@Composable
private fun Pulse(label: String, value: String, sub: String, accent: Color, modifier: Modifier) {
    val c = BADGymTheme.colors
    Column(
        modifier
            .height(55.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(c.surfaceElevated)
            .border(1.dp, c.border.copy(alpha = .45f), RoundedCornerShape(10.dp))
            .padding(horizontal = 2.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = c.textMuted, fontSize = 5.8.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(value, color = if (label == "PAY" && value != "PAID") c.danger else c.textPrimary, fontSize = 8.5.sp, fontWeight = FontWeight.Black, maxLines = 1)
        Text(sub, color = accent, fontSize = 5.6.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun LiveContext(snapshot: MemberSnapshot, event: MemberEvent?) {
    val c = BADGymTheme.colors
    val eventText = when (event?.eventType) {
        EventType.CHECK_IN -> "Checked in"
        EventType.CHECK_OUT -> "Checked out"
        EventType.WORKOUT -> "Workout active"
        EventType.TRAINER_SESSION -> "PT session"
        EventType.PAYMENT -> "Payment activity"
        EventType.SUPPLEMENT_PURCHASE -> "Supplement purchase"
        EventType.RENEWAL -> "Renewal activity"
        else -> "Member live context"
    }
    Row(
        Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(c.surfaceMuted)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(c.success))
        Spacer(Modifier.width(6.dp))
        Column(Modifier.weight(1f)) {
            Text(eventText, color = c.textPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            Text(event?.occurredAt?.let(::formatEventTime) ?: "No live event", color = c.textSecondary, fontSize = 7.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(snapshot.workout?.currentRoutine ?: "No active routine", color = c.textPrimary, fontSize = 7.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            snapshot.workout?.durationMinutes?.let { Text(it.toString() + " min", color = c.textSecondary, fontSize = 6.5.sp) }
        }
    }
}

@Composable
private fun CuriosityRow(
    snapshot: MemberSnapshot,
    secondarySignals: List<IntelligenceSignal>,
    onMore: () -> Unit
) {
    val c = BADGymTheme.colors
    val signal = secondarySignals.firstOrNull()
    val supplement = snapshot.supplements
    val services = snapshot.services.orEmpty()

    val title: String
    val detail: String
    when {
        signal != null -> {
            title = signal.title
            detail = signal.subtitle ?: "Open related intelligence"
        }
        supplement?.hasHistory == true && supplement.lastPurchaseName != null -> {
            title = "Recent purchase"
            detail = supplement.lastPurchaseName ?: "Product history"
        }
        services.any { it.isActive } -> {
            title = "Active services"
            detail = services.count { it.isActive }.toString() + " services • tap for detail"
        }
        snapshot.nutrition?.isSubscribed == true -> {
            title = "Nutrition"
            detail = snapshot.nutrition?.planName ?: "Active plan"
        }
        else -> {
            title = "Member intelligence"
            detail = "Open detailed member view"
        }
    }

    Row(
        Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(c.accentSoft.copy(alpha = .45f))
            .clickable(onClick = onMore)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.AutoAwesome, null, tint = c.accentStrong, modifier = Modifier.size(13.dp))
        Spacer(Modifier.width(5.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = c.textPrimary, fontSize = 7.8.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(detail, color = c.textSecondary, fontSize = 6.8.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text("More →", color = c.accentStrong, fontSize = 6.8.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun CompactPrimaryAction(snapshot: MemberSnapshot, cta: SignalAction?, onCta: (SignalAction) -> Unit) {
    val c = BADGymTheme.colors
    val due = snapshot.payment?.totalOutstanding ?: 0.0
    val label = if (due > 0) "COLLECT PAYMENT" else cta?.label?.uppercase() ?: "OPEN MEMBER"

    Box(
        Modifier
            .fillMaxWidth()
            .height(39.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(c.accent)
            .clickable { cta?.let(onCta) },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.CreditCard, null, tint = c.textOnAccent, modifier = Modifier.size(15.dp))
            Spacer(Modifier.width(6.dp))
            Text(label, color = c.textOnAccent, fontSize = 10.sp, fontWeight = FontWeight.Black)
        }
    }
}

private fun shortEventLabel(event: MemberEvent): String = when (event.eventType) {
    EventType.CHECK_IN -> "IN"
    EventType.CHECK_OUT -> "OUT"
    EventType.WORKOUT -> "WORKOUT"
    EventType.TRAINER_SESSION -> "PT"
    EventType.PAYMENT -> "PAY"
    EventType.SUPPLEMENT_PURCHASE -> "BUY"
    EventType.RENEWAL -> "RENEW"
    else -> "LIVE"
}

private fun money(value: Double): String =
    "₹" + NumberFormat.getNumberInstance(Locale("en", "IN")).format(value.toInt())
