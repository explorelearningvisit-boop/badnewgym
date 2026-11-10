package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.design.MemberCardAccent
import com.example.badnewgym.feature.membercard.design.MemberCardAccentManager
import com.example.badnewgym.feature.membercard.presentation.intelligence.NeuStyle
import com.example.badnewgym.feature.membercard.presentation.intelligence.NeuTheme
import com.example.badnewgym.feature.membercard.presentation.intelligence.NeuThemes

/**
 * Small, honest action surface. It exposes only actions that are implemented
 * in this build: search, notifications, receipts, and the four canonical
 * member-card colour variants from the master mockup.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberQuickActionsSheet(
    onDismissRequest: () -> Unit,
    onSearch: () -> Unit,
    onNotifications: () -> Unit,
    onReceipt: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        scrimColor = Color(0x66021C17),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        GlassSheetSurface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                GlassSheetHeader(
                    title = "Quick actions",
                    subtitle = "Member workspace",
                    onClose = onDismissRequest
                )

                Spacer(modifier = Modifier.height(14.dp))

                GlassActionTile(
                    icon = Icons.Default.Search,
                    title = "Search records",
                    subtitle = "Check-ins, payments, workouts",
                    iconTint = InfographicTokens.Brand,
                    onClick = onSearch
                )
                Spacer(modifier = Modifier.height(9.dp))
                GlassActionTile(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Gym updates and reminders",
                    iconTint = InfographicTokens.Purple,
                    onClick = onNotifications
                )
                Spacer(modifier = Modifier.height(9.dp))
                GlassActionTile(
                    icon = Icons.Default.ReceiptLong,
                    title = "Receipts",
                    subtitle = "Your latest payment receipt",
                    iconTint = InfographicTokens.Orange,
                    onClick = onReceipt
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Card colour variants — re-skins the whole member card accent.
                GlassSectionLabel(text = "Card colour")
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    MemberCardAccentManager.all.forEach { accent ->
                        AccentSwatch(
                            accent = accent,
                            isSelected = MemberCardAccentManager.activeAccent == accent,
                            onClick = { MemberCardAccentManager.select(accent) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AppearanceSection()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "BAD GYM member workspace",
                    fontSize = 11.5.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GlassFaint,
                    modifier = Modifier.fillMaxWidth()
                )

                GlassSheetFooterPad(height = 14)
            }
        }
    }
}

@Composable
private fun AccentSwatch(
    accent: MemberCardAccent,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "swatchScale"
    )
    val rim by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = "swatchRim"
    )

    Column(
        modifier = modifier
            .scale(scale)
            .clickable(interactionSource = interaction, indication = null) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .shadow(
                    elevation = (3f + rim * 5f).dp,
                    shape = CircleShape,
                    ambientColor = accent.color.copy(alpha = 0.55f),
                    spotColor = accent.color.copy(alpha = 0.65f)
                )
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            accent.color.copy(alpha = 0.92f),
                            accent.color
                        )
                    )
                )
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.45f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = accent.title,
                    tint = Color.White,
                    modifier = Modifier.size(21.dp)
                )
            }
        }
        Text(
            text = accent.title,
            fontSize = 12.5.sp,
            lineHeight = 15.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
            color = if (isSelected) GlassInk else GlassInkSoft,
            maxLines = 1
        )
    }
}

@Composable
private fun AppearanceSection() {
    val activeTheme = NeuStyle.theme
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        GlassSectionLabel(text = "Dashboard theme")
        // Seven skins in a 4 + 3 grid so no label is ever clipped by a scroll edge.
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            NeuThemes.all.chunked(4).forEach { rowThemes ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    rowThemes.forEach { theme ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            ThemeSwatch(
                                theme = theme,
                                isSelected = theme.id == activeTheme.id,
                                onClick = { NeuStyle.selectTheme(theme.id) }
                            )
                        }
                    }
                    repeat(4 - rowThemes.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        ScaleSlider("Text size", NeuStyle.textScale, NeuStyle.TEXT_MIN, NeuStyle.TEXT_MAX) {
            NeuStyle.updateTextScale(it)
        }
        ScaleSlider("Icons & controls", NeuStyle.componentScale, NeuStyle.COMPONENT_MIN, NeuStyle.COMPONENT_MAX) {
            NeuStyle.updateComponentScale(it)
        }
        ScaleSlider("Corner radius", NeuStyle.radiusScale, NeuStyle.RADIUS_MIN, NeuStyle.RADIUS_MAX) {
            NeuStyle.updateRadiusScale(it)
        }
        ScaleSlider("Shadow depth", NeuStyle.depthScale, NeuStyle.DEPTH_MIN, NeuStyle.DEPTH_MAX) {
            NeuStyle.updateDepthScale(it)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                .clickable { NeuStyle.reset() }
                .padding(vertical = 9.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "↺ Reset to Guide Defaults",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF6D28D9)
            )
        }
    }
}

@Composable
private fun ThemeSwatch(
    theme: NeuTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clickable { onClick() }
                .shadow(if (isSelected) 4.dp else 1.dp, CircleShape)
                .clip(CircleShape)
                .background(theme.surface)
                .border(
                    width = if (isSelected) 2.5.dp else 1.dp,
                    color = if (isSelected) theme.violet else Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(theme.violet)
            )
        }
        Text(
            text = theme.label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
            color = if (isSelected) GlassInk else GlassInkSoft,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ScaleSlider(
    label: String,
    value: Float,
    min: Float,
    max: Float,
    onChange: (Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                label,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GlassInk
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFEDE9FE))
                    .border(1.dp, Color(0xFFDDD6FE), RoundedCornerShape(50))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    "${(value * 100).toInt()}%",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF6D28D9)
                )
            }
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = min..max,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF7C3AED),
                activeTrackColor = Color(0xFF7C3AED),
                inactiveTrackColor = Color(0xFFE2E8F0)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
