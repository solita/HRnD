package fi.solita.hrnd.feature.list

import fi.solita.hrnd.core.data.model.PatientInfo

sealed class ListScreenEvent {
    data object FabClicked : ListScreenEvent()
    data class PatientClicked(val patientInfo: PatientInfo) : ListScreenEvent()
    data class SearchUpdate(val keyWord: String) : ListScreenEvent()
    data object Refresh : ListScreenEvent()
}