package com.example.badnewgym.feature.memberintelligence.presentation.components.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.MediaAssets
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.presentation.components.AvatarImage
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberPhoto
import com.example.badnewgym.feature.memberintelligence.presentation.components.RemoteOrAssetImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PaymentHistoryRow(
    val dateLabel: String,
    val amountLabel: String,
    val method: String
)

object HomeDemoData {
    val weeklyAttendance: List<Float> = listOf(0.35f, 0.7f, 0.55f, 0.95f, 0.62f, 0.28f, 0.82f)
    val weekLabels: List<String> = listOf("M", "T", "W", "T", "F", "S", "S")
    val monthBars: List<Float> = listOf(
        0.25f, 0.45f, 0.35f, 0.7f, 0.55f, 0.8f, 0.4f, 0.9f, 0.5f, 0.65f,
        0.75f, 0.3f, 0.85f, 0.6f, 0.95f, 0.4f, 0.7f, 0.55f, 0.8f, 0.45f,
        0.9f, 0.35f, 0.6f, 0.75f, 0.5f, 0.85f, 0.65f, 0.4f, 0.7f, 0.55f
    )
    val paymentHistory: List<PaymentHistoryRow> = listOf(
        PaymentHistoryRow("18 Aug", "₹1,400", "UPI"),
        PaymentHistoryRow("02 Aug", "₹2,000", "Cash"),
        PaymentHistoryRow("12 Jul", "₹1,400", "UPI"),
        PaymentHistoryRow("28 Jun", "₹1,500", "Card"),
        PaymentHistoryRow("05 Jun", "₹900", "UPI")
    )
    const val streakDays = 4
    const val avgPerWeek = 3.2f
    const val wheyTitle = "MuscleBlaze Whey 2kg"
    const val wheySubtitle = "Limited Time Offer • Member exclusive"
    const val wheyMrp = "₹4,999"
    const val wheyPrice = "₹4,249"
    const val wheyDiscount = "15% OFF"
}

fun MemberSnapshot.attendancePercent(): Int {
    val target = attendance?.target ?: 0
    val visits = attendance?.visits ?: 0
    // Design board shows 61% for 16/26 (truncate, not round-half-up)
    return if (target > 0) ((visits * 100) / target) else 0
}

fun MemberSnapshot.memberCode(): String = identity.code ?: "BG${id.padStart(3, '0')}"

fun MemberSnapshot.activeServicesCount(): Int = services?.count { it.isActive } ?: 0

fun MemberSnapshot.planLine(): String {
    val plan = membership?.planName ?: "Plan"
    val type = membership?.planType
    return if (type.isNullOrBlank()) plan else "$plan • $type"
}

fun formatEventTime(millis: Long): String =
    SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(millis))

fun formatEventDate(millis: Long): String =
    SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(millis))

fun EventType.timelineLabel(): String = when (this) {
    EventType.CHECK_IN -> "Check-in"
    EventType.CHECK_OUT -> "Check-out"
    EventType.WORKOUT -> "Workout"
    EventType.TRAINER_SESSION -> "PT Session"
    EventType.PAYMENT -> "Payment"
    EventType.SUPPLEMENT_PURCHASE -> "Supplement Purchase"
    EventType.RENEWAL -> "Plan Renewal"
    else -> name.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }
}

@Composable
fun CheckInHeader(
    occurredAt: Long,
    modifier: Modifier = Modifier,
    pillColor: Color = BADGymTheme.colors.success,
    timeColor: Color = BADGymTheme.colors.textPrimary,
    subtitleColor: Color = BADGymTheme.colors.textSecondary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(pillColor)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(BADGymTheme.colors.surfaceElevated)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CHECK-IN",
                    color = BADGymTheme.colors.textOnAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatEventTime(occurredAt),
                color = timeColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = "Just now", color = subtitleColor, fontSize = 13.sp)
        }
    }
}

@Composable
fun MemberHero(
    snapshot: MemberSnapshot,
    modifier: Modifier = Modifier,
    showStatusChip: Boolean = true,
    nameColor: Color = BADGymTheme.colors.textPrimary,
    metaColor: Color = BADGymTheme.colors.textSecondary
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        MemberPhoto(
            photoUrl = snapshot.identity.photoUrl,
            tier = snapshot.identity.tier,
            showVerified = true
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = snapshot.identity.name,
                    color = nameColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Rounded.Verified,
                    contentDescription = null,
                    tint = BADGymTheme.colors.info,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = snapshot.memberCode(),
                color = metaColor,
                fontSize = 14.sp
            )
            Text(
                text = snapshot.planLine(),
                color = metaColor,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (showStatusChip) {
                Spacer(modifier = Modifier.height(6.dp))
                StatusChip(snapshot = snapshot)
            }
        }
    }
}

@Composable
fun StatusChip(
    snapshot: MemberSnapshot,
    modifier: Modifier = Modifier
) {
    val active = snapshot.membership?.isActive == true
    val bg = if (active) BADGymTheme.colors.successSoft else BADGymTheme.colors.dangerSoft
    val fg = if (active) BADGymTheme.colors.success else BADGymTheme.colors.danger
    val days = snapshot.membership?.daysRemaining ?: 0
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = if (active) "ACTIVE • $days Days Left" else "INACTIVE",
            color = fg,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun AttendanceRing(
    percent: Int,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    stroke: Dp = 5.dp,
    trackColor: Color = BADGymTheme.colors.surfaceMuted,
    progressColor: Color = BADGymTheme.colors.success,
    textColor: Color = BADGymTheme.colors.textPrimary
) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = stroke.toPx()
            val diameter = size.toPx() - strokeWidth
            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
            val arcSize = Size(diameter, diameter)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * (percent.coerceIn(0, 100) / 100f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "$percent%",
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MiniBarChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    barColor: Color = BADGymTheme.colors.accent,
    height: Dp = 36.dp
) {
    Row(
        modifier = modifier.height(height),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEach { value ->
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height((height.value * value.coerceIn(0.08f, 1f)).dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }
    }
}

@Composable
fun LabeledBarChart(
    values: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    barColor: Color = BADGymTheme.colors.accent,
    labelColor: Color = BADGymTheme.colors.textMuted,
    height: Dp = 56.dp
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            values.forEach { value ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height((height.value * value.coerceIn(0.1f, 1f)).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(barColor)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            labels.take(values.size).forEach { label ->
                Text(
                    text = label,
                    color = labelColor,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun MetricPanel(
    title: String,
    modifier: Modifier = Modifier,
    background: Color = BADGymTheme.colors.surfaceElevated,
    titleColor: Color = BADGymTheme.colors.textSecondary,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(12.dp)
    ) {
        Text(
            text = title, 
            color = titleColor, 
            fontSize = 13.sp, 
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun DueBlock(
    amount: Double?,
    overdueDays: Int?,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val isOverdue = (amount ?: 0.0) > 0.0
    val bgColor = if (isOverdue) BADGymTheme.colors.dangerSoft else BADGymTheme.colors.surfaceElevated
    val titleColor = if (isOverdue) BADGymTheme.colors.danger else BADGymTheme.colors.textSecondary
    val amountColor = if (isOverdue) BADGymTheme.colors.danger else BADGymTheme.colors.textPrimary
    
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(12.dp)
    ) {
        Text(
            text = if (compact) "Due" else "Outstanding Due",
            color = titleColor,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = amount?.let { com.example.badnewgym.feature.memberintelligence.domain.format.MoneyFormat.inr(it) } ?: "₹0",
            color = amountColor,
            fontSize = if (compact) 16.sp else 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (isOverdue) {
            Text(
                text = "Overdue: ${overdueDays ?: 0} days",
                color = titleColor,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ShortcutTriple(
    trainer: String,
    workout: String,
    services: String,
    modifier: Modifier = Modifier,
    trainerPhoto: String? = MediaAssets.TRAINER_VIKAS,
    iconBg: Color = BADGymTheme.colors.surfaceMuted,
    labelColor: Color = BADGymTheme.colors.textPrimary,
    valueColor: Color = BADGymTheme.colors.textSecondary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(BADGymTheme.colors.surfaceMuted.copy(alpha = 0.6f))
                .border(1.dp, BADGymTheme.colors.border.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(vertical = 6.dp, horizontal = 4.dp)
        ) {
            AvatarImage(
                model = trainerPhoto,
                size = 36.dp,
                shape = RoundedCornerShape(18.dp),
                placeholderTint = iconBg
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Trainer", color = labelColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = trainer, color = valueColor, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        ShortcutCell("W", "Workout", workout, iconBg, labelColor, valueColor, Modifier.weight(1f))
        ShortcutCell("S", "Services", services, iconBg, labelColor, BADGymTheme.colors.success, Modifier.weight(1f))
    }
}

@Composable
private fun ShortcutCell(
    glyph: String,
    label: String,
    value: String,
    iconBg: Color,
    labelColor: Color,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BADGymTheme.colors.surfaceMuted.copy(alpha = 0.6f))
            .border(1.dp, BADGymTheme.colors.border.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconBg)
                .border(1.dp, BADGymTheme.colors.border.copy(alpha = 0.7f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = glyph, color = labelColor, fontWeight = FontWeight.Black, fontSize = 15.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = labelColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Text(
            text = value,
            color = valueColor,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AlertBanner(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BADGymTheme.colors.dangerSoft)
            .border(1.dp, BADGymTheme.colors.danger.copy(alpha=0.3f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(BADGymTheme.colors.danger),
            contentAlignment = Alignment.Center
        ) {
            Text("!", color = BADGymTheme.colors.textOnAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = BADGymTheme.colors.danger, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = BADGymTheme.colors.danger.copy(alpha=0.8f), fontSize = 13.sp)
        }
        Text("›", color = BADGymTheme.colors.danger, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun QuoteFooter(
    text: String,
    modifier: Modifier = Modifier,
    background: Color = BADGymTheme.colors.surfaceElevated,
    textColor: Color = BADGymTheme.colors.textSecondary
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PrimaryCta(
    label: String,
    modifier: Modifier = Modifier,
    background: Color = BADGymTheme.colors.accent,
    contentColor: Color = BADGymTheme.colors.textOnAccent
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = contentColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PromoBanner(
    title: String = HomeDemoData.wheyTitle,
    subtitle: String = HomeDemoData.wheySubtitle,
    badge: String = HomeDemoData.wheyDiscount,
    imageUrl: String = MediaAssets.WHEY_PRODUCT,
    modifier: Modifier = Modifier,
    showPrice: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BADGymTheme.colors.surfaceElevated)
            .border(1.dp, BADGymTheme.colors.border, RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RemoteOrAssetImage(
            model = imageUrl,
            contentDescription = title,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = BADGymTheme.colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = BADGymTheme.colors.textSecondary, fontSize = 13.sp)
            if (showPrice) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = HomeDemoData.wheyMrp,
                        color = BADGymTheme.colors.textMuted,
                        fontSize = 13.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = HomeDemoData.wheyPrice,
                        color = BADGymTheme.colors.success,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(BADGymTheme.colors.danger)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = badge, color = BADGymTheme.colors.textOnAccent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

fun demoTimeline(snapshot: MemberSnapshot, currentEvent: MemberEvent): List<MemberEvent> {
    val base = snapshot.recentEvents
    if (base.size >= 5) return base.take(5)
    val now = currentEvent.occurredAt
    return listOf(
        currentEvent,
        MemberEvent("t2", snapshot.id, snapshot.gymId, EventType.WORKOUT, now - 86_400_000L),
        MemberEvent("t3", snapshot.id, snapshot.gymId, EventType.TRAINER_SESSION, now - 2 * 86_400_000L),
        MemberEvent("t4", snapshot.id, snapshot.gymId, EventType.PAYMENT, now - 5 * 86_400_000L),
        MemberEvent("t5", snapshot.id, snapshot.gymId, EventType.SUPPLEMENT_PURCHASE, now - 10 * 86_400_000L)
    )
}
