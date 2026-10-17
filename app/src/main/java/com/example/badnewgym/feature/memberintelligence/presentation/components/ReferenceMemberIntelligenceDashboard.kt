package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyMeasurement
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTimeliness
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDayStatus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

private object MIColor {
    val page = Color(0xFFF3FAFD)
    val white = Color.White
    val ink = Color(0xFF14244B)
    val muted = Color(0xFF526781)
    val line = Color(0xFFD9E7EF)
    val mint = Color(0xFFE6F8EF)
    val green = Color(0xFF079C63)
    val greenSoft = Color(0xFFBDEED5)
    val red = Color(0xFFE7495B)
    val redSoft = Color(0xFFFFECEF)
    val amber = Color(0xFFE6A01F)
    val amberSoft = Color(0xFFFFF5D9)
    val blue = Color(0xFF267DE8)
    val blueSoft = Color(0xFFECF5FF)
    val purple = Color(0xFF8155E8)
    val purpleSoft = Color(0xFFF3EDFF)
    val gold = Color(0xFFB9780B)
    val goldSoft = Color(0xFFFFF5D8)
    val slate = Color(0xFF93A5B5)
}

private data class MenuItemUi(val id: MenuType, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val index: Int)

@Composable
fun ReferenceMemberIntelligenceDashboard(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    menus: List<com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu>,
    activeMenu: MenuType?,
    onMenuSelected: (MenuType) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val menuItems = remember(menus) {
        listOf(
            MenuItemUi(MenuType.HOME, "Overview", Icons.Rounded.Home, 2),
            MenuItemUi(MenuType.PAYMENT, "Payment", Icons.Rounded.Payment, 3),
            MenuItemUi(MenuType.ATTENDANCE, "Attendance", Icons.Rounded.CalendarMonth, 4),
            MenuItemUi(MenuType.WORKOUT, "Workout", Icons.Rounded.FitnessCenter, 5),
            MenuItemUi(MenuType.PLAN, "Plan", Icons.Rounded.Verified, 1),
            MenuItemUi(MenuType.INSIGHT, "Progress", Icons.Rounded.Insights, 6),
            MenuItemUi(MenuType.NUTRITION, "Nutrition", Icons.Rounded.Restaurant, 6),
            MenuItemUi(MenuType.SERVICES, "More", Icons.Rounded.GridView, 6)
        ).filter { item ->
            menus.firstOrNull { it.id == item.id }?.let { it.isVisible && it.isEnabled && !it.isLocked } ?: item.id == MenuType.HOME
        }
    }

    Box(
        modifier = modifier.fillMaxSize().background(MIColor.page),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().widthIn(max = 480.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item(key = "brand") { AppBrandStrip() }
            item(key = "identity") { MemberIdentityPanel(snapshot) }
            item(key = "menu") {
                HorizontalMemberMenu(
                    items = menuItems,
                    selected = activeMenu ?: MenuType.HOME,
                    onSelect = { item ->
                        onMenuSelected(item.id)
                        scope.launch { listState.animateScrollToItem(item.index) }
                    }
                )
            }
            item(key = "payment") { PaymentPlanPanel(snapshot, onMenuSelected) }
            item(key = "attendance") { AttendancePanel(snapshot) }
            item(key = "workout") { WorkoutPanel(snapshot) }
            item(key = "body") { BodyProgressPanel(snapshot) }
            item(key = "footer") {
                Text(
                    text = currentEvent?.eventType?.displayLabel()?.let { "Current event · $it" } ?: "Member intelligence",
                    color = MIColor.muted,
                    fontSize = 11.sp,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AppBrandStrip() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Text("BAD GYM", color = MIColor.green, fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.7).sp)
            Text("MEMBER INTELLIGENCE", color = MIColor.ink, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
        }
        SmallHeaderAction(Icons.Rounded.Search, "Search")
        Spacer(Modifier.width(7.dp))
        Box {
            SmallHeaderAction(Icons.Rounded.Notifications, "Notifications")
            Box(Modifier.padding(start = 27.dp, top = 0.dp).size(15.dp).clip(CircleShape).background(MIColor.red), contentAlignment = Alignment.Center) {
                Text("7", color = MIColor.white, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.width(7.dp))
        SmallHeaderAction(Icons.Rounded.MoreHoriz, "More")
    }
}

@Composable
private fun SmallHeaderAction(icon: androidx.compose.ui.graphics.vector.ImageVector, description: String) {
    Surface(
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        color = MIColor.white,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MIColor.line)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = description, tint = MIColor.ink, modifier = Modifier.size(21.dp))
        }
    }
}

@Composable
private fun MemberIdentityPanel(snapshot: MemberSnapshot) {
    val membership = snapshot.membership
    val daysRemaining = membership?.daysRemaining?.coerceAtLeast(0) ?: 0
    val totalDays = if (membership != null) ((membership.expiryDate - membership.startDate) / DAY_MS).toInt().coerceAtLeast(1) else 1
    val remainingFraction = (daysRemaining.toFloat() / totalDays).coerceIn(0f, 1f)
    val elapsedPercent = ((1f - remainingFraction) * 100).roundToInt()
    val tint = when (membership?.lifecycle) {
        com.example.badnewgym.feature.memberintelligence.domain.model.MembershipLifecycle.EXPIRED,
        com.example.badnewgym.feature.memberintelligence.domain.model.MembershipLifecycle.CANCELLED -> MIColor.red
        else -> MIColor.green
    }

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFFFFBE9),
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3E8B9))
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    Modifier.size(78.dp).clip(CircleShape).background(MIColor.white)
                        .border(3.dp, Color(0xFFE8BD53), CircleShape).padding(3.dp)
                ) {
                    if (!snapshot.identity.photoUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = snapshot.identity.photoUrl,
                            contentDescription = "Photo of ${snapshot.identity.name}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    } else {
                        Box(Modifier.fillMaxSize().clip(CircleShape).background(MIColor.goldSoft), contentAlignment = Alignment.Center) {
                            Text(snapshot.identity.name.split(" ").take(2).mapNotNull { it.firstOrNull() }.joinToString(""), color = MIColor.gold, fontSize = 22.sp, fontWeight = FontWeight.Black)
                        }
                    }
                    Box(Modifier.align(Alignment.BottomEnd).size(18.dp).clip(CircleShape).background(tint).border(2.dp, MIColor.white, CircleShape))
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(snapshot.identity.name, color = MIColor.ink, fontSize = 21.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(snapshot.identity.code ?: "Member ID unavailable", color = MIColor.muted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        TinyStatusChip(membership?.planName ?: snapshot.identity.tier.name.replace('_', ' '), MIColor.goldSoft, MIColor.gold)
                        TinyStatusChip(
                            when {
                                membership == null -> "NO PLAN"
                                !membership.isActive -> "INACTIVE"
                                daysRemaining <= 7 -> "EXPIRING"
                                else -> "ACTIVE"
                            },
                            if (membership?.isActive == true && daysRemaining > 7) MIColor.mint else if (daysRemaining <= 7) MIColor.amberSoft else MIColor.redSoft,
                            if (membership?.isActive == true && daysRemaining > 7) MIColor.green else if (daysRemaining <= 7) MIColor.amber else MIColor.red
                        )
                    }
                }
                MembershipRing(remainingFraction = remainingFraction, daysRemaining = daysRemaining)
            }
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(13.dp)).background(Color(0xFFFFF4CC)).padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.CalendarMonth, null, tint = MIColor.gold, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Column(Modifier.weight(1f)) {
                    Text("MEMBER SINCE", color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(formatDate(snapshot.identity.memberSince), color = MIColor.ink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Box(Modifier.width(1.dp).height(30.dp).background(Color(0xFFE8DDAE)))
                Spacer(Modifier.width(9.dp))
                Column(Modifier.weight(1f)) {
                    Text("PLAN VALIDITY", color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(
                        if (membership != null) "${formatDate(membership.startDate)} – ${formatDate(membership.expiryDate)}" else "Not available",
                        color = MIColor.ink, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun MembershipRing(remainingFraction: Float, daysRemaining: Int) {
    Box(Modifier.size(78.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 7.dp.toPx()
            drawArc(Color(0xFFE6EDE8), -90f, 360f, false, style = Stroke(stroke, cap = StrokeCap.Round))
            drawArc(MIColor.green, -90f, 360f * remainingFraction, false, style = Stroke(stroke, cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Text("${(remainingFraction * 100).roundToInt()}%", color = MIColor.ink, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text("left", color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TinyStatusChip(label: String, bg: Color, fg: Color) {
    Box(Modifier.clip(RoundedCornerShape(30.dp)).background(bg).padding(horizontal = 8.dp, vertical = 5.dp)) {
        Text(label, color = fg, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun HorizontalMemberMenu(items: List<MenuItemUi>, selected: MenuType, onSelect: (MenuItemUi) -> Unit) {
    Row(
        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        items.forEach { item ->
            val active = item.id == selected
            Column(
                Modifier.width(76.dp).defaultMinSize(minHeight = 64.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(if (active) MIColor.green else MIColor.white)
                    .border(1.dp, if (active) MIColor.green else MIColor.line, RoundedCornerShape(15.dp))
                    .clickable { onSelect(item) }
                    .padding(horizontal = 6.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(item.icon, contentDescription = item.label, tint = if (active) MIColor.white else menuTint(item.id), modifier = Modifier.size(22.dp))
                Text(item.label, color = if (active) MIColor.white else MIColor.ink, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

private fun menuTint(menu: MenuType): Color = when (menu) {
    MenuType.PAYMENT -> MIColor.red
    MenuType.ATTENDANCE -> MIColor.green
    MenuType.WORKOUT -> MIColor.blue
    MenuType.PLAN -> MIColor.gold
    MenuType.INSIGHT -> MIColor.purple
    MenuType.NUTRITION -> MIColor.green
    else -> MIColor.ink
}

@Composable
private fun SectionCard(
    title: String,
    subtitle: String? = null,
    accent: Color,
    surface: Color = MIColor.white,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = surface,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, accent.copy(alpha = 0.22f))
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.width(4.dp).height(24.dp).clip(RoundedCornerShape(3.dp)).background(accent))
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(title, color = accent, fontSize = 18.sp, fontWeight = FontWeight.Black)
                    if (subtitle != null) Text(subtitle, color = MIColor.muted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
                trailing?.invoke()
            }
            content()
        }
    }
}

@Composable
private fun PaymentPlanPanel(snapshot: MemberSnapshot, onMenuSelected: (MenuType) -> Unit) {
    val payment = snapshot.payment
    val membership = snapshot.membership
    val outstanding = payment?.totalOutstanding ?: 0.0
    val due = outstanding > 0.0
    val accent = if (due) MIColor.red else MIColor.green
    val surface = if (due) Color(0xFFFFF5F5) else Color(0xFFF4FFF8)

    SectionCard(
        title = "Payment & Plan",
        subtitle = membership?.let { "${it.planName} · ${it.planType}" },
        accent = accent,
        surface = surface,
        trailing = {
            TinyStatusChip(if (due) "ACTION NEEDED" else "PAID", if (due) MIColor.redSoft else MIColor.mint, accent)
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1.1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(if (due) "OUTSTANDING" else "LAST RECEIVED", color = MIColor.muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(
                    money(if (due) outstanding else payment?.lastPaymentAmount ?: 0.0),
                    color = accent, fontSize = 25.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(
                    when {
                        due && (payment?.overdueDays ?: 0) > 0 -> "${payment?.overdueDays} days overdue"
                        due && payment?.dueDate != null -> "Due ${formatDate(payment.dueDate)}"
                        !due && payment?.lastPaymentDate != null -> "Received ${formatDate(payment.lastPaymentDate)}"
                        else -> "Payment date unavailable"
                    },
                    color = if (due) MIColor.red else MIColor.muted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold
                )
            }
            Box(Modifier.width(1.dp).height(62.dp).background(MIColor.line))
            Column(Modifier.weight(0.9f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("NEXT DUE", color = MIColor.muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(
                    if (payment?.dueDate != null && !due) formatDate(payment.dueDate) else if (due && payment?.dueDate != null) formatDate(payment.dueDate) else "—",
                    color = MIColor.ink, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold
                )
                Text(
                    if (membership != null) "${membership.daysRemaining.coerceAtLeast(0)} plan days left" else "Plan not available",
                    color = MIColor.muted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        }
        Button(
            onClick = { onMenuSelected(MenuType.PAYMENT) },
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = MIColor.white)
        ) {
            Icon(if (due) Icons.Rounded.Payment else Icons.Rounded.ReceiptLong, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(7.dp))
            Text(if (due) "Open payment actions" else "View receipt & history", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Rounded.ChevronRight, null, modifier = Modifier.size(18.dp))
        }
        PaymentHistoryChart(snapshot)
    }
}

@Composable
private fun PaymentHistoryChart(snapshot: MemberSnapshot) {
    val history = snapshot.payment?.history.orEmpty()
    val months = remember(history) { lastTwelveMonths() }
    val monthlyTransactions = months.map { month ->
        history.filter { sameMonth(it.occurredAt, month.first) }
    }
    val values = monthlyTransactions.map { transactions -> transactions.sumOf { it.amount } }
    val maxValue = values.maxOrNull()?.coerceAtLeast(1.0) ?: 1.0
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Payment history", color = MIColor.ink, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            Text("12 months", color = MIColor.blue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        if (history.isEmpty()) {
            EmptyDataLine("No payment transactions are linked yet.")
        } else {
            Row(
                Modifier.fillMaxWidth().height(78.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                months.forEachIndexed { index, month ->
                    val amount = values[index]
                    val transactions = monthlyTransactions[index]
                    val monthColor = when {
                        transactions.any { it.planUpgraded } -> MIColor.gold
                        transactions.any { it.timeliness == PaymentTimeliness.VERY_LATE } -> MIColor.red
                        transactions.any { it.timeliness == PaymentTimeliness.LATE } -> MIColor.amber
                        transactions.any { it.timeliness == PaymentTimeliness.ON_TIME } -> MIColor.green
                        transactions.isNotEmpty() -> MIColor.slate
                        else -> MIColor.slate.copy(alpha = 0.28f)
                    }
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                        if (amount > 0.0) {
                            Text(compactMoney(amount), color = MIColor.ink, fontSize = 8.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        } else {
                            Spacer(Modifier.height(10.dp))
                        }
                        Box(
                            Modifier.fillMaxWidth(0.68f)
                                .height(if (amount > 0.0) (10 + (amount / maxValue * 32).toInt()).dp else 4.dp)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(monthColor)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(month.second, color = MIColor.muted, fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                LegendDot("On time", MIColor.green)
                LegendDot("Late", MIColor.amber)
                LegendDot("Very late", MIColor.red)
                LegendDot("Unknown", MIColor.slate)
            }
            Text("No transaction is shown as an empty month, not as a confirmed missed payment.", color = MIColor.muted, fontSize = 9.sp)
        }
    }
}

@Composable
private fun AttendancePanel(snapshot: MemberSnapshot) {
    val attendance = snapshot.attendance
    val days = snapshot.attendanceHistory
    val percent = attendance?.let { if ((it.target ?: 0) > 0) (it.visits * 100 / (it.target ?: 1)) else 0 } ?: 0
    val monthDays = remember(days) { currentMonthDayCount() }
    val dayMap = remember(days) { days.associateBy { dayOfMonth(it.date) } }

    SectionCard(
        title = "Attendance",
        subtitle = currentMonthLabel(),
        accent = MIColor.green,
        surface = Color(0xFFF2FFF8),
        trailing = { CircularKpi(percent.coerceIn(0, 100), MIColor.green, MIColor.mint, "%") }
    ) {
        if (days.isEmpty()) {
            EmptyDataLine("Daily attendance history is not available yet.")
            attendance?.let {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniStat("Visits", it.visits.toString(), MIColor.green)
                    MiniStat("Target", it.target?.toString() ?: "—", MIColor.muted)
                    MiniStat("Streak", it.streakDays?.let { n -> "${n}d" } ?: "—", MIColor.green)
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..monthDays).chunked(7).forEach { week ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                        week.forEach { dayNumber ->
                            val status = dayMap[dayNumber]?.status ?: AttendanceDayStatus.UNKNOWN
                            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Box(
                                    Modifier.size(23.dp).clip(CircleShape)
                                        .background(attendanceDotColor(status))
                                        .border(1.dp, if (status == AttendanceDayStatus.LATE) MIColor.amber else Color.Transparent, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (status == AttendanceDayStatus.HOLIDAY || status == AttendanceDayStatus.CLOSED) {
                                        Text("×", color = MIColor.white, fontSize = 14.sp, fontWeight = FontWeight.Black)
                                    } else if (status == AttendanceDayStatus.UNKNOWN) {
                                        Text("·", color = MIColor.muted, fontSize = 15.sp, fontWeight = FontWeight.Black)
                                    } else if (status == AttendanceDayStatus.PRESENT) {
                                        Icon(Icons.Rounded.CheckCircle, null, tint = MIColor.white, modifier = Modifier.size(14.dp))
                                    }
                                }
                                Text(dayNumber.toString(), color = MIColor.ink, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        repeat(7 - week.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    LegendDot("Present", MIColor.green)
                    LegendDot("Absent", MIColor.red)
                    LegendDot("Late", MIColor.amber)
                    LegendDot("Holiday", MIColor.slate)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniStat("Streak", attendance?.streakDays?.let { "${it} days" } ?: "—", MIColor.green)
                    MiniStat("Lifetime", attendance?.lifetimeVisits?.toString() ?: "—", MIColor.ink)
                    MiniStat("Per week", attendance?.avgVisitsPerWeek?.let { String.format(Locale.getDefault(), "%.1f", it) } ?: "—", MIColor.green)
                }
            }
        }
    }
}

private fun attendanceDotColor(status: AttendanceDayStatus): Color = when (status) {
    AttendanceDayStatus.PRESENT -> MIColor.green
    AttendanceDayStatus.ABSENT -> MIColor.red
    AttendanceDayStatus.LATE -> MIColor.green
    AttendanceDayStatus.HOLIDAY, AttendanceDayStatus.CLOSED -> MIColor.slate
    AttendanceDayStatus.UNKNOWN -> MIColor.slate.copy(alpha = 0.18f)
}

@Composable
private fun WorkoutPanel(snapshot: MemberSnapshot) {
    val workoutDays = snapshot.workoutHistory
    val durations = workoutDays.mapNotNull { it.durationMinutes }.filter { it >= 0 }
    val average = durations.takeIf { it.isNotEmpty() }?.average()?.roundToInt()
    val peak = workoutDays.filter { it.status == WorkoutDayStatus.RECORDED }.maxByOrNull { it.durationMinutes ?: -1 }
    val lowest = workoutDays.filter { it.status == WorkoutDayStatus.RECORDED && it.durationMinutes != null }.minByOrNull { it.durationMinutes!! }

    SectionCard(
        title = "Workout Trend",
        subtitle = "Last 30 days · minutes per session",
        accent = MIColor.blue,
        surface = Color(0xFFF2F8FF),
        trailing = {
            if (average != null) CircularKpi(average.coerceAtMost(100), MIColor.blue, MIColor.blueSoft, "m")
        }
    ) {
        if (workoutDays.isEmpty()) {
            EmptyDataLine(snapshot.workout?.durationMinutes?.let { "Latest recorded session: ${it} min. Daily history is not linked yet." } ?: "Daily workout history is not available yet.")
        } else {
            WorkoutBars(workoutDays)
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                MiniStat("Peak", peak?.durationMinutes?.let { "${it}m" } ?: "—", MIColor.green)
                MiniStat("Lowest", lowest?.durationMinutes?.let { "${it}m" } ?: "—", MIColor.amber)
                MiniStat("Average", average?.let { "${it}m" } ?: "—", MIColor.blue)
            }
        }
    }
}

@Composable
private fun WorkoutBars(days: List<com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDay>) {
    val durations = days.mapNotNull { it.durationMinutes }.filter { it >= 0 }
    val max = (durations.maxOrNull() ?: 60).coerceAtLeast(60)
    val average = durations.takeIf { it.isNotEmpty() }?.average() ?: 0.0
    Row(
        Modifier.fillMaxWidth().height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        days.takeLast(30).forEach { day ->
            val duration = day.durationMinutes
            val color = when {
                day.status == WorkoutDayStatus.CONFIRMED_NO_WORKOUT -> MIColor.red
                duration == null || day.status == WorkoutDayStatus.UNKNOWN -> MIColor.slate.copy(alpha = 0.35f)
                duration >= average -> MIColor.green
                duration >= average * 0.65 -> MIColor.amber
                else -> MIColor.red
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                Box(
                    Modifier.fillMaxWidth(0.72f)
                        .height(if (duration == null) 4.dp else (5 + (duration.toFloat() / max * 56).toInt()).dp)
                        .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                        .background(color)
                )
            }
        }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("30d ago", color = MIColor.muted, fontSize = 9.sp)
        Text("Today", color = MIColor.muted, fontSize = 9.sp)
    }
}

@Composable
private fun BodyProgressPanel(snapshot: MemberSnapshot) {
    val history = snapshot.bodyProgress.sortedBy { it.date }
    val latest = history.lastOrNull()
    val previous = history.dropLast(1).lastOrNull()
    SectionCard(
        title = "Body Progress",
        subtitle = if (history.isNotEmpty()) "Since joined · latest measurements" else "Measurements & goal-aligned trends",
        accent = MIColor.purple,
        surface = Color(0xFFFAF7FF),
        trailing = { Icon(Icons.Rounded.ChevronRight, contentDescription = "Open body progress", tint = MIColor.purple) }
    ) {
        if (latest == null) {
            EmptyDataLine("No body measurements recorded. Add a measurement to start a real trend.")
        } else {
            val metrics = listOf(
                MetricValue("Weight", latest.weightKg, previous?.weightKg, "kg", MIColor.green),
                MetricValue("Muscle", latest.muscleKg, previous?.muscleKg, "kg", MIColor.purple),
                MetricValue("Body fat", latest.bodyFatPercent, previous?.bodyFatPercent, "%", MIColor.red),
                MetricValue("BMI", latest.bmi, previous?.bmi, "", MIColor.blue)
            ).filter { it.value != null }
            metrics.chunked(2).forEach { rowMetrics ->
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
                    rowMetrics.forEach { metric ->
                        MetricTile(metric, Modifier.weight(1f))
                    }
                    repeat(2 - rowMetrics.size) { Spacer(Modifier.weight(1f)) }
                }
            }
            Text("Trend color is goal-aware only when the member goal is known; otherwise changes are shown neutrally.", color = MIColor.muted, fontSize = 9.sp)
        }
    }
}

private data class MetricValue(val label: String, val value: Double?, val previous: Double?, val unit: String, val accent: Color)

@Composable
private fun MetricTile(metric: MetricValue, modifier: Modifier = Modifier) {
    val delta = if (metric.value != null && metric.previous != null) metric.value - metric.previous else null
    Column(
        modifier.clip(RoundedCornerShape(14.dp)).background(MIColor.white)
            .border(1.dp, metric.accent.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
            .padding(10.dp)
    ) {
        Text(metric.label, color = MIColor.muted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Text(
            "${formatMetric(metric.value)}${if (metric.unit.isBlank()) "" else " ${metric.unit}"}",
            color = MIColor.ink, fontSize = 19.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        if (delta != null) {
            val positive = delta >= 0
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(if (positive) Icons.Rounded.ArrowUpward else Icons.Rounded.ArrowDownward, null, tint = metric.accent, modifier = Modifier.size(13.dp))
                Text(
                    String.format(Locale.getDefault(), "%.1f", kotlin.math.abs(delta)),
                    color = metric.accent, fontSize = 11.sp, fontWeight = FontWeight.Bold
                )
            }
        } else {
            Text("Baseline", color = MIColor.muted, fontSize = 10.sp)
        }
        MiniSparkline(values = listOfNotNull(metric.previous, metric.value), color = metric.accent, modifier = Modifier.fillMaxWidth().height(18.dp))
    }
}

@Composable
private fun CircularKpi(value: Int, accent: Color, track: Color, suffix: String) {
    Box(Modifier.size(58.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 5.dp.toPx()
            drawArc(track, -90f, 360f, false, style = Stroke(stroke, cap = StrokeCap.Round))
            drawArc(accent, -90f, 360f * (value.coerceIn(0, 100) / 100f), false, style = Stroke(stroke, cap = StrokeCap.Round))
        }
        Text("$value$suffix", color = MIColor.ink, fontSize = 13.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun MiniSparkline(values: List<Double>, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        if (values.size < 2) return@Canvas
        val min = values.minOrNull() ?: return@Canvas
        val max = values.maxOrNull() ?: return@Canvas
        val range = (max - min).takeIf { it > 0.0 } ?: 1.0
        val points = values.mapIndexed { index, value ->
            Offset(
                x = size.width * index / (values.size - 1).toFloat(),
                y = size.height - ((value - min) / range).toFloat() * (size.height * 0.72f) - size.height * 0.14f
            )
        }
        for (i in 0 until points.lastIndex) {
            drawLine(color, points[i], points[i + 1], strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
        }
    }
}

@Composable
private fun RowScope.MiniStat(label: String, value: String, accent: Color) {
    Column(
        Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(MIColor.white)
            .border(1.dp, MIColor.line, RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 7.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(label, color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
        Text(value, color = accent, fontSize = 14.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}


@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(label, color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun EmptyDataLine(message: String) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MIColor.white)
            .border(1.dp, MIColor.line, RoundedCornerShape(12.dp)).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(message, color = MIColor.muted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

private fun formatDate(time: Long): String = SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(Date(time))
private fun money(amount: Double): String = java.text.NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(amount).replace(".00", "")
private fun compactMoney(amount: Double): String = if (amount >= 1000) "₹${(amount / 1000.0).let { String.format(Locale.getDefault(), "%.1f", it) }}k" else "₹${amount.roundToInt()}"
private fun formatMetric(value: Double?): String = value?.let { String.format(Locale.getDefault(), "%.1f", it) } ?: "—"
private fun currentMonthLabel(): String = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
private fun currentMonthDayCount(): Int = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
private fun dayOfMonth(time: Long): Int = Calendar.getInstance().apply { timeInMillis = time }.get(Calendar.DAY_OF_MONTH)
private fun lastTwelveMonths(): List<Pair<Long, String>> {
    val base = Calendar.getInstance()
    base.set(Calendar.DAY_OF_MONTH, 1)
    base.set(Calendar.HOUR_OF_DAY, 0); base.set(Calendar.MINUTE, 0); base.set(Calendar.SECOND, 0); base.set(Calendar.MILLISECOND, 0)
    return (11 downTo 0).map { offset ->
        val c = base.clone() as Calendar
        c.add(Calendar.MONTH, -offset)
        c.timeInMillis to SimpleDateFormat("MMM", Locale.getDefault()).format(c.time)
    }
}
private fun sameMonth(first: Long, second: Long): Boolean {
    val a = Calendar.getInstance().apply { timeInMillis = first }
    val b = Calendar.getInstance().apply { timeInMillis = second }
    return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) && a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
}
private fun moneyLabel(amount: Double): String = compactMoney(amount)
private const val DAY_MS = 86_400_000L
