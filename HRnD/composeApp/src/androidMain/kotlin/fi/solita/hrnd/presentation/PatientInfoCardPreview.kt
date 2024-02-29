package fi.solita.hrnd.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import fi.solita.hrnd.data.model.PatientInfo
import fi.solita.hrnd.presentation.screens.list.composables.PatientInfoCard

@Preview
@Composable
fun PatientInfoCardPreivew() {
    PatientInfoCard(
            patientInfo = PatientInfo(
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
            ), onClick = {})
}