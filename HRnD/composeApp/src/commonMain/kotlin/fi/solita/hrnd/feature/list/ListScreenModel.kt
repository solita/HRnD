package fi.solita.hrnd.feature.list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import fi.solita.hrnd.core.data.HealthRepository
import fi.solita.hrnd.core.data.model.PatientInfo
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class ListScreenModel(
        private val healthRepository: HealthRepository
) : ContainerHost<ListScreenState, ListScreenSideEffect>, ScreenModel {

    override val container: Container<ListScreenState, ListScreenSideEffect> =
            screenModelScope.container(ListScreenState())

    fun fetchPatients() = intent {
        healthRepository.fetchPatients()
        screenModelScope.launch {
            healthRepository.patients.collect {
                reduce {
                    state.copy(patients = it.filterPatientInfo(state.patientSearchKeyWord))
                }
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    private fun onSearchUpdate(searchKeyWord: String) = blockingIntent {
        reduce {
            state.copy(
                    patients = healthRepository.patients.value.filterPatientInfo(searchKeyWord),
                    patientSearchKeyWord = searchKeyWord
            )
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
        Napier.i { "handleEvent $event" }
        when (event) {
            ListScreenEvent.OnFabClicked -> onFabClicked()
            is ListScreenEvent.OnPatientClicked -> onPatientClicked(event.patientInfo)
            is ListScreenEvent.OnSearchUpdate -> onSearchUpdate(event.keyWord)
        }
    }
}