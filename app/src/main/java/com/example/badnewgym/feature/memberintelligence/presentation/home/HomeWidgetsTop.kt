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
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(Color(0xFFE8B93C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.FitnessCenter, contentDescription = null,
                            tint = Color.White, modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                if (live) "CHECK-IN" else "CHECK-OUT",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = theme.accentText
                            )
                            Spacer(Modifier.width(6.dp))
                            if (live) {
                                Pill("LIVE", theme.pillBg, Color.White, 9)
                            } else {
                                Pill("Completed", Color(0xFF2FA36B), Color.White, 9)
                            }
                        }
                        if (!live) {
                            val lastVisit = checkIn.lastStartMs ?: snapshot.attendance?.lastVisitAt
                            if (lastVisit != null) {
                                Text(
                                    "Today \u00B7 ${fmtDay(lastVisit)}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = theme.accentText,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    if (live) {
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xFFE54848))
                                .clickable(onClick = onEndSession)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("\u25FC END SESSION", fontSize = 10.5.sp, fontWeight = FontWeight.Black, color = Color.White)
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
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            fmtDurationHms(elapsed),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = theme.accentText,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        TimeBlock("IN", fmtTime(checkIn.liveStartMs), theme.accentText)
                        Spacer(Modifier.width(6.dp))
                        val visit = checkIn.visitNumberToday
                        if (visit != null) {
                            TimeBlock("VISIT", "#$visit", theme.accentText)
                        } else if (checkIn.methodLabel != null) {
                            TimeBlock("VIA", checkIn.methodLabel!!, theme.accentText)
                        }
                    }
                } else {
                    val dur = checkIn.lastDurationMs
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            if (dur != null) fmtDurationHm(dur) else "No active session",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = theme.accentText,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        if (checkIn.lastStartMs != null && checkIn.lastEndMs != null) {
                            TimeBlock("IN", fmtTime(checkIn.lastStartMs), theme.accentText)
                            Spacer(Modifier.width(6.dp))
                            TimeBlock("OUT", fmtTime(checkIn.lastEndMs), theme.accentText)
                        }
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
fun MemberIdentityWidget(snapshot: MemberSnapshot, onOpen: () -> Unit) {
    val identity = snapshot.identity
    val membership = snapshot.membership
    val planTheme = themeForPlan(membership?.planName)
    HomeCard(onClick = onOpen, padding = 6.dp) {
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

private fun totalPlanDays(startMs: Long, endMs: Long): Int {
    if (endMs <= startMs) return 30
    return ((endMs - startMs) / 86_400_000L).toInt().coerceIn(1, 730)
}
