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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // TOP 8-MODULE WORKSPACE NAVIGATOR (Overview, Composition, Measurements, Progress, Gallery, Goals, Health, More)
        BodyPrimaryNavBar(
            selectedTab = resolvedState.primaryTab,
            onTabSelected = { resolvedState.primaryTab = it }
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

            // HEADER CARD: Body Intelligence • Composition • Measurements • Progress
            IntelligenceSectionCard(
                title = "Body Intelligence",
                icon = Icons.Filled.Accessibility,
                subtitle = "Composition • Measurements • Progress",
                trailing = { IntelligencePillOutlined("Oct 2026 ▾") }
            ) {
                // Inside Header Card: Empty wrapper, content follows below
            }

            // CARD 1: TRANSFORMATION GOAL HERO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color(0x148B5CF6), spotColor = Color(0x148B5CF6))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0x288B5CF6), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                    .background(Color(0xFFEDE9FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.TrackChanges, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(15.dp))
                            }
                            Column {
                                Text("Transformation Goal", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = c.ink)
                                Text("Muscle Gain • Target 72.0 kg", fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                            }
                        }

                        // "✔ On Track" Green Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFDCFCE7))
                                .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(Icons.Filled.Check, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(10.dp))
                                Text("On Track", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                            }
                        }
                    }

                    // Main Row: Left Athlete Illustration + Center 46% Ring Gauge + Right Milestones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Left: Athlete in Soft Lavender Aura Glow
                        Box(
                            modifier = Modifier
                                .size(width = 86.dp, height = 104.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color(0xFFDDD6FE), Color(0xFFFAF5FF), Color.White),
                                        center = Offset(100f, 120f),
                                        radius = 180f
                                    )
                                )
                                .border(1.dp, Color(0xFFE9D5FF), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.illustration_character_flex),
                                contentDescription = "Physique Athlete",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp)
                            )
                        }

                        // Center: 46% Donut Gauge with Dumbbell Glyph
                        Box(
                            modifier = Modifier.size(88.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val strokeW = 7.dp.toPx()
                                val arcSize = size.minDimension - strokeW
                                val topLeft = Offset(strokeW / 2f, strokeW / 2f)

                                // Grey track
                                drawArc(
                                    color = Color(0xFFF1F5F9),
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
                                            Color(0xFF10B981),
                                            Color(0xFF8B5CF6),
                                            Color(0xFF7C3AED),
                                            Color(0xFF10B981)
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
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    "46%",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
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
                            MilestoneRow(icon = "🚩", label = "Goal", value = "72.0 kg", color = Color(0xFF7C3AED))
                            MilestoneRow(icon = "📦", label = "Current", value = "69.5 kg", color = Color(0xFF10B981))
                            MilestoneRow(icon = "⚖", label = "Start", value = "67.4 kg", color = Color(0xFF64748B))
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
                            color = Color(0xFF10B981),
                            bg = Color(0xFFDCFCE7),
                            modifier = Modifier.weight(1f)
                        )
                        DeltaPill(
                            icon = "💪",
                            label = "+2.6 kg",
                            sub = "Muscle",
                            color = Color(0xFF8B5CF6),
                            bg = Color(0xFFEDE9FE),
                            modifier = Modifier.weight(1f)
                        )
                        DeltaPill(
                            icon = "↓",
                            label = "-1.8%",
                            sub = "Body Fat",
                            color = Color(0xFFEF4444),
                            bg = Color(0xFFFEE2E2),
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
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp), ambientColor = Color(0x108B5CF6), spotColor = Color(0x108B5CF6))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0x208B5CF6), RoundedCornerShape(14.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.AutoMirrored.Filled.FactCheck, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(13.dp))
                            Text("Body Composition", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = c.ink)
                        }

                        // Torso Silhouette with circular glow frame
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(56.dp)) {
                                drawCircle(
                                    color = Color(0xFFEDE9FE),
                                    radius = size.minDimension / 2f
                                )
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(Color(0xFF8B5CF6), Color(0xFF10B981), Color(0xFFF59E0B), Color(0xFF8B5CF6))
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
                        CompositionMetricRow("BMI", "22.8", "Normal", Color(0xFF10B981))
                        CompositionMetricRow("Body Fat", "17.6%", "Athletic", Color(0xFF10B981))
                        CompositionMetricRow("Muscle", "57.2 kg", "Good", Color(0xFF10B981))
                    }
                }

                // RIGHT: Measurements Card (with vs. Last toggle)
                Box(
                    modifier = Modifier
                        .weight(0.95f)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp), ambientColor = Color(0x108B5CF6), spotColor = Color(0x108B5CF6))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0x208B5CF6), RoundedCornerShape(14.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Filled.Straighten, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(13.dp))
                                Text("Measurements", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = c.ink)
                            }
                            Text("vs. Last", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        // 3 Measurements: Chest, Waist, Arm
                        MeasurementMetricRow("Chest", "98.0 cm", "↑ +2.0 cm", Color(0xFF10B981))
                        MeasurementMetricRow("Waist", "82.0 cm", "↓ -1.0 cm", Color(0xFFEF4444))
                        MeasurementMetricRow("Arm", "34.0 cm", "↑ +1.0 cm", Color(0xFF10B981))
                    }
                }
            }

            // CARD 3: WEIGHT & COMPOSITION TREND (3-Curve Multi-Spline Chart)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color(0x148B5CF6), spotColor = Color(0x148B5CF6))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0x288B5CF6), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Header + 3-Color Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.TrackChanges, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(14.dp))
                            Text("Weight & Composition Trend", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = c.ink)
                        }

                        // 3 Legend Items
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            LegendDot("Weight (kg)", Color(0xFF8B5CF6))
                            LegendDot("Muscle (kg)", Color(0xFF10B981))
                            LegendDot("Body Fat (%)", Color(0xFFF59E0B))
                        }
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
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp), ambientColor = Color(0x108B5CF6), spotColor = Color(0x108B5CF6))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0x208B5CF6), RoundedCornerShape(14.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(13.dp))
                            Text("Gallery", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = c.ink)
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
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFAF5FF))
                                    .border(1.dp, Color(0xFFDDD6FE), RoundedCornerShape(8.dp))
                                    .clickable { resolvedState.primaryTab = BodyPrimaryTab.GALLERY },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Filled.Add, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(14.dp))
                                    Text("+ Add", fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6))
                                }
                            }
                        }
                    }
                }

                // RIGHT: AI Insights Card (with Next Target button)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp), ambientColor = Color(0x108B5CF6), spotColor = Color(0x108B5CF6))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0x208B5CF6), RoundedCornerShape(14.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text("✨", fontSize = 11.sp)
                                Text("AI Insights", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = c.ink)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFEDE9FE))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("AI", fontSize = 7.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B5CF6))
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
                                .background(Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED))))
                                .clickable { resolvedState.primaryTab = BodyPrimaryTab.GOALS }
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎯 Next Target 72.0 kg →", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
            Text(icon, fontSize = 10.sp)
            Text(value, fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, color = color)
        }
        Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
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
                Text(icon, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = color)
                Text(label, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, color = color)
            }
            Text(sub, fontSize = 7.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
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
        Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
        Text(value, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1B4B))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(statusColor.copy(alpha = 0.12f))
                .padding(horizontal = 4.dp, vertical = 1.dp)
        ) {
            Text(status, fontSize = 7.5.sp, fontWeight = FontWeight.Bold, color = statusColor)
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
        Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
        Text(value, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1B4B))
        Text(delta, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = deltaColor)
    }
}

/** Small legend dot with label. */
@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(label, fontSize = 7.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
    }
}

/** Gallery thumbnail item. */
@Composable
private fun GalleryThumbItem(resId: Int, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp)),
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
        Text("✔", fontSize = 8.sp, color = Color(0xFF10B981))
        Text(text, fontSize = 8.sp, fontWeight = FontWeight.Normal, color = Color(0xFF475569), lineHeight = 11.sp)
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
            .height(115.dp)
    ) {
        val plotLeft = 24.dp.toPx()
        val plotRight = size.width - 6.dp.toPx()
        val plotTop = 22.dp.toPx()
        val plotBottom = size.height - 18.dp.toPx()
        val plotH = (plotBottom - plotTop).coerceAtLeast(1f)
        val span = plotRight - plotLeft

        val axisStyle = TextStyle(fontSize = 7.5.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)

        // Y-axis gridlines & labels: 76, 72, 68, 64, 60
        val yVals = listOf(76, 72, 68, 64, 60)
        yVals.forEachIndexed { i, yVal ->
            val y = plotTop + i * plotH / 4f
            drawLine(
                color = Color(0xFFF1F5F9),
                start = Offset(plotLeft, y),
                end = Offset(plotRight, y),
                strokeWidth = 1f
            )
            drawText(measurer, yVal.toString(), Offset(0f, y - 5.dp.toPx()), style = axisStyle)
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
        drawSplineCurve(points = weightPoints, xOf = xOf, yOf = yOf, color = Color(0xFF8B5CF6))

        // Curve 2: Muscle (54.6kg -> 57.2kg, projected in visible band e.g. 64 -> 66)
        val musclePoints = listOf(64.2f, 64.6f, 65.0f, 65.4f, 65.8f, 66.2f)
        drawSplineCurve(points = musclePoints, xOf = xOf, yOf = yOf, color = Color(0xFF10B981))

        // Curve 3: Body Fat (19.4% -> 17.6%, projected in visible band e.g. 61 -> 62)
        val fatPoints = listOf(61.8f, 61.5f, 61.2f, 61.0f, 60.8f, 60.6f)
        drawSplineCurve(points = fatPoints, xOf = xOf, yOf = yOf, color = Color(0xFFF59E0B))

        // Floating Badges matching reference:
        // May Start: "67.4 kg" in purple badge
        drawCalloutPill("67.4 kg", xOf(0), yOf(weightPoints[0]), Color(0xFF8B5CF6), measurer)
        // Oct End: "69.5 kg" in purple badge
        drawCalloutPill("69.5 kg", xOf(5), yOf(weightPoints[5]), Color(0xFF8B5CF6), measurer)
        // Oct Muscle End: "57.2 kg" in green badge
        drawCalloutPill("57.2 kg", xOf(5), yOf(musclePoints[5]), Color(0xFF10B981), measurer)
        // Oct Fat End: "17.6%" in orange badge
        drawCalloutPill("17.6%", xOf(5), yOf(fatPoints[5]), Color(0xFFF59E0B), measurer)
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
    measurer: TextMeasurer
) {
    val style = TextStyle(fontSize = 7.5.sp, color = Color.White, fontWeight = FontWeight.Bold)
    val measured = measurer.measure(text, style)
    val padH = 4.dp.toPx()
    val padV = 1.5.dp.toPx()
    val boxW = measured.size.width + padH * 2f
    val boxH = measured.size.height + padV * 2f
    val top = y - boxH - 4.dp.toPx()
    val left = (x - boxW / 2f).coerceIn(2.dp.toPx(), size.width - boxW - 2.dp.toPx())

    drawRoundRect(
        color = color,
        topLeft = Offset(left, top),
        size = Size(boxW, boxH),
        cornerRadius = CornerRadius(4.dp.toPx())
    )
    drawText(measurer, text, Offset(left + padH, top + padV), style = style)
}
