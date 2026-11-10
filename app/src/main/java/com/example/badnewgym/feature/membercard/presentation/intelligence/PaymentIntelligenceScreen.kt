package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import java.util.Locale

/**
 * BAD GYM — PAYMENT INTELLIGENCE
 * 100% Pixel-Perfect 1:1 Implementation of Target Reference: `media_1794217986950_71037fae.jpg`
 * - 5 Sub-nav Stadium Pills: [ Overview ] [ History ] [ Plan & Changes ] [ Behaviour ] [ Analytics ]
 * - Unified 3-Column Payment Status Hero Card: (Paid Status | Gold Plan Details | Next Renewal & Countdown)
 * - 4 Stat Pods (1×4 Equal-Width Row: Lifetime Paid, Cycles, Discount, Outstanding)
 * - Two Split Side-by-Side Cards: (Payment Behaviour Donut + Breakdown | Plan History 4-step Timeline)
 * - Monthly Payment Trend (₹) Continuous Spline (Jan–Oct with April Dip & May Plan ↑ 1.5k Pill)
 * - Bottom Split Card: (AI Insight | Suggestion with Chevron)
 */
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
    val measurer = rememberTextMeasurer()

    var selectedSubTab by rememberSaveable { mutableIntStateOf(0) }
    val subTabs = listOf(
        SubTabItem("Overview", Icons.Filled.DateRange),
        SubTabItem("History", Icons.Filled.Schedule),
        SubTabItem("Plan", Icons.Filled.FactCheck),
        SubTabItem("Behaviour", Icons.Filled.Security),
        SubTabItem("Analytics", Icons.AutoMirrored.Filled.TrendingUp)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 1. Sub-navigation: a 3 + 2 grid so every label stays fully visible at
        // large type sizes instead of being hard-cut by a scroll viewport.
        Column(verticalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGapSm)) {
            subTabs.chunked(3).forEachIndexed { rowIndex, rowTabs ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IntelligenceDensity.widgetGapSm)
                ) {
                    rowTabs.forEachIndexed { colIndex, item ->
                        val index = rowIndex * 3 + colIndex
                        val isSelected = selectedSubTab == index
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(IntelligenceDensity.radiusPill))
                                .background(
                                    if (isSelected) Brush.horizontalGradient(listOf(NeuPalette.violet, NeuPalette.violetDeep))
                                    else Brush.horizontalGradient(listOf(Color.White, Color.White))
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) NeuPalette.violetDeep else NeuPalette.shadow.copy(alpha = 0.24f),
                                    shape = RoundedCornerShape(IntelligenceDensity.radiusPill)
                                )
                                .clickable { selectedSubTab = index }
                                .padding(vertical = 8.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                item.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else NeuPalette.violet,
                                modifier = Modifier.size(IntelligenceDensity.iconSmall)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.title,
                                fontSize = IntelligenceTypography.sizeSmall,
                                fontWeight = IntelligenceTypography.weightBody,
                                color = if (isSelected) Color.White else c.ink,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    // Keep the last row balanced when it holds fewer cells.
                    repeat(3 - rowTabs.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
            }
        }
        // Sub-Tab Content
        when (selectedSubTab) {
            0 -> PaymentOverviewDittoView(
                measurer = measurer,
                c = c,
                onViewDueClick = onViewDueClick,
                onReceiptClick = onReceiptClick
            )
            1 -> PaymentHistorySubView(onReceiptClick = onReceiptClick, c = c)
            2 -> PaymentPlanAndChangesSubView(c = c)
            3 -> PaymentBehaviourSubView(c = c)
            4 -> PaymentAnalyticsSubView(c = c)
        }
    }
}

private data class SubTabItem(val title: String, val icon: ImageVector)

/**
 * 01. Payment Overview View — Ditto Clone of media_1794217986950_71037fae.jpg
 */
@Composable
private fun PaymentOverviewDittoView(
    measurer: TextMeasurer,
    c: IntelligenceColors,
    onViewDueClick: () -> Unit,
    onReceiptClick: () -> Unit
) {
    // 2. Unified 3-Column Payment Status Hero Card
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neuRaised(radius = 18.dp, depth = 6.dp, blur = 16.dp)
            
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column 1: Payment Status Paid
            Row(
                modifier = Modifier.weight(1.3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(NeuPalette.green),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text("Payment Status", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft)
                    Text("Paid", fontSize = IntelligenceTypography.sizeMetric, fontWeight = IntelligenceTypography.weightValue, color = NeuPalette.green, letterSpacing = (-0.5).sp)
                    Text("Valid till 07 Nov 2026", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft)
                }
            }

            // Divider 1
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(46.dp)
                    .background(NeuPalette.violetWash)
            )

            // Column 2: Gold Plan details
            Column(
                modifier = Modifier
                    .weight(0.95f)
                    .padding(horizontal = 6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(13.dp))
                    Text("Gold Plan", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = c.ink)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(12.dp))
                    Text("30 Days", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("₹1,200", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = NeuPalette.violet)
                }
            }

            // Divider 2
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(38.dp)
                    .background(NeuPalette.shadowSoft)
            )

            // Column 3: Next Renewal & Countdown
            Column(
                modifier = Modifier
                    .weight(1.05f)
                    .padding(start = 6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(12.dp))
                    Text("Next Renewal", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft)
                }
                Text("07 Nov 2026", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = c.ink)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(NeuPalette.green)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("8 Days Left", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = Color.White)
                }
            }
        }
    }

    // 3. 4 Key Stat Pods (1×4 Equal-Width Row)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        StatPodItem(
            icon = Icons.Filled.AccountBalanceWallet,
            iconTint = NeuPalette.green,
            iconBg = NeuPalette.greenWash,
            value = "₹14,400",
            label = "Lifetime Paid",
            c = c,
            modifier = Modifier.weight(1f)
        )
        StatPodItem(
            icon = Icons.Filled.Layers,
            iconTint = NeuPalette.violet,
            iconBg = NeuPalette.violetWash,
            value = "12",
            label = "Total Cycles",
            c = c,
            modifier = Modifier.weight(1f)
        )
        StatPodItem(
            icon = Icons.Filled.Star,
            iconTint = NeuPalette.amber,
            iconBg = NeuPalette.amberWash,
            value = "₹1,200",
            label = "Total Discount",
            c = c,
            modifier = Modifier.weight(1f)
        )
        StatPodItem(
            icon = Icons.Filled.Autorenew,
            iconTint = NeuPalette.violet,
            iconBg = NeuPalette.violetWash,
            value = "0",
            label = "Outstanding",
            c = c,
            modifier = Modifier.weight(1f)
        )
    }

    // 4. Two Split Cards Side-by-Side: Payment Behaviour & Plan History
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Left Card: Payment Behaviour
        Box(
            modifier = Modifier
                .weight(1.05f)
                .neuRaised(radius = 16.dp, depth = 6.dp, blur = 16.dp)
                
                .padding(10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(NeuPalette.violetWash),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.FactCheck, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(12.dp))
                    }
                    Text("Payment Behaviour", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightTitle, color = c.ink)
                }

                // Middle: Donut + Breakdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Donut Chart
                    Box(
                        modifier = Modifier.size(78.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeW = 7.dp.toPx()
                            val arcSize = size.minDimension - strokeW
                            val topLeft = Offset(strokeW / 2f, strokeW / 2f)
                            // Track
                            drawArc(
                                color = NeuPalette.surface,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(arcSize, arcSize),
                                style = Stroke(strokeW, cap = StrokeCap.Round)
                            )
                            // Emerald Progress
                            drawArc(
                                brush = Brush.sweepGradient(listOf(NeuPalette.green, NeuPalette.greenBright, NeuPalette.green)),
                                startAngle = -90f,
                                sweepAngle = 360f * 0.86f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(arcSize, arcSize),
                                style = Stroke(strokeW, cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("86", fontSize = IntelligenceTypography.sizeMetric, fontWeight = IntelligenceTypography.weightValue, color = c.ink)
                            Text("Reliability", fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightValue, color = NeuPalette.violet)
                        }
                    }

                    // Breakdown Bars
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        BehaviourBarItem("On Time", 0.67f, "8 (67%)", NeuPalette.green)
                        BehaviourBarItem("Early", 0.17f, "2 (17%)", NeuPalette.sky)
                        BehaviourBarItem("Late", 0.17f, "2 (17%)", NeuPalette.amber)
                    }
                }

                // Divider
                Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(NeuPalette.surface))

                // Bottom 3 mini chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiniBadge("📅 3d", "Avg. Late", NeuPalette.violet)
                    MiniBadge("⚡ 5d", "Avg. Early", NeuPalette.amber)
                    MiniBadge("⚠️ 0", "Overdue", NeuPalette.coral)
                }
            }
        }

        // Right Card: Plan History
        Box(
            modifier = Modifier
                .weight(1f)
                .neuRaised(radius = 16.dp, depth = 6.dp, blur = 16.dp)
                
                .padding(10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(NeuPalette.violetWash),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.CreditCard, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(12.dp))
                    }
                    Text("Plan History", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightTitle, color = c.ink)
                }

                // Timeline List
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    TimelineItem("May 2026", "Plan Upgrade", "Silver → Gold", NeuPalette.green, isRing = false)
                    TimelineItem("Mar 2026", "Late Payment", "Paid after 3 days", NeuPalette.amber, isRing = false)
                    TimelineItem("Apr 2026", "Membership Gap", "No active plan", NeuPalette.faint, isRing = true)
                    TimelineItem("Jan 2026", "Joined Gym", "Gold Plan (₹1,200)", NeuPalette.green, isRing = false, isLast = true)
                }
            }
        }
    }

    // 5. Monthly Payment Trend (₹) Continuous Spline
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neuRaised(radius = 18.dp, depth = 6.dp, blur = 16.dp)
            
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeuPalette.violetWash),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(14.dp))
                    }
                    Text("Monthly Payment Trend (₹)", fontSize = IntelligenceTypography.sizeCardTitle, fontWeight = IntelligenceTypography.weightTitle, color = c.ink)
                }
            }

            // Spline Canvas
            MonthlySplineChart(measurer = measurer, c = c)
        }
    }

    // 6. Bottom Split Card: AI Insight & Suggestion
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neuRaised(radius = 18.dp, depth = 6.dp, blur = 16.dp)
            
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Half: AI Insight
            Row(
                modifier = Modifier.weight(1.05f),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeuPalette.violet),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("AI Insight", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightTitle, color = NeuPalette.violetDeep)
                    Text(
                        "Payment reliability is High (86%). You usually pay on time. Next renewal on 07 Nov. 👍",
                        fontSize = IntelligenceTypography.sizeMicro,
                        fontWeight = IntelligenceTypography.weightBody,
                        color = c.ink,
                        lineHeight = 12.5.sp
                    )
                }
            }

            // Vertical Hairline Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(NeuPalette.violetWash)
            )

            // Right Half: Suggestion
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuPalette.amberWash),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = NeuPalette.amberDeep, modifier = Modifier.size(17.dp))
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Suggestion", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightTitle, color = c.ink)
                        Text("• Renew 1–3 days early to keep 100% reliability.", fontSize = IntelligenceTypography.sizeMicro, color = NeuPalette.inkSoft, lineHeight = 10.sp)
                        Text("• Consider 3-Month Plan & save 10%.", fontSize = IntelligenceTypography.sizeMicro, color = NeuPalette.inkSoft, lineHeight = 10.sp)
                    }
                }
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(NeuPalette.violetWash)
                        .clickable { onReceiptClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = NeuPalette.violet,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }
}

// ---------------- Helper Components for Ditto Match ----------------

@Composable
private fun StatPodItem(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    value: String,
    label: String,
    c: IntelligenceColors,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .neuRaised(radius = 14.dp, depth = 6.dp, blur = 16.dp)
            
            .padding(vertical = 6.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(14.dp))
            }
            Text(value, fontSize = IntelligenceTypography.sizeCardTitle, fontWeight = IntelligenceTypography.weightValue, color = c.ink, maxLines = 1)
            Text(label, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft, maxLines = 1)
        }
    }
}

@Composable
private fun BehaviourBarItem(
    title: String,
    fraction: Float,
    label: String,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$title   $label",
                fontSize = IntelligenceTypography.sizeSmall,
                fontWeight = IntelligenceTypography.weightBody,
                color = NeuPalette.ink,
                maxLines = 2,
                lineHeight = 13.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(50))
                .background(NeuPalette.shadowSoft)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}

@Composable
private fun MiniBadge(top: String, sub: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(top, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = color)
        Text(sub, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft)
    }
}

@Composable
private fun TimelineItem(
    date: String,
    title: String,
    sub: String,
    dotColor: Color,
    isRing: Boolean = false,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isRing) Color.White else dotColor)
                    .border(if (isRing) 1.5.dp else 0.dp, dotColor, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(14.dp)
                        .background(NeuPalette.shadowSoft)
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    title,
                    fontSize = IntelligenceTypography.sizeSmall,
                    fontWeight = IntelligenceTypography.weightValue,
                    color = NeuPalette.ink,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    date,
                    fontSize = IntelligenceTypography.sizeSmall,
                    fontWeight = IntelligenceTypography.weightBody,
                    color = NeuPalette.inkSoft,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
            Text(
                sub,
                fontSize = IntelligenceTypography.sizeMicro,
                fontWeight = IntelligenceTypography.weightBody,
                color = NeuPalette.muted,
                maxLines = 1
            )
        }
    }
}

/**
 * Monthly Spline Chart with Y-Axis (1.5k to 0), Jan-Oct X-Axis,
 * April Dip to 0, and May Plan ↑ 1.5k Pill
 */
@Composable
private fun MonthlySplineChart(measurer: TextMeasurer, c: IntelligenceColors) {
    Canvas(modifier = Modifier.fillMaxWidth().height(76.dp)) {
        val leftPad = 34.dp.toPx()
        val rightPad = 22.dp.toPx()
        val topPad = 20.dp.toPx()
        val bottomPad = 18.dp.toPx()
        val plotW = size.width - leftPad - rightPad
        val plotH = size.height - topPad - bottomPad

        // Grid lines (1.5k, 1.2k, 1.0k, 0.5k, 0)
        val yLabels = listOf("1.5k", "0")
        val yVals = listOf(1500f, 0f)
        val maxVal = 1500f
        val axisStyle = TextStyle(fontSize = IntelligenceTypography.sizeSmall, color = NeuPalette.inkSoft, fontWeight = IntelligenceTypography.weightBody)

        yVals.forEachIndexed { index, v ->
            val y = topPad + (1f - v / maxVal) * plotH
            drawLine(
                color = NeuPalette.shadowSoft,
                start = Offset(leftPad, y),
                end = Offset(size.width - rightPad, y),
                strokeWidth = 0.8.dp.toPx()
            )
            val measured = measurer.measure(yLabels[index], axisStyle)
            drawText(
                measurer,
                yLabels[index],
                Offset(leftPad - measured.size.width - 4.dp.toPx(), y - measured.size.height / 2f),
                style = axisStyle
            )
        }

        // Data points: Jan(1.2k), Feb(1.2k), Mar(1.2k), Apr(0), May(1.5k), Jun(1.5k), Jul(1.2k), Aug(1.2k), Sep(1.2k), Oct(1.2k)
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct")
        val points = listOf(1200f, 1200f, 1200f, 0f, 1500f, 1500f, 1200f, 1200f, 1200f, 1200f)
        val n = months.size
        val stepX = plotW / (n - 1)

        val coords = points.mapIndexed { i, rupees ->
            val x = leftPad + i * stepX
            val y = topPad + (1f - rupees / maxVal) * plotH
            Offset(x, y)
        }

        // Draw Spline Line
        val path = Path().apply {
            moveTo(coords.first().x, coords.first().y)
            for (i in 0 until coords.size - 1) {
                val p0 = coords[i]
                val p1 = coords[i + 1]
                val midX = (p0.x + p1.x) / 2f
                cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
            }
        }

        // Shaded Area beneath spline
        val areaPath = Path().apply {
            addPath(path)
            lineTo(coords.last().x, topPad + plotH)
            lineTo(coords.first().x, topPad + plotH)
            close()
        }
        drawPath(
            path = areaPath,
            brush = Brush.verticalGradient(
                colors = listOf(NeuPalette.violet.copy(alpha = 0.35f), NeuPalette.sky.copy(alpha = 0.08f), Color.Transparent),
                startY = topPad,
                endY = topPad + plotH
            )
        )

        // Draw Line Stroke
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(listOf(NeuPalette.violet, NeuPalette.violet, NeuPalette.green)),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw Points & Floating Labels
        val dotValStyle = TextStyle(fontSize = IntelligenceTypography.sizeMicro, color = NeuPalette.ink, fontWeight = IntelligenceTypography.weightBody)
        val redStyle = TextStyle(fontSize = IntelligenceTypography.sizeMicro, color = NeuPalette.coral, fontWeight = IntelligenceTypography.weightValue)

        coords.forEachIndexed { i, pt ->
            val rupees = points[i]
            val isApr = i == 3 // Apr = 0
            val isMay = i == 4 // May = 1.5k
            val isOct = i == 9 // Oct = Current

            // Outer glow circle for Oct
            if (isOct) {
                drawCircle(color = NeuPalette.green.copy(alpha = 0.3f), radius = 6.dp.toPx(), center = pt)
                drawCircle(color = NeuPalette.green, radius = 3.5.dp.toPx(), center = pt)
                drawCircle(color = Color.White, radius = 1.5.dp.toPx(), center = pt)
            } else if (isApr) {
                drawCircle(color = NeuPalette.coral, radius = 3.dp.toPx(), center = pt)
                drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = pt)
            } else {
                drawCircle(color = NeuPalette.violet, radius = 2.8.dp.toPx(), center = pt)
                drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = pt)
            }

            // Month text below
            val mText = months[i]
            val mMeasured = measurer.measure(mText, axisStyle)
            drawText(measurer, mText, Offset(pt.x - mMeasured.size.width / 2f, topPad + plotH + 4.dp.toPx()), style = axisStyle)

            // Top Value Label
            if (isApr) {
                val measured = measurer.measure("0", redStyle)
                drawText(measurer, "0", Offset(pt.x - measured.size.width / 2f, pt.y - measured.size.height - 2.dp.toPx()), style = redStyle)
            } else if (isOct) {
                val label = "${(rupees / 1000f).toString().take(3)}k"
                val measured = measurer.measure(label, dotValStyle)
                drawText(measurer, label, Offset(pt.x - measured.size.width / 2f, pt.y - measured.size.height - 3.dp.toPx()), style = dotValStyle)
            } else if (isMay) {
                // May Plan ↑ 1.5k pill badge: sized to its text so nothing is clipped.
                val pillStyle = TextStyle(fontSize = IntelligenceTypography.sizeMicro, color = Color.White, fontWeight = IntelligenceTypography.weightBody)
                val pText = "Plan ↑ 1.5k"
                val pMeasured = measurer.measure(pText, pillStyle)
                val pillW = pMeasured.size.width + 10.dp.toPx()
                val pillH = pMeasured.size.height + 4.dp.toPx()
                val pillLeft = (pt.x - pillW / 2f).coerceIn(2.dp.toPx(), size.width - pillW - 2.dp.toPx())
                val pillTop = pt.y - pillH - 4.dp.toPx()
                drawRoundRect(
                    color = NeuPalette.violet,
                    topLeft = Offset(pillLeft, pillTop),
                    size = Size(pillW, pillH),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(50f, 50f)
                )
                drawText(measurer, pText, Offset(pillLeft + 5.dp.toPx(), pillTop + 2.dp.toPx()), style = pillStyle)
            }
        }
    }
}

/**
 * 02. Payment History Sub-Tab
 */
@Composable
private fun PaymentHistorySubView(onReceiptClick: () -> Unit, c: IntelligenceColors) {
    val history = listOf(
        HistoryTx("Cycle 12", "16 Oct 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", NeuPalette.green, "Ref: BG-99214"),
        HistoryTx("Cycle 11", "16 Sep 2026", "Gold Plan (30 Days)", "₹1,200", "⚡ 2d Early", NeuPalette.sky, "Ref: BG-88102"),
        HistoryTx("Cycle 10", "16 Aug 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", NeuPalette.green, "Ref: BG-77491"),
        HistoryTx("Cycle 09", "16 Jul 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", NeuPalette.green, "Ref: BG-66230"),
        HistoryTx("Cycle 08", "16 Jun 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", NeuPalette.green, "Ref: BG-55819"),
        HistoryTx("Cycle 07", "16 May 2026", "Annual Plan Upgrade", "₹1,800", "👑 Upgraded", NeuPalette.violet, "Ref: BG-44912"),
        HistoryTx("Cycle 06", "16 Apr 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", NeuPalette.green, "Ref: BG-33821"),
        HistoryTx("Cycle 05", "16 Mar 2026", "Gold Plan (30 Days)", "₹1,200", "🕒 3d Late", NeuPalette.amber, "Ref: BG-22194"),
        HistoryTx("Cycle 04", "16 Feb 2026", "Silver Plan (30 Days)", "₹800", "✔ On Time", NeuPalette.green, "Ref: BG-11083"),
        HistoryTx("Cycle 03", "16 Jan 2026", "Joined BAD GYM (Silver)", "₹800", "🎉 Joined", NeuPalette.violet, "Ref: BG-00912")
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        history.forEach { tx ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neuRaised(radius = 14.dp, depth = 6.dp, blur = 16.dp)
                    
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(tx.cycle, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = c.ink)
                            Text("•", fontSize = IntelligenceTypography.sizeSmall, color = NeuPalette.shadowSoft)
                            Text(tx.date, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.muted)
                        }
                        Text(tx.plan, fontSize = IntelligenceTypography.sizeMicro, fontWeight = FontWeight.Medium, color = NeuPalette.inkSoft)
                        Text(tx.ref, fontSize = IntelligenceTypography.sizeMicro, color = NeuPalette.faint)
                    }
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(tx.amount, fontSize = IntelligenceTypography.sizeCardTitle, fontWeight = IntelligenceTypography.weightValue, color = c.ink)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(tx.statusColor.copy(alpha = 0.14f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(tx.status, fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = tx.statusColor)
                        }
                    }
                }
            }
        }
    }
}

private data class HistoryTx(
    val cycle: String,
    val date: String,
    val plan: String,
    val amount: String,
    val status: String,
    val statusColor: Color,
    val ref: String
)

/** 03. Plan & Changes Sub-Tab */
@Composable
private fun PaymentPlanAndChangesSubView(c: IntelligenceColors) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neuRaised(radius = 18.dp, depth = 6.dp, blur = 16.dp)
            
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("💎 Gold Plan (30 Days)", fontSize = IntelligenceTypography.sizeMetric, fontWeight = IntelligenceTypography.weightValue, color = c.ink)
            Text("Price: ₹1,200 / Month (Auto-Renews 07 Nov 2026)", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.violet)

            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(NeuPalette.violetWash))

            Text("Included Benefits:", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = c.ink)
            listOf(
                "✔ Full Gym & Strength Floor Access",
                "✔ Cardio & High-Intensity HIIT Zone",
                "✔ Dedicated Locker & Hot Shower Access",
                "✔ Free Hydration & Electrolyte Station",
                "✔ Steam & Sauna Access (2 Days / Week)",
                "✔ Monthly 1-on-1 Trainer Assessment"
            ).forEach {
                Text(it, fontSize = IntelligenceTypography.sizeSmall, fontWeight = FontWeight.Medium, color = NeuPalette.inkSoft)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Platinum Upgrade Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeuPalette.amberWash.copy(alpha = 0.5f))
                    .border(1.dp, NeuPalette.amber.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("👑 Platinum VIP Upgrade", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightTitle, color = NeuPalette.amberDeep)
                        Text("Unlimited Sauna + Daily Trainer (₹2,200/mo)", fontSize = IntelligenceTypography.sizeMicro, color = NeuPalette.amberDeep)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Brush.horizontalGradient(listOf(NeuPalette.amber, NeuPalette.amberDeep)))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Upgrade ▸", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = Color.White)
                    }
                }
            }
        }
    }
}

/** 04. Behaviour Sub-Tab */
@Composable
private fun PaymentBehaviourSubView(c: IntelligenceColors) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neuRaised(radius = 18.dp, depth = 6.dp, blur = 16.dp)
            
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Payment Reliability Score: 86 / 100", fontSize = IntelligenceTypography.sizeCardTitle, fontWeight = IntelligenceTypography.weightValue, color = c.ink)
            Text("Punctuality Rating: High (Grade A+)", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.green)

            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(NeuPalette.violetWash))

            BehaviourDetailRow("On-Time Settlement Rate", "83.3% (10/12 cycles)", NeuPalette.green)
            BehaviourDetailRow("Consecutive Paid Cycles", "12 Cycles (No Lapses)", NeuPalette.green)
            BehaviourDetailRow("Avg. Advance Payment", "+2.4 Days Early", NeuPalette.sky)
            BehaviourDetailRow("Preferred Mode", "UPI (83.3%)", NeuPalette.violet)
            BehaviourDetailRow("Default / Overdue Risk", "Very Low (< 3%)", NeuPalette.green)
        }
    }
}

@Composable
private fun BehaviourDetailRow(title: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = IntelligenceTypography.sizeSmall, fontWeight = FontWeight.Medium, color = NeuPalette.inkSoft)
        Text(value, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = color)
    }
}

/** 05. Analytics Sub-Tab */
@Composable
private fun PaymentAnalyticsSubView(c: IntelligenceColors) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neuRaised(radius = 18.dp, depth = 6.dp, blur = 16.dp)
            
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Annual Financial Summary (2026)", fontSize = IntelligenceTypography.sizeCardTitle, fontWeight = IntelligenceTypography.weightValue, color = c.ink)
            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(NeuPalette.violetWash))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AnalyticsStatBox("Lifetime Paid", "₹14,400", NeuPalette.green, Modifier.weight(1f))
                AnalyticsStatBox("Loyalty Discounts", "₹1,200", NeuPalette.amber, Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AnalyticsStatBox("Avg Monthly Spend", "₹1,200", NeuPalette.violet, Modifier.weight(1f))
                AnalyticsStatBox("Next Due Date", "07 Nov", NeuPalette.sky, Modifier.weight(1f))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeuPalette.greenWash.copy(alpha = 0.6f))
                    .border(1.dp, NeuPalette.green.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Early Bird Renewal Bonus", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.greenDeep)
                        Text("Pay by 03 Nov to save ₹120 (10% Off)", fontSize = IntelligenceTypography.sizeMicro, color = NeuPalette.greenDeep)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(NeuPalette.green)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Pay ₹1,080", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsStatBox(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(NeuPalette.surface)
            .border(1.dp, NeuPalette.shadowSoft, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.muted)
            Text(value, fontSize = IntelligenceTypography.sizeCardTitle, fontWeight = IntelligenceTypography.weightValue, color = color)
        }
    }
}
