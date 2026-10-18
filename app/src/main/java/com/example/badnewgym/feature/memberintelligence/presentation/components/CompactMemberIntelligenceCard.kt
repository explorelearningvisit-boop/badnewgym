package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.badnewgym.feature.memberintelligence.domain.engine.DayClosure
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
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.PaymentLifecycle
import com.example.badnewgym.feature.memberintelligence.domain.model.WorkoutDayStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Semantic palette for the compact member intelligence card.
 *
 * Central mapping so a tier/status/series colour never drifts between the card and
 * the rest of the app (see blueprint §3 and prompt §7).
 */
object CardPalette {
    val Page = Color(0xFFF4FBFF)
    val Card = Color(0xFFFFFFFF)
    val Ink = Color(0xFF10224A)
    val Muted = Color(0xFF526681)
    val Hairline = Color(0xFFDCE8F2)
    val Mint = Color(0xFF0AA86B)
    val MintPale = Color(0xFFE7F8EF)
    val Coral = Color(0xFFE84B5B)
    val CoralPale = Color(0xFFFFF0F1)
    val Cobalt = Color(0xFF267DE8)
    val CobaltPale = Color(0xFFEDF5FF)
    val Purple = Color(0xFF8155E8)
    val Gold = Color(0xFFD99816)
    val GoldPale = Color(0xFFFFF7DF)
    val Amber = Color(0xFFE7A126)
    val Slate = Color(0xFFA3B2C4)

    /**
     * One central mapping from a real membership tier to its badge treatment.
     * Invented tiers are never introduced; unmapped tiers fall back to neutral mint.
     */
    fun tierStyle(tier: MembershipTier): Pair<Color, Color> = when (tier) {
        MembershipTier.PREMIUM, MembershipTier.VIP -> Gold to GoldPale
        MembershipTier.CORPORATE -> Cobalt to CobaltPale
        MembershipTier.NORMAL, MembershipTier.TRIAL -> Mint to MintPale
        MembershipTier.WALK_IN -> Slate to Color(0xFFF1F5F9)
    }

    /** Attendance status → marker colour. `UNKNOWN` is deliberately the faintest. */
    fun attendanceColor(status: AttendanceDayStatus): Color = when (status) {
        AttendanceDayStatus.PRESENT -> Mint
        AttendanceDayStatus.ABSENT -> Coral
        AttendanceDayStatus.LATE -> Amber
        AttendanceDayStatus.HOLIDAY, AttendanceDayStatus.CLOSED -> Slate
        AttendanceDayStatus.UNKNOWN -> Color(0xFFE2E8F0)
    }
}

private val CardShape = RoundedCornerShape(13.dp)
private val SectionShape = RoundedCornerShape(8.dp)

private val dayLabelFormat = SimpleDateFormat("d MMM", Locale.ENGLISH)
private val shortDateFormat = SimpleDateFormat("d MMM", Locale.ENGLISH)

/**
 * The approved BAD GYM Member Intelligence card.
 *
 * A single self-contained card constrained to **200dp x 300dp**. Every value is
 * derived from [snapshot]; nothing is hardcoded. Tapping a section header asks the
 * host to open the existing detail flow — the card itself never fabricates data and
 * never renders a dead control.
 */
@Composable
fun CompactMemberIntelligenceCard(
    snapshot: MemberSnapshot,
    modifier: Modifier = Modifier,
    now: Long = System.currentTimeMillis(),
    onOpenBodyStats: () -> Unit = {},
    onOpenPayment: () -> Unit = {},
    onOpenActivity: () -> Unit = {},
    onOpenOverflow: () -> Unit = {}
) {
    val monthKey = remember(now) { DashboardMath.monthKey(now) }

    Box(
        modifier
            .width(CARD_WIDTH)
            .height(CARD_HEIGHT)
            .clip(CardShape)
            .background(CardPalette.Card)
            .border(1.dp, CardPalette.Mint.copy(alpha = 0.35f), CardShape)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            CardBrandStrip(onOpenOverflow)
            MemberIdentityBlock(snapshot, now)
            CardSectionBox {
                CardSectionHeader(
                    icon = Icons.Rounded.Person,
                    iconTint = CardPalette.Mint,
                    iconPale = CardPalette.MintPale,
                    title = "Body Stats",
                    onClick = onOpenBodyStats
                )
                BodyStatsRow(snapshot)
            }
            CardSectionBox {
                CardSectionHeader(
                    icon = Icons.Rounded.AccountBalanceWallet,
                    iconTint = CardPalette.Coral,
                    iconPale = CardPalette.CoralPale,
                    title = "Payment & Plan",
                    onClick = onOpenPayment
                )
                PaymentPlanRow(snapshot, now)
            }
            CardSectionBox(Modifier.weight(1f)) {
                CardSectionHeader(
                    icon = Icons.Rounded.FitnessCenter,
                    iconTint = CardPalette.Cobalt,
                    iconPale = CardPalette.CobaltPale,
                    title = "Attendance & Workout",
                    trailing = DashboardMath.monthLabelForKey(monthKey),
                    onClick = onOpenActivity
                )
                ActivityBlock(snapshot, monthKey)
            }
        }
    }
}

/** Locked card geometry. Measured in dp; physical pixels depend on device density. */
val CARD_WIDTH = 200.dp
val CARD_HEIGHT = 300.dp

// ------------------------------------------------------------------ brand strip

@Composable
private fun CardBrandStrip(onOverflow: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "BAD",
                color = CardPalette.Mint,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.4).sp
            )
            Text(
                "GYM",
                color = CardPalette.Ink,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.4).sp
            )
        }
        Text(
            "MEMBER",
            color = CardPalette.Cobalt,
            fontSize = 5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(start = 3.dp)
        )
        Spacer(Modifier.width(4.dp))
        Box(
            Modifier
                .size(15.dp)
                .clip(CircleShape)
                .background(CardPalette.CobaltPale)
                .clickable(onClick = onOverflow),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.MoreVert,
                contentDescription = "More member actions",
                tint = CardPalette.Ink,
                modifier = Modifier.size(9.dp)
            )
        }
    }
}

// ------------------------------------------------------------------ identity

@Composable
private fun MemberIdentityBlock(snapshot: MemberSnapshot, now: Long) {
    val identity = snapshot.identity
    val membership = snapshot.membership
    val elapsed = DashboardMath.elapsedPercent(membership, now)
    val daysLeft = DashboardMath.daysRemaining(membership, now)
    val (tierFg, tierBg) = CardPalette.tierStyle(identity.tier)

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        // Portrait with crown badge and online dot.
        Box(Modifier.size(38.dp)) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(CardPalette.Mint, CardPalette.Cobalt, CardPalette.Gold)
                        )
                    )
                    .padding(1.6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFFEFF3F8)),
                    contentAlignment = Alignment.Center
                ) {
                    if (identity.photoUrl != null) {
                        AsyncImage(
                            model = identity.photoUrl,
                            contentDescription = "Photo of ${identity.name}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            identity.name.take(1).uppercase(Locale.getDefault()),
                            color = CardPalette.Muted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
            if (identity.isVerified) {
                Box(
                    Modifier
                        .size(11.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(CardPalette.Gold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Bolt,
                        contentDescription = "Verified member",
                        tint = Color.White,
                        modifier = Modifier.size(7.dp)
                    )
                }
            }
            if (membership?.isActive == true) {
                Box(
                    Modifier
                        .size(9.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(CardPalette.Mint)
                        .border(1.dp, CardPalette.Card, CircleShape)
                )
            }
        }

        Spacer(Modifier.width(5.dp))

        Column(Modifier.weight(1f)) {
            Text(
                identity.name,
                color = CardPalette.Ink,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val meta = buildList {
                identity.code?.let { add(it) }
                identity.ageYears?.let { add("$it yrs") }
                identity.gender?.let { add(it) }
            }.joinToString(" | ")
            if (meta.isNotEmpty()) {
                Text(
                    meta,
                    color = CardPalette.Cobalt,
                    fontSize = 5.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                MiniBadge(
                    icon = Icons.Rounded.Bolt,
                    label = membership?.planName?.substringBefore(" Plan") ?: tierLabel(identity.tier),
                    fg = tierFg,
                    bg = tierBg,
                    modifier = Modifier.weight(1f, fill = false)
                )
                MembershipStatusBadge(membership?.lifecycle, membership?.isActive == true)
            }
        }

        Spacer(Modifier.width(4.dp))

        MembershipRing(elapsed, daysLeft, DashboardMath.planTotalDays(membership))
    }
}

private fun tierLabel(tier: MembershipTier): String = when (tier) {
    MembershipTier.PREMIUM -> "Premium"
    MembershipTier.VIP -> "VIP"
    MembershipTier.CORPORATE -> "Corporate"
    MembershipTier.NORMAL -> "Normal"
    MembershipTier.TRIAL -> "Trial"
    MembershipTier.WALK_IN -> "Walk-in"
}

@Composable
private fun MiniBadge(
    icon: ImageVector,
    label: String,
    fg: Color,
    bg: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 3.dp, vertical = 1.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.5.dp)
    ) {
        Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(6.dp))
        Text(
            label,
            color = fg,
            fontSize = 6.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun MembershipStatusBadge(lifecycle: MembershipLifecycle?, isActive: Boolean) {
    val (label, fg, bg) = when {
        isActive && lifecycle == MembershipLifecycle.EXPIRING -> Triple("Expiring", CardPalette.Amber, Color(0xFFFFF6E3))
        isActive -> Triple("Active", CardPalette.Mint, CardPalette.MintPale)
        else -> Triple(lifecycle?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Inactive", CardPalette.Coral, CardPalette.CoralPale)
    }
    Row(
        Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 3.dp, vertical = 1.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.5.dp)
    ) {
        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = fg, modifier = Modifier.size(6.dp))
        Text(label, color = fg, fontSize = 6.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun MembershipRing(elapsedPercent: Int?, daysLeft: Int?, totalDays: Int?) {
    val pct = elapsedPercent?.coerceIn(0, 100) ?: 0
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(36.dp)) {
        Box(Modifier.size(30.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val stroke = 3.dp.toPx()
                val inset = stroke / 2
                val arcSize = size.width - inset * 2
                drawArc(
                    color = Color(0xFFE7EEF7),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = Size(arcSize, arcSize),
                    style = Stroke(stroke, cap = StrokeCap.Round)
                )
                if (pct > 0) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(CardPalette.Mint, CardPalette.Cobalt, CardPalette.Mint)
                        ),
                        startAngle = -90f,
                        sweepAngle = 360f * (pct / 100f),
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = Size(arcSize, arcSize),
                        style = Stroke(stroke, cap = StrokeCap.Round)
                    )
                }
            }
            Text(
                elapsedPercent?.let { "$it%" } ?: "—",
                color = CardPalette.Ink,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black
            )
        }
        // Days caption only when both the remaining and total span are known.
        Text(
            if (daysLeft != null && totalDays != null && totalDays > 0) "$daysLeft / $totalDays" else "—",
            color = CardPalette.Ink,
            fontSize = 5.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            "Days",
            color = CardPalette.Muted,
            fontSize = 4.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

// ------------------------------------------------------------------ body stats

@Composable
private fun BodyStatsRow(snapshot: MemberSnapshot) {
    val history = remember(snapshot.bodyProgress) { snapshot.bodyProgress.sortedBy { it.date } }
    val latest = history.lastOrNull()
    val previous = history.dropLast(1).lastOrNull()

    if (latest == null) {
        Text(
            "No measurements recorded",
            color = CardPalette.Muted,
            fontSize = 5.5.sp,
            maxLines = 1
        )
        return
    }

    Row(horizontalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.fillMaxWidth()) {
        MetricTile(
            value = formatNumber(latest.weightKg),
            unit = "kg",
            delta = change(latest.weightKg, previous?.weightKg),
            label = "Weight",
            tint = CardPalette.Mint,
            modifier = Modifier.weight(1f)
        )
        MetricTile(
            value = formatNumber(latest.muscleKg),
            unit = "kg",
            delta = change(latest.muscleKg, previous?.muscleKg),
            label = "Muscle",
            tint = CardPalette.Purple,
            modifier = Modifier.weight(1f)
        )
        MetricTile(
            value = formatNumber(latest.bodyFatPercent),
            unit = "%",
            delta = change(latest.bodyFatPercent, previous?.bodyFatPercent),
            label = "Body Fat",
            tint = CardPalette.Coral,
            modifier = Modifier.weight(1f)
        )
        MetricTile(
            value = formatNumber(latest.bmi),
            unit = null,
            delta = null,
            band = DashboardMath.bmiBand(latest.bmi),
            label = "BMI",
            tint = CardPalette.Cobalt,
            modifier = Modifier.weight(1f)
        )
    }
}

private data class MetricDelta(val text: String, val isDown: Boolean)

private fun change(latest: Double?, previous: Double?): MetricDelta? {
    if (latest == null || previous == null) return null
    val delta = latest - previous
    if (delta == 0.0) return null
    return MetricDelta(String.format(Locale.getDefault(), "%.1f", kotlin.math.abs(delta)), delta < 0)
}

private fun formatNumber(value: Double?): String =
    value?.let { String.format(Locale.getDefault(), "%.1f", it) } ?: "—"

@Composable
private fun MetricTile(
    value: String,
    unit: String?,
    delta: MetricDelta?,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier,
    band: String? = null
) {
    Column(
        modifier
            .clip(RoundedCornerShape(6.dp))
            .background(CardPalette.Card)
            .border(1.dp, tint.copy(alpha = 0.22f), RoundedCornerShape(6.dp))
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .defaultMinSize(minHeight = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.5.dp)
    ) {
        Box(
            Modifier
                .size(9.dp)
                .clip(RoundedCornerShape(2.5.dp))
                .background(tint),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label.take(2).uppercase(Locale.getDefault()),
                color = Color.White,
                fontSize = 3.5.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                value,
                color = CardPalette.Ink,
                fontSize = 7.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )
            if (unit != null) {
                Text(
                    unit,
                    color = CardPalette.Muted,
                    fontSize = 4.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 0.5.dp, bottom = 0.5.dp)
                )
            }
        }
        if (delta != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (delta.isDown) Icons.Rounded.ArrowDownward else Icons.Rounded.ArrowUpward,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(5.dp)
                )
                Text(
                    delta.text,
                    color = tint,
                    fontSize = 4.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        } else if (band != null) {
            Text(band, color = tint, fontSize = 4.5.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
        Text(
            label,
            color = CardPalette.Muted,
            fontSize = 4.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ------------------------------------------------------------------ payment

@Composable
private fun PaymentPlanRow(snapshot: MemberSnapshot, now: Long) {
    val payment = snapshot.payment
    val membership = snapshot.membership
    val outstanding = payment?.totalOutstanding ?: 0.0
    val due = outstanding > 0.0
    val lifecycle = payment?.lifecycle
    val (stateLabel, stateFg) = when {
        due && lifecycle == PaymentLifecycle.OVERDUE -> "Overdue" to CardPalette.Coral
        due -> "Payment Due" to CardPalette.Coral
        lifecycle == PaymentLifecycle.FAILED -> "Failed" to CardPalette.Coral
        lifecycle == PaymentLifecycle.PAID || outstanding <= 0.0 -> "Paid · On Time" to CardPalette.Mint
        else -> "Unknown" to CardPalette.Muted
    }
    val headAmount = if (due) MoneyFormat.inr(outstanding) else payment?.lastPaymentAmount?.let { MoneyFormat.inr(it) } ?: "—"
    val dueTarget = if (due) payment?.dueDate else membership?.expiryDate
    val daysToDue = dueTarget?.let { DashboardMath.daysBetween(now, it) }
    val (tierFg, tierBg) = CardPalette.tierStyle(snapshot.identity.tier)

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .size(17.dp)
                .clip(CircleShape)
                .background(if (due) CardPalette.Coral else CardPalette.Mint),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (due) Icons.Rounded.ErrorOutline else Icons.Rounded.CheckCircle,
                contentDescription = stateLabel,
                tint = Color.White,
                modifier = Modifier.size(11.dp)
            )
        }
        Spacer(Modifier.width(3.dp))
        Column(Modifier.weight(1f)) {
            Text(
                headAmount,
                color = CardPalette.Ink,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                stateLabel,
                color = stateFg,
                fontSize = 5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(Modifier.width(0.8.dp).height(16.dp).background(CardPalette.Hairline))
        Spacer(Modifier.width(3.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "Next Due",
                color = CardPalette.Muted,
                fontSize = 4.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                dueTarget?.let { dayLabelFormat.format(Date(it)) } ?: "Unknown",
                color = CardPalette.Ink,
                fontSize = 7.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                daysToDue?.let { days ->
                    when {
                        days > 0 -> "$days Days Left"
                        days == 0 -> "Due Today"
                        else -> "${-days} Days Overdue"
                    }
                } ?: "Not scheduled",
                color = if ((daysToDue ?: 1) < 0) CardPalette.Coral else CardPalette.Cobalt,
                fontSize = 4.5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        MiniBadge(
            icon = Icons.Rounded.Bolt,
            label = tierLabel(snapshot.identity.tier),
            fg = tierFg,
            bg = tierBg
        )
    }
}

// ------------------------------------------------------------------ attendance + workout

@Composable
private fun ActivityBlock(snapshot: MemberSnapshot, monthKey: Long) {
    val days = remember(snapshot.attendanceHistory, snapshot.workoutHistory, monthKey, snapshot.gymSchedule) {
        buildCombinedDays(
            snapshot.attendanceHistory,
            snapshot.workoutHistory,
            monthKey,
            snapshot.gymSchedule
        )
    }
    val breakdown = remember(days) { attendanceBreakdown(days) }
    val rate = remember(days) { attendanceRateExcludingClosures(days) }
    val average = remember(days) { averageRecordedMinutes(days) }
    val peak = remember(days) { peakRecordedMinutes(days) }
    val lowest = remember(days) { lowestRecordedMinutes(days) }
    val ceiling = remember(days) { chartCeilingMinutes(days) }

    Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.fillMaxWidth()) {
        ActivityLegend()
        CombinedActivityChart(days, ceiling)
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.fillMaxWidth()) {
            SummaryPill(
                icon = Icons.Rounded.AccessTime,
                tint = CardPalette.Mint,
                value = average?.let { "$it m" } ?: "—",
                label = "Avg / Day",
                modifier = Modifier.weight(1f)
            )
            SummaryPill(
                icon = Icons.Rounded.ArrowUpward,
                tint = CardPalette.Mint,
                value = peak?.let { "${it.first} m" } ?: "—",
                label = peak?.let { "Peak (${dayLabelFormat.format(Date(DashboardMath.dateForMonthDay(monthKey, it.second)))})" },
                modifier = Modifier.weight(1f)
            )
            SummaryPill(
                icon = Icons.Rounded.ArrowDownward,
                tint = CardPalette.Coral,
                value = lowest?.let { "${it.first} m" } ?: "—",
                label = lowest?.let { "Low (${dayLabelFormat.format(Date(DashboardMath.dateForMonthDay(monthKey, it.second)))})" },
                modifier = Modifier.weight(1f)
            )
        }
        AttendanceSummaryRow(breakdown, rate)
    }
}

/**
 * Persistent legend. Attendance (status markers) and workout (minute bars) are
 * visually distinct encodings, never two similar series in the same colour.
 */
@Composable
private fun ActivityLegend() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        LegendDot("Present", CardPalette.Mint)
        LegendDot("Absent", CardPalette.Coral)
        LegendDot("Off", CardPalette.Slate)
        LegendDot("Workout", CardPalette.Cobalt)
    }
}

@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(1.dp)) {
        Box(Modifier.size(3.5.dp).clip(CircleShape).background(color))
        Text(label, color = CardPalette.Muted, fontSize = 4.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}

/**
 * Combined attendance + workout chart.
 *
 * Two clearly separated encodings: **blue vertical bars** scale to recorded workout
 * minutes (0m/30m/60m axis), while a **status strip** along the baseline marks each
 * day's attendance. Gym-closed days get a pencil/closure marker and are excluded from
 * both attendance rate and workout averages, so a closure never reads as inactivity.
 */
@Composable
private fun CombinedActivityChart(
    days: List<com.example.badnewgym.feature.memberintelligence.domain.engine.CombinedDay>,
    ceilingMinutes: Int
) {
    val barColor = CardPalette.Cobalt
    val gridColor = Color(0xFFEDF2F8)
    val bandHeight = 2.6.dp
    val labelCount = 7

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().height(40.dp)) {
            // Y axis labels.
            Column(
                Modifier
                    .width(11.dp)
                    .height(34.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                listOf("${ceilingMinutes}m", "${ceilingMinutes / 2}m", "0m").forEach {
                    Text(it, color = CardPalette.Muted, fontSize = 3.5.sp, maxLines = 1)
                }
            }
            Canvas(
                Modifier
                    .weight(1f)
                    .height(34.dp)
                    .semantics {
                        contentDescription =
                            "Attendance and workout chart: blue bars are recorded workout minutes, the strip marks daily attendance"
                    }
            ) {
                val plotHeight = size.height - bandHeight.toPx()
                val dayCount = days.size.coerceAtLeast(1)
                val colWidth = size.width / dayCount

                // Horizontal gridlines at 0, mid and ceiling.
                listOf(0f, 0.5f, 1f).forEach { fraction ->
                    val y = plotHeight * (1f - fraction)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 0.6.dp.toPx()
                    )
                }

                days.forEachIndexed { index, day ->
                    val centerX = colWidth * (index + 0.5f)

                    // Attendance status strip on the baseline.
                    val stripTop = size.height - bandHeight.toPx()
                    drawRoundRect(
                        color = CardPalette.attendanceColor(day.attendance),
                        topLeft = Offset(colWidth * index + colWidth * 0.12f, stripTop),
                        size = Size(colWidth * 0.76f, bandHeight.toPx()),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(0.8.dp.toPx())
                    )

                    // Closure marker: a small pencil/slash glyph, only on gym-off days.
                    if (day.isGymClosed) {
                        val markerTop = stripTop - 5.5.dp.toPx()
                        drawRoundRect(
                            color = Color(0xFFCBD5E1),
                            topLeft = Offset(centerX - 2.4.dp.toPx(), markerTop),
                            size = Size(4.8.dp.toPx(), 5.5.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(0.7.dp.toPx())
                        )
                        val slash = Path().apply {
                            moveTo(centerX - 1.6.dp.toPx(), markerTop + 4.2.dp.toPx())
                            lineTo(centerX + 1.6.dp.toPx(), markerTop + 1.2.dp.toPx())
                        }
                        drawPath(
                            path = slash,
                            color = Color(0xFF94A3B8),
                            style = Stroke(width = 0.9.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Workout bar: only for a recorded session on an open day.
                    val minutes = day.workoutMinutes
                    if (!day.isGymClosed &&
                        day.workoutStatus == WorkoutDayStatus.RECORDED &&
                        minutes != null
                    ) {
                        val barHeight = (minutes.toFloat() / ceilingMinutes.toFloat())
                            .coerceIn(0f, 1f) * plotHeight
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(centerX - colWidth * 0.28f, plotHeight - barHeight),
                            size = Size(colWidth * 0.56f, barHeight.coerceAtLeast(0.6f)),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(0.7.dp.toPx())
                        )
                    } else if (!day.isGymClosed && day.workoutStatus == WorkoutDayStatus.CONFIRMED_NO_WORKOUT) {
                        // Confirmed no-workout: a small explicit zero tick, never a tall "0m" bar.
                        val tickHeight = 1.4.dp.toPx()
                        drawRoundRect(
                            color = CardPalette.Coral,
                            topLeft = Offset(centerX - colWidth * 0.28f, plotHeight - tickHeight),
                            size = Size(colWidth * 0.56f, tickHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(0.5.dp.toPx())
                        )
                    }
                }
            }
        }
        // X axis day labels.
        Row(Modifier.fillMaxWidth().padding(start = 11.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            val marks = remember(days) { dayLabelMarks(days.size) }
            marks.forEach { mark ->
                Text(
                    mark,
                    color = CardPalette.Muted,
                    fontSize = 3.5.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun dayLabelMarks(dayCount: Int): List<String> {
    if (dayCount <= 0) return emptyList()
    val preferred = listOf(1, 5, 10, 15, 20, 25, 30, dayCount)
    return preferred.distinct().filter { it in 1..dayCount }.sorted().take(7).map { it.toString() }
}

@Composable
private fun SummaryPill(
    icon: ImageVector,
    tint: Color,
    value: String,
    label: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .clip(RoundedCornerShape(5.dp))
            .background(CardPalette.Card)
            .border(1.dp, tint.copy(alpha = 0.2f), RoundedCornerShape(5.dp))
            .padding(horizontal = 2.dp, vertical = 1.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.5.dp)
    ) {
        Box(
            Modifier.size(7.dp).clip(CircleShape).background(tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(4.5.dp))
        }
        Column {
            Text(value, color = CardPalette.Ink, fontSize = 6.sp, fontWeight = FontWeight.Black, maxLines = 1)
            if (label != null) {
                Text(
                    label,
                    color = CardPalette.Muted,
                    fontSize = 3.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AttendanceSummaryRow(
    breakdown: com.example.badnewgym.feature.memberintelligence.domain.engine.AttendanceBreakdown,
    rate: Int?
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFFF7FBFD))
            .padding(horizontal = 3.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SummaryCell(Icons.Rounded.CalendarMonth, CardPalette.Mint, "${breakdown.present}", "Present", rate)
        SummaryCell(Icons.Rounded.ErrorOutline, CardPalette.Coral, "${breakdown.absent}", "Absent", percentOf(breakdown.absent, breakdown.operatingDays))
        SummaryCell(Icons.Rounded.AccessTime, CardPalette.Slate, "${breakdown.off}", "Off", percentOf(breakdown.off, breakdown.operatingDays))
        SummaryCell(Icons.Rounded.AccessTime, CardPalette.Amber, "${breakdown.late}", "Late", percentOf(breakdown.late, breakdown.operatingDays))
    }
}

private fun percentOf(part: Int, whole: Int): Int? = if (whole <= 0) null else (part * 100 / whole)

@Composable
private fun SummaryCell(icon: ImageVector, tint: Color, value: String, label: String, percent: Int?) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(1.dp)) {
        Box(
            Modifier.size(7.dp).clip(CircleShape).background(tint),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(4.5.dp))
        }
        Column {
            Text(value, color = CardPalette.Ink, fontSize = 6.sp, fontWeight = FontWeight.Black, maxLines = 1)
            Text(label, color = CardPalette.Muted, fontSize = 3.5.sp, maxLines = 1)
            percent?.let {
                Text(
                    "$it%",
                    color = tint,
                    fontSize = 3.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

// ------------------------------------------------------------------ section chrome

@Composable
private fun CardSectionBox(
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(SectionShape)
            .background(Color(0xFFFAFDFF))
            .border(1.dp, CardPalette.Hairline, SectionShape)
            .padding(horizontal = 3.dp, vertical = 2.5.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        content = content
    )
}

@Composable
private fun CardSectionHeader(
    icon: ImageVector,
    iconTint: Color,
    iconPale: Color,
    title: String,
    onClick: () -> Unit,
    trailing: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.5.dp))
                .background(iconTint),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(6.dp))
        }
        Spacer(Modifier.width(3.dp))
        Text(
            title,
            color = CardPalette.Ink,
            fontSize = 6.5.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (trailing != null) {
            Text(
                trailing,
                color = CardPalette.Cobalt,
                fontSize = 5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
        Icon(
            Icons.Rounded.ChevronRight,
            contentDescription = "Open $title",
            tint = CardPalette.Muted,
            modifier = Modifier
                .size(8.dp)
                .clickable(onClick = onClick)
        )
    }
}