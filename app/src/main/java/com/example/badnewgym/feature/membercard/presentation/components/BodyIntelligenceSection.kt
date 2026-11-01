package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.R
import com.example.badnewgym.feature.membercard.domain.model.MemberCardData

// =============================================================================
// BODY INTELLIGENCE
// Reading-first surface: every label clears 10sp, values sit in the 13-20sp band,
// cards use soft neumorphic depth over translucent glass fills.
// =============================================================================

private val BiInk = Color(0xFF0B1220)
private val BiInk2 = Color(0xFF2B3A4F)
private val BiMuted = Color(0xFF64748B)
private val BiFaint = Color(0xFF93A2B5)
private val BiLine = Color(0xFFE3EAF2)
private val BiPanel = Color(0xFFF7FAFC)
private val BiGreen = Color(0xFF10B981)
private val BiGreenDeep = Color(0xFF047857)
private val BiGreenWash = Color(0xFFECFDF5)
private val BiGreenEdge = Color(0xFFA7F3D0)
private val BiBlue = Color(0xFF0C7CD5)
private val BiViolet = Color(0xFF6D28D9)
private val BiAmber = Color(0xFFD97706)
private val BiRed = Color(0xFFDC2626)

private val BiShape = RoundedCornerShape(18.dp)

private val BiViewLabels = listOf(
    "Overview", "Body", "Strength", "Muscle", "Activity", "Tests", "Plan", "Insights"
)
private val BiViewIcons = listOf(
    Icons.Filled.BarChart,
    Icons.Filled.TrackChanges,
    Icons.Filled.FitnessCenter,
    Icons.Filled.Bolt,
    Icons.Filled.DirectionsRun,
    Icons.Filled.Timer,
    Icons.Filled.DateRange,
    Icons.Filled.Stars
)

@Composable
fun BodyIntelligenceSection(
    data: MemberCardData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedView by remember { mutableIntStateOf(0) }
    var metricIndex by remember { mutableIntStateOf(0) }
    var rangeIndex by remember { mutableIntStateOf(3) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BiHeaderBar(selected = selectedView, onSelect = { selectedView = it })

        when (selectedView) {
            0 -> {
                BiTransformationJourneyCard()
                BiCompositionCard(
                    data = data,
                    metricIndex = metricIndex,
                    onMetric = { metricIndex = it },
                    rangeIndex = rangeIndex,
                    onRange = { rangeIndex = it }
                )
                BiStrengthCard()
                BiMuscleFocusCard()
                BiActivityCard()
                BiAssessmentCard()
                BiInsightBanner(onClick = onClick)
            }

            1 -> BiCompositionCard(
                data = data,
                metricIndex = metricIndex,
                onMetric = { metricIndex = it },
                rangeIndex = rangeIndex,
                onRange = { rangeIndex = it }
            )

            2 -> BiStrengthCard()
            3 -> BiMuscleFocusCard()
            4 -> BiActivityCard()
            5 -> BiAssessmentCard()
            6 -> BiTransformationJourneyCard()
            else -> BiInsightBanner(onClick = onClick)
        }
    }
}

// -----------------------------------------------------------------------------
// HEADER
// -----------------------------------------------------------------------------

@Composable
private fun BiHeaderBar(selected: Int, onSelect: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .shadow(10.dp, RoundedCornerShape(14.dp), ambientColor = BiGreen, spotColor = BiGreen)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF2DD4BF), Color(0xFF059669)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.FitnessCenter,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Body Intelligence",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = BiInk,
                        letterSpacing = (-0.4).sp
                    )
                    Spacer(Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFFEDE9FE),
                        border = BorderStroke(0.8.dp, Color(0xFFC4B5FD))
                    ) {
                        Text(
                            text = "PRO",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = BiViolet,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "Complete fitness, performance & transformation",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = BiMuted,
                    maxLines = 1
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BiViewLabels.forEachIndexed { index, label ->
                val active = index == selected
                val bg by androidx.compose.animation.animateColorAsState(
                    targetValue = if (active) Color.White else Color.White.copy(alpha = 0.55f),
                    animationSpec = tween(220),
                    label = "biNavBg"
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(bg)
                        .border(
                            width = 1.dp,
                            brush = if (active) {
                                Brush.horizontalGradient(listOf(BiGreen, Color(0xFF34D399)))
                            } else {
                                Brush.horizontalGradient(listOf(BiLine, BiLine))
                            },
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable { onSelect(index) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = BiViewIcons[index],
                        contentDescription = label,
                        tint = if (active) BiGreenDeep else BiFaint,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (active) FontWeight.Black else FontWeight.SemiBold,
                        color = if (active) BiGreenDeep else BiMuted,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// TRANSFORMATION JOURNEY
// -----------------------------------------------------------------------------

@Composable
private fun BiTransformationJourneyCard(modifier: Modifier = Modifier) {
    val progress by animateFloatAsState(
        targetValue = 0.73f,
        animationSpec = tween(1100, easing = FastOutSlowInEasing),
        label = "journeyProgress"
    )

    Surface(
        shape = BiShape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, BiGreenEdge),
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, BiShape, ambientColor = Color(0xFF34D399), spotColor = Color(0xFF34D399))
            .clip(BiShape)
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFF0FDF4), Color(0xFFE7FBF3), Color(0xFFF7FEFB))
                )
            )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFE6FBF1), Color(0xFFD6F5E7))
                        )
                    )
                    .border(1.dp, Color.White, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.illustration_3d_mannequin),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Transformation Journey",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = BiInk
                    )
                    Spacer(Modifier.weight(1f))
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White,
                        border = BorderStroke(0.9.dp, BiGreenEdge)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "View Plan",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BiGreenDeep
                            )
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = null,
                                tint = BiGreenDeep,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = BiGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Goal: Fat Loss",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BiInk2,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFFDCFCE7),
                        border = BorderStroke(0.8.dp, Color(0xFF86EFAC)),
                        modifier = Modifier.padding(start = 6.dp)
                    ) {
                        Text(
                            text = "On Track",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF15803D),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(9.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD7F5E4))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .height(9.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF34D399), Color(0xFF059669))
                                    )
                                )
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "73%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = BiGreenDeep
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = BiFaint,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = " 09 Oct 2026",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BiMuted
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Filled.TrackChanges,
                        contentDescription = null,
                        tint = BiFaint,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = " 07 Nov 2026",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BiMuted
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// BODY COMPOSITION
// -----------------------------------------------------------------------------

private data class BiMetric(
    val label: String,
    val unit: String,
    val points: List<Float>,
    val goal: Float?,
    val color: Color,
    val scaleMin: Float,
    val scaleMax: Float,
    val tickStep: Float,
    val change: String,
    val format: (Float) -> String
)

private val BiMetrics = listOf(
    BiMetric("Weight", "kg", listOf(72.0f, 71.3f, 70.6f, 70.1f, 69.7f, 69.5f), 66f, BiGreen, 64f, 74f, 2.5f, "−2.5 kg") { "%.1f".format(it) },
    BiMetric("Muscle", "kg", listOf(55.8f, 56.1f, 56.4f, 56.8f, 57.0f, 57.2f), 60f, BiViolet, 54f, 60f, 1.5f, "+1.4 kg") { "%.1f".format(it) },
    BiMetric("Body Fat", "%", listOf(18.8f, 18.5f, 18.2f, 18.0f, 17.8f, 17.6f), 15f, BiRed, 14f, 20f, 1.5f, "−1.2%") { "%.1f".format(it) },
    BiMetric("Fat Mass", "kg", listOf(13.5f, 13.1f, 12.8f, 12.6f, 12.4f, 12.2f), 11f, BiAmber, 10f, 14f, 1f, "−1.3 kg") { "%.1f".format(it) },
    BiMetric("Lean Mass", "kg", listOf(51.0f, 51.6f, 52.2f, 52.7f, 53.1f, 53.4f), 56f, BiBlue, 50f, 57f, 2f, "+2.4 kg") { "%.1f".format(it) }
)

private val BiMonths = listOf("May", "Jun", "Jul", "Aug", "Sep", "Oct")
private val BiRanges = listOf("7D", "30D", "90D", "6M", "1Y")

@Composable
private fun BiCompositionCard(
    data: MemberCardData,
    metricIndex: Int,
    onMetric: (Int) -> Unit,
    rangeIndex: Int,
    onRange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val metric = BiMetrics[metricIndex.coerceIn(0, BiMetrics.lastIndex)]

    BiBlock(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BiBlockIcon(Icons.Filled.TrackChanges, BiGreenDeep, Color(0xFFD9F7E8))
            Spacer(Modifier.width(7.dp))
            Text(
                text = "Body Composition",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = BiInk
            )
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                tint = Color(0xFFC3CEDC),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(12.dp)
            )
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                BiRanges.forEachIndexed { index, label ->
                    val active = index == rangeIndex
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (active) FontWeight.Black else FontWeight.SemiBold,
                        color = if (active) Color.White else BiMuted,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (active) BiGreenDeep else Color(0xFFF1F5F9))
                            .clickable { onRange(index) }
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BiMetrics.forEachIndexed { index, item ->
                val active = index == metricIndex
                Text(
                    text = item.label,
                    fontSize = 11.sp,
                    fontWeight = if (active) FontWeight.Black else FontWeight.SemiBold,
                    color = if (active) Color.White else BiInk2,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (active) item.color else Color(0xFFEFF3F8))
                        .clickable { onMetric(index) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        BiCompositionChart(metric = metric)

        Text(
            text = "Key changes",
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = BiMuted,
            letterSpacing = 0.4.sp
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            BiChangeCell(
                icon = Icons.Filled.FitnessCenter,
                tint = BiGreen,
                label = "Weight",
                value = "${data.bodyStats.firstOrNull()?.value ?: "69.5"} kg",
                delta = "−2.5 kg",
                modifier = Modifier.weight(1f)
            )
            BiChangeCell(
                icon = Icons.Filled.Bolt,
                tint = BiViolet,
                label = "Muscle",
                value = "${data.bodyStats.getOrNull(1)?.value ?: "57.2"} kg",
                delta = "+1.4 kg",
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            BiChangeCell(
                icon = Icons.Filled.Percent,
                tint = BiRed,
                label = "Body Fat",
                value = "${data.bodyStats.getOrNull(2)?.value ?: "17.6%"}",
                delta = "−1.2%",
                modifier = Modifier.weight(1f)
            )
            BiChangeCell(
                icon = Icons.Filled.Timer,
                tint = BiAmber,
                label = "Waist",
                value = "32.0\"",
                delta = "−1.0\"",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BiChangeCell(
    icon: ImageVector,
    tint: Color,
    label: String,
    value: String,
    delta: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BiPanel,
        border = BorderStroke(0.8.dp, Color(0xFFEDF2F7)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = " $label",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BiMuted,
                    maxLines = 1
                )
            }
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    color = BiInk,
                    maxLines = 1
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = delta,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (delta.startsWith("+")) BiGreenDeep else BiGreenDeep,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun BiCompositionChart(metric: BiMetric) {
    val span = (metric.scaleMax - metric.scaleMin).coerceAtLeast(0.01f)
    var revealed by remember(metric.label) { mutableStateOf(false) }
    LaunchedEffect(metric.label) { revealed = true }
    val reveal by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(850, easing = FastOutSlowInEasing),
        label = "compositionReveal"
    )

    val ticks = buildList {
        var tick = metric.scaleMin
        while (tick <= metric.scaleMax + 0.001f) {
            add(tick)
            tick += metric.tickStep
        }
    }.reversed()

    val plotHeight = 108.dp

    Column {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val plotWidth = maxWidth
            Row {
                Column(
                    modifier = Modifier.height(plotHeight),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    ticks.forEach { tick ->
                        Text(
                            text = metric.format(tick),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BiFaint
                        )
                    }
                }
                Spacer(Modifier.width(6.dp))
                Canvas(
                    modifier = Modifier
                        .width(plotWidth - 30.dp)
                        .height(plotHeight)
                ) {
                    val stepX = size.width / (metric.points.size - 1)
                    fun pointAt(index: Int): Offset {
                        val norm = (metric.points[index] - metric.scaleMin) / span
                        return Offset(index * stepX, size.height - norm * size.height)
                    }

                    ticks.forEach { tick ->
                        val y = size.height - ((tick - metric.scaleMin) / span) * size.height
                        drawLine(
                            color = Color(0xFFEDF1F6),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                    }

                    metric.goal?.let { goal ->
                        if (goal in metric.scaleMin..metric.scaleMax) {
                            val y = size.height - ((goal - metric.scaleMin) / span) * size.height
                            drawLine(
                                color = Color(0xFFC3CEDC),
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 1.6f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(7f, 6f))
                            )
                        }
                    }

                    val points = metric.points.indices.map { pointAt(it) }
                    val line = biSmoothPath(points)
                    val fill = Path().apply {
                        addPath(line)
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }

                    clipRect(right = size.width * reveal) {
                        drawPath(
                            path = fill,
                            brush = Brush.verticalGradient(
                                listOf(
                                    metric.color.copy(alpha = 0.30f),
                                    metric.color.copy(alpha = 0.02f)
                                )
                            )
                        )
                        drawPath(
                            path = line,
                            color = metric.color,
                            style = Stroke(width = 3f, cap = StrokeCap.Round)
                        )
                        points.forEachIndexed { index, point ->
                            val isLast = index == points.lastIndex
                            drawCircle(Color.White, radius = if (isLast) 6f else 4f, center = point)
                            drawCircle(
                                color = metric.color,
                                radius = if (isLast) 4f else 2.6f,
                                center = point
                            )
                        }
                    }
                }
            }

            val lastValue = metric.points.last()
            val fraction = ((lastValue - metric.scaleMin) / span).coerceIn(0f, 1f)
            val bubbleY = ((1f - fraction) * 108f).dp - 13.dp
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = metric.color,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = bubbleY)
            ) {
                Text(
                    text = "${metric.format(lastValue)} ${metric.unit}",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(start = 30.dp)) {
            BiMonths.forEach { month ->
                Text(
                    text = month,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BiFaint,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        metric.goal?.let { goal ->
            Text(
                text = "Goal ${metric.format(goal)} ${metric.unit} · ${metric.change} in 6 months",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = BiMuted,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp)
            )
        }
    }
}

private fun biSmoothPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path
    path.moveTo(points.first().x, points.first().y)
    if (points.size == 1) return path
    for (index in 0 until points.lastIndex) {
        val current = points[index]
        val next = points[index + 1]
        val controlX = (next.x - current.x) * 0.45f
        path.cubicTo(
            current.x + controlX, current.y,
            next.x - controlX, next.y,
            next.x, next.y
        )
    }
    return path
}

// -----------------------------------------------------------------------------
// STRENGTH PROGRESS
// -----------------------------------------------------------------------------

private data class BiLift(
    val muscle: String,
    val name: String,
    val weight: String,
    val reps: String,
    val gain: String,
    val is1RM: String,
    val volume: String,
    val series: List<Float>
)

private val BiLifts = listOf(
    BiLift("Chest", "Bench Press", "80", "8", "+20 kg (33%)", "101 kg", "1,920 kg", listOf(60f, 65f, 70f, 74f, 78f, 80f)),
    BiLift("Legs", "Squat", "100", "6", "+30 kg (43%)", "120 kg", "1,600 kg", listOf(70f, 76f, 82f, 88f, 95f, 100f)),
    BiLift("Back", "Deadlift", "140", "5", "+40 kg (40%)", "162 kg", "2,100 kg", listOf(100f, 108f, 118f, 126f, 134f, 140f)),
    BiLift("Shoulder", "Overhead Press", "52", "8", "+12 kg (30%)", "65 kg", "1,040 kg", listOf(40f, 42f, 45f, 47f, 50f, 52f))
)

private val BiMuscleFilters = listOf("All", "Chest", "Back", "Shoulder", "Arms", "Legs", "Core")

@Composable
private fun BiStrengthCard(modifier: Modifier = Modifier) {
    var filter by remember { mutableIntStateOf(0) }
    val visible = if (filter == 0) BiLifts else BiLifts.filter { it.muscle == BiMuscleFilters[filter] }
    val lift = visible.firstOrNull() ?: BiLifts.first()

    BiBlock(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BiBlockIcon(Icons.Filled.FitnessCenter, BiViolet, Color(0xFFF0EBFF))
            Spacer(Modifier.width(7.dp))
            Text(
                text = "Strength Progress",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = BiInk
            )
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                tint = Color(0xFFC3CEDC),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(12.dp)
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "View all",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = BiViolet
            )
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = BiViolet,
                modifier = Modifier.size(14.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            BiMuscleFilters.forEachIndexed { index, label ->
                val active = index == filter
                Text(
                    text = label,
                    fontSize = 10.5.sp,
                    fontWeight = if (active) FontWeight.Black else FontWeight.SemiBold,
                    color = if (active) Color.White else BiInk2,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (active) BiViolet else Color(0xFFEFF3F8))
                        .clickable { filter = index }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.linearGradient(listOf(Color(0xFFFAF8FF), Color(0xFFF3EFFF)))
                )
                .border(0.9.dp, Color(0xFFE7E0FF), RoundedCornerShape(14.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.9f)) {
                Text(
                    text = lift.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BiInk2,
                    maxLines = 1
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = lift.weight,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Black,
                        color = BiInk,
                        letterSpacing = (-0.6).sp
                    )
                    Text(
                        text = " kg",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BiMuted,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
                Text(
                    text = "× ${lift.reps} reps",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BiMuted
                )
                Spacer(Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFD9F7E8),
                    border = BorderStroke(0.8.dp, Color(0xFFA7F3D0))
                ) {
                    Text(
                        text = "▲ ${lift.gain}",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF047857),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        maxLines = 1
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1.25f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.TrendingUp,
                        contentDescription = null,
                        tint = BiViolet,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = " Strength Trend",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BiMuted,
                        maxLines = 1
                    )
                }
                BiStrengthSparkline(lift = lift, modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier.fillMaxWidth()) {
                    BiMonths.forEach { month ->
                        Text(
                            text = month,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BiFaint,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            BiStrengthStat("Last", "${lift.weight} × ${lift.reps}", Modifier.weight(1f))
            BiStrengthStat("Best", "${lift.weight} × ${lift.reps}", Modifier.weight(1f))
            BiStrengthStat("1RM", lift.is1RM, Modifier.weight(1f))
            BiStrengthStat("Volume", lift.volume, Modifier.weight(1f))
        }
    }
}

@Composable
private fun BiStrengthSparkline(lift: BiLift, modifier: Modifier = Modifier) {
    val min = lift.series.min()
    val max = lift.series.max()
    val span = (max - min).coerceAtLeast(1f)

    BoxWithConstraints(modifier = modifier.height(58.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(58.dp)) {
            val stepX = size.width / (lift.series.size - 1)
            val points = lift.series.mapIndexed { index, value ->
                val norm = (value - min) / span
                Offset(index * stepX, size.height - (0.20f + norm * 0.62f) * size.height)
            }
            val line = biSmoothPath(points)
            val fill = Path().apply {
                addPath(line)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(
                path = fill,
                brush = Brush.verticalGradient(
                    listOf(BiViolet.copy(alpha = 0.26f), BiViolet.copy(alpha = 0.02f))
                )
            )
            drawPath(path = line, color = BiViolet, style = Stroke(width = 2.8f, cap = StrokeCap.Round))
            points.forEachIndexed { index, point ->
                val isLast = index == points.lastIndex
                drawCircle(Color.White, radius = if (isLast) 5f else 3.2f, center = point)
                drawCircle(BiViolet, radius = if (isLast) 3.2f else 2f, center = point)
            }
        }

        val lastFraction = ((lift.series.last() - min) / span)
        Surface(
            shape = RoundedCornerShape(50.dp),
            color = BiViolet,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-2).dp, y = ((0.82f - lastFraction * 0.62f) * 58f).dp - 6.dp)
        ) {
            Text(
                text = "${lift.weight} kg",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.5.dp)
            )
        }
    }
}

@Composable
private fun BiStrengthStat(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(11.dp),
        color = BiPanel,
        border = BorderStroke(0.8.dp, Color(0xFFEDF2F7)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = BiFaint,
                maxLines = 1
            )
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = BiInk,
                maxLines = 1
            )
        }
    }
}

// -----------------------------------------------------------------------------
// MUSCLE FOCUS
// -----------------------------------------------------------------------------

private data class BiMuscle(val name: String, val percent: Int, val sets: Int, val color: Color)

private val BiMuscles = listOf(
    BiMuscle("Back", 28, 12, BiGreen),
    BiMuscle("Chest", 22, 10, Color(0xFF22C55E)),
    BiMuscle("Shoulder", 18, 8, Color(0xFF4ADE80)),
    BiMuscle("Legs", 17, 8, Color(0xFF65A30D)),
    BiMuscle("Arms", 15, 6, Color(0xFF84CC16)),
    BiMuscle("Core", 8, 4, BiAmber)
)

@Composable
private fun BiMuscleFocusCard(modifier: Modifier = Modifier) {
    BiBlock(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BiBlockIcon(Icons.Filled.Bolt, BiGreenDeep, Color(0xFFD9F7E8))
            Spacer(Modifier.width(7.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Muscle Focus",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = BiInk
                )
                Text(
                    text = "Training volume · this week",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BiMuted
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFC3CEDC),
                modifier = Modifier.size(16.dp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFE6FBF1), Color(0xFFD6F5E7))
                        )
                    )
                    .border(1.dp, Color.White, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.illustration_3d_mannequin),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(44.dp)
                )
            }
            Spacer(Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BiMuscles.forEach { muscle ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = muscle.name,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BiInk2,
                            modifier = Modifier.width(52.dp),
                            maxLines = 1
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEDF2F7))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(muscle.percent / 28f)
                                    .height(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(muscle.color.copy(alpha = 0.7f), muscle.color)
                                        )
                                    )
                            )
                        }
                        Text(
                            text = " ${muscle.percent}%",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = BiInk,
                            modifier = Modifier.width(38.dp),
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// ACTIVITY & ENDURANCE
// -----------------------------------------------------------------------------

private data class BiActivity(val label: String, val hours: String, val percent: Int, val color: Color)

private val BiActivities = listOf(
    BiActivity("Strength", "8.4 hr", 49, BiGreen),
    BiActivity("Cardio", "3.1 hr", 18, Color(0xFF84CC16)),
    BiActivity("Running", "2.2 hr", 13, BiAmber),
    BiActivity("HIIT", "1.5 hr", 9, BiRed),
    BiActivity("Cycling", "1.0 hr", 6, BiBlue),
    BiActivity("Other", "1.0 hr", 5, Color(0xFF94A3B8))
)

@Composable
private fun BiActivityCard(modifier: Modifier = Modifier) {
    BiBlock(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BiBlockIcon(Icons.Filled.DirectionsRun, BiBlue, Color(0xFFE3F2FE))
            Spacer(Modifier.width(7.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Activity & Endurance",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = BiInk
                )
                Text(
                    text = "17.2 hr logged this month",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BiMuted
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .border(0.9.dp, BiLine, RoundedCornerShape(50.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "Month",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = BiMuted
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = BiMuted,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(96.dp)) {
                Canvas(modifier = Modifier.size(96.dp)) {
                    var startAngle = -90f
                    val stroke = size.minDimension * 0.19f
                    val inset = stroke / 2f
                    BiActivities.forEach { activity ->
                        val sweep = activity.percent / 100f * 360f
                        drawArc(
                            color = activity.color,
                            startAngle = startAngle + 1.8f,
                            sweepAngle = (sweep - 3.6f).coerceAtLeast(0f),
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round),
                            topLeft = Offset(inset, inset),
                            size = Size(size.width - stroke, size.height - stroke)
                        )
                        startAngle += sweep
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "17.2",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = BiInk
                    )
                    Text(
                        text = "hours",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BiMuted
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                BiActivities.forEach { activity ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(activity.color)
                        )
                        Text(
                            text = " ${activity.label}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BiInk2,
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )
                        Text(
                            text = activity.hours,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = BiInk,
                            maxLines = 1
                        )
                        Text(
                            text = " ${activity.percent}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BiFaint,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// FITNESS ASSESSMENT
// -----------------------------------------------------------------------------

private data class BiTest(
    val label: String,
    val value: String,
    val delta: String,
    val icon: ImageVector,
    val color: Color
)

private val BiTests = listOf(
    BiTest("Push-up", "25 reps", "▲ +5", Icons.Filled.FitnessCenter, Color(0xFF16A34A)),
    BiTest("Pull-up", "8 reps", "▲ +2", Icons.Filled.Bolt, Color(0xFF059669)),
    BiTest("1 km Run", "6:50 min", "▼ −40s", Icons.Filled.DirectionsRun, Color(0xFF0284C7)),
    BiTest("Plank", "1:20 min", "▲ +20s", Icons.Filled.Timer, Color(0xFF0D9488)),
    BiTest("BMR", "1,620 kcal", "▲ +45", Icons.Filled.LocalFireDepartment, Color(0xFFDC2626)),
    BiTest("Waist", "32.0\"", "▼ −1.0\"", Icons.Filled.TrackChanges, Color(0xFFD97706))
)

@Composable
private fun BiAssessmentCard(modifier: Modifier = Modifier) {
    BiBlock(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BiBlockIcon(Icons.Filled.PieChart, Color(0xFF475569), Color(0xFFF1F5F9))
            Spacer(Modifier.width(7.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fitness Assessment",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = BiInk
                )
                Text(
                    text = "Performance tests · this month",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BiMuted
                )
            }
            Text(
                text = "View all",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = BiGreenDeep
            )
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = BiGreenDeep,
                modifier = Modifier.size(14.dp)
            )
        }

        BiTests.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { test ->
                    BiTestTile(test = test, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun BiTestTile(test: BiTest, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(13.dp),
        color = Color.White,
        border = BorderStroke(0.9.dp, BiLine),
        modifier = modifier.shadow(4.dp, RoundedCornerShape(13.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(test.color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = test.icon,
                    contentDescription = null,
                    tint = test.color,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = test.label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = BiMuted,
                maxLines = 1
            )
            Text(
                text = test.value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = BiInk,
                maxLines = 1
            )
            Text(
                text = test.delta,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Black,
                color = BiGreenDeep,
                maxLines = 1
            )
        }
    }
}

// -----------------------------------------------------------------------------
// AI INSIGHT
// -----------------------------------------------------------------------------

@Composable
private fun BiInsightBanner(onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    Surface(
        shape = BiShape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, BiGreenEdge),
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, BiShape, ambientColor = BiGreen, spotColor = BiGreen)
            .clip(BiShape)
            .background(
                Brush.linearGradient(listOf(Color(0xFFF0FDF4), Color(0xFFE4FAF0)))
            )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFF34D399), Color(0xFF059669))))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Stars,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "AI Insight",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color.White,
                    border = BorderStroke(0.9.dp, BiGreenEdge),
                    modifier = Modifier.clickable { onClick() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = null,
                            tint = BiGreenDeep,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = " Detailed Analysis",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BiGreenDeep
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            BiInsightLine("Great progress! Weight −2.5 kg and body fat −1.2% in 6 months.")
            BiInsightLine("Back & shoulders training volume is higher (28% & 18%).")
            BiInsightLine("Add 1–2 leg sessions per week for better balance.")
            BiInsightLine("Current plan is 73% effective for your Fat Loss goal.")
        }
    }
}

@Composable
private fun BiInsightLine(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = BiGreen,
            modifier = Modifier
                .padding(top = 1.dp)
                .size(12.dp)
        )
        Text(
            text = " $text",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = BiInk2,
            lineHeight = 15.sp
        )
    }
}

// -----------------------------------------------------------------------------
// SHARED PRIMITIVES
// -----------------------------------------------------------------------------

@Composable
private fun BiBlock(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        shape = BiShape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color.White),
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, BiShape, ambientColor = Color(0xFF9DB2C8), spotColor = Color(0xFFB9C7D6))
            .clip(BiShape)
            .background(
                Brush.verticalGradient(
                    listOf(Color.White, Color(0xFFFBFDFF), Color(0xFFF6FAFD))
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun BiBlockIcon(icon: ImageVector, tint: Color, wash: Color) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(wash),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(15.dp)
        )
    }
}
