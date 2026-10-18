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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile

@Composable
fun HeroHeaderSection(
    profile: MemberProfile,
    selectedTab: String = "Overview",
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onTabSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(MemberCardTokens.HeaderBackgroundGradient)
            .drawBehind {
                // Curved neon cyan and emerald glowing aura lines
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x3500E676), Color(0x1500B0FF), Color.Transparent),
                        center = Offset(size.width * 0.85f, size.height * 0.35f),
                        radius = size.width * 0.5f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x2500B0FF), Color.Transparent),
                        center = Offset(size.width * 0.15f, size.height * 0.15f),
                        radius = size.width * 0.45f
                    )
                )
            }
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. Top Bar: Overflow Menu, BAD GYM Branding, Search & Notification Bell
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onMenuClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "BAD",
                                color = MemberCardTokens.BrandEmerald,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                text = " GYM",
                                color = Color.White,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic
                            )
                        }
                        Text(
                            text = "MEMBER INTELLIGENCE",
                            color = Color(0xFFBAE6FD),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.offset(y = (-2).dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Search Action Button
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x20FFFFFF))
                            .border(1.dp, Color(0x30FFFFFF), CircleShape)
                            .clickable(onClick = onSearchClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Notification Bell with Red Badge
                    Box(
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x20FFFFFF))
                                .border(1.dp, Color(0x30FFFFFF), CircleShape)
                                .clickable(onClick = onNotificationClick),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        // Red Counter Badge (3)
                        if (profile.unreadNotifications > 0) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 1.dp, y = (-2).dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                                    .border(1.5.dp, Color(0xFF031A3D), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = profile.unreadNotifications.toString(),
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Member Profile Row + Glowing Radial Gauge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Crown & Active Badge
                Box(modifier = Modifier.size(78.dp)) {
                    Box(
                        modifier = Modifier
                            .size(74.dp)
                            .align(Alignment.Center)
                            .border(
                                width = 3.dp,
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color(0xFF00E676),
                                        Color(0xFF00B0FF),
                                        Color(0xFF10B981),
                                        Color(0xFF00E676)
                                    )
                                ),
                                shape = CircleShape
                            )
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_aman),
                            contentDescription = "Aman Tripathi",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }

                    // Gold Crown Badge (Top-Left)
                    Box(
                        modifier = Modifier
                            .size(23.dp)
                            .align(Alignment.TopStart)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFFBBF24), Color(0xFFD97706))
                                )
                            )
                            .border(1.dp, Color(0xFFFDE68A), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            val path = Path().apply {
                                moveTo(size.width * 0.15f, size.height * 0.75f)
                                lineTo(size.width * 0.85f, size.height * 0.75f)
                                lineTo(size.width * 0.85f, size.height * 0.35f)
                                lineTo(size.width * 0.65f, size.height * 0.55f)
                                lineTo(size.width * 0.50f, size.height * 0.20f)
                                lineTo(size.width * 0.35f, size.height * 0.55f)
                                lineTo(size.width * 0.15f, size.height * 0.35f)
                                close()
                            }
                            drawPath(path, Color.White)
                        }
                    }

                    // Active Green Dot (Bottom-Right)
                    Box(
                        modifier = Modifier
                            .size(17.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-2).dp, y = (-2).dp)
                            .clip(CircleShape)
                            .background(MemberCardTokens.BrandEmerald)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Name, Greeting, Badges
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.greeting,
                        color = Color(0xFFBAE6FD),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = profile.name,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Text(
                        text = "${profile.memberCode}  |  ${profile.age} yrs  |  ${profile.gender}",
                        color = Color(0xFF93C5FD),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Prime Plan Badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFEF3C7))
                                .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Canvas(modifier = Modifier.size(9.dp)) {
                                val boltPath = Path().apply {
                                    moveTo(size.width * 0.6f, 0f)
                                    lineTo(size.width * 0.1f, size.height * 0.55f)
                                    lineTo(size.width * 0.5f, size.height * 0.55f)
                                    lineTo(size.width * 0.4f, size.height)
                                    lineTo(size.width * 0.9f, size.height * 0.45f)
                                    lineTo(size.width * 0.5f, size.height * 0.45f)
                                    close()
                                }
                                drawPath(boltPath, Color(0xFF92400E))
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Prime Plan",
                                color = Color(0xFF92400E),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Active Badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF10B981))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Active",
                                color = Color.White,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Glowing Attendance Circular Gauge
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val strokeWidth = 7.dp.toPx()
                        val arcPadding = strokeWidth / 2f
                        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)

                        // Track
                        drawArc(
                            color = Color(0x2000B0FF),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = Offset(arcPadding, arcPadding),
                            size = arcSize,
                            style = Stroke(width = strokeWidth)
                        )

                        // Progress
                        val sweep = (profile.attendancePercent / 100f) * 360f
                        drawArc(
                            brush = MemberCardTokens.RadialGaugeGradient,
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            topLeft = Offset(arcPadding, arcPadding),
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${profile.attendancePercent}%",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "${profile.daysLeft} Days Left",
                            color = Color(0xFFBAE6FD),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Metadata Strip: Joined, Goal, Branch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x18FFFFFF))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Joined
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(
                            text = "Joined",
                            color = Color(0xFF93C5FD),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = profile.joinedDate,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Goal
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(16.dp)) {
                        drawCircle(color = Color(0xFF38BDF8), radius = size.width * 0.45f, style = Stroke(1.5.dp.toPx()))
                        drawCircle(color = Color(0xFF38BDF8), radius = size.width * 0.2f)
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(
                            text = "Goal",
                            color = Color(0xFF93C5FD),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = profile.goal,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Branch
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(15.dp)) {
                        val path = Path().apply {
                            moveTo(size.width * 0.5f, size.height * 0.9f)
                            lineTo(size.width * 0.2f, size.height * 0.35f)
                            arcTo(
                                rect = androidx.compose.ui.geometry.Rect(0f, 0f, size.width, size.height * 0.7f),
                                startAngleDegrees = 180f,
                                sweepAngleDegrees = 180f,
                                forceMoveTo = false
                            )
                            close()
                        }
                        drawPath(path, Color(0xFFF87171))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(
                            text = "Branch",
                            color = Color(0xFF93C5FD),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = profile.branch,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Horizontal Navigation Tabs
            val tabs = listOf(
                "Overview",
                "Payment",
                "Attendance",
                "Workout",
                "Progress"
            )
            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { title ->
                    val isSelected = (title == selectedTab)
                    val bg = if (isSelected) Color(0xFF10B981) else Color(0x25FFFFFF)
                    val border = if (isSelected) Color(0xFF00E676) else Color(0x20FFFFFF)

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(bg)
                            .border(1.dp, border, RoundedCornerShape(16.dp))
                            .clickable { onTabSelected(title) }
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (title) {
                            "Overview" -> Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            "Payment" -> Canvas(modifier = Modifier.size(14.dp)) {
                                drawRoundRect(
                                    color = Color.White,
                                    topLeft = Offset(0f, size.height * 0.2f),
                                    size = Size(size.width, size.height * 0.6f),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                                )
                            }
                            "Attendance" -> Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            "Workout" -> Canvas(modifier = Modifier.size(14.dp)) {
                                drawLine(color = Color.White, start = Offset(size.width * 0.2f, size.height * 0.3f), end = Offset(size.width * 0.2f, size.height * 0.7f), strokeWidth = 2.dp.toPx())
                                drawLine(color = Color.White, start = Offset(size.width * 0.2f, size.height * 0.5f), end = Offset(size.width * 0.8f, size.height * 0.5f), strokeWidth = 1.5.dp.toPx())
                                drawLine(color = Color.White, start = Offset(size.width * 0.8f, size.height * 0.3f), end = Offset(size.width * 0.8f, size.height * 0.7f), strokeWidth = 2.dp.toPx())
                            }
                            "Progress" -> Canvas(modifier = Modifier.size(14.dp)) {
                                drawLine(color = Color.White, start = Offset(size.width * 0.25f, size.height * 0.8f), end = Offset(size.width * 0.25f, size.height * 0.5f), strokeWidth = 2.dp.toPx())
                                drawLine(color = Color.White, start = Offset(size.width * 0.5f, size.height * 0.8f), end = Offset(size.width * 0.5f, size.height * 0.3f), strokeWidth = 2.dp.toPx())
                                drawLine(color = Color.White, start = Offset(size.width * 0.75f, size.height * 0.8f), end = Offset(size.width * 0.75f, size.height * 0.15f), strokeWidth = 2.dp.toPx())
                            }
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
