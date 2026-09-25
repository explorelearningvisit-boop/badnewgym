package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.HorizontalDivider
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
// 1. ATTENDANCE PANEL (STAGE 7.4 TEMPORAL DRILL-DOWN)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AttendancePanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    temporalRange: TemporalRange = TemporalRange.forCurrentMonth(),
    onRangeChange: (TemporalRange) -> Unit = {},
    onEventClick: (TemporalEventRecord) -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    var selectedSubMenu by remember { mutableStateOf("Pattern") }
    val subMenus = listOf("Pattern", "Calendar", "Timing", "Live", "Forecast")

    val att = snapshot.attendance
    val visits = att?.visits ?: 16
    val target = (att?.target ?: 26).coerceAtLeast(1)
    val progress = (visits.toFloat() / target).coerceIn(0f, 1f)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("ATTENDANCE & ACCESS INTELLIGENCE", theme)

        // Sub-Menu Segmented Control
        SubMenuTabBar(
            items = subMenus,
            selectedItem = selectedSubMenu,
            onSelect = { selectedSubMenu = it }
        )

        // Temporal Navigator
        TemporalNavigator(
            currentRange = temporalRange,
            availableGranularities = listOf(
                TemporalGranularity.DAY,
                TemporalGranularity.WEEK,
                TemporalGranularity.MONTH,
                TemporalGranularity.YEAR
            ),
            isLive = selectedSubMenu == "Live",
            eventCount = visits,
            onRangeChange = onRangeChange
        )

        when (selectedSubMenu) {
            "Pattern" -> {
                // Monthly Ring & Overview
                InfoCard(theme) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(74.dp)
                                .semantics {
                                    contentDescription = "${(progress * 100).toInt()}% attendance rate"
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            val accent = colors.accent
                            val track = colors.surfaceMuted
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val stroke = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                                val inset = stroke.width / 2f
                                val arcSize = Size(size.width - stroke.width, size.height - stroke.width)
                                drawArc(track, -90f, 360f, false, Offset(inset, inset), arcSize, style = stroke)
                                drawArc(accent, -90f, 360f * progress, false, Offset(inset, inset), arcSize, style = stroke)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    color = colors.accent,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text("Rate", color = colors.textMuted, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${temporalRange.formatPeriodLabel()} Summary",
                                color = colors.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
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
                                Text("Peak: 07:30 - 08:45 AM • Avg: 68m", color = colors.textMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }

                // 7-Day Consistency Strip
                val weeklyPattern = att?.weeklyPattern ?: listOf(1, 1, 0, 1, 1, 0, 1)
                val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                InfoCard(theme) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("7-Day Check-in Consistency", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                            Text("${weeklyPattern.count { it > 0 }} of 7 days", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 11.sp)
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
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(if (attended) colors.accent else colors.surfaceMuted)
                                            .border(0.8.dp, if (attended) colors.accent else colors.border.copy(alpha = 0.5f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (attended) {
                                            Icon(Icons.Rounded.Check, null, tint = colors.textOnAccent, modifier = Modifier.size(13.dp))
                                        }
                                    }
                                    Text(
                                        dayLabels.getOrElse(i) { "·" },
                                        color = if (attended) colors.textPrimary else colors.textMuted,
                                        fontSize = 10.sp,
                                        fontWeight = if (attended) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            "Calendar", "Live", "Timing", "Forecast" -> {
                // Access & Gate Audit Event List with Second-level Precision
                InfoCard(theme) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Access Verifications (${selectedSubMenu})", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                            Text("Tap for Audit Detail", color = colors.accent, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }

                        val sampleEvents = listOf(
                            TemporalEventRecord(
                                eventId = "att_01",
                                memberId = snapshot.id,
                                eventType = EventType.CHECK_IN,
                                occurredAt = System.currentTimeMillis() - 7200000L,
                                source = EventSource.GATE,
                                sourceMetadata = SourceVerificationMetadata("FACE", "Turnstile Gate 1", "CAM_ENTRANCE_01", "SUCCESS", 95L, 0.992f),
                                title = "Check-in • FACE Verification",
                                subtitle = "Gate 1 • Turnstile 1 • Latency: 95ms"
                            ),
                            TemporalEventRecord(
                                eventId = "att_02",
                                memberId = snapshot.id,
                                eventType = EventType.CHECK_OUT,
                                occurredAt = System.currentTimeMillis() - 3600000L,
                                source = EventSource.GATE,
                                sourceMetadata = SourceVerificationMetadata("FACE", "Turnstile Gate 1 Exit", "CAM_EXIT_01", "SUCCESS", 102L, 0.988f),
                                title = "Check-out • FACE Verification",
                                subtitle = "Session Duration: 60 mins • Gate 1 Exit"
                            ),
                            TemporalEventRecord(
                                eventId = "att_03",
                                memberId = snapshot.id,
                                eventType = EventType.CHECK_IN,
                                occurredAt = System.currentTimeMillis() - 93600000L,
                                source = EventSource.GATE,
                                sourceMetadata = SourceVerificationMetadata("QR", "Front Desk Scanner", "QR_RECEPTION", "SUCCESS", 45L, 1.0f),
                                title = "Check-in • QR Code Scan",
                                subtitle = "Reception Desk • Latency: 45ms"
                            )
                        )

                        sampleEvents.forEach { ev ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(colors.surfaceMuted.copy(alpha = 0.5f))
                                    .border(1.dp, colors.border.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .clickable { onEventClick(ev) }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(if (ev.eventType == EventType.CHECK_IN) colors.success.copy(alpha = 0.15f) else colors.textMuted.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            if (ev.eventType == EventType.CHECK_IN) Icons.AutoMirrored.Rounded.Login else Icons.AutoMirrored.Rounded.Logout,
                                            null,
                                            tint = if (ev.eventType == EventType.CHECK_IN) colors.success else colors.textMuted,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                    Column {
                                        Text(ev.title, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(ev.subtitle ?: "", color = colors.textMuted, fontSize = 10.sp)
                                    }
                                }
                                Text(ev.formatTimeOnly(withSeconds = true), color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. PAYMENT PANEL (STAGE 7.4 TEMPORAL AUDIT & RECEIPTS)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PaymentPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    temporalRange: TemporalRange = TemporalRange.forCurrentMonth(),
    onRangeChange: (TemporalRange) -> Unit = {},
    onEventClick: (TemporalEventRecord) -> Unit = {},
    onCtaClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    var selectedSubMenu by remember { mutableStateOf("Timeline") }
    val subMenus = listOf("Audit", "Timeline", "Recurring", "Dues", "Forecast")

    val pay = snapshot.payment
    val lifetime = pay?.lifetimePaid ?: 45000.0
    val totalDue = pay?.totalOutstanding ?: 0.0
    val isOverdue = totalDue > 0
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("FINANCIAL & PAYMENT INTELLIGENCE", theme)

        // Sub-Menu Segmented Control
        SubMenuTabBar(
            items = subMenus,
            selectedItem = selectedSubMenu,
            onSelect = { selectedSubMenu = it }
        )

        // Temporal Navigator
        TemporalNavigator(
            currentRange = temporalRange,
            availableGranularities = listOf(
                TemporalGranularity.MONTH,
                TemporalGranularity.QUARTER,
                TemporalGranularity.HALF_YEAR,
                TemporalGranularity.YEAR
            ),
            eventCount = 3,
            onRangeChange = onRangeChange
        )

        // Financial Hero Card
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isOverdue) "OUTSTANDING BALANCE" else "LIFETIME REVENUE",
                            color = if (isOverdue) colors.danger else colors.textMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = if (isOverdue) formatter.format(totalDue) else formatter.format(lifetime),
                            color = if (isOverdue) colors.danger else colors.accent,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    if (isOverdue) {
                        ActionChip("COLLECT ₹${totalDue.toInt()}", colors.danger) { onCtaClick() }
                    } else {
                        BadgeChip("PAID UP", colors.success)
                    }
                }

                HorizontalDivider(color = colors.divider)

                // 3 Financial Columns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Paid Lifetime", color = colors.textMuted, fontSize = 10.sp)
                        Text(formatter.format(lifetime), color = colors.textPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Cycle Status", color = colors.textMuted, fontSize = 10.sp)
                        Text(pay?.lifecycle?.name ?: "PAID", color = if (isOverdue) colors.danger else colors.success, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Due Date", color = colors.textMuted, fontSize = 10.sp)
                        Text(pay?.dueDate?.let { formatDate(it) } ?: "30 Sep 2026", color = colors.textPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Transactions Timeline with Tap-to-Receipt
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Transaction Records (${temporalRange.formatPeriodLabel()})", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                    Text("Tap for Receipt", color = colors.accent, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }

                val sampleTransactions = listOf(
                    TemporalEventRecord(
                        eventId = "pay_01",
                        memberId = snapshot.id,
                        eventType = EventType.PAYMENT,
                        occurredAt = System.currentTimeMillis() - (12L * 86400000L),
                        source = EventSource.PAYMENT,
                        status = "SUCCESS",
                        title = "Quarterly Elite Renewal",
                        subtitle = "Paid via Razorpay UPI AutoPay",
                        amount = 15000.0,
                        invoiceNumber = "INV-2026-0902",
                        referenceId = "TXN_RZP_98231908",
                        details = mapOf(
                            "Plan" to "Quarterly Elite",
                            "GST No." to "27AAAAA0000A1Z5",
                            "Tax Paid" to "₹2,288.14",
                            "Method" to "UPI AutoPay"
                        )
                    ),
                    TemporalEventRecord(
                        eventId = "pay_02",
                        memberId = snapshot.id,
                        eventType = if (isOverdue) EventType.PAYMENT_FAILED else EventType.PAYMENT,
                        occurredAt = System.currentTimeMillis() - (45L * 86400000L),
                        source = EventSource.PAYMENT,
                        status = if (isOverdue) "FAILED" else "SUCCESS",
                        title = if (isOverdue) "Monthly Locker Fee (Overdue)" else "PT 10-Session Package",
                        subtitle = if (isOverdue) "Bank Auto-Debit Failed" else "Paid via HDFC NetBanking",
                        amount = if (isOverdue) 3500.0 else 8000.0,
                        invoiceNumber = "INV-2026-0814",
                        referenceId = "TXN_HDFC_4719280",
                        details = mapOf(
                            "Item" to if (isOverdue) "Locker Rental" else "10 PT Sessions",
                            "Bank Ref" to "HDFC889921",
                            "Status" to if (isOverdue) "Declined (Insufficient Funds)" else "Cleared"
                        )
                    )
                )

                sampleTransactions.forEach { txn ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.surfaceMuted.copy(alpha = 0.5f))
                            .border(1.dp, colors.border.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable { onEventClick(txn) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (txn.status == "SUCCESS") colors.success.copy(alpha = 0.15f) else colors.danger.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.ReceiptLong,
                                    null,
                                    tint = if (txn.status == "SUCCESS") colors.success else colors.danger,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Column {
                                Text(txn.title, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(txn.subtitle ?: "", color = colors.textMuted, fontSize = 10.sp)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                formatter.format(txn.amount ?: 0.0),
                                color = if (txn.status == "SUCCESS") colors.textPrimary else colors.danger,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(txn.formatTimestamp(withSeconds = false), color = colors.textMuted, fontSize = 9.5.sp)
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. WORKOUT PANEL (STAGE 7.4 TEMPORAL SESSIONS & DRILL-DOWN)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun WorkoutPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    temporalRange: TemporalRange = TemporalRange.forCurrentMonth(),
    onRangeChange: (TemporalRange) -> Unit = {},
    onEventClick: (TemporalEventRecord) -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    var selectedSubMenu by remember { mutableStateOf("Today") }
    val subMenus = listOf("Today", "Program", "Sessions", "Progress", "Muscles", "PRs")

    val wo = snapshot.workout
    val completed = 4
    val target = 5

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("WORKOUT & TRAINING INTELLIGENCE", theme)

        SubMenuTabBar(
            items = subMenus,
            selectedItem = selectedSubMenu,
            onSelect = { selectedSubMenu = it }
        )

        TemporalNavigator(
            currentRange = temporalRange,
            availableGranularities = listOf(
                TemporalGranularity.DAY,
                TemporalGranularity.WEEK,
                TemporalGranularity.MONTH,
                TemporalGranularity.YEAR
            ),
            eventCount = 14,
            onRangeChange = onRangeChange
        )

        // Workout Program & Split
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(wo?.currentRoutine ?: "Hypertrophy Push-Pull-Legs", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                        Text("Phase: Strength & Volume Accumulation", color = colors.textMuted, fontSize = 11.sp)
                    }
                    BadgeChip("${completed}/$target Wk", colors.accent)
                }

                LinearProgressIndicator(
                    progress = { (completed.toFloat() / target).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = colors.accent,
                    trackColor = colors.surfaceMuted,
                )
            }
        }

        // Sessions Drill-down List
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Training Sessions (${temporalRange.formatPeriodLabel()})", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                    Text("Tap for Sets & Load", color = colors.accent, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }

                val sessions = listOf(
                    TemporalEventRecord(
                        eventId = "wo_01",
                        memberId = snapshot.id,
                        eventType = EventType.WORKOUT,
                        occurredAt = System.currentTimeMillis() - 86400000L,
                        title = "Push Day A — Chest & Triceps",
                        subtitle = "6 exercises • 18 sets • 62 mins",
                        durationMinutes = 62,
                        details = mapOf(
                            "Flat DB Bench Press" to "4 sets x 10 reps @ 32kg",
                            "Incline Barbell Press" to "4 sets x 8 reps @ 75kg",
                            "Lateral Raises" to "4 sets x 15 reps @ 12kg",
                            "Tricep Rope Pushdown" to "3 sets x 12 reps @ 30kg",
                            "Overhead DB Extension" to "3 sets x 12 reps @ 24kg"
                        )
                    ),
                    TemporalEventRecord(
                        eventId = "wo_02",
                        memberId = snapshot.id,
                        eventType = EventType.WORKOUT,
                        occurredAt = System.currentTimeMillis() - (3L * 86400000L),
                        title = "Pull Day B — Back & Biceps",
                        subtitle = "5 exercises • 16 sets • 55 mins",
                        durationMinutes = 55,
                        details = mapOf(
                            "Conventional Deadlift" to "4 sets x 5 reps @ 150kg",
                            "Weighted Pull-ups" to "4 sets x 6 reps @ +15kg",
                            "Chest Supported Rows" to "4 sets x 8 reps @ 80kg",
                            "Incline DB Curls" to "4 sets x 10 reps @ 16kg"
                        )
                    )
                )

                sessions.forEach { s ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.surfaceMuted.copy(alpha = 0.5f))
                            .border(1.dp, colors.border.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable { onEventClick(s) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(colors.accent.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Rounded.FitnessCenter, null, tint = colors.accent, modifier = Modifier.size(15.dp))
                            }
                            Column {
                                Text(s.title, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(s.subtitle ?: "", color = colors.textMuted, fontSize = 10.sp)
                            }
                        }
                        Text(formatDate(s.occurredAt), color = colors.textSecondary, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. HISTORY PANEL (STAGE 7.4 COMPLETE AUDIT STREAM & FILTERS)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HistoryPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    temporalRange: TemporalRange = TemporalRange.forCurrentMonth(),
    onRangeChange: (TemporalRange) -> Unit = {},
    onEventClick: (TemporalEventRecord) -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Attendance", "Payment", "Workout", "Trainer", "Issues")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("MEMBER AUDIT & EVENT TIMELINE", theme)

        // Filter Bar
        SubMenuTabBar(
            items = filters,
            selectedItem = selectedFilter,
            onSelect = { selectedFilter = it }
        )

        // Temporal Navigator
        TemporalNavigator(
            currentRange = temporalRange,
            availableGranularities = listOf(
                TemporalGranularity.DAY,
                TemporalGranularity.WEEK,
                TemporalGranularity.MONTH,
                TemporalGranularity.YEAR
            ),
            eventCount = 48,
            onRangeChange = onRangeChange
        )

        // Event Stream
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Audit Trail (${selectedFilter})", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                    Text("Tap Event for Audit", color = colors.accent, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }

                val auditEvents = listOf(
                    TemporalEventRecord(
                        eventId = "hist_01",
                        memberId = snapshot.id,
                        eventType = EventType.CHECK_IN,
                        occurredAt = System.currentTimeMillis() - 7200000L,
                        source = EventSource.GATE,
                        sourceMetadata = SourceVerificationMetadata("FACE", "Turnstile Gate 1", "CAM_01", "SUCCESS", 95L, 0.99f),
                        title = "Check-in • Face Verification",
                        subtitle = "Gate 1 • 95ms verification latency"
                    ),
                    TemporalEventRecord(
                        eventId = "hist_02",
                        memberId = snapshot.id,
                        eventType = EventType.WORKOUT,
                        occurredAt = System.currentTimeMillis() - 5400000L,
                        source = EventSource.MEMBER,
                        title = "Push Day A Workout Completed",
                        subtitle = "Logged via BAD GYM App • 62 mins"
                    ),
                    TemporalEventRecord(
                        eventId = "hist_03",
                        memberId = snapshot.id,
                        eventType = EventType.TRAINER_SESSION,
                        occurredAt = System.currentTimeMillis() - 86400000L,
                        source = EventSource.TRAINER,
                        actorName = "Coach Vikram",
                        title = "PT Session with Coach Vikram",
                        subtitle = "Strength Coaching & Form Correction"
                    ),
                    TemporalEventRecord(
                        eventId = "hist_04",
                        memberId = snapshot.id,
                        eventType = EventType.PAYMENT,
                        occurredAt = System.currentTimeMillis() - (12L * 86400000L),
                        source = EventSource.PAYMENT,
                        amount = 15000.0,
                        invoiceNumber = "INV-2026-0902",
                        title = "Quarterly Elite Plan Renewal",
                        subtitle = "₹15,000 via Razorpay UPI AutoPay"
                    )
                )

                auditEvents.forEach { ev ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.surfaceMuted.copy(alpha = 0.5f))
                            .border(1.dp, colors.border.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable { onEventClick(ev) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(colors.accent.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (ev.eventType) {
                                        EventType.CHECK_IN -> Icons.AutoMirrored.Rounded.Login
                                        EventType.CHECK_OUT -> Icons.AutoMirrored.Rounded.Logout
                                        EventType.PAYMENT -> Icons.Rounded.ReceiptLong
                                        EventType.WORKOUT -> Icons.Rounded.FitnessCenter
                                        EventType.TRAINER_SESSION -> Icons.Rounded.SportsMartialArts
                                        else -> Icons.Rounded.Event
                                    },
                                    contentDescription = null,
                                    tint = colors.accent,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Column {
                                Text(ev.title, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(ev.subtitle ?: "", color = colors.textMuted, fontSize = 10.sp)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(ev.formatTimestamp(withSeconds = false), color = colors.textSecondary, fontSize = 10.sp)
                            Text(ev.eventType.displayLabel(), color = colors.accent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. PLAN PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PlanPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val mem = snapshot.membership
    val planName = mem?.planName ?: "General Fitness Plan"
    val tierName = snapshot.identity.tier.name
    val isActive = mem?.isActive ?: true
    val startDate = mem?.startDate ?: snapshot.identity.memberSince
    val daysRemaining = mem?.daysRemaining ?: 45
    val expiryDate = mem?.expiryDate ?: (System.currentTimeMillis() + 45L * 86400000L)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("MEMBERSHIP & PLAN INTELLIGENCE", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(planName, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("$tierName Plan Tier", color = colors.textMuted, fontSize = 11.5.sp)
                    }
                    BadgeChip(if (isActive) "ACTIVE" else "EXPIRED", if (isActive) colors.success else colors.danger)
                }

                HorizontalDivider(color = colors.divider)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Start Date", color = colors.textMuted, fontSize = 10.sp)
                        Text(formatDate(startDate), color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Days Remaining", color = colors.textMuted, fontSize = 10.sp)
                        Text("$daysRemaining days", color = if (daysRemaining < 7) colors.danger else colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("End Date", color = colors.textMuted, fontSize = 10.sp)
                        Text(formatDate(expiryDate), color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 6. TRAINER PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun TrainerPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val tr = snapshot.trainer

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("PERSONAL TRAINING INTELLIGENCE", theme)

        InfoCard(theme) {
            if (tr != null) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(tr.trainerName, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Specialty: Hypertrophy & Biomechanics", color = colors.textMuted, fontSize = 11.5.sp)
                        }
                        BadgeChip("${tr.sessionsTotal - tr.sessionsUsed} Left", colors.accent)
                    }

                    HorizontalDivider(color = colors.divider)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Sessions", color = colors.textMuted, fontSize = 10.sp)
                            Text("${tr.sessionsTotal}", color = colors.textPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Completed", color = colors.textMuted, fontSize = 10.sp)
                            Text("${tr.sessionsUsed}", color = colors.success, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Next Session", color = colors.textMuted, fontSize = 10.sp)
                            Text(tr.nextSessionDate?.let { formatDate(it) } ?: "Tomorrow 6PM", color = colors.accent, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Text("No active Personal Trainer assigned.", color = colors.textMuted, fontSize = 12.sp)
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
    val nut = snapshot.nutrition

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("NUTRITION & MACROS INTELLIGENCE", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(nut?.planName ?: "High-Protein Clean Bulk", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                        Text("Target: 2,650 kcal / day", color = colors.textMuted, fontSize = 11.sp)
                    }
                    BadgeChip("ON TRACK", colors.success)
                }

                HorizontalDivider(color = colors.divider)

                // Macros Row (Protein, Carbs, Fats)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MacroChip("Protein", "180g", "Target: 175g", colors.accent)
                    MacroChip("Carbs", "280g", "Target: 300g", colors.textSecondary)
                    MacroChip("Fats", "65g", "Target: 70g", colors.textMuted)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 8. SUPPLEMENTS PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SupplementsPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("SUPPLEMENTS & STACK INTELLIGENCE", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Daily Protocol", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                SupplementRow("Whey Isolate (Gold Standard)", "1 Scoop Post-Workout (30g)")
                SupplementRow("Creatine Monohydrate", "5g Daily Morning")
                SupplementRow("Omega-3 & Multivitamins", "1 Capsule with Lunch")
            }
        }

        PromotionBanner("Supplements Store Offer: 15% OFF on Whey Refill", "PROMOTION", theme)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 9. SERVICES PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ServicesPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onActionClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("FACILITIES & VALUE-ADDED SERVICES", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ServiceRow("Locker Rental (#42)", "Active until 31 Dec 2026", colors.success)
                ServiceRow("Steam & Sauna Access", "Unlimited (Included in Elite)", colors.accent)
                ServiceRow("Physio Assessment", "1 Complimentary Session Pending", colors.textMuted)
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
    theme: ThemeId
) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("INTELLIGENCE SIGNALS & EVIDENCE", theme)

        if (signals.isNotEmpty()) {
            signals.forEach { sig ->
                InfoCard(theme) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(sig.title, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            BadgeChip(sig.priority.name, if (sig.priority == SignalPriority.P0_CRITICAL) colors.danger else colors.accent)
                        }
                        Text(sig.subtitle ?: sig.evidence.firstOrNull() ?: "", color = colors.textMuted, fontSize = 11.5.sp)
                    }
                }
            }
        } else {
            InfoCard(theme) {
                Text("All signals nominal. No critical member alerts.", color = colors.textMuted, fontSize = 12.sp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 11. MORE DIRECTORY PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun MorePanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onMenuSelect: (MenuType) -> Unit = {}
) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("ALL ACTIONS & SETTINGS DIRECTORY", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                DirectoryRow("Edit Member Profile", Icons.Rounded.Person)
                DirectoryRow("Manage Biometric Face Template", Icons.Rounded.Face)
                DirectoryRow("Download Invoices & Tax Receipts", Icons.Rounded.ReceiptLong)
                DirectoryRow("Submit Support Ticket / Complaint", Icons.AutoMirrored.Rounded.HelpOutline)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// REUSABLE HELPER UI COMPONENTS
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SubMenuTabBar(
    items: List<String>,
    selectedItem: String,
    onSelect: (String) -> Unit
) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) colors.accent else colors.surfaceMuted)
                    .border(1.dp, if (isSelected) colors.accent else colors.border.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .clickable { onSelect(item) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = item,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) colors.textOnAccent else colors.textPrimary
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, theme: ThemeId) {
    val colors = BADGymTheme.colors
    Text(
        text = title,
        color = colors.textPrimary,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 0.5.sp
    )
}

@Composable
fun InfoCard(
    theme: ThemeId,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = BADGymTheme.colors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colors.surface)
            .border(1.dp, colors.border.copy(alpha = 0.75f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        content = content
    )
}

@Composable
fun KpiChip(value: String, label: String, color: Color) {
    val colors = BADGymTheme.colors
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colors.surfaceMuted)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(label, color = colors.textMuted, fontSize = 9.sp)
    }
}

@Composable
fun BadgeChip(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    )
}

@Composable
fun ActionChip(text: String, color: Color, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 10.5.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun MacroChip(name: String, current: String, target: String, color: Color) {
    val colors = BADGymTheme.colors
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colors.surfaceMuted)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(name, color = colors.textMuted, fontSize = 9.sp)
        Text(current, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(target, color = colors.textMuted, fontSize = 8.5.sp)
    }
}

@Composable
fun SupplementRow(name: String, dosage: String) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, color = colors.textPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
        Text(dosage, color = colors.textMuted, fontSize = 11.sp)
    }
}

@Composable
fun ServiceRow(name: String, status: String, color: Color) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, color = colors.textPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
        Text(status, color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun DirectoryRow(title: String, icon: ImageVector) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { }
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = colors.accent, modifier = Modifier.size(16.dp))
            Text(title, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = colors.textMuted, modifier = Modifier.size(14.dp))
    }
}

@Composable
fun PromotionBanner(text: String, badge: String, theme: ThemeId) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colors.warning.copy(alpha = 0.1f))
            .border(1.dp, colors.warning.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = badge,
            color = colors.warning,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(colors.warning.copy(alpha = 0.2f))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = text,
            color = colors.textPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PromotionBanner(
    promotion: PromotionSlot,
    theme: ThemeId,
    onClaim: () -> Unit = {}
) {
    PromotionBanner(
        text = if (promotion.subtitle != null) "${promotion.title} — ${promotion.subtitle}" else promotion.title,
        badge = promotion.badge ?: "PROMOTION",
        theme = theme
    )
}

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

    InfoCard(
        theme = theme,
        modifier = Modifier.clickable { onClick() }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Attendance — Last 7 Days",
                    color = colors.textPrimary,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${weeklyPattern.count { it > 0 }}/7 days",
                    color = colors.accent,
                    fontSize = 10.5.sp,
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
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(if (attended) colors.accent else colors.surfaceMuted)
                                .border(0.6.dp, if (attended) colors.accent else colors.border.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (attended) {
                                Icon(Icons.Rounded.Check, null, tint = colors.textOnAccent, modifier = Modifier.size(11.dp))
                            }
                        }
                        Text(
                            text = dayLabels.getOrElse(i) { "·" },
                            color = if (attended) colors.textPrimary else colors.textMuted,
                            fontSize = 9.sp,
                            fontWeight = if (attended) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

private fun formatDate(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))
