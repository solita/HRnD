package fi.solita.hrnd.feature.qr

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Scanner(modifier: Modifier = Modifier, passResult: (String) -> Unit)