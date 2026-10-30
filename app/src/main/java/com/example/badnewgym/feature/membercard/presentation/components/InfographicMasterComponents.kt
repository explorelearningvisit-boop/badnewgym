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
            // RIGHT: White Stadium Capsule (₹2,800 Payment Due / 03 Nov 2026)
            // =================================================================
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
                modifier = Modifier.size(62.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(62.dp)) {
                    drawCircle(
                        color = InfographicTokens.BrandSoft,
                        radius = size.minDimension / 2f
                    )
                    drawCircle(
                        color = InfographicTokens.Brand.copy(alpha = 0.35f),
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
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Rounded green calendar icon
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(Color(0xFFE6F5F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF078A69),
                    modifier = Modifier.size(14.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // Top row: "22 / 30 days used" + "8 days left"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$used / $safeTotal",
                        fontSize = 12.sp,
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
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "$daysLeft days left",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = InfographicTokens.Muted
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Bottom row: Ultra-thin 5dp progress bar (#FF9D32 -> #EF36A8)
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
                            .background(InfographicTokens.ThinProgressGradient)
                    )
                }
            }

            // Right side: "73%" + chevron ›
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "$percentage%",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.Ink
                )
                Text(
                    text = "›",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.Muted
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
    onMenuClick: () -> Unit = {},
    onGoalClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.98f),
        border = BorderStroke(1.5.dp, InfographicTokens.BorderGradient),
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

    val monthDate = remember(attendance.monthLabel) {
        runCatching { SimpleDateFormat("MMM yyyy", Locale.US).parse(attendance.monthLabel) }.getOrNull()
    }
    val today = Calendar.getInstance()
    val isCurrentMonth = monthDate != null &&
        Calendar.getInstance().apply { time = monthDate }.let {
            it.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                it.get(Calendar.MONTH) == today.get(Calendar.MONTH)
        }
    val todayDay = if (isCurrentMonth) today.get(Calendar.DAY_OF_MONTH) else -1

    val leadingBlanks = days.firstOrNull()?.let { (it.dayOfWeekIndex + 6) % 7 } ?: 0
    val cells: List<CalendarDay?> = buildList {
        repeat(leadingBlanks) { add(null) }
        addAll(days)
        while (size % 7 != 0) add(null)
    }
    val weeks = cells.chunked(7)

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = InfographicTokens.Surface,
        border = BorderStroke(1.dp, InfographicTokens.Hairline),
        shadowElevation = 0.5.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(InfographicTokens.BrandSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = InfographicTokens.Brand,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Attendance",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = InfographicTokens.Ink
                    )
                    Text(
                        text = "Daily check-ins · Mon – Sun",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = InfographicTokens.Muted
                    )
                }

                // Sponsored Slot: "Stay Fit with MYPROTEIN [AD]"
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(0.8.dp, Color(0xFFBBF7D0))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Stay Fit with",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = InfographicTokens.Muted
                        )
                        Text(
                            text = "MYPROTEIN",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0369A1)
                        )
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

                // Month Selector with Chevrons: < Oct 2026 >
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, InfographicTokens.Hairline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "‹", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = InfographicTokens.Muted)
                        Text(
                            text = attendance.monthLabel,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = InfographicTokens.Ink,
                            maxLines = 1
                        )
                        Text(text = "›", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = InfographicTokens.Muted)
                    }
                }
            }

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                AttendanceLegendChip("Present", present, InfographicTokens.Brand, InfographicTokens.BrandSoft, Modifier.weight(1f))
                AttendanceLegendChip("Late", late, InfographicTokens.Gold, InfographicTokens.GoldSoft, Modifier.weight(1f))
                AttendanceLegendChip("Absent", absent, InfographicTokens.Danger, InfographicTokens.DangerSoft, Modifier.weight(1f))
                AttendanceLegendChip("Holiday", holiday, InfographicTokens.Muted, Color(0xFFF1F5F2), Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Summary gauge
                Column(
                    modifier = Modifier
                        .width(88.dp)
                        .clickable { onAttendanceClick() },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier.size(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val stroke = 6.dp.toPx()
                            drawArc(
                                color = InfographicTokens.BrandSoft,
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round)
                            )
                            drawArc(
                                color = InfographicTokens.Brand,
                                startAngle = -90f,
                                sweepAngle = 360f * (rate / 100f),
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$rate%",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = InfographicTokens.BrandDeep,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "rate",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = InfographicTokens.Muted
                            )
                        }
                    }
                    Text(
                        text = "$attended / $total days",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = InfographicTokens.InkSoft,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "$holiday holidays",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        color = InfographicTokens.Muted,
                        textAlign = TextAlign.Center
                    )
                }

                // Calendar
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                            Text(
                                text = day,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Medium,
                                color = InfographicTokens.Muted,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    weeks.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            week.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(27.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (day != null) {
                                        val (bg, fg) = when (day.status) {
                                            DayAttendanceStatus.PRESENT -> InfographicTokens.Brand to Color.White
                                            DayAttendanceStatus.LATE -> InfographicTokens.Gold to Color.White
                                            DayAttendanceStatus.ABSENT -> InfographicTokens.Danger to Color.White
                                            DayAttendanceStatus.HOLIDAY -> Color(0xFFEDF3EF) to InfographicTokens.InkSoft
                                        }
                                        val isToday = day.dayOfMonth == todayDay
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(bg)
                                                .clickable { onDayClick(day) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isToday) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(17.dp)
                                                        .border(1.4.dp, Color.White.copy(alpha = 0.92f), CircleShape)
                                                )
                                            }
                                            Text(
                                                text = day.dayOfMonth.toString(),
                                                fontSize = 10.sp,
                                                fontWeight = if (isToday) FontWeight.Black else FontWeight.SemiBold,
                                                color = fg
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tap a date for that day's record",
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = InfographicTokens.Muted,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .clickable { onAttendanceClick() }
                        .padding(horizontal = 4.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Details",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = InfographicTokens.Brand
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = InfographicTokens.Brand,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AttendanceLegendChip(
    label: String,
    count: Int,
    dotColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(9.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Text(
                text = label,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Medium,
                color = InfographicTokens.InkSoft,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = count.toString(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = InfographicTokens.Ink,
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
    val current = currentPercent.coerceIn(0, 100)

    val allMonths = remember(current, monthLabel) {
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
            AttendanceTrendPoint("Oct", monthLabel, current, 25, 31)
        )
    }
    val visible = remember(rangeMonths, allMonths) {
        if (rangeMonths == 6) allMonths.takeLast(6) else allMonths
    }
    val activeMonths = visible.filter { it.percent != null }
    val best = activeMonths.maxByOrNull { it.percent ?: 0 } ?: visible.last()
    val lowest = activeMonths.minByOrNull { it.percent ?: 100 } ?: visible.last()
    val average = if (activeMonths.isEmpty()) 0 else activeMonths.sumOf { it.percent ?: 0 } / activeMonths.size

    var selectedIndex by remember(rangeMonths) { mutableIntStateOf(visible.lastIndex) }
    val selected = visible.getOrElse(selectedIndex) { visible.last() }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = InfographicTokens.Surface,
        border = BorderStroke(1.dp, InfographicTokens.Hairline),
        shadowElevation = 0.5.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(InfographicTokens.BrandSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = InfographicTokens.Brand,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Attendance trend",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = InfographicTokens.Ink
                    )
                    Text(
                        text = "Attendance rate by month",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = InfographicTokens.Muted
                    )
                }

                // Sponsored Slot: "blinkit Healthy Snacks [AD]"
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFFFFBEB),
                    border = BorderStroke(0.8.dp, Color(0xFFFDE68A))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "blinkit",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFF59E0B)
                        )
                        Text(
                            text = "Healthy Snacks",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = InfographicTokens.Ink
                        )
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

                Box {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = InfographicTokens.SurfaceMint,
                        border = BorderStroke(1.dp, InfographicTokens.Hairline),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .clickable { dropdownOpen = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "$rangeMonths months",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = InfographicTokens.InkSoft,
                                maxLines = 1
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select time range",
                                tint = InfographicTokens.Muted,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = dropdownOpen,
                        onDismissRequest = { dropdownOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("6 months", fontSize = 12.sp) },
                            onClick = {
                                rangeMonths = 6
                                dropdownOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("12 months", fontSize = 12.sp) },
                            onClick = {
                                rangeMonths = 12
                                dropdownOpen = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                TrendKpiCard(
                    icon = Icons.Default.EmojiEvents,
                    label = "Best month",
                    value = "${best.percent ?: 0}%",
                    sub = best.label,
                    accent = InfographicTokens.Brand,
                    background = InfographicTokens.BrandSoft,
                    modifier = Modifier.weight(1f)
                )
                TrendKpiCard(
                    icon = Icons.Default.TrendingUp,
                    label = "Average",
                    value = "$average%",
                    sub = "${activeMonths.size} active months",
                    accent = InfographicTokens.Gold,
                    background = InfographicTokens.GoldSoft,
                    modifier = Modifier.weight(1f)
                )
                TrendKpiCard(
                    icon = Icons.Default.TrendingDown,
                    label = "Lowest",
                    value = "${lowest.percent ?: 0}%",
                    sub = lowest.label,
                    accent = InfographicTokens.Danger,
                    background = InfographicTokens.DangerSoft,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .width(24.dp)
                        .height(64.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    Text("100", fontSize = 7.sp, fontWeight = FontWeight.Medium, color = InfographicTokens.Muted)
                    Text("50", fontSize = 7.sp, fontWeight = FontWeight.Medium, color = InfographicTokens.Muted)
                    Text("0", fontSize = 7.sp, fontWeight = FontWeight.Medium, color = InfographicTokens.Muted)
                }

                Column(modifier = Modifier.weight(1f)) {
                    // Dynamic 3 Dashed Reference Lines (Max, Avg, Min) + 12-Month Bars
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                    ) {
                        // Canvas overlay for 3 dynamically calculated dashed lines
                        val maxPct = (best.percent ?: 97).toFloat()
                        val minPct = (lowest.percent ?: 58).toFloat()
                        val avgPct = average.toFloat()

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val h = size.height
                            val maxLineY = h - (h * (maxPct / 100f) * 0.85f)
                            val avgLineY = h - (h * (avgPct / 100f) * 0.85f)
                            val minLineY = h - (h * (minPct / 100f) * 0.85f)

                            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f)

                            // Max Line (Green)
                            drawLine(
                                color = Color(0xFF078A69).copy(alpha = 0.7f),
                                start = Offset(0f, maxLineY),
                                end = Offset(size.width, maxLineY),
                                strokeWidth = 1.2.dp.toPx(),
                                pathEffect = dashEffect
                            )

                            // Avg Line (Amber/Gold)
                            drawLine(
                                color = Color(0xFFEAA12E).copy(alpha = 0.7f),
                                start = Offset(0f, avgLineY),
                                end = Offset(size.width, avgLineY),
                                strokeWidth = 1.2.dp.toPx(),
                                pathEffect = dashEffect
                            )

                            // Min Line (Coral/Red)
                            drawLine(
                                color = Color(0xFFE53945).copy(alpha = 0.7f),
                                start = Offset(0f, minLineY),
                                end = Offset(size.width, minLineY),
                                strokeWidth = 1.2.dp.toPx(),
                                pathEffect = dashEffect
                            )
                        }

                        // Bars Row
                        Row(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                        visible.forEachIndexed { index, point ->
                            val isSelected = index == selectedIndex
                            val hasValue = point.percent != null
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedIndex = index },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    text = if (hasValue) "${point.percent}" else "—",
                                    fontSize = 7.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) InfographicTokens.BrandDeep else InfographicTokens.Muted,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .width(if (isSelected) 14.dp else 11.dp)
                                        .height(
                                            if (hasValue) {
                                                (42.dp * ((point.percent ?: 0) / 100f)).coerceAtLeast(4.dp)
                                            } else {
                                                8.dp
                                            }
                                        )
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(
                                            when {
                                                !hasValue -> Brush.verticalGradient(
                                                    listOf(Color(0xFFEDF3EF), Color(0xFFEDF3EF))
                                                )
                                                isSelected -> Brush.verticalGradient(
                                                    listOf(InfographicTokens.Brand, InfographicTokens.BrandDeep)
                                                )
                                                else -> Brush.verticalGradient(
                                                    listOf(
                                                        InfographicTokens.Brand.copy(alpha = 0.38f),
                                                        InfographicTokens.Brand.copy(alpha = 0.22f)
                                                    )
                                                )
                                            }
                                        )
                                )
                            }
                        }
                    }

                    } // End Box with Dashed Lines

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        visible.forEachIndexed { index, point ->
                            val isSelected = index == selectedIndex
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(
                                        if (isSelected) InfographicTokens.BrandSoft else Color.Transparent
                                    )
                                    .padding(vertical = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = point.label,
                                    fontSize = 8.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) InfographicTokens.BrandDeep else InfographicTokens.Muted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selected.fullLabel,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = InfographicTokens.Ink
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (selected.percent != null) {
                        "${selected.percent}% · ${selected.days} of ${selected.totalDays} days"
                    } else {
                        "No membership"
                    },
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = InfographicTokens.Muted
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Avg $average%",
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = InfographicTokens.Gold
                )
            }

            // Compact Legend with 3 Dynamic Reference Values
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Max
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
                            text = "Max ${best.percent ?: 97}%",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF078A69)
                        )
                    }

                    // Avg
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
                            text = "Avg $average%",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEAA12E)
                        )
                    }

                    // Min
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
                            text = "Min ${lowest.percent ?: 58}%",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53945)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrendKpiCard(
    icon: ImageVector,
    label: String,
    value: String,
    sub: String,
    accent: Color,
    background: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = background,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(11.dp)
                )
            }
            Column {
                Text(
                    text = label,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = InfographicTokens.Muted,
                    maxLines = 1
                )
                Text(
                    text = value,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = InfographicTokens.Ink,
                    maxLines = 1
                )
                Text(
                    text = sub,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Medium,
                    color = accent,
                    maxLines = 1
                )
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
