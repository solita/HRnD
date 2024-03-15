package fi.solita.hrnd.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import fi.solita.hrnd.core.data.model.PatientInfo
import fi.solita.hrnd.core.utils.PreviewUtil
import fi.solita.hrnd.designSystem.GenderAndAgeRow
import fi.solita.hrnd.designSystem.GenderAndAgeRowSize
import fi.solita.hrnd.designSystem.NavigationElement
import fi.solita.hrnd.designSystem.NoDataAvailable
import fi.solita.hrnd.domain.utils.getMostRecentDay
import fi.solita.hrnd.feature.details.composables.CurrentStatusCard
import fi.solita.hrnd.feature.details.composables.MedicalHistoryItem
import fi.solita.hrnd.feature.details.composables.MyMiniChart
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.blood_pressure
import hrnd.composeapp.generated.resources.current_status
import hrnd.composeapp.generated.resources.heart_rate
import hrnd.composeapp.generated.resources.medical_history
import hrnd.composeapp.generated.resources.nav_list
import hrnd.composeapp.generated.resources.no_medical_history
import hrnd.composeapp.generated.resources.patient_id
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

class DetailsScreen(private val patientInfo: PatientInfo?,private val patientId: String? = null) : Screen {

    @Composable
    override fun Content() {
        val screenModel: DetailsScreenModel =
            getScreenModel(parameters = { parametersOf(patientInfo) })
        val state by screenModel.container.stateFlow.collectAsState()

        LaunchedEffect(patientInfo) {
            if (!state.patientDetailsFetched) {
                screenModel.handleEvent(
                    DetailsScreenEvent.ScreenInitialized(
                        patientInfo,
                        patientId
                    )
                )
            }
        }

        BuildContent(state)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BuildContent(state: DetailsScreenState) {

    val primaryColor = MaterialTheme.colors.primary
    val secondaryColor = MaterialTheme.colors.secondary
    val navigator = LocalNavigator.current

    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        NavigationElement(
            previousScreenTitle = stringResource(Res.string.nav_list),
            modifier = Modifier.fillMaxWidth()
        ) {
            navigator?.pop()
        }

        if (state.patientInfo != null) {
            Spacer(Modifier.height(16.dp))
            Text(state.patientInfo.fullName, style = MaterialTheme.typography.h1)
            Spacer(Modifier.height(18.dp))
            GenderAndAgeRow(
                gender = state.patientInfo.gender,
                dateOfBirth = state.patientInfo.dateOfBirth,
                size = GenderAndAgeRowSize.BIG
            )
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(Res.string.patient_id, state.patientInfo.patientId),
                style = MaterialTheme.typography.caption
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.current_status),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h2
            )
            Spacer(Modifier.height(16.dp))

            CurrentStatusCard(
                modifier = Modifier,
                placement = state.patientInfo.currentRoom,
                currentMedication = state.patientDetails?.medication ?: persistentListOf(),
                currentDiagnosis = state.patientInfo.currentDiagnosis?.toImmutableList()
                    ?: persistentListOf()
            )

            Spacer(Modifier.height(16.dp))

            // Heart rate and Blood pressure section
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(Modifier.weight(0.5f)) {
                    Text(
                        stringResource(Res.string.heart_rate),
                        style = MaterialTheme.typography.h2
                    )
                    MyMiniChart(
                        data = persistentListOf(
                            state.patientDetails?.mostRecentDayHeartRate?.copy(
                                color = primaryColor
                            )
                        ).filterNotNull().toImmutableList(),
                        onclick = {
                            navigator?.push(
                                DetailedChartScreen(
                                    persistentListOf(
                                        state.patientDetails?.fullHeartRate?.copy(
                                            color = primaryColor
                                        )
                                    ).filterNotNull().toTypedArray(),
                                    mostRecentDayOfRecordedDataEpoch =
                                    state.patientDetails?.mostRecentDayHeartRate?.getMostRecentDay()
                                        ?.toEpochDays() ?: Int.MAX_VALUE
                                )
                            )
                        }
                    )
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(0.5f)) {
                    Text(
                        stringResource(Res.string.blood_pressure),
                        style = MaterialTheme.typography.h2
                    )
                    MyMiniChart(
                        data = persistentListOf(
                            state.patientDetails?.mostRecentDaySystolicPressure?.copy(
                                color = primaryColor
                            ),
                            state.patientDetails?.mostRecentDayDiastolicPressure?.copy(
                                color = secondaryColor
                            )
                        ).filterNotNull().toImmutableList(),
                        onclick = {
                            val mostRecentDayOfRecordedSystolicPressure =
                                state.patientDetails?.mostRecentDaySystolicPressure?.getMostRecentDay()
                                    ?.toEpochDays()
                            val mostRecentDayOfRecordedDiastolicPressure =
                                state.patientDetails?.mostRecentDayDiastolicPressure?.getMostRecentDay()
                                    ?.toEpochDays()
                            navigator?.push(
                                DetailedChartScreen(
                                    persistentListOf(
                                        state.patientDetails?.fullSystolicPressure?.copy(
                                            color = primaryColor
                                        ),
                                        state.patientDetails?.fullDiastolicPressure?.copy(
                                            color = secondaryColor
                                        )
                                    ).filterNotNull().toTypedArray(),
                                    mostRecentDayOfRecordedDataEpoch = maxOf(
                                        mostRecentDayOfRecordedDiastolicPressure
                                            ?: Int.MAX_VALUE,
                                        mostRecentDayOfRecordedSystolicPressure ?: Int.MAX_VALUE
                                    )
                                )
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Medical History Section
            Text(
                text = stringResource(Res.string.medical_history),
                style = MaterialTheme.typography.h2,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            Spacer(Modifier.height(16.dp))
            if (state.patientDetails?.surgery != null) {
                if (state.patientDetails.surgery.isEmpty()) {
                    Text(stringResource(Res.string.no_medical_history))
                } else {
                    state.patientDetails.surgery.forEach { surgery ->
                        MedicalHistoryItem(
                            modifier = Modifier.fillMaxWidth(),
                            surgery = surgery
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            } else {
                NoDataAvailable()
            }

        } else {
            NoDataAvailable(modifier = Modifier.fillMaxSize(), text = "Patient not found")
        }
    }
}

@Preview
@Composable
private fun BuildContentPreview() {
    BuildContent(state = DetailsScreenState(PreviewUtil.mockPatientInfo))
}