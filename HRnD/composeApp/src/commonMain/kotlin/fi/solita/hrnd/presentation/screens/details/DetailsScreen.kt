package fi.solita.hrnd.presentation.screens.details

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class DetailsScreen(val patientId : String?): Screen {
    @Composable
    override fun Content() {
        Text("Details Screen $patientId")
    }
}