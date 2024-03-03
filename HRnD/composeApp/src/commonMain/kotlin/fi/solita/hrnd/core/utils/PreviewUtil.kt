package fi.solita.hrnd.core.utils

import fi.solita.hrnd.core.data.model.PatientInfo

object PreviewUtil {

    // Mock of Patient Info used for the previews.
    val mockPatientInfo = PatientInfo(
        patientId = "",
        firstName = "Tester",
        lastName = "Test",
        gender = "male",
        bloodType = "A rh-",
        height = "175 cm",
        weight = "80 kg",
        dateOfBirth = "1/29/1979",
        currentRoom = "70",
        allergies = null,
    )
}