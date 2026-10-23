package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.badnewgym.feature.memberintelligence.domain.engine.AttendanceBreakdown
import com.example.badnewgym.feature.memberintelligence.domain.engine.CombinedDay
import com.example.badnewgym.feature.memberintelligence.domain.engine.DashboardMath
import com.example.badnewgym.feature.memberintelligence.domain.engine.attendanceBreakdown
import com.example.badnewgym.feature.memberintelligence.domain.engine.attendanceRateExcludingClosures
import com.example.badnewgym.feature.memberintelligence.domain.engine.averageRecordedMinutes
import com.example.badnewgym.feature.memberintelligence.domain.engine.buildCombinedDays
import com.example.badnewgym.feature.memberintelligence.domain.engine.chartCeilingMinutes
import com.example.badnewgym.feature.memberintelligence.domain.engine.lowestRecordedMinutes
import com.example.badnewgym.feature.memberintelligence.domain.engine.peakRecordedMinutes
import com.example.badnewgym.feature.memberintelligence.domain.format.MoneyFormat
import com.example.badnewgym.feature.memberintelligence.domain.model.AttendanceDayStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.BodyMeasurement
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentTimeliness
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDayStatus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.floor

/**
 * Colour system for the full Member Intelligence dashboard.
 *
 * Kept beside [CardPalette] rather than replacing it: the compact card is still used by
 * the debug component gallery, while this palette describes the mobile screen.
 */
object DashPalette {
    val Ink = Color(0xFF0B1633)
    val Muted = Color(0xFF5B6B8C)
    val Hair = Color(0xFFE6EDF7)
    val White = Color(0xFFFFFFFF)

    val Mint = Color(0xFF12B76A)
    val MintDeep = Color(0xFF079455)
    val MintSoft = Color(0xFFE7FAF0)
    val Teal = Color(0xFF0FB5A0)

    val Cobalt = Color(0xFF2E7DF6)
    val CobaltSoft = Color(0xFFEAF2FE)

    val Purple = Color(0xFF7C5CE6)
    val PurpleSoft = Color(0xFFF1ECFE)

    val Pink = Color(0xFFF0507A)
    val PinkSoft = Color(0xFFFFECF1)
    val Coral = Color(0xFFEF4E5E)

    val Amber = Color(0xFFF5A524)
    val AmberSoft = Color(0xFFFFF4E1)
    val Gold = Color(0xFFF0A512)
    val GoldSoft = Color(0xFFFFF6DE)

    val Slate = Color(0xFFA9B6CC)
    val SlateSoft = Color(0xFFF1F5FA)

    val PageTop = Color(0xFFEFF7FF)
    val PageMid = Color(0xFFF1FCF6)
    val PageBottom = Color(0xFFF7F1FF)

    val pageBrush = Brush.verticalGradient(listOf(PageTop, PageMid, PageBottom))
}

private val dateFull = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
private val dayMonth = SimpleDateFormat("d MMM", Locale.ENGLISH)
private val numberLocale = Locale("en", "IN")

private val SectionShape = RoundedCornerShape(22.dp)
private val TileShape = RoundedCornerShape(16.dp)

/**
 * The BAD GYM Member Intelligence dashboard, laid out for a phone viewport.
 *
 * Everything shown is derived from [snapshot] and [now]. Optional identity fields
 * (goal, branch) disappear when absent instead of being invented, and the payment block
 * distinguishes money owed (a real due date) from a plan renewal date.
 */
@Composable
fun MemberDashboard(
    snapshot: MemberSnapshot,
    now: Long,
    onMenu: () -> Unit = {},
    onMonth: () -> Unit = {},
    onBodyStats: () -> Unit = {},
    onActivity: () -> Unit = {},
    onPayment: () -> Unit = {},
    onReceipt: () -> Unit = {},
    onCollect: () -> Unit = {},
    onOverflow: () -> Unit = {}
) {
    val monthKey = remember(now) { DashboardMath.monthKey(now) }
    val days = remember(snapshot.attendanceHistory, snapshot.workoutHistory, monthKey, snapshot.gymSchedule) {
        buildCombinedDays(
            attendanceHistory = snapshot.attendanceHistory,
            workoutHistory = snapshot.workoutHistory,
            monthKey = monthKey,
            schedule = snapshot.gymSchedule
        )
    }
    val breakdown = remember(days) { attendanceBreakdown(days) }
    val rate = remember(days) { attendanceRateExcludingClosures(days) }
    val average = remember(days) { averageRecordedMinutes(days) }
    val peak = remember(days) { peakRecordedMinutes(days) }
    val lowest = remember(days) { lowestRecordedMinutes(days) }
    val ceiling = remember(days) { chartCeilingMinutes(days) }

    Box(
        Modifier
            .fillMaxSize()
            .background(DashPalette.pageBrush)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp)
                .padding(top = 6.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardTopBar(
                monthLabel = DashboardMath.monthLabelForKey(monthKey),
                onMenu = onMenu,
                onMonth = onMonth,
                onOverflow = onOverflow
            )
            HeroCard(snapshot, now)
            BodyStatsCard(snapshot, onBodyStats)
            ActivityCard(
                days = days,
                breakdown = breakdown,
                rate = rate,
                average = average,
                peak = peak,
                lowest = lowest,
                ceiling = ceiling,
                monthKey = monthKey,
                monthLabel = DashboardMath.monthLabelForKey(monthKey),
                onDetails = onActivity
            )
            PaymentCard(snapshot, now, onPayment, onReceipt, onCollect)
            TodayCard(snapshot, now)
        }
    }
}

// ------------------------------------------------------------------ top bar

@Composable
private fun DashboardTopBar(
    monthLabel: String,
    onMenu: () -> Unit,
    onMonth: () -> Unit,
    onOverflow: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(DashPalette.White)
                .clickable(role = Role.Button, onClickLabel = "Open navigation", onClick = onMenu),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = null, tint = DashPalette.Ink, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "BAD",
                    color = DashPalette.Ink,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.6).sp
                )
                Text(
                    "GYM",
                    color = DashPalette.MintDeep,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.6).sp
                )
            }
            Text(
                "Member Intelligence",
                color = DashPalette.Muted,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
        Row(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(DashPalette.White)
                .clickable(role = Role.Button, onClickLabel = "Change month", onClick = onMonth)
                .padding(horizontal = 9.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(Icons.Rounded.CalendarMonth, contentDescription = null, tint = DashPalette.Cobalt, modifier = Modifier.size(15.dp))
            Text(
                monthLabel,
                color = DashPalette.Ink,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null, tint = DashPalette.Muted, modifier = Modifier.size(15.dp))
        }
        Spacer(Modifier.width(6.dp))
        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(DashPalette.White)
                .clickable(role = Role.Button, onClickLabel = "More actions", onClick = onOverflow),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.MoreVert, contentDescription = null, tint = DashPalette.Ink, modifier = Modifier.size(20.dp))
        }
    }
}

// ------------------------------------------------------------------ hero

@Composable
private fun HeroCard(snapshot: MemberSnapshot, now: Long) {
    val identity = snapshot.identity
    val membership = snapshot.membership
    val daysLeft = DashboardMath.daysRemaining(membership, now)
    val totalDays = DashboardMath.planTotalDays(membership)
    val elapsed = DashboardMath.elapsedPercent(membership, now)

    Box(
        Modifier
            .fillMaxWidth()
            .shadow(8.dp, SectionShape, clip = false)
            .clip(SectionShape)
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFFFFFFF), Color(0xFFF4FBF7), Color(0xFFEFF7FF))
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.9f), SectionShape)
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarBlock(
                    name = identity.name,
                    photoUrl = identity.photoUrl,
                    tier = identity.tier,
                    active = membership != null && membership.isActive && membership.expiryDate >= now
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        identity.name,
                        color = DashPalette.Ink,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        metaLine(identity.code, identity.ageYears, identity.gender),
                        color = DashPalette.Muted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        PlanPill(membership?.planName)
                        StatusPill(membership, now)
                    }
                }
                Spacer(Modifier.width(8.dp))
                // Without a membership there is nothing to measure, so the ring is omitted
                // rather than rendered as a truthful-looking 0%.
                if (membership != null) {
                    MembershipRing(elapsed, daysLeft, totalDays)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
                MetaChip(
                    icon = Icons.Rounded.CalendarMonth,
                    tint = DashPalette.Mint,
                    soft = DashPalette.MintSoft,
                    label = "Joined",
                    value = dateFull.format(Date(identity.memberSince)),
                    sub = tenureLabel(identity.memberSince, now),
                    modifier = Modifier.weight(1.12f)
                )
                identity.goal?.let { goal ->
                    MetaChip(
                        icon = Icons.Rounded.Flag,
                        tint = DashPalette.Purple,
                        soft = DashPalette.PurpleSoft,
                        label = "Goal",
                        value = goal,
                        sub = null,
                        modifier = Modifier.weight(1f)
                    )
                }
                identity.branchName?.let { branch ->
                    MetaChip(
                        icon = Icons.Rounded.LocationOn,
                        tint = DashPalette.Coral,
                        soft = DashPalette.PinkSoft,
                        label = "Branch",
                        value = branch,
                        sub = null,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

private fun metaLine(code: String?, age: Int?, gender: String?): String {
    val parts = buildList {
        code?.takeIf { it.isNotBlank() }?.let { add(it) }
        age?.let { add("$it yrs") }
        gender?.takeIf { it.isNotBlank() }?.let { add(it) }
    }
    return if (parts.isEmpty()) "Member" else parts.joinToString("  |  ")
}

@Composable
private fun AvatarBlock(name: String, photoUrl: String?, tier: MembershipTier?, active: Boolean) {
    Box(Modifier.size(88.dp)) {
        Box(
            Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    Brush.sweepGradient(
                        listOf(DashPalette.Mint, DashPalette.Teal, DashPalette.Cobalt, DashPalette.Mint)
                    )
                )
                .padding(3.dp)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFFEFF3F8)),
                contentAlignment = Alignment.Center
            ) {
                // Initial stays underneath: a missing or failed photo still reads as a member.
                Text(
                    name.trim().take(1).uppercase(Locale.getDefault()).ifEmpty { "?" },
                    color = DashPalette.Muted,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black
                )
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Photo of $name",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        // Crown marks a premium-tier member; the dot marks a live membership. Both are
        // driven by data so a lapsed member never carries an "active" indicator.
        if (tier == MembershipTier.PREMIUM || tier == MembershipTier.VIP) {
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(DashPalette.Gold)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
            }
        }
        if (active) {
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(DashPalette.Mint)
                    .border(3.dp, Color.White, CircleShape)
            )
        }
    }
}

@Composable
private fun PlanPill(planName: String?) {
    if (planName.isNullOrBlank()) return
    Row(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(Brush.horizontalGradient(listOf(DashPalette.GoldSoft, Color(0xFFFFEFC2))))
            .border(1.dp, DashPalette.Gold.copy(alpha = 0.45f), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(Icons.Rounded.Bolt, contentDescription = null, tint = DashPalette.Gold, modifier = Modifier.size(13.dp))
        Text(
            planName,
            color = Color(0xFF9A6503),
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatusPill(membership: MembershipStatus?, now: Long) {
    // An expiry date that has passed outranks a stale isActive flag, so the hero can never
    // claim "Active" while the payment block says "Expired".
    val lapsed = membership != null && membership.expiryDate < now
    val (label, fg, bg) = when {
        membership == null -> Triple("No plan", DashPalette.Muted, DashPalette.SlateSoft)
        membership.lifecycle == MembershipLifecycle.CANCELLED ->
            Triple("Cancelled", DashPalette.Muted, DashPalette.SlateSoft)
        membership.lifecycle == MembershipLifecycle.FROZEN ->
            Triple("Frozen", Color(0xFF2E6BE6), DashPalette.CobaltSoft)
        membership.lifecycle == MembershipLifecycle.EXPIRED || lapsed ->
            Triple("Expired", DashPalette.Coral, Color(0xFFFFECEE))
        membership.lifecycle == MembershipLifecycle.PENDING ->
            Triple("Pending", Color(0xFF9A6503), DashPalette.GoldSoft)
        membership.lifecycle == MembershipLifecycle.EXPIRING ->
            Triple("Expiring", Color(0xFF9A6503), DashPalette.GoldSoft)
        membership.isActive -> Triple("Active", Color.White, DashPalette.Mint)
        else -> Triple("Inactive", DashPalette.Muted, DashPalette.SlateSoft)
    }
    Row(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(Icons.Rounded.Check, contentDescription = null, tint = fg, modifier = Modifier.size(13.dp))
        Text(label, color = fg, fontSize = 11.sp, fontWeight = FontWeight.Black, maxLines = 1)
    }
}

@Composable
private fun MembershipRing(elapsedPercent: Int, daysLeft: Int, totalDays: Int) {
    Box(Modifier.size(92.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 9.dp.toPx()
            val inset = stroke / 2f
            val arcSize = Size(size.width - stroke, size.height - stroke)
            drawArc(
                color = Color(0xFFE7F1EA),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(DashPalette.Mint, DashPalette.Teal, DashPalette.Cobalt, DashPalette.Mint)
                ),
                startAngle = -90f,
                sweepAngle = 360f * (elapsedPercent.coerceIn(0, 100) / 100f),
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "$elapsedPercent%",
                color = DashPalette.Ink,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            if (totalDays > 0) {
                Text(
                    "$daysLeft / $totalDays",
                    color = DashPalette.Muted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
            Text(
                "Days Left",
                color = DashPalette.Muted,
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun MetaChip(
    icon: ImageVector,
    tint: Color,
    soft: Color,
    label: String,
    value: String,
    sub: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .clip(TileShape)
            .background(Color.White.copy(alpha = 0.92f))
            .border(1.dp, DashPalette.Hair, TileShape)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(soft),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(15.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(label, color = DashPalette.Muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(
                value,
                color = DashPalette.Ink,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (sub != null) {
                Text(sub, color = DashPalette.Muted, fontSize = 8.5.sp, maxLines = 1)
            }
        }
    }
}

// ------------------------------------------------------------------ body stats

@Composable
private fun BodyStatsCard(snapshot: MemberSnapshot, onDetails: () -> Unit) {
    val history = remember(snapshot.bodyProgress) { snapshot.bodyProgress.sortedBy { it.date } }
    val latest = history.lastOrNull()
    val previous = history.dropLast(1).lastOrNull()
    val goal = snapshot.identity.goal

    Column(
        Modifier
            .fillMaxWidth()
            .shadow(8.dp, SectionShape, clip = false)
            .clip(SectionShape)
            .background(Color.White)
            .border(1.dp, Color.White, SectionShape)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SectionHeader(
            icon = Icons.Rounded.FitnessCenter,
            tint = DashPalette.Purple,
            soft = DashPalette.PurpleSoft,
            title = "Body Stats",
            subtitle = "Latest vs Previous",
            action = "View Details",
            onAction = onDetails
        )
        if (latest == null) {
            Text(
                "No measurements recorded yet",
                color = DashPalette.Muted,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            return@Column
        }
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
            BodyTile(
                value = formatNumber(latest.weightKg),
                unit = "kg",
                label = "Weight",
                tint = DashPalette.Mint,
                soft = DashPalette.MintSoft,
                icon = Icons.Rounded.MonitorWeight,
                delta = deltaOf("Weight", goal, latest.weightKg, previous?.weightKg),
                spark = history.mapNotNull { it.weightKg },
                footer = null,
                onClick = onDetails,
                modifier = Modifier.weight(1f)
            )
            BodyTile(
                value = formatNumber(latest.muscleKg),
                unit = "kg",
                label = "Muscle",
                tint = DashPalette.Purple,
                soft = DashPalette.PurpleSoft,
                icon = Icons.Rounded.FitnessCenter,
                delta = deltaOf("Muscle", goal, latest.muscleKg, previous?.muscleKg),
                spark = history.mapNotNull { it.muscleKg },
                footer = null,
                onClick = onDetails,
                modifier = Modifier.weight(1f)
            )
            BodyTile(
                value = formatNumber(latest.bodyFatPercent),
                unit = "%",
                label = "Body Fat",
                tint = DashPalette.Pink,
                soft = DashPalette.PinkSoft,
                icon = Icons.Rounded.WaterDrop,
                delta = deltaOf("Body Fat", goal, latest.bodyFatPercent, previous?.bodyFatPercent),
                spark = history.mapNotNull { it.bodyFatPercent },
                footer = null,
                onClick = onDetails,
                modifier = Modifier.weight(1f)
            )
            BodyTile(
                value = formatBmi(latest.bmi),
                unit = null,
                label = "BMI",
                tint = DashPalette.Cobalt,
                soft = DashPalette.CobaltSoft,
                icon = Icons.Rounded.Person,
                delta = null,
                spark = emptyList(),
                footer = DashboardMath.bmiBand(latest.bmi),
                onClick = onDetails,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private data class Delta(val text: String, val isDown: Boolean, val favorable: Boolean?)

private fun deltaOf(metric: String, goal: String?, latest: Double?, previous: Double?): Delta? {
    if (latest == null || previous == null) return null
    val change = latest - previous
    if (change == 0.0) return null
    val direction = DashboardMath.deltaDirection(metric, goal, change)
    return Delta(
        text = String.format(numberLocale, "%.1f", abs(change)),
        isDown = change < 0,
        favorable = when (direction) {
            DashboardMath.DeltaDirection.FAVORABLE -> true
            DashboardMath.DeltaDirection.UNFAVORABLE -> false
            DashboardMath.DeltaDirection.NEUTRAL -> null
        }
    )
}

@Composable
private fun BodyTile(
    value: String,
    unit: String?,
    label: String,
    tint: Color,
    soft: Color,
    icon: ImageVector,
    delta: Delta?,
    spark: List<Double>,
    footer: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clip(TileShape)
            .background(tint.copy(alpha = 0.045f))
            .border(1.dp, tint.copy(alpha = 0.16f), TileShape)
            .clickable(role = Role.Button, onClickLabel = "Open body measurements", onClick = onClick)
            .padding(7.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(soft),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = tint.copy(alpha = 0.7f), modifier = Modifier.size(13.dp))
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                value,
                color = DashPalette.Ink,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.4).sp,
                maxLines = 1
            )
            if (unit != null) {
                Text(
                    " $unit",
                    color = DashPalette.Muted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
        Text(label, color = DashPalette.Muted, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)

        when {
            delta != null -> {
                val color = when (delta.favorable) {
                    true -> DashPalette.MintDeep
                    false -> DashPalette.Coral
                    null -> DashPalette.Muted
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (delta.isDown) Icons.Rounded.ArrowDownward else Icons.Rounded.ArrowUpward,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        "${delta.text} $unit".trim(),
                        color = color,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1
                    )
                }
                if (spark.size >= 2) {
                    Sparkline(spark.takeLast(8), tint, Modifier.fillMaxWidth().height(22.dp))
                } else {
                    Spacer(Modifier.height(22.dp))
                }
            }
            footer != null -> {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(50))
                        .background(DashPalette.MintSoft)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        footer,
                        color = DashPalette.MintDeep,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1
                    )
                }
                BmiScale(value.toDoubleOrNull())
            }
            else -> Spacer(Modifier.height(34.dp))
        }
    }
}

@Composable
private fun Sparkline(values: List<Double>, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        if (values.size < 2) return@Canvas
        val min = values.min()
        val max = values.max()
        val range = (max - min).takeIf { it > 0.0 } ?: 1.0
        val stepX = size.width / (values.size - 1)
        val padY = 3.dp.toPx()
        val usableH = (size.height - padY * 2).coerceAtLeast(1f)
        val path = Path()
        values.forEachIndexed { index, raw ->
            val x = stepX * index
            val y = padY + (1f - ((raw - min) / range).toFloat()) * usableH
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = color, style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round))
        val lastX = stepX * (values.size - 1)
        val lastY = padY + (1f - ((values.last() - min) / range).toFloat()) * usableH
        drawCircle(color = color, radius = 2.6.dp.toPx(), center = Offset(lastX, lastY))
        drawCircle(color = Color.White, radius = 1.1.dp.toPx(), center = Offset(lastX, lastY))
    }
}

@Composable
private fun BmiScale(bmi: Double?) {
    val fraction = when {
        bmi == null -> null
        else -> ((bmi - 15.0) / 20.0).coerceIn(0.0, 1.0).toFloat()
    }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Canvas(Modifier.fillMaxWidth().height(8.dp)) {
            val barH = 5.dp.toPx()
            val top = (size.height - barH) / 2f
            val radius = CornerRadius(barH / 2f)
            // Fixed WHO band proportions across the 15–35 BMI window.
            val bands = listOf(
                15.0 to 18.5 to Color(0xFF7CC7F8),
                18.5 to 25.0 to DashPalette.Mint,
                25.0 to 30.0 to DashPalette.Amber,
                30.0 to 35.0 to DashPalette.Coral
            )
            bands.forEach { (range, color) ->
                val start = ((range.first - 15.0) / 20.0).toFloat() * size.width
                val end = ((range.second - 15.0) / 20.0).toFloat() * size.width
                drawRoundRect(
                    color = color,
                    topLeft = Offset(start, top),
                    size = Size((end - start).coerceAtLeast(0.5f), barH),
                    cornerRadius = radius
                )
            }
            if (fraction != null) {
                drawCircle(
                    color = Color.White,
                    radius = 5.dp.toPx(),
                    center = Offset(fraction * size.width, size.height / 2f)
                )
                drawCircle(
                    color = DashPalette.Cobalt,
                    radius = 3.4.dp.toPx(),
                    center = Offset(fraction * size.width, size.height / 2f)
                )
            }
        }
        Text("18.5 – 24.9", color = DashPalette.Muted, fontSize = 8.sp, maxLines = 1)
    }
}

// ------------------------------------------------------------------ activity

@Composable
private fun ActivityCard(
    days: List<CombinedDay>,
    breakdown: AttendanceBreakdown,
    rate: Int?,
    average: Int?,
    peak: Pair<Int, Int>?,
    lowest: Pair<Int, Int>?,
    ceiling: Int,
    monthKey: Long,
    monthLabel: String,
    onDetails: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .shadow(8.dp, SectionShape, clip = false)
            .clip(SectionShape)
            .background(Color.White)
            .border(1.dp, Color.White, SectionShape)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SectionHeader(
            icon = Icons.Rounded.BarChart,
            tint = DashPalette.Cobalt,
            soft = DashPalette.CobaltSoft,
            title = "Workout Trend & Attendance",
            subtitle = null,
            action = "Details",
            onAction = onDetails
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                Modifier
                    .clip(RoundedCornerShape(50))
                    .background(DashPalette.CobaltSoft)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(Icons.Rounded.CalendarMonth, contentDescription = null, tint = DashPalette.Cobalt, modifier = Modifier.size(12.dp))
                Text(monthLabel, color = DashPalette.Cobalt, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.weight(1f))
            Text(
                rate?.let { "Attendance $it%" } ?: "Attendance —",
                color = DashPalette.MintDeep,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Black
            )
        }

        WorkoutChart(days, ceiling, average, peak, lowest, monthKey)

        ActivityStats(breakdown, average, peak, lowest)
    }
}

@Composable
private fun WorkoutChart(
    days: List<CombinedDay>,
    ceiling: Int,
    average: Int?,
    peak: Pair<Int, Int>?,
    lowest: Pair<Int, Int>?,
    monthKey: Long
) {
    fun dateLabel(dayOfMonth: Int): String =
        dayMonth.format(Date(DashboardMath.dateForMonthDay(monthKey, dayOfMonth)))

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        // Callouts summarise the same series the bars encode; they are derived, not decorative.
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Callout(
                icon = Icons.Rounded.EmojiEvents,
                tint = DashPalette.Gold,
                title = peak?.let { "Peak ${it.first}m" } ?: "Peak —",
                sub = peak?.let { dateLabel(it.second) },
                modifier = Modifier.weight(1f)
            )
            Callout(
                icon = Icons.Rounded.AccessTime,
                tint = DashPalette.Cobalt,
                title = average?.let { "Avg ${it}m" } ?: "Avg —",
                sub = null,
                modifier = Modifier.weight(0.8f)
            )
            Callout(
                icon = Icons.Rounded.ArrowDownward,
                tint = DashPalette.Coral,
                title = lowest?.let { "Low ${it.first}m" } ?: "Low —",
                sub = lowest?.let { dateLabel(it.second) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(Modifier.fillMaxWidth()) {
            Column(
                Modifier.width(26.dp).height(132.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                listOf(ceiling, ceiling * 3 / 4, ceiling / 2, ceiling / 4, 0).forEach {
                    Text("${it}m", color = DashPalette.Muted, fontSize = 8.sp, maxLines = 1)
                }
            }
            Spacer(Modifier.width(5.dp))
            Canvas(
                Modifier
                    .weight(1f)
                    .height(132.dp)
            ) {
                val plotBottom = size.height - 6.dp.toPx()
                val plotHeight = plotBottom
                val count = days.size.coerceAtLeast(1)
                val colWidth = size.width / count

                // Gridlines at 0 / 25 / 50 / 75 / 100%.
                listOf(0f, 0.25f, 0.5f, 0.75f, 1f).forEach { fraction ->
                    val y = plotBottom - plotHeight * fraction
                    drawLine(
                        color = Color(0xFFEDF2F8),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                days.forEachIndexed { index, day ->
                    val centerX = colWidth * (index + 0.5f)
                    val barWidth = (colWidth * 0.62f).coerceAtLeast(1.5f)
                    val minutes = day.workoutMinutes

                    if (day.hasRecordedWorkout && minutes != null) {
                        val height = (minutes.toFloat() / ceiling.toFloat()).coerceIn(0f, 1f) * plotHeight
                        drawRoundRect(
                            color = bandColor(minutes),
                            topLeft = Offset(centerX - barWidth / 2f, plotBottom - height),
                            size = Size(barWidth, height.coerceAtLeast(2f)),
                            cornerRadius = CornerRadius(barWidth / 3f)
                        )
                    } else if (!day.isGymClosed && day.attendance == AttendanceDayStatus.ABSENT) {
                        drawRoundRect(
                            color = DashPalette.Coral.copy(alpha = 0.65f),
                            topLeft = Offset(centerX - barWidth / 2f, plotBottom - 5.dp.toPx()),
                            size = Size(barWidth, 5.dp.toPx()),
                            cornerRadius = CornerRadius(barWidth / 3f)
                        )
                    } else if (!day.isGymClosed) {
                        drawRoundRect(
                            color = DashPalette.Slate.copy(alpha = 0.55f),
                            topLeft = Offset(centerX - barWidth / 2f, plotBottom - 3.dp.toPx()),
                            size = Size(barWidth, 3.dp.toPx()),
                            cornerRadius = CornerRadius(barWidth / 3f)
                        )
                    }

                    // Attendance strip directly under the baseline: closure never reads as absence.
                    val stripColor = when {
                        day.isGymClosed -> DashPalette.Slate
                        day.attendance == AttendanceDayStatus.PRESENT -> DashPalette.Mint
                        day.attendance == AttendanceDayStatus.LATE -> DashPalette.Amber
                        day.attendance == AttendanceDayStatus.ABSENT -> DashPalette.Coral
                        else -> Color.Transparent
                    }
                    if (stripColor != Color.Transparent) {
                        drawCircle(color = stripColor, radius = 1.6.dp.toPx(), center = Offset(centerX, size.height - 1.5.dp.toPx()))
                    }
                }

                // Average line across the recorded series.
                if (average != null && average > 0) {
                    val y = plotBottom - (average.toFloat() / ceiling.toFloat()).coerceIn(0f, 1f) * plotHeight
                    drawLine(
                        color = DashPalette.Cobalt.copy(alpha = 0.55f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.2.dp.toPx(),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                            floatArrayOf(5.dp.toPx(), 4.dp.toPx())
                        )
                    )
                }
            }
        }

        // Day-of-month axis: every day is labelled, matching the reference density.
        Row(Modifier.fillMaxWidth().padding(start = 31.dp)) {
            days.forEach { day ->
                Text(
                    "${day.dayOfMonth}",
                    color = if (day.attendance == AttendanceDayStatus.UNKNOWN) DashPalette.Slate else DashPalette.Muted,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        BandLegend()
    }
}

/** Minutes → band colour. Thresholds are declared by [BandLegend]. */
private fun bandColor(minutes: Int): Color = when {
    minutes >= 45 -> DashPalette.Teal
    minutes >= 30 -> DashPalette.Cobalt
    minutes >= 15 -> DashPalette.Amber
    else -> DashPalette.Coral
}

@Composable
private fun BandLegend() {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        BandItem("High 45m+", DashPalette.Teal)
        BandItem("Good 30–45m", DashPalette.Cobalt)
        BandItem("Normal 15–30m", DashPalette.Amber)
        BandItem("Low <15m", DashPalette.Coral)
    }
}

@Composable
private fun BandItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(color))
        Text(label, color = DashPalette.Muted, fontSize = 8.sp, maxLines = 1)
    }
}

@Composable
private fun Callout(
    icon: ImageVector,
    tint: Color,
    title: String,
    sub: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(tint.copy(alpha = 0.08f))
            .border(1.dp, tint.copy(alpha = 0.18f), RoundedCornerShape(10.dp))
            .padding(horizontal = 7.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            Modifier.size(20.dp).clip(RoundedCornerShape(7.dp)).background(tint),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
        }
        Column {
            Text(title, color = DashPalette.Ink, fontSize = 10.sp, fontWeight = FontWeight.Black, maxLines = 1)
            if (sub != null) {
                Text(sub, color = DashPalette.Muted, fontSize = 8.sp, maxLines = 1)
            }
        }
    }
}

@Composable
private fun ActivityStats(
    breakdown: AttendanceBreakdown,
    average: Int?,
    peak: Pair<Int, Int>?,
    lowest: Pair<Int, Int>?
) {
    // A late arrival is still a visit, so it belongs in "Present" — otherwise the tile
    // disagrees with the attendance rate in the header. Shares use the same denominator
    // as that rate (recorded open days); closures are excluded, never counted as absences.
    val visited = breakdown.present + breakdown.late
    val recordedOpen = visited + breakdown.absent
    val presentShare = if (recordedOpen > 0) "${visited * 100 / recordedOpen}%" else null
    val absentShare = if (recordedOpen > 0) "${breakdown.absent * 100 / recordedOpen}%" else null

    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        StatTile(
            label = "Present",
            value = "$visited Days",
            share = presentShare,
            tint = DashPalette.Mint,
            modifier = Modifier.weight(1f)
        )
        StatTile(
            label = "Absent",
            value = "${breakdown.absent} Days",
            share = absentShare,
            tint = DashPalette.Coral,
            modifier = Modifier.weight(1f)
        )
        // A closure has no percentage on purpose: it is excluded from the attendance
        // denominator, so a share of recorded days would be meaningless.
        StatTile(
            label = "Gym Off",
            value = "${breakdown.off} Days",
            share = null,
            tint = DashPalette.Slate,
            modifier = Modifier.weight(1f)
        )
    }
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        StatTile("Avg", average?.let { "$it min" } ?: "—", null, DashPalette.Cobalt, Modifier.weight(1f))
        StatTile("Peak", peak?.let { "${it.first} min" } ?: "—", null, DashPalette.Gold, Modifier.weight(1f))
        StatTile("Lowest", lowest?.let { "${it.first} min" } ?: "—", null, DashPalette.Coral, Modifier.weight(1f))
    }
}

@Composable
private fun StatTile(
    label: String,
    value: String,
    share: String?,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(tint.copy(alpha = 0.06f))
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(Modifier.size(14.dp).clip(CircleShape).background(tint.copy(alpha = 0.9f)))
        Column {
            Text(label, color = DashPalette.Muted, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, color = DashPalette.Ink, fontSize = 11.sp, fontWeight = FontWeight.Black, maxLines = 1)
                if (share != null) {
                    Text(" ($share)", color = tint, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
            }
        }
    }
}

// ------------------------------------------------------------------ payment

@Composable
private fun PaymentCard(
    snapshot: MemberSnapshot,
    now: Long,
    onPayment: () -> Unit,
    onReceipt: () -> Unit,
    onCollect: () -> Unit
) {
    val payment = snapshot.payment
    val membership = snapshot.membership
    val outstanding = payment?.totalOutstanding ?: 0.0
    val due = outstanding > 0.0
    val headAmount = when {
        due -> MoneyFormat.inr(outstanding)
        payment?.lastPaymentAmount != null -> MoneyFormat.inr(payment.lastPaymentAmount)
        else -> "—"
    }
    // Timeliness is only ever the recorded verdict. Absent a record the label stays "Paid";
    // an unknown timing is never promoted to "On Time".
    val lastTimeliness = payment?.history?.maxByOrNull { it.occurredAt }?.timeliness
    val stateLabel: String
    val stateColor: Color
    when {
        due -> {
            stateLabel = "Outstanding"
            stateColor = DashPalette.Coral
        }
        payment?.lastPaymentAmount == null -> {
            stateLabel = "No payment record"
            stateColor = DashPalette.Muted
        }
        lastTimeliness == PaymentTimeliness.ON_TIME -> {
            stateLabel = "Paid · On Time"
            stateColor = DashPalette.MintDeep
        }
        lastTimeliness == PaymentTimeliness.LATE -> {
            stateLabel = "Paid · Late"
            stateColor = DashPalette.Amber
        }
        lastTimeliness == PaymentTimeliness.VERY_LATE -> {
            stateLabel = "Paid · Very Late"
            stateColor = DashPalette.Coral
        }
        else -> {
            stateLabel = "Paid"
            stateColor = DashPalette.MintDeep
        }
    }
    val dueTarget = if (due) payment?.dueDate else membership?.expiryDate
    val daysToDue = dueTarget?.let { DashboardMath.daysBetween(now, it) }
    val dueCaption = when {
        daysToDue == null -> "Not scheduled"
        due && daysToDue > 0 -> "$daysToDue Days Left"
        due && daysToDue == 0 -> "Due Today"
        due -> "${-daysToDue} Days Overdue"
        daysToDue > 0 -> "$daysToDue Days Left"
        daysToDue == 0 -> "Ends Today"
        else -> "Expired"
    }

    Column(
        Modifier
            .fillMaxWidth()
            .shadow(8.dp, SectionShape, clip = false)
            .clip(SectionShape)
            .background(Color.White)
            .border(1.dp, Color.White, SectionShape)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconTile(Icons.Rounded.ReceiptLong, DashPalette.Coral, DashPalette.PinkSoft)
                Text(
                    "Payment & Plan",
                    color = DashPalette.Ink,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            membership?.let {
                Row(
                    Modifier
                        .clip(RoundedCornerShape(50))
                        .background(DashPalette.GoldSoft)
                        .border(1.dp, DashPalette.Gold.copy(alpha = 0.4f), RoundedCornerShape(50))
                        .clickable(role = Role.Button, onClickLabel = "Open plan details", onClick = onPayment)
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        it.planName,
                        color = Color(0xFF9A6503),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1
                    )
                    Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color(0xFF9A6503), modifier = Modifier.size(13.dp))
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (due) DashPalette.Coral else DashPalette.Mint),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    headAmount,
                    color = DashPalette.Ink,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                    maxLines = 1
                )
                Text(stateLabel, color = stateColor, fontSize = 10.5.sp, fontWeight = FontWeight.Black, maxLines = 1)
                payment?.lastPaymentDate?.let {
                    Text(dateFull.format(Date(it)), color = DashPalette.Muted, fontSize = 9.sp, maxLines = 1)
                }
            }
            Box(Modifier.width(1.dp).height(38.dp).background(DashPalette.Hair))
            Spacer(Modifier.width(9.dp))
            Column(Modifier.weight(1f)) {
                Text(if (due) "Next Due" else "Plan Renewal", color = DashPalette.Muted, fontSize = 9.sp, maxLines = 1)
                Text(
                    dueTarget?.let { dateFull.format(Date(it)) } ?: "—",
                    color = DashPalette.Ink,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    dueCaption,
                    color = if ((due && (daysToDue ?: 0) < 0) || (!due && (daysToDue ?: 1) <= 0)) {
                        DashPalette.Coral
                    } else {
                        DashPalette.Cobalt
                    },
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedAction("View Receipt", Icons.Rounded.ReceiptLong, onReceipt, Modifier.weight(1f))
            FilledAction("Collect Money", Icons.Rounded.ArrowForward, onCollect, Modifier.weight(1f))
        }
    }
}

@Composable
private fun OutlinedAction(label: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.2.dp, DashPalette.Cobalt.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = DashPalette.Cobalt, modifier = Modifier.size(15.dp))
        Spacer(Modifier.width(5.dp))
        Text(label, color = DashPalette.Cobalt, fontSize = 11.5.sp, fontWeight = FontWeight.Black, maxLines = 1)
    }
}

@Composable
private fun FilledAction(label: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.horizontalGradient(listOf(DashPalette.Mint, DashPalette.MintDeep)))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
        Spacer(Modifier.width(5.dp))
        Text(label, color = Color.White, fontSize = 11.5.sp, fontWeight = FontWeight.Black, maxLines = 1)
    }
}

// ------------------------------------------------------------------ today

@Composable
private fun TodayCard(snapshot: MemberSnapshot, now: Long) {
    val todayAttendance = snapshot.attendanceHistory.lastOrNull { sameDay(it.date, now) }?.status
    val todayWorkout = snapshot.workoutHistory.lastOrNull { sameDay(it.date, now) }
        ?.takeIf { it.status == WorkoutDayStatus.RECORDED }

    Column(
        Modifier
            .fillMaxWidth()
            .shadow(8.dp, SectionShape, clip = false)
            .clip(SectionShape)
            .background(Color.White)
            .border(1.dp, Color.White, SectionShape)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconTile(Icons.Rounded.AccessTime, DashPalette.Purple, DashPalette.PurpleSoft)
            Text(
                "Today's Activity",
                color = DashPalette.Ink,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Today",
                color = DashPalette.Muted,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (todayAttendance == null && todayWorkout == null) {
            Text(
                "No activity recorded today",
                color = DashPalette.Muted,
                fontSize = 11.5.sp
            )
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
                TodayTile(
                    icon = Icons.Rounded.Login,
                    tint = DashPalette.Mint,
                    label = "Attendance",
                    value = when (todayAttendance) {
                        AttendanceDayStatus.PRESENT -> "Present"
                        AttendanceDayStatus.LATE -> "Late"
                        AttendanceDayStatus.ABSENT -> "Absent"
                        AttendanceDayStatus.HOLIDAY, AttendanceDayStatus.CLOSED -> "Gym Off"
                        else -> "—"
                    },
                    modifier = Modifier.weight(1f)
                )
                TodayTile(
                    icon = Icons.Rounded.FitnessCenter,
                    tint = DashPalette.Purple,
                    label = "Workout",
                    // A recorded zero-duration entry is not a workout, so it reads "—" here
                    // exactly as it is excluded from the chart and averages.
                    value = todayWorkout?.durationMinutes?.takeIf { it > 0 }?.let { "$it min" } ?: "—",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TodayTile(icon: ImageVector, tint: Color, label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(tint.copy(alpha = 0.07f))
            .padding(horizontal = 9.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            Modifier.size(28.dp).clip(RoundedCornerShape(9.dp)).background(tint.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(15.dp))
        }
        Column {
            Text(label, color = DashPalette.Muted, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(value, color = DashPalette.Ink, fontSize = 12.5.sp, fontWeight = FontWeight.Black, maxLines = 1)
        }
    }
}

// ------------------------------------------------------------------ shared

@Composable
private fun SectionHeader(
    icon: ImageVector,
    tint: Color,
    soft: Color,
    title: String,
    subtitle: String?,
    action: String?,
    onAction: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconTile(icon, tint, soft)
        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Text(
                title,
                color = DashPalette.Ink,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.3).sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    "  •  $subtitle",
                    color = DashPalette.Purple,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
        if (action != null) {
            Row(
                Modifier
                    .clip(RoundedCornerShape(50))
                    .background(DashPalette.CobaltSoft)
                    .clickable(role = Role.Button, onClickLabel = action, onClick = onAction)
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(action, color = DashPalette.Cobalt, fontSize = 10.sp, fontWeight = FontWeight.Black, maxLines = 1)
                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = DashPalette.Cobalt, modifier = Modifier.size(13.dp))
            }
        }
    }
}

@Composable
private fun IconTile(icon: ImageVector, tint: Color, soft: Color) {
    Box(
        Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(soft),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
    }
}

private fun formatNumber(value: Double?): String =
    value?.let { String.format(numberLocale, "%.1f", it) } ?: "—"

/**
 * BMI is truncated, not rounded: 24.96 must never display as "25.0" beside a
 * "Normal (18.5–24.9)" label, which would contradict the band it is drawn in.
 */
private fun formatBmi(value: Double?): String =
    value?.let { String.format(numberLocale, "%.1f", floor(it * 10.0) / 10.0) } ?: "—"

private fun sameDay(a: Long, b: Long): Boolean {
    val ca = Calendar.getInstance().apply { timeInMillis = a }
    val cb = Calendar.getInstance().apply { timeInMillis = b }
    return ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR) &&
        ca.get(Calendar.DAY_OF_YEAR) == cb.get(Calendar.DAY_OF_YEAR)
}

/** "2 Months 15 Days" style tenure from a join date, never negative. */
private fun tenureLabel(since: Long, now: Long): String {
    val start = Calendar.getInstance().apply { timeInMillis = since }
    val end = Calendar.getInstance().apply { timeInMillis = now }
    if (end.before(start)) return "—"
    var months = 0
    val cursor = start.clone() as Calendar
    while (true) {
        val next = cursor.clone() as Calendar
        next.add(Calendar.MONTH, 1)
        if (next.after(end)) break
        cursor.add(Calendar.MONTH, 1)
        months++
    }
    var days = 0
    val dayCursor = cursor.clone() as Calendar
    while (true) {
        val next = dayCursor.clone() as Calendar
        next.add(Calendar.DAY_OF_MONTH, 1)
        if (next.after(end)) break
        dayCursor.add(Calendar.DAY_OF_MONTH, 1)
        days++
    }
    return buildList {
        if (months > 0) add("$months ${if (months == 1) "Month" else "Months"}")
        if (days > 0 || months == 0) add("$days ${if (days == 1) "Day" else "Days"}")
    }.joinToString(" ")
}
