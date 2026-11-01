package com.example.badnewgym.feature.bodyprogress.presentation.intelligence

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import com.example.badnewgym.feature.bodyprogress.domain.catalog.ExerciseCatalog
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.AnatomyRegions
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.AnatomyView
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyDashboardAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyDashboardAnalytics.DayState
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyInsight
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyIntelligenceAnalytics
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyIntelligenceView
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.BodyWidget
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.CompositionReading
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.DayMarker
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.InsightTone
import com.example.badnewgym.feature.bodyprogress.domain.intelligence.PrivacyCategory
import com.example.badnewgym.feature.bodyprogress.domain.model.Exercise
import com.example.badnewgym.feature.bodyprogress.domain.model.FitnessTestKind
import com.example.badnewgym.feature.bodyprogress.domain.model.MuscleGroup
import kotlin.math.roundToInt

/**
 * Body Intelligence dashboard.
 *
 * One continuous scrollable page: the identity header, the horizontal chip nav,
 * then all eight dense sections stacked vertically. Tapping a chip scrolls to
 * its section (like anchor links) rather than replacing the screen.
 *
 * Every section renders only what the privacy-filtered [BodyIntelligenceView]
 * and the member's ledger contain. Where a reference screen shows a value that
 * this app has no record for (heart rate, VO2 max, device steps) the section
 * states "Not recorded" instead of printing a plausible number.
 */
fun LazyListScope.bodyIntelligenceItems(
    state: BodyIntelligenceState,
    listState: LazyListState,
    onPrivacyClick: () -> Unit = { state.openSheet(BodySheetTarget.Privacy) }
) {
    item(key = "bi_identity") {
        BodyIdentityHeader(view = state.view, onPrivacyClick = onPrivacyClick)
    }

    item(key = "bi_chips") {
        BodySectionChips(
            state = state,
            listState = listState,
            onPrivacyClick = onPrivacyClick
        )
    }

    // Section keys for chip-to-section scrolling. Order matches the item list
    // below: 0 = identity, 1 = chips, then sections 2..9.
    val sectionItemIndex = { section: BodySection -> 2 + section.ordinal }

    BodySection.entries.forEach { section ->
        item(key = "bi_section_${section.name}") {
            when (section) {
                BodySection.BODY_MAP -> this@bodyIntelligenceItems.bodyMapSection(state)
                BodySection.COMPOSITION -> this@bodyIntelligenceItems.compositionSection(state)
                BodySection.STRENGTH -> this@bodyIntelligenceItems.strengthSection(state)
                BodySection.FOCUS -> this@bodyIntelligenceItems.focusSection(state)
                BodySection.ACTIVITY -> this@bodyIntelligenceItems.activitySection(state)
                BodySection.ASSESSMENT -> this@bodyIntelligenceItems.assessmentSection(state)
                BodySection.CONSISTENCY -> this@bodyIntelligenceItems.consistencySection(state)
                BodySection.INSIGHTS -> this@bodyIntelligenceItems.insightsSection(state)
            }
        }
    }

    item(key = "bi_footnote") {
        Text(
            text = "Measurements are stored member records. Session volume, steps and calories are " +
                "template-derived estimates from recorded training days and are labelled as such. " +
                "Nothing is invented to fill an empty section.",
            fontSize = 9.sp,
            lineHeight = 13.sp,
            color = BodyIntelPalette.Faint,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }

    item(key = "bi_bottom_space") { Spacer(modifier = Modifier.height(4.dp)) }
}

// ------------------------------------------------------------------- identity

@Composable
private fun BodyIdentityHeader(view: BodyIntelligenceView, onPrivacyClick: () -> Unit) {
    val profile = view.input.profile
    val hidden = view.privacy.hidden.size

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = BodyIntelPalette.Card,
        border = BorderStroke(1.dp, BodyIntelPalette.Hairline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 11.dp, vertical = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(BodyIntelPalette.Emerald, BodyIntelPalette.EmeraldDeep)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initialsOf(profile?.name ?: view.input.memberName),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
                Spacer(Modifier.width(9.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "BODY INTELLIGENCE",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black,
                        color = BodyIntelPalette.Emerald,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = profile?.name ?: view.input.memberName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = BodyIntelPalette.Ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = buildString {
                            append(profile?.memberId ?: view.input.memberId)
                            val facts = profile?.factsLabel.orEmpty()
                            if (facts.isNotBlank()) append(" · ").append(facts)
                        },
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BodyIntelPalette.Muted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(BodyIntelPalette.CardSoft)
                        .border(1.dp, BodyIntelPalette.Hairline, RoundedCornerShape(50))
                        .clickable { onPrivacyClick() }
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = BodyIntelPalette.EmeraldDeep,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = if (hidden == 0) "Privacy" else "Private $hidden",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = BodyIntelPalette.InkSoft
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                profile?.plan?.takeIf { it.isNotBlank() }?.let {
                    IdentityChip(it, BodyIntelPalette.Amber, BodyIntelPalette.AmberWash)
                }
                val goal = profile?.goalLabel?.takeIf { it.isNotBlank() } ?: view.goal.label
                IdentityChip("Goal $goal", BodyIntelPalette.EmeraldDeep, BodyIntelPalette.EmeraldWash)
                profile?.dueAmount?.takeIf { it.isNotBlank() }?.let { amount ->
                    val date = profile.dueDate?.takeIf { it.isNotBlank() }
                    IdentityChip(
                        if (date == null) "$amount due" else "$amount due $date",
                        BodyIntelPalette.Rose,
                        BodyIntelPalette.RoseWash
                    )
                }
            }
        }
    }
}

@Composable
private fun IdentityChip(label: String, textColor: Color, background: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            maxLines = 1
        )
    }
}

private fun initialsOf(name: String): String = name
    .split(' ')
    .filter { it.isNotBlank() }
    .take(2)
    .joinToString("") { it.first().uppercase() }
    .ifBlank { "BG" }

// ---------------------------------------------------------------- chip nav

@Composable
private fun BodySectionChips(
    state: BodyIntelligenceState,
    listState: LazyListState,
    onPrivacyClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    // Matches item order: 0 = identity, 1 = chips, then sections 2..9.
    val sectionItemIndex = { section: BodySection -> 2 + section.ordinal }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        BodySection.entries.forEach { section ->
            val selected = section == state.section
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (selected) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.Card)
                    .border(
                        width = 1.dp,
                        color = if (selected) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.Hairline,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable {
                        state.section = section
                        scope.launch { listState.animateScrollToItem(sectionItemIndex(section)) }
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = section.label,
                    fontSize = 9.5.sp,
                    fontWeight = if (selected) FontWeight.Black else FontWeight.Bold,
                    color = if (selected) Color.White else BodyIntelPalette.InkSoft,
                    maxLines = 1
                )
            }
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(BodyIntelPalette.CardSoft)
                .border(1.dp, BodyIntelPalette.Hairline, RoundedCornerShape(50))
                .clickable { onPrivacyClick() }
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = BodyIntelPalette.EmeraldDeep,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = "Privacy",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = BodyIntelPalette.InkSoft
            )
        }
    }
}

// ------------------------------------------------------------------ card shell

@Composable
private fun SectionCard(
    title: String,
    subtitle: String? = null,
    icon: ImageVector = Icons.Filled.Insights,
    trailingLabel: String? = null,
    onTrailing: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = BodyIntelPalette.Card,
        border = BorderStroke(1.dp, BodyIntelPalette.Hairline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 11.dp, vertical = 9.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(BodyIntelPalette.EmeraldWash),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BodyIntelPalette.EmeraldDeep,
                        modifier = Modifier.size(13.dp)
                    )
                }
                Spacer(Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = BodyIntelPalette.Ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Muted,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (trailingLabel != null && onTrailing != null) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(BodyIntelPalette.CardSoft)
                            .clickable { onTrailing() }
                            .padding(horizontal = 7.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = trailingLabel,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.EmeraldDeep,
                            maxLines = 1
                        )
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = BodyIntelPalette.EmeraldDeep,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

private fun LazyListScope.sectionItem(key: String, content: @Composable () -> Unit) {
    item(key = key) { content() }
}

// ------------------------------------------------------------- 01 body map

private fun LazyListScope.bodyMapSection(state: BodyIntelligenceState) {
    val view = state.view
    val focus = state.focus

    sectionItem("map_body") {
        SectionCard(
            title = "Human Body Map",
            subtitle = "Tap a muscle group or use the chips below",
            icon = Icons.Filled.AccessibilityNew,
            trailingLabel = state.anatomyView.label,
            onTrailing = {
                state.anatomyView = if (state.anatomyView == AnatomyView.FRONT) AnatomyView.BACK
                else AnatomyView.FRONT
            }
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(BodyIntelPalette.CardInset)
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                AnatomyView.entries.forEach { candidate ->
                    val selected = candidate == state.anatomyView
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (selected) BodyIntelPalette.Emerald else Color.Transparent)
                            .clickable { state.anatomyView = candidate }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = candidate.label,
                            fontSize = 9.sp,
                            fontWeight = if (selected) FontWeight.Black else FontWeight.SemiBold,
                            color = if (selected) Color.White else BodyIntelPalette.Muted
                        )
                    }
                }
            }
            Spacer(Modifier.height(7.dp))
            BodyAnatomyMap(
                view = state.anatomyView,
                selected = state.selectedMuscle,
                onSelect = { state.selectedMuscle = it }
            )
            Spacer(Modifier.height(7.dp))
            BodyMuscleChips(
                view = state.anatomyView,
                selected = state.selectedMuscle,
                onSelect = { state.selectedMuscle = it }
            )
        }
    }

    sectionItem("map_detail") {
        val muscle = state.selectedMuscle
        val share = focus?.muscles?.firstOrNull {
            it.label.equals(muscle.label, ignoreCase = true) ||
                it.label.contains(muscle.label.take(4), ignoreCase = true)
        }
        SectionCard(
            title = muscle.label,
            subtitle = "Selected muscle group",
            icon = Icons.Filled.FitnessCenter,
            trailingLabel = "Exercises",
            onTrailing = { state.openSheet(BodySheetTarget.MuscleDetail(muscle)) }
        ) {
            if (share == null) {
                BodyIntelInfo(
                    "No training volume is recorded against this muscle group in the current block. " +
                        "It appears on the map so it can be selected, not to imply work was done."
                )
            } else {
                KpiStrip(
                    items = listOf(
                        Kpi("Working sets", share.sets.toString()),
                        Kpi("Share", "${share.percent}%"),
                        Kpi("Volume", BodyDashboardAnalytics.volumeLabel(share.volumeKg)),
                        Kpi(
                            "Trend",
                            if (share.trendPercent >= 0) "+${share.trendPercent}%" else "${share.trendPercent}%",
                            tone = if (share.trendPercent >= 0) KpiTone.POSITIVE else KpiTone.WARNING
                        )
                    )
                )
                Spacer(Modifier.height(7.dp))
                BodyIntelInfo(
                    "Volume counts a compound lift's secondary muscles at 40% of a set so a single " +
                        "press is not counted as a full set for every muscle it touches."
                )
            }
        }
    }

    sectionItem("map_balance") {
        SectionCard(
            title = "Muscle Balance",
            subtitle = "Working-set share across the last block",
            icon = Icons.Filled.ShowChart
        ) {
            val muscles = state.focus?.muscles.orEmpty()
            if (muscles.isEmpty()) {
                BodyIntelEmpty(
                    title = "No volume recorded",
                    body = "Muscle balance appears after the first completed session is recorded."
                )
            } else {
                val maxSets = muscles.maxOf { it.sets }.coerceAtLeast(1)
                BarRowList(
                    rows = muscles.take(6).map { bar ->
                        BarRow(
                            label = bar.label,
                            percent = (bar.sets * 100 / maxSets),
                            valueLabel = "${bar.sets}",
                            trendPercent = bar.trendPercent
                        )
                    }
                )
            }
        }
    }

    sectionItem("map_week") {
        SectionCard(
            title = "This Week",
            subtitle = "Recorded training days",
            icon = Icons.Filled.Timeline
        ) {
            val week = state.focus?.week.orEmpty()
            WeekFrequencyStrip(marks = week.map { DayMark(it.label, it.trained, it.missed) })
            Spacer(Modifier.height(6.dp))
            Text(
                text = "${week.count { it.trained }} of 7 days trained",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = BodyIntelPalette.InkSoft
            )
        }
    }

    sectionItem("map_actions") {
        FooterActions(listOf("View Muscle Detail", "Log Measurement", "Body Report")) { index ->
            when (index) {
                0 -> state.openSheet(BodySheetTarget.MuscleDetail(state.selectedMuscle))
                1 -> state.openSheet(BodySheetTarget.Plan(BodyPlanKind.NEXT_ACTIVITY))
                else -> state.openSheet(BodySheetTarget.Report(BodySection.BODY_MAP))
            }
        }
    }
}

// ----------------------------------------------------------- 02 composition

private fun LazyListScope.compositionSection(state: BodyIntelligenceState) {
    val view = state.view

    if (view.readings.isEmpty()) {
        sectionItem("comp_empty") {
            SectionCard(
                title = "Body Composition",
                subtitle = "Stored member measurements",
                icon = Icons.Filled.Tune
            ) {
                BodyIntelEmpty(
                    title = if (view.hiddenCategories.isEmpty()) "No measurements recorded"
                    else "Measurements are private",
                    body = if (view.hiddenCategories.isEmpty())
                        "Weight, body fat, muscle mass and BMI appear once the member records them."
                    else
                        "Every composition metric is currently hidden. The records still exist and " +
                            "return as soon as they are made visible.",
                    action = if (view.hiddenCategories.isEmpty()) null else "Manage privacy",
                    onAction = { state.openSheet(BodySheetTarget.Privacy) }
                )
            }
        }
        return
    }

    sectionItem("comp_primary") {
        val reading = state.activeReading ?: return@sectionItem
        SectionCard(
            title = "Body Composition",
            subtitle = "${reading.sourceLabel} · ${reading.metric.label}",
            icon = Icons.Filled.Tune,
            trailingLabel = "Trend",
            onTrailing = { state.openSheet(BodySheetTarget.MetricDetail(reading.metric)) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                view.readings.forEach { candidate ->
                    val selected = candidate.metric == reading.metric
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (selected) BodyIntelPalette.EmeraldWash else BodyIntelPalette.CardInset)
                            .border(
                                1.dp,
                                if (selected) BodyIntelPalette.EmeraldEdge else BodyIntelPalette.Hairline,
                                RoundedCornerShape(50)
                            )
                            .clickable { state.selectMetric(candidate.metric) }
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = candidate.metric.label,
                            fontSize = 8.5.sp,
                            fontWeight = if (selected) FontWeight.Black else FontWeight.SemiBold,
                            color = if (selected) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.Muted,
                            maxLines = 1
                        )
                    }
                }
            }
            Spacer(Modifier.height(9.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = reading.valueLabel,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = BodyIntelPalette.Ink
                )
                Spacer(Modifier.width(7.dp))
                reading.deltaLabel?.let { delta ->
                    Text(
                        text = delta,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = if (reading.deltaIsFavourable) BodyIntelPalette.EmeraldDeep
                        else BodyIntelPalette.Rose,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (reading.deltaIsFavourable) BodyIntelPalette.EmeraldWash
                                else BodyIntelPalette.RoseWash
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            reading.goalLabel?.let {
                Text(
                    text = it,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BodyIntelPalette.Muted
                )
            }
            Spacer(Modifier.height(9.dp))
            if (reading.hasTrend) {
                TrendShapeChart(
                    points = reading.trend,
                    highlight = BodyIntelPalette.Emerald,
                    height = 92
                )
            } else {
                BodyIntelInfo("This metric has a single recorded value, so no trend line is drawn.")
            }
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SourceLabel(reading.sourceLabel)
                Spacer(Modifier.width(6.dp))
                PrivacyBadge(reading.privacy)
            }
        }
    }

    sectionItem("comp_all") {
        SectionCard(
            title = "All Metrics",
            subtitle = "${view.readings.size} recorded · tap for full history",
            icon = Icons.Filled.ShowChart
        ) {
            view.readings.forEachIndexed { index, reading ->
                if (index > 0) Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { state.openSheet(BodySheetTarget.MetricDetail(reading.metric)) }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = reading.metric.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.Ink
                        )
                        Text(
                            text = reading.goalLabel ?: "No goal set",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Faint,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    reading.deltaLabel?.let { delta ->
                        Text(
                            text = delta,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (reading.deltaIsFavourable) BodyIntelPalette.EmeraldDeep
                            else BodyIntelPalette.Rose,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                    }
                    Text(
                        text = reading.valueLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = BodyIntelPalette.Ink
                    )
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = BodyIntelPalette.Faint,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }

    sectionItem("comp_actions") {
        FooterActions(listOf("Detailed Trend", "Add Measurement", "Body Report")) { index ->
            val reading = state.activeReading
            when (index) {
                0 -> reading?.let { state.openSheet(BodySheetTarget.MetricDetail(it.metric)) }
                1 -> state.openSheet(BodySheetTarget.Plan(BodyPlanKind.NEXT_ACTIVITY))
                else -> state.openSheet(BodySheetTarget.Report(BodySection.COMPOSITION))
            }
        }
    }
}

// -------------------------------------------------------------- 03 strength

private fun LazyListScope.strengthSection(state: BodyIntelligenceState) {
    val ledger = state.ledger
    val bestByExercise = ledger.sessions
        .flatMap { it.sets }
        .filter { it.loadKg != null && it.reps != null }
        .groupBy { it.exerciseId }
        .mapValues { (_, sets) ->
            sets.maxByOrNull { (it.loadKg ?: 0.0) * (it.reps ?: 0) }
        }

    sectionItem("strength_kpi") {
        SectionCard(
            title = "Strength Overview",
            subtitle = if (state.input.hasSetLevelRecords) "Recorded sets" else "Template estimate · no logged sets",
            icon = Icons.Filled.FitnessCenter
        ) {
            val bench = bestByExercise["bench_press_barbell"]
            val oneRm = bench?.let { set ->
                val load = set.loadKg ?: 0.0
                val reps = set.reps ?: 1
                if (reps <= 1) load else load * (1.0 + reps / 30.0)
            }
            KpiStrip(
                items = listOf(
                    Kpi("Exercises", bestByExercise.size.toString()),
                    Kpi("Working sets", ledger.totalSets.toString()),
                    Kpi("Volume", BodyDashboardAnalytics.volumeLabel(ledger.totalVolumeKg)),
                    Kpi("Bench e1RM", if (oneRm == null) "-" else "${oneRm.roundToInt()} kg")
                )
            )
            Spacer(Modifier.height(6.dp))
            BodyIntelInfo(
                "Loads come from the session template applied to this member's recorded training days. " +
                    "They are estimates until a set is logged with an actual load."
            )
        }
    }

    sectionItem("strength_filters") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            StrengthFilter.entries.forEach { filter ->
                val selected = filter == state.strengthFilter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (selected) BodyIntelPalette.Emerald else BodyIntelPalette.Card)
                        .border(
                            1.dp,
                            if (selected) BodyIntelPalette.Emerald else BodyIntelPalette.Hairline,
                            RoundedCornerShape(50)
                        )
                        .clickable { state.strengthFilter = filter }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = filter.label,
                        fontSize = 9.sp,
                        fontWeight = if (selected) FontWeight.Black else FontWeight.SemiBold,
                        color = if (selected) Color.White else BodyIntelPalette.InkSoft
                    )
                }
            }
        }
    }

    sectionItem("strength_list") {
        val exercises = ExerciseCatalog.active()
            .filter { state.strengthFilter.matches(it) }
            .filter { !it.isCardio }

        SectionCard(
            title = "Exercise Intelligence",
            subtitle = "${exercises.size} in ${state.strengthFilter.label.lowercase()}",
            icon = Icons.Filled.Timeline,
            trailingLabel = if (state.showAllExercises) "Less" else "All",
            onTrailing = { state.showAllExercises = !state.showAllExercises }
        ) {
            val visible = if (state.showAllExercises) exercises else exercises.take(5)
            visible.forEachIndexed { index, exercise ->
                if (index > 0) Divider()
                val best = bestByExercise[exercise.id]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { state.openSheet(BodySheetTarget.ExerciseDetail(exercise)) }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = exercise.name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.Ink,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${exercise.primaryMuscles.firstOrNull()?.label ?: "Full body"} · ${exercise.pattern.label}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Muted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (best == null) {
                        StatusPill(
                            text = "Not started",
                            color = BodyIntelPalette.Amber,
                            wash = BodyIntelPalette.AmberWash
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${best.loadKg?.let { BodyDashboardAnalytics.fmt(it) } ?: "BW"} kg × ${best.reps}",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                color = BodyIntelPalette.Ink
                            )
                            Text(
                                text = "Best recorded set",
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = BodyIntelPalette.Faint
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = BodyIntelPalette.Faint,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
            if (exercises.isEmpty()) {
                BodyIntelEmpty(
                    title = "No exercises in this filter",
                    body = "Pick another muscle group to see its exercises."
                )
            }
        }
    }

    sectionItem("strength_actions") {
        FooterActions(listOf("View All Exercises", "Log Workout", "Detailed Report")) { index ->
            when (index) {
                0 -> state.showAllExercises = true
                1 -> state.openSheet(BodySheetTarget.Plan(BodyPlanKind.NEXT_ACTIVITY))
                else -> state.openSheet(BodySheetTarget.Report(BodySection.STRENGTH))
            }
        }
    }
}

// ----------------------------------------------------------------- 04 focus

private fun LazyListScope.focusSection(state: BodyIntelligenceState) {
    val focus = state.focus

    if (focus == null || !focus.hasData) {
        sectionItem("focus_empty") {
            SectionCard(
                title = "Muscle & Training Focus",
                subtitle = "Where the training effort went",
                icon = Icons.Filled.ShowChart
            ) {
                BodyIntelEmpty(
                    title = "No training recorded",
                    body = "Muscle volume, split balance and movement coverage appear after the first recorded session."
                )
            }
        }
        return
    }

    sectionItem("focus_kpi") {
        SectionCard(
            title = "Training Load",
            subtitle = "${focus.trainingDays} training days · ${focus.averagePerWeek}/week average",
            icon = Icons.Filled.FitnessCenter
        ) {
            KpiStrip(
                items = listOf(
                    Kpi("Sets", focus.totalSets.toString()),
                    Kpi("Days", focus.trainingDays.toString()),
                    Kpi("Avg", "${focus.averageMinutes}m"),
                    Kpi("Volume", BodyDashboardAnalytics.volumeLabel(focus.totalVolumeKg)),
                    Kpi(
                        "vs 1st half",
                        if (focus.vsLastMonthDays >= 0) "+${BodyDashboardAnalytics.fmt(focus.vsLastMonthDays)}"
                        else BodyDashboardAnalytics.fmt(focus.vsLastMonthDays),
                        caption = "days/wk",
                        tone = if (focus.vsLastMonthDays >= 0) KpiTone.POSITIVE else KpiTone.WARNING
                    )
                )
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SourceLabel("Derived from recorded days")
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "${focus.planCompletionPercent}% of plan",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = BodyIntelPalette.EmeraldDeep
                )
            }
        }
    }

    sectionItem("focus_muscles") {
        SectionCard(
            title = "Muscle Volume",
            subtitle = "Share of working sets · trend vs previous block",
            icon = Icons.Filled.ShowChart
        ) {
            val maxSets = focus.muscles.maxOfOrNull { it.sets }?.coerceAtLeast(1) ?: 1
            BarRowList(
                rows = focus.muscles.map { bar ->
                    BarRow(bar.label, bar.sets * 100 / maxSets, "${bar.sets}", bar.trendPercent)
                }
            )
        }
    }

    sectionItem("focus_split") {
        SectionCard(
            title = "Push / Pull / Legs",
            subtitle = "Session split balance",
            icon = Icons.Filled.Tune
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DonutChart(
                    segments = listOf(
                        DonutSegment("Push", focus.split[0].percent, BodyIntelPalette.Emerald),
                        DonutSegment("Pull", focus.split[1].percent, BodyIntelPalette.Blue),
                        DonutSegment("Legs", focus.split[2].percent, BodyIntelPalette.Violet)
                    ),
                    centerTop = "${focus.trainingDays}",
                    centerBottom = "sessions"
                )
                Spacer(Modifier.width(12.dp))
                DonutLegend(
                    segments = listOf(
                        DonutSegment("Push", focus.split[0].percent, BodyIntelPalette.Emerald),
                        DonutSegment("Pull", focus.split[1].percent, BodyIntelPalette.Blue),
                        DonutSegment("Legs", focus.split[2].percent, BodyIntelPalette.Violet)
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = focus.split.joinToString(" · ") { "${it.label} ${it.sessions}" } + " sessions",
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Medium,
                color = BodyIntelPalette.Muted
            )
        }
    }

    sectionItem("focus_coverage") {
        SectionCard(
            title = "Movement Pattern Coverage",
            subtitle = "${focus.coverage.count { it.covered }} of ${focus.coverage.size} patterns trained",
            icon = Icons.Filled.Timeline
        ) {
            focus.coverage.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    rowItems.forEach { pattern ->
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(9.dp))
                                .background(
                                    if (pattern.covered) BodyIntelPalette.EmeraldWash
                                    else BodyIntelPalette.CardInset
                                )
                                .border(
                                    1.dp,
                                    if (pattern.covered) BodyIntelPalette.EmeraldEdge
                                    else BodyIntelPalette.Hairline,
                                    RoundedCornerShape(9.dp)
                                )
                                .padding(horizontal = 5.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pattern.covered) BodyIntelPalette.Emerald
                                        else BodyIntelPalette.Faint
                                    )
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = pattern.label,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (pattern.covered) BodyIntelPalette.EmeraldDeep
                                else BodyIntelPalette.Muted,
                                maxLines = 1
                            )
                        }
                    }
                    repeat(3 - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(5.dp))
            }
        }
    }

    sectionItem("focus_week") {
        SectionCard(
            title = "Weekly Frequency",
            subtitle = "${focus.sessionsThisWeek} sessions this week",
            icon = Icons.Filled.Timeline
        ) {
            WeekFrequencyStrip(marks = focus.week.map { DayMark(it.label, it.trained, it.missed) })
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LegendDot(BodyIntelPalette.Emerald, "Trained")
                LegendDot(BodyIntelPalette.Rose.copy(alpha = 0.5f), "Missed")
                LegendDot(BodyIntelPalette.CardSoft, "Rest")
            }
        }
    }

    sectionItem("focus_goal") {
        SectionCard(
            title = "Goal Alignment",
            subtitle = focus.goalTitle,
            icon = Icons.Filled.Assessment
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RingChart(
                    percent = focus.goalPercent,
                    centerValue = "${focus.goalPercent}%",
                    centerLabel = "aligned",
                    size = 88.dp,
                    stroke = 9.dp
                )
                Spacer(Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    focus.goalBullets.forEach { bullet ->
                        BulletLine(bullet.text, bullet.ok)
                    }
                }
            }
        }
    }

    sectionItem("focus_insights") {
        SectionCard(
            title = "What This Means",
            subtitle = "Derived observations, not predictions",
            icon = Icons.Filled.Insights,
            trailingLabel = "Details",
            onTrailing = { state.openSheet(BodySheetTarget.Report(BodySection.FOCUS)) }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                focus.insights.forEach { text -> BulletLine(text, null) }
            }
        }
    }

    sectionItem("focus_actions") {
        FooterActions(listOf("Detailed Focus", "Plan Next Activity", "Detailed Report")) { index ->
            when (index) {
                0, 2 -> state.openSheet(BodySheetTarget.Report(BodySection.FOCUS))
                else -> state.openSheet(BodySheetTarget.Plan(BodyPlanKind.NEXT_ACTIVITY))
            }
        }
    }
}

// -------------------------------------------------------------- 05 activity

private fun LazyListScope.activitySection(state: BodyIntelligenceState) {
    val activity = state.activity

    if (activity == null || !activity.hasData) {
        sectionItem("activity_empty") {
            SectionCard(
                title = "Activity & Endurance",
                subtitle = "Recorded session time",
                icon = Icons.Filled.ShowChart
            ) {
                BodyIntelEmpty(
                    title = "No activity recorded",
                    body = "Duration, distance and endurance metrics appear after the first recorded session."
                )
            }
        }
        return
    }

    sectionItem("activity_kpi") {
        SectionCard(
            title = "Activity Summary",
            subtitle = "${activity.activeDays} active days · ${BodyIntelligenceAnalytics.minutesLabel(activity.totalMinutes)}",
            icon = Icons.Filled.ShowChart,
            trailingLabel = state.activityRange.label,
            onTrailing = {
                val next = ActivityRange.entries[(state.activityRange.ordinal + 1) % ActivityRange.entries.size]
                state.activityRange = next
            }
        ) {
            KpiStrip(
                items = listOf(
                    Kpi("Steps", compactNumber(activity.totalSteps), caption = "est."),
                    Kpi("Distance", "${BodyDashboardAnalytics.fmt(activity.totalDistanceKm)} km"),
                    Kpi("Active", "${activity.totalMinutes}m"),
                    Kpi("Calories", compactNumber(activity.calories), caption = "est."),
                    Kpi("Days", "${activity.activeDays}/${activity.windowDays}")
                )
            )
            Spacer(Modifier.height(6.dp))
            BodyIntelInfo(
                "Steps and calories are duration-based estimates, not device measurements. " +
                    "Distance is taken from recorded cardio sessions plus a walking allowance."
            )
        }
    }

    sectionItem("activity_mix") {
        SectionCard(
            title = "Activity Mix",
            subtitle = "Recorded types",
            icon = Icons.Filled.Tune
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                activity.types.forEach { type ->
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(BodyIntelPalette.CardInset)
                            .border(1.dp, BodyIntelPalette.Hairline, RoundedCornerShape(10.dp))
                            .padding(horizontal = 9.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = type.label,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.Muted
                        )
                        Text(
                            text = type.valueLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = BodyIntelPalette.Ink
                        )
                        Text(
                            text = "${type.sessions} sessions",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Faint
                        )
                    }
                }
            }
            Spacer(Modifier.height(9.dp))
            val chart = activityChart(activity, state.activityRange)
            LineTrendChart(
                series = listOf(TrendLine(chart.title, chart.points, BodyIntelPalette.Emerald)),
                labels = chart.labels,
                height = 104.dp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = chart.caption,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = BodyIntelPalette.Muted
            )
        }
    }

    sectionItem("activity_endurance") {
        SectionCard(
            title = "Endurance Metrics",
            subtitle = "Only recorded endurance values are shown",
            icon = Icons.Filled.Timeline
        ) {
            val pace = activity.averagePaceSecPerKm
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                EnduranceTile(
                    label = "Avg pace",
                    value = pace?.let { seconds ->
                        "%d:%02d".format(seconds / 60, seconds % 60)
                    } ?: "Not recorded",
                    unit = if (pace != null) "min/km" else "no cardio pace",
                    modifier = Modifier.weight(1f)
                )
                EnduranceTile(
                    label = "Avg heart rate",
                    value = activity.averageHeartRate?.let { "$it bpm" } ?: "Not recorded",
                    unit = "no device link",
                    modifier = Modifier.weight(1f)
                )
                EnduranceTile(
                    label = "VO2 max",
                    value = activity.vo2Max?.let { BodyDashboardAnalytics.fmt(it) } ?: "Not recorded",
                    unit = "needs a lab test",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(6.dp))
            BodyIntelInfo(
                "Heart rate and VO2 max stay empty rather than being estimated from pace: both require " +
                    "a device or a supervised test."
            )
        }
    }

    sectionItem("activity_recent") {
        SectionCard(
            title = "Recent Activity",
            subtitle = "Last ${activity.recent.size} recorded sessions",
            icon = Icons.Filled.DirectionsRun,
            trailingLabel = "All",
            onTrailing = { state.openSheet(BodySheetTarget.Report(BodySection.ACTIVITY)) }
        ) {
            activity.recent.forEachIndexed { index, session ->
                if (index > 0) Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BodyIntelPalette.EmeraldWash),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DirectionsRun,
                            contentDescription = null,
                            tint = BodyIntelPalette.EmeraldDeep,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(Modifier.width(7.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = session.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.Ink
                        )
                        Text(
                            text = "${session.dateLabel} · ${session.durationLabel}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Muted
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = session.valueLabel,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = BodyIntelPalette.EmeraldDeep
                        )
                        Text(
                            text = "${session.calories} kcal est.",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Faint
                        )
                    }
                }
            }
        }
    }

    sectionItem("activity_calories") {
        SectionCard(
            title = "Calories Burned",
            subtitle = "Estimated split by activity",
            icon = Icons.Filled.ShowChart
        ) {
            val palette = listOf(
                BodyIntelPalette.Emerald,
                BodyIntelPalette.Blue,
                BodyIntelPalette.Violet,
                BodyIntelPalette.Amber
            )
            val segments = activity.calorieShares.mapIndexed { index, share ->
                DonutSegment(share.label, share.percent, palette[index % palette.size])
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                DonutChart(
                    segments = segments,
                    centerTop = compactNumber(activity.calories),
                    centerBottom = "kcal est."
                )
                Spacer(Modifier.width(12.dp))
                DonutLegend(segments = segments, modifier = Modifier.weight(1f))
            }
        }
    }

    sectionItem("activity_goal") {
        SectionCard(
            title = "Weekly Distance Goal",
            subtitle = if (activity.weeklyGoalKm <= 0.0) "No goal configured"
            else "Target ${BodyDashboardAnalytics.fmt(activity.weeklyGoalKm)} km / week",
            icon = Icons.Filled.Assessment
        ) {
            if (activity.weeklyGoalKm <= 0.0) {
                BodyIntelInfo("A weekly distance goal appears once the member sets one with their trainer.")
            } else {
                ProgressRow(
                    label = "Running distance",
                    percent = activity.goalPercent.coerceAtMost(100),
                    valueLabel = "${BodyDashboardAnalytics.fmt(activity.cardioDistanceKm)} / " +
                        "${BodyDashboardAnalytics.fmt(activity.weeklyGoalKm * 4)} km"
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = if (activity.goalPercent >= 100) "Target met for this block."
                    else "${100 - activity.goalPercent}% remaining - one more cardio session closes most of the gap.",
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BodyIntelPalette.InkSoft
                )
            }
        }
    }

    sectionItem("activity_actions") {
        FooterActions(listOf("Full Activity", "Plan Next Activity", "Detailed Report")) { index ->
            when (index) {
                0, 2 -> state.openSheet(BodySheetTarget.Report(BodySection.ACTIVITY))
                else -> state.openSheet(BodySheetTarget.Plan(BodyPlanKind.NEXT_ACTIVITY))
            }
        }
    }
}

private data class ActivityChart(
    val title: String,
    val labels: List<String>,
    val points: List<Float>,
    val caption: String
)

private fun activityChart(
    activity: BodyDashboardAnalytics.ActivityDashboard,
    range: ActivityRange
): ActivityChart = when (range) {
    ActivityRange.DAY -> ActivityChart(
        title = "Minutes",
        labels = activity.recent.reversed().map { it.dateLabel.removePrefix("Day ") },
        points = activity.recent.reversed().map { it.durationLabel.removeSuffix(" min").toFloatOrNull() ?: 0f },
        caption = "Per-session minutes, last ${activity.recent.size} sessions"
    )
    ActivityRange.MONTH -> ActivityChart(
        title = "Calories",
        labels = activity.weekly.map { it.label },
        points = activity.weekly.map { (it.value * 55).toFloat() },
        caption = "Estimated weekly calories from recorded distance"
    )
    ActivityRange.WEEK -> ActivityChart(
        title = "Distance",
        labels = activity.weekly.map { it.label },
        points = activity.weekly.map { it.value.toFloat() },
        caption = "Weekly distance in kilometres across this block"
    )
}

@Composable
private fun EnduranceTile(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(BodyIntelPalette.CardInset)
            .border(1.dp, BodyIntelPalette.Hairline, RoundedCornerShape(10.dp))
            .padding(horizontal = 7.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = BodyIntelPalette.Muted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = if (value == "Not recorded") 9.sp else 12.5.sp,
            fontWeight = FontWeight.Black,
            color = if (value == "Not recorded") BodyIntelPalette.Faint else BodyIntelPalette.Ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = unit,
            fontSize = 7.sp,
            fontWeight = FontWeight.Medium,
            color = BodyIntelPalette.Faint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun compactNumber(value: Int): String = when {
    value >= 100_000 -> "${value / 1000}k"
    value >= 10_000 -> String.format("%.1fk", value / 1000f)
    value >= 1000 -> String.format("%,d", value).replace(",", ",")
    else -> value.toString()
}

// ------------------------------------------------------------ 06 assessment

private fun LazyListScope.assessmentSection(state: BodyIntelligenceState) {
    val assessment = state.assessment

    sectionItem("assess_score") {
        SectionCard(
            title = "Overall Fitness Score",
            subtitle = "${assessment.scoreLabel} · ${assessment.categories.size} categories",
            icon = Icons.Filled.Assessment,
            trailingLabel = "History",
            onTrailing = { state.openSheet(BodySheetTarget.Report(BodySection.ASSESSMENT)) }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RingChart(
                    percent = assessment.score,
                    centerValue = "${assessment.score}%",
                    centerLabel = "overall",
                    size = 96.dp,
                    stroke = 10.dp,
                    caption = assessment.scoreLabel
                )
                Spacer(Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    assessment.categories.take(3).forEach { category ->
                        ProgressRow(
                            label = category.label,
                            percent = category.percent,
                            valueLabel = "${category.percent}%"
                        )
                    }
                }
            }
            Spacer(Modifier.height(9.dp))
            assessment.categories.drop(3).forEach { category ->
                ProgressRow(
                    label = category.label,
                    percent = category.percent,
                    valueLabel = "${category.percent}%"
                )
                Spacer(Modifier.height(6.dp))
            }
        }
    }

    sectionItem("assess_status") {
        SectionCard(
            title = "Current Status",
            subtitle = assessment.statusTitle,
            icon = Icons.Filled.Timeline
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                assessment.statusBullets.forEach { bullet -> BulletLine(bullet.text, bullet.ok) }
            }
        }
    }

    sectionItem("assess_tests") {
        SectionCard(
            title = "Test Results",
            subtitle = "${assessment.tests.count { it.recorded }} of ${assessment.tests.size} tests recorded",
            icon = Icons.Filled.Assessment,
            trailingLabel = if (state.showAllAssessments) "Less" else "All",
            onTrailing = { state.showAllAssessments = !state.showAllAssessments }
        ) {
            val tests = if (state.showAllAssessments) assessment.tests else assessment.tests.take(6)
            tests.chunked(2).forEach { rowTests ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    rowTests.forEach { test ->
                        TestTile(
                            label = test.label,
                            value = test.valueLabel,
                            changePercent = test.changePercent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val kind = BODY_KINDS.firstOrNull { it.label == test.label }
                                    if (kind != null) {
                                        state.openSheet(BodySheetTarget.AssessmentDetail(kind))
                                    }
                                }
                        )
                    }
                    repeat(2 - rowTests.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(6.dp))
            }
            BodyIntelInfo(
                "Derived results are estimated from the current session template. Unperformed tests read " +
                    "\"Not tested\" and are never shown as zero."
            )
        }
    }

    sectionItem("assess_trend") {
        SectionCard(
            title = "Fitness Trend",
            subtitle = "Category scores across this block",
            icon = Icons.Filled.ShowChart
        ) {
            LineTrendChart(
                series = listOf(
                    TrendLine(
                        label = "Category score",
                        points = assessment.trend.map { it.value.toFloat() },
                        color = BodyIntelPalette.Emerald
                    )
                ),
                labels = assessment.trend.map { it.label },
                height = 96.dp
            )
        }
    }

    sectionItem("assess_history") {
        SectionCard(
            title = "Assessment History",
            subtitle = "Most recent first",
            icon = Icons.Filled.Timeline
        ) {
            TableRow(
                cells = listOf("Assessment", "Score", "Change"),
                weights = listOf(1.6f, 0.7f, 0.9f),
                header = true
            )
            Divider()
            assessment.history.forEachIndexed { index, row ->
                TableRow(
                    cells = listOf(
                        row.dateLabel,
                        "${row.score}%",
                        row.change?.let { if (it >= 0) "+$it%" else "$it%" } ?: "-"
                    ),
                    weights = listOf(1.6f, 0.7f, 0.9f),
                    highlight = index == 0
                )
            }
        }
    }

    sectionItem("assess_next") {
        SectionCard(
            title = "Next Assessment",
            subtitle = "${assessment.nextDateLabel} · in ${assessment.daysToNext} days",
            icon = Icons.Filled.Assessment
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Re-test to keep the score honest",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BodyIntelPalette.Ink
                    )
                    Text(
                        text = "Untested categories are the ones that can still move the overall score.",
                        fontSize = 8.5.sp,
                        lineHeight = 12.sp,
                        color = BodyIntelPalette.Muted
                    )
                }
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BodyIntelPalette.EmeraldWash)
                        .padding(horizontal = 9.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Day ${assessment.nextDateLabel.filter { it.isDigit() }}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = BodyIntelPalette.EmeraldDeep
                    )
                }
            }
        }
    }

    sectionItem("assess_actions") {
        FooterActions(listOf("Full History", "Book Assessment", "Detailed Report")) { index ->
            when (index) {
                0, 2 -> state.openSheet(BodySheetTarget.Report(BodySection.ASSESSMENT))
                else -> state.openSheet(BodySheetTarget.Plan(BodyPlanKind.ASSESSMENT))
            }
        }
    }
}

private val BODY_KINDS: List<FitnessTestKind> = FitnessTestKind.entries

// ----------------------------------------------------------- 07 consistency

private fun LazyListScope.consistencySection(state: BodyIntelligenceState) {
    val consistency = state.consistency

    if (consistency == null || !consistency.hasData) {
        sectionItem("cons_empty") {
            SectionCard(
                title = "Training Consistency",
                subtitle = "Check-ins versus completed work",
                icon = Icons.Filled.Timeline
            ) {
                BodyIntelEmpty(
                    title = "No attendance recorded",
                    body = "The calendar fills from gym check-ins. A check-in alone is not counted as a completed session."
                )
            }
        }
        return
    }

    sectionItem("cons_kpi") {
        SectionCard(
            title = "Consistency Summary",
            subtitle = consistency.monthLabel.ifBlank { "Current block" },
            icon = Icons.Filled.Timeline
        ) {
            KpiStrip(
                items = listOf(
                    Kpi("Streak", "${consistency.streakDays}d", tone = KpiTone.POSITIVE),
                    Kpi("Completed", "${consistency.completed}"),
                    Kpi("Planned", "${consistency.planned}"),
                    Kpi("Avg/week", BodyDashboardAnalytics.fmt(consistency.averagePerWeek)),
                    Kpi("Avg min", "${consistency.averageSessionMinutes}")
                )
            )
            Spacer(Modifier.height(6.dp))
            BodyIntelInfo(
                "A streak counts days with recorded training. Check-ins without a session are not counted."
            )
        }
    }

    sectionItem("cons_calendar") {
        SectionCard(
            title = "Attendance Calendar",
            subtitle = "${consistency.completionPercent}% of planned days completed",
            icon = Icons.Filled.Timeline
        ) {
            MonthCalendar(cells = consistency.calendar, startOffset = consistency.startOffset)
            Spacer(Modifier.height(7.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                LegendDot(BodyIntelPalette.EmeraldWash, "Trained")
                LegendDot(BodyIntelPalette.RoseWash, "Missed")
                LegendDot(BodyIntelPalette.CardInset, "Rest")
            }
        }
    }

    sectionItem("cons_weekly") {
        SectionCard(
            title = "Weekly Summary",
            subtitle = "Completed versus planned",
            icon = Icons.Filled.ShowChart
        ) {
            TableRow(
                cells = listOf("Week", "Done", "Planned", "Rate"),
                weights = listOf(1.1f, 0.8f, 0.9f, 0.8f),
                header = true
            )
            Divider()
            consistency.weekly.forEach { week ->
                TableRow(
                    cells = listOf(week.label, "${week.done}", "${week.planned}", "${week.percent}%"),
                    weights = listOf(1.1f, 0.8f, 0.9f, 0.8f),
                    highlight = week.percent == 100
                )
            }
        }
    }

    sectionItem("cons_month") {
        SectionCard(
            title = "Monthly Summary",
            subtitle = consistency.monthLabel,
            icon = Icons.Filled.Assessment
        ) {
            ProgressRow(
                label = "Sessions completed",
                percent = consistency.completionPercent,
                valueLabel = "${consistency.monthlyCompleted} / ${consistency.monthlyPlanned}",
                warn = consistency.completionPercent < 70
            )
            Spacer(Modifier.height(7.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ConsistencyStat("Best week", "${consistency.bestStreak}")
                ConsistencyStat("Avg/week", BodyDashboardAnalytics.fmt(consistency.averagePerWeek))
                ConsistencyStat("Avg session", "${consistency.averageSessionMinutes}m")
            }
        }
    }

    sectionItem("cons_plan") {
        SectionCard(
            title = "Plan vs Completed",
            subtitle = "Where the plan was met",
            icon = Icons.Filled.Tune
        ) {
            consistency.planRows.forEach { row ->
                ProgressRow(
                    label = row.label,
                    percent = row.percent,
                    valueLabel = "${row.done} / ${row.planned}",
                    warn = row.percent < 60
                )
                Spacer(Modifier.height(6.dp))
            }
        }
    }

    sectionItem("cons_recent") {
        SectionCard(
            title = "Recent Sessions",
            subtitle = "Most recent recorded work",
            icon = Icons.Filled.FitnessCenter
        ) {
            consistency.recentSessions.forEachIndexed { index, session ->
                if (index > 0) Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = session.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BodyIntelPalette.Ink
                        )
                        Text(
                            text = session.dateLabel,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Muted
                        )
                    }
                    Text(
                        text = "${session.minutes} min",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = BodyIntelPalette.EmeraldDeep
                    )
                }
            }
        }
    }

    sectionItem("cons_actions") {
        FooterActions(listOf("Full History", "Set Reminder", "Detailed Report")) { index ->
            when (index) {
                0, 2 -> state.openSheet(BodySheetTarget.Report(BodySection.CONSISTENCY))
                else -> state.openSheet(BodySheetTarget.Plan(BodyPlanKind.REMINDER))
            }
        }
    }
}

@Composable
private fun ConsistencyStat(label: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(9.dp))
            .background(BodyIntelPalette.CardInset)
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = BodyIntelPalette.Ink
        )
        Text(
            text = label,
            fontSize = 7.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = BodyIntelPalette.Muted
        )
    }
}

// -------------------------------------------------------------- 08 insights

private fun LazyListScope.insightsSection(state: BodyIntelligenceState) {
    val insights = state.insights

    sectionItem("ai_score") {
        SectionCard(
            title = "AI Fitness Score",
            subtitle = "${insights.scoreLabel} · derived from ${insights.overall.count { it.percent > 0 }} categories",
            icon = Icons.Filled.Insights,
            trailingLabel = "Report",
            onTrailing = { state.openSheet(BodySheetTarget.Report(BodySection.INSIGHTS)) }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RingChart(
                    percent = insights.score,
                    centerValue = "${insights.score}%",
                    centerLabel = "fitness",
                    size = 96.dp,
                    stroke = 10.dp,
                    color = BodyIntelPalette.Violet,
                    caption = insights.scoreLabel
                )
                Spacer(Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    insights.overall.take(3).forEach { category ->
                        ProgressRow(category.label, category.percent, "${category.percent}%")
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            insights.overall.drop(3).forEach { category ->
                ProgressRow(category.label, category.percent, "${category.percent}%")
                Spacer(Modifier.height(5.dp))
            }
        }
    }

    sectionItem("ai_overall") {
        SectionCard(
            title = "Overall Analysis",
            subtitle = "Computed from this member's records",
            icon = Icons.Filled.Insights
        ) {
            Text(
                text = insights.summary,
                fontSize = 9.5.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BodyIntelPalette.InkSoft
            )
            Spacer(Modifier.height(7.dp))
            insights.keyInsights.take(4).forEach { line ->
                BulletLine(line, null)
                Spacer(Modifier.height(4.dp))
            }
        }
    }

    sectionItem("ai_progress") {
        SectionCard(
            title = "Progress in This Block",
            subtitle = "Change versus the start of the block",
            icon = Icons.Filled.ShowChart
        ) {
            insights.progress.forEach { row ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = row.label,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BodyIntelPalette.InkSoft,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (row.percent >= 0) "+${row.percent}%" else "${row.percent}%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = if (row.percent >= 0) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.Rose
                    )
                }
                Spacer(Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(BodyIntelPalette.CardSoft)
                ) {
                    // Negative change renders as a short amber bar rather than a negative width.
                    val fraction = (kotlin.math.abs(row.percent) / 25f).coerceIn(0.03f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .height(5.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (row.percent >= 0) BodyIntelPalette.Emerald else BodyIntelPalette.Amber
                            )
                    )
                }
                Spacer(Modifier.height(6.dp))
            }
        }
    }

    sectionItem("ai_recommendations") {
        SectionCard(
            title = "Summary & Recommendations",
            subtitle = "Next best actions",
            icon = Icons.Filled.Assessment
        ) {
            insights.recommendations.forEachIndexed { index, line ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 1.dp)
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(BodyIntelPalette.EmeraldWash),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Black,
                            color = BodyIntelPalette.EmeraldDeep
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = line,
                        fontSize = 9.5.sp,
                        lineHeight = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = BodyIntelPalette.InkSoft
                    )
                }
                Spacer(Modifier.height(5.dp))
            }
        }
    }

    sectionItem("ai_predictions") {
        SectionCard(
            title = "Predictions",
            subtitle = "Projections, clearly labelled as estimates",
            icon = Icons.Filled.ShowChart
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                insights.predictions.forEach { prediction ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(BodyIntelPalette.CardInset)
                            .border(1.dp, BodyIntelPalette.Hairline, RoundedCornerShape(9.dp))
                            .padding(horizontal = 7.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = prediction.valueLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = BodyIntelPalette.Ink,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = prediction.caption,
                            fontSize = 7.5.sp,
                            lineHeight = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = BodyIntelPalette.Muted,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }

    sectionItem("ai_goal") {
        SectionCard(
            title = "Goal Progress",
            subtitle = insights.goalTitle,
            icon = Icons.Filled.Assessment
        ) {
            ProgressRow(
                label = insights.goalTarget,
                percent = insights.goalPercent,
                valueLabel = "${insights.goalPercent}%"
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = insights.goalRemaining,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = BodyIntelPalette.Muted
            )
        }
    }

    sectionItem("ai_trend") {
        SectionCard(
            title = "Weekly Trend",
            subtitle = "Strength share, distance and completion",
            icon = Icons.Filled.Timeline
        ) {
            val palette = listOf(
                BodyIntelPalette.Emerald,
                BodyIntelPalette.Blue,
                BodyIntelPalette.Violet
            )
            val series = insights.trend
                .filter { it.points.size >= 2 }
                .mapIndexed { index, line ->
                    TrendLine(line.label, line.points, palette[index % palette.size])
                }
            LineTrendChart(
                series = series,
                labels = (1..(series.maxOfOrNull { it.points.size } ?: 4)).map { "W$it" },
                height = 112.dp,
                sharedScale = false,
                showDots = false
            )
            Spacer(Modifier.height(6.dp))
            ChartLegend(series)
            Spacer(Modifier.height(4.dp))
            BodyIntelInfo(
                "Each line is a weekly aggregate from this block and is scaled on its own axis. " +
                    "Strength is the share of strength sessions, Distance is weekly kilometres and Score is the " +
                    "weekly completion rate."
            )
        }
    }

    sectionItem("ai_comparison") {
        SectionCard(
            title = "Comparison",
            subtitle = "Second half versus first half of this block",
            icon = Icons.Filled.ShowChart
        ) {
            insights.comparison.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = row.label,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BodyIntelPalette.InkSoft,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (row.changePercent >= 0) "+${row.changePercent}%" else "${row.changePercent}%",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        color = if (row.changePercent >= 0) BodyIntelPalette.EmeraldDeep
                        else BodyIntelPalette.Rose
                    )
                }
            }
        }
    }

    sectionItem("ai_actions_list") {
        SectionCard(
            title = "Next Actions",
            subtitle = "What the data says to do next",
            icon = Icons.Filled.Tune
        ) {
            insights.nextActions.forEachIndexed { index, action ->
                if (index > 0) Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = action.label,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BodyIntelPalette.Ink,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = action.detail,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = BodyIntelPalette.Muted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = BodyIntelPalette.Faint,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }

    sectionItem("ai_actions") {
        FooterActions(listOf("AI Fitness Plan", "Ask Coach", "Full Report")) { index ->
            when (index) {
                0 -> state.openSheet(BodySheetTarget.Plan(BodyPlanKind.AI_PLAN))
                1 -> state.openSheet(BodySheetTarget.Plan(BodyPlanKind.NEXT_ACTIVITY))
                else -> state.openSheet(BodySheetTarget.Report(BodySection.INSIGHTS))
            }
        }
    }
}

// ------------------------------------------------------------------- shared

/**
 * Trend shape used by the composition card and the metric detail sheet.
 *
 * It draws the normalised record shape the member's own values produce; it is
 * not a reconstruction of missing days.
 */
@Composable
internal fun TrendShapeChart(
    points: List<Float>,
    highlight: Color,
    height: Int = 90,
    modifier: Modifier = Modifier
) {
    if (points.size < 2) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BodyIntelPalette.CardInset),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "One record - no trend yet",
                fontSize = 8.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = BodyIntelPalette.Faint
            )
        }
        return
    }

    val min = points.min()
    val max = points.max()
    val span = (max - min).takeIf { it > 0.0001f } ?: 1f

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        val path = Path()
        val step = size.width / (points.size - 1)
        points.forEachIndexed { index, value ->
            val ratio = (value - min) / span
            val x = step * index
            val y = size.height - ratio * (size.height * 0.86f) - size.height * 0.07f
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        val fill = Path().apply {
            addPath(path)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(
            path = fill,
            brush = Brush.verticalGradient(
                listOf(highlight.copy(alpha = 0.22f), highlight.copy(alpha = 0.01f))
            )
        )
        drawPath(
            path = path,
            color = highlight,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 2.2.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
        val lastX = size.width
        val lastRatio = (points.last() - min) / span
        val lastY = size.height - lastRatio * (size.height * 0.86f) - size.height * 0.07f
        drawCircle(color = Color.White, radius = 4f.dp.toPx(), center = androidx.compose.ui.geometry.Offset(lastX, lastY))
        drawCircle(color = highlight, radius = 2.4f.dp.toPx(), center = androidx.compose.ui.geometry.Offset(lastX, lastY))
    }
}

@Composable
internal fun BodyIntelEmpty(
    title: String,
    body: String,
    action: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BodyIntelPalette.CardInset)
            .padding(horizontal = 9.dp, vertical = 9.dp)
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = BodyIntelPalette.InkSoft
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = body,
            fontSize = 8.5.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Medium,
            color = BodyIntelPalette.Muted
        )
        if (action != null && onAction != null) {
            Spacer(Modifier.height(5.dp))
            Text(
                text = action,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Black,
                color = BodyIntelPalette.EmeraldDeep,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(BodyIntelPalette.EmeraldWash)
                    .clickable { onAction() }
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
internal fun BodyIntelInfo(text: String) {
    Text(
        text = text,
        fontSize = 8.sp,
        lineHeight = 11.5.sp,
        fontWeight = FontWeight.Medium,
        color = BodyIntelPalette.Muted,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BodyIntelPalette.CardInset)
            .padding(horizontal = 7.dp, vertical = 5.dp)
    )
}

@Composable
internal fun StatusPill(text: String, color: Color, wash: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(wash)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 7.5.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            maxLines = 1
        )
    }
}

@Composable
internal fun SourceLabel(text: String) {
    Text(
        text = text,
        fontSize = 7.5.sp,
        fontWeight = FontWeight.SemiBold,
        color = BodyIntelPalette.Faint,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
internal fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(BodyIntelPalette.Hairline)
    )
}

@Composable
internal fun PrivacyBadge(category: PrivacyCategory) {
    val visible = category !in PrivacyCategory.privateDefaults
    StatusPill(
        text = if (visible) "Visible" else "Private",
        color = if (visible) BodyIntelPalette.EmeraldDeep else BodyIntelPalette.Muted,
        wash = if (visible) BodyIntelPalette.EmeraldWash else BodyIntelPalette.CardSoft
    )
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 7.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = BodyIntelPalette.Muted
        )
    }
}
