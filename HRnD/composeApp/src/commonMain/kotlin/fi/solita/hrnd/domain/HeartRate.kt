package fi.solita.hrnd.domain

import kotlinx.datetime.LocalDateTime

data class HeartRate(
    val heartRate: Int,
    val localDateTime : LocalDateTime
)