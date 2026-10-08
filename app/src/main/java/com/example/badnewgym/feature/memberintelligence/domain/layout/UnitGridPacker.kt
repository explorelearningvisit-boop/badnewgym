package com.example.badnewgym.feature.memberintelligence.domain.layout

import com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetId
import com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetLayout
import com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetSize

/**
 * Unit grid packer.
 *
 * The runtime card is a fixed 4 unit wide grid (see BAD_GYM Widget Size System:
 * XS 1x1, S 2x1, M 2x2, L 3x2, XL 4x2). Widgets declare their span in units and
 * the packer decides placement.
 *
 * Rules the packer guarantees:
 *  1. A widget is never squeezed below the height it asked for.
 *  2. A widget is never placed so it overflows the row width.
 *  3. A widget that cannot fit the height budget is deferred, never clipped.
 *  4. A short final row is promoted to full width so no dead space is left.
 *
 * Pure Kotlin: no Android, no Compose, unit testable.
 */
object UnitGridPacker {

    const val COLUMNS = 4

    /** Height of one vertical unit in dp. Width unit is derived from content width. */
    const val HEIGHT_UNIT_DP = 44
    const val GAP_DP = 6

    data class Item(
        val id: VisitWidgetId,
        val widthUnits: Int,
        val heightUnits: Int,
        val priority: Int
    ) {
        val heightDp: Int get() = heightUnits * HEIGHT_UNIT_DP
    }

    data class Row(
        val items: List<Item>,
        val heightUnits: Int
    ) {
        val heightDp: Int get() = heightUnits * HEIGHT_UNIT_DP
    }

    data class Packed(
        val rows: List<Row>,
        val deferred: List<Item>,
        val usedHeightDp: Int
    )

    /**
     * @param items widgets in render priority order (already filtered for
     *        entitlement and data conditions by [com.example.badnewgym.feature.memberintelligence.domain.model.VisitWidgetConditionResolver]).
     * @param heightBudgetDp vertical space available to the grid itself.
     * @param heightOverrides admin per-widget height unit overrides.
     */
    fun pack(
        items: List<Item>,
        heightBudgetDp: Int,
        heightOverrides: Map<VisitWidgetId, Int> = emptyMap()
    ): Packed {
        val rows = mutableListOf<Row>()
        val deferred = mutableListOf<Item>()

        var current = mutableListOf<Item>()
        var currentWidth = 0
        var currentHeight = 0
        var used = 0

        items.forEach { raw ->
            val item = raw.copy(heightUnits = (heightOverrides[raw.id] ?: raw.heightUnits).coerceAtLeast(1))

            // Wrap when the widget does not fit the remaining columns.
            if (current.isNotEmpty() && currentWidth + item.widthUnits > COLUMNS) {
                rows += Row(current.toList(), currentHeight)
                used += currentHeight * HEIGHT_UNIT_DP
                current = mutableListOf()
                currentWidth = 0
                currentHeight = 0
            }

            val projected = if (current.isEmpty()) item.heightUnits else maxOf(currentHeight, item.heightUnits)

            // Height budget: defer rather than compress or clip.
            if (used + projected * HEIGHT_UNIT_DP > heightBudgetDp) {
                deferred += item
                return@forEach
            }

            current += item
            currentWidth += item.widthUnits
            currentHeight = projected
        }

        if (current.isNotEmpty()) {
            rows += Row(current.toList(), currentHeight)
            used += currentHeight * HEIGHT_UNIT_DP
        }

        // Promote any row holding a single short widget to full width, so a
        // wide widget never leaves a dead gap beside it.
        rows.indices.forEach { index ->
            val row = rows[index]
            if (row.items.size == 1 && row.items.first().widthUnits < COLUMNS) {
                val grown = row.items.first().copy(widthUnits = COLUMNS)
                rows[index] = Row(listOf(grown), row.heightUnits)
            }
        }

        return Packed(rows, deferred, used)
    }

    /** Maps a catalog size to grid units, clamped to the grid. */
    fun unitsFor(size: VisitWidgetSize): Pair<Int, Int> =
        size.widthUnits.coerceIn(1, COLUMNS) to size.heightUnits.coerceAtLeast(1)

    /** Reads admin size overrides into packer items. */
    fun itemsFrom(
        ordered: List<VisitWidgetId>,
        layout: VisitWidgetLayout,
        priorityOf: (VisitWidgetId) -> Int,
        heightOverrides: Map<VisitWidgetId, Int> = emptyMap()
    ): List<Item> = ordered.map { id ->
        val size = layout.sizeFor(id)
        val (w, h) = unitsFor(size)
        Item(id, w, (heightOverrides[id] ?: h).coerceAtLeast(1), priorityOf(id))
    }
}