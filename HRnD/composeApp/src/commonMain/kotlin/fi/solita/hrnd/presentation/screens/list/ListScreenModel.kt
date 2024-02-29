package fi.solita.hrnd.presentation.screens.list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import fi.solita.hrnd.data.HealthRepository
import fi.solita.hrnd.data.model.PatientInfo
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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

    private fun onPatientClicked(patientId: String?) = intent {
        if (patientId == null){
            return@intent
        }
        postSideEffect(ListScreenSideEffect.NavigateToPatient(patientId))
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
            is ListScreenEvent.OnPatientClicked -> onPatientClicked(event.patientId)
            is ListScreenEvent.OnSearchUpdate -> onSearchUpdate(event.keyWord)
        }
    }
}


data class ListScreenState(
    val patients: ImmutableList<PatientInfo> = persistentListOf(),
    val patientSearchKeyWord: String = ""
)

val listScreenMockState = ListScreenState(
        patients = persistentListOf(
                PatientInfo("1","Michal","Guspiel","9/18/1997", currentRoom = null),
                PatientInfo("1","Johh","Doe","1/29/1979", currentRoom = "12"),
        ),
        patientSearchKeyWord = ""
)

sealed class ListScreenSideEffect {
    data class NavigateToPatient(val patientId: String) : ListScreenSideEffect()
    data object NavigateToQRScreen : ListScreenSideEffect()
}

sealed class ListScreenEvent {
    data object OnFabClicked : ListScreenEvent()
    data class OnPatientClicked(val patientId: String?) : ListScreenEvent()
    data class OnSearchUpdate(val keyWord: String) : ListScreenEvent()
}
