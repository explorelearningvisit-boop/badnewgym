package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Ultra-compact live card. Critical only: state, real ticking timer from the
 * actual check-in event, today + visit count, END SESSION. Method and history
 * live one tap away in the module sheet.
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
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
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
                        Pill("Completed", MiColors.Neutral, Color.White, 9)
                    }
                    Spacer(Modifier.weight(1f))
                    if (live) {
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xFFE54848))
                                .clickable(onClick = onEndSession)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("END SESSION", fontSize = 10.5.sp, fontWeight = FontWeight.Black, color = Color.White)
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
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Black,
                            color = theme.accentText,
                            maxLines = 1
                        )
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text(
                                "Today \u00B7 ${fmtTime(checkIn.liveStartMs)}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = theme.accentText,
                                maxLines = 1
                            )
                            val visit = checkIn.visitNumberToday
                            Text(
                                if (visit != null) visitOrdinal(visit) else (checkIn.methodLabel ?: ""),
                                fontSize = 9.5.sp,
                                color = theme.accentText,
                                maxLines = 1
                            )
                        }
                    }
                } else {
                    val dur = checkIn.lastDurationMs
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            if (dur != null) fmtDurationHm(dur) else "No active session",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = theme.accentText,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        val lastVisit = checkIn.lastStartMs ?: snapshot.attendance?.lastVisitAt
                        if (lastVisit != null) {
                            Text(
                                fmtDay(lastVisit),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = theme.accentText,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ IDENTITY

/** One-row member summary. Whole card opens the full Profile module. */
@Composable
fun MemberIdentityWidget(snapshot: MemberSnapshot, onOpen: () -> Unit) {
    val identity = snapshot.identity
    val membership = snapshot.membership
    val planTheme = themeForPlan(membership?.planName)
    HomeCard(onClick = onOpen, padding = 6.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val photo = identity.photoUrl
            if (!photo.isNullOrBlank()) {
                AsyncImage(
                    model = photo,
                    contentDescription = identity.name,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MiColors.Mint),
                    contentScale = ContentScale.Crop
                )
            } else {
                AvatarDot(identity.name, MiColors.Emerald, 34.dp)
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
                    Text(" >", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MiColors.TextTertiary)
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
                        Pill(membership.planName, planTheme.surfaceTop, planTheme.accentText, 9)
                    }
                    if (membership?.isActive == true) {
                        Pill("Active", Color(0xFFE5F6EC), Color(0xFF0E7A4D), 9)
                    }
                    if (membership != null) {
                        val totalDays = totalPlanDays(membership.startDate, membership.expiryDate)
                        val used = (totalDays - membership.daysRemaining).coerceIn(0, totalDays)
                        Text(
                            "$used/$totalDays used",
                            fontSize = 9.5.sp,
                            color = MiColors.TextSecondary,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }
            }
            Spacer(Modifier.width(6.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${membership?.daysRemaining ?: "-"}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = MiColors.Red,
                    maxLines = 1
                )
                Text("Days Left", fontSize = 9.sp, color = MiColors.TextSecondary, maxLines = 1)
            }
        }
    }
}

private fun totalPlanDays(startMs: Long, endMs: Long): Int {
    if (endMs <= startMs) return 30
    return ((endMs - startMs) / 86_400_000L).toInt().coerceIn(1, 730)
}
