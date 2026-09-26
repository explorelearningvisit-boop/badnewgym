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
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.badnewgym.feature.memberintelligence.domain.model.TemporalEventRecord
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
import com.example.badnewgym.feature.memberintelligence.presentation.components.CompactIntelligenceStrip
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.colors.ThemeResolver
import com.example.badnewgym.feature.memberintelligence.design.dimensions.CompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.design.dimensions.rememberCompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.motion.contentDepthSeparation
import com.example.badnewgym.feature.memberintelligence.design.semantics.MemberSemanticResolver
import com.example.badnewgym.feature.memberintelligence.design.semantics.MemberSemanticStyle
import com.example.badnewgym.feature.memberintelligence.design.semantics.MemberStateVisual
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import java.text.NumberFormat
import java.util.Locale

/**
 * BAD GYM Stage 7.2 — Final Production Member Intelligence Card.
 *
 * Dedicated browse-surface card designed for horizontal carousels.
 * Geometry: Compact 296x410dp browse / 320x440dp detail; Default 312x426dp / 340x463dp; Expanded 328x442dp / 360x480dp.
 * - Interactive Home Cockpit: 3 Core KPIs (Attendance, Sessions, Workouts) + 1 Trend + Signal + Contextual CTA.
 * - Intelligent vertical rail with truthful semantic micro-data badges.
 * - Fully populated 11 menu panels + More action directory.
 * - 100% Light material personalities with white/ivory base surfaces and independent semantic colors.
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
    temporalRange: TemporalRange = TemporalRange.forCurrentMonth(),
    onRangeChange: (TemporalRange) -> Unit = {},
    onEventClick: (TemporalEventRecord) -> Unit = {},
    onMenuSelected: (MenuType) -> Unit = {},
    onClick: () -> Unit = {},
    onCtaClick: () -> Unit = {},
    onCloseDetail: () -> Unit = {},
    isReducedMotion: Boolean = false,
    modifier: Modifier = Modifier
) {
    var inspectedEvent by remember { mutableStateOf<TemporalEventRecord?>(null) }

    BADGymTheme(
        colors = theme.colors(),
        shapes = theme.shapes(),
        motion = theme.motion(),
        elevation = theme.elevation()
    ) {
        val colors = BADGymTheme.colors

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
            else -> colors.border.copy(alpha = 0.85f)
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
                    contentDescription = "Member card: ${snapshot.identity.name}, ${snapshot.identity.code ?: ""}, ${theme.title}"
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
                Row(modifier = Modifier.fillMaxSize()) {
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
                            .padding(start = 7.dp, end = 7.dp, top = 7.dp, bottom = 5.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 1. Persistent Identity, Event, and Membership Header
                        PersistentDetailHeader(
                            snapshot = snapshot,
                            currentEvent = currentEvent,
                            theme = theme,
                            semantics = semantics,
                            dimensions = dimensions,
                            onClose = onCloseDetail,
                            onIdentityClick = onClick
                        )

                        if (activeMenu != MenuType.HOME) {
                            CompactIntelligenceStrip(
                                snapshot = snapshot,
                                activeMenu = activeMenu,
                                onAttendanceClick = { onMenuSelected(MenuType.ATTENDANCE) },
                                onTrainerClick = { onMenuSelected(MenuType.TRAINER) },
                                onWorkoutClick = { onMenuSelected(MenuType.WORKOUT) },
                                onPaymentClick = { onMenuSelected(MenuType.PAYMENT) },
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Spacer(Modifier.height(3.dp))

                        // 2. Menu Content Panel Viewport
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .contentDepthSeparation(isReducedMotion)
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
                                            currentEvent = currentEvent,
                                            signals = if (primarySignal != null) listOf(primarySignal) + secondarySignals else secondarySignals,
                                            primarySignal = primarySignal,
                                            secondarySignals = secondarySignals,
                                            cta = cta,
                                            theme = theme,
                                            semantics = semantics,
                                            onCta = onCtaClick,
                                            onNavigate = onMenuSelected
                                        )
                                        MenuType.ATTENDANCE -> AttendancePanel(
                                            snapshot = snapshot,
                                            theme = theme,
                                            temporalRange = temporalRange,
                                            onRangeChange = onRangeChange,
                                            onEventClick = { inspectedEvent = it; onEventClick(it) }
                                        )
                                        MenuType.PLAN -> PlanPanel(snapshot, theme)
                                        MenuType.PAYMENT -> PaymentPanel(
                                            snapshot = snapshot,
                                            theme = theme,
                                            temporalRange = temporalRange,
                                            onRangeChange = onRangeChange,
                                            onEventClick = { inspectedEvent = it; onEventClick(it) },
                                            onCtaClick = onCtaClick
                                        )
                                        MenuType.TRAINER -> TrainerPanel(snapshot, theme)
                                        MenuType.WORKOUT -> WorkoutPanel(
                                            snapshot = snapshot,
                                            theme = theme,
                                            temporalRange = temporalRange,
                                            onRangeChange = onRangeChange,
                                            onEventClick = { inspectedEvent = it; onEventClick(it) }
                                        )
                                        MenuType.SUPPLEMENTS -> SupplementsPanel(snapshot, theme)
                                        MenuType.NUTRITION -> NutritionPanel(snapshot, theme)
                                        MenuType.SERVICES -> ServicesPanel(snapshot, theme)
                                        MenuType.HISTORY -> HistoryPanel(
                                            snapshot = snapshot,
                                            theme = theme,
                                            temporalRange = temporalRange,
                                            onRangeChange = onRangeChange,
                                            onEventClick = { inspectedEvent = it; onEventClick(it) }
                                        )
                                        MenuType.INSIGHT -> InsightPanel(
                                            snapshot = snapshot,
                                            signals = if (primarySignal != null) listOf(primarySignal) + secondarySignals else secondarySignals,
                                            theme = theme
                                        )
                                        MenuType.MORE -> MorePanel(snapshot = snapshot, theme = theme)
                                    }
                                }
                            }
                        }

                        // Event Audit Drill-down Dialog
                        inspectedEvent?.let { ev ->
                            EventDetailDialog(event = ev, onDismiss = { inspectedEvent = null })
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
                    CompactEventHeader(event = currentEvent, theme = theme, onClick = onClick)

                    // Section 2: Hero Identity (Portrait + Name + Code + Motto + Tier Badge)
                    CompactHeroIdentity(
                        identity = snapshot.identity,
                        theme = theme,
                        portraitWidth = dimensions.portraitWidth,
                        portraitHeight = dimensions.portraitHeight,
                        semantics = semantics,
                        onClick = onClick
                    )

                    // Section 3: Membership Tier & Status Dual Bands
                    CompactMembershipBands(
                        membership = snapshot.membership,
                        theme = theme,
                        semantics = semantics,
                        payment = snapshot.payment,
                        onClick = onClick
                    )

                    // Section 4: Compact Decision Metrics (Attendance, Sessions, Workouts)
                    CompactMetricsGrid(
                        attendance = snapshot.attendance,
                        trainer = snapshot.trainer,
                        workout = snapshot.workout,
                        theme = theme,
                        onAttendanceClick = onClick,
                        onSessionsClick = onClick,
                        onWorkoutsClick = onClick
                    )

                    // Section 5: Urgent/Actionable Signal banner
                    val signalText: String? = if (semantics.isUrgent && semantics.urgentMessage != null) {
                        semantics.urgentMessage
                    } else {
                        primarySignal?.title
                            ?: secondarySignals.firstOrNull()?.title
                            ?: snapshot.issues.firstOrNull()?.description
                    }

                    // Event evidence belongs inside the canonical member card.
                    // Do not replace this card with a second detail/event interface.
                    CompactEventContext(
                        event = currentEvent,
                        snapshot = snapshot,
                        theme = theme,
                        isUrgent = semantics.isUrgent,
                        signalText = signalText,
                        onClick = onClick
                    )

                    // Section 6: Contextual Primary CTA Button
                    val ctaLabel = resolveDynamicCtaLabel(
                        snapshot = snapshot,
                        currentEvent = currentEvent,
                        semantics = semantics,
                        cta = cta
                    )

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

private fun resolveDynamicCtaLabel(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    semantics: MemberSemanticStyle,
    cta: SignalAction?
): String {
    val due = snapshot.payment?.totalOutstanding ?: 0.0
    val isOverdue = due > 0
    val eventAction = when (currentEvent?.eventType) {
        EventType.PAYMENT_OVERDUE, EventType.PAYMENT_DUE, EventType.PAYMENT_FAILED -> "Collect Payment →"
        EventType.PAYMENT_PARTIAL -> "Collect Balance →"
        EventType.TRIAL_STARTED, EventType.TRIAL_EXPIRED -> "Convert Trial →"
        EventType.TRIAL_CONVERTED -> "Activate Plan →"
        EventType.FREEZE_STARTED -> "View Freeze →"
        EventType.FREEZE_ENDED -> "View Plan →"
        EventType.BANNED -> "View Ban Reason →"
        EventType.BAN_LIFTED -> "View Access →"
        EventType.TRAINER_SESSION_SCHEDULED -> "View Trainer Session →"
        EventType.TRAINER_SESSION_STARTED -> "View Live Session →"
        EventType.TRAINER_SESSION_MISSED -> "Reschedule Session →"
        EventType.WORKOUT_COMPLETED, EventType.WORKOUT_STARTED -> "View Workout →"
        EventType.WORKOUT_SKIPPED -> "View Workout Pattern →"
        EventType.SERVICE_PURCHASE -> "View Service →"
        EventType.MAINTENANCE -> "View Maintenance →"
        else -> null
    }
    return when {
        eventAction != null -> eventAction
        isOverdue -> {
            val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))
            "Collect ₹" + formatter.format(due.toInt()) + " →"
        }
        semantics.stateVisual == MemberStateVisual.EXPIRED -> "Renew Plan Now →"
        semantics.stateVisual == MemberStateVisual.CRITICAL_ALERT -> "Review Alert →"
        snapshot.trainer != null && (snapshot.trainer.sessionsTotal - snapshot.trainer.sessionsUsed) > 0 -> "Schedule Session →"
        cta != null -> "${cta.label} →"
        else -> "View Member Profile →"
    }
}

@Composable
private fun CompactEventHeader(
    event: MemberEvent?,
    theme: ThemeId,
    onClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val eventType = event?.eventType ?: EventType.CHECK_IN
    val eventColor = ThemeResolver.resolveEventBadgeColor(eventType, colors)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Event badge
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(eventColor)
                .padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(BADGymTheme.colors.textOnAccent)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "+ " + eventType.displayLabel(),
                color = BADGymTheme.colors.textOnAccent,
                fontSize = 13.sp,
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
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "• ${theme.timeRelative}",
                color = colors.textMuted,
                fontSize = 13.sp,
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
    semantics: MemberSemanticStyle,
    onClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MemberPhoto(
            photoUrl = identity.photoUrl,
            tier = identity.tier,
            width = portraitWidth,
            height = portraitHeight,
            showVerified = identity.isVerified,
            memberName = identity.name
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.5.dp)
        ) {
            // Tier Badge Chip
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(semantics.tierBgColor)
                    .border(0.8.dp, semantics.tierBorderColor, RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.5.dp)
            ) {
                Icon(
                    imageVector = semantics.tierIcon,
                    contentDescription = null,
                    tint = semantics.tierAccentColor,
                    modifier = Modifier.size(10.dp)
                )
                Text(
                    text = semantics.tierLabel,
                    color = semantics.tierAccentColor,
                    fontSize = 12.sp,
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
                    fontSize = 16.5.sp,
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
                        tint = colors.vip,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Text(
                text = identity.code ?: "BG---",
                color = colors.textSecondary,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = theme.motto,
                color = colors.mottoColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
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
    theme: ThemeId,
    semantics: MemberSemanticStyle,
    payment: PaymentSummary? = null,
    onClick: () -> Unit = {}
) {
    val planName = membership?.planName ?: "Plan not recorded"
    val daysRemaining = membership?.daysRemaining

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Plan Band
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(semantics.tierBgColor)
                .border(0.8.dp, semantics.tierBorderColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 4.5.dp),
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
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${membership?.planType ?: "12M"}",
                    color = semantics.tierAccentColor.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Active Status Band (Urgent state overrides default styling)
        val statusSubtext = when (semantics.stateVisual) {
            MemberStateVisual.EXPIRED -> if (daysRemaining != null) "${daysRemaining}d left • Renew" else "Renew required"
            MemberStateVisual.PAYMENT_OVERDUE -> payment?.overdueDays?.let { "${it}d overdue" } ?: "Overdue"
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
                .padding(horizontal = 6.dp, vertical = 4.5.dp),
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
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = statusSubtext,
                    color = semantics.stateAccentColor.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * 3 Core Decision Metrics for Home Cockpit: Attendance, Sessions, Workouts.
 * Explicit semantics, unique non-overlapping dimensions, interactive tap targets.
 */
@Composable
private fun CompactMetricsGrid(
    attendance: AttendanceSummary?,
    trainer: TrainerSummary?,
    workout: WorkoutSummary?,
    theme: ThemeId,
    onAttendanceClick: () -> Unit = {},
    onSessionsClick: () -> Unit = {},
    onWorkoutsClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Tile 1: Attendance
        val visits = attendance?.visits
        val target = attendance?.target
        val attendancePercent = if (visits != null && target != null && target > 0) ((visits.toFloat() / target) * 100).toInt() else null
        CompactMetricTile(
            theme = theme,
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onAttendanceClick)
        ) {
            Text(
                text = if (visits != null && target != null) "$visits/$target" else "—",
                color = colors.textPrimary,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Black
            )
            Box(
                modifier = Modifier.size(26.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { ((attendancePercent ?: 0) / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxSize(),
                    color = colors.accent,
                    strokeWidth = 2.5.dp,
                    trackColor = colors.surfaceMuted
                )
                Text(
                    text = attendancePercent?.let { "$it%" } ?: "—",
                    color = colors.textPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Attendance",
                color = colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Tile 2: Sessions (PT / Coach)
        val sessionsLeft = trainer?.let { (it.sessionsTotal - it.sessionsUsed).coerceAtLeast(0) }
        CompactMetricTile(
            theme = theme,
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onSessionsClick)
        ) {
            Text(
                text = sessionsLeft?.let { "$it Left" } ?: "—",
                color = colors.accent,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.accentSoft)
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = trainer?.trainerName?.take(7) ?: "Not recorded",
                    color = colors.accent,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "PT Sessions",
                color = colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Tile 3: Workouts
        val duration = workout?.durationMinutes
        CompactMetricTile(
            theme = theme,
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onWorkoutsClick)
        ) {
            Text(
                text = duration?.let { "${it}m" } ?: "—",
                color = colors.textPrimary,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Black
            )
            Row(
                modifier = Modifier
                    .height(18.dp)
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                val barHeights = listOf(0.4f, 0.7f, 1.0f, 0.65f, 0.9f)
                val barColor = colors.accent
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
                fontSize = 12.sp,
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
            .height(62.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surfaceMuted)
            .border(0.8.dp, colors.border.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
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
private fun CompactEventContext(
    event: MemberEvent?,
    snapshot: MemberSnapshot,
    theme: ThemeId,
    isUrgent: Boolean,
    signalText: String?,
    onClick: () -> Unit
) {
    val colors = BADGymTheme.colors
    val metadata = event?.metadata.orEmpty()
    val eventType = event?.eventType

    val facts = when (eventType) {
        EventType.CHECK_IN -> listOf(
            "ARRIVAL" to (metadata["checkInAt"] ?: metadata["at"] ?: theme.timeText),
            "STATUS" to if (metadata["late"]?.equals("true", true) == true) {
                "Late " + (metadata["latenessMinutes"]?.let { "$it min" } ?: "")
            } else "On time"
        )
        EventType.CHECK_OUT -> listOf(
            "CHECK-OUT" to (metadata["checkOutAt"] ?: theme.timeText),
            "SESSION" to (metadata["durationMinutes"]?.let { "$it min" } ?: "Completed")
        )
        EventType.PAYMENT_OVERDUE, EventType.PAYMENT_DUE, EventType.PAYMENT_FAILED, EventType.PAYMENT_PARTIAL -> listOf(
            "OUTSTANDING" to snapshot.payment?.totalOutstanding?.let {
                NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN")).format(it).let { value -> "₹$value" }
            }.orEmpty().ifBlank { "—" },
            "STATUS" to (metadata["overdueDays"]?.let { "$it days overdue" }
                ?: snapshot.payment?.overdueDays?.let { "$it days overdue" }
                ?: eventType.displayLabel())
        )
        EventType.TRIAL_STARTED, EventType.TRIAL_EXPIRED, EventType.TRIAL_CONVERTED, EventType.WALK_IN -> listOf(
            "JOURNEY" to (metadata["stage"] ?: eventType.displayLabel()),
            "EXPIRY / OUTCOME" to (metadata["expiresAt"] ?: metadata["convertedAt"] ?: metadata["expiredAt"] ?: "Pending")
        )
        EventType.FREEZE_STARTED, EventType.FREEZE_ENDED -> listOf(
            "FREEZE" to (metadata["freezeStart"] ?: "Recorded"),
            "END" to (metadata["freezeEnd"] ?: "—")
        )
        EventType.BANNED, EventType.BAN_LIFTED -> listOf(
            "ACCESS" to if (eventType == EventType.BANNED) "Blocked" else "Restored",
            "REASON" to (metadata["reason"] ?: "Recorded restriction")
        )
        EventType.TRAINER_SESSION_SCHEDULED, EventType.TRAINER_SESSION_STARTED,
        EventType.TRAINER_SESSION_COMPLETED, EventType.TRAINER_SESSION_MISSED,
        EventType.TRAINER_SESSION_CANCELLED -> listOf(
            "COACH" to (snapshot.trainer?.trainerName ?: "—"),
            "SESSION" to (metadata["sessionAt"] ?: snapshot.trainer?.nextSessionDate?.toString() ?: eventType.displayLabel())
        )
        EventType.WORKOUT_STARTED, EventType.WORKOUT_COMPLETED, EventType.WORKOUT_SKIPPED, EventType.PR_ACHIEVED -> listOf(
            "ROUTINE" to (snapshot.workout?.currentRoutine ?: "—"),
            "ACTIVITY" to (metadata["durationMinutes"]?.let { "$it min" } ?: eventType.displayLabel())
        )
        EventType.SERVICE_PURCHASE, EventType.SUPPLEMENT_PURCHASE, EventType.NUTRITION -> listOf(
            "SERVICE" to (metadata["serviceName"] ?: snapshot.services.orEmpty().firstOrNull { it.isActive }?.serviceName ?: eventType.displayLabel()),
            "STATUS" to (metadata["status"] ?: "Recorded")
        )
        EventType.MAINTENANCE -> listOf(
            "ASSET" to (metadata["asset"] ?: "Equipment"),
            "STATUS" to (metadata["status"] ?: "Reported")
        )
        else -> {
            val label = signalText ?: eventType?.displayLabel() ?: "Member activity"
            listOf("EVENT" to label, "STATUS" to (snapshot.membership?.planName ?: "Recorded"))
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isUrgent) colors.dangerSoft.copy(alpha = 0.72f)
                else colors.surfaceMuted.copy(alpha = 0.68f)
            )
            .border(
                0.8.dp,
                if (isUrgent) colors.danger.copy(alpha = 0.34f) else colors.border.copy(alpha = 0.62f),
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        facts.take(2).forEach { (label, value) ->
            Column(Modifier.weight(1f)) {
                Text(label, color = if (isUrgent) colors.danger else colors.textMuted, fontSize = 7.5.sp, fontWeight = FontWeight.Black, maxLines = 1)
                Text(value.ifBlank { "—" }, color = colors.textPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun CompactSignalBanner(
    text: String?,
    isCritical: Boolean,
    semantics: MemberSemanticStyle? = null,
    onClick: () -> Unit = {}
) {
    if (text.isNullOrBlank()) {
        Spacer(Modifier.height(2.dp))
        return
    }

    val colors = BADGymTheme.colors
    val bannerBg = if (semantics != null && semantics.isUrgent) {
        semantics.stateAccentColor.copy(alpha = 0.14f)
    } else if (isCritical) {
        colors.dangerSoft
    } else {
        colors.warningSoft
    }

    val bannerBorder = if (semantics != null && semantics.isUrgent) {
        semantics.stateAccentColor.copy(alpha = 0.45f)
    } else if (isCritical) {
        colors.danger.copy(alpha = 0.4f)
    } else {
        colors.warning.copy(alpha = 0.4f)
    }

    val iconTint = if (semantics != null && semantics.isUrgent) {
        semantics.stateAccentColor
    } else if (isCritical) {
        colors.danger
    } else {
        colors.warning
    }

    val textColor = if (semantics != null && semantics.isUrgent) {
        semantics.stateAccentColor
    } else if (isCritical) {
        colors.danger
    } else {
        colors.warning
    }

    val icon = semantics?.stateIcon ?: Icons.Rounded.WarningAmber

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(bannerBg)
            .border(0.8.dp, bannerBorder, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(12.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Intelligent Vertical Navigation Rail for Bounded Detail mode.
 * Real semantic badges derived from MemberSnapshot per Section 5 of Stage 7.2 packet.
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
            MemberMenu(MenuType.SUPPLEMENTS, "Supps", 60, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.NUTRITION, "Diet", 70, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.SERVICES, "Services", 80, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.HISTORY, "History", 90, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.INSIGHT, "Insight", 100, isVisible = true, isEnabled = true, isLocked = false),
            MemberMenu(MenuType.MORE, "More", 110, isVisible = true, isEnabled = true, isLocked = false)
        )
    }

    Column(
        modifier = modifier
            .width(railWidth)
            .clip(RoundedCornerShape(topStart = 22.dp, bottomStart = 22.dp))
            .background(colors.railBackground.copy(alpha = 0.96f))
            .border(
                width = 0.8.dp,
                color = colors.border.copy(alpha = 0.5f),
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

            val microData = getRailMicroData(menu.id, snapshot, primarySignal)

            val bg by animateColorAsState(
                if (isActive) colors.railActiveBackground else Color.Transparent,
                tween(180),
                label = "bounded-rail-bg"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
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
                        .size(if (isActive) 30.dp else 26.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(bg)
                        .then(
                            if (isActionNeeded) {
                                Modifier.border(1.2.dp, colors.danger, RoundedCornerShape(7.dp))
                            } else if (isActive) {
                                Modifier.border(1.dp, colors.border.copy(alpha = 0.6f), RoundedCornerShape(7.dp))
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getRailIcon(menu.id),
                        contentDescription = menu.label,
                        tint = if (isActionNeeded) colors.danger else if (isActive) colors.railActiveIcon else colors.railInactiveIcon,
                        modifier = Modifier.size(if (isActive) 16.dp else 14.dp)
                    )
                    if (isActionNeeded) {
                        Box(
                            Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 1.dp, y = (-1).dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(colors.danger)
                        )
                    }
                }

                Spacer(Modifier.height(1.5.dp))

                Text(
                    text = menu.label,
                    color = if (isActionNeeded) colors.danger else if (isActive) colors.textPrimary else colors.textMuted,
                    fontSize = 11.5.sp,
                    fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (microData.isNotBlank()) {
                    Text(
                        text = microData,
                        color = if (isActionNeeded) colors.danger else if (isActive) colors.accent else colors.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun getRailMicroData(
    menuType: MenuType,
    snapshot: MemberSnapshot,
    primarySignal: IntelligenceSignal? = null
): String {
    return when (menuType) {
        MenuType.HOME -> ""
        MenuType.ATTENDANCE -> snapshot.attendance?.let { "${it.visits}/${it.target ?: 26}" } ?: "16/26"
        MenuType.PLAN -> snapshot.membership?.let { if (!it.isActive || it.daysRemaining <= 0) "Expired" else "${it.daysRemaining}d" } ?: "48d"
        MenuType.PAYMENT -> {
            val due = snapshot.payment?.totalOutstanding ?: 0.0
            if (due > 0) {
                if (due >= 1000) "₹${(due / 1000).toInt()}k!" else "₹${due.toInt()}!"
            } else {
                "Paid"
            }
        }
        MenuType.TRAINER -> snapshot.trainer?.let { "${it.sessionsTotal - it.sessionsUsed} PT" } ?: "8 PT"
        MenuType.WORKOUT -> snapshot.attendance?.avgVisitsPerWeek?.let { "${it.toInt()}/wk" } ?: "4/wk"
        MenuType.SUPPLEMENTS -> if (snapshot.supplements?.hasHistory == true) "Active" else "Stack"
        MenuType.NUTRITION -> if (snapshot.nutrition?.isSubscribed == true) "Active" else "Diet"
        MenuType.SERVICES -> snapshot.services?.let { "${it.count { s -> s.isActive }} act" } ?: "2 act"
        MenuType.HISTORY -> "${snapshot.recentEvents.size.coerceAtLeast(3)} ev"
        MenuType.INSIGHT -> if (primarySignal?.priority == SignalPriority.P0_CRITICAL) "1 P0" else if (snapshot.issues.isNotEmpty()) "${snapshot.issues.size} act" else "AI"
        MenuType.MORE -> "" // Section 5: More has no meaningless badge
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
    MenuType.MORE -> Icons.Rounded.MoreHoriz
}

@Composable
private fun PersistentDetailHeader(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    theme: ThemeId,
    semantics: MemberSemanticStyle,
    dimensions: CompactCardDimensions,
    onClose: () -> Unit,
    onIdentityClick: () -> Unit = {}
) {
    val colors = BADGymTheme.colors
    val eventType = currentEvent?.eventType ?: EventType.CHECK_IN
    val eventColor = ThemeResolver.resolveEventBadgeColor(eventType, colors)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
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
                    .background(eventColor)
                    .padding(horizontal = 7.dp, vertical = 2.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(4.5.dp)
                        .clip(CircleShape)
                        .background(BADGymTheme.colors.textOnAccent)
                )
                Spacer(Modifier.width(3.5.dp))
                Text(
                    text = "+ " + eventType.displayLabel(),
                    color = BADGymTheme.colors.textOnAccent,
                    fontSize = 13.5.sp,
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
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• ${theme.timeRelative}",
                    color = colors.textMuted,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Back / Close icon button
            Box(
                modifier = Modifier
                    .size(26.dp)
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
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Row 2: Hero Identity (Photo + Name + Code + Motto)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onIdentityClick),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MemberPhoto(
                photoUrl = snapshot.identity.photoUrl,
                tier = snapshot.identity.tier,
                width = dimensions.detailPortraitWidth,
                height = dimensions.detailPortraitHeight,
                showVerified = snapshot.identity.isVerified,
                memberName = snapshot.identity.name
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
                        text = snapshot.identity.name,
                        color = colors.textPrimary,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.2).sp,
                        lineHeight = 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (snapshot.identity.isVerified) {
                        Icon(
                            Icons.Rounded.CheckCircle,
                            contentDescription = "Verified",
                            tint = colors.vip,
                            modifier = Modifier.size(12.dp)
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
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "• ${theme.motto.replace("\n", " ")}",
                        color = colors.mottoColor,
                        fontSize = 12.5.sp,
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
            payment = snapshot.payment,
            onClick = onIdentityClick
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
 * Interactive Home Cockpit for Bounded Detail Mode.
 * - 3 Core KPIs (Attendance, Sessions, Workouts)
 * - 1 Trend Card ("Attendance — Last 7 Days")
 * - 1 Primary Intelligence Signal
 * - Isolated Promotion Slot (if present)
 * - Dynamic Contextual CTA Button
 */
@Composable
private fun HomeBoundedContent(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent? = null,
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    theme: ThemeId,
    semantics: MemberSemanticStyle,
    onCta: () -> Unit,
    onNavigate: (MenuType) -> Unit = {}
) {
    val colors = BADGymTheme.colors
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Home owns the large ring + bar chart. The persistent menu strip already
        // carries compact context, so do not repeat the same KPIs below it.
        HomeEnergyPanel(snapshot, theme, { onNavigate(MenuType.ATTENDANCE) }, { onNavigate(MenuType.WORKOUT) })

        val signalText: String? = if (semantics.isUrgent && semantics.urgentMessage != null) semantics.urgentMessage
        else primarySignal?.title ?: secondarySignals.firstOrNull()?.title ?: snapshot.issues.firstOrNull()?.description
        val isCritical = semantics.isUrgent ||
            primarySignal?.priority == SignalPriority.P0_CRITICAL ||
            snapshot.issues.firstOrNull()?.let { it.severity == IssueSeverity.HIGH || it.severity == IssueSeverity.CRITICAL } == true

        CompactSignalBanner(signalText, isCritical, semantics) { onNavigate(MenuType.INSIGHT) }

        snapshot.promotion?.let { promo ->
            PromotionBanner(promo, theme) { onNavigate(MenuType.MORE) }
        }

        val coachLabel = snapshot.trainer?.trainerName ?: "No trainer assigned"
        val routineLabel = snapshot.workout?.currentRoutine ?: "Routine not recorded"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(colors.surfaceMuted.copy(alpha = 0.72f))
                .border(1.dp, colors.border.copy(alpha = 0.72f), RoundedCornerShape(12.dp))
                .clickable { onNavigate(MenuType.TRAINER) }
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.FitnessCenter, null, tint = colors.accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(coachLabel, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(routineLabel, color = colors.textSecondary, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Text("TRAINER", color = colors.accent, fontSize = 9.sp, fontWeight = FontWeight.Black)
        }

        val ctaLabel = resolveDynamicCtaLabel(snapshot, currentEvent, semantics, cta)
        androidx.compose.material3.Button(
            onClick = onCta,
            modifier = Modifier.fillMaxWidth().height(42.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = colors.accent,
                contentColor = colors.textOnAccent
            )
        ) {
            Text(ctaLabel, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(16.dp))
    }
}

