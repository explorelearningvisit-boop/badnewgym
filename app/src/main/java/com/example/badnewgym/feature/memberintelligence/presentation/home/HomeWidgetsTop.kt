package com.example.badnewgym.feature.memberintelligence.presentation.home

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
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.badnewgym.feature.memberintelligence.design.MediaAssets
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import kotlinx.coroutines.delay

// ------------------------------------------------------------------ CHECK-IN

/**
 * Session hero card. Completed state shows the real duration plus IN / OUT /
 * date blocks; live state ticks from the persisted check-in timestamp with an
 * End Session action. A missing duration renders as "No active session",
 * never as a fabricated 00h 00m.
 */
@Composable
fun CheckInWidget(
    checkIn: HomeCheckIn,
    snapshot: MemberSnapshot,
    onEndSession: () -> Unit,
    onOpen: () -> Unit,
    compact: Boolean = false
) {
    val live = checkIn.liveStartMs != null
    Box(
        Modifier
            .fillMaxWidth()
            .height(if (compact) 45.dp else 118.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF06231A))
            .clickable(onClick = onOpen)
    ) {
        AsyncImage(
            model = MediaAssets.GYM_BG,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = if (compact) 0.38f else 0.5f,
            modifier = Modifier.matchParentSize()
        )
        Box(
            Modifier.matchParentSize().background(
                Brush.verticalGradient(
                    listOf(Color(0xCC041D16), Color(0x99062E20), Color(0xF2062320))
                )
            )
        )
        Column(
            Modifier.fillMaxSize().padding(horizontal = 9.dp, vertical = 7.dp)
        ) {
            if (!compact) {
                // The reference gives the photo its own breathing room with a
                // motivational line. At compact height there is no room for it,
                // so it is dropped rather than clipped.
                Column(Modifier.weight(1f)) {
                    Text(
                        "GOOD WORKOUT",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        "A HEALTHY YOU",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF7FE8B8),
                        maxLines = 1
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "\u201CDiscipline Builds Freedom\u201D",
                        fontSize = 9.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFB8E8CE),
                        maxLines = 1
                    )
                }
            }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x3300E08A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.DirectionsRun, contentDescription = null,
                    tint = Color(0xFF6BFFC0), modifier = Modifier.size(15.dp)
                )
            }
            Spacer(Modifier.width(7.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (live) "LIVE SESSION" else "LAST SESSION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(4.dp))
                    Pill(
                        if (live) "LIVE" else "DONE",
                        if (live) Color(0xFF16C172) else Color(0xFF6E8079),
                        Color.White,
                        8
                    )
                }
                // The visit number rides on the second line instead of taking a
                // chip of its own, which is what pushed the hero into clipping.
                Text(
                    listOfNotNull(
                        checkIn.visitNumberToday?.let { "Visit #$it" },
                        checkInMethodLabel(checkIn.methodLabel)
                    ).joinToString(" \u00B7 "),
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7FE8B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            var nowMs by remember { mutableLongStateOf(System.currentTimeMillis()) }
            LaunchedEffect(checkIn.liveStartMs) {
                while (true) {
                    nowMs = System.currentTimeMillis()
                    delay(1_000)
                }
            }
            val recorded = if (live) (nowMs - (checkIn.liveStartMs ?: nowMs)).coerceAtLeast(0)
            else checkIn.lastDurationMs
            Spacer(Modifier.width(6.dp))
            Text(
                if (recorded != null) fmtDurationHms(recorded) else "--:--:--",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                maxLines = 1
            )
            if (live) {
                Spacer(Modifier.width(7.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFFE54848))
                        .clickable(onClick = onEndSession)
                        .padding(horizontal = 9.dp, vertical = 5.dp)
                ) {
                    Text(
                        "\u25A0 END",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
        }
    }
}

@Composable
private fun TimeBlock(label: String, value: String, fg: Color) {
    Column(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.65f))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MiColors.TextSecondary, maxLines = 1)
        Text(value, fontSize = 10.5.sp, fontWeight = FontWeight.Black, color = fg, maxLines = 1)
    }
}

// ------------------------------------------------------------------ IDENTITY

/** Member summary: photo ring, identity, plan badges, days progress, days left. */
@Composable
fun MemberIdentityWidget(snapshot: MemberSnapshot, onOpen: () -> Unit, checkIn: HomeCheckIn? = null) {
    val identity = snapshot.identity
    val membership = snapshot.membership
    val planTheme = themeForPlan(membership?.planName)
    HomeCard(onClick = onOpen, padding = 6.dp) {
        if (checkIn != null) {
            ContextStrip(checkIn)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(2.dp))
            Box(contentAlignment = Alignment.BottomEnd) {
                val photo = identity.photoUrl
                if (!photo.isNullOrBlank()) {
                    AsyncImage(
                        model = photo,
                        contentDescription = identity.name,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MiColors.Mint)
                            .border(
                                2.dp, MiColors.Emerald,
                                CircleShape
                            ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MiColors.Mint)
                            .border(2.dp, MiColors.Emerald, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AvatarDot(identity.name, MiColors.Emerald, 32.dp)
                    }
                }
                Box(
                    Modifier
                        .size(13.dp)
                        .clip(CircleShape)
                        .background(if (membership?.isActive == true) Color(0xFF22C55E) else MiColors.TextTertiary)
                        .border(2.dp, MiColors.Surface, CircleShape)
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        identity.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MiColors.TextPrimary,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (identity.isVerified) {
                        Text(" \u2713", fontSize = 12.sp, fontWeight = FontWeight.Black, color = MiColors.EmeraldDark)
                    }
                }
                val sub = listOfNotNull(
                    identity.code,
                    identity.age?.let { "$it yrs" },
                    identity.gender
                ).joinToString("  \u00B7  ")
                if (sub.isNotBlank()) {
                    Text(sub, fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (membership != null) {
                        Pill(membership.planName, Color(0xFFFFF3D6), Color(0xFF8A6D1B), 9)
                    }
                    if (membership?.isActive == true) {
                        Pill("Active", Color(0xFFE5F6EC), Color(0xFF0E7A4D), 9)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${membership?.daysRemaining ?: "-"}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = MiColors.Red,
                    maxLines = 1
                )
                Text("Days Left", fontSize = 9.sp, color = MiColors.TextSecondary, maxLines = 1)
                Text(">", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary)
                // The plan progress lives under the countdown instead of on its
                // own full-width row, which is what forced a second line.
                if (membership != null) {
                    val totalDays = totalPlanDays(membership.startDate, membership.expiryDate)
                    val used = (totalDays - membership.daysRemaining).coerceIn(0, totalDays)
                    val pct = if (totalDays > 0) used * 100 / totalDays else 0
                    Spacer(Modifier.height(3.dp))
                    Box(
                        Modifier
                            .width(74.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MiColors.Border)
                    ) {
                        Box(
                            Modifier
                                .fillMaxHeight()
                                .fillMaxWidth((pct / 100f).coerceIn(0f, 1f))
                                .clip(RoundedCornerShape(3.dp))
                                .background(MiColors.Emerald)
                        )
                    }
                    Text(
                        "$used / $totalDays days \u00B7 $pct%",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = MiColors.TextSecondary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Verified context strip above the member's name. It renders only when the
 * records support a context, and never repeats the LIVE state the hero already
 * shows prominently (an older live session yields a plain location line). The
 * glyph and wording carry the meaning; the tint is decoration, never the only
 * signal.
 */
@Composable
private fun ContextStrip(checkIn: HomeCheckIn) {
    val zone = java.time.ZoneId.systemDefault()
    val todayStart = remember {
        java.time.LocalDate.now(zone).atStartOfDay(zone).toInstant().toEpochMilli()
    }
    val nowMs = remember { System.currentTimeMillis() }
    val ctx = remember(checkIn, todayStart, nowMs) {
        memberContext(checkIn, todayStart, nowMs)
    }
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (ctx) {
            MemberContext.JUST_CHECKED_IN -> {
                StripPill(
                    glyph = "\u25CF",
                    label = "Checked In \u00B7 ${fmtTime(checkIn.liveStartMs)}",
                    tint = Color(0xFF0E7A4D), bg = Color(0xFFE5F6EC)
                )
            }
            MemberContext.COMPLETED_VISIT -> {
                StripPill(
                    glyph = "\u2713",
                    label = checkIn.lastDurationMs?.let { "Visit completed \u00B7 ${fmtDurationHm(it)}" }
                        ?: "Visit completed",
                    tint = Color(0xFF1F5E87), bg = Color(0xFFEAF2FD)
                )
            }
            else -> {
                StripPill(
                    glyph = "\u25CB", label = "No active visit",
                    tint = MiColors.TextSecondary, bg = MiColors.SurfaceSunken
                )
            }
        }
        // The reference shows a floor/location here; we have no location record,
        // so we print the real check-in mechanism instead of inventing one.
        val method = checkInMethodLabel(checkIn.methodLabel)
        if (method != null) {
            Spacer(Modifier.width(5.dp))
            Text(
                method,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = MiColors.TextSecondary,
                maxLines = 1,
                modifier = Modifier.weight(1f, fill = false)
            )
        }
        Spacer(Modifier.weight(1f))
        if (checkIn.liveStartMs != null) {
            Pill("Active Session", Color(0xFFE5F6EC), Color(0xFF0E7A4D), 9)
        }
    }
    Spacer(Modifier.height(3.dp))
}

@Composable
private fun StripPill(glyph: String, label: String, tint: Color, bg: Color) {
    Row(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(glyph, fontSize = 8.sp, fontWeight = FontWeight.Black, color = tint, maxLines = 1)
        Spacer(Modifier.width(3.dp))
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = tint, maxLines = 1)
    }
}

private fun totalPlanDays(startMs: Long, endMs: Long): Int {
    if (endMs <= startMs) return 30
    return ((endMs - startMs) / 86_400_000L).toInt().coerceIn(1, 730)
}
