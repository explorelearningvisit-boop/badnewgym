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
    // ---- BAD GYM Master Luxury Porcelain Canvas (Image 1, 2, 4 & 5) ----
    val MainBg = Color(0xFFFFFFFF)
    val PageTop = Color(0xFF1E0A38)
    val PageMid = Color(0xFFF4F7FB)
    val PageBottom = Color(0xFFFFFFFF)
    val SurfaceMint = Color(0xFFF0FDF4)
    val Surface = Color(0xFFFFFFFF)         // Pure Porcelain White Card Surface
    val SurfaceSoft = Color(0xFFF8FAFC)     // Frosted Arctic Slate Tint
    val Ink = Color(0xFF0F172A)             // Deep Obsidian Slate Black Text (High Contrast)
    val InkSoft = Color(0xFF334155)         // Slate Navy
    val Muted = Color(0xFF64748B)           // Cool Grey Secondary Text
    val Hairline = Color(0xFFE2E8F0)        // Subtle Crisp Border

    // Multi-stop Ambient Canvas Brush:
    // Cosmic Galaxy Dusk Top -> Pearlescent Sky Mist -> Crisp Arctic Porcelain White
    val CanvasBrush = Brush.verticalGradient(
        0.00f to Color(0xFF1E0A38), // Cosmic Galaxy Purple Top
        0.10f to Color(0xFF3B0764), // Deep Royal Velvet
        0.22f to Color(0xFF5A0E72), // Cosmic Magenta
        0.38f to Color(0xFFF1F5F9), // Ice Pearl Mist
        0.60f to Color(0xFFF8FAFC), // Pure Arctic White
        1.00f to Color(0xFFFFFFFF)  // Clean Canvas
    )

    // Multi-color Accents
    val Purple = Color(0xFF7C3AED)
    val Magenta = Color(0xFFD92BAF)
    val Pink = Color(0xFFF43F5E)
    val Orange = Color(0xFFFF963C)
    val Yellow = Color(0xFFFFD45C)
    val Cyan = Color(0xFF38BDF8)
    val Blue = Color(0xFF0284C7) // Bond Scanner Electric Royal Cobalt Blue
    val Emerald = Color(0xFF059669)
    val Amber = Color(0xFFF59E0B)
    val RedAlert = Color(0xFFEF4444)
    val HolidayGray = Color(0xFFF1F5F9)

    // Semantic colors
    val Brand = Emerald
    val BrandDeep = Color(0xFF047857)
    val BrandSoft = Color(0xFFDCFCE7)
    val Gold = Color(0xFFFFD700)
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
            Color(0xFF021C13), // Deep Forest Night
            Color(0xFF042F22), // Pine
            Color(0xFF065F46), // Emerald
            Color(0xFF047857), // Deep Green
            Color(0xFF0E7A4E)  // Fresh Leaf
        )
    )

    val BorderGradient = Brush.linearGradient(
        listOf(
            Color(0xFFFFD700), // 24K Radiant Gold
            Color(0xFFFF2A85), // Rose Neon
            Color(0xFF38BDF8), // Electric Cyan
            Color(0xFF00F59B)  // Laser Emerald
        )
    )

    val ThinProgressGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFFFF7A00), // Warm Orange
            Color(0xFFFF2A85), // Vibrant Magenta
            Color(0xFF8B5CF6)  // Electric Violet
        )
    )

    val AuroraWaveGradient = HeaderGradient
    val TrendWaveGradient = HeaderGradient
    val DaysCardGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF065F46),
            Color(0xFF047857),
            Color(0xFF10B981)
        )
    )
    val AttendanceGradient = Brush.horizontalGradient(listOf(Color(0xFF047857), Color(0xFF059669), Color(0xFF10B981)))
    val WorkoutGradient = Brush.horizontalGradient(listOf(Color(0xFF6B21A8), Color(0xFF7C3AED), Color(0xFF9333EA)))
    val PaymentGradient = Brush.horizontalGradient(listOf(Color(0xFFC2410C), Color(0xFFEA580C), Color(0xFFF97316)))
    val BodyGradient = Brush.horizontalGradient(listOf(Color(0xFF1D4ED8), Color(0xFF0284C7), Color(0xFF38BDF8)))
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
            .height(54.dp)
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
                Text(text = "👑", fontSize = 18.sp)
                Column(verticalArrangement = Arrangement.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BAD ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 15.sp
                        )
                        Text(
                            text = "GYM",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFB9F227), // Electric Lime
                            lineHeight = 15.sp
                        )
                    }
                    Text(
                        text = "MEMBER INTELLIGENCE",
                        fontSize = 7.4.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.95f),
                        letterSpacing = 0.2.sp,
                        lineHeight = 8.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "DISCIPLINE BUILDS FREEDOM",
                        fontSize = 6.4.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.82f),
                        letterSpacing = 0.1.sp,
                        lineHeight = 7.2.sp,
                        maxLines = 1
                    )
                }
            }

            // =================================================================
            // MIDDLE: Translucent Glowing Stadium Capsule (CHECK-IN)
            // =================================================================
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFF0B7A4B),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .clickable { onCheckInClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "CHECK-IN",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "CHECK-IN",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.3.sp,
                            lineHeight = 11.sp,
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
                    // Green Leather Wallet with embossed white Rupee symbol + Caustic dot
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
                        // Caustic pinpoint reflection dot
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = 3.dp, y = 2.dp)
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.7f))
                        )
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
        shape = RoundedCornerShape(18.dp),
        color = InfographicTokens.Surface,
        border = BorderStroke(1.dp, InfographicTokens.Hairline),
        shadowElevation = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // A single clean avatar with the reference green halo and live dot.
            Box(
                modifier = Modifier.size(72.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(72.dp)) {
                    drawCircle(
                        color = Color(0xFFDCFCE7),
                        radius = size.minDimension / 2f
                    )
                    drawCircle(
                        color = Color(0xFF22C55E),
                        radius = size.minDimension / 2f - 1.dp.toPx(),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable { onAvatarClick() }
                )

                // Live green status dot at bottom right.
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 1.dp, y = 1.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E))
                        .border(2.dp, Color.White, CircleShape)
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
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Black,
                        color = InfographicTokens.Ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        letterSpacing = (-0.4).sp,
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
                    fontSize = 11.5.sp,
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
                        fontSize = 11.sp,
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
                        fontSize = 10.sp,
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
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.7f)),
        shadowElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(InfographicTokens.DaysCardGradient)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Translucent calendar badge on the deep green bar.
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
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
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "days used",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
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
                                    listOf(
                                        Color(0xFFB9F227),
                                        Color(0xFF86EFAC)
                                    )
                                )
                            )
                    )
                }
            }

            // Right side: days left, percentage, circular chevron button.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$daysLeft days left",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "$percentage%",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.White,
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
    weightDelta: String? = "↓ 2.5",
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
    weightDelta: String? = "↓ 2.5",
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
    paymentDue: String = "₹2,800",
    bodyWeight: String = "69.5 kg",
    attendanceProgress: Float = 0.90f,
    workoutProgress: Float = 0.70f,
    paymentProgress: Float = 0.65f,
    bodyProgress: Float = 0.74f,
    paymentHasAlert: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
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
            progress = attendanceProgress,
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
            progress = workoutProgress,
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
            progress = paymentProgress,
            isSelected = selectedTabIndex == 2,
            hasAlert = paymentHasAlert,
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
            progress = bodyProgress,
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
    // Reference 1:1 Section 8 + Bond Scanner + Liquid Glassmorphism Sheen
    val tabBg = when (activeTheme) {
        0 -> Brush.horizontalGradient(listOf(Color(0xFF047857), Color(0xFF059669), Color(0xFF10B981))) // Attendance Emerald
        1 -> Brush.horizontalGradient(listOf(Color(0xFF6B21A8), Color(0xFF7C3AED), Color(0xFF9333EA))) // Workout Purple
        2 -> Brush.horizontalGradient(listOf(Color(0xFFC2410C), Color(0xFFEA580C), Color(0xFFF97316))) // Payment Orange
        else -> Brush.horizontalGradient(listOf(Color(0xFF1D4ED8), Color(0xFF2563EB), Color(0xFF38BDF8))) // Body Cobalt (Bond Scanner)
    }

    val borderColor = if (isSelected) {
        Color.White.copy(alpha = 0.95f)
    } else {
        when (activeTheme) {
            0 -> Color(0xFF34D399).copy(alpha = 0.50f)
            1 -> Color(0xFFC084FC).copy(alpha = 0.50f)
            2 -> Color(0xFFFB923C).copy(alpha = 0.50f)
            else -> Color(0xFF38BDF8).copy(alpha = 0.50f)
        }
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent,
        border = BorderStroke(if (isSelected) 2.dp else 1.2.dp, borderColor),
        shadowElevation = if (isSelected) 5.dp else 2.5.dp,
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(18.dp))
            .background(tabBg)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Specular Top Rim Highlight (Image 1 & Bond Scanner bevel sheen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.20f),
                                Color.White.copy(alpha = 0.75f),
                                Color.White.copy(alpha = 0.20f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp, end = 5.dp, top = 2.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 3D Spherical Droplet Icon Badge with Specular Caustic Highlight (Image 2)
                Box(
                    modifier = Modifier
                        .size(28.dp)
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
                            .background(Color.White.copy(alpha = 0.70f))
                    )
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }

                // Details Column: Title (+ alert), Value + chevron
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
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White.copy(alpha = 0.95f),
                            maxLines = 1,
                            letterSpacing = (-0.2).sp
                        )
                        if (hasAlert) {
                            Box(
                                modifier = Modifier
                                    .size(11.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "!",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    lineHeight = 8.sp
                                )
                            }
                        }
                    }

                    // Bottom: Value + Chevron
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Text(
                            text = value,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = "›",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White.copy(alpha = 0.90f)
                        )
                    }
                }
            }

            // Slim data-driven progress bar at the foot of the tab.
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 3.dp)
                    .height(2.5.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White.copy(alpha = 0.22f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(50.dp))
                        .background(barGradient)
                )
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

    val now = Calendar.getInstance()
    val thisMonth = SimpleDateFormat("MMMM yyyy", Locale.US).format(now.time)
    val todayDay = if (thisMonth.equals(attendance.monthLabel, ignoreCase = true)) {
        now.get(Calendar.DAY_OF_MONTH)
    } else {
        -1
    }
    val monthShort = attendance.monthLabel
        .split(" ")
        .let { parts ->
            val month = parts.firstOrNull()?.take(3).orEmpty()
            val year = parts.getOrNull(1).orEmpty()
            listOf(month, year).filter { it.isNotBlank() }.joinToString(" ")
        }

    val leadingBlanks = days.firstOrNull()?.let { (it.dayOfWeekIndex + 6) % 7 } ?: 0
    val cells: List<CalendarDay?> = buildList {
        repeat(leadingBlanks) { add(null) }
        addAll(days)
        while (size % 7 != 0) add(null)
    }
    val weeks = cells.chunked(7)

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFFFFBF4),
        border = BorderStroke(1.2.dp, Color(0xFFFBCFE8)),
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. VIBRANT HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFF43F5E),
                                Color(0xFFFB7185),
                                Color(0xFFFF9A5A)
                            )
                        )
                    )
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(9.dp),
                        color = Color.White,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFFE11D48),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Attendance",
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 16.sp
                        )
                        Text(
                            text = "Daily check-ins \u2022 Mon \u2013 Sun",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.92f),
                            lineHeight = 9.sp
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFFE11D48),
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = monthShort,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // 2. HORIZONTAL LEGEND BAR
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFFFF7EC),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AttendanceLegendChip(
                        color = Color(0xFF047857),
                        label = "Present",
                        count = present,
                        modifier = Modifier.weight(1f)
                    )
                    AttendanceLegendChip(
                        color = Color(0xFFF59E0B),
                        label = "Late",
                        count = late,
                        modifier = Modifier.weight(1f)
                    )
                    AttendanceLegendChip(
                        color = Color(0xFFEF4444),
                        label = "Absent",
                        count = absent,
                        modifier = Modifier.weight(1f)
                    )
                    AttendanceLegendChip(
                        color = Color(0xFF94A3B8),
                        label = "Holiday",
                        count = holiday,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 3. DONUT + CALENDAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.width(108.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(92.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val stroke = 8.dp.toPx()
                            drawArc(
                                color = Color(0xFFF1F5F9),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round)
                            )
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$rate%",
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A),
                                lineHeight = 24.sp
                            )
                            Text(
                                text = "rate",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                lineHeight = 10.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "$attended / $total days",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    Text(
                        text = if (holiday == 1) "1 holiday" else "$holiday holidays",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { label ->
                            Text(
                                text = label,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
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
                                        .height(28.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (day != null) {
                                        val isToday = day.dayOfMonth == todayDay
                                        val (background, foreground) = when (day.status) {
                                            DayAttendanceStatus.PRESENT ->
                                                Color(0xFF047857) to Color.White
                                            DayAttendanceStatus.LATE ->
                                                Color(0xFFF59E0B) to Color.White
                                            DayAttendanceStatus.ABSENT ->
                                                Color(0xFFEF4444) to Color.White
                                            DayAttendanceStatus.HOLIDAY ->
                                                Color(0xFFF1F5F9) to Color(0xFF334155)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(26.dp)
                                                .clip(CircleShape)
                                                .background(background)
                                                .then(
                                                    if (isToday) {
                                                        Modifier.border(1.8.dp, Color.White, CircleShape)
                                                    } else {
                                                        Modifier
                                                    }
                                                )
                                                .clickable { onDayClick(day) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = day.dayOfMonth.toString(),
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Black,
                                                color = foreground,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. FOOTER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "\u261D Tap a date for that day\u2019s record",
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B),
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .clickable { onAttendanceClick() }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = "Details",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Attendance details",
                        tint = Color(0xFF334155),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AttendanceLegendChip(
    color: Color,
    label: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF475569),
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "$count",
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0F172A),
            maxLines = 1
        )
    }
}

// =============================================================================
// COMPONENT 7: ATTENDANCE TREND SECTION
// =============================================================================

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

    val allMonths = remember(currentPercent) {
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
            AttendanceTrendPoint("Oct", "Oct 2026", currentPercent, 25, 31)
        )
    }

    val visibleMonths = remember(rangeMonths, allMonths) {
        allMonths.takeLast(rangeMonths.coerceAtMost(allMonths.size))
    }
    val activeMonths = visibleMonths.filter { it.percent != null }
    val bestMonth = activeMonths.maxByOrNull { it.percent ?: 0 }
    val lowestMonth = activeMonths.minByOrNull { it.percent ?: 0 }
    val averagePercent = if (activeMonths.isEmpty()) {
        0
    } else {
        activeMonths.sumOf { it.percent ?: 0 } / activeMonths.size
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFFFFBF4),
        border = BorderStroke(1.2.dp, Color(0xFFFBCFE8)),
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0EA5E9),
                                Color(0xFF06B6D4),
                                Color(0xFF10B981)
                            )
                        )
                    )
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(9.dp),
                        color = Color.White,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                                modifier = Modifier.height(14.dp)
                            ) {
                                Box(
                                    Modifier
                                        .width(2.5.dp)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(Color(0xFF059669))
                                )
                                Box(
                                    Modifier
                                        .width(2.5.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(Color(0xFF059669))
                                )
                                Box(
                                    Modifier
                                        .width(2.5.dp)
                                        .height(11.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(Color(0xFF059669))
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Attendance trend",
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 16.sp
                        )
                        Text(
                            text = "Attendance rate by month",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.92f),
                            lineHeight = 9.sp
                        )
                    }

                    Box {
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                            modifier = Modifier.clickable { dropdownOpen = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = "$rangeMonths months",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A),
                                    maxLines = 1
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Change trend range",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(12.dp)
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
            }

            // 2. CHART
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 9.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(126.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier
                            .width(24.dp)
                            .height(100.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("100", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text("50", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text("0", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val height = size.height
                                listOf(0f, 0.5f, 1f).forEach { fraction ->
                                    val y = height - height * fraction
                                    drawLine(
                                        color = Color(0xFFE2E8F0),
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = 0.7.dp.toPx()
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                visibleMonths.forEach { point ->
                                    val isCurrent = point.label == "Oct"
                                    val hasValue = point.percent != null
                                    val barColor = when {
                                        !hasValue -> Brush.verticalGradient(
                                            listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0))
                                        )
                                        isCurrent -> Brush.verticalGradient(
                                            listOf(Color(0xFF047857), Color(0xFF065F46))
                                        )
                                        else -> Brush.verticalGradient(
                                            listOf(Color(0xFF6EE7B7), Color(0xFF10B981))
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Text(
                                            text = point.percent?.toString() ?: "-",
                                            fontSize = 7.5.sp,
                                            fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                                            color = if (isCurrent) Color(0xFF047857) else Color(0xFF64748B),
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(if (rangeMonths == 12) 11.dp else 18.dp)
                                                .height(
                                                    if (hasValue) {
                                                        (78.dp * ((point.percent ?: 0) / 100f))
                                                            .coerceAtLeast(6.dp)
                                                    } else {
                                                        7.dp
                                                    }
                                                )
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(barColor)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.8.dp)
                                .background(Color(0xFFCBD5E1))
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            visibleMonths.forEach { point ->
                                val isCurrent = point.label == "Oct"
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = point.label,
                                        fontSize = 7.5.sp,
                                        fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                                        color = if (isCurrent) Color(0xFF047857) else Color(0xFF64748B),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                // 3. KPI CARDS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    TrendKpiCard(
                        emoji = "\uD83D\uDC51",
                        label = "Best month",
                        value = "${bestMonth?.percent ?: 0}%",
                        detail = bestMonth?.label ?: "--",
                        background = Color(0xFFECFDF5),
                        border = Color(0xFFBBF7D0),
                        accent = Color(0xFF047857),
                        modifier = Modifier.weight(1f)
                    )
                    TrendKpiCard(
                        emoji = "\u2197",
                        label = "Average",
                        value = "$averagePercent%",
                        detail = "${activeMonths.size} active months",
                        background = Color(0xFFFFFBEB),
                        border = Color(0xFFFDE68A),
                        accent = Color(0xFFB45309),
                        modifier = Modifier.weight(1.15f)
                    )
                    TrendKpiCard(
                        emoji = "\u2198",
                        label = "Lowest",
                        value = "${lowestMonth?.percent ?: 0}%",
                        detail = lowestMonth?.label ?: "--",
                        background = Color(0xFFFEF2F2),
                        border = Color(0xFFFECACA),
                        accent = Color(0xFFDC2626),
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .clickable {
                                rangeMonths = if (rangeMonths == 12) 6 else 12
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Switch trend range",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrendKpiCard(
    emoji: String,
    label: String,
    value: String,
    detail: String,
    background: Color,
    border: Color,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(13.dp),
        color = background,
        border = BorderStroke(1.dp, border),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 9.sp)
                }
                Text(
                    text = label,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = accent,
                maxLines = 1
            )
            Text(
                text = detail,
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                color = accent.copy(alpha = 0.85f),
                maxLines = 1
            )
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
