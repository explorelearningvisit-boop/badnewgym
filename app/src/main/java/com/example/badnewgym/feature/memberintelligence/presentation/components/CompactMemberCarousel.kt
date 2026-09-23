package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.domain.model.SignalAction
import com.example.badnewgym.feature.memberintelligence.presentation.MemberCardItem

/**
 * BAD GYM Stage 2 — Compact Member Card Horizontal Carousel.
 *
 * Provides smooth snapping, side-peeking of adjacent cards, and subtle selection elevation.
 * Card width remains constant at ~220-240dp in browse mode and ~276dp in bounded detail mode,
 * without expanding to fill viewport.
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
    onMenuSelected: (MenuType) -> Unit = {},
    onCloseDetail: () -> Unit = {},
    dimensions: CompactCardDimensions = rememberCompactCardDimensions()
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Synchronize scroll when selected index changes externally
    LaunchedEffect(selectedIndex) {
        if (selectedIndex in members.indices) {
            listState.animateScrollToItem(selectedIndex)
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

    LaunchedEffect(currentCenteredIndex) {
        if (currentCenteredIndex != selectedIndex && currentCenteredIndex in members.indices) {
            onMemberSelected(currentCenteredIndex)
        }
    }

    val rowHeight by animateDpAsState(
        targetValue = if (isDetailExpanded) dimensions.detailCardHeight + 16.dp else dimensions.cardHeight + 16.dp,
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
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.0f else 0.96f,
                animationSpec = tween(durationMillis = 200),
                label = "card-scale-$index"
            )

            val effectiveTheme = if (activeTheme != null && isSelected) activeTheme else item.themeId

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                contentAlignment = Alignment.Center
            ) {
                CompactMemberCard(
                    snapshot = item.snapshot,
                    currentEvent = item.currentEvent,
                    theme = effectiveTheme,
                    primarySignal = item.primarySignal,
                    secondarySignals = item.secondarySignals,
                    cta = item.cta,
                    dimensions = dimensions,
                    isSelected = isSelected,
                    isDetail = isCardDetail,
                    menus = menus,
                    activeMenu = activeMenu,
                    onMenuSelected = onMenuSelected,
                    onCloseDetail = onCloseDetail,
                    onClick = {
                        if (!isDetailExpanded) {
                            onMemberSelected(index)
                            onMemberClick(index)
                        } else if (index != selectedIndex) {
                            onMemberSelected(index)
                        }
                    },
                    onCtaClick = {
                        item.cta?.let(onCta) ?: onMemberClick(index)
                    }
                )
            }
        }
    }
}
