package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.ShortcutTriple

/**
 * Production Member Intelligence card.
 * Reference geometry: 0.70 width/height ratio, 54dp rail, 24dp shell,
 * live Compose content and theme-specific material layers.
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
    onCta: (SignalAction) -> Unit = {},
    onBack: () -> Unit = {}
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
                .background(colors.background)
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            if (snapshot == null) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .background(colors.surface)
                        .border(1.dp, colors.border, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.accent)
                }
            } else {
                CardBody(
                    onBack = onBack,
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
                        .fillMaxSize(),
                    onMenuSelected = onMenuSelected,
                    onCta = onCta
                )
            }
        }
    }
}

@Composable
private fun CardBody(
    onBack: () -> Unit,
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    signals: List<IntelligenceSignal>,
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    theme: ThemeId,
    modifier: Modifier,
    onMenuSelected: (MenuType) -> Unit,
    onCta: (SignalAction) -> Unit
) {
    val colors = BADGymTheme.colors

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(colors.surface)
            .border(
                1.dp,
                colors.border.copy(
                    alpha = if (theme == ThemeId.FUTURISTIC_NEON ||
                        theme == ThemeId.BEAST_MODE ||
                        theme == ThemeId.PURPLE_ROYAL) .92f else .75f
                ),
                RoundedCornerShape(24.dp)
            )
    ) {
        ThemeOrnamentLayer(theme = theme, modifier = Modifier.matchParentSize())

        if (theme == ThemeId.NATURAL_FRESH) {
            Image(
                painter = painterResource(R.drawable.badgym_leaf_cluster_left),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .width(110.dp)
                    .height(110.dp)
            )
            Image(
                painter = painterResource(R.drawable.badgym_leaf_cluster_right),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(110.dp)
                    .height(110.dp)
            )
        }

        Row(Modifier.fillMaxSize()) {
            IntelligenceRail(
                menus = menus,
                activeMenu = activeMenu,
                onMenuSelected = onMenuSelected,
                modifier = Modifier.fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CardHeader(theme = theme, onBack = onBack)
                currentEvent?.let { EventHeader(event = it, theme = theme) }

                HeroMemberSection(
                    identity = snapshot.identity,
                    theme = theme
                )

                AnimatedContent(
                    targetState = activeMenu,
                    transitionSpec = {
                        slideInHorizontally(tween(220)) + fadeIn(tween(180)) togetherWith
                            slideOutHorizontally(tween(160)) + fadeOut(tween(120))
                    },
                    label = "member-menu-content"
                ) { menu ->
                    when (menu) {
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
                        MenuType.ATTENDANCE -> AttendancePanel(snapshot, theme)
                        MenuType.PLAN -> PlanPanel(snapshot, theme)
                        MenuType.PAYMENT -> PaymentPanel(
                            snapshot = snapshot,
                            theme = theme,
                            onCtaClick = { cta?.let(onCta) }
                        )
                        MenuType.TRAINER -> TrainerPanel(snapshot, theme)
                        MenuType.WORKOUT -> WorkoutPanel(snapshot, theme)
                        MenuType.SUPPLEMENTS -> SupplementsPanel(snapshot, theme)
                        MenuType.NUTRITION -> NutritionPanel(snapshot, theme)
                        MenuType.SERVICES -> ServicesPanel(snapshot, theme)
                        MenuType.HISTORY -> HistoryPanel(snapshot, theme)
                        MenuType.INSIGHT -> InsightPanel(snapshot, signals, theme)
                    }
                }

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
    val membership = snapshot.membership

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        MembershipTierStatus(
            membership = membership,
            theme = theme
        )

        CardMetricsGrid(
            attendance = snapshot.attendance,
            payment = snapshot.payment,
            workoutsCount = snapshot.workout?.durationMinutes ?: 12,
            theme = theme
        )

        ShortcutTriple(
            trainer = snapshot.trainer?.trainerName ?: "Unassigned",
            workout = snapshot.workout?.currentRoutine ?: "Rest Day",
            services = (snapshot.services?.count { it.isActive } ?: 0).toString() + " Active"
        )

        (primarySignal ?: signals.firstOrNull())?.let {
            IntelligenceSignalCard(signal = it, emphasized = false)
        }

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
