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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
fun MemberHeaderBlock(
    profile: MemberProfile,
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Top Branding & Overflow Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "BAD",
                        color = MemberCardTokens.BrandEmerald,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = " GYM",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic
                    )
                }
                Text(
                    text = "M E M B E R",
                    color = MemberCardTokens.SlateTitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onMenuClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = MemberCardTokens.DarkNavy,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Profile Details & Attendance Ring
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Photo with Badges
            Box(
                modifier = Modifier.size(76.dp)
            ) {
                // Outer mint ring
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .align(Alignment.Center)
                        .border(
                            width = 2.5.dp,
                            color = MemberCardTokens.BrandEmeraldLight,
                            shape = CircleShape
                        )
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2E8F0))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_aman),
                        contentDescription = "Member Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                }

                // Crown Badge (Top-Left)
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(Color(0xFFFBBF24))
                        .border(1.dp, Color(0xFFF59E0B), CircleShape),
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
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-2).dp, y = (-2).dp)
                        .clip(CircleShape)
                        .background(MemberCardTokens.BrandEmerald)
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Center: Name, ID, Age, Gender, Badges
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = profile.name,
                    color = MemberCardTokens.DarkNavy,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${profile.memberCode}  |  ${profile.age} yrs  |  ${profile.gender}",
                    color = MemberCardTokens.SlateMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Prime Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MemberCardTokens.PillAmberBg)
                            .border(1.dp, MemberCardTokens.PillAmberBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Canvas(modifier = Modifier.size(10.dp)) {
                            val boltPath = Path().apply {
                                moveTo(size.width * 0.6f, 0f)
                                lineTo(size.width * 0.1f, size.height * 0.55f)
                                lineTo(size.width * 0.5f, size.height * 0.55f)
                                lineTo(size.width * 0.4f, size.height)
                                lineTo(size.width * 0.9f, size.height * 0.45f)
                                lineTo(size.width * 0.5f, size.height * 0.45f)
                                close()
                            }
                            drawPath(boltPath, MemberCardTokens.PillAmberText)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Prime",
                            color = MemberCardTokens.PillAmberText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Active Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MemberCardTokens.PillGreenBg)
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Active",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right: Attendance Ring Gauge
            Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val strokeWidth = 6.5.dp.toPx()
                    val arcPadding = strokeWidth / 2f
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)

                    // Background Track
                    drawArc(
                        color = Color(0xFFD1FAE5),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(arcPadding, arcPadding),
                        size = arcSize,
                        style = Stroke(width = strokeWidth)
                    )

                    // Active Progress Gradient Arc
                    val sweep = (profile.attendancePercent / 100f) * 360f
                    drawArc(
                        brush = MemberCardTokens.AttendanceGradient,
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(arcPadding, arcPadding),
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${profile.attendancePercent}%",
                        color = MemberCardTokens.DarkNavy,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "${profile.attendedDays} / ${profile.totalDays}",
                        color = MemberCardTokens.SlateTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Days",
                        color = MemberCardTokens.SlateMuted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
