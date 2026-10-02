package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CardMembership
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*

private val PAGE = Color(0xFFF4FBF8)
private val SURFACE = Color(0xFFFFFFFF)
private val TEXT = Color(0xFF102B26)
private val MUTED = Color(0xFF6C8580)
private val GREEN = Color(0xFF00B86B)
private val GREEN_DARK = Color(0xFF08764F)
private val MINT = Color(0xFFE5F8F0)
private val BLUE = Color(0xFF367FE8)
private val RED = Color(0xFFE54848)
private val AMBER = Color(0xFFE59A1A)
private val PURPLE = Color(0xFF7659E8)
private val LINE = Color(0xFFDCEDE7)

/**
 * BAD GYM Member Intelligence — canonical visit card.
 *
 * The card is intentionally NOT a dashboard full of equal white boxes.
 * One member, one event, one dominant signal, one active menu.
 *
 * Geometry:
 * - 360dp maximum shell
 * - full-height mobile shell; the active viewport is the design constraint
 * - 16dp safe content
 * - 56dp vertical rail
 * - active menu content is kept inside one practical mobile viewport
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
    visitWidgetEntitlements: VisitWidgetEntitlements = VisitWidgetEntitlements(),
    onMenuSelected: (MenuType) -> Unit = {},
    onThemeSelected: (ThemeId) -> Unit = {},
    onCta: (SignalAction) -> Unit = {}
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        if (snapshot == null || currentEvent == null) {
            Text(
                "Loading member intelligence…",
                color = MUTED,
                modifier = Modifier.padding(32.dp)
            )
        } else {
            CanonicalMemberCard(
                snapshot = snapshot,
                event = currentEvent,
                signals = signals,
                menus = menus,
                activeMenu = activeMenu ?: MenuType.HOME,
                entitlements = visitWidgetEntitlements,
                primarySignal = primarySignal,
                secondarySignals = secondarySignals,
                cta = cta,
                onMenuSelected = onMenuSelected,
                onCta = onCta
            )
        }
    }
}

@Composable
private fun CanonicalMemberCard(
    snapshot: MemberSnapshot,
    event: MemberEvent,
    signals: List<IntelligenceSignal>,
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    entitlements: VisitWidgetEntitlements,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    onMenuSelected: (MenuType) -> Unit,
    onCta: (SignalAction) -> Unit
) {
    var editorOpen by remember { mutableStateOf(false) }
    var layout by remember { mutableStateOf(VisitWidgetLayout()) }

    val accent = eventAccent(event.eventType)
    val visibleWidgetIds = VisitWidgetConditionResolver
        .resolve(event, snapshot, entitlements, layout)
        .map { it.id }
        .toSet()

    Box(
        Modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color.White, PAGE, Color(0xFFEAF8F2))
                )
            )
            .border(1.dp, LINE, RoundedCornerShape(30.dp))
            .shadow(22.dp, RoundedCornerShape(30.dp))
    ) {
        Column(Modifier.fillMaxSize()) {
            CardHeader(
                snapshot = snapshot,
                event = event,
                accent = accent,
                subscriptionActive = entitlements.subscriptionActive,
                onEdit = { editorOpen = !editorOpen }
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                VerticalRail(
                    menus = menus,
                    activeMenu = activeMenu,
                    accent = accent,
                    onMenuSelected = onMenuSelected
                )

                Spacer(Modifier.width(8.dp))

                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White.copy(alpha = 0.74f))
                        .border(1.dp, LINE, RoundedCornerShape(22.dp))
                        .padding(9.dp)
                ) {
                    ActiveMenuContent(
                        menu = activeMenu,
                        snapshot = snapshot,
                        event = event,
                        accent = accent,
                        visibleWidgetIds = visibleWidgetIds,
                        signals = signals,
                        primarySignal = primarySignal,
                        secondarySignals = secondarySignals,
                        cta = cta,
                        onCta = onCta
                    )
                }
            }
        }

        if (editorOpen) {
            WidgetEditor(
                layout = layout,
                entitlements = entitlements,
                onToggle = { id ->
                    val disabled = layout.locallyDisabled.toMutableSet()
                    if (!disabled.add(id)) disabled.remove(id)
                    layout = layout.copy(locallyDisabled = disabled)
                },
                onClose = { editorOpen = false }
            )
        }
    }
}

@Composable
private fun CardHeader(
    snapshot: MemberSnapshot,
    event: MemberEvent,
    accent: Color,
    subscriptionActive: Boolean,
    onEdit: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Brush.linearGradient(listOf(GREEN, Color(0xFFBDF3DD))))
        ) {
            if (!snapshot.identity.photoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = snapshot.identity.photoUrl,
                    contentDescription = snapshot.identity.name,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(18.dp))
                )
            } else {
                Icon(
                    Icons.Rounded.Person,
                    null,
                    tint = GREEN_DARK,
                    modifier = Modifier.align(Alignment.Center).size(27.dp)
                )
            }
            if (snapshot.identity.isVerified) {
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .size(19.dp)
                        .clip(CircleShape)
                        .background(SURFACE)
                        .border(2.dp, SURFACE, CircleShape)
                ) {
                    Icon(
                        Icons.Rounded.Verified,
                        null,
                        tint = GREEN,
                        modifier = Modifier.fillMaxSize().padding(2.dp)
                    )
                }
            }
        }

        Spacer(Modifier.width(10.dp))

        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    snapshot.identity.name,
                    color = TEXT,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    snapshot.identity.code.orEmpty(),
                    color = MUTED,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                snapshot.membership?.planName ?: snapshot.identity.tier.name.replace('_', ' '),
                color = TEXT,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
            EventPill(event.eventType.displayLabel(), accent)
        }

        Column(horizontalAlignment = Alignment.End) {
            Icon(
                if (subscriptionActive) Icons.Rounded.Verified else Icons.Rounded.Lock,
                null,
                tint = if (subscriptionActive) GREEN else RED,
                modifier = Modifier.size(18.dp)
            )
            IconButton(onClick = onEdit, modifier = Modifier.size(34.dp)) {
                Icon(Icons.Rounded.Tune, "Widget controls", tint = TEXT, modifier = Modifier.size(19.dp))
            }
        }
    }
}

@Composable
private fun EventPill(label: String, accent: Color) {
    Box(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(accent.copy(alpha = 0.10f))
            .border(1.dp, accent.copy(alpha = 0.20f), RoundedCornerShape(8.dp))
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Text(label, color = accent, fontSize = 7.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun VerticalRail(
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    accent: Color,
    onMenuSelected: (MenuType) -> Unit
) {
    val visible = menus
        .filter { it.isVisible }
        .sortedBy { it.priority }
        .take(7)

    Column(
        Modifier
            .width(52.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.70f))
            .border(1.dp, LINE, RoundedCornerShape(18.dp))
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        visible.forEach { menu ->
            val selected = menu.id == activeMenu
            Box(
                Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(
                        if (selected) Brush.linearGradient(listOf(accent, accent.copy(alpha = 0.78f)))
                        else Brush.verticalGradient(listOf(Color.White, Color(0xFFF3F8F6)))
                    )
                    .border(
                        1.dp,
                        if (selected) accent.copy(alpha = 0.25f) else LINE,
                        RoundedCornerShape(13.dp)
                    )
                    .clickable(enabled = menu.isEnabled && !menu.isLocked) {
                        onMenuSelected(menu.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    menuIcon(menu.id),
                    menu.label,
                    tint = if (selected) Color.White else TEXT,
                    modifier = Modifier.size(19.dp)
                )
                if (menu.hasAlert || menu.badgeCount > 0) {
                    Box(
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (selected) Color.White else RED)
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(MINT)
                .border(1.dp, LINE, RoundedCornerShape(13.dp))
                .clickable { onMenuSelected(MenuType.HOME) },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Home, "Home", tint = GREEN_DARK, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun ActiveMenuContent(
    menu: MenuType,
    snapshot: MemberSnapshot,
    event: MemberEvent,
    accent: Color,
    visibleWidgetIds: Set<VisitWidgetId>,
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    onCta: (SignalAction) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        MenuTitle(
            menu = menu,
            event = event,
            accent = accent
        )

        when (menu) {
            MenuType.HOME -> HomeMenu(
                snapshot, event, accent, visibleWidgetIds,
                primarySignal, secondarySignals, cta, onCta
            )
            MenuType.ATTENDANCE -> AttendanceMenu(snapshot, accent)
            MenuType.PLAN -> PlanMenu(snapshot, accent)
            MenuType.PAYMENT -> PaymentMenu(snapshot, accent, cta, onCta)
            MenuType.TRAINER -> TrainerMenu(snapshot, accent)
            MenuType.WORKOUT -> WorkoutMenu(snapshot, accent)
            MenuType.SUPPLEMENTS -> SupplementsMenu(snapshot, accent)
            MenuType.NUTRITION -> NutritionMenu(snapshot, accent)
            MenuType.SERVICES -> ServicesMenu(snapshot, accent)
            MenuType.HISTORY -> HistoryMenu(snapshot, accent)
            MenuType.INSIGHT -> InsightMenu(signals, primarySignal, accent)
        }
    }
}

@Composable
private fun MenuTitle(menu: MenuType, event: MemberEvent, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(accent.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(menuIcon(menu), null, tint = accent, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(menu.defaultLabel, color = TEXT, fontSize = 13.sp, fontWeight = FontWeight.Black)
            Text(
                if (menu == MenuType.HOME) event.eventType.displayLabel() + " intelligence"
                else "Member intelligence",
                color = MUTED,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            timeText(event.occurredAt),
            color = MUTED,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HomeMenu(
    snapshot: MemberSnapshot,
    event: MemberEvent,
    accent: Color,
    visible: Set<VisitWidgetId>,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    onCta: (SignalAction) -> Unit
) {
    // Home is a command surface, not a dashboard. The order is:
    // live event -> urgent action + key metrics -> intelligence -> compact status.
    if (VisitWidgetId.CURRENT_VISIT in visible) {
        CurrentVisitHero(event, accent)
    }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (VisitWidgetId.PAYMENT_ALERT in visible && (snapshot.payment?.totalOutstanding ?: 0.0) > 0.0) {
            PaymentStrip(
                snapshot = snapshot,
                cta = cta,
                onCta = onCta,
                modifier = Modifier.weight(1.25f)
            )
        } else {
            SmartStatusStrip(
                snapshot = snapshot,
                event = event,
                accent = accent,
                modifier = Modifier.weight(1.25f)
            )
        }

        Column(
            Modifier.weight(0.75f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (VisitWidgetId.ATTENDANCE in visible) {
                CompactMetric(
                    Modifier.fillMaxWidth(),
                    Icons.Rounded.CalendarMonth,
                    "ATTENDANCE",
                    snapshot.attendance?.visits?.toString() ?: "—",
                    (snapshot.attendance?.streakDays ?: 0).toString() + " day streak",
                    BLUE
                )
            }
            if (VisitWidgetId.PLAN in visible) {
                CompactMetric(
                    Modifier.fillMaxWidth(),
                    Icons.Rounded.CardMembership,
                    "PLAN",
                    snapshot.membership?.daysRemaining?.coerceAtLeast(0)?.toString() ?: "—",
                    "days remaining",
                    AMBER
                )
            }
        }
    }

    PrimaryInsight(
        signal = primarySignal,
        fallback = secondarySignals.firstOrNull(),
        accent = accent
    )

    HomeContextStrip(snapshot = snapshot, event = event, accent = accent)
}

@Composable
private fun HomeContextStrip(
    snapshot: MemberSnapshot,
    event: MemberEvent,
    accent: Color
) {
    val visitLabel = when (event.eventType) {
        EventType.CHECK_IN -> "IN • LIVE"
        EventType.CHECK_OUT -> "OUT • SAVED"
        else -> event.eventType.displayLabel()
    }
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ContextChip(
            modifier = Modifier.weight(1f),
            label = "VISIT",
            value = visitLabel,
            accent = accent
        )
        ContextChip(
            modifier = Modifier.weight(1f),
            label = "PLAN",
            value = snapshot.membership?.planName ?: "No plan",
            accent = AMBER
        )
        ContextChip(
            modifier = Modifier.weight(1f),
            label = "MEMBER",
            value = snapshot.identity.tier.name.replace('_', ' '),
            accent = GREEN
        )
    }
}

@Composable
private fun ContextChip(
    modifier: Modifier,
    label: String,
    value: String,
    accent: Color
) {
    Column(
        modifier
            .height(48.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(accent.copy(alpha = 0.055f))
            .border(1.dp, accent.copy(alpha = 0.12f), RoundedCornerShape(13.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text(label, color = accent, fontSize = 6.sp, fontWeight = FontWeight.Black)
        Text(value, color = TEXT, fontSize = 8.sp, fontWeight = FontWeight.Black, maxLines = 1)
    }
}

@Composable
private fun CurrentVisitHero(event: MemberEvent, accent: Color) {
    val checkout = event.eventType == EventType.CHECK_OUT
    Box(
        Modifier
            .fillMaxWidth()
            .height(92.dp)
            .clip(RoundedCornerShape(19.dp))
            .background(
                Brush.linearGradient(
                    listOf(accent.copy(alpha = 0.12f), Color.White)
                )
            )
            .border(1.dp, accent.copy(alpha = 0.18f), RoundedCornerShape(19.dp))
            .padding(horizontal = 11.dp, vertical = 10.dp)
    ) {
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(accent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (checkout) Icons.Rounded.CheckCircle else Icons.Rounded.EventAvailable,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
            Spacer(Modifier.width(9.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    if (checkout) "CHECK-OUT" else "CHECK-IN",
                    color = TEXT,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    timeText(event.occurredAt),
                    color = TEXT,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    if (checkout) "Visit completed • history updated"
                    else "Current visit • live context",
                    color = MUTED,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
            Text(
                if (checkout) "SAVED" else "LIVE",
                color = accent,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun PrimaryInsight(
    signal: IntelligenceSignal?,
    fallback: IntelligenceSignal?,
    accent: Color
) {
    val title = signal?.title ?: fallback?.title ?: "Member is on track"
    val detail = signal?.subtitle ?: signal?.value ?: fallback?.subtitle ?: fallback?.value ?: "No urgent action is required right now."

    Box(
        Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF123D32), Color(0xFF1D6D55))))
            .padding(12.dp)
    ) {
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.13f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(23.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("MEMBER INTELLIGENCE", color = Color.White.copy(alpha = 0.68f), fontSize = 7.sp, fontWeight = FontWeight.Black)
                Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black, maxLines = 2)
                Text(detail, color = Color.White.copy(alpha = 0.78f), fontSize = 8.sp, maxLines = 2)
            }
            Box(
                Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accent)
            )
        }
    }
}

@Composable
private fun SmartStatusStrip(snapshot: MemberSnapshot, event: MemberEvent, accent: Color) {
    val days = snapshot.membership?.daysRemaining?.coerceAtLeast(0)
    val message = when {
        days != null && days <= 7 -> "Membership expires in $days days"
        snapshot.attendance?.streakDays ?: 0 >= 5 -> "Strong attendance streak"
        event.eventType == EventType.CHECK_OUT -> "Visit saved • progress can be reviewed"
        else -> "Everything important is visible from the rail"
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(accent.copy(alpha = 0.08f))
            .border(1.dp, accent.copy(alpha = 0.14f), RoundedCornerShape(13.dp))
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(message, color = TEXT, fontSize = 8.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PaymentStrip(
    snapshot: MemberSnapshot,
    cta: SignalAction?,
    onCta: (SignalAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val payment = snapshot.payment ?: return
    Column(
        modifier
            .height(132.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(RED.copy(alpha = 0.12f), Color.White)
                )
            )
            .border(1.dp, RED.copy(alpha = 0.18f), RoundedCornerShape(18.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(RED.copy(alpha = 0.13f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Payment, null, tint = RED, modifier = Modifier.size(17.dp))
            }
            Spacer(Modifier.width(7.dp))
            Text(
                if (payment.overdueDays > 0) "Payment overdue" else "Payment due",
                color = RED,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "₹" + payment.totalOutstanding.toInt(),
            color = TEXT,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            if (payment.overdueDays > 0) payment.overdueDays.toString() + " days late"
            else "Outstanding amount",
            color = MUTED,
            fontSize = 7.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        if (cta != null) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(RED)
                    .clickable { onCta(cta) },
                contentAlignment = Alignment.Center
            ) {
                Text("Collect payment  →", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun SmartStatusStrip(
    snapshot: MemberSnapshot,
    event: MemberEvent,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val days = snapshot.membership?.daysRemaining?.coerceAtLeast(0)
    val message = when {
        days != null && days <= 7 -> "Membership expires in $days days"
        snapshot.attendance?.streakDays ?: 0 >= 5 -> "Strong attendance streak"
        event.eventType == EventType.CHECK_OUT -> "Visit saved • progress can be reviewed"
        else -> "Everything important is visible from the rail"
    }
    Box(
        modifier
            .height(132.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(accent.copy(alpha = 0.07f))
            .border(1.dp, accent.copy(alpha = 0.14f), RoundedCornerShape(18.dp))
            .padding(10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Icon(Icons.Rounded.CheckCircle, null, tint = accent, modifier = Modifier.size(20.dp))
            Text("STATUS", color = accent, fontSize = 7.sp, fontWeight = FontWeight.Black)
            Text(message, color = TEXT, fontSize = 11.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun AttendanceMenu(snapshot: MemberSnapshot, accent: Color) {
    val a = snapshot.attendance
    MetricPanel(
        title = "Attendance rhythm",
        icon = Icons.Rounded.CalendarMonth,
        accent = BLUE
    ) {
        Text(
            (a?.visits ?: 0).toString() + " visits",
            color = TEXT,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            "Lifetime " + (a?.lifetimeVisits ?: 0) + " • " + (a?.avgVisitsPerWeek ?: 0.0).format1() + "/week",
            color = MUTED,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        ProgressLine(
            value = if ((a?.target ?: 0) > 0) (a!!.visits.toFloat() / a.target!!.toFloat()).coerceIn(0f, 1f) else 0f,
            accent = BLUE
        )
        Spacer(Modifier.height(7.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            MiniStat("STREAK", (a?.streakDays ?: 0).toString() + "d", BLUE)
            MiniStat("TARGET", (a?.target ?: 0).toString(), BLUE)
        }
    }
    if (a?.preferredSlot != null || a?.lastVisitAt != null) {
        InfoPanel("Pattern", Icons.Rounded.AccessTime, BLUE) {
            Text(
                listOfNotNull(
                    a.preferredSlot?.let { "Preferred: $it" },
                    a.lastVisitAt?.let { "Last visit: " + dateText(it) }
                ).joinToString(" • "),
                color = TEXT,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PlanMenu(snapshot: MemberSnapshot, accent: Color) {
    val m = snapshot.membership
    MetricPanel(title = "Membership", icon = Icons.Rounded.CardMembership, accent = AMBER) {
        Text(m?.planName ?: "No active plan", color = TEXT, fontSize = 22.sp, fontWeight = FontWeight.Black)
        Text(m?.planType ?: "—", color = MUTED, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            MiniStat("DAYS LEFT", (m?.daysRemaining ?: 0).coerceAtLeast(0).toString(), AMBER)
            MiniStat("RENEWALS", (m?.renewalCount ?: 0).toString(), AMBER)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "Expires " + (m?.expiryDate?.let(::dateText) ?: "—"),
            color = TEXT,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
    m?.previousPlanName?.let {
        InfoPanel("Previous plan", Icons.Rounded.History, AMBER) {
            Text(it, color = TEXT, fontSize = 10.sp, fontWeight = FontWeight.Black)
            Text("Plan history is available from History.", color = MUTED, fontSize = 8.sp)
        }
    }
}

@Composable
private fun PaymentMenu(
    snapshot: MemberSnapshot,
    accent: Color,
    cta: SignalAction?,
    onCta: (SignalAction) -> Unit
) {
    val p = snapshot.payment
    Box(
        Modifier
            .fillMaxWidth()
            .height(122.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(RED.copy(alpha = 0.13f), Color.White)
                )
            )
            .border(1.dp, RED.copy(alpha = 0.18f), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Payment, null, tint = RED, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(7.dp))
                Text(
                    if ((p?.overdueDays ?: 0) > 0) "Payment overdue" else "Payment status",
                    color = RED,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.weight(1f))
                Text("₹" + (p?.totalOutstanding ?: 0.0).toInt(), color = TEXT, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                when {
                    p == null -> "No payment record"
                    p.overdueDays > 0 -> p.overdueDays.toString() + " days late"
                    p.dueDate != null -> "Due " + dateText(p.dueDate)
                    else -> "No outstanding due"
                },
                color = MUTED,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            if (cta != null) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(29.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(RED)
                        .clickable { onCta(cta) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Collect payment", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }

    InfoPanel("Recent payment", Icons.Rounded.CheckCircle, GREEN) {
        Text(
            p?.lastPaymentAmount?.let { "₹" + it.toInt() } ?: "No recent payment",
            color = TEXT,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            p?.lastPaymentDate?.let(::dateText) ?: "—",
            color = MUTED,
            fontSize = 8.sp
        )
    }
}

@Composable
private fun TrainerMenu(snapshot: MemberSnapshot, accent: Color) {
    val t = snapshot.trainer
    MetricPanel(title = "Trainer / PT", icon = Icons.Rounded.Person, accent = PURPLE) {
        Text(t?.trainerName ?: "No trainer assigned", color = TEXT, fontSize = 18.sp, fontWeight = FontWeight.Black)
        Text(t?.focus ?: "Personal training data will appear here.", color = MUTED, fontSize = 8.sp)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            MiniStat("SESSIONS", (t?.sessionsUsed ?: 0).toString() + "/" + (t?.sessionsTotal ?: 0), PURPLE)
            MiniStat("NEXT", t?.nextSessionDate?.let(::dateText) ?: "—", PURPLE)
        }
    }
    t?.lastSessionDate?.let {
        InfoPanel("Last session", Icons.Rounded.FitnessCenter, PURPLE) {
            Text(dateText(it), color = TEXT, fontSize = 10.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun WorkoutMenu(snapshot: MemberSnapshot, accent: Color) {
    val w = snapshot.workout
    MetricPanel(title = "Workout", icon = Icons.Rounded.FitnessCenter, accent = BLUE) {
        Text(w?.currentRoutine ?: "No active routine", color = TEXT, fontSize = 18.sp, fontWeight = FontWeight.Black)
        Text(
            (w?.durationMinutes?.toString() ?: "—") + " min • " +
                (w?.calories?.toString() ?: "—") + " kcal",
            color = MUTED,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
    }
    InfoPanel("Last workout", Icons.Rounded.History, BLUE) {
        Text(w?.lastWorkoutDate?.let(::dateText) ?: "No history", color = TEXT, fontSize = 10.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun SupplementsMenu(snapshot: MemberSnapshot, accent: Color) {
    val s = snapshot.supplements
    MetricPanel(title = "Supplements", icon = Icons.Rounded.Apps, accent = GREEN) {
        Text(s?.lastPurchaseName ?: "No purchase history", color = TEXT, fontSize = 16.sp, fontWeight = FontWeight.Black)
        Text(
            s?.lastPurchaseDate?.let(::dateText) ?: "No recent purchase",
            color = MUTED,
            fontSize = 8.sp
        )
        s?.lastPurchasePrice?.let {
            Spacer(Modifier.height(6.dp))
            Text("₹" + it.toInt(), color = GREEN_DARK, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun NutritionMenu(snapshot: MemberSnapshot, accent: Color) {
    val n = snapshot.nutrition
    MetricPanel(title = "Nutrition", icon = Icons.Rounded.AccessTime, accent = GREEN) {
        Text(n?.planName ?: "No nutrition plan", color = TEXT, fontSize = 17.sp, fontWeight = FontWeight.Black)
        Text(
            if (n?.isSubscribed == true) "Active subscription" else "Not subscribed",
            color = if (n?.isSubscribed == true) GREEN_DARK else MUTED,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
        n?.renewalDate?.let {
            Spacer(Modifier.height(7.dp))
            Text("Renewal " + dateText(it), color = TEXT, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ServicesMenu(snapshot: MemberSnapshot, accent: Color) {
    val services = snapshot.services.orEmpty()
    MetricPanel(title = "Services", icon = Icons.Rounded.Apps, accent = GREEN) {
        Text(
            services.count { it.isActive }.toString() + " active services",
            color = TEXT,
            fontSize = 19.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(Modifier.height(6.dp))
        services.take(3).forEach { service ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(if (service.isActive) GREEN else MUTED))
                Spacer(Modifier.width(6.dp))
                Text(service.serviceName, color = TEXT, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(if (service.isActive) "ACTIVE" else "OFF", color = MUTED, fontSize = 7.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun HistoryMenu(snapshot: MemberSnapshot, accent: Color) {
    val events = snapshot.recentEvents.take(4)
    MetricPanel(title = "Recent history", icon = Icons.Rounded.History, accent = PURPLE) {
        if (events.isEmpty()) {
            Text("No recent events", color = MUTED, fontSize = 9.sp)
        } else {
            events.forEach { event ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(7.dp).clip(CircleShape).background(accent))
                    Spacer(Modifier.width(7.dp))
                    Text(event.eventType.displayLabel(), color = TEXT, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text(dateText(event.occurredAt), color = MUTED, fontSize = 7.sp)
                }
            }
        }
    }
}

@Composable
private fun InsightMenu(
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal?,
    accent: Color
) {
    MetricPanel(title = "AI insight", icon = Icons.Rounded.AutoAwesome, accent = accent) {
        Text(
            primarySignal?.title ?: signals.firstOrNull()?.title ?: "No urgent insight",
            color = TEXT,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(Modifier.height(5.dp))
        Text(
            primarySignal?.subtitle ?: primarySignal?.value ?: signals.firstOrNull()?.subtitle ?: signals.firstOrNull()?.value ?: "The member currently has no high-priority signal.",
            color = MUTED,
            fontSize = 8.sp,
            maxLines = 4
        )
    }
    signals.drop(1).take(2).forEach { signal ->
        InfoPanel(signal.title, Icons.Rounded.AutoAwesome, accent) {
            Text(signal.subtitle ?: signal.value ?: signal.evidence.firstOrNull() ?: "No additional detail", color = TEXT, fontSize = 8.sp, maxLines = 2)
        }
    }
}

@Composable
private fun MetricPanel(
    title: String,
    icon: ImageVector,
    accent: Color,
    content: @Composable Column.() -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SURFACE)
            .border(1.dp, LINE, RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accent.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = accent, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.width(7.dp))
            Text(title, color = TEXT, fontSize = 9.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun InfoPanel(
    title: String,
    icon: ImageVector,
    accent: Color,
    content: @Composable Column.() -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(accent.copy(alpha = 0.055f))
            .border(1.dp, accent.copy(alpha = 0.12f), RoundedCornerShape(15.dp))
            .padding(9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(5.dp))
            Text(title, color = TEXT, fontSize = 8.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(5.dp))
        content()
    }
}

@Composable
private fun CompactMetric(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    detail: String,
    accent: Color
) {
    Column(
        modifier
            .height(58.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(SURFACE)
            .border(1.dp, LINE, RoundedCornerShape(17.dp))
            .padding(9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(5.dp))
            Text(label, color = MUTED, fontSize = 7.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(3.dp))
        Text(value, color = TEXT, fontSize = 11.sp, fontWeight = FontWeight.Black, maxLines = 1)
        Text(detail, color = MUTED, fontSize = 7.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun MiniStat(label: String, value: String, accent: Color) {
    Column(
        Modifier
            .clip(RoundedCornerShape(11.dp))
            .background(accent.copy(alpha = 0.07f))
            .padding(horizontal = 9.dp, vertical = 7.dp)
    ) {
        Text(label, color = MUTED, fontSize = 6.sp, fontWeight = FontWeight.Black)
        Text(value, color = TEXT, fontSize = 9.sp, fontWeight = FontWeight.Black, maxLines = 1)
    }
}

@Composable
private fun ProgressLine(value: Float, accent: Color) {
    LinearProgressIndicator(
        progress = value.coerceIn(0f, 1f),
        modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
        color = accent,
        trackColor = Color(0xFFE8F1EE)
    )
}

@Composable
private fun WidgetEditor(
    layout: VisitWidgetLayout,
    entitlements: VisitWidgetEntitlements,
    onToggle: (VisitWidgetId) -> Unit,
    onClose: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xD9FFFFFF))
            .padding(12.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White)
                .border(1.dp, LINE, RoundedCornerShape(22.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Card layout", color = TEXT, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    Text(
                        "Only server-granted features can be enabled.",
                        color = MUTED,
                        fontSize = 8.sp
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Rounded.CheckCircle, "Close", tint = GREEN)
                }
            }

            VisitWidgetCatalog.definitions.take(9).forEach { definition ->
                val allowed = entitlements.allows(definition.feature)
                val enabled = definition.id !in layout.locallyDisabled
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (allowed) Icons.Rounded.Tune else Icons.Rounded.Lock,
                        null,
                        tint = if (allowed) GREEN else RED,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Column(Modifier.weight(1f)) {
                        Text(definition.title, color = TEXT, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        Text(definition.size.name, color = MUTED, fontSize = 6.sp)
                    }
                    Switch(
                        checked = allowed && enabled,
                        enabled = allowed,
                        onCheckedChange = { if (allowed) onToggle(definition.id) }
                    )
                }
            }
        }
    }
}

private fun eventAccent(event: EventType): Color = when (event) {
    EventType.PAYMENT_OVERDUE, EventType.PAYMENT_FAILED, EventType.EXPIRED,
    EventType.MEMBERSHIP_EXPIRED -> RED
    EventType.PAYMENT_DUE, EventType.PAYMENT_PARTIAL, EventType.TRIAL_EXPIRED -> AMBER
    EventType.TRAINER_SESSION, EventType.TRAINER_SESSION_SCHEDULED,
    EventType.TRAINER_SESSION_STARTED, EventType.TRAINER_SESSION_COMPLETED,
    EventType.TRAINER_SESSION_MISSED, EventType.TRAINER_ASSIGNED -> PURPLE
    EventType.WORKOUT, EventType.WORKOUT_STARTED, EventType.WORKOUT_COMPLETED,
    EventType.PR_ACHIEVED -> BLUE
    else -> GREEN
}

private fun menuIcon(type: MenuType): ImageVector = when (type) {
    MenuType.HOME -> Icons.Rounded.Home
    MenuType.ATTENDANCE -> Icons.Rounded.CalendarMonth
    MenuType.PLAN -> Icons.Rounded.CardMembership
    MenuType.PAYMENT -> Icons.Rounded.Payment
    MenuType.TRAINER -> Icons.Rounded.Person
    MenuType.WORKOUT -> Icons.Rounded.FitnessCenter
    MenuType.SUPPLEMENTS, MenuType.SERVICES -> Icons.Rounded.Apps
    MenuType.NUTRITION -> Icons.Rounded.AccessTime
    MenuType.HISTORY -> Icons.Rounded.History
    MenuType.INSIGHT -> Icons.Rounded.AutoAwesome
}

private fun timeText(epoch: Long): String =
    java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(epoch))

private fun dateText(epoch: Long): String =
    java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(java.util.Date(epoch))

private fun Double.format1(): String = String.format(java.util.Locale.getDefault(), "%.1f", this)
