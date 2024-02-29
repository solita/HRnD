package fi.solita.hrnd.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Surgery(
  @SerialName("patient_id") var patientId: String? = null,
  @SerialName("surgery_name") var surgeryName: String? = null,
  @SerialName("surgery_date") var surgeryDate: String? = null,
  @SerialName("surgery_outcome") var surgeryOutcome: String? = null,
  @SerialName("surgery_description") var surgeryDescription: String? = null
)