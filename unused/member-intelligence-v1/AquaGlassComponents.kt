package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
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
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.domain.model.PaymentPlanState

/**
 * Base Neumorphic Aqua Frosted Glass Card
 */
@Composable
fun AquaGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 26.dp,
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
        // Specular highlight on top bevel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(AquaGlassTokens.GlassShimmerBrush)
        )
        content()
    }
}

/**
 * Left Vertical Navigation Rail (Dock)
 * Matches the reference image dock with top circular close button and 10 items
 */
@Composable
fun AquaLeftNavRail(
    selectedItem: String,
    onSelectItem: (String) -> Unit,
    onCloseClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(62.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(AquaGlassTokens.RailSurface)
            .border(1.dp, AquaGlassTokens.RailBorder, RoundedCornerShape(22.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        // Top Dark Circular Close (✕) Button
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF262626))
                .clickable { onCloseClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Rail",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Item 1: Check-In (Special header item with LIVE)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectItem("Check-In") },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Check-In",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = AquaGlassTokens.TextSlate,
                textAlign = TextAlign.Center
            )
            Text(
                text = "LIVE",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AquaGlassTokens.MintNeon,
                textAlign = TextAlign.Center
            )
        }

        // Remaining 9 Items with Icon Square + Label + Badge
        val railItems = listOf(
            RailNavItem("Payment", "₹2,800", RailIconType.PAYMENT),
            RailNavItem("Attendance", "90%", RailIconType.ATTENDANCE),
            RailNavItem("Workout", "11", RailIconType.WORKOUT),
            RailNavItem("Body", "45%", RailIconType.BODY),
            RailNavItem("Supplements", "1", RailIconType.SUPPLEMENTS),
            RailNavItem("Services", "1", RailIconType.SERVICES),
            RailNavItem("History", "0", RailIconType.HISTORY),
            RailNavItem("Insights", "8d", RailIconType.INSIGHTS),
            RailNavItem("More", "", RailIconType.MORE)
        )

        railItems.forEach { item ->
            val isSelected = selectedItem.equals(item.title, ignoreCase = true)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectItem(item.title) }
                    .padding(vertical = 1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Rounded square icon container
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) AquaGlassTokens.MintGreen else AquaGlassTokens.RailIconBg)
                        .border(
                            0.5.dp,
                            if (isSelected) AquaGlassTokens.EmeraldDark else AquaGlassTokens.CardBorder,
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    RailIconRenderer(
                        type = item.iconType,
                        tint = if (isSelected) Color.White else AquaGlassTokens.RailIconTint
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.title,
                    fontSize = 10.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) AquaGlassTokens.EmeraldDark else AquaGlassTokens.TextSlate,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                if (item.badge.isNotEmpty()) {
                    Text(
                        text = item.badge,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.EmeraldDark,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private enum class RailIconType {
    PAYMENT, ATTENDANCE, WORKOUT, BODY, SUPPLEMENTS, SERVICES, HISTORY, INSIGHTS, MORE
}

private data class RailNavItem(
    val title: String,
    val badge: String,
    val iconType: RailIconType
)

@Composable
private fun RailIconRenderer(type: RailIconType, tint: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        val w = size.width
        val h = size.height
        when (type) {
            RailIconType.PAYMENT -> {
                // Wallet / Card
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(1f, 3f),
                    size = Size(w - 2f, h - 6f),
                    cornerRadius = CornerRadius(3f, 3f),
                    style = Stroke(width = 1.6f)
                )
                drawCircle(
                    color = tint,
                    radius = 1.8f,
                    center = Offset(w - 5f, h / 2f)
                )
            }
            RailIconType.ATTENDANCE -> {
                // Calendar with top hooks and dots
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(1f, 3f),
                    size = Size(w - 2f, h - 4f),
                    cornerRadius = CornerRadius(3f, 3f),
                    style = Stroke(width = 1.6f)
                )
                drawLine(color = tint, start = Offset(4f, 1f), end = Offset(4f, 4f), strokeWidth = 1.6f)
                drawLine(color = tint, start = Offset(w - 4f, 1f), end = Offset(w - 4f, 4f), strokeWidth = 1.6f)
                drawLine(color = tint, start = Offset(2f, 7f), end = Offset(w - 2f, 7f), strokeWidth = 1.2f)
            }
            RailIconType.WORKOUT -> {
                // Dumbbell
                drawLine(color = tint, start = Offset(4f, h / 2f), end = Offset(w - 4f, h / 2f), strokeWidth = 2.2f)
                drawRoundRect(color = tint, topLeft = Offset(1f, 4f), size = Size(3f, h - 8f), cornerRadius = CornerRadius(1.5f, 1.5f))
                drawRoundRect(color = tint, topLeft = Offset(w - 4f, 4f), size = Size(3f, h - 8f), cornerRadius = CornerRadius(1.5f, 1.5f))
            }
            RailIconType.BODY -> {
                // Human silhouette head & torso
                drawCircle(color = tint, radius = 3f, center = Offset(w / 2f, 4f))
                val path = Path().apply {
                    moveTo(3f, h - 2f)
                    cubicTo(3f, 9f, w - 3f, 9f, w - 3f, h - 2f)
                    close()
                }
                drawPath(path = path, color = tint)
            }
            RailIconType.SUPPLEMENTS -> {
                // Bottle / Shaker
                drawRoundRect(color = tint, topLeft = Offset(4f, 1f), size = Size(w - 8f, 3f), cornerRadius = CornerRadius(1f, 1f))
                drawRoundRect(color = tint, topLeft = Offset(2f, 4f), size = Size(w - 4f, h - 5f), cornerRadius = CornerRadius(3f, 3f))
                drawLine(color = Color.White, start = Offset(4f, 9f), end = Offset(w - 4f, 9f), strokeWidth = 1.2f)
            }
            RailIconType.SERVICES -> {
                // Bell / Cloche
                val path = Path().apply {
                    moveTo(2f, h - 3f)
                    cubicTo(2f, 5f, w - 2f, 5f, w - 2f, h - 3f)
                    close()
                }
                drawPath(path = path, color = tint)
                drawCircle(color = tint, radius = 1.8f, center = Offset(w / 2f, 3f))
                drawLine(color = tint, start = Offset(1f, h - 2f), end = Offset(w - 1f, h - 2f), strokeWidth = 1.6f)
            }
            RailIconType.HISTORY -> {
                // Clock / Rewind
                drawCircle(color = tint, radius = (w - 2f) / 2f, center = Offset(w / 2f, h / 2f), style = Stroke(width = 1.6f))
                drawLine(color = tint, start = Offset(w / 2f, h / 2f), end = Offset(w / 2f, 4f), strokeWidth = 1.6f)
                drawLine(color = tint, start = Offset(w / 2f, h / 2f), end = Offset(w - 4f, h / 2f), strokeWidth = 1.6f)
            }
            RailIconType.INSIGHTS -> {
                // 3 Vertical Bars
                drawRoundRect(color = tint, topLeft = Offset(2f, 8f), size = Size(3f, 7f), cornerRadius = CornerRadius(1f, 1f))
                drawRoundRect(color = tint, topLeft = Offset(6.5f, 4f), size = Size(3f, 11f), cornerRadius = CornerRadius(1f, 1f))
                drawRoundRect(color = tint, topLeft = Offset(11f, 1f), size = Size(3f, 14f), cornerRadius = CornerRadius(1f, 1f))
            }
            RailIconType.MORE -> {
                // 4 squares grid
                val s = 5f
                drawRoundRect(color = tint, topLeft = Offset(2f, 2f), size = Size(s, s), cornerRadius = CornerRadius(1.5f, 1.5f))
                drawRoundRect(color = tint, topLeft = Offset(9f, 2f), size = Size(s, s), cornerRadius = CornerRadius(1.5f, 1.5f))
                drawRoundRect(color = tint, topLeft = Offset(2f, 9f), size = Size(s, s), cornerRadius = CornerRadius(1.5f, 1.5f))
                drawRoundRect(color = tint, topLeft = Offset(9f, 9f), size = Size(s, s), cornerRadius = CornerRadius(1.5f, 1.5f))
            }
        }
    }
}

/**
 * 1. Hero Profile Card (Aqua Frosted Capsule)
 * Exactly reproduces Aman Tripathi's profile, dark action circles, gold plan pill, and usage capsule
 */
@Composable
fun AquaHeroCard(
    profile: MemberProfile,
    onAvatarClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AquaGlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Emerald Status Ring & Online Indicator
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.dp, AquaGlassTokens.MintGreen, CircleShape)
                        .clickable { onAvatarClick() }
                        .padding(2.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_aman),
                        contentDescription = profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                    // Bright neon online badge
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(AquaGlassTokens.MintNeon)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Name & Top Right Dark Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = profile.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AquaGlassTokens.TextNavy
                        )

                        // Dark action circles (⋮ and ↓ and 📅)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF262626))
                                    .clickable { onMenuClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Options",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF262626))
                                    .clickable { onDownloadClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.size(14.dp)) {
                                    val w = size.width
                                    val h = size.height
                                    drawLine(color = Color.White, start = Offset(w / 2f, 2f), end = Offset(w / 2f, h - 3f), strokeWidth = 1.8f)
                                    val path = Path().apply {
                                        moveTo(w / 2f - 4f, h - 6f)
                                        lineTo(w / 2f, h - 2f)
                                        lineTo(w / 2f + 4f, h - 6f)
                                    }
                                    drawPath(path = path, color = Color.White, style = Stroke(width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${profile.memberCode}  •  ${profile.age} yrs  •  ${profile.gender}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = AquaGlassTokens.TextMuted
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Badges row: 👑 Gold Plan + Active
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                                .border(0.5.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                                .padding(horizontal = 7.dp, vertical = 2.5.dp)
                        ) {
                            Text(
                                text = "👑 Gold Plan",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AquaGlassTokens.MintGreen)
                                .padding(horizontal = 9.dp, vertical = 2.5.dp)
                        ) {
                            Text(
                                text = "Active",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Usage Bar: 12 / 30 days used + Capsule
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "12 / 30 days used",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )

                // Sleek green gradient progress capsule
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD1FAE5))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.40f)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(AquaGlassTokens.MintGreen, AquaGlassTokens.MintNeon)
                                )
                            )
                    )
                }
            }
        }
    }
}

/**
 * 2. Payment & Plan Card (Coral Rose Frosted Glass + 3D Card Stack)
 */
@Composable
fun AquaPaymentCard(
    payment: PaymentPlanState,
    onReceiptClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AquaGlassCard(
        modifier = modifier,
        backgroundColor = Color(0xF7FFFFFF),
        borderColor = Color(0xFFFFD4D8)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Red briefcase / wallet icon square
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AquaGlassTokens.CoralRose),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            val w = size.width
                            val h = size.height
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(1f, 4f),
                                size = Size(w - 2f, h - 5f),
                                cornerRadius = CornerRadius(2.5f, 2.5f)
                            )
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(4.5f, 1.5f),
                                size = Size(w - 9f, 3f),
                                cornerRadius = CornerRadius(1.5f, 1.5f),
                                style = Stroke(width = 1.4f)
                            )
                            drawCircle(color = AquaGlassTokens.CoralRose, radius = 1.6f, center = Offset(w / 2f, 7.5f))
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Payment & Plan",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // DUE SOON ⚠️ Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .border(0.5.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                            .padding(horizontal = 7.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = "DUE SOON  ⚠️",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9A3412)
                        )
                    }
                }

                // Golden square with 3D crown
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFFBBF24), Color(0xFFD97706))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👑", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body Row: Amount + 3D Cards + Timeline
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Column: Amount + Dates + Green Card Stand
                Column(modifier = Modifier.width(96.dp)) {
                    Text(
                        text = "₹2,800",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AquaGlassTokens.CoralRose
                    )
                    Text(
                        text = "Due in 3 days",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.CoralDark
                    )
                    Text(
                        text = "16 Oct 2026",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AquaGlassTokens.TextMuted
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 3D Green Payment Stand / Terminal
                    Canvas(modifier = Modifier.size(width = 64.dp, height = 30.dp)) {
                        val path = Path().apply {
                            moveTo(6f, 16f)
                            lineTo(24f, 6f)
                            lineTo(56f, 10f)
                            lineTo(38f, 22f)
                            close()
                        }
                        drawPath(
                            path = path,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF34D399), Color(0xFF059669))
                            )
                        )
                        // Beveled base
                        val basePath = Path().apply {
                            moveTo(6f, 16f)
                            lineTo(38f, 22f)
                            lineTo(36f, 28f)
                            lineTo(4f, 22f)
                            close()
                        }
                        drawPath(path = basePath, color = Color(0xFF047857))
                        // Glowing card slot
                        drawRoundRect(
                            color = Color(0xFFA7F3D0),
                            topLeft = Offset(18f, 10f),
                            size = Size(18f, 4f),
                            cornerRadius = CornerRadius(1.5f, 1.5f)
                        )
                    }
                }

                // Center: 3D Floating Dual Credit Cards
                Box(
                    modifier = Modifier
                        .size(width = 110.dp, height = 64.dp)
                        .clickable { onReceiptClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Back Card (Cyan/Blue metallic tilted)
                        val backPath = Path().apply {
                            moveTo(28f, 8f)
                            lineTo(104f, 2f)
                            lineTo(96f, 36f)
                            lineTo(20f, 42f)
                            close()
                        }
                        drawPath(
                            path = backPath,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF0284C7), Color(0xFF0F172A))
                            )
                        )
                        // Magnetic stripe on back card
                        drawLine(
                            color = Color(0x66FFFFFF),
                            start = Offset(24f, 18f),
                            end = Offset(98f, 12f),
                            strokeWidth = 3f
                        )

                        // Front Card (Holographic Lavender/Cyan tilted)
                        val frontPath = Path().apply {
                            moveTo(10f, 22f)
                            lineTo(92f, 14f)
                            lineTo(84f, 56f)
                            lineTo(2f, 64f)
                            close()
                        }
                        drawPath(
                            path = frontPath,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFFE9D5FF), Color(0xFFA5F3FC), Color(0xFFDDD6FE))
                            )
                        )
                        // Gold EMV Chip on front card
                        drawRoundRect(
                            color = Color(0xFFFBBF24),
                            topLeft = Offset(16f, 34f),
                            size = Size(12f, 9f),
                            cornerRadius = CornerRadius(2f, 2f)
                        )
                        // Holographic shimmer lines
                        drawLine(
                            color = Color(0x80FFFFFF),
                            start = Offset(32f, 38f),
                            end = Offset(68f, 34f),
                            strokeWidth = 1.5f
                        )
                    }
                }

                // Right Column: Timeline / Last Paid info
                Column(
                    modifier = Modifier.width(76.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    // Timeline indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AquaGlassTokens.MintGreen)
                        )
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(1.5.dp)
                                .background(AquaGlassTokens.MintGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Last Paid",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = AquaGlassTokens.TextMuted,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "₹2,800",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "15 Sep 2026",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = AquaGlassTokens.TextLight,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

/**
 * 3. Attendance Card (Radial Gauge 90% + 3D Scanner Cube + Badges + Date Carousel)
 */
@Composable
fun AquaAttendanceCard(
    onDayClick: (CalendarDay) -> Unit = {},
    modifier: Modifier = Modifier
) {
    AquaGlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Green bar-chart icon square
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AquaGlassTokens.MintGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            drawRoundRect(color = Color.White, topLeft = Offset(1f, 7f), size = Size(3.5f, 9f), cornerRadius = CornerRadius(1.2f, 1.2f))
                            drawRoundRect(color = Color.White, topLeft = Offset(6f, 3f), size = Size(3.5f, 13f), cornerRadius = CornerRadius(1.2f, 1.2f))
                            drawRoundRect(color = Color.White, topLeft = Offset(11f, 5f), size = Size(3.5f, 11f), cornerRadius = CornerRadius(1.2f, 1.2f))
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Attendance",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "(Oct 2026)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.BlueRoyal
                    )
                }

                // Right circular navigation pill (<)
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(AquaGlassTokens.BlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "<",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.BlueCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Middle Section: 3D Biometric Cube + Donut Gauge + Status & Dates
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: 3D Glowing Biometric Scanner Cube
                Canvas(modifier = Modifier.size(width = 44.dp, height = 54.dp)) {
                    val w = size.width
                    val h = size.height

                    // Bottom base slab
                    val base = Path().apply {
                        moveTo(w / 2f, h - 6f)
                        lineTo(w - 2f, h - 14f)
                        lineTo(w / 2f, h - 22f)
                        lineTo(2f, h - 14f)
                        close()
                    }
                    drawPath(base, Brush.linearGradient(listOf(Color(0xFF047857), Color(0xFF10B981))))

                    // Middle tech slab with glowing beam
                    val mid = Path().apply {
                        moveTo(w / 2f, h - 16f)
                        lineTo(w - 4f, h - 23f)
                        lineTo(w / 2f, h - 30f)
                        lineTo(4f, h - 23f)
                        close()
                    }
                    drawPath(mid, Brush.linearGradient(listOf(Color(0xFF2DD4BF), Color(0xFF059669))))

                    // Top biometric scanner slab
                    val top = Path().apply {
                        moveTo(w / 2f, h - 28f)
                        lineTo(w - 4f, h - 35f)
                        lineTo(w / 2f, h - 42f)
                        lineTo(4f, h - 35f)
                        close()
                    }
                    drawPath(top, Brush.linearGradient(listOf(Color(0xFFA7F3D0), Color(0xFF34D399))))

                    // Biometric concentric scanning rings on top face
                    drawCircle(
                        color = Color.White.copy(alpha = 0.8f),
                        radius = 4f,
                        center = Offset(w / 2f, h - 35f),
                        style = Stroke(width = 1.2f)
                    )
                }

                // Center: 90% Donut Gauge
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(82.dp)
                ) {
                    Box(
                        modifier = Modifier.size(76.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 7.dp.toPx()
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
                                    listOf(AquaGlassTokens.MintGreen, Color(0xFF059669), AquaGlassTokens.MintGreen)
                                ),
                                startAngle = -90f,
                                sweepAngle = 324f, // 90%
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }
                        Text(
                            text = "90%",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AquaGlassTokens.TextNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "9 / 10",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )
                    Text(
                        text = "Open Days",
                        fontSize = 11.sp,
                        color = AquaGlassTokens.TextMuted
                    )
                    Text(
                        text = "↓ 2% vs prev",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.CoralRose
                    )
                }

                // Right: Status Badges (Top) + 4 Date Pills (Bottom)
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top 3 Status Badges in a Row: Present, Late, Absent
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Present
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(AquaGlassTokens.MintGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                            }
                            Text(text = "7", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = AquaGlassTokens.TextNavy)
                            Text(text = "Present", fontSize = 10.5.sp, color = AquaGlassTokens.TextSlate)
                        }

                        // Late
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(AquaGlassTokens.AmberWarning),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                            Text(text = "2", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = AquaGlassTokens.TextNavy)
                            Text(text = "Late", fontSize = 10.5.sp, color = AquaGlassTokens.TextSlate)
                        }

                        // Absent
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(AquaGlassTokens.CoralRose),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                            }
                            Text(text = "1", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = AquaGlassTokens.TextNavy)
                            Text(text = "Absent", fontSize = 10.5.sp, color = AquaGlassTokens.TextSlate)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bottom 4 Calendar Day Pills: Sun 12, Mon 13, Tue 14, Wed 15
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(
                            Triple("Sun", 12, DayAttendanceStatus.PRESENT),
                            Triple("Mon", 13, DayAttendanceStatus.PRESENT),
                            Triple("Tue", 14, DayAttendanceStatus.LATE),
                            Triple("Wed", 15, DayAttendanceStatus.PRESENT)
                        ).forEach { (day, num, status) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onDayClick(CalendarDay(num, 0, status)) }
                                    .padding(horizontal = 2.dp, vertical = 1.dp)
                            ) {
                                Text(text = day, fontSize = 11.sp, color = AquaGlassTokens.TextMuted)
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (status == DayAttendanceStatus.PRESENT) AquaGlassTokens.MintGreen
                                            else AquaGlassTokens.AmberWarning
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (status == DayAttendanceStatus.PRESENT) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                    } else {
                                        Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color.White))
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "$num", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AquaGlassTokens.TextSlate)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 4. Workout (Last 15 Days) Card with 3D Earbuds & 7-Bar Chart Peak
 */
@Composable
fun AquaWorkoutCard(
    onAnalyticsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AquaGlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Blue dumbbell icon square
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AquaGlassTokens.BlueCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            val w = size.width
                            val h = size.height
                            drawLine(color = Color.White, start = Offset(4f, h / 2f), end = Offset(w - 4f, h / 2f), strokeWidth = 2.2f)
                            drawRoundRect(color = Color.White, topLeft = Offset(1f, 4f), size = Size(3f, h - 8f), cornerRadius = CornerRadius(1.5f, 1.5f))
                            drawRoundRect(color = Color.White, topLeft = Offset(w - 4f, 4f), size = Size(3f, h - 8f), cornerRadius = CornerRadius(1.5f, 1.5f))
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Workout",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "(Last 15 Days)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.BlueRoyal
                    )
                }

                // 7D Toggle Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AquaGlassTokens.BlueLight)
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "7D",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.BlueRoyal
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Middle Section: 3D Earbuds + 7-Bar Chart
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: 3D Cyan Wireless Earbuds in charging case
                Canvas(modifier = Modifier.size(width = 44.dp, height = 62.dp)) {
                    val w = size.width
                    val h = size.height

                    // Case base pedestal
                    val base = Path().apply {
                        moveTo(w / 2f, h - 4f)
                        lineTo(w - 2f, h - 12f)
                        lineTo(w / 2f, h - 18f)
                        lineTo(2f, h - 12f)
                        close()
                    }
                    drawPath(base, Brush.linearGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B))))

                    // Glowing cyan LED rim
                    drawPath(base, Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7))), style = Stroke(width = 1.2f))

                    // Left Earbud
                    drawRoundRect(
                        color = Color(0xFF0284C7),
                        topLeft = Offset(8f, 12f),
                        size = Size(8f, 26f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawCircle(color = Color(0xFF38BDF8), radius = 6f, center = Offset(12f, 10f))

                    // Right Earbud
                    drawRoundRect(
                        color = Color(0xFF0369A1),
                        topLeft = Offset(w - 16f, 8f),
                        size = Size(8f, 26f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawCircle(color = Color(0xFF0284C7), radius = 6f, center = Offset(w - 12f, 6f))
                }

                Spacer(modifier = Modifier.width(6.dp))

                // 7-Bar Chart with labels & minute values
                val workoutBars = listOf(
                    Triple("6\nOct", 89, false),
                    Triple("7\nOct", 106, false),
                    Triple("8\nOct", 123, true), // Peak Emerald Green
                    Triple("9\nOct", 35, false),
                    Triple("10\nOct", 52, false),
                    Triple("11\nOct", 0, false),
                    Triple("12\nOct", 0, false)
                )

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    workoutBars.forEach { (date, mins, isPeak) ->
                        val barHeight = when {
                            mins >= 120 -> 52.dp
                            mins >= 100 -> 44.dp
                            mins >= 80 -> 36.dp
                            mins >= 50 -> 24.dp
                            mins >= 30 -> 16.dp
                            else -> 4.dp
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(26.dp)
                        ) {
                            Text(
                                text = if (mins > 0) "${mins}m" else "0",
                                fontSize = 10.5.sp,
                                fontWeight = if (isPeak) FontWeight.ExtraBold else FontWeight.SemiBold,
                                color = if (isPeak) AquaGlassTokens.EmeraldDark else AquaGlassTokens.TextNavy
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Box(
                                modifier = Modifier
                                    .width(16.dp)
                                    .height(barHeight)
                                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                    .background(
                                        if (isPeak) {
                                            Brush.verticalGradient(
                                                listOf(AquaGlassTokens.MintNeon, AquaGlassTokens.MintGreen)
                                            )
                                        } else if (mins > 0) {
                                            Brush.verticalGradient(
                                                listOf(Color(0xFF38BDF8), AquaGlassTokens.BlueCyan)
                                            )
                                        } else {
                                            Brush.verticalGradient(
                                                listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1))
                                            )
                                        }
                                    )
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = date,
                                fontSize = 10.5.sp,
                                color = AquaGlassTokens.TextMuted,
                                textAlign = TextAlign.Center,
                                lineHeight = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom 3 KPI Cards: Sessions, Total Duration, Average
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Card 1: 11 Sessions
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "11", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AquaGlassTokens.TextNavy)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Sessions", fontSize = 10.5.sp, color = AquaGlassTokens.TextMuted)
                    }
                    Text(text = "↑ 2 vs prev", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = AquaGlassTokens.MintGreen)
                }

                // Card 2: 823m Total Duration
                Column(
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "823m", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AquaGlassTokens.TextNavy)
                        Text(text = "↑ 14%", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = AquaGlassTokens.MintGreen)
                    }
                    Text(text = "Total Duration", fontSize = 10.5.sp, color = AquaGlassTokens.TextMuted)
                }

                // Card 3: 74m Average
                Column(
                    modifier = Modifier
                        .weight(0.9f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "74m", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AquaGlassTokens.TextNavy)
                    Text(text = "Average", fontSize = 10.5.sp, color = AquaGlassTokens.TextMuted)
                }
            }
        }
    }
}

/**
 * 5. Body Progress (30D) Card with Dual Sparklines, Top Exercise & Muscle Focus
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
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Purple crossed dumbbells icon square
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AquaGlassTokens.PurpleViolet),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            val w = size.width
                            val h = size.height
                            drawLine(color = Color.White, start = Offset(2f, 2f), end = Offset(w - 2f, h - 2f), strokeWidth = 2.2f)
                            drawLine(color = Color.White, start = Offset(w - 2f, 2f), end = Offset(2f, h - 2f), strokeWidth = 2.2f)
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Body Progress",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "(30D)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.PurpleViolet
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dual Stat Boxes: Weight & Muscle with Sparklines
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Weight Box
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(AquaGlassTokens.PurpleViolet),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "⚖", fontSize = 11.5.sp, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "Weight", fontSize = 11.5.sp, fontWeight = FontWeight.Medium, color = AquaGlassTokens.TextSlate)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "69.5 kg",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )
                    Text(
                        text = "↓ 1.2 kg",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.MintGreen
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Green Smooth Sparkline
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                    ) {
                        val pts = listOf(0.3f, 0.4f, 0.35f, 0.5f, 0.45f, 0.6f, 0.75f)
                        val stepX = size.width / (pts.size - 1)
                        val path = Path()
                        pts.forEachIndexed { i, p ->
                            val x = i * stepX
                            val y = size.height - (p * size.height)
                            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }
                        drawPath(path, AquaGlassTokens.MintGreen, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                    }
                }

                // Muscle Box
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(AquaGlassTokens.BlueCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "💪", fontSize = 11.5.sp, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "Muscle", fontSize = 11.5.sp, fontWeight = FontWeight.Medium, color = AquaGlassTokens.TextSlate)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "57.2 kg",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )
                    Text(
                        text = "↑ 1.8 kg",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.MintGreen
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Blue Smooth Sparkline
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                    ) {
                        val pts = listOf(0.2f, 0.25f, 0.35f, 0.45f, 0.6f, 0.7f, 0.85f)
                        val stepX = size.width / (pts.size - 1)
                        val path = Path()
                        pts.forEachIndexed { i, p ->
                            val x = i * stepX
                            val y = size.height - (p * size.height)
                            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }
                        drawPath(path, AquaGlassTokens.BlueRoyal, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Row: Top Exercise (Bench Press) + Muscle Focus (Sets)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Top Exercise (Bench Press)
                Column(
                    modifier = Modifier
                        .weight(1.1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Top Exercise",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = AquaGlassTokens.TextMuted
                    )
                    Text(
                        text = "Bench Press",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "65 → 67.5 kg",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AquaGlassTokens.TextNavy
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(+4%)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AquaGlassTokens.MintGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 3D Gym Flat Bench Graphic
                    Canvas(modifier = Modifier.size(width = 80.dp, height = 32.dp)) {
                        val w = size.width
                        val h = size.height

                        // Padded bench cushion
                        val cushion = Path().apply {
                            moveTo(10f, 10f)
                            lineTo(w - 6f, 4f)
                            lineTo(w - 14f, 16f)
                            lineTo(2f, 22f)
                            close()
                        }
                        drawPath(cushion, Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))))
                        drawPath(cushion, Color(0xFF38BDF8), style = Stroke(width = 1f))

                        // Front metal leg
                        drawLine(color = Color(0xFF334155), start = Offset(14f, 20f), end = Offset(14f, h - 2f), strokeWidth = 3f)
                        drawLine(color = Color(0xFF64748B), start = Offset(8f, h - 2f), end = Offset(20f, h - 2f), strokeWidth = 2.5f)

                        // Back metal leg
                        drawLine(color = Color(0xFF334155), start = Offset(w - 18f, 12f), end = Offset(w - 18f, h - 6f), strokeWidth = 3f)
                        drawLine(color = Color(0xFF64748B), start = Offset(w - 24f, h - 6f), end = Offset(w - 12f, h - 6f), strokeWidth = 2.5f)

                        // Center support bar
                        drawLine(color = Color(0xFF475569), start = Offset(14f, h - 8f), end = Offset(w - 18f, h - 12f), strokeWidth = 2f)
                    }
                }

                // Right: Muscle Focus (Sets)
                Column(
                    modifier = Modifier.weight(0.9f)
                ) {
                    Text(
                        text = "Muscle Focus (Sets)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AquaGlassTokens.TextNavy
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    MuscleFocusRow("Chest", 0.85f, listOf(Color(0xFFA7F3D0), Color(0xFFFCA5A5)))
                    MuscleFocusRow("Back", 0.70f, listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
                    MuscleFocusRow("Arms", 0.60f, listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
                    MuscleFocusRow("Legs", 0.50f, listOf(Color(0xFFFBBF24), Color(0xFFF59E0B)))
                    MuscleFocusRow("Others", 0.35f, listOf(Color(0xFF94A3B8), Color(0xFF64748B)))
                }
            }
        }
    }
}

@Composable
private fun MuscleFocusRow(name: String, progress: Float, gradient: List<Color>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 11.sp,
            color = AquaGlassTokens.TextMuted,
            modifier = Modifier.width(34.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(5.5.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2E8F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(gradient))
            )
        }
    }
}
