package fi.solita.hrnd.presentation.screens.list.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import fi.solita.hrnd.MR
import fi.solita.hrnd.data.model.PatientInfo
import fi.solita.hrnd.domain.utils.getAgeAndDateTimeUnit
import fi.solita.hrnd.presentation.designSystem.DecorativeBall
import kotlinx.datetime.DateTimeUnit

@Composable
fun PatientInfoCard(
    patientInfo: PatientInfo,
    modifier: Modifier = Modifier,
    onClick: (patientId: String?) -> Unit
) {
    with(patientInfo) {
        val ageAndDateTimeUnit = getAgeAndDateTimeUnit(dateOfBirth)
        val age: String = when (ageAndDateTimeUnit?.second) {
            DateTimeUnit.YEAR -> {
                stringResource(MR.strings.years_old,ageAndDateTimeUnit.first.toString())
            }
            DateTimeUnit.MONTH -> {
                stringResource(MR.strings.months_old,ageAndDateTimeUnit.first.toString())
            }
            DateTimeUnit.WEEK -> {
                stringResource(MR.strings.weeks_old,ageAndDateTimeUnit.first.toString())
            }
            else -> stringResource(MR.strings.unknown_age)
        }

        Row(
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            Arrangement.SpaceBetween
        ) {

            Column(horizontalAlignment = Alignment.Start) {
                Text("$firstName $lastName", style = MaterialTheme.typography.h3)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(gender ?: "Unknown gender", style = MaterialTheme.typography.body1)
                    DecorativeBall(
                        Modifier.size(12.dp),
                        color = MaterialTheme.colors.primaryVariant
                    )
                    Text(text = age)
                }
            }
            Column {
                IconButton(onClick = { onClick(patientId) }) {
                    Icon(
                        Icons.Outlined.Info,
                        modifier = Modifier.size(32.dp),
                        contentDescription = "patient info"
                    )
                }
            }
        }
    }
}