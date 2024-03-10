package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import fi.solita.hrnd.designSystem.DecorativeBall
import fi.solita.hrnd.domain.utils.binaryClosestFloatSearch
import fi.solita.hrnd.feature.details.model.ChartData
import fi.solita.hrnd.feature.details.model.ChartRead
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDateTime
import kotlin.math.round
import kotlin.math.roundToInt


@Composable
fun Chart(
    numberOfDataSteps: Int,
    data: ImmutableList<ChartData>,
    timeStamps: ImmutableList<LocalDateTime>,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp,
    horizontalLineColor: Color = MaterialTheme.colors.secondaryVariant,
    isDetailedChart: Boolean = false
) {
    data.forEach {
        if (it.data.size != timeStamps.size) {
            throw Exception("Data size does not match timeStamps size")
        }
    }
    val textMeasurer = rememberTextMeasurer()

    val textStyle = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onBackground)

    val density = LocalDensity.current

    val upperValue = remember(data) {
        (data.maxOfOrNull { chartData ->
            chartData.data.maxOfOrNull { it.toFloat() } ?: 0.0f
        } ?: 0.0f) * 1.1f
    }
    val lowerValue = remember(data) {
        (data.minOfOrNull { chartData ->
            chartData.data.minOfOrNull { it.toFloat() } ?: 0.0f
        } ?: 0.0f) * 0.9f
    }

    var legendTextHeightPx by remember {
        mutableIntStateOf(0)
    }

    var legendTextWidthPx by remember {
        mutableIntStateOf(0)
    }

    var legendWidthPx by remember {
        mutableIntStateOf(0)
    }

    var chartAreaWidth by remember {
        mutableIntStateOf(0)
    }

    var pointsXOffsets by remember {
        mutableStateOf<List<Float>>(listOf())
    }

    var touchedXChartAreaOffset by remember {
        mutableStateOf<Float?>(null)
    }

    var dataIslandWidthPx by remember {
        mutableIntStateOf(0)
    }

    var dataIslandRowWidthPx by remember {
        mutableIntStateOf(0)
    }

    Column(modifier) {

        if (isDetailedChart) {
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)
                    .onGloballyPositioned {
                        dataIslandRowWidthPx = it.size.width
                    },
                horizontalArrangement = Arrangement.Start
            ) {
                touchedXChartAreaOffset?.let { touchedX ->
                    val index = binaryClosestFloatSearch(pointsXOffsets, touchedX)
                    val chartReads = data.map {
                        ChartRead(it.color, timeStamps[index], it.data[index])
                    }.toPersistentList()
                    DataIsland(
                        modifier = Modifier.onSizeChanged {
                            dataIslandWidthPx = it.width
                        }.offset(x = with(density) {
                            val offsetX = (touchedXChartAreaOffset?.toDp()
                                ?: 0.dp) - (dataIslandWidthPx / 2).toDp() + legendWidthPx.toDp()
                            offsetX.coerceIn(
                                0.dp,
                                dataIslandRowWidthPx.toDp() - (dataIslandWidthPx.toDp())
                            )
                        }, y = 0.dp),
                        chartReads = chartReads
                    )
                }
            }
        }

        Row(Modifier.weight(0.8f).padding(horizontal = horizontalPadding)) {
            val stepValue = (upperValue - lowerValue) / (numberOfDataSteps - 1)
            //Legend section
            Canvas(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .fillMaxHeight(1.0f)
                    .width(with(density) {
                        legendTextWidthPx.toDp()
                    }).onGloballyPositioned {
                        legendWidthPx = it.size.width
                    }
            ) {
                // draw 2 less than provided, cut first and last
                for (i in 1..<numberOfDataSteps - 1) {
                    val text = round(upperValue - stepValue * (i)).roundToInt().toString()
                    val textBounds = textMeasurer.measure(text, textStyle)
                    legendTextHeightPx = textBounds.size.height
                    legendTextWidthPx = maxOf(textBounds.size.width, legendTextWidthPx)
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = text,
                            style = textStyle,
                            topLeft = Offset(
                                y = ((size.height / (numberOfDataSteps - 1)) * i) - legendTextHeightPx / 2,
                                x = (size.width - textBounds.size.width) / 2f
                            )
                        )
                    }
                }
            }

            Canvas(modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    chartAreaWidth = it.size.width
                }
                .pointerInput(Unit) {
                    this.detectDragGestures(onDrag = { i, j ->
                        touchedXChartAreaOffset = i.position.x.also {
                            Napier.v { "onDrag: $it" }
                        }
                    }, onDragEnd = {
                        Napier.i { "onDragEnd" }
                        touchedXChartAreaOffset = null
                    }, onDragStart = {
                        Napier.i { "onDragStart" }
                    })
                }
            ) {
                touchedXChartAreaOffset?.let { touchedX ->
                    drawContext.canvas.nativeCanvas.apply {
                        val x = touchedX.coerceIn(0f, chartAreaWidth.toFloat())
                        drawLine(
                            color = horizontalLineColor,
                            start = Offset(x = x, y = 0f),
                            end = Offset(x = x, y = size.height),
                            strokeWidth = 3f
                        )
                    }
                }

                // Draw horizontal lines
                // Draw 2 less than provided numberOfSteps, cut first and last
                for (i in 1..<numberOfDataSteps - 1) {
                    Napier.i { "drawHorizontalLines: textHeight = $legendTextHeightPx" }

                    drawContext.canvas.nativeCanvas.apply {
                        val yOffset = ((size.height / (numberOfDataSteps - 1)) * i)
                        drawLine(
                            color = horizontalLineColor,
                            start = Offset(x = 0f, y = yOffset),
                            end = Offset(x = size.width, y = yOffset),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f))
                        )
                    }
                }
                // Draw data
                data.forEach { chartData ->
                    pointsXOffsets = drawLine(chartData, timeStamps, lowerValue, upperValue)
                }
            }
        }
        if (isDetailedChart) {
            TimeLine(
                modifier = Modifier
                .fillMaxWidth()
                .weight(0.15f),
                chartAreaWidth = chartAreaWidth,
                textMeasurer = textMeasurer,
                textStyle = textStyle,
                horizontalPadding = horizontalPadding,
                horizontalLineColor = horizontalLineColor
            )
        }
    }
}

@Composable
private fun TimeLine(
    chartAreaWidth: Int,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    horizontalPadding: Dp,
    horizontalLineColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
    ) {
        val timeline = listOf("00", "04", "08", "12", "16", "20", "24")
        val legendRealWidth = size.width - chartAreaWidth
        timeline.forEachIndexed { i, text ->
            val textBounds = textMeasurer.measure(text, textStyle)
            val distanceBetweenPoints = chartAreaWidth / (timeline.size - 1)
            val offsetX =
                (i * distanceBetweenPoints) + legendRealWidth - textBounds.size.width / 2 - horizontalPadding.toPx()
            val offsetY = size.height - textBounds.size.height
            drawContext.canvas.nativeCanvas.apply {
                drawCircle(
                    color = horizontalLineColor,
                    center = Offset(
                        offsetX + textBounds.size.width / 2,
                        offsetY - textBounds.size.height / 2
                    ),
                    radius = 9f
                )
                drawText(
                    textMeasurer = textMeasurer,
                    text = text,
                    style = textStyle,
                    topLeft = Offset(x = offsetX, y = offsetY)
                )
                if (i < timeline.size - 1) {
                    val divider = 0.25f
                    for (j in 1..3) {
                        drawCircle(
                            color = horizontalLineColor,
                            center = Offset(
                                offsetX + textBounds.size.width / 2 + distanceBetweenPoints * divider * j,
                                offsetY - textBounds.size.height / 2
                            ),
                            radius = 3f
                        )
                    }
                }
            }
        }
    }
}

/**
 * Draws chart line.
 *
 * @return list of x offsets. Necessary to provide touch details interaction.
 */
private fun DrawScope.drawLine(
    chartData: ChartData,
    timeStamps: ImmutableList<LocalDateTime>,
    lowerValue: Float,
    upperValue: Float,
): ImmutableList<Float> {
    val height = size.height
    val width = size.width
    val yOffsetPerPoint = height / (upperValue - lowerValue)
    val xOffsetPerPoint = width / (60 * 60 * 24)
    val points: MutableList<Offset> = mutableListOf()
    for (i in chartData.data.indices) {
        val value = chartData.data.elementAt(i).toFloat()
        val time = timeStamps.elementAt(i).time
        val offsetY = (upperValue - value) * yOffsetPerPoint
        val offsetX = width - ((60 * 60 * 24 - time.toSecondOfDay()) * xOffsetPerPoint)
        points.add(Offset(offsetX, offsetY))
    }

    drawPoints(
        points = points,
        pointMode = PointMode.Polygon,
        color = chartData.color,
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )

    return points.map { it.x }.toImmutableList()
}