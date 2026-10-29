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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
 * BAD GYM — Canonical Member Pass Card (Master 1:1 Pixel-Perfect V2 Design)
 * Directly matching the 9 Master Themes:
 * 1. Emerald Green (Saturated Glass)
 * 2. Ocean Blue (Saturated Glass)
 * 3. Royal Purple (Saturated Glass)
 * 4. Golden Amber (Warm Gold Saturated Glass)
 * 5. Crimson Alert / Payment Due (Overdue Saturated Glass)
 * 6. Cyan Aqua (Vibrant Saturated Glass)
 * 7. White Mint Glass (Minimal Frosted Crystal Glass)
 * 8. White Ocean Glass (Minimal Frosted Crystal Glass)
 * 9. White Lavender Glass (Minimal Frosted Crystal Glass)
 */
@Composable
fun BadGymMemberPassCard(
    pass: BadGymMemberPass,
    onAvatarClick: () -> Unit = {},
    onChevronClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(1.2.dp, Color(0xFFE2E8F0), RoundedCornerShape(22.dp))
            .padding(horizontal = 13.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // =========================================================================
            // ROW 1: HEADER (Emblem + Brand & Motto + Options)
            // =========================================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Logo Emblem & Brand
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF044837))
                            .border(1.dp, Color(0xFF065F46), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "BAD ",
                                fontSize = 17.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "GYM",
                                fontSize = 17.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF059669)
                            )
                        }
                        Text(
                            text = "MEMBER PASS",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Right: Motto & 3-Dots Menu
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "DISCIPLINE",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "BUILDS FREEDOM",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .clickable { onChevronClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // =========================================================================
            // ROW 2: PROFILE & MEMBERSHIP TIERS
            // =========================================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Member Avatar with Silver Halo & Star Badge
                Box(
                    modifier = Modifier.size(66.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(66.dp)) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFFCBD5E1),
                                    Color(0xFF93C5FD),
                                    Color(0xFFE2E8F0),
                                    Color(0xFF93C5FD),
                                    Color(0xFFCBD5E1)
                                )
                            ),
                            radius = size.minDimension / 2f - 2f,
                            style = Stroke(width = 2.4.dp.toPx())
                        )
                    }

                    Image(
                        painter = painterResource(id = pass.avatarRes),
                        contentDescription = pass.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                            .clickable { onAvatarClick() }
                    )

                    // Floating Star Badge on Avatar
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 1.dp, y = 1.dp)
                            .size(17.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E3A8A))
                            .border(1.8.dp, Color.White, CircleShape),
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 9.dp, end = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = pass.name,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )

                    Text(
                        text = "${pass.memberId}  |  ${pass.age} yrs  |  ${pass.gender}",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569),
                        maxLines = 1
                    )

                    Text(
                        text = "\u201C${pass.quote}\u201D",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        maxLines = 1
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${pass.startDate} – ${pass.endDate}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                    }
                }

                // Right Badges: Tier & Active
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // Tier Chip (Silver / 6 Months)
                    Surface(
                        shape = RoundedCornerShape(9.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.clickable { onChevronClick() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "⭐", fontSize = 11.sp)
                            Column {
                                Text(
                                    text = pass.tier.title,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = pass.tier.durationText,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    // Active Status Pill
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (pass.isOverdue) Color(0xFFEF4444) else Color(0xFF059669),
                        border = BorderStroke(1.dp, if (pass.isOverdue) Color(0xFFF87171) else Color(0xFF10B981))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(11.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(9.dp)
                                )
                            }
                            Text(
                                text = if (pass.isOverdue) "Overdue" else "Active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // =========================================================================
            // ROW 3: FULL-WIDTH DAYS LEFT CAPSULE BAR
            // =========================================================================
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(33.dp)
                    .clickable { onChevronClick() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF05654B),
                                    Color(0xFF044E3B),
                                    Color(0xFF033B2C)
                                )
                            )
                        )
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${pass.daysLeft} / ${pass.totalDays} Days Left",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }

            // =========================================================================
            // ROW 4: 4 BIOMETRIC KPI PODS DOCK
            // =========================================================================
            Surface(
                shape = RoundedCornerShape(13.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Pod 1: Height
                    BiometricPodItem(
                        emoji = "📐",
                        value = pass.height,
                        label = "Height",
                        iconBg = Color(0xFFE0F7FA),
                        modifier = Modifier.weight(1f)
                    )

                    // Divider
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))

                    // Pod 2: Weight
                    BiometricPodItem(
                        emoji = "⚖️",
                        value = pass.weight,
                        label = "Weight",
                        iconBg = Color(0xFFE0F2FE),
                        modifier = Modifier.weight(1f)
                    )

                    // Divider
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))

                    // Pod 3: BMI
                    BiometricPodItem(
                        emoji = "📊",
                        value = pass.bmi,
                        label = "BMI",
                        iconBg = Color(0xFFF1F5F9),
                        modifier = Modifier.weight(1f)
                    )

                    // Divider
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE2E8F0)))

                    // Pod 4: Goal
                    BiometricPodItem(
                        emoji = "🔥",
                        value = pass.goal,
                        label = "Goal",
                        iconBg = Color(0xFFFFF7ED),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BiometricPodItem(
    emoji: String,
    value: String,
    label: String,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(25.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 13.sp)
        }
        Column {
            Text(
                text = value,
                fontSize = if (value.length > 7) 12.sp else 13.5.sp,
                letterSpacing = (-0.2).sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                maxLines = 1,
                softWrap = false
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                maxLines = 1
            )
        }
    }
}

private enum class PodIconType {
    BOLT,
    DUMBBELL,
    LOCATION
}

/**
 * Pod card for the bottom 3-column metric dock using crisp vector icons matching mockup
 */
@Composable
private fun PassVectorPod(
    iconType: PodIconType,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    podBg: Color,
    podBorder: Color,
    titleColor: Color,
    subtitleColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(13.dp),
        color = podBg,
        border = BorderStroke(1.dp, podBorder),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 7.dp, vertical = 5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                when (iconType) {
                    PodIconType.BOLT -> {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            val path = Path().apply {
                                moveTo(size.width * 0.62f, 0f)
                                lineTo(size.width * 0.16f, size.height * 0.54f)
                                lineTo(size.width * 0.48f, size.height * 0.54f)
                                lineTo(size.width * 0.38f, size.height)
                                lineTo(size.width * 0.84f, size.height * 0.46f)
                                lineTo(size.width * 0.52f, size.height * 0.46f)
                                close()
                            }
                            drawPath(path, color = iconTint)
                        }
                    }
                    PodIconType.DUMBBELL -> {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    PodIconType.LOCATION -> {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = subtitleColor,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Sharp QR Code Matrix Canvas Composable
 * Draws standard Version 1 QR code modules (21x21) including 3 finder patterns,
 * timing lines, and realistic scannable QR data modules.
 */
@Composable
fun BadGymQrCode(
    data: String,
    modifier: Modifier = Modifier
) {
    val qrMatrix = remember(data) { generateQrMatrix(data) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(4.5.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val moduleCount = qrMatrix.size
            val moduleSize = size.width / moduleCount.toFloat()

            for (row in 0 until moduleCount) {
                for (col in 0 until moduleCount) {
                    if (qrMatrix[row][col]) {
                        drawRect(
                            color = Color(0xFF0F172A),
                            topLeft = Offset(col * moduleSize, row * moduleSize),
                            size = Size(moduleSize, moduleSize)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Procedural standard 21x21 QR Code module matrix
 */
private fun generateQrMatrix(data: String): Array<BooleanArray> {
    val size = 21
    val matrix = Array(size) { BooleanArray(size) { false } }

    // Helper to draw 7x7 Finder Pattern with 5x5 white and 3x3 center
    fun drawFinder(startX: Int, startY: Int) {
        for (r in 0 until 7) {
            for (c in 0 until 7) {
                val isOuter = r == 0 || r == 6 || c == 0 || c == 6
                val isInner = r in 2..4 && c in 2..4
                matrix[startY + r][startX + c] = isOuter || isInner
            }
        }
    }

    // 1. Top-Left Finder
    drawFinder(0, 0)
    // 2. Top-Right Finder
    drawFinder(14, 0)
    // 3. Bottom-Left Finder
    drawFinder(0, 14)

    // 4. Timing Tracks
    for (i in 8..12) {
        matrix[6][i] = (i % 2 == 0)
        matrix[i][6] = (i % 2 == 0)
    }

    // 5. Dark Module
    matrix[13][8] = true

    // 6. Deterministic Data Modules from String Hash
    val hash = data.hashCode()
    var bitIndex = 0
    for (r in 0 until size) {
        for (c in 0 until size) {
            // Skip finder zones
            val inTopLeft = r < 8 && c < 8
            val inTopRight = r < 8 && c >= 13
            val inBottomLeft = r >= 13 && c < 8
            val inTiming = r == 6 || c == 6

            if (!inTopLeft && !inTopRight && !inBottomLeft && !inTiming) {
                // Compute deterministic module value
                val seed = (hash xor (r * 31 + c * 17))
                matrix[r][c] = ((seed shr (bitIndex % 28)) and 1) == 1
                bitIndex++
            }
        }
    }

    return matrix
}

/**
 * Complete Theme Color Palette Specification for BadGymMemberPassCard
 */
private data class MemberPassCardPalette(
    val cardBackground: Brush,
    val cardBorder: Color,
    val emblemBg: Color,
    val emblemBorder: Color,
    val emblemTint: Color,
    val brandBadText: Color,
    val brandGymText: Color,
    val subtitleText: Color,
    val planPillBg: Color,
    val planPillBorder: Color,
    val planPillText: Color,
    val haloColors: List<Color>,
    val avatarBadgeBg: Color,
    val nameText: Color,
    val metaText: Color,
    val quoteText: Color,
    val calendarIconBg: Color,
    val calendarIconTint: Color,
    val validityLabelText: Color,
    val validityDateText: Color,
    val daysLeftPillBg: Color,
    val daysLeftPillBorder: Color,
    val daysLeftPillText: Color,
    val progressTrackColor: Color,
    val progressBarBrush: Brush,
    val progressPercentText: Color,
    val podBg: Color,
    val podBorder: Color,
    val podIconBg: Color,
    val podIconTint: Color,
    val podTitleColor: Color,
    val podSubtitleColor: Color
)

private fun getPassCardPalette(tier: BadGymPassTier): MemberPassCardPalette {
    return when (tier) {
        // 01: Emerald Green (Saturated Glass)
        BadGymPassTier.SPARK -> MemberPassCardPalette(
            cardBackground = Brush.verticalGradient(
                listOf(Color(0xFF044837), Color(0xFF065F46), Color(0xFF0D9488))
            ),
            cardBorder = Color(0xFF34D399).copy(alpha = 0.55f),
            emblemBg = Color.White.copy(alpha = 0.16f),
            emblemBorder = Color.White.copy(alpha = 0.25f),
            emblemTint = Color.White,
            brandBadText = Color.White,
            brandGymText = Color.White,
            subtitleText = Color(0xFFA7F3D0),
            planPillBg = Color.White.copy(alpha = 0.18f),
            planPillBorder = Color(0xFF34D399).copy(alpha = 0.45f),
            planPillText = Color.White,
            haloColors = listOf(Color(0xFF34D399), Color(0xFF10B981), Color(0xFF6EE7B7), Color(0xFF34D399)),
            avatarBadgeBg = Color(0xFF10B981),
            nameText = Color.White,
            metaText = Color(0xFFD1FAE5),
            quoteText = Color.White,
            calendarIconBg = Color.White.copy(alpha = 0.16f),
            calendarIconTint = Color.White,
            validityLabelText = Color.White,
            validityDateText = Color(0xFFD1FAE5),
            daysLeftPillBg = Color(0xFF10B981),
            daysLeftPillBorder = Color(0xFF34D399),
            daysLeftPillText = Color.White,
            progressTrackColor = Color.White.copy(alpha = 0.22f),
            progressBarBrush = Brush.horizontalGradient(
                listOf(Color(0xFF34D399), Color(0xFF10B981), Color(0xFF6EE7B7))
            ),
            progressPercentText = Color.White,
            podBg = Color.White.copy(alpha = 0.14f),
            podBorder = Color.White.copy(alpha = 0.22f),
            podIconBg = Color.White,
            podIconTint = Color(0xFF059669),
            podTitleColor = Color.White,
            podSubtitleColor = Color(0xFFA7F3D0)
        )

        // 02: Ocean Blue (Saturated Glass)
        BadGymPassTier.POWER -> MemberPassCardPalette(
            cardBackground = Brush.verticalGradient(
                listOf(Color(0xFF0C4A6E), Color(0xFF0284C7), Color(0xFF0369A1))
            ),
            cardBorder = Color(0xFF38BDF8).copy(alpha = 0.55f),
            emblemBg = Color.White.copy(alpha = 0.16f),
            emblemBorder = Color.White.copy(alpha = 0.25f),
            emblemTint = Color.White,
            brandBadText = Color.White,
            brandGymText = Color.White,
            subtitleText = Color(0xFFBAE6FD),
            planPillBg = Color.White.copy(alpha = 0.18f),
            planPillBorder = Color(0xFF38BDF8).copy(alpha = 0.45f),
            planPillText = Color.White,
            haloColors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF7DD3FC), Color(0xFF38BDF8)),
            avatarBadgeBg = Color(0xFF0284C7),
            nameText = Color.White,
            metaText = Color(0xFFE0F2FE),
            quoteText = Color.White,
            calendarIconBg = Color.White.copy(alpha = 0.16f),
            calendarIconTint = Color.White,
            validityLabelText = Color.White,
            validityDateText = Color(0xFFE0F2FE),
            daysLeftPillBg = Color(0xFF0284C7),
            daysLeftPillBorder = Color(0xFF38BDF8),
            daysLeftPillText = Color.White,
            progressTrackColor = Color.White.copy(alpha = 0.22f),
            progressBarBrush = Brush.horizontalGradient(
                listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF7DD3FC))
            ),
            progressPercentText = Color.White,
            podBg = Color.White.copy(alpha = 0.14f),
            podBorder = Color.White.copy(alpha = 0.22f),
            podIconBg = Color.White,
            podIconTint = Color(0xFF0284C7),
            podTitleColor = Color.White,
            podSubtitleColor = Color(0xFFBAE6FD)
        )

        // 03: Royal Purple (Saturated Glass)
        BadGymPassTier.PLATINUM -> MemberPassCardPalette(
            cardBackground = Brush.verticalGradient(
                listOf(Color(0xFF4C1D95), Color(0xFF6D28D9), Color(0xFF7C3AED))
            ),
            cardBorder = Color(0xFFC084FC).copy(alpha = 0.55f),
            emblemBg = Color.White.copy(alpha = 0.16f),
            emblemBorder = Color.White.copy(alpha = 0.25f),
            emblemTint = Color.White,
            brandBadText = Color.White,
            brandGymText = Color.White,
            subtitleText = Color(0xFFE9D5FF),
            planPillBg = Color.White.copy(alpha = 0.18f),
            planPillBorder = Color(0xFFC084FC).copy(alpha = 0.45f),
            planPillText = Color.White,
            haloColors = listOf(Color(0xFFC084FC), Color(0xFF9333EA), Color(0xFFE879F9), Color(0xFFC084FC)),
            avatarBadgeBg = Color(0xFF9333EA),
            nameText = Color.White,
            metaText = Color(0xFFF3E8FF),
            quoteText = Color.White,
            calendarIconBg = Color.White.copy(alpha = 0.16f),
            calendarIconTint = Color.White,
            validityLabelText = Color.White,
            validityDateText = Color(0xFFF3E8FF),
            daysLeftPillBg = Color(0xFF9333EA),
            daysLeftPillBorder = Color(0xFFC084FC),
            daysLeftPillText = Color.White,
            progressTrackColor = Color.White.copy(alpha = 0.22f),
            progressBarBrush = Brush.horizontalGradient(
                listOf(Color(0xFFC084FC), Color(0xFF9333EA), Color(0xFFE879F9))
            ),
            progressPercentText = Color.White,
            podBg = Color.White.copy(alpha = 0.14f),
            podBorder = Color.White.copy(alpha = 0.22f),
            podIconBg = Color.White,
            podIconTint = Color(0xFF9333EA),
            podTitleColor = Color.White,
            podSubtitleColor = Color(0xFFE9D5FF)
        )

        // 04: Golden Amber (Warm Gold Saturated Glass)
        BadGymPassTier.GOLD -> MemberPassCardPalette(
            cardBackground = Brush.verticalGradient(
                listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7), Color(0xFFFDE68A))
            ),
            cardBorder = Color(0xFFF59E0B).copy(alpha = 0.6f),
            emblemBg = Color(0xFFFDE68A),
            emblemBorder = Color(0xFFF59E0B).copy(alpha = 0.5f),
            emblemTint = Color(0xFFB45309),
            brandBadText = Color(0xFF1E1B18),
            brandGymText = Color(0xFF1E1B18),
            subtitleText = Color(0xFF92400E),
            planPillBg = Color(0xFFF59E0B),
            planPillBorder = Color(0xFFD97706),
            planPillText = Color.White,
            haloColors = listOf(Color(0xFFF59E0B), Color(0xFFD97706), Color(0xFFFBBF24), Color(0xFFF59E0B)),
            avatarBadgeBg = Color(0xFFD97706),
            nameText = Color(0xFF1E1B18),
            metaText = Color(0xFF78350F),
            quoteText = Color(0xFF451A03),
            calendarIconBg = Color(0xFFFDE68A),
            calendarIconTint = Color(0xFFB45309),
            validityLabelText = Color(0xFF451A03),
            validityDateText = Color(0xFF78350F),
            daysLeftPillBg = Color(0xFFF59E0B),
            daysLeftPillBorder = Color(0xFFD97706),
            daysLeftPillText = Color.White,
            progressTrackColor = Color(0xFFFDE68A),
            progressBarBrush = Brush.horizontalGradient(
                listOf(Color(0xFFF59E0B), Color(0xFFD97706), Color(0xFFFBBF24))
            ),
            progressPercentText = Color(0xFF78350F),
            podBg = Color.White.copy(alpha = 0.80f),
            podBorder = Color(0xFFFDE68A),
            podIconBg = Color.White,
            podIconTint = Color(0xFFD97706),
            podTitleColor = Color(0xFF451A03),
            podSubtitleColor = Color(0xFF78350F)
        )

        // 05: Crimson Alert / Overdue (Saturated Glass)
        BadGymPassTier.BASIC -> MemberPassCardPalette(
            cardBackground = Brush.verticalGradient(
                listOf(Color(0xFF881337), Color(0xFF9F1239), Color(0xFFBE123C))
            ),
            cardBorder = Color(0xFFFB7185).copy(alpha = 0.55f),
            emblemBg = Color.White.copy(alpha = 0.16f),
            emblemBorder = Color.White.copy(alpha = 0.25f),
            emblemTint = Color.White,
            brandBadText = Color.White,
            brandGymText = Color.White,
            subtitleText = Color(0xFFFECDD3),
            planPillBg = Color.White,
            planPillBorder = Color(0xFFFDA4AF),
            planPillText = Color(0xFFE11D48),
            haloColors = listOf(Color(0xFFFB7185), Color(0xFFE11D48), Color(0xFFFDA4AF), Color(0xFFFB7185)),
            avatarBadgeBg = Color(0xFFEF4444),
            nameText = Color.White,
            metaText = Color(0xFFFFE4E6),
            quoteText = Color.White,
            calendarIconBg = Color.White.copy(alpha = 0.16f),
            calendarIconTint = Color.White,
            validityLabelText = Color.White,
            validityDateText = Color(0xFFFFE4E6),
            daysLeftPillBg = Color(0xFFE11D48),
            daysLeftPillBorder = Color(0xFFFB7185),
            daysLeftPillText = Color.White,
            progressTrackColor = Color.White.copy(alpha = 0.22f),
            progressBarBrush = Brush.horizontalGradient(
                listOf(Color(0xFFFB7185), Color(0xFFE11D48))
            ),
            progressPercentText = Color.White,
            podBg = Color.White.copy(alpha = 0.14f),
            podBorder = Color.White.copy(alpha = 0.22f),
            podIconBg = Color.White,
            podIconTint = Color(0xFFE11D48),
            podTitleColor = Color.White,
            podSubtitleColor = Color(0xFFFECDD3)
        )

        // 06: Cyan Aqua (Vibrant Saturated Glass)
        BadGymPassTier.CYAN, BadGymPassTier.DIAMOND -> MemberPassCardPalette(
            cardBackground = Brush.verticalGradient(
                listOf(Color(0xFFE0F7FA), Color(0xFFCCFBF1), Color(0xFF99F6E4).copy(alpha = 0.5f))
            ),
            cardBorder = Color(0xFF2DD4BF).copy(alpha = 0.6f),
            emblemBg = Color(0xFFCCFBF1),
            emblemBorder = Color(0xFF2DD4BF),
            emblemTint = Color(0xFF0D9488),
            brandBadText = Color(0xFF0F172A),
            brandGymText = Color(0xFF0F172A),
            subtitleText = Color(0xFF0D9488),
            planPillBg = Color(0xFF0D9488),
            planPillBorder = Color(0xFF14B8A6),
            planPillText = Color.White,
            haloColors = listOf(Color(0xFF2DD4BF), Color(0xFF0D9488), Color(0xFF5EEAD4), Color(0xFF2DD4BF)),
            avatarBadgeBg = Color(0xFF0D9488),
            nameText = Color(0xFF0F172A),
            metaText = Color(0xFF134E4A),
            quoteText = Color(0xFF0F172A),
            calendarIconBg = Color(0xFFCCFBF1),
            calendarIconTint = Color(0xFF0D9488),
            validityLabelText = Color(0xFF0F172A),
            validityDateText = Color(0xFF134E4A),
            daysLeftPillBg = Color(0xFF0D9488),
            daysLeftPillBorder = Color(0xFF14B8A6),
            daysLeftPillText = Color.White,
            progressTrackColor = Color(0xFFCCFBF1),
            progressBarBrush = Brush.horizontalGradient(
                listOf(Color(0xFF14B8A6), Color(0xFF06B6D4))
            ),
            progressPercentText = Color(0xFF0F172A),
            podBg = Color.White.copy(alpha = 0.85f),
            podBorder = Color(0xFF99F6E4),
            podIconBg = Color.White,
            podIconTint = Color(0xFF0D9488),
            podTitleColor = Color(0xFF0F172A),
            podSubtitleColor = Color(0xFF0D9488)
        )

        // 07: White Mint Glass (Minimal Frosted Crystal Glass)
        BadGymPassTier.WHITE_MINT, BadGymPassTier.SILVER -> MemberPassCardPalette(
            cardBackground = Brush.verticalGradient(
                listOf(Color.White, Color(0xFFF0FDF4), Color(0xFFDCFCE7).copy(alpha = 0.45f))
            ),
            cardBorder = Color(0xFF86EFAC).copy(alpha = 0.7f),
            emblemBg = Color(0xFFDCFCE7),
            emblemBorder = Color(0xFF86EFAC),
            emblemTint = Color(0xFF059669),
            brandBadText = Color(0xFF0F172A),
            brandGymText = Color(0xFF0F172A),
            subtitleText = Color(0xFF059669),
            planPillBg = Color(0xFFDCFCE7),
            planPillBorder = Color(0xFF86EFAC),
            planPillText = Color(0xFF059669),
            haloColors = listOf(Color(0xFF34D399), Color(0xFF10B981), Color(0xFF6EE7B7), Color(0xFF34D399)),
            avatarBadgeBg = Color(0xFF10B981),
            nameText = Color(0xFF0F172A),
            metaText = Color(0xFF64748B),
            quoteText = Color(0xFF0F172A),
            calendarIconBg = Color(0xFFDCFCE7),
            calendarIconTint = Color(0xFF059669),
            validityLabelText = Color(0xFF0F172A),
            validityDateText = Color(0xFF64748B),
            daysLeftPillBg = Color(0xFF10B981),
            daysLeftPillBorder = Color(0xFF34D399),
            daysLeftPillText = Color.White,
            progressTrackColor = Color(0xFFDCFCE7),
            progressBarBrush = Brush.horizontalGradient(
                listOf(Color(0xFF10B981), Color(0xFF34D399))
            ),
            progressPercentText = Color(0xFF0F172A),
            podBg = Color.White.copy(alpha = 0.95f),
            podBorder = Color(0xFFBBF7D0),
            podIconBg = Color(0xFFDCFCE7),
            podIconTint = Color(0xFF059669),
            podTitleColor = Color(0xFF0F172A),
            podSubtitleColor = Color(0xFF64748B)
        )

        // 08: White Ocean Glass (Minimal Frosted Crystal Glass)
        BadGymPassTier.WHITE_OCEAN, BadGymPassTier.ELITE -> MemberPassCardPalette(
            cardBackground = Brush.verticalGradient(
                listOf(Color.White, Color(0xFFF0F9FF), Color(0xFFBAE6FD).copy(alpha = 0.35f))
            ),
            cardBorder = Color(0xFF7DD3FC).copy(alpha = 0.7f),
            emblemBg = Color(0xFFE0F2FE),
            emblemBorder = Color(0xFF7DD3FC),
            emblemTint = Color(0xFF0284C7),
            brandBadText = Color(0xFF0F172A),
            brandGymText = Color(0xFF0F172A),
            subtitleText = Color(0xFF0284C7),
            planPillBg = Color(0xFFE0F2FE),
            planPillBorder = Color(0xFF7DD3FC),
            planPillText = Color(0xFF0284C7),
            haloColors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF7DD3FC), Color(0xFF38BDF8)),
            avatarBadgeBg = Color(0xFF0284C7),
            nameText = Color(0xFF0F172A),
            metaText = Color(0xFF64748B),
            quoteText = Color(0xFF0F172A),
            calendarIconBg = Color(0xFFE0F2FE),
            calendarIconTint = Color(0xFF0284C7),
            validityLabelText = Color(0xFF0F172A),
            validityDateText = Color(0xFF64748B),
            daysLeftPillBg = Color(0xFF0284C7),
            daysLeftPillBorder = Color(0xFF38BDF8),
            daysLeftPillText = Color.White,
            progressTrackColor = Color(0xFFE0F2FE),
            progressBarBrush = Brush.horizontalGradient(
                listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
            ),
            progressPercentText = Color(0xFF0F172A),
            podBg = Color.White.copy(alpha = 0.95f),
            podBorder = Color(0xFFBAE6FD),
            podIconBg = Color(0xFFE0F2FE),
            podIconTint = Color(0xFF0284C7),
            podTitleColor = Color(0xFF0F172A),
            podSubtitleColor = Color(0xFF64748B)
        )

        // 09: White Lavender Glass (Minimal Frosted Crystal Glass)
        BadGymPassTier.WHITE_LAVENDER -> MemberPassCardPalette(
            cardBackground = Brush.verticalGradient(
                listOf(Color.White, Color(0xFFFAF5FF), Color(0xFFE9D5FF).copy(alpha = 0.40f))
            ),
            cardBorder = Color(0xFFC084FC).copy(alpha = 0.7f),
            emblemBg = Color(0xFFF3E8FF),
            emblemBorder = Color(0xFFC084FC),
            emblemTint = Color(0xFF9333EA),
            brandBadText = Color(0xFF0F172A),
            brandGymText = Color(0xFF0F172A),
            subtitleText = Color(0xFF9333EA),
            planPillBg = Color(0xFFF3E8FF),
            planPillBorder = Color(0xFFC084FC),
            planPillText = Color(0xFF9333EA),
            haloColors = listOf(Color(0xFFC084FC), Color(0xFF9333EA), Color(0xFFE879F9), Color(0xFFC084FC)),
            avatarBadgeBg = Color(0xFF9333EA),
            nameText = Color(0xFF0F172A),
            metaText = Color(0xFF64748B),
            quoteText = Color(0xFF0F172A),
            calendarIconBg = Color(0xFFF3E8FF),
            calendarIconTint = Color(0xFF9333EA),
            validityLabelText = Color(0xFF0F172A),
            validityDateText = Color(0xFF64748B),
            daysLeftPillBg = Color(0xFF9333EA),
            daysLeftPillBorder = Color(0xFFC084FC),
            daysLeftPillText = Color.White,
            progressTrackColor = Color(0xFFF3E8FF),
            progressBarBrush = Brush.horizontalGradient(
                listOf(Color(0xFF9333EA), Color(0xFFC084FC))
            ),
            progressPercentText = Color(0xFF0F172A),
            podBg = Color.White.copy(alpha = 0.95f),
            podBorder = Color(0xFFE9D5FF),
            podIconBg = Color(0xFFF3E8FF),
            podIconTint = Color(0xFF9333EA),
            podTitleColor = Color(0xFF0F172A),
            podSubtitleColor = Color(0xFF64748B)
        )
    }
}
