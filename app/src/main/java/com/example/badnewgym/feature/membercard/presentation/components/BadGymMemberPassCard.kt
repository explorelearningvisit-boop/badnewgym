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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.BadGymPassTier

/**
 * BAD GYM — Canonical Member Pass Card (V2 Specification)
 * 1:1 Pixel-Perfect reproduction of the 8 Canonical Passes:
 * 1. Spark (2 Months) - Green
 * 2. Power (3 Months) - Cyan/Blue
 * 3. Silver (6 Months) - Silver Metallic
 * 4. Gold (12 Months) - Warm Gold
 * 5. Diamond (18 Months) - Sky Diamond Blue
 * 6. Platinum (24 Months) - Amethyst Purple
 * 7. Elite (36 Months) - Dark Luxury VIP
 * 8. Basic (1 Month) - Coral Red Overdue
 */
@Composable
fun BadGymMemberPassCard(
    pass: BadGymMemberPass,
    onAvatarClick: () -> Unit = {},
    onChevronClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tier = pass.tier
    val isElite = tier == BadGymPassTier.ELITE
    val isOverdue = pass.isOverdue

    // Palette Definition
    val cardBackground = when (tier) {
        BadGymPassTier.SPARK -> Brush.verticalGradient(
            listOf(Color(0xFFE8FDF0), Color(0xFFDCFCE7), Color(0xFFF0FDF4))
        )
        BadGymPassTier.POWER -> Brush.verticalGradient(
            listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD), Color(0xFFF0F9FF))
        )
        BadGymPassTier.SILVER -> Brush.verticalGradient(
            listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0), Color(0xFFF1F5F9))
        )
        BadGymPassTier.GOLD -> Brush.verticalGradient(
            listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7), Color(0xFFFDE68A).copy(alpha = 0.7f))
        )
        BadGymPassTier.DIAMOND -> Brush.verticalGradient(
            listOf(Color(0xFFF0F9FF), Color(0xFFBAE6FD), Color(0xFFE0F2FE))
        )
        BadGymPassTier.PLATINUM -> Brush.verticalGradient(
            listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF), Color(0xFFE9D5FF))
        )
        BadGymPassTier.ELITE -> Brush.verticalGradient(
            listOf(Color(0xFF26211C), Color(0xFF181512), Color(0xFF0F0D0C))
        )
        BadGymPassTier.BASIC -> Brush.verticalGradient(
            listOf(Color(0xFFFFF1F2), Color(0xFFFFE4E6), Color(0xFFFECDD3).copy(alpha = 0.5f))
        )
    }

    val cardBorderColor = when (tier) {
        BadGymPassTier.SPARK -> Color(0xFF86EFAC)
        BadGymPassTier.POWER -> Color(0xFF7DD3FC)
        BadGymPassTier.SILVER -> Color(0xFFCBD5E1)
        BadGymPassTier.GOLD -> Color(0xFFFCD34D)
        BadGymPassTier.DIAMOND -> Color(0xFF38BDF8)
        BadGymPassTier.PLATINUM -> Color(0xFFC084FC)
        BadGymPassTier.ELITE -> Color(0xFFEAB308)
        BadGymPassTier.BASIC -> Color(0xFFFDA4AF)
    }

    val textPrimary = if (isElite) Color.White else Color(0xFF0F172A)
    val textSubtitle = if (isElite) Color(0xFFD4D4D8) else Color(0xFF64748B)

    val mottoColor = when (tier) {
        BadGymPassTier.SPARK -> Color(0xFF15803D)
        BadGymPassTier.POWER -> Color(0xFF0369A1)
        BadGymPassTier.SILVER -> Color(0xFF475569)
        BadGymPassTier.GOLD -> Color(0xFFB45309)
        BadGymPassTier.DIAMOND -> Color(0xFF0284C7)
        BadGymPassTier.PLATINUM -> Color(0xFF7E22CE)
        BadGymPassTier.ELITE -> Color(0xFFFDE047)
        BadGymPassTier.BASIC -> Color(0xFFBE123C)
    }

    val haloColors = when (tier) {
        BadGymPassTier.SPARK -> listOf(Color(0xFF10B981), Color(0xFF34D399), Color(0xFF059669), Color(0xFF10B981))
        BadGymPassTier.POWER -> listOf(Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFF0369A1), Color(0xFF0284C7))
        BadGymPassTier.SILVER -> listOf(Color(0xFF94A3B8), Color(0xFFCBD5E1), Color(0xFF64748B), Color(0xFF94A3B8))
        BadGymPassTier.GOLD -> listOf(Color(0xFFF59E0B), Color(0xFFFDE047), Color(0xFFD97706), Color(0xFFF59E0B))
        BadGymPassTier.DIAMOND -> listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF67E8F9), Color(0xFF38BDF8))
        BadGymPassTier.PLATINUM -> listOf(Color(0xFFA855F7), Color(0xFFC084FC), Color(0xFF7E22CE), Color(0xFFA855F7))
        BadGymPassTier.ELITE -> listOf(Color(0xFFFACC15), Color(0xFFCA8A04), Color(0xFFEAB308), Color(0xFFFACC15))
        BadGymPassTier.BASIC -> listOf(Color(0xFFF43F5E), Color(0xFFFDA4AF), Color(0xFFE11D48), Color(0xFFF43F5E))
    }

    val progressBrush = when (tier) {
        BadGymPassTier.SPARK -> Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF059669)))
        BadGymPassTier.POWER -> Brush.horizontalGradient(listOf(Color(0xFF0284C7), Color(0xFF00A3FF)))
        BadGymPassTier.SILVER -> Brush.horizontalGradient(listOf(Color(0xFF64748B), Color(0xFF475569)))
        BadGymPassTier.GOLD -> Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFD97706)))
        BadGymPassTier.DIAMOND -> Brush.horizontalGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1)))
        BadGymPassTier.PLATINUM -> Brush.horizontalGradient(listOf(Color(0xFF9333EA), Color(0xFF7E22CE)))
        BadGymPassTier.ELITE -> Brush.horizontalGradient(listOf(Color(0xFFEAB308), Color(0xFFB45309)))
        BadGymPassTier.BASIC -> Brush.horizontalGradient(listOf(Color(0xFFFDA4AF), Color(0xFFF43F5E)))
    }

    val statCardBg = if (isElite) Color(0xFF221E19) else Color.White.copy(alpha = 0.88f)
    val statCardBorder = if (isElite) Color(0xFF523E15) else cardBorderColor.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(cardBackground)
            .border(1.2.dp, cardBorderColor, RoundedCornerShape(22.dp))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // =========================================================================
            // ROW 1: HEADER (Emblem + BAD GYM + MEMBER PASS & Motto)
            // =========================================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Emblem + Brand Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tier Emblem
                    TierHeaderEmblem(tier = tier)

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "BAD ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isElite) Color(0xFFFACC15) else Color(0xFF0F172A)
                            )
                            Text(
                                text = "GYM",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isElite) Color.White else Color(0xFF0F172A)
                            )
                        }
                        Text(
                            text = "MEMBER PASS",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.6.sp,
                            color = if (isElite) Color(0xFFCA8A04) else Color(0xFF64748B)
                        )
                    }
                }

                // Right: Tier Motto
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = tier.mottoLine1,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black,
                        color = mottoColor,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = tier.mottoLine2,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = mottoColor.copy(alpha = 0.9f),
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.End
                    )
                }
            }

            // =========================================================================
            // ROW 2: MEMBER PROFILE & TIERS
            // =========================================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Maximized Member Avatar with Halo Ring & Corner Badge
                Box(
                    modifier = Modifier.size(92.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(92.dp)) {
                        drawCircle(
                            brush = Brush.sweepGradient(haloColors),
                            radius = size.minDimension / 2f - 2f,
                            style = Stroke(width = 3.5.dp.toPx())
                        )
                    }

                    Image(
                        painter = painterResource(id = pass.avatarRes),
                        contentDescription = pass.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.8.dp, if (isElite) Color(0xFFEAB308) else Color.White, CircleShape)
                            .clickable { onAvatarClick() }
                    )

                    // Floating Corner Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 1.dp, y = 1.dp)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isOverdue -> Color(0xFFEF4444)
                                    isElite -> Color(0xFFEAB308)
                                    tier == BadGymPassTier.POWER -> Color(0xFF0284C7)
                                    tier == BadGymPassTier.DIAMOND -> Color(0xFF0284C7)
                                    tier == BadGymPassTier.PLATINUM -> Color(0xFF9333EA)
                                    tier == BadGymPassTier.GOLD -> Color(0xFFF59E0B)
                                    tier == BadGymPassTier.SILVER -> Color(0xFF64748B)
                                    else -> Color(0xFF10B981)
                                }
                            )
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when {
                                isOverdue -> "!"
                                isElite -> "👑"
                                tier == BadGymPassTier.GOLD -> "👑"
                                tier == BadGymPassTier.SILVER -> "★"
                                tier == BadGymPassTier.PLATINUM -> "★"
                                tier == BadGymPassTier.DIAMOND -> "💎"
                                tier == BadGymPassTier.POWER -> "⚡"
                                else -> "✓"
                            },
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // VIP Ribbon (for Elite)
                    if (pass.isVip) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .offset(x = (-3).dp, y = 1.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFEAB308))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "VIP",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }

                // Member Details Column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(2.5.dp)
                ) {
                    Text(
                        text = pass.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = textPrimary,
                        maxLines = 1
                    )

                    Text(
                        text = "${pass.memberId}  |  ${pass.age} yrs  |  ${pass.gender}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textSubtitle,
                        maxLines = 1
                    )

                    // Personal Quote
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "\u201C${pass.quote}\u201D",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isElite) Color(0xFFFDE047) else Color(0xFF1E293B),
                            maxLines = 1
                        )
                        if (pass.quoteEmoji.isNotBlank()) {
                            Text(text = pass.quoteEmoji, fontSize = 11.5.sp)
                        }
                    }

                    // Validity Dates
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = if (isElite) Color(0xFFEAB308) else Color(0xFF059669),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "${pass.startDate} – ${pass.endDate}",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textSubtitle
                        )
                    }
                }

                // Right Column: Tier Badge Pill & Status Pill
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Tier Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = when (tier) {
                            BadGymPassTier.ELITE -> Color(0xFF332A1D)
                            BadGymPassTier.BASIC -> Color(0xFFFFE4E6)
                            BadGymPassTier.GOLD -> Color(0xFFFEF3C7)
                            BadGymPassTier.POWER -> Color(0xFFE0F2FE)
                            BadGymPassTier.DIAMOND -> Color(0xFFE0F2FE)
                            BadGymPassTier.PLATINUM -> Color(0xFFF3E8FF)
                            BadGymPassTier.SILVER -> Color(0xFFF1F5F9)
                            else -> Color(0xFFDCFCE7)
                        },
                        border = BorderStroke(
                            0.8.dp,
                            when (tier) {
                                BadGymPassTier.ELITE -> Color(0xFFCA8A04)
                                BadGymPassTier.BASIC -> Color(0xFFFDA4AF)
                                BadGymPassTier.GOLD -> Color(0xFFFDE68A)
                                BadGymPassTier.POWER -> Color(0xFF7DD3FC)
                                BadGymPassTier.DIAMOND -> Color(0xFF38BDF8)
                                BadGymPassTier.PLATINUM -> Color(0xFFC084FC)
                                BadGymPassTier.SILVER -> Color(0xFFCBD5E1)
                                else -> Color(0xFF86EFAC)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = when (tier) {
                                    BadGymPassTier.SPARK -> "🌱"
                                    BadGymPassTier.POWER -> "⚡"
                                    BadGymPassTier.SILVER -> "⭐"
                                    BadGymPassTier.GOLD -> "👑"
                                    BadGymPassTier.DIAMOND -> "💎"
                                    BadGymPassTier.PLATINUM -> "👑"
                                    BadGymPassTier.ELITE -> "👑"
                                    BadGymPassTier.BASIC -> "⏱️"
                                },
                                fontSize = 11.sp
                            )
                            Column {
                                Text(
                                    text = tier.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isElite) Color(0xFFFDE047) else textPrimary
                                )
                                Text(
                                    text = tier.durationText,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textSubtitle
                                )
                            }
                        }
                    }

                    // Status Pill (Active vs Overdue)
                    if (isOverdue) {
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color(0xFFEF4444)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(13.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PriorityHigh,
                                        contentDescription = null,
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(9.dp)
                                    )
                                }
                                Text(
                                    text = "Overdue ${pass.overdueDays} Days",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color(0xFF10B981)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(13.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(9.dp)
                                    )
                                }
                                Text(
                                    text = "Active",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // ROW 3: PROGRESS BAR (Days Left Capsule)
            // =========================================================================
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = if (isElite) Color(0xFF2B251F) else Color.White.copy(alpha = 0.7f),
                border = BorderStroke(1.dp, if (isElite) Color(0xFF856218) else cardBorderColor.copy(alpha = 0.6f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChevronClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(26.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(progressBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isOverdue) {
                                "0 / ${pass.totalDays} Days (${pass.overdueDays} Days Overdue)"
                            } else {
                                "${pass.daysLeft} / ${pass.totalDays} Days Left"
                            },
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Circular Chevron Button
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = if (isElite) 0.15f else 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = if (isElite) Color(0xFFFDE047) else textPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // =========================================================================
            // ROW 4: UNIFIED COHESIVE BIOMETRIC & GOAL DOCK
            // =========================================================================
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = statCardBg,
                border = BorderStroke(1.dp, statCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Height Column
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text("📏", fontSize = 11.sp)
                            Text(
                                text = pass.height,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isElite) Color.White else Color(0xFF0F172A),
                                maxLines = 1
                            )
                        }
                        Text(
                            text = "Height",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isElite) Color(0xFFCA8A04) else Color(0xFF64748B),
                            maxLines = 1
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(26.dp)
                            .background(statCardBorder.copy(alpha = 0.6f))
                    )

                    // Weight Column
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text("⚖️", fontSize = 11.sp)
                            Text(
                                text = pass.weight,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isElite) Color.White else Color(0xFF0F172A),
                                maxLines = 1
                            )
                        }
                        Text(
                            text = "Weight",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isElite) Color(0xFFCA8A04) else Color(0xFF64748B),
                            maxLines = 1
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(26.dp)
                            .background(statCardBorder.copy(alpha = 0.6f))
                    )

                    // BMI Column
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text("📊", fontSize = 11.sp)
                            Text(
                                text = pass.bmi,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isElite) Color.White else Color(0xFF0F172A),
                                maxLines = 1
                            )
                        }
                        Text(
                            text = "BMI",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isElite) Color(0xFFCA8A04) else Color(0xFF64748B),
                            maxLines = 1
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(26.dp)
                            .background(statCardBorder.copy(alpha = 0.6f))
                    )

                    // Goal Column
                    Column(
                        modifier = Modifier.weight(1.25f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = when {
                                    pass.goal.contains("Muscle", ignoreCase = true) -> "💪"
                                    pass.goal.contains("Strength", ignoreCase = true) -> "🏋️"
                                    pass.goal.contains("Fat", ignoreCase = true) -> "🔥"
                                    pass.goal.contains("Toning", ignoreCase = true) -> "🦾"
                                    else -> "🎯"
                                },
                                fontSize = 11.sp
                            )
                            Text(
                                text = pass.goal,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isElite) Color.White else Color(0xFF0F172A),
                                maxLines = 1
                            )
                        }
                        Text(
                            text = "Goal",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isElite) Color(0xFFCA8A04) else Color(0xFF64748B),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TierHeaderEmblem(tier: BadGymPassTier) {
    val isElite = tier == BadGymPassTier.ELITE

    val (bgBrush, iconText) = when (tier) {
        BadGymPassTier.SPARK -> Brush.linearGradient(listOf(Color(0xFF34D399), Color(0xFF059669))) to "🌱"
        BadGymPassTier.POWER -> Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7))) to "⚡"
        BadGymPassTier.SILVER -> Brush.linearGradient(listOf(Color(0xFFCBD5E1), Color(0xFF64748B))) to "⭐"
        BadGymPassTier.GOLD -> Brush.linearGradient(listOf(Color(0xFFFDE047), Color(0xFFD97706))) to "👑"
        BadGymPassTier.DIAMOND -> Brush.linearGradient(listOf(Color(0xFF7DD3FC), Color(0xFF0284C7))) to "💎"
        BadGymPassTier.PLATINUM -> Brush.linearGradient(listOf(Color(0xFFC084FC), Color(0xFF7E22CE))) to "👑"
        BadGymPassTier.ELITE -> Brush.linearGradient(listOf(Color(0xFFFDE047), Color(0xFFCA8A04))) to "⚜️"
        BadGymPassTier.BASIC -> Brush.linearGradient(listOf(Color(0xFFFDA4AF), Color(0xFFE11D48))) to "⏱️"
    }

    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(bgBrush)
            .border(1.dp, if (isElite) Color(0xFFFDE047) else Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = iconText, fontSize = 14.sp)
    }
}

@Composable
private fun PassMetricChip(
    label: String,
    value: String,
    iconText: String,
    isElite: Boolean,
    backgroundColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(0.8.dp, borderColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = iconText, fontSize = 9.sp)
                Text(
                    text = value,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isElite) Color.White else Color(0xFF0F172A),
                    maxLines = 1
                )
            }
            Text(
                text = label,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = if (isElite) Color(0xFFCA8A04) else Color(0xFF64748B),
                maxLines = 1
            )
        }
    }
}

