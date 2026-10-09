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
import com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceAnalytics
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceWindow
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentReceipt
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSummary

// ------------------------------------------------------------------ PAYMENT

/**
 * Ultra-compact payment card. Critical only: state, amount, due date, last +
 * next payment, Collect action. History and breakdown live in the sheet.
 */
@Composable
fun PaymentWidget(
    payment: PaymentSummary?,
    situation: PaymentSituation,
    onCollect: () -> Unit,
    onOpen: () -> Unit,
    compact: Boolean = false,
    receipt: PaymentReceipt? = null
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
        is PaymentSituation.Overdue -> PaymentOverdueCard(payment, situation.overdueDays, onCollect, onOpen)
        is PaymentSituation.Due -> PaymentDueSoonCard(payment, situation, onCollect, onOpen)
        is PaymentSituation.Partial -> PaymentPartialCard(payment, situation, onCollect, onOpen)
        // A fresh receipt means this session cleared the balance: success,
        // not silence. Otherwise a settled account is calmly CURRENT.
        PaymentSituation.Settled ->
            if (receipt != null) PaymentSuccessfulCard(payment, receipt, onOpen)
            else PaymentCurrentCard(payment, onOpen)
    }
}

@Composable
private fun PaymentDueSoonCard(
    payment: PaymentSummary,
    situation: PaymentSituation,
    onCollect: () -> Unit,
    onOpen: () -> Unit
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
    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Payment Due", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
            Spacer(Modifier.width(4.dp))
            Pill("DUE SOON", MiColors.AmberSoft, MiColors.Amber, 9)
            Spacer(Modifier.weight(1f))
            CollectButton(onCollect, small = true)
        }
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(fmtRupees(amount), fontSize = 19.sp, fontWeight = FontWeight.Black, color = MiColors.Red, maxLines = 1)
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text(
                    when {
                        daysLeft == null -> fmtDate(dueMs)
                        daysLeft > 0 -> "Due in $daysLeft days"
                        daysLeft == 0 -> "Due today"
                        else -> "${-daysLeft}d late \u00B7 ${fmtDate(dueMs)}"
                    },
                    fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MiColors.Amber, maxLines = 1
                )
                Text(fmtDate(dueMs), fontSize = 9.5.sp, color = MiColors.TextSecondary, maxLines = 1)
            }
        }
        Spacer(Modifier.height(2.dp))
        KvRow(
            "Last",
            "${fmtDate(payment.lastPaymentDate)} \u00B7 ${fmtRupees(payment.lastPaymentAmount)}" +
                (payment.lastPaymentMethod?.let { " \u00B7 $it" } ?: ""),
            dense = true
        )
        KvRow("Next", fmtDate(dueMs), dense = true)
    }
}

@Composable
private fun PaymentPartialCard(
    payment: PaymentSummary,
    situation: PaymentSituation.Partial,
    onCollect: () -> Unit,
    onOpen: () -> Unit
) {
    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Payment", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
            Spacer(Modifier.width(4.dp))
            Pill("PARTIAL", MiColors.AmberSoft, MiColors.Amber, 9)
            Spacer(Modifier.weight(1f))
            CollectButton(onCollect, small = true)
        }
        Spacer(Modifier.height(2.dp))
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
    }
}

@Composable
private fun PaymentOverdueCard(
    payment: PaymentSummary,
    overdueDays: Int,
    onCollect: () -> Unit,
    onOpen: () -> Unit
) {
    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Pill("OVERDUE", MiColors.Red, Color.White, 9)
            Spacer(Modifier.weight(1f))
            CollectButton(onCollect, small = true)
        }
        Spacer(Modifier.height(2.dp))
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
    }
}

@Composable
private fun PaymentCurrentCard(payment: PaymentSummary, onOpen: () -> Unit) {
    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Payment", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
            Spacer(Modifier.width(4.dp))
            Pill("CLEAR", Color(0xFFE5F6EC), Color(0xFF0E7A4D), 9)
            Spacer(Modifier.weight(1f))
            Text(">", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary)
        }
        Spacer(Modifier.height(2.dp))
        KvRow(
            "Last",
            "${fmtDate(payment.lastPaymentDate)} \u00B7 ${fmtRupees(payment.lastPaymentAmount)}" +
                (payment.lastPaymentMethod?.let { " \u00B7 $it" } ?: ""),
            dense = true
        )
        KvRow("Next", fmtDate(payment.dueDate), dense = true)
    }
}

@Composable
private fun PaymentSuccessfulCard(
    payment: PaymentSummary,
    receipt: PaymentReceipt,
    onOpen: () -> Unit
) {
    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Payment", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
            Spacer(Modifier.width(4.dp))
            Pill("SUCCESSFUL", Color(0xFFE5F6EC), Color(0xFF0E7A4D), 9)
            Spacer(Modifier.weight(1f))
            Text(">", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary)
        }
        Spacer(Modifier.height(2.dp))
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
 * Ultra-compact attendance card: donut + 2x2 legend + 30-day grid + inline
 * trend. Streak chips live in the module sheet ([AttendanceDetailContent]).
 */
@Composable
fun AttendanceWidget(window: AttendanceWindow?, onOpen: () -> Unit, compact: Boolean = false) {
    if (window == null || window.elapsed.isEmpty()) {
        HomeCard {
            WidgetTitle("Attendance", "(30 Days)")
            Spacer(Modifier.height(6.dp))
            EmptyWidgetLine("No attendance records")
        }
        return
    }
    val openDays = window.elapsed.count { it.state != AttendanceDayState.HOLIDAY }
    val attended = window.present + window.late
    val pct = remember(window) {
        if (openDays > 0) (attended * 100 / openDays) else 0
    }
    val trend = remember(window) { AttendanceAnalytics.trendPoints(window) }

    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Attendance", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
            Spacer(Modifier.width(4.dp))
            Text("(30 Days)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MiColors.Blue)
            Spacer(Modifier.weight(1f))
            ViewAllChip(onOpen, small = true)
        }
        Spacer(Modifier.height(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                Donut(pct, size = 46.dp, pctSize = 12)
                // pct label drawn inside Donut
            }
            Spacer(Modifier.width(6.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text("$attended / $openDays Open Days", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
                if (trend != 0) {
                    Text(
                        (if (trend > 0) "\u2191 +" else "\u2193 ") + "${kotlin.math.abs(trend)}% vs prev",
                        fontSize = 9.5.sp, fontWeight = FontWeight.Bold,
                        color = if (trend > 0) MiColors.EmeraldDark else MiColors.Red, maxLines = 1
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                LegendMini(MiColors.Emerald, "Present", window.present.toString())
                LegendMini(MiColors.Emerald, "Late", window.late.toString(), "\u25D4")
                LegendMini(MiColors.Red, "Absent", window.absent.toString())
                LegendMini(Color(0xFF9AA5B1), "Closed", window.holiday.toString(), "\u2715")
            }
        }
        Spacer(Modifier.height(3.dp))
        val days = remember(window) { window.days.takeLast(30) }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DayRow(days.take(15), dot = 13.dp, numSize = 7)
            DayRow(days.drop(15), dot = 13.dp, numSize = 7)
        }
    }
}

/** Full attendance detail for the module sheet. */
@Composable
fun AttendanceDetailContent(window: AttendanceWindow?) {
    if (window == null || window.elapsed.isEmpty()) {
        HomeCard { EmptyWidgetLine("No attendance records") }
        return
    }
    val openDays = window.elapsed.count { it.state != AttendanceDayState.HOLIDAY }
    val attended = window.present + window.late
    val pct = if (openDays > 0) (attended * 100 / openDays) else 0
    val trend = AttendanceAnalytics.trendPoints(window)
    HomeCard {
        WidgetTitle("Attendance", "(30 Days)")
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.Top) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(86.dp)) {
                Donut(pct, size = 76.dp, pctSize = 17)
                Spacer(Modifier.height(2.dp))
                Text("$attended / $openDays", fontSize = 13.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
                Text("Open Days", fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1)
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                LegendRow(MiColors.Emerald, "Present", window.present.toString(), null)
                LegendRow(MiColors.Emerald, "Late", window.late.toString(), "\u25D4")
                LegendRow(MiColors.Red, "Absent", window.absent.toString(), null)
                LegendRow(Color(0xFF9AA5B1), "Closed", window.holiday.toString(), "\u2715")
            }
        }
        Spacer(Modifier.height(6.dp))
        val days = window.days.takeLast(30)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            DayRow(days.take(15), dot = 17.dp, numSize = 8)
            DayRow(days.drop(15), dot = 17.dp, numSize = 8)
        }
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (trend != 0) {
                StatChip(
                    "vs prev 30d",
                    (if (trend > 0) "\u2191 +" else "\u2193 ") + "${kotlin.math.abs(trend)}%",
                    if (trend > 0) MiColors.EmeraldDark else MiColors.Red,
                    Modifier.weight(1f)
                )
            }
            StatChip("Current Streak", "${AttendanceAnalytics.currentStreak(window)} Days", modifier = Modifier.weight(1f))
            StatChip("Best Streak", "${AttendanceAnalytics.longestStreak(window)} Days", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun LegendMini(color: Color, label: String, count: String, glyph: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(11.dp).clip(CircleShape).background(color),
            contentAlignment = Alignment.Center
        ) {
            if (glyph != null) {
                Text(glyph, fontSize = 7.sp, color = Color.White, maxLines = 1)
            }
        }
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 9.5.sp, color = MiColors.TextSecondary, maxLines = 1)
        Spacer(Modifier.width(4.dp))
        Text(count, fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
    }
}

@Composable
private fun LegendRow(color: Color, label: String, count: String, glyph: String?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(14.dp).clip(CircleShape).background(color),
            contentAlignment = Alignment.Center
        ) {
            if (glyph != null) {
                Text(glyph, fontSize = 8.sp, color = Color.White, maxLines = 1)
            }
        }
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 11.5.sp, color = MiColors.TextSecondary, modifier = Modifier.weight(1f), maxLines = 1)
        Text(count, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
    }
}

@Composable
private fun DayRow(
    days: List<com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay>,
    dot: Dp = 17.dp,
    numSize: Int = 8
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        days.forEach { day ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    day.dayOfMonth.toString(),
                    fontSize = numSize.sp,
                    color = MiColors.TextTertiary,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                val (bg, glyph, glyphColor) = when (day.state) {
                    AttendanceDayState.PRESENT -> Triple(MiColors.Emerald, "\u2713", Color.White)
                    AttendanceDayState.LATE -> Triple(MiColors.Emerald, "\u25D4", Color.White)
                    AttendanceDayState.ABSENT -> Triple(MiColors.Red, "\u2715", Color.White)
                    AttendanceDayState.HOLIDAY -> Triple(Color(0xFFD4DCE3), "\u2715", Color.White)
                    AttendanceDayState.FUTURE -> Triple(MiColors.Border, "", MiColors.TextTertiary)
                }
                Box(
                    Modifier.size(dot).clip(CircleShape).background(bg),
                    contentAlignment = Alignment.Center
                ) {
                    if (glyph.isNotEmpty()) {
                        Text(glyph, fontSize = (numSize - 1).coerceAtLeast(6).sp, color = glyphColor, maxLines = 1)
                    }
                }
            }
        }
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
