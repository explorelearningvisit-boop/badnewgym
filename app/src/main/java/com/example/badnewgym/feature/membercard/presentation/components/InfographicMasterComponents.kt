package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.MemberCardType
import com.example.badnewgym.feature.membercard.domain.model.PaymentStatus

/**
 * BAD GYM — Master Member Intelligence Design Tokens
 * Extracted directly from official Infographic Specification media_1793290011933_8ef34b93.jpg
 */
object InfographicTokens {
    val PrimaryBlue = Color(0xFF6C88FF)
    val NeonPurple = Color(0xFFB56CFF)
    val ElectricCyan = Color(0xFF00E7FF)
    val SoftPink = Color(0xFFFF98D7)
    val MintGreen = Color(0xFF3CF7B5)
    val LimeGreen = Color(0xFFA8FF5B)
    val Yellow = Color(0xFFFFD34E)
    val Orange = Color(0xFFFF9F4D)
    val RedAlert = Color(0xFFFF5B6E)
    val SkyLight = Color(0xCCF3F4FF)
    val CardBg = Color(0xCCF8FEFF)
    val Glass = Color(0xCCE8F4FF)

    val HeaderGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFFE0F2FE).copy(alpha = 0.9f),
            Color(0xFFF3E8FF).copy(alpha = 0.9f),
            Color(0xFFEDE9FE).copy(alpha = 0.9f)
        )
    )

    val AuroraWaveGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF4F46E5),
            Color(0xFF7C3AED),
            Color(0xFF2563EB),
            Color(0xFF00E7FF),
            Color(0xFF8B5CF6)
        )
    )

    val TrendWaveGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF6366F1),
            Color(0xFF8B5CF6),
            Color(0xFF3B82F6),
            Color(0xFF00E7FF)
        )
    )

    val DaysCardGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF0284C7),
            Color(0xFF0099E5),
            Color(0xFF00E7FF)
        )
    )

    val AttendanceGradient = Brush.horizontalGradient(
        listOf(Color(0xFF059669), Color(0xFF10B981), Color(0xFF34D399))
    )
    val WorkoutGradient = Brush.horizontalGradient(
        listOf(Color(0xFF7C3AED), Color(0xFF8B5CF6), Color(0xFFA78BFA))
    )
    val PaymentGradient = Brush.horizontalGradient(
        listOf(Color(0xFFEA580C), Color(0xFFF97316), Color(0xFFFB923C))
    )
    val BodyGradient = Brush.horizontalGradient(
        listOf(Color(0xFF0284C7), Color(0xFF0EA5E9), Color(0xFF38BDF8))
    )
    val BottomBannerGradient = Brush.horizontalGradient(
        listOf(Color(0xFF3B0764), Color(0xFF581C87), Color(0xFF1D4ED8), Color(0xFF2563EB))
    )
}

// =============================================================================
// COMPONENT 1: TOP HEADER COMPONENT (56-64dp, Glass Gradient)
// =============================================================================

@Composable
fun TopHeaderComponent(
    planTitle: String = "SILVER",
    checkInTime: String = "Today • 08:49 AM",
    isActive: Boolean = true,
    cardType: MemberCardType = MemberCardType.CHECK_IN,
    paymentStatus: PaymentStatus? = null,
    onCheckInClick: () -> Unit = {},
    onPlanClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Dynamic sub-header and color according to card type
    val (headerSubtext, headerSubtextColor) = when (cardType) {
        MemberCardType.CHECK_IN -> Pair("MEMBER INTELLIGENCE", Color(0xFF2563EB))
        MemberCardType.CHECK_OUT -> Pair("CHECK-OUT ACTIVE", Color(0xFF0284C7))
        MemberCardType.PAYMENT -> {
            if (paymentStatus == PaymentStatus.PENDING) Pair("PAYMENT DUE", Color(0xFFD97706))
            else Pair("PAYMENT VERIFIED", Color(0xFF059669))
        }
        MemberCardType.TRAINER_CHECK_IN -> Pair("TRAINER ASSIGNED", Color(0xFF7C3AED))
        MemberCardType.MACHINE_FAULT -> Pair("EQUIPMENT ALERT", Color(0xFFDC2626))
        MemberCardType.MEMBER_PASS -> Pair("MEMBER INTELLIGENCE", Color(0xFF2563EB))
    }

    // Dynamic icon and background gradient for activity pill
    val (badgeIcon, badgeIconGradient) = when (cardType) {
        MemberCardType.CHECK_IN -> Pair(
            Icons.Default.Fingerprint,
            listOf(Color(0xFF00E7FF), Color(0xFF0284C7))
        )
        MemberCardType.CHECK_OUT -> Pair(
            Icons.Default.ExitToApp,
            listOf(Color(0xFF0284C7), Color(0xFF0EA5E9))
        )
        MemberCardType.PAYMENT -> Pair(
            Icons.Default.AccountBalanceWallet,
            if (paymentStatus == PaymentStatus.PENDING) listOf(Color(0xFFFBBF24), Color(0xFFD97706))
            else listOf(Color(0xFF34D399), Color(0xFF059669))
        )
        MemberCardType.TRAINER_CHECK_IN -> Pair(
            Icons.Default.FitnessCenter,
            listOf(Color(0xFFA78BFA), Color(0xFF7C3AED))
        )
        MemberCardType.MACHINE_FAULT -> Pair(
            Icons.Default.Warning,
            listOf(Color(0xFFF87171), Color(0xFFDC2626))
        )
        MemberCardType.MEMBER_PASS -> Pair(
            Icons.Default.Verified,
            listOf(Color(0xFF00E7FF), Color(0xFF10B981))
        )
    }

    // Dynamic title and subtitle
    val (badgeTitle, badgeSubtitle) = when (cardType) {
        MemberCardType.CHECK_IN -> Pair("CHECK-IN", checkInTime)
        MemberCardType.CHECK_OUT -> Pair("CHECK-OUT", "Today • 10:15 AM")
        MemberCardType.PAYMENT -> {
            if (paymentStatus == PaymentStatus.PENDING) Pair("PAYMENT", "₹800 Due")
            else Pair("PAYMENT", "₹500 • 08:49 AM")
        }
        MemberCardType.TRAINER_CHECK_IN -> Pair("TRAINER", "Coach Vikram")
        MemberCardType.MACHINE_FAULT -> Pair("FAULT", "Treadmill #3")
        MemberCardType.MEMBER_PASS -> Pair("MEMBER PASS", "30 Days Plan")
    }

    // Dynamic status badge text and color
    val (statusPillText, statusPillColor) = when (cardType) {
        MemberCardType.CHECK_IN -> Pair(if (isActive) "Active" else "Inactive", Color(0xFF10B981))
        MemberCardType.CHECK_OUT -> Pair("Out", Color(0xFF0284C7))
        MemberCardType.PAYMENT -> when (paymentStatus) {
            PaymentStatus.PENDING -> Pair("Pending", Color(0xFFF59E0B))
            PaymentStatus.FAILED -> Pair("Failed", Color(0xFFEF4444))
            else -> Pair("Paid ✓", Color(0xFF10B981))
        }
        MemberCardType.TRAINER_CHECK_IN -> Pair("Active", Color(0xFF8B5CF6))
        MemberCardType.MACHINE_FAULT -> Pair("Reported", Color(0xFFEF4444))
        MemberCardType.MEMBER_PASS -> Pair("Verified", Color(0xFF10B981))
    }

    val pillBorderColor = when (cardType) {
        MemberCardType.CHECK_IN -> Color(0xFF00E7FF).copy(alpha = 0.6f)
        MemberCardType.CHECK_OUT -> Color(0xFF0284C7).copy(alpha = 0.6f)
        MemberCardType.PAYMENT -> if (paymentStatus == PaymentStatus.PENDING) Color(0xFFFCD34D) else Color(0xFFA7F3D0)
        MemberCardType.TRAINER_CHECK_IN -> Color(0xFFD8B4FE).copy(alpha = 0.8f)
        MemberCardType.MACHINE_FAULT -> Color(0xFFFCA5A5)
        MemberCardType.MEMBER_PASS -> Color(0xFFBAE6FD).copy(alpha = 0.8f)
    }

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent,
        border = BorderStroke(1.2.dp, Color(0xFFBAE6FD).copy(alpha = 0.8f)),
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(InfographicTokens.HeaderGradient, RoundedCornerShape(28.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Sparkles ✨ + Logo + Dynamic Tagline
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "✨", fontSize = 13.sp)
                Text(text = "👑", fontSize = 15.sp)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BAD ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "GYM",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF059669)
                        )
                    }
                    Text(
                        text = headerSubtext,
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Black,
                        color = headerSubtextColor,
                        letterSpacing = 0.3.sp
                    )
                    Text(
                        text = "DISCIPLINE BUILDS FREEDOM",
                        fontSize = 6.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Middle: Dynamic Activity Pill with Electric Gradient
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, pillBorderColor),
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Brush.horizontalGradient(badgeIconGradient))
                    .clickable { onCheckInClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = badgeIcon,
                            contentDescription = badgeTitle,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Column {
                        Text(
                            text = badgeTitle,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.3.sp
                        )
                        Text(
                            text = badgeSubtitle,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE0F2FE)
                        )
                    }

                    // Status Pill
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = statusPillColor
                    ) {
                        Text(
                            text = statusPillText,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Right: Starter Plan Badge
            Surface(
                shape = RoundedCornerShape(9.dp),
                color = Color.White.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .clip(RoundedCornerShape(9.dp))
                    .clickable { onPlanClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFFDE047), Color(0xFFF59E0B))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⭐", fontSize = 10.sp)
                    }
                    Column {
                        Text(
                            text = "Starter Plan",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = planTitle,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

// =============================================================================
// COMPONENT 2: PROFILE SECTION (24dp Card with Watermark Silhouette)
// =============================================================================

@Composable
fun ProfileSection(
    name: String = "Ashwani Kumar Singh Srivastav",
    memberId: String = "BG215",
    age: Int = 28,
    gender: String = "Male",
    quote: String = "Discipline Builds Freedom",
    validity: String = "01 Aug 2026 – 31 Aug 2026 (30 Days Plan)",
    avatarRes: Int = R.drawable.avatar_ashwini,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.95f),
        border = BorderStroke(1.2.dp, Color(0xFFBAE6FD).copy(alpha = 0.8f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 11.dp, vertical = 9.dp)
        ) {
            // Watermark on right: Crown + "Stronger Healthier Happier"
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "👑",
                    fontSize = 15.sp,
                    modifier = Modifier.alpha(0.35f)
                )
                Text(
                    text = "Stronger\nHealthier\nHappier",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA855F7).copy(alpha = 0.35f),
                    textAlign = TextAlign.End,
                    lineHeight = 13.sp
                )
            }

            // Foreground Profile Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                // Avatar (68dp) with cyan/mint/blue halo ring + star badge
                Box(
                    modifier = Modifier.size(68.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(68.dp)) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFF00E7FF),
                                    Color(0xFF3CF7B5),
                                    Color(0xFF6C88FF),
                                    Color(0xFF3CF7B5),
                                    Color(0xFF00E7FF)
                                )
                            ),
                            radius = size.minDimension / 2f - 2f,
                            style = Stroke(width = 2.4.dp.toPx())
                        )
                    }

                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                            .clickable { onAvatarClick() }
                    )

                    // Blue Star Pin Badge on bottom right of avatar
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 1.dp, y = 1.dp)
                            .size(17.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E3A8A))
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "★",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                // Member Details Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Name + Edit Pencil Icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            letterSpacing = (-0.3).sp
                        )
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = Color(0xFF64748B),
                            modifier = Modifier
                                .size(13.dp)
                                .clickable { onAvatarClick() }
                        )
                    }

                    // Demographics
                    Text(
                        text = "$memberId  |  $age yrs  |  $gender",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )

                    // Quote + [✔ Active] Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "\u201C$quote\u201D",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color(0xFF10B981)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(9.dp)
                                )
                                Text(
                                    text = "Active",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Validity
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = validity,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF059669)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// COMPONENT 3: PROGRESS & PAYMENT ROW (48dp Split Row)
// =============================================================================

@Composable
fun ProgressAndPaymentRow(
    daysPassed: Int = 22,
    totalDays: Int = 30,
    paidAmount: String = "₹500",
    paidTime: String = "08:49 AM",
    onProgressClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val daysLeft = totalDays - daysPassed
    val progressFraction = (daysPassed.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        // Left Card: Progress (Days Left) with full Electric Cyan/Blue Gradient
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.8f)),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(InfographicTokens.DaysCardGradient)
                .clickable { onProgressClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // White translucent calendar icon box
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
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

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Top: "22 / 30 Days Left"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "$daysPassed / $totalDays",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Days Left",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE0F2FE)
                        )
                    }

                    // Bottom: Progress bar track + "8 Days Left" pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // 8dp progress bar
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White.copy(alpha = 0.3f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progressFraction)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF3CF7B5), Color(0xFFA8FF5B))
                                        )
                                    )
                            )
                        }

                        // "8 Days Left" pill
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color.White.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "$daysLeft Days Left",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }

        // Right Card: Payment Status (Paid ₹500 08:49 AM)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White.copy(alpha = 0.95f),
            border = BorderStroke(1.dp, Color(0xFFA7F3D0).copy(alpha = 0.8f)),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onPaymentClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Mint wallet icon box
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFD1FAE5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "Paid",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF065F46)
                            )
                            Box(
                                modifier = Modifier
                                    .size(11.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(8.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = paidAmount,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = paidTime,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

// =============================================================================
// COMPONENT 4: STATS ROW COMPACT (48dp with 4 Pods)
// =============================================================================

@Composable
fun StatsRowCompact(
    height: String = "5'9\"",
    weight: String = "74.1 kg",
    bmi: String = "23.5",
    bmiCategory: String = "Normal",
    goal: String = "Fat Loss",
    onGoalClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.95f),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Pod 1: Height
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.5.dp),
                modifier = Modifier.weight(1f)
            ) {
                BiometricRulerIcon(modifier = Modifier.size(20.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = height,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    Text(
                        text = "Height",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                }
            }

            Box(modifier = Modifier.width(1.dp).height(22.dp).background(Color(0xFFE2E8F0)))

            // Pod 2: Weight
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.5.dp),
                modifier = Modifier.weight(1.05f)
            ) {
                BiometricScaleIcon(modifier = Modifier.size(20.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = weight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    Text(
                        text = "Weight",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                }
            }

            Box(modifier = Modifier.width(1.dp).height(22.dp).background(Color(0xFFE2E8F0)))

            // Pod 3: BMI with [Normal] green pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.5.dp),
                modifier = Modifier.weight(1.1f)
            ) {
                BiometricChartIcon(modifier = Modifier.size(20.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.5.dp)
                    ) {
                        Text(
                            text = bmi,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1
                        )
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color(0xFF10B981)
                        ) {
                            Text(
                                text = bmiCategory,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 3.5.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "BMI",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                }
            }

            Box(modifier = Modifier.width(1.dp).height(22.dp).background(Color(0xFFE2E8F0)))

            // Pod 4: Goal with chevron
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.5.dp),
                modifier = Modifier
                    .weight(1.15f)
                    .clickable { onGoalClick() }
            ) {
                BiometricFlameIcon(modifier = Modifier.size(20.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Text(
                            text = goal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(11.dp)
                        )
                    }
                    Text(
                        text = "Goal",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// =============================================================================
// COMPONENT 5: MENU ROW (4 TABS) (56dp Neon Gradient Buttons)
// =============================================================================

@Composable
fun MenuRow4Tabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    attendanceRate: String = "90%",
    workoutAvg: String = "70m",
    paymentDue: String = "₹800",
    bodyWeight: String = "69.5 kg",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Tab 0: Attendance (Green Neon Gradient)
        MenuTabItem(
            title = "Attendance",
            value = attendanceRate,
            icon = Icons.Default.CalendarToday,
            gradient = InfographicTokens.AttendanceGradient,
            isSelected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) },
            modifier = Modifier.weight(1f)
        )

        // Tab 1: Workout (Purple Neon Gradient)
        MenuTabItem(
            title = "Workout",
            value = workoutAvg,
            icon = Icons.Default.FitnessCenter,
            gradient = InfographicTokens.WorkoutGradient,
            isSelected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) },
            modifier = Modifier.weight(1f)
        )

        // Tab 2: Payment (Orange Neon Gradient)
        MenuTabItem(
            title = "Payment",
            value = paymentDue,
            icon = Icons.Default.AccountBalanceWallet,
            gradient = InfographicTokens.PaymentGradient,
            isSelected = selectedTabIndex == 2,
            onClick = { onTabSelected(2) },
            modifier = Modifier.weight(1f)
        )

        // Tab 3: Body (Sky Blue Neon Gradient)
        MenuTabItem(
            title = "Body",
            value = bodyWeight,
            icon = Icons.Default.Person,
            gradient = InfographicTokens.BodyGradient,
            isSelected = selectedTabIndex == 3,
            onClick = { onTabSelected(3) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MenuTabItem(
    title: String,
    value: String,
    icon: ImageVector,
    gradient: Brush,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = BorderStroke(
            if (isSelected) 2.dp else 0.8.dp,
            if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Icon in circular semi-transparent white box
            Box(
                modifier = Modifier
                    .size(23.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = value,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        text = "›",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// =============================================================================
// COMPONENT 6: ATTENDANCE SECTION (7-Column Calendar Grid View)
// =============================================================================

@Composable
fun AttendanceSection(
    monthTitle: String = "Oct 2026",
    percentage: String = "90%",
    daysPresentText: String = "27 / 30 Days",
    onMonthPrev: () -> Unit = {},
    onMonthNext: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF8FEFA),
        border = BorderStroke(1.2.dp, Color(0xFFA7F3D0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 1. Aurora Wave Header Bar (44dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(InfographicTokens.AuroraWaveGradient)
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
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
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "Attendance",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    // Month Stepper Pill < Oct 2026 >
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        border = BorderStroke(0.8.dp, Color(0xFFCBD5E1))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "‹",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF334155),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { onMonthPrev() }
                            )
                            Text(
                                text = monthTitle,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "›",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF334155),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { onMonthNext() }
                            )
                        }
                    }
                }
            }

            // Body Content Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // 2. Body Row: Left 3D Calendar + 90%, Right 7-Column Calendar Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Left Column: 3D Calendar + 90% + 27/30 Days
                    Column(
                        modifier = Modifier.width(96.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.illustration_3d_attendance_calendar),
                            contentDescription = "Attendance 3D Calendar",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(54.dp)
                        )
                        Text(
                            text = percentage,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF047857),
                            letterSpacing = (-1).sp,
                            lineHeight = 32.sp
                        )
                        Text(
                            text = daysPresentText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    // Right Column: 7-Column Calendar Grid (Mon .. Sun)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        // Day of week headers
                        val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            weekDays.forEach { day ->
                                Text(
                                    text = day,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // 5 Weeks of Calendar Rows
                        val days = listOf(
                            listOf(Pair(1, "N"), Pair(2, "P"), Pair(3, "P"), Pair(4, "N"), Pair(5, "P"), Pair(6, "H"), Pair(7, "S")),
                            listOf(Pair(8, "P"), Pair(9, "P"), Pair(10, "P"), Pair(11, "N"), Pair(12, "P"), Pair(13, "P"), Pair(14, "S")),
                            listOf(Pair(15, "A"), Pair(16, "P"), Pair(17, "P"), Pair(18, "N"), Pair(19, "P"), Pair(20, "P"), Pair(21, "S")),
                            listOf(Pair(22, "P"), Pair(23, "P"), Pair(24, "P"), Pair(25, "N"), Pair(26, "P"), Pair(27, "P"), Pair(28, "S")),
                            listOf(Pair(29, "P"), Pair(30, "P"), Pair(31, "P"), Pair(0, ""), Pair(0, ""), Pair(0, ""), Pair(0, ""))
                        )

                        days.forEach { week ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                week.forEach { (dateNum, type) ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 1.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (dateNum > 0) {
                                            val (bgColor, textColor) = when (type) {
                                                "P" -> Pair(Color(0xFF10B981), Color.White)
                                                "A" -> Pair(Color(0xFFFF5B6E), Color.White)
                                                "H" -> Pair(Color(0xFFFFD34E), Color(0xFF78350F))
                                                "S" -> Pair(Color(0xFFE2E8F0), Color(0xFF475569))
                                                else -> Pair(Color(0xFFF1F5F9), Color(0xFF64748B))
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .clip(CircleShape)
                                                    .background(bgColor),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = dateNum.toString(),
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black,
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

                // 3. Legend Row (Horizontal small chips with dividers)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendChip(dotColor = Color(0xFF10B981), label = "P", count = "27")
                    Text(text = "|", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                    LegendChip(dotColor = Color(0xFFFF5B6E), label = "A", count = "1")
                    Text(text = "|", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                    LegendChip(dotColor = Color(0xFFFFD34E), label = "H", count = "1")
                    Text(text = "|", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                    LegendChip(dotColor = Color(0xFF94A3B8), label = "S", count = "1")
                    Text(text = "|", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                    LegendChip(dotColor = Color.Transparent, isOutline = true, label = "N", count = "0")
                }
            }
        }
    }
}

@Composable
private fun LegendChip(
    dotColor: Color,
    label: String,
    count: String,
    isOutline: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .then(
                    if (isOutline) Modifier.border(0.8.dp, Color(0xFF94A3B8), CircleShape)
                    else Modifier.background(dotColor)
                )
        )
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF475569)
        )
        Text(
            text = count,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0F172A)
        )
    }
}

// =============================================================================
// COMPONENT 7: MONTHLY TREND SECTION (12-Month Bar Chart)
// =============================================================================

@Composable
fun MonthlyTrendSection(
    selectedMonthIndex: Int = 9, // Oct (0-based)
    onMonthClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF8FEFA),
        border = BorderStroke(1.2.dp, Color(0xFFA7F3D0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Bar: Trend Aurora Wave Header (40dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(InfographicTokens.TrendWaveGradient)
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
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
                                .size(22.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            BiometricChartIcon(modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = "Monthly Attendance Trend",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = BorderStroke(0.8.dp, Color(0xFFCBD5E1))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "12 Months",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF475569)
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }

            // Chart Body Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 9.dp, vertical = 7.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val months = listOf(
                    Triple("Jan", "100%", "30d"),
                    Triple("Feb", "85%", "24d"),
                    Triple("Mar", "60%", "18d"),
                    Triple("Apr", "No Mem", "-"),
                    Triple("May", "90%", "27d"),
                    Triple("Jun", "70%", "21d"),
                    Triple("Jul", "95%", "29d"),
                    Triple("Aug", "No Mem", "-"),
                    Triple("Sep", "80%", "24d"),
                    Triple("Oct", "90%", "27d"),
                    Triple("Nov", "--", "-"),
                    Triple("Dec", "--", "-")
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    months.forEachIndexed { idx, (month, pct, days) ->
                        val isOct = idx == 9
                        val isMin = idx == 2
                        val isBest = idx == 6
                        val isNoMem = pct == "No Mem" || pct == "--"

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onMonthClick(idx) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            // Badge / Days count above bar (fixed 16dp height container for perfect baseline)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                if (isMin) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFFDE68A)
                                    ) {
                                        Text(
                                            text = "✔ Min",
                                            fontSize = 7.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFFB45309),
                                            modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.5.dp)
                                        )
                                    }
                                } else if (isBest) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFA7F3D0)
                                    ) {
                                        Text(
                                            text = "Best",
                                            fontSize = 7.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF047857),
                                            modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.5.dp)
                                        )
                                    }
                                } else {
                                    Text(
                                        text = days,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isOct) Color(0xFF047857) else Color(0xFF64748B),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            // Candle Bar: 100% max 58dp down to 20dp proportionally scaled
                            val barHeight = when (pct) {
                                "100%" -> 58.dp
                                "95%" -> 50.dp
                                "90%" -> 42.dp
                                "85%" -> 36.dp
                                "80%" -> 32.dp
                                "70%" -> 26.dp
                                "60%" -> 20.dp
                                else -> 20.dp
                            }

                            if (isNoMem) {
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .height(barHeight)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .border(
                                            0.8.dp,
                                            Color(0xFFCBD5E1),
                                            RoundedCornerShape(6.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (pct == "No Mem") "No\nMem" else "--",
                                        fontSize = 6.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF94A3B8),
                                        textAlign = TextAlign.Center,
                                        lineHeight = 7.5.sp
                                    )
                                }
                            } else {
                                val barGradient = when {
                                    pct == "60%" || pct == "70%" -> Brush.verticalGradient(
                                        listOf(Color(0xFFFBBF24), Color(0xFFF59E0B))
                                    )
                                    else -> Brush.verticalGradient(
                                        listOf(Color(0xFF34D399), Color(0xFF10B981), Color(0xFF059669))
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .height(barHeight)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(barGradient)
                                        .then(
                                            if (isOct) Modifier.border(1.2.dp, Color(0xFF047857), RoundedCornerShape(6.dp))
                                            else Modifier
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = pct,
                                        fontSize = 7.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            // Month Label: fixed 18dp height box guarantees all candle bars share the same baseline
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(18.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isOct) {
                                    Surface(
                                        shape = RoundedCornerShape(50.dp),
                                        color = Color.White,
                                        border = BorderStroke(1.dp, Color(0xFF10B981))
                                    ) {
                                        Text(
                                            text = month,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF047857),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.5.dp)
                                        )
                                    }
                                } else {
                                    Text(
                                        text = month,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// COMPONENT 8: BOTTOM MOTIVATION BANNER (56-64dp Full Width with CTA)
// =============================================================================

@Composable
fun BottomMotivationBanner(
    onKeepGoingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        border = BorderStroke(1.2.dp, Color(0xFFA855F7).copy(alpha = 0.5f)),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(InfographicTokens.BottomBannerGradient)
            .clickable { onKeepGoingClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Purple Dumbbell
            Image(
                painter = painterResource(id = R.drawable.illustration_3d_footer_dumbbell),
                contentDescription = "Workout Dumbbell",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp)
            )

            // Middle: Motto
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "DISCIPLINE TODAY",
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF93C5FD),
                    letterSpacing = 0.8.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "Consistent Steps, ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Brighter Days ☀️",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFDE047)
                    )
                }
            }

            // Right: Yellow CTA Button "Keep Going! ->"
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFFDE047),
                modifier = Modifier.clip(RoundedCornerShape(50.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "Keep Going!",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "➔",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                }
            }
        }
    }
}

