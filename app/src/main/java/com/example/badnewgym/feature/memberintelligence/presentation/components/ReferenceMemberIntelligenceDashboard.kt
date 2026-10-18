package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.domain.engine.DashboardMath
import com.example.badnewgym.feature.memberintelligence.domain.engine.PaymentCalculator
import com.example.badnewgym.feature.memberintelligence.domain.format.MoneyFormat
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTimeliness
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDay
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDayStatus
import com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios
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

/** Panels list order used by the navigation chips. Must mirror the LazyColumn items below. */
private const val SECTION_BRAND = 0
private const val SECTION_IDENTITY = 1
private const val SECTION_PAYMENT = 3
private const val SECTION_ATTENDANCE = 4
private const val SECTION_WORKOUT = 5
private const val SECTION_BODY = 6

private data class MenuItemUi(
    val id: MenuType,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val scrollTo: Int? = null,
    val dialog: DashboardDialog? = null
)

private enum class DashboardDialog { SEARCH, NOTIFICATIONS, ABOUT, RECEIPT }

@Composable
fun ReferenceMemberIntelligenceDashboard(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    menus: List<MemberMenu>,
    activeMenu: MenuType?,
    onMenuSelected: (MenuType) -> Unit,
    paymentAction: SignalAction? = null,
    onCta: (SignalAction) -> Unit = {},
    onMemberSelected: (String) -> Unit = {},
    onNotice: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var openDialog by remember { mutableStateOf<DashboardDialog?>(null) }

    val menuItems = remember(menus) {
        listOf(
            MenuItemUi(MenuType.HOME, "Overview", Icons.Rounded.Home, SECTION_BRAND),
            MenuItemUi(MenuType.PAYMENT, "Payment", Icons.Rounded.Payment, SECTION_PAYMENT),
            MenuItemUi(MenuType.ATTENDANCE, "Attendance", Icons.Rounded.CalendarMonth, SECTION_ATTENDANCE),
            MenuItemUi(MenuType.WORKOUT, "Workout", Icons.Rounded.FitnessCenter, SECTION_WORKOUT),
            MenuItemUi(MenuType.INSIGHT, "Progress", Icons.Rounded.Insights, SECTION_BODY),
            MenuItemUi(MenuType.NUTRITION, "Nutrition", Icons.Rounded.Restaurant, null),
            MenuItemUi(MenuType.SERVICES, "Machine", Icons.Rounded.GridView, null),
            MenuItemUi(MenuType.HOME, "More", Icons.Rounded.MoreHoriz, dialog = DashboardDialog.ABOUT)
        ).filter { item ->
            menus.firstOrNull { it.id == item.id }?.let { it.isVisible && it.isEnabled && !it.isLocked }
                ?: (item.id == MenuType.HOME)
        }
    }

    fun openSection(item: MenuItemUi) {
        if (item.dialog != null) {
            openDialog = item.dialog
            return
        }
        onMenuSelected(item.id)
        val index = item.scrollTo
        if (index == null) {
            onNotice("${item.label} has its own module and is not part of this dashboard yet.")
        } else {
            scope.launch { listState.animateScrollToItem(index) }
        }
    }

    Box(
        modifier = modifier.fillMaxSize().background(MIColor.page),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().widthIn(max = 480.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item(key = "brand") {
                AppBrandStrip(
                    alertCount = snapshot.issues.size,
                    onSearch = { openDialog = DashboardDialog.SEARCH },
                    onNotifications = { openDialog = DashboardDialog.NOTIFICATIONS },
                    onMore = { openDialog = DashboardDialog.ABOUT }
                )
            }
            item(key = "identity") { MemberSummaryCard(snapshot) }
            item(key = "menu") {
                HorizontalMemberMenu(
                    items = menuItems,
                    selected = activeMenu ?: MenuType.HOME,
                    onSelect = ::openSection
                )
            }
            item(key = "payment") {
                PaymentPlanPanel(
                    snapshot = snapshot,
                    paymentAction = paymentAction,
                    onCta = onCta,
                    onOpenReceipt = { openDialog = DashboardDialog.RECEIPT }
                )
            }
            item(key = "attendance") { AttendancePanel(snapshot) }
            item(key = "workout") { WorkoutPanel(snapshot) }
            item(key = "body") { BodyProgressPanel(snapshot) }
        }
    }

    when (openDialog) {
        DashboardDialog.SEARCH -> MemberPickerDialog(
            onSelect = { code ->
                openDialog = null
                onMemberSelected(code)
            },
            onDismiss = { openDialog = null }
        )
        DashboardDialog.NOTIFICATIONS -> NotificationsDialog(
            snapshot = snapshot,
            onDismiss = { openDialog = null }
        )
        DashboardDialog.RECEIPT -> ReceiptDialog(payment = snapshot.payment, onDismiss = { openDialog = null })
        DashboardDialog.ABOUT -> AboutDialog(onDismiss = { openDialog = null })
        null -> Unit
    }
}

// ------------------------------------------------------------------ brand strip

@Composable
private fun AppBrandStrip(
    alertCount: Int,
    onSearch: () -> Unit,
    onNotifications: () -> Unit,
    onMore: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("BAD", color = MIColor.green, fontSize = 23.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, letterSpacing = (-0.7).sp)
                Text(" GYM", color = MIColor.ink, fontSize = 23.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, letterSpacing = (-0.7).sp)
            }
            Text("MEMBER INTELLIGENCE", color = MIColor.ink, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
        }
        SmallHeaderAction(Icons.Rounded.Search, "Search members", onSearch)
        Spacer(Modifier.width(7.dp))
        Box {
            SmallHeaderAction(Icons.Rounded.Notifications, "Member alerts", onNotifications)
            if (alertCount > 0) {
                Text(
                    text = if (alertCount > 9) "9+" else alertCount.toString(),
                    color = MIColor.white,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 27.dp)
                        .clip(CircleShape)
                        .background(MIColor.red)
                        .defaultMinSize(minWidth = 15.dp, minHeight = 15.dp)
                        .wrapContentSize(Alignment.Center)
                        .padding(horizontal = 3.dp)
                )
            }
        }
        Spacer(Modifier.width(7.dp))
        SmallHeaderAction(Icons.Rounded.MoreVert, "About this dashboard", onMore)
    }
}

@Composable
private fun SmallHeaderAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.size(40.dp).clip(CircleShape).clickable(onClick = onClick),
        shape = CircleShape,
        color = MIColor.white,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, MIColor.line)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = description, tint = MIColor.ink, modifier = Modifier.size(21.dp))
        }
    }
}

@Composable
private fun TinyStatusChip(label: String, bg: Color, fg: Color) {
    Box(Modifier.clip(RoundedCornerShape(30.dp)).background(bg).padding(horizontal = 8.dp, vertical = 5.dp)) {
        Text(label, color = fg, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

// ------------------------------------------------------------------ navigation rail

@Composable
private fun HorizontalMemberMenu(items: List<MenuItemUi>, selected: MenuType, onSelect: (MenuItemUi) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items.forEach { item ->
            val active = item.label != "More" && item.id == selected
            Column(
                Modifier.weight(1f).defaultMinSize(minHeight = 48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (active) MIColor.green else MIColor.white)
                    .border(1.dp, if (active) MIColor.green else MIColor.line, RoundedCornerShape(12.dp))
                    .clickable { onSelect(item) }
                    .padding(horizontal = 1.dp, vertical = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(item.icon, contentDescription = item.label, tint = if (active) MIColor.white else menuTint(item.id), modifier = Modifier.size(17.dp))
                Text(item.label, color = if (active) MIColor.white else MIColor.ink, fontSize = 8.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Clip)
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

// ------------------------------------------------------------------ section scaffold

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
        border = BorderStroke(1.dp, accent.copy(alpha = 0.22f))
    ) {
        Column(Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.width(3.dp).height(20.dp).clip(RoundedCornerShape(3.dp)).background(accent))
                Spacer(Modifier.width(6.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(title, color = accent, fontSize = 15.sp, fontWeight = FontWeight.Black, maxLines = 1)
                    if (subtitle != null) {
                        Text(subtitle, color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                    }
                }
                trailing?.invoke()
            }
            content()
        }
    }
}

// ------------------------------------------------------------------ payment

@Composable
private fun PaymentPlanPanel(
    snapshot: MemberSnapshot,
    paymentAction: SignalAction?,
    onCta: (SignalAction) -> Unit,
    onOpenReceipt: () -> Unit
) {
    val payment = snapshot.payment
    val membership = snapshot.membership
    val lifecycle = PaymentCalculator.resolve(payment)
    val outstanding = payment?.totalOutstanding ?: 0.0
    val due = outstanding > 0.0
    val accent = if (due) MIColor.red else MIColor.green
    val surface = if (due) Color(0xFFFFF5F5) else Color(0xFFF4FFF8)
    val now = System.currentTimeMillis()

    SectionCard(
        title = "Payment & Plan",
        subtitle = membership?.let { "${it.planName} · ${it.planType}" },
        accent = accent,
        surface = surface,
        trailing = {
            TinyStatusChip(
                label = if (due) "ACTION NEEDED" else "PAID",
                bg = if (due) MIColor.redSoft else MIColor.mint,
                fg = accent
            )
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Column(Modifier.weight(1.05f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(if (due) "DUE" else "PAID · ON TIME", color = accent, fontSize = 8.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                val paidAmount = if (due) outstanding else payment?.lastPaymentAmount
                Text(paidAmount?.let { MoneyFormat.inr(it) } ?: "—", color = accent, fontSize = 17.sp, fontWeight = FontWeight.Black, maxLines = 1)
                Text(
                    when {
                        due && (payment?.overdueDays ?: 0) > 0 -> "${payment?.overdueDays} days overdue"
                        due && payment?.dueDate != null -> "Due ${formatDate(payment.dueDate!!)}"
                        !due && payment?.lastPaymentDate != null -> "Paid ${formatDate(payment.lastPaymentDate)}"
                        else -> "No receipt linked"
                    }, color = if (due) MIColor.red else MIColor.muted, fontSize = 7.sp, maxLines = 1
                )
            }
            Box(Modifier.width(1.dp).height(43.dp).background(MIColor.line))
            Column(Modifier.weight(0.9f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text("Next Due", color = MIColor.muted, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                val nextAmount = if (due) outstanding else membership?.currentCost
                Text(nextAmount?.let { MoneyFormat.inr(it) } ?: "—", color = MIColor.ink, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                Text(
                    when {
                        due && payment?.dueDate != null -> "${formatDate(payment.dueDate!!)} · ${dueRelativeLabel(payment.dueDate!!, now)}"
                        membership != null -> "${formatDate(membership.expiryDate)} · ${dueRelativeLabel(membership.expiryDate, now)}"
                        else -> "Date unavailable"
                    }, color = MIColor.muted, fontSize = 7.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
            Button(
                onClick = {
                    if (due) {
                        val action = paymentAction
                            ?: SignalAction(label = "Collect ${MoneyFormat.inr(outstanding)}", actionType = "COLLECT_PAYMENT")
                        onCta(action)
                    } else onOpenReceipt()
                },
                modifier = Modifier.weight(0.86f).height(42.dp),
                shape = RoundedCornerShape(10.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (due) MIColor.blue else MIColor.blueSoft, contentColor = if (due) MIColor.white else MIColor.blue)
            ) {
                Icon(if (due) Icons.Rounded.Payment else Icons.Rounded.ReceiptLong, null, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(3.dp))
                Text(if (due) "Collect" else "View Receipt", fontSize = 8.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }
        }
        PaymentHistoryChart(snapshot)
    }
}

private fun dueRelativeLabel(target: Long, now: Long): String {
    val days = ((target - now) / DashboardMath.DAY_MS).toInt()
    return when {
        days > 1 -> "in $days days"
        days == 1 -> "in 1 day"
        days == 0 -> "Due today"
        days == -1 -> "1 day overdue"
        else -> "${-days} days overdue"
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
    val hasUpgrade = history.any { it.planUpgraded }
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Payment History", color = MIColor.ink, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            Text("12 Months", color = MIColor.blue, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        }
        if (history.isEmpty()) {
            EmptyDataLine("No payment transactions are linked yet.")
        } else {
            Row(
                Modifier.fillMaxWidth().height(48.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
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
                            Text(compactMoney(amount), color = MIColor.ink, fontSize = 6.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        } else {
                            Spacer(Modifier.height(8.dp))
                        }
                        Box(
                            Modifier.fillMaxWidth(0.68f)
                                .height(if (amount > 0.0) (8 + (amount / maxValue * 22).toInt()).dp else 3.dp)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(monthColor)
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(month.second, color = MIColor.muted, fontSize = 6.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TinyLegend("On time", MIColor.green)
                TinyLegend("Late", MIColor.amber)
                TinyLegend("Very late", MIColor.red)
                if (hasUpgrade) TinyLegend("Plan upgraded", MIColor.gold)
                TinyLegend("No transaction", MIColor.slate.copy(alpha = 0.5f))
            }
            Text(
                "No transaction is shown as an empty month, not as a confirmed missed payment.",
                color = MIColor.muted, fontSize = 6.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ------------------------------------------------------------------ attendance

@Composable
private fun AttendancePanel(snapshot: MemberSnapshot) {
    val attendance = snapshot.attendance
    val days = snapshot.attendanceHistory
    val now = System.currentTimeMillis()
    val percent = DashboardMath.attendancePercent(attendance?.visits ?: 0, attendance?.target)
    val counts = remember(days, now) { DashboardMath.statusCounts(days, now) }
    val leadingOffset = remember(now) { DashboardMath.firstWeekdayOffset(now) }
    val monthDayCount = remember(now) { DashboardMath.daysInMonth(now) }
    val dayByNumber = remember(days, now) {
        days.filter { DashboardMath.monthKey(it.date) == DashboardMath.monthKey(now) }
            .associateBy { dayOfMonth(it.date) }
    }

    SectionCard(
        title = "Attendance",
        subtitle = currentMonthLabel(),
        accent = MIColor.green,
        surface = Color(0xFFF2FFF8)
    ) {
        if (days.isEmpty()) {
            EmptyDataLine("Daily attendance history is not available yet.")
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1.5f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf("S", "M", "T", "W", "T", "F", "S").forEach { letter ->
                            Text(letter, color = MIColor.muted, fontSize = 7.sp, fontWeight = FontWeight.Bold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.weight(1f))
                        }
                    }
                    val padded: List<Int?> = List(leadingOffset) { null } + (1..monthDayCount).toList()
                    padded.chunked(7).forEach { week ->
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.fillMaxWidth()) {
                            week.forEach { dayNumber ->
                                Box(Modifier.weight(1f)) {
                                    if (dayNumber == null) Spacer(Modifier.height(20.dp))
                                    else AttendanceDayCell(
                                        dayNumber = dayNumber,
                                        status = dayByNumber[dayNumber]?.status ?: AttendanceDayStatus.UNKNOWN
                                    )
                                }
                            }
                            repeat(7 - week.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
                Column(Modifier.weight(0.85f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    CompactAttendanceCount("Present", counts[AttendanceDayStatus.PRESENT] ?: 0, MIColor.green)
                    CompactAttendanceCount("Absent", counts[AttendanceDayStatus.ABSENT] ?: 0, MIColor.red)
                    CompactAttendanceCount("Late", counts[AttendanceDayStatus.LATE] ?: 0, MIColor.amber)
                    val closures = (counts[AttendanceDayStatus.HOLIDAY] ?: 0) + (counts[AttendanceDayStatus.CLOSED] ?: 0)
                    if (closures > 0) CompactAttendanceCount("Holiday", closures, MIColor.slate)
                    if ((counts[AttendanceDayStatus.UNKNOWN] ?: 0) > 0) CompactAttendanceCount("No record", counts[AttendanceDayStatus.UNKNOWN] ?: 0, MIColor.slate)
                }
                CircularKpi(
                    ringPercent = percent ?: 0,
                    centerText = percent?.let { "$it%" } ?: "—",
                    caption = if (attendance?.target != null) "${attendance.visits}/${attendance.target}" else null,
                    accent = MIColor.green,
                    track = MIColor.mint
                )
            }
        }
    }
}

@Composable
private fun CompactAttendanceCount(label: String, count: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(color))
        Text(label, color = MIColor.ink, fontSize = 7.sp, maxLines = 1, modifier = Modifier.weight(1f))
        Text(count.toString(), color = MIColor.ink, fontSize = 8.sp, fontWeight = FontWeight.Bold)
    }
}

/** Sunday-first calendar letters match the reference and Indian gym weeks. */
@Composable
private fun AttendanceDayCell(dayNumber: Int, status: AttendanceDayStatus) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Box(
            Modifier.size(14.dp).clip(CircleShape)
                .background(attendanceDotColor(status))
                .border(1.dp, if (status == AttendanceDayStatus.LATE) MIColor.amber else Color.Transparent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when (status) {
                AttendanceDayStatus.HOLIDAY, AttendanceDayStatus.CLOSED ->
                    Text("×", color = MIColor.white, fontSize = 10.sp, fontWeight = FontWeight.Black)
                AttendanceDayStatus.UNKNOWN -> Text("·", color = MIColor.muted, fontSize = 11.sp, fontWeight = FontWeight.Black)
                AttendanceDayStatus.PRESENT ->
                    Icon(Icons.Rounded.CheckCircle, null, tint = MIColor.white, modifier = Modifier.size(9.dp))
                else -> Unit
            }
        }
        Text(dayNumber.toString(), color = MIColor.ink, fontSize = 6.sp, fontWeight = FontWeight.SemiBold, lineHeight = 7.sp)
    }
}

private fun attendanceDotColor(status: AttendanceDayStatus): Color = when (status) {
    AttendanceDayStatus.PRESENT -> MIColor.green
    AttendanceDayStatus.ABSENT -> MIColor.red
    AttendanceDayStatus.LATE -> MIColor.green
    AttendanceDayStatus.HOLIDAY, AttendanceDayStatus.CLOSED -> MIColor.slate
    AttendanceDayStatus.UNKNOWN -> MIColor.slate.copy(alpha = 0.18f)
}

// ------------------------------------------------------------------ workout

@Composable
private fun WorkoutPanel(snapshot: MemberSnapshot) {
    val workoutDays = snapshot.workoutHistory
    val now = System.currentTimeMillis()
    val currentKey = DashboardMath.monthKey(now)
    val monthDays = remember(workoutDays, currentKey) {
        workoutDays
            .filter { DashboardMath.monthKey(it.date) == currentKey }
            .associateBy { dayOfMonth(it.date) }
    }
    val monthRecorded = remember(workoutDays, currentKey) {
        workoutDays.filter { DashboardMath.monthKey(it.date) == currentKey }
    }
    val monthDayCount = remember(now) { DashboardMath.daysInMonth(now) }
    val average = DashboardMath.recordedAverageMinutes(monthRecorded)
    val peak = DashboardMath.peakRecordedDay(monthRecorded)
    val lowest = DashboardMath.lowestRecordedDay(monthRecorded)
    val previousKey = DashboardMath.previousMonthKey(currentKey)
    val currentMonthAverage = average?.toDouble()
    val previousMonthAverage = DashboardMath.averageForMonth(workoutDays, previousKey)
    val monthOverMonth = DashboardMath.monthOverMonthPercent(currentMonthAverage, previousMonthAverage)

    SectionCard(
        title = "Workout Trend",
        subtitle = "${currentMonthLabel()} · minutes per session",
        accent = MIColor.blue,
        surface = Color(0xFFF2F8FF),
        trailing = {
            CircularKpi(
                ringPercent = (((average ?: 0) * 100.0) / 120.0).toInt().coerceIn(0, 100),
                centerText = average?.let { "${it}m" } ?: "—",
                caption = "avg / day",
                accent = MIColor.blue,
                track = MIColor.blueSoft
            )
        }
    ) {
        if (workoutDays.isEmpty()) {
            EmptyDataLine(
                snapshot.workout?.durationMinutes?.let {
                    "Latest recorded session: ${it} min. Daily history is not linked yet."
                } ?: "Daily workout history is not available yet."
            )
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f)) {
                    WorkoutMonthChart(
                        monthDayCount = monthDayCount,
                        dayByNumber = monthDays,
                        average = average
                    )
                }
                Column(Modifier.width(74.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    CompactWorkoutStat("Peak", peak?.durationMinutes?.let { "${it}m" } ?: "—", MIColor.green, peak?.let { formatDate(it.date) })
                    CompactWorkoutStat("Lowest", lowest?.durationMinutes?.let { "${it}m" } ?: "—", MIColor.red, lowest?.let { formatDate(it.date) })
                    if (monthOverMonth != null) {
                        val rounded = monthOverMonth.roundToInt()
                        CompactWorkoutStat("vs ${monthLabelFromKey(previousKey)}", "${if (rounded >= 0) "+" else ""}$rounded%", if (rounded >= 0) MIColor.green else MIColor.amber, "avg / day")
                    } else {
                        CompactWorkoutStat("Average", average?.let { "${it}m" } ?: "—", MIColor.blue, "per day")
                    }
                }
            }
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendDot("Above average", MIColor.green)
                LegendDot("Below average", MIColor.amber)
                LegendDot("No workout", MIColor.red)
                LegendDot("No record", MIColor.slate.copy(alpha = 0.5f))
            }
            Text(
                "Days without a workout record stay empty in the chart — they are never drawn as 0.",
                color = MIColor.muted, fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun CompactWorkoutStat(label: String, value: String, accent: Color, caption: String?) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(9.dp)).background(MIColor.white)
            .border(1.dp, accent.copy(alpha = 0.18f), RoundedCornerShape(9.dp)).padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(value, color = accent, fontSize = 11.sp, fontWeight = FontWeight.Black, maxLines = 1)
        Column(Modifier.weight(1f)) {
            Text(label, color = MIColor.ink, fontSize = 7.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            caption?.let { Text(it, color = MIColor.muted, fontSize = 6.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }
        }
    }
}

@Composable
private fun WorkoutMonthChart(
    monthDayCount: Int,
    dayByNumber: Map<Int, WorkoutDay>,
    average: Int?
) {
    val recordedMax = dayByNumber.values.mapNotNull { it.durationMinutes }.maxOrNull()?.coerceAtLeast(60) ?: 60
    val scale = recordedMax.coerceAtLeast(60)
    val labels = buildList {
        addAll(listOf(1, 5, 10, 15, 20, 25).filter { it < monthDayCount })
        add(monthDayCount)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Column(
            Modifier
                .height(70.dp)
                .fillMaxWidth(0.075f)
                .widthIn(min = 22.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text("${scale}m", color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
            Text("${scale / 2}m", color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
            Text("0m", color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
        }
        Column(Modifier.weight(1f)) {
            Row(
                Modifier.fillMaxWidth().height(70.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                (1..monthDayCount).forEach { dayNumber ->
                    val day = dayByNumber[dayNumber]
                    val bucket = DashboardMath.workoutBucket(day ?: WorkoutDay(0, null, WorkoutDayStatus.UNKNOWN), average)
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        when {
                            day != null && day.status == WorkoutDayStatus.RECORDED &&
                                day.durationMinutes != null && day.durationMinutes > 0 -> {
                                Box(
                                    Modifier.fillMaxWidth(0.7f)
                                        .height((4 + (day.durationMinutes!!.toDouble() / scale * 54).toInt()).dp)
                                        .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                        .background(
                                            when (bucket) {
                                                DashboardMath.WorkoutBucket.ABOVE_AVERAGE -> MIColor.green
                                                DashboardMath.WorkoutBucket.BELOW_AVERAGE -> MIColor.amber
                                                else -> MIColor.slate.copy(alpha = 0.4f)
                                            }
                                        )
                                )
                            }
                            bucket == DashboardMath.WorkoutBucket.NO_WORKOUT -> {
                                LightDot(MIColor.red)
                            }
                            else -> LightDot(MIColor.slate.copy(alpha = 0.55f))
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth()) {
                (1..monthDayCount).forEach { day ->
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        if (day in labels) {
                            Text(day.toString(), color = MIColor.muted, fontSize = 6.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        } else {
                            Spacer(Modifier.height(11.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LightDot(color: Color) {
    Box(
        Modifier
            .padding(bottom = 2.dp)
            .size(5.dp)
            .clip(CircleShape)
            .background(color)
    )
}

// ------------------------------------------------------------------ body progress

@Composable
private fun BodyProgressPanel(snapshot: MemberSnapshot) {
    val history = snapshot.bodyProgress.sortedBy { it.date }
    val latest = history.lastOrNull()
    val previous = history.dropLast(1).lastOrNull()
    val goal = snapshot.identity.goal

    SectionCard(
        title = "Body Progress",
        subtitle = if (history.isNotEmpty()) {
            "Since ${formatDate(snapshot.identity.memberSince)} · latest measurements"
        } else {
            "Measurements & goal-aligned trends"
        },
        accent = MIColor.purple,
        surface = Color(0xFFFAF7FF)
    ) {
        if (latest == null) {
            EmptyDataLine("No body measurements recorded. Add a measurement to start a real trend.")
        } else {
            val metrics = buildList {
                add(BodyMetric("Weight", latest.weightKg, previous?.weightKg, "kg", history.mapNotNull { it.weightKg }))
                add(BodyMetric("Muscle", latest.muscleKg, previous?.muscleKg, "kg", history.mapNotNull { it.muscleKg }))
                add(BodyMetric("Body fat", latest.bodyFatPercent, previous?.bodyFatPercent, "%", history.mapNotNull { it.bodyFatPercent }))
                add(BodyMetric("BMI", latest.bmi, previous?.bmi, null, history.mapNotNull { it.bmi }))
            }.filter { it.latest != null }
            metrics.chunked(4).forEach { rowMetrics ->
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
                    rowMetrics.forEach { metric ->
                        BodyMetricTile(metric, goal, Modifier.weight(1f))
                    }
                    repeat(4 - rowMetrics.size) { Spacer(Modifier.weight(1f)) }
                }
            }
            Text(
                if (goal != null) {
                    "Goal: $goal — colours follow progress toward it."
                } else {
                    "Trend colour is goal-aware only when the member goal is known; otherwise changes are shown neutrally."
                },
                color = MIColor.muted, fontSize = 7.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private data class BodyMetric(
    val label: String,
    val latest: Double?,
    val previous: Double?,
    val unit: String?,
    val series: List<Double>
)

@Composable
private fun BodyMetricTile(metric: BodyMetric, goal: String?, modifier: Modifier = Modifier) {
    val delta = if (metric.latest != null && metric.previous != null) metric.latest - metric.previous else null
    val direction = when (metric.label) {
        "BMI" -> DashboardMath.bmiDirection(metric.latest, metric.previous)
        else -> DashboardMath.deltaDirection(metric.label, goal, delta ?: 0.0)
    }
    val directionColor = when (direction) {
        DashboardMath.DeltaDirection.FAVORABLE -> MIColor.green
        DashboardMath.DeltaDirection.UNFAVORABLE -> MIColor.red
        DashboardMath.DeltaDirection.NEUTRAL -> MIColor.muted
    }
    Column(
        modifier.clip(RoundedCornerShape(14.dp)).background(MIColor.white)
            .border(1.dp, directionColor.copy(alpha = 0.22f), RoundedCornerShape(14.dp))
            .padding(horizontal = 5.dp, vertical = 5.dp)
    ) {
        Text(metric.label, color = MIColor.muted, fontSize = 8.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(
            formatMetricValue(metric.latest, metric.unit),
            color = MIColor.ink, fontSize = 13.sp, fontWeight = FontWeight.Black,
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        if (metric.label == "BMI") {
            val band = DashboardMath.bmiBand(metric.latest)
            if (band != null) {
                Text(band, color = directionColor, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
        }
        if (delta != null) {
            val growing = delta >= 0
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Icon(
                    if (growing) Icons.Rounded.ArrowUpward else Icons.Rounded.ArrowDownward,
                    null, tint = directionColor, modifier = Modifier.size(13.dp)
                )
                Text(
                    String.format(Locale.getDefault(), "%.1f", kotlin.math.abs(delta)) +
                        if (metric.unit.isNullOrBlank()) "" else " ${metric.unit}",
                    color = directionColor, fontSize = 8.sp, fontWeight = FontWeight.Bold, maxLines = 1
                )
            }
        } else {
            Text("Baseline", color = MIColor.muted, fontSize = 8.sp)
        }
        MiniSparkline(values = metric.series, color = directionColor, modifier = Modifier.fillMaxWidth().height(12.dp))
    }
}

// ------------------------------------------------------------------ shared visuals

@Composable
private fun CircularKpi(
    ringPercent: Int,
    centerText: String,
    caption: String?,
    accent: Color,
    track: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Box(Modifier.size(58.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val stroke = 5.dp.toPx()
                drawArc(track, -90f, 360f, false, style = Stroke(stroke, cap = StrokeCap.Round))
                drawArc(accent, -90f, 360f * (ringPercent.coerceIn(0, 100) / 100f), false, style = Stroke(stroke, cap = StrokeCap.Round))
            }
            Text(centerText, color = MIColor.ink, fontSize = 13.sp, fontWeight = FontWeight.Black)
        }
        if (caption != null) {
            Text(caption, color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
        }
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
private fun RowScope.MiniStat(label: String, value: String, accent: Color, caption: String? = null) {
    Column(
        Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(MIColor.white)
            .border(1.dp, MIColor.line, RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 7.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(label, color = MIColor.muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(value, color = accent, fontSize = 14.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (caption != null) {
            Text(caption, color = MIColor.muted, fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
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
private fun TinyLegend(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(color))
        Text(label, color = MIColor.ink, fontSize = 6.sp, maxLines = 1)
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

// ------------------------------------------------------------------ dialogs

@Composable
private fun MemberPickerDialog(onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Search members", fontWeight = FontWeight.Black) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    "Demo roster of this build. Choosing a member loads their real snapshot data.",
                    color = MIColor.muted, fontSize = 11.sp
                )
                Spacer(Modifier.height(8.dp))
                MemberScenarios.demoRoster.forEach { member ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelect(member.code) }
                            .padding(horizontal = 8.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(member.name, color = MIColor.ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(member.code, color = MIColor.muted, fontSize = 10.sp)
                        }
                        Icon(Icons.Rounded.ChevronRight, null, tint = MIColor.muted, modifier = Modifier.size(18.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable
private fun NotificationsDialog(snapshot: MemberSnapshot, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Member alerts", fontWeight = FontWeight.Black) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                if (snapshot.issues.isEmpty()) {
                    Text(
                        "No open issues are recorded for this member.",
                        color = MIColor.muted, fontSize = 12.sp
                    )
                } else {
                    snapshot.issues.forEach { issue ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(9.dp).clip(CircleShape).background(
                                    when (issue.severity) {
                                        com.example.badnewgym.feature.memberintelligence.domain.model.IssueSeverity.CRITICAL,
                                        com.example.badnewgym.feature.memberintelligence.domain.model.IssueSeverity.HIGH -> MIColor.red
                                        com.example.badnewgym.feature.memberintelligence.domain.model.IssueSeverity.MEDIUM -> MIColor.amber
                                        else -> MIColor.slate
                                    }
                                )
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(issue.description, color = MIColor.ink, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable
private fun ReceiptDialog(payment: com.example.badnewgym.feature.memberintelligence.domain.model.PaymentSummary?, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Receipt & history", fontWeight = FontWeight.Black) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (payment == null) {
                    Text("No payment record is linked to this member.", color = MIColor.muted, fontSize = 12.sp)
                } else {
                    Text(
                        "Lifetime paid: " + (payment.lifetimePaid?.let { MoneyFormat.inr(it) } ?: "not recorded"),
                        color = MIColor.ink, fontSize = 12.sp, fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Outstanding now: ${MoneyFormat.inr(payment.totalOutstanding)}",
                        color = MIColor.muted, fontSize = 11.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Recent transactions", color = MIColor.muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    payment.history.take(8).forEach { transaction ->
                        Row(
                            Modifier.fillMaxWidth().padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "${MoneyFormat.inr(transaction.amount)} · ${transaction.method}",
                                    color = MIColor.ink, fontSize = 12.sp, fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${formatDate(transaction.occurredAt)} · ${transaction.timeliness.name.lowercase().replace('_', ' ')}",
                                    color = MIColor.muted, fontSize = 10.sp
                                )
                            }
                            if (transaction.planUpgraded) {
                                Text("UPGRADE", color = MIColor.gold, fontSize = 9.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("About this dashboard", fontWeight = FontWeight.Black) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(
                    "Figures come from the demo scenario fixtures of this build, not from a production backend.",
                    color = MIColor.ink, fontSize = 12.sp
                )
                Text(
                    "Recorded payments are stored in this session only and refresh the panels immediately.",
                    color = MIColor.ink, fontSize = 12.sp
                )
                Text(
                    "Attendance and workout days without a record stay 'no record' — they are never drawn as zeros.",
                    color = MIColor.ink, fontSize = 12.sp
                )
                Text(
                    "Body changes are graded against the member's goal only when a goal is known.",
                    color = MIColor.ink, fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

// ------------------------------------------------------------------ formatting helpers

private fun formatDate(time: Long): String =
    SimpleDateFormat("d MMM yy", Locale.getDefault()).format(Date(time))

private fun formatMetricValue(value: Double?, unit: String?): String {
    if (value == null) return "—"
    val formatted = String.format(Locale.getDefault(), "%.1f", value)
    return formatted + (if (unit.isNullOrBlank()) "" else " $unit")
}

private fun compactMoney(amount: Double): String =
    if (amount >= 1000) {
        "₹${String.format(Locale.getDefault(), "%.1f", amount / 1000.0)}k"
    } else {
        "₹${amount.roundToInt()}"
    }

private fun currentMonthLabel(): String =
    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

private fun monthLabelFromKey(key: Long): String {
    val year = (key / 12).toInt()
    val month = (key % 12).toInt()
    val calendar = Calendar.getInstance().apply { set(Calendar.YEAR, year); set(Calendar.MONTH, month) }
    return SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(calendar.time)
}

private fun dayOfMonth(time: Long): Int =
    Calendar.getInstance().apply { timeInMillis = time }.get(Calendar.DAY_OF_MONTH)

private fun lastTwelveMonths(): List<Pair<Long, String>> {
    val base = Calendar.getInstance()
    base.set(Calendar.DAY_OF_MONTH, 1)
    base.set(Calendar.HOUR_OF_DAY, 0); base.set(Calendar.MINUTE, 0); base.set(Calendar.SECOND, 0); base.set(Calendar.MILLISECOND, 0)
    return (11 downTo 0).map { offset ->
        val calendar = base.clone() as Calendar
        calendar.add(Calendar.MONTH, -offset)
        calendar.timeInMillis to SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)
    }
}

private fun sameMonth(first: Long, second: Long): Boolean {
    val a = Calendar.getInstance().apply { timeInMillis = first }
    val b = Calendar.getInstance().apply { timeInMillis = second }
    return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) && a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
}
