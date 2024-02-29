package fi.solita.hrnd.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pressure(
  @SerialName("patient_id") var patientId: String? = null,
  @SerialName("systolic_pressure") var systolicPressure: String? = null,
  @SerialName("diastolic_pressure") var diastolicPressure: String? = null,
  @SerialName("timestamp") var timestamp: String? = null,
)