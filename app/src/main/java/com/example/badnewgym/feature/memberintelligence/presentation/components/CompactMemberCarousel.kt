package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.dimensions.CompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.design.dimensions.rememberCompactCardDimensions
import com.example.badnewgym.feature.memberintelligence.design.motion.DepthTokens
import com.example.badnewgym.feature.memberintelligence.design.motion.rememberIsReducedMotion
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.presentation.MemberCardItem

/**
 * BAD GYM Stage 7 — Compact Member Card Horizontal Carousel with 2.5D Depth.
 *
 * Applies production-grade carousel depth:
 * - Focused card: scale 1.02, alpha 1.0, no rotation.
 * - Adjacent cards: scale 0.96, alpha 0.88, subtle rotationY parallax (±4°).
 * - Depth driven by continuous scroll position (not a discrete step).
 * - Reduced-motion: all graphicsLayer transforms suppressed.
 * - Gesture interruption is safe (LazyRow fling + snap).
 */
@Composable
fun CompactMemberCarousel(
    members: List<MemberCardItem>,
    selectedIndex: Int,
    onMemberSelected: (Int) -> Unit,
    onMemberClick: (Int) -> Unit,
    onCta: (SignalAction) -> Unit,
    modifier: Modifier = Modifier,
    activeTheme: ThemeId? = null,
    isDetailExpanded: Boolean = false,
    activeMenu: MenuType = MenuType.HOME,
    menus: List<MemberMenu> = emptyList(),
    temporalRange: com.example.badnewgym.feature.memberintelligence.domain.model.TemporalRange = com.example.badnewgym.feature.memberintelligence.domain.model.TemporalRange.forCurrentMonth(),
    onRangeChange: (com.example.badnewgym.feature.memberintelligence.domain.model.TemporalRange) -> Unit = {},
    onEventClick: (com.example.badnewgym.feature.memberintelligence.domain.model.TemporalEventRecord) -> Unit = {},
    onMenuSelected: (MenuType) -> Unit = {},
    onCloseDetail: () -> Unit = {},
    dimensions: CompactCardDimensions = rememberCompactCardDimensions()
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val isReducedMotion = rememberIsReducedMotion()

    // Synchronize scroll when selected index changes externally
    val currentSelectedMemberId = members.getOrNull(selectedIndex)?.snapshot?.id
    LaunchedEffect(selectedIndex, currentSelectedMemberId) {
        if (selectedIndex in members.indices) {
            listState.scrollToItem(selectedIndex)
        }
    }

    // Detect center item as user scrolls/settles
    val currentCenteredIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) return@derivedStateOf selectedIndex
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            layoutInfo.visibleItemsInfo.minByOrNull { item ->
                val itemCenter = item.offset + (item.size / 2)
                kotlin.math.abs(itemCenter - viewportCenter)
            }?.index ?: selectedIndex
        }
    }

    LaunchedEffect(currentCenteredIndex, listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && currentCenteredIndex != selectedIndex && currentCenteredIndex in members.indices) {
            onMemberSelected(currentCenteredIndex)
        }
    }

    val rowHeight by animateDpAsState(
        targetValue = dimensions.detailCardHeight + 16.dp,
        animationSpec = tween(durationMillis = 220),
        label = "carousel-height"
    )

    LazyRow(
        state = listState,
        flingBehavior = flingBehavior,
        contentPadding = PaddingValues(horizontal = dimensions.outerHorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(dimensions.carouselGap),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(rowHeight)
    ) {
        itemsIndexed(
            items = members,
            key = { _, item -> item.snapshot.id }
        ) { index, item ->
            val isSelected = index == selectedIndex
            val isCardDetail = isSelected && isDetailExpanded

            // Compute focus offset from scroll state for continuous parallax depth
            // focusOffset = 0 when centered, ±1 when one card width away
            val focusOffset by remember {
                derivedStateOf {
                    val layoutInfo = listState.layoutInfo
                    val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                    val item = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
                        ?: return@derivedStateOf if (index < currentCenteredIndex) -1f else 1f
                    val itemCenter = item.offset + (item.size / 2f)
                    val cardWidth = item.size.toFloat().coerceAtLeast(1f)
                    ((itemCenter - viewportCenter) / cardWidth).coerceIn(-1.5f, 1.5f)
                }
            }

            val effectiveTheme = if (activeTheme != null && isSelected) activeTheme else item.themeId

            Box(
                modifier = Modifier.graphicsLayer {
                    if (!isReducedMotion) {
                        val absOffset = kotlin.math.abs(focusOffset).coerceIn(0f, 1f)

                        // Scale: 1.02 focused → 0.96 adjacent
                        val targetScale = lerp(DepthTokens.FOCUS_SCALE, DepthTokens.ADJACENT_SCALE, absOffset)
                        scaleX = targetScale
                        scaleY = targetScale

                        // Alpha: 1.0 focused → 0.88 adjacent
                        alpha = lerp(DepthTokens.FOCUS_ALPHA, DepthTokens.ADJACENT_ALPHA, absOffset)

                        // Subtle rotationY parallax: max ±4 degrees
                        rotationY = -focusOffset.coerceIn(-1f, 1f) * DepthTokens.PARALLAX_MAX_ROTATION_Y

                        // Safe camera distance
                        cameraDistance = 8f * density
                    }
                },
                contentAlignment = Alignment.Center
            ) {
                // Canonical Member Intelligence card with integrated vertical navigation rail on the selected card.
                CompactMemberCard(
                    snapshot = item.snapshot,
                    currentEvent = item.currentEvent,
                    theme = effectiveTheme,
                    primarySignal = item.primarySignal,
                    secondarySignals = item.secondarySignals,
                    cta = item.cta,
                    dimensions = dimensions,
                    isSelected = isSelected,
                    isDetail = isSelected,
                    menus = menus,
                    activeMenu = activeMenu,
                    temporalRange = temporalRange,
                    onRangeChange = onRangeChange,
                    onEventClick = onEventClick,
                    onMenuSelected = onMenuSelected,
                    onCloseDetail = onCloseDetail,
                    isReducedMotion = isReducedMotion,
                    onClick = {
                        onMemberSelected(index)
                        onMemberClick(index)
                    },
                    onCtaClick = {
                        item.cta?.let(onCta) ?: onMemberClick(index)
                    }
                )
            }
        }
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float =
    start + fraction * (stop - start)




