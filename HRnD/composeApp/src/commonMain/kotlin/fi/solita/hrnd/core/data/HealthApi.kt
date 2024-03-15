package fi.solita.hrnd.core.data

import fi.solita.hrnd.core.data.model.HeartRate
import fi.solita.hrnd.core.data.model.MedicalHistory
import fi.solita.hrnd.core.data.model.Medication
import fi.solita.hrnd.core.data.model.PatientInfo
import fi.solita.hrnd.core.data.model.Pressure
import fi.solita.hrnd.core.data.model.Surgery
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.utils.io.CancellationException

interface HealthApi {
    suspend fun fetchPatientsData(): List<PatientInfo>
    suspend fun fetchPatientInfo(patientId: String): PatientInfo?

    suspend fun fetchPatientMedication(patientId: String): List<Medication>
    suspend fun fetchPatientHeartRate(patientId: String): List<HeartRate>
    suspend fun fetchPatientHeartPressure(patientId: String): List<Pressure>
    suspend fun fetchPatientSurgery(patientId: String): List<Surgery>
    suspend fun fetchPatientMedicalHistory(patientId: String): List<MedicalHistory>
}

//TODO IMPLEMENT METHODS
class KtorHealthApi(private val client: HttpClient) : HealthApi {
    companion object {
        private const val API_URL =
            "http://localhost:8090"
    }

    override suspend fun fetchPatientsData(): List<PatientInfo> {
        return try {
            client.get("$API_URL/get_patients").body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun fetchPatientInfo(patientId: String): PatientInfo? {
        return try {
            client.get("$API_URL/get_patient"){
                withPatientId(patientId)
            }.body<PatientInfo?>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            null
        }
    }

    override suspend fun fetchPatientMedication(patientId: String): List<Medication> {
        return try {
            client.get("$API_URL/get_patient_medication"){
                withPatientId(patientId)
            }.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun fetchPatientHeartRate(patientId: String): List<HeartRate> {
        return try {
            client.get("$API_URL/get_patient_heartrate"){
                withPatientId(patientId)
            }.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun fetchPatientHeartPressure(patientId: String): List<Pressure> {
        return try {
            client.get("$API_URL/get_patient_pressure"){
                withPatientId(patientId)
            }.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun fetchPatientSurgery(patientId: String): List<Surgery> {
        return try {
            client.get("$API_URL/get_patient_surgeries"){
                withPatientId(patientId)
            }.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun fetchPatientMedicalHistory(patientId: String): List<MedicalHistory> {
        return try {
            client.get("$API_URL/get_patient_medical_history"){
                withPatientId(patientId)
            }.body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            listOf()
        }
    }

    private fun HttpRequestBuilder.withPatientId(patientId: String) {
        parameter("patient_id", patientId)
    }

}
