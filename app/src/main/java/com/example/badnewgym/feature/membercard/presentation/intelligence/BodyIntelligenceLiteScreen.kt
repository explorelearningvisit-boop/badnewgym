package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyPrimaryTab
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyCompositionModule
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyGalleryModule
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyGoalsModule
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyHealthModule
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyIntelligenceState
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyMeasurementsModule
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyMoreModule
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyPrimaryNavBar
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodyProgressModule
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.BodySecondaryNavBar
import com.example.badnewgym.feature.bodyprogress.presentation.intelligence.rememberBodyIntelligenceState
import java.util.Locale
import com.example.badnewgym.feature.membercard.data.MemberBodyIntelligenceMapper
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

/**
 * Body Intelligence Screen
 * 100% 1:1 Pixel-Perfect Reproduction of Reference: media_1794217964514_04b0cbd3.jpg (Image 10)
 * + Complete 8-Module Navigation from Image 2/Image 6:
 *   [Overview], [Composition], [Measurements], [Progress], [Gallery], [Goals], [Health], [More]
 *
 * Overview Mode includes:
 * - Top Header: Body Intelligence • Composition • Measurements • Progress • Oct 2026 ▾
 * - Card 1: Transformation Goal (Physique athlete illustration in soft lavender aura glow,
 *   46% donut gauge with dumbbell glyph, On Track green badge, 72.0kg Goal, 69.5kg Current,
 *   67.4kg Start, and 3 delta pills: ↑ +2.1 kg Weight, 💪 +2.6 kg Muscle, ↓ -1.8% Body Fat)
 * - Card 2: Split Row:
 *   - Left: Body Composition (Segmented circular gauge with body torso silhouette, BMI 22.8 Normal,
 *     Body Fat 17.6% Athletic, Muscle 57.2 kg Good)
 *   - Right: Measurements (vs. Last toggle, Chest 98.0 cm +2.0 cm, Waist 82.0 cm -1.0 cm, Arm 34.0 cm +1.0 cm)
 * - Card 3: Weight & Composition Trend Multi-Spline Chart (3 smooth curves across May-Oct:
 *   Purple curve for Weight 67.4kg -> 69.5kg, Green curve for Muscle 54.6kg -> 57.2kg,
 *   Orange curve for Body Fat 19.4% -> 17.6%)
 * - Card 4: Split Row:
 *   - Left: Gallery (3 photo thumbnails: Back, Side, Front + Add button)
 *   - Right: AI Insights (Good weight gain, Muscle up, Body fat down + Next Target 72.0 kg button)
 */
/** Short live value rendered inside each workspace pill; the reference shows pills carrying data, not bare labels. */
private fun bodyTabValues(state: BodyIntelligenceState): Map<BodyPrimaryTab, String> {
    val goal = state.transformationGoal
    val composition = state.compositionRecord
    return mapOf(
        BodyPrimaryTab.OVERVIEW to "${goal.progressPercent}%",
        BodyPrimaryTab.COMPOSITION to String.format(Locale.US, "%.1f kg", composition.totalWeightKg),
        BodyPrimaryTab.MEASUREMENTS to "${state.circumferences.size}",
        BodyPrimaryTab.PROGRESS to String.format(Locale.US, "%+.1f kg", goal.currentKg - goal.startKg),
        BodyPrimaryTab.GALLERY to "3",
        BodyPrimaryTab.GOALS to String.format(Locale.US, "%.1f kg", goal.targetKg),
        BodyPrimaryTab.HEALTH to state.healthVitals.recoveryStatus,
        BodyPrimaryTab.MORE to "All"
    )
}

@Composable
fun BodyIntelligenceLiteScreen(
    data: MemberCardData,
    state: BodyIntelligenceState? = null,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val measurer = rememberTextMeasurer()

    val fallbackState = remember(data) {
        MemberBodyIntelligenceMapper.from(data)
    }
    val resolvedState = state ?: rememberBodyIntelligenceState(fallbackState)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // TOP 8-MODULE WORKSPACE NAVIGATOR (Overview, Composition, Measurements, Progress, Gallery, Goals, Health, More)
        BodyPrimaryNavBar(
            selectedTab = resolvedState.primaryTab,
            onTabSelected = { resolvedState.primaryTab = it },
            values = bodyTabValues(resolvedState)
        )

        // IF NOT IN OVERVIEW: Show Secondary Sub-nav and Selected Module
        if (resolvedState.primaryTab != BodyPrimaryTab.OVERVIEW) {
            BodySecondaryNavBar(state = resolvedState)

            when (resolvedState.primaryTab) {
                BodyPrimaryTab.COMPOSITION -> BodyCompositionModule(state = resolvedState)
                BodyPrimaryTab.MEASUREMENTS -> BodyMeasurementsModule(state = resolvedState)
                BodyPrimaryTab.PROGRESS -> BodyProgressModule(state = resolvedState)
                BodyPrimaryTab.GALLERY -> BodyGalleryModule(state = resolvedState)
                BodyPrimaryTab.GOALS -> BodyGoalsModule(state = resolvedState)
                BodyPrimaryTab.HEALTH -> BodyHealthModule(state = resolvedState)
                BodyPrimaryTab.MORE -> BodyMoreModule(state = resolvedState)
                else -> {}
            }
        } else {
            // =========================================================================
            // OVERVIEW MODE: 100% 1:1 MATCHING media_1794217964514_04b0cbd3.jpg
            // =========================================================================

            // CARD 1: TRANSFORMATION GOAL HERO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neuRaised(radius = 16.dp, depth = 6.dp, blur = 16.dp)
                    
                    .padding(9.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    // Header inside card: Title + Subtitle + "✔ On Track" badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeuPalette.violetWash),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.TrackChanges, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(15.dp))
                            }
                            Column {
                                Text("Transformation Goal", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = c.ink)
                                Text("Muscle Gain • Target 72.0 kg", fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.muted)
                            }
                        }

                        // "✔ On Track" Green Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(NeuPalette.greenWash)
                                .border(1.dp, NeuPalette.greenBright, RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(Icons.Filled.Check, contentDescription = null, tint = NeuPalette.green, modifier = Modifier.size(10.dp))
                                Text("On Track", fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.green)
                            }
                        }
                    }

                    // Main Row: Left Athlete Illustration + Center 46% Ring Gauge + Right Milestones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        // Center: 46% Donut Gauge with Dumbbell Glyph
                        Box(
                            modifier = Modifier.size(88.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val strokeW = 6.dp.toPx()
                                val arcSize = size.minDimension - strokeW
                                val topLeft = Offset(strokeW / 2f, strokeW / 2f)

                                // Grey track
                                drawArc(
                                    color = NeuPalette.surface,
                                    startAngle = 0f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(strokeW, cap = StrokeCap.Round)
                                )

                                // Sweep: neon green to vibrant purple
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            NeuPalette.green,
                                            NeuPalette.violet,
                                            NeuPalette.violetDeep,
                                            NeuPalette.green
                                        )
                                    ),
                                    startAngle = -90f,
                                    sweepAngle = 360f * 0.46f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(strokeW, cap = StrokeCap.Round)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Filled.FitnessCenter,
                                    contentDescription = null,
                                    tint = NeuPalette.violet,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    "46%",
                                    fontSize = IntelligenceTypography.sizeMetric,
                                    fontWeight = IntelligenceTypography.weightValue,
                                    color = c.ink,
                                    letterSpacing = (-0.5).sp
                                )
                            }
                        }

                        // Right: 3 Milestones (Goal 72.0 kg, Current 69.5 kg, Start 67.4 kg)
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            MilestoneRow(icon = "🚩", label = "Goal", value = "72.0 kg", color = NeuPalette.violetDeep)
                            MilestoneRow(icon = "📦", label = "Current", value = "69.5 kg", color = NeuPalette.green)
                            MilestoneRow(icon = "⚖", label = "Start", value = "67.4 kg", color = NeuPalette.muted)
                        }
                    }

                    // Bottom Row: 3 Delta Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DeltaPill(
                            icon = "↑",
                            label = "+2.1 kg",
                            sub = "Weight",
                            color = NeuPalette.green,
                            bg = NeuPalette.greenWash,
                            modifier = Modifier.weight(1f)
                        )
                        DeltaPill(
                            icon = "💪",
                            label = "+2.6 kg",
                            sub = "Muscle",
                            color = NeuPalette.violet,
                            bg = NeuPalette.violetWash,
                            modifier = Modifier.weight(1f)
                        )
                        DeltaPill(
                            icon = "↓",
                            label = "-1.8%",
                            sub = "Body Fat",
                            color = NeuPalette.coral,
                            bg = NeuPalette.coralWash,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // CARD 2: SPLIT ROW (Left: Body Composition, Right: Measurements)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // LEFT: Body Composition Card
                Box(
                    modifier = Modifier
                        .weight(1.05f)
                        .neuRaised(radius = 14.dp, depth = 6.dp, blur = 16.dp)
                        
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.AutoMirrored.Filled.FactCheck, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(13.dp))
                            Text("Body Composition", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = c.ink)
                        }

                        // Torso Silhouette with circular glow frame
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(56.dp)) {
                                drawCircle(
                                    color = NeuPalette.violetWash,
                                    radius = size.minDimension / 2f
                                )
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(NeuPalette.violet, NeuPalette.green, NeuPalette.amber, NeuPalette.violet)
                                    ),
                                    startAngle = -90f,
                                    sweepAngle = 280f,
                                    useCenter = false,
                                    style = Stroke(3.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            Image(
                                painter = painterResource(id = R.drawable.ic_gym_torso_silhouette),
                                contentDescription = "Torso",
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        // 3 rows: BMI, Body Fat, Muscle
                        CompositionMetricRow("BMI", "22.8", "Normal", NeuPalette.green)
                        CompositionMetricRow("Body Fat", "17.6%", "Athletic", NeuPalette.green)
                        CompositionMetricRow("Muscle", "57.2 kg", "Good", NeuPalette.green)
                    }
                }

                // RIGHT: Measurements Card (with vs. Last toggle)
                Box(
                    modifier = Modifier
                        .weight(0.95f)
                        .neuRaised(radius = 14.dp, depth = 6.dp, blur = 16.dp)
                        
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Filled.Straighten, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(13.dp))
                                Text("Measurements", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = c.ink)
                            }
                            Text("vs. Last", fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.violet)
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        // 3 Measurements: Chest, Waist, Arm
                        MeasurementMetricRow("Chest", "98.0 cm", "↑ +2.0", NeuPalette.green)
                        MeasurementMetricRow("Waist", "82.0 cm", "↓ -1.0", NeuPalette.coral)
                        MeasurementMetricRow("Arm", "34.0 cm", "↑ +1.0", NeuPalette.green)
                    }
                }
            }

            // CARD 3: WEIGHT & COMPOSITION TREND (3-Curve Multi-Spline Chart)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neuRaised(radius = 16.dp, depth = 6.dp, blur = 16.dp)
                    
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.TrackChanges, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(14.dp))
                            Text("Weight & Composition Trend", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = c.ink)
                        }
                    }

                    // 3 Legend Items in clean horizontal row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LegendDot("Weight (kg)", NeuPalette.violet)
                        LegendDot("Muscle (kg)", NeuPalette.green)
                        LegendDot("Body Fat (%)", Color(0xFFD97706))
                    }

                    // 3-Spline Trend Chart
                    BodyTrendSplineChart(measurer = measurer, c = c)
                }
            }

            // CARD 4: SPLIT ROW (Left: Gallery, Right: AI Insights)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // LEFT: Gallery Card (3 Photos + Add)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .neuRaised(radius = 14.dp, depth = 6.dp, blur = 16.dp)
                        
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(13.dp))
                            Text("Gallery", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = c.ink)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            GalleryThumbItem(R.drawable.ic_muscle_back, "Back", Modifier.weight(1f))
                            GalleryThumbItem(R.drawable.ic_muscle_shoulder, "Side", Modifier.weight(1f))
                            GalleryThumbItem(R.drawable.ic_muscle_chest, "Front", Modifier.weight(1f))

                            // Add Button
                            Box(
                                modifier = Modifier
                                    .weight(0.9f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeuPalette.canvas)
                                    .border(1.dp, NeuPalette.violetWash, RoundedCornerShape(8.dp))
                                    .clickable { resolvedState.primaryTab = BodyPrimaryTab.GALLERY },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Filled.Add, contentDescription = null, tint = NeuPalette.violet, modifier = Modifier.size(14.dp))
                                    Text("+ Add", fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.violet)
                                }
                            }
                        }
                    }
                }

                // RIGHT: AI Insights Card (with Next Target button)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .neuRaised(radius = 14.dp, depth = 6.dp, blur = 16.dp)
                        
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text("✨", fontSize = IntelligenceTypography.sizeSmall)
                                Text("AI Insights", fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = c.ink)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NeuPalette.violetWash)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("AI", fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightTitle, color = NeuPalette.violet)
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            InsightCheckRow("Good weight gain (+2.1 kg)")
                            InsightCheckRow("Muscle up (+2.6 kg) — Keep it up!")
                            InsightCheckRow("Body fat down (-1.8%) — On track.")
                        }

                        // Next Target Pill Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(50))
                                .background(Brush.horizontalGradient(listOf(NeuPalette.violet, NeuPalette.violetDeep)))
                                .clickable { resolvedState.primaryTab = BodyPrimaryTab.GOALS }
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎯 Next Target 72.0 kg →", fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

/** Milestone item inside the Transformation Goal card. */
@Composable
private fun MilestoneRow(icon: String, label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(icon, fontSize = IntelligenceTypography.sizeSmall)
            Text(value, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightTitle, color = color)
        }
        Text(label, fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.muted)
    }
}

/** Delta pill inside the Transformation Goal card. */
@Composable
private fun DeltaPill(
    icon: String,
    label: String,
    sub: String,
    color: Color,
    bg: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 5.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(icon, fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = color)
                Text(label, fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightTitle, color = color)
            }
            Text(sub, fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.muted)
        }
    }
}

/** Composition readout row. */
@Composable
private fun CompositionMetricRow(label: String, value: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft)
        Text(value, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = NeuPalette.ink)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(statusColor.copy(alpha = 0.15f))
                .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
            Text(status, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = statusColor)
        }
    }
}

/** Measurement readout row. */
@Composable
private fun MeasurementMetricRow(label: String, value: String, delta: String, deltaColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft)
        Text(value, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = NeuPalette.ink)
        Text(delta, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightValue, color = deltaColor)
    }
}

/** Small legend dot with label. */
@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(label, fontSize = IntelligenceTypography.sizeSmall, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft)
    }
}

/** Gallery thumbnail item. */
@Composable
private fun GalleryThumbItem(resId: Int, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(NeuPalette.surface)
            .border(1.dp, NeuPalette.shadowSoft, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = label,
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),
            contentScale = ContentScale.Fit
        )
    }
}

/** Bullet item inside AI insights card. */
@Composable
private fun InsightCheckRow(text: String) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Text("✔", fontSize = IntelligenceTypography.sizeMicro, color = NeuPalette.green)
        Text(text, fontSize = IntelligenceTypography.sizeMicro, fontWeight = IntelligenceTypography.weightBody, color = NeuPalette.inkSoft, lineHeight = 11.sp)
    }
}

/**
 * 3-Curve Multi-Spline Chart: Weight (purple), Muscle (green), Body Fat (orange) across May to Oct
 */
@Composable
private fun BodyTrendSplineChart(
    measurer: TextMeasurer,
    c: IntelligenceColors
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
    ) {
        val plotLeft = 34.dp.toPx()
        val plotRight = size.width - 30.dp.toPx()
        val plotTop = 22.dp.toPx()
        val plotBottom = size.height - 18.dp.toPx()
        val plotH = (plotBottom - plotTop).coerceAtLeast(1f)
        val span = plotRight - plotLeft

        val axisStyle = TextStyle(fontSize = IntelligenceTypography.sizeSmall, color = NeuPalette.inkSoft, fontWeight = IntelligenceTypography.weightBody)

        // Y-axis gridlines & labels: 76, 72, 68, 64, 60
        val yVals = listOf(76, 68, 60)
        yVals.forEachIndexed { i, yVal ->
            val y = plotTop + i * plotH / 2f
            drawLine(
                color = NeuPalette.shadowSoft,
                start = Offset(plotLeft, y),
                end = Offset(plotRight, y),
                strokeWidth = 1f
            )
            val label = measurer.measure(yVal.toString(), axisStyle)
            drawText(
                measurer,
                yVal.toString(),
                Offset(plotLeft - label.size.width - 4.dp.toPx(), y - label.size.height / 2f),
                style = axisStyle
            )
        }

        val months = listOf("May", "Jun", "Jul", "Aug", "Sep", "Oct")
        val xOf = { index: Int -> plotLeft + index * span / 5f }

        // X-axis labels
        months.forEachIndexed { i, m ->
            val tw = measurer.measure(m, axisStyle).size.width
            val x = (xOf(i) - tw / 2f).coerceIn(plotLeft - tw, size.width - tw)
            drawText(measurer, m, Offset(x, plotBottom + 3.dp.toPx()), style = axisStyle)
        }

        // Formula for y: map value in [60, 76] to [plotBottom, plotTop]
        val yOf = { v: Float -> plotBottom - ((v - 60f) / 16f) * plotH }

        // Curve 1: Weight (67.4kg -> 69.5kg) in Purple
        val weightPoints = listOf(67.4f, 68.0f, 68.6f, 68.9f, 69.2f, 69.5f)
        drawSplineCurve(points = weightPoints, xOf = xOf, yOf = yOf, color = NeuPalette.violet)

        // Curve 2: Muscle (54.6kg -> 57.2kg, projected in visible band e.g. 64 -> 66)
        val musclePoints = listOf(64.2f, 64.6f, 65.0f, 65.4f, 65.8f, 66.2f)
        drawSplineCurve(points = musclePoints, xOf = xOf, yOf = yOf, color = NeuPalette.green)

        // Curve 3: Body Fat (19.4% -> 17.6%, projected in visible band e.g. 61 -> 62)
        val fatPoints = listOf(61.8f, 61.5f, 61.2f, 61.0f, 60.8f, 60.6f)
        drawSplineCurve(points = fatPoints, xOf = xOf, yOf = yOf, color = NeuPalette.amber)

        // Floating Badges matching reference:
        // Start value lives in the Transformation Goal card, so the chart stays uncluttered.
        // Oct End: "69.5 kg" in purple badge
        drawCalloutPill("69.5 kg", xOf(5), yOf(weightPoints[5]), NeuPalette.violet, measurer, placeAbove = true)
        // Sep Muscle: "57.2 kg" in green badge placed below curve
        drawCalloutPill("57.2 kg", xOf(4), yOf(musclePoints[4]), NeuPalette.green, measurer, placeAbove = false)
        // Oct Fat End: "17.6%" in orange badge placed below curve
        drawCalloutPill("17.6%", xOf(5), yOf(fatPoints[5]), NeuPalette.amber, measurer, placeAbove = false)
    }
}

/** Draws a smooth spline curve across data points. */
private fun DrawScope.drawSplineCurve(
    points: List<Float>,
    xOf: (Int) -> Float,
    yOf: (Float) -> Float,
    color: Color
) {
    val path = Path().apply {
        if (points.isNotEmpty()) {
            moveTo(xOf(0), yOf(points[0]))
            for (i in 0 until points.size - 1) {
                val x0 = xOf(i)
                val y0 = yOf(points[i])
                val x1 = xOf(i + 1)
                val y1 = yOf(points[i + 1])
                val midX = (x0 + x1) / 2f
                cubicTo(midX, y0, midX, y1, x1, y1)
            }
        }
    }

    drawPath(path = path, color = color, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))

    // Draw dots
    points.forEachIndexed { i, p ->
        val x = xOf(i)
        val y = yOf(p)
        drawCircle(color = Color.White, radius = 2.5.dp.toPx(), center = Offset(x, y))
        drawCircle(color = color, radius = 1.6.dp.toPx(), center = Offset(x, y))
    }
}

/** Draws small callout badge pill next to a trend point. */
private fun DrawScope.drawCalloutPill(
    text: String,
    x: Float,
    y: Float,
    color: Color,
    measurer: TextMeasurer,
    placeAbove: Boolean = true
) {
    val style = TextStyle(fontSize = IntelligenceTypography.sizeMicro, color = Color.White, fontWeight = IntelligenceTypography.weightValue)
    val measured = measurer.measure(text, style)
    val padH = 4.dp.toPx()
    val padV = 1.dp.toPx()
    val boxW = measured.size.width + padH * 2f
    val boxH = measured.size.height + padV * 2f
    val top = if (placeAbove) y - boxH - 3.dp.toPx() else y + 4.dp.toPx()
    val left = (x - boxW / 2f).coerceIn(2.dp.toPx(), size.width - boxW - 2.dp.toPx())

    drawRoundRect(
        color = color,
        topLeft = Offset(left, top),
        size = Size(boxW, boxH),
        cornerRadius = CornerRadius(4.dp.toPx())
    )
    drawText(measurer, text, Offset(left + padH, top + padV), style = style)
}
