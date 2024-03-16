package fi.solita.hrnd.feature.details

sealed class DetailsScreenError : Error() {
    data object PatientDataMissing : DetailsScreenError()
}