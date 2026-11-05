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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.design.AquaGlassTokens
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

/**
 * 1. Top Bar: Hamburger Menu, BAD GYM Branding, Date Capsule, Notification Bell with Badge
 */
@Composable
fun DashboardTopBar(
    dateText: String = "02 Oct 2026",
    notificationCount: Int = 1,
    onMenuClick: () -> Unit = {},
    onDateClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Hamburger Menu Button (Squircle White Glass)
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color.White.copy(alpha = 0.88f))
                .border(1.dp, Color.White, RoundedCornerShape(13.dp))
                .clickable { onMenuClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color(0xFF072C4F),
                modifier = Modifier.size(21.dp)
            )
        }

        // Center-Left: BAD GYM Brand (Single Line: BAD Navy + GYM Emerald)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BAD ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF072C4F),
                    letterSpacing = 0.2.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = "GYM",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF00A336),
                    letterSpacing = 0.2.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }
            Text(
                text = "Member Intelligence",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A4363),
                maxLines = 1,
                softWrap = false
            )
        }

        // Right Group: Date Capsule, Notification Bell, More Options
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // Date Capsule Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White.copy(alpha = 0.90f))
                    .border(1.dp, Color.White, RoundedCornerShape(50.dp))
                    .clickable { onDateClick() }
                    .padding(horizontal = 7.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF072C4F),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = dateText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF072C4F),
                    maxLines = 1,
                    softWrap = false
                )
            }

            // Notification Bell (Squircle White Glass with Red Dot Badge)
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color.White.copy(alpha = 0.88f))
                    .border(1.dp, Color.White, RoundedCornerShape(13.dp))
                    .clickable { onNotificationClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF072C4F),
                    modifier = Modifier.size(19.dp)
                )

                if (notificationCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-7).dp, y = 7.dp)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                    )
                }
            }

            // More [ ••• ] Button (Squircle White Glass)
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color.White.copy(alpha = 0.88f))
                    .border(1.dp, Color.White, RoundedCornerShape(13.dp))
                    .clickable { onMoreClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More Options",
                    tint = Color(0xFF072C4F),
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}

/**
 * 2. Member Centerpiece Hero:
 * - Circular Avatar with Luminous Neon Cyan/Mint Halo Ring
 * - Gold 3D Crown Badge on Top-Right Edge
 * - "Active" Emerald Pill under Avatar
 * - "Ashwini Singh 👑"
 * - "BG305 | Prime Plan"
 * - "👑 25 Days Left" Pill Badge with Gold Border
 * - "Prime Plan" Subtitle
 */
@Composable
fun MemberCenterpieceHero(
    name: String = "Ashwini Singh",
    memberCode: String = "BG305",
    planName: String = "Prime Plan",
    daysLeft: Int = 25,
    isActive: Boolean = true,
    avatarResId: Int = R.drawable.avatar_ashwini,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar Box with Glowing Halo Ring & Badges
        Box(
            modifier = Modifier.size(108.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glowing Halo Ring Canvas
            Canvas(modifier = Modifier.size(104.dp)) {
                // Outer Cyan-Mint Neon Gradient Ring
                drawCircle(
                    brush = Brush.sweepGradient(
                        listOf(
                            Color(0xFF00E676),
                            Color(0xFF00E5FF),
                            Color(0xFF10B981),
                            Color(0xFF00F5A0),
                            Color(0xFF00E676)
                        )
                    ),
                    radius = size.minDimension / 2f - 2f,
                    style = Stroke(width = 3.5.dp.toPx())
                )
            }

            // Circular Cropped Profile Picture
            Image(
                painter = painterResource(id = avatarResId),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )

            // Gold Crown Badge (Top-Right / 2 o'clock)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = 2.dp)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFBBF24), Color(0xFFF59E0B), Color(0xFFD97706))
                        )
                    )
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👑",
                    fontSize = 13.sp
                )
            }

            // "Active" Emerald Status Pill (Bottom Center)
            if (isActive) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 10.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF10B981))
                        .border(1.5.dp, Color.White, RoundedCornerShape(50.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "Active",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Name with inline Gold Crown
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "👑",
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Code and Plan
        Text(
            text = "$memberCode | $planName",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // "25 Days Left" Pill Badge with Gold Crown and Coral Text
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White.copy(alpha = 0.9f))
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(Color(0xFFFDE68A), Color(0xFFFECDD3))),
                    RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "👑",
                fontSize = 13.sp
            )
            Text(
                text = "$daysLeft Days Left",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE11D48)
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = planName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569)
        )
    }
}

/**
 * 3. Bento Card 1: Attendance (Top-Left)
 * - Radiant Spring Green -> Deep Mint Gradient
 * - "85%", "18/21 Days", "+12%"
 * - Mini vertical bar chart with connected trendline
 * - Circular arrow chevron button
 */
@Composable
fun AttendanceBentoCard(
    percentage: String = "85%",
    daysRatio: String = "18/21 Days",
    trend: String = "+12%",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AquaGlassTokens.AttendanceCardGradient)
            .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Squircle Calendar Icon + "Attendance" Label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "Attendance",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.95f)
                )
            }

            // Center: Big Percentage & Subtext
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = percentage,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = daysRatio,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            // Bottom Row: Mini Bar Chart + "+12%" Pill + Chevron Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Mini ascending bar chart with trendline
                Canvas(modifier = Modifier.size(width = 46.dp, height = 24.dp)) {
                    val barWidth = 4.dp.toPx()
                    val heights = listOf(0.3f, 0.45f, 0.4f, 0.6f, 0.5f, 0.8f, 0.95f)
                    val spacing = (size.width - (heights.size * barWidth)) / (heights.size - 1)

                    heights.forEachIndexed { index, fraction ->
                        val x = index * (barWidth + spacing)
                        val h = size.height * fraction
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.85f),
                            topLeft = Offset(x, size.height - h),
                            size = Size(barWidth, h),
                            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                        )
                    }
                }

                // Trend Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "↑ $trend",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Chevron Button
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * 4. Bento Card 2: Payment (Top-Right)
 * - Amber -> Flame Orange Gradient
 * - "₹2,800", "Due Amount"
 * - Alert Pill "! 25 Days Left"
 * - Circular arrow chevron button
 */
@Composable
fun PaymentBentoCard(
    amount: String = "₹2,800",
    statusText: String = "Due Amount",
    alertText: String = "25 Days Left",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AquaGlassTokens.PaymentCardGradient)
            .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Squircle Wallet Icon + "Payment" Label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "Payment",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.95f)
                )
            }

            // Center: Big Amount & Due Amount
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = amount,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = statusText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            // Bottom Row: Warning Alert Pill + Mini Bars + Chevron Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Alert Pill "! 25 Days Left"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFDC2626).copy(alpha = 0.85f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "! $alertText",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                // Mini bar indicator
                Canvas(modifier = Modifier.size(width = 38.dp, height = 20.dp)) {
                    val barWidth = 3.5.dp.toPx()
                    val heights = listOf(0.8f, 0.6f, 0.9f, 0.4f, 0.5f)
                    val spacing = (size.width - (heights.size * barWidth)) / (heights.size - 1)

                    heights.forEachIndexed { index, fraction ->
                        val x = index * (barWidth + spacing)
                        val h = size.height * fraction
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.85f),
                            topLeft = Offset(x, size.height - h),
                            size = Size(barWidth, h),
                            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                        )
                    }
                }

                // Chevron Button
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * 5. Bento Card 3: Workout (Bottom-Left)
 * - Cyan -> Sky Blue -> Royal Cobalt Gradient
 * - "47 min", "Avg/Day", "+8%"
 * - Mini equalizer bars preview
 */
@Composable
fun WorkoutBentoCard(
    duration: String = "47 min",
    subLabel: String = "Avg/Day",
    trend: String = "+8%",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AquaGlassTokens.WorkoutCardGradient)
            .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Squircle Dumbbell Icon + "Workout" Label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "Workout",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.95f)
                )
            }

            // Center: Big Duration & Avg/Day
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = duration,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = subLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            // Bottom Row: "+8%" Pill + Mini Equalizer Bars
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Trend Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "↑ $trend",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Mini equalizer vertical bars
                Canvas(modifier = Modifier.size(width = 60.dp, height = 22.dp)) {
                    val barWidth = 3.5.dp.toPx()
                    val heights = listOf(0.4f, 0.7f, 0.5f, 0.85f, 0.6f, 0.9f, 0.75f, 0.5f)
                    val spacing = (size.width - (heights.size * barWidth)) / (heights.size - 1)

                    heights.forEachIndexed { index, fraction ->
                        val x = index * (barWidth + spacing)
                        val h = size.height * fraction
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.85f),
                            topLeft = Offset(x, size.height - h),
                            size = Size(barWidth, h),
                            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                        )
                    }
                }
            }
        }
    }
}

/**
 * 6. Bento Card 4: Body Progress (Bottom-Right)
 * - Magenta -> Vivid Fuchsia -> Radiant Violet Gradient
 * - "69.5 kg", "-0.7 kg"
 * - Mini wave line graph
 */
@Composable
fun BodyProgressBentoCard(
    weight: String = "69.5 kg",
    difference: String = "-0.7 kg",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AquaGlassTokens.BodyProgressCardGradient)
            .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Squircle Person Icon + "Body Progress" Label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "Body Progress",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.95f)
                )
            }

            // Center: Big Weight & Difference
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = weight,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "↓ $difference",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            // Bottom Row: Mini sparkline line graph
            Canvas(modifier = Modifier.fillMaxWidth().height(22.dp)) {
                val points = listOf(0.4f, 0.6f, 0.5f, 0.8f, 0.45f, 0.7f, 0.35f, 0.6f, 0.2f)
                val step = size.width / (points.size - 1)

                val path = Path()
                points.forEachIndexed { index, fraction ->
                    val x = index * step
                    val y = size.height * (1f - fraction)
                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = Color.White,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )

                // Fill underneath
                val fillPath = Path().apply {
                    addPath(path)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.35f), Color.Transparent)
                    )
                )
            }
        }
    }
}

/**
 * 7. Detailed Overview Card (Large Glass Container):
 * - Header: BarChart Icon + "Detailed Overview", "View All >"
 * - Filter Pills: "7D", "30D" (active), "3M", "6M"
 * - Split Analytics:
 *    - Left: 16 Gradient Bars + Overlaid Spline Curve + Connected Nodes + Tooltip "85% (18/21)"
 *    - Right: 4 Micro Summary Metric Pills (Attendance, Workout, Payment, Body Weight)
 * - Bottom Fitness Target Pills:
 *    - 🏆 Goal: Fit & Strong
 *    - 🎯 Target Weight: 68 kg
 *    - 🔥 Burn Calories: 500 kcal/day
 */
@Composable
fun DetailedOverviewCard(
    selectedTimeframe: String = "30D",
    onTimeframeSelect: (String) -> Unit = {},
    onViewAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.88f))
            .border(1.5.dp, Color(0x66A7F3D0), RoundedCornerShape(28.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Icon + Title and "View All >"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Detailed Overview",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Row(
                    modifier = Modifier.clickable { onViewAllClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "View All",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A34)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF1E3A34),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Timeframe Selector Pills ("7D", "30D", "3M", "6M")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("7D", "30D", "3M", "6M").forEach { timeframe ->
                    val isActive = timeframe == selectedTimeframe
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (isActive) Color(0xFF10B981) else Color.White.copy(alpha = 0.7f)
                            )
                            .border(
                                1.dp,
                                if (isActive) Color.Transparent else Color(0x33A7F3D0),
                                RoundedCornerShape(50.dp)
                            )
                            .clickable { onTimeframeSelect(timeframe) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = timeframe,
                            fontSize = 13.sp,
                            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.SemiBold,
                            color = if (isActive) Color.White else Color(0xFF475569)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Split Layout: Left Chart Canvas + Right Metric Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Dual Bar + Spline Curve + Tooltip Canvas
                Box(
                    modifier = Modifier
                        .weight(1.35f)
                        .height(168.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val barData = listOf(
                            0.35f, 0.45f, 0.4f, 0.6f, 0.55f, 0.7f, 0.5f, 0.65f,
                            0.6f, 0.85f, 0.75f, 0.6f, 0.55f, 0.7f, 0.75f, 0.85f, 0.95f
                        )
                        val barWidth = 5.dp.toPx()
                        val spacing = (size.width - (barData.size * barWidth)) / (barData.size - 1)

                        // 1. Draw Bars (Gradient Cyan -> Emerald)
                        barData.forEachIndexed { index, fraction ->
                            val x = index * (barWidth + spacing)
                            val h = size.height * 0.75f * fraction
                            val y = size.height - h - 16.dp.toPx()

                            drawRoundRect(
                                brush = Brush.verticalGradient(
                                    listOf(Color(0xFF00E676), Color(0xFF00E5FF))
                                ),
                                topLeft = Offset(x, y),
                                size = Size(barWidth, h),
                                cornerRadius = CornerRadius(2.5.dp.toPx(), 2.5.dp.toPx())
                            )
                        }

                        // 2. Draw Spline Curve across the bars
                        val splinePath = Path()
                        val nodes = mutableListOf<Offset>()
                        barData.forEachIndexed { index, fraction ->
                            val x = index * (barWidth + spacing) + barWidth / 2f
                            val y = size.height - (size.height * 0.75f * fraction) - 16.dp.toPx()
                            nodes.add(Offset(x, y))
                            if (index == 0) splinePath.moveTo(x, y) else splinePath.lineTo(x, y)
                        }

                        drawPath(
                            path = splinePath,
                            color = Color(0xFF00E5FF),
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // 3. Draw Nodes on Spline
                        nodes.forEach { point ->
                            drawCircle(
                                color = Color.White,
                                radius = 2.5.dp.toPx(),
                                center = point
                            )
                        }

                        // Base baseline
                        drawLine(
                            color = Color(0xFF00E5FF).copy(alpha = 0.8f),
                            start = Offset(0f, size.height - 16.dp.toPx()),
                            end = Offset(size.width, size.height - 16.dp.toPx()),
                            strokeWidth = 2.dp.toPx()
                        )
                    }

                    // Floating Tooltip over active bar
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(x = 6.dp, y = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF059669))
                            .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "85%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "(18/21)",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    // X-Axis labels at bottom: '1  1  1  1  1...'
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        repeat(16) {
                            Text(
                                text = "1",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Right: 4 Stacked Metric Micro-Pills
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MetricSummaryPill(
                        icon = Icons.Default.CalendarToday,
                        iconTint = Color(0xFF10B981),
                        label = "Attendance",
                        value = "85%",
                        trend = "↑ +12%",
                        trendColor = Color(0xFF10B981)
                    )
                    MetricSummaryPill(
                        icon = Icons.Default.FitnessCenter,
                        iconTint = Color(0xFF0284C7),
                        label = "Workout",
                        value = "47 min",
                        trend = "↑ +8%",
                        trendColor = Color(0xFF10B981)
                    )
                    MetricSummaryPill(
                        icon = Icons.Default.AccountBalanceWallet,
                        iconTint = Color(0xFFF59E0B),
                        label = "Payment",
                        value = "₹2,800",
                        trend = "↓ -5%",
                        trendColor = Color(0xFFEF4444)
                    )
                    MetricSummaryPill(
                        icon = Icons.Default.Person,
                        iconTint = Color(0xFF8B5CF6),
                        label = "Body Weight",
                        value = "69.5 kg",
                        trend = "↓ -0.7 kg",
                        trendColor = Color(0xFFEF4444)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom 3 Fitness Targets Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Goal: Fit & Strong
                FitnessMilestoneCapsule(
                    emoji = "🏆",
                    label = "Goal",
                    value = "Fit & Strong",
                    modifier = Modifier.weight(1f)
                )

                // 2. Target Weight: 68 kg
                FitnessMilestoneCapsule(
                    emoji = "🎯",
                    label = "Target Weight",
                    value = "68 kg",
                    modifier = Modifier.weight(1f)
                )

                // 3. Burn Calories: 500 kcal/day
                FitnessMilestoneCapsule(
                    emoji = "🔥",
                    label = "Burn Calories",
                    value = "500 kcal/day",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Micro Pill for Detailed Overview Right Stack
 */
@Composable
fun MetricSummaryPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    trend: String,
    trendColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(1.dp, Color(0x33A7F3D0), RoundedCornerShape(50.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(14.dp)
        )

        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = trend,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = trendColor
                )
            }
        }
    }
}

/**
 * Capsule Pill for Bottom Goals
 */
@Composable
fun FitnessMilestoneCapsule(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(1.dp, Color(0x44A7F3D0), RoundedCornerShape(50.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = emoji,
            fontSize = 16.sp
        )
        Column {
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF64748B)
            )
            Text(
                text = value,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }
    }
}

/**
 * 8. Floating Glass Bottom Navigation Dock:
 * - Home (Active emerald glowing capsule)
 * - Progress (Dumbbell icon)
 * - Goals (Trophy icon)
 * - More (4-Squares Grid icon)
 */
@Composable
fun FloatingGlassBottomDock(
    activeTab: String = "Home",
    onTabSelect: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .height(62.dp)
            .shadow(12.dp, RoundedCornerShape(28.dp), spotColor = Color(0x3300A336))
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.92f))
            .border(1.5.dp, Color.White, RoundedCornerShape(28.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Tab 1: Home (Active Glowing Capsule)
            val isHome = activeTab == "Home"
            if (isHome) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF00E676), Color(0xFF00A336))
                            )
                        )
                        .clickable { onTabSelect("Home") }
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                        Text(
                            text = "Home",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            } else {
                NavDockItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    isActive = false,
                    onClick = { onTabSelect("Home") }
                )
            }

            // Tab 2: Attendance
            NavDockItem(
                icon = Icons.Default.CalendarToday,
                label = "Attendance",
                isActive = activeTab == "Attendance",
                onClick = { onTabSelect("Attendance") }
            )

            // Tab 3: Workout
            NavDockItem(
                icon = Icons.Default.FitnessCenter,
                label = "Workout",
                isActive = activeTab == "Workout",
                onClick = { onTabSelect("Workout") }
            )

            // Tab 4: Payment
            NavDockItem(
                icon = Icons.Default.AccountBalanceWallet,
                label = "Payment",
                isActive = activeTab == "Payment",
                onClick = { onTabSelect("Payment") }
            )

            // Tab 5: More
            NavDockItem(
                icon = Icons.Default.GridView,
                label = "More",
                isActive = activeTab == "More",
                onClick = { onTabSelect("More") }
            )
        }
    }
}

@Composable
fun NavDockItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) Color(0xFF00A336) else Color(0xFF1E3A5F),
            modifier = Modifier.size(19.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isActive) Color(0xFF00A336) else Color(0xFF1E3A5F),
            maxLines = 1,
            softWrap = false
        )
    }
}
