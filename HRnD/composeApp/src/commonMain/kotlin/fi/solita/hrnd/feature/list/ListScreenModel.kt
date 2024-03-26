package fi.solita.hrnd.feature.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import fi.solita.hrnd.core.data.HealthRepository
import fi.solita.hrnd.core.data.model.PatientInfo
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.annotation.OrbitInternal
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription

class ListScreenModel(
    private val healthRepository: HealthRepository
) : ContainerHost<ListScreenState, ListScreenSideEffect>, ScreenModel {

    override val container: Container<ListScreenState, ListScreenSideEffect> =
        screenModelScope.container(ListScreenState())

    var text by mutableStateOf("")
        private set

    fun fetchPatients() = intent {
        healthRepository.fetchPatients()
        screenModelScope.launch {
            healthRepository.patients.collect {
                reduce {
                    state.copy(
                        patients = it.filterPatientInfo(text),
                        isBusy = false
                    )
                }
            }
        }
    }

    fun onSearchUpdate(searchKeyWord: String) {
        text = searchKeyWord

        intent {
            reduce {
                state.copy(
                    patients =
                    healthRepository
                        .patients
                        .value
                        .filterPatientInfo(text),
                )
            }
        }
    }

    private fun onPatientClicked(patientInfo: PatientInfo) = intent {
        postSideEffect(ListScreenSideEffect.NavigateToPatient(patientInfo))
    }

    private fun onFabClicked() = intent {
        postSideEffect(ListScreenSideEffect.NavigateToQRScreen)
    }

    private fun List<PatientInfo>.filterPatientInfo(keyWord: String?): ImmutableList<PatientInfo> {
        if (keyWord == null) return this.toImmutableList()
        return this.filter { patientInfo ->
            val fullName = "${patientInfo.firstName.orEmpty()} ${patientInfo.lastName.orEmpty()}"
            fullName.contains(keyWord, ignoreCase = true)
        }.toImmutableList()
    }

    fun handleEvent(event: ListScreenEvent) {
        when (event) {
            ListScreenEvent.FabClicked -> onFabClicked()
            is ListScreenEvent.PatientClicked -> onPatientClicked(event.patientInfo)
            is ListScreenEvent.Refresh -> fetchPatients()
        }
    }
}