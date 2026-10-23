package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import com.example.badnewgym.feature.membercard.domain.model.TodayActivityState
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendBar

/**
 * Base Frosted Glass Card for BAD GYM Member Intelligence
 */
@Composable
fun IntelligenceGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    backgroundColor: Color = Color(0xF9FFFFFF),
    borderColor: Color = Color(0x60E2E8F0),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color(0x0A000000),
                spotColor = Color(0x10000000)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}

// ==========================================
// 1. TOP BRANDING APP BAR
// ==========================================
@Composable
fun IntelligenceTopBar(
    monthLabel: String = "Oct 2026",
    onMenuClick: () -> Unit = {},
    onMonthClick: () -> Unit = {},
    onOptionsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Hamburger Menu + BAD GYM Title & Subtitle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onMenuClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Main Menu",
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = "BAD GYM",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF047857),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Member Intelligence",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Right: Date Selector Pill + 3-Dots Menu Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // "Oct 2026 ∨" Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF1F5F9))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                    .clickable(onClick = onMonthClick)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF0284C7),
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = monthLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(16.dp)
                )
            }

            // 3-Dots Circular Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(2.dp, CircleShape, spotColor = Color(0x10000000))
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                    .clickable(onClick = onOptionsClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ==========================================
// 2. HERO PROFILE CARD
// ==========================================
@Composable
fun IntelligenceHeroCard(
    profile: MemberProfile,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    IntelligenceGlassCard(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Subtle ambient lavender/cyan glow behind circular gauge
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-10).dp)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0x25818CF8), Color(0x1038BDF8), Color.Transparent)
                        )
                    )
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Row: Avatar + Details + Circular Speedometer Ring
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Avatar with Gold Crown and Green Online Dot
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clickable(onClick = onAvatarClick)
                    ) {
                        // Avatar image inside glowing green circle
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .align(Alignment.Center)
                                .clip(CircleShape)
                                .border(
                                    3.dp,
                                    Brush.sweepGradient(
                                        listOf(
                                            Color(0xFF10B981),
                                            Color(0xFF34D399),
                                            Color(0xFF059669),
                                            Color(0xFF10B981)
                                        )
                                    ),
                                    CircleShape
                                )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_ashwini),
                                contentDescription = profile.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        // Gold Crown Badge (Top-Left)
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.TopStart)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFFFBBF24), Color(0xFFF59E0B))
                                    )
                                )
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            VectorCrown(tint = Color(0xFF78350F), modifier = Modifier.size(13.dp))
                        }

                        // Neon Green Online Status Indicator (Bottom-Right)
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E))
                                .border(2.5.dp, Color.White, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Middle: Name, Subtitle, Badges
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "${profile.memberCode}  |  ${profile.age} yrs  |  ${profile.gender}",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Badges: Prime Plan + Active
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Amber Prime Plan Pill
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .border(0.5.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                VectorLightning(tint = Color(0xFFD97706), modifier = Modifier.size(11.dp))
                                Text(
                                    text = profile.planBadgeText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            }

                            // Emerald Active Pill
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF059669))
                                    .padding(horizontal = 9.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = "Active",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Right: Open Horseshoe Speedometer Gauge (75%, 25 / 100 Days Left)
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 7.dp.toPx()
                            val arcRadius = (size.minDimension - strokeWidth) / 2f
                            val centerOffset = Offset(size.width / 2f, size.height / 2f)

                            // Open horseshoe geometry: starts at 135°, sweeps 270° (gap at bottom)
                            val startAngle = 135f
                            val totalSweep = 270f
                            val fillSweep = totalSweep * (profile.attendancePercent / 100f)

                            // Background track
                            drawArc(
                                color = Color(0xFFD1FAE5),
                                startAngle = startAngle,
                                sweepAngle = totalSweep,
                                useCenter = false,
                                topLeft = Offset(centerOffset.x - arcRadius, centerOffset.y - arcRadius),
                                size = Size(arcRadius * 2, arcRadius * 2),
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // Progress arc with glowing emerald/cyan gradient
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color(0xFF10B981),
                                        Color(0xFF06B6D4),
                                        Color(0xFF10B981)
                                    )
                                ),
                                startAngle = startAngle,
                                sweepAngle = fillSweep,
                                useCenter = false,
                                topLeft = Offset(centerOffset.x - arcRadius, centerOffset.y - arcRadius),
                                size = Size(arcRadius * 2, arcRadius * 2),
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${profile.attendancePercent}%",
                                fontSize = 17.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "${profile.daysLeft} / ${profile.totalPlanDays}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                            Text(
                                text = "Days Left",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Section: 3 Quick-Stat Glass Tiles (Joined, Goal, Branch)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Tile 1: Joined
                    Row(
                        modifier = Modifier
                            .weight(1.25f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 6.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            VectorCalendar(tint = Color.White, modifier = Modifier.size(15.dp))
                        }

                        Column {
                            Text("Joined", fontSize = 8.5.sp, color = Color(0xFF64748B))
                            Text(
                                text = profile.joinedDate,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                maxLines = 1
                            )
                            Text(
                                text = profile.joinedDuration,
                                fontSize = 8.sp,
                                color = Color(0xFF64748B),
                                maxLines = 1
                            )
                        }
                    }

                    // Tile 2: Goal
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 6.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(Color(0xFF8B5CF6)),
                            contentAlignment = Alignment.Center
                        ) {
                            VectorTarget(tint = Color.White, modifier = Modifier.size(15.dp))
                        }

                        Column {
                            Text("Goal", fontSize = 8.5.sp, color = Color(0xFF64748B))
                            Text(
                                text = profile.goal,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Tile 3: Branch
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 6.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(Color(0xFFEF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            VectorLocationPin(tint = Color.White, modifier = Modifier.size(15.dp))
                        }

                        Column {
                            Text("Branch", fontSize = 8.5.sp, color = Color(0xFF64748B))
                            Text(
                                text = profile.branch,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. BODY STATS BENTO CARD
// ==========================================
@Composable
fun IntelligenceBodyStatsCard(
    bodyStats: List<BodyStatItem>,
    onDetailsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    IntelligenceGlassCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header Row: Dumbbell Icon + Title + "View Details >"
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
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFF8B5CF6)),
                        contentAlignment = Alignment.Center
                    ) {
                        VectorDumbbell(tint = Color.White, modifier = Modifier.size(15.dp))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Body Stats",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "  • Latest vs Previous",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF7C3AED)
                        )
                    }
                }

                Row(
                    modifier = Modifier.clickable(onClick = onDetailsClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View Details",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2563EB)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Bento Tiles in 1 Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                bodyStats.forEach { stat ->
                    BentoStatTile(
                        item = stat,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BentoStatTile(
    item: BodyStatItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        // Top Icon + Chevron
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconBg = when (item.type) {
                BodyStatType.WEIGHT -> Color(0xFF10B981)
                BodyStatType.MUSCLE -> Color(0xFF8B5CF6)
                BodyStatType.BODY_FAT -> Color(0xFFEF4444)
                BodyStatType.BMI -> Color(0xFF3B82F6)
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                when (item.type) {
                    BodyStatType.WEIGHT -> VectorKettlebell(tint = Color.White, modifier = Modifier.size(13.dp))
                    BodyStatType.MUSCLE -> VectorDumbbell(tint = Color.White, modifier = Modifier.size(13.dp))
                    BodyStatType.BODY_FAT -> Text("%", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    BodyStatType.BMI -> VectorUserSilhouette(tint = Color.White, modifier = Modifier.size(13.dp))
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(13.dp)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Main Metric Value
        Text(
            text = item.value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0F172A),
            maxLines = 1
        )

        // Label
        Text(
            text = item.label,
            fontSize = 10.sp,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Delta or Badge
        if (item.badgeText.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFDCFCE7))
                    .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
                Text(
                    text = item.badgeText,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF16A34A)
                )
            }
        } else {
            val deltaColor = if (item.delta.startsWith("↓") && item.type == BodyStatType.BODY_FAT) Color(0xFFDC2626)
            else if (item.delta.startsWith("↓")) Color(0xFF10B981)
            else Color(0xFF10B981)

            Text(
                text = item.delta,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = deltaColor
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Smooth Bezier Sparkline
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        ) {
            val pts = item.sparklinePoints
            if (pts.size >= 2) {
                val path = Path()
                val stepX = size.width / (pts.size - 1)
                pts.forEachIndexed { i, p ->
                    val x = i * stepX
                    val y = size.height * (1f - p)
                    if (i == 0) path.moveTo(x, y)
                    else {
                        val prevX = (i - 1) * stepX
                        val prevY = size.height * (1f - pts[i - 1])
                        path.cubicTo(
                            (prevX + x) / 2f, prevY,
                            (prevX + x) / 2f, y,
                            x, y
                        )
                    }
                }
                drawPath(
                    path = path,
                    color = item.lineColor,
                    style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
                )

                // Peak dot for muscle
                if (item.type == BodyStatType.MUSCLE) {
                    val peakIdx = pts.indexOf(pts.maxOrNull() ?: 1f)
                    drawCircle(
                        color = item.lineColor,
                        radius = 2.5.dp.toPx(),
                        center = Offset(peakIdx * stepX, size.height * (1f - pts[peakIdx]))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Footer Chip (Good Progress, Improving, Leaner, Healthy)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.5.dp)
        ) {
            val footerColor = when (item.type) {
                BodyStatType.WEIGHT -> Color(0xFF059669)
                BodyStatType.MUSCLE -> Color(0xFF7C3AED)
                BodyStatType.BODY_FAT -> Color(0xFFDC2626)
                BodyStatType.BMI -> Color(0xFF2563EB)
            }
            when (item.type) {
                BodyStatType.WEIGHT -> VectorSync(tint = footerColor, modifier = Modifier.size(9.dp))
                BodyStatType.MUSCLE -> VectorTrendUp(tint = footerColor, modifier = Modifier.size(9.dp))
                BodyStatType.BODY_FAT -> VectorTrendDown(tint = footerColor, modifier = Modifier.size(9.dp))
                BodyStatType.BMI -> VectorShield(tint = footerColor, modifier = Modifier.size(9.dp))
            }
            Text(
                text = item.footerText.ifEmpty { "Active" },
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                color = footerColor,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    }
}

// ==========================================
// 4. WORKOUT TREND & ATTENDANCE CARD
// ==========================================
@Composable
fun IntelligenceWorkoutAttendanceCard(
    bars: List<WorkoutTrendBar>,
    calendarDays: List<CalendarDay>,
    onDetailsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    IntelligenceGlassCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header Row: Bar Chart Icon + Title + Oct 2026 ∨ + Details >
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
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFF3B82F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        VectorBarChart(tint = Color.White, modifier = Modifier.size(15.dp))
                    }

                    Text(
                        text = "Workout Trend & Attendance",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // Month filter
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Oct 2026",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    // Details > Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEFF6FF))
                            .clickable(onClick = onDetailsClick)
                            .padding(horizontal = 7.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Details",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chart Container with Threshold Background Bands, Bars, Floating Callouts, and Bottom 31 Day Chips
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val totalWidthPx = constraints.maxWidth.toFloat()
                val leftMarginPx = 28.dp.value * 2.75f
                val rightMarginPx = 52.dp.value * 2.75f
                val chartWidthPx = totalWidthPx - leftMarginPx - rightMarginPx
                val dayStepPx = chartWidthPx / 31f

                // 1. Background Colored Threshold Bands and Guidelines
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height - 24.dp.toPx()
                    val lm = 28.dp.toPx()
                    val rm = 52.dp.toPx()
                    val cw = w - lm - rm

                    // Threshold tinted bands
                    val bandH = h / 4f
                    // 45m-60m (High): green tint
                    drawRect(color = Color(0x0C10B981), topLeft = Offset(lm, 0f), size = Size(cw, bandH))
                    // 30m-45m (Good): blue tint
                    drawRect(color = Color(0x0C0284C7), topLeft = Offset(lm, bandH), size = Size(cw, bandH))
                    // 15m-30m (Normal): amber tint
                    drawRect(color = Color(0x0CF59E0B), topLeft = Offset(lm, bandH * 2f), size = Size(cw, bandH))
                    // 0m-15m (Low): red tint
                    drawRect(color = Color(0x0CEF4444), topLeft = Offset(lm, bandH * 3f), size = Size(cw, bandH))

                    // Horizontal guideline lines
                    for (i in 0..4) {
                        val y = bandH * i
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(lm, y),
                            end = Offset(w - rm, y),
                            strokeWidth = 0.8.dp.toPx()
                        )
                    }

                    // Dashed line at 28m
                    val avgY = h * (1f - (28f / 60f))
                    drawLine(
                        color = Color(0xFF93C5FD),
                        start = Offset(lm, avgY),
                        end = Offset(w - rm, avgY),
                        strokeWidth = 1.2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )

                    // Connecting vertical pointer line for Peak (Day 10)
                    val pkX = lm + (cw / 31f) * 9.5f
                    val pkBarTop = h * (1f - (52f / 60f))
                    drawLine(
                        color = Color(0xFF6D28D9),
                        start = Offset(pkX, 22.dp.toPx()),
                        end = Offset(pkX, pkBarTop),
                        strokeWidth = 1.dp.toPx()
                    )

                    // Connecting vertical pointer line for Lowest (Day 16)
                    val lwX = lm + (cw / 31f) * 15.5f
                    val lwBarTop = h * (1f - (12f / 60f))
                    drawLine(
                        color = Color(0xFFDC2626),
                        start = Offset(lwX, 90.dp.toPx()),
                        end = Offset(lwX, lwBarTop),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // 2. Left Y-Axis Labels
                Column(
                    modifier = Modifier
                        .height(156.dp)
                        .padding(start = 2.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("60m", fontSize = 8.5.sp, color = Color(0xFF64748B))
                    Text("45m", fontSize = 8.5.sp, color = Color(0xFF64748B))
                    Text("30m", fontSize = 8.5.sp, color = Color(0xFF64748B))
                    Text("15m", fontSize = 8.5.sp, color = Color(0xFF64748B))
                    Text("0m", fontSize = 8.5.sp, color = Color(0xFF64748B))
                }

                // 3. Right Y-Axis Threshold Bands
                Column(
                    modifier = Modifier
                        .height(156.dp)
                        .align(Alignment.TopEnd),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    ThresholdPill("High 45m+", Color(0xFFD1FAE5), Color(0xFF059669))
                    ThresholdPill("Good 30-45m", Color(0xFFE0F2FE), Color(0xFF0284C7))
                    ThresholdPill("Normal 15-30m", Color(0xFFFEF3C7), Color(0xFFD97706))
                    ThresholdPill("Low <15m", Color(0xFFFEE2E2), Color(0xFFDC2626))
                }

                // 4. "Avg 28m" Floating Center Pill Badge
                val avgCenterX = 28.dp + ((maxWidth - 80.dp) * (13.5f / 31f))
                Box(
                    modifier = Modifier
                        .offset(x = avgCenterX - 20.dp, y = (156.dp * (1f - (28f / 60f))) - 10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFF1E40AF))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Avg 28m",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // 5. 31 Bars and Bottom 31 Day Chips
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 28.dp, end = 52.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    bars.take(31).forEachIndexed { index, bar ->
                        val dayNum = index + 1
                        val calDay = calendarDays.getOrNull(index)
                        val status = calDay?.status ?: if (bar.minutes > 0) DayAttendanceStatus.PRESENT else DayAttendanceStatus.ABSENT

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Vertical Duration Bar
                            val barHeightDp = (bar.minutes / 60f * 115f).coerceIn(3f, 115f).dp
                            val barColor = when {
                                dayNum == 10 -> Color(0xFF06B6D4) // Peak Cyan
                                dayNum == 16 -> Color(0xFFF87171) // Lowest Coral
                                bar.minutes < 18 && bar.minutes > 0 -> Color(0xFFF87171)
                                bar.minutes == 0 -> Color.Transparent
                                else -> Color(0xFF00C48C)
                            }

                            Box(
                                modifier = Modifier
                                    .width(5.dp)
                                    .height(barHeightDp)
                                    .clip(RoundedCornerShape(topStart = 2.5.dp, topEnd = 2.5.dp))
                                    .background(barColor)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Bottom Circular Day Chip with Day Number
                            val chipColor = when (status) {
                                DayAttendanceStatus.PRESENT -> Color(0xFF10B981)
                                DayAttendanceStatus.ABSENT -> Color(0xFFEF4444)
                                DayAttendanceStatus.HOLIDAY, DayAttendanceStatus.LATE -> Color(0xFF94A3B8)
                            }

                            Box(
                                modifier = Modifier
                                    .size(13.dp)
                                    .clip(CircleShape)
                                    .background(chipColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNum.toString(),
                                    fontSize = 6.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // 6. Floating Callout Badge for Peak (Day 10)
                val peakOffsetXDp = (28.dp + ((maxWidth - 80.dp) * (9.5f / 31f))) - 26.dp
                Box(
                    modifier = Modifier
                        .offset(x = peakOffsetXDp, y = 2.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFF6D28D9))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF5B21B6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                        Column {
                            Text("Peak", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("52m (10 Oct)", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                // 7. Floating Callout Badge for Lowest (Day 16)
                val lowOffsetXDp = (28.dp + ((maxWidth - 80.dp) * (15.5f / 31f))) - 26.dp
                Box(
                    modifier = Modifier
                        .offset(x = lowOffsetXDp, y = 72.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFFDC2626))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFFB91C1C)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("↓", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                        Column {
                            Text("Lowest", fontSize = 6.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("12m (16 Oct)", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Summary Stats Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendDotItem(Color(0xFF10B981), "Present", "24 Days (77%)")
                LegendDotItem(Color(0xFFEF4444), "Absent", "3 Days (10%)")
                LegendDotItem(Color(0xFF94A3B8), "Holiday", "2 Days (6%)")
                LegendVectorItem(
                    icon = { VectorBarChart(tint = Color(0xFF0284C7), modifier = Modifier.size(11.dp)) },
                    label = "Avg",
                    count = "28 min",
                    fg = Color(0xFF0284C7)
                )
                LegendVectorItem(
                    icon = { VectorTrophy(tint = Color(0xFFD97706), modifier = Modifier.size(11.dp)) },
                    label = "Peak",
                    count = "52 min",
                    fg = Color(0xFFD97706)
                )
                LegendVectorItem(
                    icon = { VectorDownArrow(tint = Color(0xFFDC2626), modifier = Modifier.size(11.dp)) },
                    label = "Lowest",
                    count = "12 min",
                    fg = Color(0xFFDC2626)
                )
            }
        }
    }
}

@Composable
private fun LegendVectorItem(icon: @Composable () -> Unit, label: String, count: String, fg: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        icon()
        Column {
            Text(label, fontSize = 7.5.sp, color = Color(0xFF64748B))
            Text(count, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = fg)
        }
    }
}


@Composable
private fun ThresholdPill(text: String, bg: Color, fg: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 4.dp, vertical = 1.dp)
    ) {
        Text(
            text = text,
            fontSize = 7.5.sp,
            fontWeight = FontWeight.Bold,
            color = fg
        )
    }
}

@Composable
private fun LegendDotItem(color: Color, label: String, count: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Column {
            Text(label, fontSize = 7.5.sp, color = Color(0xFF64748B))
            Text(count, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        }
    }
}

@Composable
private fun LegendStatItem(icon: String, label: String, count: String, fg: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(icon, fontSize = 9.sp)
        Column {
            Text(label, fontSize = 7.5.sp, color = Color(0xFF64748B))
            Text(count, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = fg)
        }
    }
}

// ==========================================
// 5. PAYMENT & PLAN CARD
// ==========================================
@Composable
fun IntelligencePaymentPlanCard(
    payment: PaymentPlanState,
    onViewReceiptClick: () -> Unit = {},
    onCollectClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    IntelligenceGlassCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header Row: Wallet Icon + Title + "Prime Plan (1 Month) >"
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
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        VectorWallet(tint = Color.White, modifier = Modifier.size(15.dp))
                    }

                    Text(
                        text = "Payment & Plan",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                }

                // Amber Prime Plan Pill Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 9.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    VectorLightning(tint = Color(0xFFD97706), modifier = Modifier.size(11.dp))
                    Text(
                        text = "Prime Plan (1 Month)",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFB45309),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Items in Horizontal Row with balanced proportional weights
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Item 1: Paid Status (Weight 1.4f)
                Row(
                    modifier = Modifier
                        .weight(1.4f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 5.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(23.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Column {
                        Text(
                            text = payment.amountPaid,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1
                        )
                        Text(
                            text = "Paid • On Time",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981),
                            maxLines = 1
                        )
                        Text(
                            text = "01 Sep 2026",
                            fontSize = 7.5.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1
                        )
                    }
                }

                // Item 2: Next Due (Weight 1.35f)
                Row(
                    modifier = Modifier
                        .weight(1.35f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 5.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(23.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF3B82F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        VectorCalendar(tint = Color.White, modifier = Modifier.size(12.dp))
                    }

                    Column {
                        Text(
                            text = "Next Due",
                            fontSize = 7.5.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1
                        )
                        Text(
                            text = payment.nextDueDate,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            maxLines = 1
                        )
                        Text(
                            text = "${payment.daysLeft} Days Left",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB),
                            maxLines = 1
                        )
                    }
                }

                // Item 3: View Receipt (Weight 0.9f)
                Row(
                    modifier = Modifier
                        .weight(0.9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0F7FF))
                        .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(12.dp))
                        .clickable(onClick = onViewReceiptClick)
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    VectorReceipt(tint = Color(0xFF2563EB), modifier = Modifier.size(14.dp))
                    Text(
                        text = "View\nReceipt",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB),
                        lineHeight = 11.sp
                    )
                }

                // Item 4: Collect Money Button (Weight 1.1f)
                Row(
                    modifier = Modifier
                        .weight(1.1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF059669), Color(0xFF10B981))
                            )
                        )
                        .clickable(onClick = onCollectClick)
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "₹",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF059669)
                        )
                    }
                    Text(
                        text = "Collect\nMoney",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 11.sp
                    )
                }
            }
        }
    }
}

// ==========================================
// 6. TODAY'S ACTIVITY CARD
// ==========================================
@Composable
fun IntelligenceTodayActivityCard(
    activity: TodayActivityState,
    onDetailsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    IntelligenceGlassCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header Row: Lightning Icon + Title + "Details >"
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
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFF8B5CF6)),
                        contentAlignment = Alignment.Center
                    ) {
                        VectorLightning(tint = Color.White, modifier = Modifier.size(14.dp))
                    }

                    Text(
                        text = "Today's Activity",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                }

                Row(
                    modifier = Modifier.clickable(onClick = onDetailsClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Details",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3 Activity Tiles in Row (Check-in, Workout, Check-out)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tile 1: Check-in
                TodayActivityTile(
                    iconBg = Color(0xFFD1FAE5),
                    borderColor = Color(0xFF34D399),
                    glowColor = Color(0xFF10B981),
                    symbolType = ActivityIconType.CLOCK_GREEN,
                    label = "Check-in",
                    value = activity.checkInTime,
                    modifier = Modifier.weight(1f)
                )

                // Tile 2: Workout
                TodayActivityTile(
                    iconBg = Color(0xFFEDE9FE),
                    borderColor = Color(0xFFA78BFA),
                    glowColor = Color(0xFF8B5CF6),
                    symbolType = ActivityIconType.DUMBBELL_PURPLE,
                    label = "Workout",
                    value = activity.workoutDuration,
                    modifier = Modifier.weight(1f)
                )

                // Tile 3: Check-out
                TodayActivityTile(
                    iconBg = Color(0xFFFEE2E2),
                    borderColor = Color(0xFFF87171),
                    glowColor = Color(0xFFEF4444),
                    symbolType = ActivityIconType.CLOCK_RED,
                    label = "Check-out",
                    value = activity.checkOutTime,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private enum class ActivityIconType {
    CLOCK_GREEN, DUMBBELL_PURPLE, CLOCK_RED
}

@Composable
private fun TodayActivityTile(
    iconBg: Color,
    borderColor: Color,
    glowColor: Color,
    symbolType: ActivityIconType,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .padding(horizontal = 7.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        // Glowing circular icon badge
        Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            // Ambient Radial Glow
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(glowColor.copy(alpha = 0.45f), Color.Transparent)
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(iconBg)
                    .border(1.5.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when (symbolType) {
                    ActivityIconType.CLOCK_GREEN -> VectorClock(tint = Color(0xFF059669), modifier = Modifier.size(14.dp))
                    ActivityIconType.DUMBBELL_PURPLE -> VectorDumbbell(tint = Color(0xFF7C3AED), modifier = Modifier.size(14.dp))
                    ActivityIconType.CLOCK_RED -> VectorClock(tint = Color(0xFFDC2626), modifier = Modifier.size(14.dp))
                }
            }
        }

        Column {
            Text(
                text = label,
                fontSize = 9.sp,
                color = Color(0xFF64748B)
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A)
            )
        }
    }
}

// ==========================================
// VECTOR GRAPHIC HELPERS (CRISP CANVAS)
// ==========================================

@Composable
fun VectorCrown(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(2f, h - 3f)
            lineTo(2f, 4f)
            lineTo(w * 0.3f, h * 0.55f)
            lineTo(w * 0.5f, 2f)
            lineTo(w * 0.7f, h * 0.55f)
            lineTo(w - 2f, 4f)
            lineTo(w - 2f, h - 3f)
            close()
        }
        drawPath(path = path, color = tint)
    }
}

@Composable
fun VectorLightning(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.55f, 1f)
            lineTo(w * 0.15f, h * 0.55f)
            lineTo(w * 0.48f, h * 0.55f)
            lineTo(w * 0.42f, h - 1f)
            lineTo(w * 0.85f, h * 0.45f)
            lineTo(w * 0.52f, h * 0.45f)
            close()
        }
        drawPath(path = path, color = tint)
    }
}

@Composable
fun VectorCalendar(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // Calendar frame
        drawRoundRect(
            color = tint,
            topLeft = Offset(1f, 3f),
            size = Size(w - 2f, h - 4f),
            cornerRadius = CornerRadius(2.5f, 2.5f),
            style = Stroke(width = 1.4f)
        )
        // Top binders
        drawLine(color = tint, start = Offset(w * 0.3f, 1f), end = Offset(w * 0.3f, 4f), strokeWidth = 1.4f)
        drawLine(color = tint, start = Offset(w * 0.7f, 1f), end = Offset(w * 0.7f, 4f), strokeWidth = 1.4f)
        // Header line
        drawLine(color = tint, start = Offset(2f, 6.5f), end = Offset(w - 2f, 6.5f), strokeWidth = 1f)
        // Grid dots
        drawCircle(color = tint, radius = 1f, center = Offset(w * 0.35f, h * 0.65f))
        drawCircle(color = tint, radius = 1f, center = Offset(w * 0.65f, h * 0.65f))
    }
}

@Composable
fun VectorTarget(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val r = (size.minDimension - 2f) / 2f
        drawCircle(color = tint, radius = r, center = center, style = Stroke(width = 1.3f))
        drawCircle(color = tint, radius = r * 0.55f, center = center, style = Stroke(width = 1.3f))
        drawCircle(color = tint, radius = r * 0.22f, center = center)
    }
}

@Composable
fun VectorLocationPin(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w / 2f, h - 1.5f)
            cubicTo(w * 0.15f, h * 0.55f, w * 0.15f, 2f, w / 2f, 2f)
            cubicTo(w * 0.85f, 2f, w * 0.85f, h * 0.55f, w / 2f, h - 1.5f)
            close()
        }
        drawPath(path = path, color = tint)
        drawCircle(color = Color(0xFFEF4444), radius = w * 0.18f, center = Offset(w / 2f, h * 0.42f))
    }
}

@Composable
fun VectorKettlebell(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // Handle
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.28f, 1.5f),
            size = Size(w * 0.44f, h * 0.45f),
            cornerRadius = CornerRadius(2f, 2f),
            style = Stroke(width = 1.4f)
        )
        // Body
        drawCircle(color = tint, radius = w * 0.36f, center = Offset(w / 2f, h * 0.64f))
    }
}

@Composable
fun VectorDumbbell(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // Center grip
        drawLine(color = tint, start = Offset(w * 0.25f, h / 2f), end = Offset(w * 0.75f, h / 2f), strokeWidth = 2f)
        // Left plates
        drawRoundRect(color = tint, topLeft = Offset(1f, h * 0.2f), size = Size(w * 0.18f, h * 0.6f), cornerRadius = CornerRadius(1.5f, 1.5f))
        // Right plates
        drawRoundRect(color = tint, topLeft = Offset(w - 1f - w * 0.18f, h * 0.2f), size = Size(w * 0.18f, h * 0.6f), cornerRadius = CornerRadius(1.5f, 1.5f))
    }
}

@Composable
fun VectorUserSilhouette(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawCircle(color = tint, radius = w * 0.24f, center = Offset(w / 2f, h * 0.3f))
        val path = Path().apply {
            moveTo(2f, h - 1.5f)
            cubicTo(2f, h * 0.6f, w - 2f, h * 0.6f, w - 2f, h - 1.5f)
            close()
        }
        drawPath(path = path, color = tint)
    }
}

@Composable
fun VectorWallet(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(
            color = tint,
            topLeft = Offset(1.5f, 2.5f),
            size = Size(w - 3f, h - 5f),
            cornerRadius = CornerRadius(2.5f, 2.5f),
            style = Stroke(width = 1.4f)
        )
        // Clasp
        drawCircle(color = tint, radius = 1.8f, center = Offset(w - 4.5f, h / 2f))
    }
}

@Composable
fun VectorReceipt(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(
            color = tint,
            topLeft = Offset(1.5f, 1.5f),
            size = Size(w - 3f, h - 3f),
            cornerRadius = CornerRadius(2f, 2f),
            style = Stroke(width = 1.3f)
        )
        drawLine(color = tint, start = Offset(4f, 5f), end = Offset(w - 4f, 5f), strokeWidth = 1.2f)
        drawLine(color = tint, start = Offset(4f, 8.5f), end = Offset(w - 4f, 8.5f), strokeWidth = 1.2f)
        drawLine(color = tint, start = Offset(4f, 12f), end = Offset(w * 0.65f, 12f), strokeWidth = 1.2f)
    }
}

@Composable
fun VectorClock(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val r = (size.minDimension - 2f) / 2f
        drawCircle(color = tint, radius = r, center = center, style = Stroke(width = 1.4f))
        // Clock hands
        drawLine(color = tint, start = center, end = Offset(center.x, center.y - r * 0.6f), strokeWidth = 1.4f, cap = StrokeCap.Round)
        drawLine(color = tint, start = center, end = Offset(center.x + r * 0.5f, center.y), strokeWidth = 1.4f, cap = StrokeCap.Round)
    }
}

@Composable
fun VectorBarChart(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val bw = w * 0.22f
        drawRoundRect(color = tint, topLeft = Offset(1f, h * 0.55f), size = Size(bw, h * 0.45f), cornerRadius = CornerRadius(1f, 1f))
        drawRoundRect(color = tint, topLeft = Offset(1f + bw + 2.5f, h * 0.2f), size = Size(bw, h * 0.8f), cornerRadius = CornerRadius(1f, 1f))
        drawRoundRect(color = tint, topLeft = Offset(1f + (bw + 2.5f) * 2f, h * 0.4f), size = Size(bw, h * 0.6f), cornerRadius = CornerRadius(1f, 1f))
    }
}

@Composable
fun VectorTrophy(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // Cup body
        val path = Path().apply {
            moveTo(w * 0.22f, 1f)
            lineTo(w * 0.78f, 1f)
            lineTo(w * 0.72f, h * 0.52f)
            cubicTo(w * 0.7f, h * 0.72f, w * 0.3f, h * 0.72f, w * 0.28f, h * 0.52f)
            close()
        }
        drawPath(path = path, color = tint)
        // Handles
        drawArc(
            color = tint,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(0.5f, h * 0.12f),
            size = Size(w * 0.35f, h * 0.35f),
            style = Stroke(width = 1.2f)
        )
        drawArc(
            color = tint,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.65f, h * 0.12f),
            size = Size(w * 0.35f, h * 0.35f),
            style = Stroke(width = 1.2f)
        )
        // Stem and base
        drawRect(color = tint, topLeft = Offset(w * 0.44f, h * 0.7f), size = Size(w * 0.12f, h * 0.16f))
        drawRoundRect(color = tint, topLeft = Offset(w * 0.25f, h * 0.84f), size = Size(w * 0.5f, h * 0.14f), cornerRadius = CornerRadius(1f, 1f))
    }
}

@Composable
fun VectorDownArrow(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        drawLine(color = tint, start = Offset(cx, 1f), end = Offset(cx, h - 2f), strokeWidth = 1.5f, cap = StrokeCap.Round)
        val head = Path().apply {
            moveTo(cx - w * 0.32f, h * 0.55f)
            lineTo(cx, h - 1f)
            lineTo(cx + w * 0.32f, h * 0.55f)
        }
        drawPath(path = head, color = tint, style = Stroke(width = 1.5f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun VectorSync(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val r = (size.minDimension - 2f) / 2f
        val center = Offset(w / 2f, h / 2f)
        drawArc(
            color = tint,
            startAngle = 30f,
            sweepAngle = 150f,
            useCenter = false,
            topLeft = Offset(center.x - r, center.y - r),
            size = Size(r * 2f, r * 2f),
            style = Stroke(width = 1.2f, cap = StrokeCap.Round)
        )
        drawArc(
            color = tint,
            startAngle = 210f,
            sweepAngle = 150f,
            useCenter = false,
            topLeft = Offset(center.x - r, center.y - r),
            size = Size(r * 2f, r * 2f),
            style = Stroke(width = 1.2f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun VectorTrendUp(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawLine(color = tint, start = Offset(1f, h - 1.5f), end = Offset(w - 2f, 2f), strokeWidth = 1.4f, cap = StrokeCap.Round)
        val arrow = Path().apply {
            moveTo(w * 0.55f, 2f)
            lineTo(w - 1.5f, 2f)
            lineTo(w - 1.5f, h * 0.45f)
        }
        drawPath(path = arrow, color = tint, style = Stroke(width = 1.4f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun VectorTrendDown(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawLine(color = tint, start = Offset(1f, 2f), end = Offset(w - 2f, h - 1.5f), strokeWidth = 1.4f, cap = StrokeCap.Round)
        val arrow = Path().apply {
            moveTo(w * 0.55f, h - 1.5f)
            lineTo(w - 1.5f, h - 1.5f)
            lineTo(w - 1.5f, h * 0.55f)
        }
        drawPath(path = arrow, color = tint, style = Stroke(width = 1.4f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun VectorShield(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w / 2f, 1f)
            lineTo(w - 1.5f, 3f)
            lineTo(w - 1.5f, h * 0.55f)
            cubicTo(w - 1.5f, h * 0.85f, w / 2f, h - 1f, w / 2f, h - 1f)
            cubicTo(w / 2f, h - 1f, 1.5f, h * 0.85f, 1.5f, h * 0.55f)
            lineTo(1.5f, 3f)
            close()
        }
        drawPath(path = path, color = tint, style = Stroke(width = 1.2f))
        drawCircle(color = tint, radius = 1f, center = Offset(w / 2f, h * 0.48f))
    }
}

