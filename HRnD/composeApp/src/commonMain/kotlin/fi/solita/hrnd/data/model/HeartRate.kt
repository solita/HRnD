package fi.solita.hrnd.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HeartRate(
  @SerialName("patient_id") var patientId: String? = null,
  @SerialName("timestamp") var timestamp: String? = null,
  @SerialName("heart_rate") var heartRate: String? = null
)