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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.badnewgym.feature.memberintelligence.design.tokens.ChromeGradient
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import com.example.badnewgym.feature.memberintelligence.design.tokens.RailActiveGradient
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentMethod

// ------------------------------------------------------------------ rail

private data class RailItem(val id: String, val label: String, val icon: ImageVector)

/** Rail order follows the reference: Home through More, no extras. */
private val RAIL_ITEMS = listOf(
    RailItem("home", "Home", Icons.Filled.Home),
    RailItem("members", "Members", Icons.Filled.People),
    RailItem("checkin", "Check-In", Icons.Filled.HowToReg),
    RailItem("payment", "Payment", Icons.Filled.Payments),
    RailItem("attendance", "Attendance", Icons.Filled.CalendarMonth),
    RailItem("workout", "Workout", Icons.Filled.FitnessCenter),
    RailItem("body", "Body", Icons.Filled.Accessibility),
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
    badges: Map<String, String> = emptyMap(),
    onSelect: (String) -> Unit = {}
) {
    Column(
        Modifier
            .width(62.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        RAIL_ITEMS.forEach { item ->
            val selected = item.id == selectedId
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(58.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .then(if (selected) Modifier.background(RailActiveGradient) else Modifier)
                    .clickable(onClick = { onSelect(item.id) })
                    .padding(vertical = 6.dp)
            ) {
                androidx.compose.material3.Icon(
                    item.icon,
                    contentDescription = item.label,
                    tint = if (selected) Color.White else MiColors.ChromeTextDim,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    item.label,
                    fontSize = 9.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) Color.White else MiColors.ChromeTextDim,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                badges[item.id]?.let { value ->
                    Text(
                        value,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = if (selected) Color.White else MiColors.ChromeGlow,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Deep-emerald header: wordmark, real member search, and the shell's global
 * actions. Every control here opens something real - there is no inert icon.
 */
@Composable
private fun AppHeader(
    memberName: String?,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenAlerts: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(RailActiveGradient),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Icon(
                Icons.Filled.FitnessCenter,
                contentDescription = "BAD GYM",
                tint = Color.White,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "BAD GYM",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                maxLines = 1
            )
            Text(
                "MEMBER INTELLIGENCE",
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = MiColors.ChromeTextDim,
                maxLines = 1
            )
        }
        // Search doubles as the current-member affordance, so the header always
        // names who is on screen.
        Row(
            Modifier
                .height(34.dp)
                .widthIn(max = 132.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0x22FFFFFF))
                .clickable(onClick = onOpenSearch)
                .padding(horizontal = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Icon(
                Icons.Filled.Search,
                contentDescription = "Search members",
                tint = MiColors.ChromeTextDim,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(5.dp))
            Text(
                memberName ?: "Search",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (memberName != null) Color.White else MiColors.ChromeTextDim,
                maxLines = 1
            )
        }
        Spacer(Modifier.width(4.dp))
        HeaderIcon(Icons.Filled.Settings, "Settings", onOpenSettings)
        HeaderIcon(Icons.Filled.Notifications, "Alerts", onOpenAlerts)
    }
}

@Composable
private fun HeaderIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    Box(
        Modifier
            .padding(start = 4.dp)
            .size(34.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(Color(0x22FFFFFF))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Icon(
            icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(17.dp)
        )
    }
}

/**
 * Real member search over the local directory. Selecting a row switches the
 * dashboard to that member; an unmatched query says so rather than quietly
 * showing everyone.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemberPickerSheet(onPick: (String) -> Unit, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var query by rememberSaveable { mutableStateOf("") }
    val results = remember(query) { searchMembers(query) }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MiColors.Surface
    ) {
        Column(Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp)) {
            SheetTitle("Search members", "Type a name, member code or plan")
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Aman or BG305", fontSize = 13.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MiColors.Surface,
                    unfocusedContainerColor = MiColors.Surface
                )
            )
            Spacer(Modifier.height(10.dp))
            if (results.isEmpty()) {
                Text(
                    "No member matches \"${query.trim()}\".",
                    fontSize = 12.sp,
                    color = MiColors.TextSecondary
                )
            } else {
                results.forEach { m ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onPick(m.id) }
                            .padding(vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(MiColors.Mint),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                m.name.take(1),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = MiColors.EmeraldDark
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                m.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MiColors.TextPrimary,
                                maxLines = 1
                            )
                            Text(
                                listOfNotNull(m.code, m.plan).joinToString(" \u00B7 "),
                                fontSize = 10.sp,
                                color = MiColors.TextSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }
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
    val notice by vm.notice.collectAsState()
    val selectedMonth by vm.attendanceMonth.collectAsState()
    val workoutMonth by vm.workoutMonth.collectAsState()

    // Member identity is shell state: the header search switches it and the
    // dashboard reloads against an id the repository can actually resolve.
    var currentMemberId by rememberSaveable { mutableStateOf(memberId) }
    var searchOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(currentMemberId) { vm.load(currentMemberId) }

    // Body Progress runs its own aggregation so Home and the analytics screen
    // always agree; the card only reads the finished summary.
    val bodyVm: BodyProgressViewModel = viewModel(factory = BodyProgressViewModel.factory())
    val bodyState by bodyVm.state.collectAsState()
    LaunchedEffect(Unit) { bodyVm.load() }
    val bodySummary = (bodyState as? BodyProgressUiState.Ready)?.summary

    // Rail badges: one live number per item, each from a real record. Items
    // with nothing meaningful to show get no badge rather than a filler.
    val readyData = (state as? HomeUiState.Ready)?.data
    val badges = remember(readyData, range, workoutMonth, bodySummary) {
        buildRailBadges(readyData, range, workoutMonth, bodySummary)
    }

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

    // Reference shell: a deep-emerald frame (header + rail) wrapping a light
    // content canvas. System bars stay outside it.
    Box(
        Modifier
            .fillMaxSize()
            .background(ChromeGradient)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(Modifier.fillMaxSize()) {
            AppHeader(
                memberName = readyData?.snapshot?.identity?.name,
                onOpenSearch = { searchOpen = true },
                onOpenSettings = { vm.openModule(HomeWidgetId.MORE) },
                onOpenAlerts = { vm.openModule(HomeWidgetId.INSIGHTS) }
            )
            Row(Modifier.fillMaxSize()) {
                HomeRail(badges = badges, onSelect = { id ->
                when (id) {
                    "home" -> vm.closeModule()
                    "members" -> vm.openModule(HomeWidgetId.IDENTITY)
                    "checkin" -> vm.openModule(HomeWidgetId.CHECK_IN)
                    "payment" -> vm.openModule(HomeWidgetId.PAYMENT)
                    "attendance" -> vm.openModule(HomeWidgetId.ATTENDANCE)
                    "workout" -> vm.openModule(HomeWidgetId.WORKOUT)
                    "body" -> openBodyProgress()
                    "supplements" -> vm.openModule(HomeWidgetId.SUPPLEMENTS)
                    "services" -> vm.openModule(HomeWidgetId.SERVICES)
                    "history" -> vm.openModule(HomeWidgetId.HISTORY)
                    "insights" -> vm.openModule(HomeWidgetId.INSIGHTS)
                    "messages" -> vm.showNotice("Messaging is not available in this build.")
                    "more" -> vm.openModule(HomeWidgetId.MORE)
                    else -> vm.closeModule()
                }
            })
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                    .background(MiColors.Page)
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
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 6.dp, vertical = 5.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            CheckInWidget(
                                checkIn = data.checkIn,
                                snapshot = data.snapshot,
                                onEndSession = vm::endSession,
                                onOpen = { vm.openModule(HomeWidgetId.CHECK_IN) }
                            )
                            MemberIdentityWidget(
                                snapshot = data.snapshot,
                                onOpen = { vm.openModule(HomeWidgetId.IDENTITY) },
                                checkIn = data.checkIn
                            )
                            PaymentWidget(
                                payment = data.snapshot.payment,
                                situation = data.paymentSituation,
                                planName = data.snapshot.membership?.planName,
                                planPeriod = data.snapshot.membership?.planType,
                                validTill = data.snapshot.membership?.expiryDate?.let { fmtDate(it) },
                                onCollect = { vm.openModule(HomeWidgetId.PAYMENT) },
                                onOpen = { vm.openModule(HomeWidgetId.PAYMENT) },
                                onPlan = { vm.openModule(HomeWidgetId.IDENTITY) },
                                onHistory = { vm.openModule(HomeWidgetId.PAYMENT) },
                                onUnavailable = vm::showNotice,
                                receipt = receipt
                            )
                            AttendanceWidget(
                                month = data.attendanceMonth,
                                prevPoints = data.attendancePrevPoints,
                                selected = selectedMonth,
                                onMonthChange = vm::setAttendanceMonth,
                                onOpen = { vm.openModule(HomeWidgetId.ATTENDANCE) }
                            )
                            WorkoutWidget(
                                month = data.workout,
                                weekIndex = week,
                                onWeekChange = vm::setWorkoutWeek,
                                onOpen = { vm.openModule(HomeWidgetId.WORKOUT) },
                                range = range,
                                onRangeChange = vm::setWorkoutRange,
                                workoutYearMonth = workoutMonth,
                                onWorkoutMonthChange = vm::setWorkoutMonth
                            )
                            BodyProgressWidget(
                                goals = data.bodyGoals,
                                histories = data.bodyHistories,
                                summary = bodySummary,
                                singleRow = true,
                                onOpen = { openBodyProgress() }
                            )
                        }
                    }
                }
            }
        }
    }
    }

    if (notice != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = vm::clearNotice,
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = vm::clearNotice) {
                    Text("OK", fontWeight = FontWeight.Bold, color = MiColors.EmeraldDark)
                }
            },
            title = { Text("Not available", fontSize = 16.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary) },
            text = { Text(notice!!, fontSize = 13.sp, color = MiColors.TextSecondary) }
        )
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

    if (searchOpen) {
        MemberPickerSheet(
            onPick = { id ->
                searchOpen = false
                currentMemberId = id
            },
            onDismiss = { searchOpen = false }
        )
    }
}

// ------------------------------------------------------------------ module sheets

// ------------------------------------------------------------------ rail badges

/**
 * One live number per rail item, each derived from a real record.
 * Items with nothing meaningful to show are absent from the map rather than
 * carrying a filler value.
 */
private fun buildRailBadges(
    data: HomeData?,
    range: WorkoutRange,
    workoutYearMonth: java.time.YearMonth,
    body: com.example.badnewgym.feature.bodyprogress.domain.BodyProgressSummary?
): Map<String, String> {
    if (data == null) return emptyMap()
    val out = mutableMapOf<String, String>()
    data.snapshot.membership?.daysRemaining?.let { out["members"] = "${it}d" }
    val checkIn = data.checkIn
    if (checkIn.liveStartMs != null) {
        out["checkin"] = "LIVE"
    } else {
        checkIn.visitNumberToday?.let { out["checkin"] = "#$it" }
    }
    data.snapshot.payment?.let { p ->
        if (p.totalOutstanding > 0.009) out["payment"] = fmtRupees(p.totalOutstanding)
    }
    data.attendanceMonth?.let { m ->
        if (m.openDays.isNotEmpty()) out["attendance"] = "${m.percent}%"
    }
    data.workout?.let { month ->
        if (range.isMonth) {
            val active = com.example.badnewgym.feature.memberintelligence.domain.analytics.WorkoutAnalytics
                .monthDays(month, workoutYearMonth).count { it.minutes > 0 }
            if (active > 0) out["workout"] = "$active"
        } else {
            val sorted = month.sorted()
            if (sorted.isNotEmpty()) {
                val newest = sorted.last().epochDay
                val scoped = sorted.filter { it.epochDay in (newest - range.days + 1)..newest }
                val active = scoped.count { it.minutes > 0 }
                if (active > 0) out["workout"] = "$active"
            }
        }
    }
    body?.goalPercent?.let { out["body"] = "$it%" }
    data.snapshot.supplements?.let { s ->
        if (s.hasHistory) out["supplements"] = "1"
    }
    data.snapshot.services?.let { list ->
        val active = list.count { it.isActive }
        if (active > 0) out["services"] = "$active"
    }
    data.snapshot.payment?.history?.let { txns ->
        if (txns.isNotEmpty()) out["history"] = "${txns.size}"
    }
    data.attendance?.let { window ->
        if (window.elapsed.isNotEmpty()) {
            val streak = com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceAnalytics.currentStreak(window)
            if (streak > 0) out["insights"] = "${streak}d"
        }
    }
    return out
}

private fun moduleFrom(name: String): HomeWidgetId? = when (name.lowercase()) {
    "checkin", "check-in" -> HomeWidgetId.CHECK_IN
    "identity", "members", "profile" -> HomeWidgetId.IDENTITY
    "payment" -> HomeWidgetId.PAYMENT
    "attendance" -> HomeWidgetId.ATTENDANCE
    "workout" -> HomeWidgetId.WORKOUT
    "body" -> HomeWidgetId.BODY
    "supplements" -> HomeWidgetId.SUPPLEMENTS
    "services" -> HomeWidgetId.SERVICES
    "history" -> HomeWidgetId.HISTORY
    "insights" -> HomeWidgetId.INSIGHTS
    "more" -> HomeWidgetId.MORE
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
    val selectedMonth by vm.attendanceMonth.collectAsState()
    val workoutMonthSheet by vm.workoutMonth.collectAsState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MiColors.Surface
    ) {
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
                HomeWidgetId.ATTENDANCE -> AttendanceModule(data, selectedMonth, vm::setAttendanceMonth)
                HomeWidgetId.WORKOUT -> WorkoutModule(data, range, vm::setWorkoutRange, workoutMonthSheet, vm::setWorkoutMonth)
                HomeWidgetId.BODY -> BodyModule(data)
                HomeWidgetId.SUPPLEMENTS -> SupplementsModule(data)
                HomeWidgetId.SERVICES -> ServicesModule(data)
                HomeWidgetId.HISTORY -> HistoryModule(data)
                HomeWidgetId.INSIGHTS -> InsightsModule(data, range, workoutMonthSheet)
                HomeWidgetId.MORE -> MoreModule(data)
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
private fun AttendanceModule(
    data: HomeData,
    selected: java.time.YearMonth,
    onMonthChange: (java.time.YearMonth) -> Unit
) {
    SheetTitle("Attendance", "Full month view")
    AttendanceDetailContent(
        month = data.attendanceMonth,
        prevPoints = data.attendancePrevPoints,
        selected = selected,
        onMonthChange = onMonthChange
    )
}

@Composable
private fun WorkoutModule(
    data: HomeData,
    range: WorkoutRange,
    onRangeChange: (WorkoutRange) -> Unit,
    workoutYearMonth: java.time.YearMonth,
    onWorkoutMonthChange: (java.time.YearMonth) -> Unit
) {
    SheetTitle("Workout", "Sessions and volume")
    val month = data.workout
    if (month == null || month.sessions.isEmpty()) {
        HomeCard { EmptyWidgetLine("No workout data") }
        return
    }
    HomeCard {
        val sessions = remember(month, range, workoutYearMonth) {
            if (range.isMonth) {
                val start = workoutYearMonth.atDay(1).toEpochDay()
                val end = workoutYearMonth.atEndOfMonth().toEpochDay()
                month.sessions.filter { it.epochDay in start..end }.sortedByDescending { it.epochDay }
            } else {
                val newest = month.sorted().lastOrNull()?.epochDay ?: 0L
                val from = newest - range.days + 1
                month.sessions.filter { it.epochDay in from..newest }.sortedByDescending { it.epochDay }
            }
        }
        val scopeLabel = if (range.isMonth) {
            workoutYearMonth.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() } +
                " " + workoutYearMonth.year
        } else {
            "last ${range.days} days"
        }
        WidgetTitle("Sessions", "${sessions.size} in $scopeLabel")
        Spacer(Modifier.height(2.dp))
        Text(
            "Chart colors record session type: blue strength, green cardio, gray no session.",
            fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 2
        )
        Spacer(Modifier.height(4.dp))
        if (range.isMonth) {
            MonthNavRowMini(workoutYearMonth, onWorkoutMonthChange)
            Spacer(Modifier.height(4.dp))
        }
        Spacer(Modifier.height(2.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(WorkoutRange.THIS_MONTH, WorkoutRange.D7, WorkoutRange.D15, WorkoutRange.D30).forEach { entry ->
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
                        entry.short, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = if (selected) Color.White else MiColors.Blue, maxLines = 1
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        if (sessions.isEmpty()) {
            EmptyWidgetLine("No sessions in $scopeLabel")
        } else {
            sessions.forEach { s ->
                KvRow(fmtDay(epochDayToMs(s.epochDay)), "${s.minutes} min")
            }
        }
    }
}

@Composable
private fun MonthNavRowMini(selected: java.time.YearMonth, onChange: (java.time.YearMonth) -> Unit) {
    val now = remember { java.time.YearMonth.now() }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
        Text(
            selected.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() } + " " + selected.year,
            fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MiColors.TextPrimary, maxLines = 1
        )
        Spacer(Modifier.width(8.dp))
        MonthArrowMiniSheet("<", enabled = true) { onChange(selected.minusMonths(1)) }
        Spacer(Modifier.width(6.dp))
        MonthArrowMiniSheet(">", enabled = selected < now) {
            if (selected < now) onChange(selected.plusMonths(1))
        }
    }
}

@Composable
private fun MonthArrowMiniSheet(label: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (enabled) MiColors.BlueSoft else MiColors.SurfaceSunken)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Black, color = if (enabled) MiColors.Blue else MiColors.TextTertiary)
    }
}

@Composable
private fun BodyModule(data: HomeData) {
    SheetTitle("Body Progress", "Goals and measurements")
    BodyDetailContent(goals = data.bodyGoals, histories = data.bodyHistories)
}

@Composable
private fun SupplementsModule(data: HomeData) {
    val s = data.snapshot.supplements
    SheetTitle("Supplements", "Purchase history")
    if (s == null || !s.hasHistory) {
        HomeCard { EmptyWidgetLine("No supplement purchases on record") }
        return
    }
    HomeCard {
        KvRow("Last purchase", s.lastPurchaseName ?: "-")
        KvRow("Date", fmtDate(s.lastPurchaseDate))
        KvRow("Price", s.lastPurchasePrice?.let { fmtRupees(it) } ?: "-")
        KvRow("Brand", s.brand ?: "-")
    }
}

@Composable
private fun ServicesModule(data: HomeData) {
    val services = data.snapshot.services ?: emptyList()
    SheetTitle("Services", "Active and past services")
    if (services.isEmpty()) {
        HomeCard { EmptyWidgetLine("No services on record") }
        return
    }
    HomeCard {
        WidgetTitle("Services")
        Spacer(Modifier.height(4.dp))
        services.forEach { svc ->
            KvRow(
                svc.serviceName,
                (if (svc.isActive) "Active" else "Ended") +
                    (svc.expiryDate?.let { " \u00B7 ${fmtDate(it)}" } ?: "")
            )
        }
    }
}

@Composable
private fun HistoryModule(data: HomeData) {
    SheetTitle("History", "Payments, membership and visits")
    val txns = data.snapshot.payment?.history.orEmpty()
    if (txns.isNotEmpty()) {
        HomeCard {
            WidgetTitle("Transactions")
            Spacer(Modifier.height(4.dp))
            txns.sortedByDescending { it.occurredAt }.take(10).forEach { t ->
                KvRow("${fmtDate(t.occurredAt)} \u00B7 ${t.method}", fmtRupees(t.amount))
            }
        }
    }
    if (data.snapshot.membershipHistory.isNotEmpty()) {
        HomeCard {
            WidgetTitle("Membership History")
            Spacer(Modifier.height(4.dp))
            data.snapshot.membershipHistory.forEach { h ->
                KvRow(h.planName, "${fmtDate(h.startDate)} > ${fmtDate(h.endDate)}")
            }
        }
    }
    val visits = data.snapshot.recentEvents
        .filter {
            it.eventType == com.example.badnewgym.feature.memberintelligence.domain.model.EventType.CHECK_IN ||
                it.eventType == com.example.badnewgym.feature.memberintelligence.domain.model.EventType.CHECK_OUT
        }
        .sortedByDescending { it.occurredAt }
        .take(10)
    if (visits.isNotEmpty()) {
        HomeCard {
            WidgetTitle("Recent Visits")
            Spacer(Modifier.height(4.dp))
            visits.forEach { e ->
                KvRow(
                    if (e.eventType == com.example.badnewgym.feature.memberintelligence.domain.model.EventType.CHECK_IN) "Check-in" else "Check-out",
                    "${fmtDate(e.occurredAt)} \u00B7 ${fmtTime(e.occurredAt)}"
                )
            }
        }
    }
    if (txns.isEmpty() && visits.isEmpty() && data.snapshot.membershipHistory.isEmpty()) {
        HomeCard { EmptyWidgetLine("No history on record") }
    }
}

@Composable
private fun InsightsModule(data: HomeData, range: WorkoutRange, workoutYearMonth: java.time.YearMonth) {
    SheetTitle("Insights", "Derived from recorded data")
    val window = data.attendance
    if (window != null && window.elapsed.isNotEmpty()) {
        HomeCard {
            WidgetTitle("Attendance")
            Spacer(Modifier.height(4.dp))
            KvRow("Current streak", "${com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceAnalytics.currentStreak(window)} days")
            KvRow("Longest streak", "${com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceAnalytics.longestStreak(window)} days")
            KvRow("Completion", "${(com.example.badnewgym.feature.memberintelligence.domain.analytics.AttendanceAnalytics.completion(window) * 100).toInt()}% of open days")
        }
    }
    val sessions = data.workout?.sessions.orEmpty()
    if (sessions.isNotEmpty()) {
        val scoped: List<com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutSession>
        val scopeLabel: String
        if (range.isMonth) {
            val start = workoutYearMonth.atDay(1).toEpochDay()
            val end = workoutYearMonth.atEndOfMonth().toEpochDay()
            scoped = sessions.filter { it.epochDay in start..end }
            scopeLabel = workoutYearMonth.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() } +
                " " + workoutYearMonth.year
        } else {
            val newest = sessions.maxOf { it.epochDay }
            scoped = sessions.filter { it.epochDay in (newest - range.days + 1)..newest }
            scopeLabel = "last ${range.days} days"
        }
        val summary = com.example.badnewgym.feature.memberintelligence.domain.analytics.WorkoutAnalytics.summarize(scoped)
        HomeCard {
            WidgetTitle("Workout", scopeLabel)
            Spacer(Modifier.height(4.dp))
            KvRow("Sessions", summary.activeDays.toString())
            KvRow("Total", "${summary.totalMinutes} min")
            KvRow("Trend", summary.trend.name.lowercase().replaceFirstChar { it.uppercase() })
        }
    }
    if (data.bodyGoals.isNotEmpty()) {
        val verdicts = data.bodyGoals.groupingBy {
            com.example.badnewgym.feature.memberintelligence.domain.analytics.BodyGoalAnalytics.verdict(it).name
        }.eachCount()
        HomeCard {
            WidgetTitle("Body Goals")
            Spacer(Modifier.height(4.dp))
            verdicts.forEach { (name, count) ->
                KvRow(
                    name.lowercase().replaceFirstChar { it.uppercase() }.replace('_', ' '),
                    "$count goal${if (count == 1) "" else "s"}"
                )
            }
        }
    }
}

@Composable
private fun MoreModule(data: HomeData) {
    SheetTitle("More", "Account and app")
    HomeCard {
        KvRow("Member", data.snapshot.identity.code ?: "-")
        KvRow("Gym", data.snapshot.gymId)
        KvRow("Plan access", data.snapshot.membership?.planName ?: "-")
    }
    HomeCard {
        Text(
            "Messaging is not available in this build.",
            fontSize = 12.sp, color = MiColors.TextSecondary
        )
    }
}

private fun epochDayToMs(epochDay: Long): Long = epochDay * 86_400_000L
