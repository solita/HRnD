package fi.solita.hrnd.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import fi.solita.hrnd.core.utils.isPortrait
import fi.solita.hrnd.designSystem.DecorativeBall
import fi.solita.hrnd.feature.details.composables.Chart
import fi.solita.hrnd.feature.details.model.ChartData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DetailedChartScreen(
    private val data: Array<ChartData>,
    private val epochSeconds: Array<Long>
) : Screen {

    @Composable
    override fun Content() {
        BuildContent(data.toList().toImmutableList(), epochSeconds.toList().toImmutableList())
    }
}

@Composable
fun BuildContent(data: ImmutableList<ChartData>, epochSeconds: ImmutableList<Long>) {

    var isPortrait by remember {
        mutableStateOf(isPortrait())
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp).onSizeChanged {
                isPortrait = isPortrait()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if(isPortrait){
            Spacer(Modifier.fillMaxSize(0.2f))
        }else {
            Spacer(Modifier.height(16.dp))
        }
        //LEGEND
        data.forEach {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DecorativeBall(modifier = Modifier.size(16.dp), color = it.color)
                Text(text = it.label, style = MaterialTheme.typography.h3)
            }
        }

        Chart(
            numberOfDataSteps = 8,
            data = data,
            timeStamps = epochSeconds.map {
                Instant.fromEpochSeconds(it).toLocalDateTime(TimeZone.UTC)
            }.toImmutableList(),
            modifier = Modifier.applySize(isPortrait),
            isDetailedChart = true,
            horizontalPadding = 16.dp
        )
    }
}



fun Modifier.applySize(condition: Boolean): Modifier {
    return if (condition) {
        this.fillMaxWidth().aspectRatio(16 / 9f)
    } else {
        this.fillMaxHeight(0.85f)
    }
}