package com.badgym.memberintelligence

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * BAD GYM — Centralized 8-Theme Visual Engine Tokens.
 * Conforms to MEMBER_INTELLIGENCE_8_THEME_SPEC.md.
 */
enum class BadGymTheme(
    val title: String,
    val category: String,
    val isDark: Boolean,
    val motto: String,
    val bgGradient: List<Color>,
    val surface: Color,
    val surfaceElevated: Color,
    val surfaceMuted: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val accent: Color,
    val accentSecondary: Color,
    val success: Color,
    val warning: Color,
    val danger: Color,
    val ctaGradient: List<Color>,
    val glowColor: Color
) {
    NATURAL_FRESH(
        title = "Natural Fresh",
        category = "Wellness • Botanical • Mint",
        isDark = false,
        motto = "Good Fitness • Brighter You",
        bgGradient = listOf(Color(0xFFE8F5EC), Color(0xFFF2FAF5), Color(0xFFDCEFE3)),
        surface = Color(0xFAFFFFFF),
        surfaceElevated = Color(0xFFFFFFFF),
        surfaceMuted = Color(0xFFE2F1E7),
        border = Color(0xC0D1E7D7),
        textPrimary = Color(0xFF14291B),
        textSecondary = Color(0xFF335C43),
        textMuted = Color(0xFF6B8F77),
        accent = Color(0xFF16A34A),
        accentSecondary = Color(0xFF0F766E),
        success = Color(0xFF16A34A),
        warning = Color(0xFFEA580C),
        danger = Color(0xFFDC2626),
        ctaGradient = listOf(Color(0xFF22C55E), Color(0xFF15803D)),
        glowColor = Color(0x3316A34A)
    ),
    FUTURISTIC_NEON(
        title = "Futuristic Neon",
        category = "Cyberpunk • Midnight Sapphire",
        isDark = true,
        motto = "STRONGER • EVERY DAY",
        bgGradient = listOf(Color(0xFF0E1E38), Color(0xFF132A4D), Color(0xFF193761)),
        surface = Color(0xD9162C4E),
        surfaceElevated = Color(0xF01E3A64),
        surfaceMuted = Color(0x99132542),
        border = Color(0xFF00E5FF),
        textPrimary = Color(0xFFF0FDFA),
        textSecondary = Color(0xFF38BDF8),
        textMuted = Color(0xFF7DD3FC),
        accent = Color(0xFF00E5FF),
        accentSecondary = Color(0xFF39FF88),
        success = Color(0xFF00E5FF),
        warning = Color(0xFFFACC15),
        danger = Color(0xFFFF2D55),
        ctaGradient = listOf(Color(0xFF00E5FF), Color(0xFF0284C7)),
        glowColor = Color(0x5500E5FF)
    ),
    MINIMAL_DARK(
        title = "Minimal Dark",
        category = "Executive Slate • Clean Convex",
        isDark = true,
        motto = "CONSISTENCY • CREATES CHANGE",
        bgGradient = listOf(Color(0xFFE2E8F0), Color(0xFFEDF2F7), Color(0xFFCBD5E1)),
        surface = Color(0xFFF1F5F9),
        surfaceElevated = Color(0xFFFFFFFF),
        surfaceMuted = Color(0xFFE2E8F0),
        border = Color(0xFFCBD5E1),
        textPrimary = Color(0xFF0F172A),
        textSecondary = Color(0xFF475569),
        textMuted = Color(0xFF64748B),
        accent = Color(0xFF3B82F6),
        accentSecondary = Color(0xFF1D4ED8),
        success = Color(0xFF10B981),
        warning = Color(0xFFF59E0B),
        danger = Color(0xFFEF4444),
        ctaGradient = listOf(Color(0xFF334155), Color(0xFF1E293B)),
        glowColor = Color(0x22334155)
    ),
    GLASSMORPHISM(
        title = "Glassmorphism",
        category = "Atmospheric Ice • Translucent",
        isDark = false,
        motto = "More Than A Gym ↗",
        bgGradient = listOf(Color(0xFFD9E8F8), Color(0xFFEBF3FC), Color(0xFFCFE2F5)),
        surface = Color(0xD8FFFFFF),
        surfaceElevated = Color(0xF0FFFFFF),
        surfaceMuted = Color(0x70E0F2FE),
        border = Color(0xB3FFFFFF),
        textPrimary = Color(0xFF0A2540),
        textSecondary = Color(0xFF0284C7),
        textMuted = Color(0xFF475569),
        accent = Color(0xFF0284C7),
        accentSecondary = Color(0xFF14B8A6),
        success = Color(0xFF10B981),
        warning = Color(0xFFF59E0B),
        danger = Color(0xFFEF4444),
        ctaGradient = listOf(Color(0xFF38BDF8), Color(0xFF0284C7)),
        glowColor = Color(0x4038BDF8)
    ),
    PREMIUM_3D(
        title = "Premium 3D",
        category = "Champagne Gold • Luxury Silk",
        isDark = true,
        motto = "Make Fitness A Lifestyle",
        bgGradient = listOf(Color(0xFFF5EFE6), Color(0xFFFAF6F0), Color(0xFFECE3D4)),
        surface = Color(0xFDFBF7),
        surfaceElevated = Color(0xFFFFFFFF),
        surfaceMuted = Color(0xFFF3ECE0),
        border = Color(0xFFE5B842),
        textPrimary = Color(0xFF2C220E),
        textSecondary = Color(0xFF926F15),
        textMuted = Color(0xFFB4975A),
        accent = Color(0xFFD4AF37),
        accentSecondary = Color(0xFFB8860B),
        success = Color(0xFF10B981),
        warning = Color(0xFFD4AF37),
        danger = Color(0xFFDC2626),
        ctaGradient = listOf(Color(0xFFE5B842), Color(0xFFB8860B)),
        glowColor = Color(0x44E5B842)
    ),
    VIBRANT_GRADIENT(
        title = "Vibrant Gradient",
        category = "Aurora Blush • Prismatic Energy",
        isDark = false,
        motto = "Train • Eat • Repeat",
        bgGradient = listOf(Color(0xFFFCE7F3), Color(0xFFF3E8FF), Color(0xFFE0E7FF)),
        surface = Color(0xF2FFFFFF),
        surfaceElevated = Color(0xFFFFFFFF),
        surfaceMuted = Color(0xFFFCE7F3),
        border = Color(0xFFFBCFE8),
        textPrimary = Color(0xFF1E1B4B),
        textSecondary = Color(0xFF6B21A8),
        textMuted = Color(0xFF9333EA),
        accent = Color(0xFFEC4899),
        accentSecondary = Color(0xFF8B5CF6),
        success = Color(0xFF10B981),
        warning = Color(0xFFF97316),
        danger = Color(0xFFEF4444),
        ctaGradient = listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6), Color(0xFFEC4899)),
        glowColor = Color(0x4DEC4899)
    ),
    BEAST_MODE(
        title = "Gym Beast Mode",
        category = "Titanium Ruby • Athletic Crimson",
        isDark = true,
        motto = "NO PAIN • NO GAIN",
        bgGradient = listOf(Color(0xFF3B1017), Color(0xFF4D1720), Color(0xFF2E0B11)),
        surface = Color(0xE645121A),
        surfaceElevated = Color(0xF05A1923),
        surfaceMuted = Color(0x992B080E),
        border = Color(0xFFFF334B),
        textPrimary = Color(0xFFFFF1F2),
        textSecondary = Color(0xFFFDA4AF),
        textMuted = Color(0xFFF43F5E),
        accent = Color(0xFFFF223B),
        accentSecondary = Color(0xFFFF576D),
        success = Color(0xFF10B981),
        warning = Color(0xFFF59E0B),
        danger = Color(0xFFFF223B),
        ctaGradient = listOf(Color(0xFFFF223B), Color(0xFFBE123C)),
        glowColor = Color(0x66FF223B)
    ),
    PURPLE_ROYAL(
        title = "Purple Royal",
        category = "Royal Amethyst • Velvet Orchid",
        isDark = true,
        motto = "Stronger • Fitter • Happier",
        bgGradient = listOf(Color(0xFF2D144E), Color(0xFF3B1C64), Color(0xFF220C3C)),
        surface = Color(0xEB361B5A),
        surfaceElevated = Color(0xF5492478),
        surfaceMuted = Color(0x99220C38),
        border = Color(0xFFC084FC),
        textPrimary = Color(0xFFFAF5FF),
        textSecondary = Color(0xFFE9D5FF),
        textMuted = Color(0xFFC084FC),
        accent = Color(0xFFA855F7),
        accentSecondary = Color(0xFFC026D3),
        success = Color(0xFF10B981),
        warning = Color(0xFFF59E0B),
        danger = Color(0xFFEF4444),
        ctaGradient = listOf(Color(0xFFA855F7), Color(0xFF7E22CE)),
        glowColor = Color(0x55A855F7)
    )
}

enum class MenuSection(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Rounded.Home),
    ATTENDANCE("Attend", Icons.Rounded.CalendarToday),
    PLAN("Plan", Icons.Rounded.Star),
    PAYMENT("Pay", Icons.Rounded.CreditCard),
    TRAINER("Trainer", Icons.Rounded.Person),
    WORKOUT("Workout", Icons.Rounded.FitnessCenter),
    SUPPLEMENTS("Supplements", Icons.Rounded.LocalDrink),
    NUTRITION("Nutrition", Icons.Rounded.Restaurant),
    SERVICES("Services", Icons.Rounded.MiscellaneousServices),
    HISTORY("History", Icons.Rounded.History),
    INSIGHT("Insight", Icons.Rounded.AutoAwesome)
}

/**
 * Production-ready member card profile data.
 */
data class MemberCardProfile(
    val id: String,
    val name: String,
    val code: String,
    val tier: String,
    val plan: String,
    val status: String,
    val isOverdue: Boolean,
    val overdueAmount: String,
    val daysLeft: Int,
    val visits: Int,
    val targetVisits: Int,
    val workouts: Int,
    val coachName: String,
    val routineName: String,
    val initialLetter: String
)

val SampleMembers = listOf(
    MemberCardProfile(
        id = "1",
        name = "Yash Singh",
        code = "BG204",
        tier = "GOLD",
        plan = "Gold Plan • 12 Months",
        status = "3d OVERDUE",
        isOverdue = true,
        overdueAmount = "₹4,500",
        daysLeft = 48,
        visits = 16,
        targetVisits = 26,
        workouts = 12,
        coachName = "Vikas Yadav",
        routineName = "Chest & Triceps",
        initialLetter = "Y"
    ),
    MemberCardProfile(
        id = "2",
        name = "Arjun Mehta",
        code = "BG105",
        tier = "PREMIUM",
        plan = "Premium Plan • 12 Months",
        status = "ACTIVE • PT ACTIVE",
        isOverdue = false,
        overdueAmount = "₹0",
        daysLeft = 102,
        visits = 22,
        targetVisits = 26,
        workouts = 18,
        coachName = "Vikas Yadav",
        routineName = "Hypertrophy Push",
        initialLetter = "A"
    ),
    MemberCardProfile(
        id = "3",
        name = "Riya Kapoor",
        code = "BG310",
        tier = "SILVER",
        plan = "Silver Plan • 6 Months",
        status = "ACTIVE",
        isOverdue = false,
        overdueAmount = "₹0",
        daysLeft = 28,
        visits = 12,
        targetVisits = 18,
        workouts = 10,
        coachName = "Ananya Roy",
        routineName = "Pilates & Core",
        initialLetter = "R"
    ),
    MemberCardProfile(
        id = "4",
        name = "Vikram Rathore",
        code = "BG901",
        tier = "NORMAL",
        plan = "Gold Plan • Expired",
        status = "EXPIRED • RENEW NOW",
        isOverdue = true,
        overdueAmount = "₹12,000",
        daysLeft = 0,
        visits = 0,
        targetVisits = 26,
        workouts = 0,
        coachName = "Unassigned",
        routineName = "Inactive",
        initialLetter = "V"
    )
)

@Composable
fun MemberIntelligenceApp() {
    var activeTheme by remember { mutableStateOf(BadGymTheme.NATURAL_FRESH) }
    var activeMenu by remember { mutableStateOf(MenuSection.HOME) }
    var selectedMemberIndex by remember { mutableIntStateOf(0) }
    var isDetailMode by remember { mutableStateOf(false) }
    var showThemePicker by remember { mutableStateOf(false) }

    val member = SampleMembers[selectedMemberIndex % SampleMembers.size]

    // Outer root dashboard maintains stable dark canvas (#0C1017)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0C1017)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Stable App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF161F2E))
                    .border(1.dp, Color(0xFF334155).copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⚡", fontSize = 20.sp, color = Color(0xFF00E5FF))
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "BAD GYM",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        "Member Intelligence • 8-Theme System",
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                // Theme selector trigger chip
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, activeTheme.accent.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                        .clickable { showThemePicker = true }
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(activeTheme.accent)
                    )
                    Text(
                        activeTheme.title,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = activeTheme.accent
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // 2. Compact Bounded Member Card Shell (Bounded geometry ~291dp wide x 372dp high)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = if (isDetailMode) 320.dp else 268.dp)
                    .heightIn(max = if (isDetailMode) 385.dp else 365.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = activeTheme.glowColor)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(activeTheme.bgGradient)
                    )
                    .border(
                        width = if (member.isOverdue) 1.8.dp else 1.2.dp,
                        color = if (member.isOverdue) Color(0xFFEF4444) else activeTheme.border,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                if (isDetailMode) {
                    // Bounded Detail Layout: Vertical Navigation Rail + Persistent Header + Panel
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Vertical Navigation Rail
                        Column(
                            modifier = Modifier
                                .width(52.dp)
                                .fillMaxHeight()
                                .background(activeTheme.surfaceMuted.copy(alpha = 0.8f))
                                .verticalScroll(rememberScrollState())
                                .padding(vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Collapse affordance
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(activeTheme.surface)
                                    .clickable { isDetailMode = false },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = "Collapse to browse",
                                    tint = activeTheme.accent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(Modifier.height(4.dp))

                            MenuSection.values().forEach { section ->
                                val active = section == activeMenu
                                val isAttention = section == MenuSection.PAYMENT && member.isOverdue
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 3.dp, vertical = 2.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (active) activeTheme.accent.copy(alpha = 0.2f)
                                            else if (isAttention) Color(0xFFEF4444).copy(alpha = 0.15f)
                                            else Color.Transparent
                                        )
                                        .border(
                                            width = if (active) 1.dp else if (isAttention) 1.dp else 0.dp,
                                            color = if (active) activeTheme.accent else if (isAttention) Color(0xFFEF4444) else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { activeMenu = section }
                                        .padding(vertical = 5.dp, horizontal = 2.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = section.icon,
                                        contentDescription = section.title,
                                        tint = if (active) activeTheme.accent else if (isAttention) Color(0xFFEF4444) else activeTheme.textMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = section.title,
                                        fontSize = 7.sp,
                                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                                        color = if (active) activeTheme.accent else if (isAttention) Color(0xFFEF4444) else activeTheme.textMuted,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        // Right Content Area: Persistent Header + Active Menu Content
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Persistent Detail Header
                            PersistentHeader(member, activeTheme) { isDetailMode = false }

                            Spacer(Modifier.height(4.dp))

                            // Dynamic Bounded Content
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                AnimatedContent(
                                    targetState = activeMenu,
                                    transitionSpec = {
                                        (slideInHorizontally(tween(200)) + fadeIn(tween(180))) togetherWith
                                                (slideOutHorizontally(tween(160)) + fadeOut(tween(140)))
                                    },
                                    label = "menu_transition"
                                ) { menu ->
                                    when (menu) {
                                        MenuSection.HOME -> HomePanel(member, activeTheme)
                                        MenuSection.ATTENDANCE -> AttendancePanel(member, activeTheme)
                                        MenuSection.PLAN -> PlanPanel(member, activeTheme)
                                        MenuSection.PAYMENT -> PaymentPanel(member, activeTheme)
                                        MenuSection.TRAINER -> TrainerPanel(member, activeTheme)
                                        MenuSection.WORKOUT -> WorkoutPanel(member, activeTheme)
                                        MenuSection.SUPPLEMENTS -> SupplementsPanel(member, activeTheme)
                                        MenuSection.NUTRITION -> NutritionPanel(member, activeTheme)
                                        MenuSection.SERVICES -> ServicesPanel(member, activeTheme)
                                        MenuSection.HISTORY -> HistoryPanel(member, activeTheme)
                                        MenuSection.INSIGHT -> InsightPanel(member, activeTheme)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Browse Mode Compact Card
                    BrowseModeCard(
                        member = member,
                        theme = activeTheme,
                        onOpenDetail = { isDetailMode = true }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // 3. Member Switcher Strip (Browse Carousel context)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Member ${selectedMemberIndex + 1} of ${SampleMembers.size}",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            selectedMemberIndex = (selectedMemberIndex - 1 + SampleMembers.size) % SampleMembers.size
                            activeTheme = BadGymTheme.values()[selectedMemberIndex % BadGymTheme.values().size]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("← Prev", fontSize = 11.sp)
                    }
                    Button(
                        onClick = {
                            selectedMemberIndex = (selectedMemberIndex + 1) % SampleMembers.size
                            activeTheme = BadGymTheme.values()[selectedMemberIndex % BadGymTheme.values().size]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Next →", fontSize = 11.sp)
                    }
                }
            }
        }
    }

    // Theme Picker Dialog
    if (showThemePicker) {
        AlertDialog(
            onDismissRequest = { showThemePicker = false },
            title = {
                Text("Select BAD GYM 8-Theme Skin", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(BadGymTheme.values()) { t ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (t == activeTheme) t.accent.copy(alpha = 0.15f) else Color(0xFFF1F5F9))
                                .border(
                                    1.dp,
                                    if (t == activeTheme) t.accent else Color(0xFFE2E8F0),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    activeTheme = t
                                    showThemePicker = false
                                }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(t.accent)
                            )
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(t.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(t.category, fontSize = 9.sp, color = Color(0xFF64748B))
                            }
                            if (t == activeTheme) {
                                Text("✓", color = t.accent, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
private fun PersistentHeader(
    member: MemberCardProfile,
    theme: BadGymTheme,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (member.isOverdue) Color(0xFFEF4444) else theme.success)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (member.isOverdue) "● OVERDUE" else "● CHECK-IN",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Text("4:03 PM • Just now", color = theme.textMuted, fontSize = 8.sp)
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(theme.surfaceMuted)
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Collapse",
                    tint = theme.textPrimary,
                    modifier = Modifier.size(11.dp)
                )
            }
        }

        // Hero Portrait + Info Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            VerifiedPortrait(
                initial = member.initialLetter,
                theme = theme,
                isUrgent = member.isOverdue,
                width = 38.dp,
                height = 42.dp
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        member.name,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = theme.textPrimary
                    )
                    Spacer(Modifier.width(3.dp))
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = "Verified",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(10.dp)
                    )
                }
                Text("${member.code} • ${member.plan}", fontSize = 8.sp, color = theme.textSecondary)
                Text(theme.motto, fontSize = 7.5.sp, fontStyle = FontStyle.Italic, color = theme.accent)
            }
        }
    }
}

@Composable
private fun BrowseModeCard(
    member: MemberCardProfile,
    theme: BadGymTheme,
    onOpenDetail: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Section 1: Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (member.isOverdue) Color(0xFFEF4444) else theme.success)
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (member.isOverdue) "+ OVERDUE" else "+ CHECK-IN",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text("4:03 PM • Just now", color = theme.textMuted, fontSize = 9.sp)
        }

        // Section 2: Portrait + Identity
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VerifiedPortrait(
                initial = member.initialLetter,
                theme = theme,
                isUrgent = member.isOverdue,
                width = 68.dp,
                height = 74.dp
            )
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(theme.accent.copy(alpha = 0.15f))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        member.tier,
                        color = theme.accent,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        member.name,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = theme.textPrimary
                    )
                    Spacer(Modifier.width(3.dp))
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = "Verified",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(12.dp)
                    )
                }
                Text(member.code, fontSize = 9.sp, color = theme.textSecondary)
                Text(
                    theme.motto,
                    fontSize = 8.sp,
                    fontStyle = FontStyle.Italic,
                    color = theme.accent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Section 3: Dual status bands
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(theme.surface)
                    .border(0.6.dp, theme.border, RoundedCornerShape(6.dp))
                    .padding(4.dp)
            ) {
                Text(member.plan, fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = theme.textPrimary)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (member.isOverdue) Color(0xFFEF4444).copy(alpha = 0.15f) else theme.surface)
                    .border(
                        0.6.dp,
                        if (member.isOverdue) Color(0xFFEF4444) else theme.border,
                        RoundedCornerShape(6.dp)
                    )
                    .padding(4.dp)
            ) {
                Text(
                    member.status,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (member.isOverdue) Color(0xFFEF4444) else theme.success
                )
            }
        }

        // Section 4: 3 Decision metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MetricBox("Attendance", "${member.visits}/${member.targetVisits}", "${(member.visits * 100 / member.targetVisits.coerceAtLeast(1))}%", theme, Modifier.weight(1f))
            MetricBox(
                "Payment",
                if (member.isOverdue) member.overdueAmount else "₹0",
                if (member.isOverdue) "Overdue" else "Clear",
                theme,
                Modifier.weight(1f),
                isDanger = member.isOverdue
            )
            MetricBox("Workouts", "${member.workouts}", "This month", theme, Modifier.weight(1f))
        }

        // Section 5: Urgent signal banner if overdue
        if (member.isOverdue) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFEF4444).copy(alpha = 0.15f))
                    .border(0.8.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.WarningAmber, null, tint = Color(0xFFEF4444), modifier = Modifier.size(11.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    "OVERDUE • ${member.overdueAmount} payment pending",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444)
                )
            }
        }

        // Section 6: Action CTA
        Button(
            onClick = onOpenDetail,
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (member.isOverdue) Color(0xFFEF4444) else theme.accent
            )
        ) {
            Text(
                if (member.isOverdue) "Collect ${member.overdueAmount} →" else "Inspect Detail →",
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun VerifiedPortrait(
    initial: String,
    theme: BadGymTheme,
    isUrgent: Boolean,
    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        theme.accent.copy(alpha = 0.35f),
                        theme.surfaceElevated
                    )
                )
            )
            .border(
                width = if (isUrgent) 1.8.dp else 1.2.dp,
                color = if (isUrgent) Color(0xFFEF4444) else theme.accent,
                shape = RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Rounded.Person,
                contentDescription = "Member portrait",
                tint = theme.accent,
                modifier = Modifier.size(width * 0.55f)
            )
        }
        Icon(
            imageVector = Icons.Rounded.Verified,
            contentDescription = "Verified badge",
            tint = Color(0xFF38BDF8),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(14.dp)
                .background(Color.White, CircleShape)
        )
    }
}

@Composable
private fun MetricBox(
    label: String,
    value: String,
    sub: String,
    theme: BadGymTheme,
    modifier: Modifier = Modifier,
    isDanger: Boolean = false
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isDanger) Color(0xFFEF4444).copy(alpha = 0.12f) else theme.surface)
            .border(0.6.dp, if (isDanger) Color(0xFFEF4444).copy(alpha = 0.4f) else theme.border, RoundedCornerShape(8.dp))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 7.sp, color = theme.textMuted)
        Text(
            value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = if (isDanger) Color(0xFFEF4444) else theme.textPrimary
        )
        Text(sub, fontSize = 7.sp, color = if (isDanger) Color(0xFFEF4444) else theme.accent)
    }
}

// 11 Bounded Menu Panel Implementations with Truthful Domain Data
@Composable
private fun HomePanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("HOME SNAPSHOT", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            MetricBox("Attended", "${member.visits} Days", "Target ${member.targetVisits}", theme, Modifier.weight(1f))
            MetricBox("Payment", if (member.isOverdue) member.overdueAmount else "Clear", if (member.isOverdue) "Overdue" else "Paid", theme, Modifier.weight(1f), member.isOverdue)
            MetricBox("Routine", member.routineName, member.coachName, theme, Modifier.weight(1f))
        }
        Text("Coach: ${member.coachName} • Focus: Functional Push", fontSize = 8.5.sp, color = theme.textSecondary)
    }
}

@Composable
private fun AttendancePanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("ATTENDANCE INTELLIGENCE", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Text("${member.visits} of ${member.targetVisits} visits logged this month (${member.visits * 100 / member.targetVisits.coerceAtLeast(1)}%)", fontSize = 8.5.sp, color = theme.textSecondary)
        LinearProgressIndicator(
            progress = { (member.visits.toFloat() / member.targetVisits.coerceAtLeast(1)).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = theme.accent,
            trackColor = theme.surfaceMuted
        )
        Text("Recent activity: Gate check-in today 4:03 PM", fontSize = 8.sp, color = theme.textMuted)
    }
}

@Composable
private fun PlanPanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("MEMBERSHIP PLAN", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Text("Plan: ${member.plan}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = theme.textPrimary)
        Text("Days Remaining: ${member.daysLeft} days", fontSize = 8.5.sp, color = if (member.daysLeft <= 0) Color(0xFFEF4444) else theme.accent)
        Text("Freeze Privileges: 1 of 3 used • Renews on expiry", fontSize = 8.sp, color = theme.textMuted)
    }
}

@Composable
private fun PaymentPanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("PAYMENT INTELLIGENCE", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Text("Outstanding: ${if (member.isOverdue) member.overdueAmount else "₹0 Clear"}", fontSize = 10.sp, fontWeight = FontWeight.Black, color = if (member.isOverdue) Color(0xFFEF4444) else theme.success)
        Text(if (member.isOverdue) "Status: 3 days overdue • Action: Collect immediately" else "Status: Clean account", fontSize = 8.5.sp, color = theme.textSecondary)
        Text("Last payment: ₹1,400 via UPI (28 days ago)", fontSize = 8.sp, color = theme.textMuted)
    }
}

@Composable
private fun TrainerPanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("PERSONAL TRAINER", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Text("Coach: ${member.coachName}", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = theme.textPrimary)
        Text("Sessions: 8 of 12 completed • Next: Tomorrow 7:00 AM", fontSize = 8.5.sp, color = theme.textSecondary)
    }
}

@Composable
private fun WorkoutPanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("WORKOUT PROGRAM", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Text("Routine: ${member.routineName}", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = theme.textPrimary)
        Text("Duration: 60 mins • Volume: 5,400 kg • Burn: 520 kcal", fontSize = 8.5.sp, color = theme.textSecondary)
    }
}

@Composable
private fun SupplementsPanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("SUPPLEMENTS STACK", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Text("Active: MuscleBlaze Whey Gold 2kg", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = theme.textPrimary)
        Text("Inventory: 1.2 kg remaining • Reorder window: 10 days", fontSize = 8.5.sp, color = theme.textSecondary)
    }
}

@Composable
private fun NutritionPanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("NUTRITION PLAN", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Text("Plan: BAD GYM High Protein • ₹2,000/mo", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = theme.textPrimary)
        Text("Daily target: 160g Protein, 220g Carbs, 60g Fat (2,060 kcal)", fontSize = 8.5.sp, color = theme.textSecondary)
    }
}

@Composable
private fun ServicesPanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("ACTIVE SERVICES", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Text("1. Locker L12 • Active until month-end", fontSize = 8.5.sp, color = theme.textPrimary)
        Text("2. Steam & Sauna Access • Active VIP perk", fontSize = 8.5.sp, color = theme.textPrimary)
    }
}

@Composable
private fun HistoryPanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("ACTIVITY TIMELINE", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        Text("• 4:03 PM Check-in at Gate 1", fontSize = 8.5.sp, color = theme.textSecondary)
        Text("• Yesterday: 60m Workout completed", fontSize = 8.5.sp, color = theme.textSecondary)
        Text("• 3 days ago: PT Session with ${member.coachName}", fontSize = 8.5.sp, color = theme.textSecondary)
    }
}

@Composable
private fun InsightPanel(member: MemberCardProfile, theme: BadGymTheme) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("ACTIONABLE INSIGHTS", fontWeight = FontWeight.Black, fontSize = 11.sp, color = theme.textPrimary)
        if (member.isOverdue) {
            Text("P0 Urgent: ₹4,500 overdue for 3 days. Recommend follow-up during today's workout.", fontSize = 8.5.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
        } else {
            Text("P1 Opportunity: Member consistency is 84%. Excellent renewal candidate for Annual VIP.", fontSize = 8.5.sp, color = theme.accent, fontWeight = FontWeight.Bold)
        }
    }
}
