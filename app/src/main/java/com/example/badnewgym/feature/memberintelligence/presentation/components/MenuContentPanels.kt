package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AttendancePanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val att = snapshot.attendance
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Attendance", theme)

        InfoCard(theme) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem(att?.periodName ?: "Period", (att?.visits ?: 0).toString(), colors.textPrimary)
                StatItem("Target", (att?.target ?: 0).toString(), colors.textSecondary)
                StatItem("Streak", (att?.streakDays ?: 0).toString() + " days", colors.success)
            }
        }

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Attendance intelligence", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                KeyValue("Lifetime visits", (att?.lifetimeVisits ?: 0).toString())
                att?.avgVisitsPerWeek?.let { KeyValue("Average / week", String.format(Locale.getDefault(), "%.1f", it)) }
                att?.preferredSlot?.let { KeyValue("Preferred slot", it) }
                att?.lastVisitAt?.let { KeyValue("Last visit", formatDate(it)) }
            }
        }

        val events = snapshot.recentEvents.filter {
            it.eventType == EventType.CHECK_IN || it.eventType == EventType.CHECK_OUT
        }.sortedByDescending { it.occurredAt }

        if (events.isNotEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Recent gate activity", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    events.take(8).forEach {
                        KeyValue(it.eventType.displayLabel(), formatDate(it.occurredAt))
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Membership Plan", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(mem?.planName ?: "No active membership", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                mem?.let {
                    KeyValue("Type", it.planType)
                    KeyValue("Start", formatDate(it.startDate))
                    KeyValue("Expiry", formatDate(it.expiryDate))
                    KeyValue("Days remaining", it.daysRemaining.toString())
                    KeyValue("Status", if (it.isActive) "ACTIVE" else "EXPIRED")
                    KeyValue("Renewals", it.renewalCount.toString())
                }
            }
        }

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Freeze & lifecycle", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                KeyValue("Freeze allowance", (mem?.freezeAllowanceDays ?: 0).toString() + " days")
                KeyValue("Freeze used", (mem?.freezeUsedDays ?: 0).toString() + " days")
                mem?.previousPlanName?.let { KeyValue("Previous plan", it) }
                snapshot.identity.memberSince.let { KeyValue("Member since", formatDate(it)) }
            }
        }
    }
}

@Composable
fun PaymentPanel(snapshot: MemberSnapshot, theme: ThemeId, onCtaClick: () -> Unit) {
    val colors = BADGymTheme.colors
    val pay = snapshot.payment
    val due = pay?.totalOutstanding ?: 0.0
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Payment", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    if (due > 0) "₹" + due.toInt() else "₹0",
                    color = if (due > 0) colors.danger else colors.success,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                )
                Text(if (due > 0) "Outstanding" else "All Clear", color = colors.textSecondary, fontSize = 11.sp)
                if (pay?.overdueDays ?: 0 > 0) {
                    Text("Overdue " + pay!!.overdueDays + " days", color = colors.danger, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                pay?.dueDate?.let { KeyValue("Due date", formatDate(it)) }
            }
        }

        if (due > 0) {
            ThemedCtaButton(theme = theme, onClick = onCtaClick)
        }

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Payment intelligence", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                pay?.lastPaymentAmount?.let { KeyValue("Last payment", "₹" + it.toInt()) }
                pay?.lastPaymentDate?.let { KeyValue("Last paid", formatDate(it)) }
                pay?.lastPaymentMethod?.let { KeyValue("Method", it) }
                pay?.lifetimePaid?.let { KeyValue("Lifetime paid", "₹" + it.toInt()) }
            }
        }

        if (!pay?.history.isNullOrEmpty()) {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Transaction history", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    pay!!.history.sortedByDescending { it.occurredAt }.take(10).forEach {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(formatDate(it.occurredAt), color = colors.textSecondary, fontSize = 10.sp)
                            Text("₹" + it.amount.toInt(), color = colors.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text(it.method, color = colors.success, fontSize = 9.sp)
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Trainer", theme)

        if (trainer == null) {
            InfoCard(theme) {
                Text("No trainer assigned.", color = colors.textSecondary, fontSize = 12.sp)
            }
            return
        }

        InfoCard(theme) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(colors.surfaceMuted),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Person, null, tint = colors.accent, modifier = Modifier.size(27.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(trainer.trainerName, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    trainer.focus?.let { Text(it, color = colors.textSecondary, fontSize = 10.sp) }
                    trainer.rating?.let { Text("Rating " + it, color = colors.accent, fontSize = 10.sp) }
                }
            }
        }

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                KeyValue("Sessions used", trainer.sessionsUsed.toString())
                KeyValue("Sessions remaining", (trainer.sessionsTotal - trainer.sessionsUsed).coerceAtLeast(0).toString())
                trainer.nextSessionDate?.let { KeyValue("Next session", formatDate(it)) }
                trainer.lastSessionDate?.let { KeyValue("Last session", formatDate(it)) }
            }
        }
    }
}

@Composable
fun WorkoutPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val workout = snapshot.workout
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Workout", theme)
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                KeyValue("Current routine", workout?.currentRoutine ?: "Not assigned")
                workout?.lastWorkoutDate?.let { KeyValue("Last workout", formatDate(it)) }
                workout?.durationMinutes?.let { KeyValue("Last duration", it.toString() + " min") }
                workout?.calories?.let { KeyValue("Calories", it.toString() + " kcal") }
            }
        }
    }
}

@Composable
fun SupplementsPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val item = snapshot.supplements
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Supplements", theme)
        if (item == null || !item.hasHistory) {
            InfoCard(theme) { Text("No supplement purchase history.", color = colors.textSecondary, fontSize = 12.sp) }
        } else {
            InfoCard(theme) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(item.lastPurchaseName ?: "Last supplement", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    item.brand?.let { KeyValue("Brand", it) }
                    item.lastPurchaseDate?.let { KeyValue("Purchased", formatDate(it)) }
                    item.lastPurchasePrice?.let { KeyValue("Price", "₹" + it.toInt()) }
                }
            }
        }
    }
}

@Composable
fun NutritionPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val item = snapshot.nutrition
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Nutrition", theme)
        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    if (item?.isSubscribed == true) "ACTIVE SUBSCRIPTION" else "NOT SUBSCRIBED",
                    color = if (item?.isSubscribed == true) colors.success else colors.textSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                item?.planName?.let { KeyValue("Plan", it) }
                item?.monthlyPrice?.let { KeyValue("Monthly price", "₹" + it.toInt()) }
                item?.renewalDate?.let { KeyValue("Renewal", formatDate(it)) }
            }
        }
    }
}

@Composable
fun ServicesPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val services = snapshot.services.orEmpty()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Services", theme)
        if (services.isEmpty()) {
            InfoCard(theme) { Text("No service history.", color = colors.textSecondary, fontSize = 12.sp) }
        } else {
            services.forEach { service ->
                InfoCard(theme) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(service.serviceName, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        KeyValue("Status", if (service.isActive) "ACTIVE" else "INACTIVE")
                        service.price?.let { KeyValue("Price", "₹" + it.toInt()) }
                        service.expiryDate?.let { KeyValue("Expiry", formatDate(it)) }
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("History", theme)
        if (events.isEmpty()) {
            InfoCard(theme) { Text("No recent events.", color = colors.textSecondary, fontSize = 12.sp) }
        } else {
            events.take(15).forEach { event ->
                InfoCard(theme) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(event.eventType.displayLabel(), color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(event.source.name, color = colors.textSecondary, fontSize = 9.sp)
                        }
                        Text(formatDate(event.occurredAt), color = colors.textSecondary, fontSize = 9.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun InsightPanel(snapshot: MemberSnapshot, signals: List<IntelligenceSignal>, theme: ThemeId) {
    val colors = BADGymTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Member Intelligence", theme)
        if (signals.isEmpty()) {
            InfoCard(theme) { Text("No active intelligence signals.", color = colors.textSecondary, fontSize = 12.sp) }
        } else {
            signals.forEach { IntelligenceSignalCard(signal = it, emphasized = true) }
        }
    }
}

@Composable
private fun SectionTitle(text: String, theme: ThemeId) {
    Text(text = text, color = BADGymTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
}

@Composable
private fun InfoCard(theme: ThemeId, content: @Composable ColumnScope.() -> Unit) {
    val colors = BADGymTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFF1F1B12)
                    ThemeId.BEAST_MODE -> Color(0xFF1C0609)
                    ThemeId.PURPLE_ROYAL -> Color(0xFF22113B)
                    ThemeId.FUTURISTIC_NEON -> Color(0xFF0A1428)
                    ThemeId.VIBRANT_GRADIENT -> Color(0xFFF8FAFC)
                    ThemeId.GLASSMORPHISM -> Color(0xB0FFFFFF)
                    ThemeId.MINIMAL_DARK -> Color(0xFF181C22)
                    ThemeId.NATURAL_FRESH -> Color(0xFFF6FBF7)
                }
            )
            .border(
                1.dp,
                when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFF4A3B1D)
                    ThemeId.BEAST_MODE -> Color(0x66FF1E27)
                    ThemeId.PURPLE_ROYAL -> Color(0x66A855F7)
                    ThemeId.FUTURISTIC_NEON -> Color(0x6600E5FF)
                    ThemeId.VIBRANT_GRADIENT -> Color(0xFFE2E8F0)
                    ThemeId.GLASSMORPHISM -> Color(0x80FFFFFF)
                    ThemeId.MINIMAL_DARK -> Color(0xFF28303C)
                    ThemeId.NATURAL_FRESH -> Color(0xFFD1E7D7)
                },
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        content = content
    )
}

@Composable
private fun KeyValue(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 10.sp)
        Text(value, color = BADGymTheme.colors.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 9.sp)
    }
}

private fun formatDate(value: Long): String =
    SimpleDateFormat("dd MMM • h:mm a", Locale.getDefault()).format(Date(value))
