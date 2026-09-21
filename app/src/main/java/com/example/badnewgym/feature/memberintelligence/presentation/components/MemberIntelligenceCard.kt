package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(BADGymTheme.colors.border, label = "card-border")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 14.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(cardBackground(theme))
            .border(1.dp, borderColor.copy(alpha = if (theme.isDark) 0.9f else 0.95f), RoundedCornerShape(28.dp))
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            IntelligenceRail(
                menus = menus,
                activeMenu = activeMenu,
                onMenuSelected = onMenuSelected,
                modifier = Modifier.width(58.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CardHeader(
                    theme = theme,
                    memberPhotoUrl = snapshot.identity.photoUrl,
                    eventTime = currentEvent.occurredAt
                )
                CheckInPill(theme)
                MemberHero(snapshot, theme)

                AnimatedContent(
                    targetState = activeMenu,
                    label = "member-panel-transition"
                ) { menu ->
                    when (menu) {
                        MenuType.HOME -> HomeCockpit(snapshot, currentEvent, signals, primarySignal, secondarySignals, theme, cta, onCtaClick)
                        MenuType.ATTENDANCE -> AttendancePanel(snapshot, theme)
                        MenuType.PLAN -> PlanPanel(snapshot, theme)
                        MenuType.PAYMENT -> PaymentPanel(snapshot, theme, onCtaClick)
                        MenuType.TRAINER -> TrainerPanel(snapshot, theme)
                        MenuType.WORKOUT -> WorkoutPanel(snapshot, theme)
                        MenuType.SERVICES -> ServicesPanel(snapshot, theme)
                        else -> ServicesPanel(snapshot, theme)
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckInPill(theme: ThemeId) {
    val colors = BADGymTheme.colors
    Row(
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(colors.accent)
            .border(1.dp, colors.accentStrong.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.CheckCircle, null, tint = colors.textOnAccent, modifier = Modifier.size(15.dp))
        Spacer(Modifier.width(6.dp))
        Text("+ CHECK-IN", color = colors.textOnAccent, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.7.sp)
    }
}

@Composable
private fun MemberHero(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        MemberPhoto(
            photoUrl = snapshot.identity.photoUrl,
            tier = snapshot.identity.tier,
            size = 82.dp,
            showVerified = snapshot.identity.isVerified
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(snapshot.identity.name, color = colors.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (snapshot.identity.isVerified) {
                    Spacer(Modifier.width(5.dp))
                    Icon(Icons.Rounded.Verified, "Verified", tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp))
                }
            }
            Text(snapshot.identity.code ?: "MEMBER", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(snapshot.membership?.let { it.planName + " • " + it.planType } ?: "Membership", color = colors.textMuted, fontSize = 9.5.sp)
            Text(theme.motto, color = colors.mottoColor, fontSize = 12.sp, lineHeight = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2)
        }
    }
}

@Composable
private fun HomeCockpit(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent,
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    theme: ThemeId,
    cta: SignalAction?,
    onCtaClick: () -> Unit
) {
    val colors = BADGymTheme.colors
    val attendance = snapshot.attendance
    val payment = snapshot.payment

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        MembershipStatusCard(snapshot.membership, theme)

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            MiniStatus(
                Modifier.weight(1f), Icons.Rounded.CalendarMonth, "Attendance",
                (attendance?.visits ?: 0).toString() + "/" + (attendance?.target ?: 0),
                (attendance?.streakDays ?: 0).toString() + " day streak", colors.success, theme
            )
            MiniStatus(
                Modifier.weight(1f), Icons.Rounded.Payments, "Payment",
                formatRupees(payment?.totalOutstanding ?: 0.0),
                if ((payment?.overdueDays ?: 0) > 0) (payment?.overdueDays ?: 0).toString() + "d overdue" else "All clear",
                if ((payment?.totalOutstanding ?: 0.0) > 0) colors.danger else colors.success, theme
            )
            MiniStatus(
                Modifier.weight(1f), Icons.Rounded.FitnessCenter, "Workout",
                "12", snapshot.workout?.currentRoutine ?: "On plan", colors.accent, theme
            )
        }

        IntelligenceFeed(currentEvent, primarySignal, secondarySignals, signals)

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            QuickAction(Icons.Rounded.Person, "Trainer", colors.accent, Modifier.weight(1f))
            QuickAction(Icons.Rounded.FitnessCenter, "Workout", colors.accent, Modifier.weight(1f))
            QuickAction(Icons.Rounded.Restaurant, "Nutrition", colors.accent, Modifier.weight(1f))
        }

        if (cta != null) {
            ThemedCtaButton(theme = theme, label = cta.label, onClick = onCtaClick)
        }
    }
}

@Composable
private fun MembershipStatusCard(membership: MembershipStatus?, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val isActive = membership?.isActive == true
    val accent = if (theme == ThemeId.PREMIUM_3D || theme == ThemeId.PURPLE_ROYAL) colors.vip else colors.accent

    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(listOf(accent.copy(alpha = if (theme.isDark) 0.25f else 0.12f), colors.surfaceElevated.copy(alpha = 0.9f))))
            .border(1.dp, accent.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(if (theme == ThemeId.PREMIUM_3D) Icons.Rounded.WorkspacePremium else Icons.Rounded.CardMembership, null, tint = accent, modifier = Modifier.size(19.dp))
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(membership?.planName ?: "Membership", color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
            Text(membership?.planType ?: "No plan", color = colors.textSecondary, fontSize = 9.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(if (isActive) "ACTIVE" else "INACTIVE", color = if (isActive) colors.success else colors.danger, fontSize = 9.sp, fontWeight = FontWeight.Black)
            Text((membership?.daysRemaining ?: 0).toString() + " days left", color = colors.textSecondary, fontSize = 8.sp)
        }
    }
}

@Composable
private fun IntelligenceFeed(
    currentEvent: MemberEvent,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    signals: List<IntelligenceSignal>
) {
    val feed = buildList {
        if (primarySignal != null) add(primarySignal)
        addAll(secondarySignals)
        if (isEmpty()) addAll(signals.take(2))
    }.distinctBy { it.id }.take(3)

    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("LIVE INTELLIGENCE", color = BADGymTheme.colors.textPrimary, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            Text(currentEvent.eventType.displayLabel() + " • " + formatTime(currentEvent.occurredAt), color = BADGymTheme.colors.textMuted, fontSize = 8.sp)
        }

        if (feed.isEmpty()) {
            FeedRow(eventIcon(currentEvent.eventType), currentEvent.eventType.displayLabel(), "Member activity recorded", BADGymTheme.colors.success)
        } else {
            feed.forEach { signal ->
                FeedRow(signalIcon(signal.category), signal.title, signal.subtitle ?: signal.value ?: "Action available", signalColor(signal.priority))
            }
        }
    }
}

@Composable
private fun FeedRow(icon: ImageVector, title: String, subtitle: String, accent: Color) {
    val colors = BADGymTheme.colors
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(colors.surfaceMuted.copy(alpha = 0.78f))
            .border(1.dp, colors.border.copy(alpha = 0.65f), RoundedCornerShape(13.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(28.dp).clip(RoundedCornerShape(9.dp)).background(accent.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(15.dp))
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = colors.textPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = colors.textSecondary, fontSize = 8.5.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Rounded.ChevronRight, null, tint = colors.textMuted, modifier = Modifier.size(15.dp))
    }
}

@Composable
private fun MiniStatus(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    helper: String,
    accent: Color,
    theme: ThemeId
) {
    val colors = BADGymTheme.colors
    Column(
        modifier
            .height(82.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(colors.surface.copy(alpha = if (theme.isDark) 0.88f else 0.78f))
            .border(1.dp, colors.border.copy(alpha = 0.75f), RoundedCornerShape(14.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(13.dp))
            Spacer(Modifier.width(4.dp))
            Text(label, color = colors.textSecondary, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
        }
        Text(value, color = if (accent == colors.danger) colors.danger else colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(helper, color = colors.textMuted, fontSize = 7.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun QuickAction(icon: ImageVector, label: String, color: Color, modifier: Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BADGymTheme.colors.surfaceMuted.copy(alpha = 0.75f))
            .border(1.dp, BADGymTheme.colors.border.copy(alpha = 0.65f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(13.dp))
        Spacer(Modifier.width(5.dp))
        Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
    }
}

private fun signalColor(priority: SignalPriority): Color = when (priority) {
    SignalPriority.P0_CRITICAL, SignalPriority.P1_ACTION_REQUIRED -> BADGymTheme.colors.danger
    SignalPriority.P2_IMPORTANT -> BADGymTheme.colors.warning
    SignalPriority.P3_BACKGROUND -> BADGymTheme.colors.success
}

private fun signalIcon(category: SignalCategory): ImageVector = when (category) {
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

private fun eventIcon(type: EventType): ImageVector = when (type) {
    EventType.CHECK_IN, EventType.CHECK_OUT -> Icons.Rounded.Login
    EventType.PAYMENT, EventType.PAYMENT_FAILED -> Icons.Rounded.Payments
    EventType.TRAINER_SESSION -> Icons.Rounded.Person
    EventType.WORKOUT -> Icons.Rounded.FitnessCenter
    EventType.RENEWAL, EventType.REACTIVATION -> Icons.Rounded.Autorenew
    EventType.COMPLAINT, EventType.MAINTENANCE -> Icons.Rounded.ReportProblem
    else -> Icons.Rounded.Bolt
}

private fun formatRupees(value: Double): String =
    if (value <= 0.0) "₹0" else "₹" + NumberFormat.getNumberInstance(Locale("en", "IN")).format(value.toInt())

private fun formatTime(timestamp: Long): String =
    SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))

private fun cardBackground(theme: ThemeId): Brush = when (theme) {
    ThemeId.NATURAL_FRESH -> Brush.verticalGradient(listOf(Color.White, Color(0xFFF6FCF8)))
    ThemeId.FUTURISTIC_NEON -> Brush.verticalGradient(listOf(Color(0xFF071426), Color(0xFF020712)))
    ThemeId.MINIMAL_DARK -> Brush.verticalGradient(listOf(Color(0xFF15191F), Color(0xFF0B0D11)))
    ThemeId.GLASSMORPHISM -> Brush.verticalGradient(listOf(Color(0xEFFFFFFF), Color(0xBDEAF5FF)))
    ThemeId.PREMIUM_3D -> Brush.verticalGradient(listOf(Color(0xFF1A150C), Color(0xFF080704)))
    ThemeId.VIBRANT_GRADIENT -> Brush.verticalGradient(listOf(Color.White, Color(0xFFFFF8FC)))
    ThemeId.BEAST_MODE -> Brush.verticalGradient(listOf(Color(0xFF1A0508), Color(0xFF090102)))
    ThemeId.PURPLE_ROYAL -> Brush.verticalGradient(listOf(Color(0xFF211039), Color(0xFF0D061A)))
}
