package fi.solita.hrnd.feature.list

import fi.solita.hrnd.core.data.model.PatientInfo

sealed class ListScreenSideEffect {
    data class NavigateToPatient(val patientInfo: PatientInfo) : ListScreenSideEffect()
    data object NavigateToQRScreen : ListScreenSideEffect()
}