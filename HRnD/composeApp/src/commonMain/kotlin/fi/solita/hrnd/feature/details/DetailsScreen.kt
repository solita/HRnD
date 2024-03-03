package fi.solita.hrnd.feature.details

import androidx.compose.foundation.layout.*
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
import cafe.adriel.voyager.navigator.currentOrThrow
import fi.solita.hrnd.core.data.model.PatientInfo
import fi.solita.hrnd.core.utils.PreviewUtil
import fi.solita.hrnd.designSystem.EmptyContent
import fi.solita.hrnd.designSystem.GenderAndAgeRow
import fi.solita.hrnd.designSystem.GenderAndAgeRowSize
import fi.solita.hrnd.feature.details.composables.CurrentStatusCard
import fi.solita.hrnd.feature.details.composables.MedicalHistoryItem
import fi.solita.hrnd.feature.details.composables.MiniChart
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.current_status
import hrnd.composeapp.generated.resources.patient_id
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

class DetailsScreen(val patientInfo: PatientInfo?) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: DetailsScreenModel = getScreenModel(parameters = { parametersOf(patientInfo) })
        val state by screenModel.container.stateFlow.collectAsState()

        LaunchedEffect(patientInfo){
            screenModel.fetchPatientDetails()
        }

        BuildContent(state)
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun BuildContent(state: DetailsScreenState = DetailsScreenState(patientInfo)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

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
                    currentMedication = state.patientDetails?.medication?.toImmutableList() ?: persistentListOf(),
                    currentDiagnosis = state.patientInfo?.currentDiagnosis?.toImmutableList() ?: persistentListOf()
                )

                Spacer(Modifier.height(16.dp))

                // Heart rate and Blood pressure section
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Column(Modifier.weight(0.5f)) {
                        Text("Heart rate", style = MaterialTheme.typography.h2)
                        MiniChart(Modifier)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(0.5f)) {
                        Text("Blood pressure", style = MaterialTheme.typography.h2)
                        MiniChart(Modifier)
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
                            MedicalHistoryItem(modifier = Modifier.fillMaxWidth(), surgery = surgery)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                } else {
                    EmptyContent()
                }

            } else {
                EmptyContent(Modifier.fillMaxSize())
            }
        }
    }
}

@Preview
@Composable
private fun BuildContentPreview() {
    DetailsScreen(patientInfo = PreviewUtil.mockPatientInfo).BuildContent()
}