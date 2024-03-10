package fi.solita.hrnd.domain

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.datetime.LocalDateTime

data class Pressure(
    val systolicPressure: Double,
    val diastolicPressure: Double,
    val localDateTime : LocalDateTime
): JavaSerializable