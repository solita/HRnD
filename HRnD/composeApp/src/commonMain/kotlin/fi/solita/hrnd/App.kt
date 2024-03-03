package fi.solita.hrnd

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import fi.solita.hrnd.designSystem.theme.HrndTheme
import fi.solita.hrnd.feature.list.ListScreen

@Composable
fun App() {
    HrndTheme {
        Scaffold {
            Navigator(ListScreen)
        }
    }
}
