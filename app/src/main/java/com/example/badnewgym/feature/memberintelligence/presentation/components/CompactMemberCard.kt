package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalDrink
import androidx.compose.material.icons.rounded.MiscellaneousServices
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
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
import com.example.badnewgym.feature.memberintelligence.design.semantics.MemberSemanticResolver
import com.example.badnewgym.feature.memberintelligence.design.semantics.MemberSemanticStyle
import com.example.badnewgym.feature.memberintelligence.design.semantics.MemberStateVisual
import com.example.badnewgym.feature.memberintelligence.design.semantics.MemberTierVisual
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
 * In Bounded Detail mode (isDetail=true), presents vertical navigation rail and persistent
 * photo/name/event/time/status header while switching menu panels within bounded dimensions (~276dp x 372dp).
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
    isDetail: Boolean = false,
    menus: List<MemberMenu> = emptyList(),
    activeMenu: MenuType = MenuType.HOME,
    onMenuSelected: (MenuType) -> Unit = {},
    onClick: () -> Unit = {},
    onCtaClick: () -> Unit = {},
    onCloseDetail: () -> Unit = {},
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

        val semantics = remember(snapshot, currentEvent, theme) {
            MemberSemanticResolver.resolve(snapshot, currentEvent, theme)
        }

        val cardElevation = if (isSelected) 6.dp else 2.dp

        val currentCardWidth by animateDpAsState(
            targetValue = if (isDetail) dimensions.detailCardWidth else dimensions.cardWidth,
            animationSpec = tween(durationMillis = 220),
            label = "compact-card-width"
        )
        val currentCardHeight by animateDpAsState(
            targetValue = if (isDetail) dimensions.detailCardHeight else dimensions.cardHeight,
            animationSpec = tween(durationMillis = 220),
            label = "compact-card-height"
        )

        val borderColor = when {
            semantics.isUrgent -> semantics.prominentBorderColor
            isSelected -> colors.accent
            else -> colors.border.copy(alpha = if (isDark) 0.85f else 0.65f)
        }
        val borderWidth = when {
            semantics.isUrgent -> 1.8.dp
            isSelected -> 1.5.dp
            else -> dimensions.strokeWidth
        }

        Box(
            modifier = modifier
                .width(currentCardWidth)
                .height(currentCardHeight)
                .shadow(cardElevation, RoundedCornerShape(dimensions.cornerRadius))
                .clip(RoundedCornerShape(dimensions.cornerRadius))
                .background(colors.surface)
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(dimensions.cornerRadius)
                )
                .then(
                    if (!isDetail) {
                        Modifier.clickable(
                            onClick = onClick,
                            onClickLabel = "Open member intelligence profile for ${snapshot.identity.name}"
                        )
                    } else Modifier
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
            if (isDetail) {
                // Bounded Detail Layout: Integrated Vertical Rail + Persistent Header + Content Panel
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Integrated Navigation Rail on the Left
                    BoundedDetailRail(
                        snapshot = snapshot,
                        primarySignal = primarySignal,
                        menus = menus,
                        activeMenu = activeMenu,
                        onMenuSelected = onMenuSelected,
                        onCloseRail = onCloseDetail,
                        railWidth = dimensions.railWidth,
                        modifier = Modifier.fillMaxHeight()
                    )

                    // Right Content Column
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(
                                start = 7.dp,
                                end = 7.dp,
                                top = 7.dp,
                                bottom = 5.dp
                            ),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 1. Persistent Identity, Event, and Membership Header
                        // (CRITICAL: REMAINS PERSISTENT AND VISIBLE ACROSS ALL MENU CHANGES)
                        PersistentDetailHeader(
                            snapshot = snapshot,
                            currentEvent = currentEvent,
                            theme = theme,
                            semantics = semantics,
                            onClose = onCloseDetail
                        )

                        Spacer(Modifier.height(3.dp))

                        // 2. Menu Content Panel Viewport
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            AnimatedContent(
                                targetState = activeMenu,
                                transitionSpec = {
                                    (slideInHorizontally(tween(200)) + fadeIn(tween(200))) togetherWith
                                        (slideOutHorizontally(tween(160)) + fadeOut(tween(160)))
                                },
                                label = "bounded-detail-menu-content"
                            ) { menu ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    when (menu) {
                                        MenuType.HOME -> HomeBoundedContent(
                                            snapshot = snapshot,
                                            signals = if (primarySignal != null) listOf(primarySignal) + secondarySignals else secondarySignals,
                                            primarySignal = primarySignal,
                                            secondarySignals = secondarySignals,
                                            cta = cta,
                                            theme = theme,
                                            semantics = semantics,
                                            onCta = onCtaClick
                                        )
                                        MenuType.ATTENDANCE -> AttendancePanel(snapshot, theme)
                                        MenuType.PLAN -> PlanPanel(snapshot, theme)
                                        MenuType.PAYMENT -> PaymentPanel(
                                            snapshot = snapshot,
                                            theme = theme,
                                            onCtaClick = onCtaClick
                                        )
                                        MenuType.TRAINER -> TrainerPanel(snapshot, theme)
                                        MenuType.WORKOUT -> WorkoutPanel(snapshot, theme)
                                        MenuType.SUPPLEMENTS -> SupplementsPanel(snapshot, theme)
                                        MenuType.NUTRITION -> NutritionPanel(snapshot, theme)
                                        MenuType.SERVICES -> ServicesPanel(snapshot, theme)
                                        MenuType.HISTORY -> HistoryPanel(snapshot, theme)
                                        MenuType.INSIGHT -> InsightPanel(
                                            snapshot = snapshot,
                                            signals = if (primarySignal != null) listOf(primarySignal) + secondarySignals else secondarySignals,
                                            theme = theme
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(2.dp))

                        // 3. Dedicated clean debug / identity row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DebugRuntimeIdentityBadge()
                        }
                    }
                }
            } else {
                // Browse Mode Structured Content
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

                    // Section 2: Hero Identity (Portrait + Name + Code + Motto + Tier Badge)
                    CompactHeroIdentity(
                        identity = snapshot.identity,
                        theme = theme,
                        portraitWidth = dimensions.portraitWidth,
                        portraitHeight = dimensions.portraitHeight,
                        semantics = semantics
                    )

                    // Section 3: Membership Tier & Status Dual Bands
                    CompactMembershipBands(
                        membership = snapshot.membership,
                        theme = theme,
                        semantics = semantics,
                        payment = snapshot.payment
                    )

                    // Section 4: Compact Decision Metrics (Attendance, Payment, Workouts)
                    CompactMetricsGrid(
                        attendance = snapshot.attendance,
                        payment = snapshot.payment,
                        workoutsCount = snapshot.workout?.durationMinutes ?: 12,
                        theme = theme
                    )

                    // Section 5: Urgent/Actionable Signal banner
                    val signalText: String? = if (semantics.isUrgent && semantics.urgentMessage != null) {
                        semantics.urgentMessage
                    } else {
                        primarySignal?.title
                            ?: secondarySignals.firstOrNull()?.title
                            ?: snapshot.issues.firstOrNull()?.description
                    }

                    CompactSignalBanner(
                        text = signalText,
                        isCritical = semantics.isUrgent,
                        semantics = semantics
                    )

                    // Section 6: Contextual Primary CTA Button
                    val ctaLabel = when (semantics.stateVisual) {
                        MemberStateVisual.EXPIRED -> "Renew Plan Now →"
                        MemberStateVisual.PAYMENT_OVERDUE, MemberStateVisual.PAYMENT_DUE -> "Collect Payment →"
                        MemberStateVisual.TRAINER_ACTIVE -> "Coach Check-in →"
                        else -> null
                    }

                    ThemedCtaButton(
                        theme = theme,
                        onClick = onCtaClick,
                        label = ctaLabel,
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
    portraitHeight: Dp,
    semantics: MemberSemanticStyle
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
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Tier Badge Chip
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(semantics.tierBgColor)
                    .border(0.8.dp, semantics.tierBorderColor, RoundedCornerShape(6.dp))
                    .padding(horizontal = 5.dp, vertical = 1.5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    imageVector = semantics.tierIcon,
                    contentDescription = null,
                    tint = semantics.tierAccentColor,
                    modifier = Modifier.size(9.dp)
                )
                Text(
                    text = semantics.tierLabel,
                    color = semantics.tierAccentColor,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.3.sp
                )
            }

            // Name + Verified Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = identity.name,
                    color = colors.textPrimary,
                    fontSize = 14.sp,
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
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Text(
                text = identity.code ?: "BG---",
                color = colors.textSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = theme.motto,
                color = colors.mottoColor,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = if (theme == ThemeId.NATURAL_FRESH || theme == ThemeId.GLASSMORPHISM || theme == ThemeId.PURPLE_ROYAL || theme == ThemeId.PREMIUM_3D) FontStyle.Italic else FontStyle.Normal,
                lineHeight = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CompactMembershipBands(
    membership: MembershipStatus?,
    theme: ThemeId,
    semantics: MemberSemanticStyle,
    payment: PaymentSummary? = null
) {
    val colors = BADGymTheme.colors
    val planName = membership?.planName ?: "Gold Plan"
    val daysRemaining = membership?.daysRemaining ?: 48

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Plan Band
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(semantics.tierBgColor)
                .border(0.8.dp, semantics.tierBorderColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = semantics.tierIcon,
                contentDescription = null,
                tint = semantics.tierAccentColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(Modifier.width(4.dp))
            Column {
                Text(
                    text = planName,
                    color = semantics.tierAccentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${membership?.planType ?: "12M"}",
                    color = semantics.tierAccentColor.copy(alpha = 0.8f),
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Active Status Band (Urgent state overrides default styling)
        val statusSubtext = when (semantics.stateVisual) {
            MemberStateVisual.EXPIRED -> "0d left • Renew"
            MemberStateVisual.PAYMENT_OVERDUE -> "${payment?.overdueDays ?: 3}d overdue"
            MemberStateVisual.PAYMENT_DUE -> "Due Soon"
            MemberStateVisual.TRAINER_ACTIVE -> "Active Coach"
            MemberStateVisual.CRITICAL_ALERT -> "Action Needed"
            MemberStateVisual.FROZEN -> "On Pause"
            else -> "$daysRemaining d left"
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(semantics.stateBgColor)
                .border(
                    width = if (semantics.isUrgent) 1.2.dp else 0.8.dp,
                    color = semantics.stateBorderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = semantics.stateIcon,
                contentDescription = null,
                tint = semantics.stateAccentColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(Modifier.width(4.dp))
            Column {
                Text(
                    text = semantics.stateLabel,
                    color = semantics.stateAccentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = statusSubtext,
                    color = semantics.stateAccentColor.copy(alpha = 0.85f),
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
                    ThemeId.MINIMAL_DARK -> Color(0xFFF1F5F9)
                    ThemeId.PREMIUM_3D -> Color(0xFFFAF7F0)
                    ThemeId.BEAST_MODE -> Color(0xCC4A141D)
                    ThemeId.PURPLE_ROYAL -> Color(0xCC3B1C64)
                    ThemeId.FUTURISTIC_NEON -> Color(0xCC162C4E)
                    else -> colors.surfaceMuted
                }
            )
            .border(
                0.8.dp,
                when (theme) {
                    ThemeId.NATURAL_FRESH -> Color(0xFFD1E7D7)
                    ThemeId.MINIMAL_DARK -> Color(0xFFCBD5E1)
                    ThemeId.PREMIUM_3D -> Color(0xFFE5B842)
                    ThemeId.BEAST_MODE -> Color(0xFFFF334B)
                    ThemeId.PURPLE_ROYAL -> Color(0xFFC084FC)
                    ThemeId.FUTURISTIC_NEON -> Color(0xFF00E5FF)
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
    isCritical: Boolean,
    semantics: MemberSemanticStyle? = null
) {
    if (text.isNullOrBlank()) {
        Spacer(Modifier.height(2.dp))
        return
    }

    val bannerBg = if (semantics != null && semantics.isUrgent) {
        semantics.stateAccentColor.copy(alpha = 0.14f)
    } else if (isCritical) {
        Color(0xFFFEF2F2)
    } else {
        Color(0xFFFFFBEB)
    }

    val bannerBorder = if (semantics != null && semantics.isUrgent) {
        semantics.stateAccentColor.copy(alpha = 0.45f)
    } else if (isCritical) {
        Color(0xFFFCA5A5)
    } else {
        Color(0xFFFDE68A)
    }

    val iconTint = if (semantics != null && semantics.isUrgent) {
        semantics.stateAccentColor
    } else if (isCritical) {
        Color(0xFFDC2626)
    } else {
        Color(0xFFD97706)
    }

    val textColor = if (semantics != null && semantics.isUrgent) {
        semantics.stateAccentColor
    } else if (isCritical) {
        Color(0xFF991B1B)
    } else {
        Color(0xFF92400E)
    }

    val icon = semantics?.stateIcon ?: Icons.Rounded.WarningAmber

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(bannerBg)
            .border(
                0.8.dp,
                bannerBorder,
                RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(11.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Integrated navigation rail for the bounded detail state.
 * Fixed to the left edge of the card, with accessible >=48dp touch targets.
 * Displays truthful micro-data tags from MemberSnapshot, collapse affordance,
 * and prominent action-needed alert indicators for critical menus.
 */
@Composable
private fun BoundedDetailRail(
    snapshot: MemberSnapshot,
    primarySignal: IntelligenceSignal?,
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    onMenuSelected: (MenuType) -> Unit,
    onCloseRail: () -> Unit,
    railWidth: Dp,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors
    val visibleMenus = if (menus.isNotEmpty()) {
        menus.filter { it.isVisible && it.isEnabled }
    } else {
        listOf(
            MemberMenu(MenuType.HOME, "Home", 0, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.ATTENDANCE, "Attend", 10, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.PLAN, "Plan", 20, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.PAYMENT, "Pay", 30, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.TRAINER, "Coach", 40, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.WORKOUT, "Workout", 50, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.INSIGHT, "Insight", 60, isVisible = true, isEnabled = true, isLocked = false)
        )
    }

    Column(
        modifier = modifier
            .width(railWidth)
            .clip(RoundedCornerShape(topStart = 22.dp, bottomStart = 22.dp))
            .background(colors.railBackground.copy(alpha = 0.96f))
            .border(
                width = 0.8.dp,
                color = colors.border.copy(alpha = 0.4f),
                shape = RoundedCornerShape(topStart = 22.dp, bottomStart = 22.dp)
            )
            .padding(vertical = 4.dp, horizontal = 2.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        // Collapse affordance at top of rail
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(colors.surfaceMuted)
                .border(0.6.dp, colors.border.copy(alpha = 0.5f), CircleShape)
                .clickable(
                    role = Role.Button,
                    onClickLabel = "Collapse detail rail",
                    onClick = onCloseRail
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Collapse detail",
                tint = colors.textPrimary,
                modifier = Modifier.size(13.dp)
            )
        }

        Spacer(Modifier.height(1.dp))

        visibleMenus.forEach { menu ->
            val isActive = activeMenu == menu.id
            val isActionNeeded = when (menu.id) {
                MenuType.PAYMENT -> (snapshot.payment?.totalOutstanding ?: 0.0) > 0 ||
                        (snapshot.payment?.overdueDays ?: 0) > 0 ||
                        snapshot.payment?.lifecycle == PaymentLifecycle.OVERDUE ||
                        snapshot.payment?.lifecycle == PaymentLifecycle.DUE
                MenuType.PLAN -> snapshot.membership?.lifecycle == MembershipLifecycle.EXPIRED ||
                        (snapshot.membership?.daysRemaining ?: 1) <= 0 ||
                        snapshot.membership?.isActive == false
                MenuType.INSIGHT -> primarySignal?.priority == SignalPriority.P0_CRITICAL ||
                        snapshot.issues.any { it.severity == IssueSeverity.CRITICAL }
                else -> menu.hasAlert
            }

            val microData = getRailMicroData(menu.id, snapshot)

            val bg by animateColorAsState(
                if (isActive) colors.railActiveBackground else Color.Transparent,
                tween(180),
                label = "bounded-rail-bg"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp) // Minimum 48dp accessibility touch target
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        role = Role.Tab,
                        onClickLabel = "Select ${menu.label}"
                    ) { onMenuSelected(menu.id) }
                    .padding(vertical = 2.dp, horizontal = 1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isActive) 28.dp else 24.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(bg)
                        .then(
                            if (isActionNeeded) {
                                Modifier.border(
                                    1.2.dp,
                                    Color(0xFFEF4444),
                                    RoundedCornerShape(7.dp)
                                )
                            } else if (isActive) {
                                Modifier.border(
                                    1.dp,
                                    colors.border.copy(alpha = 0.6f),
                                    RoundedCornerShape(7.dp)
                                )
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getRailIcon(menu.id),
                        contentDescription = menu.label,
                        tint = if (isActionNeeded) Color(0xFFEF4444) else if (isActive) colors.railActiveIcon else colors.railInactiveIcon,
                        modifier = Modifier.size(if (isActive) 15.dp else 13.dp)
                    )
                    if (isActionNeeded || menu.hasAlert || menu.badgeCount > 0) {
                        Box(
                            Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 1.dp, y = (-1).dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444))
                        )
                    }
                }

                Spacer(Modifier.height(1.dp))

                Text(
                    text = menu.label,
                    color = if (isActionNeeded) Color(0xFFEF4444) else if (isActive) colors.textPrimary else colors.textMuted,
                    fontSize = 6.8.sp,
                    fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (microData.isNotBlank()) {
                    Text(
                        text = microData,
                        color = if (isActionNeeded) Color(0xFFEF4444) else if (isActive) colors.accent else colors.textSecondary,
                        fontSize = 6.2.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun getRailMicroData(menuType: MenuType, snapshot: MemberSnapshot): String {
    return when (menuType) {
        MenuType.HOME -> ""
        MenuType.ATTENDANCE -> snapshot.attendance?.let { "${it.visits}/${it.target ?: 26}" } ?: ""
        MenuType.PLAN -> snapshot.membership?.let { "${it.daysRemaining}d" } ?: ""
        MenuType.PAYMENT -> {
            val due = snapshot.payment?.totalOutstanding ?: 0.0
            if (due > 0) {
                if (due >= 1000) "₹${(due / 1000).toInt()}k!" else "₹${due.toInt()}!"
            } else {
                "Paid"
            }
        }
        MenuType.TRAINER -> snapshot.trainer?.let { "${it.sessionsTotal - it.sessionsUsed} PT" } ?: ""
        MenuType.WORKOUT -> snapshot.attendance?.avgVisitsPerWeek?.let { "${it.toInt()}/wk" } ?: "3/wk"
        MenuType.SUPPLEMENTS -> if (snapshot.supplements?.hasHistory == true) "Active" else "Off"
        MenuType.NUTRITION -> if (snapshot.nutrition?.isSubscribed == true) "On" else "Off"
        MenuType.SERVICES -> snapshot.services?.let { "${it.count { s -> s.isActive }} act" } ?: ""
        MenuType.HISTORY -> "${snapshot.recentEvents.size} ev"
        MenuType.INSIGHT -> if (snapshot.issues.isNotEmpty()) "${snapshot.issues.size} act" else "AI"
    }
}

private fun getRailIcon(type: MenuType): ImageVector = when (type) {
    MenuType.HOME -> Icons.Rounded.Home
    MenuType.ATTENDANCE -> Icons.Rounded.CalendarToday
    MenuType.PLAN -> Icons.Rounded.Assignment
    MenuType.PAYMENT -> Icons.Rounded.CreditCard
    MenuType.TRAINER -> Icons.Rounded.PersonOutline
    MenuType.WORKOUT -> Icons.Rounded.FitnessCenter
    MenuType.SUPPLEMENTS -> Icons.Rounded.LocalDrink
    MenuType.NUTRITION -> Icons.Rounded.Restaurant
    MenuType.SERVICES -> Icons.Rounded.MiscellaneousServices
    MenuType.HISTORY -> Icons.Rounded.History
    MenuType.INSIGHT -> Icons.Rounded.AutoAwesome
}

/**
 * Persistent detail header that stays visible at the top of the bounded detail card
 * regardless of which menu item is selected.
 * Displays photo, member name, verified badge, member ID, motto, event type, time, and membership status.
 */
@Composable
private fun PersistentDetailHeader(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    theme: ThemeId,
    semantics: MemberSemanticStyle,
    onClose: () -> Unit
) {
    val colors = BADGymTheme.colors
    val eventType = currentEvent?.eventType ?: EventType.CHECK_IN
    val isNatural = theme == ThemeId.NATURAL_FRESH
    val eventColor = ThemeResolver.resolveEventBadgeColor(eventType, colors)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Row 1: Event + Timestamp + Close / Collapse Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Event badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isNatural) Color(0xFF16A34A) else eventColor)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = "+ " + eventType.displayLabel(),
                    color = Color.White,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.2.sp
                )
            }

            // Event time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = theme.timeText,
                    color = colors.textPrimary,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• ${theme.timeRelative}",
                    color = colors.textMuted,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Back / Close icon button
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceMuted)
                    .border(0.6.dp, colors.border.copy(alpha = 0.5f), CircleShape)
                    .clickable(
                        onClick = onClose,
                        onClickLabel = "Return to browse cards"
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Collapse detail",
                    tint = colors.textPrimary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        // Row 2: Hero Identity (Photo + Name + Code + Motto)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MemberPhoto(
                photoUrl = snapshot.identity.photoUrl,
                tier = snapshot.identity.tier,
                width = 46.dp,
                height = 52.dp,
                showVerified = snapshot.identity.isVerified
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = snapshot.identity.name,
                        color = colors.textPrimary,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.2).sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (snapshot.identity.isVerified) {
                        Icon(
                            Icons.Rounded.CheckCircle,
                            contentDescription = "Verified",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = snapshot.identity.code ?: "BG---",
                        color = colors.textSecondary,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "• ${theme.motto.replace("\n", " ")}",
                        color = colors.mottoColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Row 3: Compact Membership Tier & Status
        CompactMembershipBands(
            membership = snapshot.membership,
            theme = theme,
            semantics = semantics,
            payment = snapshot.payment
        )

        // Subtle divider separating persistent header from menu panel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.8.dp)
                .background(colors.border.copy(alpha = 0.35f))
        )
    }
}

/**
 * Recomposed Home content tailored specifically for the bounded detail card.
 * Presents high-value session metrics, urgent actionable signals, coach/workout summary, and contextual CTA.
 */
@Composable
private fun HomeBoundedContent(
    snapshot: MemberSnapshot,
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    theme: ThemeId,
    semantics: MemberSemanticStyle,
    onCta: () -> Unit
) {
    val colors = BADGymTheme.colors

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Decision Metrics Grid (Attendance, Payment, Workouts)
        CompactMetricsGrid(
            attendance = snapshot.attendance,
            payment = snapshot.payment,
            workoutsCount = snapshot.workout?.durationMinutes ?: 12,
            theme = theme
        )

        // Actionable signal banner
        val signalText: String? = if (semantics.isUrgent && semantics.urgentMessage != null) {
            semantics.urgentMessage
        } else {
            primarySignal?.title
                ?: secondarySignals.firstOrNull()?.title
                ?: snapshot.issues.firstOrNull()?.description
        }

        val isCritical: Boolean = semantics.isUrgent || primarySignal?.priority == SignalPriority.P0_CRITICAL ||
                snapshot.issues.firstOrNull()?.let {
                    it.severity == IssueSeverity.HIGH || it.severity == IssueSeverity.CRITICAL
                } == true

        CompactSignalBanner(text = signalText, isCritical = isCritical, semantics = semantics)

        // Trainer / Routine Quick Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(colors.surfaceMuted.copy(alpha = 0.6f))
                .border(0.6.dp, colors.border.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Coach: ${snapshot.trainer?.trainerName ?: "Unassigned"}",
                    color = colors.textSecondary,
                    fontSize = 8.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Routine: ${snapshot.workout?.currentRoutine ?: "Routine"}",
                    color = colors.textPrimary,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Contextual CTA Button
        val ctaLabel = when (semantics.stateVisual) {
            MemberStateVisual.PAYMENT_OVERDUE, MemberStateVisual.PAYMENT_DUE -> "Collect Payment →"
            MemberStateVisual.EXPIRED -> "Renew Plan Now →"
            MemberStateVisual.CRITICAL_ALERT -> "Review Alert →"
            else -> cta?.label ?: "Open Full Profile"
        }

        ThemedCtaButton(
            theme = theme,
            label = ctaLabel,
            onClick = onCta,
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
        )
    }
}
