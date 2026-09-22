package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MemberIntelligenceCard(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal? = null,
    secondarySignals: List<IntelligenceSignal> = emptyList(),
    cta: SignalAction? = null,
    theme: ThemeId,
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    onMenuSelected: (MenuType) -> Unit,
    onCtaClick: () -> Unit,
    gymLive: GymLiveSnapshot = GymLiveSnapshot(),
    gymEvents: List<GymOperationalEvent> = emptyList(),
    modifier: Modifier = Modifier
) {
    val border by animateColorAsState(BADGymTheme.colors.border, label = "card-border")
    BoxWithConstraints(modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 2.dp)) {
        val compact = maxWidth < 600.dp
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(if (compact) 22.dp else 28.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, border.copy(alpha = if (theme.isDark) .85f else .95f))
        ) {
            Box(Modifier.fillMaxWidth().background(background(theme))) {
                LazyColumn(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(12.dp, 12.dp, 12.dp, 18.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    item { Header(snapshot, currentEvent, theme) }
                    item { Identity(snapshot, theme, compact) }
                    item { Navigator(menus, activeMenu, onMenuSelected, compact) }
                    item { LiveBar(currentEvent, gymLive) }
                    item {
                        AnimatedContent(
                            targetState = activeMenu,
                            transitionSpec = { fadeIn(tween(160)) togetherWith fadeOut(tween(100)) },
                            label = "menu-transition"
                        ) { menu ->
                            Content(menu, snapshot, currentEvent, signals, primarySignal, secondarySignals, gymLive, gymEvents, theme, compact)
                        }
                    }
                    item { Related(snapshot, signals, gymEvents, theme) }
                    if (cta != null) item { ThemedCtaButton(theme = theme, onClick = onCtaClick, label = cta.label) }
                }
            }
        }
    }
}

@Composable
private fun Header(snapshot: MemberSnapshot, event: MemberEvent, theme: ThemeId) {
    val c = BADGymTheme.colors
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(theme.brandBrush()), Alignment.Center) {
            Icon(theme.brandIcon(), null, tint = Color.White, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(9.dp))
        Column(Modifier.weight(1f)) {
            Text("BAD GYM", color = c.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Black)
            Text(theme.subtitle.replace("\n", " • "), color = c.textSecondary, fontSize = 8.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(time(event.occurredAt), color = c.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(theme.timeRelative, color = c.textMuted, fontSize = 7.sp)
        }
    }
}

@Composable
private fun Identity(snapshot: MemberSnapshot, theme: ThemeId, compact: Boolean) {
    val c = BADGymTheme.colors
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(c.surface.copy(alpha = if (theme.isDark) .72f else .9f))
            .border(1.dp, c.border.copy(alpha = .7f), RoundedCornerShape(16.dp))
            .padding(9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MemberPhoto(photoUrl = snapshot.identity.photoUrl, tier = snapshot.identity.tier, size = if (compact) 50.dp else 56.dp, showVerified = snapshot.identity.isVerified)
        Spacer(Modifier.width(9.dp))
        Column(Modifier.weight(1f)) {
            Text(snapshot.identity.name, color = c.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(joinData(snapshot.identity.code, snapshot.membership?.planName, snapshot.membership?.planType), color = c.textSecondary, fontSize = 8.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Pill(if (snapshot.membership?.isActive == true) "ACTIVE" else "EXPIRED", if (snapshot.membership?.isActive == true) c.success else c.danger)
    }
}

@Composable
private fun Navigator(menus: List<MemberMenu>, active: MenuType, select: (MenuType) -> Unit, compact: Boolean) {
    val c = BADGymTheme.colors
    val types = listOf(MenuType.HOME, MenuType.ATTENDANCE, MenuType.PLAN, MenuType.PAYMENT, MenuType.TRAINER, MenuType.WORKOUT, MenuType.SUPPLEMENTS, MenuType.NUTRITION, MenuType.SERVICES, MenuType.HISTORY, MenuType.INSIGHT)
    val display = types.map { t -> menus.firstOrNull { it.id == t } ?: MemberMenu(t, t.defaultLabel, 0, true, true, false) }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(display, key = { it.id.name }) { m ->
            val selected = m.id == active
            Row(
                Modifier.clip(RoundedCornerShape(13.dp))
                    .background(if (selected) c.accent.copy(alpha = .16f) else c.surfaceMuted.copy(alpha = .72f))
                    .border(1.dp, if (selected) c.accent.copy(alpha = .65f) else c.border.copy(alpha = .7f), RoundedCornerShape(13.dp))
                    .clickable(enabled = m.isEnabled && !m.isLocked) { select(m.id) }
                    .padding(horizontal = if (compact) 8.dp else 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(menuIcon(m.id), null, tint = if (selected) c.accent else c.textSecondary, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(m.label, color = if (selected) c.textPrimary else c.textSecondary, fontSize = 8.sp, fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold)
                if (m.badgeCount > 0) Text(m.badgeCount.toString(), color = c.danger, fontSize = 7.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(start = 3.dp))
            }
        }
    }
}

@Composable
private fun LiveBar(event: MemberEvent, live: GymLiveSnapshot) {
    val c = BADGymTheme.colors
    val online = live.powerStatus.equals("ONLINE", true)
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
            .background(c.accent.copy(alpha = .08f))
            .border(1.dp, c.accent.copy(alpha = .25f), RoundedCornerShape(14.dp))
            .padding(9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(if (online) c.success else c.danger))
        Spacer(Modifier.width(7.dp))
        Column(Modifier.weight(1f)) {
            Text("NOW • " + event.eventType.displayLabel(), color = c.textPrimary, fontSize = 8.5.sp, fontWeight = FontWeight.Black)
            Text(live.activeMembers.toString() + " active • " + live.checkInsToday + " in • " + live.activeTrainers + " trainers", color = c.textSecondary, fontSize = 7.sp)
        }
        Text(if (online) "POWER OK" else "POWER ALERT", color = if (online) c.success else c.danger, fontSize = 7.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun Content(
    menu: MenuType,
    s: MemberSnapshot,
    event: MemberEvent,
    signals: List<IntelligenceSignal>,
    primary: IntelligenceSignal?,
    secondary: List<IntelligenceSignal>,
    live: GymLiveSnapshot,
    gymEvents: List<GymOperationalEvent>,
    theme: ThemeId,
    compact: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        when (menu) {
            MenuType.HOME -> Home(s, primary, secondary, live, gymEvents, theme, compact)
            MenuType.ATTENDANCE -> Attendance(s)
            MenuType.PLAN -> Plan(s)
            MenuType.PAYMENT -> Payment(s)
            MenuType.TRAINER -> Trainer(s)
            MenuType.WORKOUT -> Workout(s)
            MenuType.SUPPLEMENTS -> Supplements(s)
            MenuType.NUTRITION -> Nutrition(s)
            MenuType.SERVICES -> Services(s)
            MenuType.HISTORY -> History(s, event, gymEvents, compact)
            MenuType.INSIGHT -> Insight(s, signals, theme, compact)
        }
    }
}

@Composable
private fun Home(s: MemberSnapshot, primary: IntelligenceSignal?, secondary: List<IntelligenceSignal>, live: GymLiveSnapshot, events: List<GymOperationalEvent>, theme: ThemeId, compact: Boolean) {
    val c = BADGymTheme.colors
    val a = s.attendance
    val p = s.payment
    val m = s.membership
    Title("MEMBER COCKPIT", "Member state + live gym context")
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Metric(Modifier.weight(1f), "VISITS", a?.visits?.toString() ?: "—", (a?.streakDays ?: 0).toString() + "d streak", c.accent)
        Metric(Modifier.weight(1f), "DUE", rupees(p?.totalOutstanding ?: 0.0), if ((p?.overdueDays ?: 0) > 0) p?.overdueDays.toString() + "d late" else "Clear", if ((p?.totalOutstanding ?: 0.0) > 0) c.danger else c.success)
        Metric(Modifier.weight(1f), "PLAN", (m?.daysRemaining ?: 0).toString() + "d", m?.planType ?: "—", c.info)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Metric(Modifier.weight(1f), "GYM IN", live.activeMembers.toString(), live.checkInsToday.toString() + " check-ins", c.success)
        Metric(Modifier.weight(1f), "REVENUE", rupees(live.todayRevenue), live.todayPayments.toString() + " payments", c.warning)
        Metric(Modifier.weight(1f), "ISSUES", live.unresolvedAlerts.toString(), live.openMaintenance.toString() + " maintenance", c.danger)
    }
    primary?.let { Signal(it, theme) }
    secondary.take(2).forEach { Signal(it, theme) }
    Events(events.take(if (compact) 3 else 5))
}

@Composable
private fun Attendance(s: MemberSnapshot) {
    val a = s.attendance
    val c = BADGymTheme.colors
    Title("ATTENDANCE", "Access + consistency")
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Metric(Modifier.weight(1f), "PERIOD", (a?.visits ?: 0).toString() + "/" + (a?.target ?: 0), a?.periodName ?: "Current", c.accent)
        Metric(Modifier.weight(1f), "LIFETIME", a?.lifetimeVisits?.toString() ?: "0", "total visits", c.info)
        Metric(Modifier.weight(1f), "STREAK", (a?.streakDays ?: 0).toString(), "days", c.success)
    }
    Rows(listOf("Preferred slot" to (a?.preferredSlot ?: "Not set"), "Average / week" to (a?.avgVisitsPerWeek?.let { String.format("%.1f", it) } ?: "—"), "Last visit" to (a?.lastVisitAt?.let(::dateTime) ?: "No visit recorded")))
}

@Composable
private fun Plan(s: MemberSnapshot) {
    val m = s.membership
    val c = BADGymTheme.colors
    Title("MEMBERSHIP PLAN", "Lifecycle + renewal")
    Status(m?.planName ?: "No plan", if (m?.isActive == true) "ACTIVE" else "EXPIRED", (m?.daysRemaining ?: 0).toString() + " days remaining", if (m?.isActive == true) c.success else c.danger)
    Rows(listOf("Type" to (m?.planType ?: "—"), "Renewals" to (m?.renewalCount?.toString() ?: "0"), "Freeze" to (m?.freezeUsedDays ?: 0).toString() + "/" + (m?.freezeAllowanceDays ?: 0) + " days", "Expiry" to (m?.expiryDate?.let(::dateTime) ?: "—")))
}

@Composable
private fun Payment(s: MemberSnapshot) {
    val p = s.payment
    val c = BADGymTheme.colors
    Title("PAYMENT", "Collections + transaction evidence")
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Metric(Modifier.weight(1f), "OUTSTANDING", rupees(p?.totalOutstanding ?: 0.0), if ((p?.overdueDays ?: 0) > 0) p?.overdueDays.toString() + "d overdue" else "Clear", if ((p?.totalOutstanding ?: 0.0) > 0) c.danger else c.success)
        Metric(Modifier.weight(1f), "LIFETIME", rupees(p?.lifetimePaid ?: 0.0), "paid", c.accent)
        Metric(Modifier.weight(1f), "LAST", rupees(p?.lastPaymentAmount ?: 0.0), p?.lastPaymentMethod ?: "—", c.info)
    }
    Rows(listOf("Due date" to (p?.dueDate?.let(::dateTime) ?: "—"), "Last payment" to (p?.lastPaymentDate?.let(::dateTime) ?: "—")))
}

@Composable
private fun Trainer(s: MemberSnapshot) {
    val t = s.trainer
    val c = BADGymTheme.colors
    Title("TRAINER", "Sessions + next commitment")
    Status(t?.trainerName ?: "No trainer assigned", (t?.sessionsUsed ?: 0).toString() + "/" + (t?.sessionsTotal ?: 0), t?.focus ?: "Training focus not set", c.info)
    Rows(listOf("Next session" to (t?.nextSessionDate?.let(::dateTime) ?: "Not scheduled"), "Last session" to (t?.lastSessionDate?.let(::dateTime) ?: "—"), "Rating" to (t?.rating?.let { String.format("%.1f/5", it) } ?: "—")))
}

@Composable
private fun Workout(s: MemberSnapshot) {
    val w = s.workout
    Title("WORKOUT", "Routine + recent training")
    Status(w?.currentRoutine ?: "No active routine", w?.durationMinutes?.let { it.toString() + " min" } ?: "—", w?.calories?.let { it.toString() + " kcal" } ?: "Training data pending", BADGymTheme.colors.accent)
    Rows(listOf("Last workout" to (w?.lastWorkoutDate?.let(::dateTime) ?: "—")))
}

@Composable
private fun Supplements(s: MemberSnapshot) {
    val x = s.supplements
    Title("SUPPLEMENTS", "Purchase history")
    Status(x?.lastPurchaseName ?: "No purchase recorded", rupees(x?.lastPurchasePrice ?: 0.0), x?.brand ?: "No brand recorded", BADGymTheme.colors.warning)
    Rows(listOf("Last purchase" to (x?.lastPurchaseDate?.let(::dateTime) ?: "—")))
}

@Composable
private fun Nutrition(s: MemberSnapshot) {
    val n = s.nutrition
    Title("NUTRITION", "Plan + renewal")
    Status(n?.planName ?: "No nutrition plan", if (n?.isSubscribed == true) "ACTIVE" else "INACTIVE", rupees(n?.monthlyPrice ?: 0.0) + "/month", BADGymTheme.colors.success)
    Rows(listOf("Renewal" to (n?.renewalDate?.let(::dateTime) ?: "—")))
}

@Composable
private fun Services(s: MemberSnapshot) {
    val c = BADGymTheme.colors
    Title("SERVICES", "Add-ons + expiry")
    if (s.services.isNullOrEmpty()) Empty("No services recorded")
    s.services.orEmpty().take(5).forEach { x ->
        EventRow(if (x.isActive) Icons.Rounded.CheckCircle else Icons.Rounded.Block, x.serviceName, (if (x.isActive) "Active" else "Inactive") + " • " + (x.expiryDate?.let(::dateTime) ?: "No expiry"), if (x.isActive) c.success else c.textMuted)
    }
}

@Composable
private fun History(s: MemberSnapshot, event: MemberEvent, gymEvents: List<GymOperationalEvent>, compact: Boolean) {
    Title("HISTORY", "Member + gym evidence")
    s.recentEvents.take(if (compact) 4 else 6).forEach { EventRow(eventIcon(it.eventType), it.eventType.displayLabel(), dateTime(it.occurredAt), BADGymTheme.colors.info) }
    gymEvents.take(if (compact) 3 else 5).forEach { EventRow(operationalIcon(it.category), it.title, it.status.name + " • " + (it.location ?: "Gym"), severity(it.severity)) }
}

@Composable
private fun Insight(s: MemberSnapshot, signals: List<IntelligenceSignal>, theme: ThemeId, compact: Boolean) {
    Title("INTELLIGENCE", "Signals + evidence")
    if (signals.isEmpty()) Empty("No active signals")
    signals.take(if (compact) 5 else 8).forEach { Signal(it, theme) }
    s.issues.take(3).forEach { EventRow(Icons.Rounded.ReportProblem, it.description, it.severity.name, issueColor(it.severity)) }
}

@Composable
private fun Related(s: MemberSnapshot, signals: List<IntelligenceSignal>, events: List<GymOperationalEvent>, theme: ThemeId) {
    val key = s.identity.code
    val related = events.filter { it.relatedMemberId == key }.take(2)
    if (related.isEmpty() && signals.isEmpty()) return
    Title("RELATED SIGNALS", "Evidence behind this member view")
    related.forEach { EventRow(operationalIcon(it.category), it.title, it.detail, severity(it.severity)) }
    if (related.isEmpty()) signals.take(2).forEach { Signal(it, theme) }
}

@Composable
private fun Events(events: List<GymOperationalEvent>) {
    if (events.isEmpty()) return
    Title("GYM PULSE", "Live operational context")
    events.forEach { EventRow(operationalIcon(it.category), it.title, it.detail, severity(it.severity)) }
}

@Composable
private fun Signal(signal: IntelligenceSignal, theme: ThemeId) {
    val c = BADGymTheme.colors
    val accent = when (signal.priority) {
        SignalPriority.P0_CRITICAL, SignalPriority.P1_ACTION_REQUIRED -> c.danger
        SignalPriority.P2_IMPORTANT -> c.warning
        SignalPriority.P3_BACKGROUND -> c.success
    }
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(accent.copy(alpha = .08f)).border(1.dp, accent.copy(alpha = .3f), RoundedCornerShape(14.dp)).padding(9.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(signalIcon(signal.category), null, tint = accent, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(signal.title, color = c.textPrimary, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(signal.subtitle ?: signal.value ?: "Action available", color = c.textSecondary, fontSize = 7.5.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun Metric(modifier: Modifier, label: String, value: String, helper: String, accent: Color) {
    val c = BADGymTheme.colors
    Column(modifier.height(74.dp).clip(RoundedCornerShape(13.dp)).background(c.surface.copy(alpha = .82f)).border(1.dp, c.border.copy(alpha = .7f), RoundedCornerShape(13.dp)).padding(8.dp), verticalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = c.textMuted, fontSize = 6.5.sp, fontWeight = FontWeight.Black)
        Text(value, color = c.textPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(helper, color = accent, fontSize = 7.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun Status(title: String, value: String, helper: String, accent: Color) {
    val c = BADGymTheme.colors
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp)).background(accent.copy(alpha = .08f)).border(1.dp, accent.copy(alpha = .35f), RoundedCornerShape(15.dp)).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Rounded.Bolt, null, tint = accent, modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = c.textPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(helper, color = c.textSecondary, fontSize = 8.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(value, color = accent, fontSize = 10.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun Rows(rows: List<Pair<String, String>>) {
    val c = BADGymTheme.colors
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(13.dp)).background(c.surfaceMuted.copy(alpha = .62f)).border(1.dp, c.border.copy(alpha = .6f), RoundedCornerShape(13.dp))) {
        rows.forEachIndexed { i, pair ->
            Row(Modifier.fillMaxWidth().padding(9.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(pair.first, color = c.textSecondary, fontSize = 8.sp)
                Text(pair.second, color = c.textPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            if (i < rows.lastIndex) Box(Modifier.fillMaxWidth().height(1.dp).background(c.border.copy(alpha = .4f)))
        }
    }
}

@Composable
private fun EventRow(icon: ImageVector, title: String, detail: String, accent: Color) {
    val c = BADGymTheme.colors
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(c.surfaceMuted.copy(alpha = .66f)).border(1.dp, c.border.copy(alpha = .55f), RoundedCornerShape(12.dp)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = accent, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = c.textPrimary, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(detail, color = c.textSecondary, fontSize = 7.5.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable private fun Empty(text: String) { Text(text, color = BADGymTheme.colors.textMuted, fontSize = 9.sp, modifier = Modifier.padding(vertical = 8.dp)) }
@Composable private fun Title(title: String, subtitle: String) {
    Column {
        Text(title, color = BADGymTheme.colors.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = .8.sp)
        Text(subtitle, color = BADGymTheme.colors.textMuted, fontSize = 7.5.sp)
    }
}
@Composable private fun Pill(text: String, color: Color) {
    Text(text, color = color, fontSize = 7.sp, fontWeight = FontWeight.Black, modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(color.copy(alpha = .12f)).padding(horizontal = 8.dp, vertical = 5.dp))
}

private fun signalIcon(c: SignalCategory): ImageVector = when (c) {
    SignalCategory.PAYMENT -> Icons.Rounded.Payments
    SignalCategory.MEMBERSHIP -> Icons.Rounded.CardMembership
    SignalCategory.ATTENDANCE -> Icons.Rounded.CalendarMonth
    SignalCategory.TRAINER -> Icons.Rounded.Person
    SignalCategory.SAFETY -> Icons.Rounded.Shield
    SignalCategory.ENGAGEMENT -> Icons.Rounded.Favorite
    SignalCategory.COMPLAINT -> Icons.Rounded.ReportProblem
    SignalCategory.UPSELL -> Icons.Rounded.AutoAwesome
    SignalCategory.GENERAL -> Icons.Rounded.Insights
}
private fun menuIcon(t: MenuType): ImageVector = when (t) {
    MenuType.HOME -> Icons.Rounded.Home
    MenuType.ATTENDANCE -> Icons.Rounded.CalendarToday
    MenuType.PLAN -> Icons.Rounded.CardMembership
    MenuType.PAYMENT -> Icons.Rounded.Payments
    MenuType.TRAINER -> Icons.Rounded.Person
    MenuType.WORKOUT -> Icons.Rounded.FitnessCenter
    MenuType.SUPPLEMENTS -> Icons.Rounded.Restaurant
    MenuType.NUTRITION -> Icons.Rounded.Restaurant
    MenuType.SERVICES -> Icons.Rounded.MoreHoriz
    MenuType.HISTORY -> Icons.Rounded.History
    MenuType.INSIGHT -> Icons.Rounded.AutoAwesome
}
private fun eventIcon(t: EventType): ImageVector = when (t) {
    EventType.CHECK_IN, EventType.CHECK_OUT -> Icons.Rounded.Login
    EventType.PAYMENT, EventType.PAYMENT_FAILED -> Icons.Rounded.Payments
    EventType.TRAINER_SESSION -> Icons.Rounded.Person
    EventType.WORKOUT -> Icons.Rounded.FitnessCenter
    EventType.RENEWAL, EventType.REACTIVATION -> Icons.Rounded.Autorenew
    EventType.COMPLAINT, EventType.MAINTENANCE -> Icons.Rounded.ReportProblem
    else -> Icons.Rounded.Bolt
}
private fun operationalIcon(c: GymOperationalCategory): ImageVector = when (c) {
    GymOperationalCategory.ACCESS -> Icons.Rounded.Login
    GymOperationalCategory.PAYMENT, GymOperationalCategory.FINANCE -> Icons.Rounded.Payments
    GymOperationalCategory.MEMBERSHIP -> Icons.Rounded.CardMembership
    GymOperationalCategory.TRAINER -> Icons.Rounded.Person
    GymOperationalCategory.EQUIPMENT, GymOperationalCategory.MAINTENANCE -> Icons.Rounded.ReportProblem
    GymOperationalCategory.CLEANING -> Icons.Rounded.Home
    GymOperationalCategory.POWER -> Icons.Rounded.Bolt
    GymOperationalCategory.FACILITY -> Icons.Rounded.Home
    GymOperationalCategory.SECURITY -> Icons.Rounded.Shield
    GymOperationalCategory.SYSTEM -> Icons.Rounded.Settings
}
@Composable
private fun severity(p: SignalPriority): Color = when (p) {
    SignalPriority.P0_CRITICAL, SignalPriority.P1_ACTION_REQUIRED -> BADGymTheme.colors.danger
    SignalPriority.P2_IMPORTANT -> BADGymTheme.colors.warning
    SignalPriority.P3_BACKGROUND -> BADGymTheme.colors.success
}
@Composable
private fun issueColor(s: IssueSeverity): Color = when (s) {
    IssueSeverity.LOW -> BADGymTheme.colors.success
    IssueSeverity.MEDIUM -> BADGymTheme.colors.warning
    IssueSeverity.HIGH, IssueSeverity.CRITICAL -> BADGymTheme.colors.danger
}
private fun rupees(v: Double): String = if (v <= 0.0) "₹0" else "₹" + NumberFormat.getNumberInstance(Locale("en", "IN")).format(v.toInt())
private fun time(v: Long): String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(v))
private fun dateTime(v: Long): String = SimpleDateFormat("dd MMM • h:mm a", Locale.getDefault()).format(Date(v))
private fun joinData(vararg values: String?): String = values.filterNotNull().filter { it.isNotBlank() }.joinToString(" • ")
private fun background(t: ThemeId): Brush = when (t) {
    ThemeId.NATURAL_FRESH -> Brush.verticalGradient(listOf(Color.White, Color(0xFFF6FCF8)))
    ThemeId.FUTURISTIC_NEON -> Brush.verticalGradient(listOf(Color(0xFF071426), Color(0xFF020712)))
    ThemeId.MINIMAL_DARK -> Brush.verticalGradient(listOf(Color(0xFF15191F), Color(0xFF0B0D11)))
    ThemeId.GLASSMORPHISM -> Brush.verticalGradient(listOf(Color(0xEFFFFFFF), Color(0xBDEAF5FF)))
    ThemeId.PREMIUM_3D -> Brush.verticalGradient(listOf(Color(0xFF1A150C), Color(0xFF080704)))
    ThemeId.VIBRANT_GRADIENT -> Brush.verticalGradient(listOf(Color.White, Color(0xFFFFF8FC)))
    ThemeId.BEAST_MODE -> Brush.verticalGradient(listOf(Color(0xFF1A0508), Color(0xFF090102)))
    ThemeId.PURPLE_ROYAL -> Brush.verticalGradient(listOf(Color(0xFF211039), Color(0xFF0D061A)))
}
private fun ThemeId.brandBrush(): Brush = when (this) {
    ThemeId.NATURAL_FRESH -> Brush.linearGradient(listOf(Color(0xFF22C55E), Color(0xFF15803D)))
    ThemeId.FUTURISTIC_NEON -> Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF0369A1)))
    ThemeId.MINIMAL_DARK -> Brush.linearGradient(listOf(Color(0xFF64748B), Color(0xFF1F2937)))
    ThemeId.GLASSMORPHISM -> Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
    ThemeId.PREMIUM_3D -> Brush.linearGradient(listOf(Color(0xFFE5B842), Color(0xFF8B6516)))
    ThemeId.VIBRANT_GRADIENT -> Brush.linearGradient(listOf(Color(0xFFF97316), Color(0xFFEC4899)))
    ThemeId.BEAST_MODE -> Brush.linearGradient(listOf(Color(0xFFFF1E27), Color(0xFF660000)))
    ThemeId.PURPLE_ROYAL -> Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF4C1D95)))
}
private fun ThemeId.brandIcon(): ImageVector = when (this) {
    ThemeId.NATURAL_FRESH -> Icons.Rounded.Eco
    ThemeId.FUTURISTIC_NEON -> Icons.Rounded.Bolt
    ThemeId.MINIMAL_DARK -> Icons.Rounded.FitnessCenter
    ThemeId.GLASSMORPHISM -> Icons.Rounded.Favorite
    ThemeId.PREMIUM_3D -> Icons.Rounded.WorkspacePremium
    ThemeId.VIBRANT_GRADIENT -> Icons.Rounded.LocalFireDepartment
    ThemeId.BEAST_MODE -> Icons.Rounded.Pets
    ThemeId.PURPLE_ROYAL -> Icons.Rounded.Diamond
}
