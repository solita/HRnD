package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.solita.hrnd.designSystem.EmptyContent
import fi.solita.hrnd.domain.ChartData
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MyMiniChart(
    data: ImmutableList<ChartData>?,
    modifier: Modifier = Modifier,
    onclick: () -> Unit
) {

    Surface(
        modifier = modifier.aspectRatio(1f),
        color = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium,
        elevation = 6.dp
    ) {

        if (data == null) {
            EmptyContent()
        } else {
            Chart(
                numberOfDataSteps = 6,
                data = data,
                modifier = Modifier.padding(4.dp).fillMaxSize().clickable {
                    onclick()
                }
            )
        }
    }
}

//@Preview
//@Composable
//private fun MyMiniChartPreview() {
//    MyMiniChart(
//        data = persistentListOf(
//            ChartData(
//                data = listOf(20.0, 30.0, 20.0),
//                label = "test",
//                color = Color.Blue
//            )
//        ), timeStamps = persistentListOf(), onclick = {}
//    )
//}
