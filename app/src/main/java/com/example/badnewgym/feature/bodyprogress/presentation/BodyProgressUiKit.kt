package com.example.badnewgym.feature.bodyprogress.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.tokens.MiColors
import kotlin.math.abs

/**
 * Chart primitives for Body Progress.
 *
 * Every chart answers one question, carries its own unit and time period, and
 * exposes an accessible summary. None of them invents points: an empty series
 * renders an explicit empty state instead of a flat decorative line.
 */

// ------------------------------------------------------------------ tokens

object BodyChartColors {
    val weight = Color(0xFF8B5CF6)
    val muscle = Color(0xFF00CFF0)
    val bodyFat = Color(0xFFF04452)
    val strength = Color(0xFF00C98A)
    val endurance = Color(0xFF4D9BFF)
    val waist = Color(0xFFF5B33B)
    val gridLine = Color(0xFF1E4A8A)
}

private fun colorFor(metricKey: String): Color = when (metricKey) {
    "WEIGHT" -> BodyChartColors.weight
    "MUSCLE_MASS" -> BodyChartColors.muscle
    "BODY_FAT", "FAT_MASS" -> BodyChartColors.bodyFat
    "WAIST" -> BodyChartColors.waist
    else -> BodyChartColors.strength
}

/** Light glass card matching the approved Home language. */
@Composable
fun BodyCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    padding: Dp = 10.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MiColors.Surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MiColors.Border),
        shadowElevation = 2.dp
    ) {
        val inner: @Composable () -> Unit = { Column(Modifier.padding(padding)) { content() } }
        if (onClick != null) Box(Modifier.clickable(onClick = onClick)) { inner() } else inner()
    }
}

@Composable
fun BodyPill(text: String, bg: Color, fg: Color = Color.White, size: Int = 10) {
    Box(
        Modifier.clip(RoundedCornerShape(20.dp)).background(bg).padding(horizontal = 9.dp, vertical = 4.dp)
    ) { Text(text, fontSize = size.sp, fontWeight = FontWeight.Bold, color = fg, maxLines = 1) }
}

/** Section header with optional trailing control. */
@Composable
fun BodySectionHeader(
    title: String,
    subtitle: String? = null,
    iconTint: Color = MiColors.Emerald,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(26.dp).clip(RoundedCornerShape(8.dp)).background(iconTint),
            contentAlignment = Alignment.Center
        ) { Text(title.take(1), fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White) }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MiColors.TextPrimary, maxLines = 1)
            if (subtitle != null) {
                Text(subtitle, fontSize = 10.sp, color = MiColors.TextSecondary, maxLines = 1)
            }
        }
        trailing?.invoke()
    }
}

/**
 * Dated line chart with unit and period in the caller's subtitle.
 *
 * A single point is NOT drawn as a line: it is reported as a dot plus an
 * explicit "not enough points for a trend" note.
 */
@Composable
fun BodyLineChart(
    values: List<Double>,
    labels: List<String>,
    color: Color,
    unit: String,
    modifier: Modifier = Modifier.height(96.dp).fillMaxWidth(),
    highlightLast: Boolean = true,
    /** Compact cards hide the min/max axis row: the unit still travels in the
     *  widget subtitle, so no axis is ever left unlabelled. */
    showAxis: Boolean = true
) {
    if (values.isEmpty()) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Text("No readings in this range", fontSize = 11.sp, color = MiColors.TextTertiary)
        }
        return
    }
    val lo = values.min()
    val hi = values.max()
    val description = buildString {
        append(if (values.size == 1) "Single reading" else "Trend")
        append(" from ")
        append(trim(lo)); append(unit); append(" to ")
        append(trim(hi)); append(unit)
        append(": ")
        values.forEachIndexed { i, v ->
            append(labels.getOrNull(i) ?: "?")
            append(" ")
            append(trim(v))
            append(unit)
            if (i < values.size - 1) append(", ")
        }
    }
    Column(modifier.semantics { contentDescription = description }) {
        Canvas(Modifier.fillMaxWidth().weight(1f)) {
            if (values.size < 2) {
                val y = size.height / 2
                drawCircle(color, radius = 4.dp.toPx(), center = androidx.compose.ui.geometry.Offset(size.width / 2, y))
                drawLine(
                    MiColors.Border,
                    androidx.compose.ui.geometry.Offset(0f, size.height - 1f),
                    androidx.compose.ui.geometry.Offset(size.width, size.height - 1f),
                    strokeWidth = 1.dp.toPx()
                )
                return@Canvas
            }
            val span = (hi - lo).takeIf { it > 0.0001 } ?: 1.0
            val pad = 8.dp.toPx()
            val stepX = if (values.size == 1) 0f else size.width / (values.size - 1)
            fun yOf(v: Double) = size.height - pad - (((v - lo) / span) * (size.height - pad * 2)).toFloat()

            // baseline + gridlines
            drawLine(
                MiColors.Border,
                androidx.compose.ui.geometry.Offset(0f, size.height - 1f),
                androidx.compose.ui.geometry.Offset(size.width, size.height - 1f),
                strokeWidth = 1.dp.toPx()
            )
            val gridYs = listOf(0f, 0.5f, 1f)
            gridYs.forEach { frac ->
                val y = pad + (size.height - pad * 2) * frac
                drawLine(
                    BodyChartColors.gridLine,
                    androidx.compose.ui.geometry.Offset(0f, y),
                    androidx.compose.ui.geometry.Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val path = Path()
            values.forEachIndexed { i, v ->
                val x = i * stepX
                val y = yOf(v)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(
                path,
                color,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            )
            values.forEachIndexed { i, v ->
                drawCircle(
                    color,
                    radius = if (highlightLast && i == values.size - 1) 4.dp.toPx() else 2.4.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(i * stepX, yOf(v))
                )
            }
        }
        Spacer(Modifier.height(2.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(labels.firstOrNull() ?: "", fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1)
            if (showAxis) {
                Text("${trim(lo)}-${trim(hi)}$unit", fontSize = 8.5.sp, color = MiColors.TextTertiary, maxLines = 1)
            }
            Text(labels.lastOrNull() ?: "", fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1)
        }
        if (values.size < 2) {
            Text(
                "Only one reading - a trend needs at least two points.",
                fontSize = 9.sp, color = MiColors.TextTertiary, maxLines = 1
            )
        }
    }
}

/** Horizontal ranked bars - the right chart for comparable discrete values. */
@Composable
fun BodyRankedBars(
    rows: List<RankedBar>,
    modifier: Modifier = Modifier.fillMaxWidth(),
    barColor: Color = MiColors.Emerald
) {
    if (rows.isEmpty()) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Text("No recorded data in this range", fontSize = 11.sp, color = MiColors.TextTertiary)
        }
        return
    }
    val max = rows.maxOf { it.value }.coerceAtLeast(0.0001)
    Column(
        modifier.semantics {
            contentDescription = rows.joinToString("; ") {
                "${it.label} ${trim(it.value)}${it.unitSuffix} ${it.percent}%"
            }
        },
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        rows.forEach { row ->
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        row.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                        color = MiColors.TextPrimary, maxLines = 1, modifier = Modifier.weight(1f)
                    )
                    Text(
                        "${row.percent}%", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                        color = row.accent, maxLines = 1
                    )
                }
                Spacer(Modifier.height(3.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MiColors.Border)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth((row.value / max).toFloat().coerceIn(0.02f, 1f))
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(listOf(barColor, barColor.copy(alpha = 0.65f)))
                            )
                    )
                }
            }
        }
    }
}

data class RankedBar(
    val label: String,
    val value: Double,
    val percent: Int,
    val unitSuffix: String = "",
    val accent: Color = MiColors.TextPrimary
)

/** Donut used only when a real denominator exists (equipment share, goal %). */
@Composable
fun BodyDonut(
    percent: Int,
    centerLabel: String,
    caption: String,
    color: Color,
    modifier: Modifier = Modifier.size(92.dp),
    semanticsText: String
) {
    Box(modifier.semantics { contentDescription = semanticsText }, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 10.dp.toPx()
            drawArc(
                MiColors.Border, -90f, 360f, false,
                style = Stroke(width = stroke)
            )
            drawArc(
                color, -90f, (percent.coerceIn(0, 100) / 100f) * 360f, false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(centerLabel, fontSize = 16.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
            Text(caption, fontSize = 9.sp, color = MiColors.TextSecondary, maxLines = 1)
        }
    }
}

/** Calendar heatmap of daily training state. */
@Composable
fun BodyCalendar(
    cells: List<Pair<Long, ConsistencyCell>>,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    if (cells.isEmpty()) {
        Text("No days in range", fontSize = 11.sp, color = MiColors.TextTertiary)
        return
    }
    val rows = cells.chunked(7)
    val colors = mapOf(
        ConsistencyCell.WORKOUT to Color(0xFF16A34A),
        ConsistencyCell.LIGHT to Color(0xFFFBBF24),
        ConsistencyCell.ABSENT to Color(0xFFCBD5E1),
        ConsistencyCell.REST to MiColors.Border
    )
    Column(
        modifier.semantics {
            contentDescription = cells.joinToString(", ") { it.second.label }
        },
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        rows.forEach { week ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                week.forEach { (_, state) ->
                    Box(
                        Modifier.size(15.dp).clip(RoundedCornerShape(4.dp)).background(colors[state] ?: MiColors.Border)
                    )
                }
            }
        }
    }
}

enum class ConsistencyCell(val label: String) { WORKOUT("Workout"), LIGHT("Light"), ABSENT("Absent"), REST("Rest") }

/** Small metric tile used in the supporting-metrics strip. */
@Composable
fun BodyMetricTile(
    label: String,
    value: String,
    delta: String?,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clip(RoundedCornerShape(11.dp))
            .background(MiColors.SurfaceSunken)
            .padding(horizontal = 7.dp, vertical = 5.dp)
            .semantics {
                contentDescription = buildString {
                    append(label); append(": "); append(value)
                    if (delta != null) append(", change "); append(delta)
                }
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(5.dp).clip(CircleShape).background(color))
            Spacer(Modifier.width(3.dp))
            Text(
                label, fontSize = 8.5.sp, fontWeight = FontWeight.Bold,
                color = MiColors.TextSecondary, maxLines = 1
            )
        }
        Spacer(Modifier.height(1.dp))
        Text(value, fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = MiColors.TextPrimary, maxLines = 1)
        if (delta != null) {
            Text(
                delta, fontSize = 8.5.sp, fontWeight = FontWeight.Bold,
                color = if (delta.startsWith("-")) MiColors.Red else MiColors.EmeraldDark, maxLines = 1
            )
        }
    }
}

@Composable
fun BodyEmptyState(message: String, hint: String? = null) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MiColors.SurfaceSunken)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MiColors.TextSecondary)
        if (hint != null) {
            Spacer(Modifier.height(3.dp))
            Text(hint, fontSize = 10.5.sp, color = MiColors.TextTertiary)
        }
    }
}

internal fun trim(v: Double): String =
    if (abs(v - Math.rint(v)) < 0.001) v.toInt().toString() else "%.1f".format(v)
