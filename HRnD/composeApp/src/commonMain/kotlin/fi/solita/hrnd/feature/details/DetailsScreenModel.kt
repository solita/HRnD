package fi.solita.hrnd.feature.details

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import fi.solita.hrnd.core.data.HealthRepository
import fi.solita.hrnd.core.data.model.PatientInfo
import fi.solita.hrnd.domain.PatientDetails
import fi.solita.hrnd.domain.utils.toDomainPatientDetails
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class DetailsScreenModel(
    private val patientInfo: PatientInfo?,
    private val healthRepository: HealthRepository
) : ContainerHost<DetailsScreenState, DetailsScreenSideEffect>, ScreenModel {

    override val container: Container<DetailsScreenState, DetailsScreenSideEffect> =
        screenModelScope.container(DetailsScreenState(patientInfo))

    fun fetchPatientDetails() = intent {
        if (patientInfo?.patientId == null) {
            reduce {
                state.copy(error = DetailsScreenError.PatientIdMissing)
            }
        } else {
            screenModelScope.launch {
                healthRepository.fetchPatientDetails(patientInfo.patientId).collect {
                    reduce {
                        state.copy(patientDetails = it.toDomainPatientDetails())
                    }
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
        when (event) {
            DetailsScreenEvent.OnRefresh -> fetchPatientDetails()
            DetailsScreenEvent.ErrorDismissed -> errorDismissed()
        }
    }
}

data class DetailsScreenState(
    val patientInfo: PatientInfo?,
    val patientDetails: PatientDetails? = null,
    val error: DetailsScreenError? = null
)

sealed class DetailsScreenSideEffect

sealed class DetailsScreenError : Error() {
    data object PatientIdMissing : DetailsScreenError()
}

sealed class DetailsScreenEvent {
    data object ErrorDismissed : DetailsScreenEvent()
    data object OnRefresh : DetailsScreenEvent()
}