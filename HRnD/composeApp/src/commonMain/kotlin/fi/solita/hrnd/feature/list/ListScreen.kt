package fi.solita.hrnd.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import fi.solita.hrnd.designSystem.NoDataAvailable
import fi.solita.hrnd.designSystem.theme.HrndTheme
import fi.solita.hrnd.designSystem.theme.Typography
import fi.solita.hrnd.feature.details.DetailsScreen
import fi.solita.hrnd.feature.list.composables.PatientInfoCard
import fi.solita.hrnd.feature.qr.ScanQRScreen
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.patients
import hrnd.composeapp.generated.resources.scan_qr_fab
import hrnd.composeapp.generated.resources.scan_qr_fab_desc
import hrnd.composeapp.generated.resources.search_icon_desc
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
                            (sideEffect.value as? ListScreenSideEffect.NavigateToPatient)?.patientInfo
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

    @OptIn(ExperimentalResourceApi::class, ExperimentalMaterialApi::class)
    @Composable
    fun BuildContent(
        state: ListScreenState,
        modifier: Modifier = Modifier,
        onEvent: (ListScreenEvent) -> Unit
    ) {
        val pullRefreshState = rememberPullRefreshState(
            onRefresh = { onEvent(ListScreenEvent.Refresh) },
            refreshing = state.isBusy
        )
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .pullRefresh(pullRefreshState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    PullRefreshIndicator(state.isBusy, pullRefreshState)
                }
                item {
                    Text(
                        text = stringResource(Res.string.patients),
                        style = Typography.h1,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                item {
                    TextField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                        value = state.patientSearchKeyWord,
                        onValueChange = { onEvent(ListScreenEvent.SearchUpdate(it)) },
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
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }

                if (state.patients.isNotEmpty()) {
                    items(state.patients) { item ->
                        PatientInfoCard(item) {
                            onEvent(ListScreenEvent.PatientClicked(item))
                        }
                    }
                    item {
                        Spacer(Modifier.height(120.dp))
                    }
                } else {
                    item {
                        NoDataAvailable(Modifier.fillMaxSize())
                    }
                }
            }

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier.padding(32.dp),
                        onClick = { onEvent(ListScreenEvent.FabClicked) },
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowForward,
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
private fun ListScreenPreview() {
    HrndTheme {
        Surface {
            ListScreen.BuildContent(state = listScreenMockState, onEvent = {})
        }
    }
}

val listScreenMockState = ListScreenState(
    patients = persistentListOf(
        PatientInfo("1", "Michal", "Guspiel", "9/18/1997", currentRoom = null),
        PatientInfo("1", "Johh", "Doe", "1/29/1979", currentRoom = "12"),
    ),
    patientSearchKeyWord = ""
)