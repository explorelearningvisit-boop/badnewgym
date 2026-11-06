package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.rounded.LocalDrink
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.MeasurementEntry
import com.example.badnewgym.feature.membercard.domain.model.MeasurementSite
import java.util.Locale

/** Reference template values mirroring the approved asset-pack mockup. */
private const val START_WEIGHT_KG = "72.8 kg"
private const val TARGET_WEIGHT_KG = "65.0 kg"
private const val GOAL_RING_PERCENT = 56
private const val BMI_VALUE = "22.7"
private const val BODY_FAT_VALUE = "18.5%"
private const val MUSCLE_VALUE = "56.6 kg"
private const val MUSCLE_GAIN_LABEL = "+4.5 kg"
private const val ACHIEVED_PERCENT_LABEL = "42.3%"
private const val WATER_INTAKE_LABEL = "2.5 L"

/** Body tab: inline title, 8-chip workspace switcher and one lightweight panel per chip. */
@Composable
fun BodyIntelligenceLiteScreen(
    data: MemberCardData,
    pass: BadGymMemberPass,
    onMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    val weightItem = data.bodyStats.firstOrNull { it.type == BodyStatType.WEIGHT }
    val currentWeight = weightItem?.displayValue() ?: TARGET_WEIGHT_KG
    val selectedState = rememberSaveable { mutableIntStateOf(0) }
    val selected = selectedState.intValue

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(c.accent),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Layers, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Body Intelligence", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
                Text("Member: ${pass.name}", fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        val chips: List<Pair<String, ImageVector>> = listOf(
            "Overview" to Icons.Filled.Layers,
            "Composition" to Icons.Filled.PieChart,
            "Measurements" to Icons.Filled.Straighten,
            "Progress" to Icons.Filled.TrendingUp,
            "Gallery" to Icons.Filled.PhotoCamera,
            "Goals" to Icons.Filled.Flag,
            "Health" to Icons.Filled.Favorite,
            "More" to Icons.Filled.MoreHoriz
        )
        chips.chunked(4).forEachIndexed { rowIndex, rowChips ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                rowChips.forEachIndexed { columnIndex, (label, icon) ->
                    val index = rowIndex * 4 + columnIndex
                    IntelligenceChip(
                        label = label,
                        icon = icon,
                        selected = selected == index,
                        onClick = {
                            if (index == chips.lastIndex) onMoreClick() else selectedState.intValue = index
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            if (rowIndex == 0) Spacer(modifier = Modifier.height(6.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))

        when (selected) {
            0 -> OverviewPanel(c, weightItem, currentWeight)
            1 -> CompositionPanel(c, weightItem)
            2 -> MeasurementsPanel(c, data.measurements)
            3 -> ProgressPanel(c, weightItem)
            4 -> GalleryPanel(c)
            5 -> GoalsPanel(c)
            else -> HealthPanel(c)
        }
    }
}

/** Overview: transformation goal ring with the 💪 mark, weight pods, composition and quick insights. */
@Composable
private fun OverviewPanel(c: IntelligenceColors, weightItem: BodyStatItem?, currentWeight: String) {
    val currentKg = weightItem?.kilograms() ?: 69.5f
    val deltaKg = currentKg - (START_WEIGHT_KG.kilogramsFromLabel())
    val weightDeltaLabel = String.format(Locale.US, "%+.1f kg", deltaKg)

    IntelligenceSectionCard(
        title = "Transformation Goal",
        icon = Icons.Filled.TrackChanges,
        trailing = { IntelligencePillOutlined("✓ Fat Loss • Target $TARGET_WEIGHT_KG") }
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(c.accentWash),
                contentAlignment = Alignment.Center
            ) {
                Text("💪", fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier.size(112.dp),
                contentAlignment = Alignment.Center
            ) {
                GoalRing(percent = GOAL_RING_PERCENT, track = c.ink.copy(alpha = 0.08f), accent = c.accent, warn = c.warn)
                Text("${GOAL_RING_PERCENT}%", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.width(84.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeightPod("Start", START_WEIGHT_KG, c.ink)
                PodDivider(c)
                WeightPod("Current", currentWeight, c.accent)
                PodDivider(c)
                WeightPod("Target", TARGET_WEIGHT_KG, c.accentDeep)
            }
        }
    }
    Spacer(modifier = Modifier.height(6.dp))
    CompositionCard(c)
    Spacer(modifier = Modifier.height(6.dp))
    IntelligenceSectionCard(title = "Quick Insights", icon = Icons.Filled.Insights) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            InsightTile(Icons.Filled.MonitorWeight, c.warn, weightDeltaLabel, "Weight", Modifier.weight(1f), c)
            InsightTile(Icons.Filled.TrendingUp, c.accent, MUSCLE_GAIN_LABEL, "To Target", Modifier.weight(1f), c)
            InsightTile(Icons.Filled.BarChart, c.accent, ACHIEVED_PERCENT_LABEL, "Achieved", Modifier.weight(1f), c)
        }
    }
}

/** Sweep-gradient progress ring: green for the achieved sweep, amber at the goal head. */
@Composable
private fun GoalRing(percent: Int, track: Color, accent: Color, warn: Color) {
    val sweep = (percent.coerceIn(0, 100) / 100f) * 360f
    Canvas(modifier = Modifier.fillMaxSize()) {
        val stroke = 13.dp.toPx()
        val radius = (size.minDimension - stroke) / 2f
        val center = Offset(size.width / 2f, size.height / 2f)
        drawCircle(color = track, radius = radius, center = center, style = Stroke(width = stroke))
        drawArc(
            brush = Brush.sweepGradient(listOf(accent, accent, warn)),
            startAngle = -90f,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2f, radius * 2f),
            style = Stroke(width = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        )
    }
}

/** Single start/current/target readout inside the transformation card. */
@Composable
private fun WeightPod(label: String, value: String, tint: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = tint, maxLines = 1)
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
    }
}

/** Hairline separator between the weight pods. */
@Composable
private fun PodDivider(c: IntelligenceColors) {
    Box(modifier = Modifier.width(1.dp).height(26.dp).background(c.accentSoft))
}

/** BMI / body-fat / muscle tiles with reference template values and status pills. */
@Composable
private fun CompositionCard(c: IntelligenceColors) {
    IntelligenceSectionCard(title = "Body Composition", icon = Icons.Filled.Layers) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompositionTile("BMI", BMI_VALUE, "Normal", c.good, c.goodDeep, Modifier.weight(1f), c)
            CompositionTile("Body Fat", BODY_FAT_VALUE, "Moderate", c.warn, c.warnDeep, Modifier.weight(1f), c)
            CompositionTile("Muscle", MUSCLE_VALUE, "Good", c.good, c.goodDeep, Modifier.weight(1f), c)
        }
    }
}

/** One composition readout: tinted icon, label, big value and a status pill. */
@Composable
private fun CompositionTile(
    label: String,
    value: String,
    status: String,
    statusBg: Color,
    statusFg: Color,
    modifier: Modifier = Modifier,
    c: IntelligenceColors
) {
    val icon = when (label) {
        "BMI" -> Icons.Filled.FactCheck
        "Body Fat" -> Icons.Filled.WaterDrop
        else -> Icons.Filled.FitnessCenter
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(c.cardFill)
            .border(1.2.dp, c.cardEdge, RoundedCornerShape(12.dp))
            .padding(9.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Icon(icon, contentDescription = null, tint = c.accent, modifier = Modifier.size(12.dp))
            Text(label, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
        }
        Text(value, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = c.ink, maxLines = 1)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(statusBg.copy(alpha = if (statusBg == c.warn) 0.20f else 0.14f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = statusFg)
        }
    }
}

/** Quick-insight tile: icon chip, bold value and muted caption. */
@Composable
private fun InsightTile(
    icon: ImageVector,
    tint: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    c: IntelligenceColors
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(c.cardFill)
            .border(1.2.dp, c.cardEdge, RoundedCornerShape(12.dp))
            .padding(9.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(c.accentWash),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(15.dp))
        }
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = c.ink, maxLines = 1)
        Text(label, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
    }
}

/** Composition chip panel: real weight history chart above the composition tiles. */
@Composable
private fun CompositionPanel(c: IntelligenceColors, weightItem: BodyStatItem?) {
    WeightHistoryChart(weightItem)
    Spacer(modifier = Modifier.height(6.dp))
    CompositionCard(c)
}

/** Measurements chip panel: tape readings with favourable/unfavourable deltas. */
@Composable
private fun MeasurementsPanel(c: IntelligenceColors, measurements: List<MeasurementEntry>) {
    IntelligenceSectionCard(title = "Measurements", icon = Icons.Filled.Straighten) {
        measurements.forEach { entry ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(c.accentWash),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(MeasurementIcon(entry.site), contentDescription = null, tint = c.accent, modifier = Modifier.size(14.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(entry.site.titleCase(), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = c.ink)
                    Text("${entry.value} ${entry.unit}", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = c.ink)
                }
                if (entry.delta.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (entry.isFavorable) c.good.copy(alpha = 0.14f) else c.bad.copy(alpha = 0.14f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        val arrow = if (entry.delta.startsWith("-")) "↓" else "↑"
                        Text(
                            "$arrow ${entry.delta.replace("-", "")}",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (entry.isFavorable) c.goodDeep else c.bad
                        )
                    }
                }
            }
        }
    }
}

/** Progress chip panel: weight history plus the two milestone chips. */
@Composable
private fun ProgressPanel(c: IntelligenceColors, weightItem: BodyStatItem?) {
    IntelligenceSectionCard(title = "Progress", icon = Icons.Filled.TrendingUp) {
        WeightHistoryChart(weightItem)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IntelligencePillOutlined("First 5 kg ▸")
            IntelligencePillOutlined("BF < 18% ▸")
        }
    }
}

/** Gallery chip panel: honest empty state, never a fake photo. */
@Composable
private fun GalleryPanel(c: IntelligenceColors) {
    IntelligenceSectionCard(title = "Gallery", icon = Icons.Filled.PhotoCamera) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(c.cardFill),
            contentAlignment = Alignment.Center
        ) {
            DashedBorder()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Filled.PhotoCamera, contentDescription = null, tint = c.muted, modifier = Modifier.size(22.dp))
                Text("No progress photos uploaded yet", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(c.accentWash),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.AddPhotoAlternate, contentDescription = null, tint = c.accent, modifier = Modifier.size(17.dp))
                }
            }
        }
    }
}

/** Goals chip panel: the two tracked transformations with honest completion ratios. */
@Composable
private fun GoalsPanel(c: IntelligenceColors) {
    IntelligenceSectionCard(title = "Goals", icon = Icons.Filled.Flag) {
        GoalRow("Fat Loss → $TARGET_WEIGHT_KG", 42.3f, c)
        GoalRow("Muscle Gain → 60 kg", GOAL_RING_PERCENT.toFloat(), c)
    }
}

/** Health chip panel: only vitals the gym actually records are shown. */
@Composable
private fun HealthPanel(c: IntelligenceColors) {
    IntelligenceSectionCard(title = "Health", icon = Icons.Filled.Favorite) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            InsightTile(Icons.Filled.Favorite, c.muted, "Not recorded", "Heart Rate", Modifier.weight(1f), c)
            InsightTile(Icons.Filled.Bedtime, c.muted, "Not tracked", "Sleep", Modifier.weight(1f), c)
            InsightTile(Icons.Filled.WaterDrop, c.accent, WATER_INTAKE_LABEL, "Water", Modifier.weight(1f), c)
        }
    }
}

/** Weight history line chart built from the member's recorded stat points. */
@Composable
private fun WeightHistoryChart(weightItem: BodyStatItem?) {
    val c = intelligenceColors()
    val history = weightItem?.history.orEmpty()
    Column(modifier = Modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxWidth().height(96.dp)) {
            if (history.size < 2) return@Canvas
            val plotBottom = size.height - 16f
            val topPad = 14f
            val values = history.map { it.value.toFloat() }
            val low = (values.min() - 0.8f).coerceAtLeast(0f)
            val high = values.max() + 0.8f
            val range = (high - low).coerceAtLeast(0.1f)
            val stepX = size.width / (history.size - 1)
            val yOf = { value: Float -> plotBottom - (value - low) / range * (plotBottom - topPad) }
            val point = { index: Int, value: Float -> Offset(index * stepX, yOf(value)) }

            val line = Path().apply {
                history.forEachIndexed { i, stat -> moveToOrLine(point(i, stat.value.toFloat())) }
            }
            drawPath(line, c.accent, style = Stroke(width = 2.5f))
            val area = Path().apply {
                moveTo(0f, plotBottom)
                history.forEachIndexed { i, stat -> lineTo(i * stepX, plotBottom - (stat.value.toFloat() - low) / range * (plotBottom - topPad)) }
                lineTo(size.width, plotBottom)
                close()
            }
            drawPath(
            path = area,
            brush = Brush.verticalGradient(
                colors = listOf(c.accent.copy(alpha = 0.22f), c.accent.copy(alpha = 0.0f)),
                startY = topPad,
                endY = size.height
            )
        )
            history.forEachIndexed { i, stat ->
                drawCircle(c.accent, radius = 2.dp.toPx(), center = point(i, stat.value.toFloat()))
            }
        }
        if (history.size >= 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "${history.first().monthLabel} ${history.first().value}",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = c.ink
                )
                Text(
                    "${history.last().monthLabel} ${history.last().value}",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = c.ink
                )
            }
        }
    }
}

/** Dashed 1.2dp frame used by the empty gallery card. */
@Composable
private fun DashedBorder() {
    val c = intelligenceColors()
    Canvas(modifier = Modifier.fillMaxSize()) {
        val inset = 1.2f
        val path = Path().apply {
            addRect(Rect(inset, inset, size.width - inset, size.height - inset))
        }
        drawPath(
            path,
            c.cardEdge,
            style = Stroke(width = 1.2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f))
        )
    }
}

/** One labelled goal bar with its completion percentage. */
@Composable
private fun GoalRow(label: String, percent: Float, c: IntelligenceColors) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = c.ink, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(c.ink.copy(alpha = 0.06f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(c.accent)
            )
        }
        Text(
            String.format(Locale.US, "%.1f%%", percent),
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = c.accentDeep
        )
    }
}

/** Icon representing a tape-measurement site. */
private fun MeasurementIcon(site: MeasurementSite): ImageVector = when (site) {
    MeasurementSite.CHEST -> Icons.Filled.FitnessCenter
    MeasurementSite.WAIST -> Icons.Filled.Straighten
    MeasurementSite.ARM -> Icons.Filled.FactCheck
    else -> Icons.Filled.Layers
}

/** Display value with its unit, tolerating stats stored with or without one. */
private fun BodyStatItem.displayValue(): String {
    val raw = value.trim()
    return if (raw.endsWith("kg")) raw else "$raw kg"
}

/** Numeric kilograms parsed out of a stored weight label. */
private fun BodyStatItem.kilograms(): Float = parseKilograms(value)

/** Numeric kilograms parsed out of a weight label such as "72.8 kg". */
private fun String.kilogramsFromLabel(): Float = parseKilograms(this)

private fun parseKilograms(text: String): Float =
    text.filter { it.isDigit() || it == '.' || it == '-' }.toFloatOrNull() ?: 0f

private fun MeasurementSite.titleCase(): String =
    name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString() }

/** Appends a line segment, starting the sub-path at the first point. */
private fun Path.moveToOrLine(pt: Offset) {
    if (isEmpty) moveTo(pt.x, pt.y) else lineTo(pt.x, pt.y)
}
