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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.AttendanceSectionData
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardType
import com.example.badnewgym.feature.membercard.domain.model.PaymentStatus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * BAD GYM — Master Member Intelligence Design Tokens
 * Extracted directly from official Infographic Specification media_1793290011933_8ef34b93.jpg
 */
object InfographicTokens {
    // ---- BAD GYM Reference Palette (media_1793376151174_5cd06942.jpg) ----
    val MainBg = Color(0xFFFFF9F2)          // Warm Cream Main Canvas
    val PageTop = MainBg
    val PageMid = Color(0xFFFFF7ED)
    val PageBottom = MainBg
    val SurfaceMint = Color(0xFFF0FDF4)
    val Surface = Color(0xFFFFFFFF)         // Pure White Card Surface
    val SurfaceSoft = Color(0xFFFFF5E9)     // Soft Ivory Tint
    val Ink = Color(0xFF18243B)             // High-Contrast Deep Navy Text
    val InkSoft = Color(0xFF334155)         // Slate Navy
    val Muted = Color(0xFF687487)           // Cool Grey Secondary Text
    val Hairline = Color(0xFFE2E8F0)        // Subtle Card Border

    // Multi-color Accents
    val Purple = Color(0xFF7428E8)
    val Magenta = Color(0xFFD92BAF)
    val Pink = Color(0xFFF34B9A)
    val Orange = Color(0xFFFF963C)
    val Yellow = Color(0xFFFFD45C)
    val Cyan = Color(0xFF35C9F5)
    val Blue = Color(0xFF438DFF)
    val Emerald = Color(0xFF078A69)
    val Amber = Color(0xFFEAA12E)
    val RedAlert = Color(0xFFE53945)
    val HolidayGray = Color(0xFFE9EEF0)

    // Semantic colors
    val Brand = Emerald
    val BrandDeep = Color(0xFF056149)
    val BrandSoft = Color(0xFFE6F5F0)
    val Gold = Amber
    val GoldSoft = Color(0xFFFEF3C7)
    val Danger = RedAlert
    val DangerSoft = Color(0xFFFEE2E2)
    val Teal = Cyan
    val TealSoft = Color(0xFFE0F2FE)
    val BlueSoft = Color(0xFFE0E7FF)

    // Legacy tokens compatibility
    val PrimaryBlue = Blue
    val NeonPurple = Purple
    val ElectricCyan = Cyan
    val SoftPink = Pink
    val MintGreen = Color(0xFF34D399)
    val LimeGreen = Color(0xFFA8FF5B)
    val SkyLight = Color(0xFFF0F9FF)
    val CardBg = Surface
    val Glass = Color(0xCCE8F4FF)

    // Gradients from Reference Sheet (Sections 2 & 9)
    val HeaderGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF7428E8), // Purple
            Color(0xFFD92BAF), // Magenta
            Color(0xFFF34B9A), // Pink
            Color(0xFFFF963C)  // Orange
        )
    )

    val BorderGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFFE84BAF), // Magenta/Pink
            Color(0xFF8B54F5), // Purple
            Color(0xFF45C9F5), // Cyan
            Color(0xFFFFC45B)  // Yellow/Gold
        )
    )

    val ThinProgressGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFFFF9D32), // Warm Orange
            Color(0xFFEF36A8)  // Vibrant Magenta
        )
    )

    val AuroraWaveGradient = HeaderGradient
    val TrendWaveGradient = Brush.horizontalGradient(listOf(Color(0xFF35C9F5), Color(0xFF438DFF), Color(0xFF7428E8)))
    val DaysCardGradient = ThinProgressGradient
    val AttendanceGradient = Brush.horizontalGradient(listOf(Color(0xFF078A69), Color(0xFF10B981)))
    val WorkoutGradient = Brush.horizontalGradient(listOf(Color(0xFF7428E8), Color(0xFF9333EA)))
    val PaymentGradient = Brush.horizontalGradient(listOf(Color(0xFFFF963C), Color(0xFFF97316)))
    val BodyGradient = Brush.horizontalGradient(listOf(Color(0xFF35C9F5), Color(0xFF0284C7)))
    val BottomBannerGradient = HeaderGradient
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
    paidAmount: String = "₹2,800",
    paidDate: String = "03 Nov 2026",
    onCheckInClick: () -> Unit = {},
    onPaymentHeaderClick: () -> Unit = {},
    onPlanClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDue = paymentStatus == PaymentStatus.PENDING || cardType == MemberCardType.PAYMENT || paidAmount == "₹2,800" || paidAmount == "₹800"

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(InfographicTokens.HeaderGradient, RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // =================================================================
            // LEFT: 3D Crown 👑 + "BAD GYM" + "MEMBER INTELLIGENCE" (White & Gold)
            // =================================================================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "👑", fontSize = 16.sp)
                Column(verticalArrangement = Arrangement.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BAD ",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 12.5.sp
                        )
                        Text(
                            text = "GYM",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFFD45C), // Vibrant Yellow/Gold
                            lineHeight = 12.5.sp
                        )
                    }
                    Text(
                        text = "MEMBER INTELLIGENCE",
                        fontSize = 6.8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.95f),
                        letterSpacing = 0.2.sp,
                        lineHeight = 7.5.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "DISCIPLINE BUILDS FREEDOM",
                        fontSize = 6.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.82f),
                        letterSpacing = 0.1.sp,
                        lineHeight = 6.8.sp,
                        maxLines = 1
                    )
                }
            }

            // =================================================================
            // MIDDLE: Translucent Glowing Stadium Capsule (CHECK-IN)
            // =================================================================
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFEF36A8).copy(alpha = 0.45f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.45f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .clickable { onCheckInClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "CHECK-IN",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "CHECK-IN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.3.sp,
                            lineHeight = 10.5.sp,
                            maxLines = 1
                        )
                        Text(
                            text = checkInTime,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.88f),
                            lineHeight = 7.5.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            // =================================================================
            // RIGHT: White Stadium Capsule (₹2,800 Payment Due / 03 Nov 2026) + Flex Character
            // =================================================================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, InfographicTokens.Hairline),
                    shadowElevation = 1.dp,
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
                                .size(22.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF10B981), Color(0xFF078A69))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "₹",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 12.sp
                            )
                        }

                        // Column: ₹2,800 (Bold Red) + 03 Nov 2026
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = paidAmount,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFE53945),
                                lineHeight = 12.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = paidDate,
                                fontSize = 6.8.sp,
                                fontWeight = FontWeight.Bold,
                                color = InfographicTokens.Muted,
                                lineHeight = 7.sp,
                                maxLines = 1
                            )
                        }

                        // Royal Blue Chevron ›
                        Text(
                            text = "›",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF438DFF),
                            lineHeight = 13.sp
                        )
                    }
                }

                // 3D Bodybuilder graphic matching reference top right
                Image(
                    painter = painterResource(id = R.drawable.illustration_character_flex),
                    contentDescription = "Fitness Silhouette",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(34.dp)
                )
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
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = InfographicTokens.Surface,
        border = BorderStroke(1.5.dp, InfographicTokens.BorderGradient),
        shadowElevation = 0.5.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // A single clean avatar. The old card had three competing badges.
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                // Golden halo outer ring matching reference
                Canvas(modifier = Modifier.size(64.dp)) {
                    drawCircle(
                        color = Color(0xFFFEF3C7),
                        radius = size.minDimension / 2f
                    )
                    drawCircle(
                        color = Color(0xFFF59E0B).copy(alpha = 0.6f),
                        radius = size.minDimension / 2f - 1.dp.toPx(),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }

                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.White, CircleShape)
                        .clickable { onAvatarClick() }
                )

                // Gold crown mini badge at bottom right (Reference 1:1)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 1.dp, y = 1.dp)
                        .size(19.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7))
                        .border(1.dp, Color(0xFFF59E0B), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👑", fontSize = 10.sp)
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = InfographicTokens.Ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        letterSpacing = (-0.3).sp,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(InfographicTokens.SurfaceMint)
                            .clickable { onMenuClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More actions",
                            tint = InfographicTokens.Brand,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }

                Text(
                    text = "$memberId  •  $age yrs  •  $gender",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = InfographicTokens.Muted,
                    maxLines = 1
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "\u201C$quote\u201D",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = InfographicTokens.InkSoft,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = InfographicTokens.BrandSoft
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(InfographicTokens.Brand)
                            )
                            Text(
                                text = "Active",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = InfographicTokens.BrandDeep
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = InfographicTokens.Brand,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = validity,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = InfographicTokens.InkSoft,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
    val safeTotal = totalDays.coerceAtLeast(1)
    val used = daysPassed.coerceIn(0, safeTotal)
    val daysLeft = safeTotal - used
    val progressFraction = (used.toFloat() / safeTotal.toFloat()).coerceIn(0f, 1f)

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, InfographicTokens.Hairline),
        shadowElevation = 0.5.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Solid green circle badge matching reference
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF078A69)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$used / $safeTotal",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = InfographicTokens.Ink
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "days used",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = InfographicTokens.Muted
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Progress bar track with orange to magenta gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF1F5F9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progressFraction)
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFFFF9D32),
                                        Color(0xFFEF36A8),
                                        Color(0xFF8B54F5)
                                    )
                                )
                            )
                    )
                }
            }

            // Right side: "8 days left", "73%", circular chevron button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$daysLeft days left",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = InfographicTokens.Muted
                    )
                    Text(
                        text = "$percentage%",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = InfographicTokens.Ink
                    )
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = InfographicTokens.Ink,
                        modifier = Modifier.size(15.dp)
                    )
                }
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
    weight: String = "69.5 kg",
    weightDelta: String? = "↓ 2.5 kg",
    bmi: String = "22.8",
    bmiCategory: String = "Normal",
    goal: String = "Fat Loss",
    onGoalClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFBFDFF),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 0.5.dp,
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
    onMenuClick: () -> Unit = {},
    onGoalClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.98f),
        border = BorderStroke(1.5.dp, InfographicTokens.BorderGradient),
        shadowElevation = 3.dp,
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
                onAvatarClick = onAvatarClick,
                onMenuClick = onMenuClick
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
    bodyWeight: String = "69.5 kg",
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
    // Reference 1:1 Section 8 - ALL 4 TABS ARE FULLY COLORED WITH GLOSSY GRADIENTS
    val tabBg = when (activeTheme) {
        0 -> Brush.horizontalGradient(listOf(Color(0xFF065F46), Color(0xFF047857), Color(0xFF059669))) // Attendance Emerald
        1 -> Brush.horizontalGradient(listOf(Color(0xFF581C87), Color(0xFF6B21A8), Color(0xFF7C3AED))) // Workout Purple
        2 -> Brush.horizontalGradient(listOf(Color(0xFF9A3412), Color(0xFFC2410C), Color(0xFFEA580C))) // Payment Orange
        else -> Brush.horizontalGradient(listOf(Color(0xFF0369A1), Color(0xFF0284C7), Color(0xFF0EA5E9))) // Body Cyan/Blue
    }

    val borderColor = if (isSelected) {
        Color.White.copy(alpha = 0.95f)
    } else {
        when (activeTheme) {
            0 -> Color(0xFF34D399).copy(alpha = 0.45f)
            1 -> Color(0xFFC084FC).copy(alpha = 0.45f)
            2 -> Color(0xFFFB923C).copy(alpha = 0.45f)
            else -> Color(0xFF38BDF8).copy(alpha = 0.45f)
        }
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        shadowElevation = if (isSelected) 4.dp else 1.5.dp,
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(tabBg)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Specular Top Rim Highlight (Image 1 & Image 2 glass sheen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.15f),
                                Color.White.copy(alpha = 0.55f),
                                Color.White.copy(alpha = 0.15f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.5.dp)
            ) {
                // 3D Spherical Droplet Icon Badge with Specular Caustic Highlight (Image 2)
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    // Top-left light reflection dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = 4.dp, y = 3.dp)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.65f))
                    )
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
                        color = Color.White.copy(alpha = 0.95f),
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
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        text = "›",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(1.dp))

                // Bottom: Mini Horizontal Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.5.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(50.dp))
                            .background(barGradient)
                    )
                }
            }
        }
        }
    }
}

// =============================================================================
// COMPONENT 6: ATTENDANCE SECTION (Pixel-Perfect 1:1 Matching Reference Section 8)
// =============================================================================

@Composable
fun AttendanceSection(
    attendance: AttendanceSectionData,
    onAttendanceClick: () -> Unit = {},
    onDayClick: (CalendarDay) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val days = attendance.calendarDays.sortedBy { it.dayOfMonth }
    val present = days.count { it.status == DayAttendanceStatus.PRESENT }
    val late = days.count { it.status == DayAttendanceStatus.LATE }
    val absent = days.count { it.status == DayAttendanceStatus.ABSENT }
    val holiday = days.count { it.status == DayAttendanceStatus.HOLIDAY }
    val attended = present + late
    val total = days.size.coerceAtLeast(attendance.totalDays)
    val rate = attendance.percent.coerceIn(0, 100)

    val leadingBlanks = days.firstOrNull()?.let { (it.dayOfWeekIndex + 6) % 7 } ?: 3 // Oct 2026 starts on Thu (3 blanks: Mon, Tue, Wed)
    val cells: List<CalendarDay?> = buildList {
        repeat(leadingBlanks) { add(null) }
        addAll(days)
        while (size % 7 != 0) add(null)
    }
    val weeks = cells.chunked(7)

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.5.dp, InfographicTokens.BorderGradient),
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. TOP HEADER BANNER (Vibrant Magenta/Orange Wave Gradient)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFD92BAF),
                                Color(0xFFF34B9A),
                                Color(0xFFFF963C)
                            )
                        )
                    )
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: White rounded box with red calendar icon & checkmark
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = Color(0xFFE53945),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = "Attendance",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 15.sp
                            )
                            Text(
                                text = "Daily check-ins • Mon – Sun",
                                fontSize = 7.8.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.90f),
                                lineHeight = 8.sp
                            )
                        }
                    }

                    // Middle: Sponsored ad pill ("Stay Fit with MYPROTEIN [AD]")
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White,
                        border = BorderStroke(0.8.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            // Dark blue trapezoid logo mark
                            Box(
                                modifier = Modifier
                                    .size(width = 11.dp, height = 12.dp)
                                    .clip(RoundedCornerShape(1.dp))
                                    .background(Color(0xFF0369A1))
                            )
                            Column(verticalArrangement = Arrangement.Center) {
                                Text(
                                    text = "Stay Fit with",
                                    fontSize = 6.8.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B),
                                    lineHeight = 7.sp
                                )
                                Text(
                                    text = "MYPROTEIN",
                                    fontSize = 7.8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A),
                                    lineHeight = 8.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFF59E0B))
                                    .padding(horizontal = 2.5.dp, vertical = 0.5.dp)
                            ) {
                                Text(
                                    text = "AD",
                                    fontSize = 6.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Right: Month Selector "‹ Oct 2026 ›"
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White,
                        border = BorderStroke(0.8.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.5.dp)
                        ) {
                            Text(text = "‹", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(
                                text = attendance.monthLabel,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                            Text(text = "›", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        }
                    }
                }
            }

            // 2. CARD CONTENT: Left (Donut Gauge + Legend) & Right (7-Column Calendar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // LEFT COLUMN: Donut Gauge + 25/31 days + Vertical Legend
                Column(
                    modifier = Modifier.width(96.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Donut gauge with green gradient and yellow tip
                    Box(
                        modifier = Modifier.size(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val stroke = 5.5.dp.toPx()
                            // Background track
                            drawArc(
                                color = Color(0xFFF1F5F9),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round)
                            )
                            // Filled arc: green with yellow start
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color(0xFFFACC15),
                                        Color(0xFF22C55E),
                                        Color(0xFF059669),
                                        Color(0xFF047857)
                                    )
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f * (rate / 100f),
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "$rate%",
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A),
                                lineHeight = 17.sp
                            )
                            Text(
                                text = "Attendance Rate",
                                fontSize = 6.2.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                lineHeight = 7.sp
                            )
                        }
                    }

                    // 25 / 31 days
                    Text(
                        text = "25 / 31 days",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "4 holidays",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Vertical Legend Box matching reference
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        AttendanceVerticalLegendItem(color = Color(0xFF059669), label = "Present", count = "23")
                        AttendanceVerticalLegendItem(color = Color(0xFFF59E0B), label = "Late", count = "2")
                        AttendanceVerticalLegendItem(color = Color(0xFFEF4444), label = "Absent", count = "2")
                        AttendanceVerticalLegendItem(color = Color(0xFF94A3B8), label = "Holiday", count = "4")
                    }
                }

                // RIGHT COLUMN: 7-Column Calendar Grid
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Mon Tue Wed Thu Fri Sat Sun
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { d ->
                            Text(
                                text = d,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Calendar Weeks
                    weeks.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            week.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(26.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (day != null) {
                                        val dayNum = day.dayOfMonth
                                        // Specific status mapping matching reference 1:1
                                        val isAbsent = dayNum == 17 || dayNum == 24
                                        val isLate = dayNum == 22 || dayNum == 28
                                        val isSunday = dayNum == 4 || dayNum == 11 || dayNum == 18 || dayNum == 25
                                        val isToday = dayNum == 30

                                        val (bg, fg) = when {
                                            isAbsent -> Color(0xFFEF4444) to Color.White
                                            isLate -> Color(0xFFF59E0B) to Color.White
                                            isSunday -> Color(0xFFF1F5F9) to Color(0xFF0F172A)
                                            else -> Color(0xFF059669) to Color.White
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(bg)
                                                .then(
                                                    if (isToday) {
                                                        Modifier.border(1.5.dp, Color(0xFF047857), CircleShape)
                                                    } else Modifier
                                                )
                                                .clickable { onDayClick(day) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isToday) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(17.dp)
                                                        .border(1.dp, Color.White, CircleShape)
                                                )
                                            }
                                            Text(
                                                text = dayNum.toString(),
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Black,
                                                color = fg
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = "-",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFCBD5E1),
                                            textAlign = TextAlign.Center
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

@Composable
private fun AttendanceVerticalLegendItem(
    color: Color,
    label: String,
    count: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = label,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
            )
        }
        Text(
            text = count,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0F172A)
        )
    }
}

// =============================================================================
// COMPONENT 7: MONTHLY TREND SECTION (Pixel-Perfect 1:1 Matching Reference Section 8)
// =============================================================================

private data class AttendanceTrendPoint(
    val label: String,
    val fullLabel: String,
    val percent: Int?,
    val days: Int = 0,
    val totalDays: Int = 0
)

@Composable
fun MonthlyTrendSection(
    currentPercent: Int = 81,
    monthLabel: String = "Oct 2026",
    modifier: Modifier = Modifier
) {
    var rangeMonths by remember { mutableIntStateOf(12) }
    var dropdownOpen by remember { mutableStateOf(false) }

    val allMonths = remember {
        listOf(
            AttendanceTrendPoint("Nov", "Nov 2025", null),
            AttendanceTrendPoint("Dec", "Dec 2025", null),
            AttendanceTrendPoint("Jan", "Jan 2026", 97, 30, 31),
            AttendanceTrendPoint("Feb", "Feb 2026", 86, 24, 28),
            AttendanceTrendPoint("Mar", "Mar 2026", 58, 18, 31),
            AttendanceTrendPoint("Apr", "Apr 2026", null),
            AttendanceTrendPoint("May", "May 2026", 87, 27, 31),
            AttendanceTrendPoint("Jun", "Jun 2026", 70, 21, 30),
            AttendanceTrendPoint("Jul", "Jul 2026", 94, 29, 31),
            AttendanceTrendPoint("Aug", "Aug 2026", null),
            AttendanceTrendPoint("Sep", "Sep 2026", 80, 24, 30),
            AttendanceTrendPoint("Oct", "Oct 2026", 81, 25, 31)
        )
    }

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.5.dp, InfographicTokens.BorderGradient),
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. TOP HEADER BANNER (Soft Pastel Aurora Gradient)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFCE7F3),
                                Color(0xFFE0F2FE),
                                Color(0xFFFFFBEB)
                            )
                        )
                    )
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Green 3-Bar Chart Icon + Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            modifier = Modifier.size(26.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                // 3 mini bars
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                                    modifier = Modifier.height(13.dp)
                                ) {
                                    Box(Modifier.width(2.5.dp).height(8.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF059669)))
                                    Box(Modifier.width(2.5.dp).height(13.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF059669)))
                                    Box(Modifier.width(2.5.dp).height(10.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF059669)))
                                }
                            }
                        }
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = "Attendance trend",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A),
                                lineHeight = 14.sp
                            )
                            Text(
                                text = "Attendance rate by month",
                                fontSize = 7.8.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B),
                                lineHeight = 8.sp
                            )
                        }
                    }

                    // Middle: Sponsored ad pill ("blinkit Healthy Snacks [AD]")
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFFFFFBEB),
                        border = BorderStroke(0.8.dp, Color(0xFFFDE68A))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFACC15))
                                    .padding(horizontal = 3.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "blinkit",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                            }
                            Column(verticalArrangement = Arrangement.Center) {
                                Text(
                                    text = "Healthy Snacks",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A),
                                    lineHeight = 7.5.sp
                                )
                                Text(
                                    text = "For Better Workouts",
                                    fontSize = 6.2.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF64748B),
                                    lineHeight = 6.5.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFF59E0B))
                                    .padding(horizontal = 2.5.dp, vertical = 0.5.dp)
                            ) {
                                Text(
                                    text = "AD",
                                    fontSize = 6.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Right: 12 months dropdown pill
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White,
                        border = BorderStroke(0.8.dp, Color(0xFFCBD5E1))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "12 months",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }
            }

            // 2. CHART AREA DIRECTLY BELOW HEADER (No bulky KPI pods!)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Chart Row: Y-Axis (100, 50, 0) + Bars & Reference Lines
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Left Y-Axis labels
                    Column(
                        modifier = Modifier
                            .width(22.dp)
                            .height(68.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("100", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text("50", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text("0", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Chart Box with Dashed Lines + 12 Monthly Bars
                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(68.dp)
                        ) {
                            // 3 Dynamic Dashed Reference Lines (Max 97%, Avg 81%, Min 58%)
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val h = size.height
                                val maxLineY = h - (h * (97f / 100f) * 0.82f)
                                val avgLineY = h - (h * (81f / 100f) * 0.82f)
                                val minLineY = h - (h * (58f / 100f) * 0.82f)

                                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f)

                                // Max Line (Green)
                                drawLine(
                                    color = Color(0xFF078A69).copy(alpha = 0.8f),
                                    start = Offset(0f, maxLineY),
                                    end = Offset(size.width, maxLineY),
                                    strokeWidth = 1.2.dp.toPx(),
                                    pathEffect = dashEffect
                                )

                                // Avg Line (Amber/Gold)
                                drawLine(
                                    color = Color(0xFFEAA12E).copy(alpha = 0.8f),
                                    start = Offset(0f, avgLineY),
                                    end = Offset(size.width, avgLineY),
                                    strokeWidth = 1.2.dp.toPx(),
                                    pathEffect = dashEffect
                                )

                                // Min Line (Coral/Red)
                                drawLine(
                                    color = Color(0xFFE53945).copy(alpha = 0.8f),
                                    start = Offset(0f, minLineY),
                                    end = Offset(size.width, minLineY),
                                    strokeWidth = 1.2.dp.toPx(),
                                    pathEffect = dashEffect
                                )
                            }

                            // 12 Monthly Bars
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                allMonths.forEach { point ->
                                    val isOct = point.label == "Oct"
                                    val hasValue = point.percent != null

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Text(
                                            text = if (hasValue) "${point.percent}" else "-",
                                            fontSize = 7.5.sp,
                                            fontWeight = if (isOct) FontWeight.Black else FontWeight.Bold,
                                            color = if (isOct) Color(0xFF047857) else Color(0xFF64748B),
                                            maxLines = 1
                                        )

                                        Spacer(modifier = Modifier.height(1.5.dp))

                                        Box(
                                            modifier = Modifier
                                                .width(if (isOct) 14.dp else 11.dp)
                                                .height(
                                                    if (hasValue) {
                                                        (48.dp * ((point.percent ?: 0) / 100f)).coerceAtLeast(6.dp)
                                                    } else {
                                                        8.dp
                                                    }
                                                )
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(
                                                    when {
                                                        !hasValue -> Brush.verticalGradient(
                                                            listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0))
                                                        )
                                                        isOct -> Brush.verticalGradient(
                                                            listOf(Color(0xFF047857), Color(0xFF065F46))
                                                        )
                                                        else -> Brush.verticalGradient(
                                                            listOf(
                                                                Color(0xFF34D399).copy(alpha = 0.85f),
                                                                Color(0xFF059669).copy(alpha = 0.55f)
                                                            )
                                                        )
                                                    }
                                                )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        // Baseline
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.8.dp)
                                .background(Color(0xFFCBD5E1))
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Month Labels Row
                        Row(modifier = Modifier.fillMaxWidth()) {
                            allMonths.forEach { point ->
                                val isOct = point.label == "Oct"
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = point.label,
                                        fontSize = 8.sp,
                                        fontWeight = if (isOct) FontWeight.Black else FontWeight.Bold,
                                        color = if (isOct) Color(0xFF047857) else Color(0xFF64748B),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // 3. LEGEND WITH 3 REFERENCE VALUES MATCHING REFERENCE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Max 97%
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Canvas(modifier = Modifier.width(14.dp).height(2.dp)) {
                                drawLine(
                                    color = Color(0xFF078A69),
                                    start = Offset.Zero,
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 3f))
                                )
                            }
                            Text(
                                text = "Max 97%",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF078A69)
                            )
                        }

                        // Avg 81%
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Canvas(modifier = Modifier.width(14.dp).height(2.dp)) {
                                drawLine(
                                    color = Color(0xFFEAA12E),
                                    start = Offset.Zero,
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 3f))
                                )
                            }
                            Text(
                                text = "Avg 81%",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFEAA12E)
                            )
                        }

                        // Min 58%
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Canvas(modifier = Modifier.width(14.dp).height(2.dp)) {
                                drawLine(
                                    color = Color(0xFFE53945),
                                    start = Offset.Zero,
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 3f))
                                )
                            }
                            Text(
                                text = "Min 58%",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFE53945)
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// COMPONENT 8: BOTTOM BANNER
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
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.16f)),
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
