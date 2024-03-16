package fi.solita.hrnd.feature.qr

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import fi.solita.hrnd.designSystem.NavigationElement
import fi.solita.hrnd.domain.utils.Platform
import fi.solita.hrnd.domain.utils.getPlatform
import fi.solita.hrnd.feature.details.DetailsScreen
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.nav_list
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

class ScanQRScreen : Screen {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column {
            // In android we don't want even a spacer on top.
            if (getPlatform() == Platform.iOS) {
                NavigationElement(
                    modifier = Modifier.padding(16.dp),
                    previousScreenTitle = stringResource(Res.string.nav_list)
                ) {
                    navigator.pop()
                }
            }
            Scanner {
                navigator.push(DetailsScreen(patientInfo = null, patientId = it))
            }
        }
    }
}