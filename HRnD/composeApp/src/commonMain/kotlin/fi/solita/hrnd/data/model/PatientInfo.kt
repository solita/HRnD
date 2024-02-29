package fi.solita.hrnd.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientInfo(
  @SerialName("patient_id") val patientId: String? = null,
  @SerialName("first_name") val firstName: String? = null,
  @SerialName("last_name") val lastName: String? = null,
  @SerialName("date_of_birth") val dateOfBirth: String? = null,
  @SerialName("gender") val gender: String? = null,
  @SerialName("blood_type") val bloodType: String? = null,
  @SerialName("height") val height: String? = null,
  @SerialName("weight") val weight: String? = null,
  @SerialName("allergies") val allergies: String? = null,
  @SerialName("current_room") val currentRoom: String? = null
)