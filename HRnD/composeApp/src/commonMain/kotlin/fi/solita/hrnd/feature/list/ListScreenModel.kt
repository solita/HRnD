package fi.solita.hrnd.feature.list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import fi.solita.hrnd.core.data.HealthRepository
import fi.solita.hrnd.core.data.model.PatientInfo
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListScreenModel(
    private val healthRepository: HealthRepository
) : ScreenModel {


    private val _state = MutableStateFlow(ListScreenState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<ListScreenSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun fetchPatients() {
        screenModelScope.launch {
            healthRepository.fetchPatients()
            healthRepository.patients.collect { patients ->
                _state.update { state ->
                    _state.value.copy(
                        patients = patients.filterPatientInfo(state.searchKeyWord),
                        isBusy = false
                    )
                }
            }
        }
    }

    private fun onSearchUpdate(searchKeyWord: String) {
        _state.update {
            _state.value.copy(
                searchKeyWord = searchKeyWord,
            )
        }
    }

    private fun onPatientClicked(patientInfo: PatientInfo) {
        screenModelScope.launch {
            _sideEffect.send(ListScreenSideEffect.NavigateToPatient(patientInfo))
        }
    }

    private fun onFabClicked() {
        screenModelScope.launch {
            _sideEffect.send(ListScreenSideEffect.NavigateToQRScreen)
        }
    }

    private fun List<PatientInfo>.filterPatientInfo(keyWord: String?): ImmutableList<PatientInfo> {
        if (keyWord == null) return this.toImmutableList()
        return this.filter { patientInfo ->
            val fullName =
                "${patientInfo.firstName.orEmpty()} ${patientInfo.lastName.orEmpty()}"
            fullName.contains(keyWord, ignoreCase = true)
        }.toImmutableList()
    }

    fun handleEvent(event: ListScreenEvent) {
        when (event) {
            is ListScreenEvent.SearchUpdate -> onSearchUpdate(event.keyWord)
            is ListScreenEvent.PatientClicked -> onPatientClicked(event.patientInfo)
            is ListScreenEvent.Refresh -> fetchPatients()
            ListScreenEvent.FabClicked -> onFabClicked()
        }
    }

    init {
        Napier.i { "Init screen model!" }
        fetchPatients()
        debounceSearch()
    }

    @OptIn(FlowPreview::class)
    private fun debounceSearch() {
        screenModelScope.launch {
            _state.map { it.searchKeyWord }.debounce(500).collectLatest {searchKeyWord ->
                _state.update {
                    _state.value.copy(
                        patients = healthRepository.patients.value.filterPatientInfo(searchKeyWord)
                    )
                }
            }
        }
    }
}