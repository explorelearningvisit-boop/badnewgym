package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import java.text.SimpleDateFormat
import java.util.*

private data class P(val bg:Color,val card:Color,val card2:Color,val ink:Color,val muted:Color,val accent:Color,val accent2:Color,val danger:Color,val good:Color,val line:Color,val onAccent:Color){
 val cta=Brush.horizontalGradient(listOf(accent,accent2))
}
private fun p(id:ThemeId)=when(id){
 ThemeId.NATURAL_FRESH->P(Color(0xFFF2FBF5),Color.White,Color(0xFFEAF8EE),Color(0xFF102118),Color(0xFF668070),Color(0xFF18B957),Color(0xFF6DE89A),Color(0xFFE63E4B),Color(0xFF0AA85A),Color(0xFFD8EEE0),Color.White)
 ThemeId.FUTURISTIC_NEON->P(Color(0xFF03101C),Color(0xFF071B2B),Color(0xFF0A2537),Color.White,Color(0xFF7FA8B7),Color(0xFF00E5FF),Color(0xFF63FF5C),Color(0xFFFF3864),Color(0xFF00F59B),Color(0xFF15566C),Color(0xFF001016))
 ThemeId.MINIMAL_DARK->P(Color(0xFF0B0F13),Color(0xFF11171D),Color(0xFF151C22),Color(0xFFF3F5F7),Color(0xFF8A959F),Color(0xFF16D6A0),Color(0xFF6B7280),Color(0xFFFF4B63),Color(0xFF16D6A0),Color(0xFF2A333B),Color(0xFF06120F))
 ThemeId.GLASSMORPHISM->P(Color(0xFFEAF4FF),Color(0xE6FFFFFF),Color(0xCCF5FAFF),Color(0xFF12254A),Color(0xFF65749A),Color(0xFF278CFF),Color(0xFFD66DFF),Color(0xFFFF4E67),Color(0xFF10B981),Color(0xFFAFC9E9),Color.White)
 ThemeId.PREMIUM_3D->P(Color(0xFF0A0804),Color(0xFF17130B),Color(0xFF201A0D),Color(0xFFFFF9E8),Color(0xFFB8A77C),Color(0xFFFFC83D),Color(0xFFFFE49A),Color(0xFFFF4D4D),Color(0xFF4ADE80),Color(0xFF5D481D),Color(0xFF140E02))
 ThemeId.VIBRANT_GRADIENT->P(Color(0xFFFFF9FD),Color.White,Color(0xFFF6F0FF),Color(0xFF17203C),Color(0xFF7B819A),Color(0xFFEF4E7B),Color(0xFF7266FF),Color(0xFFFF3E63),Color(0xFF11B981),Color(0xFFD9DDF1),Color.White)
 ThemeId.BEAST_MODE->P(Color(0xFF080304),Color(0xFF130708),Color(0xFF1B0B0D),Color.White,Color(0xFFB78A8D),Color(0xFFFF202B),Color(0xFFFF4C55),Color(0xFFFF202B),Color(0xFF00E98A),Color(0xFF4C1A1E),Color.White)
 ThemeId.PURPLE_ROYAL->P(Color(0xFF0D041B),Color(0xFF17082B),Color(0xFF211039),Color(0xFFF8F1FF),Color(0xFFAD91C8),Color(0xFFB86CFF),Color(0xFF7A4DFF),Color(0xFFFF4D79),Color(0xFF21D89B),Color(0xFF49256C),Color.White)
}
private data class R(val m:MenuType,val name:String,val icon:ImageVector)
private fun rails()=listOf(R(MenuType.HOME,"Home",Icons.Rounded.Home),R(MenuType.ATTENDANCE,"Attend",Icons.Rounded.EventAvailable),R(MenuType.PLAN,"Plan",Icons.Rounded.CardMembership),R(MenuType.PAYMENT,"Pay",Icons.Rounded.CreditCard),R(MenuType.TRAINER,"Trainer",Icons.Rounded.Person),R(MenuType.WORKOUT,"Workout",Icons.Rounded.FitnessCenter),R(MenuType.SERVICES,"Services",Icons.Rounded.Apps),R(MenuType.INSIGHT,"Insight",Icons.Rounded.AutoAwesome))

@Composable fun PixelPerfectMemberCard(snapshot:MemberSnapshot?,currentEvent:MemberEvent?,signals:List<IntelligenceSignal>,menus:List<MemberMenu>,activeMenu:MenuType?,modifier:Modifier=Modifier,theme:ThemeId=ThemeId.NATURAL_FRESH,primarySignal:IntelligenceSignal?=null,secondarySignals:List<IntelligenceSignal> = emptyList(),cta:SignalAction?=null,onMenuSelected:(MenuType)->Unit={},onThemeSelected:(ThemeId)->Unit={},onCta:(SignalAction)->Unit={}){
 val t=p(theme)
 Box(modifier.background(Brush.verticalGradient(listOf(t.bg,t.card2))).padding(8.dp)){
  Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(6.dp)){
   ThemeStrip(theme,t,onThemeSelected); Brand(theme,t)
   if(snapshot==null) Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){Text("Loading member intelligence…",color=t.muted,fontSize=10.sp)}
   else Card(snapshot,currentEvent,signals,menus,activeMenu?:MenuType.HOME,primarySignal,secondarySignals,cta,theme,t,onMenuSelected,onCta)
  }
 }
}
@Composable private fun ThemeStrip(theme:ThemeId,t:P,on:(ThemeId)->Unit){
 Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(4.dp)){ThemeId.entries.forEach{v->val a=v==theme;Box(Modifier.clip(RoundedCornerShape(50)).background(if(a)t.accent else t.card.copy(alpha=.82f)).border(1.dp,if(a)t.accent else t.line,RoundedCornerShape(50)).clickable{on(v)}.padding(horizontal=8.dp,vertical=5.dp)){Text("§{v.ordinal+1}  §{v.title.uppercase()}",color=if(a)t.onAccent else t.muted,fontSize=7.sp,fontWeight=if(a)FontWeight.Black else FontWeight.Bold)}}}
}
@Composable private fun Brand(theme:ThemeId,t:P){
 Row(Modifier.fillMaxWidth().height(40.dp),verticalAlignment=Alignment.CenterVertically){
  Box(Modifier.size(35.dp).clip(RoundedCornerShape(11.dp)).background(t.cta),contentAlignment=Alignment.Center){Icon(icon(theme),null,tint=t.onAccent,modifier=Modifier.size(20.dp))}
  Spacer(Modifier.width(8.dp));Column(Modifier.weight(1f)){Text("BAD GYM",color=t.ink,fontSize=15.sp,fontWeight=FontWeight.Black);Text(theme.subtitle.replace("\n"," • "),color=t.muted,fontSize=7.sp,maxLines=1)}
  Box(Modifier.clip(RoundedCornerShape(11.dp)).background(t.card).border(1.dp,t.line,RoundedCornerShape(11.dp)).padding(7.dp)){Text(theme.headerTag.replace("\n","  "),color=t.accent,fontSize=6.8.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.End)}
 }
}
@Composable private fun Card(s:MemberSnapshot,e:MemberEvent?,signals:List<IntelligenceSignal>,menus:List<MemberMenu>,active:MenuType,primary:IntelligenceSignal?,secondary:List<IntelligenceSignal>,cta:SignalAction?,theme:ThemeId,t:P,onMenu:(MenuType)->Unit,onCta:(SignalAction)->Unit){
 val visible=menus.filter{it.isVisible}.map{it.id}.toSet()
 Surface(t,Modifier.fillMaxWidth().weight(1f)){Row(Modifier.fillMaxSize()){
  Column(Modifier.width(53.dp).fillMaxHeight().padding(6.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(4.dp)){
   rails().filter{it.m in visible||it.m==MenuType.HOME}.forEach{r->val a=r.m==active;Column(Modifier.fillMaxWidth().height(45.dp).clip(RoundedCornerShape(11.dp)).background(if(a)t.accent else t.card2).border(1.dp,if(a)t.accent else t.line,RoundedCornerShape(11.dp)).clickable{onMenu(r.m)}.padding(vertical=4.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Center){Icon(r.icon,null,tint=if(a)t.onAccent else t.ink,modifier=Modifier.size(16.dp));Text(r.name,color=if(a)t.onAccent else t.muted,fontSize=6.4.sp,fontWeight=FontWeight.Bold)}} 
   Spacer(Modifier.weight(1f));Icon(Icons.Rounded.Settings,null,tint=t.muted,modifier=Modifier.size(14.dp))
  }
  Column(Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState()).padding(start=7.dp,end=9.dp,top=9.dp,bottom=8.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){
   EventRow(e,t); Hero(s.identity,s.membership,theme,t)
   AnimatedContent(targetState=active,transitionSpec={slideInHorizontally(tween(210))+fadeIn(tween(150)) togetherWith slideOutHorizontally(tween(150))+fadeOut(tween(100))},label="menu"){m->when(m){
    MenuType.HOME->Home(s,primary,secondary,t);MenuType.ATTENDANCE->Detail("Attendance","Consistency • visits • streak",t){Home(s,null,emptyList(),null,emptyList(),t)}
    MenuType.PLAN->Detail("Membership","Plan lifecycle • value • renewal",t){Plan(s,t)}
    MenuType.PAYMENT->Detail("Payments","Outstanding balance • due date • history",t){Payment(s,t)}
    MenuType.TRAINER->Detail("Trainer","Sessions • next coaching touchpoint",t){Trainer(s,t)}
    MenuType.WORKOUT->Detail("Workout","Routine • momentum • recovery",t){Workout(s,t)}
    MenuType.SERVICES->Detail("Services","Active services • supplements • nutrition",t){Services(s,t)}
    MenuType.INSIGHT->Detail("Member Intelligence","What changed • what matters • what next",t){signals.forEach{Signal(it,t)}}
    else->Home(s,primary,secondary,t)
   }}
   cta?.let{Action(it,t,onCta)};Text(theme.footer.replace("\n"," • "),Modifier.fillMaxWidth(),color=t.muted,fontSize=6.6.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
  }
 }}
}
@Composable private fun EventRow(e:MemberEvent?,t:P){val a=eventAccent(e?.eventType?:EventType.CHECK_IN,t);Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){Box(Modifier.clip(RoundedCornerShape(50)).background(a.copy(alpha=.14f)).border(1.dp,a.copy(alpha=.4f),RoundedCornerShape(50)).padding(horizontal=8.dp,vertical=5.dp)){Text(e?.eventType?.displayLabel()?:"CHECK-IN",color=a,fontSize=7.sp,fontWeight=FontWeight.Black)};Spacer(Modifier.weight(1f));Column(horizontalAlignment=Alignment.End){Text(e?.occurredAt?.let(::time)?:"4:03 PM",color=t.ink,fontSize=10.sp,fontWeight=FontWeight.Black);Text(e?.occurredAt?.let(::rel)?:"Just now",color=t.muted,fontSize=6.5.sp)} }}
@Composable private fun Hero(id:MemberIdentity,plan:MembershipStatus?,theme:ThemeId,t:P){
 Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp)).background(t.card2).border(1.dp,t.line,RoundedCornerShape(17.dp)).padding(8.dp),verticalAlignment=Alignment.CenterVertically){
  Image(painterResource(R.drawable.member_yash),id.name,contentScale=ContentScale.Crop,modifier=Modifier.size(82.dp).clip(RoundedCornerShape(13.dp)))
  Spacer(Modifier.width(8.dp));Column(Modifier.weight(1f)){Row(verticalAlignment=Alignment.CenterVertically){Text(id.name,color=t.ink,fontSize=14.sp,fontWeight=FontWeight.Black,maxLines=1,overflow=TextOverflow.Ellipsis);if(id.isVerified){Icon(Icons.Rounded.Verified,null,tint=t.accent,modifier=Modifier.size(12.dp))}};Text(id.code?:"BG204",color=t.muted,fontSize=7.2.sp);Text("§{plan?.planName?:"Gold Plan"}  •  12 Months",color=t.muted,fontSize=7.sp);Box(Modifier.clip(RoundedCornerShape(8.dp)).background(t.good.copy(alpha=.13f)).padding(horizontal=6.dp,vertical=4.dp)){Text(if(plan?.isActive==false)"EXPIRED" else "ACTIVE  •  §{plan?.daysRemaining?:48} DAYS LEFT",color=t.good,fontSize=6.5.sp,fontWeight=FontWeight.Black)}}
  Text(theme.motto,color=t.accent,fontSize=7.6.sp,fontWeight=FontWeight.Black,textAlign=TextAlign.End,modifier=Modifier.width(58.dp))
 }
}
@Composable private fun Home(s:MemberSnapshot,primary:IntelligenceSignal?,secondary:List<IntelligenceSignal>,t:P){
 primary?.let{Signal(it,t)}
 Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(5.dp)){
  Stat(Modifier.weight(1f),Icons.Rounded.EventAvailable,"§{s.attendance?.visits?:16}/§{s.attendance?.target?:26}","Attendance","§{s.attendance?.streakDays?:6}d streak",t.good,t)
  val due=s.payment?.totalOutstanding?:4500.0;Stat(Modifier.weight(1f),Icons.Rounded.Payments,"₹§{due.toInt()}","Payment Due","§{s.payment?.overdueDays?:3}d overdue",if(due>0)t.danger else t.good,t)
  Stat(Modifier.weight(1f),Icons.Rounded.FitnessCenter,"12","Workouts","This month",t.accent,t)
 }
 Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(5.dp)){Mini(Icons.Rounded.Person,"Trainer",s.trainer?.trainerName?:"Vikas Yadav",t);Mini(Icons.Rounded.FitnessCenter,"Workout",s.workout?.currentRoutine?:"Chest & Triceps",t);Mini(Icons.Rounded.Apps,"Services","§{s.services?.count{it.isActive}?:2} Active",t)}
 secondary.take(2).forEach{Signal(it,t)}
}
@Composable private fun Stat(m:Modifier,i:ImageVector,v:String,l:String,f:String,a:Color,t:P){Column(m.clip(RoundedCornerShape(12.dp)).background(t.card2).border(1.dp,t.line,RoundedCornerShape(12.dp)).padding(6.dp),horizontalAlignment=Alignment.CenterHorizontally){Icon(i,null,tint=a,modifier=Modifier.size(14.dp));Text(v,color=if(a==t.danger)a else t.ink,fontSize=12.5.sp,fontWeight=FontWeight.Black);Text(l,color=t.muted,fontSize=6.3.sp,maxLines=1);Text(f,color=a,fontSize=6.1.sp,fontWeight=FontWeight.Bold,maxLines=1)}}
@Composable private fun Mini(i:ImageVector,l:String,v:String,t:P){Row(Modifier.weight(1f).height(39.dp).clip(RoundedCornerShape(10.dp)).background(t.card2).border(1.dp,t.line,RoundedCornerShape(10.dp)).padding(5.dp),verticalAlignment=Alignment.CenterVertically){Icon(i,null,tint=t.accent,modifier=Modifier.size(15.dp));Spacer(Modifier.width(4.dp));Column{Text(l,color=t.muted,fontSize=6.sp);Text(v,color=t.ink,fontSize=6.4.sp,fontWeight=FontWeight.Black,maxLines=2,overflow=TextOverflow.Ellipsis)}}}
@Composable private fun Signal(s:IntelligenceSignal,t:P){val a=when(s.priority){SignalPriority.P0_CRITICAL->t.danger;SignalPriority.P1_ACTION_REQUIRED->Color(0xFFFFA51F);SignalPriority.P2_IMPORTANT->t.accent;else->t.good};Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(11.dp)).background(a.copy(alpha=.1f)).border(1.dp,a.copy(alpha=.25f),RoundedCornerShape(11.dp)).padding(7.dp),verticalAlignment=Alignment.CenterVertically){Icon(Icons.Rounded.AutoAwesome,null,tint=a,modifier=Modifier.size(14.dp));Spacer(Modifier.width(6.dp));Column(Modifier.weight(1f)){Text(s.title,color=t.ink,fontSize=8.sp,fontWeight=FontWeight.Black,maxLines=1);s.subtitle?.let{Text(it,color=t.muted,fontSize=6.7.sp,maxLines=1)}};s.value?.let{Text(it,color=a,fontSize=7.5.sp,fontWeight=FontWeight.Black)}}
@Composable private fun Detail(title:String,sub:String,t:P,body:@Composable()->Unit){Column(verticalArrangement=Arrangement.spacedBy(6.dp)){Text(title,color=t.ink,fontSize=13.sp,fontWeight=FontWeight.Black);Text(sub,color=t.muted,fontSize=6.8.sp);body()}}
@Composable private fun Plan(s:MemberSnapshot,t:P){Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(13.dp)).background(t.card2).padding(9.dp)){Column{Text(s.membership?.planName?:"Gold Plan",color=t.ink,fontSize=16.sp,fontWeight=FontWeight.Black);Text(s.membership?.planType?:"12 Months",color=t.muted,fontSize=7.sp);Text("ACTIVE • §{s.membership?.daysRemaining?:48} DAYS LEFT",color=t.good,fontSize=7.sp,fontWeight=FontWeight.Black)}}}
@Composable private fun Payment(s:MemberSnapshot,t:P){Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(13.dp)).background(t.card2).padding(10.dp)){Text("Payment Due",color=t.muted,fontSize=7.sp);Text("₹§{(s.payment?.totalOutstanding?:4500.0).toInt()}",color=t.danger,fontSize=25.sp,fontWeight=FontWeight.Black);Text("§{s.payment?.overdueDays?:3} days overdue",color=t.danger,fontSize=7.sp,fontWeight=FontWeight.Bold)}}
@Composable private fun Trainer(s:MemberSnapshot,t:P){Mini(Icons.Rounded.Person,"Trainer",s.trainer?.trainerName?:"Vikas Yadav",t)}
@Composable private fun Workout(s:MemberSnapshot,t:P){PlanBox(s.workout?.currentRoutine?:"Chest & Triceps","Current routine",t)}
@Composable private fun Services(s:MemberSnapshot,t:P){s.services.orEmpty().forEach{Mini(Icons.Rounded.CheckCircle,it.serviceName,if(it.isActive)"Active" else "Inactive",t)}}
@Composable private fun PlanBox(v:String,l:String,t:P){Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(13.dp)).background(t.card2).padding(10.dp)){Text(v,color=t.ink,fontSize=14.sp,fontWeight=FontWeight.Black);Text(l,color=t.muted,fontSize=7.sp)}}
@Composable private fun Action(a:SignalAction,t:P,on:(SignalAction)->Unit){Row(Modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(13.dp)).background(t.cta).clickable{on(a)},verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.Center){Text(if(a.label.isBlank())"Collect Payment" else a.label,color=t.onAccent,fontSize=9.sp,fontWeight=FontWeight.Black);Spacer(Modifier.width(6.dp));Icon(Icons.Rounded.ArrowForward,null,tint=t.onAccent,modifier=Modifier.size(15.dp))}}
@Composable private fun Surface(t:P,m:Modifier=Modifier,content:@Composable ColumnScope.()->Unit){Column(m.clip(RoundedCornerShape(18.dp)).background(t.card.copy(alpha=.97f)).border(1.dp,t.line,RoundedCornerShape(18.dp)),content=content)}
private fun icon(id:ThemeId)=when(id){ThemeId.NATURAL_FRESH->Icons.Rounded.Eco;ThemeId.FUTURISTIC_NEON->Icons.Rounded.Bolt;ThemeId.MINIMAL_DARK->Icons.Rounded.FitnessCenter;ThemeId.GLASSMORPHISM->Icons.Rounded.FavoriteBorder;ThemeId.PREMIUM_3D->Icons.Rounded.WorkspacePremium;ThemeId.VIBRANT_GRADIENT->Icons.Rounded.AutoAwesome;ThemeId.BEAST_MODE->Icons.Rounded.LocalFireDepartment;ThemeId.PURPLE_ROYAL->Icons.Rounded.Diamond}
private fun eventAccent(e:EventType,t:P)=when(e){EventType.PAYMENT_FAILED,EventType.COMPLAINT->t.danger;EventType.MAINTENANCE->Color(0xFFFFA51F);EventType.PAYMENT,EventType.RENEWAL->t.good;else->t.accent}
private fun time(v:Long)=SimpleDateFormat("h:mm a",Locale.getDefault()).format(Date(v))
private fun rel(v:Long):String{val d=(System.currentTimeMillis()-v).coerceAtLeast(0);return when{d<60000->"Just now";d<3600000->"§{d/60000}m ago";else->"§{d/3600000}h ago"}}
