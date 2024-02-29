package fi.solita.hrnd.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Medication(
  @SerialName("medication_id") var medicationId: String? = null,
  @SerialName("patient_id") var patientId: String? = null,
  @SerialName("medication_name") var medicationName: String? = null,
  @SerialName("dosage") var dosage: String? = null,
  @SerialName("dosage_unit") var dosageUnit: String? = null,
  @SerialName("frequency") var frequency: String? = null,
  @SerialName("start_date") var startDate: String? = null,
  @SerialName("end_date") var endDate: String? = null
)