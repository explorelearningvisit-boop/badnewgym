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
// COMPONENT 1: TOP HEADER COMPONENT (Pixel-Perfect Clone of media_1793346029417_a6bb6f74.png)
// =============================================================================

@Composable
fun TopHeaderComponent(
    planTitle: String = "SILVER",
    checkInTime: String = "Today • 08:49 AM",
    isActive: Boolean = true,
    cardType: MemberCardType = MemberCardType.CHECK_IN,
    paymentStatus: PaymentStatus? = null,
    paidAmount: String = "₹800",
    paidDate: String = "12 Oct 2026",
    onCheckInClick: () -> Unit = {},
    onPaymentHeaderClick: () -> Unit = {},
    onPlanClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDue = paymentStatus == PaymentStatus.PENDING || cardType == MemberCardType.PAYMENT || paidAmount == "₹800"

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color(0xFFBAE6FD).copy(alpha = 0.85f)),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(InfographicTokens.HeaderGradient, RoundedCornerShape(26.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // =================================================================
            // LEFT: 3D Crown 👑 + Logo & 3-Line Brand Typography
            // =================================================================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "👑", fontSize = 17.sp)
                Column(verticalArrangement = Arrangement.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BAD ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            lineHeight = 13.sp
                        )
                        Text(
                            text = "GYM",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF059669),
                            lineHeight = 13.sp
                        )
                    }
                    Text(
                        text = "MEMBER INTELLIGENCE",
                        fontSize = 7.2.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2563EB),
                        letterSpacing = 0.2.sp,
                        lineHeight = 8.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "DISCIPLINE BUILDS FREEDOM",
                        fontSize = 6.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        letterSpacing = 0.1.sp,
                        lineHeight = 7.sp,
                        maxLines = 1
                    )
                }
            }

            // =================================================================
            // MIDDLE: Deep Emerald Green Stadium Pill (CHECK-IN Active)
            // =================================================================
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color.Transparent,
                border = BorderStroke(0.8.dp, Color(0xFF34D399).copy(alpha = 0.7f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF059669), Color(0xFF047857), Color(0xFF065F46))
                        )
                    )
                    .clickable { onCheckInClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(23.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "CHECK-IN",
                            tint = Color(0xFF34D399),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "CHECK-IN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.3.sp,
                            lineHeight = 11.sp,
                            maxLines = 1
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "Today • 08:49 AM",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 8.sp,
                                maxLines = 1
                            )
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = Color(0xFF10B981)
                            ) {
                                Text(
                                    text = "Active",
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 3.5.dp, vertical = 0.5.dp),
                                    lineHeight = 7.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // =================================================================
            // RIGHT: White Stadium Capsule (Payment Due ₹800 with Green Wallet)
            // =================================================================
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color.White.copy(alpha = 0.98f),
                border = BorderStroke(0.9.dp, Color(0xFFBAE6FD)),
                shadowElevation = 0.5.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .clickable { onPaymentHeaderClick() }
            ) {
                Row(
                    modifier = Modifier.padding(start = 3.5.dp, end = 5.dp, top = 2.dp, bottom = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.5.dp)
                ) {
                    // Green Leather Wallet with embossed white Rupee symbol
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF10B981), Color(0xFF059669))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "₹",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 13.sp
                        )
                    }

                    // Column: Payment Due, ₹800 (Bold Red), 12 Oct 2026
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = if (isDue) "Payment Due" else "Paid",
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            lineHeight = 7.sp,
                            maxLines = 1
                        )
                        Text(
                            text = paidAmount,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isDue) Color(0xFFDC2626) else Color(0xFF059669),
                            lineHeight = 13.5.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = paidDate,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
                            lineHeight = 7.sp,
                            maxLines = 1
                        )
                    }

                    // Royal Blue Chevron ›
                    Text(
                        text = "›",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2563EB),
                        lineHeight = 15.sp
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
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.92f),
        border = BorderStroke(0.8.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp)
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
// COMPONENT 3B: FULL-WIDTH DAYS PROGRESS CARD (44dp with Neon Glow)
// =============================================================================

@Composable
fun FullWidthDaysProgressCard(
    daysPassed: Int = 22,
    totalDays: Int = 30,
    percentage: Int = 73,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val progressFraction = (daysPassed.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.Transparent,
        border = BorderStroke(0.8.dp, Color(0xFF38BDF8).copy(alpha = 0.8f)),
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(InfographicTokens.DaysCardGradient)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Calendar icon
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
                    modifier = Modifier.size(17.dp)
                )
            }

            // Days Left text (2x larger)
            Text(
                text = "$daysPassed / $totalDays Days Left",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            // Neon cyan/lime capsule track
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(11.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White.copy(alpha = 0.25f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progressFraction)
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF3CF7B5), Color(0xFF00E7FF), Color(0xFF6C88FF))
                            )
                        )
                )
            }

            // Percentage (2x larger)
            Text(
                text = "$percentage%",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

// =============================================================================
// COMPONENT 4: STATS ROW COMPACT (48dp with 4 Pods)
// =============================================================================

@Composable
fun StatsRowCompact(
    height: String = "5'9\"",
    weight: String = "69.5 kg",
    weightDelta: String? = "↓ 2.5 kg",
    bmi: String = "22.8",
    bmiCategory: String = "Normal",
    goal: String = "Fat Loss",
    onGoalClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.95f),
        border = BorderStroke(0.8.dp, Color(0xFFE2E8F0)),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Pod 1: Height
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                BiometricRulerIcon(modifier = Modifier.size(22.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = height,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    Text(
                        text = "Height",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                }
            }

            Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

            // Pod 2: Weight
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1.1f)
            ) {
                BiometricScaleIcon(modifier = Modifier.size(22.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = weight,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    Text(
                        text = weightDelta ?: "Weight",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = if (weightDelta != null) Color(0xFF16A34A) else Color(0xFF64748B),
                        maxLines = 1
                    )
                }
            }

            Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

            // Pod 3: BMI
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                BiometricChartIcon(modifier = Modifier.size(22.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = bmi,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = bmiCategory,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF15803D),
                            maxLines = 1
                        )
                    }
                }
            }

            Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFFE2E8F0)))

            // Pod 4: Goal
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1.1f)
                    .clickable { onGoalClick() }
            ) {
                BiometricFlameIcon(modifier = Modifier.size(22.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = goal,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Text(
                        text = "Goal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// =============================================================================
// UNIFIED MASTER MEMBER CARD WIDGET (Packs Header, Profile, Stats & Progress)
// =============================================================================

@Composable
fun UnifiedBadGymMemberCard(
    planTitle: String = "SILVER",
    checkInTime: String = "Today • 08:49 AM",
    isActive: Boolean = true,
    cardType: MemberCardType = MemberCardType.CHECK_IN,
    paymentStatus: PaymentStatus? = null,
    paidAmount: String = "₹500",
    paidDate: String = "12 Oct 2026",
    name: String = "Ashwani Kumar Singh Srivastav",
    memberId: String = "BG215",
    age: Int = 28,
    gender: String = "Male",
    quote: String = "Discipline Builds Freedom",
    validity: String = "01 Aug 2026 – 31 Aug 2026 (30 Days Plan)",
    avatarRes: Int = R.drawable.avatar_ashwini,
    height: String = "5'9\"",
    weight: String = "69.5 kg",
    weightDelta: String? = "↓ 2.5 kg",
    bmi: String = "22.8",
    bmiCategory: String = "Normal",
    goal: String = "Fat Loss",
    daysPassed: Int = 22,
    totalDays: Int = 30,
    percentage: Int = 73,
    onCheckInClick: () -> Unit = {},
    onPaymentHeaderClick: () -> Unit = {},
    onPlanClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onGoalClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.98f),
        border = BorderStroke(1.2.dp, Color(0xFFBAE6FD).copy(alpha = 0.9f)),
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // 1. TOP HEADER ROW (Inside unified card)
            TopHeaderComponent(
                planTitle = planTitle,
                checkInTime = checkInTime,
                isActive = isActive,
                cardType = cardType,
                paymentStatus = paymentStatus,
                paidAmount = paidAmount,
                paidDate = paidDate,
                onCheckInClick = onCheckInClick,
                onPaymentHeaderClick = onPaymentHeaderClick,
                onPlanClick = onPlanClick
            )

            // 2. PROFILE DETAILS ROW
            ProfileSection(
                name = name,
                memberId = memberId,
                age = age,
                gender = gender,
                quote = quote,
                validity = validity,
                avatarRes = avatarRes,
                onAvatarClick = onAvatarClick
            )

            // 3. STATS ROW (Compact 4 Pods)
            StatsRowCompact(
                height = height,
                weight = weight,
                weightDelta = weightDelta,
                bmi = bmi,
                bmiCategory = bmiCategory,
                goal = goal,
                onGoalClick = onGoalClick
            )

            // 4. FULL-WIDTH DAYS PROGRESS BAR
            FullWidthDaysProgressCard(
                daysPassed = daysPassed,
                totalDays = totalDays,
                percentage = percentage,
                onClick = onProgressClick
            )
        }
    }
}

// =============================================================================
// COMPONENT 5: MENU ROW (4 TABS) (Matching Image 1: media_1793300075801_5ac48325.jpg)
// =============================================================================

@Composable
fun MenuRow4Tabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    attendanceRate: String = "90%",
    workoutAvg: String = "70m",
    paymentDue: String = "₹800",
    bodyWeight: String = "74.1 kg",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Tab 0: Attendance (Emerald Green Stadium Pill with Mini Progress Bar)
        NewMenuTabItem(
            title = "Attendance",
            value = attendanceRate,
            icon = Icons.Default.CalendarToday,
            iconBg = Brush.radialGradient(listOf(Color(0xFF22C55E), Color(0xFF059669))),
            textColor = Color(0xFF059669),
            barGradient = Brush.horizontalGradient(listOf(Color(0xFF22C55E), Color(0xFF059669))),
            progress = 0.90f,
            isSelected = selectedTabIndex == 0,
            hasAlert = false,
            activeTheme = 0,
            onClick = { onTabSelected(0) },
            modifier = Modifier.weight(1f)
        )

        // Tab 1: Workout (Purple Neon Stadium Pill with Mini Progress Bar)
        NewMenuTabItem(
            title = "Workout",
            value = workoutAvg,
            icon = Icons.Default.FitnessCenter,
            iconBg = Brush.radialGradient(listOf(Color(0xFFA855F7), Color(0xFF7C3AED))),
            textColor = Color(0xFF7C3AED),
            barGradient = Brush.horizontalGradient(listOf(Color(0xFFA855F7), Color(0xFF6D28D9))),
            progress = 0.70f,
            isSelected = selectedTabIndex == 1,
            hasAlert = false,
            activeTheme = 1,
            onClick = { onTabSelected(1) },
            modifier = Modifier.weight(1f)
        )

        // Tab 2: Payment (Glowing Orange Stadium Pill with Red ! Alert & Mini Progress Bar)
        NewMenuTabItem(
            title = "Payment",
            value = paymentDue,
            icon = Icons.Default.AccountBalanceWallet,
            iconBg = Brush.radialGradient(listOf(Color(0xFFFB923C), Color(0xFFEA580C))),
            textColor = if (selectedTabIndex == 2) Color.White else Color(0xFFEA580C),
            barGradient = Brush.horizontalGradient(listOf(Color(0xFFFDE047), Color(0xFFEA580C))),
            progress = 0.65f,
            isSelected = selectedTabIndex == 2,
            hasAlert = true,
            activeTheme = 2,
            onClick = { onTabSelected(2) },
            modifier = Modifier.weight(1f)
        )

        // Tab 3: Body (Sky Blue Stadium Pill with Mini Progress Bar)
        NewMenuTabItem(
            title = "Body",
            value = bodyWeight,
            icon = Icons.Default.Person,
            iconBg = Brush.radialGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7))),
            textColor = if (selectedTabIndex == 3) Color.White else Color(0xFF0284C7),
            barGradient = Brush.horizontalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0369A1))),
            progress = 0.74f,
            isSelected = selectedTabIndex == 3,
            hasAlert = false,
            activeTheme = 3,
            onClick = { onTabSelected(3) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NewMenuTabItem(
    title: String,
    value: String,
    icon: ImageVector,
    iconBg: Brush,
    textColor: Color,
    barGradient: Brush,
    progress: Float,
    isSelected: Boolean,
    hasAlert: Boolean = false,
    activeTheme: Int = 0,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Exact matching for Image 4 (media_1793300075801_5ac48325.jpg)
    val isOrangeGlow = (activeTheme == 2) && isSelected
    val isPurpleGlow = (activeTheme == 1) && isSelected
    val isBlueGlow = (activeTheme == 3) && isSelected
    val isGreenGlow = (activeTheme == 0) && isSelected

    val tabBg = when {
        isOrangeGlow -> Brush.horizontalGradient(
            listOf(Color(0xFFEA580C), Color(0xFFF97316), Color(0xFFFB923C))
        )
        isPurpleGlow -> Brush.horizontalGradient(
            listOf(Color(0xFF7C3AED), Color(0xFF8B5CF6), Color(0xFFA855F7))
        )
        isBlueGlow -> Brush.horizontalGradient(
            listOf(Color(0xFF0284C7), Color(0xFF0EA5E9), Color(0xFF38BDF8))
        )
        isGreenGlow -> Brush.horizontalGradient(
            listOf(Color(0xFF059669), Color(0xFF10B981), Color(0xFF34D399))
        )
        // Soft frosted translucent backgrounds with gentle color tint
        activeTheme == 0 -> Brush.horizontalGradient(
            listOf(Color(0xFFF0FDF4).copy(alpha = 0.92f), Color.White.copy(alpha = 0.96f))
        )
        activeTheme == 1 -> Brush.horizontalGradient(
            listOf(Color(0xFFFAF5FF).copy(alpha = 0.92f), Color.White.copy(alpha = 0.96f))
        )
        activeTheme == 2 -> Brush.horizontalGradient(
            listOf(Color(0xFFFFF7ED).copy(alpha = 0.92f), Color.White.copy(alpha = 0.96f))
        )
        else -> Brush.horizontalGradient(
            listOf(Color(0xFFF0F9FF).copy(alpha = 0.92f), Color.White.copy(alpha = 0.96f))
        )
    }

    val borderColor = when {
        isOrangeGlow -> Color(0xFFFDBA74)
        isPurpleGlow -> Color(0xFFDDD6FE)
        isBlueGlow -> Color(0xFFBAE6FD)
        isGreenGlow -> Color(0xFFA7F3D0)
        activeTheme == 0 -> Color(0xFFA7F3D0).copy(alpha = 0.8f)
        activeTheme == 1 -> Color(0xFFDDD6FE).copy(alpha = 0.8f)
        activeTheme == 2 -> Color(0xFFFED7AA).copy(alpha = 0.8f)
        else -> Color(0xFFBAE6FD).copy(alpha = 0.8f)
    }

    val isFullColoredBg = isSelected

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = Color.Transparent,
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
        shadowElevation = if (isSelected) 2.dp else 0.5.dp,
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(50.dp))
            .background(tabBg)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.5.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.5.dp)
        ) {
            // 3D Spherical Icon Badge
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Details Column: Title (+ alert), Value + chevron, Mini Progress Bar
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                // Top: Title (+ Red ! Alert if Payment)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 9.8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isFullColoredBg) Color.White.copy(alpha = 0.95f) else Color(0xFF0F172A),
                        maxLines = 1,
                        letterSpacing = (-0.2).sp
                    )
                    if (hasAlert) {
                        Box(
                            modifier = Modifier
                                .size(10.5.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "!",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 7.5.sp
                            )
                        }
                    }
                }

                // Middle: Value + Chevron
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = value,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isFullColoredBg) Color.White else textColor,
                        maxLines = 1
                    )
                    Text(
                        text = "›",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isFullColoredBg) Color.White else textColor
                    )
                }

                Spacer(modifier = Modifier.height(1.dp))

                // Bottom: Mini Horizontal Progress Bar (Image 4 exact)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.5.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            if (isFullColoredBg) Color.White.copy(alpha = 0.35f)
                            else Color(0xFFE2E8F0)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (isFullColoredBg) Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.8f), Color.White))
                                else barGradient
                            )
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
                    .height(38.dp)
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

            // 2. Top Legend Row (5-chip cards right below header bar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.5.dp)
            ) {
                AttendanceTopLegendChip(
                    dotColor = Color(0xFF10B981),
                    label = "Present",
                    count = "27 (90%)",
                    bgColor = Color(0xFFECFDF5),
                    borderColor = Color(0xFFA7F3D0),
                    textColor = Color(0xFF065F46),
                    modifier = Modifier.weight(1f)
                )
                AttendanceTopLegendChip(
                    dotColor = Color(0xFFEF4444),
                    label = "Absent",
                    count = "1 (3%)",
                    bgColor = Color(0xFFFEF2F2),
                    borderColor = Color(0xFFFECACA),
                    textColor = Color(0xFF991B1B),
                    modifier = Modifier.weight(1f)
                )
                AttendanceTopLegendChip(
                    dotColor = Color(0xFFF59E0B),
                    label = "Holiday",
                    count = "1 (3%)",
                    bgColor = Color(0xFFFFFBEB),
                    borderColor = Color(0xFFFDE68A),
                    textColor = Color(0xFF92400E),
                    modifier = Modifier.weight(1f)
                )
                AttendanceTopLegendChip(
                    dotColor = Color(0xFF64748B),
                    label = "Sunday",
                    count = "1 (3%)",
                    bgColor = Color(0xFFF8FAFC),
                    borderColor = Color(0xFFE2E8F0),
                    textColor = Color(0xFF334155),
                    modifier = Modifier.weight(1f)
                )
                AttendanceTopLegendChip(
                    dotColor = Color(0xFF94A3B8),
                    label = "Not Member",
                    count = "0 (0%)",
                    bgColor = Color(0xFFF1F5F9),
                    borderColor = Color(0xFFCBD5E1),
                    textColor = Color(0xFF64748B),
                    isOutline = true,
                    modifier = Modifier.weight(1.08f)
                )
            }

            // 3. Body Content Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 9.dp, vertical = 3.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Body Row: Left 3D Calendar + 90%, Right 7-Column Calendar Grid
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
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF047857),
                            letterSpacing = (-1).sp,
                            lineHeight = 32.sp
                        )
                        Text(
                            text = daysPresentText,
                            fontSize = 15.sp,
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
                                                    fontSize = 12.5.sp,
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
            }
        }
    }
}

@Composable
private fun AttendanceTopLegendChip(
    dotColor: Color,
    label: String,
    count: String,
    bgColor: Color,
    borderColor: Color,
    textColor: Color,
    isOutline: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        border = BorderStroke(0.9.dp, borderColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 3.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(1.5.dp)
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
                            if (isOutline) Modifier.border(0.9.dp, dotColor, CircleShape)
                            else Modifier.background(dotColor)
                        )
                )
                Text(
                    text = label,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    maxLines = 1
                )
            }
            Text(
                text = count,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                maxLines = 1
            )
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
            // 1. Header Bar: Trend Aurora Wave Header (40dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
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

            // 2. Body Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Top KPI Summary Pods: Best Day (95%), Average (82%), Min Day (60%)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // Best Day (Jul 29d - 95%)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF0FDF4),
                        border = BorderStroke(0.8.dp, Color(0xFFA7F3D0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🏆", fontSize = 11.sp)
                                }
                                Column {
                                    Text(
                                        text = "Best Day",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF64748B)
                                    )
                                    Text(
                                        text = "29d (Jul)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF0F172A)
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = Color(0xFF10B981)
                            ) {
                                Text(
                                    text = "95%",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    // Average (24.6d - 82%)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFFFBEB),
                        border = BorderStroke(0.8.dp, Color(0xFFFDE68A)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF59E0B).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "📈", fontSize = 11.sp)
                                }
                                Column {
                                    Text(
                                        text = "Average",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF64748B)
                                    )
                                    Text(
                                        text = "24.6d",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF0F172A)
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = Color(0xFFF59E0B)
                            ) {
                                Text(
                                    text = "82%",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    // Min Day (Mar 18d - 60%)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFEF2F2),
                        border = BorderStroke(0.8.dp, Color(0xFFFECACA)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEF4444).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "⬇️", fontSize = 11.sp)
                                }
                                Column {
                                    Text(
                                        text = "Min Day",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF64748B)
                                    )
                                    Text(
                                        text = "18d (Mar)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF0F172A)
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = Color(0xFFEF4444)
                            ) {
                                Text(
                                    text = "60%",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }

                // Chart Container with Y-Axis and Reference Lines
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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Y-Axis Labels
                    Column(
                        modifier = Modifier
                            .height(78.dp)
                            .padding(end = 4.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "100%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text(text = "75%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text(text = "50%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text(text = "25%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text(text = "0%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                    }

                    // Chart Bars with Canvas Reference Lines behind them
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(98.dp)
                    ) {
                        // Background Dashed Reference Lines (95%, 82%, 60%)
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .align(Alignment.BottomStart)
                                .padding(bottom = 18.dp)
                        ) {
                            val w = size.width
                            val h = size.height

                            // 95% Max Line (Red)
                            val y95 = h * (1f - 0.95f)
                            drawLine(
                                color = Color(0xFFEF4444).copy(alpha = 0.7f),
                                start = Offset(0f, y95),
                                end = Offset(w, y95),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                            )

                            // 82% Avg Line (Yellow)
                            val y82 = h * (1f - 0.82f)
                            drawLine(
                                color = Color(0xFFF59E0B).copy(alpha = 0.7f),
                                start = Offset(0f, y82),
                                end = Offset(w, y82),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 5f), 0f)
                            )

                            // 60% Min Line (Blue)
                            val y60 = h * (1f - 0.60f)
                            drawLine(
                                color = Color(0xFF3B82F6).copy(alpha = 0.7f),
                                start = Offset(0f, y60),
                                end = Offset(w, y60),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                            )
                        }

                        // 12 Bars on Foreground
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            months.forEachIndexed { idx, (month, pct, days) ->
                                val isOct = idx == 9
                                val isBest = idx == 6
                                val isNoMem = pct == "No Mem" || pct == "--"

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onMonthClick(idx) },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    // Top tag above bar (fixed 18dp container)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(18.dp),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        if (isBest) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFF10B981)
                                            ) {
                                                Text(
                                                    text = "Best",
                                                    fontSize = 6.5.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 2.5.dp, vertical = 0.5.dp)
                                                )
                                            }
                                        } else {
                                            Text(
                                                text = if (pct == "100%") "100%" else days,
                                                fontSize = 7.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isOct) Color(0xFF047857) else Color(0xFF64748B),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(1.dp))

                                    // Bar height proportional
                                    val barHeight = when (pct) {
                                        "100%" -> 54.dp
                                        "95%" -> 48.dp
                                        "90%" -> 42.dp
                                        "85%" -> 36.dp
                                        "80%" -> 32.dp
                                        "70%" -> 26.dp
                                        "60%" -> 20.dp
                                        else -> 18.dp
                                    }

                                    if (isNoMem) {
                                        Box(
                                            modifier = Modifier
                                                .width(20.dp)
                                                .height(barHeight)
                                                .clip(RoundedCornerShape(5.dp))
                                                .background(Color(0xFFF1F5F9))
                                                .border(
                                                    0.8.dp,
                                                    Color(0xFFCBD5E1),
                                                    RoundedCornerShape(5.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (pct == "No Mem") "No\nMem" else "--",
                                                fontSize = 6.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF94A3B8),
                                                textAlign = TextAlign.Center,
                                                lineHeight = 7.sp
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
                                                .width(20.dp)
                                                .height(barHeight)
                                                .clip(RoundedCornerShape(5.dp))
                                                .background(barGradient)
                                                .then(
                                                    if (isOct) Modifier.border(1.2.dp, Color(0xFF047857), RoundedCornerShape(5.dp))
                                                    else Modifier
                                                ),
                                            contentAlignment = Alignment.BottomCenter
                                        ) {
                                            Text(
                                                text = when (pct) {
                                                    "100%" -> "30d"
                                                    "85%" -> "85d"
                                                    "60%" -> "18d"
                                                    "95%" -> "95%"
                                                    else -> pct
                                                },
                                                fontSize = 7.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.White,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(bottom = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    // Month label below bar
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(16.dp),
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
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFF047857),
                                                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 0.5.dp)
                                                )
                                            }
                                        } else {
                                            Text(
                                                text = month,
                                                fontSize = 11.sp,
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

                // 3. Bottom Reference Legend
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Days Present (%)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF10B981)))
                        Text(text = "Days Present (%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                    }
                    // Average (82%)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                        Canvas(modifier = Modifier.width(12.dp).height(2.dp)) {
                            drawLine(
                                color = Color(0xFFF59E0B),
                                start = Offset.Zero,
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 3f))
                            )
                        }
                        Text(text = "Average (82%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                    }
                    // Min (60%)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                        Canvas(modifier = Modifier.width(12.dp).height(2.dp)) {
                            drawLine(
                                color = Color(0xFF3B82F6),
                                start = Offset.Zero,
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 3f))
                            )
                        }
                        Text(text = "Min (60%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                    }
                    // Max (95%)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
                        Canvas(modifier = Modifier.width(12.dp).height(2.dp)) {
                            drawLine(
                                color = Color(0xFFEF4444),
                                start = Offset.Zero,
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 3f))
                            )
                        }
                        Text(text = "Max (95%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
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
    isAchievement: Boolean = false,
    onKeepGoingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        border = BorderStroke(1.2.dp, Color(0xFFA855F7).copy(alpha = 0.5f)),
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(InfographicTokens.BottomBannerGradient)
            .clickable { onKeepGoingClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isAchievement) {
                // Achievement Mode (Matches Body Progress Screen in media_1793338088611_f0e49ddf.jpg)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Trophy in gold circular badge
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏆", fontSize = 19.sp)
                    }

                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "Latest Achievement",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDE047)
                        )
                        Text(
                            text = "✨ New Personal Best! 🎉",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 12.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Bench Press • 80 kg",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFF22C55E).copy(alpha = 0.9f))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "✓ Verified by Trainer",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "20 Oct 2026",
                                fontSize = 8.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            } else {
                // Normal Motivation Banner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.illustration_3d_footer_dumbbell),
                        contentDescription = "Workout Dumbbell",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(34.dp)
                    )
                    Column {
                        Text(
                            text = "DISCIPLINE TODAY",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White.copy(alpha = 0.8f),
                            letterSpacing = 1.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "Consistent Steps,",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "Brighter Days ☀️",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFDE047)
                            )
                        }
                    }
                }
            }

            // Right: Neon Yellow CTA Pill Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFFACC15))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Keep Going!",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "➔",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}
