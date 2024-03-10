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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import fi.solita.hrnd.core.data.model.PatientInfo
import fi.solita.hrnd.core.utils.PreviewUtil
import fi.solita.hrnd.designSystem.GenderAndAgeRow
import fi.solita.hrnd.designSystem.GenderAndAgeRowSize
import fi.solita.hrnd.designSystem.NoDataAvailable
import fi.solita.hrnd.feature.details.model.ChartData
import fi.solita.hrnd.feature.details.composables.CurrentStatusCard
import fi.solita.hrnd.feature.details.composables.MedicalHistoryItem
import fi.solita.hrnd.feature.details.composables.MyMiniChart
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.current_status
import hrnd.composeapp.generated.resources.patient_id
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

class DetailsScreen(val patientInfo: PatientInfo?) : Screen {

    @Composable
    override fun Content() {
        val screenModel: DetailsScreenModel =
            getScreenModel(parameters = { parametersOf(patientInfo) })
        val state by screenModel.container.stateFlow.collectAsState()

        var patientDetailsFetched by rememberSaveable { mutableStateOf(false) } // todo move to ScreenModel

        LaunchedEffect(patientInfo) {
            if (!patientDetailsFetched){
                screenModel.fetchPatientDetails()
                patientDetailsFetched = true
            }
        }

        BuildContent(state)
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun BuildContent(state: DetailsScreenState = DetailsScreenState(patientInfo)) {

        Column(
            Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (patientInfo != null) {
                Spacer(Modifier.height(16.dp))
                Text(patientInfo.fullName, style = MaterialTheme.typography.h1)
                Spacer(Modifier.height(18.dp))
                GenderAndAgeRow(
                    gender = patientInfo.gender,
                    dateOfBirth = patientInfo.dateOfBirth,
                    size = GenderAndAgeRowSize.BIG
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    stringResource(Res.string.patient_id, patientInfo.patientId),
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
                    placement = state.patientInfo?.currentRoom,
                    currentMedication = state.patientDetails?.medication ?: persistentListOf(),
                    currentDiagnosis = state.patientInfo?.currentDiagnosis?.toImmutableList()
                        ?: persistentListOf()
                )

                Spacer(Modifier.height(16.dp))

                // Heart rate and Blood pressure section
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Column(Modifier.weight(0.5f)) {
                        Text("Heart rate", style = MaterialTheme.typography.h2)
                        MyMiniChart(
                            data = persistentListOf(
                                ChartData(
                                    label = "Heart rate",
                                    color = MaterialTheme.colors.primary,
                                    data = state.patientDetails?.mostRecentDayHeartRate?.map { it.heartRate.toDouble() }
                                        ?: listOf()
                                )
                            ),
                            timeStamps = state.patientDetails?.mostRecentDayHeartRate?.map { it.localDateTime }
                                ?.toImmutableList(),
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(0.5f)) {
                        Text("Blood pressure", style = MaterialTheme.typography.h2)
                        MyMiniChart(
                            data = persistentListOf(
                                ChartData(
                                    label = "Systolic Pressure",
                                    color = MaterialTheme.colors.primary,
                                    data = state.patientDetails?.mostRecentDayPressure?.map { it.systolicPressure }
                                        ?: listOf()
                                ),
                                ChartData(
                                    label = "Diastolic Pressure",
                                    color = MaterialTheme.colors.secondary,
                                    data = state.patientDetails?.mostRecentDayPressure?.map { it.diastolicPressure }
                                        ?: listOf()
                                )
                            ),
                            timeStamps = state.patientDetails?.mostRecentDayPressure?.map { it.localDateTime }
                                ?.toPersistentList(),
                            modifier = Modifier
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Medical History Section
                Text(
                    text = "Medical history",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                )
                Spacer(Modifier.height(16.dp))
                if (state.patientDetails?.surgery != null) {
                    if (state.patientDetails.surgery.isEmpty()) {
                        Text("No medical history")
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
                NoDataAvailable(Modifier.fillMaxSize())
            }
        }
    }
}

@Preview
@Composable
private fun BuildContentPreview() {
    DetailsScreen(patientInfo = PreviewUtil.mockPatientInfo).BuildContent()
}