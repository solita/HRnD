package fi.solita.hrnd.feature.list

sealed class ListScreenEvent {
    data object OnFabClicked : ListScreenEvent()
    data class OnPatientClicked(val patientId: String?) : ListScreenEvent()
    data class OnSearchUpdate(val keyWord: String) : ListScreenEvent()
}