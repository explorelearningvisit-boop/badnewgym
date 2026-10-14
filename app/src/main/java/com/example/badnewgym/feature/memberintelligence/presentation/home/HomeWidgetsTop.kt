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
import androidx.compose.ui.text.font.FontWeight
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
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF06231A))
            .clickable(onClick = onOpen)
    ) {
        AsyncImage(
            model = MediaAssets.GYM_BG,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.38f,
            modifier = Modifier.matchParentSize()
        )
        Box(
            Modifier.matchParentSize().background(
                Brush.horizontalGradient(
                    listOf(Color(0xE6062E20), Color(0x9E062E20), Color(0xCC041D16))
                )
            )
        )
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x3300E08A)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.DirectionsRun, contentDescription = null,
                        tint = Color(0xFF6BFFC0), modifier = Modifier.size(17.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            if (live) "LIVE SESSION" else "LAST SESSION",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            maxLines = 1
                        )
                        Spacer(Modifier.width(6.dp))
                        Pill(
                            if (live) "LIVE" else "COMPLETED",
                            if (live) Color(0xFF16C172) else Color(0xFF6E8079),
                            Color.White,
                            9
                        )
                    }
                    Text(
                        "DISCIPLINE BUILDS FREEDOM",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7FE8B8),
                        maxLines = 1
                    )
                }
                if (live) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFE54848))
                            .clickable(onClick = onEndSession)
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            "\u25A0 END SESSION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            var nowMs by remember { mutableLongStateOf(System.currentTimeMillis()) }
            LaunchedEffect(checkIn.liveStartMs) {
                while (true) {
                    nowMs = System.currentTimeMillis()
                    delay(1_000)
                }
            }
            val recorded = if (live) (nowMs - (checkIn.liveStartMs ?: nowMs)).coerceAtLeast(0)
            else checkIn.lastDurationMs
            Row(verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f)) {
                    Text(
                        if (recorded != null) fmtDurationHms(recorded) else "--:--:--",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        if (live) "in session" else "last recorded duration",
                        fontSize = 9.sp,
                        color = Color(0xFF8FD9B6),
                        maxLines = 1
                    )
                }
                HeroStat(
                    "STARTED",
                    fmtTime(if (live) checkIn.liveStartMs else checkIn.lastStartMs)
                )
                checkIn.visitNumberToday?.let { HeroStat("VISIT", "#$it") }
            }
        }
    }
}

/** One stat chip on the dark session hero. */
@Composable
private fun HeroStat(label: String, value: String) {
    Column(
        Modifier
            .padding(start = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x2EFFFFFF))
            .padding(horizontal = 9.dp, vertical = 5.dp)
    ) {
        Text(label, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7FE8B8), maxLines = 1)
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White, maxLines = 1)
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
            Box(contentAlignment = Alignment.BottomEnd) {
                val photo = identity.photoUrl
                if (!photo.isNullOrBlank()) {
                    AsyncImage(
                        model = photo,
                        contentDescription = identity.name,
                        modifier = Modifier
                            .size(36.dp)
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MiColors.Mint)
                            .border(2.dp, MiColors.Emerald, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AvatarDot(identity.name, MiColors.Emerald, 34.dp)
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
            }
        }
        if (membership != null) {
            Spacer(Modifier.height(4.dp))
            val totalDays = totalPlanDays(membership.startDate, membership.expiryDate)
            val used = (totalDays - membership.daysRemaining).coerceIn(0, totalDays)
            val pct = if (totalDays > 0) used * 100 / totalDays else 0
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "$used / $totalDays days",
                    fontSize = 10.sp,
                    color = MiColors.TextSecondary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MiColors.Border)
                ) {
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((pct / 100f).coerceIn(0f, 1f))
                            .clip(RoundedCornerShape(4.dp))
                            .background(MiColors.Emerald)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "$pct%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = MiColors.TextPrimary,
                    maxLines = 1
                )
            }
        }
    }
}

private data class StripStyle(val glyph: String, val label: String, val tint: Color, val bg: Color)

/**
 * Slim verified-context strip above the member's name. It renders only when
 * the records support a context, and never repeats the LIVE state the session
 * hero already shows prominently (an older live session yields null). The
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
    } ?: return
    val style = when (ctx) {
        MemberContext.JUST_CHECKED_IN -> StripStyle(
            "\u25CF", "Just checked in",
            Color(0xFF0E7A4D), Color(0xFFE5F6EC)
        )
        MemberContext.COMPLETED_VISIT -> StripStyle(
            "\u2713",
            checkIn.lastDurationMs?.let { "Visit completed \u00B7 ${fmtDurationHm(it)}" }
                ?: "Visit completed",
            Color(0xFF1F5E87), Color(0xFFEAF2FD)
        )
        MemberContext.NO_ACTIVE_VISIT -> StripStyle(
            "\u25CB", "No active visit",
            MiColors.TextSecondary, MiColors.SurfaceSunken
        )
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(9.dp))
            .background(style.bg)
            .padding(horizontal = 7.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(style.glyph, fontSize = 9.sp, fontWeight = FontWeight.Black, color = style.tint, maxLines = 1)
        Spacer(Modifier.width(5.dp))
        Text(style.label, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = style.tint, maxLines = 1)
    }
    Spacer(Modifier.height(4.dp))
}

private fun totalPlanDays(startMs: Long, endMs: Long): Int {
    if (endMs <= startMs) return 30
    return ((endMs - startMs) / 86_400_000L).toInt().coerceIn(1, 730)
}
