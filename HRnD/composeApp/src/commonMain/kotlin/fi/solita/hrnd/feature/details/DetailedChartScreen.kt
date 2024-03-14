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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import fi.solita.hrnd.core.utils.isPortrait
import fi.solita.hrnd.designSystem.DecorativeBall
import fi.solita.hrnd.designSystem.NavigationElement
import fi.solita.hrnd.domain.ChartData
import fi.solita.hrnd.domain.ChartDataType
import fi.solita.hrnd.feature.details.composables.Chart
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.diastolic_pressure
import hrnd.composeapp.generated.resources.heart_rate
import hrnd.composeapp.generated.resources.nav_patient_details
import hrnd.composeapp.generated.resources.next_day
import hrnd.composeapp.generated.resources.previous_day
import hrnd.composeapp.generated.resources.systolic_pressure
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

class DetailedChartScreen(
    private val chartData: Array<ChartData>,
    private val mostRecentDayOfRecordedDataEpoch: Int
) : Screen {

    @Composable
    override fun Content() {
        BuildContent(chartData.toList().toImmutableList(), mostRecentDayOfRecordedDataEpoch)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BuildContent(data: ImmutableList<ChartData>, mostRecentDayOfRecordedDataEpoch: Int) {

    val navigator = LocalNavigator.currentOrThrow

    var date by remember(mostRecentDayOfRecordedDataEpoch) {
        mutableStateOf(LocalDate.fromEpochDays(mostRecentDayOfRecordedDataEpoch))
    }

    val dateIsToday by remember(date) {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.UTC).date == date
        )
    }

    val dataFilteredByDate by remember(date) {
        mutableStateOf(
            data.map { chartData ->
                chartData.copy(
                    entries = chartData.entries?.filter {
                        Instant.fromEpochSeconds(it.timeStampEpochSeconds)
                            .toLocalDateTime(TimeZone.UTC).date == date
                    }
                )
            }.toImmutableList()
        )
    }

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
        NavigationElement(
            previousScreenTitle = stringResource(Res.string.nav_patient_details),
            modifier = Modifier.fillMaxWidth()
        ) {
            navigator.pop()
        }

        if (isPortrait) {
            Spacer(Modifier.fillMaxSize(0.2f))
        } else {
            Spacer(Modifier.height(16.dp))
        }
        //LEGEND
        data.forEach {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DecorativeBall(
                    modifier = Modifier.size(16.dp),
                    color = it.color ?: MaterialTheme.colors.primary
                )
                Text(
                    text =
                    when (it.type) {
                        ChartDataType.HEART_RATE -> stringResource(Res.string.heart_rate)
                        ChartDataType.DIASTOLIC_PRESSURE -> stringResource(Res.string.diastolic_pressure)
                        ChartDataType.SYSTOLIC_PRESSURE -> stringResource(Res.string.systolic_pressure)
                    }, style = MaterialTheme.typography.h3
                )
            }
        }

        Chart(
            numberOfDataSteps = 8,
            data = dataFilteredByDate,
            modifier = Modifier.applySize(isPortrait),
            isDetailedChart = true,
            horizontalPadding = 16.dp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                Napier.i { "Navigate day before" }
                date = date.minus(1, DateTimeUnit.DAY)
            }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(Res.string.previous_day)
                )
            }

            Text(
                modifier = Modifier,
                text = if (dateIsToday) "Today" else date.toString(),
                style = MaterialTheme.typography.body2
            )

            IconButton(enabled = !dateIsToday, onClick = {
                Napier.i { "Navigate day after" }
                date = date.plus(1, DateTimeUnit.DAY)
            }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = stringResource(Res.string.next_day)
                )
            }
        }
    }
}


fun Modifier.applySize(condition: Boolean): Modifier {
    return if (condition) {
        this.fillMaxWidth().aspectRatio(16 / 9f)
    } else {
        this.fillMaxHeight(0.85f)
    }
}