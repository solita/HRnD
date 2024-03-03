package fi.solita.hrnd.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//TODO CREATE A DATAMODEL AND UPDATE SERVER

@Serializable
data class MedicalHistory(
    @SerialName("patient_id") val patientId: String
)