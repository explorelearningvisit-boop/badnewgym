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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

/**
 * Ultra-compact, bold-first Member Intelligence Header.
 * Vertically compressed by 50% so cards and charts are immediately visible without excessive scrolling.
 * All text is bold, crisp, and high-contrast for effortless readability.
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
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
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
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 1. Sleek Top Bar (zero clipping)
            IntelligenceTopBar(
                colors = c,
                dueAmount = dueAmount,
                dueDate = dueDate,
                isCheckedIn = selectedTab == 2,
                onMenuClick = onMenuClick,
                onDueClick = onDueClick
            )

            // 2. Streamlined Combined Identity + Membership Progress Card
            AnimatedVisibility(
                visible = !compact,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                CombinedIdentityAndProgressCard(
                    pass = pass,
                    dueAmount = dueAmount,
                    usedDays = usedDays,
                    usedLabel = usedLabel,
                    fillFraction = if (pass.totalDays > 0) usedDays.toFloat() / pass.totalDays else 0f,
                    colors = c,
                    onAvatarClick = onAvatarClick
                )
            }

            // 3. Compact 4-KPI Tab Row (Attendance, Workout, Payment, Body)
            KpiTabRow(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                colors = c
            )
        }
    }
}

/** Sleek single-row top bar: Wordmark, Check-in capsule, Trophy pill and Notification Bell. */
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
        // LEFT - Hamburger + BAD GYM wordmark
        Row(
            modifier = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable(onClick = onMenuClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = colors.ink,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = GOLD,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        "BAD GYM",
                        fontSize = IntelligenceTypography.sizeCardTitle,
                        fontWeight = IntelligenceTypography.weightValue,
                        color = colors.ink,
                        letterSpacing = (-0.5).sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    "Member Intelligence",
                    fontSize = IntelligenceTypography.sizeSmall,
                    fontWeight = IntelligenceTypography.weightBody,
                    color = NeuPalette.violetDeep,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        // CENTRE - Compact Bold CHECK-IN action pill
        Box(
            modifier = Modifier.weight(0.95f),
            contentAlignment = Alignment.Center
        ) {
            val pillColor = if (isCheckedIn) NeuPalette.green else NeuPalette.violet
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(IntelligenceDensity.radiusPill))
                    .background(pillColor)
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    if (isCheckedIn) "CHECKED" else "CHECK-IN",
                    fontSize = IntelligenceTypography.sizeSmall,
                    fontWeight = IntelligenceTypography.weightValue,
                    color = Color.White,
                    letterSpacing = 0.3.sp,
                    maxLines = 1
                )
            }
        }

        // RIGHT - Notification Bell with Red Badge + Golden Trophy Capsule
        Row(
            modifier = Modifier.weight(1.15f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    tint = colors.ink,
                    modifier = Modifier.size(16.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-1).dp, y = 2.dp)
                        .size(11.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("1", fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightValue, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(3.dp))

            Surface(
                onClick = onDueClick,
                shape = RoundedCornerShape(IntelligenceDensity.radiusPill),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = GOLD,
                        modifier = Modifier.size(13.dp)
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            dueAmount,
                            fontSize = IntelligenceTypography.sizeSmall,
                            fontWeight = IntelligenceTypography.weightValue,
                            color = colors.ink,
                            maxLines = 1
                        )
                        Text(
                            dueDate.take(11),
                            fontSize = IntelligenceTypography.sizeMicro,
                            fontWeight = IntelligenceTypography.weightBody,
                            color = NeuPalette.violet,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/**
 * Combined Identity + Membership Progress Card:
 * Merges member profile, plan status, and days countdown into a single ultra-compact, high-density card.
 */
@Composable
private fun CombinedIdentityAndProgressCard(
    pass: BadGymMemberPass,
    dueAmount: String,
    usedDays: Int,
    usedLabel: String,
    fillFraction: Float,
    colors: IntelligenceColors,
    onAvatarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neuRaised(radius = 12.dp, depth = 3.dp, blur = 6.dp)
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        // TOP ROW: Avatar + Member Name & Meta + Status Badges
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(NeuPalette.violet)
                    .padding(1.5.dp)
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

            // Member details & pills
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        pass.name,
                        fontSize = IntelligenceTypography.sizeCardTitle,
                        fontWeight = IntelligenceTypography.weightValue,
                        color = colors.ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        // Active Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(NeuPalette.green)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Active", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = Color.White)
                        }
                        // Plan Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(NeuPalette.violet)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Gold Plan", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = Color.White)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${pass.memberId} • ${pass.age}y • ${pass.gender}",
                        fontSize = IntelligenceTypography.sizeSmall,
                        fontWeight = IntelligenceTypography.weightBody,
                        color = NeuPalette.inkSoft,
                        maxLines = 1
                    )
                    Text(
                        "$dueAmount / 30d (Auto ON)",
                        fontSize = IntelligenceTypography.sizeSmall,
                        fontWeight = IntelligenceTypography.weightValue,
                        color = NeuPalette.violetDeep,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        // BOTTOM ROW: Membership Progress bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(
                    Icons.Filled.Layers,
                    contentDescription = null,
                    tint = NeuPalette.violet,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    "Membership: $usedDays / ${pass.totalDays} Days",
                    fontSize = IntelligenceTypography.sizeSmall,
                    fontWeight = IntelligenceTypography.weightBody,
                    color = colors.ink
                )
            }
            Text(
                "• ${pass.daysLeft}d left (${usedLabel}% Used)",
                fontSize = IntelligenceTypography.sizeSmall,
                fontWeight = IntelligenceTypography.weightValue,
                color = if (pass.daysLeft <= 7) Color(0xFFD97706) else NeuPalette.green
            )
        }

        // Slim Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(NeuPalette.shadowSoft)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillFraction.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(Brush.horizontalGradient(listOf(NeuPalette.green, NeuPalette.green)))
            )
        }
    }
}

/** Four equal KPI tiles acting as dashboard tabs. Compact height, bold readable values. */
@Composable
private fun KpiTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    colors: IntelligenceColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        KpiTile(
            index = 0,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.CalendarMonth,
            iconTint = NeuPalette.green,
            iconBg = Color(0xFFD1FAE5),
            title = "Attn",
            value = "81%",
            valueColor = NeuPalette.green,
            colors = colors,
            modifier = Modifier.weight(1f)
        )
        KpiTile(
            index = 1,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.FitnessCenter,
            iconTint = NeuPalette.violet,
            iconBg = Color(0xFFEDE9FE),
            title = "Workout",
            value = "61 min",
            valueColor = NeuPalette.violet,
            colors = colors,
            modifier = Modifier.weight(1f)
        )
        KpiTile(
            index = 2,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.AccountBalanceWallet,
            iconTint = NeuPalette.green,
            iconBg = Color(0xFFD1FAE5),
            title = "Payment",
            value = "Paid",
            valueColor = NeuPalette.green,
            colors = colors,
            modifier = Modifier.weight(1f)
        )
        KpiTile(
            index = 3,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            icon = Icons.Filled.Accessibility,
            iconTint = Color(0xFF9333EA),
            iconBg = Color(0xFFF3E8FF),
            title = "Body",
            value = "69.5 kg",
            valueColor = Color(0xFF9333EA),
            colors = colors,
            modifier = Modifier.weight(1f)
        )
    }
}

/** One KPI tab tile: clean compact layout with squircle icon, bold title and big bold value. */
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
    colors: IntelligenceColors,
    modifier: Modifier = Modifier
) {
    val selected = index == selectedTab
    val tileModifier = if (selected) {
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Brush.horizontalGradient(listOf(NeuPalette.violet, NeuPalette.violetDeep)))
    } else {
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, NeuPalette.shadowSoft, RoundedCornerShape(8.dp))
    }

    Box(
        modifier = modifier
            .height(36.dp)
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
                    .size(22.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(if (selected) Color.White.copy(alpha = 0.25f) else iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = if (selected) Color.White else iconTint,
                    modifier = Modifier.size(13.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    title,
                    fontSize = IntelligenceTypography.sizeSmall,
                    fontWeight = IntelligenceTypography.weightBody,
                    color = if (selected) Color.White.copy(alpha = 0.9f) else NeuPalette.muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    value,
                    fontSize = IntelligenceTypography.sizeSmall,
                    fontWeight = IntelligenceTypography.weightValue,
                    color = if (selected) Color.White else valueColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
