package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ─────────────────────────────────────────────────────────────────────────────
// ATTENDANCE PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AttendancePanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val att = snapshot.attendance
    val visits = att?.visits ?: 0
    val target = (att?.target ?: 26).coerceAtLeast(1)
    val progress = (visits.toFloat() / target).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600),
        label = "att-ring"
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("ATTENDANCE", theme)

        // Ring + stats row
        InfoCard(theme) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Attendance ring
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .semantics {
                            contentDescription =
                                "${(progress * 100).toInt()}% attendance rate"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val accent = colors.accent
                    val track = colors.surfaceMuted
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        val inset = stroke.width / 2f
                        val arcSize = Size(size.width - stroke.width, size.height - stroke.width)
                        val topLeft = Offset(inset, inset)
                        drawArc(color = track, startAngle = -90f, sweepAngle = 360f,
                            useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
                        drawArc(color = accent, startAngle = -90f,
                            sweepAngle = 360f * animatedProgress,
                            useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = colors.accent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text("rate", color = colors.textMuted, fontSize = 10.sp)
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = att?.periodName ?: "Current Period",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        KpiChip("$visits", "visits", colors.accent)
                        KpiChip("$target", "target", colors.textSecondary)
                        KpiChip("${att?.streakDays ?: 0}d", "streak", colors.success)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Rounded.Schedule, null, tint = colors.textMuted, modifier = Modifier.size(12.dp))
                        Text(
                            text = att?.preferredSlot ?: "–",
                            color = colors.textMuted,
                            fontSize = 12.sp
                        )
                        Text("·", color = colors.textMuted, fontSize = 12.sp)
                        Text(
                            text = "${String.format(Locale.getDefault(), "%.1f", att?.avgVisitsPerWeek ?: 0.0)}/wk avg",
                            color = colors.textMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 7-day heat strip
        val weeklyPattern = att?.weeklyPattern ?: listOf(1, 1, 0, 1, 1, 0, 1)
        val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
        InfoCard(theme) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                weeklyPattern.take(7).forEachIndexed { i, active ->
                    val attended = active > 0
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(if (attended) colors.accent else colors.surfaceMuted)
                                .border(
                                    0.8.dp,
                                    if (attended) colors.accent else colors.border.copy(alpha = 0.4f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (attended) {
                                Icon(Icons.Rounded.Check, null, tint = colors.textOnAccent,
                                    modifier = Modifier.size(12.dp))
                            }
                        }
                        Text(
                            dayLabels.getOrElse(i) { "·" },
                            color = if (attended) colors.textPrimary else colors.textMuted,
                            fontSize = 11.sp,
                            fontWeight = if (attended) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Recent gate events — compact timeline
        val events = snapshot.recentEvents
            .filter { it.eventType == EventType.CHECK_IN || it.eventType == EventType.CHECK_OUT }
            .sortedByDescending { it.occurredAt }
        if (events.isNotEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    events.take(3).forEachIndexed { i, ev ->
                        val isIn = ev.eventType == EventType.CHECK_IN
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                Icon(
                                    if (isIn) Icons.AutoMirrored.Rounded.Login else Icons.AutoMirrored.Rounded.Logout,
                                    null,
                                    tint = if (isIn) colors.success else colors.textMuted,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(ev.eventType.displayLabel(), color = colors.textPrimary,
                                    fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Text(formatDate(ev.occurredAt), color = colors.textSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PLAN / MEMBERSHIP PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PlanPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val mem = snapshot.membership
    val isExpired = mem?.isActive == false || (mem?.daysRemaining ?: 1) <= 0
    val daysRemaining = mem?.daysRemaining ?: 0
    val totalDays = if (mem != null) {
        ((mem.expiryDate - mem.startDate) / 86400000L).toInt().coerceAtLeast(1)
    } else 365
    val daysProgress = 1f - (daysRemaining.toFloat() / totalDays).coerceIn(0f, 1f)
    val animatedDays by animateFloatAsState(
        targetValue = daysProgress, animationSpec = tween(600), label = "plan-days")

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("MEMBERSHIP PLAN", theme)

        // Plan status hero row
        InfoCard(theme) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Days remaining ring
                Box(
                    modifier = Modifier.size(68.dp).semantics {
                        contentDescription = "$daysRemaining days remaining"
                    },
                    contentAlignment = Alignment.Center
                ) {
                    val ringColor = when {
                        isExpired -> colors.danger
                        daysRemaining <= 7 -> colors.warning
                        else -> colors.success
                    }
                    val track = colors.surfaceMuted
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                        val inset = stroke.width / 2f
                        val arcSize = Size(size.width - stroke.width, size.height - stroke.width)
                        val topLeft = Offset(inset, inset)
                        drawArc(color = track, startAngle = -90f, sweepAngle = 360f,
                            useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
                        drawArc(color = ringColor, startAngle = -90f,
                            sweepAngle = 360f * animatedDays,
                            useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val ringColor = when {
                            isExpired -> colors.danger
                            daysRemaining <= 7 -> colors.warning
                            else -> colors.success
                        }
                        Text(
                            text = if (isExpired) "0" else "$daysRemaining",
                            color = ringColor, fontSize = 15.sp, fontWeight = FontWeight.Black
                        )
                        Text("days", color = colors.textMuted, fontSize = 10.sp)
                    }
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mem?.planName ?: "No Plan",
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        StatusBadge(
                            label = if (isExpired) "EXPIRED" else "ACTIVE",
                            bgColor = if (isExpired) colors.dangerSoft else colors.successSoft,
                            textColor = if (isExpired) colors.danger else colors.success
                        )
                    }
                    Text(
                        text = mem?.planType ?: "–",
                        color = colors.textSecondary, fontSize = 12.5.sp
                    )
                    mem?.currentCost?.let {
                        Text("₹${it.toInt()}", color = colors.accent, fontSize = 13.sp,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Start → Expiry visual timeline
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("START", color = colors.textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(if (mem != null) formatDateOnly(mem.startDate) else "–",
                            color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("EXPIRY", color = colors.textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(if (mem != null) formatDateOnly(mem.expiryDate) else "–",
                            color = if (isExpired) colors.danger else colors.textPrimary,
                            fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                LinearProgressIndicator(
                    progress = { animatedDays },
                    modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(2.5.dp)),
                    color = if (isExpired) colors.danger else if (daysRemaining <= 7) colors.warning else colors.success,
                    trackColor = colors.surfaceMuted
                )
                Text(
                    text = "Renewed ${mem?.renewalCount ?: 0}× · Freeze ${mem?.freezeUsedDays ?: 0}/${mem?.freezeAllowanceDays ?: 30}d",
                    color = colors.textMuted, fontSize = 11.5.sp
                )
            }
        }

        // Benefit tiles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BenefitTile("Gym Access", Icons.Rounded.FitnessCenter, colors.accent, Modifier.weight(1f), theme)
            BenefitTile("Locker", Icons.Rounded.Lock, colors.textSecondary, Modifier.weight(1f), theme)
            BenefitTile("Wi-Fi", Icons.Rounded.Wifi, colors.textSecondary, Modifier.weight(1f), theme)
        }

        if (isExpired) {
            ThemedCtaButton(theme = theme, onClick = {}, modifier = Modifier.fillMaxWidth().height(34.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PAYMENT PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PaymentPanel(snapshot: MemberSnapshot, theme: ThemeId, onCtaClick: () -> Unit) {
    val colors = BADGymTheme.colors
    val pay = snapshot.payment
    val due = pay?.totalOutstanding ?: 0.0
    val isOverdue = due > 0
    val overdueDays = pay?.overdueDays ?: 0
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))
    val formattedDue = "₹" + formatter.format(due.toLong())

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("PAYMENT AUDIT", theme)

        // Amount hero
        InfoCard(theme) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            if (isOverdue) Icons.Rounded.WarningAmber else Icons.Rounded.CheckCircle,
                            null,
                            tint = if (isOverdue) colors.danger else colors.success,
                            modifier = Modifier.size(16.dp)
                        )
                        StatusBadge(
                            label = if (isOverdue) "${overdueDays}d OVERDUE" else "CLEARED",
                            bgColor = if (isOverdue) colors.dangerSoft else colors.successSoft,
                            textColor = if (isOverdue) colors.danger else colors.success
                        )
                    }
                    Text(formattedDue,
                        color = if (isOverdue) colors.danger else colors.textPrimary,
                        fontWeight = FontWeight.Black, fontSize = 22.sp)
                    pay?.dueDate?.let {
                        Text("Due ${formatDateOnly(it)}",
                            color = if (isOverdue) colors.danger else colors.textMuted, fontSize = 12.sp)
                    }
                }
                // Payment status circle
                Box(
                    modifier = Modifier.size(52.dp).semantics {
                        contentDescription = if (isOverdue) "Payment overdue" else "Payment cleared"
                    },
                    contentAlignment = Alignment.Center
                ) {
                    val ringColor = if (isOverdue) colors.danger else colors.success
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                        val inset = stroke.width / 2f
                        val arcSize = Size(size.width - stroke.width, size.height - stroke.width)
                        drawArc(color = colors.surfaceMuted, startAngle = -90f, sweepAngle = 360f,
                            useCenter = false, topLeft = Offset(inset, inset), size = arcSize, style = stroke)
                        drawArc(color = ringColor, startAngle = -90f,
                            sweepAngle = if (isOverdue) 360f else 360f,
                            useCenter = false, topLeft = Offset(inset, inset), size = arcSize, style = stroke)
                    }
                    Icon(
                        if (isOverdue) Icons.Rounded.CreditCard else Icons.Rounded.CheckCircle,
                        null, tint = if (isOverdue) colors.danger else colors.success,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (isOverdue) {
            ThemedCtaButton(theme = theme, onClick = onCtaClick,
                modifier = Modifier.fillMaxWidth().height(34.dp))
        }

        // Last payment + method
        InfoCard(theme) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                StatItem("Last Paid",
                    pay?.lastPaymentAmount?.let { "₹${it.toInt()}" } ?: "–",
                    colors.textPrimary, Icons.Rounded.CreditCard)
                StatItem("Date",
                    pay?.lastPaymentDate?.let { formatDateOnly(it) } ?: "–",
                    colors.textSecondary, Icons.Rounded.CalendarToday)
                StatItem("Lifetime",
                    pay?.lifetimePaid?.let { "₹${it.toInt()}" } ?: "–",
                    colors.accent, Icons.Rounded.WorkspacePremium)
            }
        }

        // Mini transaction history bar chart
        if (!pay?.history.isNullOrEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Recent Transactions", color = colors.textPrimary,
                        fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                    val txList = pay!!.history.take(4)
                    val maxAmount = txList.maxOf { it.amount }.coerceAtLeast(1.0)
                    Row(
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        txList.forEach { tx ->
                            val barH = (tx.amount / maxAmount).toFloat().coerceIn(0.1f, 1f)
                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Box(
                                    modifier = Modifier
                                        .width(18.dp)
                                        .fillMaxHeight(barH)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(colors.accent.copy(alpha = 0.7f))
                                )
                                Text(formatShortDate(tx.occurredAt), color = colors.textMuted, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Breakdown
        if (!pay?.breakdown.isNullOrEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("Breakdown", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                    pay!!.breakdown.forEach { item ->
                        KeyValue(item.label, "₹${item.amount.toInt()}")
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TRAINER PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun TrainerPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val trainer = snapshot.trainer

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("COACH & PT SESSIONS", theme)

        if (trainer == null) {
            EmptyState("No personal trainer assigned", theme)
            return
        }

        val sessionsLeft = (trainer.sessionsTotal - trainer.sessionsUsed).coerceAtLeast(0)
        val sessionsProgress = (trainer.sessionsUsed.toFloat() / trainer.sessionsTotal.coerceAtLeast(1)).coerceIn(0f, 1f)
        val animatedSessions by animateFloatAsState(
            targetValue = sessionsProgress, animationSpec = tween(600), label = "session-ring")

        // Coach identity + sessions ring row
        InfoCard(theme) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Sessions ring
                Box(
                    modifier = Modifier.size(68.dp).semantics {
                        contentDescription = "$sessionsLeft sessions remaining"
                    },
                    contentAlignment = Alignment.Center
                ) {
                    val accent = colors.accent
                    val track = colors.surfaceMuted
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                        val inset = stroke.width / 2f
                        val arcSize = Size(size.width - stroke.width, size.height - stroke.width)
                        val topLeft = Offset(inset, inset)
                        drawArc(color = track, startAngle = -90f, sweepAngle = 360f,
                            useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
                        drawArc(color = accent, startAngle = -90f,
                            sweepAngle = 360f * animatedSessions,
                            useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$sessionsLeft", color = colors.accent, fontSize = 15.sp, fontWeight = FontWeight.Black)
                        Text("left", color = colors.textMuted, fontSize = 10.sp)
                    }
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(trainer.trainerName, color = colors.textPrimary, fontWeight = FontWeight.Black, fontSize = 15.sp)
                    trainer.focus?.let {
                        Text(it, color = colors.textSecondary, fontSize = 12.5.sp, maxLines = 1)
                    }
                    trainer.rating?.let {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Rounded.Star, null, tint = colors.warning, modifier = Modifier.size(13.dp))
                            Text(String.format(Locale.getDefault(), "%.1f", it),
                                color = colors.warning, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(
                        text = "${trainer.sessionsUsed}/${trainer.sessionsTotal} sessions used",
                        color = colors.textMuted, fontSize = 12.sp
                    )
                }
            }
        }

        // Next / last session timeline
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                TimelineItem(
                    icon = Icons.Rounded.CalendarToday,
                    label = "Next Session",
                    value = trainer.nextSessionDate?.let { formatDate(it) } ?: "Not Scheduled",
                    isActive = trainer.nextSessionDate != null,
                    theme = theme
                )
                TimelineItem(
                    icon = Icons.Rounded.History,
                    label = "Last Session",
                    value = trainer.lastSessionDate?.let { formatDate(it) } ?: "–",
                    isActive = false,
                    theme = theme
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// WORKOUT PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun WorkoutPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val workout = snapshot.workout

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("WORKOUT & PROGRESSION", theme)

        // Routine hero + KPI tiles
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = workout?.currentRoutine ?: "No routine recorded",
                    color = colors.textPrimary, fontWeight = FontWeight.Black, fontSize = 15.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    workout?.durationMinutes?.let {
                        KpiCard("${it}min", "Duration", colors.accent, Modifier.weight(1f))
                    }
                    workout?.calories?.let {
                        KpiCard("${it}kcal", "Energy", colors.warning, Modifier.weight(1f))
                    }
                    workout?.lastWorkoutDate?.let {
                        KpiCard(formatShortDate(it), "Last Session", colors.textSecondary, Modifier.weight(1f))
                    }
                }
            }
        }

        // Weekly load mini bar chart — uses attendance pattern as proxy if available, else empty state
        val weekPattern = snapshot.attendance?.weeklyPattern
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Weekly Load", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                if (weekPattern != null) {
                    val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
                    Row(
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weekPattern.take(7).forEachIndexed { i, active ->
                            val barH = if (active > 0) 0.85f else 0.15f
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .fillMaxHeight(barH)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(
                                            if (active > 0) colors.accent
                                            else colors.surfaceMuted
                                        )
                                )
                                Text(dayLabels.getOrElse(i) { "·" }, color = colors.textMuted, fontSize = 11.sp)
                            }
                        }
                    }
                } else {
                    Text("No workout log data available.", color = colors.textMuted, fontSize = 12.sp)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SUPPLEMENTS PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SupplementsPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val item = snapshot.supplements

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("SUPPLEMENTS STACK", theme)

        if (item == null || !item.hasHistory) {
            EmptyState("No supplement purchase history recorded.", theme)
        } else {
            // Product hero
            InfoCard(theme) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(colors.accent.copy(alpha = 0.12f))
                            .border(0.8.dp, colors.accent.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.LocalDrink, null, tint = colors.accent, modifier = Modifier.size(22.dp))
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = item.lastPurchaseName ?: "Supplement",
                            color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                        item.brand?.let { Text(it, color = colors.textSecondary, fontSize = 12.sp) }
                        item.lastPurchasePrice?.let {
                            Text("₹${it.toInt()}", color = colors.accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    StatusBadge("IN STOCK", colors.successSoft, colors.success)
                }
            }

            // Supply progress
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Supply Level", color = colors.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.5.sp)
                        item.lastPurchaseDate?.let {
                            Text("Bought ${formatDateOnly(it)}", color = colors.textMuted, fontSize = 11.sp)
                        }
                    }
                    // Estimated 30-day supply visual (no real data → show empty state visually)
                    val supplyProgress = 0.6f // visual indicator only — no actual remaining data available
                    LinearProgressIndicator(
                        progress = { supplyProgress },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = colors.accent,
                        trackColor = colors.surfaceMuted
                    )
                    Text("Supply status based on purchase date only",
                        color = colors.textMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// NUTRITION PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun NutritionPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val item = snapshot.nutrition

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("NUTRITION & DIET", theme)

        // Plan status hero
        InfoCard(theme) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Rounded.Restaurant, null, tint = colors.accent, modifier = Modifier.size(16.dp))
                        Text(item?.planName ?: "No Diet Plan", color = colors.textPrimary,
                            fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    StatusBadge(
                        label = if (item?.isSubscribed == true) "SUBSCRIBED" else "INACTIVE",
                        bgColor = if (item?.isSubscribed == true) colors.successSoft else colors.surfaceMuted,
                        textColor = if (item?.isSubscribed == true) colors.success else colors.textMuted
                    )
                    item?.monthlyPrice?.let {
                        Text("₹${it.toInt()} / month", color = colors.accent, fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold)
                    }
                    item?.renewalDate?.let {
                        Text("Renews ${formatDateOnly(it)}", color = colors.textMuted, fontSize = 12.sp)
                    }
                }
            }
        }

        // Macro proportion visual (proportional bars — shown only if subscribed)
        if (item?.isSubscribed == true) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Macro Target Distribution", color = colors.textPrimary,
                        fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                    // Domain has no actual macro values — show labeled target proportions only
                    MacroBar("Protein", 0.35f, colors.accent, theme)
                    MacroBar("Carbs", 0.45f, colors.warning, theme)
                    MacroBar("Fats", 0.20f, colors.textSecondary, theme)
                    Text("Target proportions only. No daily macro data in snapshot.",
                        color = colors.textMuted, fontSize = 10.sp)
                }
            }
        } else {
            EmptyState("Subscribe to a diet plan to see macro targets.", theme)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SERVICES PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ServicesPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val services = snapshot.services.orEmpty()

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("GYM SERVICES", theme)

        if (services.isEmpty()) {
            EmptyState("No additional services on file.", theme)
        } else {
            // Services status matrix
            services.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    pair.forEach { service ->
                        InfoCard(theme, modifier = Modifier.weight(1f)) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Rounded.MiscellaneousServices, null,
                                        tint = if (service.isActive) colors.accent else colors.textMuted,
                                        modifier = Modifier.size(16.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (service.isActive) colors.success else colors.danger)
                                    )
                                }
                                Text(service.serviceName, color = colors.textPrimary,
                                    fontWeight = FontWeight.Bold, fontSize = 12.sp,
                                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                                service.expiryDate?.let {
                                    Text(formatDateOnly(it), color = colors.textMuted, fontSize = 11.sp)
                                }
                                service.price?.let {
                                    Text("₹${it.toInt()}/mo", color = colors.accent, fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// HISTORY PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HistoryPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val events = snapshot.recentEvents.sortedByDescending { it.occurredAt }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("ACTIVITY TIMELINE", theme)

        if (events.isEmpty()) {
            EmptyState("No recent activity logged.", theme)
        } else {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    events.take(6).forEachIndexed { i, ev ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Timeline dot + line
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(18.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(colors.accent.copy(alpha = 0.15f))
                                        .border(1.dp, colors.accent.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(eventIcon(ev.eventType), null, tint = colors.accent,
                                        modifier = Modifier.size(9.dp))
                                }
                                if (i < events.take(6).size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(20.dp)
                                            .background(colors.border.copy(alpha = 0.4f))
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(ev.eventType.displayLabel(), color = colors.textPrimary,
                                    fontWeight = FontWeight.SemiBold, fontSize = 12.5.sp)
                                Text(formatDate(ev.occurredAt), color = colors.textMuted, fontSize = 11.5.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// INSIGHT PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun InsightPanel(
    snapshot: MemberSnapshot,
    signals: List<IntelligenceSignal>,
    theme: ThemeId
) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("INTELLIGENCE", theme)

        if (signals.isEmpty()) {
            InfoCard(theme) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Rounded.CheckCircle, null, tint = colors.success, modifier = Modifier.size(20.dp))
                    Text("All nominal. No urgent actions.", color = colors.success,
                        fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        } else {
            signals.take(5).forEach { sig ->
                val isP0 = sig.priority == SignalPriority.P0_CRITICAL
                val isP1 = sig.priority == SignalPriority.P1_ACTION_REQUIRED
                val sigColor = when {
                    isP0 -> colors.danger
                    isP1 -> colors.warning
                    else -> colors.accent
                }
                val sigBg = when {
                    isP0 -> colors.dangerSoft
                    isP1 -> colors.warningSoft
                    else -> colors.surfaceElevated
                }
                val priorityLabel = when {
                    isP0 -> "P0"
                    isP1 -> "P1"
                    else -> "P2"
                }
                val sigIcon = when {
                    isP0 -> Icons.Rounded.WarningAmber
                    isP1 -> Icons.Rounded.Info
                    else -> Icons.Rounded.AutoAwesome
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(sigBg)
                        .border(0.8.dp, sigColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 7.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Priority icon badge
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(sigColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(sigIcon, null, tint = sigColor, modifier = Modifier.size(18.dp))
                    }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            StatusBadge(priorityLabel, sigBg, sigColor)
                            Text(sig.title, color = sigColor, fontWeight = FontWeight.Bold,
                                fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f))
                        }
                        val evidence = sig.subtitle ?: sig.evidence.firstOrNull() ?: sig.value.orEmpty()
                        if (evidence.isNotEmpty()) {
                            Text(evidence, color = colors.textSecondary, fontSize = 12.sp,
                                maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SHARED VISUAL PRIMITIVES
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(text: String, theme: ThemeId) {
    Text(
        text = text,
        color = BADGymTheme.colors.textPrimary,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 13.5.sp,
        letterSpacing = 0.5.sp
    )
}

@Composable
private fun InfoCard(
    theme: ThemeId,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = BADGymTheme.colors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colors.surfaceElevated)
            .border(0.8.dp, colors.border, RoundedCornerShape(10.dp))
            .padding(8.dp),
        content = content
    )
}

@Composable
private fun KeyValue(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 12.5.sp)
        Text(value, color = BADGymTheme.colors.textPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(13.dp))
        Text(value, color = color, fontWeight = FontWeight.Black, fontSize = 13.sp)
        Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 11.sp)
    }
}

/** Small KPI chip: value + label on two lines inside a tinted box */
@Composable
private fun KpiChip(value: String, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.10f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Black)
        Text(label, color = color.copy(alpha = 0.7f), fontSize = 10.sp)
    }
}

/** KPI card used in WorkoutPanel */
@Composable
private fun KpiCard(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    val colors = BADGymTheme.colors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surfaceElevated)
            .border(0.8.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(6.dp)
    ) {
        Text(value, color = color, fontSize = 13.sp, fontWeight = FontWeight.Black, maxLines = 1)
        Text(label, color = colors.textMuted, fontSize = 10.sp, maxLines = 1)
    }
}

/** Benefit tile used in PlanPanel */
@Composable
private fun BenefitTile(
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    theme: ThemeId
) {
    val colors = BADGymTheme.colors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surfaceElevated)
            .border(0.8.dp, colors.border, RoundedCornerShape(8.dp))
            .padding(vertical = 7.dp, horizontal = 4.dp)
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
        Text(label, color = colors.textSecondary, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/** Status badge chip */
@Composable
private fun StatusBadge(label: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(bgColor)
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(label, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Black)
    }
}

/** Timeline item for trainer scheduled/last sessions */
@Composable
private fun TimelineItem(icon: ImageVector, label: String, value: String, isActive: Boolean, theme: ThemeId) {
    val colors = BADGymTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isActive) colors.accent.copy(alpha = 0.15f) else colors.surfaceMuted)
                .border(1.dp, if (isActive) colors.accent.copy(alpha = 0.4f) else colors.border, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = if (isActive) colors.accent else colors.textMuted, modifier = Modifier.size(14.dp))
        }
        Column {
            Text(label, color = colors.textMuted, fontSize = 11.sp)
            Text(value, color = if (isActive) colors.textPrimary else colors.textSecondary,
                fontSize = 12.5.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

/** Macro proportion horizontal bar row */
@Composable
private fun MacroBar(label: String, fraction: Float, color: Color, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val animFraction by animateFloatAsState(targetValue = fraction, animationSpec = tween(600), label = "macro-$label")
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(label, color = colors.textSecondary, fontSize = 11.5.sp, modifier = Modifier.width(50.dp))
        LinearProgressIndicator(
            progress = { animFraction },
            modifier = Modifier.weight(1f).height(5.dp).clip(RoundedCornerShape(2.5.dp)),
            color = color,
            trackColor = colors.surfaceMuted
        )
        Text("${(fraction * 100).toInt()}%", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

/** Empty state block */
@Composable
private fun EmptyState(message: String, theme: ThemeId) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colors.surfaceElevated)
            .border(0.8.dp, colors.border, RoundedCornerShape(10.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.Info, null, tint = colors.textMuted, modifier = Modifier.size(16.dp))
        Text(message, color = colors.textSecondary, fontSize = 13.sp)
    }
}

/** Map EventType to icon */
private fun eventIcon(eventType: EventType): ImageVector = when (eventType) {
    EventType.CHECK_IN -> Icons.AutoMirrored.Rounded.Login
    EventType.CHECK_OUT -> Icons.AutoMirrored.Rounded.Logout
    EventType.TRAINER_SESSION -> Icons.Rounded.FitnessCenter
    EventType.WORKOUT -> Icons.Rounded.Bolt
    EventType.PAYMENT -> Icons.Rounded.CreditCard
    else -> Icons.Rounded.Info
}

private fun formatDate(value: Long): String =
    SimpleDateFormat("dd MMM • h:mm a", Locale.getDefault()).format(Date(value))

private fun formatDateOnly(value: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(value))

private fun formatShortDate(value: Long): String =
    SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(value))
