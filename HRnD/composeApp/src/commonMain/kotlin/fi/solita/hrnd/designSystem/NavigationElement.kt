package fi.solita.hrnd.designSystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.solita.hrnd.domain.utils.Platform
import fi.solita.hrnd.domain.utils.getPlatform
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.back_button_desc
import hrnd.composeapp.generated.resources.chevron_ios
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun NavigationElement(
    previousScreenTitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier.height(22.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (getPlatform() == Platform.iOS) {
            IconButton(onClick = { onClick() }) {
                Icon(
                    painterResource(Res.drawable.chevron_ios),
                    contentDescription = stringResource(Res.string.back_button_desc)
                )
            }

            Text(text = previousScreenTitle, style = MaterialTheme.typography.h5)
        }
    }
}