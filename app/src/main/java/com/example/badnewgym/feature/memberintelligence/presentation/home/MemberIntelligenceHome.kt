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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors

// ------------------------------------------------------------------ rail

private data class RailItem(val id: String, val label: String, val icon: ImageVector)

/** Launch extra: open Home pre-scrolled to the bottom for screenshot checks. */
const val EXTRA_SCROLL_BOTTOM = "home_scroll_bottom"

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
    val week by vm.workoutWeek.collectAsState()
    val openModule by vm.openModule.collectAsState()

    LaunchedEffect(memberId) { vm.load(memberId) }
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
                .padding(start = 8.dp, end = 12.dp, top = 10.dp, bottom = 10.dp)
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
                    val ids = visibleOrdered(data.widgetDefs)
                    // Visual-verification hook: `am start --ez home_scroll_bottom true`
                    // opens Home pre-scrolled so the lower widgets can be
                    // screenshot-verified without a swipe gesture.
                    val activity = androidx.compose.ui.platform.LocalContext.current as? android.app.Activity
                    val startAtBottom =
                        activity?.intent?.getBooleanExtra(EXTRA_SCROLL_BOTTOM, false) == true
                    val scroll = rememberScrollState()
                    LaunchedEffect(s.data.snapshot.id, startAtBottom) {
                        if (startAtBottom) {
                            // First frames have maxValue 0 (nothing laid out yet).
                            kotlinx.coroutines.delay(500)
                            scroll.scrollTo(scroll.maxValue)
                        }
                    }
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(scroll),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ids.forEach { id ->
                            when (id) {
                                HomeWidgetId.CHECK_IN -> CheckInWidget(
                                    checkIn = data.checkIn,
                                    snapshot = data.snapshot,
                                    onEndSession = vm::endSession,
                                    onOpen = { vm.openModule(HomeWidgetId.CHECK_IN) }
                                )
                                HomeWidgetId.IDENTITY -> MemberIdentityWidget(
                                    snapshot = data.snapshot,
                                    onOpen = { vm.openModule(HomeWidgetId.IDENTITY) }
                                )
                                HomeWidgetId.PAYMENT -> PaymentWidget(
                                    payment = data.snapshot.payment,
                                    situation = data.paymentSituation,
                                    onCollect = { vm.openModule(HomeWidgetId.PAYMENT) },
                                    onOpen = { vm.openModule(HomeWidgetId.PAYMENT) }
                                )
                                HomeWidgetId.ATTENDANCE -> AttendanceWidget(
                                    window = data.attendance,
                                    onOpen = { vm.openModule(HomeWidgetId.ATTENDANCE) }
                                )
                                HomeWidgetId.WORKOUT -> WorkoutWidget(
                                    month = data.workout,
                                    weekIndex = week,
                                    onWeekChange = vm::setWorkoutWeek,
                                    onOpen = { vm.openModule(HomeWidgetId.WORKOUT) }
                                )
                                HomeWidgetId.BODY -> BodyProgressWidget(
                                    goals = data.bodyGoals,
                                    histories = data.bodyHistories,
                                    onOpen = { vm.openModule(HomeWidgetId.BODY) }
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
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
                HomeWidgetId.PAYMENT -> PaymentModule(data)
                HomeWidgetId.ATTENDANCE -> AttendanceModule(data)
                HomeWidgetId.WORKOUT -> WorkoutModule(data)
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
private fun PaymentModule(data: HomeData) {
    val p = data.snapshot.payment
    SheetTitle("Payment", "Full history")
    if (p == null) {
        HomeCard { EmptyWidgetLine("No payment history") }
        return
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
private fun AttendanceModule(data: HomeData) {
    SheetTitle("Attendance", "Full 30-day view")
    AttendanceWidget(window = data.attendance, onOpen = {})
}

@Composable
private fun WorkoutModule(data: HomeData) {
    SheetTitle("Workout", "Sessions and volume")
    val month = data.workout
    if (month == null || month.sessions.isEmpty()) {
        HomeCard { EmptyWidgetLine("No workout data") }
        return
    }
    HomeCard {
        val sessions = month.sorted()
        WidgetTitle("All Sessions (${sessions.size})")
        sessions.takeLast(15).reversed().forEach { s ->
            KvRow(fmtDay(epochDayToMs(s.epochDay)), "${s.minutes} min")
        }
    }
}

@Composable
private fun BodyModule(data: HomeData) {
    SheetTitle("Body Progress", "Goals and measurements")
    BodyProgressWidget(goals = data.bodyGoals, histories = data.bodyHistories, onOpen = {})
}

private fun epochDayToMs(epochDay: Long): Long = epochDay * 86_400_000L
