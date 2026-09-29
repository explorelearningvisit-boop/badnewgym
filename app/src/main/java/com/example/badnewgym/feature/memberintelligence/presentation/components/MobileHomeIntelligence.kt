package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.IntelligenceSignal
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.HomeDemoData
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.MemberHero
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.attendancePercent
import com.example.badnewgym.feature.memberintelligence.presentation.components.home.formatEventTime
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MobileHomeIntelligence(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    signals: List<IntelligenceSignal>,
    primarySignal: IntelligenceSignal?,
    secondarySignals: List<IntelligenceSignal>,
    cta: SignalAction?,
    theme: ThemeId,
    expanded: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onCta: (SignalAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = BADGymTheme.colors
    var more by remember(expanded) { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(if (expanded) 22.dp else 18.dp))
            .background(c.surface)
            .border(1.dp, c.border.copy(alpha = .65f), RoundedCornerShape(if (expanded) 22.dp else 18.dp))
    ) {
        val compact = maxWidth < 340.dp
        Column(Modifier.fillMaxSize()) {
            HomeHeader(snapshot, currentEvent, expanded, compact, onExpand, onCollapse)

            Column(
                Modifier.weight(1f).verticalScroll(rememberScrollState())
                    .padding(horizontal = if (compact) 9.dp else 11.dp, vertical = 7.dp),
                verticalArrangement = Arrangement.spacedBy(if (compact) 7.dp else 9.dp)
            ) {
                MemberHero(
                    snapshot = snapshot,
                    showStatusChip = true,
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(17.dp))
                        .background(c.surfaceElevated)
                        .border(1.dp, c.border.copy(alpha = .5f), RoundedCornerShape(17.dp))
                        .padding(if (compact) 9.dp else 11.dp)
                )
                TodayStatus(snapshot, currentEvent, compact)
                Kpis(snapshot, compact)
                PairTitles("Recent Activity", "This Week Overview", compact)
                RecentActivity(snapshot, currentEvent, compact)
                WeekOverview(snapshot, compact)

                if (expanded) {
                    PairTitles("Body Progress", "Focus Area", compact)
                    BodyProgress(snapshot, compact)
                    FocusArea(compact)
                    PairTitles("Upcoming", "New at Gym", compact)
                    Upcoming(snapshot, compact)
                    NewAtGym(snapshot, compact)
                    PairTitles("AI Insights", "Intelligence", compact)
                    AiInsights(signals, primarySignal, secondarySignals, compact)
                }

                if (more) MoreData(snapshot, signals, compact)

                Box(
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(13.dp))
                        .background(c.surfaceMuted)
                        .border(1.dp, c.border.copy(alpha = .55f), RoundedCornerShape(13.dp))
                        .clickable { if (!expanded) onExpand() else more = !more }
                        .padding(vertical = if (compact) 8.dp else 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        when {
                            !expanded -> "More Home Intelligence →"
                            more -> "Show Less ↑"
                            else -> "More Data +"
                        },
                        color = c.textPrimary, fontSize = 9.sp, fontWeight = FontWeight.Black
                    )
                }
            }

            Box(
                Modifier.fillMaxWidth()
                    .padding(horizontal = if (compact) 9.dp else 11.dp, vertical = 7.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(c.ctaGradient))
                    .clickable { cta?.let(onCta) }
                    .padding(vertical = if (compact) 10.dp else 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.CreditCard, null, tint = c.textOnAccent, modifier = Modifier.size(17.dp))
                    Spacer(Modifier.width(7.dp))
                    val label = if ((snapshot.payment?.totalOutstanding ?: 0.0) > 0) {
                        "Collect Payment →"
                    } else {
                        cta?.label ?: "Open Intelligence →"
                    }
                    Text(label, color = c.textOnAccent, fontSize = if (compact) 12.sp else 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(snapshot: MemberSnapshot, event: MemberEvent?, expanded: Boolean, compact: Boolean, onExpand: () -> Unit, onCollapse: () -> Unit) {
    val c = BADGymTheme.colors
    Row(Modifier.fillMaxWidth().padding(horizontal = if (compact) 9.dp else 11.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        if (expanded) {
            Box(Modifier.size(if (compact) 32.dp else 36.dp).clip(CircleShape).background(c.surfaceMuted).border(1.dp, c.border, CircleShape).clickable(onClick = onCollapse), contentAlignment = Alignment.Center) {
                Text("‹", color = c.textPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(7.dp))
        }
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("WORKOUT", color = c.textPrimary, fontSize = if (compact) 16.sp else 18.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.width(6.dp))
                SmallPill(eventLabel(event?.eventType ?: EventType.CHECK_IN), c.accentSoft, c.accentStrong)
            }
            Text(snapshot.identity.name + " • live decision workspace", color = c.textSecondary, fontSize = 8.5.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        SmallPill("9 live", c.successSoft, c.success)
        if (!expanded) {
            Spacer(Modifier.width(5.dp))
            SmallPill("OPEN", c.surfaceMuted, c.textSecondary, onExpand)
        }
    }
}

@Composable
private fun TodayStatus(snapshot: MemberSnapshot, event: MemberEvent?, compact: Boolean) {
    val c = BADGymTheme.colors
    val checked = event?.eventType == EventType.CHECK_IN
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp)).background(c.successSoft.copy(alpha=.68f)).border(1.dp,c.success.copy(alpha=.22f),RoundedCornerShape(17.dp)).padding(if(compact)9.dp else 11.dp),verticalAlignment=Alignment.CenterVertically) {
        Box(Modifier.size(if(compact)31.dp else 36.dp).clip(CircleShape).background(c.success),contentAlignment=Alignment.Center) {
            Icon(Icons.Rounded.CheckCircle,null,tint=Color.White,modifier=Modifier.size(19.dp))
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text("Today Status",color=c.textSecondary,fontSize=8.sp)
            Text(if(checked)"Checked In" else "Today",color=c.success,fontSize=12.sp,fontWeight=FontWeight.Black)
            Text((event?.occurredAt?.let(::formatEventTime) ?: "7:42 AM") + " • Main Floor",color=c.textSecondary,fontSize=8.sp)
        }
        Column(horizontalAlignment=Alignment.End) {
            Text(if(checked)"1h 12m" else "—",color=c.textPrimary,fontSize=11.sp,fontWeight=FontWeight.Bold)
            Text(snapshot.workout?.currentRoutine ?: "Workout",color=c.textSecondary,fontSize=7.sp,maxLines=1,overflow=TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun Kpis(snapshot: MemberSnapshot, compact: Boolean) {
    val c=BADGymTheme.colors
    val due=snapshot.payment?.totalOutstanding ?: 0.0
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(5.dp)) {
        Kpi("Attendance",(snapshot.attendance?.visits ?: 0).toString()+"/"+(snapshot.attendance?.target ?: 0),snapshot.attendancePercent().toString()+"%",c.success,compact)
        Kpi("Payment",money(due),if(due>0)(snapshot.payment?.overdueDays ?: 0).toString()+"d overdue" else "PAID",c.danger,compact)
        Kpi("Plan",(snapshot.membership?.daysRemaining ?: 0).toString(),"Days Left",c.vip,compact)
        Kpi("Workout",(snapshot.workout?.durationMinutes ?: 0).toString(),"min",c.info,compact)
        Kpi("PT",(snapshot.trainer?.sessionsUsed ?: 0).toString()+"/"+(snapshot.trainer?.sessionsTotal ?: 0),"sessions",Color(0xFF8B5CF6),compact)
    }
}

@Composable
private fun Kpi(label:String,value:String,sub:String,accent:Color,compact:Boolean) {
    val c=BADGymTheme.colors
    Column(Modifier.weight(1f).height(if(compact)75.dp else 83.dp).clip(RoundedCornerShape(14.dp)).background(c.surfaceElevated).border(1.dp,c.border.copy(alpha=.5f),RoundedCornerShape(14.dp)).padding(horizontal=3.dp,vertical=7.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.SpaceBetween) {
        Text(label,color=c.textSecondary,fontSize=7.sp,fontWeight=FontWeight.SemiBold,maxLines=1)
        Text(value,color=if(label=="Payment"&&value!="₹0")c.danger else c.textPrimary,fontSize=10.5.sp,fontWeight=FontWeight.Black,maxLines=1)
        Text(sub,color=if(label=="Payment"&&value!="₹0")c.danger else accent,fontSize=6.5.sp,fontWeight=FontWeight.Bold,maxLines=1)
        Row(Modifier.height(15.dp),horizontalArrangement=Arrangement.spacedBy(2.dp),verticalAlignment=Alignment.Bottom) {
            listOf(.3f,.5f,.68f,.82f,1f).forEach { h -> Box(Modifier.width(4.dp).height(15.dp*h).clip(RoundedCornerShape(2.dp)).background(accent.copy(alpha=.82f))) }
        }
    }
}

@Composable private fun PairTitles(a:String,b:String,compact:Boolean) {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
        Text(a,color=BADGymTheme.colors.textPrimary,fontSize=if(compact)10.5.sp else 11.5.sp,fontWeight=FontWeight.Black,modifier=Modifier.weight(1f))
        Text(b,color=BADGymTheme.colors.textPrimary,fontSize=if(compact)10.5.sp else 11.5.sp,fontWeight=FontWeight.Black,modifier=Modifier.weight(1f))
    }
}

@Composable
private fun RecentActivity(snapshot:MemberSnapshot,current:MemberEvent?,compact:Boolean) {
    val c=BADGymTheme.colors
    val events=buildList { current?.let(::add); addAll(snapshot.recentEvents.filter{it.id!=current?.id}) }.take(4)
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.surfaceElevated).border(1.dp,c.border.copy(alpha=.5f),RoundedCornerShape(16.dp)).padding(if(compact)9.dp else 11.dp)) {
        events.forEachIndexed { i,e ->
            Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
                Box(Modifier.size(23.dp).clip(CircleShape).background(c.accentSoft),contentAlignment=Alignment.Center){Icon(eventIcon(e.eventType),null,tint=c.accentStrong,modifier=Modifier.size(12.dp))}
                Spacer(Modifier.width(7.dp))
                Column(Modifier.weight(1f)){
                    Text(eventLabel(e.eventType),color=c.textPrimary,fontSize=8.5.sp,fontWeight=FontWeight.Bold)
                    Text(eventDetail(e.eventType,snapshot),color=c.textSecondary,fontSize=7.5.sp,maxLines=1,overflow=TextOverflow.Ellipsis)
                }
                Text(formatEventTime(e.occurredAt),color=c.textSecondary,fontSize=7.5.sp)
            }
            if(i<events.lastIndex)Spacer(Modifier.height(7.dp))
        }
        if(events.isEmpty())Text("No recent activity",color=c.textSecondary,fontSize=9.sp)
    }
}

@Composable
private fun WeekOverview(snapshot:MemberSnapshot,compact:Boolean) {
    val c=BADGymTheme.colors
    val values=snapshot.attendance?.weeklyPattern?.map{if(it>0).82f else .28f} ?: HomeDemoData.weeklyAttendance
    val labels=listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.surfaceElevated).border(1.dp,c.border.copy(alpha=.5f),RoundedCornerShape(16.dp)).padding(if(compact)9.dp else 11.dp)){
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("This Week Overview",color=c.textPrimary,fontSize=10.sp,fontWeight=FontWeight.Black);Text("This Week⌄",color=c.textSecondary,fontSize=7.5.sp)}
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth().height(if(compact)58.dp else 68.dp),horizontalArrangement=Arrangement.spacedBy(4.dp),verticalAlignment=Alignment.Bottom){
            values.take(7).forEachIndexed{i,v->Column(Modifier.weight(1f),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Bottom){Box(Modifier.fillMaxWidth().height((if(compact)40 else 50).dp*v).clip(RoundedCornerShape(topStart=4.dp,topEnd=4.dp)).background(if(i%2==0)c.info else c.success));Spacer(Modifier.height(3.dp));Text(labels[i],color=c.textMuted,fontSize=6.sp)}}
        }
        Spacer(Modifier.height(7.dp))
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(4.dp)){
            Ring(snapshot.attendancePercent().toString()+"%","Attendance",c.success,Modifier.weight(1f))
            Ring((snapshot.workout?.durationMinutes ?: 5).toString(),"Workout",c.info,Modifier.weight(1f))
            Ring((snapshot.attendance?.streakDays ?: 3).toString(),"Streak",c.warning,Modifier.weight(1f))
        }
    }
}

@Composable private fun Ring(value:String,label:String,color:Color,modifier:Modifier){
    val c=BADGymTheme.colors
    Row(modifier,verticalAlignment=Alignment.CenterVertically){
        Box(Modifier.size(28.dp),contentAlignment=Alignment.Center){Canvas(Modifier.fillMaxSize()){drawArc(c.surfaceMuted,-90f,360f,false,style=Stroke(3.dp.toPx()));drawArc(color,-90f,245f,false,style=Stroke(3.dp.toPx(),cap=StrokeCap.Round))};Text(value,color=c.textPrimary,fontSize=6.5.sp,fontWeight=FontWeight.Black)}
        Spacer(Modifier.width(3.dp));Text(label,color=c.textSecondary,fontSize=6.5.sp,maxLines=2)
    }
}

@Composable private fun BodyProgress(snapshot:MemberSnapshot,compact:Boolean){
    val c=BADGymTheme.colors
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.surfaceElevated).border(1.dp,c.border.copy(alpha=.5f),RoundedCornerShape(16.dp)).padding(if(compact)9.dp else 11.dp),verticalAlignment=Alignment.CenterVertically){
        MemberPhoto(photoUrl = snapshot.identity.photoUrl, tier = snapshot.identity.tier, size = if(compact)45.dp else 52.dp, showVerified = false)
        Text("→",color=c.textSecondary,fontSize=17.sp,modifier=Modifier.padding(horizontal=6.dp))
        MemberPhoto(snapshot.identity.photoUrl,snapshot.identity.tier,if(compact)45.dp else 52.dp,false)
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(3.dp)){Progress("Weight","− 1.8 kg",c.success);Progress("Body Fat","− 3.2%",c.success);Progress("Muscle","+ 2.1 kg",c.info)}
    }
}

@Composable private fun Progress(a:String,b:String,color:Color){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(a,color=BADGymTheme.colors.textSecondary,fontSize=7.5.sp);Text(b,color=color,fontSize=8.sp,fontWeight=FontWeight.Bold)}}

@Composable private fun FocusArea(compact:Boolean){
    val c=BADGymTheme.colors
    val parts=listOf("Chest" to 38f,"Back" to 22f,"Legs" to 18f,"Shoulders" to 14f,"Arms" to 8f)
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.surfaceElevated).border(1.dp,c.border.copy(alpha=.5f),RoundedCornerShape(16.dp)).padding(if(compact)9.dp else 11.dp)){
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("Focus Area",color=c.textPrimary,fontSize=10.sp,fontWeight=FontWeight.Black);Text("View All ›",color=c.textSecondary,fontSize=7.5.sp)}
        Spacer(Modifier.height(7.dp))
        Row(verticalAlignment=Alignment.CenterVertically){
            Box(Modifier.size(if(compact)62.dp else 70.dp),contentAlignment=Alignment.Center){Canvas(Modifier.fillMaxSize()){var start=-90f;val palette=listOf(c.info,c.accent,c.success,Color(0xFF8B5CF6),c.danger);parts.forEachIndexed{i,entry->val sweep=entry.second/100f*360f;drawArc(palette[i],start,sweep-3f,false,style=Stroke(9.dp.toPx()));start+=sweep}};Column(horizontalAlignment=Alignment.CenterHorizontally){Text("38%",color=c.textPrimary,fontSize=13.sp,fontWeight=FontWeight.Black);Text("Chest",color=c.textSecondary,fontSize=6.5.sp)}}
            Spacer(Modifier.width(8.dp));Column(verticalArrangement=Arrangement.spacedBy(2.dp)){parts.forEach{p->Row(verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(5.dp).clip(CircleShape).background(c.accent));Spacer(Modifier.width(3.dp));Text(p.first,color=c.textSecondary,fontSize=7.sp,modifier=Modifier.width(48.dp));Text(p.second.toInt().toString()+"%",color=c.textPrimary,fontSize=7.sp,fontWeight=FontWeight.Bold)}}}
        }
    }
}

@Composable private fun Upcoming(snapshot:MemberSnapshot,compact:Boolean){
    val c=BADGymTheme.colors
    val rows=listOf("PT Session" to "Tomorrow, 6:30 PM","Payment Due" to "12 Oct 2026","Plan Renewal" to ((snapshot.membership?.daysRemaining ?: 0).toString()+" days"))
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.surfaceElevated).border(1.dp,c.border.copy(alpha=.5f),RoundedCornerShape(16.dp)).padding(if(compact)9.dp else 11.dp)){
        rows.forEachIndexed{i,r->Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(24.dp).clip(CircleShape).background(c.accentSoft),contentAlignment=Alignment.Center){Icon(if(i==0)Icons.Rounded.FitnessCenter else if(i==1)Icons.Rounded.CreditCard else Icons.Rounded.CalendarMonth,null,tint=c.accentStrong,modifier=Modifier.size(13.dp))};Spacer(Modifier.width(6.dp));Column(Modifier.weight(1f)){Text(r.first,color=c.textPrimary,fontSize=8.sp,fontWeight=FontWeight.Bold);Text(r.second,color=c.textSecondary,fontSize=7.sp)};Icon(Icons.Rounded.ArrowForward,null,tint=c.textMuted,modifier=Modifier.size(12.dp))};if(i<rows.lastIndex)Spacer(Modifier.height(6.dp))}
    }
}

@Composable private fun NewAtGym(snapshot:MemberSnapshot,compact:Boolean){
    val c=BADGymTheme.colors
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.surfaceElevated).border(1.dp,c.border.copy(alpha=.5f),RoundedCornerShape(16.dp)).padding(if(compact)9.dp else 11.dp),verticalAlignment=Alignment.CenterVertically){
        val image=snapshot.supplements?.imageUrl
        if(image!=null)AvatarImage(model=image,size=if(compact)50.dp else 58.dp,shape=RoundedCornerShape(11.dp))
        else Box(Modifier.size(if(compact)50.dp else 58.dp).clip(RoundedCornerShape(11.dp)).background(c.surfaceMuted),contentAlignment=Alignment.Center){Icon(Icons.Rounded.FitnessCenter,null,tint=c.accent,modifier=Modifier.size(23.dp))}
        Spacer(Modifier.width(7.dp));Column(Modifier.weight(1f)){Text("New at Gym",color=c.textSecondary,fontSize=7.sp);Text(snapshot.supplements?.lastPurchaseName ?: "Whey Protein 1 Kg",color=c.textPrimary,fontSize=9.sp,fontWeight=FontWeight.Black,maxLines=2,overflow=TextOverflow.Ellipsis);Text(money(snapshot.supplements?.lastPurchasePrice ?: 2499.0),color=c.danger,fontSize=10.sp,fontWeight=FontWeight.Black)};SmallPill("New Stock",c.successSoft,c.success)
    }
}

@Composable private fun AiInsights(signals:List<IntelligenceSignal>,primary:IntelligenceSignal?,secondary:List<IntelligenceSignal>,compact:Boolean){
    val c=BADGymTheme.colors
    val list=buildList{primary?.let(::add);addAll(secondary);addAll(signals)}.distinctBy{it.id}.take(4)
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.surfaceElevated).border(1.dp,c.border.copy(alpha=.5f),RoundedCornerShape(16.dp)).padding(if(compact)9.dp else 11.dp)){
        Row(verticalAlignment=Alignment.CenterVertically){Icon(Icons.Rounded.AutoAwesome,null,tint=c.accent,modifier=Modifier.size(14.dp));Spacer(Modifier.width(4.dp));Text("AI Insights",color=c.textPrimary,fontSize=10.sp,fontWeight=FontWeight.Black);Spacer(Modifier.weight(1f));Text("View All ›",color=c.textSecondary,fontSize=7.5.sp)}
        Spacer(Modifier.height(6.dp))
        if(list.isEmpty())Text("Good time to start • member data is stable",color=c.textSecondary,fontSize=8.sp)
        else list.forEachIndexed{i,s->Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(9.dp)).background(c.accentSoft.copy(alpha=.25f)).padding(6.dp),verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(19.dp).clip(CircleShape).background(if(i==0)c.danger else c.info),contentAlignment=Alignment.Center){Text("!",color=Color.White,fontSize=8.sp,fontWeight=FontWeight.Black)};Spacer(Modifier.width(5.dp));Column(Modifier.weight(1f)){Text(s.title,color=c.textPrimary,fontSize=8.sp,fontWeight=FontWeight.Bold,maxLines=1,overflow=TextOverflow.Ellipsis);Text(s.subtitle,color=c.textSecondary,fontSize=6.8.sp,maxLines=1,overflow=TextOverflow.Ellipsis)};Icon(Icons.Rounded.ArrowForward,null,tint=c.textMuted,modifier=Modifier.size(11.dp))};if(i<list.lastIndex)Spacer(Modifier.height(4.dp))}
    }
}

@Composable private fun MoreData(snapshot:MemberSnapshot,signals:List<IntelligenceSignal>,compact:Boolean){
    val c=BADGymTheme.colors
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.surfaceMuted.copy(alpha=.65f)).border(1.dp,c.border.copy(alpha=.45f),RoundedCornerShape(16.dp)).padding(if(compact)9.dp else 11.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
        Text("More member intelligence",color=c.textPrimary,fontSize=10.sp,fontWeight=FontWeight.Black)
        Detail(Icons.Rounded.Person,"Member",snapshot.identity.code ?: "—")
        Detail(Icons.Rounded.Restaurant,"Nutrition",snapshot.nutrition?.planName ?: "Not subscribed")
        Detail(Icons.Rounded.Star,"Signals",signals.size.toString()+" detected")
        Detail(Icons.Rounded.CalendarMonth,"Services",(snapshot.services?.count{it.isActive} ?: 0).toString()+" active")
    }
}

@Composable private fun Detail(icon:androidx.compose.ui.graphics.vector.ImageVector,label:String,value:String){
    val c=BADGymTheme.colors
    Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){Icon(icon,null,tint=c.accent,modifier=Modifier.size(14.dp));Spacer(Modifier.width(6.dp));Text(label,color=c.textSecondary,fontSize=8.sp,modifier=Modifier.weight(1f));Text(value,color=c.textPrimary,fontSize=8.sp,fontWeight=FontWeight.Bold,maxLines=1,overflow=TextOverflow.Ellipsis)}
}

@Composable private fun SmallPill(text:String,bg:Color,fg:Color,onClick:(()->Unit)?=null){
    Box(Modifier.clip(RoundedCornerShape(10.dp)).background(bg).then(if(onClick!=null)Modifier.clickable{onClick()}else Modifier).padding(horizontal=7.dp,vertical=4.dp)){Text(text,color=fg,fontSize=7.sp,fontWeight=FontWeight.Bold,maxLines=1)}
}

private fun eventIcon(t:EventType)=when(t){EventType.CHECK_IN->Icons.Rounded.CheckCircle;EventType.WORKOUT->Icons.Rounded.FitnessCenter;EventType.TRAINER_SESSION->Icons.Rounded.Person;EventType.PAYMENT->Icons.Rounded.CreditCard;else->Icons.Rounded.CalendarMonth}
private fun eventLabel(t:EventType)=when(t){EventType.CHECK_IN->"Checked In";EventType.CHECK_OUT->"Checked Out";EventType.WORKOUT->"Workout";EventType.TRAINER_SESSION->"PT Session";EventType.PAYMENT->"Payment";EventType.SUPPLEMENT_PURCHASE->"Supplement";EventType.RENEWAL->"Plan Renewal";else->t.name.replace('_',' ').lowercase(Locale.getDefault()).replaceFirstChar{it.titlecase(Locale.getDefault())}}
private fun eventDetail(t:EventType,s:MemberSnapshot)=when(t){EventType.WORKOUT->s.workout?.currentRoutine ?: "Workout";EventType.TRAINER_SESSION->s.trainer?.trainerName ?: "Trainer";EventType.PAYMENT->"Payment recorded";else->"Main Floor"}
private fun money(value:Double)="₹"+NumberFormat.getNumberInstance(Locale("en","IN")).format(value.toInt())
