package fi.solita.hrnd.feature.details

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import fi.solita.hrnd.core.data.HealthRepository
import fi.solita.hrnd.core.data.model.PatientInfo
import fi.solita.hrnd.domain.PatientDetails
import fi.solita.hrnd.domain.utils.toDomainPatientDetails
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class DetailsScreenModel(
    patientInfo: PatientInfo?,
    private val healthRepository: HealthRepository
) : ContainerHost<DetailsScreenState, DetailsScreenSideEffect>, ScreenModel {

    init {
        Napier.i { "init DetailsScreenModel" }
    }

    override val container: Container<DetailsScreenState, DetailsScreenSideEffect> =
        screenModelScope.container(DetailsScreenState(patientInfo))

    private fun screenInitialized(patientInfo: PatientInfo?, patientId: String?) = intent {
        if (patientId == null && patientInfo == null) {
            reduce {
                state.copy(
                    error = DetailsScreenError.PatientDataMissing
                )
            }
            return@intent
        }

        if (patientInfo == null) {
            fetchPatientInfo(patientId)
        }
        fetchPatientDetails(patientId ?: patientInfo?.patientId)
    }

    private fun fetchPatientInfo(patientId: String?) = intent {
        Napier.i { "fetchPatientInfo $patientId" }
        val fetchedPatientInfo = patientId?.let {
            healthRepository.fetchPatientInfo(patientId)
        }
        reduce {
            state.copy(patientInfo = fetchedPatientInfo)
        }
    }

    private fun fetchPatientDetails(patientId: String?) = intent {
        if (patientId == null){
            return@intent
        }
        Napier.i { "fetchPatientDetails" }
        screenModelScope.launch {
                healthRepository.fetchPatientDetails(patientId).collect {
                    reduce {
                        state.copy(
                            patientDetails = it.toDomainPatientDetails(),
                            patientDetailsFetched = true
                        )
                }
            }
        }
    }

    private fun errorDismissed() = intent {
        reduce {
            state.copy(error = null)
        }
    }

    fun handleEvent(event: DetailsScreenEvent) {
        Napier.i { "handleEvent $event" }
        when (event) {
            DetailsScreenEvent.ErrorDismissed -> errorDismissed()
            is DetailsScreenEvent.ScreenInitialized -> screenInitialized(
                event.patientInfo,
                event.patientId
            )
        }
    }
}

data class DetailsScreenState(
    val patientInfo: PatientInfo?,
    val patientDetailsFetched: Boolean = false,
    val patientDetails: PatientDetails? = null,
    val error: DetailsScreenError? = null
)

sealed class DetailsScreenSideEffect

sealed class DetailsScreenError : Error() {
    data object PatientDataMissing : DetailsScreenError()
}

sealed class DetailsScreenEvent {
    data object ErrorDismissed : DetailsScreenEvent()
    data class ScreenInitialized(val patientInfo: PatientInfo?, val patientId: String?) :
        DetailsScreenEvent()
}