package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.*
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import kotlin.math.roundToInt

// =============================================================================
// MODULAR BODY INTELLIGENCE WORKSPACE (PROFESSIONAL FIXED-VIEWPORT SYSTEM)
// =============================================================================

/**
 * Modular Body Intelligence Workspace.
 *
 * Implements mathematical symmetry, unified typography, equal-column weights,
 * uniform heights, and ultra-compact vertical density to guarantee zero clipping
 * and complete visibility inside the mobile fixed viewport.
 */
fun LazyListScope.bodyModularWorkspace(state: BodyIntelligenceState) {
    // 1. PRIMARY HORIZONTAL NAVIGATION (Compact 26dp Height)
    item(key = "body_primary_nav") {
        BodyPrimaryNavBar(
            selectedTab = state.primaryTab,
            onTabSelected = { state.primaryTab = it }
        )
    }

    // 2. CONTEXTUAL SECONDARY NAVIGATION (Compact 20dp Height)
    item(key = "body_secondary_nav") {
        BodySecondaryNavBar(state = state)
    }

    // 3. DEDICATED SYMMETRICAL MODULE INTERFACE
    item(key = "body_module_content") {
        when (state.primaryTab) {
            BodyPrimaryTab.OVERVIEW -> BodyOverviewModule(state = state)
            BodyPrimaryTab.COMPOSITION -> BodyCompositionModule(state = state)
            BodyPrimaryTab.MEASUREMENTS -> BodyMeasurementsModule(state = state)
            BodyPrimaryTab.PROGRESS -> BodyProgressModule(state = state)
            BodyPrimaryTab.GALLERY -> BodyGalleryModule(state = state)
            BodyPrimaryTab.GOALS -> BodyGoalsModule(state = state)
            BodyPrimaryTab.HEALTH -> BodyHealthModule(state = state)
            BodyPrimaryTab.MORE -> BodyMoreModule(state = state)
        }
    }
}

// =============================================================================
// REUSABLE DESIGN TOKENS & MASTER COMPONENTS
// =============================================================================

@Composable
fun BodyMasterCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder),
        shadowElevation = 0.5.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            content = content
        )
    }
}

@Composable
fun CardHeaderStrip(
    title: String,
    icon: ImageVector,
    trailingText: String? = null,
    trailingBadge: String? = null,
    trailingBadgeColor: Color = BodyIntelPalette.Primary,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BodyIntelPalette.Primary,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = title,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = BodyIntelPalette.Text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (actionLabel != null && onActionClick != null) {
            Surface(
                onClick = onActionClick,
                shape = RoundedCornerShape(8.dp),
                color = BodyIntelPalette.Primary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = actionLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        } else if (trailingBadge != null) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = trailingBadgeColor.copy(alpha = 0.12f)
            ) {
                Text(
                    text = trailingBadge,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = trailingBadgeColor,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
        } else if (trailingText != null) {
            Text(
                text = trailingText,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = BodyIntelPalette.Secondary
            )
        }
    }
}

@Composable
fun UniformMetricPod(
    label: String,
    value: String,
    unit: String = "",
    badge: String? = null,
    badgeColor: Color = BodyIntelPalette.Primary,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(10.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = BodyIntelPalette.Secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (badge != null) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                } else if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BodyIntelPalette.Secondary,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = BodyIntelPalette.Text,
                    lineHeight = 16.sp
                )
                if (unit.isNotEmpty()) {
                    Text(
                        text = unit,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BodyIntelPalette.Primary,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun UniformModuleTile(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(28.dp),
        shape = RoundedCornerShape(8.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = BodyIntelPalette.Primary,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = BodyIntelPalette.Text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// =============================================================================
// 1. PRIMARY & SECONDARY NAVIGATION BARS (ULTRA SLIM DENSITY)
// =============================================================================

@Composable
fun BodyPrimaryNavBar(
    selectedTab: BodyPrimaryTab,
    onTabSelected: (BodyPrimaryTab) -> Unit,
    values: Map<BodyPrimaryTab, String> = emptyMap(),
    modifier: Modifier = Modifier
) {
    val icons = mapOf(
        BodyPrimaryTab.OVERVIEW to Icons.Filled.Assessment,
        BodyPrimaryTab.COMPOSITION to Icons.Filled.Scale,
        BodyPrimaryTab.MEASUREMENTS to Icons.Filled.Straighten,
        BodyPrimaryTab.PROGRESS to Icons.Filled.ShowChart,
        BodyPrimaryTab.GALLERY to Icons.Filled.CameraAlt,
        BodyPrimaryTab.GOALS to Icons.Filled.Flag,
        BodyPrimaryTab.HEALTH to Icons.Filled.Favorite,
        BodyPrimaryTab.MORE to Icons.Filled.MoreHoriz
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        BodyPrimaryTab.entries.forEach { tab ->
            BodyNavPill(
                tab = tab,
                icon = icons.getValue(tab),
                value = values[tab],
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

/** Reference-style stadium pill: violet hairline when idle, violet gradient when active, live value trailing. */
@Composable
private fun BodyNavPill(
    tab: BodyPrimaryTab,
    icon: ImageVector,
    value: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeTheme = com.example.badnewgym.feature.membercard.design.BadGymThemeManager.activeTheme
    val accent = activeTheme.primaryColor
    val accentDeep = activeTheme.secondaryColor
    val onAccent = if ((accent.red + accent.green + accent.blue) / 3f > 0.62f) Color(0xFF0F172A) else Color.White
    val shape = RoundedCornerShape(50)

    Surface(
        onClick = onClick,
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, if (isSelected) accentDeep else accent.copy(alpha = 0.22f)),
        shadowElevation = if (isSelected) 1.5.dp else 0.dp,
        modifier = Modifier
            .height(34.dp)
            .clip(shape)
            .background(
                if (isSelected) Brush.horizontalGradient(listOf(accent, accentDeep))
                else Brush.horizontalGradient(listOf(Color.White, Color.White))
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = tab.label,
                tint = if (isSelected) onAccent else accentDeep,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = tab.label,
                fontSize = 10.5.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                color = if (isSelected) onAccent else Color(0xFF334155),
                maxLines = 1
            )
            if (!value.isNullOrBlank()) {
                Text(
                    text = value,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) onAccent else accentDeep,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun BodySecondaryNavBar(
    state: BodyIntelligenceState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 2.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Active Sub-Module Tag (Image 2 style: Body • [Tab])
        val activeTheme = com.example.badnewgym.feature.membercard.design.BadGymThemeManager.activeTheme
        val tagBg = activeTheme.getActiveTabBrush()
        val tagBorder = activeTheme.getActiveTabBorder()
        val tagTextColor = activeTheme.getActiveTabTextColor()
        Surface(
            shape = RoundedCornerShape(50.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, tagBorder),
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(tagBg)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(tagTextColor)
                )
                Text(
                    text = "Body • ${state.primaryTab.label}",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = tagTextColor
                )
            }
        }

        when (state.primaryTab) {
            BodyPrimaryTab.OVERVIEW -> {
                OverviewSubTab.entries.forEach { sub ->
                    SecondaryPill(
                        label = sub.label,
                        isSelected = state.overviewSubTab == sub,
                        onClick = { state.overviewSubTab = sub }
                    )
                }
            }
            BodyPrimaryTab.COMPOSITION -> {
                CompositionSubTab.entries.forEach { sub ->
                    SecondaryPill(
                        label = sub.label,
                        isSelected = state.compositionSubTab == sub,
                        onClick = { state.compositionSubTab = sub }
                    )
                }
            }
            BodyPrimaryTab.MEASUREMENTS -> {
                MeasurementsSubTab.entries.forEach { sub ->
                    SecondaryPill(
                        label = sub.label,
                        isSelected = state.measurementsSubTab == sub,
                        onClick = { state.measurementsSubTab = sub }
                    )
                }
            }
            BodyPrimaryTab.PROGRESS -> {
                ProgressSubTab.entries.forEach { sub ->
                    SecondaryPill(
                        label = sub.label,
                        isSelected = state.progressSubTab == sub,
                        onClick = { state.progressSubTab = sub }
                    )
                }
            }
            BodyPrimaryTab.GALLERY -> {
                GallerySubTab.entries.forEach { sub ->
                    SecondaryPill(
                        label = sub.label,
                        isSelected = state.gallerySubTab == sub,
                        onClick = { state.gallerySubTab = sub }
                    )
                }
            }
            BodyPrimaryTab.GOALS -> {
                GoalsSubTab.entries.forEach { sub ->
                    SecondaryPill(
                        label = sub.label,
                        isSelected = state.goalsSubTab == sub,
                        onClick = { state.goalsSubTab = sub }
                    )
                }
            }
            BodyPrimaryTab.HEALTH -> {
                HealthSubTab.entries.forEach { sub ->
                    SecondaryPill(
                        label = sub.label,
                        isSelected = state.healthSubTab == sub,
                        onClick = { state.healthSubTab = sub }
                    )
                }
            }
            BodyPrimaryTab.MORE -> {
                MoreSubTab.entries.forEach { sub ->
                    SecondaryPill(
                        label = sub.label,
                        isSelected = state.moreSubTab == sub,
                        onClick = { state.moreSubTab = sub }
                    )
                }
            }
        }
    }
}

@Composable
private fun SecondaryPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeTheme = com.example.badnewgym.feature.membercard.design.BadGymThemeManager.activeTheme
    val accent = activeTheme.primaryColor
    val accentDeep = activeTheme.secondaryColor
    val onAccent = if ((accent.red + accent.green + accent.blue) / 3f > 0.62f) Color(0xFF0F172A) else Color.White
    val fill = if (isSelected) Brush.verticalGradient(listOf(accent, accentDeep)) else Brush.verticalGradient(listOf(Color.White, accent.copy(alpha = 0.07f)))
    val borderColor = if (isSelected) accentDeep else accent.copy(alpha = 0.22f)
    val textColor = if (isSelected) onAccent else Color(0xFF475569)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(fill)
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
        )
    }
}

// =============================================================================
// MODULE 1: OVERVIEW (EXACT FIXED VIEWPORT DENSITY)
// =============================================================================

@Composable
fun BodyOverviewModule(state: BodyIntelligenceState) {
    val readings = state.view.readings
    val weight = readings.firstOrNull { it.metric == BodyMetric.WEIGHT }
    val muscle = readings.firstOrNull { it.metric == BodyMetric.MUSCLE_MASS }
    val fat = readings.firstOrNull { it.metric == BodyMetric.BODY_FAT }
    val bmi = readings.firstOrNull { it.metric == BodyMetric.BMI }
    val current = weight?.value ?: 0.0
    val start = weight?.history?.firstOrNull()?.second ?: current
    val target = weight?.goalLabel.orEmpty().bodyParseDouble()
    val percent = if (target > start) (((current - start) / (target - start)) * 100).roundToInt().coerceIn(0, 100) else 0
    val goalLabel = state.input.profile?.goalLabel?.takeIf { it.isNotBlank() }
        ?: if (target >= current) "Muscle Gain" else "Fat Loss"
    val startLabel = weight?.history?.firstOrNull()?.first ?: "start"
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // CARD 1: TRANSFORMATION GOAL (real goal, computed progress, recorded composition)
        BodyMasterCard {
            CardHeaderStrip(
                title = "Transformation Goal",
                icon = Icons.Filled.Flag,
                trailingText = "● $goalLabel • ${bodyFmt(target)} kg"
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Hero section: Left athlete image + Right 56% gauge and milestones
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Left: Muscular Physique Athlete Flex Illustration
                Box(
                    modifier = Modifier
                        .size(width = 72.dp, height = 90.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFF8FAFC), Color(0xFFF1F5F9))
                            )
                        )
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.illustration_character_flex),
                        contentDescription = "Physique Athlete",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                    )
                }

                // Right: 56% Gauge + 3 Milestones
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 56% Donut Gauge with vibrant Cyan-Emerald-Yellow-Gold arc
                    Box(
                        modifier = Modifier.size(54.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = Color(0xFFE2E8F0),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                            )
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(
                                        BodyIntelPalette.Emerald,
                                        BodyIntelPalette.EmeraldDeep,
                                        BodyIntelPalette.Emerald
                                    )
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f * percent / 100f,
                                useCenter = false,
                                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Text(
                            text = "$percent%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = BodyIntelPalette.Ink
                        )
                    }

                    // 3 Milestones: Start, Current, Target
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GoalMilestone(bodyFmt(start), "Start", BodyIntelPalette.Ink)
                        GoalMilestone(bodyFmt(current), "Now", BodyIntelPalette.EmeraldDeep)
                        GoalMilestone(bodyFmt(target), "Goal", BodyIntelPalette.Violet)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 3 Metric Pods: BMI, Body Fat, Muscle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                UniformMetricPod(
                    modifier = Modifier.weight(1f),
                    label = "BMI",
                    value = bmi?.value?.let { bodyFmt(it) } ?: "--",
                    badge = bodyBmiStatus(bmi?.value),
                    badgeColor = Color(0xFF16A34A)
                )
                UniformMetricPod(
                    modifier = Modifier.weight(1f),
                    label = "Body Fat",
                    value = fat?.value?.let { bodyFmt(it) } ?: "--",
                    unit = "%",
                    badge = bodyFatStatus(fat?.value),
                    badgeColor = Color(0xFFD97706)
                )
                UniformMetricPod(
                    modifier = Modifier.weight(1f),
                    label = "Muscle",
                    value = muscle?.value?.let { bodyFmt(it) } ?: "--",
                    unit = "kg",
                    badge = "Good",
                    badgeColor = Color(0xFF16A34A)
                )
            }
        }

        // CARD 2: QUICK INSIGHTS (real change since the first recorded weigh-in)
        BodyMasterCard {
            CardHeaderStrip(
                title = "Quick Insights",
                icon = Icons.Filled.Info,
                trailingText = "Since $startLabel"
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                UniformMetricPod(
                    modifier = Modifier.weight(1f),
                    label = "Weight",
                    value = bodySigned(current - start),
                    unit = "kg",
                    badge = "Change",
                    badgeColor = Color(0xFF16A34A)
                )
                UniformMetricPod(
                    modifier = Modifier.weight(1f),
                    label = "Muscle",
                    value = bodySigned(bodyDelta(muscle)),
                    unit = "kg",
                    badge = "Change",
                    badgeColor = Color(0xFF2563EB)
                )
                UniformMetricPod(
                    modifier = Modifier.weight(1f),
                    label = "Body Fat",
                    value = bodySigned(bodyDelta(fat)),
                    unit = "%",
                    badge = "Change",
                    badgeColor = Color(0xFF7E22CE)
                )
            }
        }
    }
}

/** One labelled start/now/goal readout inside the transformation card. */
@Composable
private fun GoalMilestone(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = color)
        Text(text = label, fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
    }
}

private fun bodyFmt(v: Double): String = String.format(java.util.Locale.US, "%.1f", v)
private fun bodySigned(v: Double): String = String.format(java.util.Locale.US, "%+.1f", v)
private fun String.bodyParseDouble(): Double =
    filter { it.isDigit() || it == '.' || it == '-' }.toDoubleOrNull() ?: 0.0
private fun bodyDelta(r: CompositionReading?): Double {
    val h = r?.history ?: return 0.0
    return if (h.size >= 2) h.last().second - h.first().second else 0.0
}
private fun bodyBmiStatus(v: Double?): String =
    if (v == null || v <= 0.0) "--" else if (v < 18.5) "Lean" else if (v <= 24.9) "Normal" else "High"
private fun bodyFatStatus(v: Double?): String =
    if (v == null || v <= 0.0) "--" else if (v <= 15.0) "Lean" else if (v <= 20.0) "Athletic" else "Moderate"
private fun bodyPctOf(part: Double, whole: Double): String =
    if (whole > 0.0) String.format(java.util.Locale.US, "%.1f%%", part / whole * 100.0) else "--"

// =============================================================================
// MODULE 2: COMPOSITION
// =============================================================================

@Composable
fun BodyCompositionModule(state: BodyIntelligenceState) {
    val rec = state.compositionRecord

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (state.compositionSubTab) {
            CompositionSubTab.OVERVIEW -> {
                val readings = state.view.readings
                val wKg = readings.firstOrNull { it.metric == BodyMetric.WEIGHT }?.value ?: 0.0
                val fatPct = readings.firstOrNull { it.metric == BodyMetric.BODY_FAT }?.value ?: 0.0
                val muscleKg = readings.firstOrNull { it.metric == BodyMetric.MUSCLE_MASS }?.value ?: 0.0
                val fatMass = if (wKg > 0.0 && fatPct > 0.0) wKg * fatPct / 100.0 else 0.0
                val leanMass = if (wKg > 0.0) wKg - fatMass else 0.0
                val leanPct = if (wKg > 0.0) (leanMass / wKg * 100.0).roundToInt() else 0
                val fatSlicePct = 100 - leanPct

                // CARD 1: COMPOSITION BREAKDOWN (lean vs fat computed from recorded weight + body-fat)
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Body Composition (${bodyFmt(wKg)} kg)",
                        icon = Icons.Filled.Scale,
                        trailingBadge = "Truth Verified",
                        trailingBadgeColor = BodyIntelPalette.EmeraldDeep
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DonutChart(
                            segments = listOf(
                                DonutSegment("Lean", leanPct, BodyIntelPalette.Emerald),
                                DonutSegment("Fat", fatSlicePct, Color(0xFFF59E0B))
                            ),
                            centerTop = bodyFmt(wKg),
                            centerBottom = "kg Total",
                            size = 70.dp,
                            stroke = 9.dp
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            CompositionLegendRow(color = BodyIntelPalette.Emerald, label = "Muscle", value = "${bodyFmt(muscleKg)} kg", pct = bodyPctOf(muscleKg, wKg))
                            CompositionLegendRow(color = Color(0xFF10B981), label = "Lean Mass", value = "${bodyFmt(leanMass)} kg", pct = bodyPctOf(leanMass, wKg))
                            CompositionLegendRow(color = Color(0xFFF59E0B), label = "Fat Mass", value = "${bodyFmt(fatMass)} kg", pct = bodyPctOf(fatMass, wKg))
                        }
                    }
                }

                // CARD 2: recorded adipose markers only (no fabricated water/bone/BMR/visceral)
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Adipose & Metabolic Markers",
                        icon = Icons.Filled.LocalFireDepartment,
                        trailingText = "Recorded"
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Body Fat",
                            value = bodyFmt(fatPct),
                            unit = "%",
                            badge = bodyFatStatus(fatPct),
                            badgeColor = BodyIntelPalette.Primary
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Fat Mass",
                            value = bodyFmt(fatMass),
                            unit = "kg",
                            badge = "Adipose",
                            badgeColor = Color(0xFFD97706)
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Lean Mass",
                            value = bodyFmt(leanMass),
                            unit = "kg",
                            badge = "Active",
                            badgeColor = BodyIntelPalette.DeepEmerald
                        )
                    }
                }
            }

            CompositionSubTab.FAT_MASS -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Fat Mass Analysis",
                        icon = Icons.Filled.Opacity,
                        trailingBadge = "${rec.bodyFatPercent}% Fat",
                        trailingBadgeColor = Color(0xFFD97706)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Total Fat",
                            value = "${rec.fatMassKg}",
                            unit = "kg",
                            badge = "18.5%",
                            badgeColor = Color(0xFFD97706)
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Subcutaneous",
                            value = "10.8",
                            unit = "kg",
                            badge = "Surface",
                            badgeColor = BodyIntelPalette.Primary
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Visceral",
                            value = "2.1",
                            unit = "kg",
                            badge = "Grade 4",
                            badgeColor = BodyIntelPalette.DeepEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BodyIntelPalette.SoftMint.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, BodyIntelPalette.Primary.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Info, contentDescription = null, tint = BodyIntelPalette.Primary, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Target fat loss: -4.5 kg to reach 15.0% athletic body fat goal.",
                                fontSize = 9.sp,
                                color = BodyIntelPalette.DeepEmerald,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            CompositionSubTab.LEAN_MASS -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Fat-Free Lean Mass",
                        icon = Icons.Filled.FitnessCenter,
                        trailingBadge = "81.4% Lean",
                        trailingBadgeColor = BodyIntelPalette.DeepEmerald
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Skeletal Muscle",
                            value = "${rec.skeletalMuscleKg}",
                            unit = "kg",
                            badge = "Prime",
                            badgeColor = BodyIntelPalette.Primary
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Total Water",
                            value = "${rec.bodyWaterKg}",
                            unit = "kg",
                            badge = "60.1%",
                            badgeColor = Color(0xFF0284C7)
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Bone Mineral",
                            value = "${rec.boneMineralKg}",
                            unit = "kg",
                            badge = "Dense",
                            badgeColor = Color(0xFF8B5CF6)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Daily Basal Burn",
                            value = "${rec.bmrKcal}",
                            unit = "kcal",
                            badge = "Efficient",
                            badgeColor = BodyIntelPalette.DeepEmerald
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Total Fat-Free",
                            value = "${rec.fatFreeMassKg}",
                            unit = "kg",
                            badge = "56.6 kg",
                            badgeColor = BodyIntelPalette.Primary
                        )
                    }
                }
            }

            CompositionSubTab.SEGMENTAL -> {
                val seg = state.segmentalComposition
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Segmental Lean Distribution",
                        icon = Icons.Filled.Person,
                        trailingBadge = "InBody 570",
                        trailingBadgeColor = BodyIntelPalette.Primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Row 1: Left Arm, Trunk, Right Arm (symmetrical 3 columns, exact same width & height)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Left Arm",
                            value = "${seg.leftArmKg}",
                            unit = "kg",
                            badge = "${seg.leftArmPct}%",
                            badgeColor = BodyIntelPalette.Primary
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Trunk Core",
                            value = "${seg.trunkKg}",
                            unit = "kg",
                            badge = "${seg.trunkPct}%",
                            badgeColor = BodyIntelPalette.DeepEmerald
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Right Arm",
                            value = "${seg.rightArmKg}",
                            unit = "kg",
                            badge = "${seg.rightArmPct}%",
                            badgeColor = BodyIntelPalette.Primary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Row 2: Left Leg, Right Leg (symmetrical 2 columns, exact same width & height)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Left Leg",
                            value = "${seg.leftLegKg}",
                            unit = "kg",
                            badge = "${seg.leftLegPct}%",
                            badgeColor = BodyIntelPalette.Primary
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Right Leg",
                            value = "${seg.rightLegKg}",
                            unit = "kg",
                            badge = "${seg.rightLegPct}%",
                            badgeColor = BodyIntelPalette.Primary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BodyIntelPalette.SoftMint.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Bilateral Balance", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald)
                            Text(text = "98.5% Symmetrical • Balanced", fontSize = 9.sp, color = BodyIntelPalette.Primary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompositionLegendRow(color: Color, label: String, value: String, pct: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(color))
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = label, fontSize = 10.sp, color = BodyIntelPalette.Text, fontWeight = FontWeight.Medium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = "($pct)", fontSize = 8.5.sp, color = BodyIntelPalette.Secondary)
        }
    }
}

// =============================================================================
// MODULE 3: MEASUREMENTS (14 Circumferences + Ratios)
// =============================================================================

@Composable
fun BodyMeasurementsModule(state: BodyIntelligenceState) {
    val isCm = state.measurementUnit == "cm"

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (state.measurementsSubTab == MeasurementsSubTab.RATIOS) {
            // DEDICATED RATIOS & SYMMETRY VIEW
            BodyMasterCard {
                CardHeaderStrip(
                    title = "Health & Aesthetic Proportions",
                    icon = Icons.Filled.Straighten,
                    trailingBadge = "Golden Ratio",
                    trailingBadgeColor = BodyIntelPalette.DeepEmerald
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    UniformMetricPod(
                        modifier = Modifier.weight(1f),
                        label = "Waist / Height",
                        value = "${state.bodyRatios.waistToHeight}",
                        badge = state.bodyRatios.waistToHeightLabel,
                        badgeColor = BodyIntelPalette.Primary
                    )
                    UniformMetricPod(
                        modifier = Modifier.weight(1f),
                        label = "Waist / Hip",
                        value = "${state.bodyRatios.waistToHip}",
                        badge = state.bodyRatios.waistToHipLabel,
                        badgeColor = BodyIntelPalette.Primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    UniformMetricPod(
                        modifier = Modifier.weight(1f),
                        label = "Shoulder / Waist",
                        value = "1.48",
                        badge = "Athletic V-Taper",
                        badgeColor = BodyIntelPalette.DeepEmerald
                    )
                    UniformMetricPod(
                        modifier = Modifier.weight(1f),
                        label = "Chest / Waist",
                        value = "1.28",
                        badge = "Tapered",
                        badgeColor = BodyIntelPalette.Primary
                    )
                }
            }

            BodyMasterCard {
                CardHeaderStrip(
                    title = "Bilateral Limb Symmetry",
                    icon = Icons.Filled.Person,
                    trailingText = "Bilateral Balance"
                )

                Spacer(modifier = Modifier.height(4.dp))

                SymmetryRow("Biceps (L / R)", if (isCm) "36.1 / 36.3 cm" else "14.2\" / 14.3\"", "99.3% Balanced")
                SymmetryRow("Thighs (L / R)", if (isCm) "55.4 / 55.9 cm" else "21.8\" / 22.0\"", "99.1% Balanced")
                SymmetryRow("Calves (L / R)", if (isCm) "38.1 / 38.4 cm" else "15.0\" / 15.1\"", "99.3% Balanced")
            }
        } else {
            // CIRCUMFERENCE TABLE CARD
            BodyMasterCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Straighten,
                            contentDescription = null,
                            tint = BodyIntelPalette.Primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "14 Circumference Records",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.Text
                        )
                    }

                    // Compact unit toggle pill
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = BodyIntelPalette.SoftMint,
                        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
                    ) {
                        Row(modifier = Modifier.padding(1.dp)) {
                            Surface(
                                onClick = { state.measurementUnit = "in" },
                                shape = RoundedCornerShape(5.dp),
                                color = if (!isCm) BodyIntelPalette.Primary else Color.Transparent
                            ) {
                                Text(
                                    text = "in",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isCm) Color.White else BodyIntelPalette.Secondary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                )
                            }
                            Surface(
                                onClick = { state.measurementUnit = "cm" },
                                shape = RoundedCornerShape(5.dp),
                                color = if (isCm) BodyIntelPalette.Primary else Color.Transparent
                            ) {
                                Text(
                                    text = "cm",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCm) Color.White else BodyIntelPalette.Secondary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Table Header
                Surface(
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    color = BodyIntelPalette.SoftMint
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Measurement", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald)
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            Text(text = "Current", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald)
                            Text(text = "Change", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald)
                        }
                    }
                }

                val filtered = when (state.measurementsSubTab) {
                    MeasurementsSubTab.ALL -> state.circumferences
                    MeasurementsSubTab.UPPER -> state.circumferences.filter { it.category == MeasurementCategory.UPPER }
                    MeasurementsSubTab.CORE -> state.circumferences.filter { it.category == MeasurementCategory.CORE }
                    MeasurementsSubTab.LOWER -> state.circumferences.filter { it.category == MeasurementCategory.LOWER }
                    MeasurementsSubTab.RATIOS -> state.circumferences
                }

                filtered.forEachIndexed { i, m ->
                    val curVal = if (isCm) String.format(java.util.Locale.US, "%.1f cm", m.currentCm()) else "${m.currentInches} in"
                    val changeVal = if (isCm) {
                        (if (m.changeCm() >= 0) "+" else "") + String.format(java.util.Locale.US, "%.1f cm", m.changeCm())
                    } else {
                        (if (m.changeInches >= 0) "+${m.changeInches}" else "${m.changeInches}") + " in"
                    }
                    val changeColor = if (m.category == MeasurementCategory.CORE && m.changeInches < 0) Color(0xFF10B981)
                                      else if (m.category != MeasurementCategory.CORE && m.changeInches > 0) Color(0xFF10B981)
                                      else Color(0xFFEF4444)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 1.8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = m.name, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = BodyIntelPalette.Text)
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(text = curVal, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                            Text(text = changeVal, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = changeColor)
                        }
                    }
                    if (i < filtered.lastIndex) {
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F3)))
                    }
                }
            }

            if (state.measurementsSubTab == MeasurementsSubTab.ALL) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    UniformMetricPod(
                        modifier = Modifier.weight(1f),
                        label = "Waist / Height",
                        value = "${state.bodyRatios.waistToHeight}",
                        badge = state.bodyRatios.waistToHeightLabel,
                        badgeColor = BodyIntelPalette.Primary
                    )
                    UniformMetricPod(
                        modifier = Modifier.weight(1f),
                        label = "Waist / Hip",
                        value = "${state.bodyRatios.waistToHip}",
                        badge = state.bodyRatios.waistToHipLabel,
                        badgeColor = BodyIntelPalette.Primary
                    )
                }
            }
        }
    }
}

@Composable
private fun SymmetryRow(label: String, measurements: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 9.5.sp, color = BodyIntelPalette.Text, fontWeight = FontWeight.Medium)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = measurements, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
            Text(text = status, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = BodyIntelPalette.DeepEmerald)
        }
    }
}

// =============================================================================
// MODULE 4: PROGRESS (Trend Curves + Timeline)
// =============================================================================

private data class ProgressTrendConfig(
    val titleText: String,
    val pointsList: List<Float>,
    val unitStr: String,
    val minVal: Float,
    val maxVal: Float,
    val curText: String,
    val prevText: String,
    val chgText: String,
    val pctText: String
)

@Composable
fun BodyProgressModule(state: BodyIntelligenceState) {
    val readings = state.view.readings
    fun cfgFor(title: String, metric: BodyMetric, unit: String): ProgressTrendConfig {
        val r = readings.firstOrNull { it.metric == metric }
        var pts = r?.history?.map { it.second.toFloat() }.orEmpty()
        if (pts.size < 2) {
            val v = r?.value?.toFloat() ?: 0f
            pts = listOf(v, v)
        }
        val cur = pts.last()
        val prev = pts[pts.size - 2]
        val chg = cur - prev
        val pct = if (prev != 0f) chg / prev * 100f else 0f
        val lo = pts.min() - 1f
        val hi = pts.max() + 1f
        val arrow = if (chg <= 0f) "▼" else "▲"
        val curText = if (unit == "%") "${bodyFmt(cur.toDouble())}%" else "${bodyFmt(cur.toDouble())} $unit"
        val prevText = if (unit == "%") "${bodyFmt(prev.toDouble())}%" else "${bodyFmt(prev.toDouble())} $unit"
        val chgText = if (unit == "%") "${bodySigned(chg.toDouble())}%" else "${bodySigned(chg.toDouble())} $unit"
        val pctText = "$arrow ${String.format(java.util.Locale.US, "%.1f", kotlin.math.abs(pct.toDouble()))}%"
        return ProgressTrendConfig(
            titleText = title,
            pointsList = pts,
            unitStr = unit,
            minVal = lo,
            maxVal = hi,
            curText = curText,
            prevText = prevText,
            chgText = chgText,
            pctText = pctText
        )
    }
    val config = when (state.progressSubTab) {
        ProgressSubTab.WEIGHT -> cfgFor("Weight Trend (kg)", BodyMetric.WEIGHT, "kg")
        ProgressSubTab.BODY_FAT -> cfgFor("Body Fat Trend (%)", BodyMetric.BODY_FAT, "%")
        ProgressSubTab.MUSCLE -> cfgFor("Muscle Mass Trend (kg)", BodyMetric.MUSCLE_MASS, "kg")
        ProgressSubTab.SIZE -> cfgFor("Waist Circumference (cm)", BodyMetric.WAIST, "cm")
    }

    val pointsList = config.pointsList
    val minVal = config.minVal
    val maxVal = config.maxVal
    val titleText = config.titleText
    val curText = config.curText
    val prevText = config.prevText
    val chgText = config.chgText
    val pctText = config.pctText

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Time Range Filter Pills (Compact 22dp height)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("1M", "3M", "6M", "1Y", "All").forEach { range ->
                val isSelected = state.progressTimeRange == range
                Surface(
                    onClick = { state.progressTimeRange = range },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) BodyIntelPalette.Primary else BodyIntelPalette.Surface,
                    border = BorderStroke(1.dp, if (isSelected) BodyIntelPalette.DeepEmerald else BodyIntelPalette.CardBorder)
                ) {
                    Text(
                        text = range,
                        fontSize = 9.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else BodyIntelPalette.Secondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        // Trend Canvas Card
        BodyMasterCard {
            CardHeaderStrip(
                title = titleText,
                icon = Icons.Filled.Timeline,
                trailingBadge = curText,
                trailingBadgeColor = BodyIntelPalette.Primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // High Contrast Compact Curve Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (pointsList.size < 2) return@Canvas

                    val range = (maxVal - minVal).coerceAtLeast(1f)
                    val w = size.width
                    val h = size.height

                    // Grid lines
                    for (i in 0..2) {
                        val y = h * (i / 2f)
                        drawLine(
                            color = Color(0xFFF1F5F3),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                    }

                    // Curve Path
                    val path = Path()
                    val stepX = w / (pointsList.size - 1)

                    pointsList.forEachIndexed { i, pVal ->
                        val x = i * stepX
                        val y = h - ((pVal - minVal) / range) * h
                        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }

                    // Underfill
                    val fillPath = Path()
                    fillPath.addPath(path)
                    fillPath.lineTo(w, h)
                    fillPath.lineTo(0f, h)
                    fillPath.close()

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            listOf(BodyIntelPalette.Primary.copy(alpha = 0.25f), Color.Transparent)
                        )
                    )

                    // Line
                    drawPath(
                        path = path,
                        color = BodyIntelPalette.Primary,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Points
                    pointsList.forEachIndexed { i, pVal ->
                        val x = i * stepX
                        val y = h - ((pVal - minVal) / range) * h
                        drawCircle(color = Color.White, radius = 4.dp.toPx(), center = Offset(x, y))
                        drawCircle(color = BodyIntelPalette.Primary, radius = 2.5.dp.toPx(), center = Offset(x, y))
                    }
                }
            }

            // X-Axis Labels (real months from the trended metric's own history)
            val axisMetric = when (state.progressSubTab) {
                ProgressSubTab.WEIGHT -> BodyMetric.WEIGHT
                ProgressSubTab.BODY_FAT -> BodyMetric.BODY_FAT
                ProgressSubTab.MUSCLE -> BodyMetric.MUSCLE_MASS
                ProgressSubTab.SIZE -> BodyMetric.WAIST
            }
            val axisLabels = readings.firstOrNull { it.metric == axisMetric }
                ?.history?.map { it.first }
                ?.takeIf { it.isNotEmpty() }
                ?: pointsList.map { "" }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                axisLabels.forEach { label ->
                    Text(text = label, fontSize = 8.sp, color = BodyIntelPalette.Secondary)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 4 Symmetrical Stat Pods (Exact same width & height)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                UniformMetricPod(modifier = Modifier.weight(1f), label = "Current", value = curText)
                UniformMetricPod(modifier = Modifier.weight(1f), label = "Previous", value = prevText)
                UniformMetricPod(modifier = Modifier.weight(1f), label = "Change", value = chgText)
                UniformMetricPod(modifier = Modifier.weight(1f), label = "Progress", value = pctText, badgeColor = BodyIntelPalette.Primary)
            }
        }

        // Assessment Milestones (real weigh-in checkpoints from the recorded history)
        val wHist = readings.firstOrNull { it.metric == BodyMetric.WEIGHT }?.history ?: emptyList()
        if (wHist.size >= 2) {
            BodyMasterCard {
                CardHeaderStrip(
                    title = "Assessment Milestones",
                    icon = Icons.Filled.Flag,
                    trailingText = "Weigh-in log"
                )

                Spacer(modifier = Modifier.height(4.dp))

                val ms = listOf(
                    MilestoneItem("Initial weigh-in", wHist.first().first, true, "${bodyFmt(wHist.first().second)} kg • starting point"),
                    MilestoneItem("Latest weigh-in", wHist.last().first, true, "${bodyFmt(wHist.last().second)} kg • ${bodySigned(wHist.last().second - wHist.first().second)} kg total")
                )
                ms.forEach { m ->
                    MilestoneItemCard(m)
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }
        }
    }
}

@Composable
private fun MilestoneItemCard(item: MilestoneItem) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(if (item.completed) BodyIntelPalette.Primary else Color(0xFFE2EBE5)),
                contentAlignment = Alignment.Center
            ) {
                if (item.completed) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                }
            }
            Spacer(modifier = Modifier.width(7.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                if (item.note != null) {
                    Text(text = item.note, fontSize = 8.sp, color = BodyIntelPalette.Secondary)
                }
            }
            Text(text = item.date, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = BodyIntelPalette.DeepEmerald)
        }
    }
}

// =============================================================================
// MODULE 5: GALLERY (Before / After Comparison Slider)
// =============================================================================

@Composable
fun BodyGalleryModule(state: BodyIntelligenceState) {
    var sliderX by remember { mutableFloatStateOf(0.5f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (state.gallerySubTab) {
            GallerySubTab.COMPARE -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Visual Transformation",
                        icon = Icons.Filled.Compare,
                        actionLabel = "Add Photo",
                        onActionClick = { /* Add Photo */ }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F3))
                            .pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    change.consume()
                                    val newRatio = (change.position.x / size.width.toFloat()).coerceIn(0.05f, 0.95f)
                                    sliderX = newRatio
                                }
                            }
                    ) {
                        val w = maxWidth
                        val h = maxHeight
                        val dividerOffset = w * sliderX

                        // "BEFORE" Panel (Left slice with clip)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(dividerOffset)
                                .align(Alignment.CenterStart)
                                .clipToBounds()
                                .background(Brush.verticalGradient(listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "BEFORE", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF475569))
                                Text(text = "01 Apr 2025 • 74.5 kg", fontSize = 7.5.sp, color = Color(0xFF64748B))
                            }
                        }

                        // "AFTER" Panel (Right slice with clip)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(w * (1f - sliderX))
                                .align(Alignment.CenterEnd)
                                .clipToBounds()
                                .background(Brush.verticalGradient(listOf(Color(0xFFD1FAE5), Color(0xFFA7F3D0)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.FitnessCenter, contentDescription = null, tint = BodyIntelPalette.Primary, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "AFTER", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = BodyIntelPalette.DeepEmerald)
                                Text(text = "30 Jun 2025 • 69.5 kg", fontSize = 7.5.sp, color = BodyIntelPalette.Primary)
                            }
                        }

                        // Draggable Vertical Divider Line
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(2.dp)
                                .offset(x = dividerOffset)
                                .background(Color.White)
                        )

                        // Slider Handle Circle
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .offset(x = dividerOffset - 11.dp, y = h / 2 - 11.dp)
                                .clip(CircleShape)
                                .background(BodyIntelPalette.Primary)
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Compare, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "BEFORE: 74.5 kg", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Secondary)
                        Text(text = "AFTER: 69.5 kg (-5.0 kg)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Primary)
                    }
                }
            }
            GallerySubTab.FRONT, GallerySubTab.SIDE, GallerySubTab.BACK -> {
                val poseName = state.gallerySubTab.label
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "$poseName Profile Photo",
                        icon = Icons.Filled.CameraAlt,
                        actionLabel = "Add Photo",
                        onActionClick = { /* Add Photo */ }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Brush.verticalGradient(listOf(Color(0xFFF0FDF4), Color(0xFFDCFCE7)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = BodyIntelPalette.Primary, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "$poseName Standard Profile", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald)
                            Text(text = "Latest: 30 Jun 2025 • 69.5 kg", fontSize = 8.5.sp, color = BodyIntelPalette.Secondary)
                        }
                    }
                }
            }
            GallerySubTab.TIMELINE -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Monthly Photo Timeline",
                        icon = Icons.Filled.History,
                        actionLabel = "Add Photo",
                        onActionClick = { /* Add Photo */ }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(
                            Triple("Apr 2025", "74.5 kg", "Start"),
                            Triple("May 2025", "71.2 kg", "-3.3 kg"),
                            Triple("Jun 2025", "69.5 kg", "Current")
                        ).forEach { (month, weight, tag) ->
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = BodyIntelPalette.SoftMint.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, BodyIntelPalette.Primary.copy(alpha = 0.2f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = BodyIntelPalette.Primary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = month, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                                    Text(text = weight, fontSize = 8.sp, color = BodyIntelPalette.Secondary)
                                    Surface(shape = RoundedCornerShape(4.dp), color = BodyIntelPalette.Primary) {
                                        Text(text = tag, fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Privacy note (compact 8sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Lock, contentDescription = null, tint = BodyIntelPalette.Secondary, modifier = Modifier.size(10.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "Photos are privately encrypted and only stored on your device.",
                fontSize = 8.sp,
                color = BodyIntelPalette.Secondary
            )
        }
    }
}

// =============================================================================
// MODULE 6: GOALS
// =============================================================================

@Composable
fun BodyGoalsModule(state: BodyIntelligenceState) {
    val goal = state.transformationGoal

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (state.goalsSubTab) {
            GoalsSubTab.ACTIVE -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = goal.title,
                        icon = Icons.Filled.Flag,
                        trailingBadge = goal.status,
                        trailingBadgeColor = BodyIntelPalette.Primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Start",
                            value = "${goal.startKg}",
                            unit = "kg"
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Current",
                            value = "${goal.currentKg}",
                            unit = "kg",
                            badge = "-3.3kg",
                            badgeColor = BodyIntelPalette.Primary
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Target",
                            value = "${goal.targetKg}",
                            unit = "kg",
                            badge = "Goal",
                            badgeColor = BodyIntelPalette.DeepEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = { goal.progressPercent / 100f },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                        color = BodyIntelPalette.Primary,
                        trackColor = BodyIntelPalette.SoftMint
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "3.3 kg lost • 4.5 kg to target", fontSize = 8.5.sp, color = BodyIntelPalette.Secondary)
                        Text(text = "${goal.progressPercent}% achieved", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Primary)
                    }
                }
            }
            GoalsSubTab.MILESTONES -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Progression Milestones",
                        icon = Icons.Filled.Flag,
                        trailingText = "Active Cycle"
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    goal.milestones.forEach { item ->
                        MilestoneItemCard(item)
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                }
            }
            GoalsSubTab.HISTORY -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Archived Goal Cycles",
                        icon = Icons.Filled.History,
                        trailingBadge = "Completed",
                        trailingBadgeColor = BodyIntelPalette.DeepEmerald
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "✓ Phase 1: Reconditioning (75.0 kg → 72.8 kg) • Completed Mar 2025", fontSize = 9.5.sp, color = BodyIntelPalette.DeepEmerald)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(text = "✓ Foundation Phase: Assessment & Onboarding • Completed Feb 2025", fontSize = 9.5.sp, color = BodyIntelPalette.Secondary)
                }
            }
        }
    }
}

// =============================================================================
// MODULE 7: HEALTH & RECOVERY (Vitals, Recovery, Lifestyle)
// =============================================================================

@Composable
fun BodyHealthModule(state: BodyIntelligenceState) {
    val h = state.healthVitals

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (state.healthSubTab) {
            HealthSubTab.VITALS -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Cardiovascular Vitals",
                        icon = Icons.Filled.Favorite,
                        trailingBadge = "Optimal",
                        trailingBadgeColor = BodyIntelPalette.Primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Resting HR",
                            value = "${h.restingHeartRateBpm}",
                            unit = "bpm",
                            badge = "60-100",
                            badgeColor = Color(0xFFEF4444)
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Blood Pressure",
                            value = "${h.bloodPressureSys}/${h.bloodPressureDia}",
                            unit = "mmHg",
                            badge = "Normal",
                            badgeColor = Color(0xFF0284C7)
                        )
                    }
                }
            }
            HealthSubTab.RECOVERY -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Recovery & Sleep Readiness",
                        icon = Icons.Filled.Hotel,
                        trailingBadge = "84% Ready",
                        trailingBadgeColor = BodyIntelPalette.DeepEmerald
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Sleep Duration",
                            value = h.sleepDuration,
                            badge = "88% Rest",
                            badgeColor = Color(0xFF8B5CF6)
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Recovery Status",
                            value = h.recoveryStatus,
                            badge = "Prime",
                            badgeColor = BodyIntelPalette.Primary
                        )
                    }
                }
            }
            HealthSubTab.LIFESTYLE -> {
                BodyMasterCard {
                    CardHeaderStrip(
                        title = "Daily Activity & Hydration",
                        icon = Icons.Filled.DirectionsRun,
                        trailingBadge = "Active Day",
                        trailingBadgeColor = BodyIntelPalette.Primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Daily Steps",
                            value = "${h.dailySteps}",
                            unit = "steps",
                            badge = "10k Goal",
                            badgeColor = Color(0xFF10B981)
                        )
                        UniformMetricPod(
                            modifier = Modifier.weight(1f),
                            label = "Hydration",
                            value = "${h.hydrationLiters}",
                            unit = "L",
                            badge = "Optimal",
                            badgeColor = Color(0xFF0EA5E9)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// MODULE 8: MORE OPTIONS (Assessment History, Reports, Settings)
// =============================================================================

@Composable
fun BodyMoreModule(state: BodyIntelligenceState) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BodyMasterCard {
            CardHeaderStrip(
                title = "Body Intelligence Workspace",
                icon = Icons.Filled.Settings,
                trailingText = "Options"
            )

            Spacer(modifier = Modifier.height(4.dp))

            MoreOptionRow(title = "Assessment History", subtitle = "View past InBody and manual tests", icon = Icons.Filled.History)
            MoreOptionRow(title = "Reports & Analytics", subtitle = "Download or share progress PDF reports", icon = Icons.Filled.FileDownload)
            MoreOptionRow(title = "Smart Scale Sync", subtitle = "Connect & auto-sync with Bluetooth scales", icon = Icons.Filled.Sync)
            MoreOptionRow(title = "Trainer Notes & Feedback", subtitle = "View coach feedback and form reviews", icon = Icons.Filled.Note)
            MoreOptionRow(title = "Import Assessment", subtitle = "Scan InBody / DEXA report sheet", icon = Icons.Filled.Scale)
            MoreOptionRow(title = "Body Settings", subtitle = "Units (kg/lb, cm/in), privacy and reminders", icon = Icons.Filled.Settings)
        }
    }
}

@Composable
private fun MoreOptionRow(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(BodyIntelPalette.SoftMint),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = BodyIntelPalette.Primary, modifier = Modifier.size(13.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
            Text(text = subtitle, fontSize = 8.sp, color = BodyIntelPalette.Secondary)
        }
    }
}
