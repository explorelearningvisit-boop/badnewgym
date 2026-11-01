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
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.AttendanceSectionData
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus
import com.example.badnewgym.feature.membercard.domain.model.MemberCardType
import com.example.badnewgym.feature.membercard.domain.model.PaymentStatus
import com.example.badnewgym.feature.membercard.design.MemberCardAccent
import com.example.badnewgym.feature.membercard.design.MemberCardAccentManager
import com.example.badnewgym.feature.membercard.design.MemberCardAccentTones
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * BAD GYM — Master Member Intelligence Design Tokens
 * Extracted directly from official Infographic Specification media_1793290011933_8ef34b93.jpg
 */
// =============================================================================
// BAD GYM — ICE-MINT GLASS DESIGN TOKENS
// =============================================================================

object InfographicTokens {
    // ---- Page canvas: mint -> glacier blue -> mint wash ----
    val MainBg = Color(0xFFF4FBF8)
    val PageTop = Color(0xFFE1F6F0)
    val PageMid = Color(0xFFECF4FE)
    val PageBottom = Color(0xFFE6F7F0)
    val CanvasBrush = Brush.verticalGradient(
        0.00f to Color(0xFFE1F6F0),
        0.38f to Color(0xFFEDF4FD),
        0.72f to Color(0xFFE8F7F1),
        1.00f to Color(0xFFE6F7F0)
    )

    // ---- Frosted glass surfaces ----
    val Surface = Color(0xF7FFFFFF)          // Primary glass card
    val SurfaceSoft = Color(0xE6FFFFFF)      // Secondary glass
    val SurfaceMint = Color(0xFFF0FBF6)
    val CardBg = Surface
    val Glass = Color(0xCCEFFBF7)

    // Frosted fill + lit edge: the two layers that make the glass read as glass.
    val GlassFill = Brush.verticalGradient(
        0.00f to Color(0xFCFFFFFF),
        0.55f to Color(0xF7FDFFFE),
        1.00f to Color(0xF0F2FBF8)
    )
    val GlassEdge = Brush.verticalGradient(
        0.00f to Color(0xFFFFFFFF),
        0.45f to Color(0xFFE3F0EC),
        1.00f to Color(0xFFCFE3DD)
    )

    // ---- Typography ----
    val Ink = Color(0xFF081A15)              // Near-black forest ink
    val InkSoft = Color(0xFF2C4741)
    val Muted = Color(0xFF52706A)            // 5.1:1 on white — readable micro-labels
    val Hairline = Color(0xFFD5E7E2)

    // ---- Accents ----
    val Purple = Color(0xFF7C3AED)
    val Magenta = Color(0xFFD92BAF)
    val Pink = Color(0xFFF43F5E)
    val Orange = Color(0xFFF97316)
    val Yellow = Color(0xFFFFC53D)
    val Cyan = Color(0xFF22C3E6)
    val Blue = Color(0xFF2563EB)
    val Emerald = Color(0xFF10B981)
    val EmeraldDeep = Color(0xFF0B8A61)
    val Amber = Color(0xFFF59E0B)
    val RedAlert = Color(0xFFEF4444)
    val HolidayGray = Color(0xFFEFF4F3)

    // ---- Semantic ----
    val Brand = Emerald
    val BrandDeep = EmeraldDeep
    val BrandSoft = Color(0xFFDCF7EC)
    val Gold = Color(0xFFF6B73C)
    val GoldSoft = Color(0xFFFEF3C7)
    val Danger = RedAlert
    val DangerSoft = Color(0xFFFEE2E2)
    val Teal = Cyan
    val TealSoft = Color(0xFFE0F7FB)
    val BlueSoft = Color(0xFFE0E7FF)

    // ---- Legacy aliases kept for existing call sites ----
    val PrimaryBlue = Blue
    val NeonPurple = Purple
    val ElectricCyan = Cyan
    val SoftPink = Pink
    val MintGreen = Color(0xFF34D399)
    val LimeGreen = Color(0xFFA8FF5B)
    val SkyLight = Color(0xFFF0F9FF)

    // ---- Gradients ----
    // Check-in stadium: glossy emerald ribbon.
    val HeaderGradient = Brush.horizontalGradient(
        listOf(Color(0xFF19C48A), Color(0xFF0FA875), Color(0xFF07956A))
    )
    val BorderGradient = Brush.linearGradient(
        listOf(Color(0xFFCFEDE3), Color(0xFFDCE9FB), Color(0xFFD2F0E1))
    )
    // Membership progress rail: mint -> teal -> cyan.
    val ThinProgressGradient = Brush.horizontalGradient(
        listOf(Color(0xFF39D98A), Color(0xFF12B0A0), Color(0xFF1FB6E4))
    )
    val AuroraWaveGradient = Brush.horizontalGradient(
        listOf(Color(0xFF10B981), Color(0xFF14B8A6), Color(0xFF22C3E6))
    )
    val TrendWaveGradient = Brush.horizontalGradient(
        listOf(Color(0xFFF2FDF8), Color(0xFFF4FAFF), Color(0xFFF7F5FF))
    )
    val DaysCardGradient = Brush.horizontalGradient(
        listOf(Color(0xFF39D98A), Color(0xFF12B0A0), Color(0xFF1FB6E4))
    )
    val AttendanceGradient = Brush.horizontalGradient(listOf(Color(0xFF047857), Color(0xFF059669), Color(0xFF10B981)))
    val WorkoutGradient = Brush.horizontalGradient(listOf(Color(0xFF6B21A8), Color(0xFF7C3AED), Color(0xFF9333EA)))
    val PaymentGradient = Brush.horizontalGradient(listOf(Color(0xFFC2410C), Color(0xFFEA580C), Color(0xFFF97316)))
    val BodyGradient = Brush.horizontalGradient(listOf(Color(0xFF1D4ED8), Color(0xFF0284C7), Color(0xFF38BDF8)))
    val BottomBannerGradient = Brush.horizontalGradient(
        listOf(Color(0xFF0B8A61), Color(0xFF0FA875), Color(0xFF12B0A0))
    )
}


// =============================================================================
// SECTION TINTS — the gentle wash each block carries
// =============================================================================

object GlassTint {
    val Mint = Color(0xFFF2FBF7)
    val MintTint = Color(0xFFEAF8F1)
    val MintEdge = Color(0xFFB9E6D2)

    val Sky = Color(0xFFF3F9FF)
    val SkyTint = Color(0xFFEAF2FE)
    val SkyEdge = Color(0xFFC4D9F7)

    val Violet = Color(0xFFF7F3FF)
    val VioletTint = Color(0xFFF0EBFF)
    val VioletEdge = Color(0xFFDDD2FA)

    val Rose = Color(0xFFFFF4F5)
    val RoseEdge = Color(0xFFFBD6DA)

    val Amber = Color(0xFFFFF9EE)
    val AmberEdge = Color(0xFFFBE4BD)

    // ---- Card fills: white at the crown, the section hue at the base ----
    val MemberWash = Brush.verticalGradient(
        0.00f to Color(0xFFFFFFFF),
        0.45f to Color(0xFFF4FCF8),
        0.75f to Color(0xFFF1F8FF),
        1.00f to Color(0xFFF0FAF5)
    )
    val MemberEdgeBrush = Brush.verticalGradient(
        0.00f to Color(0xFFFFFFFF),
        0.50f to Color(0xFFCDEBDD),
        1.00f to Color(0xFFBFE0EE)
    )

    val SkyWash = Brush.verticalGradient(
        0.00f to Color(0xFFFFFFFF),
        0.55f to Color(0xFFF4FAFF),
        1.00f to Color(0xFFEDF4FE)
    )
    val SkyEdgeBrush = Brush.verticalGradient(
        0.00f to Color(0xFFFFFFFF),
        0.50f to Color(0xFFD5E4F8),
        1.00f to Color(0xFFBFD5F2)
    )

    val VioletWash = Brush.verticalGradient(
        0.00f to Color(0xFFFFFFFF),
        0.55f to Color(0xFFF8F6FF),
        1.00f to Color(0xFFF1ECFF)
    )
    val VioletEdgeBrush = Brush.verticalGradient(
        0.00f to Color(0xFFFFFFFF),
        0.50f to Color(0xFFE2D9FA),
        1.00f to Color(0xFFD3C6F6)
    )
}

// =============================================================================
// GLASS SHELL — frosted card, neumorphic shadow, lit edge, optional tint
// =============================================================================

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 20.dp,
    fill: Brush = InfographicTokens.GlassFill,
    edge: Brush = InfographicTokens.GlassEdge,
    shadow: Dp = 3.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    // The lift lives on the modifier so the shadow can carry the section hue
    // instead of the flat slate Surface elevation produces on its own.
    Surface(
        shape = shape,
        color = Color.Transparent,
        shadowElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .neumorphicLift(shape, elevation = shadow + 5.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(shape)
                .background(fill)
                .border(1.dp, edge, shape)
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
            content = content
        )
    }
}

@Composable
private fun BandDivider(inset: Dp = 0.dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = inset)
            .height(1.dp)
            .background(InfographicTokens.Hairline.copy(alpha = 0.7f))
    )
}

// =============================================================================
// COMPONENT 1 — HEADER BAND (Crown · CHECK-IN stadium · Paid capsule)
// =============================================================================

@Composable
fun TopHeaderComponent(
    planTitle: String = "SILVER",
    checkInTime: String = "Today \u2022 08:49 AM",
    isActive: Boolean = true,
    cardType: MemberCardType = MemberCardType.CHECK_IN,
    paymentStatus: PaymentStatus? = null,
    paidAmount: String = "\u20B92,800",
    paidDate: String = "03 Nov 2026",
    isPaid: Boolean = true,
    accent: Color = MemberCardAccent.EMERALD.color,
    onCheckInClick: () -> Unit = {},
    onPaymentHeaderClick: () -> Unit = {},
    onPlanClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val checkInLabel = when (cardType) {
        MemberCardType.TRAINER_CHECK_IN -> "TRAINER CHECK-IN"
        MemberCardType.PAYMENT -> "PAYMENT UPDATE"
        MemberCardType.MEMBER_PASS -> "MEMBER PASS"
        else -> "CHECK-IN"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .padding(start = 8.dp, end = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "\uD83D\uDC51", fontSize = 17.6.sp)
        Spacer(modifier = Modifier.width(3.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "BAD ",
                    fontSize = 17.6.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.Ink,
                    letterSpacing = (-0.4).sp,
                    lineHeight = 17.6.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = "GYM",
                    fontSize = 17.6.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.Emerald,
                    letterSpacing = 0.2.sp,
                    lineHeight = 17.6.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }
            Text(
                text = "Member Intelligence",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = InfographicTokens.Muted,
                lineHeight = 12.sp,
                maxLines = 1,
                softWrap = false
            )
        }

        Spacer(modifier = Modifier.width(5.dp))
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(22.dp)
                .background(InfographicTokens.Hairline)
        )
        Spacer(modifier = Modifier.width(5.dp))

        // Glossy accent-colored check-in stadium
        Box(
            modifier = Modifier
                .weight(1f)
                .height(33.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MemberCardAccentTones.stadiumBrush(accent))
                .border(1.dp, Color(0x4DFFFFFF), RoundedCornerShape(50.dp))
                .clickable { onCheckInClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0x40FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = checkInLabel,
                        tint = Color.White,
                        modifier = Modifier.size(12.5.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = checkInLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-0.1).sp,
                        lineHeight = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                    Text(
                        text = checkInTime,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xF2FFFFFF),
                        lineHeight = 11.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        // White paid capsule: accent calendar tile, red amount & red due date
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
                .border(1.dp, InfographicTokens.Hairline, RoundedCornerShape(50.dp))
                .clickable { onPaymentHeaderClick() }
                .padding(start = 3.dp, end = 5.dp, top = 2.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MemberCardAccentTones.verticalBrush(accent)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\u20B9",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    lineHeight = 12.sp
                )
            }
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = paidAmount,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.RedAlert,
                    lineHeight = 15.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = paidDate,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.RedAlert,
                    lineHeight = 11.5.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

// =============================================================================
// COMPONENT 2 — PROFILE BAND (avatar · identity · plan pills)
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
    planName: String = "Gold Plan",
    planAmount: String = "\u20B92,800",
    planDays: Int = 30,
    autoRenew: Boolean = true,
    accent: Color = MemberCardAccent.EMERALD.color,
    onAvatarClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MemberCardAccentTones.wash(accent))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        // Avatar with accent ring + solid "Active" pill underneath (mockup layout)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Box(
                modifier = Modifier.size(42.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MemberCardAccentTones.soft(accent))
                        .border(2.dp, accent, CircleShape)
                )
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.White, CircleShape)
                        .clickable { onAvatarClick() }
                )
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(accent)
                    .padding(horizontal = 5.dp, vertical = 0.5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Text(
                    text = "Active",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    lineHeight = 11.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = name,
                fontSize = 17.6.sp,
                fontWeight = FontWeight.Black,
                color = InfographicTokens.Ink,
                letterSpacing = (-0.5).sp,
                lineHeight = 18.7.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Meta: member id · age · gender
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ProfileMetaItem(icon = Icons.Default.Person, text = memberId)
                ProfileMetaDivider()
                ProfileMetaItem(icon = Icons.Default.CalendarToday, text = "$age Yrs")
                ProfileMetaDivider()
                ProfileMetaItem(icon = null, text = "\u2642 $gender")
            }

            // Plan pills
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF7F2FF))
                        .border(1.dp, GlassTint.VioletEdge, RoundedCornerShape(50.dp))
                        .padding(horizontal = 5.dp, vertical = 1.5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(text = "\uD83D\uDC8E", fontSize = 11.sp)
                    Text(
                        text = planName,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = InfographicTokens.Purple,
                        lineHeight = 12.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFEFFAF5))
                        .border(1.dp, GlassTint.MintEdge, RoundedCornerShape(50.dp))
                        .padding(horizontal = 5.dp, vertical = 1.5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = planAmount,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = InfographicTokens.Ink,
                        lineHeight = 12.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        text = "/ $planDays Days",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = InfographicTokens.Muted,
                        lineHeight = 11.5.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF1F7FF))
                        .border(1.dp, GlassTint.SkyEdge, RoundedCornerShape(50.dp))
                        .padding(horizontal = 5.dp, vertical = 1.5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = InfographicTokens.Blue,
                        modifier = Modifier.size(8.5.dp)
                    )
                    Text(
                        text = "Auto",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = InfographicTokens.InkSoft,
                        lineHeight = 11.5.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (autoRenew) InfographicTokens.EmeraldDeep
                                else Color(0xFFCBD5E1)
                            )
                            .padding(horizontal = 3.dp, vertical = 0.5.dp)
                    ) {
                        Text(
                            text = if (autoRenew) "ON" else "OFF",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 10.5.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileMetaItem(icon: ImageVector?, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF70908A),
                modifier = Modifier.size(9.5.dp)
            )
        }
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = InfographicTokens.InkSoft,
            lineHeight = 12.sp,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun ProfileMetaDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(8.dp)
            .background(Color(0xFFCDE3DD))
    )
}

// =============================================================================
// COMPONENT 3 — BIOMETRIC BAND (Height · Weight · BMI · Goal)
// =============================================================================

@Composable
fun StatsRowCompact(
    height: String = "5'9\"",
    weight: String = "69.5 kg",
    weightDelta: String? = "\u25BC 2.5 kg",
    bmi: String = "22.8",
    bmiCategory: String = "Normal",
    goal: String = "General Fitness",
    goalSubtitle: String = "Regular Training",
    accent: Color = MemberCardAccent.EMERALD.color,
    onGoalClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(MemberCardAccentTones.wash(accent))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Height
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.weight(1f)
        ) {
            BiometricRulerIcon(modifier = Modifier.size(19.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Height",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = InfographicTokens.Muted,
                    lineHeight = 12.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = height,
                    fontSize = 15.9.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.Ink,
                    lineHeight = 16.5.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        StatsDivider()

        // Weight
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.weight(1.2f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.illustration_3d_body_scale),
                contentDescription = "Weight",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(21.dp)
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Weight",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = InfographicTokens.Muted,
                    lineHeight = 12.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = weight,
                    fontSize = 15.9.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.Ink,
                    lineHeight = 16.5.sp,
                    maxLines = 1,
                    softWrap = false
                )
                if (weightDelta != null) {
                    Text(
                        text = weightDelta,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0B8A61),
                        lineHeight = 11.5.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        StatsDivider()

        // BMI
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.weight(1.3f)
        ) {
            BiometricChartIcon(modifier = Modifier.size(19.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "BMI",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = InfographicTokens.Muted,
                    lineHeight = 12.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = bmi,
                        fontSize = 15.9.sp,
                        fontWeight = FontWeight.Black,
                        color = InfographicTokens.Ink,
                        lineHeight = 16.5.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFD9F6EA))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = bmiCategory,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF12734B),
                        lineHeight = 11.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        StatsDivider()

        // Goal
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .weight(1.35f)
                .clickable { onGoalClick() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_current_focus_target),
                contentDescription = "Goal",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(19.dp)
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Goal",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = InfographicTokens.Muted,
                    lineHeight = 12.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = goal,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.Ink,
                    lineHeight = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun StatsDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(InfographicTokens.Hairline.copy(alpha = 0.9f))
    )
}

// =============================================================================
// COMPONENT 3B — MEMBERSHIP PROGRESS BAND
// =============================================================================

@Composable
fun FullWidthDaysProgressCard(
    daysPassed: Int = 22,
    totalDays: Int = 30,
    percentage: Int = 73,
    validity: String = "09 Oct \u2013 07 Nov 2026",
    accent: Color = MemberCardAccent.EMERALD.color,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val safeTotal = totalDays.coerceAtLeast(1)
    val used = daysPassed.coerceIn(0, safeTotal)
    val daysLeft = safeTotal - used
    val fraction = (used.toFloat() / safeTotal.toFloat()).coerceIn(0f, 1f)
    val usedPercent = String.format(Locale.US, "%.1f", fraction * 100f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(MemberCardAccentTones.wash(accent))
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(MemberCardAccentTones.verticalBrush(accent))
                .border(1.dp, MemberCardAccentTones.edge(accent), RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(17.dp)
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
                    text = "Membership Progress",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = InfographicTokens.Ink,
                    letterSpacing = (-0.3).sp,
                    lineHeight = 15.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$used/$safeTotal Days",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = accent,
                    lineHeight = 12.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(13.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFE6F2EE))
                    .border(1.dp, Color(0xFFD8EAE4), RoundedCornerShape(50.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction)
                        .clip(RoundedCornerShape(50.dp))
                        .background(MemberCardAccentTones.stadiumBrush(accent))
                )
                Text(
                    text = "$usedPercent% Used",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (fraction >= 0.62f) Color.White else InfographicTokens.InkSoft,
                    maxLines = 1,
                    softWrap = false,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = validity,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = InfographicTokens.Muted,
                    lineHeight = 11.5.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(MemberCardAccentTones.soft(accent))
                        .border(1.dp, MemberCardAccentTones.edge(accent), RoundedCornerShape(50.dp))
                        .padding(horizontal = 5.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(8.dp)
                    )
                    Text(
                        text = "$daysLeft Days Left",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = accent,
                        lineHeight = 11.5.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

// =============================================================================
// COMPONENT 3C — OVERALL STATUS BAND (On Track · Paid · Attendance · Weight)
// =============================================================================

@Composable
fun OverallStatusStrip(
    statusLabel: String = "On Track",
    paymentLabel: String = "Payment Paid",
    attendanceLabel: String = "Attendance 81%",
    weightLabel: String = "Weight \u2193 2.5 kg",
    accent: Color = MemberCardAccent.EMERALD.color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MemberCardAccentTones.wash(accent))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            StatusChip(
                icon = Icons.Default.Check,
                tone = InfographicTokens.EmeraldDeep,
                value = statusLabel,
                modifier = Modifier.weight(1f)
            )
            StatusChip(
                icon = Icons.Default.Check,
                tone = InfographicTokens.EmeraldDeep,
                value = paymentLabel,
                modifier = Modifier.weight(1f)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            StatusChip(
                icon = Icons.Default.Warning,
                tone = InfographicTokens.Amber,
                value = attendanceLabel,
                modifier = Modifier.weight(1f)
            )
            StatusChip(
                icon = Icons.Default.Check,
                tone = InfographicTokens.EmeraldDeep,
                value = weightLabel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatusChip(
    icon: ImageVector,
    tone: Color,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .clip(CircleShape)
                .background(tone.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tone,
                modifier = Modifier.size(7.5.dp)
            )
        }
        Text(
            text = value,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Black,
            color = InfographicTokens.Ink,
            lineHeight = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}

@Composable
private fun StatusSeparator() {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .width(1.dp)
            .height(10.dp)
            .background(Color(0xFFCFE4DE))
    )
}

// =============================================================================
// UNIFIED MEMBER CARD — header + profile + biometrics + progress + status
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
    weightDelta: String? = "▼ 2.5 kg",
    bmi: String = "22.8",
    bmiCategory: String = "Normal",
    goal: String = "General Fitness",
    goalSubtitle: String = "Regular Training",
    planName: String = "Gold Plan",
    isPaid: Boolean = true,
    autoRenew: Boolean = true,
    daysPassed: Int = 22,
    totalDays: Int = 30,
    percentage: Int = 73,
    statusLabel: String = "On Track",
    paymentLabel: String = "Payment Paid",
    attendanceLabel: String = "Attendance 81%",
    weightLabel: String = "Weight ↓ 2.5 kg",
    onCheckInClick: () -> Unit = {},
    onPaymentHeaderClick: () -> Unit = {},
    onPlanClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onGoalClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Single reactive accent re-skins every band of the card at once.
    val accent = MemberCardAccentManager.activeAccent.color

    GlassCard(
        modifier = modifier,
        corner = 20.dp,
        fill = MemberCardAccentTones.cardWash(accent),
        edge = MemberCardAccentTones.cardEdge(accent),
        shadow = 3.dp
    ) {
        TopHeaderComponent(
            planTitle = planTitle,
            checkInTime = checkInTime,
            isActive = isActive,
            cardType = cardType,
            paymentStatus = paymentStatus,
            paidAmount = paidAmount,
            paidDate = paidDate,
            isPaid = isPaid,
            accent = accent,
            onCheckInClick = onCheckInClick,
            onPaymentHeaderClick = onPaymentHeaderClick,
            onPlanClick = onPlanClick
        )

        BandDivider()

        ProfileSection(
            name = name,
            memberId = memberId,
            age = age,
            gender = gender,
            quote = quote,
            validity = validity,
            avatarRes = avatarRes,
            planName = planName,
            planAmount = paidAmount,
            planDays = totalDays,
            autoRenew = autoRenew,
            accent = accent,
            onAvatarClick = onAvatarClick,
            onMenuClick = onMenuClick
        )

        BandDivider()

        StatsRowCompact(
            height = height,
            weight = weight,
            weightDelta = weightDelta,
            bmi = bmi,
            bmiCategory = bmiCategory,
            goal = goal,
            goalSubtitle = goalSubtitle,
            accent = accent,
            onGoalClick = onGoalClick
        )

        BandDivider()

        FullWidthDaysProgressCard(
            daysPassed = daysPassed,
            totalDays = totalDays,
            percentage = percentage,
            validity = validity,
            accent = accent,
            onClick = onProgressClick
        )

        BandDivider()

        OverallStatusStrip(
            statusLabel = statusLabel,
            paymentLabel = paymentLabel,
            attendanceLabel = attendanceLabel,
            weightLabel = weightLabel,
            accent = accent
        )
    }
}

// =============================================================================
// COMPONENT 5 — 4-TAB DOCK (fixed 48dp rail · even tiles · live values)
// =============================================================================

@Composable
fun MenuRow4Tabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    attendanceRate: String = "81%",
    workoutAvg: String = "34 min",
    paymentDue: String = "\u20B92,800",
    bodyWeight: String = "69.5 kg",
    attendanceProgress: Float = 0.81f,
    workoutProgress: Float = 0.70f,
    paymentProgress: Float = 0.65f,
    bodyProgress: Float = 0.74f,
    paymentHasAlert: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)

    Surface(
        shape = shape,
        color = Color.Transparent,
        shadowElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .neumorphicLift(shape, elevation = 7.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(Color(0xFFFFFFFF))
                .border(1.dp, Color(0xFFD3EAE2), shape)
                .height(62.dp)
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            GlassTabItem(
                title = "Attendance",
                value = attendanceRate,
                iconRes = R.drawable.illustration_3d_attendance_calendar,
                accent = Color(0xFF0B8A61),
                tint = Color(0xFFE4FAF0),
                isSelected = selectedTabIndex == 0,
                hasCheck = false,
                onClick = { onTabSelected(0) },
                modifier = Modifier.weight(1f)
            )
            GlassTabItem(
                title = "Workout",
                value = workoutAvg,
                iconRes = R.drawable.illustration_3d_dumbbell_mini,
                accent = Color(0xFF7C3AED),
                tint = Color(0xFFF3EDFF),
                isSelected = selectedTabIndex == 1,
                hasCheck = true,
                onClick = { onTabSelected(1) },
                modifier = Modifier.weight(1f)
            )
            GlassTabItem(
                title = "Payment",
                value = paymentDue,
                iconRes = R.drawable.illustration_3d_payment_wallet,
                accent = Color(0xFFE8720C),
                tint = Color(0xFFFFF1DF),
                isSelected = selectedTabIndex == 2,
                hasCheck = !paymentHasAlert,
                onClick = { onTabSelected(2) },
                modifier = Modifier.weight(1f)
            )
            GlassTabItem(
                title = "Body",
                value = bodyWeight,
                iconRes = R.drawable.ic_body_progress_header_badge,
                accent = Color(0xFF1D6FE8),
                tint = Color(0xFFE8F1FF),
                isSelected = selectedTabIndex == 3,
                hasCheck = false,
                onClick = { onTabSelected(3) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun GlassTabItem(
    title: String,
    value: String,
    iconRes: Int,
    accent: Color,
    tint: Color,
    isSelected: Boolean,
    hasCheck: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val itemShape = RoundedCornerShape(13.dp)
    val fill = if (isSelected) {
        Brush.verticalGradient(listOf(Color(0xFFFFFFFF), tint.copy(alpha = 0.55f)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFDFFFE)))
    }
    val borderColor = if (isSelected) accent.copy(alpha = 0.55f) else Color(0xFFE4F1ED)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(itemShape)
            .background(fill)
            .border(if (isSelected) 1.6.dp else 1.dp, borderColor, itemShape)
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(tint),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(17.dp)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = accent,
                lineHeight = 15.sp,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            if (hasCheck) {
                Spacer(modifier = Modifier.width(2.dp))
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .clip(CircleShape)
                        .background(InfographicTokens.Emerald),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(7.5.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = title,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) accent else InfographicTokens.InkSoft,
            lineHeight = 12.sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// =============================================================================
// COMPONENT 6 — ATTENDANCE CARD (hero · legend · number-only day circles)
// =============================================================================

private object AttendanceBlueprint {
    val Ink = Color(0xFF0A201A)
    val Muted = Color(0xFF55706A)
    val Hairline = Color(0xFFD5E7E2)
    val CardSurface = Color(0xF7FFFFFF)
    val Present = Color(0xFF10B981)
    val PresentDeep = Color(0xFF0B8A61)
    val Late = Color(0xFFF5B301)
    val Absent = Color(0xFFEF4444)
    val Holiday = Color(0xFFB9C6C3)
    val Sunday = Color(0xFFDFE8E6)
    val SundayInk = Color(0xFF7C908B)
    val Empty = Color(0xFFEDF4F2)
    val BlinkitYellow = Color(0xFFF8CB46)
    val HeaderBrush = Brush.horizontalGradient(
        listOf(Color(0xFF19C48A), Color(0xFF0FA875), Color(0xFF07956A))
    )
}

/**
 * One month of attendance history. [percent] is null for months the club has no
 * record for, so the pager can tell "zero" apart from "not tracked".
 */
data class AttendanceMonthRecord(
    val label: String,
    val fullLabel: String,
    val percent: Int?,
    val attendedDays: Int = 0,
    val totalDays: Int = 0
)

fun attendanceMonthHistory(currentPercent: Int = 81): List<AttendanceMonthRecord> = listOf(
    AttendanceMonthRecord("Nov", "Nov 2025", null),
    AttendanceMonthRecord("Dec", "Dec 2025", null),
    AttendanceMonthRecord("Jan", "Jan 2026", 97, 30, 31),
    AttendanceMonthRecord("Feb", "Feb 2026", 86, 24, 28),
    AttendanceMonthRecord("Mar", "Mar 2026", 58, 18, 31),
    AttendanceMonthRecord("Apr", "Apr 2026", null),
    AttendanceMonthRecord("May", "May 2026", 87, 27, 31),
    AttendanceMonthRecord("Jun", "Jun 2026", 70, 21, 30),
    AttendanceMonthRecord("Jul", "Jul 2026", 94, 29, 31),
    AttendanceMonthRecord("Aug", "Aug 2026", null),
    AttendanceMonthRecord("Sep", "Sep 2026", 80, 24, 30),
    AttendanceMonthRecord("Oct", "Oct 2026", currentPercent, 25, 31)
)

@Composable
fun AttendanceSection(
    attendance: AttendanceSectionData,
    onAttendanceClick: () -> Unit = {},
    onDayClick: (CalendarDay) -> Unit = {},
    paidDay: Int? = null,
    dueDay: Int? = null,
    monthRecords: List<AttendanceMonthRecord> = attendanceMonthHistory(),
    modifier: Modifier = Modifier
) {
    val days = attendance.calendarDays.sortedBy { it.dayOfMonth }
    val present = days.count { it.status == DayAttendanceStatus.PRESENT }
    val late = days.count { it.status == DayAttendanceStatus.LATE }
    val absent = days.count { it.status == DayAttendanceStatus.ABSENT }
    val sundays = days.count { it.dayOfWeekIndex == 0 }
    val holidays = days.count {
        it.status == DayAttendanceStatus.HOLIDAY && it.dayOfWeekIndex != 0
    }
    val attended = present + late
    val total = days.size.coerceAtLeast(attendance.totalDays).coerceAtLeast(1)
    val rate = attendance.percent.coerceIn(0, 100)

    val now = Calendar.getInstance()
    val thisMonth = SimpleDateFormat("MMMM yyyy", Locale.US).format(now.time)
    val todayDay = if (thisMonth.equals(attendance.monthLabel, ignoreCase = true)) {
        now.get(Calendar.DAY_OF_MONTH)
    } else {
        -1
    }

    val leadingBlanks = days.firstOrNull()?.let { (it.dayOfWeekIndex + 6) % 7 } ?: 0
    val cells: List<CalendarDay?> = buildList {
        repeat(leadingBlanks) { add(null) }
        addAll(days)
        while (size % 7 != 0) add(null)
    }
    val weeks = cells.chunked(7)

    // Month navigation is backed by the same history the trend chart plots, so
    // the pager never has to invent a month that was not recorded.
    val records = monthRecords.ifEmpty { attendanceMonthHistory(rate) }
    var monthIndex by remember(records) { mutableIntStateOf(records.lastIndex.coerceAtLeast(0)) }
    val activeRecord = records.getOrNull(monthIndex)
    val showingCurrent = activeRecord == null ||
        activeRecord.fullLabel.equals(attendance.monthLabel, ignoreCase = true)
    val shownRate = if (showingCurrent) rate else activeRecord?.percent
    val shownAttended = if (showingCurrent) attended else activeRecord?.attendedDays ?: 0
    val shownTotal = if (showingCurrent) total else activeRecord?.totalDays ?: 0

    GlassCard(
        modifier = modifier,
        corner = 20.dp,
        fill = GlassTint.SkyWash,
        edge = GlassTint.SkyEdgeBrush,
        onClick = onAttendanceClick
    ) {
        AttendanceCardHeader(
            monthLabel = activeRecord?.fullLabel ?: attendance.monthLabel,
            canPrev = monthIndex > 0,
            canNext = monthIndex < records.lastIndex,
            onPrev = { if (monthIndex > 0) monthIndex -= 1 },
            onNext = { if (monthIndex < records.lastIndex) monthIndex += 1 }
        )

        BandDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 9.dp, end = 9.dp, top = 4.dp, bottom = 5.dp)
        ) {
            Column(
                modifier = Modifier.width(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = shownRate?.let { "$it%" } ?: "\u2014",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Black,
                    color = AttendanceBlueprint.PresentDeep,
                    letterSpacing = (-1).sp,
                    lineHeight = 21.6.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = if (shownTotal > 0) "$shownAttended / $shownTotal Days" else "No record",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    color = AttendanceBlueprint.Ink,
                    lineHeight = 12.sp,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )

                Spacer(modifier = Modifier.height(3.dp))

                if (showingCurrent) {
                    AttendanceLegendRow(AttendanceBlueprint.Present, "Present", present)
                    AttendanceLegendRow(AttendanceBlueprint.Late, "Late", late)
                    AttendanceLegendRow(AttendanceBlueprint.Absent, "Absent", absent)
                    AttendanceLegendRow(
                        AttendanceBlueprint.Holiday,
                        "Holiday",
                        holidays + sundays
                    )
                } else {
                    AttendanceLegendRow(
                        AttendanceBlueprint.Present,
                        "Attended",
                        shownAttended
                    )
                    AttendanceLegendRow(
                        AttendanceBlueprint.Absent,
                        "Missed",
                        (shownTotal - shownAttended).coerceAtLeast(0)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .width(1.dp)
                    .height(128.dp)
                    .background(Color(0xFFCFDDEE))
            )

            Column(modifier = Modifier.weight(1f)) {
                if (showingCurrent) {
                    WeekdayHeaderRow()
                    Spacer(modifier = Modifier.height(2.dp))
                    weeks.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            week.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(21.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (day != null) {
                                        AttendanceDayToken(
                                            day = day,
                                            isToday = day.dayOfMonth == todayDay,
                                            badge = when (day.dayOfMonth) {
                                                paidDay -> DayBadge.PAID
                                                dueDay -> DayBadge.DUE
                                                else -> null
                                            },
                                            onDayClick = onDayClick
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    ArchivedMonthPanel(
                        record = activeRecord,
                        onOpenHistory = onAttendanceClick
                    )
                }
            }
        }

        // Horizontal attendance-rate bar pinned to the card bottom.
        val barFraction = (shownRate ?: 0).coerceIn(0, 100) / 100f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 9.dp, end = 9.dp, top = 6.dp, bottom = 2.dp)
                .height(7.dp)
                .clip(CircleShape)
                .background(AttendanceBlueprint.Empty)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(barFraction)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(AttendanceBlueprint.Present, AttendanceBlueprint.PresentDeep)
                        )
                    )
            )
        }
    }
}

private enum class DayBadge { PAID, DUE }

@Composable
private fun WeekdayHeaderRow() {
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { label ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 1.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFEAF3FB))
                    .padding(vertical = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AttendanceBlueprint.Muted,
                    lineHeight = 11.5.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
private fun ArchivedMonthPanel(
    record: AttendanceMonthRecord?,
    onOpenHistory: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .clip(shape)
            .background(Color(0xFFF2F7FE))
            .border(1.dp, Color(0xFFD3E1F6), shape)
            .clickable { onOpenHistory() }
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = record?.fullLabel ?: "No records",
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            color = AttendanceBlueprint.Ink,
            lineHeight = 15.sp,
            maxLines = 1,
            softWrap = false
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = if (record?.percent != null) {
                "${record.attendedDays} of ${record.totalDays} days attended"
            } else {
                "Attendance was not tracked this month"
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = AttendanceBlueprint.Muted,
            lineHeight = 14.sp,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = "Day-by-day view",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1D6FE8),
                lineHeight = 12.sp
            )
            Text(
                text = "\u203A",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1D6FE8),
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
private fun AttendanceCardHeader(
    monthLabel: String,
    canPrev: Boolean,
    canNext: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(horizontal = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.illustration_3d_attendance_calendar),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(21.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Attendance",
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            color = AttendanceBlueprint.Ink,
            letterSpacing = (-0.3).sp,
            lineHeight = 15.sp,
            maxLines = 1,
            softWrap = false
        )
        Spacer(modifier = Modifier.weight(1f))
        MonthPagerPill(
            monthLabel = monthLabel,
            canPrev = canPrev,
            canNext = canNext,
            onPrev = onPrev,
            onNext = onNext
        )
    }
}

@Composable
private fun MyProteinPill() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFFFFFFF))
            .border(1.dp, Color(0xFFD8E7F3), RoundedCornerShape(50.dp))
            .padding(horizontal = 5.dp, vertical = 2.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Supplement bottle glyph.
        Column(
            modifier = Modifier.height(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(2.8.dp)
                    .clip(RoundedCornerShape(topStart = 1.dp, topEnd = 1.dp))
                    .background(Color(0xFF94A3B8))
            )
            Box(
                modifier = Modifier
                    .width(8.5.dp)
                    .height(10.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF0F172A)))
                    )
                    .border(0.5.dp, Color(0xFF64748B), RoundedCornerShape(2.dp))
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Stay Fit with",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = AttendanceBlueprint.Muted,
                lineHeight = 10.5.sp,
                maxLines = 1,
                softWrap = false
            )
            Text(
                text = "MYPROTEIN",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Black,
                color = AttendanceBlueprint.Ink,
                letterSpacing = (-0.1).sp,
                lineHeight = 12.sp,
                maxLines = 1,
                softWrap = false
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFFF59E0B))
                .padding(horizontal = 2.5.dp, vertical = 1.dp)
        ) {
            Text(
                text = "AD",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                lineHeight = 10.sp
            )
        }
    }
}

@Composable
private fun BlinkitPill() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFFFFFFF))
            .border(1.dp, Color(0xFFE4E4EF), RoundedCornerShape(50.dp))
            .padding(horizontal = 5.dp, vertical = 2.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Shaker bottle glyph.
        Column(
            modifier = Modifier.height(19.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .width(7.dp)
                    .height(4.5.dp)
                    .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF6B21A8), Color(0xFF3B0764)))
                    )
            )
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(12.5.dp)
                    .clip(RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFFF1F5F9), Color(0xFFCBD5E1)))
                    )
                    .border(
                        0.5.dp,
                        Color(0xFFCBD5E1),
                        RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp)
                    )
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(AttendanceBlueprint.BlinkitYellow)
                .padding(horizontal = 3.dp, vertical = 1.dp)
        ) {
            Text(
                text = "blinkit",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0B0B0B),
                lineHeight = 11.sp,
                maxLines = 1
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Healthy Snacks",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AttendanceBlueprint.Ink,
                lineHeight = 11.sp,
                maxLines = 1,
                softWrap = false
            )
            Text(
                text = "For Better Workouts",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = AttendanceBlueprint.Muted,
                lineHeight = 10.5.sp,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
private fun MonthPagerPill(
    monthLabel: String,
    canPrev: Boolean,
    canNext: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFFFFFFF))
            .border(1.dp, Color(0xFFD8E7F3), RoundedCornerShape(50.dp))
            .padding(horizontal = 3.dp, vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PagerArrow(
            glyph = "\u2039",
            enabled = canPrev,
            contentDescription = "Previous month",
            onClick = onPrev
        )
        Text(
            text = monthLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = if (canPrev || canNext) AttendanceBlueprint.Ink else AttendanceBlueprint.Muted,
            lineHeight = 12.sp,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.padding(horizontal = 1.dp)
        )
        PagerArrow(
            glyph = "\u203A",
            enabled = canNext,
            contentDescription = "Next month",
            onClick = onNext
        )
    }
}

@Composable
private fun PagerArrow(
    glyph: String,
    enabled: Boolean,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(if (enabled) Color(0xFFE9F3FD) else Color.Transparent)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = glyph,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = if (enabled) AttendanceBlueprint.Ink else Color(0xFFB9C8C4),
            lineHeight = 13.sp
        )
    }
}

@Composable
private fun AttendanceLegendRow(
    color: Color,
    label: String,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(5.5.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = AttendanceBlueprint.Ink,
            lineHeight = 11.5.sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$count",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Black,
            color = AttendanceBlueprint.Ink,
            lineHeight = 12.sp,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun AttendanceDayToken(
    day: CalendarDay,
    isToday: Boolean,
    badge: DayBadge?,
    onDayClick: (CalendarDay) -> Unit
) {
    val isSunday = day.dayOfWeekIndex == 0
    val fill = when {
        isSunday -> AttendanceBlueprint.Sunday
        day.status == DayAttendanceStatus.PRESENT -> AttendanceBlueprint.Present
        day.status == DayAttendanceStatus.LATE -> AttendanceBlueprint.Late
        day.status == DayAttendanceStatus.ABSENT -> AttendanceBlueprint.Absent
        else -> AttendanceBlueprint.Empty
    }
    val foreground = if (isSunday) AttendanceBlueprint.SundayInk else Color.White

    Box(
        modifier = Modifier
            .size(19.dp)
            .clickable { onDayClick(day) },
        contentAlignment = Alignment.Center
    ) {
        if (isToday) {
            Box(
                modifier = Modifier
                    .size(19.dp)
                    .clip(CircleShape)
                    .border(1.4.dp, AttendanceBlueprint.PresentDeep, CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .size(17.dp)
                .clip(CircleShape)
                .background(fill),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${day.dayOfMonth}",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Black,
                color = foreground,
                lineHeight = 11.5.sp,
                maxLines = 1,
                softWrap = false
            )
        }
        if (badge != null && !isSunday) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-0.5).dp, y = 1.dp)
                    .size(5.5.dp)
                    .clip(CircleShape)
                    .background(
                        if (badge == DayBadge.DUE) AttendanceBlueprint.Absent
                        else AttendanceBlueprint.PresentDeep
                    )
                    .border(1.dp, Color.White, CircleShape)
            )
        }
    }
}

// =============================================================================
// COMPONENT 7 — MONTHLY TREND (bars · BEST/LOW callouts · 4 KPI pods)
// =============================================================================

/**
 * Catmull-Rom → cubic Bézier: a smooth, non-overshooting spline through [pts].
 */
private fun smoothTrendPath(pts: List<Offset>): Path {
    val path = Path()
    if (pts.isEmpty()) return path
    path.moveTo(pts.first().x, pts.first().y)
    if (pts.size == 1) return path
    for (i in 0 until pts.size - 1) {
        val p0 = pts.getOrElse(i - 1) { pts[i] }
        val p1 = pts[i]
        val p2 = pts[i + 1]
        val p3 = pts.getOrElse(i + 2) { p2 }
        val c1x = p1.x + (p2.x - p0.x) / 6f
        val c1y = p1.y + (p2.y - p0.y) / 6f
        val c2x = p2.x - (p3.x - p1.x) / 6f
        val c2y = p2.y - (p3.y - p1.y) / 6f
        path.cubicTo(c1x, c1y, c2x, c2y, p2.x, p2.y)
    }
    return path
}

@Composable
fun MonthlyTrendSection(
    currentPercent: Int = 81,
    monthLabel: String = "Oct 2026",
    modifier: Modifier = Modifier
) {
    var rangeMonths by remember { mutableIntStateOf(12) }

    val allMonths = remember(currentPercent) { attendanceMonthHistory(currentPercent) }

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
    val maxPercent = bestMonth?.percent ?: 0
    val minPercent = lowestMonth?.percent ?: 0
    val currentLabel = monthLabel.take(3)
    val rangeCaption = listOfNotNull(
        visibleMonths.firstOrNull()?.fullLabel,
        visibleMonths.lastOrNull()?.fullLabel
    ).joinToString(" \u2013 ")

    // Spline / area chart geometry (compact — sized to save vertical space).
    val trendHeadroom = 31.dp          // room for the tallest bubble + crown
    val trendPlotHeight = 58.dp        // the 0%–100% curve region
    val trendBubbleHeight = 20.dp
    val trendBoxHeight = trendHeadroom + trendPlotHeight

    GlassCard(
        modifier = modifier,
        corner = 20.dp,
        fill = GlassTint.VioletWash,
        edge = GlassTint.VioletEdgeBrush
    ) {
        TrendCardHeader(
            caption = rangeCaption,
            rangeMonths = rangeMonths,
            onRangeChange = { rangeMonths = it }
        )

        BandDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 7.dp, end = 8.dp, top = 6.dp, bottom = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(23.dp)
                    .height(trendBoxHeight)
            ) {
                listOf(100, 75, 50, 25, 0).forEachIndexed { index, value ->
                    Text(
                        text = "$value%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7C8CA8),
                        lineHeight = 11.5.sp,
                        maxLines = 1,
                        modifier = Modifier.offset(
                            y = trendHeadroom + trendPlotHeight * (index / 4f) - 8.dp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(2.dp))

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(trendBoxHeight)
                ) {
                    val n = visibleMonths.size.coerceAtLeast(1)
                    val activeNodes = visibleMonths.mapIndexedNotNull { i, p ->
                        p.percent?.let { i to it }
                    }

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val headroomPx = trendHeadroom.toPx()
                        val plotPx = trendPlotHeight.toPx()
                        val baseline = headroomPx + plotPx
                        fun yFor(v: Float) = headroomPx + plotPx * (1f - v / 100f)
                        fun xFor(i: Int) = size.width * (i + 0.5f) / n

                        // Horizontal grid lines.
                        listOf(0f, 25f, 50f, 75f, 100f).forEach { reference ->
                            drawLine(
                                color = Color(0xFFE3E6F2),
                                start = Offset(0f, yFor(reference)),
                                end = Offset(size.width, yFor(reference)),
                                strokeWidth = 0.7.dp.toPx()
                            )
                        }

                        // Average reference line (dashed amber).
                        drawLine(
                            color = Color(0xFFF59E0B),
                            start = Offset(0f, yFor(averagePercent.toFloat())),
                            end = Offset(size.width, yFor(averagePercent.toFloat())),
                            strokeWidth = 1.2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(4.dp.toPx(), 3.dp.toPx()),
                                0f
                            )
                        )

                        if (activeNodes.size >= 2) {
                            val pts = activeNodes.map { (i, v) ->
                                Offset(xFor(i), yFor(v.toFloat()))
                            }
                            val line = smoothTrendPath(pts)
                            val area = Path().apply {
                                addPath(line)
                                lineTo(pts.last().x, baseline)
                                lineTo(pts.first().x, baseline)
                                close()
                            }
                            drawPath(
                                path = area,
                                brush = Brush.verticalGradient(
                                    startY = headroomPx,
                                    endY = baseline,
                                    colors = listOf(
                                        Color(0xFF7C3AED).copy(alpha = 0.45f),
                                        Color(0xFFA78BFA).copy(alpha = 0.06f)
                                    )
                                )
                            )
                            drawPath(
                                path = line,
                                color = Color(0xFF6D28D9),
                                style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }

                        // Nodes: only tracked months get a marker. Greying out an untracked
                        // month parks a stray ring on the baseline that reads as 0%.
                        visibleMonths.forEachIndexed { i, point ->
                            val v = point.percent ?: return@forEachIndexed
                            val cx = xFor(i)
                            val isMin = point == lowestMonth
                            val cy = yFor(v.toFloat())
                            if (isMin) {
                                drawCircle(color = Color(0xFFEF4444).copy(alpha = 0.22f), radius = 8.dp.toPx(), center = Offset(cx, cy))
                            }
                            drawCircle(color = Color.White, radius = 4.6.dp.toPx(), center = Offset(cx, cy))
                            drawCircle(
                                color = if (isMin) Color(0xFFEF4444) else Color(0xFF6D28D9),
                                radius = 3.dp.toPx(),
                                center = Offset(cx, cy)
                            )
                        }
                    }

                    // Callout bubbles floating above each tracked node.
                    Row(modifier = Modifier.fillMaxSize()) {
                        visibleMonths.forEach { point ->
                            val v = point.percent
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                if (v != null) {
                                    val isBest = point == bestMonth
                                    val isMin = point == lowestMonth
                                    val bubbleTop = trendHeadroom +
                                        trendPlotHeight * (1f - v / 100f) - trendBubbleHeight
                                    Column(
                                        modifier = Modifier.offset(y = bubbleTop - 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = if (isBest) "\uD83D\uDC51" else " ",
                                            fontSize = 11.5.sp,
                                            lineHeight = 13.sp,
                                            maxLines = 1,
                                            modifier = Modifier.height(12.dp)
                                        )
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.94f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isMin) Brush.verticalGradient(
                                                        listOf(Color(0xFFFF6B6B), Color(0xFFE8453C))
                                                    ) else Brush.verticalGradient(
                                                        listOf(Color(0xFF1FC57A), Color(0xFF0E9F6E))
                                                    )
                                                )
                                                .border(
                                                    1.dp,
                                                    Color.White.copy(alpha = 0.7f),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .padding(vertical = 1.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "$v%",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.White,
                                                lineHeight = 11.5.sp,
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                            Text(
                                                text = "(${point.attendedDays}d)",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White.copy(alpha = 0.92f),
                                                lineHeight = 10.sp,
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.8.dp)
                        .background(Color(0xFFD9DCEC))
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    visibleMonths.forEach { point ->
                        val isCurrent = point.label == currentLabel
                        // Untracked months fade back so the blank stretch in the curve
                        // reads as "no record" rather than "nothing happened".
                        val isUntracked = point.percent == null
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = point.label,
                                fontSize = 11.5.sp,
                                fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                                color = when {
                                    isCurrent -> Color(0xFF6D28D9)
                                    isUntracked -> Color(0xFFB4BECF)
                                    else -> AttendanceBlueprint.Muted
                                },
                                lineHeight = 11.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        }

        // Four KPI pods
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 3.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TrendKpiPod(
                emoji = "\uD83C\uDFC6",
                label = "Best",
                value = "$maxPercent%",
                suffix = "(${bestMonth?.label ?: "--"})",
                accent = Color(0xFF0B8A61),
                background = Color(0xFFEFFBF4),
                border = Color(0xFFBBF7D0),
                modifier = Modifier.weight(1f)
            )
            TrendKpiPod(
                emoji = "\uD83D\uDCCA",
                label = "Average",
                value = "$averagePercent%",
                suffix = "",
                accent = Color(0xFFB45309),
                background = Color(0xFFFFFBEB),
                border = Color(0xFFFDE68A),
                modifier = Modifier.weight(1f)
            )
            TrendKpiPod(
                emoji = "\u2B07",
                label = "Min",
                value = "$minPercent%",
                suffix = "(${lowestMonth?.label ?: "--"})",
                accent = Color(0xFFDC2626),
                background = Color(0xFFFFF5F5),
                border = Color(0xFFFECACA),
                modifier = Modifier.weight(1f)
            )
            TrendKpiPod(
                emoji = "\uD83D\uDCC5",
                label = "Tracked",
                value = "${activeMonths.size} / ${visibleMonths.size}",
                suffix = "mo",
                accent = Color(0xFF5B3FD9),
                background = Color(0xFFF6F4FF),
                border = Color(0xFFDCD5FB),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TrendCardHeader(
    caption: String,
    rangeMonths: Int,
    onRangeChange: (Int) -> Unit
) {
    var dropdownOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .padding(horizontal = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 3D bar-chart squircle (purple)
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9)))
                )
                .border(1.dp, Color(0xFF7C48E8), RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(1.6.dp),
                modifier = Modifier.height(15.dp)
            ) {
                Box(
                    Modifier
                        .width(3.4.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(1.4.dp))
                        .background(Color.White)
                )
                Box(
                    Modifier
                        .width(3.4.dp)
                        .height(15.dp)
                        .clip(RoundedCornerShape(1.4.dp))
                        .background(Color.White)
                )
                Box(
                    Modifier
                        .width(3.4.dp)
                        .height(11.dp)
                        .clip(RoundedCornerShape(1.4.dp))
                        .background(Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Attendance Trend",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = AttendanceBlueprint.Ink,
                letterSpacing = (-0.3).sp,
                lineHeight = 15.9.sp,
                maxLines = 1,
                softWrap = false
            )
            Text(
                text = caption,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = AttendanceBlueprint.Muted,
                lineHeight = 11.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFFFFFFF))
                    .border(1.dp, Color(0xFFDCD5F3), RoundedCornerShape(50.dp))
                    .clickable { dropdownOpen = true }
                    .padding(horizontal = 7.dp, vertical = 3.5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF6D28D9),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "$rangeMonths Months",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = AttendanceBlueprint.Ink,
                    lineHeight = 12.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Change trend range",
                    tint = Color(0xFF33514A),
                    modifier = Modifier.size(11.dp)
                )
            }
            DropdownMenu(
                expanded = dropdownOpen,
                onDismissRequest = { dropdownOpen = false }
            ) {
                DropdownMenuItem(
                    text = { Text("6 months", fontSize = 14.sp) },
                    onClick = {
                        onRangeChange(6)
                        dropdownOpen = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("12 months", fontSize = 14.sp) },
                    onClick = {
                        onRangeChange(12)
                        dropdownOpen = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TrendKpiPod(
    emoji: String,
    label: String,
    value: String,
    suffix: String,
    accent: Color,
    background: Color,
    border: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .padding(horizontal = 5.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(text = emoji, fontSize = 13.sp)
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AttendanceBlueprint.Muted,
                lineHeight = 11.sp,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = accent,
                    lineHeight = 15.sp,
                    maxLines = 1,
                    softWrap = false
                )
                if (suffix.isNotEmpty()) {
                    Text(
                        text = suffix,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accent.copy(alpha = 0.85f),
                        lineHeight = 11.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}


// =============================================================================
// COMPONENT 8 — BOTTOM MOTIVATION BANNER
// =============================================================================

@Composable
fun BottomMotivationBanner(
    isAchievement: Boolean = false,
    onKeepGoingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(20.dp))
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_footer_dumbbell),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(34.dp)
                )
                Column {
                    Text(
                        text = if (isAchievement) "LATEST ACHIEVEMENT" else "DISCIPLINE TODAY",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFDE047),
                        letterSpacing = 0.8.sp,
                        lineHeight = 12.sp
                    )
                    Text(
                        text = if (isAchievement) "New Personal Best" else "Consistent Steps, Brighter Days",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        lineHeight = 15.9.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFFACC15))
                    .clickable { onKeepGoingClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Keep Going!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "\u2794",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}
