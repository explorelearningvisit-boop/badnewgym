package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.*
import kotlin.math.roundToInt

// =============================================================================
// MODULAR BODY INTELLIGENCE WORKSPACE
// =============================================================================

/**
 * Modular Body Intelligence Workspace.
 *
 * Implements the 8-module architecture with primary horizontal swipeable navigation,
 * contextual secondary sub-tabs, and dedicated module views.
 */
fun LazyListScope.bodyModularWorkspace(state: BodyIntelligenceState) {
    // 1. PRIMARY HORIZONTAL NAVIGATION
    item(key = "body_primary_nav") {
        BodyPrimaryNavBar(
            selectedTab = state.primaryTab,
            onTabSelected = { state.primaryTab = it }
        )
    }

    // 2. CONTEXTUAL SECONDARY NAVIGATION
    item(key = "body_secondary_nav") {
        BodySecondaryNavBar(state = state)
    }

    // 3. DEDICATED MODULE INTERFACE
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
// 1. PRIMARY NAVIGATION BAR (Horizontal Scrollable 8 Tabs)
// =============================================================================

@Composable
fun BodyPrimaryNavBar(
    selectedTab: BodyPrimaryTab,
    onTabSelected: (BodyPrimaryTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BodyPrimaryTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            val icon = when (tab) {
                BodyPrimaryTab.OVERVIEW -> Icons.Filled.Assessment
                BodyPrimaryTab.COMPOSITION -> Icons.Filled.Scale
                BodyPrimaryTab.MEASUREMENTS -> Icons.Filled.Straighten
                BodyPrimaryTab.PROGRESS -> Icons.Filled.ShowChart
                BodyPrimaryTab.GALLERY -> Icons.Filled.CameraAlt
                BodyPrimaryTab.GOALS -> Icons.Filled.Flag
                BodyPrimaryTab.HEALTH -> Icons.Filled.Favorite
                BodyPrimaryTab.MORE -> Icons.Filled.MoreHoriz
            }

            Surface(
                onClick = { onTabSelected(tab) },
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) BodyIntelPalette.Primary else BodyIntelPalette.Surface,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) BodyIntelPalette.DeepEmerald else BodyIntelPalette.CardBorder
                ),
                shadowElevation = if (isSelected) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = tab.label,
                        tint = if (isSelected) Color.White else BodyIntelPalette.Secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = tab.label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) Color.White else BodyIntelPalette.Text
                    )
                }
            }
        }
    }
}

// =============================================================================
// 2. CONTEXTUAL SECONDARY NAVIGATION BAR
// =============================================================================

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
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) BodyIntelPalette.SoftMint else Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) BodyIntelPalette.Primary else Color(0xFFD6E2DC)
        )
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) BodyIntelPalette.DeepEmerald else BodyIntelPalette.Secondary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// =============================================================================
// MODULE 1: OVERVIEW
// =============================================================================

@Composable
fun BodyOverviewModule(state: BodyIntelligenceState) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Module Title Card
        ModuleHeaderCard(
            title = "Body Intelligence",
            subtitle = "Your Physical Progress & Measurements"
        )

        // 1. Current Weight vs Target Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BodyIntelPalette.Surface,
            border = BorderStroke(1.dp, BodyIntelPalette.CardBorder),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Current Weight",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Secondary
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "69.5",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = BodyIntelPalette.Text
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "kg",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = BodyIntelPalette.Primary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }

                    // Green scale icon badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(BodyIntelPalette.SoftMint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Scale,
                            contentDescription = "Scale",
                            tint = BodyIntelPalette.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Target",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Secondary
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "65.0",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = BodyIntelPalette.Text
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "kg",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BodyIntelPalette.Secondary,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Goal Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥 Fat Loss Goal • 4.5 kg to go",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BodyIntelPalette.Text
                    )
                    Text(
                        text = "56%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BodyIntelPalette.Primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { 0.56f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = BodyIntelPalette.Primary,
                    trackColor = BodyIntelPalette.SoftMint,
                )
            }
        }

        // 2. Three Key Metric Pods (BMI, Body Fat, Muscle Mass)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricOverviewPod(
                modifier = Modifier.weight(1f),
                label = "BMI",
                value = "22.7",
                unit = "",
                badge = "Normal",
                badgeColor = BodyIntelPalette.Primary,
                icon = Icons.Filled.Person
            )
            MetricOverviewPod(
                modifier = Modifier.weight(1f),
                label = "Body Fat",
                value = "18.5",
                unit = "%",
                badge = "Moderate",
                badgeColor = Color(0xFFD97706),
                icon = Icons.Filled.Opacity
            )
            MetricOverviewPod(
                modifier = Modifier.weight(1f),
                label = "Muscle Mass",
                value = "56.6",
                unit = "kg",
                badge = "Good",
                badgeColor = BodyIntelPalette.Primary,
                icon = Icons.Filled.FitnessCenter
            )
        }

        // 3. Quick Navigation Grid (6 modules)
        Text(
            text = "EXPLORE MODULES",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BodyIntelPalette.Secondary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(start = 2.dp, top = 2.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickModuleTile(
                modifier = Modifier.weight(1f),
                title = "Composition",
                icon = Icons.Filled.Scale,
                onClick = { state.primaryTab = BodyPrimaryTab.COMPOSITION }
            )
            QuickModuleTile(
                modifier = Modifier.weight(1f),
                title = "Measurements",
                icon = Icons.Filled.Straighten,
                onClick = { state.primaryTab = BodyPrimaryTab.MEASUREMENTS }
            )
            QuickModuleTile(
                modifier = Modifier.weight(1f),
                title = "Progress",
                icon = Icons.Filled.ShowChart,
                onClick = { state.primaryTab = BodyPrimaryTab.PROGRESS }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickModuleTile(
                modifier = Modifier.weight(1f),
                title = "Gallery",
                icon = Icons.Filled.CameraAlt,
                onClick = { state.primaryTab = BodyPrimaryTab.GALLERY }
            )
            QuickModuleTile(
                modifier = Modifier.weight(1f),
                title = "Health",
                icon = Icons.Filled.Favorite,
                onClick = { state.primaryTab = BodyPrimaryTab.HEALTH }
            )
            QuickModuleTile(
                modifier = Modifier.weight(1f),
                title = "Goals",
                icon = Icons.Filled.Flag,
                onClick = { state.primaryTab = BodyPrimaryTab.GOALS }
            )
        }

        // 4. Primary Call to Action
        Button(
            onClick = { /* Open new assessment form */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BodyIntelPalette.Primary,
                contentColor = Color.White
            )
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "New Assessment", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun MetricOverviewPod(
    label: String,
    value: String,
    unit: String,
    badge: String,
    badgeColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BodyIntelPalette.Secondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = BodyIntelPalette.Text)
                if (unit.isNotEmpty()) {
                    Text(text = unit, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Secondary)
                }
            }
            Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = BodyIntelPalette.Secondary)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = badgeColor.copy(alpha = 0.12f)
            ) {
                Text(
                    text = badge,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickModuleTile(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(14.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = BodyIntelPalette.Primary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = BodyIntelPalette.Text)
        }
    }
}

// =============================================================================
// MODULE 2: COMPOSITION
// =============================================================================

@Composable
fun BodyCompositionModule(state: BodyIntelligenceState) {
    val rec = state.compositionRecord

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModuleHeaderCard(
            title = "Body Composition",
            subtitle = "Detailed Breakdown • Fat, Lean Mass & Segmental"
        )

        // 1. Donut Composition Breakdown Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BodyIntelPalette.Surface,
            border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Multi-color Donut Chart
                DonutChart(
                    segments = listOf(
                        DonutSegment("Muscle", 81, Color(0xFF10B981)),
                        DonutSegment("Fat", 19, Color(0xFFF59E0B)),
                        DonutSegment("Water", 60, Color(0xFF0284C7)),
                        DonutSegment("Bone", 5, Color(0xFF8B5CF6))
                    ),
                    centerTop = "${rec.totalWeightKg}",
                    centerBottom = "Total Weight",
                    size = 110.dp,
                    stroke = 14.dp
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Breakdown Legend
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CompositionLegendRow(color = Color(0xFF10B981), label = "Muscle", value = "${rec.fatFreeMassKg} kg", pct = "81.4%")
                    CompositionLegendRow(color = Color(0xFFF59E0B), label = "Fat", value = "${rec.fatMassKg} kg", pct = "18.5%")
                    CompositionLegendRow(color = Color(0xFF0284C7), label = "Water", value = "${rec.bodyWaterKg} kg", pct = "60.1%")
                    CompositionLegendRow(color = Color(0xFF8B5CF6), label = "Bone", value = "${rec.boneMineralKg} kg", pct = "4.5%")
                }
            }
        }

        // Truth Verification Notice
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF0FDF4),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = BodyIntelPalette.DeepEmerald, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Truth Verified: Total Body Weight (69.5 kg) = Fat Mass (12.9 kg) + Lean Mass (56.6 kg).",
                    fontSize = 9.sp,
                    color = BodyIntelPalette.DeepEmerald,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 2. Metric Grid (6 Pods)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompositionPod(label = "Body Fat", value = "${rec.bodyFatPercent}%", modifier = Modifier.weight(1f))
            CompositionPod(label = "Fat Mass", value = "${rec.fatMassKg} kg", modifier = Modifier.weight(1f))
            CompositionPod(label = "Lean Mass", value = "${rec.fatFreeMassKg} kg", modifier = Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompositionPod(label = "Skeletal Muscle", value = "${rec.skeletalMuscleKg} kg", subtitle = "Subset of Lean", modifier = Modifier.weight(1f))
            CompositionPod(label = "Body Water", value = "${rec.bodyWaterKg} kg", subtitle = "Lean Component", modifier = Modifier.weight(1f))
            CompositionPod(label = "Bone Mineral", value = "${rec.boneMineralKg} kg", subtitle = "Lean Component", modifier = Modifier.weight(1f))
        }

        // 3. Secondary Metrics: Visceral Fat & BMR
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                color = BodyIntelPalette.Surface,
                border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(28.dp).clip(CircleShape).background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${rec.visceralFatGrade}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "Visceral Fat", fontSize = 10.sp, color = BodyIntelPalette.Secondary)
                        Text(text = "Grade ${rec.visceralFatGrade} • Normal", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                    }
                }
            }

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                color = BodyIntelPalette.Surface,
                border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(28.dp).clip(CircleShape).background(BodyIntelPalette.SoftMint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.LocalFireDepartment, contentDescription = null, tint = BodyIntelPalette.Primary, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "BMR (Estimated)", fontSize = 10.sp, color = BodyIntelPalette.Secondary)
                        Text(text = "${rec.bmrKcal} kcal/day", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                    }
                }
            }
        }

        // 4. Segmental Analysis
        val seg = state.segmentalComposition
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BodyIntelPalette.Surface,
            border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Segmental Analysis",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BodyIntelPalette.Text
                    )
                    Text(
                        text = "Device: ${rec.sourceDevice}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = BodyIntelPalette.Secondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SegmentalMetricPod(label = "Left Arm", weight = "${seg.leftArmKg} kg", pct = "${seg.leftArmPct}%")
                    SegmentalMetricPod(label = "Trunk", weight = "${seg.trunkKg} kg", pct = "${seg.trunkPct}%")
                    SegmentalMetricPod(label = "Right Arm", weight = "${seg.rightArmKg} kg", pct = "${seg.rightArmPct}%")
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    SegmentalMetricPod(label = "Left Leg", weight = "${seg.leftLegKg} kg", pct = "${seg.leftLegPct}%")
                    SegmentalMetricPod(label = "Right Leg", weight = "${seg.rightLegKg} kg", pct = "${seg.rightLegPct}%")
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
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, fontSize = 11.sp, color = BodyIntelPalette.Text, fontWeight = FontWeight.Medium)
        }
        Row {
            Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "($pct)", fontSize = 9.5.sp, color = BodyIntelPalette.Secondary)
        }
    }
}

@Composable
private fun CompositionPod(label: String, value: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp)) {
            Text(text = label, fontSize = 9.sp, color = BodyIntelPalette.Secondary, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
            if (subtitle != null) {
                Text(text = subtitle, fontSize = 7.5.sp, color = BodyIntelPalette.Primary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SegmentalMetricPod(label: String, weight: String, pct: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = BodyIntelPalette.SoftMint.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, BodyIntelPalette.Primary.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 9.sp, color = BodyIntelPalette.Secondary)
            Text(text = weight, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
            Text(text = pct, fontSize = 8.sp, color = BodyIntelPalette.DeepEmerald, fontWeight = FontWeight.SemiBold)
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModuleHeaderCard(
            title = "Body Measurements",
            subtitle = "14 Circumference Tracking & Ratios"
        )

        // Filter and Unit Toggle Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Unit Switcher
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = BodyIntelPalette.Surface,
                border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
            ) {
                Row(modifier = Modifier.padding(2.dp)) {
                    Surface(
                        onClick = { state.measurementUnit = "in" },
                        shape = RoundedCornerShape(8.dp),
                        color = if (!isCm) BodyIntelPalette.Primary else Color.Transparent
                    ) {
                        Text(
                            text = "in",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!isCm) Color.White else BodyIntelPalette.Secondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Surface(
                        onClick = { state.measurementUnit = "cm" },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCm) BodyIntelPalette.Primary else Color.Transparent
                    ) {
                        Text(
                            text = "cm",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCm) Color.White else BodyIntelPalette.Secondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Text(
                text = "Last Assessment: 30 Jun 2025",
                fontSize = 9.sp,
                color = BodyIntelPalette.Secondary
            )
        }

        // Table Header
        Surface(
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            color = BodyIntelPalette.SoftMint
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Measurement", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald)
                Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                    Text(text = "Current", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald)
                    Text(text = "Change", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald)
                }
            }
        }

        // 14 Circumferences Table
        Surface(
            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
            color = BodyIntelPalette.Surface,
            border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                val filtered = when (state.measurementsSubTab) {
                    MeasurementsSubTab.ALL -> state.circumferences
                    MeasurementsSubTab.UPPER -> state.circumferences.filter { it.category == MeasurementCategory.UPPER }
                    MeasurementsSubTab.CORE -> state.circumferences.filter { it.category == MeasurementCategory.CORE }
                    MeasurementsSubTab.LOWER -> state.circumferences.filter { it.category == MeasurementCategory.LOWER }
                    MeasurementsSubTab.RATIOS -> emptyList()
                }

                filtered.forEachIndexed { i, m ->
                    val curVal = if (isCm) "${String.format("%.1f", m.currentCm())} cm" else "${m.currentInches} in"
                    val changeVal = if (m.changeInches >= 0) "+${if (isCm) String.format("%.1f", m.changeCm()) else m.changeInches.toString()} ${if (isCm) "cm" else "in"}"
                                    else "${if (isCm) String.format("%.1f", m.changeCm()) else m.changeInches.toString()} ${if (isCm) "cm" else "in"}"
                    val changeColor = if (m.category == MeasurementCategory.CORE && m.changeInches < 0) Color(0xFF10B981) // waist decrease is good
                                      else if (m.category != MeasurementCategory.CORE && m.changeInches > 0) Color(0xFF10B981) // muscle increase is good
                                      else Color(0xFFEF4444)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = m.name, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = BodyIntelPalette.Text)
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            Text(text = curVal, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                            Text(text = changeVal, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = changeColor)
                        }
                    }
                    if (i < filtered.lastIndex) {
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F3)))
                    }
                }
            }
        }

        // Two Ratio Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = BodyIntelPalette.Surface,
                border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Waist / Height", fontSize = 10.sp, color = BodyIntelPalette.Secondary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "${state.bodyRatios.waistToHeight}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = BodyIntelPalette.Text)
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFDCFCE7)) {
                            Text(text = state.bodyRatios.waistToHeightLabel, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                        }
                    }
                    Text(text = "< 0.50 Target", fontSize = 8.sp, color = BodyIntelPalette.Secondary)
                }
            }

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = BodyIntelPalette.Surface,
                border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Waist / Hip", fontSize = 10.sp, color = BodyIntelPalette.Secondary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "${state.bodyRatios.waistToHip}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = BodyIntelPalette.Text)
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFDCFCE7)) {
                            Text(text = state.bodyRatios.waistToHipLabel, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                        }
                    }
                    Text(text = "< 0.90 Target", fontSize = 8.sp, color = BodyIntelPalette.Secondary)
                }
            }
        }
    }
}

// =============================================================================
// MODULE 4: PROGRESS (Trend Curves + Timeline)
// =============================================================================

@Composable
fun BodyProgressModule(state: BodyIntelligenceState) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModuleHeaderCard(
            title = "Transformation Progress",
            subtitle = "Track Your Physical Changes Over Time"
        )

        // Time Range Filter Pills (1M, 3M, 6M, 1Y, All)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("1M", "3M", "6M", "1Y", "All").forEach { range ->
                val isSelected = state.progressTimeRange == range
                Surface(
                    onClick = { state.progressTimeRange = range },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) BodyIntelPalette.Primary else BodyIntelPalette.Surface,
                    border = BorderStroke(1.dp, if (isSelected) BodyIntelPalette.DeepEmerald else BodyIntelPalette.CardBorder)
                ) {
                    Text(
                        text = range,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else BodyIntelPalette.Secondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
        }

        // Trend Canvas
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BodyIntelPalette.Surface,
            border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Weight Trend (kg)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                    Surface(shape = RoundedCornerShape(8.dp), color = BodyIntelPalette.Primary) {
                        Text(text = "69.5 kg • 30 Jun", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // High Contrast Curve Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val points = state.progressTimeline
                        if (points.size < 2) return@Canvas

                        val minVal = 64f
                        val maxVal = 76f
                        val range = maxVal - minVal
                        val w = size.width
                        val h = size.height

                        // Grid lines
                        for (i in 0..3) {
                            val y = h * (i / 3f)
                            drawLine(
                                color = Color(0xFFF1F5F3),
                                start = Offset(0f, y),
                                end = Offset(w, y),
                                strokeWidth = 1f
                            )
                        }

                        // Curve Path
                        val path = Path()
                        val stepX = w / (points.size - 1)

                        points.forEachIndexed { i, p ->
                            val x = i * stepX
                            val y = h - ((p.weightKg.toFloat() - minVal) / range) * h
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
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Points
                        points.forEachIndexed { i, p ->
                            val x = i * stepX
                            val y = h - ((p.weightKg.toFloat() - minVal) / range) * h
                            drawCircle(color = Color.White, radius = 5.dp.toPx(), center = Offset(x, y))
                            drawCircle(color = BodyIntelPalette.Primary, radius = 3.5.dp.toPx(), center = Offset(x, y))
                        }
                    }
                }

                // X-Axis Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    state.progressTimeline.forEach { p ->
                        Text(text = p.dateLabel, fontSize = 8.5.sp, color = BodyIntelPalette.Secondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stats Summary Strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProgressStatBox(label = "Current", value = "69.5 kg")
                    ProgressStatBox(label = "Previous", value = "72.8 kg")
                    ProgressStatBox(label = "Change", value = "-3.3 kg")
                    ProgressStatBox(label = "Progress", value = "▼ 4.5%", isHighlight = true)
                }
            }
        }

        // Progress Timeline
        Text(
            text = "ASSESSMENT TIMELINE",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BodyIntelPalette.Secondary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(start = 2.dp, top = 4.dp)
        )

        state.transformationGoal.milestones.forEach { m ->
            MilestoneItemCard(m)
        }
    }
}

@Composable
private fun ProgressStatBox(label: String, value: String, isHighlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 9.sp, color = BodyIntelPalette.Secondary)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isHighlight) BodyIntelPalette.Primary else BodyIntelPalette.Text
        )
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModuleHeaderCard(
            title = "Transformation Gallery",
            subtitle = "Visual Proof • Before / After Comparison"
        )

        // Before & After Interactive Slider View
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BodyIntelPalette.Surface,
            border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
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

                    // "BEFORE" Panel (Left)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(54.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "BEFORE", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF475569))
                            Text(text = "01 Apr 2025 • 74.5 kg", fontSize = 9.sp, color = Color(0xFF64748B))
                        }
                    }

                    // "AFTER" Panel (Right with clip)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(w * (1f - sliderX))
                            .align(Alignment.CenterEnd)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFD1FAE5), Color(0xFFA7F3D0))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.FitnessCenter, contentDescription = null, tint = BodyIntelPalette.Primary, modifier = Modifier.size(54.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "AFTER", fontSize = 12.sp, fontWeight = FontWeight.Black, color = BodyIntelPalette.DeepEmerald)
                            Text(text = "30 Jun 2025 • 69.5 kg", fontSize = 9.sp, color = BodyIntelPalette.Primary)
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
                            .size(28.dp)
                            .offset(x = dividerOffset - 14.dp, y = h / 2 - 14.dp)
                            .clip(CircleShape)
                            .background(BodyIntelPalette.Primary)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Compare, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "BEFORE • 01 Apr 2025", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Secondary)
                    Text(text = "AFTER • 30 Jun 2025", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Primary)
                }
            }
        }

        // Add Progress Photo CTA
        Button(
            onClick = { /* Upload photo */ },
            modifier = Modifier.fillMaxWidth().height(42.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BodyIntelPalette.Primary)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Add Progress Photo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        // Privacy note
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Lock, contentDescription = null, tint = BodyIntelPalette.Secondary, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Photos are privately encrypted and only visible to you.",
                fontSize = 9.sp,
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModuleHeaderCard(
            title = "Fitness Goals",
            subtitle = "Active Targets, Milestones & History"
        )

        // Active Goal Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BodyIntelPalette.Surface,
            border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Flag, contentDescription = null, tint = BodyIntelPalette.Primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = goal.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = BodyIntelPalette.SoftMint) {
                        Text(text = goal.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Primary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Start", fontSize = 9.sp, color = BodyIntelPalette.Secondary)
                        Text(text = "${goal.startKg} kg", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Current", fontSize = 9.sp, color = BodyIntelPalette.Secondary)
                        Text(text = "${goal.currentKg} kg", fontSize = 16.sp, fontWeight = FontWeight.Black, color = BodyIntelPalette.Primary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Target", fontSize = 9.sp, color = BodyIntelPalette.Secondary)
                        Text(text = "${goal.targetKg} kg", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { goal.progressPercent / 100f },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = BodyIntelPalette.Primary,
                    trackColor = BodyIntelPalette.SoftMint
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "3.3 kg lost • 4.5 kg to target", fontSize = 10.sp, color = BodyIntelPalette.Secondary)
                    Text(text = "${goal.progressPercent}% achieved", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Primary)
                }
            }
        }

        // Milestones Section
        Text(
            text = "GOAL MILESTONES",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BodyIntelPalette.Secondary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(start = 2.dp, top = 2.dp)
        )

        goal.milestones.forEach { item ->
            MilestoneItemCard(item)
        }
    }
}

@Composable
private fun MilestoneItemCard(item: MilestoneItem) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (item.completed) BodyIntelPalette.Primary else Color(0xFFE2EBE5)),
                contentAlignment = Alignment.Center
            ) {
                if (item.completed) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                if (item.note != null) {
                    Text(text = item.note, fontSize = 9.sp, color = BodyIntelPalette.Secondary)
                }
            }
            Text(text = item.date, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = BodyIntelPalette.DeepEmerald)
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModuleHeaderCard(
            title = "Health & Recovery",
            subtitle = "Key Wellness Indicators • Non-Duplicate Vitals"
        )

        // 6 Health Indicator Tiles (2 columns)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HealthPod(
                modifier = Modifier.weight(1f),
                title = "Resting HR",
                value = "${h.restingHeartRateBpm} bpm",
                subtitle = "Normal range: 60-100",
                icon = Icons.Filled.Favorite,
                iconTint = Color(0xFFEF4444)
            )
            HealthPod(
                modifier = Modifier.weight(1f),
                title = "Blood Pressure",
                value = "${h.bloodPressureSys}/${h.bloodPressureDia} mmHg",
                subtitle = "Optimal < 120/80",
                icon = Icons.Filled.Timeline,
                iconTint = Color(0xFF0284C7)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HealthPod(
                modifier = Modifier.weight(1f),
                title = "Sleep Duration",
                value = h.sleepDuration,
                subtitle = "Quality 88% • Restful",
                icon = Icons.Filled.Hotel,
                iconTint = Color(0xFF8B5CF6)
            )
            HealthPod(
                modifier = Modifier.weight(1f),
                title = "Daily Steps",
                value = "${h.dailySteps}",
                subtitle = "Goal: 10,000 steps",
                icon = Icons.Filled.DirectionsRun,
                iconTint = Color(0xFF10B981)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HealthPod(
                modifier = Modifier.weight(1f),
                title = "Hydration",
                value = "${h.hydrationLiters} L / day",
                subtitle = "Optimal: 2.5 - 3.5 L",
                icon = Icons.Filled.Opacity,
                iconTint = Color(0xFF0EA5E9)
            )
            HealthPod(
                modifier = Modifier.weight(1f),
                title = "Recovery Status",
                value = h.recoveryStatus,
                subtitle = "Readiness score: 84%",
                icon = Icons.Filled.Sync,
                iconTint = BodyIntelPalette.Primary
            )
        }
    }
}

@Composable
private fun HealthPod(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(24.dp).clip(CircleShape).background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(13.dp))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = BodyIntelPalette.Secondary)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
            Text(text = subtitle, fontSize = 8.sp, color = BodyIntelPalette.Secondary)
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModuleHeaderCard(
            title = "More Options",
            subtitle = "Assessment History, Reports & Device Sync"
        )

        MoreOptionTile(
            title = "Assessment History",
            subtitle = "View past InBody and manual tests",
            icon = Icons.Filled.History,
            onClick = { /* open history */ }
        )

        MoreOptionTile(
            title = "Reports & Analytics",
            subtitle = "Download or share progress PDF reports",
            icon = Icons.Filled.FileDownload,
            onClick = { /* open reports */ }
        )

        MoreOptionTile(
            title = "Smart Scale Sync",
            subtitle = "Connect & auto-sync with InBody or Bluetooth scales",
            icon = Icons.Filled.Sync,
            onClick = { /* open scale sync */ }
        )

        MoreOptionTile(
            title = "Trainer Notes & Feedback",
            subtitle = "View coach feedback and form reviews",
            icon = Icons.Filled.Note,
            onClick = { /* open trainer notes */ }
        )

        MoreOptionTile(
            title = "Import Assessment",
            subtitle = "Scan InBody / DEXA report sheet",
            icon = Icons.Filled.Scale,
            onClick = { /* open import */ }
        )

        MoreOptionTile(
            title = "Body Settings",
            subtitle = "Units (kg/lb, cm/in), privacy and reminder frequency",
            icon = Icons.Filled.Settings,
            onClick = { /* open settings */ }
        )
    }
}

@Composable
private fun MoreOptionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = BodyIntelPalette.Surface,
        border = BorderStroke(1.dp, BodyIntelPalette.CardBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(BodyIntelPalette.SoftMint),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = BodyIntelPalette.Primary, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.Text)
                Text(text = subtitle, fontSize = 9.sp, color = BodyIntelPalette.Secondary)
            }
        }
    }
}

// =============================================================================
// REUSABLE CARD HEADER
// =============================================================================

@Composable
private fun ModuleHeaderCard(title: String, subtitle: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BodyIntelPalette.SoftMint.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, BodyIntelPalette.Primary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(BodyIntelPalette.Primary)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Column {
                Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BodyIntelPalette.DeepEmerald)
                Text(text = subtitle, fontSize = 9.sp, color = BodyIntelPalette.Secondary)
            }
        }
    }
}
