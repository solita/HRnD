package fi.solita.hrnd.domain

import kotlinx.datetime.LocalDateTime

data class Pressure(
    val systolicPressure: Double,
    val diastolicPressure: Double,
    val localDateTime : LocalDateTime
)