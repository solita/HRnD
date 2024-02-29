package fi.solita.hrnd.core.data

import fi.solita.hrnd.core.data.model.PatientInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.CancellationException

interface HealthApi {
    suspend fun fetchPatientsData(): List<PatientInfo>
}

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
}
