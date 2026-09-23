package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.memberintelligence.debug.DebugRuntimeIdentityBadge
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.colors.ThemeResolver
import com.example.badnewgym.feature.memberintelligence.design.dimensions.CompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.design.dimensions.rememberCompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import java.text.NumberFormat
import java.util.Locale

/**
 * BAD GYM Stage 2 — Compact Member Intelligence Card.
 *
 * Dedicated browse-surface card designed for horizontal carousels.
 * Width ~220-240dp on 360dp phone, height ~356dp (below half viewport).
 * Maintains full information density: Identity + Event + Tier/Status + Decision Metrics + Signal + CTA.
 */
@Composable
fun CompactMemberCard(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    theme: ThemeId = ThemeId.NATURAL_FRESH,
    primarySignal: IntelligenceSignal? = null,
    secondarySignals: List<IntelligenceSignal> = emptyList(),
    cta: SignalAction? = null,
    dimensions: CompactCardDimensions = rememberCompactCardDimensions(),
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onCtaClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BADGymTheme(
        colors = theme.colors(),
        shapes = theme.shapes(),
        motion = theme.motion(),
        elevation = theme.elevation()
    ) {
        val colors = BADGymTheme.colors
        val isDark = theme == ThemeId.FUTURISTIC_NEON ||
                theme == ThemeId.BEAST_MODE ||
                theme == ThemeId.PURPLE_ROYAL ||
                theme == ThemeId.MINIMAL_DARK ||
                theme == ThemeId.PREMIUM_3D

        val cardElevation = if (isSelected) 6.dp else 2.dp

        Box(
            modifier = modifier
                .width(dimensions.cardWidth)
                .height(dimensions.cardHeight)
                .shadow(cardElevation, RoundedCornerShape(dimensions.cornerRadius))
                .clip(RoundedCornerShape(dimensions.cornerRadius))
                .background(colors.surface)
                .border(
                    width = if (isSelected) 1.5.dp else dimensions.strokeWidth,
                    color = if (isSelected) colors.accent else colors.border.copy(alpha = if (isDark) 0.85f else 0.65f),
                    shape = RoundedCornerShape(dimensions.cornerRadius)
                )
                .clickable(
                    onClick = onClick,
                    onClickLabel = "Open member intelligence profile for ${snapshot.identity.name}"
                )
                .semantics {
                    contentDescription = "Member card: ${snapshot.identity.name}, ${snapshot.identity.code ?: ""}, ${theme.name}"
                }
        ) {
            // Layer 1: Ambient theme ornament background
            ThemeOrnamentLayer(theme = theme, modifier = Modifier.matchParentSize())

            // Layer 2: Restrained botanical decoration for Natural Fresh
            if (theme == ThemeId.NATURAL_FRESH) {
                Image(
                    painter = painterResource(R.drawable.badgym_leaf_pair_header),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alpha = 0.45f,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 22.dp, end = 6.dp)
                        .size(dimensions.leafAccentSize)
                )
                Image(
                    painter = painterResource(R.drawable.badgym_leaf_cluster_left),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    alpha = 0.25f,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(52.dp)
                )
            }

            // Layer 3: Main Structured Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = dimensions.innerPaddingHorizontal,
                        vertical = dimensions.innerPaddingVertical
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Section 1: Event + Time Header
                CompactEventHeader(event = currentEvent, theme = theme)

                // Section 2: Hero Identity (Portrait + Name + Code + Motto)
                CompactHeroIdentity(
                    identity = snapshot.identity,
                    theme = theme,
                    portraitWidth = dimensions.portraitWidth,
                    portraitHeight = dimensions.portraitHeight
                )

                // Section 3: Membership Tier & Status Dual Bands
                CompactMembershipBands(
                    membership = snapshot.membership,
                    theme = theme
                )

                // Section 4: Compact Decision Metrics (Attendance, Payment, Workouts)
                CompactMetricsGrid(
                    attendance = snapshot.attendance,
                    payment = snapshot.payment,
                    workoutsCount = snapshot.workout?.durationMinutes ?: 12,
                    theme = theme
                )

                // Section 5: Urgent/Actionable Signal banner (if present)
                val signalText: String? = primarySignal?.title
                    ?: secondarySignals.firstOrNull()?.title
                    ?: snapshot.issues.firstOrNull()?.description

                val isCritical: Boolean = primarySignal?.priority == SignalPriority.P0_CRITICAL ||
                        snapshot.issues.firstOrNull()?.let {
                            it.severity == IssueSeverity.HIGH || it.severity == IssueSeverity.CRITICAL
                        } == true

                CompactSignalBanner(text = signalText, isCritical = isCritical)

                // Section 6: Contextual Primary CTA Button
                ThemedCtaButton(
                    theme = theme,
                    onClick = onCtaClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensions.ctaHeight)
                )

                // Section 7: Dedicated clean debug / identity row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DebugRuntimeIdentityBadge()
                }
            }
        }
    }
}

@Composable
private fun CompactEventHeader(
    event: MemberEvent?,
    theme: ThemeId
) {
    val colors = BADGymTheme.colors
    val eventType = event?.eventType ?: EventType.CHECK_IN
    val eventColor = ThemeResolver.resolveEventBadgeColor(eventType, colors)
    val isNatural = theme == ThemeId.NATURAL_FRESH

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Event badge
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isNatural) Color(0xFF16A34A) else eventColor)
                .padding(horizontal = 7.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "+ " + eventType.displayLabel(),
                color = Color.White,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.3.sp
            )
        }

        // Event time
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = theme.timeText,
                color = colors.textPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "• ${theme.timeRelative}",
                color = colors.textMuted,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CompactHeroIdentity(
    identity: MemberIdentity,
    theme: ThemeId,
    portraitWidth: Dp,
    portraitHeight: Dp
) {
    val colors = BADGymTheme.colors

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MemberPhoto(
            photoUrl = identity.photoUrl,
            tier = identity.tier,
            width = portraitWidth,
            height = portraitHeight,
            showVerified = identity.isVerified
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = identity.name,
                    color = colors.textPrimary,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.3).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (identity.isVerified) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = "Verified",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Text(
                text = identity.code ?: "BG---",
                color = colors.textSecondary,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(1.dp))

            Text(
                text = theme.motto,
                color = colors.mottoColor,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = if (theme == ThemeId.NATURAL_FRESH || theme == ThemeId.GLASSMORPHISM || theme == ThemeId.PURPLE_ROYAL || theme == ThemeId.PREMIUM_3D) FontStyle.Italic else FontStyle.Normal,
                lineHeight = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CompactMembershipBands(
    membership: MembershipStatus?,
    theme: ThemeId
) {
    val colors = BADGymTheme.colors
    val planName = membership?.planName ?: "Gold Plan"
    val daysRemaining = membership?.daysRemaining ?: 48
    val isActive = membership?.isActive ?: true

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Plan Band
        val goldBg = when (theme) {
            ThemeId.PREMIUM_3D -> Color(0xFF2A200B)
            ThemeId.NATURAL_FRESH -> Color(0xFFFEF9C3)
            ThemeId.MINIMAL_DARK -> Color(0xFF1E232B)
            else -> colors.surfaceMuted
        }
        val goldBorder = when (theme) {
            ThemeId.PREMIUM_3D -> Color(0xFFD4AF37)
            ThemeId.NATURAL_FRESH -> Color(0xFFFDE047)
            else -> colors.border
        }
        val goldText = when (theme) {
            ThemeId.PREMIUM_3D -> Color(0xFFFFD700)
            ThemeId.NATURAL_FRESH -> Color(0xFF854D0E)
            else -> colors.textPrimary
        }
        val goldIcon = when (theme) {
            ThemeId.PREMIUM_3D -> Color(0xFFFFD700)
            ThemeId.NATURAL_FRESH -> Color(0xFFCA8A04)
            else -> colors.accent
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(goldBg)
                .border(0.8.dp, goldBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.WorkspacePremium,
                contentDescription = null,
                tint = goldIcon,
                modifier = Modifier.size(13.dp)
            )
            Spacer(Modifier.width(4.dp))
            Column {
                Text(
                    text = planName,
                    color = goldText,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${membership?.planType ?: "12M"}",
                    color = goldText.copy(alpha = 0.8f),
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Active Status Band
        val activeBg = if (isActive) colors.successSoft.copy(alpha = 0.95f) else colors.dangerSoft.copy(alpha = 0.95f)
        val activeBorder = if (isActive) colors.success.copy(alpha = 0.5f) else colors.danger.copy(alpha = 0.5f)
        val activeTint = if (isActive) colors.success else colors.danger

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(activeBg)
                .border(0.8.dp, activeBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = activeTint,
                modifier = Modifier.size(13.dp)
            )
            Spacer(Modifier.width(4.dp))
            Column {
                Text(
                    text = if (isActive) "ACTIVE" else "EXPIRED",
                    color = activeTint,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$daysRemaining d left",
                    color = activeTint.copy(alpha = 0.85f),
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CompactMetricsGrid(
    attendance: AttendanceSummary?,
    payment: PaymentSummary?,
    workoutsCount: Int,
    theme: ThemeId
) {
    val colors = BADGymTheme.colors

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Tile 1: Attendance
        val visits = attendance?.visits ?: 16
        val target = attendance?.target ?: 26
        val attendancePercent = if (target > 0) ((visits.toFloat() / target) * 100).toInt() else 0
        CompactMetricTile(theme = theme, modifier = Modifier.weight(1f)) {
            Text(
                text = "$visits/$target",
                color = colors.textPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black
            )
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { (attendancePercent / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxSize(),
                    color = if (theme == ThemeId.NATURAL_FRESH) Color(0xFF16A34A) else colors.accent,
                    strokeWidth = 2.5.dp,
                    trackColor = colors.surfaceMuted
                )
                Text(
                    text = "$attendancePercent%",
                    color = colors.textPrimary,
                    fontSize = 6.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Attendance",
                color = colors.textSecondary,
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Tile 2: Payment
        val totalDue = payment?.totalOutstanding ?: 4500.0
        val overdueDays = payment?.overdueDays ?: 3
        val isOverdue = totalDue > 0
        val formattedAmount = if (totalDue > 0) {
            val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))
            "₹" + formatter.format(totalDue.toInt())
        } else "₹0"

        CompactMetricTile(theme = theme, modifier = Modifier.weight(1f)) {
            Text(
                text = formattedAmount,
                color = if (isOverdue) Color(0xFFDC2626) else colors.textPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isOverdue) Color(0xFFFEE2E2) else Color(0xFFDCFCE7))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = if (isOverdue) "$overdueDays d due" else "Clear",
                    color = if (isOverdue) Color(0xFFDC2626) else Color(0xFF16A34A),
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Payment",
                color = colors.textSecondary,
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Tile 3: Workouts
        val countDisplay = if (workoutsCount > 0) workoutsCount else 12
        CompactMetricTile(theme = theme, modifier = Modifier.weight(1f)) {
            Text(
                text = "$countDisplay",
                color = colors.textPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black
            )
            Row(
                modifier = Modifier
                    .height(16.dp)
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                val barHeights = listOf(0.4f, 0.7f, 1.0f, 0.65f, 0.9f)
                val barColor = if (theme == ThemeId.NATURAL_FRESH) Color(0xFF16A34A) else colors.accent
                barHeights.forEach { h ->
                    Box(
                        modifier = Modifier
                            .width(2.5.dp)
                            .fillMaxHeight(h)
                            .clip(RoundedCornerShape(1.dp))
                            .background(barColor)
                    )
                }
            }
            Text(
                text = "Workouts",
                color = colors.textSecondary,
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CompactMetricTile(
    theme: ThemeId,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = BADGymTheme.colors
    Box(
        modifier = modifier
            .height(58.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when (theme) {
                    ThemeId.NATURAL_FRESH -> Color(0xFFF6FBF7)
                    ThemeId.MINIMAL_DARK -> Color(0xFF181C22)
                    ThemeId.PREMIUM_3D -> Color(0xFF1F1B12)
                    ThemeId.BEAST_MODE -> Color(0xFF1C0609)
                    ThemeId.PURPLE_ROYAL -> Color(0xFF22113B)
                    ThemeId.FUTURISTIC_NEON -> Color(0xFF0A1428)
                    else -> colors.surfaceMuted
                }
            )
            .border(
                0.8.dp,
                when (theme) {
                    ThemeId.NATURAL_FRESH -> Color(0xFFD1E7D7)
                    else -> colors.border.copy(alpha = 0.5f)
                },
                RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp, horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Composable
private fun CompactSignalBanner(
    text: String?,
    isCritical: Boolean
) {
    if (text.isNullOrBlank()) {
        Spacer(Modifier.height(2.dp))
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(if (isCritical) Color(0xFFFEF2F2) else Color(0xFFFFFBEB))
            .border(
                0.8.dp,
                if (isCritical) Color(0xFFFCA5A5) else Color(0xFFFDE68A),
                RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.WarningAmber,
            contentDescription = null,
            tint = if (isCritical) Color(0xFFDC2626) else Color(0xFFD97706),
            modifier = Modifier.size(11.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            color = if (isCritical) Color(0xFF991B1B) else Color(0xFF92400E),
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}
