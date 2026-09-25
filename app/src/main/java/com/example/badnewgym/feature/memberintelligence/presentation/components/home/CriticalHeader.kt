package com.example.badnewgym.feature.memberintelligence.presentation.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import java.text.NumberFormat
import java.util.Locale

fun formatInr(amount: Double?): String {
    val value = amount ?: 0.0
    return "₹${NumberFormat.getIntegerInstance(Locale("en", "IN")).format(value.toLong())}"
}

@Composable
fun CriticalKpiStrip(
    snapshot: MemberSnapshot,
    modifier: Modifier = Modifier
) {
    val percent = snapshot.attendancePercent()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        MiniKpi(
            label = "ATTEND",
            value = "$percent%",
            sub = "${snapshot.attendance?.visits ?: 0}/${snapshot.attendance?.target ?: 0}",
            modifier = Modifier.weight(1f),
            accent = BADGymTheme.colors.success
        )
        MiniKpi(
            label = "DUE",
            value = formatInr(snapshot.payment?.totalOutstanding),
            sub = "${snapshot.payment?.overdueDays ?: 0}d overdue",
            modifier = Modifier.weight(1f),
            accent = BADGymTheme.colors.danger,
            danger = true
        )
        MiniKpi(
            label = "PLAN",
            value = "${snapshot.membership?.daysRemaining ?: 0}d",
            sub = if (snapshot.membership?.isActive == true) "Active" else "Inactive",
            modifier = Modifier.weight(1f),
            accent = BADGymTheme.colors.accent
        )
        MiniKpi(
            label = "LAST PAY",
            value = formatInr(snapshot.payment?.lastPaymentAmount),
            sub = snapshot.payment?.lastPaymentDate?.let { formatEventDate(it) } ?: "-",
            modifier = Modifier.weight(1f),
            accent = BADGymTheme.colors.info
        )
    }
}

@Composable
private fun MiniKpi(
    label: String,
    value: String,
    sub: String,
    modifier: Modifier = Modifier,
    accent: Color,
    danger: Boolean = false
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (danger) Color(0xFF7F1D1D) else BADGymTheme.colors.surfaceElevated
            )
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Text(label, color = accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.4.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(sub, color = Color(0xFF94A3B8), fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
fun PersistentMemberHeader(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CheckInHeader(occurredAt = currentEvent.occurredAt)
        Spacer(modifier = Modifier.height(if (compact) 8.dp else 12.dp))
        MemberHero(snapshot = snapshot, showStatusChip = true)
        Spacer(modifier = Modifier.height(if (compact) 8.dp else 12.dp))
        CriticalKpiStrip(snapshot = snapshot)
    }
}
