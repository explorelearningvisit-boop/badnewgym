package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
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
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.intelligence.IntelligenceSeries
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import java.util.Locale

private val GOLD = Color(0xFFFFBB0D)

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
    } ?: "69.5 kg"

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IntelligenceTopBar(
                colors = c,
                dueAmount = dueAmount,
                dueDate = dueDate,
                isCheckedIn = selectedTab == 2,
                onMenuClick = onMenuClick,
                onDueClick = onDueClick
            )
            IntelligenceIdentityRow(
                pass = pass,
                dueAmount = dueAmount,
                colors = c,
                onAvatarClick = onAvatarClick
            )
            AnimatedVisibility(
                visible = !compact,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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
                        colors = c
                    )
                }
            }
        }
    }
}

/** Left wordmark (menu + BAD GYM), a bold centred CHECK-IN pill filled with the theme accent, and the due block on the right. */
@Composable
private fun IntelligenceTopBar(
    colors: IntelligenceColors,
    dueAmount: String,
    dueDate: String,
    isCheckedIn: Boolean = false,
    onMenuClick: () -> Unit,
    onDueClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LEFT - hamburger + BAD GYM wordmark pinned to the leading edge.
        Row(
            modifier = Modifier.weight(1.1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clickable(onClick = onMenuClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = colors.ink,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = GOLD,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "BAD GYM",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.ink,
                        letterSpacing = (-0.5).sp
                    )
                }
                Text(
                    "Member Intelligence",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.inkSoft,
                    letterSpacing = 0.2.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        // CENTRE - bold CHECK-IN / CHECKED IN action pill
        Box(
            modifier = Modifier.weight(1.2f),
            contentAlignment = Alignment.Center
        ) {
            val pillBrush = if (isCheckedIn) {
                Brush.horizontalGradient(listOf(Color(0xFF21A87D), Color(0xFF178A66)))
            } else {
                Brush.horizontalGradient(listOf(Color(0xFF7B81BE), Color(0xFF5E649E)))
            }
            val pillColor = if (isCheckedIn) NeuPalette.green else NeuPalette.violet
            Row(
                modifier = Modifier
                    .neuAccent(pillColor, radius = 50.dp, depth = 3.dp, blur = 8.dp)
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    if (isCheckedIn) "CHECKED-IN" else "CHECK-IN",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 0.4.sp,
                    maxLines = 1
                )
            }
        }

        // RIGHT - Notification Bell with Red Badge + Golden Trophy Capsule
        Row(
            modifier = Modifier.weight(1.2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            // Notification Bell with Red Unread Badge
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    tint = colors.ink,
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-1).dp, y = 2.dp)
                        .size(11.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF7272)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("1", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Golden Trophy Stadium Capsule
            Surface(
                onClick = onDueClick,
                shape = RoundedCornerShape(50),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFF7E3B2)),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = GOLD,
                        modifier = Modifier.size(15.dp)
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            dueAmount,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.ink,
                            maxLines = 1
                        )
                        Text(
                            dueDate.take(11),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.accentDeep,
                            maxLines = 1
                        )
                    }
                }
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
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.accent)
                .padding(2.dp)
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
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                pass.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                IdentityMetaItem(Icons.Filled.Person, pass.memberId, colors)
                IdentityMetaItem(Icons.Filled.CalendarMonth, "${pass.age} Yrs", colors)
                IdentityMetaItem(Icons.Filled.Male, pass.gender, colors)
            }
            Row(
                modifier = Modifier.height(19.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
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
            .background(Color(0xFF21A87D))
            .padding(horizontal = 6.dp, vertical = 2.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White))
        Text("Active", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}

/** Solid accent pill carrying the plan tier diamond. */
@Composable
private fun PlanChip(planName: String, colors: IntelligenceColors) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF7B81BE))
            .padding(horizontal = 6.dp, vertical = 2.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(Icons.Filled.Diamond, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
        Text(planName, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color.White, maxLines = 1)
    }
}

/** White pill with an accent-tinted border. */
@Composable
private fun OutlinedStatusChip(text: String, colors: IntelligenceColors) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colors.cardFill)
            .border(1.dp, Color(0xFFDCD9EE), RoundedCornerShape(50))
            .padding(horizontal = 6.dp, vertical = 2.5.dp)
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
            .background(Color(0xFFE0E3F2))
            .border(1.dp, Color(0xFFD8DBEE), RoundedCornerShape(50))
            .padding(horizontal = 6.dp, vertical = 2.5.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFF4E5484), fontWeight = FontWeight.SemiBold)) { append("Auto ") }
                withStyle(SpanStyle(color = Color(0xFF6A70A8), fontWeight = FontWeight.Bold)) { append("(ON)") }
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
            .neuRaised(radius = 16.dp, depth = 4.dp, blur = 10.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFE0E3F2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Layers,
                    contentDescription = null,
                    tint = Color(0xFF7B81BE),
                    modifier = Modifier.size(11.dp)
                )
            }
            Text(
                "Membership Progress",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colors.ink,
                letterSpacing = (-0.2).sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                "$usedDays / ${pass.totalDays} Days",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.ink
            )
            Text(
                "· ${pass.daysLeft}d left",
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD99F0B)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .neuSunken(radius = 5.dp, color = Color(0xFFDCE2E8))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillFraction.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(5.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF21A87D), Color(0xFF178A66)))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$usedPercentLabel% Used",
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
            }
        }
    }
}

/** Four equal KPI tiles acting as dashboard tabs matching media_1794217986950_71037fae.jpg. */
@Composable
private fun KpiTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    colors: IntelligenceColors
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        KpiTile(
            index = 0,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.CalendarMonth,
            iconTint = Color(0xFF21A87D),
            iconBg = Color(0xFFDAEFE6),
            title = "Attendance",
            value = "81%",
            valueColor = Color(0xFF21A87D),
            sub = "25 / 31 Days",
            colors = colors,
            modifier = Modifier.weight(1f)
        )
        KpiTile(
            index = 1,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.FitnessCenter,
            iconTint = Color(0xFF7B81BE),
            iconBg = Color(0xFFE0E3F2),
            title = "Workout",
            value = "61 min",
            valueColor = colors.ink,
            sub = "Avg / Day",
            colors = colors,
            modifier = Modifier.weight(1f)
        )
        KpiTile(
            index = 2,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.AccountBalanceWallet,
            iconTint = Color.White,
            iconBg = Color(0xFF7B81BE),
            title = "Payment",
            value = "Paid",
            valueColor = Color(0xFF21A87D),
            sub = "Next: 07 Nov",
            colors = colors,
            modifier = Modifier.weight(1f)
        )
        KpiTile(
            index = 3,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.Accessibility,
            iconTint = Color(0xFF7B81BE),
            iconBg = Color(0xFFE0E3F2),
            title = "Body",
            value = "69.5 kg",
            valueColor = colors.ink,
            sub = "▲ +2.1 kg",
            subColor = Color(0xFF21A87D),
            colors = colors,
            modifier = Modifier.weight(1f)
        )
    }
}

/** One KPI tab tile: 3-line layout with squircle icon matching the reference images. */
@Composable
private fun KpiTile(
    index: Int,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    value: String,
    valueColor: Color,
    sub: String,
    subColor: Color = Color(0xFF8C94A0),
    colors: IntelligenceColors,
    modifier: Modifier = Modifier
) {
    val selected = index == selectedTab
    val tileModifier = if (selected) {
        Modifier.neuAccent(color = NeuPalette.violet, radius = 16.dp, depth = 6.dp, blur = 16.dp)
    } else {
        Modifier.neuRaised(radius = 16.dp, depth = 4.dp, blur = 11.dp)
    }

    Box(
        modifier = modifier
            .height(46.dp)
            .then(tileModifier)
            .clickable { onTabSelected(index) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(if (selected) Color.White.copy(alpha = 0.25f) else iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = if (selected) Color.White else iconTint,
                    modifier = Modifier.size(15.dp)
                )
            }
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    title,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) Color.White.copy(alpha = 0.9f) else Color(0xFF8C94A0),
                    maxLines = 1
                )
                Text(
                    value,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (selected) Color.White else valueColor,
                    maxLines = 1
                )
                Text(
                    sub,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected) Color.White.copy(alpha = 0.85f) else subColor,
                    maxLines = 1
                )
            }
        }
    }
}
