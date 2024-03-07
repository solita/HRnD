package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fi.solita.hrnd.designSystem.EmptyContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MyMiniChart(
    data: ImmutableList<ChartData>?,
    timeStamps: ImmutableList<LocalDateTime>?,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.aspectRatio(1f),
        color = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium
    ) {

        if (data == null || timeStamps == null) {
            EmptyContent()
        } else {
            Chart(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.primaryVariant,
                textColor = MaterialTheme.colors.secondary,
                data = data,
                timeStamps = timeStamps,
                numberOfDataSteps = 6
            )
        }
    }
}

@Preview
@Composable
private fun MyMiniChartPreview(){
    MyMiniChart(
        data = persistentListOf(ChartData(data = listOf(20,30,20),label = "test", color = Color.Blue)), timeStamps = persistentListOf()
    )
}
