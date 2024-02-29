package fi.solita.hrnd.feature.list

sealed class ListScreenSideEffect {
    data class NavigateToPatient(val patientId: String) : ListScreenSideEffect()
    data object NavigateToQRScreen : ListScreenSideEffect()
}