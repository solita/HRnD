package fi.solita.hrnd.feature.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import fi.solita.hrnd.core.data.model.PatientInfo
import fi.solita.hrnd.designSystem.EmptyContent
import fi.solita.hrnd.designSystem.theme.HrndTheme
import fi.solita.hrnd.designSystem.theme.Typography
import fi.solita.hrnd.feature.details.DetailsScreen
import fi.solita.hrnd.feature.list.composables.PatientInfoCard
import fi.solita.hrnd.feature.qr.ScanQRScreen
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.*
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

data object ListScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: ListScreenModel = getScreenModel()

        val sideEffect = screenModel.container.sideEffectFlow.collectAsState(null)


        LaunchedEffect(sideEffect.value) {
            Napier.i { "launchEffect ${sideEffect.value}" }
            when (sideEffect.value) {
                is ListScreenSideEffect.NavigateToPatient -> {
                    navigator.push(
                        DetailsScreen(
                            (sideEffect.value as? ListScreenSideEffect.NavigateToPatient)?.patientId
                        )
                    )
                }

                is ListScreenSideEffect.NavigateToQRScreen -> {
                    navigator.push(ScanQRScreen())
                }

                null -> Napier.i { "Side Effect is null" }
            }
        }

        LaunchedEffect(Unit) {
            screenModel.fetchPatients()
        }

        val state by screenModel.container.stateFlow.collectAsState()

        BuildContent(
            state,
            Modifier,
            screenModel::handleEvent
        )
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun BuildContent(
        state: ListScreenState,
        modifier: Modifier = Modifier,
        onEvent: (ListScreenEvent) -> Unit
    ) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                Text(stringResource(Res.string.patients), style = Typography.h1)
                Spacer(Modifier.height(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    value = state.patientSearchKeyWord,
                    onValueChange = { onEvent(ListScreenEvent.OnSearchUpdate(it)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        Icon(
                            Icons.Default.Search,
                            stringResource(Res.string.search_icon_desc)
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(Modifier.height(16.dp))
                AnimatedContent(state.patients.isNotEmpty()) { objectsAvailable ->
                    if (objectsAvailable) {
                        LazyColumn {
                            items(state.patients) { item ->
                                PatientInfoCard(item) {
                                    onEvent(ListScreenEvent.OnPatientClicked(item.patientId))
                                }
                            }
                            item {
                                Spacer(Modifier.height(120.dp))
                            }
                        }
                    } else {
                        EmptyContent(Modifier.fillMaxSize())
                    }
                }
            }

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier.padding(32.dp),
                        onClick = { onEvent(ListScreenEvent.OnFabClicked) },
                        icon = {
                            Icon(
                                Icons.Default.ArrowForward,
                                stringResource(Res.string.scan_qr_fab_desc)
                            )
                        },
                        text = { Text(text = stringResource(Res.string.scan_qr_fab)) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListScreenPreview(){
    HrndTheme {
        Surface {
            ListScreen.BuildContent(state = listScreenMockState, onEvent = {} )
        }
    }
}

val listScreenMockState = ListScreenState(
    patients = persistentListOf(
        PatientInfo("1","Michal","Guspiel","9/18/1997", currentRoom = null),
        PatientInfo("1","Johh","Doe","1/29/1979", currentRoom = "12"),
        ),
    patientSearchKeyWord = ""
)