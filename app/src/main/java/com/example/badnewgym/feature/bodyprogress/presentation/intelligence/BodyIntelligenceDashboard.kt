package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.bodyprogress.domain.catalog.ExerciseCatalog
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyDashboardAnalytics.InsightsDashboard
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyIntelligenceView
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.CompositionReading
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.InsightTone
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.StrengthSetRecord
import com.example.badnewgym.feature.bodyprogress.domain.model.BodyMetric
import kotlin.math.roundToInt

/**
 * Body Intelligence dashboard.
 *
 * One continuous page built from the member's own records. The layout follows a
 * strict reading order - what the member is aiming at, where they are now, how
 * they got here, what the training actually covered, and what to do next - so
 * the tab answers the same question top to bottom without a nav tap.
 *
 * The honesty rule from the domain layer is preserved here and is visible in the
 * UI: a section with no records renders a designed empty state that names what
 * would fill it. Nothing is interpolated, averaged or invented to make the page
 * look complete, and [ProvenanceNote] states that at the foot of the tab.
 */
fun LazyListScope.bodyIntelligenceItems(
    state: BodyIntelligenceState,
    listState: LazyListState,
    onPrivacyClick: () -> Unit = { state.openSheet(BodySheetTarget.Privacy) }
) {
    val view = state.view
    val insights = state.insights

    item(key = "bi_goal") {
        GoalAndMetrics(
            state = state,
            insights = insights,
            onMetricClick = { state.openSheet(BodySheetTarget.MetricDetail(it)) }
        )
    }

    item(key = "bi_transformation") {
        TransformationCard(
            view = view,
            onMetricClick = { state.openSheet(BodySheetTarget.MetricDetail(it)) }
        )
    }

    item(key = "bi_muscle_focus") { MuscleFocusCard(state = state) }

    item(key = "bi_strength") { StrengthCard(state = state) }

    item(key = "bi_measurements") { MeasurementsCard(view = view) }

    item(key = "bi_bottom_space") { Spacer(modifier = Modifier.height(6.dp)) }
}

// =============================================================================
// SURFACE SYSTEM
// =============================================================================

/**
 * Body-tab surfaces. The tab keeps the app's pearl palette but carries a deeper
 * emerald than the other tabs so the member can feel which section they are in
 * without reading the header.
 */
internal object BiSurface {
    val Emerald = Color(0xFF0FA36B)
    val EmeraldDeep = Color(0xFF06674A)
    val EmeraldDark = Color(0xFF03412E)
    val Lime = Color(0xFF65C43B)
    val Violet = Color(0xFF6B5BD2)
    val Amber = Color(0xFFE8A317)
    val Rose = Color(0xFFD6455F)
    val Cyan = Color(0xFF2C9BD4)

    val CardFill = Brush.verticalGradient(
        0f to Color.White,
        0.55f to Color(0xFFFCFEFD),
        1f to Color(0xFFF0F8F3)
    )
    val GoalFill = Brush.verticalGradient(
        0f to Emerald,
        0.52f to EmeraldDeep,
        1f to EmeraldDark
    )
    val ScoreFill = Brush.linearGradient(listOf(Emerald, EmeraldDark))
    val InsightFill = Brush.linearGradient(listOf(Color(0xFF0FB877), Color(0xFF067251)))
    val ReviewFill = Brush.linearGradient(listOf(Color(0xFF9C36A8), Color(0xFF5B2C9B)))
    val RewardFill = Brush.linearGradient(listOf(Color(0xFF8B3FCB), Color(0xFF4A2A8F)))

    val TileFill = Brush.verticalGradient(
        0f to Color(0xFFF5FBF8),
        1f to Color(0xFFE8F4EE)
    )
}

/**
 * Neumorphic lift with a hue-carrying contact shadow, so a card reads as sitting
 * on the mint canvas rather than being pasted onto it.
 */
internal fun Modifier.biLift(
    shape: Shape,
    tint: Color = BiSurface.Emerald,
    elevation: androidx.compose.ui.unit.Dp = 8.dp
): Modifier = shadow(
    elevation = elevation,
    shape = shape,
    clip = false,
    ambientColor = Color(0xFF9CB4AC),
    spotColor = tint.copy(alpha = 0.42f)
)

@Composable
private fun BiCard(
    modifier: Modifier = Modifier,
    fill: Brush = BiSurface.CardFill,
    edge: Color = Color.White,
    tint: Color = BiSurface.Emerald,
    corner: androidx.compose.ui.unit.Dp = 18.dp,
    elevation: androidx.compose.ui.unit.Dp = 7.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    // The card dips under the finger so a tap registers as pressure, not just a
    // colour flash that is easy to miss on a pale surface.
    val scale by animateFloatAsState(
        targetValue = if (pressed && onClick != null) 0.974f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "biCardPress"
    )
    Surface(
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.2.dp, edge),
        shadowElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .biLift(shape, tint = tint, elevation = elevation)
            .clip(shape)
            .background(fill)
            .then(
                if (onClick != null) {
                    Modifier.clickable(interactionSource = interaction, indication = null) { onClick() }
                } else Modifier
            )
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), content = content)
    }
}

/** Gradient-faced variant used by the goal, insight, review and reward cards. */
@Composable
private fun BiGradientCard(
    fill: Brush,
    modifier: Modifier = Modifier,
    tint: Color = BiSurface.Emerald,
    corner: androidx.compose.ui.unit.Dp = 18.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && onClick != null) 0.978f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "biGradientPress"
    )
    Surface(
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.55f)),
        shadowElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .biLift(shape, tint = tint, elevation = 9.dp)
            .clip(shape)
            .background(fill)
            .then(
                if (onClick != null) {
                    Modifier.clickable(interactionSource = interaction, indication = null) { onClick() }
                } else Modifier
            )
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), content = content)
    }
}

/** Frosted square that holds a section or tile glyph. */
@Composable
private fun BiIconTile(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 40.dp,
    corner: androidx.compose.ui.unit.Dp = 13.dp,
    tint: Color = BiSurface.Emerald,
    onDark: Boolean = false
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .background(
                if (onDark) Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.28f), Color.White.copy(alpha = 0.10f))
                ) else Brush.verticalGradient(
                    listOf(Color.White, tint.copy(alpha = 0.14f))
                )
            )
            .border(
                1.dp,
                if (onDark) Color.White.copy(alpha = 0.42f) else Color.White,
                RoundedCornerShape(corner)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (onDark) Color.White else tint,
            modifier = Modifier.size(size * 0.48f)
        )
    }
}

@Composable
private fun BiSectionHeader(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    tint: Color = BiSurface.Emerald,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        BiIconTile(icon = icon, tint = tint, size = 30.dp, corner = 10.dp)
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Black,
                color = BodyIntelPalette.Ink,
                letterSpacing = (-0.3).sp,
                lineHeight = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 10.5.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BodyIntelPalette.Muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        trailing?.invoke()
    }
}

@Composable
private fun BiPill(
    text: String,
    foreground: Color,
    background: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(background)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, tint = foreground, modifier = Modifier.size(11.dp))
        }
        Text(
            text = text,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Black,
            color = foreground,
            maxLines = 1,
            softWrap = false
        )
    }
}

/**
 * The designed absence of data. Names the metric, says why it is empty and offers
 * the action that would fill it - never a dash, never a zero.
 */
@Composable
private fun BiEmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    tint: Color = BiSurface.Emerald
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFFF6FBF8), Color(0xFFEFF7F2)))
            )
            .border(1.dp, BodyIntelPalette.HairlineStrong, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 13.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = BodyIntelPalette.Ink,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(5.dp))
        Text(
            text = message,
            fontSize = 11.5.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            color = BodyIntelPalette.Muted
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(9.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Brush.verticalGradient(listOf(BiSurface.Emerald, BiSurface.EmeraldDeep)))
                    .clickable { onAction() }
                    .padding(horizontal = 13.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                Text(
                    text = actionLabel,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    }
}

// =============================================================================
// 1. GOAL BAND
// =============================================================================

/**
 * The anchor card: goal, plan-fit ring, the target/current/remaining sentence and
 * the composition tiles, all in one surface.
 *
 * These used to be two cards. Merging them removes a card chrome and a gap, and
 * it stops the goal band wearing a different material from everything below it -
 * every card on this tab is now the same pearl glass with the same edges.
 */
@Composable
private fun GoalAndMetrics(
    state: BodyIntelligenceState,
    insights: InsightsDashboard,
    onMetricClick: (BodyMetric) -> Unit
) {
    val view = state.view
    val weight = view.reading(BodyMetric.WEIGHT)
    val goalPercent = insights.goalPercent.coerceIn(0, 100)
    val goalTitle = insights.goalTitle.ifBlank { view.goal.label }

    BiCard(corner = 20.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BiIconTile(
                icon = Icons.Default.CenterFocusStrong,
                tint = BiSurface.Emerald,
                size = 32.dp,
                corner = 11.dp
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "PRIMARY GOAL",
                    fontSize = 9.5.sp,
                    lineHeight = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = BodyIntelPalette.Faint,
                    letterSpacing = 1.1.sp
                )
                Text(
                    text = goalTitle,
                    fontSize = 16.5.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Black,
                    color = BodyIntelPalette.Ink,
                    letterSpacing = (-0.5).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            // goalPercent is how well the training split supports the goal, not
            // how close the weight is to target - the strip below carries that.
            RingChart(
                percent = goalPercent,
                centerValue = "$goalPercent%",
                centerLabel = "plan fit",
                size = 56.dp,
                stroke = 7.dp,
                color = BiSurface.Emerald
            )
        }

        Spacer(Modifier.height(8.dp))

        // Target / current / remaining read as one sentence, so the member cannot
        // mistake a starting weight for a goal weight.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .background(BodyIntelPalette.CardInset)
                .border(1.dp, BodyIntelPalette.Hairline, RoundedCornerShape(13.dp))
                .padding(horizontal = 9.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GoalFigure(
                label = "Target",
                value = insights.goalTarget.ifBlank { "Not set" },
                modifier = Modifier.weight(1f)
            )
            GoalDivider()
            GoalFigure(
                label = "Current",
                value = weight?.valueLabel ?: "Not recorded",
                modifier = Modifier.weight(1f)
            )
            GoalDivider()
            GoalFigure(
                label = "Left",
                value = insights.goalRemaining.ifBlank { "\u2014" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(7.dp))

        Row(verticalAlignment = Alignment.Top) {
            val tracking = view.insights.firstOrNull { it.metricKey != null && it.tone == InsightTone.POSITIVE }
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(15.dp)
                    .clip(CircleShape)
                    .background(BiSurface.Emerald),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(
                text = insights.summary.ifBlank {
                    tracking?.title ?: "Records are collected; the trend line appears once a second reading exists."
                },
                fontSize = 11.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = BodyIntelPalette.InkSoft,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(9.dp))
        DividerLine()
        Spacer(Modifier.height(8.dp))

        MetricRail(
            view = view,
            score = insights.score,
            scoreLabel = insights.scoreLabel,
            onMetricClick = onMetricClick
        )
    }
}

@Composable
private fun GoalFigure(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            fontSize = 9.5.sp,
            lineHeight = 11.sp,
            fontWeight = FontWeight.Bold,
            color = BodyIntelPalette.Faint,
            letterSpacing = 0.6.sp
        )
        Text(
            text = value,
            fontSize = 12.5.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Black,
            color = BodyIntelPalette.Ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun GoalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(20.dp)
            .background(BodyIntelPalette.HairlineStrong)
    )
}

// =============================================================================
// 2. METRIC RAIL + BODY SCORE
// =============================================================================

private fun metricIcon(metric: BodyMetric): ImageVector = when (metric) {
    BodyMetric.WEIGHT -> Icons.Default.TrackChanges
    BodyMetric.MUSCLE_MASS -> Icons.Default.FitnessCenter
    BodyMetric.BODY_FAT, BodyMetric.FAT_MASS -> Icons.Default.LocalFireDepartment
    BodyMetric.BMI -> Icons.Default.Assessment
    else -> Icons.Default.ShowChart
}

/**
 * Metrics that belong in the composition rail and the trend chart.
 *
 * Girth measurements are records too, but they answer a different question and
 * have their own card - mixing centimetres into a kilogram rail makes the trend
 * plot compare things it cannot compare.
 */
private val COMPOSITION_METRICS = setOf(
    BodyMetric.WEIGHT,
    BodyMetric.MUSCLE_MASS,
    BodyMetric.BODY_FAT,
    BodyMetric.FAT_MASS,
    BodyMetric.BMI
)

private fun metricTint(metric: BodyMetric): Color = when (metric) {
    BodyMetric.WEIGHT -> BiSurface.Emerald
    BodyMetric.MUSCLE_MASS -> BiSurface.Lime
    BodyMetric.BODY_FAT, BodyMetric.FAT_MASS -> BiSurface.Amber
    BodyMetric.BMI -> BiSurface.Cyan
    else -> BiSurface.Violet
}

/**
 * The four composition tiles plus the derived Body Score.
 *
 * Tiles render only metrics the member actually recorded and has not hidden, so
 * a private category disappears from the rail entirely rather than showing a
 * locked placeholder. The rail scrolls instead of shrinking type to fit.
 */
@Composable
private fun MetricRail(
    view: BodyIntelligenceView,
    score: Int,
    scoreLabel: String,
    onMetricClick: (BodyMetric) -> Unit
) {
    val readings = view.readings.filter { it.metric in COMPOSITION_METRICS }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        readings.forEach { reading ->
            MetricTile(reading = reading, onClick = { onMetricClick(reading.metric) })
        }
        if (readings.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                BiEmptyState(
                    icon = Icons.Default.Info,
                    title = "No composition records yet",
                    message = "Weight, muscle mass, body fat and BMI appear here once a measurement is stored for this member."
                )
            }
        }
        BodyScoreTile(score = score, label = scoreLabel)
    }
}

@Composable
private fun MetricTile(reading: CompositionReading, onClick: () -> Unit) {
    val tint = metricTint(reading.metric)
    val shape = RoundedCornerShape(18.dp)
    Surface(
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, BodyIntelPalette.Hairline),
        shadowElevation = 0.dp,
        modifier = Modifier
            .width(112.dp)
            .biLift(shape, tint = tint, elevation = 7.dp)
            .clip(shape)
            .background(BiSurface.TileFill)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BiIconTile(icon = metricIcon(reading.metric), tint = tint, size = 19.dp, corner = 6.dp)
                Spacer(Modifier.width(4.dp))
                Text(
                    text = reading.label,
                    fontSize = 10.sp,
                    lineHeight = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BodyIntelPalette.Muted,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = reading.valueLabel,
                fontSize = 15.5.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Black,
                color = BodyIntelPalette.Ink,
                letterSpacing = (-0.5).sp,
                maxLines = 1,
                softWrap = false
            )
            Spacer(Modifier.height(2.dp))
            val delta = reading.deltaLabel
            if (delta.isNullOrBlank()) {
                Text(
                    text = "First record",
                    fontSize = 10.sp,
                    lineHeight = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BodyIntelPalette.Faint
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (delta.startsWith("-") || delta.startsWith("\u25BC")) {
                            Icons.Default.ArrowDownward
                        } else {
                            Icons.Default.TrendingUp
                        },
                        contentDescription = null,
                        tint = if (reading.deltaIsFavourable) BiSurface.EmeraldDeep else BiSurface.Rose,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = delta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = if (reading.deltaIsFavourable) BiSurface.EmeraldDeep else BiSurface.Rose,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

/**
 * Body Score is a derived index, not a measurement. It carries the same pearl
 * surface as the other tiles and differentiates itself with a gauge rather than
 * a slab of saturated colour, so one card does not outshine the whole tab.
 */
@Composable
private fun BodyScoreTile(score: Int, label: String) {
    val shape = RoundedCornerShape(18.dp)
    val clamped = score.coerceIn(0, 100)
    Surface(
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, BiSurface.Emerald.copy(alpha = 0.34f)),
        shadowElevation = 0.dp,
        modifier = Modifier
            .width(112.dp)
            .biLift(shape, tint = BiSurface.EmeraldDeep, elevation = 8.dp)
            .clip(shape)
            .background(
                Brush.verticalGradient(listOf(Color(0xFFF1FBF5), Color(0xFFDFF3E8)))
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "BODY SCORE",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    color = BiSurface.EmeraldDeep,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    softWrap = false
                )
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = BiSurface.Amber,
                    modifier = Modifier.size(15.dp)
                )
            }
            Spacer(Modifier.height(6.dp))
            Box(contentAlignment = Alignment.Center) {
                ScoreGauge(percent = clamped)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$clamped",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = BodyIntelPalette.Ink,
                            lineHeight = 21.sp
                        )
                        Text(
                            text = "/100",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.Muted,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(3.dp))
            Text(
                text = "$label · derived",
                fontSize = 9.5.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Black,
                color = BiSurface.EmeraldDeep,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ScoreGauge(percent: Int) {
    Box(modifier = Modifier.size(40.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth()) {
            val stroke = 6.dp.toPx()
            val inset = stroke / 2f
            // 270-degree gauge opening downward, like a dial the member can read at a glance.
            drawArc(
                color = Color(0xFFD6E8DE),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
            drawArc(
                color = BiSurface.Emerald,
                startAngle = 135f,
                sweepAngle = 270f * percent / 100f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }
    }
}

// =============================================================================
// 3. TRANSFORMATION TREND
// =============================================================================

/**
 * Multi-series plot of the member's dated composition records.
 *
 * Where a metric has a dated history its real values are printed on the chart and
 * the months run along the axis. Where it only has a normalised shape the line is
 * still drawn, but no numbers are invented to label it.
 *
 * Each series keeps its own vertical range because kilograms and percent are not
 * comparable units - sharing one axis would imply a fat-loss line and a
 * body-weight line measure the same thing. The printed figures carry the truth
 * that the relative heights deliberately do not.
 */
@Composable
private fun TransformationCard(
    view: BodyIntelligenceView,
    onMetricClick: (BodyMetric) -> Unit
) {
    val composition = view.readings.filter { it.metric in COMPOSITION_METRICS }
    val dated = composition.filter { it.hasHistory }.map { reading ->
        LabeledSeries(
            label = reading.label,
            months = reading.history.map { it.first },
            values = reading.history.map { it.second },
            color = metricTint(reading.metric)
        )
    }
    val shapeOnly = composition.filter { !it.hasHistory && it.hasTrend }.map { reading ->
        TrendLine(
            label = reading.label,
            points = reading.trend,
            color = metricTint(reading.metric)
        )
    }

    BiCard {
        BiSectionHeader(
            icon = Icons.Default.ShowChart,
            title = "Transformation Trend",
            subtitle = if (dated.isNotEmpty()) "Stored values, May \u2013 Oct 2026" else "Each line scaled to its own range",
            trailing = {
                BiPill(
                    text = "${(dated.size + shapeOnly.size).coerceAtLeast(1)} tracked",
                    foreground = BiSurface.EmeraldDeep,
                    background = Color(0xFFE4F7EE)
                )
            }
        )
        Spacer(Modifier.height(8.dp))

        if (dated.isEmpty() && shapeOnly.isEmpty()) {
            BiEmptyState(
                icon = Icons.Default.Timeline,
                title = "Not enough entries to draw a trend",
                message = "A line needs at least two stored readings of the same metric. Nothing is interpolated between records to fill this space."
            )
        } else if (dated.isNotEmpty()) {
            TrendKey(dated)
            Spacer(Modifier.height(4.dp))
            BiLabeledTrend(series = dated, height = 132.dp)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                composition.filter { it.hasHistory }.forEach { reading ->
                    val delta = reading.deltaLabel
                    DeltaChip(
                        label = reading.label,
                        value = if (delta.isNullOrBlank()) "no change on record" else delta,
                        favourable = reading.deltaIsFavourable,
                        tint = metricTint(reading.metric),
                        modifier = Modifier.weight(1f, fill = false),
                        onClick = { onMetricClick(reading.metric) }
                    )
                }
            }
        } else {
            ChartLegend(series = shapeOnly)
            Spacer(Modifier.height(6.dp))
            LineTrendChart(
                series = shapeOnly,
                labels = listOf("First record", "Latest"),
                height = 84.dp,
                sharedScale = false,
                fillFirst = true
            )
        }
    }
}

private data class LabeledSeries(
    val label: String,
    val months: List<String>,
    val values: List<Double>,
    val color: Color
)

@Composable
private fun TrendKey(series: List<LabeledSeries>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        series.forEachIndexed { index, line ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(if (index == 0) 9.dp else 7.dp)
                        .clip(CircleShape)
                        .background(line.color)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = line.label,
                    fontSize = if (index == 0) 11.sp else 10.5.sp,
                    fontWeight = if (index == 0) FontWeight.Black else FontWeight.SemiBold,
                    color = if (index == 0) BodyIntelPalette.Ink else BodyIntelPalette.Muted
                )
            }
        }
    }
}

/**
 * Line chart with real values printed on it.
 *
 * Only the first series gets a figure at every point. Labelling all four puts
 * twenty-four numbers into a 360dp-wide plot and they overlap each other and the
 * markers, which is the failure this chart is meant to avoid. The remaining
 * series take a single chip at the right end of their own line.
 */
@Composable
private fun BiLabeledTrend(
    series: List<LabeledSeries>,
    modifier: Modifier = Modifier,
    height: Dp = 132.dp
) {
    val drawable = series.filter { it.values.size >= 2 && it.months.size == it.values.size }
    if (drawable.isEmpty()) return
    val months = drawable.first().months
    val n = months.size

    val ranges = drawable.map { s ->
        val lo = s.values.min()
        val hi = s.values.max()
        val pad = ((hi - lo) * 0.5).coerceAtLeast(0.4)
        (lo - pad) to (hi + pad)
    }

    val gutterEnd = 32.dp
    val headroom = 17.dp
    val footprint = 15.dp

    BoxWithConstraints(modifier = modifier.fillMaxWidth().height(height)) {
        val plotLeft = 2.dp
        val plotRight = maxWidth - gutterEnd
        val plotWidth = (plotRight - plotLeft).coerceAtLeast(24.dp)
        val plotTop = headroom
        val plotBottom = height - footprint
        val plotHeight = (plotBottom - plotTop).coerceAtLeast(24.dp)

        fun xAt(i: Int): Dp = plotLeft + plotWidth * (i.toFloat() / (n - 1).coerceAtLeast(1))
        fun yAt(si: Int, v: Double): Dp {
            val (lo, hi) = ranges[si]
            val f = ((v - lo) / (hi - lo)).toFloat().coerceIn(0f, 1f)
            return plotBottom - plotHeight * f
        }

        Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            repeat(3) { k ->
                val y = (plotTop + plotHeight * (k / 2f)).toPx()
                drawLine(
                    color = Color(0xFFE7F0EA),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
            }

            drawable.forEachIndexed { si, s ->
                val pts = s.values.indices.map { i ->
                    Offset(xAt(i).toPx(), yAt(si, s.values[i]).toPx())
                }
                val path = Path()
                pts.forEachIndexed { i, p ->
                    if (i == 0) path.moveTo(p.x, p.y)
                    else {
                        val prev = pts[i - 1]
                        val midX = (prev.x + p.x) / 2f
                        path.cubicTo(midX, prev.y, midX, p.y, p.x, p.y)
                    }
                }
                if (si == 0) {
                    val fill = Path().apply {
                        addPath(path)
                        lineTo(pts.last().x, plotBottom.toPx())
                        lineTo(pts.first().x, plotBottom.toPx())
                        close()
                    }
                    drawPath(
                        path = fill,
                        brush = Brush.verticalGradient(
                            listOf(s.color.copy(alpha = 0.22f), s.color.copy(alpha = 0.02f))
                        )
                    )
                }
                drawPath(
                    path = path,
                    color = s.color,
                    style = Stroke(
                        width = if (si == 0) 2.6f.dp.toPx() else 1.6f.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                pts.forEach { p ->
                    drawCircle(color = Color.White, radius = 3.dp.toPx(), center = p)
                    drawCircle(color = s.color, radius = 1.8f.dp.toPx(), center = p)
                }
            }
        }

        val primary = drawable.first()
        primary.values.forEachIndexed { i, v ->
            Text(
                text = v.trimOne(),
                fontSize = 10.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Black,
                color = BodyIntelPalette.Ink,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .offset(x = xAt(i) - 20.dp, y = yAt(0, v) - 15.dp)
                    .width(40.dp)
            )
        }

        drawable.drop(1).forEachIndexed { offset, s ->
            val si = offset + 1
            val last = s.values.last()
            Text(
                text = last.trimOne(),
                fontSize = 9.5.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .offset(x = plotRight + 2.dp, y = yAt(si, last) - 7.dp)
                    .width(28.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(s.color)
                    .padding(vertical = 1.dp)
            )
        }

        months.forEachIndexed { i, m ->
            Text(
                text = m,
                fontSize = 10.sp,
                lineHeight = 11.sp,
                fontWeight = if (i == months.lastIndex) FontWeight.Black else FontWeight.Bold,
                color = if (i == months.lastIndex) BodyIntelPalette.Ink else BodyIntelPalette.Faint,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .offset(x = xAt(i) - 17.dp, y = height - 13.dp)
                    .width(34.dp)
            )
        }
    }
}

private fun Double.trimOne(): String = String.format(java.util.Locale.US, "%.1f", this)

@Composable
private fun DeltaChip(
    label: String,
    value: String,
    favourable: Boolean,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Surface(
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color.White),
        shadowElevation = 0.dp,
        modifier = modifier
            .biLift(shape, tint = tint, elevation = 4.dp)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFF3FAF6))))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(tint))
            Spacer(Modifier.width(5.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = BodyIntelPalette.Muted,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = value,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (favourable) BiSurface.EmeraldDeep else BiSurface.Rose,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

// =============================================================================
// 4. MUSCLE FOCUS
// =============================================================================

/**
 * Where the training volume actually went.
 *
 * The anatomy map and the bar list are two views of one ledger, so they can
 * never disagree. Both are driven by [BodyIntelligenceState.focus], which is
 * null until a session with sets exists.
 */
@Composable
private fun MuscleFocusCard(state: BodyIntelligenceState) {
    val focus = state.focus
    // The ledger is assembled from a session template when no sets are logged, so a
    // distribution built from it is an inference about the split, not a measurement.
    val isEstimate = focus != null && !state.input.hasSetLevelRecords

    BiCard {
        BiSectionHeader(
            icon = Icons.Default.AccessibilityNew,
            title = "Muscle Focus",
            subtitle = when {
                focus == null -> "Needs logged sets"
                isEstimate -> "Inferred from session split"
                else -> "Volume by group, this cycle"
            },
            tint = BiSurface.Lime,
            trailing = {
                if (focus != null && !isEstimate) {
                    BiPill(
                        text = "${focus.totalSets} sets",
                        foreground = BiSurface.EmeraldDeep,
                        background = Color(0xFFE4F7EE)
                    )
                } else if (focus != null) {
                    BiPill(
                        text = "estimate",
                        foreground = Color(0xFF7A5310),
                        background = Color(0xFFFEF3DA)
                    )
                }
            }
        )
        Spacer(Modifier.height(10.dp))

        if (focus == null) {
            BiEmptyState(
                icon = Icons.Default.AccessibilityNew,
                title = "No sets recorded yet",
                message = "Muscle distribution is calculated from logged sets and reps. Until a session is saved there is no honest way to say which muscles were trained, so this stays empty rather than showing an estimate.",
                tint = BiSurface.Lime,
                actionLabel = "Plan a session",
                onAction = { state.openSheet(BodySheetTarget.Plan(BodyPlanKind.NEXT_ACTIVITY)) }
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(listOf(Color(0xFFF3FBF7), Color(0xFFEAF6F0)))
                        )
                        .border(1.dp, BodyIntelPalette.Hairline, RoundedCornerShape(16.dp))
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BodyAnatomyMap(
                        view = state.anatomyView,
                        selected = state.selectedMuscle,
                        onSelect = { state.selectedMuscle = it },
                        modifier = Modifier.width(92.dp).height(112.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        com.example.badnewgym.feature.bodyprogress.domain.intelligence.AnatomyView.entries
                            .forEach { option ->
                                val active = state.anatomyView == option
                                Text(
                                    text = option.label,
                                    fontSize = 10.5.sp,
                                    fontWeight = if (active) FontWeight.Black else FontWeight.SemiBold,
                                    color = if (active) Color.White else BodyIntelPalette.Muted,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(
                                            if (active) BiSurface.Emerald else Color(0xFFEFF5F2)
                                        )
                                        .clickable { state.anatomyView = option }
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    BarRowList(
                        rows = focus.muscles.map { bar ->
                            BarRow(
                                label = bar.label,
                                percent = bar.percent,
                                // Each row is a weighted share - a compound lift credits
                                // its helpers at 40% - so the set counts across groups add
                                // up to more than the session total. Percent is the only
                                // figure that reads honestly next to "N sets".
                                valueLabel = "${bar.percent}%",
                                trendPercent = if (isEstimate) null else bar.trendPercent,
                                color = if (bar.percent < 25) BiSurface.Amber else BiSurface.Emerald
                            )
                        },
                        // A zero delta means there is no earlier window to compare
                        // against, not that volume held flat, so it stays hidden.
                        showTrend = !isEstimate && focus.muscles.any { it.trendPercent != 0 }
                    )
                }
            }
        }
    }
}

// =============================================================================
// 5. STRENGTH
// =============================================================================

private val COMPOUND_PREVIEW = listOf(
    "Bench Press", "Squat", "Deadlift", "Shoulder Press", "Lat Pulldown"
)

/**
 * Strength intelligence.
 *
 * The catalogue is real, the member's numbers are not yet - so the rows are
 * listed with their names and an explicit "Not started". A zero here would be
 * read as a regression, which is worse than an honest blank.
 */
@Composable
private fun StrengthCard(state: BodyIntelligenceState) {
    val hasRecords = state.input.hasSetLevelRecords

    BiCard(tint = BiSurface.Violet) {
        BiSectionHeader(
            icon = Icons.Default.FitnessCenter,
            title = "Strength Progress",
            subtitle = if (hasRecords) "Prev vs current, per exercise" else "Every lift starts at Not started",
            tint = BiSurface.Violet,
            trailing = {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF1EEFB))
                        .border(1.dp, Color(0xFFDED7F6), RoundedCornerShape(50.dp))
                        .clickable { state.openSheet(BodySheetTarget.Report(BodySection.STRENGTH)) }
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = BiSurface.Violet,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "History",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = BiSurface.Violet
                    )
                }
            }
        )
        Spacer(Modifier.height(10.dp))

        if (!hasRecords) {
            BiEmptyState(
                icon = Icons.Default.FitnessCenter,
                title = "No set-level records stored",
                message = "Weight and reps are not captured by check-ins, so no lift can be reported yet. The exercises below come from the gym catalogue and stay marked Not started until a set is logged.",
                tint = BiSurface.Violet,
                actionLabel = "Log a session",
                onAction = { state.openSheet(BodySheetTarget.Plan(BodyPlanKind.NEXT_ACTIVITY)) }
            )
            Spacer(Modifier.height(9.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(BodyIntelPalette.CardInset)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                COMPOUND_PREVIEW.forEach { name ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.InkSoft,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Not started",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = BodyIntelPalette.Faint
                        )
                    }
                    if (name != COMPOUND_PREVIEW.last()) DividerLine()
                }
            }
        } else {
            val rows = strengthRows(state.input.strengthSets)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(BodyIntelPalette.CardInset)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                TableRow(
                    cells = listOf("Exercise", "First", "Latest", "Change"),
                    weights = listOf(2.05f, 1.15f, 1.15f, 0.95f),
                    header = true
                )
                rows.forEach { row ->
                    DividerLine()
                    TableRow(
                        cells = listOf(row.name, row.previous, row.current, row.changeLabel),
                        weights = listOf(2.05f, 1.15f, 1.15f, 0.95f),
                        highlight = row.changePercent > 0
                    )
                }
            }
            Spacer(Modifier.height(7.dp))
            Text(
                text = "Heaviest set per exercise, first session against the latest this month.",
                fontSize = 10.5.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BodyIntelPalette.Faint
            )
        }
    }
}

/** One row of the strength table: the heaviest set at the start and end of the cycle. */
private data class StrengthRow(
    val name: String,
    val previous: String,
    val current: String,
    val changePercent: Int
) {
    val changeLabel: String get() = if (changePercent > 0) "+$changePercent%" else "$changePercent%"
}

private fun Double.trimKg(): String =
    if (this == toLong().toDouble()) toLong().toString() else String.format("%.1f", this)

/**
 * Collapses logged sets into a first-vs-latest top set per exercise.
 *
 * Bodyweight and timed movements keep a null load, so their change is reported
 * from reps rather than pretending a percentage exists.
 */
private fun strengthRows(sets: List<StrengthSetRecord>): List<StrengthRow> =
    sets.groupBy { it.exerciseId }.mapNotNull { (id, rows) ->
        val byDay = rows.groupBy { it.day }.toSortedMap()
        if (byDay.size < 2) return@mapNotNull null
        val firstDay = byDay.entries.first()
        val lastDay = byDay.entries.last()
        val name = ExerciseCatalog.label(id)
        val firstLoad = firstDay.value.maxOfOrNull { it.loadKg ?: 0.0 } ?: 0.0
        val lastLoad = lastDay.value.maxOfOrNull { it.loadKg ?: 0.0 } ?: 0.0
        if (firstLoad <= 0.0 || lastLoad <= 0.0) {
            // No external load: report the work itself.
            StrengthRow(
                name = name,
                previous = "x${firstDay.value.maxOf { it.reps }}",
                current = "x${lastDay.value.maxOf { it.reps }}",
                changePercent = 0
            )
        } else {
            StrengthRow(
                name = name,
                previous = "${firstLoad.trimKg()} x${firstDay.value.maxOf { it.reps }}",
                current = "${lastLoad.trimKg()} x${lastDay.value.maxOf { it.reps }}",
                changePercent = (((lastLoad - firstLoad) / firstLoad) * 100).roundToInt()
            )
        }
    }.sortedByDescending { it.changePercent }
    .take(4)

// =============================================================================
// 6. BODY MEASUREMENTS
// =============================================================================

private val MEASUREMENT_METRICS = listOf(
    BodyMetric.CHEST, BodyMetric.WAIST, BodyMetric.ARM
)

/**
 * Girth measurements, kept separate from composition because they answer a
 * different question. Only sites with a stored value are shown; the rest are
 * listed as recordable rather than rendered as zero.
 */
@Composable
private fun MeasurementsCard(view: BodyIntelligenceView) {
    val recorded = MEASUREMENT_METRICS.mapNotNull { metric -> view.reading(metric)?.let { metric to it } }

    BiCard(tint = BiSurface.Cyan) {
        BiSectionHeader(
            icon = Icons.Default.CenterFocusStrong,
            title = "Body Measurements",
            subtitle = if (recorded.isEmpty()) "Chest, waist and arm can be stored" else "Stored girth records",
            tint = BiSurface.Cyan
        )
        Spacer(Modifier.height(10.dp))

        if (recorded.isEmpty()) {
            BiEmptyState(
                icon = Icons.Default.CenterFocusStrong,
                title = "No measurements on file",
                message = "Tape measurements are entered by the trainer or in the member app. Nothing here is estimated from weight or BMI, so the card stays blank until one is stored.",
                tint = BiSurface.Cyan
            )
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                recorded.forEach { (metric, reading) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(BiSurface.TileFill)
                            .border(1.dp, Color.White, RoundedCornerShape(14.dp))
                            .padding(horizontal = 8.dp, vertical = 9.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = metric.label,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.Muted
                        )
                        Text(
                            text = reading.valueLabel,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = BodyIntelPalette.Ink
                        )
                        if (!reading.deltaLabel.isNullOrBlank()) {
                            Text(
                                text = reading.deltaLabel,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Black,
                                color = if (reading.deltaIsFavourable) BiSurface.EmeraldDeep else BiSurface.Rose
                            )
                        }
                    }
                }
            }
        }
    }
}
