package fi.solita.hrnd.data

import fi.solita.hrnd.data.model.PatientInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface HealthRepository {

    val patients: StateFlow<List<PatientInfo>>

    suspend fun fetchPatients()
}

class HealthRepositoryImpl(
        private val museumApi: HealthApi,
) : HealthRepository {

    private val _patients: MutableStateFlow<List<PatientInfo>> = MutableStateFlow(listOf())
    override val patients: StateFlow<List<PatientInfo>> = _patients

    override suspend fun fetchPatients() {
        _patients.update {
            museumApi.fetchPatientsData()
        }
    }
}
