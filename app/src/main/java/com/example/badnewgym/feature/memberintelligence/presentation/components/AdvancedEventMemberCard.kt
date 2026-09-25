package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.dimensions.CompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.domain.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdvancedEventMemberCard(
    snapshot: MemberSnapshot,
    currentEvent: MemberEvent?,
    theme: ThemeId,
    cta: SignalAction?,
    dimensions: CompactCardDimensions,
    isSelected: Boolean,
    onClick: () -> Unit,
    onCtaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val event = currentEvent ?: snapshot.recentEvents.firstOrNull()
    val spec = event?.let { EventCardCatalog.forEvent(it.eventType) } ?: EventCardCatalog.forEvent(EventType.CHECK_IN)
    val colors = BADGymTheme.colors
    val accent = when (spec.kind) {
        EventCardKind.BAN -> colors.danger
        EventCardKind.FREEZE -> colors.info
        EventCardKind.PAYMENT -> colors.warning
        EventCardKind.WALK_IN, EventCardKind.TRIAL -> colors.warning
        EventCardKind.TRAINER -> colors.info
        EventCardKind.WORKOUT -> colors.success
        EventCardKind.FACILITY_OPERATION, EventCardKind.ISSUE_RESOLUTION -> colors.danger
        EventCardKind.SERVICE -> colors.accent
        else -> colors.accent
    }
    val border by animateColorAsState(if (isSelected) accent else accent.copy(alpha = 0.22f), tween(180), label = "event-border")

    Box(
        modifier = modifier.width(dimensions.cardWidth).height(dimensions.cardHeight)
            .clip(RoundedCornerShape(28.dp)).background(colors.surface)
            .border(if (isSelected) 1.6.dp else 1.dp, border, RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
    ) {
        Box(Modifier.fillMaxWidth().height(100.dp).background(Brush.horizontalGradient(listOf(accent.copy(alpha = 0.13f), colors.surface))))
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(spec.title, color = colors.textPrimary, fontSize = 19.sp, fontWeight = FontWeight.Black, maxLines = 2)
            Text(snapshot.identity.name, color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(spec.storyLabel, color = accent, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
            EventFacts(snapshot, event, spec.kind, accent)
            if (spec.kind == EventCardKind.CHECK_IN) AttendancePulse(snapshot, accent)
            cta?.let {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(accent).clickable(onClick = onCtaClick).padding(12.dp),
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(it.label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Rounded.ArrowForward, null, tint = Color.White, modifier = Modifier.padding(start = 6.dp).size(15.dp))
                }
            }
        }
    }
}

@Composable
private fun EventFacts(s: MemberSnapshot, e: MemberEvent?, kind: EventCardKind, accent: Color) {
    val c = BADGymTheme.colors
    val m = e?.metadata.orEmpty()
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(c.surface.copy(alpha = 0.97f))
            .border(1.dp, c.border.copy(alpha = 0.7f), RoundedCornerShape(18.dp)).padding(11.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (kind) {
            EventCardKind.WALK_IN, EventCardKind.TRIAL -> {
                Pill(trialState(e), accent)
                Text("WALK-IN  →  TRIAL  →  OUTCOME", color = c.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                FactsRow("Walk-in", m["walkInAt"] ?: eventDate(e), "Trial", m["startedAt"] ?: "Not recorded")
                FactsRow("Expiry", m["expiresAt"] ?: "Not recorded", "Outcome", m["convertedAt"] ?: m["expiredAt"] ?: "Pending")
            }
            EventCardKind.FREEZE -> {
                Pill(if (e?.eventType == EventType.FREEZE_ENDED) "ACTIVE AGAIN" else "FROZEN", accent)
                FactsRow("Plan", s.membership?.planName ?: "—", "Start", m["freezeStart"] ?: eventDate(e))
                FactsRow("End", m["freezeEnd"] ?: "—", "Reason", m["reason"] ?: "Not recorded")
            }
            EventCardKind.BAN -> {
                Pill(if (e?.eventType == EventType.BAN_LIFTED) "ACCESS RESTORED" else "ACCESS BLOCKED", accent)
                FactsRow("When", m["banAt"] ?: eventDate(e), "Reason", m["reason"] ?: "Recorded restriction")
                FactsRow("Actor", m["actor"] ?: "—", "Lifted", m["liftedAt"] ?: "—")
            }
            EventCardKind.PAYMENT -> {
                FactsRow("Outstanding", money(s.payment?.totalOutstanding), "Due", s.payment?.dueDate?.let(::date) ?: "—")
                FactsRow("Last paid", money(s.payment?.lastPaymentAmount), "Overdue", s.payment?.overdueDays?.toString() ?: "0 days")
            }
            EventCardKind.TRAINER -> {
                FactsRow("Coach", s.trainer?.trainerName ?: "—", "Next", s.trainer?.nextSessionDate?.let(::date) ?: "—")
                FactsRow("Sessions", s.trainer?.sessionsUsed?.toString() ?: "—", "Remaining", s.trainer?.let { (it.sessionsTotal - it.sessionsUsed).coerceAtLeast(0).toString() } ?: "—")
            }
            EventCardKind.WORKOUT -> {
                FactsRow("Routine", s.workout?.currentRoutine ?: "—", "Last", s.workout?.lastWorkoutDate?.let(::date) ?: "—")
                FactsRow("Duration", s.workout?.durationMinutes?.let { it.toString() + " min" } ?: "—", "Event", e?.eventType?.displayLabel() ?: "—")
            }
            EventCardKind.SERVICE -> {
                val active = s.services.orEmpty().filter { it.isActive }
                if (active.isEmpty()) {
                    Pill("NO ACTIVE SERVICE", c.textMuted)
                    Text("No active entitlement. This is informational, not an error.", color = c.textSecondary, fontSize = 10.5.sp)
                } else {
                    active.take(3).forEach { Text(it.serviceName + "  •  " + (it.expiryDate?.let(::date) ?: "ACTIVE"), color = c.textPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold) }
                }
            }
            EventCardKind.FACILITY_OPERATION, EventCardKind.ISSUE_RESOLUTION -> {
                Pill(e?.eventType?.displayLabel() ?: "ISSUE", accent)
                Text(m["asset"] ?: m["reason"] ?: s.issues.firstOrNull()?.description ?: "Recorded operational issue", color = c.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 2)
                FactsRow("Reported", m["reportedAt"] ?: eventDate(e), "Owner", m["assignee"] ?: "Unassigned")
            }
            else -> {
                FactsRow("State", if (s.membership?.isActive == true) "ACTIVE" else "INACTIVE", "Visits", s.attendance?.visits?.toString() ?: "—")
                FactsRow("Plan", s.membership?.planName ?: "—", "Event", e?.eventType?.displayLabel() ?: "—")
            }
        }
    }
}

@Composable private fun FactsRow(a:String,b:String,c:String,d:String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Fact(a,b, Modifier.weight(1f)); Fact(c,d, Modifier.weight(1f))
    }
}
@Composable private fun Fact(label:String,value:String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(label.uppercase(), color=BADGymTheme.colors.textMuted, fontSize=8.sp, fontWeight=FontWeight.Black)
        Text(value, color=BADGymTheme.colors.textPrimary, fontSize=10.5.sp, fontWeight=FontWeight.Bold, maxLines=2, overflow=TextOverflow.Ellipsis)
    }
}
@Composable private fun Pill(text:String,color:Color) {
    Text(text,color=color,fontSize=9.sp,fontWeight=FontWeight.Black,letterSpacing=.6.sp,
        modifier=Modifier.clip(RoundedCornerShape(8.dp)).background(color.copy(alpha=.1f)).padding(horizontal=8.dp,vertical=5.dp))
}
@Composable private fun AttendancePulse(s:MemberSnapshot,accent:Color) {
    val p=s.attendance?.weeklyPattern.orEmpty()
    if(p.size<7)return
    val max=p.maxOrNull()?.coerceAtLeast(1)?:return
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(BADGymTheme.colors.surfaceMuted.copy(alpha=.55f)).padding(10.dp)) {
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
            Text("7-DAY ATTENDANCE",color=BADGymTheme.colors.textPrimary,fontSize=9.sp,fontWeight=FontWeight.Black)
            Text(p.sum().toString()+" visits",color=accent,fontSize=9.sp,fontWeight=FontWeight.Bold)
        }
        Row(Modifier.fillMaxWidth().height(55.dp),horizontalArrangement=Arrangement.spacedBy(6.dp),verticalAlignment=Alignment.Bottom) {
            p.take(7).forEachIndexed { i,v ->
                Column(Modifier.weight(1f),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Bottom) {
                    Box(Modifier.width(15.dp).height((8+(44f*v/max)).dp).clip(RoundedCornerShape(6.dp)).background(if(v>0)accent else BADGymTheme.colors.surfaceMuted))
                    Text(listOf("M","T","W","T","F","S","S")[i],color=BADGymTheme.colors.textMuted,fontSize=8.sp)
                }
            }
        }
    }
}
private fun trialState(e:MemberEvent?):String=when(e?.eventType){EventType.WALK_IN->"TRIAL OFFERED";EventType.TRIAL_STARTED->"TRIAL ACTIVE";EventType.TRIAL_CONVERTED->"CONVERTED";EventType.TRIAL_EXPIRED->"EXPIRED";else->"TRIAL"}
private fun eventDate(e:MemberEvent?):String=e?.let{date(it.occurredAt)}?:"—"
private fun date(v:Long):String=SimpleDateFormat("dd MMM",Locale.getDefault()).format(Date(v))
private fun money(v:Double?):String=v?.let{"₹"+String.format(Locale.US,"%,.0f",it)}?:"—"
