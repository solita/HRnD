package fi.solita.hrnd.presentation.screens.list

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import dev.icerock.moko.resources.compose.stringResource
import fi.solita.hrnd.MR
import fi.solita.hrnd.presentation.designSystem.EmptyContent
import fi.solita.hrnd.presentation.designSystem.theme.Typography
import fi.solita.hrnd.presentation.screens.details.DetailsScreen
import fi.solita.hrnd.presentation.screens.list.composables.PatientInfoCard
import fi.solita.hrnd.presentation.screens.qr.ScanQRScreen
import io.github.aakira.napier.Napier

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

                null -> {
                    Napier.i { "Side Effect is null" }
                }
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
                Text(stringResource(MR.strings.patients), style = Typography.h1)
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
                            stringResource(MR.strings.search_icon_desc)
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
                                stringResource(MR.strings.scan_qr_fab_desc)
                            )
                        },
                        text = { Text(text = stringResource(MR.strings.scan_qr_fab)) },
                    )
                }
            }
        }
    }
}
