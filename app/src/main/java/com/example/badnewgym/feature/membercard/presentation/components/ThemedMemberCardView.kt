package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
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
 * BAD GYM Master Member Intelligence Dashboard
 * 100% Pixel-Perfect Representation matching Master Reference Mockup:
 * 1. Top Hero Member Card (Halo, Crown, Details, Quote Bubble, Progress Bar & 4 Quick Stats)
 * 2. 2x2 Bento Intelligence Grid:
 *    - Top-Left: Attendance (85%, 30-Day Vertical Bar Chart, Streaks & Legend)
 *    - Top-Right: Workout Trend (47 min, 30-Day Session Duration Bars & KPI Footers)
 *    - Bottom-Left: Payment (₹2,800 Due, 12-Month Capsule Bar Chart & Receipt Trigger)
 *    - Bottom-Right: Body Progress (4 Stat Chips, 12-Month 4-Line Spline Chart & Progress Badges)
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
    var showAttendanceDetails by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // =========================================================================
        // 1. TOP HERO MEMBER PROFILE CARD
        // =========================================================================
        HeroProfileCard(
            data = data,
            onAvatarClick = { showThemePicker = true },
            modifier = Modifier.fillMaxWidth()
        )

        // =========================================================================
        // 2. 2x2 BENTO INTELLIGENCE GRID
        // =========================================================================
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: Attendance + Workout Trend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AttendanceBentoCard(
                    data = data,
                    onClick = {
                        showAttendanceDetails = true
                        onCardClick("Attendance")
                    },
                    modifier = Modifier.weight(1f)
                )

                WorkoutTrendBentoCard(
                    data = data,
                    onClick = { onCardClick("Workout") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 2: Payment + Body Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PaymentBentoCard(
                    data = data,
                    onViewReceiptClick = { onCardClick("Receipt") },
                    onClick = { onCardClick("Payment") },
                    modifier = Modifier.weight(1f)
                )

                BodyProgressBentoCard(
                    data = data,
                    onClick = { onCardClick("BodyProgress") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if (showAttendanceDetails) {
        AttendanceDetailsDialog(
            data = data,
            onDismiss = { showAttendanceDetails = false }
        )
    }

    if (showThemePicker) {
        ThemePickerDialog(
            currentTheme = MemberCardThemeManager.activeTheme,
            onThemeSelected = { newTheme ->
                MemberCardThemeManager.activeTheme = newTheme
                showThemePicker = false
            },
            onDismiss = { showThemePicker = false }
        )
    }
}

// =============================================================================
// HERO MEMBER PROFILE CARD
// =============================================================================
@Composable
fun HeroProfileCard(
    data: MemberCardData,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.2.dp, Color(0x55A7F3D0), RoundedCornerShape(24.dp))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Section: Avatar + Identity + Quote Bubble
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Avatar with Glowing Conic Halo & Crown Badge
                Box(
                    modifier = Modifier.size(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(72.dp)) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFF10B981),
                                    Color(0xFF06B6D4),
                                    Color(0xFF10B981)
                                )
                            ),
                            radius = size.minDimension / 2f - 2f,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.avatar_ashwini),
                        contentDescription = data.profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )

                    // Cyan Circular Badge with Gold Crown (Top-Right at ~1 o'clock)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                            .size(23.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF06B6D4))
                            .border(1.5.dp, Color.White, CircleShape)
                            .clickable { onAvatarClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "\uD83D\uDC51", fontSize = 11.sp)
                    }
                }

                // Middle Column: Member Name, Details & Status Pills
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.5.dp)
                ) {
                    Text(
                        text = "Ashwini Singh",
                        fontSize = 18.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        softWrap = false
                    )

                    Text(
                        text = "BG305 | 23 yrs | Male",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        softWrap = false
                    )

                    // Badges Row: [ ● Active Member ] [ 👑 Prime Plan ]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Active Member Pill (Light mint background, green border & text)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFE6FBF2))
                                .border(0.8.dp, Color(0xFF86EFAC), RoundedCornerShape(50.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF059669))
                            )
                            Text(
                                text = "Active Member",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669),
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        // Prime Plan Pill (Light amber background, amber border & text)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFFEF3C7))
                                .border(0.8.dp, Color(0xFFFDE68A), RoundedCornerShape(50.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(text = "\uD83D\uDC51", fontSize = 8.5.sp)
                            Text(
                                text = "Prime Plan",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706),
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                // Top-Right Quote Bubble: "Better Than Yesterday" 💪
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Column {
                            Text(
                                text = "\u201CBetter",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "Than",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "Yesterday\u201D",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                        Text(text = "\uD83D\uDCAA", fontSize = 21.sp)
                    }
                }
            }

            // Middle Section: 25 Days Left + Progress Bar + 75%
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "25 Days Left",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = " (4 Aug 2026 \u2013 12 Nov 2026)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(7.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(7.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF10B981), Color(0xFF059669))
                                    )
                                )
                        )
                    }
                    Text(
                        text = "75%",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1E293B)
                    )
                }
            }

            // Bottom Section: 4 Metric Compartments (Height, Weight, BMI, Goal)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                    .padding(horizontal = 6.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Height
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.weight(0.9f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Column {
                            Text(text = "Height", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                            Text(text = "178 cm", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                        }
                    }

                    Box(modifier = Modifier.width(1.dp).height(22.dp).background(Color(0xFFE2E8F0)))

                    // 2. Weight
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.weight(1.35f).padding(start = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDCFCE7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "\uD83C\uDFF7\uFE0F", fontSize = 11.sp)
                        }
                        Column {
                            Text(text = "Weight", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(text = "69.5 kg", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                                Column {
                                    Text(text = "\u2193 2.5 kg", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A), maxLines = 1, softWrap = false)
                                    Text(text = "(vs last month)", fontSize = 5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.width(1.dp).height(22.dp).background(Color(0xFFE2E8F0)))

                    // 3. BMI
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.weight(1.05f).padding(start = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "\u2696\uFE0F", fontSize = 11.sp)
                        }
                        Column {
                            Text(text = "BMI", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(text = "21.9", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(text = "Normal", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF166534), maxLines = 1, softWrap = false)
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.width(1.dp).height(22.dp).background(Color(0xFFE2E8F0)))

                    // 4. Goal
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.weight(1.05f).padding(start = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFE4E6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "\uD83C\uDFAF", fontSize = 11.sp)
                        }
                        Column {
                            Text(text = "Goal", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                            Text(text = "Lean Muscle", fontSize = 10.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// =============================================================================
// REUSABLE BENTO TIMEFRAME DROPDOWN PILL
// =============================================================================
@Composable
private fun BentoTimeframeDropdownPill(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Text(
            text = text,
            fontSize = 7.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155),
            maxLines = 1,
            softWrap = false
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF64748B),
            modifier = Modifier.size(9.dp)
        )
    }
}

// =============================================================================
// =============================================================================
// CARD 1: ATTENDANCE BENTO CARD (Top-Left) - CIRCULAR GAUGE + CALENDAR (NO BAR GRAPH)
// =============================================================================
@Composable
fun AttendanceBentoCard(
    data: MemberCardData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.2.dp, Color(0x3310B981), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(9.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Header: [ Calendar ] Attendance / Oct 2026 | [ < Oct 2026 > ]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF10B981), Color(0xFF059669))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Attendance",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "Oct 2026",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                // Month Navigator Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(text = "\u2039", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Text(
                        text = "Oct 2026",
                        fontSize = 7.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155),
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(text = "\u203A", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                }
            }

            // Top Metrics: Radiant Circular Donut Gauge + Frosted Present Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // 1. Radiant Donut Gauge (85% Attendance Rate with Golden Energy Tip)
                Box(
                    modifier = Modifier.size(54.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(54.dp)) {
                        val strokeW = 5.5.dp.toPx()
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val r = (size.minDimension - strokeW) / 2f

                        // Outer subtle glow halo ring
                        drawCircle(
                            color = Color(0x1810B981),
                            radius = r + strokeW * 0.7f,
                            center = center,
                            style = Stroke(width = 1.dp.toPx())
                        )

                        // Base translucent track
                        drawCircle(
                            color = Color(0xFFE2E8F0),
                            radius = r,
                            center = center,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )

                        // 85% Progress Arc with radiant golden/emerald gradient
                        drawArc(
                            brush = Brush.sweepGradient(
                                0.0f to Color(0xFFFDE047), // Golden energy tip at -90 deg
                                0.08f to Color(0xFF34D399), // Mint glow
                                0.80f to Color(0xFF10B981), // Emerald
                                1.0f to Color(0xFF059669),
                                center = center
                            ),
                            startAngle = -90f,
                            sweepAngle = 360f * 0.85f,
                            useCenter = false,
                            topLeft = Offset(strokeW / 2f, strokeW / 2f),
                            size = Size(r * 2, r * 2),
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "85%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "Attendance\nRate",
                            fontSize = 5.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center,
                            lineHeight = 6.5.sp,
                            maxLines = 2,
                            softWrap = false
                        )
                    }
                }

                // 2. Frosted Present Stat Card with Checkmark
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(0.8.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.weight(1f).height(54.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp, vertical = 3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = "18",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "Present",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // Monthly Calendar Grid Container (Su Mo Tu We Th Fr Sa + 31 Days)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8FAFC).copy(alpha = 0.95f),
                border = BorderStroke(0.6.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Weekday header: Su Mo Tu We Th Fr Sa
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
                            Text(
                                text = day,
                                fontSize = 6.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    // Calendar Day Rows
                    // Oct 2026: Day 1 starts under Wednesday
                    // 1 = Present (Mint), 2 = Absent (Red), 0 = Gym Off (Grey), 3 = Rest (White)
                    val calendarRows = listOf(
                        listOf(null, null, null, 1 to 1, 2 to 1, 3 to 1, 4 to 0),
                        listOf(5 to 3, 6 to 1, 7 to 1, 8 to 1, 9 to 1, 10 to 1, 11 to 0),
                        listOf(12 to 3, 13 to 1, 14 to 1, 15 to 1, 16 to 1, 17 to 1, 18 to 0),
                        listOf(19 to 3, 20 to 1, 21 to 1, 22 to 2, 23 to 1, 24 to 1, 25 to 0),
                        listOf(26 to 0, 27 to 1, 28 to 1, 29 to 1, 30 to 1, 31 to 3, null)
                    )

                    calendarRows.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(0.8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (item != null) {
                                        val (dayNum, status) = item
                                        val (bgColor, textColor, borderStroke) = when (status) {
                                            1 -> Triple(
                                                Color(0xFF6EE7B7), // Mint green
                                                Color(0xFF064E3B), // Forest green
                                                null
                                            )
                                            2 -> Triple(
                                                Color(0xFFFDA4AF), // Coral red
                                                Color(0xFF881337), // Crimson
                                                null
                                            )
                                            0 -> Triple(
                                                Color(0xFFCBD5E1), // Slate grey
                                                Color(0xFF475569), // Grey text
                                                null
                                            )
                                            else -> Triple(
                                                Color.White.copy(alpha = 0.9f),
                                                Color(0xFF475569),
                                                BorderStroke(0.4.dp, Color(0xFFCBD5E1))
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape)
                                                .background(bgColor)
                                                .then(
                                                    if (borderStroke != null) Modifier.border(borderStroke, CircleShape) else Modifier
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "$dayNum",
                                                fontSize = 6.5.sp,
                                                fontWeight = if (status == 1 || status == 2) FontWeight.Black else FontWeight.Bold,
                                                color = textColor,
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Legend Row: * Present 18  * Absent 1  * Off 5
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Present
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    Text(text = "Present ", fontSize = 6.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    Text(text = "18", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981), maxLines = 1, softWrap = false)
                }
                // Absent
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                    Text(text = "Absent ", fontSize = 6.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    Text(text = "1", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444), maxLines = 1, softWrap = false)
                }
                // Gym Off
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF94A3B8)))
                    Text(text = "Off ", fontSize = 6.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    Text(text = "5", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(Color(0xFFF1F5F9)))

            // Footer 2-Column Streaks: Current Streak, Best Streak
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(text = "\uD83D\uDD25", fontSize = 11.sp)
                    Column {
                        Text(text = "Current Streak", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Text(text = "6 Days", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(text = "\uD83C\uDFC6", fontSize = 11.sp)
                    Column {
                        Text(text = "Best Streak", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Text(text = "12 Days", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                }
            }
        }
    }
}

// =============================================================================
// CARD 2: WORKOUT TREND BENTO CARD (Top-Right)
// =============================================================================
@Composable
fun WorkoutTrendBentoCard(
    data: MemberCardData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.2.dp, Color(0x330284C7), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // Header: [ FitnessCenter ] Workout Trend / Last 30 Days | [ 30 Days v ]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0284C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Workout Trend",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "Last 30 Days",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                BentoTimeframeDropdownPill(text = "30 Days")
            }

            // Top Metric: 47 min + ^ 8% (vs previous 30 days) + Avg per session
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "47 min",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0369A1)
                    )
                    Column {
                        Text(
                            text = "\u2191 8%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF16A34A),
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "(vs previous 30 days)",
                            fontSize = 7.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
                Text(
                    text = "Avg per session",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    softWrap = false
                )
            }

            // 30-Day Workout Bars (Graduated Cyan-Blue Bars)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().height(58.dp)) {
                    // Left Y-Axis: 90m, 45m, 0
                    Column(
                        modifier = Modifier.height(58.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "90m", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "45m", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "0", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(3.dp))

                    // 30 Workout Bars Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                    ) {
                        val workoutDays = listOf(
                            1, 1, 0, 1, 1, 1, 1, 0, 1, 1,
                            1, 1, 0, 1, 1, 1, 1, 1, 0, 1,
                            1, 1, 1, 0, 1, 1, 1, 1, 1, 1
                        )
                        val count = workoutDays.size
                        val barWidth = 3.dp.toPx()
                        val step = size.width / count.toFloat()
                        val barRadius = CornerRadius(barWidth / 2f, barWidth / 2f)

                        workoutDays.forEachIndexed { i, active ->
                            val x = i * step + (step - barWidth) / 2f
                            val hRatio = if (active == 1) {
                                (0.50f + ((i * 7) % 45) / 100f).coerceIn(0.40f, 0.95f)
                            } else {
                                0.15f
                            }
                            val h = size.height * hRatio
                            val y = size.height - h

                            if (active == 1) {
                                drawRoundRect(
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
                                    ),
                                    topLeft = Offset(x, y),
                                    size = Size(barWidth, h),
                                    cornerRadius = barRadius
                                )
                            } else {
                                drawRoundRect(
                                    color = Color(0xFFCBD5E1),
                                    topLeft = Offset(x, y),
                                    size = Size(barWidth, h),
                                    cornerRadius = barRadius
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Bottom X-Axis Marks: 1, 5, 10, 15, 20, 25, 30
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("1", "5", "10", "15", "20", "25", "30").forEach { mark ->
                        Text(
                            text = mark,
                            fontSize = 6.5.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            // Legend Row: * Workout 24 days  * No Workout 6 days
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Workout
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF0284C7)))
                    Text(text = "Workout ", fontSize = 6.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    Text(text = "24 days", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7), maxLines = 1, softWrap = false)
                }
                // No Workout
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                    Text(text = "No Workout ", fontSize = 6.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    Text(text = "6 days", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(Color(0xFFF1F5F9)))

            // Footer 3-Column Stats: Total Workouts, Best Session, Current Streak
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Total Workouts
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "\u23F1\uFE0F", fontSize = 11.sp)
                    Column {
                        Text(text = "Total Workouts", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Text(text = "24", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                }
                // 2. Best Session
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "\uD83C\uDFC6", fontSize = 11.sp)
                    Column {
                        Text(text = "Best Session", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Text(text = "92 min", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                }
                // 3. Current Streak
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "\uD83D\uDD25", fontSize = 11.sp)
                    Column {
                        Text(text = "Current Streak", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Text(text = "8 Days", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                }
            }
        }
    }
}

// =============================================================================
// CARD 3: PAYMENT BENTO CARD (Bottom-Left)
// =============================================================================
@Composable
fun PaymentBentoCard(
    data: MemberCardData,
    onClick: () -> Unit,
    onViewReceiptClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.2.dp, Color(0x33EF4444), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // Header: [ Wallet ] Payment / Last 12 Months | [ 12 Months v ]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Payment",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "Last 12 Months",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                BentoTimeframeDropdownPill(text = "12 Months")
            }

            // Top Metric: Rs 2,800 + [ ! Due ] + Next Due Date + View Receipt Button
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "\u20B92,800",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF991B1B)
                        )
                        // Red Due Badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFEF4444))
                                .padding(horizontal = 4.dp, vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(text = "!", fontSize = 7.5.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Text(text = "Due", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    // View Receipt Button (Protected against truncation)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF))
                            .border(0.8.dp, Color(0xFF93C5FD), RoundedCornerShape(8.dp))
                            .clickable { onViewReceiptClick() }
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(9.dp)
                        )
                        Text(
                            text = "View Receipt",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Text(
                    text = "Next Due Date: 12 Nov 2026",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    softWrap = false
                )
            }

            // 12-Month Capsule Bar Chart with Track
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().height(58.dp)) {
                    // Left Y-Axis: Rs 3k, Rs 1.5k, 0
                    Column(
                        modifier = Modifier.height(58.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "\u20B93k", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "\u20B91.5k", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "0", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 12 Monthly Capsule Bars inside Translucent Capsule Tracks
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                    ) {
                        val months = listOf(
                            1 to 0.70f, // Jan: Paid
                            1 to 0.88f, // Feb: Paid
                            2 to 0.50f, // Mar: Partial
                            1 to 0.88f, // Apr: Paid
                            1 to 0.75f, // May: Paid
                            2 to 0.45f, // Jun: Partial
                            1 to 0.95f, // Jul: Paid
                            1 to 0.88f, // Aug: Paid
                            1 to 0.78f, // Sep: Paid
                            3 to 0.48f, // Oct: Due
                            3 to 0.38f, // Nov: Due
                            0 to 0.35f  // Dec: No Data
                        )
                        val count = months.size
                        val barWidth = 6.dp.toPx()
                        val slotWidth = size.width / count.toFloat()
                        val trackRadius = CornerRadius(barWidth / 2f, barWidth / 2f)

                        months.forEachIndexed { i, (status, h) ->
                            val x = i * slotWidth + (slotWidth - barWidth) / 2f
                            val fullH = size.height

                            // Draw full-height background track
                            drawRoundRect(
                                color = Color(0xFFF1F5F9),
                                topLeft = Offset(x, 0f),
                                size = Size(barWidth, fullH),
                                cornerRadius = trackRadius
                            )

                            if (status != 0) {
                                val bh = (fullH * h).coerceIn(barWidth, fullH)
                                val y = fullH - bh

                                val barBrush = when (status) {
                                    1 -> Brush.verticalGradient(listOf(Color(0xFF10B981), Color(0xFF059669))) // Paid
                                    2 -> Brush.verticalGradient(listOf(Color(0xFFFBBF24), Color(0xFFF59E0B))) // Partial
                                    else -> Brush.verticalGradient(listOf(Color(0xFFF87171), Color(0xFFEF4444))) // Due
                                }

                                drawRoundRect(
                                    brush = barBrush,
                                    topLeft = Offset(x, y),
                                    size = Size(barWidth, bh),
                                    cornerRadius = trackRadius
                                )
                            } else {
                                // Dec No Data: subtle outline
                                drawRoundRect(
                                    color = Color(0xFFCBD5E1),
                                    topLeft = Offset(x, fullH * 0.65f),
                                    size = Size(barWidth, fullH * 0.35f),
                                    cornerRadius = trackRadius,
                                    style = Stroke(width = 0.8.dp.toPx())
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Bottom X-Axis Labels: Jan to Dec with equal distribution
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach { lbl ->
                        Text(
                            text = lbl,
                            fontSize = 6.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // Legend Row: * Paid 8 mo  * Partial 2 mo  * Due 2 mo  * No Data -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Paid
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    Text(text = "Paid ", fontSize = 6.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    Text(text = "8 mo", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981), maxLines = 1, softWrap = false)
                }
                // Partial
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                    Text(text = "Partial ", fontSize = 6.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    Text(text = "2 mo", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B), maxLines = 1, softWrap = false)
                }
                // Due
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                    Text(text = "Due ", fontSize = 6.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    Text(text = "2 mo", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444), maxLines = 1, softWrap = false)
                }
                // No Data
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF94A3B8)))
                    Text(text = "No Data ", fontSize = 6.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    Text(text = "-", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(Color(0xFFF1F5F9)))

            // Footer 3-Column Stats: Total Paid, Avg / Month, Next Due
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Total Paid
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "\uD83D\uDCB3", fontSize = 11.sp)
                    Column {
                        Text(text = "Total Paid", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Text(text = "\u20B912,600", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                }
                // 2. Avg / Month
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(10.dp)
                    )
                    Column {
                        Text(text = "Avg / Month", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Text(text = "\u20B91,050", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                }
                // 3. Next Due
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "\uD83D\uDD52", fontSize = 11.sp)
                    Column {
                        Text(text = "Next Due", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(text = "12 Nov", fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                            Text(text = "(25d left)", fontSize = 6.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444), maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// CARD 4: BODY PROGRESS BENTO CARD (Bottom-Right)
// =============================================================================
@Composable
fun BodyProgressBentoCard(
    data: MemberCardData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.2.dp, Color(0x3310B981), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // Header: [ Person ] Body Progress / Last 12 Months | [ 12 Months v ]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Body Progress",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "Last 12 Months",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                BentoTimeframeDropdownPill(text = "12 Months")
            }

            // Top 4 Metric Chips Row (Weight, Muscle, Body Fat, Waist in 3-line format)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                // 1. Weight
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.6.dp, Color(0xFFE2E8F0), RoundedCornerShape(7.dp))
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(text = "\uD83D\uDCE6", fontSize = 6.5.sp)
                        Text(text = "69.5 kg", fontSize = 7.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                    Text(text = "\u2193 2.5 kg", fontSize = 5.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A), maxLines = 1, softWrap = false)
                    Text(text = "Weight", fontSize = 5.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }

                // 2. Muscle
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.6.dp, Color(0xFFE2E8F0), RoundedCornerShape(7.dp))
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(text = "\uD83D\uDCAA", fontSize = 6.5.sp)
                        Text(text = "57.2 kg", fontSize = 7.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                    Text(text = "\u2191 1.4 kg", fontSize = 5.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A), maxLines = 1, softWrap = false)
                    Text(text = "Muscle Mass", fontSize = 5.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }

                // 3. Body Fat
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.6.dp, Color(0xFFE2E8F0), RoundedCornerShape(7.dp))
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(text = "\uD83D\uDD25", fontSize = 6.5.sp)
                        Text(text = "17.6 %", fontSize = 7.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                    Text(text = "\u2193 1.2 %", fontSize = 5.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A), maxLines = 1, softWrap = false)
                    Text(text = "Body Fat", fontSize = 5.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }

                // 4. Waist
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.6.dp, Color(0xFFE2E8F0), RoundedCornerShape(7.dp))
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(text = "\uD83E\uDE90", fontSize = 6.5.sp)
                        Text(text = "81 cm", fontSize = 7.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1, softWrap = false)
                    }
                    Text(text = "\u2193 4 cm", fontSize = 5.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A), maxLines = 1, softWrap = false)
                    Text(text = "Waist", fontSize = 5.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }
            }

            // 12-Month Multi-Line Spline Chart
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().height(58.dp)) {
                    // Left Y-Axis (kg): 90kg, 60kg, 30kg, 0
                    Column(
                        modifier = Modifier.height(58.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "90kg", fontSize = 6.5.sp, color = Color(0xFF94A3B8))
                        Text(text = "60kg", fontSize = 6.5.sp, color = Color(0xFF94A3B8))
                        Text(text = "30kg", fontSize = 6.5.sp, color = Color(0xFF94A3B8))
                        Text(text = "0", fontSize = 6.5.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(3.dp))

                    // 4 Clean Spline Curves with Circular Dots & Soft Area Fill
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                    ) {
                        // 12 data points for 4 metrics
                        val weightPts = listOf(0.22f, 0.20f, 0.23f, 0.28f, 0.28f, 0.30f, 0.29f, 0.28f, 0.29f, 0.31f, 0.33f, 0.33f)
                        val musclePts = listOf(0.40f, 0.33f, 0.35f, 0.36f, 0.34f, 0.38f, 0.39f, 0.38f, 0.40f, 0.42f, 0.42f, 0.43f)
                        val waistPts = listOf(0.38f, 0.45f, 0.43f, 0.42f, 0.44f, 0.42f, 0.44f, 0.44f, 0.45f, 0.47f, 0.48f, 0.49f)
                        val fatPts = listOf(0.66f, 0.69f, 0.68f, 0.69f, 0.70f, 0.70f, 0.71f, 0.71f, 0.70f, 0.71f, 0.72f, 0.73f)

                        val count = weightPts.size
                        val stepX = size.width / (count - 1).toFloat()

                        // Subtle green area gradient under the curve
                        val areaPath = Path().apply {
                            moveTo(0f, size.height)
                            weightPts.forEachIndexed { i, yRatio ->
                                val x = i * stepX
                                val y = size.height * yRatio
                                if (i == 0) lineTo(x, y) else {
                                    val prevX = (i - 1) * stepX
                                    val prevY = size.height * weightPts[i - 1]
                                    val cx = (prevX + x) / 2f
                                    cubicTo(cx, prevY, cx, y, x, y)
                                }
                            }
                            lineTo(size.width, size.height)
                            close()
                        }
                        drawPath(
                            path = areaPath,
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFF10B981).copy(alpha = 0.12f), Color(0xFFFBBF24).copy(alpha = 0.04f), Color.Transparent)
                            )
                        )

                        // Helper function to draw smooth solid Bezier spline and solid circular dots
                        fun drawSeries(points: List<Float>, color: Color) {
                            val path = Path()
                            points.forEachIndexed { i, yRatio ->
                                val x = i * stepX
                                val y = size.height * yRatio
                                if (i == 0) {
                                    path.moveTo(x, y)
                                } else {
                                    val prevX = (i - 1) * stepX
                                    val prevY = size.height * points[i - 1]
                                    val cx = (prevX + x) / 2f
                                    path.cubicTo(cx, prevY, cx, y, x, y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = color,
                                style = Stroke(
                                    width = 1.4.dp.toPx(),
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )

                            // Draw solid circle dots at each data point matching curve color
                            points.forEachIndexed { i, yRatio ->
                                val x = i * stepX
                                val y = size.height * yRatio
                                drawCircle(color = color, radius = 1.6.dp.toPx(), center = Offset(x, y))
                            }
                        }

                        // Draw all 4 curves
                        drawSeries(fatPts, Color(0xFFF59E0B))      // Body Fat (Yellow)
                        drawSeries(waistPts, Color(0xFF8B5CF6))    // Waist (Purple)
                        drawSeries(musclePts, Color(0xFF0284C7))   // Muscle (Blue)
                        drawSeries(weightPts, Color(0xFF10B981))   // Weight (Green)
                    }

                    Spacer(modifier = Modifier.width(3.dp))

                    // Right Y-Axis (%): 30%, 20%, 10%, 0%
                    Column(
                        modifier = Modifier.height(58.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = "30%", fontSize = 6.5.sp, color = Color(0xFFEF4444))
                        Text(text = "20%", fontSize = 6.5.sp, color = Color(0xFFEF4444))
                        Text(text = "10%", fontSize = 6.5.sp, color = Color(0xFFEF4444))
                        Text(text = "0%", fontSize = 6.5.sp, color = Color(0xFFEF4444))
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Bottom X-Axis Labels: Jan to Dec with equal distribution matching canvas data points
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 22.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach { lbl ->
                        Text(
                            text = lbl,
                            fontSize = 5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // Legend Row: * Weight (kg)  * Muscle (kg)  * Body Fat (%)  * Waist (cm)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Weight
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    Text(text = "Weight (kg)", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }
                // Muscle
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF0284C7)))
                    Text(text = "Muscle (kg)", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }
                // Body Fat
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                    Text(text = "Body Fat (%)", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }
                // Waist
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF8B5CF6)))
                    Text(text = "Waist (cm)", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(Color(0xFFF1F5F9)))

            // Footer 3-Column Stats: Strength, Endurance, Overall Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Strength
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "\uD83D\uDCCA", fontSize = 11.sp)
                    Column {
                        Text(text = "Strength", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Text(text = "+18%", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF16A34A), maxLines = 1, softWrap = false)
                        Text(text = "(Est. 1RM)", fontSize = 5.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    }
                }
                // 2. Endurance
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "\u2764\uFE0F", fontSize = 11.sp)
                    Column {
                        Text(text = "Endurance", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Text(text = "+22%", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF16A34A), maxLines = 1, softWrap = false)
                        Text(text = "(Avg Cardio Time)", fontSize = 5.5.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                    }
                }
                // 3. Overall Progress
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "\uD83D\uDCC8", fontSize = 11.sp)
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(text = "Overall Progress", fontSize = 6.sp, color = Color(0xFF64748B), maxLines = 1, softWrap = false)
                        Spacer(modifier = Modifier.height(1.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF10B981))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "On Track",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// THEME PICKER DIALOG (Interactive modal for preset themes)
// =============================================================================
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
                        Text(text = "\u2715", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
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
                                        text = "${theme.badge} \u2022 ${itemPalette.stats.goal} \u2022 ${itemPalette.stats.totalVisits} visits",
                                        fontSize = 10.5.sp,
                                        color = if (isSelected) itemPalette.themeAccent else Color(0xFF94A3B8)
                                    )
                                }
                            }

                            if (isSelected) {
                                Text(
                                    text = "\u2713",
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


// =============================================================================
// ATTENDANCE DETAILS MODAL DIALOG (High-Fidelity Interactive Details View)
// =============================================================================
@Composable
fun AttendanceDetailsDialog(
    data: MemberCardData,
    onDismiss: () -> Unit
) {
    var selectedDay by remember { mutableStateOf(16) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.98f),
            border = BorderStroke(1.2.dp, Color(0xFF86EFAC)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header: [ Calendar ] Attendance Details + [ X ]
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
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Attendance Details",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Monthly attendance overview & logs",
                                fontSize = 10.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    // Close Button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Month Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFF1F5F9))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "\u2039", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        Text(
                            text = "October 2026",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(text = "\u203A", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    }
                }

                // Top Metrics Row: Donut Gauge + 3 Stat Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Donut Gauge
                    Box(
                        modifier = Modifier.size(72.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(72.dp)) {
                            val strokeW = 7.dp.toPx()
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val r = (size.minDimension - strokeW) / 2f

                            drawCircle(
                                color = Color(0xFFE2E8F0),
                                radius = r,
                                center = center,
                                style = Stroke(width = strokeW, cap = StrokeCap.Round)
                            )

                            drawArc(
                                brush = Brush.sweepGradient(
                                    0.0f to Color(0xFFFDE047),
                                    0.08f to Color(0xFF34D399),
                                    0.80f to Color(0xFF10B981),
                                    1.0f to Color(0xFF059669),
                                    center = center
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f * 0.85f,
                                useCenter = false,
                                topLeft = Offset(strokeW / 2f, strokeW / 2f),
                                size = Size(r * 2, r * 2),
                                style = Stroke(width = strokeW, cap = StrokeCap.Round)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "85%",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Rate",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    // 3 Stat Cards (Present, Absent, Off)
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Present
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF0FDF4),
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                            modifier = Modifier.weight(1f).height(62.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "\u2713", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF16A34A))
                                Text(text = "18", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text(text = "Present", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                            }
                        }

                        // Absent
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFEF2F2),
                            border = BorderStroke(1.dp, Color(0xFFFECACA)),
                            modifier = Modifier.weight(1f).height(62.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "!", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
                                Text(text = "1", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text(text = "Absent", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                            }
                        }

                        // Gym Off
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.weight(1f).height(62.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "\uD83C\uDFD6\uFE0F", fontSize = 11.sp)
                                Text(text = "5", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text(text = "Off", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            }
                        }
                    }
                }

                // Interactive Calendar Grid
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Weekday header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
                                Text(
                                    text = day,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B),
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        val calendarRows = listOf(
                            listOf(null, null, null, 1 to 1, 2 to 1, 3 to 1, 4 to 0),
                            listOf(5 to 3, 6 to 1, 7 to 1, 8 to 1, 9 to 1, 10 to 1, 11 to 0),
                            listOf(12 to 3, 13 to 1, 14 to 1, 15 to 1, 16 to 1, 17 to 1, 18 to 0),
                            listOf(19 to 3, 20 to 1, 21 to 1, 22 to 2, 23 to 1, 24 to 1, 25 to 0),
                            listOf(26 to 0, 27 to 1, 28 to 1, 29 to 1, 30 to 1, 31 to 3, null)
                        )

                        calendarRows.forEach { week ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                week.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(1.5.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (item != null) {
                                            val (dayNum, status) = item
                                            val isSelected = selectedDay == dayNum
                                            val (bgColor, textColor) = when (status) {
                                                1 -> Color(0xFF6EE7B7) to Color(0xFF064E3B)
                                                2 -> Color(0xFFFDA4AF) to Color(0xFF881337)
                                                0 -> Color(0xFFCBD5E1) to Color(0xFF475569)
                                                else -> Color.White to Color(0xFF64748B)
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape)
                                                    .background(bgColor)
                                                    .then(
                                                        if (isSelected) Modifier.border(2.dp, Color(0xFF0284C7), CircleShape) else Modifier
                                                    )
                                                    .clickable { selectedDay = dayNum },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "$dayNum",
                                                    fontSize = 9.sp,
                                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                                    color = textColor
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Selected Day Info Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(1.dp, Color(0xFF86EFAC)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Day $selectedDay • 16 Oct 2026",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                            Text(
                                text = "In: 06:10 AM • Out: 07:42 AM • Workout: 62 min",
                                fontSize = 9.5.sp,
                                color = Color(0xFF047857)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF10B981))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Present",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
