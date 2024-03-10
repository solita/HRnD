package fi.solita.hrnd.domain

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.datetime.LocalDateTime

data class HeartRate(
    val heartRate: Int,
    val localDateTime : LocalDateTime
) : JavaSerializable