package com.example.badnewgym.feature.membercard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.presentation.components.MasterStoryboardDashboard
import com.example.badnewgym.feature.membercard.presentation.components.MemberQuickActionsSheet
import com.example.badnewgym.feature.membercard.presentation.components.ReceiptBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDashboardV2Screen(
    data: MemberCardData = remember { MemberCardData.sampleAshwiniSingh() },
    initialSheet: String? = null,
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showQuickActions by remember { mutableStateOf(false) }
    var showReceipt by remember { mutableStateOf(initialSheet == "receipt") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = modifier.fillMaxSize()) {
        MasterStoryboardDashboard(
            data = data,
            onMenuClick = {
                showQuickActions = true
                onMenuClick()
            },
            onViewReceiptClick = { showReceipt = true },
            onPaymentClick = { /* Payment tab provides the collection and reminder actions. */ }
        )

        if (showQuickActions) {
            MemberQuickActionsSheet(
                onDismissRequest = { showQuickActions = false },
                onSearch = { showQuickActions = false },
                onNotifications = { showQuickActions = false },
                onReceipt = {
                    showQuickActions = false
                    showReceipt = true
                }
            )
        }

        if (showReceipt) {
            ReceiptBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showReceipt = false },
                amountPaid = data.paymentPlan.lastPaymentAmount,
                planName = data.paymentPlan.planName,
                memberName = data.profile.name,
                memberId = data.profile.memberCode,
                date = data.paymentPlan.lastPaymentDate
            )
        }
    }
}

@Composable
fun VerticalRail(
    activeItem: String,
    onItemSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        "Home" to Icons.Default.Home,
        "Members" to Icons.Default.Person,
        "Check-In" to Icons.Default.CheckCircle,
        "Payments" to Icons.Default.ShoppingCart,
        "Attendance" to Icons.Default.DateRange,
        "Workout" to Icons.Default.FitnessCenter,
        "Trainer" to Icons.Default.Face,
        "Nutrition" to Icons.Default.LocalDining,
        "Supplements" to Icons.Default.LocalPharmacy,
        "History" to Icons.Default.History,
        "Insights" to Icons.Default.Insights,
        "More" to Icons.Default.MoreHoriz
    )

    Column(
        modifier = modifier
            .background(Color.White)
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach { (label, icon) ->
            val isActive = activeItem == label
            val contentColor = if (isActive) Color(0xFF00C853) else Color.Gray // Emerald/mint for active
            val bgColor = if (isActive) Color(0xFFE8F5E9) else Color.Transparent
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(bgColor)
                    .clickable { onItemSelect(label) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun ContentViewport(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderBanner()
        MemberBlock()
        PaymentPlanBlock()
        AttendanceBlock()
        WorkoutBlock()
        BodyProgressGrid()
    }
}

@Composable
fun HeaderBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF00C853)) // Emerald primary
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.DirectionsRun, contentDescription = "Run", tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Check-In", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = "Right", tint = Color.White)
    }
}

@Composable
fun MemberBlock() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Aman Tripathi", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("BG305 • 23 yrs • Male", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Badge(containerColor = Color(0xFFE8F5E9), contentColor = Color(0xFF00C853)) {
                    Text("Active", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                }
            }
        }
    }
}

@Composable
fun PaymentPlanBlock() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Current Plan", color = Color.Gray, fontSize = 14.sp)
            Text("12 Month Track", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Due Amount", color = Color.Red, fontSize = 14.sp)
                    Text("Rs 2,800", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Red)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Next Payment", color = Color.Gray, fontSize = 14.sp)
                    Text("14 Oct 2026", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Collect Payment")
            }
        }
    }
}

@Composable
fun AttendanceBlock() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Attendance", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("24 / 30 Days", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(7) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (it < 5) Color(0xFF00C853) else Color.LightGray)
                        )
                    }
                }
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
                CircularProgressIndicator(progress = { 0.9f }, color = Color(0xFF00C853), strokeWidth = 6.dp)
                Text("90%", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun WorkoutBlock() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Workout", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("This Week", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.height(40.dp)
                ) {
                    val heights = listOf(0.4f, 0.6f, 0.8f, 1f, 0.3f, 0.7f, 0.9f)
                    heights.forEach { h ->
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .fillMaxHeight(h)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF00C853))
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Avg Duration", color = Color.Gray, fontSize = 12.sp)
                Text("45 min", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Peak", color = Color.Gray, fontSize = 12.sp)
                Text("1h 15m", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun BodyProgressGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Body Progress", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start = 4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            MetricCard("Weight", "75 kg", "-2 kg", modifier = Modifier.weight(1f))
            MetricCard("Muscle Mass", "32 kg", "+1 kg", modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            MetricCard("Body Fat", "18 %", "-1 %", modifier = Modifier.weight(1f))
            MetricCard("BMI", "24.5", "Normal", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, delta: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = Color.Gray, fontSize = 12.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(delta, color = Color(0xFF00C853), fontSize = 12.sp)
        }
    }
}
