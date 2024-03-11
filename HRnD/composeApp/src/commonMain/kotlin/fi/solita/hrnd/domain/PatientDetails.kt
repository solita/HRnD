package fi.solita.hrnd.domain

import kotlinx.collections.immutable.ImmutableList

data class PatientDetails(
    val patientId: String,
    val mostRecentDayHeartRate: ChartData?,
    val fullHeartRate: ChartData?,
    val mostRecentDaySystolicPressure: ChartData?,
    val mostRecentDayDiastolicPressure: ChartData?,
    val fullSystolicPressure: ChartData?,
    val fullDiastolicPressure: ChartData?,
    val medication: ImmutableList<Medication>?,
    val surgery: ImmutableList<Surgery>?
)