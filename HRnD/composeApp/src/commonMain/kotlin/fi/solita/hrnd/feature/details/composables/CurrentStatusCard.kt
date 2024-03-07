package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fi.solita.hrnd.domain.Medication
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CurrentStatusCard(
    placement: String?,
    modifier: Modifier = Modifier,
    currentMedication: ImmutableList<Medication>,
    currentDiagnosis: ImmutableList<String>
) {

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.primaryVariant,
        elevation = 20.dp
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(0.4f)) {
                Text("Placement", style = MaterialTheme.typography.h3)
                Spacer(Modifier.height(4.dp))
                Text(placement ?: "Unadmitted", style = MaterialTheme.typography.body2)
                Spacer(Modifier.height(8.dp))

                Text("Diagnosis", style = MaterialTheme.typography.h3)
                Spacer(Modifier.height(4.dp))
                if (currentDiagnosis.isEmpty()) {
                    Text("Diagnosis missing", style = MaterialTheme.typography.body2)
                } else {
                    currentDiagnosis.forEach { diagnosis ->
                        Text(diagnosis, style = MaterialTheme.typography.body2)
                    }
                }
            }
            Spacer(Modifier.size(32.dp))
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(0.6f)) {
                Text("Current medication", style = MaterialTheme.typography.h3)
                Spacer(Modifier.height(4.dp))
                if (currentMedication.isEmpty()) {
                    Text("No medication", style = MaterialTheme.typography.body2)
                } else {
                    currentMedication.forEach { medication ->
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = medication.name + " " +  medication.dosage,
                            style = MaterialTheme.typography.body2,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

//@Preview TODO FIX
//@Composable
//fun PreviewCurrentStatusCard() {
//    CurrentStatusCard(
//        placement = "Room 12a",
//        currentMedication = persistentListOf(
//            Medication(
//                medicationName = "Scorbolamid",
//                dosage = "2",
//                dosageUnit = "pills",
//                startDate = "",
//                endDate = "",
//                frequency = "2x day",
//                patientId = "1"
//            ),
//            Medication(
//                medicationName = "Vitamin C",
//                dosage = "1",
//                dosageUnit = "pill",
//                startDate = "",
//                endDate = "",
//                frequency = "3x day",
//                patientId = "1"
//            )
//        ),
//        currentDiagnosis = persistentListOf("Influenza A", "Broken Leg")
//    )
//}