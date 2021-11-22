package com.example.fashionhub.ui.scatteredgrid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    gap: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(if (gap.value.toInt() in 0..12) 0.dp else (gap.value.toInt() - 12).dp)
            .verticalScroll(scrollState)
    ) {
        StaggeredGridLayout(
            modifier = Modifier.fillMaxWidth(),
            columns = columns,
            gap = gap,
            content = content
        )
    }
}


@Composable
private fun StaggeredGridLayout(
    modifier: Modifier = Modifier,
    columns: Int,
    gap: Dp,
    content: @Composable () -> Unit
) {
    val measurePolicy = rememberStaggeredGridMeasurePolicy(cols = columns, gap = gap)
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = measurePolicy
    )
}


@Composable
private fun rememberStaggeredGridMeasurePolicy(cols: Int, gap: Dp) = remember(cols, gap) {
    staggeredGridMeasurePolicy(cols = cols, gap = gap)
}

internal fun staggeredGridMeasurePolicy(cols: Int, gap: Dp) =
    MeasurePolicy { measurables, constraints ->
        if (measurables.isEmpty()) {
            return@MeasurePolicy layout(
                constraints.minWidth,
                constraints.minHeight
            ) {}
        }

        // Maximum width of each column i.e. item
        val pixelGap = gap.toPx().toInt()
        val itemWidth = (constraints.maxWidth - ((cols + 1) * pixelGap)) / cols
        // Make changes from (cols -1)^

        val looseConstraints =
            constraints.copy(minWidth = 0, minHeight = 0, maxWidth = itemWidth)


        // Arrays to store the height of the each column
        val itemsHeights = IntArray(measurables.size) { 0 }


        // List of placeable by measuring each item
        val placeables = measurables.mapIndexed { index, measurable ->
            // Measure each child
            val placeable = measurable.measure(looseConstraints)

            // Tracking the height of each item
            itemsHeights[index] = placeable.height

            placeable
        }

        // Calculating the XY coordinate for each item
        // and adding to the list coordinates
        val coordinates = mutableListOf<Place>()

        itemsHeights.forEachIndexed { index, _ ->
            // Coordinate X
            val x = if (index % cols == 0) {
                pixelGap
            } else {
                pixelGap + ((itemWidth + pixelGap) * (index % cols))
            }

            // Coordinate Y
            val y = if (index == 0) {
                pixelGap
            } else {
                pixelGap + (itemsHeights.filterIndexed { ind, _ ->
                    ind < index && (ind % cols == index % cols)
                }.sumOf { it + pixelGap })
            }

            coordinates.add(index, Place(x = x, y = y))

        }

        // Calculating the max width and height for the layout
        val width = constraints.maxWidth
        val height = (0 until cols).map { n ->
            itemsHeights.filterIndexed { ind, _ -> ind % cols == n }
                .sumOf { it + pixelGap } + pixelGap
        }.maxOf { it }

        // Set the size of the parent layout
        layout(width = width, height = height) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = coordinates[index].x,
                    y = coordinates[index].y
                )
            }
        }

    }


private data class Place(val x: Int = 0, val y: Int = 0)