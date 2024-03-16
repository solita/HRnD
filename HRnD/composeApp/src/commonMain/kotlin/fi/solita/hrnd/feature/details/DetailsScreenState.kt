package fi.solita.hrnd.feature.details

import fi.solita.hrnd.core.data.model.PatientInfo
import fi.solita.hrnd.domain.PatientDetails

data class DetailsScreenState(
    val patientInfo: PatientInfo?,
    val patientDetailsFetched: Boolean = false,
    val patientDetails: PatientDetails? = null,
    val error: DetailsScreenError? = null
)