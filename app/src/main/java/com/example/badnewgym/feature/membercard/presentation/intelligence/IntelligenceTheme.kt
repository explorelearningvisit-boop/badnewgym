package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.BadGymDashboardTheme
import com.example.badnewgym.feature.membercard.design.BadGymThemeManager

/**
 * Resolved visual tokens for the Member Intelligence dashboard, derived from the
 * active [BadGymDashboardTheme] so all 10 pack themes drive every screen.
 */
data class IntelligenceColors(
    val accent: Color,
    val accentDeep: Color,
    val accentWash: Color,
    val accentSoft: Color,
    val onAccent: Color,
    val headerBand: Color,
    val ink: Color,
    val inkSoft: Color,
    val muted: Color,
    val faint: Color,
    val cardFill: Color,
    val cardEdge: Color,
    val good: Color,
    val warn: Color,
    val bad: Color,
    val info: Color,
    val activeTabBrush: Brush,
    val onActiveTab: Color
)

/** Maps a pack theme onto the dashboard token set. Light accents get dark foregrounds. */
fun BadGymDashboardTheme.intelligenceColors(): IntelligenceColors {
    val lightAccent = primaryColor.luminance() > 0.5f
    return IntelligenceColors(
        accent = primaryColor,
        accentDeep = secondaryColor,
        accentWash = primaryColor.copy(alpha = 0.10f),
        accentSoft = primaryColor.copy(alpha = 0.22f),
        onAccent = if (lightAccent) Color(0xFF1A2E05) else Color.White,
        headerBand = secondaryColor,
        ink = Color(0xFF0F172A),
        inkSoft = Color(0xFF334155),
        muted = Color(0xFF64748B),
        faint = Color(0xFF94A3B8),
        cardFill = Color.White,
        cardEdge = Color(0x14000000),
        good = Color(0xFF16A34A),
        warn = Color(0xFFF59E0B),
        bad = Color(0xFFEF4444),
        info = Color(0xFF38BDF8),
        activeTabBrush = Brush.linearGradient(listOf(primaryColor, secondaryColor)),
        onActiveTab = if (lightAccent) Color(0xFF1A2E05) else Color.White
    )
}

@Composable
fun intelligenceColors(): IntelligenceColors {
    val theme = BadGymThemeManager.activeTheme
    return remember(theme) { theme.intelligenceColors() }
}

/** Shared white section card with a colored icon tile, title and trailing slot. */
@Composable
fun IntelligenceSectionCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val c = intelligenceColors()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(c.cardFill)
            .border(1.dp, c.cardEdge, RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(c.accentWash),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = c.accentDeep, modifier = Modifier.size(17.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = c.ink, letterSpacing = (-0.2).sp)
                if (subtitle != null) {
                    Text(subtitle, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = c.muted)
                }
            }
            trailing?.invoke(this)
        }
        content()
    }
}

/** Horizontally scrolling filter pill row used by the Body screen. */
@Composable
fun IntelligenceChipRow(
    chips: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    Row(modifier = modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        chips.forEachIndexed { index, chip ->
            val isSel = index == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSel) c.accent else c.accentWash)
                    .border(1.dp, if (isSel) c.accent else c.accentSoft, RoundedCornerShape(50))
                    .clickable { onSelect(index) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    chip,
                    fontSize = 9.5.sp,
                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSel) c.onAccent else c.accentDeep,
                    maxLines = 1
                )
            }
        }
    }
}

/** Small colored dot + label used in legends. */
@Composable
fun IntelligenceLegendDot(color: Color, label: String, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(color))
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = c.inkSoft, maxLines = 1)
    }
}

/** Amber explanatory note row (e.g. drop explanations, prepaid cycles). */
@Composable
fun IntelligenceNote(text: String, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(c.warn.copy(alpha = 0.10f))
            .padding(horizontal = 9.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(Icons.Filled.Info, contentDescription = null, tint = c.warn, modifier = Modifier.size(12.dp))
        Text(text, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = c.inkSoft, lineHeight = 12.sp)
    }
}
