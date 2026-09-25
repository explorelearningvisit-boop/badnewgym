package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AttendancePanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val att = snapshot.attendance
    val visits = att?.visits ?: 0
    val target = (att?.target ?: 26).coerceAtLeast(1)
    val progress = (visits.toFloat() / target).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(500), label = "att-progress")

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("ATTENDANCE INTELLIGENCE", theme)

        // 1. Progress Hero Card
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = att?.periodName ?: "Current Month",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(colors.accent.copy(alpha = 0.15f))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${(progress * 100).toInt()}% Done",
                            color = colors.accent,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$visits",
                        color = colors.textPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "of $target visits target",
                        color = colors.textSecondary,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = colors.accent,
                    trackColor = colors.surfaceMuted
                )
            }
        }

        // 2. Consistency & Key Metrics
        InfoCard(theme) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Streak",
                    value = "${att?.streakDays ?: 0}d",
                    color = colors.success,
                    icon = Icons.Rounded.LocalFireDepartment
                )
                StatItem(
                    label = "Lifetime",
                    value = "${att?.lifetimeVisits ?: 0}",
                    color = colors.textPrimary,
                    icon = Icons.Rounded.CheckCircle
                )
                StatItem(
                    label = "Avg/Week",
                    value = String.format(Locale.getDefault(), "%.1f", att?.avgVisitsPerWeek ?: 3.0),
                    color = colors.accent,
                    icon = Icons.Rounded.FitnessCenter
                )
                StatItem(
                    label = "Slot",
                    value = att?.preferredSlot ?: "Morning",
                    color = colors.textSecondary,
                    icon = Icons.Rounded.Schedule
                )
            }
        }

        // 3. 7-Day Consistency Heat Strip
        val weeklyPattern = att?.weeklyPattern ?: listOf(1, 1, 0, 1, 1, 0, 1)
        val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Weekly Consistency Strip",
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    weeklyPattern.take(7).forEachIndexed { i, active ->
                        val isAttended = active > 0
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(if (isAttended) colors.accent else colors.surfaceMuted)
                                    .border(
                                        width = 0.8.dp,
                                        color = if (isAttended) colors.accent else colors.border.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isAttended) {
                                    Icon(
                                        Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(11.dp)
                                    )
                                }
                            }
                            Text(
                                text = dayLabels.getOrElse(i) { "D" },
                                color = if (isAttended) colors.textPrimary else colors.textMuted,
                                fontSize = 12.sp,
                                fontWeight = if (isAttended) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // 4. Recent Gate Activity
        val events = snapshot.recentEvents.filter {
            it.eventType == EventType.CHECK_IN || it.eventType == EventType.CHECK_OUT
        }.sortedByDescending { it.occurredAt }

        if (events.isNotEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Recent Gate Activity",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    events.take(4).forEach { ev ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (ev.eventType == EventType.CHECK_IN) Icons.Rounded.Login else Icons.Rounded.Logout,
                                    contentDescription = null,
                                    tint = if (ev.eventType == EventType.CHECK_IN) colors.success else colors.textMuted,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = ev.eventType.displayLabel(),
                                    color = colors.textPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = formatDate(ev.occurredAt),
                                color = colors.textSecondary,
                                fontSize = 12.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val mem = snapshot.membership
    val isExpired = mem?.isActive == false || (mem?.daysRemaining ?: 1) <= 0
    val daysRemaining = mem?.daysRemaining ?: 0

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("MEMBERSHIP PLAN", theme)

        // 1. Plan Hero Card
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = mem?.planName ?: "No Active Plan",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isExpired) Color(0xFFFEE2E2) else Color(0xFFDCFCE7))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isExpired) "EXPIRED" else "ACTIVE",
                            color = if (isExpired) Color(0xFFDC2626) else Color(0xFF16A34A),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Text(
                    text = "${mem?.planType ?: "12 Months"} • Annual Membership",
                    color = colors.textSecondary,
                    fontSize = 13.5.sp
                )

                Spacer(Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Days Remaining", color = colors.textMuted, fontSize = 12.sp)
                        Text(
                            text = if (isExpired) "0 Days" else "$daysRemaining Days",
                            color = if (isExpired) Color(0xFFDC2626) else colors.accent,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Renewals", color = colors.textMuted, fontSize = 12.sp)
                        Text(
                            text = "${mem?.renewalCount ?: 0} Times",
                            color = colors.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. Validity Timeline
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Validity Timeline", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                mem?.let {
                    KeyValue("Start Date", formatDateOnly(it.startDate))
                    KeyValue("Expiry Date", formatDateOnly(it.expiryDate))
                    KeyValue("Plan Fee", "₹${(it.currentCost ?: 24000.0).toInt()}")
                }
            }
        }

        // 3. Freeze & Lifecycle
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Freeze & Privileges", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                KeyValue("Freeze Allowance", "${mem?.freezeAllowanceDays ?: 30} days")
                KeyValue("Freeze Consumed", "${mem?.freezeUsedDays ?: 0} days")
                KeyValue("Member Since", formatDateOnly(snapshot.identity.memberSince))
            }
        }

        if (isExpired) {
            ThemedCtaButton(
                theme = theme,
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(34.dp)
            )
        }
    }
}

@Composable
fun PaymentPanel(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    onCtaClick: () -> Unit
) {
    val colors = BADGymTheme.colors
    val pay = snapshot.payment
    val due = pay?.totalOutstanding ?: 0.0
    val isOverdue = due > 0
    val overdueDays = pay?.overdueDays ?: 0

    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))
    val formattedDue = "₹" + formatter.format(due.toInt())

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("PAYMENT AUDIT", theme)

        // 1. Amount Hero Card
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isOverdue) "Outstanding Balance" else "Account Balance",
                        color = colors.textSecondary,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isOverdue) Color(0xFFFEE2E2) else Color(0xFFDCFCE7))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isOverdue) "${overdueDays}d OVERDUE" else "PAID IN FULL",
                            color = if (isOverdue) Color(0xFFDC2626) else Color(0xFF16A34A),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Text(
                    text = formattedDue,
                    color = if (isOverdue) Color(0xFFDC2626) else colors.textPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                )

                pay?.dueDate?.let {
                    Text(
                        text = "Due on: ${formatDate(it)}",
                        color = if (isOverdue) Color(0xFFEF4444) else colors.textMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 2. Action CTA button if due
        if (isOverdue) {
            ThemedCtaButton(
                theme = theme,
                onClick = onCtaClick,
                modifier = Modifier.fillMaxWidth().height(34.dp)
            )
        }

        // 3. Breakdown
        if (!pay?.breakdown.isNullOrEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Breakdown", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    pay!!.breakdown.forEach { item ->
                        KeyValue(item.label, "₹" + item.amount.toInt())
                    }
                }
            }
        }

        // 4. Payment Intelligence
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Payment Intelligence", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                pay?.lastPaymentAmount?.let { KeyValue("Last Payment", "₹" + it.toInt()) }
                pay?.lastPaymentDate?.let { KeyValue("Last Paid Date", formatDateOnly(it)) }
                pay?.lastPaymentMethod?.let { KeyValue("Payment Method", it) }
                pay?.lifetimePaid?.let { KeyValue("Lifetime Paid", "₹" + it.toInt()) }
            }
        }

        // 5. Transaction History
        if (!pay?.history.isNullOrEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Recent Transactions", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    pay!!.history.take(3).forEach { tx ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(formatDateOnly(tx.occurredAt), color = colors.textSecondary, fontSize = 13.sp)
                            Text("₹" + tx.amount.toInt(), color = colors.textPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                            Text(tx.method, color = colors.success, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrainerPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val trainer = snapshot.trainer

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("COACH & PT SESSIONS", theme)

        if (trainer == null) {
            InfoCard(theme) {
                Text("No personal trainer assigned.", color = colors.textSecondary, fontSize = 14.sp)
            }
            return
        }

        // 1. Coach Profile Card
        InfoCard(theme) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.accent.copy(alpha = 0.15f))
                        .border(0.8.dp, colors.accent.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = trainer.trainerName,
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.5.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text("★ 4.9", color = Color(0xFFD97706), fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    trainer.focus?.let {
                        Text(it, color = colors.textSecondary, fontSize = 13.sp, maxLines = 1)
                    }
                }
            }
        }

        // 2. Sessions Progress Card
        val sessionsLeft = (trainer.sessionsTotal - trainer.sessionsUsed).coerceAtLeast(0)
        val sessionsProgress = (trainer.sessionsUsed.toFloat() / trainer.sessionsTotal.coerceAtLeast(1)).coerceIn(0f, 1f)
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("PT Sessions Progress", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("$sessionsLeft Remaining", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                LinearProgressIndicator(
                    progress = { sessionsProgress },
                    modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(2.5.dp)),
                    color = colors.accent,
                    trackColor = colors.surfaceMuted
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Used: ${trainer.sessionsUsed}", color = colors.textMuted, fontSize = 12.5.sp)
                    Text("Total: ${trainer.sessionsTotal}", color = colors.textMuted, fontSize = 12.5.sp)
                }
            }
        }

        // 3. Schedule & Focus
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Session Schedule", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                trainer.nextSessionDate?.let { KeyValue("Next Session", formatDate(it)) }
                trainer.lastSessionDate?.let { KeyValue("Last Session", formatDate(it)) }
                trainer.focus?.let { KeyValue("Current Focus", it) }
            }
        }
    }
}

@Composable
fun WorkoutPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val workout = snapshot.workout

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("WORKOUT & PROGRESSION", theme)

        // 1. Routine Hero Card
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = workout?.currentRoutine ?: "Functional Training",
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Duration: ${workout?.durationMinutes ?: 60} mins", color = colors.textSecondary, fontSize = 13.5.sp)
                    Text("Calories: ${workout?.calories ?: 480} kcal", color = colors.accent, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 2. Weekly Load Visualization
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Session Load (Last 5 Workouts)", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val bars = listOf(0.45f, 0.75f, 1.0f, 0.6f, 0.85f)
                    val days = listOf("Mon", "Tue", "Thu", "Fri", "Sun")
                    bars.forEachIndexed { idx, h ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(14.dp)
                                    .fillMaxHeight(h)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (idx == 2) colors.accent else colors.accent.copy(alpha = 0.4f))
                            )
                            Text(days[idx], color = colors.textMuted, fontSize = 11.5.sp)
                        }
                    }
                }
            }
        }

        // 3. Last Workout Summary
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Workout Log", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                workout?.lastWorkoutDate?.let { KeyValue("Last Session", formatDate(it)) }
                KeyValue("Weekly Target", "4 Sessions / Week")
                KeyValue("Target Zone", "Hypertrophy & Core")
            }
        }
    }
}

@Composable
fun SupplementsPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val item = snapshot.supplements

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("SUPPLEMENTS STACK", theme)

        if (item == null || !item.hasHistory) {
            InfoCard(theme) {
                Text("No supplement purchase history recorded.", color = colors.textSecondary, fontSize = 14.sp)
            }
        } else {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.lastPurchaseName ?: "Whey Protein",
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(colors.accent.copy(alpha = 0.15f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text("Active Supply", color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    item.brand?.let { KeyValue("Brand", it) }
                    item.lastPurchasePrice?.let { KeyValue("Price Paid", "₹" + it.toInt()) }
                    item.lastPurchaseDate?.let { KeyValue("Purchased", formatDateOnly(it)) }
                }
            }

            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Inventory Status", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    KeyValue("Est. Supply", "18 Days Left")
                    KeyValue("Reorder Window", "Recommended in 10 Days")
                }
            }
        }
    }
}

@Composable
fun NutritionPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val item = snapshot.nutrition

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("NUTRITION & MACROS", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item?.planName ?: "Standard Diet Plan",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (item?.isSubscribed == true) Color(0xFFDCFCE7) else Color(0xFFF1F5F9))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (item?.isSubscribed == true) "SUBSCRIBED" else "INACTIVE",
                            color = if (item?.isSubscribed == true) Color(0xFF16A34A) else Color(0xFF64748B),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                item?.monthlyPrice?.let { KeyValue("Monthly Fee", "₹" + it.toInt()) }
                item?.renewalDate?.let { KeyValue("Next Renewal", formatDateOnly(it)) }
            }
        }

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Daily Macro Target", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                KeyValue("Protein", "140g / Day")
                KeyValue("Hydration", "3.5 L / Day")
                KeyValue("Dietary Focus", "Lean Bulking & Recovery")
            }
        }
    }
}

@Composable
fun ServicesPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val services = snapshot.services.orEmpty()

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("ACTIVE GYM SERVICES", theme)

        if (services.isEmpty()) {
            InfoCard(theme) {
                Text("No additional services active.", color = colors.textSecondary, fontSize = 14.sp)
            }
        } else {
            services.forEach { service ->
                InfoCard(theme) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(service.serviceName, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.5.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (service.isActive) Color(0xFFDCFCE7) else Color(0xFFF1F5F9))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = if (service.isActive) "ACTIVE" else "EXPIRED",
                                    color = if (service.isActive) Color(0xFF16A34A) else Color(0xFF64748B),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        service.price?.let { KeyValue("Monthly Price", "₹" + it.toInt()) }
                        service.expiryDate?.let { KeyValue("Valid Till", formatDateOnly(it)) }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val events = snapshot.recentEvents.sortedByDescending { it.occurredAt }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("CHRONOLOGICAL ACTIVITY", theme)

        if (events.isEmpty()) {
            InfoCard(theme) {
                Text("No recent log activity.", color = colors.textSecondary, fontSize = 14.sp)
            }
        } else {
            events.take(6).forEach { ev ->
                InfoCard(theme) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = when (ev.eventType) {
                                    EventType.CHECK_IN -> Icons.Rounded.Login
                                    EventType.CHECK_OUT -> Icons.Rounded.Logout
                                    EventType.TRAINER_SESSION -> Icons.Rounded.FitnessCenter
                                    EventType.WORKOUT -> Icons.Rounded.Bolt
                                    EventType.PAYMENT -> Icons.Rounded.CreditCard
                                    else -> Icons.Rounded.Info
                                },
                                contentDescription = null,
                                tint = colors.accent,
                                modifier = Modifier.size(14.dp)
                            )
                            Column {
                                Text(ev.eventType.displayLabel(), color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Source: ${ev.source.name}", color = colors.textMuted, fontSize = 12.sp)
                            }
                        }
                        Text(formatDate(ev.occurredAt), color = colors.textSecondary, fontSize = 12.5.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun InsightPanel(
    snapshot: MemberSnapshot,
    signals: List<IntelligenceSignal>,
    theme: ThemeId
) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionTitle("OPERATIONAL INTELLIGENCE", theme)

        if (signals.isEmpty()) {
            InfoCard(theme) {
                Text("All systems nominal. No urgent intelligence actions.", color = colors.textSecondary, fontSize = 14.sp)
            }
        } else {
            signals.forEach { sig ->
                val isP0 = sig.priority == SignalPriority.P0_CRITICAL
                InfoCard(theme) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sig.title,
                                color = if (isP0) Color(0xFFDC2626) else colors.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isP0) Color(0xFFFEE2E2) else Color(0xFFFEF3C7))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = if (isP0) "P0 URGENT" else "P1 ACTION",
                                    color = if (isP0) Color(0xFFDC2626) else Color(0xFFD97706),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                        Text(
                            text = sig.subtitle ?: sig.evidence.firstOrNull() ?: sig.value.orEmpty(),
                            color = colors.textSecondary,
                            fontSize = 12.5.sp,
                            lineHeight = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String, theme: ThemeId) {
    Text(
        text = text,
        color = BADGymTheme.colors.textPrimary,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        letterSpacing = 0.4.sp
    )
}

@Composable
private fun InfoCard(theme: ThemeId, content: @Composable ColumnScope.() -> Unit) {
    val colors = BADGymTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFFFAF7F0)
                    ThemeId.BEAST_MODE -> Color(0xCC4A141D)
                    ThemeId.PURPLE_ROYAL -> Color(0xCC3B1C64)
                    ThemeId.FUTURISTIC_NEON -> Color(0xCC162C4E)
                    ThemeId.VIBRANT_GRADIENT -> Color(0xFFF8FAFC)
                    ThemeId.GLASSMORPHISM -> Color(0xD0FFFFFF)
                    ThemeId.MINIMAL_DARK -> Color(0xFFF1F5F9)
                    ThemeId.NATURAL_FRESH -> Color(0xFFF6FBF7)
                }
            )
            .border(
                0.8.dp,
                when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFFE5B842)
                    ThemeId.BEAST_MODE -> Color(0xFFFF334B)
                    ThemeId.PURPLE_ROYAL -> Color(0xFFC084FC)
                    ThemeId.FUTURISTIC_NEON -> Color(0xFF00E5FF)
                    ThemeId.VIBRANT_GRADIENT -> Color(0xFFFBCFE8)
                    ThemeId.GLASSMORPHISM -> Color(0x80FFFFFF)
                    ThemeId.MINIMAL_DARK -> Color(0xFFCBD5E1)
                    ThemeId.NATURAL_FRESH -> Color(0xFFD1E7D7)
                },
                RoundedCornerShape(10.dp)
            )
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
        Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 13.sp)
        Text(value, color = BADGymTheme.colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    color: Color,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(13.dp))
        Text(value, color = color, fontWeight = FontWeight.Black, fontSize = 14.sp)
        Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 11.5.sp)
    }
}

private fun formatDate(value: Long): String =
    SimpleDateFormat("dd MMM • h:mm a", Locale.getDefault()).format(Date(value))

private fun formatDateOnly(value: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(value))
