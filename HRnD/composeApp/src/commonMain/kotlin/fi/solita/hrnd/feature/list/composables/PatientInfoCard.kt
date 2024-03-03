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
import fi.solita.hrnd.core.utils.PreviewUtil
import fi.solita.hrnd.domain.utils.getAgeAndDateTimeUnit
import fi.solita.hrnd.designSystem.DecorativeBall
import fi.solita.hrnd.designSystem.GenderAndAgeRow
import fi.solita.hrnd.designSystem.GenderAndAgeRowSize
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.*
import io.ktor.websocket.*
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
        Row(
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            Arrangement.SpaceBetween
        ) {

            Column(horizontalAlignment = Alignment.Start) {
                Text("$firstName $lastName", style = MaterialTheme.typography.h3)
                GenderAndAgeRow(gender = gender, dateOfBirth = dateOfBirth, size = GenderAndAgeRowSize.SMALL)
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
        patientInfo = PreviewUtil.mockPatientInfo, onClick = {})
}