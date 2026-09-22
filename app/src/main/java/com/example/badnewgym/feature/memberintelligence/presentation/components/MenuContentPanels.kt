package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot

/* =========================================================
   ATTENDANCE PANEL
   ========================================================= */
@Composable
fun AttendancePanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val att = snapshot.attendance

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Attendance Overview", theme)

        InfoCard(theme) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("This Month", "${att?.visits ?: 16}", colors.textPrimary)
                StatItem("Target", "${att?.target ?: 26}", colors.textSecondary)
                StatItem("Streak", "${att?.streakDays ?: 5} days", Color(0xFF16A34A))
            }
        }

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Recent Check-ins", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                listOf(
                    "Today • 4:03 PM" to "Check-in",
                    "Yesterday • 6:15 PM" to "Workout",
                    "2 days ago • 7:40 AM" to "Check-in",
                    "3 days ago • 5:20 PM" to "Trainer Session"
                ).forEach { (time, type) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(time, color = colors.textSecondary, fontSize = 11.sp)
                        Text(type, color = colors.accent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

/* =========================================================
   PLAN PANEL
   ========================================================= */
@Composable
fun PlanPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    val mem = snapshot.membership

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Membership Plan", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(mem?.planName ?: "Premium Plan", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("${(mem?.daysRemaining ?: 365) / 30} Months", color = colors.textSecondary, fontSize = 12.sp)
                Text("Status: ACTIVE", color = Color(0xFF16A34A), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Plan Benefits", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                listOf(
                    "Unlimited gym access",
                    "Personal trainer sessions",
                    "Nutrition consultation",
                    "Group classes",
                    "Locker & towel service"
                ).forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.CheckCircle, null, tint = Color(0xFF16A34A), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(it, color = colors.textSecondary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

/* =========================================================
   PAYMENT PANEL
   ========================================================= */
@Composable
fun PaymentPanel(snapshot: MemberSnapshot, theme: ThemeId, onCtaClick: () -> Unit) {
    val colors = BADGymTheme.colors
    val pay = snapshot.payment
    val due = pay?.totalOutstanding ?: 0.0

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Payment Status", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (due > 0) "₹${due.toInt()}" else "₹0",
                    color = if (due > 0) Color(0xFFEF4444) else Color(0xFF16A34A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = if (due > 0) "Payment Due" else "All Clear",
                    color = colors.textSecondary,
                    fontSize = 12.sp
                )
            }
        }

        if (due > 0) {
            ThemedCtaButton(theme = theme, onClick = onCtaClick)
        }

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Recent Transactions", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                listOf(
                    "15 Sep • Membership" to "₹4,500",
                    "01 Aug • Personal Training" to "₹2,000",
                    "15 Jul • Membership" to "₹4,500"
                ).forEach { (date, amount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(date, color = colors.textSecondary, fontSize = 11.sp)
                        Text(amount, color = colors.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

/* =========================================================
   TRAINER PANEL
   ========================================================= */
@Composable
fun TrainerPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Your Trainer", theme)

        InfoCard(theme) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.surfaceMuted),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Person, null, tint = colors.accent, modifier = Modifier.size(28.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Coach Arjun", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Strength & Conditioning", color = colors.textSecondary, fontSize = 11.sp)
                    Text("Next session: Tomorrow 7:00 AM", color = colors.accent, fontSize = 11.sp)
                }
            }
        }

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Upcoming Sessions", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                listOf("Tomorrow • 7:00 AM", "Thu • 6:30 PM", "Sat • 8:00 AM").forEach {
                    Text("• $it", color = colors.textSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}

/* =========================================================
   WORKOUT PANEL
   ========================================================= */
@Composable
fun WorkoutPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Workouts", theme)

        InfoCard(theme) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("This Week", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                listOf(
                    "Push Day" to "Chest, Shoulders, Triceps",
                    "Pull Day" to "Back, Biceps",
                    "Legs" to "Quads, Hamstrings, Calves",
                    "Core & Cardio" to "Abs + HIIT"
                ).forEach { (title, muscles) ->
                    Column {
                        Text(title, color = colors.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        Text(muscles, color = colors.textSecondary, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

/* =========================================================
   SERVICES / MORE PANEL
   ========================================================= */
@Composable
fun ServicesPanel(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Services & More", theme)

        listOf(
            "Nutrition Plan" to Icons.Rounded.Restaurant,
            "Group Classes" to Icons.Rounded.Groups,
            "Locker Booking" to Icons.Rounded.Lock,
            "Progress Photos" to Icons.Rounded.PhotoCamera,
            "Refer a Friend" to Icons.Rounded.Share,
            "Settings" to Icons.Rounded.Settings
        ).forEach { (title, icon) ->
            InfoCard(theme) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, null, tint = colors.accent, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(title, color = colors.textPrimary, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                }
            }
        }
    }
}

/* =========================================================
   INSIGHT PANEL
   ========================================================= */
@Composable
fun InsightPanel(
    snapshot: MemberSnapshot,
    signals: List<IntelligenceSignal>,
    theme: ThemeId
) {
    val colors = BADGymTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Member Intelligence Insights", theme)

        if (signals.isEmpty()) {
            InfoCard(theme) {
                Text(
                    text = "All systems green. No active anomalies detected.",
                    color = colors.textSecondary,
                    fontSize = 12.sp
                )
            }
        } else {
            for (signal in signals) {
                IntelligenceSignalCard(
                    signal = signal,
                    emphasized = true
                )
            }
        }
    }
}

/* =========================================================
   SHARED HELPERS
   ========================================================= */
@Composable
private fun SectionTitle(text: String, theme: ThemeId) {
    Text(
        text = text,
        color = BADGymTheme.colors.textPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
    )
}

@Composable
private fun InfoCard(theme: ThemeId, content: @Composable ColumnScope.() -> Unit) {
    val colors = BADGymTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFF1F1B12)
                    ThemeId.BEAST_MODE -> Color(0xFF1C0609)
                    ThemeId.PURPLE_ROYAL -> Color(0xFF22113B)
                    ThemeId.FUTURISTIC_NEON -> Color(0xFF0A1428)
                    ThemeId.VIBRANT_GRADIENT -> Color(0xFFF8FAFC)
                    ThemeId.GLASSMORPHISM -> Color(0xB0FFFFFF)
                    ThemeId.MINIMAL_DARK -> Color(0xFF181C22)
                    ThemeId.NATURAL_FRESH -> Color(0xFFF6FBF7)
                }
            )
            .border(
                1.dp,
                when (theme) {
                    ThemeId.PREMIUM_3D -> Color(0xFF4A3B1D)
                    ThemeId.BEAST_MODE -> Color(0x66FF1E27)
                    ThemeId.PURPLE_ROYAL -> Color(0x66A855F7)
                    ThemeId.FUTURISTIC_NEON -> Color(0x6600E5FF)
                    ThemeId.VIBRANT_GRADIENT -> Color(0xFFE2E8F0)
                    ThemeId.GLASSMORPHISM -> Color(0x80FFFFFF)
                    ThemeId.MINIMAL_DARK -> Color(0xFF28303C)
                    ThemeId.NATURAL_FRESH -> Color(0xFFD1E7D7)
                },
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        content = content
    )
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(label, color = BADGymTheme.colors.textSecondary, fontSize = 10.sp)
    }
}
