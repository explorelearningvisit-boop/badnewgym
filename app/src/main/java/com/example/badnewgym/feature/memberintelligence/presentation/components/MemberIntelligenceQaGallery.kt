package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.dimensions.rememberCompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.domain.fixture.FixtureCardItem
import com.example.badnewgym.feature.memberintelligence.domain.fixture.MemberIntelligenceFixtureUniverse
import com.example.badnewgym.feature.memberintelligence.domain.fixture.TemporalBucket
import com.example.badnewgym.feature.memberintelligence.domain.model.EventCardKind
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction

/**
 * Developer / QA-only surface for inspecting the complete Member Intelligence
 * fixture universe (all 68 EventTypes + UNKNOWN + edge cases).
 */
@Composable
fun MemberIntelligenceQaGallery(
    onSelectFixture: (FixtureCardItem) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    activeFixtureId: String? = null
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Taxonomy, 1: Edge Cases
    var selectedKind by remember { mutableStateOf<EventCardKind?>(null) }
    var selectedTemporal by remember { mutableStateOf<TemporalBucket?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var expandedFixtureId by remember { mutableStateOf<String?>(null) }

    val rawItems = if (selectedTab == 0) {
        MemberIntelligenceFixtureUniverse.allEvents
    } else {
        MemberIntelligenceFixtureUniverse.edgeCases
    }

    val filteredItems = remember(rawItems, selectedKind, selectedTemporal, searchQuery) {
        rawItems.filter { item ->
            val matchesKind = selectedKind == null || item.cardKind == selectedKind
            val matchesTemporal = selectedTemporal == null || item.temporalBucket == selectedTemporal
            val matchesSearch = searchQuery.isBlank() ||
                item.label.contains(searchQuery, ignoreCase = true) ||
                item.eventType.name.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true) ||
                item.snapshot.identity.name.contains(searchQuery, ignoreCase = true)
            matchesKind && matchesTemporal && matchesSearch
        }
    }

    val colors = BADGymTheme.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // 1. Header Bar
        QaGalleryHeader(
            totalCount = rawItems.size,
            filteredCount = filteredItems.size,
            onClose = onClose
        )

        // 2. Demo / Synthetic Notice Banner
        SyntheticNoticeBanner()

        // 3. Tab Switcher: All Taxonomy vs Edge Cases
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = colors.surface,
            contentColor = colors.accent,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0; selectedKind = null },
                text = {
                    Text(
                        "TAXONOMY (${MemberIntelligenceFixtureUniverse.allEvents.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Black else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1; selectedKind = null },
                text = {
                    Text(
                        "EDGE CASES (${MemberIntelligenceFixtureUniverse.edgeCases.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Black else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            )
        }

        // 4. Temporal Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("TEMPORAL:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colors.textMuted)
            FilterChip(
                selected = selectedTemporal == null,
                onClick = { selectedTemporal = null },
                label = { Text("ALL", fontSize = 10.sp) }
            )
            TemporalBucket.values().forEach { bucket ->
                FilterChip(
                    selected = selectedTemporal == bucket,
                    onClick = { selectedTemporal = if (selectedTemporal == bucket) null else bucket },
                    label = { Text(bucket.name, fontSize = 10.sp) }
                )
            }
        }

        // 5. Card Kind Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("ARCHETYPE:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colors.textMuted)
            FilterChip(
                selected = selectedKind == null,
                onClick = { selectedKind = null },
                label = { Text("ALL (${rawItems.size})", fontSize = 10.sp) }
            )
            EventCardKind.values().forEach { kind ->
                val count = rawItems.count { it.cardKind == kind }
                if (count > 0) {
                    FilterChip(
                        selected = selectedKind == kind,
                        onClick = { selectedKind = if (selectedKind == kind) null else kind },
                        label = { Text("${kind.name} ($count)", fontSize = 10.sp) }
                    )
                }
            }
        }

        // 6. Search input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Filter by event, member, keyword...", fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Rounded.Search, null, Modifier.size(16.dp)) },
            trailingIcon = if (searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = { searchQuery = "" }, Modifier.size(24.dp)) {
                        Icon(Icons.Rounded.Close, null, Modifier.size(14.dp))
                    }
                }
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp),
            singleLine = true
        )

        // 7. Fixtures List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(filteredItems, key = { it.id }) { item ->
                FixtureGalleryCardItem(
                    item = item,
                    isExpanded = expandedFixtureId == item.id,
                    isActive = activeFixtureId == item.id,
                    onToggleExpand = {
                        expandedFixtureId = if (expandedFixtureId == item.id) null else item.id
                    },
                    onLoadWorkspace = { onSelectFixture(item) }
                )
            }
        }
    }
}

@Composable
private fun QaGalleryHeader(
    totalCount: Int,
    filteredCount: Int,
    onClose: () -> Unit
) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .border(0.8.dp, colors.border.copy(alpha = 0.5f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClose, modifier = Modifier.size(32.dp)) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Close QA Lab", tint = colors.textPrimary)
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("QA FIXTURE LAB", color = colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Black)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colors.accent.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("DEMO", color = colors.accent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(
                "Deterministic Event Taxonomy Universe ($filteredCount / $totalCount)",
                color = colors.textSecondary,
                fontSize = 11.sp
            )
        }
        Button(
            onClick = onClose,
            colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceMuted, contentColor = colors.textPrimary),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("DONE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SyntheticNoticeBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFEF3C7))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("⚠️", fontSize = 12.sp, modifier = Modifier.padding(end = 6.dp))
        Text(
            "SYNTHETIC QA DATA ONLY — Never commingled with real production member records.",
            color = Color(0xFF92400E),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun FixtureGalleryCardItem(
    item: FixtureCardItem,
    isExpanded: Boolean,
    isActive: Boolean,
    onToggleExpand: () -> Unit,
    onLoadWorkspace: () -> Unit
) {
    val colors = BADGymTheme.colors
    val dimensions = rememberCompactCardDimensions()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = androidx.compose.foundation.BorderStroke(
            if (isActive) 2.dp else 1.dp,
            if (isActive) colors.accent else colors.border.copy(alpha = 0.7f)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Info Row
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.label, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(item.description, color = colors.textSecondary, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.width(8.dp))
                // Temporal Bucket Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when (item.temporalBucket) {
                                TemporalBucket.NOW -> colors.success.copy(alpha = 0.15f)
                                TemporalBucket.PAST -> colors.info.copy(alpha = 0.15f)
                                TemporalBucket.FUTURE -> colors.warning.copy(alpha = 0.15f)
                            }
                        )
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        item.temporalBucket.name,
                        color = when (item.temporalBucket) {
                            TemporalBucket.NOW -> colors.success
                            TemporalBucket.PAST -> colors.info
                            TemporalBucket.FUTURE -> colors.warning
                        },
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Tags row: EventType, CardKind, DefaultMenu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TagPill(item.eventType.name, colors.accent)
                TagPill(item.cardKind.name, colors.textMuted)
                TagPill("MENU: ${item.spec.defaultMenu.name}", colors.textSecondary)
                item.storyLink?.let { TagPill(it, colors.warning) }
                if (item.isEdgeCase && item.edgeCaseNote != null) {
                    TagPill("EDGE: ${item.edgeCaseNote}", Color(0xFFD97706))
                }
            }

            // Centered Compact Card Render
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                AdvancedEventMemberCard(
                    snapshot = item.snapshot,
                    currentEvent = item.event,
                    theme = ThemeId.NATURAL_FRESH,
                    cta = SignalAction(item.spec.actionLabel ?: "Select Action", "NAVIGATION", item.spec.defaultMenu.name),
                    dimensions = dimensions,
                    isSelected = isActive,
                    onClick = onToggleExpand,
                    onCtaClick = onLoadWorkspace
                )
            }

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onToggleExpand) {
                    Text(if (isExpanded) "Hide Details ▲" else "Inspect Specs ▼", fontSize = 11.sp, color = colors.accent)
                }
                Button(
                    onClick = onLoadWorkspace,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isActive) colors.success else colors.accent
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        if (isActive) Icons.Rounded.Check else Icons.Rounded.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(if (isActive) "ACTIVE IN WORKSPACE" else "LOAD IN WORKSPACE", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Expanded Spec & Metadata Inspector
            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.surfaceMuted.copy(alpha = 0.5f))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("FIXTURE SPECIFICATION:", fontSize = 10.sp, fontWeight = FontWeight.Black, color = colors.textPrimary)
                    InspectorLine("Event ID", item.event.id)
                    InspectorLine("Member ID", item.snapshot.id)
                    InspectorLine("Member Name", item.snapshot.identity.name)
                    InspectorLine("Tier", item.snapshot.identity.tier.name)
                    InspectorLine("Primary Decision", item.spec.primaryDecision.name)
                    InspectorLine("Story Label", item.spec.storyLabel)
                    InspectorLine("Timeline Mode", if (item.spec.showTimeline) "Timeline Enabled" else "Hidden")
                    InspectorLine("Evidence Keys", item.spec.evidenceKeys.joinToString(", ").ifBlank { "None" })
                    InspectorLine("Metadata", item.event.metadata.entries.joinToString { "${it.key}=${it.value}" }.ifBlank { "Empty" })
                }
            }
        }
    }
}

@Composable
private fun TagPill(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 8.5.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    )
}

@Composable
private fun InspectorLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 9.sp, color = BADGymTheme.colors.textMuted, fontWeight = FontWeight.SemiBold)
        Text(value, fontSize = 9.sp, color = BADGymTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
    }
}
