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
import androidx.compose.foundation.horizontalScroll
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MemberDashboard(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    signals: List<IntelligenceSignal>,
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    onMenuSelected: (MenuType) -> Unit,
    modifier: Modifier = Modifier,
    theme: ThemeId = ThemeId.NATURAL_FRESH,
    primarySignal: IntelligenceSignal? = null,
    secondarySignals: List<IntelligenceSignal> = emptyList(),
    cta: SignalAction? = null,
    onCta: (SignalAction) -> Unit = {},
    onThemeSelected: (ThemeId) -> Unit = {}
) {
    val c = BADGymTheme.colors

    Box(modifier.fillMaxSize().background(themeBackground(theme))) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(Modifier.fillMaxWidth().height(44.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).clip(RoundedCornerShape(13.dp)).background(brandBrush(theme)), contentAlignment = Alignment.Center) {
                    Icon(themeIcon(theme), "BAD GYM", tint = Color.White, modifier = Modifier.size(21.dp))
                }
                Spacer(Modifier.width(9.dp))
                Column(Modifier.weight(1f)) {
                    Text("BAD GYM", color = c.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Black, letterSpacing = .7.sp)
                    Text("Member Intelligence", color = c.textMuted, fontSize = 9.sp)
                }
                Box(Modifier.size(36.dp).clip(CircleShape).background(c.surfaceMuted).border(1.dp, c.border, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.NotificationsNone, null, tint = c.textSecondary, modifier = Modifier.size(19.dp))
                }
            }

            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                ThemeId.entries.forEach { item ->
                    val active = item == theme
                    Box(
                        Modifier.clip(RoundedCornerShape(50))
                            .background(if (active) item.colors().accent else item.colors().surface.copy(alpha = .82f))
                            .border(1.dp, if (active) item.colors().accent else item.colors().border, RoundedCornerShape(50))
                            .clickable { onThemeSelected(item) }
                            .padding(horizontal = 13.dp, vertical = 8.dp)
                    ) {
                        Text(item.title, color = if (active) item.colors().textOnAccent else item.colors().textSecondary, fontSize = 9.5.sp, fontWeight = if (active) FontWeight.ExtraBold else FontWeight.SemiBold)
                    }
                }
            }

            Row(Modifier.fillMaxWidth().padding(horizontal = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(theme.title.uppercase(), color = c.textPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(Modifier.weight(1f))
                Text(theme.category, color = c.textSecondary, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold)
            }

            Surface(theme, Modifier.fillMaxWidth().weight(1f), RoundedCornerShape(26.dp)) {
                Column(Modifier.fillMaxSize()) {
                    val id = snapshot.identity
                    val plan = snapshot.membership
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        MemberPhoto(id.photoUrl, id.tier, size = 68.dp, showVerified = id.isVerified)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(id.name, color = c.textPrimary, fontSize = 19.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                if (id.isVerified) {
                                    Spacer(Modifier.width(5.dp))
                                    Icon(Icons.Rounded.Verified, "Verified", tint = c.info, modifier = Modifier.size(16.dp))
                                }
                            }
                            Text((id.code ?: "MEMBER") + "  •  " + (plan?.planName ?: "No active plan"), color = c.textSecondary, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold)
                            Text(theme.motto.replace("\n", " • "), color = c.mottoColor, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(formatTime(currentEvent.occurredAt), color = c.textPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold)
                            Text(relativeTime(currentEvent.occurredAt), color = c.textMuted, fontSize = 8.sp)
                        }
                    }

                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        menus.filter { it.isVisible }.sortedBy { it.priority }.take(8).forEach { item ->
                            val active = item.id == activeMenu
                            Column(
                                Modifier.width(if (active) 78.dp else 53.dp).clip(RoundedCornerShape(14.dp))
                                    .background(if (active) c.railActiveBackground else c.railBackground)
                                    .border(1.dp, if (active) c.accent.copy(alpha = .55f) else c.border.copy(alpha = .75f), RoundedCornerShape(14.dp))
                                    .clickable(enabled = item.isEnabled && !item.isLocked) { onMenuSelected(item.id) }
                                    .padding(horizontal = 5.dp, vertical = 7.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(menuIcon(item.id), item.label, tint = if (active) c.railActiveIcon else c.railInactiveIcon, modifier = Modifier.size(17.dp))
                                Spacer(Modifier.height(2.dp))
                                Text(if (active) item.label else shortLabel(item.id), color = if (active) c.textOnAccent else c.textMuted, fontSize = 7.5.sp, fontWeight = if (active) FontWeight.ExtraBold else FontWeight.SemiBold, maxLines = 1)
                            }
                        }
                    }

                    AnimatedContent(
                        targetState = activeMenu,
                        transitionSpec = {
                            slideInHorizontally(tween(240)) + fadeIn(tween(180)) togetherWith
                                slideOutHorizontally(tween(170)) + fadeOut(tween(120))
                        },
                        label = "member-content",
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    ) { menu ->
                        Column(
                            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            when (menu) {
                                MenuType.HOME -> {
                                    Heading("Right now", primarySignal?.subtitle ?: "The most useful facts for this member.")
                                    InsightCard(primarySignal, theme)
                                    Metrics(snapshot, theme)
                                    secondarySignals.take(3).forEach { SignalRow(it, theme) }
                                    cta?.let { ActionButton(it) { onCta(it) } }
                                }
                                MenuType.ATTENDANCE -> Attendance(snapshot, theme)
                                MenuType.PLAN -> Plan(snapshot, theme)
                                MenuType.PAYMENT -> Payment(snapshot, cta, onCta, theme)
                                MenuType.TRAINER -> Trainer(snapshot, theme)
                                MenuType.WORKOUT -> Workout(snapshot, theme)
                                MenuType.SERVICES -> Services(snapshot, theme)
                                MenuType.INSIGHT -> signals.forEach { SignalRow(it, theme) }
                                else -> Heading(menu.defaultLabel, "This member area is ready for the connected data source.")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable private fun Heading(title: String, subtitle: String?) {
    Column { Text(title, color = BADGymTheme.colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black); subtitle?.let { Text(it, color = BADGymTheme.colors.textMuted, fontSize = 8.5.sp) } }
}

@Composable private fun InsightCard(signal: IntelligenceSignal?, theme: ThemeId) {
    val c = BADGymTheme.colors
    val accent = when (signal?.priority) { SignalPriority.P0_CRITICAL -> c.danger; SignalPriority.P1_ACTION_REQUIRED -> c.warning; SignalPriority.P2_IMPORTANT -> c.info; else -> c.success }
    Surface(theme, fill = accent.copy(alpha = .10f), border = accent.copy(alpha = .30f), shape = RoundedCornerShape(20.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).clip(RoundedCornerShape(13.dp)).background(accent.copy(alpha = .14f)), contentAlignment = Alignment.Center) { Icon(Icons.Rounded.AutoAwesome, null, tint = accent, modifier = Modifier.size(20.dp)) }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(signal?.title ?: "Member is on track", color = c.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                Text(signal?.subtitle ?: "No urgent action is required right now.", color = c.textSecondary, fontSize = 9.5.sp, maxLines = 2)
            }
            Text(signal?.priority?.name?.removePrefix("P") ?: "OK", color = accent, fontSize = 9.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable private fun Metrics(s: MemberSnapshot, theme: ThemeId) {
    val c = BADGymTheme.colors; val a = s.attendance; val p = s.payment; val m = s.membership
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        Metric(Modifier.weight(1f), Icons.Rounded.EventAvailable, (a?.visits ?: 0).toString() + "/" + (a?.target ?: 0), "Attendance", (a?.streakDays ?: 0).toString() + " day streak", c.success, theme)
        val due = p?.totalOutstanding ?: 0.0
        Metric(Modifier.weight(1f), Icons.Rounded.Payments, "₹" + due.toInt(), if (due > 0) "Payment due" else "Paid", if ((p?.overdueDays ?: 0) > 0) p?.overdueDays.toString() + "d overdue" else "All clear", if (due > 0) c.danger else c.success, theme)
        Metric(Modifier.weight(1f), Icons.Rounded.WorkspacePremium, (m?.daysRemaining ?: 0).toString(), "Days left", m?.planName ?: "No plan", c.vip, theme)
    }
}

@Composable private fun Metric(modifier: Modifier, icon: ImageVector, value: String, label: String, foot: String, accent: Color, theme: ThemeId) {
    Surface(theme, modifier, shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(17.dp))
            Text(value, color = BADGymTheme.colors.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Black)
            Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
            Text(foot, color = accent, fontSize = 7.5.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable private fun Attendance(s: MemberSnapshot, theme: ThemeId) {
    Heading("Attendance", "Consistency and recent activity"); Metrics(s, theme)
    Surface(theme, shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Recent activity", color = BADGymTheme.colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
            s.recentEvents.take(5).forEach { e -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(formatDateTime(e.occurredAt), color = BADGymTheme.colors.textSecondary, fontSize = 9.sp); Text(e.eventType.displayLabel(), color = BADGymTheme.colors.accent, fontSize = 9.sp, fontWeight = FontWeight.Bold) } }
        }
    }
}

@Composable private fun Plan(s: MemberSnapshot, theme: ThemeId) {
    val c = BADGymTheme.colors; val p = s.membership
    Heading("Membership", "Lifecycle, value and renewal context")
    Surface(theme, fill = c.vipSoft.copy(alpha = .55f), border = c.vip.copy(alpha = .30f), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(p?.planName ?: "No active plan", color = c.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text(p?.planType ?: "Membership unavailable", color = c.textSecondary, fontSize = 10.sp)
            Text(if (p?.isActive == true) "ACTIVE • " + p.daysRemaining + " DAYS LEFT" else "EXPIRED", color = if (p?.isActive == true) c.success else c.danger, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
    Surface(theme, shape = RoundedCornerShape(20.dp)) { Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) { Value("Renewals", (p?.renewalCount ?: 0).toString()); Value("Current cost", "₹" + (p?.currentCost ?: 0.0).toInt()); Value("Freeze allowance", (p?.freezeAllowanceDays ?: 0).toString() + " days") } }
}

@Composable private fun Payment(s: MemberSnapshot, action: SignalAction?, onCta: (SignalAction) -> Unit, theme: ThemeId) {
    val c = BADGymTheme.colors; val p = s.payment; val due = p?.totalOutstanding ?: 0.0
    Heading("Payments", "Outstanding balance and transaction context")
    Surface(theme, fill = if (due > 0) c.dangerSoft.copy(alpha = .35f) else c.successSoft.copy(alpha = .55f), border = if (due > 0) c.danger.copy(alpha = .30f) else c.success.copy(alpha = .30f), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(if (due > 0) "Payment due" else "All payments clear", color = c.textSecondary, fontSize = 9.sp)
            Text("₹" + due.toInt(), color = if (due > 0) c.danger else c.success, fontSize = 25.sp, fontWeight = FontWeight.Black)
            if ((p?.overdueDays ?: 0) > 0) Text(p?.overdueDays.toString() + " days overdue", color = c.danger, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            if (due > 0 && action != null) ActionButton(action) { onCta(action) }
        }
    }
}

@Composable private fun Trainer(s: MemberSnapshot, theme: ThemeId) {
    val c = BADGymTheme.colors; val t = s.trainer
    Heading("Trainer", "Sessions and the next coaching touchpoint")
    Surface(theme, shape = RoundedCornerShape(20.dp)) {
        Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(50.dp).clip(RoundedCornerShape(16.dp)).background(c.successSoft), contentAlignment = Alignment.Center) { Icon(Icons.Rounded.Person, null, tint = c.success, modifier = Modifier.size(26.dp)) }
            Spacer(Modifier.width(10.dp))
            Column { Text(t?.trainerName ?: "Unassigned", color = c.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black); Text(t?.focus ?: "General coaching", color = c.textSecondary, fontSize = 9.5.sp); t?.nextSessionDate?.let { Text("Next • " + formatDateTime(it), color = c.success, fontSize = 9.sp, fontWeight = FontWeight.Bold) } }
        }
    }
}

@Composable private fun Workout(s: MemberSnapshot, theme: ThemeId) {
    val c = BADGymTheme.colors; val w = s.workout
    Heading("Workout", "Current routine and training momentum")
    Surface(theme, shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(w?.currentRoutine ?: "No routine assigned", color = c.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Black)
            Text(w?.lastWorkoutDate?.let { "Last workout • " + formatDate(it) } ?: "No recent workout", color = c.textSecondary, fontSize = 9.5.sp)
        }
    }
}

@Composable private fun Services(s: MemberSnapshot, theme: ThemeId) {
    Heading("Services", "Everything attached to this member")
    s.services.orEmpty().forEach { service ->
        Surface(theme, shape = RoundedCornerShape(17.dp)) {
            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(if (service.isActive) Icons.Rounded.CheckCircle else Icons.Rounded.RemoveCircleOutline, null, tint = if (service.isActive) BADGymTheme.colors.success else BADGymTheme.colors.textMuted, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(9.dp))
                Text(service.serviceName, color = BADGymTheme.colors.textPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable private fun SignalRow(signal: IntelligenceSignal, theme: ThemeId) {
    val c = BADGymTheme.colors
    val accent = when (signal.priority) { SignalPriority.P0_CRITICAL -> c.danger; SignalPriority.P1_ACTION_REQUIRED -> c.warning; SignalPriority.P2_IMPORTANT -> c.info; else -> c.success }
    Surface(theme, shape = RoundedCornerShape(17.dp)) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(7.dp).clip(CircleShape).background(accent)); Spacer(Modifier.width(9.dp))
            Column(Modifier.weight(1f)) { Text(signal.title, color = c.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold); signal.subtitle?.let { Text(it, color = c.textMuted, fontSize = 8.5.sp, maxLines = 1) } }
            signal.value?.let { Text(it, color = accent, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold) }
        }
    }
}

@Composable private fun ActionButton(action: SignalAction, onClick: () -> Unit) {
    val c = BADGymTheme.colors
    Row(Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(16.dp)).background(Brush.horizontalGradient(c.ctaGradient)).clickable { onClick() }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Text(action.label, color = c.textOnAccent, fontSize = 10.5.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.width(6.dp)); Icon(Icons.Rounded.ArrowForward, null, tint = c.textOnAccent, modifier = Modifier.size(17.dp))
    }
}

@Composable private fun Value(label: String, value: String) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 9.sp); Text(value, color = BADGymTheme.colors.textPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold) } }

@Composable private fun Surface(theme: ThemeId, modifier: Modifier = Modifier, shape: RoundedCornerShape = RoundedCornerShape(18.dp), fill: Color? = null, border: Color? = null, content: @Composable ColumnScope.() -> Unit) {
    val c = BADGymTheme.colors
    Column(modifier.clip(shape).background(fill ?: c.surfaceElevated.copy(alpha = if (theme.isDark) .94f else .97f)).border(1.dp, border ?: c.border.copy(alpha = .82f), shape), content = content)
}

private fun shortLabel(m: MenuType) = when (m) { MenuType.ATTENDANCE -> "Attend"; MenuType.PAYMENT -> "Pay"; MenuType.TRAINER -> "Coach"; MenuType.WORKOUT -> "Train"; MenuType.SERVICES -> "More"; MenuType.INSIGHT -> "AI"; else -> m.defaultLabel }
private fun menuIcon(m: MenuType): ImageVector = when (m) {
    MenuType.HOME -> Icons.Rounded.Home; MenuType.ATTENDANCE -> Icons.Rounded.EventAvailable; MenuType.PLAN -> Icons.Rounded.CardMembership; MenuType.PAYMENT -> Icons.Rounded.CreditCard
    MenuType.TRAINER -> Icons.Rounded.Person; MenuType.WORKOUT -> Icons.Rounded.FitnessCenter; MenuType.SUPPLEMENTS -> Icons.Rounded.LocalDrink; MenuType.NUTRITION -> Icons.Rounded.Restaurant
    MenuType.SERVICES -> Icons.Rounded.Apps; MenuType.HISTORY -> Icons.Rounded.History; MenuType.INSIGHT -> Icons.Rounded.AutoAwesome
}
private fun brandBrush(t: ThemeId) = Brush.linearGradient(when (t) {
    ThemeId.NATURAL_FRESH -> listOf(Color(0xFF22C55E), Color(0xFF15803D)); ThemeId.FUTURISTIC_NEON -> listOf(Color(0xFF00E5FF), Color(0xFF0369A1))
    ThemeId.MINIMAL_DARK -> listOf(Color(0xFF4B5563), Color(0xFF111827)); ThemeId.GLASSMORPHISM -> listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
    ThemeId.PREMIUM_3D -> listOf(Color(0xFFFFD700), Color(0xFF9A6E15)); ThemeId.VIBRANT_GRADIENT -> listOf(Color(0xFFF97316), Color(0xFFEC4899))
    ThemeId.BEAST_MODE -> listOf(Color(0xFFFF1E27), Color(0xFF7F1D1D)); ThemeId.PURPLE_ROYAL -> listOf(Color(0xFFA855F7), Color(0xFF581C87))
})
private fun themeIcon(t: ThemeId) = when (t) {
    ThemeId.NATURAL_FRESH -> Icons.Rounded.Eco; ThemeId.FUTURISTIC_NEON -> Icons.Rounded.Bolt; ThemeId.MINIMAL_DARK -> Icons.Rounded.FitnessCenter; ThemeId.GLASSMORPHISM -> Icons.Rounded.Favorite
    ThemeId.PREMIUM_3D -> Icons.Rounded.WorkspacePremium; ThemeId.VIBRANT_GRADIENT -> Icons.Rounded.AutoAwesome; ThemeId.BEAST_MODE -> Icons.Rounded.LocalFireDepartment; ThemeId.PURPLE_ROYAL -> Icons.Rounded.Diamond
}
private fun themeBackground(t: ThemeId) = Brush.verticalGradient(when (t) {
    ThemeId.NATURAL_FRESH -> listOf(Color(0xFFF5FBF7), Color(0xFFEAF6EE)); ThemeId.FUTURISTIC_NEON -> listOf(Color(0xFF020617), Color(0xFF061427))
    ThemeId.MINIMAL_DARK -> listOf(Color(0xFF090B0F), Color(0xFF11151A)); ThemeId.GLASSMORPHISM -> listOf(Color(0xFFEAF5FF), Color(0xFFD9EAF8))
    ThemeId.PREMIUM_3D -> listOf(Color(0xFF070604), Color(0xFF151108)); ThemeId.VIBRANT_GRADIENT -> listOf(Color(0xFFFFF8FC), Color(0xFFF5F0FF))
    ThemeId.BEAST_MODE -> listOf(Color(0xFF090102), Color(0xFF1A0508)); ThemeId.PURPLE_ROYAL -> listOf(Color(0xFF090312), Color(0xFF17072B))
})
@Composable
private fun eventAccent(e: EventType) = when (e) {
    EventType.PAYMENT_FAILED, EventType.COMPLAINT -> BADGymTheme.colors.danger; EventType.MAINTENANCE -> BADGymTheme.colors.warning
    EventType.PAYMENT, EventType.RENEWAL -> BADGymTheme.colors.success; EventType.CHECK_IN, EventType.CHECK_OUT, EventType.WORKOUT, EventType.TRAINER_SESSION -> BADGymTheme.colors.accent
    else -> BADGymTheme.colors.info
}
private fun eventIcon(e: EventType): ImageVector = when (e) {
    EventType.CHECK_IN -> Icons.Rounded.Login; EventType.CHECK_OUT -> Icons.Rounded.Logout; EventType.PAYMENT -> Icons.Rounded.Payments; EventType.PAYMENT_FAILED -> Icons.Rounded.WarningAmber
    EventType.WALK_IN -> Icons.Rounded.DirectionsWalk; EventType.RENEWAL -> Icons.Rounded.Autorenew; EventType.TRAINER_SESSION -> Icons.Rounded.Person; EventType.WORKOUT -> Icons.Rounded.FitnessCenter
    EventType.COMPLAINT -> Icons.Rounded.ReportProblem; EventType.MAINTENANCE -> Icons.Rounded.Build; else -> Icons.Rounded.Bolt
}
private fun formatTime(v: Long) = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(v))
private fun formatDate(v: Long) = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(v))
private fun formatDateTime(v: Long) = SimpleDateFormat("dd MMM • h:mm a", Locale.getDefault()).format(Date(v))
private fun relativeTime(v: Long): String {
    val d = (System.currentTimeMillis() - v).coerceAtLeast(0L)
    return when { d < 60_000L -> "Just now"; d < 3_600_000L -> (d / 60_000L).toString() + "m ago"; d < 86_400_000L -> (d / 3_600_000L).toString() + "h ago"; else -> (d / 86_400_000L).toString() + "d ago" }
}
