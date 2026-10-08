package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import kotlinx.coroutines.delay

// ------------------------------------------------------------------ CHECK-IN

/**
 * First and most prominent widget while a session is live.
 * The plan owns the theme: gold members see champagne metal, not a badge.
 * No Main Gate, no invented method, no fake timer.
 */
@Composable
fun CheckInWidget(
    checkIn: HomeCheckIn,
    snapshot: MemberSnapshot,
    onEndSession: () -> Unit,
    onOpen: () -> Unit
) {
    val theme = themeForPlan(snapshot.membership?.planName)
    val live = checkIn.liveStartMs != null
    val shape = RoundedCornerShape(18.dp)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE3C878)),
        shadowElevation = 4.dp
    ) {
        Box(
            Modifier
                .background(Brush.verticalGradient(listOf(theme.surfaceTop, theme.surfaceBottom)))
                .clickable(onClick = onOpen)
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (live) "CHECK-IN" else "CHECK-OUT",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = theme.accentText
                    )
                    Spacer(Modifier.width(6.dp))
                    if (live) {
                        Pill("LIVE", theme.pillBg, Color.White, 10)
                    } else {
                        Pill("Completed", MiColors.Neutral, Color.White, 10)
                    }
                    Spacer(Modifier.weight(1f))
                    if (live) {
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xFFE54848))
                                .clickable(onClick = onEndSession)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text("END SESSION", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }

                if (live) {
                    var nowMs by remember { mutableLongStateOf(System.currentTimeMillis()) }
                    LaunchedEffect(checkIn.liveStartMs) {
                        while (true) {
                            nowMs = System.currentTimeMillis()
                            delay(1_000)
                        }
                    }
                    val elapsed = (nowMs - (checkIn.liveStartMs ?: nowMs)).coerceAtLeast(0)
                    Text(
                        fmtDurationHms(elapsed),
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = theme.accentText,
                        maxLines = 1
                    )
                    Text(
                        "Today \u00B7 ${fmtTime(checkIn.liveStartMs)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = theme.accentText
                    )
                } else {
                    val dur = checkIn.lastDurationMs
                    Text(
                        if (dur != null) fmtDurationHm(dur) else "No active session",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = theme.accentText,
                        maxLines = 1
                    )
                    Text(
                        if (checkIn.lastStartMs != null)
                            "Start: ${fmtTime(checkIn.lastStartMs)}" +
                                (checkIn.lastEndMs?.let { "  \u00B7  End: ${fmtTime(it)}" } ?: "")
                        else "Not checked in today",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = theme.accentText
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val method = checkIn.methodLabel
                    if (live && method != null) {
                        MiniTile(method, "Check-in Method", Modifier.weight(1f))
                    } else if (live) {
                        MiniTile("Checked in", "Status", Modifier.weight(1f))
                    } else {
                        MiniTile(fmtDay(checkIn.lastStartMs), "Last Visit", Modifier.weight(1f))
                    }
                    val visit = checkIn.visitNumberToday
                    if (visit != null) {
                        MiniTile(visitOrdinal(visit), "Today", Modifier.weight(1f))
                    } else {
                        MiniTile(snapshot.membership?.planName ?: "Member", "Plan", Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniTile(top: String, bottom: String, modifier: Modifier = Modifier) {
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.72f))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(top, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
        Text(bottom, fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1)
    }
}

// ------------------------------------------------------------------ IDENTITY

/** Compact member summary. Whole card opens the full Profile module. */
@Composable
fun MemberIdentityWidget(snapshot: MemberSnapshot, onOpen: () -> Unit) {
    val identity = snapshot.identity
    val membership = snapshot.membership
    val planTheme = themeForPlan(membership?.planName)
    HomeCard(onClick = onOpen) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val photo = identity.photoUrl
            if (!photo.isNullOrBlank()) {
                AsyncImage(
                    model = photo,
                    contentDescription = identity.name,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MiColors.Mint),
                    contentScale = ContentScale.Crop
                )
            } else {
                AvatarDot(identity.name, MiColors.Emerald, 48.dp)
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        identity.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MiColors.TextPrimary,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(" >", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary)
                }
                val sub = listOfNotNull(
                    identity.code,
                    identity.age?.let { "$it yrs" },
                    identity.gender
                ).joinToString("  \u00B7  ")
                if (sub.isNotBlank()) {
                    Text(sub, fontSize = 11.5.sp, color = MiColors.TextSecondary, maxLines = 1)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (membership != null) {
                        Pill(membership.planName, planTheme.surfaceTop, planTheme.accentText, 10)
                    }
                    if (membership?.isActive == true) {
                        Pill("Active", Color(0xFFE5F6EC), Color(0xFF0E7A4D), 10)
                    }
                }
                if (membership != null) {
                    val totalDays = totalPlanDays(membership.startDate, membership.expiryDate)
                    val used = (totalDays - membership.daysRemaining).coerceIn(0, totalDays)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = if (totalDays > 0) used.toFloat() / totalDays else 0f,
                            modifier = Modifier
                                .weight(1f)
                                .height(7.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MiColors.Emerald,
                            trackColor = MiColors.Border
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "$used / $totalDays Days Used",
                            fontSize = 10.5.sp,
                            color = MiColors.TextSecondary,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "${membership?.daysRemaining ?: "-"}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = MiColors.Red
                )
                Text("Days Left", fontSize = 10.5.sp, color = MiColors.TextSecondary, maxLines = 1)
                Text(fmtDate(identity.memberSince), fontSize = 10.5.sp, color = MiColors.TextSecondary, maxLines = 1)
                Text("Join Date", fontSize = 10.sp, color = MiColors.TextTertiary, maxLines = 1)
            }
        }
    }
}

private fun totalPlanDays(startMs: Long, endMs: Long): Int {
    if (endMs <= startMs) return 30
    return ((endMs - startMs) / 86_400_000L).toInt().coerceIn(1, 730)
}
