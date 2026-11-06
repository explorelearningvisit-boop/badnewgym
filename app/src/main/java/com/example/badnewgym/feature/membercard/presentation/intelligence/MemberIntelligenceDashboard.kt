package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyIntelligenceSheetHost
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.bodyIntelligenceItems
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.rememberBodyIntelligenceState
import com.example.badnewgym.feature.membercard.data.MemberBodyIntelligenceMapper
import com.example.badnewgym.feature.membercard.design.BadGymThemeManager
import com.example.badnewgym.feature.membercard.design.BadGymThemeSelectorStrip
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

/** V3 Member Intelligence shell: theme strip + IntelligenceHeader + the four tab bodies on the pack canvas gradient. */
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

    // Body Intelligence reads the signed-in member's own records; the pass is the
    // identity source, the member card supplies plan, goal and dues. Built once
    // per data/pass change exactly like the previous master dashboard did.
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
    var showFullBody by rememberSaveable { mutableStateOf(false) }

    // Payment markers projected onto the attendance calendar (same regex as the
    // replaced master file): only when the date falls inside the on-screen month.
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

    // Scrolling forward folds the identity block away; scrolling back restores it.
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

    // A tab switch or a full-body open restarts the reading order at the top.
    LaunchedEffect(selectedTab, showFullBody) {
        lazyListState.scrollToItem(0)
        headerCompact = false
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(intelligenceColors().screenBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AnimatedVisibility(
            visible = !headerCompact,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            BadGymThemeSelectorStrip(
                selectedTheme = BadGymThemeManager.activeTheme,
                onThemeSelect = { BadGymThemeManager.setTheme(it) }
            )
        }

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
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
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
                else -> if (showFullBody) {
                    item(key = "tab_body_back") {
                        BodyOverviewBackChip(onBack = { showFullBody = false })
                    }
                    bodyIntelligenceItems(state = bodyState, listState = lazyListState)
                } else {
                    item(key = "tab_body") {
                        BodyIntelligenceLiteScreen(
                            data = data,
                            pass = pass,
                            onMoreClick = { showFullBody = true }
                        )
                    }
                }
            }
        }

        // Drill-down and privacy sheets live at the shell root so they survive
        // the lazy list recycling its items.
        BodyIntelligenceSheetHost(state = bodyState)
    }
}

/** Muted pill that collapses the full Body workspace back to the lite Body overview. */
@Composable
private fun BodyOverviewBackChip(onBack: () -> Unit) {
    val c = intelligenceColors()
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(c.cardFill)
            .border(1.dp, c.cardEdge, RoundedCornerShape(50))
            .clickable { onBack() }
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "◂ Body overview",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = c.ink
        )
    }
}
