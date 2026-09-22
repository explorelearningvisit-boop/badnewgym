package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalPriority
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.ShortcutTriple

/**
 * Pixel-perfect implementation of the 8-theme BAD GYM Member Intelligence Card.
 * Adheres strictly to docs/PIXEL_PERFECT_CARD_SPEC.md:
 * - 3:4 card ratio design
 * - 8dp outer shell gutter
 * - 54dp vertical navigation rail
 * - Responsive layout with verified member identity & 8 distinctive themes
 */
@Composable
fun PixelPerfectMemberCard(
    snapshot: MemberSnapshot?,
    currentEvent: MemberEvent?,
    signals: List<IntelligenceSignal>,
    menus: List<MemberMenu>,
    activeMenu: MenuType?,
    modifier: Modifier = Modifier,
    theme: ThemeId = ThemeId.NATURAL_FRESH,
    primarySignal: IntelligenceSignal? = null,
    secondarySignals: List<IntelligenceSignal> = emptyList(),
    cta: SignalAction? = null,
    onMenuSelected: (MenuType) -> Unit = {},
    onThemeSelected: (ThemeId) -> Unit = {},
    onCta: (SignalAction) -> Unit = {}
) {
    BADGymTheme(
        colors = theme.colors(),
        shapes = theme.shapes(),
        motion = theme.motion(),
        elevation = theme.elevation()
    ) {
        val colors = BADGymTheme.colors

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            colors.background,
                            colors.surfaceMuted.copy(alpha = 0.95f)
                        )
                    )
                )
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Top Theme Selector Strip
                ThemeSelectorStrip(
                    currentTheme = theme,
                    onThemeSelected = onThemeSelected
                )

                // 2. Main Card Shell
                if (snapshot == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(colors.surface)
                            .border(1.dp, colors.border.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = colors.accent,
                                strokeWidth = 3.dp
                            )
                            Text(
                                text = "Loading member intelligence...",
                                color = colors.textSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    CardBody(
                        snapshot = snapshot,
                        currentEvent = currentEvent,
                        signals = signals,
                        menus = menus,
                        activeMenu = activeMenu ?: MenuType.HOME,
                        primarySignal = primarySignal,
                        secondarySignals = secondarySignals,
                        cta = cta,
                        theme = theme,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onMenuSelected = onMenuSelected,
                        onCta = onCta
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeSelectorStrip(
    currentTheme: ThemeId,
    onThemeSelected: (ThemeId) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemeId.entries.forEachIndexed { index, theme ->
            val isSelected = theme == currentTheme
            val chipBg = if (isSelected) colors.accent else colors.surface.copy(alpha = 0.85f)
            val textColor = if (isSelected) colors.textOnAccent else colors.textSecondary
            val borderColor = if (isSelected) colors.accent else colors.border.copy(alpha = 0.6f)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(chipBg)
                    .border(1.dp, borderColor, RoundedCornerShape(20.dp))
                    .clickable { onThemeSelected(theme) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${index + 1}  ${theme.title.uppercase()}",
                    color = textColor,
                    fontSize = 9.sp,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                    letterSpacing = 0.4.sp
                )
            }
        }
    }
}

@Composable
private fun CardBody(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    signals: List<IntelligenceSignal>,
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    theme: ThemeId,
    modifier: Modifier = Modifier,
    onMenuSelected: (MenuType) -> Unit,
    onCta: (SignalAction) -> Unit
) {
    val colors = BADGymTheme.colors

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(colors.surface)
            .border(1.dp, colors.border.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Left: 54dp Navigation Rail
            IntelligenceRail(
                menus = menus,
                activeMenu = activeMenu,
                onMenuSelected = onMenuSelected,
                modifier = Modifier.fillMaxHeight()
            )

            // Right: Content Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header (BAD GYM Branding + Slogan)
                CardHeader(
                    theme = theme,
                    memberPhotoUrl = snapshot.identity.photoUrl,
                    eventTime = currentEvent?.occurredAt ?: System.currentTimeMillis()
                )

                // Hero Member Info (Verified, ID, Motto, Photo)
                HeroMemberSection(
                    identity = snapshot.identity,
                    theme = theme
                )

                // Animated Menu Switcher (Fade + Horizontal Slide)
                AnimatedContent(
                    targetState = activeMenu,
                    transitionSpec = {
                        slideInHorizontally(tween(220)) + fadeIn(tween(180)) togetherWith
                            slideOutHorizontally(tween(160)) + fadeOut(tween(120))
                    },
                    label = "menuContent"
                ) { targetMenu ->
                    when (targetMenu) {
                        MenuType.HOME -> HomeContent(
                            snapshot = snapshot,
                            signals = signals,
                            primarySignal = primarySignal,
                            secondarySignals = secondarySignals,
                            cta = cta,
                            theme = theme,
                            onCta = onCta,
                            onNavigate = onMenuSelected
                        )

                        MenuType.ATTENDANCE -> AttendancePanel(
                            snapshot = snapshot,
                            theme = theme
                        )

                        MenuType.PLAN -> PlanPanel(
                            snapshot = snapshot,
                            theme = theme
                        )

                        MenuType.PAYMENT -> PaymentPanel(
                            snapshot = snapshot,
                            theme = theme,
                            onCtaClick = {
                                cta?.let(onCta) ?: onMenuSelected(MenuType.PAYMENT)
                            }
                        )

                        MenuType.TRAINER -> TrainerPanel(
                            snapshot = snapshot,
                            theme = theme
                        )

                        MenuType.WORKOUT -> WorkoutPanel(
                            snapshot = snapshot,
                            theme = theme
                        )

                        MenuType.SERVICES -> ServicesPanel(
                            snapshot = snapshot,
                            theme = theme
                        )

                        MenuType.INSIGHT -> InsightPanel(
                            snapshot = snapshot,
                            signals = signals,
                            theme = theme
                        )

                        else -> HomeContent(
                            snapshot = snapshot,
                            signals = signals,
                            primarySignal = primarySignal,
                            secondarySignals = secondarySignals,
                            cta = cta,
                            theme = theme,
                            onCta = onCta,
                            onNavigate = onMenuSelected
                        )
                    }
                }

                // Motivational Footer
                CardFooterSection(theme = theme)
            }
        }
    }
}

@Composable
private fun HomeContent(
    snapshot: MemberSnapshot,
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    theme: ThemeId,
    onCta: (SignalAction) -> Unit,
    onNavigate: (MenuType) -> Unit
) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Row 1: Metrics (Attendance, Due, Workouts)
        CardMetricsGrid(
            attendance = snapshot.attendance,
            payment = snapshot.payment,
            workoutsCount = snapshot.workout?.durationMinutes ?: 12,
            theme = theme
        )

        // Row 2: Quick Support Cards (Trainer, Workout, Services)
        ShortcutTriple(
            trainer = snapshot.trainer?.trainerName ?: "Vikas Yadav",
            workout = snapshot.workout?.currentRoutine ?: "Chest & Triceps",
            services = "${snapshot.services?.count { it.isActive } ?: 2} Active"
        )

        // Row 3: Intelligence Signals
        val displaySignal = primarySignal ?: signals.firstOrNull()
        if (displaySignal != null) {
            IntelligenceSignalCard(
                signal = displaySignal,
                emphasized = true
            )
        }

        // Secondary signals
        secondarySignals.take(2).forEach { signal ->
            IntelligenceSignalCard(
                signal = signal,
                emphasized = false
            )
        }

        // Row 4: Primary CTA Button
        ThemedCtaButton(
            theme = theme,
            onClick = {
                if (cta != null) {
                    onCta(cta)
                } else if ((snapshot.payment?.totalOutstanding ?: 0.0) > 0) {
                    onNavigate(MenuType.PAYMENT)
                } else {
                    onNavigate(MenuType.ATTENDANCE)
                }
            }
        )
    }
}
