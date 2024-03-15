package fi.solita.hrnd.feature.qr

import ScannerFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun Scanner(modifier: Modifier, passResult: (String) -> Unit) {
    UIKitViewController(
        modifier = Modifier,
        factory = { ScannerFactory.shared!!.makeController(passValue = {
            Napier.i {
                "Reading in Compose Multiplatform Passed value $it"
            }
            passResult(it)
        }) },
    )
}

