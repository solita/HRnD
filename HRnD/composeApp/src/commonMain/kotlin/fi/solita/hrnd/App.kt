package fi.solita.hrnd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import fi.solita.hrnd.designSystem.theme.HrndTheme
import fi.solita.hrnd.feature.list.ListScreen

@Composable
fun App() {
    HrndTheme {
        Scaffold(
            Modifier
                .background(MaterialTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Navigator(ListScreen)
        }
    }
}
