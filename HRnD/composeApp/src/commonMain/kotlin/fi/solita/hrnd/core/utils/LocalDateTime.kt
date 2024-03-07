package fi.solita.hrnd.core.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun String?.parseToLocalDateTime(): LocalDateTime? {
    if (this == null) return null
    val instant = Instant.parse(this)
    return instant.toLocalDateTime(TimeZone.UTC)
}
