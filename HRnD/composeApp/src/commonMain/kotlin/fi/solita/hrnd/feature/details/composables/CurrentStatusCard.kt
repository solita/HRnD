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
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.current_medication_label
import hrnd.composeapp.generated.resources.diagnosis_label
import hrnd.composeapp.generated.resources.diagnosis_missing
import hrnd.composeapp.generated.resources.no_medication
import hrnd.composeapp.generated.resources.not_admitted
import hrnd.composeapp.generated.resources.placement_label
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
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
                Text(stringResource(Res.string.placement_label), style = MaterialTheme.typography.h3)
                Spacer(Modifier.height(4.dp))
                Text(placement ?: stringResource(Res.string.not_admitted), style = MaterialTheme.typography.body2)
                Spacer(Modifier.height(8.dp))

                Text(stringResource(Res.string.diagnosis_label), style = MaterialTheme.typography.h3)
                Spacer(Modifier.height(4.dp))
                if (currentDiagnosis.isEmpty()) {
                    Text(stringResource(Res.string.diagnosis_missing), style = MaterialTheme.typography.body2)
                } else {
                    currentDiagnosis.forEach { diagnosis ->
                        Text(diagnosis, style = MaterialTheme.typography.body2)
                    }
                }
            }
            Spacer(Modifier.size(32.dp))
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(0.6f)) {
                Text(stringResource(Res.string.current_medication_label), style = MaterialTheme.typography.h3, textAlign = TextAlign.End)
                Spacer(Modifier.height(4.dp))
                if (currentMedication.isEmpty()) {
                    Text(stringResource(Res.string.no_medication), style = MaterialTheme.typography.body2)
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