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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

/**
 * BAD GYM — Master Storyboard Dashboard
 * 100% Pixel-Perfect Representation matching Master Visual Mockup:
 * 1. Top App Bar & Deep Purple Hero ("Stronger Happier You!" + 3D Flexing Character)
 * 2. Floating 4-Metric Bar (Height, Weight, BMI, Goal)
 * 3. Attendance Card (3D Calendar & Sun, 85%, 30-Day Green Bar Chart, 4-Card Stats)
 * 4. Workout Trend Card (3D Dumbbells & Cloud, 47 min, 30-Day Blue Bar Chart, 4-Card Stats)
 * 5. Payment Card (3D Wallet & Coins, ₹2,800 Due, 12-Month Capsule Bar Chart, 4-Card Stats)
 * 6. Body Progress Card (3D Scale & Cloud, 12-Month 4-Line Spline Chart, 8-Card Stats)
 * 7. Bottom Banner ("Consistent Steps, Brighter Days" + 3D Dumbbell + Keep Going Button)
 */
@Composable
fun MasterStoryboardDashboard(
    data: MemberCardData,
    onAvatarClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    onViewReceiptClick: () -> Unit = {},
    onBodyProgressClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. TOP HERO HEADER & CHARACTER SECTION
        MasterHeroHeader(
            data = data,
            onAvatarClick = onAvatarClick
        )

        // 2. ATTENDANCE CARD (Full Width Mint Green #EFFEF2)
        MasterAttendanceCard(
            data = data,
            onClick = onAttendanceClick
        )

        // 3. WORKOUT TREND CARD (Full Width Sky Blue #E5F1FB)
        MasterWorkoutTrendCard(
            data = data,
            onClick = onWorkoutClick
        )

        // 4. PAYMENT CARD (Full Width Coral Pink #FEEDED)
        MasterPaymentCard(
            data = data,
            onClick = onPaymentClick,
            onViewReceiptClick = onViewReceiptClick
        )

        // 5. BODY PROGRESS CARD (Full Width Soft Lavender #E7D6F5)
        MasterBodyProgressCard(
            data = data,
            onClick = onBodyProgressClick
        )

        // 6. BOTTOM FOOTER BANNER ("Consistent Steps, Brighter Days")
        MasterFooterBanner(
            onKeepGoingClick = onWorkoutClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// =============================================================================
// 1. MASTER HERO HEADER & CHARACTER SECTION
// =============================================================================
@Composable
fun MasterHeroHeader(
    data: MemberCardData,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF32125B),
                        Color(0xFF240A46),
                        Color(0xFF381162)
                    )
                )
            )
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // --- TOP BRANDING & STATUS ROW ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // BAD GYM + Member Intelligence
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "BAD ",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "GYM",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFCAFF04)
                            )
                        }
                        Text(
                            text = "Member Intelligence",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFE9D5FF)
                        )
                    }
                }

                // Right Status: Active Session + Date Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // Active Session Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFF00C271).copy(alpha = 0.22f))
                            .border(0.8.dp, Color(0xFF00C271).copy(alpha = 0.5f), RoundedCornerShape(50.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
                    ) {
                        Box(modifier = Modifier.size(5.5.dp).clip(CircleShape).background(Color(0xFF00E676)))
                        Text(
                            text = "Active Session",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA7F3D0)
                        )
                    }

                    // Date Pill: 02 Oct 2026
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFF5D22B6).copy(alpha = 0.85f))
                            .border(0.8.dp, Color(0xFF7C3AED).copy(alpha = 0.6f), RoundedCornerShape(50.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = "02 Oct 2026",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            // --- HERO BANNER: COMPACT HEADLINE + AVATAR PHOTO & BADGES ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Stronger, Happier You! 😃 + Subtitle + Speech Bubble
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Stronger, ",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = "Happier ",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFA3E635),
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = "You! 😃",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = (-0.3).sp
                        )
                    }

                    Text(
                        text = "Track progress, stay consistent & be your best!",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFE9D5FF)
                    )

                    // Speech Bubble Pill: "Better Than Yesterday" 💪
                    Row(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White.copy(alpha = 0.95f))
                            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "“Better Than Yesterday”",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(text = "💪", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Right: Compact Avatar with Halo & Badges
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier.size(54.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(54.dp)) {
                            drawCircle(
                                color = Color(0xFF10B981),
                                radius = size.minDimension / 2f - 1.5f,
                                style = Stroke(width = 2.5.dp.toPx())
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.avatar_ashwini),
                            contentDescription = data.profile.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color.White, CircleShape)
                                .clickable { onAvatarClick() }
                        )

                        // Crown Pin
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 1.dp, y = (-2).dp)
                                .size(17.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00C271))
                                .border(1.2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👑", fontSize = 8.5.sp)
                        }
                    }

                    // Two Compact Badges: Active & Prime Plan
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Active Member Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF00C271))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.5.dp)
                        ) {
                            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color.White))
                            Text(
                                text = "Active",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Prime Plan Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFFFDE6A))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(text = "👑", fontSize = 7.5.sp)
                            Text(
                                text = "Prime",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF854D0E)
                            )
                        }
                    }
                }
            }

            // --- REARRANGED COMPACT WHITE PROFILE & STATS CARD ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Profile Row & Expiry Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Ashwini Singh",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "BG305  |  23 yrs  |  Male",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "25 Days Left",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "75%",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF047857)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFE2E8F0))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.75f)
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFF10B981), Color(0xFF06B6D4))
                                            )
                                        )
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(0.6.dp).background(Color(0xFFF1F5F9)))

                    // 4-Metric Quick Stats Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 1. Height
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                                Text(text = "178 cm", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text(text = "Height", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            }
                        }

                        Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))

                        // 2. Weight
                        Row(
                            modifier = Modifier.weight(1.35f).padding(start = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDCFCE7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🏷️", fontSize = 10.sp)
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(text = "69.5 kg", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                    Text(text = "↓ 2.5", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                                }
                                Text(text = "Weight", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            }
                        }

                        Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))

                        // 3. BMI
                        Row(
                            modifier = Modifier.weight(1.25f).padding(start = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF3E8FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "✨", fontSize = 10.sp)
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(text = "21.9", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50.dp))
                                            .background(Color(0xFFDCFCE7))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "Normal",
                                            fontSize = 6.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF166534),
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                                Text(text = "BMI", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            }
                        }

                        Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))

                        // 4. Goal
                        Row(
                            modifier = Modifier.weight(1.2f).padding(start = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFE4E6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🎯", fontSize = 10.sp)
                            }
                            Column {
                                Text(text = "Lean Muscle", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), maxLines = 1)
                                Text(text = "Goal", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// 2. MASTER ATTENDANCE CARD (Mint Green #EFFEF2)
// =============================================================================
@Composable
fun MasterAttendanceCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFEFFEF2))
            .border(1.2.dp, Color(0xFFC6F6D5), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Left 3D Illustration + Right Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Calendar & Sun Illustration
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_attendance_calendar),
                    contentDescription = "Attendance 3D Calendar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(86.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Right Column Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Top: Star Badge + Title + Dropdown Pill
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Attendance",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 30 Days",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        TimeframeDropdownPill(text = "Oct 2026")
                    }

                    // Key Metric: 90% + 27 / 30 Present Days + Pill on left, 4 Breakdown Columns on right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column {
                                Text(
                                    text = "90%",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF047857)
                                )
                                Text(
                                    text = "27 / 30 Present Days",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "↑ 5%",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                }
                                Text(
                                    text = "vs Sep",
                                    fontSize = 7.5.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Breakdown Columns: Present 27, Late 1, Absent 1, Holiday 1
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF10B981)))
                                    Spacer(Modifier.width(2.dp))
                                    Text("Present", fontSize = 7.sp, color = Color(0xFF64748B))
                                }
                                Text("27", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text("90%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(5.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                                    Spacer(Modifier.width(2.dp))
                                    Text("Late", fontSize = 7.sp, color = Color(0xFF64748B))
                                }
                                Text("1", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text("3%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(5.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                                    Spacer(Modifier.width(2.dp))
                                    Text("Absent", fontSize = 7.sp, color = Color(0xFF64748B))
                                }
                                Text("1", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text("3%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF8B5CF6)))
                                    Spacer(Modifier.width(2.dp))
                                    Text("Holiday", fontSize = 7.sp, color = Color(0xFF64748B))
                                }
                                Text("1", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                                Text("3%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
                            }
                        }
                    }
                }
            }

            // 30-Day Attendance Numbered Hollow Circle Grid (1..30)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Day statuses for Oct 2026 (1..30):
                // 1 = Present (Green), 2 = Late (Amber), 0 = Absent (Red), 3 = Holiday (Purple), 4 = Sunday (Disabled/Gray), 5 = Upcoming (Faint Outline)
                val dayStatuses = remember {
                    listOf(
                        1, 3, 1, 4, 1, 2, 1, 1, 1, 1, 4, 1, 1, 1, 0, // Days 1..15
                        1, 1, 4, 1, 1, 1, 1, 1, 1, 4, 1, 1, 5, 5, 5  // Days 16..30
                    )
                }

                // Row 1: Days 1 to 15
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (day in 1..15) {
                        val status = dayStatuses[day - 1]
                        AttendanceHollowCircle(day = day, status = status)
                    }
                }

                // Row 2: Days 16 to 30
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (day in 16..30) {
                        val status = dayStatuses[day - 1]
                        AttendanceHollowCircle(day = day, status = status)
                    }
                }

                // Micro Legend Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AttendanceLegendItem(label = "Present", count = "20", color = Color(0xFF10B981))
                    AttendanceLegendItem(label = "Late", count = "1", color = Color(0xFFF59E0B))
                    AttendanceLegendItem(label = "Absent", count = "1", color = Color(0xFFEF4444))
                    AttendanceLegendItem(label = "Holiday", count = "1", color = Color(0xFF8B5CF6))
                    AttendanceLegendItem(label = "Sunday", count = "4", color = Color(0xFF94A3B8))
                    AttendanceLegendItem(label = "Upcoming", count = "3", color = Color(0xFFCBD5E1), isHollow = true)
                }
            }

            // Weekly Progress Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Weekly:", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Text(text = "W1 100%", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    Text(text = "•", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    Text(text = "W2 85%", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    Text(text = "•", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    Text(text = "W3 100%", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    Text(text = "•", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    Text(text = "W4 80%", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                }
                Text(
                    text = "Monthly: 90% (↑5%)",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF047857)
                )
            }

            // Bottom 4-Card Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MiniStatCard(
                    icon = "🔥",
                    title = "Current Streak",
                    value = "6 Days",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "🏆",
                    title = "Best Streak",
                    value = "12 Days",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "🟢",
                    title = "Attendance Rate",
                    value = "90%",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "📅",
                    title = "Total Visits",
                    value = "27 Days",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// =============================================================================
// 3. MASTER WORKOUT TREND CARD (Sky Blue #E5F1FB)
// =============================================================================
@Composable
fun MasterWorkoutTrendCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE5F1FB))
            .border(1.2.dp, Color(0xFFBAE6FD), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Left 3D Illustration + Right Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Dumbbells & Cloud Illustration
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_workout_dumbbells),
                    contentDescription = "Workout 3D Dumbbells",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(86.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Right Column Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Top: Star Badge + Title + Dropdown Pill
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0284C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Workout Trend",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 30 Days",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        TimeframeDropdownPill(text = "Oct 2026")
                    }

                    // Key Metric: 70 min + Avg / Session + Pill on left, Legend Column on right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column {
                                Text(
                                    text = "70 min",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0284C7)
                                )
                                Text(
                                    text = "Avg / Session",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "↑ 8%",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                }
                                Text(
                                    text = "vs Sep",
                                    fontSize = 7.5.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Right Legend Column
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            LegendDotItem(color = Color(0xFF0284C7), label = "Workout", value = "(22 d)")
                            LegendDotItem(color = Color(0xFF94A3B8), label = "Rest", value = "(8 d)")
                        }
                    }
                }
            }

            // Quick Info Metrics Strip (4 KPIs from Image)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .padding(horizontal = 7.dp, vertical = 4.5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Total Duration", fontSize = 6.8.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    Text(text = "823 min", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                    Text(text = "↑ 14% vs Sep", fontSize = 6.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Sessions", fontSize = 6.8.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    Text(text = "22", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "↑ 2 vs prev", fontSize = 6.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Avg Duration", fontSize = 6.8.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    Text(text = "74 min", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                    Text(text = "↑ 8%", fontSize = 6.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Peak Duration", fontSize = 6.8.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    Text(text = "123 min", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFD97706))
                    Text(text = "↑ 12%", fontSize = 6.5.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
            }

            // 30-Day Workout Bars Chart (0m, 60m, 120m)
            Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    // Left Y-Axis
                    Column(
                        modifier = Modifier.height(52.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "120m", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "60m", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "0m", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 30 Bars Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        val workoutMins = listOf(
                            60, 20, 75, 20, 85, 90, 45, 60, 70, 80,
                            20, 65, 70, 20, 120, 80, 50, 60, 75, 85,
                            20, 40, 90, 70, 75, 80, 95, 70, 55, 85
                        )
                        val count = workoutMins.size
                        val barWidth = 3.5.dp.toPx()
                        val step = size.width / count.toFloat()
                        val barRadius = CornerRadius(barWidth / 2f, barWidth / 2f)

                        workoutMins.forEachIndexed { i, mins ->
                            val x = i * step + (step - barWidth) / 2f
                            val hRatio = (mins / 125f).coerceIn(0.15f, 0.95f)
                            val h = size.height * hRatio
                            val y = size.height - h

                            val barColor = when {
                                mins >= 90 -> Color(0xFF0284C7)
                                mins >= 30 -> Color(0xFF38BDF8)
                                else -> Color(0xFFCBD5E1)
                            }

                            drawRoundRect(
                                color = barColor,
                                topLeft = Offset(x, y),
                                size = Size(barWidth, h),
                                cornerRadius = barRadius
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // X-Axis Marks
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("1", "5", "10", "15", "20", "25", "30").forEach { mark ->
                        Text(text = mark, fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            // 4-Tier Workout Intensity Legend
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendDotItem(color = Color(0xFF0284C7), label = "High (≥90m)", value = "12d")
                LegendDotItem(color = Color(0xFF38BDF8), label = "Med (30-89m)", value = "14d")
                LegendDotItem(color = Color(0xFFF59E0B), label = "Low (<30m)", value = "3d")
                LegendDotItem(color = Color(0xFFCBD5E1), label = "Rest", value = "1d")
            }

            // Bottom 4-Card Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MiniStatCard(
                    icon = "⏱️",
                    title = "Total Workouts",
                    value = "22",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "🏆",
                    title = "Best Session",
                    value = "2.0 hr",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "🔥",
                    title = "Current Streak",
                    value = "8 Days",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "📊",
                    title = "Weekly Avg",
                    value = "3.4 days",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// =============================================================================
// 4. MASTER PAYMENT CARD (Coral Pink #FEEDED)
// =============================================================================
@Composable
fun MasterPaymentCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    onViewReceiptClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFEEDED))
            .border(1.2.dp, Color(0xFFFECDD3), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Left 3D Illustration + Right Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Wallet & Coins Illustration
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_payment_wallet),
                    contentDescription = "Payment 3D Wallet",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(86.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Right Column Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Top: Wallet Badge + Title + Dropdown Pill
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Payment",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 12 Months",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Plan History Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White)
                                .border(0.8.dp, Color(0xFFFECDD3), RoundedCornerShape(50.dp))
                                .clickable { onClick() }
                                .padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "Plan History",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE11D48)
                            )
                            Text(
                                text = "›",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE11D48)
                            )
                        }
                    }

                    // Key Metric: ₹2,800 + [ ! Due Now ]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "₹2,800",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFDC2626)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFEF4444))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "! Due Now",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "Due on 16 Oct 2026",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B)
                    )

                    // Action Buttons Row: WhatsApp, Collect Payment, Receipt
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // WhatsApp
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF22C55E))
                                .clickable { /* WhatsApp */ }
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(text = "💬", fontSize = 8.sp)
                            Text(
                                text = "WhatsApp",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        // Collect Payment CTA
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFEF4444))
                                .clickable { /* Collect */ }
                                .padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "₹ Collect Payment",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        // View Receipt
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White)
                                .border(0.8.dp, Color(0xFFFECDD3), RoundedCornerShape(50.dp))
                                .clickable { onViewReceiptClick() }
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = Color(0xFF0F172A),
                                modifier = Modifier.size(9.dp)
                            )
                            Text(
                                text = "Receipt",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            // Plan Intelligence & 3-Month Payment Schedule Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.88f))
                    .border(0.8.dp, Color(0xFFFECDD3), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(text = "👑 Gold Plan", fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text(text = "• Monthly (Valid till 31 Oct 2026)", fontSize = 7.5.sp, color = Color(0xFF64748B))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Box(Modifier.size(4.5.dp).clip(CircleShape).background(Color(0xFF0284C7)))
                        Text(text = "Auto Pay: Enabled", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().height(0.6.dp).background(Color(0xFFF1F5F9)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Last Paid (Paid)", fontSize = 6.8.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text(text = "₹2,800", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                        Text(text = "15 Sep 2026", fontSize = 6.5.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))
                    Column {
                        Text(text = "Current Due (Due)", fontSize = 6.8.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                        Text(text = "₹2,800", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
                        Text(text = "16 Oct 2026", fontSize = 6.5.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))
                    Column {
                        Text(text = "Next Due (Scheduled)", fontSize = 6.8.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Text(text = "₹2,800", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF475569))
                        Text(text = "16 Nov 2026", fontSize = 6.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                    }
                }
            }

            // 12-Month Capsule Bar Chart (Jan to Dec, ₹3k, ₹2k, ₹1.5k, 0)
            Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    // Left Y-Axis
                    Column(
                        modifier = Modifier.height(52.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "₹3k", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹2k", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹1.5k", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "0", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 12 Capsule Bars Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        // Status: 1 = Paid (green), 2 = Plan Changed (indigo), 3 = Due Soon (amber), 4 = Due Now (red), 0 = No Data (grey)
                        val months = listOf(
                            Pair(1, 0.75f), // Jan Paid
                            Pair(1, 0.85f), // Feb Paid
                            Pair(1, 0.80f), // Mar Paid
                            Pair(1, 0.80f), // Apr Paid
                            Pair(1, 0.70f), // May Paid
                            Pair(2, 0.65f), // Jun Plan Changed
                            Pair(1, 0.85f), // Jul Paid
                            Pair(1, 0.70f), // Aug Paid
                            Pair(2, 0.75f), // Sep Plan Changed
                            Pair(4, 0.85f), // Oct Due Now
                            Pair(3, 0.50f), // Nov Due Soon
                            Pair(0, 0.30f)  // Dec Unpaid
                        )
                        val count = months.size
                        val barWidth = 8.dp.toPx()
                        val step = size.width / count.toFloat()
                        val barRadius = CornerRadius(barWidth / 2f, barWidth / 2f)

                        months.forEachIndexed { i, pair ->
                            val x = i * step + (step - barWidth) / 2f
                            val h = size.height * pair.second
                            val y = size.height - h

                            // Draw subtle background track
                            drawRoundRect(
                                color = Color(0x15000000),
                                topLeft = Offset(x, 0f),
                                size = Size(barWidth, size.height),
                                cornerRadius = barRadius
                            )

                            val color = when (pair.first) {
                                1 -> Color(0xFF10B981)
                                2 -> Color(0xFF6366F1)
                                3 -> Color(0xFFF59E0B)
                                4 -> Color(0xFFEF4444)
                                else -> Color(0xFFCBD5E1)
                            }

                            drawRoundRect(
                                color = color,
                                topLeft = Offset(x, y),
                                size = Size(barWidth, h),
                                cornerRadius = barRadius
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // X-Axis Marks: Jan..Dec
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach { m ->
                        Text(text = m, fontSize = 6.5.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            // Legend Row: Paid 8 months  Partial 2 months  Due 2 months  No Data -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendDotItem(color = Color(0xFF10B981), label = "Paid & Active", value = "(8 mo)")
                LegendDotItem(color = Color(0xFF6366F1), label = "Plan Changed", value = "(2 mo)")
                LegendDotItem(color = Color(0xFFEF4444), label = "Due Now", value = "(1 mo)")
                LegendDotItem(color = Color(0xFFF59E0B), label = "Due Soon", value = "(1 mo)")
            }

            // Bottom 4-Card Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MiniStatCard(
                    icon = "👛",
                    title = "Total Paid",
                    value = "₹12,600",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "📅",
                    title = "Avg / Month",
                    value = "₹1,050",
                    modifier = Modifier.weight(1f)
                )
                MiniStatCard(
                    icon = "🕒",
                    title = "Next Due",
                    value = "16 Oct 2026\n(Due Now)",
                    valueColor = Color(0xFFDC2626),
                    modifier = Modifier.weight(1.15f)
                )
                MiniStatCard(
                    icon = "🟢",
                    title = "On Time",
                    value = "91%\n(10/11 mo)",
                    modifier = Modifier.weight(1.05f)
                )
            }
        }
    }
}

// =============================================================================
// 5. MASTER BODY PROGRESS CARD (Soft Lavender #E7D6F5)
// =============================================================================
@Composable
fun MasterBodyProgressCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE7D6F5))
            .border(1.2.dp, Color(0xFFDDD6FE), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Left 3D Illustration + Right Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Scale & Cloud Illustration
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_body_scale),
                    contentDescription = "Body 3D Scale",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(86.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Right Column Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Top: Figure Badge + Title + Dropdown Pill
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
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF8B5CF6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🧘", fontSize = 12.sp)
                            }
                            Column {
                                Text(
                                    text = "Body Progress",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Last 12 Months",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        TimeframeDropdownPill(text = "Last 6 Mo")
                    }


                    // Key Metric Row: 69.5 kg Current Weight + Delta Pill on left, 3 Numerical Breakdowns on right
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column {
                                Text(
                                    text = "69.5 kg",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF7C3AED)
                                )
                                Text(
                                    text = "Current Weight",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "↓ 2.5 kg",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D)
                                    )
                                }
                                Text(
                                    text = "vs Aug (-3.5%)",
                                    fontSize = 7.5.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Right 3 Key Numerical Pills (Muscle, Body Fat, Waist)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Muscle", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                                Text("57.2 kg", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                                Text("↑ 1.4 kg", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Body Fat", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                                Text("17.6 %", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFF97316))
                                Text("↓ 1.2 %", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Waist", fontSize = 7.5.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                                Text("81 cm", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF8B5CF6))
                                Text("↓ 4 cm", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Legend Row: Weight (kg)  Muscle (kg)  Body Fat (%)  Waist (cm)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendDotItem(color = Color(0xFF10B981), label = "Weight (kg)", value = "")
                        LegendDotItem(color = Color(0xFF0284C7), label = "Muscle (kg)", value = "")
                        LegendDotItem(color = Color(0xFFF97316), label = "Body Fat (%)", value = "")
                        LegendDotItem(color = Color(0xFF8B5CF6), label = "Waist (cm)", value = "")
                    }
                }
            }

            // 12-Month Multi-Line Spline Chart
            Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(65.dp)) {
                    // Left Y-Axis (90, 60, 30, 0)
                    Column(
                        modifier = Modifier.height(65.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "90", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "60", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "30", fontSize = 7.sp, color = Color(0xFF94A3B8))
                        Text(text = "0", fontSize = 7.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // 4 Multi-Line Splines Canvas
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(65.dp)
                    ) {
                        val count = 12
                        val step = size.width / (count - 1).toFloat()

                        // Normalized data points (0..1 where 0 is bottom, 1 is top)
                        val weightPts = listOf(0.72f, 0.78f, 0.74f, 0.73f, 0.76f, 0.74f, 0.72f, 0.73f, 0.72f, 0.70f, 0.70f, 0.72f)
                        val waistPts = listOf(0.68f, 0.69f, 0.70f, 0.68f, 0.67f, 0.66f, 0.65f, 0.64f, 0.65f, 0.64f, 0.63f, 0.64f)
                        val musclePts = listOf(0.56f, 0.55f, 0.58f, 0.59f, 0.57f, 0.56f, 0.58f, 0.57f, 0.56f, 0.58f, 0.57f, 0.58f)
                        val bodyFatPts = listOf(0.20f, 0.22f, 0.21f, 0.20f, 0.19f, 0.18f, 0.18f, 0.19f, 0.18f, 0.17f, 0.17f, 0.18f)

                        fun drawSpline(points: List<Float>, color: Color) {
                            val path = Path()
                            val coords = points.mapIndexed { i, p ->
                                Offset(i * step, size.height * (1f - p))
                            }
                            if (coords.isNotEmpty()) {
                                path.moveTo(coords[0].x, coords[0].y)
                                for (i in 0 until coords.size - 1) {
                                    val p0 = coords[i]
                                    val p1 = coords[i + 1]
                                    val cx = (p0.x + p1.x) / 2f
                                    path.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                                }
                                drawPath(
                                    path = path,
                                    color = color,
                                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                                )
                                // Draw circular data dots
                                coords.forEach { c ->
                                    drawCircle(color = color, radius = 2.5.dp.toPx(), center = c)
                                    drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = c)
                                }
                            }
                        }

                        // Draw horizontal subtle reference lines
                        listOf(0.33f, 0.66f, 1.0f).forEach { r ->
                            val y = size.height * (1f - r)
                            drawLine(
                                color = Color(0x20000000),
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 0.8.dp.toPx()
                            )
                        }

                        drawSpline(weightPts, Color(0xFF10B981))
                        drawSpline(waistPts, Color(0xFF8B5CF6))
                        drawSpline(musclePts, Color(0xFF0284C7))
                        drawSpline(bodyFatPts, Color(0xFFF97316))
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // X-Axis Marks: Jan..Dec
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach { m ->
                        Text(text = m, fontSize = 6.5.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            // Insight Pill Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "✓", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF16A34A))
                    Text(
                        text = "Good consistency this month",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "BMI: 22.8",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B21A8)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "Normal",
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    }
                }
            }

            // Stat Cards - Row 1 (4 Cards: Weight, Muscle, Body Fat, Waist)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MetricCardWithDelta(
                    icon = "🏷️",
                    value = "69.5 kg",
                    delta = "↓ 2.5 kg",
                    deltaColor = Color(0xFF16A34A),
                    label = "Weight",
                    modifier = Modifier.weight(1f)
                )
                MetricCardWithDelta(
                    icon = "💪",
                    value = "57.2 kg",
                    delta = "↑ 1.4 kg",
                    deltaColor = Color(0xFF0284C7),
                    label = "Muscle Mass",
                    modifier = Modifier.weight(1.1f)
                )
                MetricCardWithDelta(
                    icon = "%",
                    value = "17.6 %",
                    delta = "↓ 1.2 %",
                    deltaColor = Color(0xFF16A34A),
                    label = "Body Fat",
                    modifier = Modifier.weight(1f)
                )
                MetricCardWithDelta(
                    icon = "📏",
                    value = "81 cm",
                    delta = "↓ 4 cm",
                    deltaColor = Color(0xFF16A34A),
                    label = "Waist",
                    modifier = Modifier.weight(1f)
                )
            }

            // Muscle Focus Distribution & Top PR Exercise Strip (from image)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.88f))
                    .border(0.8.dp, Color(0xFFDDD6FE), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Top PR Lift
                Column {
                    Text(text = "🏋️ Top Lift: Bench Press", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Text(text = "65 ➔ 67.5 kg (+4% PR)", fontSize = 7.2.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                }

                Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))

                // Right: Muscle Focus Sets Distribution
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(text = "🎯 Current Focus:", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        Text(text = "Legs 43%", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color(0xFF7C3AED))
                    }
                    Text(
                        text = "Arms 18% • Shoulders 12% • Back 11% • Chest 7%",
                        fontSize = 6.8.sp,
                        color = Color(0xFF475569),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Stat Cards - Row 2 (4 Cards: Strength, Endurance, Overall Progress, Goal)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MetricCardWithDelta(
                    icon = "📊",
                    value = "↑ 18%",
                    delta = "(Est. 1 RM)",
                    deltaColor = Color(0xFF64748B),
                    label = "Strength",
                    modifier = Modifier.weight(1f)
                )
                MetricCardWithDelta(
                    icon = "❤️",
                    value = "↑ 22%",
                    delta = "(Avg Cardio Time)",
                    deltaColor = Color(0xFF64748B),
                    label = "Endurance",
                    modifier = Modifier.weight(1.1f)
                )
                MiniStatBadgeCard(
                    icon = "📈",
                    title = "Overall Progress",
                    badgeText = "On Track",
                    badgeBg = Color(0xFF10B981),
                    modifier = Modifier.weight(1.1f)
                )
                MiniStatCard(
                    icon = "🎯",
                    title = "Goal",
                    value = "Lean Muscle\n(3 months)",
                    modifier = Modifier.weight(1.1f)
                )
            }
        }
    }
}

// =============================================================================
// 6. MASTER FOOTER BANNER ("Consistent Steps, Brighter Days")
// =============================================================================
@Composable
fun MasterFooterBanner(
    onKeepGoingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF5D22B6),
                        Color(0xFF4A188C),
                        Color(0xFF6D28D9)
                    )
                )
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left 3D Dumbbell & Slogan
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_footer_dumbbell),
                    contentDescription = "Footer Dumbbell",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(42.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Consistent Steps, ",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Brighter Days 彡",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFACC15)
                    )
                }
            }

            // Right "Keep Going! ->" Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFFACC15))
                    .clickable { onKeepGoingClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Keep Going!",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "➔",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}

// =============================================================================
// REUSABLE HELPER ATOMS
// =============================================================================

@Composable
private fun TimeframeDropdownPill(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
            .padding(horizontal = 7.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = text,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF64748B),
            modifier = Modifier.size(10.dp)
        )
    }
}

@Composable
private fun LegendDotItem(color: Color, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 7.5.sp,
            color = Color(0xFF64748B)
        )
        if (value.isNotEmpty()) {
            Text(
                text = value,
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }
    }
}

@Composable
private fun MiniStatCard(
    icon: String,
    title: String,
    value: String,
    valueColor: Color = Color(0xFF0F172A),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 13.sp)
            Column {
                Text(
                    text = title,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
                Text(
                    text = value,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    color = valueColor,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Composable
private fun MetricCardWithDelta(
    icon: String,
    value: String,
    delta: String,
    deltaColor: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 12.sp)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = value,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    Text(
                        text = delta,
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = deltaColor,
                        maxLines = 1
                    )
                }
                Text(
                    text = label,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun MiniStatBadgeCard(
    icon: String,
    title: String,
    badgeText: String,
    badgeBg: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 12.sp)
            Column {
                Text(
                    text = title,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(badgeBg)
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun AttendanceHollowCircle(day: Int, status: Int) {
    val (borderColor, bgColor, textColor) = when (status) {
        1 -> Triple(Color(0xFF10B981), Color(0x1810B981), Color(0xFF047857)) // Present (Green)
        2 -> Triple(Color(0xFFF59E0B), Color(0x18F59E0B), Color(0xFFB45309)) // Late (Amber)
        0 -> Triple(Color(0xFFEF4444), Color(0x18EF4444), Color(0xFFDC2626)) // Absent (Red)
        3 -> Triple(Color(0xFF8B5CF6), Color(0x188B5CF6), Color(0xFF6D28D9)) // Holiday (Purple)
        4 -> Triple(Color(0xFFCBD5E1), Color(0xFFF1F5F9), Color(0xFF94A3B8)) // Sunday (Disabled)
        else -> Triple(Color(0xFFCBD5E1), Color.Transparent, Color(0xFFCBD5E1)) // Upcoming (Faint Outline)
    }

    Box(
        modifier = Modifier
            .size(19.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(1.2.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$day",
            fontSize = 7.5.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun AttendanceLegendItem(label: String, count: String, color: Color, isHollow: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(if (isHollow) Color.Transparent else color)
                .then(if (isHollow) Modifier.border(0.9.dp, color, CircleShape) else Modifier)
        )
        Text(
            text = "$label ($count)",
            fontSize = 7.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF475569)
        )
    }
}
