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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Straighten
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
import androidx.compose.ui.graphics.SolidColor
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
    // A 1.5dp lit edge is what makes each card read as its own framed panel once
    // the cards sit tight against each other.
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
                .border(1.5.dp, edge, shape)
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
    planTitle: String = "GOLD",
    checkInTime: String = "Today • 08:49 AM",
    isActive: Boolean = true,
    cardType: MemberCardType = MemberCardType.CHECK_IN,
    paymentStatus: PaymentStatus? = null,
    paidAmount: String = "₹2,800",
    paidDate: String = "03 Nov 2026",
    isPaid: Boolean = true,
    accent: Color = MemberCardAccent.EMERALD.color,
    onCheckInClick: () -> Unit = {},
    onPaymentHeaderClick: () -> Unit = {},
    onPlanClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Golden Crown + BAD GYM + Member Intelligence + Slogan
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(text = "👑", fontSize = 23.sp)
            Column(verticalArrangement = Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "BAD ",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-0.3).sp,
                        lineHeight = 17.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        text = "GYM",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFDE047),
                        letterSpacing = 0.2.sp,
                        lineHeight = 17.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Text(
                    text = "MEMBER INTELLIGENCE",
                    fontSize = 8.8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.95f),
                    letterSpacing = 0.4.sp,
                    lineHeight = 9.8.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = "DISCIPLINE BUILDS FREEDOM",
                    fontSize = 7.2.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFBCFE8),
                    letterSpacing = 0.4.sp,
                    lineHeight = 8.2.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        // Center: Cyber-Neon Frosted "MEMBER PASS" Stadium Pill
        Surface(
            onClick = onCheckInClick,
            shape = RoundedCornerShape(50.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, Color(0xFFC084FC).copy(alpha = 0.85f)),
            shadowElevation = 4.dp,
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF7E22CE).copy(alpha = 0.45f), Color(0xFF581C87).copy(alpha = 0.65f))
                    )
                )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC084FC).copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Member Pass",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "MEMBER PASS",
                        fontSize = 9.8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.3.sp,
                        lineHeight = 10.8.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = checkInTime.ifBlank { "Today • 08:49 AM" },
                            fontSize = 7.8.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.95f),
                            lineHeight = 8.8.sp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "›",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Right: Golden Trophy Capsule (₹1,200 / 16 Oct 2026)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Surface(
                onClick = onPaymentHeaderClick,
                shape = RoundedCornerShape(50.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFFDE047).copy(alpha = 0.9f)),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFFFEF08A), Color(0xFFFACC15))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Due Trophy",
                            tint = Color(0xFFCA8A04),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = paidAmount.ifBlank { "₹1,200" },
                                fontSize = 11.2.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFDC2626),
                                lineHeight = 11.8.sp
                            )
                            Spacer(modifier = Modifier.width(1.dp))
                            Text(
                                text = "›",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        Text(
                            text = paidDate.ifBlank { "16 Oct 2026" },
                            fontSize = 7.2.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
                            lineHeight = 8.sp
                        )
                    }
                }
            }

            // Golden Bodybuilder silhouette
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0x99F59E0B), Color(0x33F59E0B), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_character_flex),
                    contentDescription = "Bad Gym Champion",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(28.dp)
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
    quote: String = "“Discipline Builds Freedom”",
    validity: String = "09 Oct 2026 – 07 Nov 2026 (30 Days Plan)",
    avatarRes: Int = R.drawable.avatar_ashwini,
    planName: String = "Gold Plan",
    planAmount: String = "₹1,200",
    planDays: Int = 30,
    autoRenew: Boolean = true,
    accent: Color = MemberCardAccent.EMERALD.color,
    onAvatarClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 9.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            // Avatar with gold border and 👑 crown badge
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp,
                            Brush.linearGradient(listOf(Color(0xFFFACC15), Color(0xFFEAB308))),
                            CircleShape
                        )
                        .clickable { onAvatarClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
                // Crown badge at bottom right
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 1.dp, y = 1.dp)
                        .size(17.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF08A))
                        .border(1.dp, Color(0xFFCA8A04), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👑", fontSize = 9.sp)
                }
            }

            // Name + Meta + Quote
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    letterSpacing = (-0.3).sp,
                    lineHeight = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$memberId  •  $age yrs  •  $gender",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B),
                    lineHeight = 11.sp,
                    maxLines = 1
                )
                Text(
                    text = quote,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF475569),
                    lineHeight = 10.sp,
                    maxLines = 1
                )
            }

            // Top Right: 3-dots and Active pill
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onMenuClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(17.dp)
                    )
                }
                // Active status pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFDCFCE7))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF16A34A))
                    )
                    Text(
                        text = "Active",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D),
                        lineHeight = 9.5.sp
                    )
                }
            }
        }

        // Validity row with calendar icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, top = 1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = validity,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155),
                lineHeight = 11.sp
            )
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
// COMPONENT 3 — BIOMETRIC BAND (Height · Weight · BMI · Goal 4 Micro-Cards)
// =============================================================================

@Composable
fun StatsRowCompact(
    height: String = "5'9\"",
    weight: String = "69.5 kg",
    weightDelta: String? = "↓ 2.5 kg",
    bmi: String = "22.8",
    bmiCategory: String = "Normal",
    goal: String = "Fat Loss",
    goalSubtitle: String = "Goal",
    accent: Color = MemberCardAccent.EMERALD.color,
    onGoalClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 7.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Height Pod (Cyan)
        StatPodItem(
            bgBrush = Brush.verticalGradient(listOf(Color(0xFFE0F2FE), Color(0xFFF0F9FF))),
            borderColor = Color(0xFFBAE6FD),
            iconContent = {
                Box(
                    modifier = Modifier
                        .size(23.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0284C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = "Height",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            },
            title = "Height",
            value = height,
            subValue = null,
            badge = null,
            modifier = Modifier.weight(1f)
        )

        // 2. Weight Pod (Lavender)
        StatPodItem(
            bgBrush = Brush.verticalGradient(listOf(Color(0xFFEDE9FE), Color(0xFFF5F3FF))),
            borderColor = Color(0xFFDDD6FE),
            iconContent = {
                Box(
                    modifier = Modifier
                        .size(23.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF7C3AED)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.illustration_3d_body_scale),
                        contentDescription = "Scale",
                        modifier = Modifier.size(15.dp)
                    )
                }
            },
            title = null,
            value = weight,
            subValue = weightDelta ?: "↓ 2.5 kg",
            subValueColor = Color(0xFF16A34A),
            badge = null,
            modifier = Modifier.weight(1.15f)
        )

        // 3. BMI Pod (Warm Amber)
        StatPodItem(
            bgBrush = Brush.verticalGradient(listOf(Color(0xFFFEF3C7), Color(0xFFFFFBEB))),
            borderColor = Color(0xFFFDE68A),
            iconContent = {
                Row(
                    modifier = Modifier.height(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(modifier = Modifier.width(3.dp).height(7.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF38BDF8)))
                    Box(modifier = Modifier.width(3.dp).height(12.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFFF59E0B)))
                    Box(modifier = Modifier.width(3.dp).height(16.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF10B981)))
                }
            },
            title = null,
            value = bmi,
            subValue = null,
            badge = "Normal",
            badgeBg = Color(0xFFDCFCE7),
            badgeColor = Color(0xFF15803D),
            modifier = Modifier.weight(1.15f)
        )

        // 4. Goal Pod (Warm Peach)
        StatPodItem(
            bgBrush = Brush.verticalGradient(listOf(Color(0xFFFFEDD5), Color(0xFFFFF7ED))),
            borderColor = Color(0xFFFED7AA),
            iconContent = {
                Box(
                    modifier = Modifier
                        .size(23.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFF97316), Color(0xFFEA580C))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Goal",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            },
            title = "Goal",
            value = goal,
            subValue = null,
            badge = null,
            onClick = onGoalClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatPodItem(
    bgBrush: Brush,
    borderColor: Color,
    iconContent: @Composable () -> Unit,
    title: String?,
    value: String,
    subValue: String? = null,
    subValueColor: Color = Color(0xFF16A34A),
    badge: String? = null,
    badgeBg: Color = Color(0xFFDCFCE7),
    badgeColor: Color = Color(0xFF15803D),
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .background(bgBrush)
            .border(1.dp, borderColor, shape)
            .clickable { onClick() }
            .padding(horizontal = 4.5.dp, vertical = 4.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
    ) {
        iconContent()
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            if (title != null) {
                Text(
                    text = value,
                    fontSize = 11.2.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    lineHeight = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = title,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B),
                    lineHeight = 9.sp,
                    maxLines = 1
                )
            } else if (subValue != null) {
                Text(
                    text = value,
                    fontSize = 11.2.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    lineHeight = 12.sp,
                    maxLines = 1
                )
                Text(
                    text = subValue,
                    fontSize = 7.8.sp,
                    fontWeight = FontWeight.Bold,
                    color = subValueColor,
                    lineHeight = 8.8.sp,
                    maxLines = 1
                )
            } else if (badge != null) {
                Text(
                    text = value,
                    fontSize = 11.2.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    lineHeight = 12.sp,
                    maxLines = 1
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(badgeBg)
                        .padding(horizontal = 2.dp, vertical = 0.5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BMI ",
                        fontSize = 6.8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        lineHeight = 7.8.sp
                    )
                    Text(
                        text = badge,
                        fontSize = 6.8.sp,
                        fontWeight = FontWeight.Black,
                        color = badgeColor,
                        lineHeight = 7.8.sp
                    )
                }
            } else {
                Text(
                    text = value,
                    fontSize = 11.2.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    lineHeight = 12.sp,
                    maxLines = 1
                )
            }
        }
        Text(
            text = "›",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF94A3B8)
        )
    }
}

// =============================================================================
// COMPONENT 3B — MEMBERSHIP PROGRESS BAND
// =============================================================================

@Composable
fun FullWidthDaysProgressCard(
    daysPassed: Int = 22,
    totalDays: Int = 30,
    percentage: Int = 73,
    validity: String = "09 Oct 2026 – 07 Nov 2026 (30 Days Plan)",
    accent: Color = MemberCardAccent.EMERALD.color,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val safeTotal = totalDays.coerceAtLeast(1)
    val used = daysPassed.coerceIn(0, safeTotal)
    val daysLeft = safeTotal - used
    val fraction = (used.toFloat() / safeTotal.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick() }
            .padding(horizontal = 9.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Green square calendar box + "22 / 30 days used"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF059669)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Days",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$used / $safeTotal ",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        lineHeight = 13.5.sp
                    )
                    Text(
                        text = "days used",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B),
                        lineHeight = 12.sp
                    )
                }
            }

            // Right: "8 days left" + "73%" + Arrow disc
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$daysLeft days left",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B),
                    lineHeight = 11.sp
                )
                Text(
                    text = "$percentage%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    lineHeight = 15.sp
                )
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "›",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // Gradient progress bar: Yellow -> Orange -> Pink -> Purple
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFFF1F5F9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFACC15), // Yellow
                                Color(0xFFFB923C), // Orange
                                Color(0xFFEC4899), // Pink
                                Color(0xFFA855F7)  // Purple
                            )
                        )
                    )
            )
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
    paidAmount: String = "₹1,200",
    paidDate: String = "16 Oct 2026",
    name: String = "Ashwani Kumar Singh",
    memberId: String = "BG215",
    age: Int = 28,
    gender: String = "Male",
    quote: String = "Discipline Builds Freedom",
    validity: String = "09 Oct 2026 – 07 Nov 2026 (30 Days Plan)",
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
    showHeader: Boolean = false,
    onCheckInClick: () -> Unit = {},
    onPaymentHeaderClick: () -> Unit = {},
    onPlanClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onGoalClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val accent = MemberCardAccentManager.activeAccent.color

    GlassCard(
        modifier = modifier,
        corner = 26.dp,
        fill = Brush.verticalGradient(listOf(Color.White, Color.White)),
        edge = Brush.verticalGradient(listOf(Color.White, Color(0xFFE2E8F0))),
        shadow = 4.dp
    ) {
        if (showHeader) {
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
        }

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

        Spacer(modifier = Modifier.height(2.dp))

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

        Spacer(modifier = Modifier.height(2.dp))

        FullWidthDaysProgressCard(
            daysPassed = daysPassed,
            totalDays = totalDays,
            percentage = percentage,
            validity = validity,
            accent = accent,
            onClick = onProgressClick
        )
    }
}

// =============================================================================
// COMPONENT 5 — 4-TAB DOCK (4 Glossy 3D Colored Action Cards)
// =============================================================================

@Composable
fun MenuRow4Tabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    attendanceRate: String = "81%",
    workoutAvg: String = "67m",
    paymentDue: String = "₹2,800",
    bodyWeight: String = "69.5 kg",
    attendanceProgress: Float = 0.81f,
    workoutProgress: Float = 0.70f,
    paymentProgress: Float = 0.65f,
    bodyProgress: Float = 0.74f,
    paymentHasAlert: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        horizontalArrangement = Arrangement.spacedBy(4.5.dp)
    ) {
        // Tab 0: Attendance (Emerald Green Glossy)
        GlossyColoredTabItem(
            title = "Attendance",
            value = attendanceRate,
            iconRes = R.drawable.illustration_3d_attendance_calendar,
            gradientColors = listOf(Color(0xFF059669), Color(0xFF047857), Color(0xFF065F46)),
            isSelected = selectedTabIndex == 0,
            hasAlert = false,
            onClick = { onTabSelected(0) },
            modifier = Modifier.weight(1f)
        )

        // Tab 1: Workout (Violet/Purple Glossy)
        GlossyColoredTabItem(
            title = "Workout",
            value = workoutAvg,
            iconRes = R.drawable.illustration_3d_dumbbell_mini,
            gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED), Color(0xFF6D28D9)),
            isSelected = selectedTabIndex == 1,
            hasAlert = false,
            onClick = { onTabSelected(1) },
            modifier = Modifier.weight(1f)
        )

        // Tab 2: Payment (Fiery Orange/Red Glossy with !)
        GlossyColoredTabItem(
            title = "Payment",
            value = paymentDue,
            iconRes = R.drawable.illustration_3d_payment_wallet,
            gradientColors = listOf(Color(0xFFF97316), Color(0xFFEA580C), Color(0xFFDC2626)),
            isSelected = selectedTabIndex == 2,
            hasAlert = paymentHasAlert,
            onClick = { onTabSelected(2) },
            modifier = Modifier.weight(1f)
        )

        // Tab 3: Body (Electric Cyan/Blue Glossy)
        GlossyColoredTabItem(
            title = "Body",
            value = bodyWeight,
            iconRes = R.drawable.ic_body_progress_header_badge,
            gradientColors = listOf(Color(0xFF0284C7), Color(0xFF0369A1), Color(0xFF1D4ED8)),
            isSelected = selectedTabIndex == 3,
            hasAlert = false,
            onClick = { onTabSelected(3) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GlossyColoredTabItem(
    title: String,
    value: String,
    iconRes: Int,
    gradientColors: List<Color>,
    isSelected: Boolean,
    hasAlert: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTheme = com.example.badnewgym.feature.membercard.design.BadGymThemeManager.activeTheme
    val shape = RoundedCornerShape(13.dp)
    val fill = if (isSelected) {
        activeTheme.getActiveTabBrush()
    } else {
        Brush.verticalGradient(listOf(Color.White, Color.White))
    }
    val borderColor = if (isSelected) activeTheme.getActiveTabBorder() else Color(0xFFE2E8F0)
    val activeTextColor = activeTheme.getActiveTabTextColor()
    val titleColor = if (isSelected) activeTextColor.copy(alpha = 0.85f) else Color(0xFF64748B)
    val valueColor = if (isSelected) activeTextColor else Color(0xFF0F172A)
    val chevronColor = if (isSelected) activeTextColor else Color(0xFF94A3B8)
    val iconBoxBg = if (isSelected) {
        if (activeTextColor == Color.White) Color.White.copy(alpha = 0.20f) else Color.Black.copy(alpha = 0.08f)
    } else Color(0xFFF1F5F9)

    Surface(
        onClick = onClick,
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            borderColor
        ),
        shadowElevation = if (isSelected) 4.dp else 1.dp,
        modifier = modifier
            .fillMaxHeight()
            .clip(shape)
            .background(fill)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Icon box
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(iconBoxBg)
                    .border(0.5.dp, if (isSelected) Color(0xFFCA8A04).copy(alpha = 0.3f) else Color(0xFFE2E8F0), RoundedCornerShape(7.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(16.dp)
                )
                if (hasAlert) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDC2626))
                            .border(1.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            fontSize = 6.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }

            // Text: Value (big bold) + Title
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = value,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = valueColor,
                        lineHeight = 12.5.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "›",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = chevronColor
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        lineHeight = 9.sp,
                        maxLines = 1
                    )
                    if (hasAlert) {
                        Spacer(modifier = Modifier.width(1.5.dp))
                        Text(
                            text = "!",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }
        }
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
        fill = Brush.verticalGradient(listOf(Color.White, Color.White)),
        edge = Brush.verticalGradient(listOf(Color.White, Color(0xFFE2E8F0))),
        onClick = onAttendanceClick
    ) {
        AttendanceCardHeader(
            monthLabel = activeRecord?.fullLabel ?: attendance.monthLabel,
            canPrev = monthIndex > 0,
            canNext = monthIndex < records.lastIndex,
            onPrev = { if (monthIndex > 0) monthIndex -= 1 },
            onNext = { if (monthIndex < records.lastIndex) monthIndex += 1 }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
        ) {
            // Left Column: Donut Progress Gauge + Stats + Legend
            Column(
                modifier = Modifier
                    .width(108.dp)
                    .padding(end = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Circular Donut Progress Ring
                Box(
                    modifier = Modifier.size(88.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 9.dp.toPx()
                        val diameter = size.minDimension - strokeWidth
                        val arcSize = Size(diameter, diameter)
                        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                        // Track
                        drawArc(
                            color = Color(0xFFCCFBF1),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        // Progress arc
                        val sweep = ((shownRate ?: 81) / 100f) * 360f
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(Color(0xFFFACC15), Color(0xFF10B981), Color(0xFF059669))
                            ),
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
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
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = shownRate?.let { "$it%" } ?: "81%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            letterSpacing = (-0.5).sp,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = "Attendance Rate",
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            lineHeight = 8.sp
                        )
                    }
                }

                // Days count
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (shownTotal > 0) "$shownAttended / $shownTotal days" else "25 / 31 days",
                        fontSize = 11.2.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        lineHeight = 12.sp
                    )
                    Text(
                        text = "${holidays + sundays} holidays",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF94A3B8),
                        lineHeight = 9.sp
                    )
                }

                // Legend
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    AttendanceLegendRow(Color(0xFF059669), "Present", present)
                    AttendanceLegendRow(Color(0xFFF59E0B), "Late", late)
                    AttendanceLegendRow(Color(0xFFEF4444), "Absent", absent)
                    AttendanceLegendRow(Color(0xFF94A3B8), "Holiday", holidays + sundays)
                }
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .width(1.dp)
                    .height(150.dp)
                    .background(Color(0xFFF1F5F9))
            )

            // Right Column: Calendar Day Grid
            Column(modifier = Modifier.weight(1f)) {
                if (showingCurrent) {
                    WeekdayHeaderRow()
                    Spacer(modifier = Modifier.height(3.dp))
                    weeks.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            week.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(24.dp),
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
            .height(42.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFB7185), Color(0xFFE11D48), Color(0xFFBE123C))
                )
            )
            .padding(horizontal = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = Color(0xFFE11D48),
                modifier = Modifier.size(15.dp)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Attendance",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-0.2).sp,
                lineHeight = 14.sp,
                maxLines = 1
            )
            Text(
                text = "Daily check-ins • Mon – Sun",
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 9.5.sp,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        MyProteinPill()
        Spacer(modifier = Modifier.width(5.dp))
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
        isSunday -> Color(0xFFF1F5F9)
        day.status == DayAttendanceStatus.PRESENT -> Color(0xFF059669)
        day.status == DayAttendanceStatus.LATE -> Color(0xFFD97706)
        day.status == DayAttendanceStatus.ABSENT -> Color(0xFFDC2626)
        else -> Color(0xFFF1F5F9)
    }
    val foreground = if (isSunday) Color(0xFF64748B) else Color.White

    Box(
        modifier = Modifier
            .size(24.dp)
            .clickable { onDayClick(day) },
        contentAlignment = Alignment.Center
    ) {
        if (isToday) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(1.6.dp, Color(0xFF059669), CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .size(if (isToday) 20.dp else 21.dp)
                .clip(CircleShape)
                .background(fill),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isSunday) "-" else "${day.dayOfMonth}",
                fontSize = if (isSunday) 12.sp else 10.5.sp,
                fontWeight = FontWeight.Black,
                color = foreground,
                lineHeight = 11.sp,
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
                        if (badge == DayBadge.DUE) Color(0xFFDC2626)
                        else Color(0xFF059669)
                    )
                    .border(1.dp, Color.White, CircleShape)
            )
        }
    }
}

// =============================================================================
// COMPONENT 7 — MONTHLY TREND (12-Month Bar Chart with Benchmark Lines)
// =============================================================================

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
    val averagePercent = if (activeMonths.isEmpty()) 0 else activeMonths.sumOf { it.percent ?: 0 } / activeMonths.size
    val maxPercent = bestMonth?.percent ?: 97
    val minPercent = lowestMonth?.percent ?: 58

    GlassCard(
        modifier = modifier,
        corner = 20.dp,
        fill = Brush.verticalGradient(listOf(Color.White, Color.White)),
        edge = Brush.verticalGradient(listOf(Color.White, Color(0xFFE2E8F0)))
    ) {
        TrendCardHeader(
            caption = "Attendance rate by month",
            rangeMonths = rangeMonths,
            onRangeChange = { rangeMonths = it }
        )

        BandDivider()

        // Bar Chart Body
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
            ) {
                // Y-Axis labels (100, 50, 0)
                Column(
                    modifier = Modifier
                        .width(22.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = "100", fontSize = 8.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                    Text(text = "50", fontSize = 8.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                    Text(text = "0", fontSize = 8.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(5.dp))

                // Chart Area with dashed benchmark lines + bars
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // Benchmark lines canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        fun yFor(pct: Float) = size.height * (1f - (pct / 100f))
                        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 4.dp.toPx()), 0f)

                        // Max 97% line (teal)
                        drawLine(
                            color = Color(0xFF10B981),
                            start = Offset(0f, yFor(maxPercent.toFloat())),
                            end = Offset(size.width, yFor(maxPercent.toFloat())),
                            strokeWidth = 1.2.dp.toPx(),
                            pathEffect = dashEffect
                        )

                        // Avg 81% line (amber)
                        drawLine(
                            color = Color(0xFFF59E0B),
                            start = Offset(0f, yFor(averagePercent.toFloat())),
                            end = Offset(size.width, yFor(averagePercent.toFloat())),
                            strokeWidth = 1.2.dp.toPx(),
                            pathEffect = dashEffect
                        )

                        // Min 58% line (red)
                        drawLine(
                            color = Color(0xFFEF4444),
                            start = Offset(0f, yFor(minPercent.toFloat())),
                            end = Offset(size.width, yFor(minPercent.toFloat())),
                            strokeWidth = 1.2.dp.toPx(),
                            pathEffect = dashEffect
                        )
                    }

                    // 12 Bars Row
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        visibleMonths.forEach { point ->
                            val v = point.percent
                            val isCurrent = point.label == "Oct"
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                if (v != null) {
                                    Text(
                                        text = "$v",
                                        fontSize = 8.sp,
                                        fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                                        color = if (isCurrent) Color(0xFF065F46) else Color(0xFF64748B),
                                        lineHeight = 9.sp
                                    )
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(13.dp)
                                            .fillMaxHeight((v / 100f) * 0.78f)
                                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                            .background(
                                                if (isCurrent) SolidColor(Color(0xFF065F46))
                                                else Brush.verticalGradient(
                                                    listOf(Color(0xFF6EE7B7), Color(0xFF34D399), Color(0xFF10B981))
                                                )
                                            )
                                    )
                                } else {
                                    Text(
                                        text = "-",
                                        fontSize = 8.sp,
                                        color = Color(0xFF94A3B8),
                                        lineHeight = 9.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Month Labels Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 27.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                visibleMonths.forEach { point ->
                    val isCurrent = point.label == "Oct"
                    Text(
                        text = point.label,
                        fontSize = 8.5.sp,
                        fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Medium,
                        color = if (isCurrent) Color(0xFF065F46) else Color(0xFF64748B),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Benchmark Legend Row: -- Max 97%   -- Avg 81%   -- Min 58%
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(text = "---", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF10B981))
                    Text(text = "Max $maxPercent%", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(text = "---", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFF59E0B))
                    Text(text = "Avg $averagePercent%", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(text = "---", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFEF4444))
                    Text(text = "Min $minPercent%", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                }
            }
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
            .height(40.dp)
            .padding(horizontal = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Green bar chart squircle
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color(0xFFE6F4EA)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.BarChart,
                contentDescription = null,
                tint = Color(0xFF059669),
                modifier = Modifier.size(15.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Attendance trend",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                letterSpacing = (-0.2).sp,
                lineHeight = 14.sp,
                maxLines = 1
            )
            Text(
                text = caption,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B),
                lineHeight = 9.5.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BlinkitPill()

        Spacer(modifier = Modifier.width(5.dp))

        Box {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFFFFFFF))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
                    .clickable { dropdownOpen = true }
                    .padding(horizontal = 7.dp, vertical = 3.5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "$rangeMonths months",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155),
                    lineHeight = 10.sp,
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(12.dp)
                )
            }
            DropdownMenu(
                expanded = dropdownOpen,
                onDismissRequest = { dropdownOpen = false }
            ) {
                DropdownMenuItem(
                    text = { Text("6 months", fontSize = 13.sp) },
                    onClick = {
                        onRangeChange(6)
                        dropdownOpen = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("12 months", fontSize = 13.sp) },
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
