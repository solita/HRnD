package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import fi.solita.hrnd.designSystem.EmptyContent
import fi.solita.hrnd.feature.details.DetailedChartScreen
import fi.solita.hrnd.feature.details.model.ChartData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MyMiniChart(
    data: ImmutableList<ChartData>?,
    timeStamps: ImmutableList<LocalDateTime>?,
    modifier: Modifier = Modifier
) {

    val navigator = LocalNavigator.current

    Surface(
        modifier = modifier.aspectRatio(1f),
        color = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium,
        elevation = 6.dp
    ) {

        if (data == null || timeStamps == null) {
            EmptyContent()
        } else {
            Chart(
                numberOfDataSteps = 6,
                data = data,
                timeStamps = timeStamps,
                modifier = Modifier.padding(4.dp).fillMaxSize().clickable {
                    navigator?.push(
                        DetailedChartScreen(
                            data.toTypedArray(),
                            timeStamps.map {
                                it.toInstant(
                                    TimeZone.UTC
                                ).epochSeconds
                            }.toTypedArray()
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun MyMiniChartPreview() {
    MyMiniChart(
        data = persistentListOf(
            ChartData(
                data = listOf(20.0, 30.0, 20.0),
                label = "test",
                color = Color.Blue
            )
        ), timeStamps = persistentListOf()
    )
}
