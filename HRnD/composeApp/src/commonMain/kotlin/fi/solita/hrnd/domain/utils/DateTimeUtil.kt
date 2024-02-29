package fi.solita.hrnd.domain.utils

import fi.solita.hrnd.domain.Age
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.toLocalDateTime

fun getAge(
    birthDate: String?,
    today: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
): Age? {
    if (birthDate == null) {
        Napier.e("Birth date missing")
        return null
    }
    val dateParts = birthDate.split("/")
    if (dateParts.size != 3) {
        Napier.e("Invalid date of birth format")
        return null
    }
    val (monthStr, dayStr, yearStr) = dateParts
    val month = monthStr.toIntOrNull() ?: return null
    val day = dayStr.toIntOrNull() ?: return null
    val year = yearStr.toIntOrNull() ?: return null

    val birthLocalDate = LocalDate(year, month, day)
    val monthsUntil = DateTimePeriod(months = birthLocalDate.monthsUntil(today))
    val daysUntil = DateTimePeriod(days = birthLocalDate.daysUntil(today))
    return Age(
        monthsUntil.years,
        monthsUntil.months,
        daysUntil.days
    )
}

fun getAgeAndDateTimeUnit(dateOfBirth: String?): Pair<Int, DateTimeUnit>? {
    val age = getAge(dateOfBirth) ?: return null
    return when {
        age.months < 1 -> {
            age.days.rem(7) to DateTimeUnit.WEEK
        }

        age.years > 1 -> {
            age.years to DateTimeUnit.YEAR
        }

        else -> {
            age.months to DateTimeUnit.MONTH
        }

    }
}