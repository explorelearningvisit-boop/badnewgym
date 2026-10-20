package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.design.AquaGlassTokens
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendBar

/**
 * Base Neumorphic Aqua Frosted Glass Card
 */
@Composable
fun AquaGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 22.dp,
    backgroundColor: Color = AquaGlassTokens.CardSurface,
    borderColor: Color = AquaGlassTokens.CardBorder,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = AquaGlassTokens.CardShadow,
                spotColor = AquaGlassTokens.CardShadow
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        // Subtle specular highlight on top bevel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(AquaGlassTokens.GlassShimmerBrush)
        )
        content()
    }
}

/**
 * Left Vertical Navigation Rail (Dock)
 * Mimics the left vertical dock seen in the mint reference UI
 */
@Composable
fun AquaLeftNavRail(
    selectedItem: String,
    onSelectItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        RailItemData("Check-In", "LIVE", Color(0xFF10B981)),
        RailItemData("Payment", "₹2.8k", Color(0xFFF43F5E)),
        RailItemData("Attendance", "90%", Color(0xFF0284C7)),
        RailItemData("Workout", "11", Color(0xFF10B981)),
        RailItemData("Body", "45%", Color(0xFF8B5CF6)),
        RailItemData("Supplements", "1", Color(0xFFF59E0B)),
        RailItemData("Services", "1", Color(0xFF0284C7)),
        RailItemData("History", "0", Color(0xFF94A3B8)),
        RailItemData("Insights", "8d", Color(0xFF10B981))
    )

    Column(
        modifier = modifier
            .width(60.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(AquaGlassTokens.RailSurface)
            .border(1.dp, AquaGlassTokens.RailBorder, RoundedCornerShape(20.dp))
            .padding(vertical = 10.dp, horizontal = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            val isSelected = selectedItem.equals(item.title, ignoreCase = true)
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) AquaGlassTokens.RailActiveBg else Color.Transparent,
                animationSpec = tween(200),
                label = "railBg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else AquaGlassTokens.TextSlate,
                animationSpec = tween(200),
                label = "railText"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .clickable { onSelectItem(item.title) }
                    .padding(vertical = 6.dp, horizontal = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.title,
                    fontSize = 7.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) Color.White.copy(alpha = 0.25f) else item.badgeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = item.badge,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else item.badgeColor
                    )
                }
            }
        }
    }
}

private data class RailItemData(
    val title: String,
    val badge: String,
    val badgeColor: Color
)

/**
 * Top Aqua Brand Header with Theme Switcher & Actions
 */
@Composable
fun AquaTopHeader(
    currentThemeTitle: String,
    onToggleTheme: () -> Unit,
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.85f))
                .border(1.dp, AquaGlassTokens.CardBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(AquaGlassTokens.MintGreen)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "BAD GYM • LIVE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AquaGlassTokens.TextNavy,
                letterSpacing = 0.5.sp
            )
        }

        // Right Actions: 1-Tap Theme Switcher + Notifications + Menu
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Theme Switcher Toggle Pill (Instant restore / switch back to Dark Aurora)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
                    .clickable { onToggleTheme() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🌙 Dark Aurora",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF38BDF8)
                    )
                }
            }

            // Notification Bell
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, AquaGlassTokens.CardBorder, CircleShape)
                    .clickable { onNotificationClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = AquaGlassTokens.TextNavy,
                    modifier = Modifier.size(18.dp)
                )
            }

            // More Options
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, AquaGlassTokens.CardBorder, CircleShape)
                    .clickable { onMenuClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = AquaGlassTokens.TextNavy,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * 1. Hero Profile Card (Aqua Frosted Capsule)
 */
@Composable
fun AquaHeroCard(
    profile: MemberProfile,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AquaGlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Emerald Status Ring
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, AquaGlassTokens.MintGreen, CircleShape)
                        .clickable { onAvatarClick() }
                        .padding(2.5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_aman),
                        contentDescription = profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                    // Online dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(AquaGlassTokens.MintGreen)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = profile.name,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = AquaGlassTokens.TextNavy
                        )
                        // Verified/Gold icon
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "👑 Gold Plan",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${profile.memberCode} • ${profile.age} yrs • ${profile.gender}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AquaGlassTokens.TextMuted
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AquaGlassTokens.MintLight)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Active",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AquaGlassTokens.EmeraldDark
                            )
                        }
                        Text(
                            text = "Branch: ${profile.branch}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            color = AquaGlassTokens.TextSlate
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Usage Bar: 12 / 30 days used
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1FDF8))
                    .border(1.dp, AquaGlassTokens.CardBorder, RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "12 / 30 days used",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )
                    Text(
                        text = "18 days left",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AquaGlassTokens.EmeraldDark
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Track & Progress Fill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFD1FAE5))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.40f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(AquaGlassTokens.MintGreen, AquaGlassTokens.EmeraldDark)
                                )
                            )
                    )
                }
            }
        }
    }
}

/**
 * 2. Payment & Plan Card (Coral Rose Tinted Glass + 3D Card Stack)
 */
@Composable
fun AquaPaymentCard(
    payment: PaymentPlanState,
    onReceiptClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AquaGlassCard(
        modifier = modifier,
        backgroundColor = Color(0xFFFFFFFF),
        borderColor = Color(0xFFFFD1D8)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(AquaGlassTokens.CoralRose)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PAYMENT & PLAN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.CoralRose,
                        letterSpacing = 0.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFE4E6))
                        .border(0.5.dp, Color(0xFFFCA5A5), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "DUE SOON ⚠️",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFBE123C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Body: Big Amount + 3D Card Stack
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹2,800",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AquaGlassTokens.TextNavy
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Due in 3 days • 16 Oct 2026",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFE11D48)
                    )
                }

                // 3D Membership Card Stack Graphics
                Canvas(modifier = Modifier.size(width = 80.dp, height = 52.dp)) {
                    // Back card shadow/layer
                    drawRoundRect(
                        color = Color(0xFFFECDD3),
                        topLeft = Offset(8f, 16f),
                        size = Size(size.width - 12f, size.height - 18f),
                        cornerRadius = CornerRadius(14f, 14f)
                    )
                    // Middle card
                    drawRoundRect(
                        color = Color(0xFFFDA4AF),
                        topLeft = Offset(4f, 8f),
                        size = Size(size.width - 12f, size.height - 18f),
                        cornerRadius = CornerRadius(14f, 14f)
                    )
                    // Front VIP Card
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFF43F5E), Color(0xFFBE123C))
                        ),
                        topLeft = Offset(0f, 0f),
                        size = Size(size.width - 12f, size.height - 18f),
                        cornerRadius = CornerRadius(14f, 14f)
                    )
                    // Chip
                    drawRoundRect(
                        color = Color(0xFFFDE047),
                        topLeft = Offset(8f, 8f),
                        size = Size(16f, 12f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Banner: Last Paid info + Clickable Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFF1F2))
                    .clickable { onReceiptClick() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Last Paid: ₹2,800 • 15 Sep 2026",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AquaGlassTokens.TextSlate
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Receipt",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.CoralRose
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "View Receipt",
                        tint = AquaGlassTokens.CoralRose,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * 3. Attendance Card (Radial Gauge 90% + Date Carousel)
 */
@Composable
fun AquaAttendanceCard(
    onDayClick: (CalendarDay) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedDayIndex by remember { mutableStateOf(5) } // Friday 17 default

    AquaGlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ATTENDANCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AquaGlassTokens.TextNavy,
                    letterSpacing = 0.5.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AquaGlassTokens.MintLight)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "9 / 10 Open Days",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.EmeraldDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Gauge + Status Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Radial 90% Gauge
                Box(
                    modifier = Modifier.size(86.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 8.dp.toPx()
                        // Track
                        drawArc(
                            color = Color(0xFFE2E8F0),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        // Progress
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(Color(0xFF10B981), Color(0xFF0284C7), Color(0xFF10B981))
                            ),
                            startAngle = -90f,
                            sweepAngle = 324f, // 90% of 360
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "90%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AquaGlassTokens.TextNavy
                        )
                        Text(
                            text = "↓ 2% vs prev",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFF43F5E)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 3 Badges
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFD1FAE5))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("✓ Present", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                        Text("7 days", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("● Late", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                        Text("2 days", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFE4E6))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("✕ Absent", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9F1239))
                        Text("1 day", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9F1239))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Horizontal Date Carousel (Sun 12, Mon 13, Tue 14, Wed 15, Thu 16, Fri 17, Sat 18)
            val daysList = listOf(
                DayPillData("Sun", 12, DayAttendanceStatus.PRESENT),
                DayPillData("Mon", 13, DayAttendanceStatus.PRESENT),
                DayPillData("Tue", 14, DayAttendanceStatus.LATE),
                DayPillData("Wed", 15, DayAttendanceStatus.PRESENT),
                DayPillData("Thu", 16, DayAttendanceStatus.ABSENT),
                DayPillData("Fri", 17, DayAttendanceStatus.PRESENT),
                DayPillData("Sat", 18, DayAttendanceStatus.PRESENT)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                daysList.forEachIndexed { index, day ->
                    val isSelected = selectedDayIndex == index
                    val bg = if (isSelected) AquaGlassTokens.MintGreen else Color(0xFFF1F5F9)
                    val textCol = if (isSelected) Color.White else AquaGlassTokens.TextSlate

                    Column(
                        modifier = Modifier
                            .width(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(bg)
                            .border(
                                width = if (isSelected) 1.5.dp else 0.5.dp,
                                color = if (isSelected) AquaGlassTokens.EmeraldDark else Color(0xFFCBD5E1),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                selectedDayIndex = index
                                onDayClick(CalendarDay(day.dayNum, index, day.status))
                            }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day.dayName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = textCol
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${day.dayNum}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = textCol
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Status dot
                        val dotColor = when (day.status) {
                            DayAttendanceStatus.PRESENT -> Color(0xFF10B981)
                            DayAttendanceStatus.LATE -> Color(0xFFF59E0B)
                            DayAttendanceStatus.ABSENT -> Color(0xFFF43F5E)
                            DayAttendanceStatus.HOLIDAY -> Color(0xFF6366F1)
                        }
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White else dotColor)
                        )
                    }
                }
            }
        }
    }
}

private data class DayPillData(
    val dayName: String,
    val dayNum: Int,
    val status: DayAttendanceStatus
)

/**
 * 4. Workout (Last 15 Days) Card with Bar Chart & Telemetry Chips
 */
@Composable
fun AquaWorkoutCard(
    onAnalyticsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedRange by remember { mutableStateOf("15D") }

    AquaGlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header + Range Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WORKOUT (LAST 15 DAYS)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AquaGlassTokens.TextNavy,
                    letterSpacing = 0.5.sp
                )

                // 7D / 15D Toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE2E8F0))
                        .padding(2.dp)
                ) {
                    listOf("7D", "15D").forEach { range ->
                        val isSel = selectedRange == range
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) Color.White else Color.Transparent)
                                .clickable { selectedRange = range }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = range,
                                fontSize = 10.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) AquaGlassTokens.TextNavy else AquaGlassTokens.TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bar Chart with Minute Values
            val barData = listOf(
                89, 106, 123, 35, 52, 0, 78, 92, 110, 45, 0, 68, 85, 95, 74
            )
            val maxVal = 130f

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                barData.forEachIndexed { i, mins ->
                    val heightRatio = if (mins > 0) mins / maxVal else 0.05f

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(22.dp)
                    ) {
                        if (mins > 0 && (i % 2 == 0 || i == 14)) {
                            Text(
                                text = "${mins}m",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = AquaGlassTokens.TextMuted
                            )
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        Spacer(modifier = Modifier.height(2.dp))

                        // Bar
                        Box(
                            modifier = Modifier
                                .width(14.dp)
                                .fillMaxHeight(heightRatio)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(
                                    if (mins > 0) {
                                        Brush.verticalGradient(
                                            listOf(AquaGlassTokens.MintGreen, Color(0xFF0284C7))
                                        )
                                    } else {
                                        Brush.verticalGradient(listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1)))
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${i + 1}",
                            fontSize = 8.sp,
                            color = AquaGlassTokens.TextLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3 Telemetry Summary Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryChip(
                    label = "11 Sessions",
                    sublabel = "Flame Active",
                    color = Color(0xFFF97316),
                    modifier = Modifier.weight(1f)
                )
                TelemetryChip(
                    label = "823m",
                    sublabel = "Total Duration",
                    color = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f)
                )
                TelemetryChip(
                    label = "74m",
                    sublabel = "Average",
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TelemetryChip(
    label: String,
    sublabel: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = sublabel,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = AquaGlassTokens.TextSlate
        )
    }
}

/**
 * 5. Body Progress (30D) Card with Sparklines, Top Exercise & Muscle Focus
 */
@Composable
fun AquaBodyProgressCard(
    onAnalyticsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AquaGlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BODY PROGRESS (30D)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AquaGlassTokens.TextNavy,
                    letterSpacing = 0.5.sp
                )
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Details",
                    tint = AquaGlassTokens.TextMuted,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onAnalyticsClick() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dual Stat Chips: Weight & Muscle with Sparklines
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Weight
                AquaStatBox(
                    label = "Weight",
                    value = "69.5 kg",
                    delta = "↓ 1.2 kg",
                    deltaColor = Color(0xFF10B981),
                    sparklinePoints = listOf(0.7f, 0.65f, 0.6f, 0.5f, 0.45f, 0.35f, 0.3f),
                    lineColor = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )

                // Muscle
                AquaStatBox(
                    label = "Muscle",
                    value = "57.2 kg",
                    delta = "↑ 1.8 kg",
                    deltaColor = Color(0xFF8B5CF6),
                    sparklinePoints = listOf(0.3f, 0.35f, 0.4f, 0.55f, 0.65f, 0.7f, 0.85f),
                    lineColor = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Top Exercise: Bench Press Highlight Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "TOP EXERCISE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextMuted,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Bench Press",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )
                    Text(
                        text = "65 → 67.5 kg (+4%)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF10B981)
                    )
                }

                // Bench Graphic
                Canvas(modifier = Modifier.size(width = 44.dp, height = 28.dp)) {
                    // Barbell line
                    drawLine(
                        color = Color(0xFF334155),
                        start = Offset(4f, 8f),
                        end = Offset(size.width - 4f, 8f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                    // Weights on left and right
                    drawRoundRect(
                        color = Color(0xFF0F172A),
                        topLeft = Offset(2f, 2f),
                        size = Size(4f, 12f),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                    drawRoundRect(
                        color = Color(0xFF0F172A),
                        topLeft = Offset(size.width - 6f, 2f),
                        size = Size(4f, 12f),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                    // Bench seat
                    drawRoundRect(
                        color = Color(0xFF0284C7),
                        topLeft = Offset(10f, 16f),
                        size = Size(size.width - 20f, 6f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    // Bench legs
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(14f, 22f),
                        end = Offset(14f, size.height),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(size.width - 14f, 22f),
                        end = Offset(size.width - 14f, size.height),
                        strokeWidth = 2f
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Muscle Focus Meters
            Text(
                text = "MUSCLE FOCUS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = AquaGlassTokens.TextMuted,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            MuscleBar(name = "Chest", progress = 0.85f, color = Color(0xFF10B981))
            Spacer(modifier = Modifier.height(6.dp))
            MuscleBar(name = "Back", progress = 0.70f, color = Color(0xFF0284C7))
            Spacer(modifier = Modifier.height(6.dp))
            MuscleBar(name = "Arms", progress = 0.60f, color = Color(0xFF8B5CF6))
            Spacer(modifier = Modifier.height(6.dp))
            MuscleBar(name = "Legs", progress = 0.50f, color = Color(0xFFF59E0B))
        }
    }
}

@Composable
private fun AquaStatBox(
    label: String,
    value: String,
    delta: String,
    deltaColor: Color,
    sparklinePoints: List<Float>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = AquaGlassTokens.TextSlate
            )
            Text(
                text = delta,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = deltaColor
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AquaGlassTokens.TextNavy
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Sparkline Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
        ) {
            if (sparklinePoints.size >= 2) {
                val stepX = size.width / (sparklinePoints.size - 1)
                val path = Path()

                sparklinePoints.forEachIndexed { i, pt ->
                    val x = i * stepX
                    val y = size.height - (pt * size.height)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }
        }
    }
}

@Composable
private fun MuscleBar(
    name: String,
    progress: Float,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = AquaGlassTokens.TextSlate,
            modifier = Modifier.width(44.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = AquaGlassTokens.TextNavy,
            modifier = Modifier.width(28.dp),
            textAlign = TextAlign.End
        )
    }
}
