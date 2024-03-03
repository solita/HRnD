package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fi.solita.hrnd.core.data.model.Surgery

@Composable
fun MedicalHistoryItem(surgery: Surgery, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(surgery.surgeryDate ?: "Unknown date")
        Text(surgery.surgeryName)
        surgery.surgeryOutcome?.let { outcome ->
            Text(outcome)
        }
        surgery.surgeryDescription?.let { desc ->
            Text(desc)
        }
    }
}