package fi.solita.hrnd.presentation.screens.qr

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class ScanQRScreen : Screen {

    @Composable
    override fun Content() {
        Text("QR code Screen!")
    }
}