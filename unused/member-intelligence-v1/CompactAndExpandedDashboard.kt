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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState

// =========================================================================
// 1. COMPACT VIEW (Maximized Space, Enlarged Fonts & Image, 360dp)
// =========================================================================

/**
 * Compact View of BAD GYM Member Intelligence Card
 * - Maximized space utilization across the full screen width (edge-to-edge)
 * - Exact Ashwini Hero Card with all reference data integrated around the avatar
 * - Full-width 2x2 Bento cards (Attendance, Workout, Payment, Body Progress)
 * - Full-width 2-row Quick Widgets Grid with enlarged emojis & text
 */
@Composable
fun CompactIntelligenceCard(
    data: MemberCardData,
    onMenuClick: () -> Unit = {},
    onCardClick: (String) -> Unit = {},
    onExpandClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.94f))
            .border(1.5.dp, Color(0x99A7F3D0), RoundedCornerShape(24.dp))
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Menu, BAD GYM / Member Intelligence, Date, Bell, More ...
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
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0x40A7F3D0), RoundedCornerShape(12.dp))
                            .clickable { onMenuClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color(0xFF0A3F2C),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "BAD GYM",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF08412B),
                            letterSpacing = 0.4.sp
                        )
                        Text(
                            text = "Member Intelligence",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A34)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Date capsule
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0x40A7F3D0), RoundedCornerShape(50.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "02 Oct 2026",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    // Bell with badge
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0x40A7F3D0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(22.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-2).dp, y = 2.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444))
                        )
                    }

                    // More ...
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0x40A7F3D0), CircleShape)
                            .clickable { onExpandClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))

            // 1. ALL 8 CANONICAL THEMED MEMBER INTELLIGENCE PRESETS (Live Interactive Switcher)
            ThemedMemberIntelligenceCard(
                data = data,
                onMenuClick = onMenuClick,
                onCardClick = onCardClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. FULL-WIDTH QUICK WIDGETS GRID (2 Rows x 3 Columns)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    QuickWidgetChip(emoji = "\uD83D\uDD25", label = "Activity", value = "48 min", modifier = Modifier.weight(1f))
                    QuickWidgetChip(emoji = "\uD83C\uDFAF", label = "Goals", value = "4/5", modifier = Modifier.weight(1f))
                    QuickWidgetChip(emoji = "\uD83E\uDD57", label = "Nutrition", value = "3,250 kcal", modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    QuickWidgetChip(emoji = "\uD83D\uDCC5", label = "Plan", value = "25 Days", modifier = Modifier.weight(1f))
                    QuickWidgetChip(emoji = "\uD83C\uDFCB\uFE0F", label = "Trainer", value = "Rahul Sir", modifier = Modifier.weight(1f))
                    QuickWidgetChip(emoji = "\u23F0", label = "Next Session", value = "Today 6 PM", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// =========================================================================
// ROHIT MEHTA HERO CARD (Pixel-Perfect Reference Layout)
// =========================================================================

/**
 * Rohit Mehta Member Hero Card
 * Integrates:
 * - Left: Rohit's avatar with glowing concentric cyan/mint halo ring,
 *         Gold crown circular badge at top-right of avatar,
 *         Emerald green camera circular button at bottom-right of avatar.
 * - Center: Rohit Mehta, ID: BG315, Demographics (green Person icon + 29 yrs | Male),
 *           Emerald gloss active status pill with white glowing dot ("Active Member").
 * - Right: 270° neon emerald arc gauge displaying "55 Days Left" with dumbbell badge.
 * - Top-Right: Frosted circular 3-dots glass button (MoreVert).
 * - Bottom: Frosted white glass plan strip (👑 Premium Plan | ₹1,800 / month | Valid till 12 Dec 2026 | >).
 * - Background: Fluid mint/emerald glassmorphic wave with subtle athletic gym torso watermark.
 */
@Composable
fun RohitHeroCard(
    profile: MemberProfile = MemberCardData.sampleRohitMehta().profile,
    paymentPlan: PaymentPlanState = MemberCardData.sampleRohitMehta().paymentPlan,
    onAvatarClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCardClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE8FDF5),
                        Color(0xFFE0F7EF),
                        Color(0xFFD1FAE5),
                        Color(0xFFCCFBF1)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color(0xFF34D399),
                        Color(0xFF10B981).copy(alpha = 0.5f),
                        Color(0xFF6EE7B7)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(12.dp)
    ) {
        // Torso Watermark in Background
        Image(
            painter = painterResource(id = R.drawable.ic_gym_torso_silhouette),
            contentDescription = null,
            modifier = Modifier
                .size(170.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 24.dp, y = 8.dp)
                .alpha(0.09f),
            colorFilter = ColorFilter.tint(Color(0xFF047857))
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Section: Avatar Block | Middle Details | Right 270° Circular Gauge & Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Avatar with Glowing Mint/Cyan Halo Ring & Badges
                Box(
                    modifier = Modifier.size(84.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Concentric Glowing Halo Ring
                    Canvas(modifier = Modifier.size(82.dp)) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFF10B981),
                                    Color(0xFF00E5FF),
                                    Color(0xFF34D399),
                                    Color(0xFF059669),
                                    Color(0xFF10B981)
                                )
                            ),
                            radius = size.minDimension / 2f - 2f,
                            style = Stroke(width = 3.5.dp.toPx())
                        )
                    }

                    // Profile Image (Rohit Mehta portrait)
                    Image(
                        painter = painterResource(id = R.drawable.avatar_rohit),
                        contentDescription = profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                            .clickable { onAvatarClick() }
                    )

                    // Gold Crown Circular Badge (Top-Right of Avatar)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 4.dp, y = (-2).dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFFDE047), Color(0xFFF59E0B), Color(0xFFD97706))
                                )
                            )
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👑", fontSize = 13.sp)
                    }

                    // Green Camera Circular Button (Bottom-Right of Avatar)
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 3.dp, y = 1.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                            .border(1.5.dp, Color.White, CircleShape)
                            .clickable { onAvatarClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Edit Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                // 2. Middle Info Column (Name, ID, Demographics, Active Pill)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, end = 6.dp)
                ) {
                    Text(
                        text = profile.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        letterSpacing = 0.2.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ID: ${profile.memberCode}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${profile.age} yrs | ${profile.gender}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    // Active Member Pill (Glossy Emerald with glowing white dot)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF10B981), Color(0xFF059669))
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(50.dp))
                            .padding(horizontal = 9.dp, vertical = 3.5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Text(
                            text = if (profile.isActive) "Active Member" else "Inactive",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // 3. Right Area: 270° Circular Arc Gauge ("55 Days Left") & 3-Dots Menu Button
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Frosted Glass 3-Dots Button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.85f))
                            .border(1.dp, Color(0xFF94A3B8).copy(alpha = 0.35f), CircleShape)
                            .clickable { onMenuClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // 270° Circular Arc Gauge
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clickable { onCardClick("Attendance") },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(72.dp)) {
                            val strokeWidth = 5.dp.toPx()
                            val diameter = size.minDimension - strokeWidth
                            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                            val arcSize = Size(diameter, diameter)

                            // Subtle background track (270°)
                            drawArc(
                                color = Color(0xFF0F172A).copy(alpha = 0.08f),
                                startAngle = 135f,
                                sweepAngle = 270f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // Glowing emerald neon foreground arc (270°)
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color(0xFF10B981),
                                        Color(0xFF34D399),
                                        Color(0xFF00E5FF),
                                        Color(0xFF10B981)
                                    )
                                ),
                                startAngle = 135f,
                                sweepAngle = 270f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        // Center Metric Text: 55 Days Left
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${profile.daysLeft}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A),
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "Days\nLeft",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF475569),
                                lineHeight = 11.5.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Dumbbell Accent Icon at Bottom-Right
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 2.dp, y = 2.dp)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                                .border(1.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Section: Frosted White Glass Plan Strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.90f))
                    .border(1.dp, Color(0xFFCBD5E1).copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                    .clickable { onCardClick("Payment") }
                    .padding(horizontal = 9.dp, vertical = 7.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "👑", fontSize = 13.sp)
                    Text(
                        text = profile.tierName.ifEmpty { "Premium Plan" },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(text = "•", fontSize = 11.5.sp, color = Color(0xFF94A3B8))
                    Text(
                        text = paymentPlan.amountPaid.ifEmpty { "₹1,800/mo" },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )
                    Text(text = "•", fontSize = 11.5.sp, color = Color(0xFF94A3B8))
                    Text(
                        text = paymentPlan.planPeriod.ifEmpty { "Till 12 Dec 2026" },
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3 Glass Info Cards: Joined | Goal | Branch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Card 1: Joined
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.85f))
                        .border(1.dp, Color(0xFFCBD5E1).copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 6.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF2563EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Joined",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = profile.joinedDate.ifEmpty { "15 Jun 2026" },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                maxLines = 1
                            )
                            Text(
                                text = profile.joinedDuration.ifEmpty { "4 Months" },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Card 2: Goal
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.85f))
                        .border(1.dp, Color(0xFFCBD5E1).copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 6.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF7C3AED)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎯", fontSize = 14.sp)
                        }
                        Column {
                            Text(
                                text = "Goal",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = profile.goal.ifEmpty { "Muscle Gain" },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                maxLines = 1
                            )
                        }
                    }
                }

                // Card 3: Branch
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.85f))
                        .border(1.dp, Color(0xFFCBD5E1).copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 6.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF059669)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Branch",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = profile.branch.ifEmpty { "Varanasi" },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Ashwini Member Hero Card (Backwards-compatible alias)
 */
@Composable
fun AshwiniHeroCard(
    onCardClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val sample = MemberCardData.sampleRohitMehta()
    RohitHeroCard(
        profile = sample.profile,
        paymentPlan = sample.paymentPlan,
        onCardClick = onCardClick,
        modifier = modifier
    )
}


@Composable
fun CompactMiniBento(
    title: String,
    value: String,
    subText: String,
    gradient: Brush,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    hasAlert: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    chartContent: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(108.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .border(1.2.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
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
                            .size(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                if (hasAlert) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDC2626)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "!", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = value,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-0.4).sp
                    )
                    Text(
                        text = subText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.92f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    chartContent()
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.35f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickWidgetChip(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(1.dp, Color(0x40A7F3D0), RoundedCornerShape(50.dp))
            .padding(horizontal = 6.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = emoji, fontSize = 18.sp)
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                maxLines = 1
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                maxLines = 1
            )
        }
    }
}

// =========================================================================
// 2. EXPANDED VIEW (Maximized Space, Enlarged Fonts, Full Width 360dp)
// =========================================================================

/**
 * Expanded View of BAD GYM Member Intelligence Card
 * All widgets take FULL WIDTH (360dp) with generous typography & enlarged charts
 */
@Composable
fun ExpandedIntelligenceCard(
    data: MemberCardData,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTimeframe by remember { mutableStateOf("30D") }
    var selectedCategoryTab by remember { mutableStateOf("All") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.94f))
            .border(1.5.dp, Color(0x99A7F3D0), RoundedCornerShape(24.dp))
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Back Arrow, BAD GYM / Member Details, Timeframe pills, Date, Bell
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
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0x40A7F3D0), RoundedCornerShape(12.dp))
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF0A3F2C),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "BAD GYM",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF08412B)
                        )
                        Text(
                            text = "Member Details",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A34)
                        )
                    }
                }

                // Timeframe Selector: 7D, 30D, 3M, 6M, 1Y
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFE2E8F0).copy(alpha = 0.6f))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    listOf("7D", "30D", "3M", "6M", "1Y").forEach { tf ->
                        val isActive = tf == selectedTimeframe
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(if (isActive) Color.White else Color.Transparent)
                                .clickable { selectedTimeframe = tf }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = tf,
                                fontSize = 13.sp,
                                fontWeight = if (isActive) FontWeight.Black else FontWeight.SemiBold,
                                color = if (isActive) Color(0xFF0F172A) else Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Date capsule + Bell
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0x40A7F3D0), RoundedCornerShape(50.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "02 Oct",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0x40A7F3D0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Pills: All | Attendance | Workout | Payment | Body Progress
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All", "Attendance", "Workout", "Payment", "Body Progress").forEach { tab ->
                    val isSelected = tab == selectedCategoryTab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isSelected) Color(0xFF10B981) else Color.White)
                            .border(1.dp, if (isSelected) Color.Transparent else Color(0x40A7F3D0), RoundedCornerShape(50.dp))
                            .clickable { selectedCategoryTab = tab }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = tab,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF334155)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Full-Width Stacked Widgets with Enlarged Typography & Canvas
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (selectedCategoryTab == "All" || selectedCategoryTab == "Attendance") {
                    FullWidthAttendanceWidget(modifier = Modifier.fillMaxWidth())
                }

                if (selectedCategoryTab == "All" || selectedCategoryTab == "Workout") {
                    FullWidthWorkoutWidget(modifier = Modifier.fillMaxWidth())
                }

                if (selectedCategoryTab == "All" || selectedCategoryTab == "Payment") {
                    FullWidthPaymentWidget(modifier = Modifier.fillMaxWidth())
                }

                if (selectedCategoryTab == "All" || selectedCategoryTab == "Body Progress") {
                    FullWidthBodyProgressWidget(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

/**
 * 1. Attendance Widget - Maximized Space & Font
 */
@Composable
fun FullWidthAttendanceWidget(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(AquaGlassTokens.AttendanceCardGradient)
            .border(1.2.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
            .padding(12.dp)
    ) {
        // Top Row: Attendance 85% + 18/21 Days + ↗ 12%
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(text = "Attendance", fontSize = 15.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "85%", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text(text = "18/21 Days", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.92f), modifier = Modifier.padding(bottom = 3.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White.copy(alpha = 0.25f))
                    .padding(horizontal = 11.dp, vertical = 5.dp)
            ) {
                Text(text = "\u2197\uFE0F 12%", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 30-Day Bar & Glowing Dots Chart
        Canvas(modifier = Modifier.fillMaxWidth().height(96.dp)) {
            val bars = listOf(
                0.35f, 0.5f, 0.3f, 0.65f, 0.7f, 0.5f, 0.8f, 0.75f, 0.6f, 0.85f,
                0.9f, 0.7f, 0.8f, 0.65f, 0.85f, 0.95f, 0.75f, 0.8f, 0.9f
            )
            val w = 8.dp.toPx()
            val sp = (size.width - bars.size * w) / (bars.size - 1)
            val path = Path()

            bars.forEachIndexed { i, f ->
                val x = i * (w + sp)
                val h = size.height * 0.82f * f
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.85f),
                    topLeft = Offset(x, size.height - h - 14.dp.toPx()),
                    size = Size(w, h),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )

                val dotY = size.height - h - 14.dp.toPx()
                if (i == 0) path.moveTo(x + w / 2, dotY) else path.lineTo(x + w / 2, dotY)
                drawCircle(Color(0xFF00F5A0), radius = 3.5.dp.toPx(), center = Offset(x + w / 2, dotY))
            }
            drawPath(path, Color(0xFF00F5A0), style = Stroke(2.2.dp.toPx()))
        }

        // Days labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("1", "5", "10", "15", "20", "25", "30").forEach { d ->
                Text(text = d, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.85f))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Legend: Present 18 | Absent 3 | Gym Off 4
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFF00E676)))
                Text(text = "Present 18", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                Text(text = "Absent 3", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color.White))
                Text(text = "Gym Off 4", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // This Week: M T W T F S S (34dp circles)
        Text(text = "This Week", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(
                Triple("M", true, false),
                Triple("T", true, false),
                Triple("W", true, false),
                Triple("T", false, true),
                Triple("F", true, false),
                Triple("S", false, false),
                Triple("S", false, false)
            ).forEach { (day, isPresent, isAbsent) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = day, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.95f))
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isPresent -> Color(0xFF10B981)
                                    isAbsent -> Color(0xFFEF4444)
                                    else -> Color.White.copy(alpha = 0.35f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                isPresent -> Icons.Default.Check
                                isAbsent -> Icons.Default.Close
                                else -> Icons.Default.Remove
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Coach Insight Callout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.22f))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = Color(0xFFFDE047),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Good! Attendance up by 12% this month.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * 2. Workout Widget - Maximized Space & Font
 */
@Composable
fun FullWidthWorkoutWidget(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(AquaGlassTokens.WorkoutCardGradient)
            .border(1.2.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
            .padding(12.dp)
    ) {
        // Top: Workout 47 min + Avg/Day + ↗ 8%
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(text = "Workout", fontSize = 15.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "47 min", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text(text = "Avg/Day", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.92f), modifier = Modifier.padding(bottom = 3.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White.copy(alpha = 0.25f))
                    .padding(horizontal = 11.dp, vertical = 5.dp)
            ) {
                Text(text = "\u2197\uFE0F 8%", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 30-Day Equalizer Chart
        Canvas(modifier = Modifier.fillMaxWidth().height(96.dp)) {
            val bars = listOf(
                0.35f, 0.7f, 0.5f, 0.8f, 0.6f, 0.9f, 0.75f, 0.5f,
                0.65f, 0.85f, 0.7f, 0.95f, 0.8f, 0.6f, 0.75f, 0.9f, 0.65f, 0.85f
            )
            val w = 8.dp.toPx()
            val sp = (size.width - bars.size * w) / (bars.size - 1)

            bars.forEachIndexed { i, f ->
                val x = i * (w + sp)
                val h = size.height * 0.85f * f
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFF00E5FF), Color(0xFF1D4ED8))
                    ),
                    topLeft = Offset(x, size.height - h - 14.dp.toPx()),
                    size = Size(w, h),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("1", "5", "10", "15", "20", "25", "30").forEach { d ->
                Text(text = d, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.85f))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Stats: Total Workouts 18 | Best Day 92 min | Streak 12 Days
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "\uD83C\uDFCB\uFE0F Total Workouts", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.92f))
                Text(text = "18", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "\uD83C\uDFC6 Best Day", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.92f))
                Text(text = "92 min", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "\uD83D\uDD25 Current Streak", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.92f))
                Text(text = "12 Days", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // This Week (min)
        Text(text = "This Week (min)", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            listOf(45 to Color(0xFF00E5FF), 60 to Color(0xFF00E676), 30 to Color(0xFFEF4444),
                50 to Color(0xFF00E676), 80 to Color(0xFF00E676), 60 to Color(0xFF00E676), 40 to Color(0xFFEF4444))
                .forEach { (mins, color) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height((mins * 0.48f).dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(color)
                        )
                        Text(text = "$mins", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Coach Insight Callout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.22f))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = Color(0xFFFDE047),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Great! Workout time increasing trend.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * 3. Payment Widget - Maximized Space & Font
 */
@Composable
fun FullWidthPaymentWidget(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(AquaGlassTokens.PaymentCardGradient)
            .border(1.2.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
            .padding(12.dp)
    ) {
        // Top: Payment ₹12,800 + 25 Days Left + ↘ 20%
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(text = "Payment", fontSize = 15.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "\u20B912,800", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text(text = "25 Days Left", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.92f), modifier = Modifier.padding(bottom = 3.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFDC2626).copy(alpha = 0.9f))
                    .padding(horizontal = 11.dp, vertical = 5.dp)
            ) {
                Text(text = "\u2198\uFE0F 20%", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 30-Day Timeline Line Graph
        Canvas(modifier = Modifier.fillMaxWidth().height(96.dp)) {
            val pts = listOf(0.4f, 0.75f, 0.5f, 0.85f, 0.6f, 0.75f, 0.9f, 0.65f, 0.85f)
            val step = size.width / (pts.size - 1)
            val p = Path()
            pts.forEachIndexed { i, f ->
                val cx = i * step
                val cy = size.height * (1f - f)
                if (i == 0) p.moveTo(cx, cy) else p.lineTo(cx, cy)
                drawCircle(Color.White, radius = 4.5.dp.toPx(), center = Offset(cx, cy))
            }
            drawPath(p, Color.White, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("1", "5", "10", "15", "20", "25", "30").forEach { d ->
                Text(text = d, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.85f))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Stats: Total Paid ₹112,600 | Pending ₹12,800 | Next Due 27 Oct 2026
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "\uD83D\uDCB3 Total Paid", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.92f))
                Text(text = "\u20B9112,600", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "\u23F3 Pending", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.92f))
                Text(text = "\u20B912,800", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "\uD83D\uDCC5 Next Due", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.92f))
                Text(text = "27 Oct 2026", fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Payment History (Last 7)
        Text(text = "Payment History (Last 7)", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(
                Color(0xFF00E676), Color(0xFF00E676), Color(0xFFFBBF24),
                Color(0xFF00E676), Color(0xFFEF4444), Color(0xFF00E676), Color(0xFFFBBF24)
            ).forEach { bg ->
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(bg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "\u20B9", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Coach Insight Callout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.22f))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = Color(0xFFFDE047),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Payments stable. Next due in 25 days.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * 4. Body Progress Widget - Maximized Space & Font
 */
@Composable
fun FullWidthBodyProgressWidget(modifier: Modifier = Modifier) {
    var selectedMetric by remember { mutableStateOf("Weight") }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(AquaGlassTokens.BodyProgressCardGradient)
            .border(1.2.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
            .padding(12.dp)
    ) {
        // Top: Body Progress 69.5 kg + 📉 -0.7 kg + ↗ 5%
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(text = "Body Progress", fontSize = 15.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "69.5 kg", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text(text = "\uD83D\uDCC9 -0.7 kg", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.92f), modifier = Modifier.padding(bottom = 3.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White.copy(alpha = 0.25f))
                    .padding(horizontal = 11.dp, vertical = 5.dp)
            ) {
                Text(text = "\u2197\uFE0F 5%", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 30-Day Spline Chart
        Canvas(modifier = Modifier.fillMaxWidth().height(86.dp)) {
            val pts = listOf(0.35f, 0.6f, 0.45f, 0.75f, 0.55f, 0.8f, 0.65f, 0.95f)
            val step = size.width / (pts.size - 1)
            val p = Path()
            pts.forEachIndexed { i, f ->
                val cx = i * step
                val cy = size.height * (1f - f)
                if (i == 0) p.moveTo(cx, cy) else p.lineTo(cx, cy)
                drawCircle(Color.White, radius = 4.5.dp.toPx(), center = Offset(cx, cy))
            }
            drawPath(p, Color.White, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
        }

        // Sub-tabs: Weight | BMI | Body Fat | Muscle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White.copy(alpha = 0.25f))
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("Weight", "BMI", "Body Fat", "Muscle").forEach { m ->
                val isAct = m == selectedMetric
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isAct) Color.White else Color.Transparent)
                        .clickable { selectedMetric = m }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = m,
                        fontSize = 14.sp,
                        fontWeight = if (isAct) FontWeight.Black else FontWeight.Bold,
                        color = if (isAct) Color(0xFF0F172A) else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Start 72.3 kg | Current 69.5 kg | Target 68 kg
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Start", fontSize = 13.sp, color = Color.White.copy(alpha = 0.85f))
                Text(text = "72.3 kg", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Current", fontSize = 13.sp, color = Color.White.copy(alpha = 0.85f))
                Text(text = "69.5 kg", fontSize = 18.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF00E676))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Target", fontSize = 13.sp, color = Color.White.copy(alpha = 0.85f))
                Text(text = "68 kg", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress 38% Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Progress 38%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White.copy(alpha = 0.35f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.38f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF00E676))
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Body Goals (2): Lose Weight -4.8kg | Build Muscle +2.5kg
        Text(text = "Body Goals (2)", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "\u2696\uFE0F", fontSize = 18.sp)
                Column {
                    Text(text = "Lose Weight", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
                    Text(text = "-4.8 kg left", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "\uD83D\uDCAA", fontSize = 18.sp)
                Column {
                    Text(text = "Build Muscle", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
                    Text(text = "+2.5 kg left", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Coach Insight Callout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.22f))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = Color(0xFFFDE047),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Good progress! Weight down by 0.7 kg.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
