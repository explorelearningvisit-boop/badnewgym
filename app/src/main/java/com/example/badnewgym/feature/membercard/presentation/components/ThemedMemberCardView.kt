package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.design.MemberCardTheme
import com.example.badnewgym.feature.membercard.design.MemberCardThemeManager
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

/**
 * BAD GYM Master Themed Member Intelligence Card & Detailed Analytics
 * Faithfully reproducing the master reference mobile design:
 * 1. Top preset switcher for all 8 canonical themes
 * 2. Hero Member Card (Avatar with mint halo & crown, quote "Better Than Yesterday", 4 chips)
 * 3. 2x2 Bento Intelligence Grid (Attendance, Workout Trend, Payment, Body Progress with 30-day charts and 3-stat footers)
 * 4. Detailed View Section with 4 tabs, dual overview chart, donut ring gauge, streak, and check-in metadata
 */
@Composable
fun ThemedMemberIntelligenceCard(
    data: MemberCardData,
    initialTheme: MemberCardTheme = MemberCardTheme.EMERALD_GLASS_MODERN,
    onMenuClick: () -> Unit = {},
    onCardClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showThemePicker by remember { mutableStateOf(false) }
    val selectedTheme = MemberCardThemeManager.activeTheme
    val palette = MemberCardThemeManager.getPalette(selectedTheme)
    var detailedTab by remember { mutableStateOf("Attendance") }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {


        // =========================================================================
        // 1. HERO MEMBER CARD (Glassmorphism with Halo, Quote, and 4 Stat Chips)
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(palette.cardBackground)
                .border(
                    width = 1.5.dp,
                    brush = palette.cardBorder,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Row: Avatar + Middle Details + Right Quote Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Avatar with Cyan-Mint Sweep Gradient Halo & Crown Badge
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(78.dp)) {
                            drawCircle(
                                brush = palette.haloBrush,
                                radius = size.minDimension / 2f - 2f,
                                style = Stroke(width = 3.2.dp.toPx())
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.avatar_ashwini),
                            contentDescription = data.profile.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(66.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                        )

                        // Green Circular Badge with Gold Crown (Top-Right)
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 2.dp, y = (-2).dp)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👑", fontSize = 11.5.sp)
                        }
                    }

                    // Middle Details Column
                    Column(
                        modifier = Modifier
                            .weight(1.2f)
                            .padding(start = 10.dp, end = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Ashwini Singh",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = palette.textPrimary,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "BG305 | 23 yrs | Male",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = palette.textSecondary
                        )

                        // Active Member Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF10B981), Color(0xFF059669))
                                    )
                                )
                                .padding(horizontal = 8.dp, vertical = 2.5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Text(
                                text = "Active Member",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Prime Plan & 25 Days Left Bar
                        Row(
                            modifier = Modifier.padding(top = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "👑", fontSize = 10.sp)
                            Text(
                                text = "Prime Plan",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = if (palette.isDark) Color(0xFFFDE047) else Color(0xFFD97706)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "25 Days Left",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.textSecondary
                            )
                            Box(
                                modifier = Modifier
                                    .width(62.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(palette.progressBarTrack)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.62f)
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(palette.progressBarFill)
                                )
                            }
                        }
                    }

                    // Right Quote Pill: "Better Than Yesterday" 💪
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (palette.isDark) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.95f)
                            )
                            .border(
                                1.dp,
                                if (palette.isDark) Color.White.copy(alpha = 0.20f) else Color(0xFFE2E8F0),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column {
                                Text(
                                    text = "“Better",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (palette.isDark) Color.White else Color(0xFF475569)
                                )
                                Text(
                                    text = "Than",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (palette.isDark) Color.White else Color(0xFF475569)
                                )
                                Text(
                                    text = "Yesterday”",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (palette.isDark) Color.White else Color(0xFF475569)
                                )
                            }
                            Text(text = "💪", fontSize = 19.sp)
                        }
                    }
                }

                // Bottom Continuous Metric Compartments Strip (Matching Mockup 1:1)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (palette.isDark) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.88f))
                        .border(1.dp, if (palette.isDark) Color.White.copy(alpha = 0.20f) else Color.White, RoundedCornerShape(16.dp))
                        .padding(horizontal = 8.dp, vertical = 7.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 1. Height
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "🧍", fontSize = 15.sp)
                            Column {
                                Text(text = "Height", fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = if (palette.isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF64748B))
                                Text(text = "178 cm", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = if (palette.isDark) Color.White else Color(0xFF0F172A))
                            }
                        }

                        Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFCBD5E1).copy(alpha = 0.6f)))

                        // 2. Weight
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(1.05f).padding(start = 4.dp)
                        ) {
                            Text(text = "🧳", fontSize = 15.sp)
                            Column {
                                Text(text = "Weight", fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = if (palette.isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF64748B))
                                Text(text = "69.5 kg", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = if (palette.isDark) Color.White else Color(0xFF0F172A))
                            }
                        }

                        Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFCBD5E1).copy(alpha = 0.6f)))

                        // 3. BMI
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(0.95f).padding(start = 4.dp)
                        ) {
                            Text(text = "⚖️", fontSize = 15.sp)
                            Column {
                                Text(text = "BMI", fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = if (palette.isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF64748B))
                                Text(text = "21.9", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = if (palette.isDark) Color.White else Color(0xFF0F172A))
                            }
                        }

                        Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFCBD5E1).copy(alpha = 0.6f)))

                        // 4. Goal
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(1.3f).padding(start = 4.dp)
                        ) {
                            Text(text = "🎯", fontSize = 15.sp)
                            Column {
                                Text(text = "Goal", fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = if (palette.isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF64748B))
                                Text(text = "Lean Muscle", fontSize = 11.sp, fontWeight = FontWeight.Black, color = if (palette.isDark) Color.White else Color(0xFF0F172A), maxLines = 1, softWrap = false)
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // 2. 2x2 BENTO INTELLIGENCE CARDS GRID
        // =========================================================================
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: Attendance & Workout Trend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 1: Attendance (Green)
                RichBentoCard(
                    title = "Attendance",
                    value = "85%",
                    badgeText = "↑ 12%",
                    badgeBg = Color(0xFFDCFCE7),
                    badgeTextColor = Color(0xFF15803D),
                    subtitle = "18/21 Days",
                    background = palette.bentoAttendanceBg,
                    borderColor = palette.bentoAttendanceBorder,
                    icon = Icons.Default.CalendarToday,
                    iconBg = Color(0xFF10B981),
                    accentColor = Color(0xFF10B981),
                    titleColor = Color(0xFF064E3B),
                    valueColor = Color(0xFF064E3B),
                    isDark = palette.isDark,
                    chartColor = palette.chartAttendanceColor,
                    barHeights = listOf(0.35f, 0.50f, 0.40f, 0.70f, 0.55f, 0.45f, 0.80f, 0.60f, 0.75f, 0.50f, 0.65f, 0.85f, 0.70f, 0.60f, 0.90f, 0.75f, 0.80f, 0.65f, 0.85f, 0.70f, 0.95f, 0.80f, 0.75f, 0.90f),
                    xAxisLabels = listOf("1", "5", "10", "15", "20", "25", "30"),
                    footerStats = listOf(
                        BentoFooterStat("● Present", "18", Color(0xFF10B981)),
                        BentoFooterStat("● Absent", "3", Color(0xFFEF4444)),
                        BentoFooterStat("● Gym Off", "0", Color(0xFF94A3B8))
                    ),
                    modifier = Modifier.weight(1f),
                    onClick = { onCardClick("Attendance") }
                )

                // Card 2: Workout Trend (Cyan/Blue)
                RichBentoCard(
                    title = "Workout Trend",
                    value = "47 min",
                    badgeText = "↑ 8%",
                    badgeBg = Color(0xFFE0F2FE),
                    badgeTextColor = Color(0xFF0284C7),
                    subtitle = "Avg / Day",
                    background = palette.bentoWorkoutBg,
                    borderColor = palette.bentoWorkoutBorder,
                    icon = Icons.Default.FitnessCenter,
                    iconBg = Color(0xFF0284C7),
                    accentColor = Color(0xFF0284C7),
                    titleColor = Color(0xFF0369A1),
                    valueColor = Color(0xFF0369A1),
                    isDark = palette.isDark,
                    chartColor = Color(0xFF0284C7),
                    barHeights = listOf(0.40f, 0.60f, 0.45f, 0.75f, 0.50f, 0.80f, 0.65f, 0.70f, 0.55f, 0.85f, 0.70f, 0.90f, 0.60f, 0.75f, 0.85f, 0.70f, 0.95f, 0.80f, 0.65f, 0.85f, 0.75f, 0.90f, 0.85f, 0.95f),
                    xAxisLabels = listOf("1", "5", "15", "15", "20", "25", "30"),
                    footerStats = listOf(
                        BentoFooterStat("⏱️ Total Workouts", "18", Color(0xFF0284C7)),
                        BentoFooterStat("🏆 Best Day", "92 min", Color(0xFFF59E0B)),
                        BentoFooterStat("🔥 Streak", "12 Days", Color(0xFFEF4444))
                    ),
                    modifier = Modifier.weight(1f),
                    onClick = { onCardClick("Workout") }
                )
            }

            // Row 2: Payment & Body Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 3: Payment (Amber/Gold)
                RichBentoCard(
                    title = "Payment",
                    value = "₹2,800",
                    badgeText = "! Due",
                    badgeBg = Color(0xFFEF4444),
                    badgeTextColor = Color.White,
                    subtitle = "25 Days Left",
                    background = palette.bentoPaymentBg,
                    borderColor = palette.bentoPaymentBorder,
                    icon = Icons.Default.AccountBalanceWallet,
                    iconBg = Color(0xFFF59E0B),
                    accentColor = Color(0xFFF59E0B),
                    titleColor = Color(0xFF92400E),
                    valueColor = Color(0xFF92400E),
                    isDark = palette.isDark,
                    chartColor = Color(0xFFF59E0B),
                    barHeights = listOf(0.30f, 0.45f, 0.60f, 0.40f, 0.70f, 0.55f, 0.80f, 0.50f, 0.65f, 0.75f, 0.60f, 0.85f, 0.70f, 0.90f, 0.65f, 0.80f, 0.75f, 0.95f, 0.70f, 0.85f, 0.60f, 0.80f, 0.75f, 0.90f),
                    xAxisLabels = listOf("1", "5", "10", "15", "20", "25", "30"),
                    footerStats = listOf(
                        BentoFooterStat("● Paid", "₹12,600", Color(0xFF10B981)),
                        BentoFooterStat("📦 Pending", "₹2,800", Color(0xFFF59E0B)),
                        BentoFooterStat("🍇 Avg / Day", "₹420", Color(0xFFA855F7))
                    ),
                    modifier = Modifier.weight(1f),
                    onClick = { onCardClick("Payment") }
                )

                // Card 4: Body Progress (Purple/Magenta)
                RichBentoCard(
                    title = "Body Progress",
                    value = "69.5 kg",
                    badgeText = "↓ 0.7 kg",
                    badgeBg = Color(0xFFFCE7F3),
                    badgeTextColor = Color(0xFFBE185D),
                    subtitle = "Current Weight",
                    background = palette.bentoBodyBg,
                    borderColor = palette.bentoBodyBorder,
                    icon = Icons.Default.Person,
                    iconBg = Color(0xFFA855F7),
                    accentColor = Color(0xFFA855F7),
                    titleColor = Color(0xFF86198F),
                    valueColor = Color(0xFF86198F),
                    isDark = palette.isDark,
                    chartColor = Color(0xFFA855F7),
                    barHeights = listOf(0.35f, 0.50f, 0.45f, 0.65f, 0.55f, 0.75f, 0.60f, 0.70f, 0.80f, 0.65f, 0.85f, 0.70f, 0.90f, 0.75f, 0.80f, 0.85f, 0.70f, 0.95f, 0.80f, 0.75f, 0.85f, 0.90f, 0.80f, 0.88f),
                    xAxisLabels = listOf("1", "5", "10", "15", "20", "25", "30"),
                    footerStats = listOf(
                        BentoFooterStat("🎯 Plan", "Lean Muscle", Color(0xFFA855F7)),
                        BentoFooterStat("🎗️ Body Fat", "14.2%", Color(0xFFEC4899)),
                        BentoFooterStat("💪 Muscle", "+2.5 kg", Color(0xFF10B981))
                    ),
                    modifier = Modifier.weight(1f),
                    onClick = { onCardClick("BodyProgress") }
                )
            }
        }

        // =========================================================================
        // 3. DETAILED VIEW SECTION (Tabs + Dual Overview + Donut Gauge + Metadata)
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.95f))
                .border(1.5.dp, Color(0x66A7F3D0), RoundedCornerShape(24.dp))
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header: [ 📊 ] Detailed View | 📅 Last 30 Days ⌵
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        Text(
                            text = "Detailed View",
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }

                    // Last 30 Days Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFF1F5F9))
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(50.dp))
                            .padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Last 30 Days",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // 4-Segmented Control / Tab Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val tabs = listOf("Attendance", "Workout", "Payment", "Body Progress")
                    tabs.forEach { tab ->
                        val isTabSelected = detailedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50.dp))
                                .then(
                                    if (isTabSelected) {
                                        Modifier.background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFF00E676), Color(0xFF00A336))
                                            )
                                        )
                                    } else {
                                        Modifier.background(Color(0xFFF1F5F9))
                                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
                                    }
                                )
                                .clickable { detailedTab = tab }
                                .padding(vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                fontSize = 11.sp,
                                fontWeight = if (isTabSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isTabSelected) Color.White else Color(0xFF475569),
                                maxLines = 1
                            )
                        }
                    }
                }

                // Dynamic Tab Body (Attendance view matches mockup exactly)
                when (detailedTab) {
                    "Attendance" -> AttendanceDetailedCardContent()
                    "Workout" -> WorkoutDetailedCardContent()
                    "Payment" -> PaymentDetailedCardContent()
                    else -> BodyProgressDetailedCardContent()
                }
            }
        }

        // Modal Theme Picker Dialog (Shows all 8 presets with swatches and details)
        if (showThemePicker) {
            ThemePickerDialog(
                currentTheme = selectedTheme,
                onThemeSelected = {
                    MemberCardThemeManager.activeTheme = it
                    showThemePicker = false
                },
                onDismiss = { showThemePicker = false }
            )
        }
    }
}

/**
 * Attendance Detailed Content matching mockup:
 * - Left: Attendance Overview 85% 18/21 Days with 30-day bars & spline curve
 * - Right: Day Wise Status Donut Ring Gauge (85% Present) + Legend
 * - Streak & Average Row: 12 Days Current Streak + 4/5 Weekly Avg
 * - Footer: Last Check-In Today 06:12 AM | Location Near Gym | Method Face Recognition
 */
@Composable
fun AttendanceDetailedCardContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Dual Column: Overview Chart (55%) + Donut Gauge (45%)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Left Column: Overview Chart
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Attendance Overview",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "↑ 12%",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF15803D)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "85%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "18/21 Days",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF10B981),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 30-Day Dense Bar + Spline Chart (Matching Mockup 1:1)
                Canvas(modifier = Modifier.fillMaxWidth().height(55.dp)) {
                    val heights = listOf(0.35f, 0.50f, 0.40f, 0.70f, 0.55f, 0.45f, 0.80f, 0.60f, 0.75f, 0.50f, 0.65f, 0.85f, 0.70f, 0.60f, 0.90f, 0.75f, 0.80f, 0.65f, 0.85f, 0.70f, 0.95f, 0.80f, 0.75f, 0.90f)
                    val barWidth = 2.4.dp.toPx()
                    val gap = (size.width - (heights.size * barWidth)) / (heights.size - 1)
                    val p = Path()

                    heights.forEachIndexed { i, h ->
                        val x = i * (barWidth + gap)
                        val bh = size.height * h
                        val y = size.height - bh
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFF00E676), Color(0xFF00A336).copy(alpha = 0.25f))
                            ),
                            topLeft = Offset(x, y),
                            size = Size(barWidth, bh),
                            cornerRadius = CornerRadius(1.2.dp.toPx(), 1.2.dp.toPx())
                        )

                        val cx = x + barWidth / 2f
                        if (i == 0) p.moveTo(cx, y) else p.lineTo(cx, y)
                        drawCircle(Color.White, radius = 2.4.dp.toPx(), center = Offset(cx, y))
                        drawCircle(Color(0xFF00A336), radius = 1.2.dp.toPx(), center = Offset(cx, y))
                    }
                    drawPath(p, Color.White.copy(alpha = 0.95f), style = Stroke(1.8.dp.toPx()))
                }

                Spacer(modifier = Modifier.height(4.dp))

                // X-Axis Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("1", "5", "10", "15", "20", "25", "30").forEach { lbl ->
                        Text(text = lbl, fontSize = 8.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Right Column: Donut Gauge + Legend
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Day Wise Status",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Donut Circular Ring Gauge
                Box(
                    modifier = Modifier.size(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(70.dp)) {
                        val strokeW = 8.dp.toPx()
                        // Pale mint track
                        drawArc(
                            color = Color(0xFFE2E8F0),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(strokeW)
                        )
                        // Green arc (85% Present)
                        drawArc(
                            color = Color(0xFF10B981),
                            startAngle = -90f,
                            sweepAngle = 360f * 0.85f,
                            useCenter = false,
                            style = Stroke(strokeW)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "85%", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text(text = "Present", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Legend
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    LegendItem("Present", "18", Color(0xFF10B981))
                    LegendItem("Absent", "3", Color(0xFFEF4444))
                    LegendItem("Gym Off", "0", Color(0xFF94A3B8))
                }
            }
        }

        // Streak & Average Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "🔥", fontSize = 18.sp)
                Column {
                    Text(text = "12 Days", fontSize = 12.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "Current Streak", fontSize = 9.sp, color = Color(0xFF64748B))
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "📅", fontSize = 18.sp)
                Column {
                    Text(text = "4/5", fontSize = 12.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "Weekly Avg", fontSize = 9.sp, color = Color(0xFF64748B))
                }
            }
        }

        // Footer Metadata Strip: Last Check-In, Location, Method
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF1F5F9))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MetadataChip("🕒", "Last Check-In", "Today 06:12 AM")
            MetadataChip("📍", "Location", "Near Gym")
            MetadataChip("👁️", "Method", "Face Recognition")
        }
    }
}

@Composable
fun WorkoutDetailedCardContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F9FF))
                    .border(1.dp, Color(0xFFBAE6FD), RoundedCornerShape(16.dp))
                    .padding(10.dp)
            ) {
                Text(text = "Workout Trend Overview", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "47 min / day", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF0C4A6E))
                Spacer(modifier = Modifier.height(8.dp))
                Canvas(modifier = Modifier.fillMaxWidth().height(50.dp)) {
                    val pts = listOf(0.40f, 0.75f, 0.50f, 0.85f, 0.65f, 0.90f)
                    val step = size.width / (pts.size - 1)
                    val p = Path()
                    pts.forEachIndexed { i, f ->
                        val cx = i * step
                        val cy = size.height * (1f - f)
                        if (i == 0) p.moveTo(cx, cy) else p.lineTo(cx, cy)
                        drawCircle(Color(0xFF0284C7), radius = 2.8.dp.toPx(), center = Offset(cx, cy))
                    }
                    drawPath(p, Color(0xFF0284C7), style = Stroke(2.dp.toPx()))
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F9FF))
                    .border(1.dp, Color(0xFFBAE6FD), RoundedCornerShape(16.dp))
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Workout Split", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Strength: 60%", fontSize = 10.sp, color = Color(0xFF0C4A6E), fontWeight = FontWeight.SemiBold)
                Text(text = "Cardio: 25%", fontSize = 10.sp, color = Color(0xFF0C4A6E), fontWeight = FontWeight.SemiBold)
                Text(text = "Mobility: 15%", fontSize = 10.sp, color = Color(0xFF0C4A6E), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun PaymentDetailedCardContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFFFFBEB))
                .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(14.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Payment Status", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                Text(text = "₹2,800 Due in 25 Days", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color(0xFF78350F))
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFF59E0B))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "Pay Now", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }
    }
}

@Composable
fun BodyProgressDetailedCardContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFFAF5FF))
                .border(1.dp, Color(0xFFE9D5FF), RoundedCornerShape(14.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Weight Change", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7E22CE))
                Text(text = "69.5 kg (-0.7 kg this month)", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF581C87))
            }
            Text(text = "Target: 65 kg", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA855F7))
        }
    }
}

@Composable
fun LegendItem(label: String, count: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(color))
            Text(text = label, fontSize = 9.sp, color = Color(0xFF334155), fontWeight = FontWeight.SemiBold)
        }
        Text(text = count, fontSize = 9.5.sp, color = color, fontWeight = FontWeight.Black)
    }
}

@Composable
fun MetadataChip(icon: String, title: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(text = icon, fontSize = 11.sp)
        Column {
            Text(text = title, fontSize = 8.sp, color = Color(0xFF64748B))
            Text(text = value, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        }
    }
}

@Composable
fun HeroQuickStatChip(
    icon: String,
    label: String,
    value: String,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isDark) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.90f)
            )
            .border(
                1.dp,
                if (isDark) Color.White.copy(alpha = 0.20f) else Color(0xFFE2E8F0),
                RoundedCornerShape(12.dp)
            )
            .padding(vertical = 5.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = icon, fontSize = 10.sp)
                Text(
                    text = value,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isDark) Color.White else Color(0xFF0F172A),
                    maxLines = 1
                )
            }
            Text(
                text = label,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF64748B),
                maxLines = 1
            )
        }
    }
}

data class BentoFooterStat(
    val label: String,
    val value: String,
    val color: Color
)

@Composable
fun RichBentoCard(
    title: String,
    value: String,
    badgeText: String,
    badgeBg: Color,
    badgeTextColor: Color,
    subtitle: String,
    background: Brush,
    borderColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    accentColor: Color,
    titleColor: Color = Color(0xFF0F172A),
    valueColor: Color = Color(0xFF0F172A),
    isDark: Boolean,
    chartColor: Color,
    barHeights: List<Float>,
    xAxisLabels: List<String>,
    footerStats: List<BentoFooterStat>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(194.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .border(1.5.dp, borderColor, RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .padding(horizontal = 9.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Row: 3D Orb Icon | Title + Value/Badge + Subtitle | Circle Chevron >
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // 3D Glowing Circular Orb Icon (Matching Mockup 1:1)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(accentColor.copy(alpha = 0.95f), iconBg)
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.9f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = titleColor,
                            maxLines = 1,
                            softWrap = false
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = value,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Black,
                                color = valueColor,
                                maxLines = 1,
                                softWrap = false
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(badgeBg)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = badgeText,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = badgeTextColor,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                        Text(
                            text = subtitle,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = titleColor.copy(alpha = 0.75f),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                // White Circular Glass Chevron >
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f))
                        .border(0.8.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = titleColor,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // Center Chart Area: Dense Vertical Glowing Bars + White Spline + X-Axis
            Column(modifier = Modifier.fillMaxWidth()) {
                Canvas(modifier = Modifier.fillMaxWidth().height(46.dp)) {
                    val barWidth = 2.4.dp.toPx()
                    val gap = (size.width - (barHeights.size * barWidth)) / (barHeights.size - 1)
                    val p = Path()

                    barHeights.forEachIndexed { i, h ->
                        val x = i * (barWidth + gap)
                        val bh = size.height * h
                        val y = size.height - bh

                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                listOf(chartColor.copy(alpha = 0.85f), chartColor.copy(alpha = 0.15f))
                            ),
                            topLeft = Offset(x, y),
                            size = Size(barWidth, bh),
                            cornerRadius = CornerRadius(1.2.dp.toPx(), 1.2.dp.toPx())
                        )

                        val cx = x + barWidth / 2f
                        if (i == 0) p.moveTo(cx, y) else p.lineTo(cx, y)
                        drawCircle(Color.White, radius = 2.4.dp.toPx(), center = Offset(cx, y))
                        drawCircle(chartColor, radius = 1.2.dp.toPx(), center = Offset(cx, y))
                    }
                    drawPath(p, Color.White.copy(alpha = 0.95f), style = Stroke(1.8.dp.toPx()))
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    xAxisLabels.forEach { lbl ->
                        Text(
                            text = lbl,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = titleColor.copy(alpha = 0.65f)
                        )
                    }
                }
            }

            // Bottom 3-Stat Footer Row (Rounded Frosted Glass Pill)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.86f))
                    .border(0.8.dp, Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 4.dp, vertical = 3.5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                footerStats.forEach { stat ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stat.label,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            softWrap = false,
                            letterSpacing = (-0.3).sp
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = stat.value,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Black,
                            color = stat.color,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

/**
 * Interactive Dialog displaying all 8 Canonical Member Intelligence Card Themes
 */
@Composable
fun ThemePickerDialog(
    currentTheme: MemberCardTheme,
    onThemeSelected: (MemberCardTheme) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0F172A))
                .border(1.5.dp, Color(0xFF38BDF8).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Choose Card Theme",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "All 8 canonical presets from mockup",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✕", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                // 8 Themes List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(390.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MemberCardTheme.entries.forEach { theme ->
                        val isSelected = theme == currentTheme
                        val itemPalette = MemberCardThemeManager.getPalette(theme)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) Color(0xFF1E293B) else Color(0xFF1E293B).copy(alpha = 0.45f)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) itemPalette.themeAccent else Color(0xFF334155),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { onThemeSelected(theme) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                // Theme ID Badge
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(itemPalette.themeAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = theme.id,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }

                                Column {
                                    Text(
                                        text = theme.title,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color(0xFFE2E8F0)
                                    )
                                    Text(
                                        text = "${theme.badge} • ${itemPalette.stats.goal} • ${itemPalette.stats.totalVisits} visits",
                                        fontSize = 10.5.sp,
                                        color = if (isSelected) itemPalette.themeAccent else Color(0xFF94A3B8)
                                    )
                                }
                            }

                            if (isSelected) {
                                Text(
                                    text = "✓",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = itemPalette.themeAccent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
