package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceMonth
import com.example.badnewgym.feature.memberintelligence.domain.analytics.currentStreak
import com.example.badnewgym.feature.memberintelligence.domain.analytics.longestStreak
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceWindow
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentReceipt
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSummary

// ------------------------------------------------------------------ PAYMENT

/**
 * Payment & Plan card. Header carries the state badge and Collect action;
 * the info row shows plan, amount, last and next payment from real records;
 * the sub-nav opens working features and reports unavailable ones honestly.
 */
@Composable
fun PaymentWidget(
    payment: PaymentSummary?,
    situation: PaymentSituation,
    onCollect: () -> Unit,
    onOpen: () -> Unit,
    compact: Boolean = false,
    receipt: PaymentReceipt? = null,
    planName: String? = null,
    planPeriod: String? = null,
    onPlan: () -> Unit = onOpen,
    onHistory: () -> Unit = onOpen,
    onUnavailable: (String) -> Unit = {}
) {
    if (payment == null) {
        HomeCard {
            WidgetTitle("Payment")
            Spacer(Modifier.height(6.dp))
            EmptyWidgetLine("No payment history")
        }
        return
    }
    when (situation) {
        is PaymentSituation.Overdue -> PaymentOverdueCard(payment, situation.overdueDays, onCollect, onOpen, planName, planPeriod, onPlan, onHistory, onUnavailable)
        is PaymentSituation.Due -> PaymentDueSoonCard(payment, situation, onCollect, onOpen, planName, planPeriod, onPlan, onHistory, onUnavailable)
        is PaymentSituation.Partial -> PaymentPartialCard(payment, situation, onCollect, onOpen, planName, planPeriod, onPlan, onHistory, onUnavailable)
        // A fresh receipt means this session cleared the balance: success,
        // not silence. Otherwise a settled account is calmly CURRENT.
        PaymentSituation.Settled ->
            if (receipt != null) PaymentSuccessfulCard(payment, receipt, onOpen, planName, planPeriod, onPlan, onHistory, onUnavailable)
            else PaymentCurrentCard(payment, onOpen, planName, planPeriod, onPlan, onHistory, onUnavailable)
    }
}

@Composable
private fun PaymentHeader(
    badge: String,
    badgeBg: Color,
    badgeFg: Color,
    onCollect: (() -> Unit)?,
    onOpen: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(30.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF04452)),
            contentAlignment = Alignment.Center
        ) { Text("P", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White) }
        Spacer(Modifier.width(7.dp))
        Text("Payment & Plan", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
        Spacer(Modifier.width(6.dp))
        Pill(badge, badgeBg, badgeFg, 9)
        Spacer(Modifier.weight(1f))
        if (onCollect != null) {
            CollectButton(onCollect, small = true)
        } else {
            Text(">", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary,
                modifier = Modifier.clickable(onClick = onOpen))
        }
    }
}

@Composable
private fun PaymentSubNav(onPlan: () -> Unit, onHistory: () -> Unit, onUnavailable: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        PaymentNavChip("Plan", MiColors.Blue, MiColors.BlueSoft, Modifier.weight(1f), onPlan)
        PaymentNavChip("History", MiColors.Purple, MiColors.PurpleSoft, Modifier.weight(1f), onHistory)
        PaymentNavChip("Invoice", MiColors.EmeraldDark, MiColors.Mint, Modifier.weight(1f)) {
            onUnavailable("Invoices are not available in this build.")
        }
        PaymentNavChip("Auto Pay", MiColors.Blue, MiColors.BlueSoft, Modifier.weight(1f)) {
            onUnavailable("Auto Pay is not available in this build.")
        }
        PaymentNavChip("\u2022\u2022\u2022", MiColors.TextSecondary, MiColors.SurfaceSunken, Modifier.weight(0.7f), onHistory)
    }
}

@Composable
private fun PaymentNavChip(label: String, fg: Color, bg: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(label, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = fg, maxLines = 1)
    }
}

@Composable
private fun PlanCell(planName: String?, planPeriod: String?, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(planName ?: "-", fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
        Text(planPeriod ?: "", fontSize = 9.5.sp, color = MiColors.TextSecondary, maxLines = 1)
    }
}

@Composable
private fun PaymentDueSoonCard(
    payment: PaymentSummary,
    situation: PaymentSituation,
    onCollect: () -> Unit,
    onOpen: () -> Unit,
    planName: String?,
    planPeriod: String?,
    onPlan: () -> Unit,
    onHistory: () -> Unit,
    onUnavailable: (String) -> Unit
) {
    val amount = when (situation) {
        is PaymentSituation.Due -> situation.amount
        is PaymentSituation.Partial -> situation.remaining
        else -> payment.totalOutstanding
    }
    val dueMs = when (situation) {
        is PaymentSituation.Due -> situation.dueAtEpoch
        is PaymentSituation.Partial -> situation.dueAtEpoch
        else -> payment.dueDate
    }
    val daysLeft = dueMs?.let { Math.round((it - System.currentTimeMillis()) / 86_400_000.0).toInt() }
    HomeCard(onClick = onOpen, padding = 8.dp) {
        PaymentHeader("DUE SOON", MiColors.AmberSoft, MiColors.Amber, onCollect, onOpen)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.Top) {
            PlanCell(planName, planPeriod, Modifier.weight(1f))
            Column(modifier = Modifier.weight(1f)) {
                Text("Amount", fontSize = 9.5.sp, color = MiColors.TextSecondary, maxLines = 1)
                Text(fmtRupees(amount), fontSize = 14.sp, fontWeight = FontWeight.Black, color = MiColors.Red, maxLines = 1)
                Text(
                    when {
                        daysLeft == null -> fmtDate(dueMs)
                        daysLeft > 0 -> "Due in $daysLeft days"
                        daysLeft == 0 -> "Due today"
                        else -> "${-daysLeft}d late"
                    },
                    fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = MiColors.Red, maxLines = 1
                )
                Text(fmtDate(dueMs), fontSize = 9.sp, color = MiColors.TextSecondary, maxLines = 1)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Last Paid", fontSize = 9.5.sp, color = MiColors.TextSecondary, maxLines = 1)
                Text(fmtRupees(payment.lastPaymentAmount), fontSize = 14.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
                Text(fmtDate(payment.lastPaymentDate), fontSize = 9.sp, color = MiColors.TextSecondary, maxLines = 1)
                Spacer(Modifier.height(2.dp))
                Pill("Paid", Color(0xFFE5F6EC), Color(0xFF0E7A4D), 9)
            }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text("Next Due", fontSize = 9.5.sp, color = MiColors.TextSecondary, maxLines = 1)
                Text(fmtRupees(amount), fontSize = 14.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
                Text(fmtDate(dueMs), fontSize = 9.sp, color = MiColors.TextSecondary, maxLines = 1)
                Spacer(Modifier.height(2.dp))
                Pill("Upcoming", MiColors.AmberSoft, MiColors.Amber, 9)
            }
        }
        Spacer(Modifier.height(6.dp))
        PaymentSubNav(onPlan, onHistory, onUnavailable)
    }
}

@Composable
private fun PaymentPartialCard(
    payment: PaymentSummary,
    situation: PaymentSituation.Partial,
    onCollect: () -> Unit,
    onOpen: () -> Unit,
    planName: String?,
    planPeriod: String?,
    onPlan: () -> Unit,
    onHistory: () -> Unit,
    onUnavailable: (String) -> Unit
) {
    HomeCard(onClick = onOpen, padding = 8.dp) {
        PaymentHeader("PARTIAL", MiColors.AmberSoft, MiColors.Amber, onCollect, onOpen)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(fmtRupees(situation.remaining), fontSize = 19.sp, fontWeight = FontWeight.Black, color = MiColors.Amber, maxLines = 1)
            Spacer(Modifier.width(8.dp))
            Text(
                "Paid ${fmtRupees(situation.received)}",
                fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MiColors.EmeraldDark,
                modifier = Modifier.weight(1f), maxLines = 1, textAlign = TextAlign.End
            )
        }
        Spacer(Modifier.height(2.dp))
        KvRow("Next", fmtDate(situation.dueAtEpoch), dense = true)
        Spacer(Modifier.height(6.dp))
        PaymentSubNav(onPlan, onHistory, onUnavailable)
    }
}

@Composable
private fun PaymentOverdueCard(
    payment: PaymentSummary,
    overdueDays: Int,
    onCollect: () -> Unit,
    onOpen: () -> Unit,
    planName: String?,
    planPeriod: String?,
    onPlan: () -> Unit,
    onHistory: () -> Unit,
    onUnavailable: (String) -> Unit
) {
    HomeCard(onClick = onOpen, padding = 8.dp) {
        PaymentHeader("OVERDUE", MiColors.Red, Color.White, onCollect, onOpen)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(fmtRupees(payment.totalOutstanding), fontSize = 19.sp, fontWeight = FontWeight.Black, color = MiColors.Red, maxLines = 1)
            Spacer(Modifier.width(8.dp))
            Text(
                "$overdueDays days overdue",
                fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MiColors.Red,
                modifier = Modifier.weight(1f), maxLines = 1,
                textAlign = TextAlign.End
            )
        }
        Spacer(Modifier.height(2.dp))
        KvRow(
            "Last",
            "${fmtDate(payment.lastPaymentDate)} \u00B7 ${fmtRupees(payment.lastPaymentAmount)}",
            dense = true
        )
        KvRow("Next", fmtDate(payment.dueDate), dense = true)
        Spacer(Modifier.height(6.dp))
        PaymentSubNav(onPlan, onHistory, onUnavailable)
    }
}

@Composable
private fun PaymentCurrentCard(
    payment: PaymentSummary,
    onOpen: () -> Unit,
    planName: String?,
    planPeriod: String?,
    onPlan: () -> Unit,
    onHistory: () -> Unit,
    onUnavailable: (String) -> Unit
) {
    HomeCard(onClick = onOpen, padding = 8.dp) {
        PaymentHeader("CLEAR", Color(0xFFE5F6EC), Color(0xFF0E7A4D), null, onOpen)
        Spacer(Modifier.height(4.dp))
        KvRow(
            "Last",
            "${fmtDate(payment.lastPaymentDate)} \u00B7 ${fmtRupees(payment.lastPaymentAmount)}" +
                (payment.lastPaymentMethod?.let { " \u00B7 $it" } ?: ""),
            dense = true
        )
        KvRow("Next", fmtDate(payment.dueDate), dense = true)
        Spacer(Modifier.height(6.dp))
        PaymentSubNav(onPlan, onHistory, onUnavailable)
    }
}

@Composable
private fun PaymentSuccessfulCard(
    payment: PaymentSummary,
    receipt: PaymentReceipt,
    onOpen: () -> Unit,
    planName: String?,
    planPeriod: String?,
    onPlan: () -> Unit,
    onHistory: () -> Unit,
    onUnavailable: (String) -> Unit
) {
    HomeCard(onClick = onOpen, padding = 8.dp) {
        PaymentHeader("SUCCESSFUL", Color(0xFFE5F6EC), Color(0xFF0E7A4D), null, onOpen)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(fmtRupees(receipt.amount), fontSize = 19.sp, fontWeight = FontWeight.Black, color = MiColors.EmeraldDark, maxLines = 1)
            Spacer(Modifier.width(8.dp))
            Text(
                "${paymentMethodLabel(receipt.method)} \u00B7 ${fmtTime(receipt.paidAtEpoch)}",
                fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MiColors.TextSecondary,
                modifier = Modifier.weight(1f), maxLines = 1, textAlign = TextAlign.End
            )
        }
        Spacer(Modifier.height(2.dp))
        KvRow("Receipt", receipt.receiptId, dense = true)
        KvRow("Remaining", fmtRupees(payment.totalOutstanding), dense = true)
        Spacer(Modifier.height(6.dp))
        PaymentSubNav(onPlan, onHistory, onUnavailable)
    }
}

@Composable
private fun CollectButton(onClick: () -> Unit, small: Boolean = false) {
    Box(
        Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFFF6B4A))
            .clickable(onClick = onClick)
            .padding(horizontal = if (small) 11.dp else 14.dp, vertical = if (small) 6.dp else 9.dp)
    ) {
        Text("Collect >", fontSize = if (small) 10.5.sp else 12.sp, fontWeight = FontWeight.Black, color = Color.White)
    }
}

// ------------------------------------------------------------------ ATTENDANCE

/**
 * Full-month attendance card: every valid date of the selected month,
 * percentage over open days only, previous-month comparison when valid, and
 * payment markers that never replace the status.
 */
@Composable
fun AttendanceWidget(
    month: AttendanceMonth?,
    prevPoints: Int?,
    selected: java.time.YearMonth,
    onMonthChange: (java.time.YearMonth) -> Unit,
    onOpen: () -> Unit,
    compact: Boolean = false
) {
    if (month == null || month.days.isEmpty()) {
        HomeCard {
            WidgetTitle("Attendance", "(Month)")
            Spacer(Modifier.height(6.dp))
            EmptyWidgetLine("No attendance records")
        }
        return
    }
    HomeCard(onClick = onOpen, padding = 8.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(30.dp).clip(RoundedCornerShape(10.dp)).background(MiColors.Emerald),
                contentAlignment = Alignment.Center
            ) { Text("A", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White) }
            Spacer(Modifier.width(7.dp))
            Text("Attendance", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
            Spacer(Modifier.width(4.dp))
            Text("(${month.monthLabel})", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = MiColors.Blue, maxLines = 1)
            Spacer(Modifier.weight(1f))
            ViewAllChip(onOpen, small = true)
        }
        Spacer(Modifier.height(4.dp))
        MonthNavRow(selected, onMonthChange)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                Donut(month.percent, size = 58.dp, pctSize = 14)
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    "${month.attended} / ${month.openDays.size} Open Days",
                    fontSize = 12.5.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1
                )
                if (prevPoints != null && prevPoints != 0) {
                    Text(
                        (if (prevPoints > 0) "\u2191 +" else "\u2193 ") + "${kotlin.math.abs(prevPoints)}% vs prev month",
                        fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        color = if (prevPoints > 0) MiColors.EmeraldDark else MiColors.Red, maxLines = 1
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                LegendMini(MiColors.Emerald, "Present", month.present.toString())
                LegendMini(Color(0xFFF5B33B), "Late", month.late.toString())
                LegendMini(MiColors.Red, "Absent", month.absent.toString())
                LegendMini(Color(0xFF8849EF), "Holiday", month.holiday.toString())
                LegendMini(Color(0xFF9AA5B1), "Closed", month.closed.toString())
            }
        }
        Spacer(Modifier.height(6.dp))
        val first = remember(month) { month.days.filter { it.dayOfMonth <= 15 } }
        val second = remember(month) { month.days.filter { it.dayOfMonth > 15 } }
        MonthDayRow(first)
        Spacer(Modifier.height(3.dp))
        MonthDayRow(second)
        Spacer(Modifier.height(6.dp))
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LegendMini(MiColors.Emerald, "Present", null)
                LegendMini(Color(0xFFF5B33B), "Late", null)
                LegendMini(MiColors.Red, "Absent", null)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LegendMini(Color(0xFF8849EF), "Holiday", null)
                LegendMini(Color(0xFF9AA5B1), "Closed", null)
                LegendMini(Color(0xFF267FEA), "Payment", null, "\u20B9")
            }
        }
    }
}

@Composable
private fun MonthNavRow(selected: java.time.YearMonth, onMonthChange: (java.time.YearMonth) -> Unit) {
    val now = remember { java.time.YearMonth.now() }
    val label = remember(selected) {
        selected.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3) + " " + selected.year
    }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
        Spacer(Modifier.width(8.dp))
        MonthArrow("<", enabled = true) { onMonthChange(selected.minusMonths(1)) }
        Spacer(Modifier.width(6.dp))
        MonthArrow(">", enabled = selected < now) {
            if (selected < now) onMonthChange(selected.plusMonths(1))
        }
    }
}

@Composable
private fun MonthArrow(label: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (enabled) MiColors.BlueSoft else MiColors.SurfaceSunken)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Black, color = if (enabled) MiColors.Blue else MiColors.TextTertiary)
    }
}

@Composable
private fun MonthDayRow(days: List<com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay>) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        days.forEach { day ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(
                    day.dayOfMonth.toString(),
                    fontSize = 9.sp,
                    color = MiColors.TextTertiary,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                val (bg, glyph, glyphColor) = when (day.state) {
                    AttendanceDayState.PRESENT -> Triple(MiColors.Emerald, "\u2713", Color.White)
                    AttendanceDayState.LATE -> Triple(Color(0xFFF5B33B), "\u25D4", Color(0xFF5B4A00))
                    AttendanceDayState.ABSENT -> Triple(MiColors.Red, "\u2715", Color.White)
                    AttendanceDayState.HOLIDAY -> Triple(Color(0xFF8849EF), "H", Color.White)
                    AttendanceDayState.CLOSED -> Triple(Color(0xFF9AA5B1), "\u2212", Color.White)
                    AttendanceDayState.FUTURE -> Triple(MiColors.Border, "", MiColors.TextTertiary)
                    AttendanceDayState.UNKNOWN -> Triple(MiColors.Border, "?", MiColors.TextTertiary)
                }
                Box(
                    Modifier.size(15.dp).clip(CircleShape).background(bg),
                    contentAlignment = Alignment.Center
                ) {
                    if (glyph.isNotEmpty()) {
                        Text(glyph, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = glyphColor, maxLines = 1)
                    }
                }
                // Payment marker occupies a fixed slot so dots stay aligned.
                Box(Modifier.height(11.dp), contentAlignment = Alignment.Center) {
                    if (day.hasPayment) {
                        Text("\u20B9", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF267FEA), maxLines = 1)
                    }
                }
            }
        }
    }
}

/** Full attendance detail for the module sheet. */
@Composable
fun AttendanceDetailContent(
    month: AttendanceMonth?,
    prevPoints: Int?,
    selected: java.time.YearMonth,
    onMonthChange: (java.time.YearMonth) -> Unit
) {
    if (month == null || month.days.isEmpty()) {
        HomeCard { EmptyWidgetLine("No attendance records") }
        return
    }
    HomeCard {
        WidgetTitle("Attendance", "(${month.monthLabel})")
        Spacer(Modifier.height(6.dp))
        MonthNavRow(selected, onMonthChange)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.Top) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(96.dp)) {
                Donut(month.percent, size = 84.dp, pctSize = 19)
                Spacer(Modifier.height(2.dp))
                Text("${month.attended} / ${month.openDays.size}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
                Text("Open Days", fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1)
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                LegendRow(MiColors.Emerald, "Present", month.present.toString(), null)
                LegendRow(Color(0xFFF5B33B), "Late", month.late.toString(), "\u25D4", Color(0xFF5B4A00))
                LegendRow(MiColors.Red, "Absent", month.absent.toString(), null)
                LegendRow(Color(0xFF8849EF), "Holiday", month.holiday.toString(), "H")
                LegendRow(Color(0xFF9AA5B1), "Closed", month.closed.toString(), "\u2212")
                if (month.unknown > 0) {
                    LegendRow(MiColors.Border, "Unknown", month.unknown.toString(), "?")
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        MonthDayRow(month.days.filter { it.dayOfMonth <= 15 })
        Spacer(Modifier.height(4.dp))
        MonthDayRow(month.days.filter { it.dayOfMonth > 15 })
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (prevPoints != null && prevPoints != 0) {
                StatChip(
                    "vs prev month",
                    (if (prevPoints > 0) "\u2191 +" else "\u2193 ") + "${kotlin.math.abs(prevPoints)}%",
                    if (prevPoints > 0) MiColors.EmeraldDark else MiColors.Red,
                    Modifier.weight(1f)
                )
            }
            StatChip("Current Streak", "${month.currentStreak()} Days", modifier = Modifier.weight(1f))
            StatChip("Best Streak", "${month.longestStreak()} Days", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun LegendMini(color: Color, label: String, count: String? = null, glyph: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(11.dp).clip(CircleShape).background(color),
            contentAlignment = Alignment.Center
        ) {
            if (glyph != null) {
                Text(glyph, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
            }
        }
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 9.sp, color = MiColors.TextSecondary, maxLines = 1)
        if (count != null) {
            Spacer(Modifier.width(4.dp))
            Text(count, fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
        }
    }
}

@Composable
private fun LegendRow(color: Color, label: String, count: String, glyph: String?, glyphColor: Color = Color.White) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(14.dp).clip(CircleShape).background(color),
            contentAlignment = Alignment.Center
        ) {
            if (glyph != null) {
                Text(glyph, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = glyphColor, maxLines = 1)
            }
        }
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 11.5.sp, color = MiColors.TextSecondary, modifier = Modifier.weight(1f), maxLines = 1)
        Text(count, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
    }
}

@Composable
private fun Donut(pct: Int, size: Dp = 76.dp, pctSize: Int = 17) {
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(Modifier.size(size)) {
            val sweep = pct.toFloat() / 100f * 360f
            drawArc(
                color = Color(0xFFE3EEE9),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 9.dp.toPx())
            )
            drawArc(
                color = Color(0xFF00B86B),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 9.dp.toPx(),
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            )
        }
        Text("$pct%", fontSize = pctSize.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
    }
}
