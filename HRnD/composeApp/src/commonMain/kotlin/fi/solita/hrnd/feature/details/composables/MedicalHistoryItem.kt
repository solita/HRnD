package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MedicalHistoryItem(surgery: fi.solita.hrnd.domain.Surgery, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = surgery.date.toString())
        Text(surgery.name)
        surgery.outcome?.let { outcome ->
            Text(outcome)
        }
        surgery.description?.let { desc ->
            Text(desc)
        }
    }
}