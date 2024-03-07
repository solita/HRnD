package fi.solita.hrnd.domain.utils

import fi.solita.hrnd.core.data.model.PatientDetails
import fi.solita.hrnd.core.utils.parseToLocalDateTime
import fi.solita.hrnd.domain.HeartRate
import fi.solita.hrnd.domain.Medication
import fi.solita.hrnd.domain.Pressure
import fi.solita.hrnd.domain.Surgery
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun PatientDetails.toDomainPatientDetails(): fi.solita.hrnd.domain.PatientDetails {

    val medication = medication?.map {
        Medication(
            name = it.medicationName,
            dosage = it.dosage + it.dosageUnit + " " + it.frequency,
            startDate = it.startDate.parseToLocalDate(),
            endDate = it.endDate.parseToLocalDate()
        )
    }

    val surgery = surgery?.map {
        Surgery(
            name = it.surgeryName,
            date = it.surgeryDate?.parseToLocalDate(),
            outcome = it.surgeryOutcome,
            description = it.surgeryDescription
        )
    }

    val heartRate: ImmutableList<HeartRate>? = heartRate?.map {
        HeartRate(
            it.heartRate?.toIntOrNull() ?: return@map null,
            it.timestamp.parseToLocalDateTime() ?: return@map null
        )
    }?.sortedBy { it?.localDateTime }?.filterNotNull()?.toImmutableList()

    val mostRecentHeartRateRecordedDate = heartRate?.maxOfOrNull { it.localDateTime }
    Napier.i { "MostRecent Heart Rate Recored date: $mostRecentHeartRateRecordedDate" }

    val mostRecentHeartRate: ImmutableList<HeartRate>? = heartRate?.filter {
        it.localDateTime.date == mostRecentHeartRateRecordedDate?.date
    }?.sortedBy { it.localDateTime }?.toImmutableList()

    Napier.i { "Most recent heart Rate: $mostRecentHeartRate" }

    val pressure: ImmutableList<Pressure>? = pressure?.map {
        Pressure(
            systolicPressure = it.systolicPressure?.toDoubleOrNull() ?: return@map null,
            diastolicPressure = it.diastolicPressure?.toDoubleOrNull() ?: return@map null,
            localDateTime = it.timestamp.parseToLocalDateTime() ?: return@map null
        )
    }?.sortedBy { it?.localDateTime }?.filterNotNull()?.toImmutableList()

    val mostRecentPressureRecorded = pressure?.maxOfOrNull { it.localDateTime }

    val mostRecentPressure: ImmutableList<Pressure>? = pressure?.filter {
        it.localDateTime.date == mostRecentPressureRecorded?.date
    }?.sortedBy { it.localDateTime }?.toImmutableList()

    return fi.solita.hrnd.domain.PatientDetails(
        patientId = patientId,
        medication = medication?.toImmutableList(),
        surgery = surgery?.toImmutableList(),
        fullHeartRate = heartRate,
        mostRecentDayHeartRate = mostRecentHeartRate,
        fullPressure = pressure,
        mostRecentDayPressure = mostRecentPressure
    )
}