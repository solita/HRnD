package fi.solita.hrnd.core.data.model

data class PatientDetails(
    val patientId: String,
    val heartRate: List<Any>? = null,
    val pressure: List<Pressure>? = null,
    val medication: List<Medication>? = null,
    val surgery: List<Surgery>? = null,
    val medicalHistory: List<MedicalHistory>? = null
)