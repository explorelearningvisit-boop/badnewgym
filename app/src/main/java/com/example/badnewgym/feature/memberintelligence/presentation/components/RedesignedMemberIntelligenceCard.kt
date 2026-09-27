package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.dimensions.CompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.domain.engine.MemberBusinessStateResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * BAD GYM Member Intelligence V4 visual shell.
 * Light-first, one canonical card, short left core rail + capability-driven right rail.
 * Existing typed menu panels remain the data source of truth.
 */
@Composable
fun RedesignedMemberIntelligenceCard(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    theme: ThemeId,
    primarySignal: IntelligenceSignal? = null,
    secondarySignals: List<IntelligenceSignal> = emptyList(),
    cta: SignalAction? = null,
    dimensions: CompactCardDimensions,
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
    onEngagementApproval: (MemberApprovalRequest, ApprovalStatus) -> Unit = { _, _ -> },
    onCloseDetail: () -> Unit = {},
    isReducedMotion: Boolean = false,
    modifier: Modifier = Modifier
) {
    val event = remember(snapshot, currentEvent) {
        MemberBusinessStateResolver.resolveDisplayEvent(snapshot, currentEvent)
    }

    BADGymTheme(colors = theme.colors(), shapes = theme.shapes(), motion = theme.motion(), elevation = theme.elevation()) {
        val colors = BADGymTheme.colors
        val accent = v4Accent(event, colors)
        val width = if (isDetail) dimensions.detailCardWidth else dimensions.cardWidth
        val height = if (isDetail) dimensions.detailCardHeight else dimensions.cardHeight

        Box(
            modifier = modifier.width(width).height(height)
                .clip(RoundedCornerShape(26.dp))
                .background(colors.surface)
                .border(if (isSelected) 1.8.dp else 1.dp, accent.copy(alpha = .42f), RoundedCornerShape(26.dp))
                .clickable(enabled = !isDetail, onClick = onClick)
        ) {
            Box(Modifier.fillMaxSize().background(accent.copy(alpha = .025f)))
            Row(Modifier.fillMaxSize().padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                V4Rail(RailSide.LEFT, menus, activeMenu, onMenuSelected, Modifier.fillMaxHeight())
                Spacer(Modifier.width(4.dp))
                Column(
                    Modifier.weight(1f).fillMaxHeight()
                        .clip(RoundedCornerShape(21.dp))
                        .background(colors.surface.copy(alpha = .98f))
                        .border(.7.dp, colors.border.copy(alpha = .72f), RoundedCornerShape(21.dp))
                        .padding(7.dp)
                ) {
                    V4Header(event, theme, accent)
                    V4Identity(snapshot, accent, onClick)
                    V4Kpis(snapshot, accent)
                    if (isDetail || activeMenu != MenuType.HOME) V4MenuStrip(activeMenu, accent)
                    Spacer(Modifier.height(5.dp))
                    Box(
                        Modifier.weight(1f).fillMaxWidth()
                            .clip(RoundedCornerShape(17.dp))
                            .background(colors.background.copy(alpha = .52f))
                            .border(.6.dp, colors.divider, RoundedCornerShape(17.dp))
                            .padding(7.dp)
                    ) {
                        AnimatedContent(
                            targetState = activeMenu,
                            transitionSpec = {
                                (slideInHorizontally(tween(190)) + fadeIn(tween(190))) togetherWith
                                    (slideOutHorizontally(tween(130)) + fadeOut(tween(130)))
                            },
                            label = "v4-menu"
                        ) { menu ->
                            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                                when (menu) {
                                    MenuType.HOME -> V4Home(snapshot, event, primarySignal, secondarySignals, accent, cta, onMenuSelected)
                                    MenuType.ATTENDANCE -> AttendancePanel(snapshot, theme, temporalRange, onRangeChange, onEventClick)
                                    MenuType.PLAN -> PlanPanel(snapshot, theme)
                                    MenuType.PAYMENT -> PaymentPanel(snapshot, theme, temporalRange, onRangeChange, onEventClick, onCtaClick)
                                    MenuType.TRAINER -> TrainerPanel(snapshot, theme)
                                    MenuType.WORKOUT -> WorkoutPanel(snapshot, theme, temporalRange, onRangeChange, onEventClick)
                                    MenuType.SUPPLEMENTS -> SupplementsPanel(snapshot, theme)
                                    MenuType.NUTRITION -> NutritionPanel(snapshot, theme)
                                    MenuType.SERVICES -> ServicesPanel(snapshot, theme)
                                    MenuType.HISTORY -> HistoryPanel(snapshot, theme, temporalRange, onRangeChange, onEventClick)
                                    MenuType.INSIGHT -> InsightPanel(snapshot, if (primarySignal != null) listOf(primarySignal) + secondarySignals else secondarySignals, theme)
                                    MenuType.MORE -> MorePanel(snapshot, theme, onMenuSelected)
                                    MenuType.ADVERTISEMENT -> V4AdvertisementPanel(snapshot.promotion)
                                }
                            }
                        }
                    }
                    V4Cta(snapshot, event, cta, accent, onCtaClick)
                }
                Spacer(Modifier.width(4.dp))
                V4Rail(RailSide.RIGHT, menus, activeMenu, onMenuSelected, Modifier.fillMaxHeight())
            }
        }
    }
}

@Composable private fun V4Header(event: MemberEvent?, theme: ThemeId, accent: Color) {
    val c = BADGymTheme.colors
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        V4Pill("+ " + (event?.eventType?.displayLabel()?.uppercase() ?: "MEMBER INTELLIGENCE"), accent, true)
        Column(Modifier.weight(1f)) {
            Text(theme.timeText + " • " + theme.timeRelative, color = c.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text("LIVE DECISION WORKSPACE", color = c.textMuted, fontSize = 7.sp, fontWeight = FontWeight.Black, letterSpacing = .7.sp)
        }
        if (event?.metadata?.containsKey("critical") == true) Icon(Icons.Rounded.WarningAmber, null, tint = c.danger, modifier = Modifier.size(17.dp))
    }
}

@Composable private fun V4Identity(snapshot: MemberSnapshot, accent: Color, onClick: () -> Unit) {
    val c = BADGymTheme.colors
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        MemberPhoto(photoUrl = snapshot.identity.photoUrl, tier = snapshot.identity.tier, width = 84.dp, height = 86.dp, showVerified = snapshot.identity.isVerified, memberName = snapshot.identity.name)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                V4Pill(snapshot.identity.tier.name.replace('_', ' '), accent)
                if (snapshot.identity.isVerified) Icon(Icons.Rounded.CheckCircle, "Verified", tint = c.info, modifier = Modifier.size(13.dp))
            }
            Text(snapshot.identity.name, color = c.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(snapshot.identity.code ?: "Member ID unavailable", color = c.textSecondary, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
            Text(v4Headline(snapshot), color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable private fun V4Kpis(snapshot: MemberSnapshot, accent: Color) {
    val c = BADGymTheme.colors
    val a = snapshot.attendance
    val p = snapshot.trainer
    val w = snapshot.workout
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        V4Kpi(if (a?.target != null) "${a.visits}/${a.target}" else "—", "ATTENDANCE", a?.streakDays?.let { "${it}d streak" } ?: "current period", c.success, Modifier.weight(1f))
        V4Kpi(p?.let { (it.sessionsTotal - it.sessionsUsed).coerceAtLeast(0).toString() } ?: "—", "PT LEFT", p?.trainerName ?: "NOT ENROLLED", Color(0xFF7E22CE), Modifier.weight(1f))
        V4Kpi(w?.durationMinutes?.let { "${it}m" } ?: w?.sessionCount?.toString() ?: "—", "WORKOUT", w?.currentRoutine ?: "NO DATA", accent, Modifier.weight(1f))
    }
}

@Composable private fun V4Kpi(value: String, label: String, detail: String, color: Color, modifier: Modifier) {
    val c = BADGymTheme.colors
    Column(modifier.clip(RoundedCornerShape(11.dp)).background(color.copy(alpha = .075f)).border(.7.dp, color.copy(alpha = .22f), RoundedCornerShape(11.dp)).padding(horizontal = 6.dp, vertical = 5.dp)) {
        Text(value, color = c.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(label, color = color, fontSize = 7.sp, fontWeight = FontWeight.Black, letterSpacing = .45.sp)
        Text(detail, color = c.textMuted, fontSize = 7.7.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable private fun V4MenuStrip(menu: MenuType, accent: Color) {
    val c = BADGymTheme.colors
    val mode = when (menu) {
        MenuType.HOME -> "COCKPIT"
        MenuType.ATTENDANCE -> "NOW • PAST • FUTURE"
        MenuType.PLAN -> "LIFECYCLE • RENEWAL"
        MenuType.PAYMENT -> "DUE • AUDIT • RECURRING"
        MenuType.TRAINER -> "SESSIONS • COACH • SCHEDULE"
        MenuType.WORKOUT -> "ROUTINE • PROGRESS • HISTORY"
        MenuType.SUPPLEMENTS -> "PURCHASE • USAGE • RENEWAL"
        MenuType.NUTRITION -> "PLAN • LOG • PROGRESS"
        MenuType.SERVICES -> "ENTITLEMENT • USAGE • BOOKING"
        MenuType.HISTORY -> "TIMELINE • EVIDENCE • ACTOR"
        MenuType.INSIGHT -> "ACTION • PATTERN • RISK"
        MenuType.MORE -> "TOOLS • RECORDS • SETTINGS"
        MenuType.ADVERTISEMENT -> "ELIGIBLE • RELEVANT • TRACKED"
    }
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(9.dp)).background(accent.copy(alpha = .065f)).padding(horizontal = 8.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(menu.defaultLabel.uppercase(), color = accent, fontSize = 8.5.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.weight(1f))
        Text(mode, color = c.textMuted, fontSize = 7.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable private fun V4Home(snapshot: MemberSnapshot, event: MemberEvent?, primary: IntelligenceSignal?, secondary: List<IntelligenceSignal>, accent: Color, cta: SignalAction?, onMenu: (MenuType) -> Unit) {
    val c = BADGymTheme.colors
    val due = snapshot.payment?.totalOutstanding ?: 0.0
    val dueText = NumberFormat.getNumberInstance(Locale("en", "IN")).format(due.toInt())
    val signal = primary ?: secondary.firstOrNull()

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        event?.let { V4Evidence("CURRENT EVENT", it.eventType.displayLabel(), it.source.name + " • " + v4Date(it.occurredAt), accent) }
        signal?.let {
            V4Evidence("DECISION SIGNAL", it.title, it.subtitle ?: it.evidence.firstOrNull() ?: "Evidence available in Insight.", when (it.priority) {
                SignalPriority.P0_CRITICAL -> c.danger
                SignalPriority.P1_ACTION_REQUIRED -> c.warning
                SignalPriority.P2_IMPORTANT -> c.info
                SignalPriority.P3_BACKGROUND -> c.textSecondary
            })
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            V4Fact("PLAN", snapshot.membership?.planName ?: "—", accent, Modifier.weight(1f))
            V4Fact("PAYMENT", if (due > 0) "₹" + dueText + " DUE" else "CLEAR", if (due > 0) c.danger else c.success, Modifier.weight(1f))
        }
        snapshot.attendance?.weeklyPattern?.takeIf { it.size >= 7 }?.let { pattern ->
            V4Evidence("7-DAY CONSISTENCY", pattern.count { it > 0 }.toString() + "/" + pattern.size + " active days", "Real attendance pattern • current loaded range", accent)
        }
        if (snapshot.issues.isNotEmpty()) {
            V4Evidence("ISSUES", snapshot.issues.size.toString() + " recorded issue" + if (snapshot.issues.size == 1) "" else "s", snapshot.issues.first().description, c.danger)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            V4Action("Attendance", Icons.Rounded.CheckCircle, c.success) { onMenu(MenuType.ATTENDANCE) }
            V4Action("Payment", Icons.Rounded.CreditCard, if (due > 0) c.danger else c.info) { onMenu(MenuType.PAYMENT) }
            V4Action("Insight", Icons.Rounded.AutoAwesome, Color(0xFF7E22CE)) { onMenu(MenuType.INSIGHT) }
        }
        cta?.let { Text("Next action: " + it.label, color = c.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Medium) }
    }
}

@Composable private fun V4Evidence(title: String, headline: String, detail: String, color: Color) {
    val c = BADGymTheme.colors
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = .065f)).border(.8.dp, color.copy(alpha = .25f), RoundedCornerShape(12.dp)).padding(8.dp)) {
        Text(title, color = color, fontSize = 7.5.sp, fontWeight = FontWeight.Black, letterSpacing = .6.sp)
        Text(headline, color = c.textPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(detail, color = c.textMuted, fontSize = 9.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable private fun V4Fact(label: String, value: String, color: Color, modifier: Modifier) {
    val c = BADGymTheme.colors
    Column(modifier.clip(RoundedCornerShape(11.dp)).background(c.surface).border(.7.dp, c.border, RoundedCornerShape(11.dp)).padding(7.dp)) {
        Text(label, color = c.textMuted, fontSize = 7.sp, fontWeight = FontWeight.Black)
        Text(value, color = color, fontSize = 11.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable private fun V4Action(label: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(Modifier.weight(1f).clip(RoundedCornerShape(11.dp)).background(color.copy(alpha = .09f)).border(.8.dp, color.copy(alpha = .24f), RoundedCornerShape(11.dp)).clickable(onClick = onClick).padding(vertical = 7.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = color, modifier = Modifier.size(17.dp))
        Text(label, color = color, fontSize = 7.5.sp, fontWeight = FontWeight.Black)
    }
}

@Composable private fun V4Cta(snapshot: MemberSnapshot, event: MemberEvent?, cta: SignalAction?, accent: Color, onClick: () -> Unit) {
    val c = BADGymTheme.colors
    val due = snapshot.payment?.totalOutstanding ?: 0.0
    val label = when {
        due > 0 -> "Collect ₹" + NumberFormat.getNumberInstance(Locale("en", "IN")).format(due.toInt())
        event?.eventType == EventType.TRAINER_SESSION_SCHEDULED -> "View PT Session"
        event?.eventType == EventType.TRAINER_SESSION_MISSED -> "Reschedule PT"
        event?.eventType == EventType.WORKOUT_STARTED -> "View Workout"
        event?.eventType == EventType.MACHINE_FAULT -> "Review Issue"
        event?.eventType == EventType.MEMBERSHIP_EXPIRED -> "Renew Membership"
        cta != null -> cta.label
        else -> "Open Member Intelligence"
    }
    Box(Modifier.fillMaxWidth().height(37.dp).clip(RoundedCornerShape(12.dp)).background(accent).clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        Text(label + " →", color = c.textOnAccent, fontSize = 10.5.sp, fontWeight = FontWeight.Black)
    }
}

@Composable private fun V4Pill(text: String, color: Color, filled: Boolean = false) {
    val c = BADGymTheme.colors
    Text(text, color = if (filled) c.textOnAccent else color, fontSize = if (filled) 9.sp else 7.5.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis,
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(if (filled) color else color.copy(alpha = .09f)).border(.7.dp, color.copy(alpha = .25f), RoundedCornerShape(8.dp)).padding(horizontal = 7.dp, vertical = 4.dp))
}

@Composable private fun V4Rail(side: RailSide, menus: List<MemberMenu>, active: MenuType, onSelect: (MenuType) -> Unit, modifier: Modifier) {
    val c = BADGymTheme.colors
    val ids = if (side == RailSide.LEFT) listOf(MenuType.HOME, MenuType.ATTENDANCE, MenuType.PLAN, MenuType.PAYMENT, MenuType.MORE)
        else listOf(MenuType.TRAINER, MenuType.WORKOUT, MenuType.SUPPLEMENTS, MenuType.NUTRITION, MenuType.SERVICES, MenuType.HISTORY, MenuType.INSIGHT, MenuType.ADVERTISEMENT)
    val lookup = menus.associateBy { it.id }
    Column(Modifier.width(43.dp).fillMaxHeight().clip(RoundedCornerShape(17.dp)).background(c.railBackground.copy(alpha = .94f)).border(.8.dp, c.border.copy(alpha = .72f), RoundedCornerShape(17.dp)).padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically)) {
        ids.forEach { id ->
            val item = lookup[id]
            if (item?.isVisible == true && item.isEnabled && !item.isLocked) {
                val selected = active == id
                val color = if (selected) c.accent else c.railInactiveIcon
                Column(Modifier.fillMaxWidth().height(41.dp).clip(RoundedCornerShape(10.dp))
                    .background(if (selected) c.railActiveBackground.copy(alpha = .13f) else Color.Transparent)
                    .border(if (selected) 1.dp else 0.dp, if (selected) c.accent.copy(alpha = .32f) else Color.Transparent, RoundedCornerShape(10.dp))
                    .clickable { onSelect(id) }.padding(vertical = 3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(v4MenuIcon(id), null, tint = color, modifier = Modifier.size(17.dp))
                        if (item.hasAlert || item.badgeCount > 0) Box(Modifier.size(6.dp).clip(CircleShape).background(if (item.hasAlert) c.danger else c.accent))
                    }
                    Text(v4Label(id), color = color, fontSize = 6.5.sp, fontWeight = if (selected) FontWeight.Black else FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun V4AdvertisementPanel(promotion: PromotionSlot?) {
    val colors = BADGymTheme.colors
    if (promotion == null) {
        V4Evidence("OFFERS", "No active offer recorded", "Commercial content is hidden until a real promotion exists.", colors.textMuted)
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        V4Evidence("OFFER", promotion.title, promotion.subtitle ?: "Eligible member offer", colors.warning)
        promotion.badge?.let { V4Pill(it, colors.warning) }
    }
}

private fun v4MenuIcon(type: MenuType): ImageVector = when (type) {
    MenuType.HOME -> Icons.Rounded.Home
    MenuType.ATTENDANCE -> Icons.Rounded.CheckCircle
    MenuType.PLAN -> Icons.Rounded.Assignment
    MenuType.PAYMENT -> Icons.Rounded.CreditCard
    MenuType.MORE -> Icons.Rounded.MoreHoriz
    MenuType.TRAINER -> Icons.Rounded.Person
    MenuType.WORKOUT -> Icons.Rounded.FitnessCenter
    MenuType.SUPPLEMENTS -> Icons.Rounded.LocalDrink
    MenuType.NUTRITION -> Icons.Rounded.Restaurant
    MenuType.SERVICES -> Icons.Rounded.MiscellaneousServices
    MenuType.HISTORY -> Icons.Rounded.History
    MenuType.INSIGHT -> Icons.Rounded.AutoAwesome
    MenuType.ADVERTISEMENT -> Icons.Rounded.Campaign
}

private fun v4Label(type: MenuType): String = when (type) {
    MenuType.HOME -> "Home"
    MenuType.ATTENDANCE -> "Attend"
    MenuType.PLAN -> "Plan"
    MenuType.PAYMENT -> "Pay"
    MenuType.MORE -> "More"
    MenuType.TRAINER -> "PT"
    MenuType.WORKOUT -> "Lift"
    MenuType.SUPPLEMENTS -> "Supps"
    MenuType.NUTRITION -> "Diet"
    MenuType.SERVICES -> "Serve"
    MenuType.HISTORY -> "Log"
    MenuType.INSIGHT -> "AI"
    MenuType.ADVERTISEMENT -> "Offer"
}

private fun v4Headline(snapshot: MemberSnapshot): String {
    val due = snapshot.payment?.totalOutstanding ?: 0.0
    return when {
        due > 0 -> "₹" + NumberFormat.getNumberInstance(Locale("en", "IN")).format(due.toInt()) + " payment attention"
        snapshot.trainer != null -> "PT active • " + (snapshot.trainer.sessionsTotal - snapshot.trainer.sessionsUsed).coerceAtLeast(0) + " sessions left"
        snapshot.membership?.isActive == false -> "Membership needs attention"
        else -> "Member intelligence • live state"
    }
}

private fun v4Accent(event: MemberEvent?, colors: com.example.badnewgym.feature.memberintelligence.design.colors.BADGymColors): Color = when (event?.eventType) {
    EventType.PAYMENT_FAILED, EventType.PAYMENT_OVERDUE, EventType.PAYMENT_DUE, EventType.PAYMENT_PARTIAL,
    EventType.MEMBERSHIP_EXPIRED, EventType.BANNED, EventType.INCIDENT_REPORTED, EventType.MACHINE_FAULT -> colors.danger
    EventType.TRAINER_SESSION_SCHEDULED, EventType.TRAINER_SESSION_STARTED, EventType.TRAINER_SESSION_MISSED, EventType.TRAINER_SESSION_COMPLETED -> Color(0xFF7E22CE)
    EventType.WORKOUT_STARTED, EventType.WORKOUT_COMPLETED, EventType.PR_ACHIEVED -> Color(0xFF0D9488)
    EventType.SERVICE_BOOKED, EventType.SERVICE_USED, EventType.SERVICE_PURCHASE, EventType.SERVICE_ACTIVATED -> colors.info
    EventType.FREEZE_STARTED, EventType.FREEZE_ENDED, EventType.MAINTENANCE_STARTED, EventType.CLEANING_STARTED -> colors.warning
    else -> colors.accent
}

private fun v4Date(millis: Long): String = SimpleDateFormat("dd MMM • h:mm a", Locale.getDefault()).format(Date(millis))
