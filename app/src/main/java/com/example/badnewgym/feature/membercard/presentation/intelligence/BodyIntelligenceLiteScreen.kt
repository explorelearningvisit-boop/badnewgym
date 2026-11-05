package com.example.badnewgym.feature.membercard.presentation.intelligence

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.membercard.domain.model.BadGymMemberPass
import com.example.badnewgym.feature.membercard.domain.model.BodyStatItem
import com.example.badnewgym.feature.membercard.domain.model.BodyStatType
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData
import com.example.badnewgym.feature.membercard.domain.model.StatPoint
import java.util.Locale

/** Reference template value: programme start weight in kilograms. */
private const val START_WEIGHT_KG = 72.8

/** Reference template value: goal body weight in kilograms. */
private const val TARGET_WEIGHT_KG = 65.0

/** Reference template value: transformation goal ring completion percent. */
private const val GOAL_RING_PERCENT = 56

/** Reference template value: overall transformation achievement percent. */
private const val ACHIEVED_PERCENT = 42.3

/** Reference template value: muscle gained since programme start in kilograms. */
private const val MUSCLE_GAIN_KG = 4.5

/** Tab labels for the body chip row; the last entry is the "More" action chip. */
private val BODY_TABS = listOf("Overview", "Composition", "Measurements", "Progress", "Gallery", "Goals", "Health", "More")

/** Index of the "More" action chip inside [BODY_TABS]. */
private const val MORE_TAB_INDEX = 7

/** Body intelligence lite dashboard: section card, "More" trailing chip, eight filter chips and seven tab bodies. */
@Composable
fun BodyIntelligenceLiteScreen(
    data: MemberCardData,
    pass: BadGymMemberPass,
    onMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    IntelligenceSectionCard(
        title = "Body Intelligence",
        icon = Icons.Filled.Accessibility,
        modifier = modifier,
        subtitle = "Member: ${pass.name}",
        trailing = {
            Box(
                Modifier
                    .clip(RoundedCornerShape(50))
                    .background(c.accentWash)
                    .border(1.dp, c.accentSoft, RoundedCornerShape(50))
                    .clickable { onMoreClick() }
                    .padding(horizontal = 9.dp, vertical = 4.dp)
            ) {
                Text(
                    "⚡ More ▸",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = c.accentDeep,
                    maxLines = 1
                )
            }
        }
    ) {
        IntelligenceChipRow(
            chips = BODY_TABS,
            selected = selectedTab,
            onSelect = { index ->
                selectedTab = index
                if (index == MORE_TAB_INDEX) onMoreClick()
            }
        )
        when (selectedTab) {
            0 -> OverviewTab(data)
            1 -> CompositionTab(data)
            2 -> MeasurementsTab(data)
            3 -> ProgressTab(data)
            4 -> GalleryTab()
            5 -> GoalsTab(data)
            6 -> HealthTab()
        }
    }
}

/** Overview body: transformation goal ring card, body composition tiles and quick insight pills. */
@Composable
private fun OverviewTab(data: MemberCardData) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TransformationCard(data)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SectionLabel("Body Composition")
            CompositionTiles(data)
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SectionLabel("Quick Insights")
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val weight = weightStat(data)
                val loss = weight?.let { numericValue(it.value) }?.let { START_WEIGHT_KG - it }
                InsightChip(if (loss != null) "▼ ${fmt1(loss)} kg Weight" else "▼ — kg Weight", intelligenceColors().bad)
                InsightChip("▲ ${fmt1(MUSCLE_GAIN_KG)} kg Muscle", intelligenceColors().good)
                InsightChip("◎ ${fmt1(ACHIEVED_PERCENT)}% Achieved", intelligenceColors().accent)
            }
        }
    }
}

/** Transformation goal card: title, sub line, 52dp goal ring and the three weight pods. */
@Composable
private fun TransformationCard(data: MemberCardData) {
    val c = intelligenceColors()
    val weight = weightStat(data)
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(c.faint.copy(alpha = 0.08f))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "// Transformation Goal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = c.ink,
                        letterSpacing = (-0.2).sp
                    )
                    Text(
                        "Fat Loss → Target ${fmt1(TARGET_WEIGHT_KG)} kg",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = c.muted,
                        maxLines = 1
                    )
                }
                GoalRing()
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WeightPod("Start", "${fmt1(START_WEIGHT_KG)} kg", isCurrent = false, showTemplateTag = true, modifier = Modifier.weight(1f))
                WeightPod("Current", weight?.let { displayValue(it) } ?: "—", isCurrent = true, showTemplateTag = false, modifier = Modifier.weight(1f))
                WeightPod("Target", "${fmt1(TARGET_WEIGHT_KG)} kg", isCurrent = false, showTemplateTag = false, modifier = Modifier.weight(1f))
            }
        }
    }
}

/** 52dp Canvas progress ring with the template goal percent centered. */
@Composable
private fun GoalRing() {
    val c = intelligenceColors()
    Box(modifier = Modifier.size(52.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 6.dp.toPx()
            val diameter = size.minDimension - stroke
            val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
            val arcSize = Size(diameter, diameter)
            drawArc(
                color = c.accentSoft,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                color = c.accent,
                startAngle = -90f,
                sweepAngle = 360f * GOAL_RING_PERCENT / 100f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Text("$GOAL_RING_PERCENT%", fontSize = 11.sp, fontWeight = FontWeight.Black, color = c.ink)
    }
}

/** One weight pod; current is accent+bold and the start pod carries the template tag. */
@Composable
private fun WeightPod(
    label: String,
    value: String,
    isCurrent: Boolean,
    showTemplateTag: Boolean,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    Column(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(c.cardFill)
            .border(1.dp, c.cardEdge, RoundedCornerShape(10.dp))
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Text(label, fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
        Text(
            value,
            fontSize = 12.sp,
            fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
            color = if (isCurrent) c.accent else c.ink,
            maxLines = 1
        )
        Text(if (showTemplateTag) "template" else "", fontSize = 7.5.sp, fontWeight = FontWeight.Medium, color = c.muted, maxLines = 1)
    }
}

/** BMI / Body Fat / Muscle tiles built from the real body stats with derived status words. */
@Composable
private fun CompositionTiles(data: MemberCardData) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CompositionTile("BMI", statOf(data, BodyStatType.BMI), Modifier.weight(1f))
        CompositionTile("Body Fat", statOf(data, BodyStatType.BODY_FAT), Modifier.weight(1f))
        CompositionTile("Muscle", statOf(data, BodyStatType.MUSCLE), Modifier.weight(1f))
    }
}

/** One composition tile: real stat value plus derived status word and tone dot. */
@Composable
private fun CompositionTile(title: String, stat: BodyStatItem?, modifier: Modifier = Modifier) {
    val c = intelligenceColors()
    val value = stat?.let { numericValue(it.value) }
    val (status, tone) = statusFor(stat?.type, value, c)
    Column(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(c.faint.copy(alpha = 0.08f))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(title, fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
        Text(
            stat?.let { displayValue(it) } ?: "—",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Black,
            color = c.ink,
            maxLines = 1
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Box(Modifier.size(6.dp).clip(CircleShape).background(tone))
            Text(status, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = tone, maxLines = 1)
        }
    }
}

/** Small tone-colored quick insight pill. */
@Composable
private fun InsightChip(text: String, tone: Color) {
    val c = intelligenceColors()
    Box(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(tone.copy(alpha = 0.12f))
            .padding(horizontal = 9.dp, vertical = 5.dp)
    ) {
        Text(text, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = tone, maxLines = 1)
    }
}

/** Composition body: single-series weight history line chart plus the three composition tiles. */
@Composable
private fun CompositionTab(data: MemberCardData) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        WeightHistoryChart(weightHistory(data))
        CompositionTiles(data)
    }
}

/** Progress body: the weight history line chart again plus two muted milestone chips. */
@Composable
private fun ProgressTab(data: MemberCardData) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        WeightHistoryChart(weightHistory(data))
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MilestoneChip("First 5 kg ▸")
            MilestoneChip("BF < 18% ▸")
        }
    }
}

/** Single-series Canvas line chart of the weight history with first/last value labels. */
@Composable
private fun WeightHistoryChart(history: List<StatPoint>) {
    val c = intelligenceColors()
    val textMeasurer = rememberTextMeasurer()
    if (history.size < 2) {
        Box(Modifier.fillMaxWidth().height(110.dp), contentAlignment = Alignment.Center) {
            Text("No weight history recorded yet", fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
        }
        return
    }
    Canvas(Modifier.fillMaxWidth().height(110.dp)) {
        val padH = 14.dp.toPx()
        val topPad = 20.dp.toPx()
        val bottomPad = 20.dp.toPx()
        val plotW = (size.width - padH * 2).coerceAtLeast(1f)
        val plotH = (size.height - topPad - bottomPad).coerceAtLeast(1f)
        val values = history.map { it.value }
        val lo = values.minOrNull() ?: 0.0
        val hi = values.maxOrNull() ?: 1.0
        val span = (hi - lo).takeIf { it > 0.0 } ?: 1.0
        val stepX = plotW / (history.size - 1)
        val points = history.mapIndexed { index, point ->
            val frac = ((point.value - lo) / span).toFloat()
            Offset(padH + stepX * index, topPad + plotH * (1f - frac))
        }
        val linePath = Path().apply {
            points.forEachIndexed { index, p -> if (index == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
        }
        val areaPath = Path().apply {
            addPath(linePath)
            lineTo(points.last().x, topPad + plotH)
            lineTo(points.first().x, topPad + plotH)
            close()
        }
        drawPath(areaPath, c.accentWash)
        drawPath(linePath, c.accent, style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round))
        points.forEach { drawCircle(c.accent, 2.5.dp.toPx(), it) }
        listOf(points.first(), points.last()).forEach { p ->
            drawCircle(c.accent, 4.dp.toPx(), p)
            drawCircle(Color.White, 1.6.dp.toPx(), p)
        }
        val firstLabel = textMeasurer.measure(
            fmt1(history.first().value),
            TextStyle(fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = c.inkSoft)
        )
        val lastLabel = textMeasurer.measure(
            fmt1(history.last().value),
            TextStyle(fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = c.ink)
        )
        drawText(
            firstLabel,
            topLeft = Offset(
                (points.first().x - firstLabel.size.width / 2f).coerceIn(2f, size.width - firstLabel.size.width - 2f),
                points.first().y + 5.dp.toPx()
            )
        )
        drawText(
            lastLabel,
            topLeft = Offset(
                (points.last().x - lastLabel.size.width / 2f).coerceIn(2f, size.width - lastLabel.size.width - 2f),
                points.last().y - lastLabel.size.height - 4.dp.toPx()
            )
        )
    }
}

/** Muted milestone pill. */
@Composable
private fun MilestoneChip(text: String) {
    val c = intelligenceColors()
    Box(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(c.ink.copy(alpha = 0.05f))
            .border(1.dp, c.cardEdge, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = c.inkSoft, maxLines = 1)
    }
}

/** Measurements body: list tiles from data.measurements with favorable-toned deltas. */
@Composable
private fun MeasurementsTab(data: MemberCardData) {
    val c = intelligenceColors()
    if (data.measurements.isEmpty()) {
        EmptyLine("No tape measurements recorded yet")
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        data.measurements.forEach { entry ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(c.faint.copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    entry.site.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = c.ink,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                Text(
                    "${fmt1(entry.value)} ${entry.unit}".trim(),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    color = c.ink,
                    maxLines = 1
                )
                Text(
                    if (entry.delta.isBlank()) "—" else entry.delta,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (entry.isFavorable) c.good else c.bad,
                    maxLines = 1
                )
            }
        }
    }
}

/** Gallery body: honest empty state card plus a dashed add-progress-photo tile. */
@Composable
private fun GalleryTab() {
    val c = intelligenceColors()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(c.faint.copy(alpha = 0.08f))
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Filled.PhotoLibrary, contentDescription = null, tint = c.faint, modifier = Modifier.size(26.dp))
                Text("No progress photos uploaded yet", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = c.inkSoft)
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = c.faint,
                        cornerRadius = CornerRadius(10.dp.toPx()),
                        style = Stroke(
                            width = 1.2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(7.dp.toPx(), 5.dp.toPx()), 0f)
                        )
                    )
                }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Filled.AddPhotoAlternate, contentDescription = null, tint = c.muted, modifier = Modifier.size(14.dp))
                Text("Add Progress Photo", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = c.inkSoft)
            }
        }
    }
}

/** Goals body: two template goal rows with progress bars and gold celebration when complete. */
@Composable
private fun GoalsTab(data: MemberCardData) {
    val muscleGoal = statOf(data, BodyStatType.MUSCLE)?.goalText
        ?.removePrefix("Goal: ")?.trim()?.takeUnless { it.isEmpty() } ?: "60 kg"
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        GoalRow("Fat Loss → ${fmt1(TARGET_WEIGHT_KG)} kg", ACHIEVED_PERCENT.toFloat(), "${fmt1(ACHIEVED_PERCENT)}%")
        GoalRow("Muscle Gain → $muscleGoal", GOAL_RING_PERCENT.toFloat(), "$GOAL_RING_PERCENT%")
    }
}

/** One goal row: title, percent and a rounded progress bar (gold chip when 100%+). */
@Composable
private fun GoalRow(title: String, percent: Float, label: String) {
    val c = intelligenceColors()
    val achieved = percent >= 100f
    val fill = if (achieved) c.warn else c.accent
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = c.ink, modifier = Modifier.weight(1f), maxLines = 1)
            if (achieved) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(50))
                        .background(c.warn.copy(alpha = 0.15f))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text("Goal Achieved", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = c.warn, maxLines = 1)
                }
            }
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Black, color = fill, maxLines = 1)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(RoundedCornerShape(50))
                .background(c.ink.copy(alpha = 0.07f))
        ) {
            Box(
                Modifier
                    .fillMaxWidth((percent / 100f).coerceIn(0.02f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(fill)
            )
        }
    }
}

/** Health body: honest muted vitals plus the template water reminder tile. */
@Composable
private fun HealthTab() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        HealthTile(Icons.Filled.Favorite, "Heart Rate", "Not recorded", null, Modifier.weight(1f))
        HealthTile(Icons.Filled.Bedtime, "Sleep", "Not tracked", null, Modifier.weight(1f))
        HealthTile(Icons.Filled.WaterDrop, "Water", "2.5 L", "template", Modifier.weight(1f))
    }
}

/** One health tile; accent value when it is a template reminder, muted when honestly untracked. */
@Composable
private fun HealthTile(
    icon: ImageVector,
    label: String,
    value: String,
    tag: String?,
    modifier: Modifier = Modifier
) {
    val c = intelligenceColors()
    Column(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(c.faint.copy(alpha = 0.08f))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(icon, contentDescription = null, tint = if (tag == null) c.muted else c.accent, modifier = Modifier.size(14.dp))
        Text(label, fontSize = 8.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted, maxLines = 1)
        Text(value, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = if (tag == null) c.inkSoft else c.accent, maxLines = 1)
        if (tag != null) {
            Text(tag, fontSize = 7.5.sp, fontWeight = FontWeight.Medium, color = c.muted, maxLines = 1)
        }
    }
}

/** Small bold section label used inside the overview tab. */
@Composable
private fun SectionLabel(text: String) {
    val c = intelligenceColors()
    Text(text, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = c.inkSoft, letterSpacing = (-0.2).sp)
}

/** Single muted honesty line for empty datasets. */
@Composable
private fun EmptyLine(text: String) {
    val c = intelligenceColors()
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(c.faint.copy(alpha = 0.08f))
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = c.muted)
    }
}

/** Weight stat lookup: typed WEIGHT entry with fallback to the first body stat. */
private fun weightStat(data: MemberCardData): BodyStatItem? =
    data.bodyStats.firstOrNull { it.type == BodyStatType.WEIGHT } ?: data.bodyStats.firstOrNull()

/** Typed body stat lookup by [BodyStatType]. */
private fun statOf(data: MemberCardData, type: BodyStatType): BodyStatItem? =
    data.bodyStats.firstOrNull { it.type == type }

/** Weight history series used by the composition and progress charts. */
private fun weightHistory(data: MemberCardData): List<StatPoint> = weightStat(data)?.history ?: emptyList()

/** Joins a stat value with its unit when the unit is not already embedded. */
private fun displayValue(stat: BodyStatItem): String =
    if (stat.unit.isBlank() || stat.value.contains(stat.unit)) stat.value else "${stat.value} ${stat.unit}"

/** Extracts the numeric reading from a display string like "69.5 kg". */
private fun numericValue(raw: String): Double? =
    raw.filter { it.isDigit() || it == '.' }.toDoubleOrNull()

/** Derives the honest status word and tone for a composition stat reading. */
private fun statusFor(type: BodyStatType?, value: Double?, c: IntelligenceColors): Pair<String, Color> = when (type) {
    BodyStatType.BMI -> when {
        value == null -> "—" to c.muted
        value < 18.5 -> "Under" to c.warn
        value <= 24.9 -> "Normal" to c.good
        value <= 29.9 -> "Over" to c.warn
        else -> "High" to c.bad
    }
    BodyStatType.BODY_FAT -> when {
        value == null -> "—" to c.muted
        value <= 15.0 -> "Good" to c.good
        value <= 20.0 -> "Moderate" to c.warn
        else -> "High" to c.bad
    }
    BodyStatType.MUSCLE -> when {
        value == null -> "—" to c.muted
        value >= 57.0 -> "Good" to c.good
        value >= 52.0 -> "Moderate" to c.warn
        else -> "Low" to c.bad
    }
    else -> "—" to c.muted
}

/** One-decimal, locale-stable formatting for kilogram and percent readings. */
private fun fmt1(value: Double): String = "%.1f".format(Locale.US, value)
