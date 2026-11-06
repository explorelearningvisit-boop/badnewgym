package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.intelligence.IntelligenceSeries
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import java.util.Locale

private val GOLD = Color(0xFFF59E0B)

/** Light-canvas dashboard header: leaf swooshes, wordmark, identity block, membership bar and the 4-tile KPI tab row. */
@Composable
fun IntelligenceHeader(
    data: MemberCardData,
    pass: BadGymMemberPass,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onMenuClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onDueClick: () -> Unit,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val plan = data.paymentPlan
    val dueAmount = plan.currentDueAmount.ifBlank { plan.dueAlertAmount }
    val dueDate = plan.currentDueDate.ifBlank { plan.dueAlertDate }

    val daily = remember(data.workoutTrend.bars) {
        IntelligenceSeries.workoutDailyNormalized(data.workoutTrend.bars)
    }
    val activeDays = daily.count { it.minutes > 0 }
    val totalMinutes = daily.sumOf { it.minutes }
    val avgSessionMin = if (activeDays > 0) totalMinutes / activeDays else 0

    val usedDays = (pass.totalDays - pass.daysLeft).coerceIn(0, pass.totalDays)
    val usedPercent = if (pass.totalDays > 0) usedDays * 100f / pass.totalDays else 0f
    val usedLabel = String.format(Locale.US, "%.1f", usedPercent)

    val weightStat = data.bodyStats.firstOrNull { it.type == BodyStatType.WEIGHT } ?: data.bodyStats.firstOrNull()
    val bodyValue = weightStat?.let {
        if (it.unit.isNotBlank() && !it.value.endsWith(it.unit)) "${it.value} ${it.unit}" else it.value
    } ?: "--"

    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawIntelligenceSwooshes(c.accent)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (compact) Modifier.padding(top = 6.dp) else Modifier),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AnimatedVisibility(
                visible = !compact,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            IntelligenceTopBar(
                ink = c.ink,
                inkSoft = c.inkSoft,
                accentDeep = c.accentDeep,
                dueAmount = dueAmount,
                dueDate = dueDate,
                onMenuClick = onMenuClick,
                onDueClick = onDueClick
            )
            IntelligenceIdentityRow(
                pass = pass,
                dueAmount = dueAmount,
                colors = c,
                onAvatarClick = onAvatarClick
            )
            MembershipProgressCard(
                pass = pass,
                usedDays = usedDays,
                usedPercentLabel = usedLabel,
                fillFraction = if (pass.totalDays > 0) usedDays.toFloat() / pass.totalDays else 0f,
                colors = c
            )
                }
            }
            KpiTabRow(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                attendanceValue = "${data.attendance.percent}%",
                workoutValue = "$avgSessionMin min",
                paymentValue = dueAmount,
                bodyValue = bodyValue,
                colors = c
            )
        }
    }
}

/** Hamburger, centred crown wordmark and the plain (pill-free) due amount block. */
@Composable
private fun IntelligenceTopBar(
    ink: Color,
    inkSoft: Color,
    accentDeep: Color,
    dueAmount: String,
    dueDate: String,
    onMenuClick: () -> Unit,
    onDueClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clickable(onClick = onMenuClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = ink,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(
                    Icons.Filled.EmojiEvents,
                    contentDescription = null,
                    tint = GOLD,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "BAD GYM",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ink,
                    letterSpacing = (-0.5).sp
                )
            }
            Text(
                "Member Intelligence",
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = inkSoft,
                letterSpacing = 0.4.sp
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onDueClick)
                .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                Icons.Filled.EmojiEvents,
                contentDescription = null,
                tint = GOLD,
                modifier = Modifier.size(20.dp)
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    dueAmount,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ink,
                    maxLines = 1
                )
                Text(
                    dueDate,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = accentDeep,
                    maxLines = 1
                )
            }
        }
    }
}

/** Avatar with a double accent ring, then name, identity meta icons and the plan status chips. */
@Composable
private fun IntelligenceIdentityRow(
    pass: BadGymMemberPass,
    dueAmount: String,
    colors: IntelligenceColors,
    onAvatarClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(colors.accent)
                .padding(2.5.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(onClick = onAvatarClick)
        ) {
            Image(
                painter = painterResource(id = pass.avatarRes),
                contentDescription = "Member avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                pass.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                IdentityMetaItem(Icons.Filled.Person, pass.memberId, colors)
                IdentityMetaItem(Icons.Filled.CalendarMonth, "${pass.age} Yrs", colors)
                IdentityMetaItem(Icons.Filled.Male, pass.gender, colors)
            }
            Row(
                modifier = Modifier.height(23.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SolidStatusChip(colors = colors)
                PlanChip(pass.planName.substringBefore(" ("), colors)
                OutlinedStatusChip("$dueAmount / ${pass.totalDays} Days", colors)
                AutoChip(colors)
            }
        }
    }
}

/** Solid accent pill with a white presence dot. */
@Composable
private fun SolidStatusChip(colors: IntelligenceColors) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colors.accent)
            .padding(horizontal = 7.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White))
        Text("Active", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = colors.onAccent)
    }
}

/** Solid accent pill carrying the plan tier diamond. */
@Composable
private fun PlanChip(planName: String, colors: IntelligenceColors) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colors.accent)
            .padding(horizontal = 7.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(Icons.Filled.Diamond, contentDescription = null, tint = colors.onAccent, modifier = Modifier.size(11.dp))
        Text(planName, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = colors.onAccent, maxLines = 1)
    }
}

/** White pill with an accent-tinted border. */
@Composable
private fun OutlinedStatusChip(text: String, colors: IntelligenceColors) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colors.cardFill)
            .border(1.2.dp, colors.accentSoft, RoundedCornerShape(50))
            .padding(horizontal = 7.dp, vertical = 4.dp)
    ) {
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = colors.ink, maxLines = 1)
    }
}

/** Auto-renew pill: ink stem with the accent-emphasised state suffix. */
@Composable
private fun AutoChip(colors: IntelligenceColors) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colors.cardFill)
            .border(1.2.dp, colors.accentSoft, RoundedCornerShape(50))
            .padding(horizontal = 7.dp, vertical = 4.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) { append("Auto ") }
                withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.Bold)) { append("(ON)") }
            },
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}

/** Small icon + text pair used in the identity meta row. */
@Composable
private fun IdentityMetaItem(icon: ImageVector, text: String, colors: IntelligenceColors) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Icon(icon, contentDescription = null, tint = colors.accentDeep, modifier = Modifier.size(11.dp))
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = colors.inkSoft, maxLines = 1)
    }
}

/** White membership card: solid accent fill with the used-percentage label inside it. */
@Composable
private fun MembershipProgressCard(
    pass: BadGymMemberPass,
    usedDays: Int,
    usedPercentLabel: String,
    fillFraction: Float,
    colors: IntelligenceColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.cardFill)
            .border(1.2.dp, colors.cardEdge, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Filled.Insights, contentDescription = null, tint = colors.accent, modifier = Modifier.size(16.dp))
            Text(
                "Membership Progress",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = colors.ink,
                letterSpacing = (-0.2).sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                "$usedDays/${pass.totalDays} Days",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.ink
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(colors.ink.copy(alpha = 0.06f))
                .border(1.dp, colors.accentSoft, RoundedCornerShape(9.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillFraction.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(9.dp))
                    .background(colors.good),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$usedPercentLabel% Used",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IntelligencePillOutlined("📅 ${pass.startDate} – ${pass.endDate} (${pass.totalDays} Days Plan)")
            IntelligencePillOutlined("⏳ ${pass.daysLeft} Days Left")
        }
    }
}

/** Four equal KPI tiles acting as dashboard tabs; the active tile carries the theme brush. */
@Composable
private fun KpiTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    attendanceValue: String,
    workoutValue: String,
    paymentValue: String,
    bodyValue: String,
    colors: IntelligenceColors
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        KpiTile(0, selectedTab, onTabSelected, Icons.Filled.CalendarMonth, attendanceValue, "Attendance", colors, Modifier.weight(1f))
        KpiTile(1, selectedTab, onTabSelected, Icons.Filled.FitnessCenter, workoutValue, "Workout", colors, Modifier.weight(1f))
        KpiTile(2, selectedTab, onTabSelected, Icons.Filled.AccountBalanceWallet, paymentValue, "Payment", colors, Modifier.weight(1f))
        KpiTile(3, selectedTab, onTabSelected, Icons.Filled.Accessibility, bodyValue, "Body", colors, Modifier.weight(1f))
    }
}

/** One KPI tab tile: icon, value and label, with no mini progress bar. */
@Composable
private fun KpiTile(
    index: Int,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    icon: ImageVector,
    value: String,
    label: String,
    colors: IntelligenceColors,
    modifier: Modifier = Modifier
) {
    val selected = index == selectedTab
    val contentColor = colors.ink
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .height(72.dp)
            .clip(shape)
            .background(colors.cardFill, shape)
            .then(
                if (selected) Modifier.border(2.dp, colors.warn, shape)
                else Modifier.border(1.2.dp, colors.cardEdge, shape)
            )
            .clickable { onTabSelected(index) }
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
    ) {
        Icon(icon, contentDescription = null, tint = colors.accent, modifier = Modifier.size(18.dp))
        Text(
            value,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = contentColor,
            maxLines = 1
        )
        Text(
            label,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
            maxLines = 1
        )
    }
}
