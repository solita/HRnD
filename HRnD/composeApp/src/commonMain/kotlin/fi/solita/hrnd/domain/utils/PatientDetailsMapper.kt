package fi.solita.hrnd.domain.utils

import fi.solita.hrnd.core.data.model.PatientDetails
import fi.solita.hrnd.core.utils.parseToLocalDateTime
import fi.solita.hrnd.domain.ChartData
import fi.solita.hrnd.domain.ChartDataEntry
import fi.solita.hrnd.domain.ChartDataType
import fi.solita.hrnd.domain.Medication
import fi.solita.hrnd.domain.Surgery
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


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

    val heartRate = ChartData(
        type = ChartDataType.HEART_RATE,
        entries = heartRate?.map {
            ChartDataEntry(
                timeStampEpochSeconds = it.timestamp.parseToEpochSeconds() ?: return@map null,
                value = it.heartRate?.toDoubleOrNull() ?: return@map null
            )
        }?.filterNotNull()?.sortedBy { it.timeStampEpochSeconds }
    )

    val mostRecentHeartRateRecordedDate = heartRate.getMostRecentDay()

    Napier.i { "MostRecent Heart Rate recorded date: $mostRecentHeartRateRecordedDate" }

    val mostRecentHeartRate: ChartData = heartRate.copy(
        entries = heartRate.entries?.filter {
            Instant.fromEpochSeconds(it.timeStampEpochSeconds)
                .toLocalDateTime(TimeZone.UTC).date == mostRecentHeartRateRecordedDate
        }
    )

    Napier.i { "Most recent heart Rate: $mostRecentHeartRate" }

    val systolicPressure = createPressureChartData(ChartDataType.SYSTOLIC_PRESSURE, pressure) {
        it.systolicPressure?.toDoubleOrNull()
    }

    val diastolicPressure = createPressureChartData(ChartDataType.DIASTOLIC_PRESSURE, pressure) {
        it.diastolicPressure?.toDoubleOrNull()
    }

    val mostRecentSystolicPressureRecorded = systolicPressure.getMostRecentDay()
    val mostRecentDiastolicPressureRecorded = diastolicPressure.getMostRecentDay()

    val mostRecentSystolicPressure = systolicPressure.copy(
        entries = systolicPressure.entries?.filter {
            Instant.fromEpochSeconds(it.timeStampEpochSeconds)
                .toLocalDateTime(TimeZone.UTC).date == mostRecentSystolicPressureRecorded
        }
    )

    val mostRecentDiastolicPressure = diastolicPressure.copy(
        entries = diastolicPressure.entries?.filter {
            Instant.fromEpochSeconds(it.timeStampEpochSeconds)
                .toLocalDateTime(TimeZone.UTC).date == mostRecentDiastolicPressureRecorded
        }
    )

    return fi.solita.hrnd.domain.PatientDetails(
        patientId = patientId,
        medication = medication?.toImmutableList(),
        surgery = surgery?.toImmutableList(),
        fullHeartRate = heartRate,
        mostRecentDayHeartRate = mostRecentHeartRate,
        fullSystolicPressure = systolicPressure,
        fullDiastolicPressure = diastolicPressure,
        mostRecentDaySystolicPressure = mostRecentSystolicPressure,
        mostRecentDayDiastolicPressure = mostRecentDiastolicPressure
    )
}

fun ChartData.getMostRecentDay(): LocalDate? {
    return this.entries?.maxOfOrNull { it.timeStampEpochSeconds }
        ?.let { Instant.fromEpochSeconds(it) }
        ?.toLocalDateTime(TimeZone.UTC)?.date
}

fun String?.parseToEpochSeconds(): Long? {
    return this.parseToLocalDateTime()
        ?.toInstant(TimeZone.UTC)?.epochSeconds
}

fun createPressureChartData(
    type: ChartDataType,
    pressure: List<fi.solita.hrnd.core.data.model.Pressure>?,
    pressureExtractor: (fi.solita.hrnd.core.data.model.Pressure) -> Double?
): ChartData {
    return ChartData(
        type = type,
        entries = pressure?.map { pressureData ->
            ChartDataEntry(
                timeStampEpochSeconds = pressureData.timestamp.parseToEpochSeconds()
                    ?: return@map null,
                value = pressureExtractor(pressureData) ?: return@map null
            )
        }?.filterNotNull()?.sortedBy { it.timeStampEpochSeconds }
    )
}