package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyIntelligenceSheetHost
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.rememberBodyIntelligenceState
import com.example.badnewgym.feature.membercard.data.MemberBodyIntelligenceMapper
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

/**
 * V3 Member Intelligence shell: Clean header + 5 KPI tabs on the pack canvas gradient.
 * (Old theme strip and extra body sub-bars removed for a clean, pixel-perfect viewport).
 */
@Composable
fun MemberIntelligenceDashboard(
    data: MemberCardData,
    pass: BadGymMemberPass,
    onMenuClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onAttendanceDayClick: (CalendarDay) -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    onViewReceiptClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    val bodyInput = remember(data, pass) {
        val base = MemberBodyIntelligenceMapper.from(data)
        base.copy(
            memberName = pass.name,
            memberId = pass.memberId,
            profile = base.profile?.copy(
                name = pass.name,
                memberId = pass.memberId,
                age = pass.age.toString(),
                gender = pass.gender,
                status = if (pass.isOverdue) "Overdue" else "Active",
                goalLabel = pass.goal.takeIf { it.isNotBlank() } ?: base.profile?.goalLabel
            )
        )
    }
    val bodyState = rememberBodyIntelligenceState(bodyInput)

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val monthShort = data.attendance.monthLabel.take(3)
    fun dayInMonth(raw: String): Int? {
        val match = Regex("(\\d{1,2})\\s+([A-Za-z]{3})").find(raw) ?: return null
        val month = match.groupValues[2]
        return if (month.equals(monthShort, ignoreCase = true)) {
            match.groupValues[1].toIntOrNull()
        } else {
            null
        }
    }
    val dueDay = remember(data) { dayInMonth(data.paymentPlan.currentDueDate) }
    val paidDay = remember(data) { dayInMonth(data.paymentPlan.lastPaymentDate) }

    var headerCompact by remember { mutableStateOf(false) }
    val foldOnScroll = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -3f) headerCompact = true
                else if (available.y > 3f) headerCompact = false
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(selectedTab) {
        lazyListState.scrollToItem(0)
        headerCompact = false
    }
    val shellColors = intelligenceColors()
    val baseDensity = LocalDensity.current
    val context = LocalContext.current
    LaunchedEffect(Unit) { NeuStyle.attach(context) }
    // One knob for text (fontScale) and one for icons/buttons/boxes (density),
    // so the whole dashboard rescales proportionally instead of per-widget.
    val scaledDensity = remember(baseDensity, NeuStyle.textScale, NeuStyle.componentScale) {
        Density(
            density = baseDensity.density * NeuStyle.componentScale,
            fontScale = baseDensity.fontScale * NeuStyle.textScale
        )
    }
    key(NeuStyle.themeId) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(shellColors.screenBrush)
    ) {
        CompositionLocalProvider(
            LocalDensity provides scaledDensity,
            LocalTextStyle provides TextStyle(fontFamily = BadGymSans, fontWeight = FontWeight.Bold)
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            IntelligenceHeader(
                data = data,
                pass = pass,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onMenuClick = onMenuClick,
                onAvatarClick = onAvatarClick,
                onDueClick = onPaymentClick,
                compact = headerCompact
            )

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .nestedScroll(foldOnScroll),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                when (selectedTab) {
                    0 -> item(key = "tab_attendance") {
                        AttendanceIntelligenceScreen(
                            data = data,
                            paidDay = paidDay,
                            dueDay = dueDay,
                            onDayClick = onAttendanceDayClick
                        )
                    }
                    1 -> item(key = "tab_workout") {
                        WorkoutIntelligenceScreen(data = data, onClick = onWorkoutClick)
                    }
                    2 -> item(key = "tab_payment") {
                        PaymentIntelligenceScreen(
                            data = data,
                            onHistoryClick = onPaymentClick,
                            onViewDueClick = onPaymentClick,
                            onReceiptClick = onViewReceiptClick,
                            onRemindClick = {}
                        )
                    }
                    3 -> item(key = "tab_body") {
                        BodyIntelligenceLiteScreen(data = data, state = bodyState)
                    }
                    else -> item(key = "tab_overview") {
                        OverviewIntelligenceScreen(data = data, pass = pass)
                    }
                }
            }

            BodyIntelligenceSheetHost(state = bodyState)
        }
        }
    }
    }
}
