package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.intelligence.IntelligenceSeries
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import java.util.Locale

/** Hero-band dashboard header: top bar, identity row, membership progress card and the 4-tile KPI tab row. */
@Composable
fun IntelligenceHeader(
    data: MemberCardData,
    pass: BadGymMemberPass,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onMenuClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onDueClick: () -> Unit,
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
    val consistencyRatio = if (daily.isNotEmpty()) activeDays.toFloat() / daily.size else 0f

    val usedDays = (pass.totalDays - pass.daysLeft).coerceIn(0, pass.totalDays)
    val usedPercent = if (pass.totalDays > 0) usedDays * 100f / pass.totalDays else 0f
    val usedLabel = String.format(Locale.US, "%.1f", usedPercent)

    val weightStat = data.bodyStats.firstOrNull { it.type == BodyStatType.WEIGHT } ?: data.bodyStats.firstOrNull()
    val bodyValue = weightStat?.let {
        if (it.unit.isNotBlank() && !it.value.endsWith(it.unit)) "${it.value} ${it.unit}" else it.value
    } ?: "--"
    val paymentRatio = if (plan.planTotalDays > 0) plan.planDaysUsed.toFloat() / plan.planTotalDays else 0f
    val attendanceRatio = data.attendance.percent.toFloat() / 100f

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        IntelligenceTopBar(
            accent = c.accent,
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
        KpiTabRow(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            attendanceValue = "${data.attendance.percent}%",
            attendanceRatio = attendanceRatio,
            workoutValue = "$avgSessionMin min",
            workoutRatio = consistencyRatio,
            paymentValue = dueAmount,
            paymentRatio = paymentRatio,
            bodyValue = bodyValue,
            colors = c
        )
    }
}

/** Menu button, centered crown BAD GYM wordmark and the gold due trophy pill. */
@Composable
private fun IntelligenceTopBar(
    accent: Color,
    dueAmount: String,
    dueDate: String,
    onMenuClick: () -> Unit,
    onDueClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = onMenuClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFFBBF24),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.White)) { append("BAD ") }
                        withStyle(SpanStyle(color = accent)) { append("GYM") }
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
            }
            Text(
                "Member Intelligence",
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 0.5.sp
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Brush.linearGradient(listOf(Color(0xFFFBBF24), Color(0xFFF59E0C))))
                .clickable(onClick = onDueClick)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = Color(0xFF78350F),
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        dueAmount,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF78350F),
                        maxLines = 1
                    )
                }
                Text(
                    dueDate,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF78350F).copy(alpha = 0.85f),
                    maxLines = 1
                )
            }
        }
    }
}

/** Avatar with accent ring plus name, identity meta icons and plan status chips. */
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
                .size(50.dp)
                .border(2.dp, colors.accent, CircleShape)
                .clip(CircleShape)
                .clickable(onClick = onAvatarClick)
        ) {
            Image(
                painter = painterResource(id = pass.avatarRes),
                contentDescription = "Member avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                pass.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                IdentityMetaItem(Icons.Filled.Person, pass.memberId)
                IdentityMetaItem(Icons.Filled.CalendarMonth, "${pass.age} Yrs")
                IdentityMetaItem(Icons.Filled.Male, pass.gender)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IdentityChip(
                    text = "● Active",
                    bg = colors.good.copy(alpha = 0.20f),
                    textColor = colors.good
                )
                IdentityChip(
                    text = "💎 ${pass.planName.substringBefore(" (")}",
                    bg = colors.accentWash,
                    textColor = Color.White
                )
                IdentityChip(
                    text = "$dueAmount / ${pass.totalDays} Days",
                    bg = Color.White.copy(alpha = 0.14f),
                    textColor = Color.White
                )
                IdentityChip(
                    text = "Auto ON",
                    bg = Color.White.copy(alpha = 0.14f),
                    textColor = Color.White
                )
            }
        }
    }
}

/** Small white-on-hero icon + text pair used in the identity meta row. */
@Composable
private fun IdentityMetaItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(10.dp)
        )
        Text(
            text,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.8f),
            maxLines = 1
        )
    }
}

/** Single rounded status pill on the hero band. */
@Composable
private fun IdentityChip(text: String, bg: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            maxLines = 1
        )
    }
}

/** White card with the used-days gradient bar and plan window / days-left chips. */
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
            .border(1.dp, colors.cardEdge, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "// Membership Progress",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colors.ink,
                letterSpacing = (-0.2).sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                "$usedDays/${pass.totalDays} Days",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = colors.ink
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(colors.ink.copy(alpha = 0.08f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillFraction.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(7.dp))
                    .background(Brush.horizontalGradient(listOf(colors.accent, colors.accentDeep)))
            )
            Text(
                "$usedPercentLabel% Used",
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            WindowChip("📅 ${pass.startDate} – ${pass.endDate} (${pass.totalDays} Days Plan)", colors)
            WindowChip("⏳ ${pass.daysLeft} Days Left", colors)
        }
    }
}

/** Muted informational chip under the progress bar. */
@Composable
private fun WindowChip(text: String, colors: IntelligenceColors) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colors.muted.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.inkSoft,
            maxLines = 1
        )
    }
}

/** Four equal KPI tiles acting as dashboard tabs, with a mini progress bar on each. */
@Composable
private fun KpiTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    attendanceValue: String,
    attendanceRatio: Float,
    workoutValue: String,
    workoutRatio: Float,
    paymentValue: String,
    paymentRatio: Float,
    bodyValue: String,
    colors: IntelligenceColors
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        KpiTile(
            index = 0,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.CalendarMonth,
            value = attendanceValue,
            label = "Attendance",
            ratio = attendanceRatio,
            colors = colors,
            modifier = Modifier.weight(1f)
        )
        KpiTile(
            index = 1,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.FitnessCenter,
            value = workoutValue,
            label = "Workout",
            ratio = workoutRatio,
            colors = colors,
            modifier = Modifier.weight(1f)
        )
        KpiTile(
            index = 2,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.Wallet,
            value = paymentValue,
            label = "Payment",
            ratio = paymentRatio,
            colors = colors,
            modifier = Modifier.weight(1f)
        )
        KpiTile(
            index = 3,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.Accessibility,
            value = bodyValue,
            label = "Body",
            ratio = 0.423f,
            colors = colors,
            modifier = Modifier.weight(1f)
        )
    }
}

/** One selectable KPI tile: active tab gets the theme brush, glow shadow and strong mini bar. */
@Composable
private fun KpiTile(
    index: Int,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    icon: ImageVector,
    value: String,
    label: String,
    ratio: Float,
    colors: IntelligenceColors,
    modifier: Modifier = Modifier
) {
    val selected = index == selectedTab
    val contentColor = if (selected) colors.onActiveTab else colors.ink
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier
            .then(
                if (selected) {
                    Modifier.shadow(
                        elevation = 3.dp,
                        shape = shape,
                        ambientColor = colors.accentDeep,
                        spotColor = colors.accent
                    )
                } else {
                    Modifier
                }
            )
            .clip(shape)
            .then(
                if (selected) Modifier.background(colors.activeTabBrush, shape)
                else Modifier.background(colors.cardFill, shape)
            )
            .border(1.dp, if (selected) Color.Transparent else colors.cardEdge, shape)
            .clickable { onTabSelected(index) }
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(14.dp))
        Text(
            value,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Black,
            color = contentColor,
            maxLines = 1
        )
        Text(
            label,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) colors.onActiveTab else colors.muted,
            maxLines = 1
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(3.dp)
                .clip(RoundedCornerShape(50))
                .background(
                    if (selected) colors.onActiveTab.copy(alpha = 0.25f) else colors.ink.copy(alpha = 0.08f)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (selected) colors.onActiveTab.copy(alpha = 0.9f) else colors.faint
                    )
            )
        }
    }
}
