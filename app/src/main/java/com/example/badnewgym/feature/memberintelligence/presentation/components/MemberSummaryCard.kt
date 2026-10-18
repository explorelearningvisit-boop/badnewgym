package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.badnewgym.feature.memberintelligence.domain.engine.DashboardMath
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Compact summary card palette, matched to the approved dark reference image.
private object SumColor {
    val ink = Color(0xFFEDF4FF)
    val meta = Color(0xFFA7BCDF)
    val label = Color(0xFF8FA5C9)
    val value = Color(0xFFEDF4FF)
    val cardStart = Color(0xFF11294E)
    val cardEnd = Color(0xFF0B1D3C)
    val goldGlow = Color(0xFF2E7A6B)
    val goldRing = Color(0xFFF3C64A)
    val goldText = Color(0xFFFFE9A8)
    val goldChip = Color(0xFF4A3A10)
    val goldDisc = Color(0xFFF0C244)
    val green = Color(0xFF2FBE72)
    val greenChip = Color(0xFF0E4B31)
    val greenChipText = Color(0xFF8CF0C0)
    val red = Color(0xFFFF7182)
    val redChip = Color(0xFF59212A)
    val redChipText = Color(0xFFFFC2C9)
    val ringTrack = Color(0xFF1E3A63)
    val ringRest = Color(0xFF1A4D7A)
    val divider = Color(0xFF1D3559)
    val validity = Color(0xFF9DB2D8)
}

/**
 * Compact member summary card — the approved Aman Tripathi (BG305) reference.
 * Replaces the former PixelPerfectMemberCard as the canonical member widget.
 *
 * Data honesty rules:
 *  - every value is derived from the snapshot; the elapsed ring and "Days Left"
 *    come from [DashboardMath], never from fixture literals;
 *  - age/gender/goal/branch render only when present;
 *  - without a membership the ring centre renders "—" and the footer omits the
 *    validity range; nothing is drawn as zero.
 */
@Composable
fun MemberSummaryCard(
    snapshot: MemberSnapshot,
    modifier: Modifier = Modifier
) {
    val membership = snapshot.membership
    val now = remember { System.currentTimeMillis() }
    val elapsedPercent = membership?.let { DashboardMath.elapsedPercent(it, now) }
    val daysLeft = membership?.let { DashboardMath.daysRemaining(it, now) }
    val active = membership?.isActive == true

    Box(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(listOf(SumColor.cardStart, SumColor.cardEnd)))
    ) {
        // Soft gold glow in the top-left corner, like the reference card.
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SumColor.goldGlow.copy(alpha = 0.55f),
                            SumColor.goldGlow.copy(alpha = 0f)
                        ),
                        center = Offset(46f, 28f),
                        radius = 620f
                    )
                )
        )
        Column(Modifier.padding(horizontal = 12.dp, vertical = 9.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SummaryAvatar(
                    photoUrl = snapshot.identity.photoUrl,
                    name = snapshot.identity.name,
                    verified = snapshot.identity.isVerified,
                    active = active,
                    modifier = Modifier.size(52.dp)
                )
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        timeGreeting() + " \uD83D\uDC4B",
                        color = SumColor.meta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        snapshot.identity.name,
                        color = SumColor.ink,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val meta = buildList {
                        snapshot.identity.code?.let { add(it) }
                        snapshot.identity.ageYears?.let { add("$it yrs") }
                        snapshot.identity.gender?.let { add(it) }
                    }
                    if (meta.isNotEmpty()) {
                        Text(
                            meta.joinToString(" | "),
                            color = SumColor.meta,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        PlanChip(
                            label = membership?.planName
                                ?: snapshot.identity.tier.name.replace('_', ' ').lowercase()
                                    .replaceFirstChar { it.uppercase() }
                        )
                        when {
                            membership == null -> Unit // No membership is rendered at all, never as "active".
                            active -> ActiveChip()
                            else -> InactiveChip()
                        }
                    }
                }
                StatusRing(
                    percent = elapsedPercent,
                    daysLeft = daysLeft,
                    active = active && membership != null,
                    modifier = Modifier.size(64.dp)
                )
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp)
                    .height(1.dp)
                    .background(SumColor.divider)
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
                    FooterItem(
                        icon = Icons.Rounded.CalendarMonth,
                        iconTint = SumColor.green,
                        label = "Joined",
                        value = formatDayFull(snapshot.identity.memberSince),
                        modifier = Modifier.weight(0.9f)
                    )
                    snapshot.identity.goal?.let {
                        FooterItem(
                            icon = Icons.Rounded.TrackChanges,
                            iconTint = Color(0xFF3B82F6),
                            label = "Goal",
                            value = it,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    snapshot.identity.branchName?.let {
                        FooterItem(
                            icon = Icons.Rounded.LocationOn,
                            iconTint = SumColor.green,
                            label = "Branch",
                            value = it,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------- pieces

@Composable
private fun SummaryAvatar(
    photoUrl: String?,
    name: String,
    verified: Boolean,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Box(
            Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(2.dp, if (active) SumColor.green else SumColor.goldRing, CircleShape)
                .padding(2.dp)
        ) {
            if (photoUrl.isNullOrBlank()) {
                Box(Modifier.fillMaxSize().clip(CircleShape).background(SumColor.goldChip), contentAlignment = Alignment.Center) {
                    Text(
                        name.split(" ").take(2).mapNotNull { it.firstOrNull() }.joinToString(""),
                        color = SumColor.goldText, fontSize = 16.sp, fontWeight = FontWeight.Black
                    )
                }
            } else {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Photo of $name",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            }
        }
        if (verified) {
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .size(17.dp)
                    .clip(CircleShape)
                    .background(SumColor.goldDisc)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("♛", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
            }
        }
        if (active) {
            Box(Modifier.align(Alignment.BottomEnd).size(13.dp).clip(CircleShape).background(SumColor.green)
                .border(2.dp, Color.White, CircleShape))
        }
    }
}

/** Plan chip. The reference shows a chevron for plan switching; no plan-switch flow
 *  exists in this build, so the chevron is omitted rather than rendered dead. */
@Composable
private fun PlanChip(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(11.dp))
            .background(SumColor.goldChip)
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Box(Modifier.size(14.dp).clip(CircleShape).background(SumColor.goldDisc), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Bolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
        }
        Text(
            label,
            color = SumColor.goldText,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ActiveChip() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(11.dp))
            .background(SumColor.greenChip)
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Box(Modifier.size(14.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Check, contentDescription = null, tint = SumColor.green, modifier = Modifier.size(10.dp))
        }
        Text("Active", color = SumColor.greenChipText, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InactiveChip() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(11.dp))
            .background(SumColor.redChip)
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Box(Modifier.size(14.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Close, contentDescription = null, tint = SumColor.red, modifier = Modifier.size(10.dp))
        }
        Text("Inactive", color = SumColor.redChipText, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun StatusRing(
    percent: Int?,
    daysLeft: Int?,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 6.5.dp.toPx(), cap = StrokeCap.Round)
            val inset = stroke.width / 2 + 1.dp.toPx()
            val arcSize = size.width - inset * 2
            drawArc(
                color = SumColor.ringTrack,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = androidx.compose.ui.geometry.Size(arcSize, arcSize),
                style = stroke
            )
            val sweep = (percent ?: 0).coerceIn(0, 100) * 3.6f
            if (active && sweep < 360f && sweep > 0f) {
                drawArc(
                    color = SumColor.ringRest,
                    startAngle = -90f + sweep,
                    sweepAngle = 360f - sweep,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = androidx.compose.ui.geometry.Size(arcSize, arcSize),
                    style = stroke
                )
            }
            if (sweep > 0f) {
                drawArc(
                    color = if (active) SumColor.green else SumColor.red,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = androidx.compose.ui.geometry.Size(arcSize, arcSize),
                    style = stroke
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                percent?.let { "$it%" } ?: "—",
                color = SumColor.ink,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 15.sp
            )
            Text(
                daysLeft?.let { "$it Days Left" } ?: "",
                color = SumColor.meta,
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 9.sp
            )
        }
    }
}

@Composable
private fun FooterItem(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(1.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(13.dp))
            Text(label, color = SumColor.label, fontSize = 8.sp, fontWeight = FontWeight.Medium)
        }
        Text(
            value,
            color = SumColor.value,
            fontSize = 7.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// --------------------------------------------------------------------- format

private val dayFullFormat by lazy { SimpleDateFormat("d MMM yyyy", Locale.ENGLISH) }

private fun timeGreeting(): String = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
    in 5..11 -> "Good Morning"
    in 12..16 -> "Good Afternoon"
    in 17..21 -> "Good Evening"
    else -> "Good Night"
}

private fun formatDayFull(time: Long): String = dayFullFormat.format(Date(time))
