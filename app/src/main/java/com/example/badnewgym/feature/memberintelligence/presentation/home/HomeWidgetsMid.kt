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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceAnalytics
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayState
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceWindow
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSituation
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSummary

// ------------------------------------------------------------------ PAYMENT

/**
 * Three honest states. Clear members get a calm CURRENT card, never a red wall.
 * Payment owns payment info - nothing else on Home repeats it.
 */
@Composable
fun PaymentWidget(
    payment: PaymentSummary?,
    situation: PaymentSituation,
    onCollect: () -> Unit,
    onOpen: () -> Unit
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
        is PaymentSituation.Due -> PaymentDueCard(payment, situation, onCollect, onOpen)
        is PaymentSituation.Partial -> PaymentDueCard(payment, situation, onCollect, onOpen)
        PaymentSituation.Settled -> PaymentCurrentCard(payment, onOpen)
    }
}

@Composable
private fun PaymentDueCard(
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
    val daysLeft = dueMs?.let { ((it - System.currentTimeMillis()) / 86_400_000L).toInt() }
    HomeCard(onClick = onOpen) {
        WidgetTitle("Payment", "Due", MiColors.Red)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(fmtRupees(amount), fontSize = 24.sp, fontWeight = FontWeight.Black, color = MiColors.Red, maxLines = 1)
                Text(
                    if (daysLeft != null && daysLeft >= 0) "Due in $daysLeft days  \u00B7  ${fmtDate(dueMs)}"
                    else fmtDate(dueMs),
                    fontSize = 11.5.sp, color = MiColors.TextSecondary, maxLines = 1
                )
            }
            CollectButton(onCollect)
        }
        Spacer(Modifier.height(6.dp))
        val paid = (payment.lifetimePaid ?: 0.0)
        val plan = paid + amount
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatChip("Paid", fmtRupees(paid), MiColors.EmeraldDark, Modifier.weight(1f))
            StatChip("Plan Value", fmtRupees(if (plan > 0) plan else null), modifier = Modifier.weight(1f))
            StatChip("Remaining", fmtRupees(amount), MiColors.Red, Modifier.weight(1f))
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = if (plan > 0) (paid / plan).toFloat().coerceIn(0f, 1f) else 0f,
            modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(4.dp)),
            color = MiColors.Emerald,
            trackColor = MiColors.Border
        )
        Spacer(Modifier.height(4.dp))
        KvRow(
            "Last Payment",
            "${fmtDate(payment.lastPaymentDate)}  \u00B7  ${fmtRupees(payment.lastPaymentAmount)}" +
                (payment.lastPaymentMethod?.let { "  \u00B7  $it" } ?: "")
        )
        KvRow("Next Due", fmtDate(dueMs))
    }
}

@Composable
private fun PaymentOverdueCard(
    payment: PaymentSummary,
    overdueDays: Int,
    onCollect: () -> Unit,
    onOpen: () -> Unit
) {
    HomeCard(onClick = onOpen) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Pill("PAYMENT OVERDUE", MiColors.Red, Color.White, 11)
            Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(fmtRupees(payment.totalOutstanding), fontSize = 24.sp, fontWeight = FontWeight.Black, color = MiColors.Red, maxLines = 1)
                Text("$overdueDays days overdue", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = MiColors.Red, maxLines = 1)
            }
            CollectButton(onCollect)
        }
        Spacer(Modifier.height(6.dp))
        KvRow(
            "Last Payment",
            "${fmtDate(payment.lastPaymentDate)}  \u00B7  ${fmtRupees(payment.lastPaymentAmount)}"
        )
        KvRow("Next Due", fmtDate(payment.dueDate))
    }
}

@Composable
private fun PaymentCurrentCard(payment: PaymentSummary, onOpen: () -> Unit) {
    HomeCard(onClick = onOpen) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Payment", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary)
            Spacer(Modifier.width(6.dp))
            Pill("CURRENT", Color(0xFFE5F6EC), Color(0xFF0E7A4D), 10)
            Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "Payment is currently clear.",
            fontSize = 12.sp, color = MiColors.TextSecondary, fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(6.dp))
        KvRow(
            "Last Payment",
            "${fmtDate(payment.lastPaymentDate)}  \u00B7  ${fmtRupees(payment.lastPaymentAmount)}"
        )
        KvRow("Next Due", fmtDate(payment.dueDate))
        KvRow("Method", payment.lastPaymentMethod ?: "-")
        KvRow("Membership", "Active", MiColors.EmeraldDark)
        if ((payment.lifetimePaid ?: 0.0) > 0) {
            KvRow("Paid", fmtRupees(payment.lifetimePaid))
            KvRow("Remaining", fmtRupees(0.0))
        }
    }
}

@Composable
private fun CollectButton(onClick: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFFF6B4A))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text("Collect Payment >", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
    }
}

// ------------------------------------------------------------------ ATTENDANCE

/**
 * Attendance answers only "DID THE MEMBER COME?" No workout minutes here.
 * Green = present, green + clock = late, red = absent, gray X = closed.
 * Denominator is open days, never blindly 30.
 */
@Composable
fun AttendanceWidget(window: AttendanceWindow?, onOpen: () -> Unit) {
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

    HomeCard(onClick = onOpen) {
        WidgetTitle("Attendance", "(30 Days)", trailing = { ViewAllChip(onOpen) })
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.Top) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(86.dp)
            ) {
                Donut(pct)
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
        // Compact 30-day grid: two rows of 15, true day numbers, state glyphs.
        val days = remember(window) { window.days.takeLast(30) }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            DayRow(days.take(15))
            DayRow(days.drop(15))
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
private fun LegendRow(color: Color, label: String, count: String, glyph: String?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(color),
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
private fun DayRow(days: List<com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDay>) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        days.forEach { day ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    day.dayOfMonth.toString(),
                    fontSize = 8.5.sp,
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
                    Modifier
                        .size(17.dp)
                        .clip(CircleShape)
                        .background(bg),
                    contentAlignment = Alignment.Center
                ) {
                    if (glyph.isNotEmpty()) {
                        Text(glyph, fontSize = 8.sp, color = glyphColor, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun Donut(pct: Int) {
    Box(Modifier.size(76.dp), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(Modifier.size(76.dp)) {
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$pct%", fontSize = 17.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
        }
    }
}
