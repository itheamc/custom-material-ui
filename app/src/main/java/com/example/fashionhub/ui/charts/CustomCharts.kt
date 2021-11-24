package com.example.fashionhub.ui.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomCharts(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Y - Axis Label and Spacer Container
        Column() {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(54.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.rotate(270f),
                    text = "Y -Axis",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(54.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Canvas(modifier = modifier
                .weight(1f)
                .padding(12.dp),
                onDraw = {
                    drawChart(this)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "X - Axis",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


/**
 * ------------------------------------------------
 * Function to draw X and Y Axis
 */
fun drawXYAxis(
    drawScope: DrawScope
) {
    val yOffset = Offset(
        x = 0f,
        y = 0f
    )

    val crossOffset = Offset(
        x = 0f,
        y = drawScope.size.height
    )

    val xOffset = Offset(
        x = drawScope.size.width,
        y = drawScope.size.height
    )
    drawScope.drawPoints(
        points = listOf(
            yOffset,
            crossOffset,
            xOffset
        ),
        pointMode = PointMode.Polygon,
        brush = SolidColor(Color.Blue),
        strokeWidth = 10f
    )
}


/**
 * ------------------------------------------------
 * Function to draw data on the line chart
 */
fun drawChart(
    drawScope: DrawScope
) {

    val canvasHeight = drawScope.size.height
    val canvasWidth = drawScope.size.width

    val maxValue = data.maxOf { it }
    val minValue = data.minOf { it }
    val upperYRange = maxValue * 1.10f
    val upperXRange = canvasWidth * 0.90f

    val xAxisGap = upperXRange / data.size

    val points = mutableListOf<Offset>()
    val pointsForXAxis = mutableListOf<Offset>()
    val pointsForYAxis = mutableListOf<Offset>()

    data.forEachIndexed { i, n ->
        val offset = Offset(
            x = (i + 1) * xAxisGap,
            y = (canvasHeight - ((n / upperYRange) * canvasHeight))
        )

        points.add(i, offset)
        pointsForXAxis.add(
            Offset(
                x = (i + 1) * xAxisGap,
                y = canvasHeight - 10f
            )
        )

        pointsForXAxis.add(
            Offset(
                x = (i + 1) * xAxisGap,
                y = canvasHeight + 10f
            )
        )
    }

    (0 until (maxValue / minValue)).forEach {
        pointsForYAxis.add(
            it,
            Offset(
                x = -10f,
                y = canvasHeight - (((minValue * (it + 1)) / upperYRange) * canvasHeight)
            )
        )

        pointsForYAxis.add(
            it,
            Offset(
                x = 10f,
                y = canvasHeight - (((minValue * (it + 1)) / upperYRange) * canvasHeight)
            )
        )
    }

    drawScope.drawPoints(
        points = pointsForXAxis,
        pointMode = PointMode.Lines,
        brush = Brush.linearGradient(
            listOf(
                Color.Red,
                Color.Blue,
                Color.Magenta,
                Color.Blue
            )
        ),
        strokeWidth = 8f,
        cap = StrokeCap.Square
    )

    drawScope.drawPoints(
        points = pointsForYAxis,
        pointMode = PointMode.Points,
        brush = Brush.linearGradient(
            listOf(
                Color.Red,
                Color.Blue,
                Color.Magenta,
                Color.Blue
            )
        ),
        strokeWidth = 8f,
        cap = StrokeCap.Square
    )

    drawXYAxis(drawScope)

    drawScope.drawPoints(
        points = points,
        pointMode = PointMode.Polygon,
        brush = Brush.linearGradient(
            listOf(
                Color.Blue,
                Color.Yellow,
                Color.Magenta,
                Color.Red,
                Color.Cyan,
                Color.Blue
            )
        ),
        strokeWidth = 8f
    )

    drawScope.drawPoints(
        points = points,
        pointMode = PointMode.Points,
        brush = Brush.linearGradient(
            listOf(
                Color.Red,
                Color.Blue,
                Color.Magenta,
                Color.Blue
            )
        ),
        strokeWidth = 15f,
        cap = StrokeCap.Round
    )
}


//private val data = arrayOf(100, 50, 160, 130, 60, 90, 140, 150, 75, 110)
private val data = arrayOf(10, 30, 25, 35, 50, 45, 15, 55, 75, 40)
