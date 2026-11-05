package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.intelligence.IntelligenceSeries
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import java.util.Locale

/** Payment tab of the Member Intelligence dashboard: renewal tiles, quick actions, trend chart and settlement summary. */
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
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        IntelligenceSectionCard(
            title = "Payment",
            icon = Icons.Filled.AccountBalanceWallet,
            subtitle = plan.planDuration,
            trailing = { HistoryChip(onClick = onHistoryClick) }
        ) {
            RenewalTilesRow(plan = plan)
            PaymentActionRow(
                onViewDueClick = onViewDueClick,
                onRemindClick = onRemindClick,
                onReceiptClick = onReceiptClick
            )
        }

        IntelligenceSectionCard(
            title = "Payment Trend",
            icon = Icons.Filled.ShowChart,
            trailing = {
                Text(
                    text = "Jan – Oct 2026",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = c.muted,
                    maxLines = 1
                )
            }
        ) {
            PaymentTrendChart()
        }

        IntelligenceNote("★ Jul Spike: Annual Plan Renewal")
        IntelligenceNote("Mar, Apr, Aug: Pre-paid cycle months — no invoice raised")

        PaymentSummaryRow(data = data)
    }
}

/** Accent-washed "History ›" pill that opens the member's payment history. */
@Composable
private fun HistoryChip(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(c.accentWash)
            .clickable { onClick() }
            .padding(horizontal = 9.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.AccessTime,
            contentDescription = null,
            tint = c.accentDeep,
            modifier = Modifier.size(11.dp)
        )
        Text(
            text = "History ›",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = c.accentDeep,
            maxLines = 1
        )
    }
}

/** Two equal renewal tiles: upcoming due amount and renewal date status. */
@Composable
private fun RenewalTilesRow(plan: PaymentPlanState, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        RenewalTile(
            icon = Icons.Filled.AccountBalanceWallet,
            title = "Upcoming Renewal",
            value = displayDueAmount(plan),
            valueSize = 16.sp,
            footer = "Due in ${plan.planDaysRemaining} Days",
            footerColor = c.accent,
            footerIcon = Icons.Filled.AccessTime,
            modifier = Modifier.weight(1f)
        )
        RenewalTile(
            icon = Icons.Filled.Event,
            title = "Renewal Date",
            value = plan.currentDueDate,
            valueSize = 13.sp,
            footer = "Active • ${plan.planDaysRemaining} Days Left",
            footerColor = c.good,
            footerIcon = Icons.Filled.CheckCircle,
            modifier = Modifier.weight(1f)
        )
    }
}

/** One white renewal info tile with an iconed label, a big value and a tone-colored footer. */
@Composable
private fun RenewalTile(
    icon: ImageVector,
    title: String,
    value: String,
    valueSize: TextUnit,
    footer: String,
    footerColor: Color,
    footerIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(c.cardFill)
            .border(1.dp, c.cardEdge, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, tint = c.accentDeep, modifier = Modifier.size(12.dp))
            Text(
                text = title,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = c.muted,
                maxLines = 1
            )
        }
        Text(
            text = value,
            fontSize = valueSize,
            fontWeight = FontWeight.Black,
            color = c.ink,
            maxLines = 1
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(footerIcon, contentDescription = null, tint = footerColor, modifier = Modifier.size(9.dp))
            Text(
                text = footer,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = footerColor,
                maxLines = 1
            )
        }
    }
}

/** Filled accent "View Due ›" button plus the Remind / Receipt / More soft tiles. */
@Composable
private fun PaymentActionRow(
    onViewDueClick: () -> Unit,
    onRemindClick: () -> Unit,
    onReceiptClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1.25f)
                .height(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(c.accent)
                .clickable { onViewDueClick() }
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "View Due ›",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = c.onAccent,
                maxLines = 1
            )
        }
        PaymentActionTile(Icons.Filled.Notifications, "Remind", Modifier.weight(1f), onClick = onRemindClick)
        PaymentActionTile(Icons.Filled.Receipt, "Receipt", Modifier.weight(1f), onClick = onReceiptClick)
        PaymentActionTile(Icons.Filled.MoreHoriz, "More", Modifier.weight(0.8f))
    }
}

/** One soft accent-washed action tile with an icon over a label. */
@Composable
private fun PaymentActionTile(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val c = intelligenceColors()
    Column(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(c.accentWash)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = c.accentDeep, modifier = Modifier.size(13.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = c.inkSoft,
            maxLines = 1
        )
    }
}

/** Canvas line/area chart of [IntelligenceSeries.paymentTrend] with a gold peak crown and dashed pre-paid gap connectors. */
@Composable
private fun PaymentTrendChart(modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    val points = remember { IntelligenceSeries.paymentTrend() }
    val maxY = 2000f
    val canvasHeight = 120.dp
    val headroom = 30.dp
    val bottomPad = 2.dp

    fun yDp(rupees: Int) = headroom + (canvasHeight - headroom - bottomPad) * (1f - rupees / maxY)

    BoxWithConstraints(modifier = modifier.fillMaxWidth().height(canvasHeight)) {
        val n = points.size
        val stepDp = maxWidth / n
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stepPx = size.width / n
            val plotTop = headroom.toPx()
            val plotBottom = size.height - bottomPad.toPx()
            val plotHeight = plotBottom - plotTop
            fun xFor(i: Int) = i * stepPx + stepPx / 2f
            fun yFor(rupees: Int) = plotTop + plotHeight * (1f - rupees / maxY)
            val coords = points.mapIndexed { i, p -> Offset(xFor(i), yFor(p.rupees ?: 0)) }

            // Solid runs of consecutive recorded months; faint dashed connectors span the pre-paid gaps.
            val runs = mutableListOf<List<Int>>()
            var current = mutableListOf<Int>()
            points.forEachIndexed { i, p ->
                if (p.rupees == null) {
                    if (current.isNotEmpty()) {
                        runs += current
                        current = mutableListOf()
                    }
                } else {
                    current += i
                }
            }
            if (current.isNotEmpty()) runs += current

            runs.forEach { run ->
                if (run.size >= 2) {
                    val runPts = run.map { coords[it] }
                    val line = smoothTrendPath(runPts)
                    val area = Path().apply {
                        addPath(line)
                        lineTo(runPts.last().x, plotBottom)
                        lineTo(runPts.first().x, plotBottom)
                        close()
                    }
                    drawPath(area, color = c.accentWash)
                    drawPath(
                        line,
                        color = c.accent,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
            for (r in 0 until runs.size - 1) {
                drawLine(
                    color = c.faint,
                    start = coords[runs[r].last()],
                    end = coords[runs[r + 1].first()],
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 5.dp.toPx()))
                )
            }
            points.forEachIndexed { i, p ->
                if (p.rupees == null) return@forEachIndexed
                val center = coords[i]
                drawCircle(c.cardFill, 4.dp.toPx(), center)
                drawCircle(if (p.isPeak) c.warn else c.accent, 2.5.dp.toPx(), center)
                if (p.isPeak) drawTrendCrown(center, c.warn)
            }
        }

        points.forEachIndexed { i, p ->
            if (p.rupees == null) return@forEachIndexed
            val isPeak = p.isPeak
            val labelY = if (isPeak) yDp(p.rupees) - 26.dp else yDp(p.rupees) - (if (i % 2 == 0) 16.dp else 22.dp)
            Box(
                modifier = Modifier
                    .offset(x = stepDp * i, y = labelY)
                    .width(stepDp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = rupeeLabel(p.rupees),
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPeak) c.warn else c.ink,
                    maxLines = 1
                )
            }
        }
    }
}

/** Draws a small gold crown marker hovering just above the peak point. */
private fun DrawScope.drawTrendCrown(center: Offset, color: Color) {
    val crownWidth = 10.dp.toPx()
    val crownHeight = 6.dp.toPx()
    val baseY = center.y - 6.dp.toPx()
    val topY = baseY - crownHeight
    val crown = Path().apply {
        moveTo(center.x - crownWidth / 2f, baseY)
        lineTo(center.x - crownWidth / 2f, topY)
        lineTo(center.x - crownWidth / 6f, baseY - crownHeight * 0.45f)
        lineTo(center.x, topY)
        lineTo(center.x + crownWidth / 6f, baseY - crownHeight * 0.45f)
        lineTo(center.x + crownWidth / 2f, topY)
        lineTo(center.x + crownWidth / 2f, baseY)
        close()
    }
    drawPath(crown, color = color)
}

/** Builds a smooth horizontal-tangent cubic path through the given points. */
private fun smoothTrendPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path
    path.moveTo(points[0].x, points[0].y)
    for (i in 1 until points.size) {
        val prev = points[i - 1]
        val cur = points[i]
        val midX = (prev.x + cur.x) / 2f
        path.cubicTo(midX, prev.y, midX, cur.y, cur.x, cur.y)
    }
    return path
}

/** Bottom settlement strip: total paid, unpaid balance and the last payment record. */
@Composable
private fun PaymentSummaryRow(data: MemberCardData, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    val plan = data.paymentPlan
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SummaryTile(
            icon = Icons.Filled.CheckCircle,
            tint = c.good,
            label = "Total Paid",
            value = plan.amountPaid,
            valueColor = c.good,
            modifier = Modifier.weight(1f)
        )
        SummaryTile(
            icon = Icons.Filled.Autorenew,
            tint = c.bad,
            label = "Unpaid Balance",
            value = displayDueAmount(plan),
            valueColor = c.bad,
            modifier = Modifier.weight(1f)
        )
        SummaryTile(
            icon = Icons.Filled.Receipt,
            tint = c.accentDeep,
            label = "Last Payment",
            value = plan.lastPaymentAmount,
            valueColor = c.ink,
            sub = plan.lastPaymentDate,
            modifier = Modifier.weight(1f)
        )
    }
}

/** One white summary tile with a tone-tinted icon, muted label and bold value plus an optional 8sp sub-line. */
@Composable
private fun SummaryTile(
    icon: ImageVector,
    tint: Color,
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
    sub: String? = null
) {
    val c = intelligenceColors()
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(c.cardFill)
            .border(1.dp, c.cardEdge, RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(12.dp))
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.SemiBold,
            color = c.muted,
            maxLines = 1
        )
        Text(
            text = value,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Black,
            color = valueColor,
            maxLines = 1
        )
        if (sub != null) {
            Text(
                text = sub,
                fontSize = 8.sp,
                fontWeight = FontWeight.Medium,
                color = c.muted,
                maxLines = 1
            )
        }
    }
}

/** Due display string with the same currentDueAmount → dueAlertAmount fallback the master payment card uses. */
private fun displayDueAmount(plan: PaymentPlanState): String =
    plan.currentDueAmount
        .takeIf { it.isNotBlank() && it != "--" }
        ?: plan.dueAlertAmount.takeIf { it.isNotBlank() && it != "--" }
        ?: "₹0"

/** Formats a chart point as a grouped rupee string (₹1,000). */
private fun rupeeLabel(rupees: Int): String = "₹" + String.format(Locale.US, "%,d", rupees)
