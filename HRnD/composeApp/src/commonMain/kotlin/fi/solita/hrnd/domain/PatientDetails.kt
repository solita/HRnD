package fi.solita.hrnd.domain

import kotlinx.collections.immutable.ImmutableList

data class PatientDetails(
    val patientId: String,
    val mostRecentDayHeartRate: ImmutableList<HeartRate>?,
    val fullHeartRate: ImmutableList<HeartRate>?,
    val mostRecentDayPressure: ImmutableList<Pressure>?,
    val fullPressure: ImmutableList<Pressure>?,
    val medication: ImmutableList<Medication>?,
    val surgery: ImmutableList<Surgery>?
)