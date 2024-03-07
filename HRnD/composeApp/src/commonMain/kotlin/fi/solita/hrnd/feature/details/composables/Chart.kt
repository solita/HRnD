package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDateTime
import kotlin.math.round
import kotlin.math.roundToInt


data class ChartData(
    val data: List<Number>,
    val label: String?,
    val color: Color
)

@Composable
fun Chart(
    color: Color,
    secondaryColor : Color = MaterialTheme.colors.secondaryVariant,
    textColor: Color,
    numberOfDataSteps: Int,
    data: ImmutableList<ChartData>,
    timeStamps: ImmutableList<LocalDateTime>,
    modifier: Modifier = Modifier,
    showTimeLine: Boolean = false
) {
    data.forEach {
        Napier.i { it.data.toString() + it.label }
    }
    val textMeasurer = rememberTextMeasurer()

    val textStyle = MaterialTheme.typography.caption

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

    var textHeight by remember {
        mutableIntStateOf(0)
    }

    Napier.i { "upperValue = $upperValue, lowerValue = $lowerValue" }

    val textPaint = remember(density) {
        Paint().apply {

        }
    }

    Row(modifier) {
        val stepValue = (upperValue - lowerValue) / (numberOfDataSteps - 1)

        //Scale section
        Canvas(modifier = Modifier.fillMaxHeight(1.0f).fillMaxWidth(0.15f)) {
            // draw 2 less than provided, cut first and last
            for (i in 1..<numberOfDataSteps - 1) {
                val text = round(upperValue - stepValue * (i)).roundToInt().toString()
                val textBounds = textMeasurer.measure(text, textStyle)
                textHeight = textBounds.size.height
                Napier.i { "newTextHeight = $textHeight" }
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        textMeasurer = textMeasurer,
                        text = text,
                        style = textStyle,
                        topLeft = Offset(
                            y = ((size.height / (numberOfDataSteps - 1)) * i) - textHeight / 2,
                            x = (size.width - textBounds.size.width) / 2f
                        )
                    )
                }
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw timeline todo
            if (showTimeLine) {
                (0 until data.size - 1 step 2).forEach { i ->
                    val dateTime = timeStamps.elementAt(i)
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = dateTime.time.toString(),
                            style = textStyle,
                            topLeft = Offset(20f, 20f)
                        )
                    }
                }
            }

            // Draw horizontal lines
            // Draw 2 less than provided numberOfSteps, cut first and last
            for (i in 1..<numberOfDataSteps - 1) {
                Napier.i { "drawHorizontalLines: textHeight = $textHeight" }

                drawContext.canvas.nativeCanvas.apply {
                    val yOffset = ((size.height / (numberOfDataSteps - 1)) * i)
                    drawLine(
                        color = secondaryColor,
                        start = Offset(x = 0f,y = yOffset),
                        end = Offset(x = size.width,y = yOffset),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f,30f))
                    )
                }
            }

            // Draw data
            data.forEach { chartData ->
                drawLine(chartData, timeStamps, lowerValue, upperValue)
            }
        }
    }


}


private fun DrawScope.drawLine(
    chartData: ChartData,
    timeStamps: ImmutableList<LocalDateTime>,
    lowerValue: Float,
    upperValue: Float,
) {
    val strokePath = Path().apply {
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
            Napier.i { "Time for $value, $time, offset: $offsetX, width = $width, offsetXPerPoint = $xOffsetPerPoint" }
            points.add(Offset(offsetX, offsetY))
        }


        drawPoints(
            points = points,
            pointMode = PointMode.Polygon,
            color = chartData.color,
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
    }
//    drawPath(
//        path = fillPath,
//        brush = Brush.verticalGradient(
//            colors = listOf(
//                Color.Magenta, // todo change?
//                Color.Transparent
//            ),
//            endY = size.height - spacing
//        )
//    )
    drawPath(
        path = strokePath,
        color = chartData.color,
        style = Stroke(
            width = 1.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
}