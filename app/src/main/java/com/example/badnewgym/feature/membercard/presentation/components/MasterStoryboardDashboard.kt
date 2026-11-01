package com.example.badnewgym.feature.membercard.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.design.MemberCardAccentManager
import com.example.badnewgym.feature.membercard.design.MemberCardAccentTones
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.MemberCardType
import com.example.badnewgym.feature.membercard.domain.model.MemberProfile
import com.example.badnewgym.feature.membercard.domain.model.PaymentStatus
import com.example.badnewgym.feature.membercard.domain.model.WorkoutTrendBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * BAD GYM — Master Storyboard Dashboard V2
 * Architectural Refactoring:
 * 1. Top Section: 8 Member Pass Showcase (Spark, Power, Silver, Gold, Diamond, Platinum, Elite, Basic Overdue)
 * 2. Sticky Menu Bar: Pins permanently below the pass card on scroll with dynamic color blending!
 * 3. Below Expanded Widgets:
 *    - Focused View: Smooth animated tab switching blending color directly from menu into active widget.
 *    - All Expanded View: Full vertical layout of all 4 widgets with scroll-tracking tab illumination.
 * 4. High-Contrast Typography, Unified 44dp Touch Targets & Advanced Data Presentation Techniques:
 *    - Attendance: 30-Day Calendar Strip with filled status tokens & weekly consistency metrics
 *    - Workout: 62dp Volume Bars Canvas with a derived average guideline & intensity hierarchy
 *    - Payment: 12-Month Capsule Bar Chart with status tracks & prominent ₹2,800 due trigger
 *    - Body Progress: Interactive Metric Selector (Weight, Muscle, Fat, Waist) with calibrated Y-axes
 */
@OptIn(ExperimentalFoundationApi::class)
private fun dashboardDate(offsetDays: Int): Date {
    return Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, offsetDays)
    }.time
}

private fun dashboardDateLabel(offsetDays: Int): String =
    SimpleDateFormat("dd MMM yyyy", Locale.US).format(dashboardDate(offsetDays))

private fun displayDueAmount(data: MemberCardData): String =
    data.paymentPlan.currentDueAmount
        .takeIf { it.isNotBlank() && it != "--" }
        ?: data.paymentPlan.dueAlertAmount.takeIf { it.isNotBlank() && it != "--" }
        ?: "₹0"

private fun sharePaymentMessage(context: Context, message: String) {
    context.startActivity(
        Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            },
            "Share payment message"
        )
    )
}

// -----------------------------------------------------------------------------
// PAYMENT REMINDER CHANNELS
// A reminder can go out over WhatsApp (default when the member is on it), the
// plain phone number (default fallback), an in-app push, or email. The dialog
// preselects the best available channel and fires the matching intent.
// -----------------------------------------------------------------------------

private enum class ReminderChannel(val title: String, val subtitle: String, val icon: ImageVector) {
    WHATSAPP("WhatsApp", "Chat on WhatsApp", Icons.Default.Chat),
    CALL("Call / SMS", "Ring the mobile number", Icons.Default.Phone),
    IN_APP("In-app", "Notify inside BAD GYM app", Icons.Default.Notifications),
    EMAIL("Email", "Send to registered email", Icons.Default.Email)
}

private fun launchReminder(
    context: Context,
    channel: ReminderChannel,
    profile: MemberProfile,
    message: String
) {
    try {
        when (channel) {
            ReminderChannel.WHATSAPP -> context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/${profile.whatsappNumber}?text=${Uri.encode(message)}")
                )
            )
            ReminderChannel.CALL -> context.startActivity(
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${profile.phone.filter { it.isDigit() || it == '+' }}"))
            )
            ReminderChannel.EMAIL -> context.startActivity(
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(profile.email))
                    putExtra(Intent.EXTRA_SUBJECT, "BAD GYM \u2014 Payment Reminder")
                    putExtra(Intent.EXTRA_TEXT, message)
                }
            )
            ReminderChannel.IN_APP -> Toast.makeText(
                context,
                "Reminder sent inside BAD GYM app",
                Toast.LENGTH_SHORT
            ).show()
        }
    } catch (_: Exception) {
        Toast.makeText(context, "No app available for this channel", Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun PaymentReminderDialog(
    profile: MemberProfile,
    dueAmount: String,
    dueDate: String,
    daysLeft: Int,
    accent: Color,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val channels = buildList {
        if (profile.hasWhatsapp) add(ReminderChannel.WHATSAPP)
        add(ReminderChannel.CALL)
        add(ReminderChannel.IN_APP)
        add(ReminderChannel.EMAIL)
    }
    // WhatsApp when available, otherwise the phone number is the default.
    var selected by remember { mutableStateOf(channels.first()) }
    val firstName = profile.name.substringBefore(" ").ifBlank { "there" }
    val message = "BAD GYM: Hi $firstName, your payment of $dueAmount is due on $dueDate " +
        "($daysLeft days left). Please pay to keep your membership active."
    val deep = MemberCardAccentTones.deep(accent)

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(20.dp), color = Color.White) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MemberCardAccentTones.verticalBrush(accent)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Send Payment Reminder",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "$dueAmount \u2022 due $dueDate",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Text(
                    text = "How should we reach $firstName?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8)
                )

                channels.forEach { channel ->
                    ReminderChannelRow(
                        channel = channel,
                        isSelected = channel == selected,
                        accent = accent,
                        onClick = { selected = channel }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1.4f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MemberCardAccentTones.stadiumBrush(accent))
                            .clickable {
                                launchReminder(context, selected, profile, message)
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Send via ${selected.title}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderChannelRow(
    channel: ReminderChannel,
    isSelected: Boolean,
    accent: Color,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (isSelected) MemberCardAccentTones.wash(accent) else Color(0xFFF8FAFC))
            .border(
                1.dp,
                if (isSelected) accent else Color(0xFFEAF0F6),
                shape
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = channel.icon,
            contentDescription = null,
            tint = if (isSelected) accent else Color(0xFF64748B),
            modifier = Modifier.size(20.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = channel.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A)
            )
            Text(
                text = channel.subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF94A3B8)
            )
        }
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .border(2.dp, if (isSelected) accent else Color(0xFFCBD5E1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(accent)
                )
            }
        }
    }
}

private fun dashboardValidity(totalDays: Int, daysLeft: Int): String {
    return "09 Oct 2026 – 07 Nov 2026 (30 Days Plan)"
}

// =============================================================================
// BAD GYM SPONSORED PROMO AD CARD
// =============================================================================

@Composable
fun BadGymSponsoredPromoCard(
    onClaimClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFFAFAFE),
        border = BorderStroke(
            1.2.dp,
            Brush.horizontalGradient(
                listOf(
                    Color(0xFFF59E0B).copy(alpha = 0.8f),
                    Color(0xFFA855F7).copy(alpha = 0.8f),
                    Color(0xFF10B981).copy(alpha = 0.8f)
                )
            )
        ),
        shadowElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .neumorphicLift(RoundedCornerShape(18.dp), tint = Color(0xFFA855F7), elevation = 7.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClaimClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFFFFBEB).copy(alpha = 0.95f),
                            Color(0xFFFAF5FF).copy(alpha = 0.95f),
                            Color(0xFFF0FDF4).copy(alpha = 0.95f)
                        )
                    )
                )
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Left 3D Dumbbell / Supplement Badge
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A))
                                )
                            )
                            .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.illustration_3d_dumbbell_mini),
                            contentDescription = "Nutrition Promo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFFEF3C7),
                                border = BorderStroke(0.6.dp, Color(0xFFF59E0B))
                            ) {
                                Text(
                                    text = "SPONSORED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFB45309),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.5.dp)
                                )
                            }
                            Text(
                                text = "25% OFF",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF7C3AED),
                                maxLines = 1
                            )
                        }

                        Text(
                            text = "Bad Nutrition Labs \u2014 100% Whey Gold",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "27g Isolate \u2022 BCAA \u2022 Code BADPUMP",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Right Claim Offer Button
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFFACC15),
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .clickable { onClaimClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Claim",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "➔",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MasterStoryboardDashboard(
    data: MemberCardData,
    onAvatarClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {},
    onAttendanceDayClick: (com.example.badnewgym.feature.membercard.domain.model.CalendarDay) -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    onViewReceiptClick: () -> Unit = {},
    onBodyProgressClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // The member dashboard always renders the one signed-in pass. It no longer
    // cycles through demo identities when the avatar is tapped.
    val currentPass = remember { BadGymMemberPass.getAll9Passes().first() }

    val hasDues = data.paymentPlan.dueAlertAmount.isNotEmpty() ||
        data.paymentPlan.currentDueAmount.isNotEmpty()

    var currentCardType by remember { mutableStateOf(MemberCardType.TRAINER_CHECK_IN) }
    var currentPaymentStatus by remember {
        mutableStateOf(if (hasDues) PaymentStatus.PENDING else PaymentStatus.DONE)
    }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val daysPassed = currentPass.totalDays - currentPass.daysLeft
    val pct = ((daysPassed.toFloat() / currentPass.totalDays.toFloat()) * 100).toInt()

    // A plan only counts as settled when the record reports PAID *and* carries no
    // outstanding balance, so the header can never contradict the payment tab.
    val isPlanPaid = remember(data) {
        data.paymentPlan.paymentStatus.contains("PAID", ignoreCase = true) &&
            (data.paymentPlan.currentDueAmount.isBlank() ||
                data.paymentPlan.currentDueAmount.contains("\u20B90") ||
                data.paymentPlan.currentDueAmount.endsWith(" 0"))
    }

    // Payment markers projected onto the attendance calendar (only when the
    // date falls inside the month currently on screen).
    val monthShort = data.attendance.monthLabel.take(3)
    fun dayInMonth(raw: String): Int? {
        val match = Regex("(\\d{1,2})\\s+([A-Za-z]{3})").find(raw) ?: return null
        val month = match.groupValues[2]
        return if (month.equals(monthShort, ignoreCase = true)) {
            match.groupValues[1].toIntOrNull()
        } else {
            null
        }
    }
    val dueDay = remember(data) { dayInMonth(data.paymentPlan.currentDueDate) }
    val paidDay = remember(data) { dayInMonth(data.paymentPlan.lastPaymentDate) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFE1F6F0),
                        Color(0xFFEDF4FD),
                        Color(0xFFE8F7F1),
                        Color(0xFFE6F7F0)
                    )
                )
            )
    ) {
        // Ambient light: two soft blooms give the frosted cards something to refract.
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 70.dp, y = (-80).dp)
                .size(280.dp)
                .background(Brush.radialGradient(listOf(Color(0x3822C3E6), Color(0x00000000))))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-90).dp, y = 100.dp)
                .size(320.dp)
                .background(Brush.radialGradient(listOf(Color(0x3010B981), Color(0x00000000))))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 10.dp)
        ) {
        Spacer(modifier = Modifier.height(6.dp))

        // =====================================================================
        // 1. PINNED MEMBER CARD (identity, stats, plan progress)
        // =====================================================================
        UnifiedBadGymMemberCard(
            planTitle = currentPass.tier.title,
            checkInTime = "Today \u2022 08:49 AM",
            isActive = !currentPass.isOverdue,
            cardType = currentCardType,
            paymentStatus = currentPaymentStatus,
            paidAmount = if (currentPaymentStatus == PaymentStatus.PENDING || hasDues) {
                displayDueAmount(data)
            } else {
                "\u20B90"
            },
            paidDate = data.paymentPlan.currentDueDate,
            name = currentPass.name,
            memberId = currentPass.memberId,
            age = currentPass.age,
            gender = currentPass.gender,
            quote = currentPass.quote,
            validity = dashboardValidity(currentPass.totalDays, currentPass.daysLeft),
            avatarRes = currentPass.avatarRes,
            height = currentPass.height,
            weight = "69.5 kg",
            bmi = "22.8",
            bmiCategory = "Normal",
            goal = currentPass.goal,
            goalSubtitle = "Regular Training",
            planName = data.paymentPlan.planName.substringBefore(" ("),
            isPaid = isPlanPaid,
            daysPassed = daysPassed,
            totalDays = currentPass.totalDays,
            percentage = pct,
            onCheckInClick = {
                selectedTabIndex = 0
                currentCardType = MemberCardType.TRAINER_CHECK_IN
                onAttendanceClick()
            },
            onPaymentHeaderClick = {
                currentCardType = MemberCardType.PAYMENT
                selectedTabIndex = 2
                onPaymentClick()
            },
            onPlanClick = {
                selectedTabIndex = 2
                currentCardType = MemberCardType.PAYMENT
            },
            onAvatarClick = onMenuClick,
            onMenuClick = onMenuClick,
            onGoalClick = onBodyProgressClick,
            onProgressClick = {
                selectedTabIndex = 0
                currentCardType = MemberCardType.CHECK_IN
                onAttendanceClick()
            },
            // Plan health rides inside the same glass card as the identity block.
            statusLabel = if (currentPass.isOverdue) "Overdue" else "On Track",
            paymentLabel = if (isPlanPaid) "Payment Paid" else "Payment Due",
            attendanceLabel = "Attendance ${data.attendance.percent}%",
            weightLabel = "Weight \u2193 2.5 kg"
        )

        Spacer(modifier = Modifier.height(7.dp))

        // =====================================================================
        // 2. PINNED 4-TAB COLOR DOCK
        // =====================================================================
        MenuRow4Tabs(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { index ->
                selectedTabIndex = index
                currentCardType = when (index) {
                    0 -> MemberCardType.TRAINER_CHECK_IN
                    1 -> MemberCardType.CHECK_IN
                    2 -> MemberCardType.PAYMENT
                    else -> MemberCardType.MEMBER_PASS
                }
                coroutineScope.launch {
                    lazyListState.scrollToItem(0)
                }
            },
            attendanceRate = "${data.attendance.percent}%",
            workoutAvg = "${workoutAvgMinutes(data.workoutTrend.bars)} min",
            paymentDue = displayDueAmount(data),
            bodyWeight = "69.5 kg",
            attendanceProgress = data.attendance.percent / 100f,
            workoutProgress = workoutActiveDays(data.workoutTrend.bars).toFloat() /
                data.workoutTrend.bars.size.coerceAtLeast(1),
            paymentProgress = data.paymentPlan.planDaysUsed.toFloat() /
                data.paymentPlan.planTotalDays.coerceAtLeast(1),
            bodyProgress = 0.74f,
            paymentHasAlert = false
        )

        Spacer(modifier = Modifier.height(7.dp))

        // =====================================================================
        // 3. SCROLLABLE SECTION CONTENT
        // =====================================================================
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clipToBounds(),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            when (selectedTabIndex) {
                0 -> {
                    item(key = "tab_attendance_calendar") {
                        AttendanceSection(
                            attendance = data.attendance,
                            onAttendanceClick = onAttendanceClick,
                            onDayClick = onAttendanceDayClick,
                            paidDay = paidDay,
                            dueDay = dueDay,
                            monthRecords = attendanceMonthHistory(data.attendance.percent)
                        )
                    }
                    item(key = "tab_attendance_trend") {
                        MonthlyTrendSection(
                            currentPercent = data.attendance.percent,
                            monthLabel = data.attendance.monthLabel
                        )
                    }
                }
                1 -> {
                    item(key = "tab_workout") {
                        MasterWorkoutTrendCard(data = data, onClick = onWorkoutClick)
                    }
                }
                2 -> {
                    item(key = "tab_payment") {
                        MasterPaymentCard(
                            data = data,
                            onClick = onPaymentClick,
                            onViewReceiptClick = onViewReceiptClick
                        )
                    }
                }
                3 -> {
                    item(key = "tab_body") {
                        MasterBodyProgressCard(data = data, onClick = onBodyProgressClick)
                    }
                }
            }

            // Member offer strip. Closes every tab with real content instead of a
            // bare wall of background, and gives the "Claim" CTA somewhere to land.
            item(key = "sponsored_promo") {
                BadGymSponsoredPromoCard(onClaimClick = onMenuClick)
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        }
    }
}

// =============================================================================
// HERO PASS HEADER
// =============================================================================

// =============================================================================
// HERO PASS HEADER
// =============================================================================

@Composable
fun MasterHeroHeader(
    data: MemberCardData,
    cardType: MemberCardType = MemberCardType.MEMBER_PASS,
    paymentStatus: PaymentStatus? = null,
    onCardTypeCycle: () -> Unit = {},
    onPaymentStatusCycle: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val passes = remember { BadGymMemberPass.getAll9Passes() }
    val currentTier = com.example.badnewgym.feature.membercard.design.MemberCardThemeManager.activeTheme.tier
    val activePassIndex = remember(currentTier) {
        val idx = passes.indexOfFirst { it.tier == currentTier }
        if (idx >= 0) idx else 0
    }

    BadGymPassShowcase(
        passes = passes,
        selectedIndex = activePassIndex,
        cardType = cardType,
        paymentStatus = paymentStatus,
        onPassSelected = { index ->
            val pass = passes.getOrNull(index) ?: passes.first()
            val theme = com.example.badnewgym.feature.membercard.design.MemberCardTheme.entries.firstOrNull { it.tier == pass.tier }
                ?: com.example.badnewgym.feature.membercard.design.MemberCardTheme.SILVER_6_MONTH
            com.example.badnewgym.feature.membercard.design.MemberCardThemeManager.activeTheme = theme
        },
        onCardTypeCycle = onCardTypeCycle,
        onPaymentStatusCycle = onPaymentStatusCycle,
        onAvatarClick = { _ -> onAvatarClick() },
        modifier = modifier
    )
}

// =============================================================================
// 1. MASTER ATTENDANCE CARD (Pixel-Perfect Target Matching media_1793271128777_8bf1743c.jpg)
// =============================================================================

@Composable
fun MasterAttendanceCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    isBlendedWithTab: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // CARD 1: 30-Day Attendance Dashboard Card
        Attendance30DayTrendCard()

        // CARD 2: Key Insights Card
        AttendanceKeyInsightsCard()
    }
}

// -----------------------------------------------------------------------------
// CARD 1: 30-DAY ATTENDANCE DASHBOARD CARD
// -----------------------------------------------------------------------------

@Composable
private fun Attendance30DayTrendCard() {
    val cardShape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(Color(0xFFF8FEFA))
            .border(1.2.dp, Color(0xFFA7F3D0), cardShape)
            .padding(horizontal = 9.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // 1. Header & Hero Metric Section with Right Legend Box
            AttendanceHeaderSection()

            // 2. 30-Day Numbered Attendance Circles (Rows 1..15, 16..30)
            Attendance30DayCirclesSection()

            // 3. Monthly Attendance Trend (12-Month Bar Chart)
            AttendanceMonthlyTrendSection()
        }
    }
}

@Composable
private fun AttendanceHeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: 3D Calendar Illustration
        Image(
            painter = painterResource(id = R.drawable.illustration_3d_attendance_calendar),
            contentDescription = "Attendance 3D Calendar",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(52.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        // Middle: Title + Month Pager + 90% Hero + 27/30 Days
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.5.dp)
        ) {
            // Row 1: Green Badge with Star + "Attendance"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Text(
                    text = "Attendance",
                    fontSize = 18.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    letterSpacing = (-0.3).sp
                )
            }

            // Row 2: "Oct 2026" + < > arrows
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Oct 2026",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569)
                )
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .border(0.8.dp, Color(0xFFCBD5E1), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "‹",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569),
                        modifier = Modifier.offset(y = (-1).dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .border(0.8.dp, Color(0xFFCBD5E1), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "›",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569),
                        modifier = Modifier.offset(y = (-1).dp)
                    )
                }
            }

            // Row 3: 90% Hero Metric
            Text(
                text = "90%",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF047857),
                letterSpacing = (-1.2).sp,
                lineHeight = 36.sp
            )

            // Row 4: 27 / 30 Days
            Text(
                text = "27 / 30 Days",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Right: 5-Row Legend Box
        AttendanceLegendPod()
    }
}

@Composable
private fun AttendanceLegendPod() {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 7.dp, vertical = 3.5.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        AttendanceLegendLine(color = Color(0xFF10B981), label = "Present", count = "27")
        AttendanceLegendLine(color = Color(0xFFEF4444), label = "Absent", count = "1", isAlert = true)
        AttendanceLegendLine(color = Color(0xFF8B5CF6), label = "Holiday", count = "1")
        AttendanceLegendLine(color = Color(0xFF94A3B8), label = "Sunday", count = "1")
        AttendanceLegendLine(color = Color(0xFFCBD5E1), label = "Not a Member", count = "0", isHollow = true)
    }
}

@Composable
private fun AttendanceLegendLine(
    color: Color,
    label: String,
    count: String,
    isHollow: Boolean = false,
    isAlert: Boolean = false
) {
    Row(
        modifier = Modifier.width(118.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.5.dp)
        ) {
            if (isHollow) {
                Box(
                    modifier = Modifier
                        .size(6.5.dp)
                        .clip(CircleShape)
                        .border(1.dp, color, CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(6.5.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
            )
        }
        Text(
            text = count,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = if (isAlert) Color(0xFFEF4444) else Color(0xFF0F172A)
        )
    }
}

// -----------------------------------------------------------------------------
// 30-DAY NUMBERED ATTENDANCE CIRCLES SECTION
// -----------------------------------------------------------------------------

private enum class AttendanceDayType {
    PRESENT, LATE, ABSENT, SUNDAY, EMPTY
}

@Composable
private fun Attendance30DayCirclesSection() {
    val row1Statuses = remember {
        listOf(
            AttendanceDayType.PRESENT, // 1
            AttendanceDayType.PRESENT, // 2
            AttendanceDayType.PRESENT, // 3
            AttendanceDayType.SUNDAY,  // 4
            AttendanceDayType.PRESENT, // 5
            AttendanceDayType.LATE,    // 6 (Amber)
            AttendanceDayType.PRESENT, // 7
            AttendanceDayType.PRESENT, // 8
            AttendanceDayType.PRESENT, // 9
            AttendanceDayType.PRESENT, // 10
            AttendanceDayType.SUNDAY,  // 11
            AttendanceDayType.PRESENT, // 12
            AttendanceDayType.PRESENT, // 13
            AttendanceDayType.PRESENT, // 14
            AttendanceDayType.ABSENT   // 15 (Red)
        )
    }

    val row2Statuses = remember {
        listOf(
            AttendanceDayType.PRESENT, // 16
            AttendanceDayType.PRESENT, // 17
            AttendanceDayType.SUNDAY,  // 18
            AttendanceDayType.PRESENT, // 19
            AttendanceDayType.PRESENT, // 20
            AttendanceDayType.PRESENT, // 21
            AttendanceDayType.PRESENT, // 22
            AttendanceDayType.PRESENT, // 23
            AttendanceDayType.PRESENT, // 24
            AttendanceDayType.SUNDAY,  // 25
            AttendanceDayType.PRESENT, // 26
            AttendanceDayType.PRESENT, // 27
            AttendanceDayType.EMPTY,   // 28
            AttendanceDayType.EMPTY,   // 29
            AttendanceDayType.EMPTY    // 30
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        // Row 1: Days 1 to 15
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (day in 1..15) {
                AttendanceDayCircle(day = day, type = row1Statuses[day - 1])
            }
        }

        // Row 2: Days 16 to 30
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (day in 16..30) {
                AttendanceDayCircle(day = day, type = row2Statuses[day - 16])
            }
        }
    }
}

@Composable
private fun AttendanceDayCircle(
    day: Int,
    type: AttendanceDayType
) {
    val (bg, textColor, hasBorder) = when (type) {
        AttendanceDayType.PRESENT -> Triple(Color(0xFF059669), Color.White, false)
        AttendanceDayType.LATE -> Triple(Color(0xFFF59E0B), Color.White, false)
        AttendanceDayType.ABSENT -> Triple(Color(0xFFEF4444), Color.White, false)
        AttendanceDayType.SUNDAY -> Triple(Color(0xFFEEF2F6), Color(0xFF64748B), false)
        AttendanceDayType.EMPTY -> Triple(Color.White, Color(0xFF94A3B8), true)
    }

    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(bg)
            .then(
                if (hasBorder) Modifier.border(0.9.dp, Color(0xFFE2E8F0), CircleShape)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Black,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

// -----------------------------------------------------------------------------
// MONTHLY ATTENDANCE TREND SECTION (12-Month Bar Chart)
// -----------------------------------------------------------------------------

private data class MonthlyAttendanceBar(
    val month: String,
    val pctLabel: String?,
    val heightDp: Float,
    val daysCount: String? = null,
    val isNoMember: Boolean = false,
    val isLow: Boolean = false,
    val isBest: Boolean = false,
    val isActiveMonth: Boolean = false
)

@Composable
private fun AttendanceMonthlyTrendSection() {
    val monthlyData = remember {
        listOf(
            MonthlyAttendanceBar("Jan", "100%", 36f, daysCount = "30"),
            MonthlyAttendanceBar("Feb", "85%", 25f, daysCount = "24"),
            MonthlyAttendanceBar("Mar", "60%", 15f, daysCount = "18", isLow = true),
            MonthlyAttendanceBar("Apr", null, 0f, isNoMember = true),
            MonthlyAttendanceBar("May", "90%", 28f, daysCount = "27"),
            MonthlyAttendanceBar("Jun", "70%", 18f, daysCount = "21", isLow = true),
            MonthlyAttendanceBar("Jul", "95%", 33f, daysCount = "29"),
            MonthlyAttendanceBar("Aug", null, 0f, isNoMember = true),
            MonthlyAttendanceBar("Sep", "80%", 23f, daysCount = "24"),
            MonthlyAttendanceBar("Oct", "90%", 28f, daysCount = "27", isActiveMonth = true),
            MonthlyAttendanceBar("Nov", "75%", 20f, daysCount = "23", isLow = true),
            MonthlyAttendanceBar("Dec", "100%", 36f, daysCount = "30", isBest = true)
        )
    }

    val trendShape = RoundedCornerShape(13.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(trendShape)
            .background(Color(0xFFF0FDF4))
            .border(1.2.dp, Color(0xFFA7F3D0), trendShape)
            .padding(horizontal = 6.dp, vertical = 5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Header: Icon + Monthly Attendance Trend | 12 Months ⌵
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // Mini 3-bar icon (emerald, violet, cyan)
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                        modifier = Modifier.height(13.dp)
                    ) {
                        Box(Modifier.width(3.dp).height(8.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF10B981)))
                        Box(Modifier.width(3.dp).height(13.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF8B5CF6)))
                        Box(Modifier.width(3.dp).height(10.dp).clip(RoundedCornerShape(1.dp)).background(Color(0xFF06B6D4)))
                    }
                    Text(
                        text = "Monthly Attendance Trend",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                }

                TimeframeDropdownPill(text = "12 Months") // Attendance has a real 6/12 selector
            }

            Spacer(modifier = Modifier.height(1.dp))

            // 12 Monthly Bars Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                monthlyData.forEach { item ->
                    val barBrush = if (item.isLow) {
                        Brush.verticalGradient(listOf(Color(0xFFFCD34D), Color(0xFFF59E0B)))
                    } else {
                        Brush.verticalGradient(listOf(Color(0xFF34D399), Color(0xFF059669)))
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Best / Min callouts
                        if (item.isBest) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .border(0.6.dp, Color(0xFF10B981), RoundedCornerShape(3.dp))
                                    .padding(horizontal = 2.dp, vertical = 0.5.dp)
                            ) {
                                Text(
                                    text = "★ Best",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF047857),
                                    maxLines = 1,
                                    lineHeight = 10.sp
                                )
                            }
                        } else if (item.isLow && item.month == "Mar") {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .border(0.6.dp, Color(0xFFF59E0B), RoundedCornerShape(3.dp))
                                    .padding(horizontal = 2.dp, vertical = 0.5.dp)
                            ) {
                                Text(
                                    text = "▼ Min",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFB45309),
                                    maxLines = 1,
                                    lineHeight = 10.sp
                                )
                            }
                        }

                        if (item.pctLabel != null) {
                            Text(
                                text = item.pctLabel,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black,
                                color = if (item.isActiveMonth) Color(0xFF059669) else Color(0xFF475569),
                                maxLines = 1
                            )
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Spacer(modifier = Modifier.height(1.5.dp))

                        if (item.isNoMember) {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(22.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(0.8.dp, Color(0xFFCBD5E1), RoundedCornerShape(3.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No\nMem",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 10.5.sp,
                                    maxLines = 2,
                                    softWrap = false
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(item.heightDp.dp)
                                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                                    .background(barBrush)
                                    .then(
                                        if (item.isActiveMonth) {
                                            Modifier.border(1.2.dp, Color(0xFF047857), RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                                        } else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (item.daysCount != null) {
                                    Text(
                                        text = item.daysCount,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Subtle continuous horizontal baseline
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(Color(0xFFE2E8F0).copy(alpha = 0.7f))
            )

            // Month labels row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                monthlyData.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .then(
                                if (item.isActiveMonth) {
                                    Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .border(0.8.dp, Color(0xFF059669), RoundedCornerShape(4.dp))
                                        .padding(vertical = 1.dp)
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.month,
                            fontSize = 12.sp,
                            fontWeight = if (item.isActiveMonth) FontWeight.Black else FontWeight.Bold,
                            color = if (item.isActiveMonth) Color(0xFF059669) else Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// CARD 2: KEY INSIGHTS CARD
// -----------------------------------------------------------------------------

@Composable
private fun AttendanceKeyInsightsCard() {
    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(Color(0xFFF8FEFA))
            .border(1.dp, Color(0xFFA7F3D0).copy(alpha = 0.8f), cardShape)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Header: Purple Lightbulb + "Key Insights"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8B5CF6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Text(
                    text = "Key Insights",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
            }

            // 3 Pods Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pod 1: Best Month (100% Dec 2026)
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(0.5.dp)) {
                        Text(
                            text = "100%",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF059669)
                        )
                        Text(
                            text = "Best Month",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "Dec 2026",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(28.dp)
                        .background(Color(0xFFE2E8F0))
                )

                // Pod 2: Lowest Month (60% Mar 2026)
                Row(
                    modifier = Modifier
                        .weight(1.05f)
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(0.5.dp)) {
                        Text(
                            text = "60%",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Lowest Month",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "Mar 2026",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(28.dp)
                        .background(Color(0xFFE2E8F0))
                )

                // Pod 3: Next Month Target (21 Days to maintain 90%+)
                Row(
                    modifier = Modifier
                        .weight(1.2f)
                        .padding(start = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEEF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(0.5.dp)) {
                        Text(
                            text = "21 Days",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Next Month Target",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "to maintain 90%+",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// 2. MASTER WORKOUT TREND CARD (Pixel-Perfect Target Matching media_1793268101617_bba53a00.jpg)
// =============================================================================

@Composable
fun MasterWorkoutTrendCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    isBlendedWithTab: Boolean = false,
    modifier: Modifier = Modifier
) {
    val accent = MemberCardAccentManager.activeAccent.color
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // CARD 1: Workout Analytics (Trend)
        Workout30DayTrendCard(
            monthLabel = data.workoutTrend.monthLabel,
            bars = data.workoutTrend.bars,
            accent = accent
        )

        // CARD 2: Monthly Pattern
        WorkoutMonthlyPatternCard(accent = accent)
    }
}

// -----------------------------------------------------------------------------
// CARD 1: 30-DAY WORKOUT TREND CARD
// -----------------------------------------------------------------------------

@Composable
private fun Workout30DayTrendCard(
    monthLabel: String,
    bars: List<WorkoutTrendBar>,
    accent: Color
) {
    val cardShape = RoundedCornerShape(24.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neumorphicLift(cardShape, tint = accent, elevation = 8.dp)
            .clip(cardShape)
            .background(MemberCardAccentTones.cardWash(accent))
            .border(1.4.dp, MemberCardAccentTones.cardEdge(accent), cardShape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Header (icon + title + month pill)
        WorkoutHeaderSection(monthLabel, accent)

        // 2. 30-Day spline chart with the stats blended into its lower band
        Workout30DayBarChartSection(bars, accent)
    }
}

@Composable
private fun WorkoutHeaderSection(monthLabel: String, accent: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Accent icon tile
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(MemberCardAccentTones.verticalBrush(accent)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Text(
                text = "Workout Analytics",
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Daily Workout Trend \u2022 ${shortMonthLabel(monthLabel)}",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF64748B)
            )
        }

        MonthDropdownPill(text = shortMonthLabel(monthLabel))
    }
}

/** "October 2026" -> "Oct 2026" */
private fun shortMonthLabel(monthLabel: String): String {
    val parts = monthLabel.trim().split(" ")
    val mon = parts.firstOrNull()?.take(3) ?: monthLabel
    val yr = parts.getOrNull(1) ?: ""
    return if (yr.isEmpty()) mon else "$mon $yr"
}

@Composable
private fun MonthDropdownPill(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF64748B),
            modifier = Modifier.size(16.dp)
        )
    }
}

// -----------------------------------------------------------------------------
// 4 QUICK KPI PODS ROW
// -----------------------------------------------------------------------------

@Composable
private fun KpiDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(34.dp)
            .background(Color(0xFFE7EDF3))
    )
}

// -----------------------------------------------------------------------------
// 30-DAY DAILY BAR CHART SECTION
// -----------------------------------------------------------------------------

private data class DailyWorkoutBar(
    val day: Int,
    val minutes: Int
)

private val workoutBars: List<DailyWorkoutBar> = listOf(
    DailyWorkoutBar(1, 55),
    DailyWorkoutBar(2, 0),
    DailyWorkoutBar(3, 32),
    DailyWorkoutBar(4, 60),
    DailyWorkoutBar(5, 25),
    DailyWorkoutBar(6, 22),
    DailyWorkoutBar(7, 0),
    DailyWorkoutBar(8, 48),
    DailyWorkoutBar(9, 58),
    DailyWorkoutBar(10, 92),
    DailyWorkoutBar(11, 0),
    DailyWorkoutBar(12, 52),
    DailyWorkoutBar(13, 60),
    DailyWorkoutBar(14, 50),
    DailyWorkoutBar(15, 120),
    DailyWorkoutBar(16, 65),
    DailyWorkoutBar(17, 0),
    DailyWorkoutBar(18, 50),
    DailyWorkoutBar(19, 60),
    DailyWorkoutBar(20, 0),
    DailyWorkoutBar(21, 0),
    DailyWorkoutBar(22, 95),
    DailyWorkoutBar(23, 90),
    DailyWorkoutBar(24, 92),
    DailyWorkoutBar(25, 98),
    DailyWorkoutBar(26, 95),
    DailyWorkoutBar(27, 0),
    DailyWorkoutBar(28, 90),
    DailyWorkoutBar(29, 0),
    DailyWorkoutBar(30, 62)
)

private fun workoutActiveDays(bars: List<WorkoutTrendBar>): Int = bars.count { it.minutes > 0 }

private fun workoutAvgMinutes(bars: List<WorkoutTrendBar>): Int {
    val active = workoutActiveDays(bars).coerceAtLeast(1)
    return Math.round(bars.sumOf { it.minutes }.toFloat() / active)
}

private fun workoutPeakMinutes(bars: List<WorkoutTrendBar>): Int = bars.maxOfOrNull { it.minutes } ?: 0

/** Catmull-Rom → cubic Bézier: a smooth, non-overshooting spline through [pts]. */
private fun buildSmoothPath(pts: List<Offset>): Path {
    val path = Path()
    if (pts.isEmpty()) return path
    path.moveTo(pts.first().x, pts.first().y)
    if (pts.size == 1) return path
    for (i in 0 until pts.size - 1) {
        val p0 = pts.getOrElse(i - 1) { pts[i] }
        val p1 = pts[i]
        val p2 = pts[i + 1]
        val p3 = pts.getOrElse(i + 2) { p2 }
        path.cubicTo(
            p1.x + (p2.x - p0.x) / 6f, p1.y + (p2.y - p0.y) / 6f,
            p2.x - (p3.x - p1.x) / 6f, p2.y - (p3.y - p1.y) / 6f,
            p2.x, p2.y
        )
    }
    return path
}

@Composable
private fun Workout30DayBarChartSection(bars: List<WorkoutTrendBar>, accent: Color) {
    val peakBar = bars.maxByOrNull { it.minutes }
    val peakIndex = peakBar?.let { bars.indexOf(it) } ?: -1
    val activeSorted = bars.filter { it.minutes > 0 }.sortedBy { it.minutes }
    val troughDays = setOfNotNull(activeSorted.getOrNull(0)?.dayNumber, activeSorted.getOrNull(1)?.dayNumber)
    val avgMin = if (bars.isNotEmpty())
        Math.round(bars.sumOf { it.minutes }.toFloat() / bars.size).toInt() else 0
    val maxScale = 90f

    // Merged KPI + insight values, blended into the graph footer.
    val totalMinutes = bars.sumOf { it.minutes }
    val totalDays = bars.size
    val activeDays = workoutActiveDays(bars)
    val totalHr = "%.1f".format(totalMinutes / 60.0)
    val peakMin = peakBar?.minutes ?: 0
    val lowMin = activeSorted.firstOrNull()?.minutes ?: 0
    val consistency = if (totalDays > 0) activeDays * 100 / totalDays else 0

    val green = Color(0xFF16A34A)
    val greenBright = Color(0xFF4ADE80)
    val greenDeep = Color(0xFF0E9F6E)
    val red = Color(0xFFEF4444)
    val redBright = Color(0xFFFF6B6B)

    val headroom = 26.dp
    val plotHeight = 100.dp
    val boxHeight = headroom + plotHeight

    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { revealed = true }
    val reveal by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "workoutChartReveal"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(boxHeight)
        ) {
            // Left Y-Axis (aligned to the plot region)
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(boxHeight)
                    .padding(end = 6.dp)
            ) {
                listOf("90m", "68m", "45m", "23m", "0m").forEachIndexed { idx, label ->
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        lineHeight = 11.sp,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(y = headroom + plotHeight * (idx / 4f) - 5.dp)
                    )
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .height(boxHeight)
            ) {
                val n = bars.size
                val stepDp = maxWidth / n
                fun nodeY(m: Int) = headroom + plotHeight * (1f - (m / maxScale).coerceIn(0.04f, 1f))

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stepPx = size.width / n
                    val headPx = headroom.toPx()
                    val plotPx = plotHeight.toPx()
                    fun xFor(i: Int) = i * stepPx + stepPx / 2f
                    fun yPix(m: Int) = headPx + plotPx * (1f - (m / maxScale).coerceIn(0.04f, 1f))

                    // Vertical dashed day gridlines
                    (0 until n).forEach { i ->
                        drawLine(
                            color = Color(0xFFE2E8F0).copy(alpha = 0.55f),
                            start = Offset(xFor(i), headPx),
                            end = Offset(xFor(i), headPx + plotPx),
                            strokeWidth = 0.6.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 4f), 0f)
                        )
                    }
                    // Horizontal gridlines
                    listOf(0, 23, 45, 68, 90).forEach { g ->
                        drawLine(
                            color = Color(0xFFE2E8F0).copy(alpha = 0.5f),
                            start = Offset(0f, yPix(g)),
                            end = Offset(size.width, yPix(g)),
                            strokeWidth = 0.6.dp.toPx()
                        )
                    }
                    // Average dashed amber line
                    drawLine(
                        color = Color(0xFFF59E0B),
                        start = Offset(0f, yPix(avgMin)),
                        end = Offset(size.width, yPix(avgMin)),
                        strokeWidth = 1.2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 5f), 0f)
                    )

                    // Spline + gradient area fill (revealed left-to-right on entry)
                    val pts = bars.mapIndexed { i, b -> Offset(xFor(i), yPix(b.minutes)) }
                    val line = buildSmoothPath(pts)
                    val area = Path().apply {
                        addPath(line)
                        lineTo(pts.last().x, headPx + plotPx)
                        lineTo(pts.first().x, headPx + plotPx)
                        close()
                    }
                    clipRect(right = size.width * reveal) {
                        drawPath(
                            path = area,
                            brush = Brush.verticalGradient(
                                startY = headPx,
                                endY = headPx + plotPx,
                                colors = listOf(greenBright.copy(alpha = 0.42f), green.copy(alpha = 0.04f))
                            )
                        )
                        drawPath(
                            path = line,
                            color = green,
                            style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )

                        // Nodes
                        bars.forEachIndexed { i, b ->
                            val cx = xFor(i)
                            val cy = yPix(b.minutes)
                            val isTrough = b.dayNumber in troughDays
                            if (isTrough) drawCircle(red.copy(alpha = 0.22f), 7.dp.toPx(), Offset(cx, cy))
                            drawCircle(Color.White, 3.4.dp.toPx(), Offset(cx, cy))
                            drawCircle(if (isTrough) red else green, 2.3.dp.toPx(), Offset(cx, cy))
                        }
                    }
                }

                // Value bubbles. Peak + troughs sit above so the footer band stays clear.
                bars.forEachIndexed { i, b ->
                    val isPeak = i == peakIndex
                    val isTrough = b.dayNumber in troughDays
                    // Only every third day carries a chip so the plot stays readable.
                    val showChip = isPeak || isTrough || i % 3 == 0
                    if (!showChip) return@forEachIndexed
                    val above = if (isPeak || isTrough) true else i % 2 == 0
                    val cx = stepDp * (i + 0.5f)
                    val ny = nodeY(b.minutes)
                    val bubbleY = if (above) ny - 18.dp else ny + 4.dp
                    Box(
                        modifier = Modifier
                            .offset(x = cx - 12.dp, y = bubbleY - (if (isPeak) 14.dp else 0.dp))
                            .width(24.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (isPeak) {
                                Text(
                                    text = "\uD83D\uDC51",
                                    fontSize = 11.sp,
                                    lineHeight = 12.sp,
                                    modifier = Modifier.height(12.dp)
                                )
                            }
                            Text(
                                text = "${b.minutes}m",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                softWrap = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isTrough) Brush.verticalGradient(listOf(redBright, red))
                                        else Brush.verticalGradient(listOf(greenBright, greenDeep))
                                    )
                                    .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                                    .padding(vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // X-Axis day numbers 1..30
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp)
                .height(14.dp)
        ) {
            val stepDp = maxWidth / bars.size
            bars.forEachIndexed { i, b ->
                val isAnchor = b.dayNumber == 1 || b.dayNumber % 5 == 0 || b.dayNumber == bars.size
                if (!isAnchor) return@forEachIndexed
                Text(
                    text = b.dayNumber.toString(),
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = stepDp * (i + 0.5f) - 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // KPI strip — two rows of three so every figure clears the legibility floor.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFFF7FCFA), Color(0xFFEEF8F3))))
                .border(0.9.dp, Color(0xFFDCEFE6), RoundedCornerShape(14.dp))
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                WorkoutStatMini("Avg / day", "${avgMin} min", green, Modifier.weight(1f))
                WorkoutStatMini("Best", "${peakMin} min", greenDeep, Modifier.weight(1f))
                WorkoutStatMini("Lowest", "${lowMin} min", red, Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                WorkoutStatMini("Total", "${totalHr} hr", Color(0xFF0F172A), Modifier.weight(1f))
                WorkoutStatMini("Active days", "$activeDays / $totalDays", Color(0xFF0F172A), Modifier.weight(1f))
                WorkoutStatMini("Consistency", "$consistency%", green, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun WorkoutStatMini(
    label: String,
    value: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7C8FA3),
            maxLines = 1,
            softWrap = false
        )
        Text(
            text = value,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Black,
            color = tint,
            maxLines = 1,
            softWrap = false
        )
    }
}

// -----------------------------------------------------------------------------
// CARD 2: MONTHLY PATTERN CARD
// -----------------------------------------------------------------------------

private data class MonthlyWorkoutItem(
    val month: String,
    val days: Int,
    val hours: Double,
    val isNoMembership: Boolean = false,
    val isPeak: Boolean = false
)

private val monthlyWorkoutPattern: List<MonthlyWorkoutItem> = listOf(
    MonthlyWorkoutItem("Jan", 18, 36.0),
    MonthlyWorkoutItem("Feb", 20, 40.0),
    MonthlyWorkoutItem("Mar", 6, 12.0),
    MonthlyWorkoutItem("Apr", 0, 0.0, isNoMembership = true),
    MonthlyWorkoutItem("May", 12, 24.0),
    MonthlyWorkoutItem("Jun", 16, 32.0),
    MonthlyWorkoutItem("Jul", 22, 44.0, isPeak = true),
    MonthlyWorkoutItem("Aug", 0, 0.0, isNoMembership = true),
    MonthlyWorkoutItem("Sep", 14, 28.0),
    MonthlyWorkoutItem("Oct", 22, 40.0)
)

@Composable
private fun WorkoutMonthlyPatternCard(accent: Color) {
    val cardShape = RoundedCornerShape(24.dp)
    val monthlyData = monthlyWorkoutPattern
    val maxHours = monthlyData.maxOf { it.hours }.coerceAtLeast(1.0)
    val totalHours = monthlyData.sumOf { it.hours }
    val barMaxHeight = 60.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neumorphicLift(cardShape, tint = accent, elevation = 8.dp)
            .clip(cardShape)
            .background(MemberCardAccentTones.cardWash(accent))
            .border(1.4.dp, MemberCardAccentTones.cardEdge(accent), cardShape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header: icon tile + title | total pill
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MemberCardAccentTones.verticalBrush(accent)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Monthly Pattern",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    letterSpacing = (-0.4).sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = "(This Year)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    softWrap = false
                )
            }

            // Total pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
                    .padding(horizontal = 9.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Total: %.1f hr".format(totalHours),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MemberCardAccentTones.deep(accent),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        // Monthly spline / area chart
        val maxScaleH = maxHours
        val headroom = 24.dp
        val plotH = 52.dp
        val boxH = headroom + plotH
        val green = Color(0xFF16A34A)
        val greenBright = Color(0xFF4ADE80)
        val greenDeep = Color(0xFF0E9F6E)

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(boxH)
        ) {
            val n = monthlyData.size
            val stepDp = maxWidth / n
            fun nodeY(h: Double) = headroom + plotH * (1f - (h / maxScaleH).toFloat().coerceIn(0.06f, 1f))

            Canvas(modifier = Modifier.fillMaxSize()) {
                val stepPx = size.width / n
                val headPx = headroom.toPx()
                val plotPx = plotH.toPx()
                fun xFor(i: Int) = i * stepPx + stepPx / 2f
                fun yPix(h: Double) = headPx + plotPx * (1f - (h / maxScaleH).toFloat().coerceIn(0.06f, 1f))

                listOf(0.06f, 0.4f, 0.7f, 1f).forEach { f ->
                    val gy = headPx + plotPx * (1f - f)
                    drawLine(Color(0xFFE2E8F0).copy(alpha = 0.5f), Offset(0f, gy), Offset(size.width, gy), 0.6.dp.toPx())
                }

                val pts = monthlyData.mapIndexedNotNull { i, m ->
                    if (m.isNoMembership) null else Offset(xFor(i), yPix(m.hours))
                }
                if (pts.size >= 2) {
                    val line = buildSmoothPath(pts)
                    val area = Path().apply {
                        addPath(line)
                        lineTo(pts.last().x, headPx + plotPx)
                        lineTo(pts.first().x, headPx + plotPx)
                        close()
                    }
                    drawPath(
                        path = area,
                        brush = Brush.verticalGradient(
                            startY = headPx, endY = headPx + plotPx,
                            colors = listOf(greenBright.copy(alpha = 0.42f), green.copy(alpha = 0.04f))
                        )
                    )
                    drawPath(line, color = green, style = Stroke(width = 2.1.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                }
                // Months with no membership carry no node at all: a dot parked on the
                // baseline reads as "0 hours" instead of "not a member yet".
                monthlyData.forEachIndexed { i, m ->
                    if (m.isNoMembership) return@forEachIndexed
                    val cx = xFor(i)
                    val cy = yPix(m.hours)
                    drawCircle(Color.White, 3.6.dp.toPx(), Offset(cx, cy))
                    drawCircle(green, 2.4.dp.toPx(), Offset(cx, cy))
                }
            }

            monthlyData.forEachIndexed { i, m ->
                // An empty stretch in the curve already says "no membership";
                // parking a dash or a dot on the baseline just adds clutter.
                if (m.isNoMembership) return@forEachIndexed
                val cx = stepDp * (i + 0.5f)
                val cy = nodeY(m.hours)
                if (m.isPeak) {
                    Text(
                        text = "\uD83D\uDC51",
                        fontSize = 12.sp,
                        modifier = Modifier.offset(x = cx - 7.dp, y = cy - 29.dp)
                    )
                }
                Text(
                    text = "%.0fh".format(m.hours),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier
                        .offset(x = cx - 16.dp, y = cy - (if (m.isPeak) 23.dp else 13.dp))
                        .width(32.dp)
                )
                Text(
                    text = "${m.days}d",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier
                        .offset(x = cx - 9.dp, y = cy + 4.dp)
                        .width(18.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Brush.verticalGradient(listOf(greenBright, greenDeep)))
                        .padding(vertical = 1.dp)
                )
            }
        }

        // Month labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
        ) {
            monthlyData.forEach { item ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.month,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

// =============================================================================
// 3. MASTER PAYMENT CARD (Clean White Card #FFFFFF with Stacked Monthly Trend)
// =============================================================================

@Composable
fun MasterPaymentCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    onViewReceiptClick: () -> Unit = {},
    isBlendedWithTab: Boolean = false,
    modifier: Modifier = Modifier
) {
    val accent = MemberCardAccentManager.activeAccent.color
    var showReminder by remember { mutableStateOf(false) }
    val cardShape = if (isBlendedWithTab) {
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 22.dp, bottomEnd = 22.dp)
    } else {
        RoundedCornerShape(22.dp)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .neumorphicLift(cardShape, tint = accent, elevation = 8.dp)
            .clip(cardShape)
            .background(MemberCardAccentTones.cardWash(accent))
            .border(1.4.dp, MemberCardAccentTones.cardEdge(accent), cardShape)
            .clickable { onClick() }
            .padding(horizontal = 11.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // 1. Header + Due Amount hero capsule
        PaymentHeaderHeroSection(
            dueAmount = displayDueAmount(data),
            planName = data.paymentPlan.planName,
            dueDate = data.paymentPlan.currentDueDate,
            daysLeft = data.paymentPlan.planDaysRemaining,
            onPlanHistoryClick = onClick,
            accent = accent
        )

        // 2. Primary payment actions. Every visible action has a destination.
        PaymentActionButtonsRow(
            accent = accent,
            onCollectPayment = onViewReceiptClick,
            onRemind = { showReminder = true },
            onSendReceipt = onViewReceiptClick,
            onMore = onClick
        )

        // 3. Payment trend (model-aligned history)
        PaymentTrendSection(accent = accent)

        // 4. Bottom summary strip (Total Paid, Unpaid Balance, Last Payment)
        PaymentSummaryMetricsRow(
            data = data,
            accent = accent,
            onTotalPaidClick = onClick,
            onUnpaidBalanceClick = onClick,
            onWaivedClick = onClick
        )
    }

    if (showReminder) {
        PaymentReminderDialog(
            profile = data.profile,
            dueAmount = displayDueAmount(data),
            dueDate = data.paymentPlan.currentDueDate,
            daysLeft = data.paymentPlan.planDaysRemaining,
            accent = accent,
            onDismiss = { showReminder = false }
        )
    }
}

// -----------------------------------------------------------------------------
// SECTION 1: HEADER & DUE AMOUNT HERO CARD (Image 2)
// -----------------------------------------------------------------------------

@Composable
private fun PaymentHeaderHeroSection(
    dueAmount: String,
    planName: String,
    dueDate: String,
    daysLeft: Int,
    onPlanHistoryClick: () -> Unit = {},
    accent: Color,
    modifier: Modifier = Modifier
) {
    val deep = MemberCardAccentTones.deep(accent)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Top Row: icon tile + title + Plan History pill
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(MemberCardAccentTones.verticalBrush(accent)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Payment",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = planName.ifBlank { "Membership plan" },
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B)
                )
            }

            // (⏰ Plan History >) Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
                    .clickable { onPlanHistoryClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = deep,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "History",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = deep,
                    maxLines = 1,
                    softWrap = false
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = deep,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        // Due capsule: wallet + due amount + divider + due date / days left
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFEAF0F6), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.illustration_3d_payment_wallet),
                contentDescription = "Wallet",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = "Due Amount",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = dueAmount,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = deep,
                    letterSpacing = (-0.7).sp,
                    lineHeight = 17.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 6.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassEmpty,
                        contentDescription = null,
                        tint = Color(0xFFB45309),
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = "Pending",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFB45309)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(38.dp)
                    .background(Color(0xFFE7EDF3))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = deep,
                        modifier = Modifier.size(13.dp)
                    )
                    Column {
                        Text(
                            text = "Due on",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = dueDate,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassEmpty,
                        contentDescription = null,
                        tint = deep,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "$daysLeft Days Left",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 2: 5 ACTION BUTTONS (Image 2)
// -----------------------------------------------------------------------------

@Composable
private fun PaymentActionButtonsRow(
    accent: Color,
    onCollectPayment: () -> Unit = {},
    onRemind: () -> Unit = {},
    onSendReceipt: () -> Unit = {},
    onMore: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. View Due — solid accent button
        Row(
            modifier = Modifier
                .weight(1.5f)
                .height(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(MemberCardAccentTones.stadiumBrush(accent))
                .clickable { onCollectPayment() }
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "View Due",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                maxLines = 1,
                softWrap = false
            )
        }

        PaymentActionTile(
            icon = Icons.Default.Notifications,
            label = "Remind",
            accent = accent,
            modifier = Modifier.weight(1f),
            onClick = onRemind
        )
        PaymentActionTile(
            icon = Icons.Default.Receipt,
            label = "Receipt",
            accent = accent,
            modifier = Modifier.weight(1.05f),
            onClick = onSendReceipt
        )
        PaymentActionTile(
            icon = Icons.Default.MoreVert,
            label = "More",
            accent = accent,
            modifier = Modifier.weight(0.7f),
            onClick = onMore
        )
    }
}

@Composable
private fun PaymentActionTile(
    icon: ImageVector,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(11.dp)
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Color(0xFFEAF0F6), shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155),
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 3: 6 QUICK ACTION CAPSULES (Image 2)
// -----------------------------------------------------------------------------

@Composable
private fun PaymentQuickActionsRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. + Add Payment
        QuickActionItem(
            icon = Icons.Default.Add,
            label = "Add Payment",
            iconColor = Color(0xFF0284C7),
            bgColor = Color(0xFFF0F9FF),
            borderColor = Color(0xFFBAE6FD),
            modifier = Modifier.weight(1f)
        )
        // 2. ◔ Split Payment
        QuickActionItem(
            icon = Icons.Default.PieChart,
            label = "Split Payment",
            iconColor = Color(0xFF7C3AED),
            bgColor = Color(0xFFFAF5FF),
            borderColor = Color(0xFFDDD6FE),
            modifier = Modifier.weight(1f)
        )
        // 3. 🔗 Payment Link
        QuickActionItem(
            icon = Icons.Default.Link,
            label = "Payment Link",
            iconColor = Color(0xFF4F46E5),
            bgColor = Color(0xFFEEF2FF),
            borderColor = Color(0xFFC7D2FE),
            modifier = Modifier.weight(1f)
        )
        // 4. % Mark Waived
        QuickActionItem(
            icon = Icons.Default.Percent,
            label = "Mark Waived",
            iconColor = Color(0xFF0284C7),
            bgColor = Color(0xFFECFEFF),
            borderColor = Color(0xFFA5F3FC),
            modifier = Modifier.weight(1f)
        )
        // 5. ⚙ Adjust Amount
        QuickActionItem(
            icon = Icons.Default.Tune,
            label = "Adjust Amount",
            iconColor = Color(0xFF0284C7),
            bgColor = Color(0xFFF0F9FF),
            borderColor = Color(0xFFBAE6FD),
            modifier = Modifier.weight(1f)
        )
        // 6. 📄 Add Note
        QuickActionItem(
            icon = Icons.Default.Edit,
            label = "Add Note",
            iconColor = Color(0xFF0284C7),
            bgColor = Color(0xFFF0F9FF),
            borderColor = Color(0xFFBAE6FD),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    iconColor: Color,
    bgColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(bgColor)
            .border(0.7.dp, borderColor, RoundedCornerShape(9.dp))
            .padding(horizontal = 1.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(17.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(10.5.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155),
                textAlign = TextAlign.Center,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 4: PAYMENT TREND STACKED 12-MONTH BAR CHART & LEGEND (Image 2)
// -----------------------------------------------------------------------------

private data class MonthlyPaymentData(
    val month: String,
    val totalLabel: String?,
    val barColor: Color,
    val heightDp: Int,
    val isDashed: Boolean = false
)

@Composable
private fun PaymentTrendSection(accent: Color, modifier: Modifier = Modifier) {
    val subShape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(subShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFEAF0F6), subShape)
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        PaymentTrendHeader(accent)
        PaymentTrendChart(accent)
    }
}

@Composable
private fun PaymentTrendHeader(accent: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MemberCardAccentTones.soft(accent)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(15.dp)
                )
            }
            Text(
                text = "Payment Trend",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                letterSpacing = (-0.3).sp,
                maxLines = 1,
                softWrap = false
            )
        }
        Text(
            text = "Jan \u2013 Oct 2026",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF94A3B8),
            maxLines = 1,
            softWrap = false
        )
    }
}

private fun paymentTrendData(): List<MonthlyPaymentData> = listOf(
    MonthlyPaymentData("Jan", "₹600", Color(0xFF10B981), 16),
    MonthlyPaymentData("Feb", "₹600", Color(0xFF10B981), 16),
    MonthlyPaymentData("Mar", "-", Color(0xFFE2E8F0), 10, isDashed = true),
    MonthlyPaymentData("Apr", "-", Color(0xFFE2E8F0), 10, isDashed = true),
    MonthlyPaymentData("May", "₹1000", Color(0xFF10B981), 27),
    MonthlyPaymentData("Jun", "₹1200", Color(0xFF10B981), 32),
    MonthlyPaymentData("Jul", "₹1800", Color(0xFF10B981), 48),
    MonthlyPaymentData("Aug", "-", Color(0xFFE2E8F0), 10, isDashed = true),
    MonthlyPaymentData("Sep", "₹1200", Color(0xFF10B981), 32),
    MonthlyPaymentData("Oct", "₹500", Color(0xFF10B981), 13)
)

@Composable
private fun PaymentTrendChart(accent: Color) {
    val monthsData = remember { paymentTrendData() }
    val maxV = monthsData.maxOf { it.heightDp }.coerceAtLeast(1)
    val green = Color(0xFF16A34A)
    val greenBright = Color(0xFF4ADE80)
    val greenDeep = Color(0xFF0E9F6E)
    val headroom = 22.dp
    val plotH = 46.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(headroom + plotH)
        ) {
            val n = monthsData.size
            val stepDp = maxWidth / n
            fun nodeY(v: Int) = headroom + plotH * (1f - (v.toFloat() / maxV).coerceIn(0.08f, 1f))

            Canvas(modifier = Modifier.fillMaxSize()) {
                val stepPx = size.width / n
                val headPx = headroom.toPx()
                val plotPx = plotH.toPx()
                fun xFor(i: Int) = i * stepPx + stepPx / 2f
                fun yPix(v: Int) = headPx + plotPx * (1f - (v.toFloat() / maxV).coerceIn(0.08f, 1f))

                val pts = monthsData.mapIndexedNotNull { i, m ->
                    if (m.isDashed) null else Offset(xFor(i), yPix(m.heightDp))
                }
                if (pts.size >= 2) {
                    val line = buildSmoothPath(pts)
                    val area = Path().apply {
                        addPath(line)
                        lineTo(pts.last().x, headPx + plotPx)
                        lineTo(pts.first().x, headPx + plotPx)
                        close()
                    }
                    drawPath(
                        path = area,
                        brush = Brush.verticalGradient(
                            startY = headPx, endY = headPx + plotPx,
                            colors = listOf(greenBright.copy(alpha = 0.4f), green.copy(alpha = 0.04f))
                        )
                    )
                    drawPath(line, color = green, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                }
                // Months without a payment carry no marker: a dot parked on the
                // baseline reads as a "₹0 paid" month rather than "no record".
                monthsData.forEachIndexed { i, m ->
                    if (m.isDashed) return@forEachIndexed
                    val cx = xFor(i)
                    val cy = yPix(m.heightDp)
                    drawCircle(Color.White, 3.2.dp.toPx(), Offset(cx, cy))
                    drawCircle(green, 2.2.dp.toPx(), Offset(cx, cy))
                }
            }

            monthsData.forEachIndexed { i, m ->
                if (m.isDashed) return@forEachIndexed
                val cx = stepDp * (i + 0.5f)
                val isPeak = m.heightDp == maxV
                val cy = nodeY(m.heightDp)
                if (isPeak) {
                    Text(
                        text = "\uD83D\uDC51",
                        fontSize = 11.5.sp,
                        modifier = Modifier.offset(x = cx - 6.dp, y = cy - 19.dp)
                    )
                }
                Text(
                    text = m.totalLabel ?: "\u2013",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    color = greenDeep,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier
                        .offset(x = cx - 15.dp, y = cy - (if (isPeak) 14.dp else 11.dp))
                        .width(30.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        ) {
            monthsData.forEach { m ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = m.month,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        // Dashed months fade back so the gap in the curve reads as
                        // "no payment recorded" rather than a missed bill.
                        color = if (m.isDashed) Color(0xFFB4BECF) else Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 5: BOTTOM 3 SUMMARY METRIC CARDS (Image 2)
// -----------------------------------------------------------------------------

@Composable
private fun PaymentSummaryMetricsRow(
    data: MemberCardData,
    accent: Color,
    onTotalPaidClick: () -> Unit = {},
    onUnpaidBalanceClick: () -> Unit = {},
    onWaivedClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val stripShape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(stripShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFEAF0F6), stripShape)
            .padding(horizontal = 6.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PaymentSummaryCell(
            icon = Icons.Default.CheckCircle,
            label = "Total Paid",
            value = data.paymentPlan.amountPaid,
            accent = accent,
            modifier = Modifier
                .weight(1f)
                .clickable { onTotalPaidClick() }
        )
        KpiDivider()
        PaymentSummaryCell(
            icon = Icons.Default.CenterFocusStrong,
            label = "Unpaid Balance",
            value = displayDueAmount(data),
            accent = accent,
            modifier = Modifier
                .weight(1f)
                .clickable { onUnpaidBalanceClick() }
        )
        KpiDivider()
        PaymentSummaryCell(
            icon = Icons.Default.Receipt,
            label = "Last Payment",
            value = data.paymentPlan.lastPaymentAmount,
            subtitle = data.paymentPlan.lastPaymentDate,
            accent = accent,
            modifier = Modifier
                .weight(1f)
                .clickable { onWaivedClick() }
        )
    }
}

@Composable
private fun PaymentSummaryCell(
    icon: ImageVector,
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF94A3B8),
            maxLines = 1,
            softWrap = false
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0F172A),
            maxLines = 1,
            softWrap = false
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF64748B),
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

// =============================================================================
// 4. MASTER BODY INTELLIGENCE CARD
// =============================================================================

@Composable
fun MasterBodyProgressCard(
    data: MemberCardData,
    onClick: () -> Unit = {},
    isBlendedWithTab: Boolean = false,
    modifier: Modifier = Modifier
) {
    val cardShape = if (isBlendedWithTab) {
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 22.dp, bottomEnd = 22.dp)
    } else {
        RoundedCornerShape(22.dp)
    }

    Surface(
        shape = cardShape,
        color = Color.White,
        border = BorderStroke(1.2.dp, InfographicTokens.Hairline),
        shadowElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .neumorphicLift(cardShape, tint = Color(0xFF2563EB), elevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp, vertical = 8.dp)
        ) {
            BodyIntelligenceSection(data = data, onClick = onClick)
        }
    }
}

// =============================================================================
// 5. MASTER FOOTER BANNER ("Consistent Steps, Brighter Days")
// =============================================================================

@Composable
fun MasterFooterBanner(
    onKeepGoingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF5D22B6),
                        Color(0xFF4A188C),
                        Color(0xFF6D28D9)
                    )
                )
            )
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_3d_footer_dumbbell),
                    contentDescription = "Footer Dumbbell",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(36.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Consistent Steps, ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Brighter Days 彡",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFACC15)
                    )
                }
            }

            // Tactile Gold Action Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFFACC15))
                    .clickable { onKeepGoingClick() }
                    .padding(horizontal = 11.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Keep Going!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "➔",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}

// =============================================================================
// REUSABLE HELPER ATOMS
// =============================================================================

@Composable
private fun TimeframeDropdownPill(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(0.8.dp, Color(0xFFCBD5E1), RoundedCornerShape(50.dp))
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
    }
}

@Composable
private fun LegendDotItem(color: Color, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569)
        )
        if (value.isNotEmpty()) {
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }
    }
}

@Composable
private fun MiniStatCard(
    icon: String,
    title: String,
    value: String,
    valueColor: Color = Color(0xFF0F172A),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 15.sp)
            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569),
                    maxLines = 1
                )
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = valueColor,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
