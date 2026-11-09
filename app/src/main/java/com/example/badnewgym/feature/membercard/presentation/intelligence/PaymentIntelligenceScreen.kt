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
        SubTabItem("Plan & Changes", Icons.Filled.FactCheck),
        SubTabItem("Behaviour", Icons.Filled.Security),
        SubTabItem("Analytics", Icons.AutoMirrored.Filled.TrendingUp)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // 1. 5-Pill Subnavigation Bar
        // Five labelled pills cannot fit 360-392dp, so the row scrolls and the trailing edge fades
        // instead of hard-cutting the fourth pill mid-word.
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                subTabs.forEachIndexed { index, item ->
                    val isSelected = selectedSubTab == index
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9)))
                                else Brush.horizontalGradient(listOf(Color.White, Color.White))
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color(0xFF7C3AED) else Color(0x338B5CF6),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable { selectedSubTab = index }
                            .padding(horizontal = 11.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                item.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Color(0xFF8B5CF6),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = item.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) Color.White else c.ink,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(22.dp)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, Color.White.copy(alpha = 0.92f))
                        )
                    )
            )
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
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(18.dp), ambientColor = Color(0x2E8B5CF6), spotColor = Color(0x2E8B5CF6))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0x2E8B5CF6), RoundedCornerShape(18.dp))
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text("Payment Status", fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                    Text("Paid", fontSize = 23.sp, fontWeight = FontWeight.Black, color = Color(0xFF10B981), letterSpacing = (-0.5).sp)
                    Text("Valid till 07 Nov 2026", fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF6B7280))
                }
            }

            // Divider 1
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(46.dp)
                    .background(Color(0xFFEDE9FE))
            )

            // Column 2: Gold Plan details
            Column(
                modifier = Modifier
                    .weight(0.95f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(13.dp))
                    Text("Gold Plan", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(12.dp))
                    Text("30 Days", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("₹", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
                    Text("₹1,200", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
                }
            }

            // Divider 2
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(46.dp)
                    .background(Color(0xFFEDE9FE))
            )

            // Column 3: Next Renewal & Countdown
            Column(
                modifier = Modifier
                    .weight(1.05f)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(12.dp))
                    Text("Next Renewal", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                }
                Text("07 Nov 2026", fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF10B981))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("8 Days Left", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
            iconTint = Color(0xFF10B981),
            iconBg = Color(0xFFDCFCE7),
            value = "₹14,400",
            label = "Lifetime Paid",
            c = c,
            modifier = Modifier.weight(1f)
        )
        StatPodItem(
            icon = Icons.Filled.Layers,
            iconTint = Color(0xFF8B5CF6),
            iconBg = Color(0xFFEDE9FE),
            value = "12",
            label = "Total Cycles",
            c = c,
            modifier = Modifier.weight(1f)
        )
        StatPodItem(
            icon = Icons.Filled.Star,
            iconTint = Color(0xFFF59E0B),
            iconBg = Color(0xFFFEF3C7),
            value = "₹1,200",
            label = "Total Discount",
            c = c,
            modifier = Modifier.weight(1f)
        )
        StatPodItem(
            icon = Icons.Filled.Autorenew,
            iconTint = Color(0xFF8B5CF6),
            iconBg = Color(0xFFEDE9FE),
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
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color(0x208B5CF6), spotColor = Color(0x208B5CF6))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color(0x288B5CF6), RoundedCornerShape(16.dp))
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
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.FactCheck, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(12.dp))
                    }
                    Text("Payment Behaviour", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
                }

                // Middle: Donut + Breakdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Donut Chart
                    Box(
                        modifier = Modifier.size(76.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeW = 7.dp.toPx()
                            val arcSize = size.minDimension - strokeW
                            val topLeft = Offset(strokeW / 2f, strokeW / 2f)
                            // Track
                            drawArc(
                                color = Color(0xFFF1F5F9),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(arcSize, arcSize),
                                style = Stroke(strokeW, cap = StrokeCap.Round)
                            )
                            // Emerald Progress
                            drawArc(
                                brush = Brush.sweepGradient(listOf(Color(0xFF10B981), Color(0xFF34D399), Color(0xFF10B981))),
                                startAngle = -90f,
                                sweepAngle = 360f * 0.86f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(arcSize, arcSize),
                                style = Stroke(strokeW, cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("86", fontSize = 17.sp, fontWeight = FontWeight.Black, color = c.ink)
                                Text("/100", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                            }
                            Text("Reliability", fontSize = 7.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF8B5CF6))
                        }
                    }

                    // Breakdown Bars
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        BehaviourBarItem("✔ On Time", 0.67f, "8 (67%)", Color(0xFF10B981))
                        BehaviourBarItem(">> Early", 0.17f, "2 (17%)", Color(0xFF0284C7))
                        BehaviourBarItem("🕒 Late", 0.17f, "2 (17%)", Color(0xFFF59E0B))
                    }
                }

                // Divider
                Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(Color(0xFFF1F5F9)))

                // Bottom 3 mini chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiniBadge("📅 3d", "Avg. Late", Color(0xFF8B5CF6))
                    MiniBadge("⚡ 5d", "Avg. Early", Color(0xFFF59E0B))
                    MiniBadge("⚠️ 0", "Overdue", Color(0xFFEF4444))
                }
            }
        }

        // Right Card: Plan History
        Box(
            modifier = Modifier
                .weight(1f)
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color(0x208B5CF6), spotColor = Color(0x208B5CF6))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color(0x288B5CF6), RoundedCornerShape(16.dp))
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
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.CreditCard, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(12.dp))
                    }
                    Text("Plan History", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
                }

                // Timeline List
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    TimelineItem("May 2026", "Plan Upgrade", "Silver → Gold", Color(0xFF10B981), isRing = false)
                    TimelineItem("Mar 2026", "Late Payment", "Paid after 3 days", Color(0xFFF59E0B), isRing = false)
                    TimelineItem("Apr 2026", "Membership Gap", "No active plan", Color(0xFF94A3B8), isRing = true)
                    TimelineItem("Jan 2026", "Joined Gym", "Gold Plan (₹1,200)", Color(0xFF10B981), isRing = false, isLast = true)
                }
            }
        }
    }

    // 5. Monthly Payment Trend (₹) Continuous Spline
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(18.dp), ambientColor = Color(0x248B5CF6), spotColor = Color(0x248B5CF6))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0x2E8B5CF6), RoundedCornerShape(18.dp))
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
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(14.dp))
                    }
                    Text("Monthly Payment Trend (₹)", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
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
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(18.dp), ambientColor = Color(0x208B5CF6), spotColor = Color(0x208B5CF6))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0x2E8B5CF6), RoundedCornerShape(18.dp))
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
                        .background(Color(0xFF8B5CF6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("AI Insight", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7C3AED))
                    Text(
                        "Payment reliability is High (86%). You usually pay on time. Next renewal on 07 Nov. 👍",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
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
                    .background(Color(0xFFEDE9FE))
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
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(17.dp))
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Suggestion", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
                        Text("• Renew 1–3 days early to keep 100% reliability.", fontSize = 8.sp, color = Color(0xFF475569), lineHeight = 10.sp)
                        Text("• Consider 3-Month Plan & save 10%.", fontSize = 8.sp, color = Color(0xFF475569), lineHeight = 10.sp)
                    }
                }
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE9FE))
                        .clickable { onReceiptClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF8B5CF6),
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
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(14.dp), ambientColor = Color(0x188B5CF6), spotColor = Color(0x188B5CF6))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0x248B5CF6), RoundedCornerShape(14.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
            }
            Text(value, fontSize = 12.5.sp, fontWeight = FontWeight.Black, color = c.ink)
            Text(label, fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), maxLines = 1)
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
            Text(title, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1B4B))
            Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1B4B))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.5.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF1F5F9))
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
        Text(top, fontSize = 8.5.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(sub, fontSize = 7.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF94A3B8))
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
                        .background(Color(0xFFE2E8F0))
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(date, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                Text("•", fontSize = 8.sp, color = Color(0xFFCBD5E1))
                Text(title, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1B4B))
            }
            Text(sub, fontSize = 8.sp, color = Color(0xFF64748B))
        }
    }
}

/**
 * Monthly Spline Chart with Y-Axis (1.5k to 0), Jan-Oct X-Axis,
 * April Dip to 0, and May Plan ↑ 1.5k Pill
 */
@Composable
private fun MonthlySplineChart(measurer: TextMeasurer, c: IntelligenceColors) {
    Canvas(modifier = Modifier.fillMaxWidth().height(96.dp)) {
        val leftPad = 26.dp.toPx()
        val rightPad = 10.dp.toPx()
        val topPad = 24.dp.toPx()
        val bottomPad = 20.dp.toPx()
        val plotW = size.width - leftPad - rightPad
        val plotH = size.height - topPad - bottomPad

        // Grid lines (1.5k, 1.2k, 1.0k, 0.5k, 0)
        val yLabels = listOf("1.5k", "1.2k", "1.0k", "0.5k", "0")
        val yVals = listOf(1500f, 1200f, 1000f, 500f, 0f)
        val maxVal = 1500f
        val axisStyle = TextStyle(fontSize = 7.5.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)

        yVals.forEachIndexed { index, v ->
            val y = topPad + (1f - v / maxVal) * plotH
            drawLine(
                color = Color(0xFFF1F5F9),
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
                colors = listOf(Color(0xFFC084FC).copy(alpha = 0.35f), Color(0xFF67E8F9).copy(alpha = 0.08f), Color.Transparent),
                startY = topPad,
                endY = topPad + plotH
            )
        )

        // Draw Line Stroke
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFA855F7), Color(0xFF10B981))),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw Points & Floating Labels
        val dotValStyle = TextStyle(fontSize = 7.5.sp, color = Color(0xFF1E1B4B), fontWeight = FontWeight.Bold)
        val redStyle = TextStyle(fontSize = 8.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Black)

        coords.forEachIndexed { i, pt ->
            val rupees = points[i]
            val isApr = i == 3 // Apr = 0
            val isMay = i == 4 // May = 1.5k
            val isOct = i == 9 // Oct = Current

            // Outer glow circle for Oct
            if (isOct) {
                drawCircle(color = Color(0xFF10B981).copy(alpha = 0.3f), radius = 6.dp.toPx(), center = pt)
                drawCircle(color = Color(0xFF10B981), radius = 3.5.dp.toPx(), center = pt)
                drawCircle(color = Color.White, radius = 1.5.dp.toPx(), center = pt)
            } else if (isApr) {
                drawCircle(color = Color(0xFFEF4444), radius = 3.dp.toPx(), center = pt)
                drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = pt)
            } else {
                drawCircle(color = Color(0xFF8B5CF6), radius = 2.8.dp.toPx(), center = pt)
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
            } else if (!isMay) {
                val label = "${(rupees / 1000f).toString().take(3)}k"
                val measured = measurer.measure(label, dotValStyle)
                drawText(measurer, label, Offset(pt.x - measured.size.width / 2f, pt.y - measured.size.height - 3.dp.toPx()), style = dotValStyle)
            } else {
                // May Plan ↑ 1.5k pill badge floating above
                val pillW = 46.dp.toPx()
                val pillH = 14.dp.toPx()
                val pillLeft = pt.x - pillW / 2f
                val pillTop = pt.y - pillH - 4.dp.toPx()
                drawRoundRect(
                    color = Color(0xFF8B5CF6),
                    topLeft = Offset(pillLeft, pillTop),
                    size = Size(pillW, pillH),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(50f, 50f)
                )
                val pillStyle = TextStyle(fontSize = 7.sp, color = Color.White, fontWeight = FontWeight.Bold)
                val pText = "Plan ↑ 1.5k"
                val pMeasured = measurer.measure(pText, pillStyle)
                drawText(measurer, pText, Offset(pt.x - pMeasured.size.width / 2f, pillTop + (pillH - pMeasured.size.height) / 2f), style = pillStyle)
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
        HistoryTx("Cycle 12", "16 Oct 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", Color(0xFF10B981), "Ref: BG-99214"),
        HistoryTx("Cycle 11", "16 Sep 2026", "Gold Plan (30 Days)", "₹1,200", "⚡ 2d Early", Color(0xFF0284C7), "Ref: BG-88102"),
        HistoryTx("Cycle 10", "16 Aug 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", Color(0xFF10B981), "Ref: BG-77491"),
        HistoryTx("Cycle 09", "16 Jul 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", Color(0xFF10B981), "Ref: BG-66230"),
        HistoryTx("Cycle 08", "16 Jun 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", Color(0xFF10B981), "Ref: BG-55819"),
        HistoryTx("Cycle 07", "16 May 2026", "Annual Plan Upgrade", "₹1,800", "👑 Upgraded", Color(0xFF8B5CF6), "Ref: BG-44912"),
        HistoryTx("Cycle 06", "16 Apr 2026", "Gold Plan (30 Days)", "₹1,200", "✔ On Time", Color(0xFF10B981), "Ref: BG-33821"),
        HistoryTx("Cycle 05", "16 Mar 2026", "Gold Plan (30 Days)", "₹1,200", "🕒 3d Late", Color(0xFFF59E0B), "Ref: BG-22194"),
        HistoryTx("Cycle 04", "16 Feb 2026", "Silver Plan (30 Days)", "₹800", "✔ On Time", Color(0xFF10B981), "Ref: BG-11083"),
        HistoryTx("Cycle 03", "16 Jan 2026", "Joined BAD GYM (Silver)", "₹800", "🎉 Joined", Color(0xFF8B5CF6), "Ref: BG-00912")
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        history.forEach { tx ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(14.dp), ambientColor = Color(0x158B5CF6), spotColor = Color(0x158B5CF6))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0x248B5CF6), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(tx.cycle, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = c.ink)
                            Text("•", fontSize = 10.sp, color = Color(0xFFCBD5E1))
                            Text(tx.date, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                        }
                        Text(tx.plan, fontSize = 9.5.sp, fontWeight = FontWeight.Medium, color = Color(0xFF475569))
                        Text(tx.ref, fontSize = 8.sp, color = Color(0xFF94A3B8))
                    }
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(tx.amount, fontSize = 14.sp, fontWeight = FontWeight.Black, color = c.ink)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(tx.statusColor.copy(alpha = 0.14f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(tx.status, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = tx.statusColor)
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
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(18.dp), ambientColor = Color(0x208B5CF6), spotColor = Color(0x208B5CF6))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0x2E8B5CF6), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("💎 Gold Plan (30 Days)", fontSize = 16.sp, fontWeight = FontWeight.Black, color = c.ink)
            Text("Price: ₹1,200 / Month (Auto-Renews 07 Nov 2026)", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF8B5CF6))

            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(Color(0xFFEDE9FE)))

            Text("Included Benefits:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = c.ink)
            listOf(
                "✔ Full Gym & Strength Floor Access",
                "✔ Cardio & High-Intensity HIIT Zone",
                "✔ Dedicated Locker & Hot Shower Access",
                "✔ Free Hydration & Electrolyte Station",
                "✔ Steam & Sauna Access (2 Days / Week)",
                "✔ Monthly 1-on-1 Trainer Assessment"
            ).forEach {
                Text(it, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Platinum Upgrade Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFEF3C7).copy(alpha = 0.5f))
                    .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("👑 Platinum VIP Upgrade", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF92400E))
                        Text("Unlimited Sauna + Daily Trainer (₹2,200/mo)", fontSize = 8.5.sp, color = Color(0xFFB45309))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFD97706))))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Upgrade ▸", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(18.dp), ambientColor = Color(0x208B5CF6), spotColor = Color(0x208B5CF6))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0x2E8B5CF6), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Payment Reliability Score: 86 / 100", fontSize = 15.sp, fontWeight = FontWeight.Black, color = c.ink)
            Text("Punctuality Rating: High (Grade A+)", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981))

            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(Color(0xFFEDE9FE)))

            BehaviourDetailRow("On-Time Settlement Rate", "83.3% (10/12 cycles)", Color(0xFF10B981))
            BehaviourDetailRow("Consecutive Paid Cycles", "12 Cycles (No Lapses)", Color(0xFF10B981))
            BehaviourDetailRow("Avg. Advance Payment", "+2.4 Days Early", Color(0xFF0284C7))
            BehaviourDetailRow("Preferred Mode", "UPI (83.3%)", Color(0xFF8B5CF6))
            BehaviourDetailRow("Default / Overdue Risk", "Very Low (< 3%)", Color(0xFF10B981))
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
        Text(title, fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = Color(0xFF475569))
        Text(value, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

/** 05. Analytics Sub-Tab */
@Composable
private fun PaymentAnalyticsSubView(c: IntelligenceColors) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(18.dp), ambientColor = Color(0x208B5CF6), spotColor = Color(0x208B5CF6))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0x2E8B5CF6), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Annual Financial Summary (2026)", fontSize = 15.sp, fontWeight = FontWeight.Black, color = c.ink)
            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(Color(0xFFEDE9FE)))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AnalyticsStatBox("Lifetime Paid", "₹14,400", Color(0xFF10B981), Modifier.weight(1f))
                AnalyticsStatBox("Loyalty Discounts", "₹1,200", Color(0xFFF59E0B), Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AnalyticsStatBox("Avg Monthly Spend", "₹1,200", Color(0xFF8B5CF6), Modifier.weight(1f))
                AnalyticsStatBox("Next Due Date", "07 Nov", Color(0xFF0284C7), Modifier.weight(1f))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFDCFCE7).copy(alpha = 0.6f))
                    .border(1.dp, Color(0xFF10B981).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Early Bird Renewal Bonus", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                        Text("Pay by 03 Nov to save ₹120 (10% Off)", fontSize = 8.5.sp, color = Color(0xFF047857))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF10B981))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Pay ₹1,080", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}
