package com.badgym.memberintelligence

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class ThemeSkin(val title: String) {
    NATURAL("Natural / Fresh"), NEON("Futuristic Neon"), DARK("Minimal Dark"),
    GLASS("Glassmorphism"), PREMIUM("Premium 3D"), GRADIENT("Vibrant Gradient"),
    BEAST("Gym Beast Mode"), ROYAL("Purple Royal")
}
private enum class Menu(val title: String) {
    HOME("Home"), ATTENDANCE("Attend"), PLAN("Plan"), PAYMENT("Pay"),
    TRAINER("Trainer"), WORKOUT("Workout"), SUPPLEMENTS("Supplements"),
    NUTRITION("Nutrition"), SERVICES("Services"), HISTORY("History"), INSIGHT("Insight")
}
private data class Tokens(
    val bg: Color, val surface: Color, val surface2: Color, val text: Color, val muted: Color,
    val accent: Color, val accent2: Color, val success: Color, val danger: Color, val border: Color
)
private fun tokens(s: ThemeSkin) = when(s) {
    ThemeSkin.NATURAL -> Tokens(Color(0xFFF2FAF4),Color.White,Color(0xFFE9F7EE),Color(0xFF163020),Color(0xFF64756A),Color(0xFF16A34A),Color(0xFF0F766E),Color(0xFF16A34A),Color(0xFFDC2626),Color(0xFFD7EBDD))
    ThemeSkin.NEON -> Tokens(Color(0xFF041016),Color(0xFF081A22),Color(0xFF0C2630),Color(0xFFF5FEFF),Color(0xFF9BB7BF),Color(0xFF39FF88),Color(0xFF00E5FF),Color(0xFF39FF88),Color(0xFFFF3D71),Color(0xFF16414B))
    ThemeSkin.DARK -> Tokens(Color(0xFF0D1115),Color(0xFF171C21),Color(0xFF20262D),Color(0xFFF8FAFC),Color(0xFF94A3B8),Color(0xFF22C55E),Color(0xFF60A5FA),Color(0xFF22C55E),Color(0xFFFF4D6D),Color(0xFF303842))
    ThemeSkin.GLASS -> Tokens(Color(0xFFEAF4FF),Color(0xDFFFFFFF),Color(0x99FFFFFF),Color(0xFF172554),Color(0xFF64748B),Color(0xFF2563EB),Color(0xFF14B8A6),Color(0xFF059669),Color(0xFFDC2626),Color.White)
    ThemeSkin.PREMIUM -> Tokens(Color(0xFF19150B),Color(0xFF211C10),Color(0xFF302717),Color(0xFFFFF8E7),Color(0xFFC9BEA3),Color(0xFFD4AF37),Color(0xFF10B981),Color(0xFF10B981),Color(0xFFEF4444),Color(0xFF5A4A28))
    ThemeSkin.GRADIENT -> Tokens(Color(0xFFF5F3FF),Color.White,Color(0xFFFDF2F8),Color(0xFF1E1B4B),Color(0xFF64748B),Color(0xFF6366F1),Color(0xFFEC4899),Color(0xFF10B981),Color(0xFFEF4444),Color(0xFFE9D5FF))
    ThemeSkin.BEAST -> Tokens(Color(0xFF090909),Color(0xFF141414),Color(0xFF202020),Color(0xFFF7F7F7),Color(0xFF9CA3AF),Color(0xFFFF2020),Color(0xFF39FF88),Color(0xFF39FF88),Color(0xFFFF2020),Color(0xFF383838))
    ThemeSkin.ROYAL -> Tokens(Color(0xFFF4EEFF),Color.White,Color(0xFFF1E8FF),Color(0xFF28104F),Color(0xFF756B86),Color(0xFF7C3AED),Color(0xFFC026D3),Color(0xFF10B981),Color(0xFFDC2626),Color(0xFFE9D5FF))
}

@Composable
fun MemberIntelligenceApp() {
    var skin by remember { mutableStateOf(ThemeSkin.NATURAL) }
    var menu by remember { mutableStateOf(Menu.HOME) }
    var picker by remember { mutableStateOf(false) }
    val t = tokens(skin)
    val menus = remember {
        listOf(Menu.HOME,Menu.ATTENDANCE,Menu.PLAN,Menu.PAYMENT,Menu.TRAINER,Menu.WORKOUT,Menu.SUPPLEMENTS,Menu.NUTRITION,Menu.SERVICES,Menu.HISTORY,Menu.INSIGHT)
    }
    Surface(Modifier.fillMaxSize(), color=t.bg) {
        Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment=Alignment.CenterHorizontally) {
            Row(
                Modifier.fillMaxWidth().widthIn(max=520.dp).clip(RoundedCornerShape(18.dp))
                    .background(t.surface).border(1.dp,t.border,RoundedCornerShape(18.dp)).padding(12.dp),
                verticalAlignment=Alignment.CenterVertically
            ) {
                Text("⚡",fontSize=22.sp,color=t.accent)
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text("BAD GYM",fontWeight=FontWeight.ExtraBold,fontSize=17.sp,color=t.text)
                    Text("Member Intelligence",fontSize=9.sp,color=t.muted)
                }
                Text(skin.title,fontSize=9.sp,fontWeight=FontWeight.Bold,color=t.accent,
                    modifier=Modifier.clip(RoundedCornerShape(12.dp)).background(t.surface2).clickable{picker=true}.padding(9.dp))
            }
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier.fillMaxWidth().widthIn(max=520.dp).heightIn(max=510.dp).weight(1f)
                    .clip(RoundedCornerShape(22.dp)).background(t.surface).border(1.dp,t.border,RoundedCornerShape(22.dp))
            ) {
                Rail(menus,menu,t){menu=it}
                AnimatedContent(menu, transitionSpec={
                    (slideInHorizontally(tween(220)){it}+fadeIn(tween(160))) togetherWith
                    (slideOutHorizontally(tween(180)) + fadeOut(tween(120)))
                }, label="menu", modifier=Modifier.fillMaxSize()) { selected ->
                    Detail(selected,t)
                }
            }
        }
    }
    if(picker) AlertDialog(
        onDismissRequest={picker=false},
        title={Text("BAD GYM Themes")},
        text={
            LazyColumn {
                items(ThemeSkin.values().toList()) { s ->
                    Row(Modifier.fillMaxWidth().clickable{skin=s;picker=false}.padding(12.dp),verticalAlignment=Alignment.CenterVertically) {
                        Box(Modifier.size(14.dp).clip(RoundedCornerShape(4.dp)).background(tokens(s).accent))
                        Spacer(Modifier.width(10.dp)); Text(s.title,Modifier.weight(1f))
                        if(s==skin) Text("✓",color=t.accent,fontWeight=FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton={}
    )
}

@Composable private fun Rail(menus:List<Menu>,selected:Menu,t:Tokens,onSelect:(Menu)->Unit) {
    Column(Modifier.width(58.dp).fillMaxHeight().verticalScroll(rememberScrollState()).padding(vertical=6.dp),horizontalAlignment=Alignment.CenterHorizontally) {
        menus.forEach { m ->
            val active=m==selected
            val icon:ImageVector=when(m){
                Menu.HOME->Icons.Default.Home;Menu.ATTENDANCE->Icons.Default.CalendarToday;Menu.PLAN->Icons.Default.Star
                Menu.PAYMENT->Icons.Default.AccountBalanceWallet;Menu.TRAINER->Icons.Default.Person;Menu.WORKOUT->Icons.Default.FitnessCenter
                Menu.SUPPLEMENTS->Icons.Default.ShoppingCart;Menu.NUTRITION->Icons.Default.Restaurant;Menu.SERVICES->Icons.Default.Settings
                Menu.HISTORY->Icons.Default.History;Menu.INSIGHT->Icons.Default.Insights
            }
            Column(Modifier.padding(2.dp).clip(RoundedCornerShape(11.dp)).background(if(active)t.accent.copy(.14f) else Color.Transparent)
                .clickable{onSelect(m)}.padding(vertical=6.dp,horizontal=2.dp),horizontalAlignment=Alignment.CenterHorizontally){
                Icon(icon,m.title,Modifier.size(18.dp),tint=if(active)t.accent else t.muted)
                Text(m.title,fontSize=7.sp,color=if(active)t.accent else t.muted,fontWeight=if(active)FontWeight.Bold else FontWeight.Medium)
            }
        }
    }
}

@Composable private fun Detail(menu:Menu,t:Tokens) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(10.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){
            Box(Modifier.clip(RoundedCornerShape(20.dp)).background(t.success.copy(.12f)).border(1.dp,t.success.copy(.35f),RoundedCornerShape(20.dp)).padding(8.dp)){
                Text("●  CHECK-IN",fontSize=8.sp,fontWeight=FontWeight.Bold,color=t.success)
            }
            Spacer(Modifier.weight(1f)); Column(horizontalAlignment=Alignment.End){Text("4:03 PM",fontWeight=FontWeight.Bold,fontSize=10.sp,color=t.text);Text("Just now",fontSize=8.sp,color=t.muted)}
        }
        Spacer(Modifier.height(7.dp))
        if(menu==Menu.HOME) Home(t) else Generic(menu,t)
    }
}

@Composable private fun Home(t:Tokens){
    Row(verticalAlignment=Alignment.CenterVertically){
        Box(Modifier.size(82.dp,94.dp).clip(RoundedCornerShape(14.dp)).background(t.surface2).border(1.dp,t.border,RoundedCornerShape(14.dp)),contentAlignment=Alignment.Center){
            Text("YS",fontSize=27.sp,fontWeight=FontWeight.ExtraBold,color=t.accent)
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)){
            Text("Yash Singh ✓",fontWeight=FontWeight.ExtraBold,fontSize=15.sp,color=t.text)
            Text("BG204",fontSize=9.sp,color=t.accent);Text("Gold Plan · 12 Months",fontSize=9.sp,color=t.muted)
            Spacer(Modifier.height(5.dp))
            Box(Modifier.clip(RoundedCornerShape(16.dp)).background(t.success.copy(.12f)).padding(7.dp)){Text("✓ ACTIVE · 48 DAYS LEFT",fontSize=8.sp,fontWeight=FontWeight.Bold,color=t.success)}
        }
    }
    Spacer(Modifier.height(7.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
        Metric("16 / 26","Attendance","61%",t,Modifier.weight(1f))
        Metric("₹4,500","Payment Due","3 days overdue",t,Modifier.weight(1f),true)
        Metric("12","Workouts","This month",t,Modifier.weight(1f))
    }
    Spacer(Modifier.height(7.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){Info("Trainer","Vikas Yadav",t,Modifier.weight(1f));Info("Workout","Chest & Triceps",t,Modifier.weight(1f))}
    Spacer(Modifier.height(6.dp));Info("Services","2 Active",t,Modifier.fillMaxWidth())
    Spacer(Modifier.height(8.dp))
    Button(onClick={},modifier=Modifier.fillMaxWidth().height(42.dp),shape=RoundedCornerShape(13.dp),colors=ButtonDefaults.buttonColors(containerColor=t.danger)){Text("COLLECT ₹4,500",fontWeight=FontWeight.ExtraBold)}
    Spacer(Modifier.height(8.dp));Info("Weekly Activity","M  T  W  T  F  S  S     ▂  ▆  ▅  █  ▆  ▃  ▇",t,Modifier.fillMaxWidth())
}

@Composable private fun Metric(value:String,label:String,sub:String,t:Tokens,modifier:Modifier,danger:Boolean=false){
    Column(modifier.clip(RoundedCornerShape(13.dp)).background(if(danger)t.danger.copy(.09f) else t.surface2).border(1.dp,if(danger)t.danger.copy(.25f) else t.border,RoundedCornerShape(13.dp)).padding(7.dp)){
        Text(label,fontSize=7.sp,color=t.muted);Text(value,fontSize=16.sp,fontWeight=FontWeight.ExtraBold,color=if(danger)t.danger else t.accent);Text(sub,fontSize=7.sp,color=t.muted)
    }
}
@Composable private fun Info(label:String,value:String,t:Tokens,modifier:Modifier){
    Column(modifier.clip(RoundedCornerShape(12.dp)).background(t.surface2).border(1.dp,t.border,RoundedCornerShape(12.dp)).padding(8.dp)){Text(label,fontSize=7.sp,color=t.muted);Text(value,fontSize=9.sp,fontWeight=FontWeight.Bold,color=t.text)}
}
@Composable private fun Generic(menu:Menu,t:Tokens){
    val title=menu.title
    Text(title,fontWeight=FontWeight.ExtraBold,fontSize=18.sp,color=t.text)
    Text(when(menu){
        Menu.ATTENDANCE->"August 2026 · 16 / 26 visits · 61% consistency"
        Menu.PLAN->"Gold Plan · 48 days remaining · Freeze 1 / 3"
        Menu.PAYMENT->"₹4,500 outstanding · 3 days overdue · Last payment ₹1,400"
        Menu.TRAINER->"Vikas Yadav · Next session tomorrow 7:00 AM · 8 / 12"
        Menu.WORKOUT->"Chest & Triceps · 58 min · 620 kcal"
        Menu.SUPPLEMENTS->"MuscleBlaze Whey 2kg · ₹4,249 · 18 Aug 2026"
        Menu.NUTRITION->"₹2,000/month · Chicken · Eggs · Rice · active"
        Menu.SERVICES->"Steam/Sauna · Locker · 2 active services"
        Menu.HISTORY->"Timeline: check-in · workout · PT · payment · supplement"
        Menu.INSIGHT->"Renewal opportunity · payment follow-up · attendance evidence"
        else->""
    },fontSize=10.sp,color=t.muted)
    Spacer(Modifier.height(10.dp))
    repeat(4){i->Info("Signal ${i+1}",listOf("Current period","Evidence-backed activity","Next action","Historical context")[i],t,Modifier.fillMaxWidth());Spacer(Modifier.height(6.dp))}
}
