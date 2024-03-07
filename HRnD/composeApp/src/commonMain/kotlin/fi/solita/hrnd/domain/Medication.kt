package fi.solita.hrnd.domain

import kotlinx.datetime.LocalDate

data class Medication(
    val name: String,
    val dosage: String,
    val startDate: LocalDate?,
    val endDate: LocalDate?
)