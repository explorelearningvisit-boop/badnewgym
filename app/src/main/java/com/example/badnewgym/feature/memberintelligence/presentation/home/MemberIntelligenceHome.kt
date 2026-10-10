package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.badnewgym.feature.bodyprogress.presentation.BodyProgressUiState
import com.example.badnewgym.feature.bodyprogress.presentation.BodyProgressViewModel
import com.example.badnewgym.feature.memberintelligence.debug.HomeDebugBridge
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentMethod

// ------------------------------------------------------------------ rail

private data class RailItem(val id: String, val label: String, val icon: ImageVector)

/** Absolute card height ceiling. All six widgets must fit without scrolling. */
private val CARD_MAX_HEIGHT = 600.dp

/** Member domains. Home is selected; the rail is navigation, not widget data. */
private val RAIL_ITEMS = listOf(
    RailItem("home", "Home", Icons.Filled.Home),
    RailItem("members", "Members", Icons.Filled.People),
    RailItem("checkin", "Check-In", Icons.Filled.HowToReg),
    RailItem("payment", "Payment", Icons.Filled.Payments),
    RailItem("attendance", "Attendance", Icons.Filled.CalendarMonth),
    RailItem("workout", "Workout", Icons.Filled.FitnessCenter),
    RailItem("body", "Body", Icons.Filled.Accessibility),
    RailItem("diet", "Diet", Icons.Filled.Restaurant),
    RailItem("supplements", "Supplements", Icons.Filled.LocalPharmacy),
    RailItem("services", "Services", Icons.Filled.RoomService),
    RailItem("history", "History", Icons.Filled.History),
    RailItem("insights", "Insights", Icons.Filled.Insights),
    RailItem("messages", "Messages", Icons.Filled.Message),
    RailItem("more", "More", Icons.Filled.MoreHoriz),
)

@Composable
private fun HomeRail(
    selectedId: String = "home",
    onSelect: (String) -> Unit = {}
) {
    Column(
        Modifier
            .width(56.dp)
            .fillMaxHeight()
            .background(MiColors.Surface)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        RAIL_ITEMS.forEach { item ->
            val selected = item.id == selectedId
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) MiColors.Mint else Color.Transparent)
                    .clickable(onClick = { onSelect(item.id) })
                    .padding(vertical = 6.dp)
            ) {
                Box(
                    Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(if (selected) MiColors.Emerald else MiColors.SurfaceSunken),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (selected) Color.White else MiColors.TextSecondary,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    item.label,
                    fontSize = 8.5.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) MiColors.EmeraldDark else MiColors.TextSecondary,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ------------------------------------------------------------------ screen

/**
 * ONE Home implementation. Check-In -> Identity -> Payment -> Attendance ->
 * Workout -> Body. Tapping any widget opens its module sheet with real data.
 */
@Composable
fun MemberIntelligenceHome(
    memberId: String = HomeViewModel.DEFAULT_MEMBER_ID,
    startModule: String? = null,
    vm: HomeViewModel = viewModel(factory = HomeViewModel.Factory())
) {
    val state by vm.state.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val week by vm.workoutWeek.collectAsState()
    val range by vm.workoutRange.collectAsState()
    val openModule by vm.openModule.collectAsState()
    val receipt by vm.lastReceipt.collectAsState()

    LaunchedEffect(memberId) { vm.load(memberId) }

    // Body Progress runs its own aggregation so Home and the analytics screen
    // always agree; the card only reads the finished summary.
    val bodyVm: BodyProgressViewModel = viewModel(factory = BodyProgressViewModel.factory())
    val bodyState by bodyVm.state.collectAsState()
    LaunchedEffect(Unit) { bodyVm.load() }
    val bodySummary = (bodyState as? BodyProgressUiState.Ready)?.summary

    // Body Progress is a full module, so it opens its own analytics screen.
    val openBodyProgress: () -> Unit = {
        context.startActivity(
            android.content.Intent(
                context,
                com.example.badnewgym.feature.bodyprogress.presentation.BodyProgressActivity::class.java
            )
        )
    }
    // No-tap verification bridge (MIUI blocks adb input): broadcasts drive
    // the same ViewModel handlers a finger would reach.
    LaunchedEffect(vm) {
        HomeDebugBridge.onEndSession = { vm.endSession() }
        HomeDebugBridge.onCollect = { amount, method ->
            val entry = PaymentMethod.entries.firstOrNull {
                it.name.equals(method, ignoreCase = true)
            } ?: PaymentMethod.UPI
            vm.collectPayment(amount, entry)
        }
        HomeDebugBridge.onSetRange = { range ->
            WorkoutRange.entries.firstOrNull {
                it.short.equals(range, ignoreCase = true) || it.name.equals(range, ignoreCase = true)
            }?.let { vm.setWorkoutRange(it) }
        }
        HomeDebugBridge.onOpenModule = { module ->
            moduleFrom(module)?.let { vm.openModule(it) }
        }
        HomeDebugBridge.onStatusQuery = {
            when (val s = vm.state.value) {
                is HomeUiState.Ready -> {
                    val d = s.data
                    val live = d.checkIn.liveStartMs != null
                    "live=$live,visits=${d.checkIn.visitNumberToday}," +
                        "outstanding=${d.snapshot.payment?.totalOutstanding}," +
                        "situation=${d.paymentSituation::class.simpleName}"
                }
                HomeUiState.Loading -> "loading"
                is HomeUiState.Error -> "error=${s.message}"
            }
        }
    }
    // No-tap drill-down verification: --es home_open_module payment|...
    LaunchedEffect(startModule, state) {
        if (state is HomeUiState.Ready && startModule != null) {
            moduleFrom(startModule)?.let { vm.openModule(it) }
        }
    }

    Row(
        Modifier
            .fillMaxSize()
            .background(MiColors.Page)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        HomeRail(onSelect = { id ->
            when (id) {
                "checkin" -> vm.openModule(HomeWidgetId.CHECK_IN)
                "payment" -> vm.openModule(HomeWidgetId.PAYMENT)
                "attendance" -> vm.openModule(HomeWidgetId.ATTENDANCE)
                "workout" -> vm.openModule(HomeWidgetId.WORKOUT)
                "body" -> vm.openModule(HomeWidgetId.BODY)
                "members" -> vm.openModule(HomeWidgetId.IDENTITY)
                else -> vm.openModule(HomeWidgetId.IDENTITY)
            }
        })
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 8.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
        ) {
            when (val s = state) {
                HomeUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MiColors.Emerald)
                }
                is HomeUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(s.message, fontSize = 13.sp, color = MiColors.TextSecondary)
                }
                is HomeUiState.Ready -> {
                    val data = s.data
                    // ONE merged card: every widget, critical data only, no
                    // scrolling. Anything deeper lives in the module sheets.
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = CARD_MAX_HEIGHT),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        CheckInWidget(
                            checkIn = data.checkIn,
                            snapshot = data.snapshot,
                            onEndSession = vm::endSession,
                            onOpen = { vm.openModule(HomeWidgetId.CHECK_IN) }
                        )
                        MemberIdentityWidget(
                            snapshot = data.snapshot,
                            onOpen = { vm.openModule(HomeWidgetId.IDENTITY) }
                        )
                        PaymentWidget(
                            payment = data.snapshot.payment,
                            situation = data.paymentSituation,
                            onCollect = { vm.openModule(HomeWidgetId.PAYMENT) },
                            onOpen = { vm.openModule(HomeWidgetId.PAYMENT) },
                            receipt = receipt
                        )
                        AttendanceWidget(
                            window = data.attendance,
                            onOpen = { vm.openModule(HomeWidgetId.ATTENDANCE) }
                        )
                        WorkoutWidget(
                            month = data.workout,
                            weekIndex = week,
                            onWeekChange = vm::setWorkoutWeek,
                            onOpen = { vm.openModule(HomeWidgetId.WORKOUT) },
                            range = range,
                            onRangeChange = vm::setWorkoutRange
                        )
                        BodyProgressWidget(
                            goals = data.bodyGoals,
                            histories = data.bodyHistories,
                            summary = bodySummary,
                            onOpen = { openBodyProgress() }
                        )
                    }
                }
            }
        }
    }

    val ready = (state as? HomeUiState.Ready)?.data
    if (openModule != null && ready != null) {
        HomeModuleSheet(
            module = openModule!!,
            data = ready,
            vm = vm,
            range = range,
            onDismiss = vm::closeModule
        )
    }
}

// ------------------------------------------------------------------ module sheets

private fun moduleFrom(name: String): HomeWidgetId? = when (name.lowercase()) {
    "checkin", "check-in" -> HomeWidgetId.CHECK_IN
    "identity", "members", "profile" -> HomeWidgetId.IDENTITY
    "payment" -> HomeWidgetId.PAYMENT
    "attendance" -> HomeWidgetId.ATTENDANCE
    "workout" -> HomeWidgetId.WORKOUT
    "body" -> HomeWidgetId.BODY
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeModuleSheet(
    module: HomeWidgetId,
    data: HomeData,
    vm: HomeViewModel,
    range: WorkoutRange,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (module) {
                HomeWidgetId.CHECK_IN -> CheckInModule(data)
                HomeWidgetId.IDENTITY -> MemberProfileModule(data)
                HomeWidgetId.PAYMENT -> PaymentModule(data, vm)
                HomeWidgetId.ATTENDANCE -> AttendanceModule(data)
                HomeWidgetId.WORKOUT -> WorkoutModule(data, range, vm::setWorkoutRange)
                HomeWidgetId.BODY -> BodyModule(data)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SheetTitle(title: String, sub: String? = null) {    Column {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary)
        if (sub != null) {
            Text(sub, fontSize = 12.sp, color = MiColors.TextSecondary)
        }
    }
}

@Composable
private fun CheckInModule(data: HomeData) {
    SheetTitle("Check-In", "Live session")
    val c = data.checkIn
    HomeCard {
        KvRow("Status", if (c.liveStartMs != null) "LIVE" else "Checked out")
        KvRow("Started", fmtTime(c.liveStartMs ?: c.lastStartMs))
        KvRow("Method", c.methodLabel ?: "-")
        KvRow("Visit today", c.visitNumberToday?.let { visitOrdinal(it) } ?: "-")
        if (c.liveStartMs == null) {
            KvRow("Last session", c.lastDurationMs?.let { fmtDurationHm(it) } ?: "-")
        }
    }
}

@Composable
private fun MemberProfileModule(data: HomeData) {
    val s = data.snapshot
    SheetTitle("Member Profile", s.identity.name)
    HomeCard {
        KvRow("Member ID", s.identity.code ?: "-")
        KvRow("Age", s.identity.age?.toString() ?: "-")
        KvRow("Gender", s.identity.gender ?: "-")
        KvRow("Member since", fmtDate(s.identity.memberSince))
        KvRow("Plan", s.membership?.planName ?: "-")
        KvRow("Status", if (s.membership?.isActive == true) "Active" else "-")
        KvRow("Valid till", fmtDate(s.membership?.expiryDate))
        KvRow("Days left", s.membership?.daysRemaining?.toString() ?: "-")
        KvRow("Renewals", s.membership?.renewalCount?.toString() ?: "-")
    }
    if (data.snapshot.membershipHistory.isNotEmpty()) {
        HomeCard {
            WidgetTitle("Membership History")
            data.snapshot.membershipHistory.forEach { h ->
                KvRow(h.planName, "${fmtDate(h.startDate)} > ${fmtDate(h.endDate)}")
            }
        }
    }
}

@Composable
private fun PaymentModule(data: HomeData, vm: HomeViewModel) {
    val p = data.snapshot.payment
    SheetTitle("Payment", "Full history")
    if (p == null) {
        HomeCard { EmptyWidgetLine("No payment history") }
        return
    }
    val receipt by vm.lastReceipt.collectAsState()
    val busy by vm.paymentBusy.collectAsState()
    val error by vm.paymentError.collectAsState()

    if (receipt != null && p.totalOutstanding <= 0.009) {
        HomeCard {
            WidgetTitle("Payment Receipt", null)
            KvRow("ID", receipt!!.receiptId)
            KvRow("Received", fmtRupees(receipt!!.amount), MiColors.EmeraldDark)
            KvRow("Method", paymentMethodLabel(receipt!!.method))
            KvRow("Time", "${fmtDate(receipt!!.paidAtEpoch)} \u00B7 ${fmtTime(receipt!!.paidAtEpoch)}")
            KvRow("Remaining", fmtRupees(p.totalOutstanding))
        }
    }

    if (p.totalOutstanding > 0.009) {
        CollectSection(
            outstanding = p.totalOutstanding,
            busy = busy,
            error = error,
            onCollect = { amount, method -> vm.collectPayment(amount, method) }
        )
    }

    HomeCard {
        KvRow("Outstanding", fmtRupees(p.totalOutstanding))
        KvRow("Due date", fmtDate(p.dueDate))
        KvRow("Overdue", if (p.overdueDays > 0) "${p.overdueDays} days" else "-")
        KvRow("Lifetime paid", fmtRupees(p.lifetimePaid))
    }
    if (p.history.isNotEmpty()) {
        HomeCard {
            WidgetTitle("Transactions")
            p.history.forEach { t ->
                KvRow("${fmtDate(t.occurredAt)} \u00B7 ${t.method}", fmtRupees(t.amount))
            }
        }
    }
    if (p.breakdown.isNotEmpty()) {
        HomeCard {
            WidgetTitle("Breakdown")
            p.breakdown.forEach { b ->
                KvRow(b.label, fmtRupees(b.amount))
            }
        }
    }
}

@Composable
private fun CollectSection(
    outstanding: Double,
    busy: Boolean,
    error: String?,
    onCollect: (Double, PaymentMethod) -> Unit
) {
    var method by remember { mutableStateOf(PaymentMethod.UPI) }
    var amountText by remember(outstanding) {
        mutableStateOf(
            if (outstanding % 1.0 == 0.0) outstanding.toInt().toString() else outstanding.toString()
        )
    }
    HomeCard {
        WidgetTitle("Collect Payment", null)
        Spacer(Modifier.height(6.dp))
        Text(
            "Outstanding ${fmtRupees(outstanding)}",
            fontSize = 13.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary
        )
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            PaymentMethod.entries.forEach { entry ->
                val selected = entry == method
                Box(
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (selected) MiColors.Emerald else MiColors.SurfaceSunken)
                        .clickable(onClick = { method = entry })
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        paymentMethodLabel(entry),
                        fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = if (selected) Color.White else MiColors.TextSecondary,
                        maxLines = 1
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        androidx.compose.material3.OutlinedTextField(
            value = amountText,
            onValueChange = { text ->
                if (text.all { it.isDigit() || it == '.' }) amountText = text
            },
            label = { Text("Amount (\u20B9)") },
            singleLine = true,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Spacer(Modifier.height(4.dp))
            Text(error, fontSize = 12.sp, color = MiColors.Red, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(6.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(if (busy) MiColors.TextTertiary else Color(0xFFFF6B4A))
                .clickable(enabled = !busy, onClick = {
                    onCollect(amountText.toDoubleOrNull() ?: 0.0, method)
                })
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (busy) "Recording\u2026" else "Record Payment",
                fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White
            )
        }
    }
}

@Composable
private fun AttendanceModule(data: HomeData) {
    SheetTitle("Attendance", "Full 30-day view")
    AttendanceDetailContent(window = data.attendance)
}

@Composable
private fun WorkoutModule(data: HomeData, range: WorkoutRange, onRangeChange: (WorkoutRange) -> Unit) {
    SheetTitle("Workout", "Sessions and volume")
    val month = data.workout
    if (month == null || month.sessions.isEmpty()) {
        HomeCard { EmptyWidgetLine("No workout data") }
        return
    }
    HomeCard {
        val newest = remember(month) { month.sorted().lastOrNull()?.epochDay ?: 0L }
        val from = newest - range.days + 1
        val sessions = remember(month, range) {
            month.sessions.filter { it.epochDay in from..newest }.sortedByDescending { it.epochDay }
        }
        WidgetTitle("Sessions", "${sessions.size} in range")
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            WorkoutRange.entries.forEach { entry ->
                val selected = entry == range
                Box(
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (selected) MiColors.Blue else MiColors.BlueSoft)
                        .clickable(onClick = { onRangeChange(entry) })
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "${entry.days} Days", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = if (selected) Color.White else MiColors.Blue, maxLines = 1
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        if (sessions.isEmpty()) {
            EmptyWidgetLine("No sessions in the last ${range.days} days")
        } else {
            sessions.forEach { s ->
                KvRow(fmtDay(epochDayToMs(s.epochDay)), "${s.minutes} min")
            }
        }
    }
}

@Composable
private fun BodyModule(data: HomeData) {
    SheetTitle("Body Progress", "Goals and measurements")
    BodyDetailContent(goals = data.bodyGoals, histories = data.bodyHistories)
}

private fun epochDayToMs(epochDay: Long): Long = epochDay * 86_400_000L
