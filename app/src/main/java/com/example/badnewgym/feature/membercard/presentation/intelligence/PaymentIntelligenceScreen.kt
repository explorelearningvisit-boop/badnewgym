package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.intelligence.IntelligenceSeries
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import java.util.Locale

/** Payment tab: renewal tiles, action rail, continuous payment trend and the settlement summary. */
@Composable
fun PaymentIntelligenceScreen(
    data: MemberCardData,
    onHistoryClick: () -> Unit = {},
    onViewDueClick: () -> Unit = {},
    onReceiptClick: () -> Unit = {},
    onRemindClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val plan = data.paymentPlan
    val dueAmount = plan.currentDueAmount.ifBlank { plan.dueAlertAmount }
    val measurer = rememberTextMeasurer()

    Column(modifier = modifier.fillMaxWidth()) {
        IntelligenceSectionCard(
            title = "Payment",
            icon = Icons.Filled.AccountBalanceWallet,
            subtitle = plan.planDuration,
            trailing = { IntelligencePillOutlined("History ▸", onClick = onHistoryClick) }
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RenewalTile(
                    icon = Icons.Filled.AccountBalanceWallet,
                    iconTint = Color(0xFFF59E0B),
                    label = "Upcoming Renewal",
                    value = dueAmount,
                    valueSize = 18.sp,
                    footnote = "Due in ${plan.planDaysRemaining} Days",
                    footnoteIcon = Icons.Filled.Schedule,
                    footnoteTint = c.inkSoft,
                    modifier = Modifier.weight(1f)
                )
                RenewalTile(
                    icon = Icons.Filled.CalendarMonth,
                    iconTint = c.accent,
                    label = "Renewal Date",
                    value = plan.currentDueDate,
                    valueSize = 15.sp,
                    footnote = "Active • ${plan.planDaysRemaining} Days Left",
                    footnoteIcon = Icons.Filled.CheckCircle,
                    footnoteTint = c.good,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ViewDueButton(fraction = 1.25f, onClick = onViewDueClick)
                ActionPill(Icons.Filled.Notifications, "Remind", onRemindClick)
                ActionPill(Icons.Filled.Receipt, "Receipt", onReceiptClick)
                ActionPill(Icons.Filled.MoreHoriz, "More", null)
            }
        }

        IntelligenceSectionCard(
            title = "Payment Trend",
            icon = Icons.Filled.ShowChart,
            trailing = { IntelligencePillOutlined("Jan – Oct 2026 ▾") }
        ) {
            PaymentTrendChart(measurer = measurer)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                SpikeChip(
                    background = c.accentWash,
                    foreground = c.ink,
                    icon = Icons.Filled.Star,
                    iconTint = Color(0xFFF59E0B),
                    text = "Jul Spike: Annual Plan Renewal",
                    modifier = Modifier.weight(1.15f)
                )
                SpikeChip(
                    background = c.ink.copy(alpha = 0.05f),
                    foreground = c.inkSoft,
                    icon = null,
                    iconTint = c.inkSoft,
                    text = "Mar, Apr, Aug: Pre-paid Cycle",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SummaryTile(
                icon = Icons.Filled.Check,
                circleBg = c.good,
                label = "Total Paid",
                value = plan.amountPaid,
                valueColor = c.ink
            )
            SummaryTile(
                icon = Icons.Filled.Autorenew,
                circleBg = c.good,
                label = "Unpaid Balance",
                value = dueAmount,
                valueColor = c.bad
            )
            SummaryTile(
                icon = Icons.Filled.Receipt,
                circleBg = c.accentDeep,
                label = "Last Payment",
                value = plan.lastPaymentAmount,
                valueColor = c.ink,
                sub = plan.lastPaymentDate
            )
        }
    }
}

/** White renewal tile: tinted icon chip, muted label, bold value and an icon footnote. */
@Composable
private fun RenewalTile(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    valueSize: androidx.compose.ui.unit.TextUnit,
    footnote: String,
    footnoteIcon: ImageVector,
    footnoteTint: Color,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(c.cardFill)
            .border(1.2.dp, c.cardEdge, RoundedCornerShape(14.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(c.accentWash),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(17.dp))
        }
        Text(label, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
        Text(value, fontSize = valueSize, fontWeight = FontWeight.ExtraBold, color = c.ink, maxLines = 1)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(footnoteIcon, contentDescription = null, tint = footnoteTint, modifier = Modifier.size(11.dp))
            Text(footnote, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = footnoteTint, maxLines = 1)
        }
    }
}

/** Solid accent primary action pill with a trailing chevron. */
@Composable
private fun RowScope.ViewDueButton(fraction: Float, onClick: () -> Unit) {
    val c = intelligenceColors()
    Row(
        modifier = Modifier
            .weight(fraction)
            .height(40.dp)
            .clip(RoundedCornerShape(50))
            .background(c.accent)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("View Due ▸", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = c.onAccent)
    }
}

/** Secondary outlined action pill: accent icon plus ink label, centred. */
@Composable
private fun RowScope.ActionPill(icon: ImageVector, text: String, onClick: (() -> Unit)?) {
    val c = intelligenceColors()
    Row(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(50))
            .background(c.cardFill)
            .border(1.2.dp, c.cardEdge, RoundedCornerShape(50))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = c.accent, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = c.ink, maxLines = 1)
    }
}

/** Inline legend chip used under the trend chart (wash fill, optional leading icon). */
@Composable
private fun RowScope.SpikeChip(
    background: Color,
    foreground: Color,
    icon: ImageVector?,
    iconTint: Color,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(11.dp))
        }
        Text(text, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = foreground, maxLines = 1)
    }
}

/** Settlement summary tile: filled icon circle, muted label, bold value and optional date. */
@Composable
private fun RowScope.SummaryTile(
    icon: ImageVector,
    circleBg: Color,
    label: String,
    value: String,
    valueColor: Color,
    sub: String? = null
) {
    val c = intelligenceColors()
    Row(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(c.cardFill)
            .border(1.2.dp, c.cardEdge, RoundedCornerShape(14.dp))
            .padding(9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(circleBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
        Column {
            Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = valueColor, maxLines = 1)
            if (sub != null) {
                Text(sub, fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
            }
        }
    }
}

/** Continuous Jan–Oct rupee trend: gaps are linearly interpolated, only valued months are labelled. */
@Composable
private fun PaymentTrendChart(measurer: TextMeasurer) {
    val c = intelligenceColors()
    val trend = IntelligenceSeries.paymentTrend()
    Canvas(modifier = Modifier.fillMaxWidth().height(110.dp)) {
        val plotBottom = size.height - 26.dp.toPx()
        val topPad = 30.dp.toPx()
        val values = trend.map { it.rupees }
        val maxVal = values.filterNotNull().maxOrNull()?.coerceAtLeast(1) ?: 1
        val slotW = size.width / trend.size
        val xOf = { i: Int -> slotW * (i + 0.5f) }
        val yOf = { rupees: Float -> plotBottom - rupees / maxVal * (plotBottom - topPad) }

        val resolved = MutableList(trend.size) { i -> values[i]?.toFloat() ?: interpolate(values, i) }
        val line = Path().apply {
            resolved.forEachIndexed { i, v ->
                val pt = Offset(xOf(i), yOf(v))
                if (i == 0) moveTo(pt.x, pt.y) else lineTo(pt.x, pt.y)
            }
        }
        drawPath(line, c.accent, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
        val area = Path().apply {
            moveTo(xOf(0), plotBottom)
            resolved.forEachIndexed { i, v -> lineTo(xOf(i), yOf(v)) }
            lineTo(xOf(resolved.lastIndex), plotBottom)
            close()
        }
        drawPath(
            path = area,
            brush = Brush.verticalGradient(
                colors = listOf(c.accent.copy(alpha = 0.24f), c.accent.copy(alpha = 0.0f)),
                startY = topPad,
                endY = plotBottom
            )
        )

        val monthStyle = TextStyle(fontSize = 8.sp, color = c.muted, fontWeight = FontWeight.SemiBold)
        val valueStyle = TextStyle(fontSize = 9.sp, color = c.ink, fontWeight = FontWeight.ExtraBold)
        val peakStyle = TextStyle(fontSize = 9.sp, color = Color(0xFFD97706), fontWeight = FontWeight.ExtraBold)

        trend.forEachIndexed { i, point ->
            val x = xOf(i)
            val mw = measurer.measure(point.month, monthStyle).size.width
            drawText(measurer, point.month, Offset(x - mw / 2f, plotBottom + 4.dp.toPx()), style = monthStyle)
            val rupees = point.rupees
            if (rupees == null) return@forEachIndexed
            val y = yOf(rupees.toFloat())
            drawCircle(color = c.accent, radius = 2.dp.toPx(), center = Offset(x, y))
            val label = String.format(Locale.US, "₹%,d", rupees)
            val style = if (point.isPeak) peakStyle else valueStyle
            val measured = measurer.measure(label, style)
            val labelTop = y - 9.dp.toPx() - measured.size.height
            if (point.isPeak) drawCrown(centerX = x, baseY = labelTop - 1.dp.toPx())
            drawText(measurer, label, Offset(x - measured.size.width / 2f, labelTop), style = style)
        }
    }
}

/** Linear estimate for a pre-paid (null) month from its nearest valued neighbours. */
private fun interpolate(values: List<Int?>, index: Int): Float {
    var before = index - 1
    while (before >= 0 && values[before] == null) before--
    var after = index + 1
    while (after < values.size && values[after] == null) after++
    val a = if (before >= 0) values[before]!!.toFloat() else 0f
    val b = if (after < values.size) values[after]!!.toFloat() else a
    if (before < 0 || after >= values.size) return if (before < 0) b else a
    val t = (index - before).toFloat() / (after - before).toFloat()
    return a + (b - a) * t
}
