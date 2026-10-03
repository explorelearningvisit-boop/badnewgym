package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetConditionResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetEntitlements
import com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetId
import com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Page = Color(0xFFF4FBF8)
private val Surface = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF102B26)
private val TextMuted = Color(0xFF6C8580)
private val Green = Color(0xFF00B86B)
private val Mint = Color(0xFFE6F8F0)
private val Blue = Color(0xFF367FE8)
private val Red = Color(0xFFE54848)
private val Amber = Color(0xFFE59A1A)
private val Purple = Color(0xFF7659E8)
private val Line = Color(0xFFDCEDE7)

@Composable
fun PixelPerfectMemberCard(
    snapshot: MemberSnapshot?,
    currentEvent: MemberEvent?,
    signals: List<com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal>,
    menus: List<com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu>,
    activeMenu: MenuType?,
    modifier: Modifier = Modifier,
    primarySignal: com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal? = null,
    secondarySignals: List<com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal> = emptyList(),
    cta: com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction? = null,
    visitWidgetEntitlements: VisitWidgetEntitlements = VisitWidgetEntitlements(),
    onMenuSelected: (MenuType) -> Unit = {},
    onCta: (com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction) -> Unit = {}
) {
    if (snapshot == null || currentEvent == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading member intelligence…", color = TextMuted)
        }
        return
    }

    var layout by remember { mutableStateOf(VisitWidgetLayout()) }

    Box(
        modifier
            .fillMaxSize()
            .background(Page)
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            Modifier
                .widthIn(max = 300.dp)
                .heightIn(min = 550.dp, max = 600.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Surface)
                .border(1.dp, Line, RoundedCornerShape(26.dp))
        ) {
            CompactMemberHeader(snapshot, currentEvent)

            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                CompactRail(
                    menus = menus,
                    activeMenu = activeMenu ?: MenuType.HOME,
                    onMenuSelected = onMenuSelected
                )
                Spacer(Modifier.width(7.dp))

                val visible = VisitWidgetConditionResolver.resolve(
                    currentEvent,
                    snapshot,
                    visitWidgetEntitlements,
                    layout
                )

                LazyColumn(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    items(visible, key = { it.id.name }) { definition ->
                        when (definition.id) {
                            VisitWidgetId.CURRENT_VISIT -> CurrentEventWidget(currentEvent)
                            VisitWidgetId.MEMBER_IDENTITY -> MemberIdentityWidget(snapshot)
                            VisitWidgetId.PAYMENT_ALERT -> PaymentWidget(snapshot)
                            VisitWidgetId.ATTENDANCE -> AttendanceWidget(snapshot)
                            VisitWidgetId.WORKOUT -> WorkoutWidget(snapshot)
                            VisitWidgetId.BODY_PROGRESS -> BodyProgressWidget(currentEvent)
                            VisitWidgetId.TRAINER -> TrainerWidget(snapshot)
                            VisitWidgetId.SERVICES -> ServicesWidget(snapshot)
                            VisitWidgetId.PLAN -> PlanWidget(snapshot)
                            VisitWidgetId.GYM_TIME -> GymTimeWidget(snapshot)
                            VisitWidgetId.HISTORY -> HistoryWidget(snapshot)
                            VisitWidgetId.INSIGHT -> InsightWidget(primarySignal, secondarySignals)
                            VisitWidgetId.OFFERS -> OfferWidget()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactMemberHeader(snapshot: MemberSnapshot, event: MemberEvent) {
    Row(
        Modifier.fillMaxWidth().background(Mint).padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(30.dp).clip(CircleShape).background(Green), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Home, null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text("MEMBER INTELLIGENCE", color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Black)
            Text(event.eventType.displayLabel(), color = Green, fontSize = 7.sp, fontWeight = FontWeight.Bold)
        }
        Text(snapshot.identity.code.orEmpty(), color = TextMuted, fontSize = 7.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CompactRail(
    menus: List<com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu>,
    activeMenu: MenuType,
    onMenuSelected: (MenuType) -> Unit
) {
    Column(
        Modifier.width(38.dp).clip(RoundedCornerShape(16.dp)).background(Page).padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        menus.take(9).forEach { menu ->
            val active = menu.id == activeMenu
            Box(
                Modifier.size(34.dp).clip(RoundedCornerShape(11.dp))
                    .background(if (active) Mint else Color.Transparent)
                    .clickable { onMenuSelected(menu.id) },
                contentAlignment = Alignment.Center
            ) {
                Icon(menuIcon(menu.id), menu.label, tint = if (active) Green else TextMuted, modifier = Modifier.size(17.dp))
            }
        }
        Icon(Icons.Rounded.MoreHoriz, "More", tint = TextMuted, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun MemberIdentityWidget(snapshot: MemberSnapshot) {
    WidgetCard("Member", Green, Icons.Rounded.Person) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(52.dp).clip(CircleShape).border(2.dp, Green, CircleShape)) {
                AsyncImage(model = snapshot.identity.photoUrl, contentDescription = "Member photo", modifier = Modifier.fillMaxSize())
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(snapshot.identity.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black, maxLines = 1)
                    if (snapshot.identity.isVerified) {
                        Spacer(Modifier.width(3.dp))
                        Icon(Icons.Rounded.Verified, "Verified", tint = Green, modifier = Modifier.size(13.dp))
                    }
                }
                Text(
                    listOfNotNull(snapshot.identity.code, snapshot.membership?.planName, snapshot.membership?.daysRemaining?.let { "$it d left" }).joinToString(" • "),
                    color = TextMuted, fontSize = 7.sp, maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun CurrentEventWidget(event: MemberEvent) {
    val accent = eventAccent(event.eventType)
    WidgetCard("Current Event", accent, Icons.Rounded.AccessTime) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(event.eventType.displayLabel(), color = accent, fontSize = 11.sp, fontWeight = FontWeight.Black)
                Text(dateTime(event.occurredAt), color = TextMuted, fontSize = 7.sp)
            }
            StatusPill(if (event.eventType == EventType.CHECK_IN) "ACTIVE" else "RECORDED", accent)
        }
        event.metadata["durationMinutes"]?.let {
            Text("$it min", color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun PaymentWidget(snapshot: MemberSnapshot) {
    val payment = snapshot.payment ?: return
    val overdue = payment.overdueDays > 0
    val accent = if (overdue) Red else Amber
    WidgetCard("Payment Due", accent, Icons.Rounded.Payment) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(currency(payment.totalOutstanding), color = accent, fontSize = 17.sp, fontWeight = FontWeight.Black)
                Text(
                    if (overdue) "${payment.overdueDays} days overdue"
                    else payment.dueDate?.let { "Due ${dateOnly(it)}" } ?: "Payment required",
                    color = TextMuted, fontSize = 7.sp
                )
            }
            ActionChip("Collect", accent)
        }
    }
}

@Composable
private fun AttendanceWidget(snapshot: MemberSnapshot) {
    val attendance = snapshot.attendance ?: return
    val target = attendance.target ?: 0
    val ratio = if (target > 0) attendance.visits.toFloat() / target else 0f
    WidgetCard("Attendance", Blue, Icons.Rounded.CalendarMonth) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("${(ratio.coerceIn(0f, 1f) * 100).toInt()}%", color = Blue, fontSize = 19.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(7.dp))
            Column(Modifier.weight(1f)) {
                Text("${attendance.visits}/${if (target > 0) target else "—"} visits", color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Black)
                Text(attendance.periodName, color = TextMuted, fontSize = 7.sp)
                LinearProgressIndicator(
                    progress = ratio.coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth().height(5.dp).clip(CircleShape),
                    color = Blue,
                    trackColor = Color(0xFFE8F0FF)
                )
            }
        }
    }
}

@Composable
private fun WorkoutWidget(snapshot: MemberSnapshot) {
    val events = snapshot.recentEvents.filter {
        it.eventType == EventType.WORKOUT || it.eventType == EventType.WORKOUT_COMPLETED || it.eventType == EventType.WORKOUT_STARTED
    }.takeLast(7)

    WidgetCard("Workout · 7 Days", Blue, Icons.Rounded.FitnessCenter) {
        if (events.isEmpty()) {
            Text("No workout duration data", color = TextMuted, fontSize = 7.sp)
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.Bottom) {
                events.forEach { event ->
                    val minutes = event.metadata["durationMinutes"]?.toIntOrNull() ?: 0
                    val bar = (minutes / 120f).coerceIn(0.08f, 1f)
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.fillMaxWidth().height((34f * bar).dp).clip(RoundedCornerShape(4.dp)).background(Blue))
                        Text("${minutes}m", color = TextMuted, fontSize = 6.sp)
                    }
                }
            }
            val durations = events.mapNotNull { it.metadata["durationMinutes"]?.toIntOrNull() }
            if (durations.isNotEmpty()) {
                Text("Peak ${durations.max()}m · Lowest ${durations.min()}m", color = TextPrimary, fontSize = 7.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
private fun BodyProgressWidget(event: MemberEvent) {
    if (event.eventType != EventType.BODY_MEASUREMENT_UPDATED) return
    val metric = event.metadata["metric"] ?: "Body Measurement"
    val previous = event.metadata["previous"] ?: return
    val current = event.metadata["current"] ?: return
    val unit = event.metadata["unit"].orEmpty()
    WidgetCard(metric, Green, Icons.Rounded.ArrowUpward) {
        Text("$previous → $current $unit", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Black)
        event.metadata["goal"]?.let { Text("Goal $it $unit", color = TextMuted, fontSize = 7.sp) }
    }
}

@Composable
private fun TrainerWidget(snapshot: MemberSnapshot) {
    val trainer = snapshot.trainer ?: return
    WidgetCard("Trainer / PT", Purple, Icons.Rounded.Person) {
        Text(trainer.trainerName, color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Black)
        trainer.focus?.let { Text(it, color = TextMuted, fontSize = 7.sp) }
        Text("${trainer.sessionsUsed}/${trainer.sessionsTotal} sessions used", color = Purple, fontSize = 7.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ServicesWidget(snapshot: MemberSnapshot) {
    val services = snapshot.services.orEmpty().filter { it.isActive }
    if (services.isEmpty()) return
    WidgetCard("Services", Green, Icons.Rounded.Tune) {
        services.take(2).forEach { Text(it.serviceName, color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun PlanWidget(snapshot: MemberSnapshot) {
    val membership = snapshot.membership ?: return
    WidgetCard("Membership", Amber, Icons.Rounded.Verified) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(membership.planName, color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Black)
                Text(membership.planType, color = TextMuted, fontSize = 7.sp)
            }
            Text("${membership.daysRemaining}d", color = Amber, fontSize = 13.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun GymTimeWidget(snapshot: MemberSnapshot) {
    val last = snapshot.attendance?.lastVisitAt
    WidgetCard("Gym Time", Blue, Icons.Rounded.AccessTime) {
        Text(last?.let { "Last visit ${dateTime(it)}" } ?: "No visit time recorded", color = TextPrimary, fontSize = 8.sp)
    }
}

@Composable
private fun HistoryWidget(snapshot: MemberSnapshot) {
    val event = snapshot.recentEvents.lastOrNull()
    WidgetCard("History", TextMuted, Icons.Rounded.CalendarMonth) {
        Text(event?.let { "${it.eventType.displayLabel()} · ${dateTime(it.occurredAt)}" } ?: "No recent history", color = TextPrimary, fontSize = 7.sp)
    }
}

@Composable
private fun InsightWidget(
    primarySignal: com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal?,
    secondary: List<com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal>
) {
    val title = primarySignal?.title ?: secondary.firstOrNull()?.title ?: return
    val detail = primarySignal?.subtitle ?: primarySignal?.value ?: secondary.firstOrNull()?.subtitle ?: secondary.firstOrNull()?.value.orEmpty()
    WidgetCard("Insight", Purple, Icons.Rounded.Tune) {
        Text(title, color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Black)
        Text(detail, color = TextMuted, fontSize = 7.sp, maxLines = 2)
    }
}

@Composable
private fun OfferWidget() {
    WidgetCard("Offers", Purple, Icons.Rounded.LocalOffer) {
        Text("Available offers", color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun WidgetCard(title: String, accent: Color, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Surface)
            .border(1.dp, Line, RoundedCornerShape(16.dp)).padding(9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(5.dp))
            Text(title, color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Rounded.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(13.dp))
        }
        Spacer(Modifier.height(5.dp))
        content()
    }
}

@Composable
private fun StatusPill(text: String, accent: Color) {
    Box(Modifier.clip(RoundedCornerShape(50)).background(accent.copy(alpha = 0.10f)).padding(horizontal = 7.dp, vertical = 4.dp)) {
        Text(text, color = accent, fontSize = 6.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun ActionChip(text: String, accent: Color) {
    Box(Modifier.clip(RoundedCornerShape(10.dp)).background(accent).padding(horizontal = 8.dp, vertical = 6.dp)) {
        Text(text, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun MemberIntelligenceAdminCommandCenter(
    layout: VisitWidgetLayout,
    entitlements: VisitWidgetEntitlements,
    onLayoutChanged: (VisitWidgetLayout) -> Unit,
    onPublish: (VisitWidgetLayout) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().background(Page).padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Member Intelligence", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                Text("Admin Command Center", color = Green, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Rounded.Settings, "Admin settings", tint = Green)
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(layout.orderedIds, key = { it.name }) { id ->
                val definition = com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetCatalog.definition(id)
                val allowed = entitlements.allows(definition.feature)
                val enabled = id !in layout.locallyDisabled
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Surface)
                        .border(1.dp, Line, RoundedCornerShape(14.dp)).padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(if (allowed) Icons.Rounded.Tune else Icons.Rounded.Lock, null, tint = if (allowed) Green else Red, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(7.dp))
                    Column(Modifier.weight(1f)) {
                        Text(definition.title, color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Black)
                        Text(layout.sizeFor(id).name, color = TextMuted, fontSize = 6.sp)
                    }
                    IconButton(onClick = { onLayoutChanged(layout.move(id, -1)) }) { Icon(Icons.Rounded.ArrowUpward, "Move up", tint = TextMuted) }
                    IconButton(onClick = { onLayoutChanged(layout.move(id, 1)) }) { Icon(Icons.Rounded.ArrowDownward, "Move down", tint = TextMuted) }
                    IconButton(
                        onClick = {
                            val sizes = com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetSize.entries
                            val current = layout.sizeFor(id)
                            val next = sizes[(sizes.indexOf(current) + 1) % sizes.size]
                            onLayoutChanged(layout.resize(id, next))
                        },
                        enabled = allowed
                    ) {
                        Icon(Icons.Rounded.Tune, "Resize " + definition.title, tint = if (allowed) Green else TextMuted)
                    }
                    Switch(checked = allowed && enabled, enabled = allowed, onCheckedChange = { if (allowed) onLayoutChanged(layout.toggle(id)) })
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Green)
                .clickable { onPublish(layout) }.padding(vertical = 11.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Publish Layout", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
        }
    }
}

private fun menuIcon(type: MenuType): ImageVector = when (type) {
    MenuType.HOME -> Icons.Rounded.Home
    MenuType.ATTENDANCE -> Icons.Rounded.CalendarMonth
    MenuType.PLAN -> Icons.Rounded.Verified
    MenuType.PAYMENT -> Icons.Rounded.Payment
    MenuType.TRAINER -> Icons.Rounded.Person
    MenuType.WORKOUT -> Icons.Rounded.FitnessCenter
    MenuType.SUPPLEMENTS, MenuType.SERVICES -> Icons.Rounded.Tune
    MenuType.NUTRITION -> Icons.Rounded.AccessTime
    MenuType.HISTORY -> Icons.Rounded.CalendarMonth
    MenuType.INSIGHT -> Icons.Rounded.Tune
}

private fun eventAccent(eventType: EventType): Color = when (eventType) {
    EventType.PAYMENT_OVERDUE, EventType.PAYMENT_FAILED, EventType.EXPIRED, EventType.MEMBERSHIP_EXPIRED -> Red
    EventType.PAYMENT_DUE, EventType.PAYMENT_PARTIAL, EventType.TRIAL_EXPIRED -> Amber
    EventType.TRAINER_SESSION, EventType.TRAINER_SESSION_SCHEDULED, EventType.TRAINER_SESSION_STARTED,
    EventType.TRAINER_SESSION_COMPLETED, EventType.TRAINER_SESSION_MISSED, EventType.TRAINER_ASSIGNED -> Purple
    EventType.WORKOUT, EventType.WORKOUT_STARTED, EventType.WORKOUT_COMPLETED, EventType.PR_ACHIEVED -> Blue
    else -> Green
}

private fun dateTime(epoch: Long): String = SimpleDateFormat("dd MMM · hh:mm a", Locale.getDefault()).format(Date(epoch))
private fun dateOnly(epoch: Long): String = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(epoch))
private fun currency(value: Double): String = "₹${String.format(Locale.getDefault(), "%,.0f", value)}"
