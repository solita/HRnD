package fi.solita.hrnd.domain

import kotlinx.datetime.LocalDate

data class Surgery(
    val name: String,
    val date: LocalDate?,
    val outcome: String? = null,
    val description: String? = null
)