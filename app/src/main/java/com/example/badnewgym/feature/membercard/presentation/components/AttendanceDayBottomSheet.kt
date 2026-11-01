package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardTokens
import com.example.badnewgym.feature.membercard.domain.model.CalendarDay
import com.example.badnewgym.feature.membercard.domain.model.DayAttendanceStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceDayBottomSheet(
    day: CalendarDay?,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    monthLabel: String = "",
    modifier: Modifier = Modifier
) {
    if (day == null) return

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color(0xFFF4FBF9),
        scrimColor = Color(0x66021C17),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = { SheetGrip() },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            // Header Row: Close + Title
            GlassSheetHeader(
                title = buildString {
                    append("Day ").append(day.dayOfMonth)
                    if (monthLabel.isNotBlank()) append(" \u2022 ").append(monthLabel)
                },
                subtitle = "Attendance log",
                onClose = onDismissRequest
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Status Card
            val (statusText, statusBg, statusColor) = when (day.status) {
                DayAttendanceStatus.PRESENT -> Triple("Present \u2022 On-Time", Color(0xFFDCFCE7), Color(0xFF15803D))
                DayAttendanceStatus.ABSENT -> Triple("Absent", Color(0xFFFEE2E2), Color(0xFFB91C1C))
                DayAttendanceStatus.LATE -> Triple("Late Entry", Color(0xFFFEF3C7), Color(0xFFB45309))
                DayAttendanceStatus.HOLIDAY -> Triple("Rest Day / Holiday", Color(0xFFF1F5F9), Color(0xFF475569))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(statusBg)
                    .border(1.dp, statusColor.copy(alpha = 0.22f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 11.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Details Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neumorphicLift(RoundedCornerShape(18.dp), tint = Color(0xFF10B981), elevation = 7.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFFDFFFF), Color(0xFFF8FCFA))))
                    .border(1.dp, Color.White, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (day.status == DayAttendanceStatus.PRESENT || day.status == DayAttendanceStatus.LATE) {
                        // Check In / Check Out Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "CHECK IN",
                                    color = MemberCardTokens.SlateMuted,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (day.status == DayAttendanceStatus.LATE) "07:22 AM" else "06:14 AM",
                                    color = MemberCardTokens.DarkNavy,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Column {
                                Text(
                                    text = "CHECK OUT",
                                    color = MemberCardTokens.SlateMuted,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (day.status == DayAttendanceStatus.LATE) "08:18 AM" else "07:38 AM",
                                    color = MemberCardTokens.DarkNavy,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Column {
                                Text(
                                    text = "DURATION",
                                    color = MemberCardTokens.SlateMuted,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (day.status == DayAttendanceStatus.LATE) "56 min" else "84 min",
                                    color = Color(0xFF00E676),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))
                        Spacer(modifier = Modifier.height(14.dp))

                        // Workout Details
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Chest & Triceps Hypertrophy",
                                color = MemberCardTokens.DarkNavy,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "490 kcal burned",
                                    color = MemberCardTokens.SlateMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Avg HR: 138 bpm",
                                    color = MemberCardTokens.SlateMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    } else if (day.status == DayAttendanceStatus.ABSENT) {
                        Text(
                            text = "No gym entry recorded for this day.",
                            color = MemberCardTokens.SlateMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "A rest day or travel period was logged.",
                            color = MemberCardTokens.DarkNavy,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = "Scheduled gym rest / recovery day.",
                            color = MemberCardTokens.DarkNavy,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Recommended: Active mobility, hydration, and 8 hours sleep.",
                            color = MemberCardTokens.SlateMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neumorphicLift(RoundedCornerShape(50.dp), tint = Color(0xFF10B981), elevation = 7.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF19C48A), Color(0xFF0FA875), Color(0xFF07956A))))
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(50.dp))
                    .clickable { onDismissRequest() }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Close",
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.2.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
