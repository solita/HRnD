package fi.solita.hrnd.core.data

import fi.solita.hrnd.core.data.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

interface HealthRepository {

    val patients: StateFlow<List<PatientInfo>>

    suspend fun fetchPatients()

    suspend fun fetchPatientInfo(id: String) : PatientInfo?

    suspend fun fetchPatientDetails(patientId: String): Flow<PatientDetails>
}

class HealthRepositoryImpl(
    private val healthApi: HealthApi,
    private val dispatcher: CoroutineDispatcher
) : HealthRepository {

    private val _patients: MutableStateFlow<List<PatientInfo>> = MutableStateFlow(listOf())
    override val patients: StateFlow<List<PatientInfo>> = _patients

    override suspend fun fetchPatients() {
        _patients.update {
            healthApi.fetchPatientsData()
        }
    }

    override suspend fun fetchPatientInfo(id: String): PatientInfo? {
        return healthApi.fetchPatientInfo(patientId = id)
    }

    override suspend fun fetchPatientDetails(patientId: String): Flow<PatientDetails> = channelFlow {
        var patientDetails = PatientDetails(patientId)
        withContext(dispatcher) {
            listOf(
                launch {
                    val heartRateData = healthApi.fetchPatientHeartRate(patientId)
                    patientDetails = patientDetails.copy(heartRate = heartRateData)
                    send(patientDetails)
                },
                launch {
                    val pressureData = healthApi.fetchPatientHeartPressure(patientId)
                    patientDetails = patientDetails.copy(pressure = pressureData)
                    send(patientDetails)
                },
                launch {
                    val medication = healthApi.fetchPatientMedication(patientId)
                    patientDetails = patientDetails.copy(medication = medication)
                    send(patientDetails)
                },
                launch {
                    val surgeryData = healthApi.fetchPatientSurgery(patientId)
                    patientDetails = patientDetails.copy(surgery = surgeryData)
                    send(patientDetails)
                },
            )
        }
    }
}
