package fi.solita.hrnd.feature.list

import fi.solita.hrnd.core.data.model.PatientInfo

sealed class ListScreenEvent {
    data object OnFabClicked : ListScreenEvent()
    data class OnPatientClicked(val patientInfo: PatientInfo) : ListScreenEvent()
    data class OnSearchUpdate(val keyWord: String) : ListScreenEvent()
}