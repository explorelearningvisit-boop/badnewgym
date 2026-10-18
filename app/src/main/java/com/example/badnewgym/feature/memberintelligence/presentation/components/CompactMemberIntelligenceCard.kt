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
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.WaterDrop
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
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
import kotlin.math.roundToInt

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

/**
 * One locale for every derived number on the card.
 *
 * The money formatter is pinned to `en-IN`, so the body-stat values must not drift to a
 * device locale that uses a comma decimal separator, which would render "69,5" beside
 * "₹2,800".
 */
private val numberLocale = Locale("en", "IN")

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
    now: Long? = null,
    onOpenBodyStats: () -> Unit = {},
    onOpenPayment: () -> Unit = {},
    onOpenActivity: () -> Unit = {},
    onOpenOverflow: () -> Unit = {}
) {
    // A caller-supplied clock must win so that recomposition cannot move the data.
    // When none is given, sample the clock exactly once and keep it for this instance:
    // a bare `now = System.currentTimeMillis()` default would re-read on every
    // recomposition and defeat every `remember(now)` downstream.
    val clock = now ?: remember { System.currentTimeMillis() }
    val monthKey = remember(clock) { DashboardMath.monthKey(clock) }

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
            MemberIdentityBlock(snapshot, clock)
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
                PaymentPlanRow(snapshot, clock)
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
internal fun CardBrandStrip(onOverflow: () -> Unit) {
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
internal fun MemberIdentityBlock(snapshot: MemberSnapshot, now: Long) {
    val identity = snapshot.identity
    val membership = snapshot.membership
    val elapsed = DashboardMath.elapsedPercent(membership, now)
    val daysLeft = DashboardMath.daysRemaining(membership, now)
    val (tierFg, tierBg) = CardPalette.tierStyle(identity.tier)
    // Terminal lifecycles must not read as live, even when a stale `isActive` says yes.
    val live = membership != null && membership.isActive &&
        membership.lifecycle != MembershipLifecycle.EXPIRED &&
        membership.lifecycle != MembershipLifecycle.CANCELLED

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
                    // The initial is always painted underneath. If the photo is missing,
                    // still loading, or fails to load, the circle shows the member's
                    // initial instead of an empty grey disc.
                    Text(
                        identity.name.trim().take(1).uppercase(Locale.getDefault()),
                        color = CardPalette.Muted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    if (identity.photoUrl != null) {
                        AsyncImage(
                            model = identity.photoUrl,
                            contentDescription = "Photo of ${identity.name}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
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
            if (live) {
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
                    label = planBadgeLabel(snapshot),
                    fg = tierFg,
                    bg = tierBg,
                    modifier = Modifier.weight(1f, fill = false)
                )
                MembershipStatusBadge(membership?.lifecycle, live)
            }
        }

        Spacer(Modifier.width(4.dp))

        MembershipRing(elapsed, daysLeft, DashboardMath.planTotalDays(membership))
    }
}

/**
 * Single source for the plan badge text.
 *
 * Uses the member's real plan name when the backend provides one, so the identity
 * badge and the payment badge can never disagree (both previously derived differently).
 */
internal fun planBadgeLabel(snapshot: MemberSnapshot): String =
    snapshot.membership?.planName
        ?.removeSuffix(" Plan")
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?: tierLabel(snapshot.identity.tier)

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
    // Terminal lifecycles outrank the `isActive` flag: a stale `isActive == true` must
    // never paint an expired/cancelled membership green.
    val (label, fg, bg) = when (lifecycle) {
        MembershipLifecycle.EXPIRED -> Triple("Expired", CardPalette.Coral, CardPalette.CoralPale)
        MembershipLifecycle.CANCELLED -> Triple("Cancelled", CardPalette.Coral, CardPalette.CoralPale)
        MembershipLifecycle.FROZEN -> Triple("Frozen", CardPalette.Cobalt, CardPalette.CobaltPale)
        MembershipLifecycle.PENDING -> Triple("Pending", CardPalette.Amber, Color(0xFFFFF6E3))
        MembershipLifecycle.EXPIRING -> Triple("Expiring", CardPalette.Amber, Color(0xFFFFF6E3))
        MembershipLifecycle.ACTIVE, MembershipLifecycle.REACTIVATED ->
            if (isActive) {
                Triple("Active", CardPalette.Mint, CardPalette.MintPale)
            } else {
                Triple("Inactive", CardPalette.Slate, Color(0xFFF1F5F9))
            }
        null -> if (isActive) {
            Triple("Active", CardPalette.Mint, CardPalette.MintPale)
        } else {
            Triple("Inactive", CardPalette.Slate, Color(0xFFF1F5F9))
        }
    }
    val allGood = fg == CardPalette.Mint
    Row(
        Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 3.dp, vertical = 1.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.5.dp)
    ) {
        Icon(
            if (allGood) Icons.Rounded.CheckCircle else Icons.Rounded.ErrorOutline,
            contentDescription = null,
            tint = fg,
            modifier = Modifier.size(6.dp)
        )
        Text(label, color = fg, fontSize = 6.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun MembershipRing(elapsedPercent: Int, daysLeft: Int, totalDays: Int) {
    val pct = elapsedPercent.coerceIn(0, 100)
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
                "$pct%",
                color = CardPalette.Ink,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black
            )
        }
        // The plan span is only meaningful when the validity window is known.
        Text(
            if (totalDays > 0) "$daysLeft / $totalDays" else "—",
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
internal fun BodyStatsRow(snapshot: MemberSnapshot) {
    val history = remember(snapshot.bodyProgress) { snapshot.bodyProgress.sortedBy { it.date } }
    val latest = history.lastOrNull()
    val previous = history.dropLast(1).lastOrNull()
    val goal = snapshot.identity.goal

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
            delta = change("Weight", goal, latest.weightKg, previous?.weightKg),
            label = "Weight",
            tint = CardPalette.Mint,
            icon = Icons.Rounded.MonitorWeight,
            modifier = Modifier.weight(1f)
        )
        MetricTile(
            value = formatNumber(latest.muscleKg),
            unit = "kg",
            delta = change("Muscle", goal, latest.muscleKg, previous?.muscleKg),
            label = "Muscle",
            tint = CardPalette.Purple,
            icon = Icons.Rounded.FitnessCenter,
            modifier = Modifier.weight(1f)
        )
        MetricTile(
            value = formatNumber(latest.bodyFatPercent),
            unit = "%",
            delta = change("Body Fat", goal, latest.bodyFatPercent, previous?.bodyFatPercent),
            label = "Body Fat",
            tint = CardPalette.Coral,
            icon = Icons.Rounded.WaterDrop,
            modifier = Modifier.weight(1f)
        )
        MetricTile(
            value = formatNumber(latest.bmi),
            unit = null,
            delta = null,
            band = DashboardMath.bmiBand(latest.bmi),
            label = "BMI",
            tint = CardPalette.Cobalt,
            icon = Icons.Rounded.Speed,
            modifier = Modifier.weight(1f)
        )
    }
}

private data class MetricDelta(
    val text: String,
    val isDown: Boolean,
    val direction: DashboardMath.DeltaDirection
)

/**
 * Change between the two most recent measurements.
 *
 * The direction is graded only against a recognised goal. With no goal the change stays
 * [DashboardMath.DeltaDirection.NEUTRAL] and is rendered in muted grey — the card must
 * not imply a change was good or bad without knowing what the member is training for.
 */
private fun change(metric: String, goal: String?, latest: Double?, previous: Double?): MetricDelta? {
    if (latest == null || previous == null) return null
    val delta = latest - previous
    if (delta == 0.0) return null
    return MetricDelta(
        text = String.format(numberLocale, "%.1f", kotlin.math.abs(delta)),
        isDown = delta < 0,
        direction = DashboardMath.deltaDirection(metric, goal, delta)
    )
}

private fun formatNumber(value: Double?): String =
    value?.let { String.format(numberLocale, "%.1f", it) } ?: "—"

@Composable
private fun MetricTile(
    value: String,
    unit: String?,
    delta: MetricDelta?,
    label: String,
    tint: Color,
    icon: ImageVector,
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
                .size(11.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(tint),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(7.dp)
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
            // FAVORABLE/UNFAVORABLE only ever come from a recognised goal; otherwise the
            // change is neutral and must read as information, not as praise or alarm.
            val deltaColor = when (delta.direction) {
                DashboardMath.DeltaDirection.FAVORABLE -> CardPalette.Mint
                DashboardMath.DeltaDirection.UNFAVORABLE -> CardPalette.Coral
                DashboardMath.DeltaDirection.NEUTRAL -> CardPalette.Muted
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (delta.isDown) Icons.Rounded.ArrowDownward else Icons.Rounded.ArrowUpward,
                    contentDescription = null,
                    tint = deltaColor,
                    modifier = Modifier.size(5.dp)
                )
                Text(
                    delta.text,
                    color = deltaColor,
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
internal fun PaymentPlanRow(snapshot: MemberSnapshot, now: Long) {
    val payment = snapshot.payment
    val membership = snapshot.membership
    val outstanding = payment?.totalOutstanding ?: 0.0
    val due = outstanding > 0.0
    val lifecycle = payment?.lifecycle
    // Only a real, settled payment may claim "Paid". A missing payment record or an
    // outstanding balance of zero with no settled record is explicitly not "Paid".
    val (stateLabel, stateFg) = when {
        due && lifecycle == PaymentLifecycle.OVERDUE -> "Overdue" to CardPalette.Coral
        due -> "Payment Due" to CardPalette.Coral
        lifecycle == PaymentLifecycle.FAILED -> "Failed" to CardPalette.Coral
        lifecycle == PaymentLifecycle.PARTIAL -> "Partly Paid" to CardPalette.Amber
        lifecycle == PaymentLifecycle.PAID -> "Paid · On Time" to CardPalette.Mint
        lifecycle == PaymentLifecycle.REFUNDED -> "Refunded" to CardPalette.Muted
        lifecycle == PaymentLifecycle.DUE -> "Due" to CardPalette.Muted
        payment == null -> "No payment record" to CardPalette.Muted
        else -> "Unknown" to CardPalette.Muted
    }
    val settled = lifecycle == PaymentLifecycle.PAID && !due
    val accent = when {
        due -> CardPalette.Coral
        settled -> CardPalette.Mint
        else -> CardPalette.Slate
    }
    val stateIcon = when {
        due -> Icons.Rounded.ErrorOutline
        settled -> Icons.Rounded.CheckCircle
        else -> Icons.Rounded.AccessTime
    }
    val headAmount = if (due) MoneyFormat.inr(outstanding) else payment?.lastPaymentAmount?.let { MoneyFormat.inr(it) } ?: "—"
    val dueTarget = if (due) payment?.dueDate else membership?.expiryDate
    val daysToDue = dueTarget?.let { DashboardMath.daysBetween(now, it) }
    // The date means two different things. With money outstanding it is a payment due
    // date; with nothing owed it is the plan's own renewal date. They must not share
    // wording: an expired plan with a zero balance is not an overdue payment.
    val dueCaption = when {
        daysToDue == null -> "Not scheduled"
        due && daysToDue > 0 -> "$daysToDue Days Left"
        due && daysToDue == 0 -> "Due Today"
        due -> "${-daysToDue} Days Overdue"
        daysToDue > 0 -> "$daysToDue Days Left"
        daysToDue == 0 -> "Ends Today"
        else -> "Expired"
    }
    val dueCaptionColor = when {
        due && daysToDue != null && daysToDue < 0 -> CardPalette.Coral
        !due && daysToDue != null && daysToDue <= 0 -> CardPalette.Coral
        else -> CardPalette.Cobalt
    }
    val (tierFg, tierBg) = CardPalette.tierStyle(snapshot.identity.tier)

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .size(17.dp)
                .clip(CircleShape)
                .background(accent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                stateIcon,
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
                if (due) "Next Due" else "Plan Renewal",
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
                dueCaption,
                color = dueCaptionColor,
                fontSize = 4.5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        MiniBadge(
            icon = Icons.Rounded.Bolt,
            label = planBadgeLabel(snapshot),
            fg = tierFg,
            bg = tierBg
        )
    }
}

// ------------------------------------------------------------------ attendance + workout

@Composable
internal fun ActivityBlock(snapshot: MemberSnapshot, monthKey: Long) {
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
        ActivityLegend(rate)
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
        AttendanceSummaryRow(breakdown)
    }
}

/**
 * Persistent legend plus the month's overall attendance rate.
 *
 * Attendance (status markers) and workout (minute bars) are visually distinct
 * encodings, never two similar series in the same colour. The rate is the honest
 * closure- and unknown-adjusted figure, so it is labelled rather than being attached
 * to the `Present` counter (which is a raw count, not a rate).
 */
@Composable
internal fun ActivityLegend(rate: Int?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            LegendDot("Present", CardPalette.Mint)
            LegendDot("Absent", CardPalette.Coral)
            LegendDot("Off", CardPalette.Slate)
            LegendDot("Workout", CardPalette.Cobalt)
        }
        Text(
            rate?.let { "Overall $it%" } ?: "Overall —",
            color = CardPalette.Mint,
            fontSize = 4.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.semantics {
                contentDescription = rate?.let { "Overall attendance rate $it percent" }
                    ?: "Overall attendance rate unavailable"
            }
        )
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
internal fun CombinedActivityChart(
    days: List<com.example.badnewgym.feature.memberintelligence.domain.engine.CombinedDay>,
    ceilingMinutes: Int
) {
    val barColor = CardPalette.Cobalt
    val gridColor = Color(0xFFEDF2F8)
    val bandHeight = 2.6.dp
    val labelCount = 7

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().height(58.dp)) {
            // Y axis labels.
            Column(
                Modifier
                    .width(11.dp)
                    .height(52.dp),
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
                    .height(52.dp)
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

                    // Attendance status strip on the baseline. A gym closure always reads
                    // as "Off" (slate), never as the faint UNKNOWN grey — an unworked
                    // closure day is not missing attendance data.
                    val stripTop = size.height - bandHeight.toPx()
                    val stripColor = if (day.isGymClosed) {
                        CardPalette.Slate
                    } else {
                        CardPalette.attendanceColor(day.attendance)
                    }
                    drawRoundRect(
                        color = stripColor,
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

                    // Workout bar: only for a genuine recorded session on an open day.
                    val minutes = day.workoutMinutes
                    if (day.hasRecordedWorkout && minutes != null) {
                        val barHeight = (minutes.toFloat() / ceilingMinutes.toFloat())
                            .coerceIn(0f, 1f) * plotHeight
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(centerX - colWidth * 0.28f, plotHeight - barHeight),
                            size = Size(colWidth * 0.56f, barHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(0.7.dp.toPx())
                        )
                    } else if (!day.isGymClosed && (
                            day.workoutStatus == WorkoutDayStatus.CONFIRMED_NO_WORKOUT ||
                                (day.workoutStatus == WorkoutDayStatus.RECORDED && (minutes ?: 0) <= 0)
                            )
                    ) {
                        // A confirmed no-workout day — including a contradictory "recorded"
                        // entry of zero minutes — gets an explicit zero tick, never a
                        // minimum-height bar that would read as a session.
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
        // X axis day labels, centred on the column each number names.
        //
        // A `SpaceBetween` row would spread the labels edge to edge while the bars sit at
        // their own column centres, leaving "30" visibly to the right of day 30. Laying
        // them out with the same column arithmetic keeps the axis honest.
        val marks = remember(days) { dayLabelMarks(days.size) }
        val dayCount = days.size.coerceAtLeast(1)
        Layout(
            content = {
                marks.forEach { (_, label) ->
                    Text(
                        label,
                        color = CardPalette.Muted,
                        fontSize = 3.5.sp,
                        maxLines = 1
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 11.dp)
        ) { measurables, constraints ->
            val placeables = measurables.map { measurable ->
                measurable.measure(androidx.compose.ui.unit.Constraints())
            }
            val width = constraints.maxWidth
            val height = placeables.maxOfOrNull { it.height } ?: 0
            layout(width, height) {
                placeables.forEachIndexed { index, placeable ->
                    val day = marks[index].first
                    val center = (day - 0.5f) / dayCount * width
                    val x = (center - placeable.width / 2f).roundToInt()
                        .coerceIn(0, (width - placeable.width).coerceAtLeast(0))
                    placeable.place(x, 0)
                }
            }
        }
    }
}

private fun dayLabelMarks(dayCount: Int): List<Pair<Int, String>> {
    if (dayCount <= 0) return emptyList()
    val preferred = listOf(1, 5, 10, 15, 20, 25, 30, dayCount)
    return preferred.distinct().filter { it in 1..dayCount }.sorted().take(7).map { it to it.toString() }
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
internal fun AttendanceSummaryRow(
    breakdown: com.example.badnewgym.feature.memberintelligence.domain.engine.AttendanceBreakdown
) {
    // Every share is taken against the whole month. The four buckets partition the
    // month's days, so they can never sum above 100%; using the operating-day count
    // for `off` would be wrong, because closures are exactly what `operatingDays`
    // excludes.
    val monthDays = breakdown.monthDays
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFFF7FBFD))
            .padding(horizontal = 3.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SummaryCell(Icons.Rounded.CalendarMonth, CardPalette.Mint, "${breakdown.present}", "Present", percentOf(breakdown.present, monthDays))
        SummaryCell(Icons.Rounded.ErrorOutline, CardPalette.Coral, "${breakdown.absent}", "Absent", percentOf(breakdown.absent, monthDays))
        SummaryCell(Icons.Rounded.AccessTime, CardPalette.Slate, "${breakdown.off}", "Off", percentOf(breakdown.off, monthDays))
        SummaryCell(Icons.Rounded.AccessTime, CardPalette.Amber, "${breakdown.late}", "Late", percentOf(breakdown.late, monthDays))
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
internal fun CardSectionBox(
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
internal fun CardSectionHeader(
    icon: ImageVector,
    iconTint: Color,
    iconPale: Color,
    title: String,
    onClick: () -> Unit,
    trailing: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            // The whole row is the control, not just the chevron. The header reads as a
            // full-width row, so an 8dp icon alone would be a misleading target.
            .clickable(
                onClickLabel = "Open $title",
                role = Role.Button,
                onClick = onClick
            )
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
            contentDescription = null,
            tint = CardPalette.Muted,
            modifier = Modifier.size(8.dp)
        )
    }
}