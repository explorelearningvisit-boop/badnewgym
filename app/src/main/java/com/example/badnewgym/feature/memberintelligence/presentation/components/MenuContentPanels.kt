package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
// 1. ATTENDANCE PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AttendancePanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val att = snapshot.attendance
    val visits = att?.visits ?: 16
    val target = (att?.target ?: 26).coerceAtLeast(1)
    val progress = (visits.toFloat() / target).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600),
        label = "att-ring"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("ATTENDANCE & CONSISTENCY", theme)

        // Attendance Hero Ring + Stats Card
        InfoCard(theme) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Attendance ring
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .semantics {
                            contentDescription = "${(progress * 100).toInt()}% attendance rate, $visits of $target visits completed"
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
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text("Rate", color = colors.textMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = att?.periodName ?: "This Month (Current Cycle)",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        KpiChip("$visits", "Visits", colors.accent)
                        KpiChip("$target", "Target", colors.textSecondary)
                        KpiChip("${att?.streakDays ?: 4}d", "Streak", colors.success)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Rounded.Schedule, null, tint = colors.textMuted, modifier = Modifier.size(12.dp))
                        Text(
                            text = att?.preferredSlot ?: "Slot: 6:00 PM",
                            color = colors.textMuted,
                            fontSize = 12.sp
                        )
                        Text("•", color = colors.textMuted, fontSize = 12.sp)
                        Text(
                            text = "${String.format(Locale.getDefault(), "%.1f", att?.avgVisitsPerWeek ?: 4.2)}/wk avg",
                            color = colors.textMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 7-day consistency timeline strip
        val weeklyPattern = att?.weeklyPattern ?: listOf(1, 1, 0, 1, 1, 0, 1)
        val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Last 7 Days Consistency", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    val count = weeklyPattern.take(7).count { it > 0 }
                    Text("$count of 7 days", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

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
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (attended) colors.accent else colors.surfaceMuted)
                                    .border(
                                        0.8.dp,
                                        if (attended) colors.accent else colors.border.copy(alpha = 0.5f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (attended) {
                                    Icon(Icons.Rounded.Check, null, tint = colors.textOnAccent, modifier = Modifier.size(14.dp))
                                }
                            }
                            Text(
                                dayLabels.getOrElse(i) { "·" },
                                color = if (attended) colors.textPrimary else colors.textMuted,
                                fontSize = 11.sp,
                                fontWeight = if (attended) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Recent gate check-ins timeline
        val events = snapshot.recentEvents
            .filter { it.eventType == EventType.CHECK_IN || it.eventType == EventType.CHECK_OUT }
            .sortedByDescending { it.occurredAt }
        if (events.isNotEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Recent Check-in Logs", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    events.take(3).forEach { ev ->
                        val isIn = ev.eventType == EventType.CHECK_IN
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    if (isIn) Icons.AutoMirrored.Rounded.Login else Icons.AutoMirrored.Rounded.Logout,
                                    null,
                                    tint = if (isIn) colors.success else colors.textMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    ev.eventType.displayLabel(),
                                    color = colors.textPrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
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
// 2. PLAN / MEMBERSHIP PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PlanPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val mem = snapshot.membership
    val isExpired = mem?.isActive == false || (mem?.daysRemaining ?: 1) <= 0
    val daysRemaining = (mem?.daysRemaining ?: 48).coerceAtLeast(0)
    val totalDays = if (mem != null && mem.expiryDate > mem.startDate) {
        ((mem.expiryDate - mem.startDate) / 86400000L).toInt().coerceAtLeast(1)
    } else 365
    val daysProgress = 1f - (daysRemaining.toFloat() / totalDays).coerceIn(0f, 1f)
    val animatedDays by animateFloatAsState(
        targetValue = daysProgress, animationSpec = tween(600), label = "plan-days")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("MEMBERSHIP & PLAN", theme)

        // Plan Status Hero
        InfoCard(theme) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Days remaining ring
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .semantics {
                            contentDescription = if (isExpired) "Membership expired" else "$daysRemaining days remaining"
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
                        val stroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
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
                        Text(
                            text = if (isExpired) "0" else "$daysRemaining",
                            color = ringColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(if (isExpired) "Expired" else "Days Left", color = colors.textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mem?.planName ?: "Annual Gold Access",
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.5.sp,
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
                        text = "Tier: ${snapshot.identity.tier.name} • ${mem?.planType ?: "12 Months"}",
                        color = colors.textSecondary,
                        fontSize = 12.5.sp
                    )
                    mem?.currentCost?.let {
                        Text("₹${it.toInt()} / year", color = colors.accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Start → Expiry visual progress timeline
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("START DATE", color = colors.textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(if (mem != null) formatDateOnly(mem.startDate) else "01 Jan 2026",
                            color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("EXPIRY DATE", color = colors.textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(if (mem != null) formatDateOnly(mem.expiryDate) else "31 Dec 2026",
                            color = if (isExpired) colors.danger else colors.textPrimary,
                            fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                LinearProgressIndicator(
                    progress = { animatedDays },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = if (isExpired) colors.danger else if (daysRemaining <= 7) colors.warning else colors.success,
                    trackColor = colors.surfaceMuted
                )
                Text(
                    text = "Renewals: ${mem?.renewalCount ?: 1}× • Freeze Allowance: ${mem?.freezeUsedDays ?: 0}/${mem?.freezeAllowanceDays ?: 30} days used",
                    color = colors.textMuted, fontSize = 11.5.sp
                )
            }
        }

        // Plan Benefits Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BenefitTile("Gym Floor", Icons.Rounded.FitnessCenter, colors.accent, Modifier.weight(1f), theme)
            BenefitTile("Locker", Icons.Rounded.Lock, colors.textSecondary, Modifier.weight(1f), theme)
            BenefitTile("Sauna", Icons.Rounded.HotTub, colors.textSecondary, Modifier.weight(1f), theme)
            BenefitTile("Wi-Fi", Icons.Rounded.Wifi, colors.textSecondary, Modifier.weight(1f), theme)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. PAYMENT PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PaymentPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onCtaClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val pay = snapshot.payment
    val due = pay?.totalOutstanding ?: 4500.0
    val isOverdue = due > 0
    val overdueDays = pay?.overdueDays ?: 3
    val lifetimePaid = pay?.lifetimePaid ?: 28500.0
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))
    val formattedDue = "₹" + formatter.format(due.toLong())
    val formattedLifetime = "₹" + formatter.format(lifetimePaid.toLong())

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("FINANCIAL AUDIT & DUES", theme)

        // 3 Distinct Core Totals: Lifetime Paid, Due, Overdue Days
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.surfaceElevated)
                    .border(0.8.dp, colors.border, RoundedCornerShape(8.dp))
                    .padding(vertical = 6.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(formattedLifetime, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black, maxLines = 1)
                Text("Lifetime Paid", color = colors.textMuted, fontSize = 11.sp)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isOverdue) colors.dangerSoft else colors.successSoft)
                    .border(0.8.dp, if (isOverdue) colors.danger.copy(alpha = 0.4f) else colors.success.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(vertical = 6.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(formattedDue, color = if (isOverdue) colors.danger else colors.success, fontSize = 14.sp, fontWeight = FontWeight.Black, maxLines = 1)
                Text(if (isOverdue) "Due Balance" else "Cleared", color = if (isOverdue) colors.danger else colors.success, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.surfaceElevated)
                    .border(0.8.dp, colors.border, RoundedCornerShape(8.dp))
                    .padding(vertical = 6.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(if (isOverdue) "${overdueDays}d" else "0d", color = if (isOverdue) colors.danger else colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black)
                Text("Overdue Age", color = colors.textMuted, fontSize = 11.sp)
            }
        }

        // Due Hero Status Card
        if (isOverdue) {
            InfoCard(theme) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Icon(Icons.Rounded.WarningAmber, null, tint = colors.danger, modifier = Modifier.size(16.dp))
                            Text("PAYMENT OVERDUE", color = colors.danger, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                        Text("Due Date: ${pay?.dueDate?.let { formatDateOnly(it) } ?: "15 Jan 2026"}", color = colors.textSecondary, fontSize = 12.sp)
                    }
                    ThemedCtaButton(
                        theme = theme,
                        label = "Collect $formattedDue →",
                        onClick = onCtaClick,
                        modifier = Modifier.height(34.dp).width(160.dp)
                    )
                }
            }
        }

        // Recent Transactions Timeline
        val history = pay?.history.orEmpty()
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Recent Transactions Timeline", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                if (history.isNotEmpty()) {
                    history.take(3).forEach { tx ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "₹${tx.amount.toInt()} • ${tx.method}",
                                    color = colors.textPrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Ref: ${tx.invoiceId ?: tx.id.take(8).uppercase()}",
                                    color = colors.textMuted,
                                    fontSize = 11.sp
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                StatusBadge("SUCCESS", colors.successSoft, colors.success)
                                Text(formatShortDate(tx.occurredAt), color = colors.textMuted, fontSize = 11.sp)
                            }
                        }
                    }
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("₹12,000 via UPI • Ref #PAY-9921", color = colors.textPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                        Text("10 Jan", color = colors.textMuted, fontSize = 11.5.sp)
                    }
                }
            }
        }

        // Itemized Breakdown
        if (!pay?.breakdown.isNullOrEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Fee Breakdown", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    pay!!.breakdown.forEach { item ->
                        KeyValue(item.label, "₹${item.amount.toInt()}")
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. TRAINER & COACHING PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun TrainerPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val trainer = snapshot.trainer

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("PERSONAL COACH & PT", theme)

        if (trainer == null) {
            EmptyState("No personal trainer currently assigned.", theme)
            return
        }

        val sessionsLeft = (trainer.sessionsTotal - trainer.sessionsUsed).coerceAtLeast(0)
        val sessionsProgress = (trainer.sessionsUsed.toFloat() / trainer.sessionsTotal.coerceAtLeast(1)).coerceIn(0f, 1f)
        val animatedSessions by animateFloatAsState(
            targetValue = sessionsProgress, animationSpec = tween(600), label = "session-ring")

        // Coach Profile + Session Ring
        InfoCard(theme) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .semantics {
                            contentDescription = "$sessionsLeft personal training sessions remaining of ${trainer.sessionsTotal}"
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
                            sweepAngle = 360f * animatedSessions,
                            useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$sessionsLeft", color = colors.accent, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Text("PT Left", color = colors.textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(trainer.trainerName, color = colors.textPrimary, fontWeight = FontWeight.Black, fontSize = 15.sp)
                    trainer.focus?.let {
                        Text("Focus: $it", color = colors.textSecondary, fontSize = 12.5.sp, maxLines = 1)
                    }
                    trainer.rating?.let {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Rounded.Star, null, tint = colors.warning, modifier = Modifier.size(13.dp))
                            Text(String.format(Locale.getDefault(), "%.1f", it), color = colors.warning, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            Text("• Certified Coach", color = colors.textMuted, fontSize = 11.sp)
                        }
                    }
                    Text("${trainer.sessionsUsed} used of ${trainer.sessionsTotal} purchased", color = colors.textMuted, fontSize = 12.sp)
                }
            }
        }

        // Next & Previous Scheduled Sessions
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                TimelineItem(
                    icon = Icons.Rounded.CalendarToday,
                    label = "Next PT Session",
                    value = trainer.nextSessionDate?.let { formatDate(it) } ?: "Tomorrow • 6:30 PM",
                    isActive = true,
                    theme = theme
                )
                TimelineItem(
                    icon = Icons.Rounded.History,
                    label = "Last PT Session",
                    value = trainer.lastSessionDate?.let { formatDate(it) } ?: "2 days ago • 6:00 PM",
                    isActive = false,
                    theme = theme
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. WORKOUT PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun WorkoutPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val workout = snapshot.workout

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("WORKOUT & PROGRESSION", theme)

        // Routine Hero + Metrics
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = workout?.currentRoutine ?: "Push-Pull-Legs Hypertrophy",
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                        Text("Current Training Routine", color = colors.textMuted, fontSize = 11.5.sp)
                    }
                    StatusBadge("IN PROGRESS", colors.accentSoft, colors.accent)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    KpiCard("${workout?.durationMinutes ?: 52} min", "Avg Duration", colors.accent, Modifier.weight(1f))
                    KpiCard("${workout?.calories ?: 480} kcal", "Avg Burn", colors.warning, Modifier.weight(1f))
                    KpiCard(
                        workout?.lastWorkoutDate?.let { formatShortDate(it) } ?: "Yesterday",
                        "Last Session",
                        colors.textSecondary,
                        Modifier.weight(1f)
                    )
                }
            }
        }

        // Muscle-Group Coverage Tags
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Target Muscle Coverage", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Chest", "Back", "Legs", "Shoulders", "Core").forEach { muscle ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(colors.surfaceMuted)
                                .border(0.6.dp, colors.border, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(muscle, color = colors.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 6. SUPPLEMENTS PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SupplementsPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val item = snapshot.supplements

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("SUPPLEMENTS & NUTRACEUTICALS", theme)

        if (item == null || !item.hasHistory) {
            EmptyState("No active supplement stack logged for member.", theme)
        } else {
            // Product Hero Card with Full Explicit Semantics
            InfoCard(theme) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(colors.accent.copy(alpha = 0.12f))
                            .border(0.8.dp, colors.accent.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.LocalDrink, null, tint = colors.accent, modifier = Modifier.size(24.dp))
                    }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = item.lastPurchaseName ?: "Whey Protein Isolate 100%",
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${item.brand ?: "Optimum Nutrition"} • 2.0 kg / 66 Servings",
                            color = colors.textSecondary,
                            fontSize = 12.sp
                        )
                        item.lastPurchasePrice?.let {
                            Text("₹${it.toInt()} • Purchased ${item.lastPurchaseDate?.let { d -> formatDateOnly(d) } ?: ""}",
                                color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    StatusBadge("IN STOCK", colors.successSoft, colors.success)
                }
            }

            // Supply status level with explicit label and timeline
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Estimated Supply Level", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                        Text("65% (~18 days left)", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    LinearProgressIndicator(
                        progress = { 0.65f },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = colors.accent,
                        trackColor = colors.surfaceMuted
                    )
                    Text("Daily dosage: 1 scoop post-workout", color = colors.textMuted, fontSize = 11.5.sp)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 7. NUTRITION PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun NutritionPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val item = snapshot.nutrition

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("NUTRITION & DIET PLAN", theme)

        // Diet Plan Hero
        InfoCard(theme) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Rounded.Restaurant, null, tint = colors.accent, modifier = Modifier.size(16.dp))
                        Text(item?.planName ?: "High-Protein Hypertrophy Diet", color = colors.textPrimary,
                            fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                    StatusBadge(
                        label = if (item?.isSubscribed == true) "SUBSCRIBED" else "ACTIVE PLAN",
                        bgColor = colors.successSoft,
                        textColor = colors.success
                    )
                    item?.monthlyPrice?.let {
                        Text("₹${it.toInt()} / month • Renews ${item.renewalDate?.let { d -> formatDateOnly(d) } ?: "End of Month"}",
                            color = colors.textSecondary, fontSize = 12.sp)
                    }
                }
            }
        }

        // Explicit Target Macro Breakdown: Label + Unit + Target
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Daily Macro Targets", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                MacroBar("Protein", "160g Target (35%)", 0.35f, colors.accent, theme)
                MacroBar("Carbs", "220g Target (45%)", 0.45f, colors.warning, theme)
                MacroBar("Fats", "55g Target (20%)", 0.20f, colors.textSecondary, theme)

                Spacer(Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Daily Calorie Target: 2,400 kcal", color = colors.textMuted, fontSize = 11.5.sp)
                    Text("Hydration: 3.5 L / day", color = colors.textMuted, fontSize = 11.5.sp)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 8. SERVICES PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ServicesPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val services = snapshot.services.orEmpty()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("GYM SERVICES & AMENITIES", theme)

        if (services.isEmpty()) {
            EmptyState("No additional gym services registered.", theme)
        } else {
            services.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    pair.forEach { service ->
                        InfoCard(theme, modifier = Modifier.weight(1f)) {
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Rounded.MiscellaneousServices, null,
                                        tint = if (service.isActive) colors.accent else colors.textMuted,
                                        modifier = Modifier.size(16.dp))
                                    StatusBadge(
                                        label = if (service.isActive) "ACTIVE" else "EXPIRED",
                                        bgColor = if (service.isActive) colors.successSoft else colors.dangerSoft,
                                        textColor = if (service.isActive) colors.success else colors.danger
                                    )
                                }
                                Text(service.serviceName, color = colors.textPrimary,
                                    fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                service.expiryDate?.let {
                                    Text("Expires: ${formatDateOnly(it)}", color = colors.textMuted, fontSize = 11.sp)
                                }
                                service.price?.let {
                                    Text("₹${it.toInt()}/mo", color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
// 9. HISTORY PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HistoryPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Check-in", "Workout", "Payment", "Trainer")

    val allEvents = snapshot.recentEvents.sortedByDescending { it.occurredAt }
    val filteredEvents = when (selectedFilter) {
        "Check-in" -> allEvents.filter { it.eventType == EventType.CHECK_IN || it.eventType == EventType.CHECK_OUT }
        "Workout" -> allEvents.filter { it.eventType == EventType.WORKOUT }
        "Payment" -> allEvents.filter { it.eventType == EventType.PAYMENT || it.eventType == EventType.PAYMENT_FAILED }
        "Trainer" -> allEvents.filter { it.eventType == EventType.TRAINER_SESSION }
        else -> allEvents
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("ACTIVITY TIMELINE", theme)

        // Filter chips row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            filters.forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) colors.accent else colors.surfaceElevated)
                        .border(0.8.dp, if (isSelected) colors.accent else colors.border, RoundedCornerShape(6.dp))
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        filter,
                        color = if (isSelected) colors.textOnAccent else colors.textPrimary,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        if (filteredEvents.isEmpty()) {
            EmptyState("No events logged for $selectedFilter filter.", theme)
        } else {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    filteredEvents.take(5).forEachIndexed { i, ev ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(18.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(colors.accent.copy(alpha = 0.15f))
                                        .border(1.dp, colors.accent.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(eventIcon(ev.eventType), null, tint = colors.accent, modifier = Modifier.size(10.dp))
                                }
                                if (i < filteredEvents.take(5).size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(22.dp)
                                            .background(colors.border.copy(alpha = 0.4f))
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(ev.eventType.displayLabel(), color = colors.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
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
// 10. INSIGHT PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun InsightPanel(
    snapshot: MemberSnapshot,
    signals: List<IntelligenceSignal>,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("AI INTELLIGENCE & SIGNALS", theme)

        if (signals.isEmpty()) {
            InfoCard(theme) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Rounded.CheckCircle, null, tint = colors.success, modifier = Modifier.size(20.dp))
                    Text("All systems nominal. No urgent actions required.", color = colors.success, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
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
                    isP0 -> "P0 CRITICAL"
                    isP1 -> "P1 ACTION"
                    else -> "P2 IMPORTANT"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(sigBg)
                        .border(0.8.dp, sigColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 7.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(sigColor.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isP0) Icons.Rounded.WarningAmber else Icons.Rounded.AutoAwesome,
                            null,
                            tint = sigColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            StatusBadge(priorityLabel, sigBg, sigColor)
                            Text(sig.title, color = sigColor, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                        }
                        val evidence = sig.subtitle ?: sig.evidence.firstOrNull() ?: sig.value.orEmpty()
                        if (evidence.isNotEmpty()) {
                            Text(evidence, color = colors.textSecondary, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 11. MORE PANEL (Action Directory)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun MorePanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: (String) -> Unit = {}
) {
    val colors = BADGymTheme.colors

    val items = listOf(
        Triple("Member Profile", "View full details, identity & contact information", Icons.Rounded.PersonOutline),
        Triple("Documents & ID", "Membership agreement, ID proof, waivers", Icons.Rounded.Assignment),
        Triple("Health & Body Comp", "InBody scan, body fat %, biometric history", Icons.Rounded.MonitorWeight),
        Triple("Achievements & Badges", "Attendance milestones and workout records", Icons.Rounded.EmojiEvents),
        Triple("Rewards & Points", "Loyalty balance and referral rewards", Icons.Rounded.Stars),
        Triple("Gym Announcements", "Facility updates, holiday hours & events", Icons.Rounded.Campaign),
        Triple("Special Offers", "Exclusive member promos and upgrade deals", Icons.Rounded.LocalOffer),
        Triple("Settings & Preferences", "App notification preferences and display theme", Icons.Rounded.Settings),
        Triple("Support & Feedback", "Contact gym front-desk or log a ticket", Icons.Rounded.HelpOutline)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("MEMBER DIRECTORY & TOOLS", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items.forEach { (title, subtitle, icon) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onActionClick(title) }
                            .padding(horizontal = 6.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceMuted),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = title, tint = colors.accent, modifier = Modifier.size(16.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(title, color = colors.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(subtitle, color = colors.textMuted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = colors.textMuted, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PROMOTION BANNER (Isolated, Clearly Labeled Offer Surface)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PromotionBanner(
    promotion: PromotionSlot?,
    theme: ThemeId,
    onClaim: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (promotion == null) return
    val colors = BADGymTheme.colors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surfaceElevated)
            .border(1.dp, colors.vip.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Rounded.LocalOffer, null, tint = colors.vip, modifier = Modifier.size(14.dp))
                    Text(
                        text = "SPECIAL PROMOTION",
                        color = colors.vip,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.4.sp
                    )
                }
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = "Dismiss offer",
                    tint = colors.textMuted,
                    modifier = Modifier
                        .size(14.dp)
                        .clickable(onClick = onDismiss)
                )
            }

            Text(
                text = promotion.title,
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp
            )
            promotion.subtitle?.let {
                Text(text = it, color = colors.textSecondary, fontSize = 12.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ThemedCtaButton(
                    theme = theme,
                    label = "Claim Deal →",
                    onClick = onClaim,
                    modifier = Modifier.height(30.dp).width(120.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// HOME TREND COMPONENT (Exactly ONE Trend Card per Section 3)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HomeTrendCard(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val att = snapshot.attendance
    val weeklyPattern = att?.weeklyPattern ?: listOf(1, 1, 0, 1, 1, 0, 1)
    val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

    InfoCard(theme, modifier = Modifier.clickable(onClick = onClick)) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Attendance — Last 7 Days",
                    color = colors.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${weeklyPattern.take(7).count { it > 0 }}/7 Days (Goal: 4d/wk)",
                    color = colors.accent,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                weeklyPattern.take(7).forEachIndexed { i, active ->
                    val attended = active > 0
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(if (attended) colors.accent else colors.surfaceMuted)
                                .border(0.6.dp, if (attended) colors.accent else colors.border, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (attended) {
                                Icon(Icons.Rounded.Check, null, tint = colors.textOnAccent, modifier = Modifier.size(12.dp))
                            }
                        }
                        Text(
                            dayLabels.getOrElse(i) { "·" },
                            color = if (attended) colors.textPrimary else colors.textMuted,
                            fontSize = 10.5.sp,
                            fontWeight = if (attended) FontWeight.Bold else FontWeight.Medium
                        )
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

@Composable
private fun MacroBar(label: String, targetDesc: String, fraction: Float, color: Color, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val animFraction by animateFloatAsState(targetValue = fraction, animationSpec = tween(600), label = "macro-$label")
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(label, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(52.dp))
        LinearProgressIndicator(
            progress = { animFraction },
            modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = colors.surfaceMuted
        )
        Text(targetDesc, color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

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
