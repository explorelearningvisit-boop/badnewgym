package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.colors.ThemeResolver
import com.example.badnewgym.feature.memberintelligence.design.dimensions.CompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.design.semantics.MemberSemanticStyle
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberEvent
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType

@Composable
fun PersistentMemberContextHeader(snapshot: MemberSnapshot, currentEvent: MemberEvent?, theme: ThemeId, semantics: MemberSemanticStyle, dimensions: CompactCardDimensions, activeMenu: MenuType, onIdentityClick: () -> Unit = {}) {
    val colors = BADGymTheme.colors
    val compact = activeMenu != MenuType.HOME
    val eventType = currentEvent?.eventType ?: EventType.CHECK_IN
    val eventColor = ThemeResolver.resolveEventBadgeColor(eventType, colors)
    if (compact) {
        Row(modifier = Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(14.dp)).background(colors.surfaceMuted.copy(alpha = 0.82f)).border(0.8.dp, colors.border.copy(alpha = 0.55f), RoundedCornerShape(14.dp)).clickable(onClick = onIdentityClick).padding(horizontal = 7.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
            MemberPhoto(photoUrl = snapshot.identity.photoUrl, tier = snapshot.identity.tier, width = 36.dp, height = 36.dp, showVerified = false, memberName = snapshot.identity.name)
            Spacer(Modifier.width(7.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(snapshot.identity.name, color = colors.textPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    if (snapshot.identity.isVerified) Icon(Icons.Rounded.CheckCircle, "Verified", tint = colors.vip, modifier = Modifier.size(11.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(snapshot.identity.code ?: "BG---", color = colors.textSecondary, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold)
                    Text(snapshot.membership?.planName ?: "Plan unavailable", color = colors.textMuted, fontSize = 9.5.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Box(modifier = Modifier.clip(RoundedCornerShape(7.dp)).background(eventColor).padding(horizontal = 5.dp, vertical = 3.dp)) {
                Text(eventType.displayLabel().uppercase(), color = colors.textOnAccent, fontSize = 7.5.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            if (semantics.isUrgent) Icon(Icons.Rounded.WarningAmber, "Attention required", tint = colors.danger, modifier = Modifier.padding(start = 4.dp).size(15.dp))
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.clip(RoundedCornerShape(7.dp)).background(eventColor).padding(horizontal = 7.dp, vertical = 3.dp)) {
                    Text("+ " + eventType.displayLabel(), color = colors.textOnAccent, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                }
                Text(theme.timeText + " • " + theme.timeRelative, color = colors.textMuted, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
            }
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onIdentityClick), horizontalArrangement = Arrangement.spacedBy(7.dp), verticalAlignment = Alignment.CenterVertically) {
                MemberPhoto(photoUrl = snapshot.identity.photoUrl, tier = snapshot.identity.tier, width = dimensions.detailPortraitWidth, height = dimensions.detailPortraitHeight, showVerified = snapshot.identity.isVerified, memberName = snapshot.identity.name)
                Column(modifier = Modifier.weight(1f)) {
                    Text(snapshot.identity.name, color = colors.textPrimary, fontSize = 16.5.sp, fontWeight = FontWeight.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text((snapshot.identity.code ?: "BG---") + " • " + (snapshot.membership?.planName ?: "Plan unavailable"), color = colors.textSecondary, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                if (semantics.isUrgent) Icon(Icons.Rounded.WarningAmber, "Attention required", tint = colors.danger, modifier = Modifier.size(18.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                ContextChip(snapshot.identity.tier.name, Modifier.weight(1f))
                ContextChip(snapshot.membership?.let { if (it.isActive) it.daysRemaining.toString() + "d left" else "Inactive" } ?: "Plan unavailable", Modifier.weight(1f))
                ContextChip(if (semantics.isUrgent) "ATTENTION" else "ACTIVE", Modifier.weight(1f), semantics.isUrgent)
            }
        }
    }
}

@Composable
private fun ContextChip(text: String, modifier: Modifier = Modifier, danger: Boolean = false) {
    val colors = BADGymTheme.colors
    Box(modifier = modifier.clip(RoundedCornerShape(8.dp)).background(if (danger) colors.danger.copy(alpha = 0.10f) else colors.surfaceMuted).border(0.6.dp, if (danger) colors.danger.copy(alpha = 0.45f) else colors.border.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(horizontal = 6.dp, vertical = 3.dp), contentAlignment = Alignment.Center) {
        Text(text, color = if (danger) colors.danger else colors.textSecondary, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
