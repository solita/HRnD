package fi.solita.hrnd.presentation

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import fi.solita.hrnd.core.designSystem.theme.HrndTheme
import fi.solita.hrnd.feature.list.ListScreen
import fi.solita.hrnd.feature.list.listScreenMockState

@Preview
@Composable
private fun ListScreenPreview(){
    HrndTheme {
        Surface {
            ListScreen.BuildContent(state = listScreenMockState, onEvent = {} )
        }
    }
}