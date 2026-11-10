package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import java.util.Locale

private val GOLD = Color(0xFFF59E0B)
private val ROYAL_BLUE_START = Color(0xFF3B82F6)
private val ROYAL_BLUE_END = Color(0xFF1D4ED8)
private val ROYAL_BLUE_PRIMARY = Color(0xFF1D4ED8)
private val SKY_BLUE_RING = Color(0xFF38BDF8)
private val EMERALD_ACTIVE = Color(0xFF16A34A)
private val PURPLE_GOLD_PLAN = Color(0xFF8B5CF6)
private val AMBER_LEFT_BG = Color(0xFFFEF3C7)
private val AMBER_LEFT_TEXT = Color(0xFF78350F)
private val INK_NAVY = Color(0xFF0F172A)
private val INK_SLATE = Color(0xFF334155)
private val INK_MUTED = Color(0xFF64748B)

/**
 * Neo-Cloud Sky-Blue & Royal Electric Blue Member Intelligence Header.
 * Pixel-perfect 1:1 match with target design:
 * 1. Top bar: Hamburger + 🏆 BAD GYM / Member Intelligence + ✔ CHECK-IN capsule + 🔔¹ + 🏆 ₹1,200 capsule
 * 2. Member Profile card: Big avatar with sky-blue ring + Ashwani Kumar Singh + ID/Age/Gender + Active/Gold Plan + ₹1,200/30d (Auto ON)
 * 3. Membership Progress stadium: ▤ Membership: 22/30 Days + 73.3% Used gradient bar + 8 Days Left amber capsule
 * 4. 4 KPI tabs: Attn (81%), Workout (61 min), Payment (Paid), Body (69.5 kg) with active electric blue gradient + cyan flare
 * 5. Subnav stadium pills: [ ⊞ Overview ] [ 🕒 History ] [ ▤ Plan & Changes ] [ 📊 Behaviour ]
 */
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
    selectedSubTab: Int = 0,
    onSubTabSelected: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val plan = data.paymentPlan
    val dueAmount = plan.currentDueAmount.ifBlank { plan.dueAlertAmount }
    val dueDate = plan.currentDueDate.ifBlank { plan.dueAlertDate }

    val usedDays = (pass.totalDays - pass.daysLeft).coerceIn(0, pass.totalDays)
    val usedPercent = if (pass.totalDays > 0) usedDays * 100f / pass.totalDays else 0f
    val usedLabel = String.format(Locale.US, "%.1f", usedPercent)

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // 1. Sleek Top Bar
            IntelligenceTopBar(
                dueAmount = dueAmount,
                dueDate = dueDate,
                isCheckedIn = selectedTab == 2,
                onMenuClick = onMenuClick,
                onDueClick = onDueClick
            )

            // 2. Member Profile Card
            AnimatedVisibility(
                visible = !compact,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                MemberProfileCard(
                    pass = pass,
                    dueAmount = dueAmount,
                    onAvatarClick = onAvatarClick
                )
            }

            // 3. Dedicated Membership Progress Stadium Capsule
            AnimatedVisibility(
                visible = !compact,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                MembershipProgressStadium(
                    pass = pass,
                    usedDays = usedDays,
                    usedLabel = usedLabel,
                    fillFraction = if (pass.totalDays > 0) usedDays.toFloat() / pass.totalDays else 0f
                )
            }

            // 4. 4 KPI Tabs Row (Attn, Workout, Payment, Body)
            KpiTabRow(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )

            // 5. Sub-navigation Pills Row (Overview, History, Plan & Changes, Behaviour)
            SubNavPillRow(
                selectedSubTab = selectedSubTab,
                onSubTabSelected = onSubTabSelected
            )
        }
    }
}

/** 1. Top bar: Hamburger, Brand, CHECK-IN capsule, Bell with badge, Trophy pill */
@Composable
private fun IntelligenceTopBar(
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
        // LEFT - Hamburger + BAD GYM wordmark
        Row(
            modifier = Modifier.weight(1.35f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onMenuClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = GOLD,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(
                        "BAD GYM",
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.Black,
                        color = ROYAL_BLUE_PRIMARY,
                        letterSpacing = (-0.5).sp
                    )
                }
                Text(
                    "Member Intelligence",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = INK_SLATE,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        // CENTRE - Royal Electric Blue CHECK-IN action stadium capsule
        Box(
            modifier = Modifier.weight(1.05f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(50.dp),
                        spotColor = Color(0x601D4ED8),
                        ambientColor = Color(0x201D4ED8)
                    )
                    .clip(RoundedCornerShape(50.dp))
                    .background(Brush.horizontalGradient(listOf(ROYAL_BLUE_START, ROYAL_BLUE_END)))
                    .clickable { }
                    .padding(horizontal = 12.dp, vertical = 6.5.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        if (isCheckedIn) "CHECKED-IN" else "CHECK-IN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.5.sp,
                        maxLines = 1
                    )
                }
            }
        }

        // RIGHT - Notification Bell with Red Badge + Golden Trophy Capsule
        Row(
            modifier = Modifier.weight(1.2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-1).dp, y = 1.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("1", fontSize = 7.5.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Surface(
                onClick = onDueClick,
                shape = RoundedCornerShape(50.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.5.dp),
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
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Black,
                            color = INK_NAVY,
                            maxLines = 1
                        )
                        Text(
                            dueDate.take(11),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = INK_MUTED,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/** 2. Member Profile Card: Large avatar with sky-blue ring, ID/Age/Gender, Active/Gold Plan pills, and price capsule */
@Composable
private fun MemberProfileCard(
    pass: BadGymMemberPass,
    dueAmount: String,
    onAvatarClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clayCard(radius = 22.dp, elevation = 5.dp)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Circular Avatar with Bright Sky-Blue Ring
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(SKY_BLUE_RING)
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

            // Member Details & Badges
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Name
                Text(
                    pass.name,
                    fontSize = 17.5.sp,
                    fontWeight = FontWeight.Black,
                    color = INK_NAVY,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Meta row: 👤 BG215 | ♀ 28 Yrs | ♂ Male
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Filled.Badge,
                        contentDescription = null,
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        pass.memberId,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        "|",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                    Icon(
                        Icons.Filled.Face,
                        contentDescription = null,
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        "${pass.age} Yrs",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        "|",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                    Icon(
                        Icons.Filled.Male,
                        contentDescription = null,
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        pass.gender,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Badges row: Active (emerald) & Gold Plan (purple)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Active Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(EMERALD_ACTIVE)
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Text(
                                "Active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    // Gold Plan Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(PURPLE_GOLD_PLAN)
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("👑", fontSize = 10.sp)
                            Text(
                                "Gold Plan",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Right Plan & Auto-ON Capsule
            Box(
                modifier = Modifier
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0x141E3A8A))
                    .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "$dueAmount / 30d",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = INK_NAVY
                    )
                    Text(
                        "(Auto ON)",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = PURPLE_GOLD_PLAN
                    )
                }
            }
        }
    }
}

/** 3. Dedicated Membership Progress Stadium Capsule */
@Composable
private fun MembershipProgressStadium(
    pass: BadGymMemberPass,
    usedDays: Int,
    usedLabel: String,
    fillFraction: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clayCard(radius = 50.dp, elevation = 4.dp)
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Left layers icon + text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    Icons.Filled.Layers,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(17.dp)
                )
                Text(
                    "Membership: $usedDays / ${pass.totalDays} Days",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = INK_NAVY
                )
            }

            // Middle Pill Progress Bar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFDBEAFE))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fillFraction.coerceIn(0.08f, 1f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF38BDF8), Color(0xFF2563EB), Color(0xFF1D4ED8))
                            )
                        ),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        "${usedLabel}% Used",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }

            // Right Amber 8 Days Left Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(AMBER_LEFT_BG)
                    .padding(horizontal = 10.dp, vertical = 4.5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${pass.daysLeft} Days Left",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = AMBER_LEFT_TEXT
                )
            }
        }
    }
}

/** 4. 4 KPI Tabs Row (Attn 81%, Workout 61 min, Payment Paid, Body 69.5 kg) */
@Composable
private fun KpiTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        KpiCard(
            index = 0,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.CalendarMonth,
            iconTint = Color(0xFF2563EB),
            title = "Attn",
            value = "81%",
            valueColor = Color(0xFF0F172A),
            modifier = Modifier.weight(1f)
        )
        KpiCard(
            index = 1,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.FitnessCenter,
            iconTint = Color(0xFF8B5CF6),
            title = "Workout",
            value = "61 min",
            valueColor = Color(0xFF0F172A),
            modifier = Modifier.weight(1f)
        )
        KpiCard(
            index = 2,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.AccountBalanceWallet,
            iconTint = Color(0xFF10B981),
            title = "Payment",
            value = "Paid",
            valueColor = Color(0xFF16A34A),
            modifier = Modifier.weight(1f)
        )
        KpiCard(
            index = 3,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.Accessibility,
            iconTint = Color(0xFF8B5CF6),
            title = "Body",
            value = "69.5 kg",
            valueColor = Color(0xFF0F172A),
            modifier = Modifier.weight(1f)
        )
    }
}

/** One KPI Tab Card with claymorphism, and active electric royal blue gradient + glowing cyan flare */
@Composable
private fun KpiCard(
    index: Int,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    icon: ImageVector,
    iconTint: Color,
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    val selected = index == selectedTab

    val baseModifier = if (selected) {
        Modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0x601D4ED8),
                ambientColor = Color(0x201D4ED8)
            )
            .border(1.dp, Color(0xFF93C5FD).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(ROYAL_BLUE_START, ROYAL_BLUE_END)))
            .drawBehind {
                // Bottom-left glowing cyan reflection flare from reference image
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF67E8F9).copy(alpha = 0.85f),
                            Color(0xFF38BDF8).copy(alpha = 0.35f),
                            Color.Transparent
                        ),
                        center = Offset(12.dp.toPx(), size.height - 8.dp.toPx()),
                        radius = 46.dp.toPx()
                    ),
                    radius = 46.dp.toPx(),
                    center = Offset(12.dp.toPx(), size.height - 8.dp.toPx())
                )
            }
    } else {
        Modifier
            .clayCard(radius = 16.dp, elevation = 4.dp)
    }

    Box(
        modifier = modifier
            .height(58.dp)
            .then(baseModifier)
            .clickable { onTabSelected(index) }
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Icon
            Icon(
                icon,
                contentDescription = title,
                tint = if (selected) Color.White else iconTint,
                modifier = Modifier.size(24.dp)
            )

            // Text column: Title above, large value below
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) Color.White.copy(alpha = 0.95f) else INK_SLATE,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    value,
                    fontSize = 18.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (selected) Color.White else valueColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/** 5. Sub-navigation Pills Row: [ ⊞ Overview ] [ 🕒 History ] [ ▤ Plan & Changes ] [ 📊 Behaviour ] */
@Composable
private fun SubNavPillRow(
    selectedSubTab: Int,
    onSubTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SubNavPill(
            index = 0,
            selected = selectedSubTab == 0,
            onClick = { onSubTabSelected(0) },
            icon = Icons.Filled.GridView,
            label = "Overview",
            modifier = Modifier.weight(1.1f)
        )
        SubNavPill(
            index = 1,
            selected = selectedSubTab == 1,
            onClick = { onSubTabSelected(1) },
            icon = Icons.Filled.Schedule,
            label = "History",
            modifier = Modifier.weight(1f)
        )
        SubNavPill(
            index = 2,
            selected = selectedSubTab == 2,
            onClick = { onSubTabSelected(2) },
            icon = Icons.Filled.Layers,
            label = "Plan & Changes",
            modifier = Modifier.weight(1.35f)
        )
        SubNavPill(
            index = 3,
            selected = selectedSubTab == 3,
            onClick = { onSubTabSelected(3) },
            icon = Icons.Filled.BarChart,
            label = "Behaviour",
            modifier = Modifier.weight(1.1f)
        )
    }
}

/** One stadium pill in the sub-navigation row */
@Composable
private fun SubNavPill(
    index: Int,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    val pillModifier = if (selected) {
        Modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(50.dp),
                spotColor = Color(0x501D4ED8)
            )
            .clip(RoundedCornerShape(50.dp))
            .background(Brush.horizontalGradient(listOf(ROYAL_BLUE_START, ROYAL_BLUE_END)))
    } else {
        Modifier
            .clayCard(radius = 50.dp, elevation = 3.dp)
    }

    Box(
        modifier = modifier
            .height(34.dp)
            .then(pillModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (selected) Color.White else Color(0xFF1E293B),
                modifier = Modifier.size(14.dp)
            )
            Text(
                label,
                fontSize = 11.5.sp,
                fontWeight = if (selected) FontWeight.Black else FontWeight.Bold,
                color = if (selected) Color.White else Color(0xFF1E293B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
