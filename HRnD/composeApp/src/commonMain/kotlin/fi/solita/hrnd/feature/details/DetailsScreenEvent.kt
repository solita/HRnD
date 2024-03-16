package fi.solita.hrnd.feature.details

import fi.solita.hrnd.core.data.model.PatientInfo

sealed class DetailsScreenEvent {
    data object ErrorDismissed : DetailsScreenEvent()
    data class ScreenInitialized(val patientInfo: PatientInfo?, val patientId: String?) :
        DetailsScreenEvent()
}