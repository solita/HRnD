package fi.solita.hrnd.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Medication(
    @SerialName("patient_id") var patientId: String,
    @SerialName("medication_name") var medicationName: String,
    @SerialName("dosage") var dosage: String,
    @SerialName("dosage_unit") var dosageUnit: String,
    @SerialName("frequency") var frequency: String,
    @SerialName("start_date") var startDate: String,
    @SerialName("end_date") var endDate: String
)