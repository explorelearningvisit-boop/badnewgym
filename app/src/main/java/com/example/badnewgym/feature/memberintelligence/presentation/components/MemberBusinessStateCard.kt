package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MemberBusinessStateCard(
    snapshot: MemberSnapshot,
    event: MemberEvent?,
    theme: ThemeId,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors
    val type = event?.eventType
    val variant = when (type) {
        EventType.MEMBERSHIP_EXPIRED, EventType.EXPIRED -> Variant.EXPIRED
        EventType.FREEZE_STARTED, EventType.FREEZE -> Variant.FROZEN
        EventType.PAYMENT_OVERDUE, EventType.PAYMENT_FAILED, EventType.PAYMENT_DUE -> Variant.PAYMENT
        EventType.TRAINER_SESSION_SCHEDULED, EventType.TRAINER_SESSION_STARTED, EventType.TRAINER_SESSION_MISSED -> Variant.TRAINER
        EventType.SERVICE_ISSUE, EventType.SERVICE_EXPIRED, EventType.SERVICE_BOOKED -> Variant.SERVICE
        EventType.MACHINE_FAULT, EventType.MACHINE_REPORTED, EventType.MAINTENANCE_STARTED, EventType.CLEANING_STARTED, EventType.STOCK_LOW -> Variant.OPERATIONS
        else -> null
    } ?: return

    val accent by animateColorAsState(
        when (variant) {
            Variant.EXPIRED, Variant.PAYMENT -> colors.danger
            Variant.FROZEN -> colors.info
            Variant.TRAINER -> colors.accent
            Variant.SERVICE -> colors.vip
            Variant.OPERATIONS -> colors.warning
        },
        tween(180),
        label = "business-state-accent"
    )

    val title = when (variant) {
        Variant.EXPIRED -> "MEMBERSHIP EXPIRED"
        Variant.FROZEN -> "MEMBERSHIP FROZEN"
        Variant.PAYMENT -> "PAYMENT NEEDS ATTENTION"
        Variant.TRAINER -> "PERSONAL TRAINING"
        Variant.SERVICE -> "MEMBER SERVICE"
        Variant.OPERATIONS -> "GYM OPERATIONS"
    }

    val headline = when (variant) {
        Variant.EXPIRED -> snapshot.membership?.planName ?: "Plan expired"
        Variant.FROZEN -> event?.metadata?.get("reason") ?: "Access paused"
        Variant.PAYMENT -> paymentHeadline(snapshot)
        Variant.TRAINER -> snapshot.trainer?.trainerName ?: "PT activity"
        Variant.SERVICE -> event?.metadata?.get("serviceName") ?: snapshot.services.orEmpty().firstOrNull { it.isActive }?.serviceName ?: "Service activity"
        Variant.OPERATIONS -> event?.metadata?.get("asset") ?: event?.metadata?.get("reason") ?: event?.eventType?.displayLabel() ?: "Operational activity"
    }

    val stateLine = when (variant) {
        Variant.EXPIRED -> "NOW • " + (snapshot.membership?.daysRemaining?.let { kotlin.math.abs(it).toString() + "d past expiry" } ?: "renewal required")
        Variant.FROZEN -> "NOW • " + (event?.metadata?.get("freezeEnd") ?: "resume date not recorded")
        Variant.PAYMENT -> "NOW • " + (snapshot.payment?.overdueDays ?: 0) + " days overdue"
        Variant.TRAINER -> "FUTURE • " + if (snapshot.trainer?.nextSessionDate != null) "session scheduled" else "schedule not recorded"
        Variant.SERVICE -> if (event?.eventType == EventType.SERVICE_EXPIRED) "PAST → ACTION • service expired" else "NOW / FUTURE • entitlement & booking"
        Variant.OPERATIONS -> if (event?.eventType == EventType.CLEANING_STARTED) "NOW • cleaning in progress" else "NOW / PAST • operational record"
    }

    Column(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(accent.copy(alpha = 0.075f)).border(1.dp, accent.copy(alpha = 0.34f), RoundedCornerShape(16.dp)).padding(horizontal = 11.dp, vertical = 9.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            androidx.compose.material3.Icon(variant.icon(), null, tint = accent)
            Column(modifier = Modifier.weight(1f)) {
                androidx.compose.material3.Text(title, color = accent, fontSize = 8.5.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
                androidx.compose.material3.Text(headline, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            androidx.compose.material3.Text(stateLine, color = accent, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, maxLines = 2)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            BusinessStateChip("NOW", accent)
            BusinessStateChip(if (variant == Variant.TRAINER || variant == Variant.SERVICE) "FUTURE" else if (variant == Variant.EXPIRED || variant == Variant.PAYMENT) "ACTION" else "EVIDENCE", colors.textSecondary)
            event?.metadata?.get("actor")?.let { BusinessStateChip("BY " + it, colors.textMuted) }
        }
    }
}

private fun paymentHeadline(snapshot: MemberSnapshot): String {
    val due = snapshot.payment?.totalOutstanding ?: 0.0
    return if (due > 0) "₹" + NumberFormat.getNumberInstance(Locale("en", "IN")).format(due.toInt()) + " outstanding" else "Payment status requires review"
}

@Composable
private fun BusinessStateChip(text: String, color: Color) {
    androidx.compose.material3.Text(text, color = color, fontSize = 7.5.sp, fontWeight = FontWeight.Black, modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(color.copy(alpha = 0.09f)).padding(horizontal = 6.dp, vertical = 3.dp))
}

private enum class Variant { EXPIRED, FROZEN, PAYMENT, TRAINER, SERVICE, OPERATIONS }

private fun Variant.icon(): ImageVector = when (this) {
    Variant.EXPIRED -> Icons.Rounded.Lock
    Variant.FROZEN -> Icons.Rounded.PauseCircle
    Variant.PAYMENT -> Icons.Rounded.CreditCard
    Variant.TRAINER -> Icons.Rounded.Person
    Variant.SERVICE -> Icons.Rounded.EventAvailable
    Variant.OPERATIONS -> Icons.Rounded.Build
}