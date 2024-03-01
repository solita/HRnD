package fi.solita.hrnd.feature.list.composables

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
import fi.solita.hrnd.core.data.model.PatientInfo
import fi.solita.hrnd.domain.utils.getAgeAndDateTimeUnit
import fi.solita.hrnd.designSystem.DecorativeBall
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.*
import kotlinx.datetime.DateTimeUnit
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
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
                stringResource(Res.string.years_old,ageAndDateTimeUnit.first.toString())
            }
            DateTimeUnit.MONTH -> {
                stringResource(Res.string.months_old,ageAndDateTimeUnit.first.toString())
            }
            DateTimeUnit.WEEK -> {
                stringResource(Res.string.weeks_old,ageAndDateTimeUnit.first.toString())
            }
            else -> stringResource(Res.string.unknown_age)
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
                    Text(gender ?: stringResource(Res.string.unknown_gender), style = MaterialTheme.typography.body1)
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
                        contentDescription = stringResource(Res.string.patient_info_desc)
                    )
                }
            }
        }
    }
}

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