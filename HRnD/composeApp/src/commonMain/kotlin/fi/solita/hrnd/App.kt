package fi.solita.hrnd

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import fi.solita.hrnd.core.designSystem.theme.HrndTheme
import fi.solita.hrnd.feature.list.ListScreen

@Composable
fun App() {
    HrndTheme {
        Scaffold {
            Navigator(ListScreen){ navigator ->
                SlideTransition(navigator)
            }
        }
    }
}
