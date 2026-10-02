package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CardMembership
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DragIndicator
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material.icons.rounded.Lock
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.consume
import coil.compose.AsyncImage
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import kotlin.math.roundToInt

private val BG = Color(0xFFF1FBF7)
private val SURFACE = Color.White
private val TEXT = Color(0xFF102A25)
private val MUTED = Color(0xFF66807A)
private val GREEN = Color(0xFF00B86B)
private val GREEN_DARK = Color(0xFF007A4B)
private val BLUE = Color(0xFF2F80ED)
private val RED = Color(0xFFE63B3B)
private val AMBER = Color(0xFFF4A11A)
private val PURPLE = Color(0xFF7A5AF8)
private val BORDER = Color(0xFFD7EEE7)

/**
 * Visit Based Member Card v1.
 *
 * Geometry: 360dp maximum width, 3:4 shell, 16dp safe content, 56dp rail,
 * 288dp content target. Old theme selector/skins are no longer rendered.
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
            Text("Loading Visit Intelligence…", color = MUTED, modifier = Modifier.padding(32.dp))
        } else {
            VisitBasedCard(
                snapshot, currentEvent, signals, menus, activeMenu ?: MenuType.HOME,
                visitWidgetEntitlements, cta, onMenuSelected, onCta
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
private fun VisitBasedCard(
    snapshot: MemberSnapshot,
    event: MemberEvent,
    signals: List<IntelligenceSignal>,
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    entitlements: VisitWidgetEntitlements,
    cta: SignalAction?,
    onMenuSelected: (MenuType) -> Unit,
    onCta: (SignalAction) -> Unit
) {
    var layout by remember { mutableStateOf(VisitWidgetLayout()) }
    var edit by remember { mutableStateOf(false) }
    var dragging by remember { mutableStateOf<VisitWidgetId?>(null) }
    var dragOffset by remember { mutableStateOf(IntOffset.Zero) }

    val visible = VisitWidgetConditionResolver.resolve(event, snapshot, entitlements, layout)

    Box(
        Modifier
            .widthIn(max = 360.dp)
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
            .padding(8.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.verticalGradient(listOf(Color.White, BG, Color(0xFFE6F8F0))))
            .border(1.dp, BORDER, RoundedCornerShape(28.dp))
            .shadow(18.dp, RoundedCornerShape(28.dp))
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().height(58.dp).padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(38.dp).clip(RoundedCornerShape(13.dp))
                        .background(Brush.linearGradient(listOf(GREEN, GREEN_DARK))),
                    contentAlignment = Alignment.Center
                ) { Text("ϟ", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black) }

                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text("BAD GYM", color = TEXT, fontSize = 18.sp, fontWeight = FontWeight.Black)
                    Text(
                        event.eventType.displayLabel() + " • " + entitlements.subscriptionPlan,
                        color = MUTED, fontSize = 8.sp, fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    if (entitlements.subscriptionActive) Icons.Rounded.Verified else Icons.Rounded.Lock,
                    null, tint = if (entitlements.subscriptionActive) GREEN else RED
                )
                IconButton(onClick = { edit = !edit }) {
                    Icon(if (edit) Icons.Rounded.CheckCircle else Icons.Rounded.Tune, "Widget controls", tint = TEXT)
                }
            }

            Row(Modifier.fillMaxSize()) {
                Column(
                    Modifier.width(56.dp).fillMaxHeight().padding(start = 6.dp, top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    menus.filter { it.isVisible }.take(10).forEach { menu ->
                        val selected = menu.id == activeMenu
                        Box(
                            Modifier.size(46.dp).clip(RoundedCornerShape(14.dp))
                                .background(if (selected) GREEN else Color.White.copy(.82f))
                                .border(1.dp, if (selected) GREEN else BORDER, RoundedCornerShape(14.dp))
                                .clickable(enabled = menu.isEnabled) { onMenuSelected(menu.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(menuIcon(menu.id), menu.label, tint = if (selected) Color.White else TEXT)
                            if (menu.hasAlert) {
                                Box(Modifier.align(Alignment.TopEnd).padding(5.dp).size(7.dp).clip(CircleShape).background(RED))
                            }
                        }
                    }
                }

                FlowRow(
                    Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())
                        .padding(start = 8.dp, end = 10.dp, top = 4.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    maxItemsInEachRow = 4
                ) {
                    visible.forEachIndexed { index, definition ->
                        val widthFraction = definition.size.widthUnits / 4f
                        val height = 58.dp * definition.size.heightUnits
                        val isDragging = dragging == definition.id
                        Box(
                            Modifier
                                .fillMaxWidth(widthFraction)
                                .height(height)
                                .shadow(if (isDragging) 16.dp else 4.dp, RoundedCornerShape(17.dp))
                                .pointerInput(edit, definition.id) {
                                    if (!edit) return@pointerInput
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = { dragging = definition.id },
                                        onDrag = { change, amount ->
                                            change.consume()
                                            dragOffset += IntOffset(amount.x.roundToInt(), amount.y.roundToInt())
                                            if (dragOffset.x > 40 || dragOffset.y > 40) {
                                                if (index < visible.lastIndex) moveWidget(layout, definition.id, visible.getOrNull(index + 1)?.id)?.let { layout = it }
                                                dragOffset = IntOffset.Zero
                                            } else if (dragOffset.x < -40 || dragOffset.y < -40) {
                                                if (index > 0) moveWidget(layout, definition.id, visible.getOrNull(index - 1)?.id)?.let { layout = it }
                                                dragOffset = IntOffset.Zero
                                            }
                                        },
                                        onDragEnd = { dragging = null; dragOffset = IntOffset.Zero },
                                        onDragCancel = { dragging = null; dragOffset = IntOffset.Zero }
                                    )
                                }
                        ) {
                            VisitWidget(definition, snapshot, event, signals, cta, onCta)
                        }
                    }
                }
            }
        }

        if (edit) {
            WidgetEditor(
                layout = layout,
                entitlements = entitlements,
                onToggle = { id ->
                    val set = layout.locallyDisabled.toMutableSet()
                    if (!set.add(id)) set.remove(id)
                    layout = layout.copy(locallyDisabled = set)
                }
            )
        }
    }
}

private fun moveWidget(layout: VisitWidgetLayout, fromId: VisitWidgetId, toId: VisitWidgetId?): VisitWidgetLayout? {
    if (toId == null || fromId == toId) return null
    val ids = layout.orderedIds.toMutableList()
    val from = ids.indexOf(fromId)
    val to = ids.indexOf(toId)
    if (from < 0 || to < 0) return null
    ids.removeAt(from)
    ids.add(to, fromId)
    return layout.copy(orderedIds = ids)
}

@Composable
private fun VisitWidget(
    definition: VisitWidgetDefinition,
    snapshot: MemberSnapshot,
    event: MemberEvent,
    signals: List<IntelligenceSignal>,
    cta: SignalAction?,
    onCta: (SignalAction) -> Unit
) {
    val accent = when (definition.id) {
        VisitWidgetId.PAYMENT_ALERT -> RED
        VisitWidgetId.ATTENDANCE -> BLUE
        VisitWidgetId.PLAN -> AMBER
        VisitWidgetId.GYM_TIME, VisitWidgetId.INSIGHT -> PURPLE
        else -> GREEN
    }
    Box(
        Modifier.fillMaxSize().clip(RoundedCornerShape(17.dp)).background(SURFACE)
            .border(1.dp, BORDER, RoundedCornerShape(17.dp)).padding(9.dp)
    ) {
        when (definition.id) {
            VisitWidgetId.MEMBER_IDENTITY -> IdentityWidget(snapshot)
            VisitWidgetId.CURRENT_VISIT -> CurrentVisitWidget(event, accent)
            VisitWidgetId.PAYMENT_ALERT -> PaymentWidget(snapshot, cta, onCta)
            VisitWidgetId.ATTENDANCE -> AttendanceWidget(snapshot, accent)
            VisitWidgetId.PLAN -> PlanWidget(snapshot, accent)
            VisitWidgetId.GYM_TIME -> MetricWidget(Icons.Rounded.AccessTime, "Gym Time", (snapshot.workout?.durationMinutes ?: 0).toString() + " min", accent)
            VisitWidgetId.WORKOUT -> MetricWidget(Icons.Rounded.FitnessCenter, "Workout", snapshot.workout?.currentRoutine ?: "Workout", accent)
            VisitWidgetId.TRAINER -> MetricWidget(Icons.Rounded.Person, "Trainer / PT", snapshot.trainer?.trainerName ?: "PT", accent)
            VisitWidgetId.BODY_PROGRESS -> MetricWidget(Icons.Rounded.AccessTime, "Body Progress", "Progress", accent)
            VisitWidgetId.SERVICES -> MetricWidget(Icons.Rounded.Apps, "Services", snapshot.services.orEmpty().count { it.isActive }.toString() + " active", accent)
            VisitWidgetId.OFFERS -> MetricWidget(Icons.Rounded.LocalOffer, "Offers", snapshot.promotion?.title ?: "Available", accent)
            VisitWidgetId.HISTORY -> MetricWidget(Icons.Rounded.History, "History", snapshot.recentEvents.size.toString() + " events", accent)
            VisitWidgetId.INSIGHT -> MetricWidget(Icons.Rounded.AutoAwesome, "Insight", signals.firstOrNull()?.title ?: "All clear", accent)
        }
    }
}

@Composable
private fun IdentityWidget(snapshot: MemberSnapshot) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(52.dp).clip(CircleShape)
                .background(Brush.linearGradient(listOf(GREEN, Color(0xFFB8F5DD))))
        ) {
            if (!snapshot.identity.photoUrl.isNullOrBlank()) {
                AsyncImage(snapshot.identity.photoUrl, null, Modifier.fillMaxSize().clip(CircleShape))
            } else Icon(Icons.Rounded.Person, null, tint = GREEN_DARK, modifier = Modifier.align(Alignment.Center))
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(snapshot.identity.name, color = TEXT, fontSize = 13.sp, fontWeight = FontWeight.Black, maxLines = 1)
                if (snapshot.identity.isVerified) {
                    Spacer(Modifier.width(4.dp)); Icon(Icons.Rounded.Verified, null, tint = GREEN, modifier = Modifier.size(14.dp))
                }
            }
            Text(
                listOfNotNull(snapshot.identity.code, snapshot.identity.tier.name.replace('_', ' ')).joinToString(" • "),
                color = MUTED, fontSize = 8.sp, fontWeight = FontWeight.Bold
            )
            snapshot.membership?.let { Text(it.planName + " • " + it.daysRemaining.coerceAtLeast(0) + " days left", color = TEXT, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun CurrentVisitWidget(event: MemberEvent, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconBubble(Icons.Rounded.EventAvailable, accent)
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(event.eventType.displayLabel(), color = TEXT, fontSize = 13.sp, fontWeight = FontWeight.Black)
            Text(timeText(event.occurredAt), color = MUTED, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(
                if (event.eventType == EventType.CHECK_OUT) "Visit completed • duration enabled"
                else "Current visit • live context",
                color = GREEN_DARK, fontSize = 8.sp, fontWeight = FontWeight.Bold
            )
        }
        Text(if (event.eventType == EventType.CHECK_OUT) "DONE" else "LIVE", color = accent, fontSize = 9.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun PaymentWidget(snapshot: MemberSnapshot, cta: SignalAction?, onCta: (SignalAction) -> Unit) {
    val payment = snapshot.payment ?: return
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBubble(Icons.Rounded.Payment, RED)
            Spacer(Modifier.width(7.dp))
            Column(Modifier.weight(1f)) {
                Text(if (payment.overdueDays > 0) "Payment Overdue" else "Payment Due", color = RED, fontSize = 10.sp, fontWeight = FontWeight.Black)
                Text("₹" + payment.totalOutstanding.toInt(), color = TEXT, fontSize = 17.sp, fontWeight = FontWeight.Black)
            }
        }
        Text(
            if (payment.overdueDays > 0) payment.overdueDays.toString() + " days late • remains visible after checkout"
            else "Due " + (payment.dueDate?.let(::dateText) ?: "soon"),
            color = MUTED, fontSize = 7.sp
        )
        if (cta != null) {
            Box(
                Modifier.fillMaxWidth().height(24.dp).clip(RoundedCornerShape(9.dp))
                    .background(Brush.horizontalGradient(listOf(RED, Color(0xFFFF7777))))
                    .clickable { onCta(cta) },
                contentAlignment = Alignment.Center
            ) { Text("Collect Payment →", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black) }
        }
    }
}

@Composable
private fun AttendanceWidget(snapshot: MemberSnapshot, accent: Color) {
    val a = snapshot.attendance ?: return
    Column {
        MetricTitle(Icons.Rounded.CalendarMonth, "Attendance", accent)
        Text(a.visits.toString() + "/" + (a.target ?: "—") + " visits", color = TEXT, fontSize = 15.sp, fontWeight = FontWeight.Black)
        LinearProgressIndicator(
            progress = if ((a.target ?: 0) > 0) (a.visits.toFloat() / a.target!!.toFloat()).coerceIn(0f, 1f) else 0f,
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
            color = accent, trackColor = Color(0xFFE6F2EE)
        )
        Text((a.streakDays ?: 0).toString() + " day streak", color = MUTED, fontSize = 8.sp)
    }
}

@Composable
private fun PlanWidget(snapshot: MemberSnapshot, accent: Color) {
    val m = snapshot.membership ?: return
    Column {
        MetricTitle(Icons.Rounded.CardMembership, "Plan", accent)
        Text(m.planName, color = TEXT, fontSize = 12.sp, fontWeight = FontWeight.Black)
        Text(m.daysRemaining.coerceAtLeast(0).toString() + " days remaining", color = MUTED, fontSize = 8.sp)
    }
}

@Composable
private fun MetricWidget(icon: ImageVector, title: String, value: String, accent: Color) {
    Column {
        MetricTitle(icon, title, accent)
        Text(value, color = TEXT, fontSize = 12.sp, fontWeight = FontWeight.Black, maxLines = 2)
        Text("Visit-based context", color = MUTED, fontSize = 7.sp)
    }
}

@Composable
private fun MetricTitle(icon: ImageVector, title: String, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = accent, modifier = Modifier.size(15.dp))
        Spacer(Modifier.width(5.dp))
        Text(title, color = TEXT, fontSize = 9.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun IconBubble(icon: ImageVector, color: Color) {
    Box(
        Modifier.size(34.dp).clip(CircleShape)
            .background(Brush.radialGradient(listOf(Color.White, color)))
            .border(1.dp, color.copy(alpha = .25f), CircleShape),
        contentAlignment = Alignment.Center
    ) { Icon(icon, null, tint = color, modifier = Modifier.size(19.dp)) }
}

@Composable
private fun WidgetEditor(
    layout: VisitWidgetLayout,
    entitlements: VisitWidgetEntitlements,
    onToggle: (VisitWidgetId) -> Unit
) {
    Box(Modifier.fillMaxSize().background(Color(0xCCFFFFFF)).padding(12.dp)) {
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White)
                .border(1.dp, BORDER, RoundedCornerShape(20.dp)).padding(12.dp)
        ) {
            Text("Widget Control", color = TEXT, fontSize = 16.sp, fontWeight = FontWeight.Black)
            Text("Gym layout can be changed only inside server-granted features.", color = MUTED, fontSize = 8.sp)
            VisitWidgetCatalog.definitions.forEach { definition ->
                val allowed = entitlements.allows(definition.feature)
                val on = definition.id !in layout.locallyDisabled
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(if (allowed) Icons.Rounded.DragIndicator else Icons.Rounded.Lock, null, tint = if (allowed) GREEN else RED)
                    Spacer(Modifier.width(6.dp))
                    Column(Modifier.weight(1f)) {
                        Text(definition.title, color = TEXT, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(definition.size.name + " • " + if (allowed) "Included" else "Upgrade required", color = MUTED, fontSize = 7.sp)
                    }
                    Switch(checked = allowed && on, onCheckedChange = { if (allowed) onToggle(definition.id) }, enabled = allowed)
                }
            }
        }
    }
}

private fun menuIcon(type: MenuType) = when (type) {
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

private fun timeText(epoch: Long) = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(epoch))
private fun dateText(epoch: Long) = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(java.util.Date(epoch))
