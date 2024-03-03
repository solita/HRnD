package fi.solita.hrnd.core.data

import fi.solita.hrnd.core.data.model.*
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
@UsesMocks(HealthApi::class)
class HealthRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mocker: Mocker
    private lateinit var mockHealthApi: MockHealthApi

    // Class Under Test
    private lateinit var cut: HealthRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mocker = Mocker()
        mockHealthApi = MockHealthApi(mocker)
        cut = HealthRepositoryImpl(mockHealthApi, testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldEmitPatientDetailsWhenReceived() = runTest {
        cut.fetchPatientDetails(patientId)

        //GIVEN
        //LAST
        mocker.everySuspending { mockHealthApi.fetchPatientHeartRate(patientId) } runs {
            delay(100)
            listOf(heartRateTestData)
        }
        // FIRST
        mocker.everySuspending { mockHealthApi.fetchPatientHeartPressure(patientId) } runs {
            delay(20)
            listOf(pressureTestData)
        }
        // SECOND
        mocker.everySuspending { mockHealthApi.fetchPatientMedication(patientId) } runs {
            delay(21)
            listOf(medicationTestData)
        }
        // THIRD
        mocker.everySuspending { mockHealthApi.fetchPatientSurgery(patientId) } runs {
            delay(66)
            listOf(surgeryTestData)
        }

        // WHEN
        val flow = cut.fetchPatientDetails(patientId)

        val patientDetailsList = mutableListOf<PatientDetails>()
        flow.take(4).collect { patientDetails ->
            patientDetailsList.add(patientDetails)
        }

        // THEN
        patientDetailsList[0].pressure shouldBe listOf(pressureTestData)

        patientDetailsList[1].pressure shouldBe listOf(pressureTestData)
        patientDetailsList[1].medication shouldBe listOf(medicationTestData)

        patientDetailsList[2].pressure shouldBe listOf(pressureTestData)
        patientDetailsList[2].medication shouldBe listOf(medicationTestData)
        patientDetailsList[2].surgery shouldBe listOf(surgeryTestData)
    
        patientDetailsList[3].pressure shouldBe listOf(pressureTestData)
        patientDetailsList[3].medication shouldBe listOf(medicationTestData)
        patientDetailsList[3].surgery shouldBe listOf(surgeryTestData)
        patientDetailsList[3].heartRate shouldBe listOf(heartRateTestData)
    }


    companion object {
        val patientId = "123"

        val heartRateTestData = HeartRate(
            patientId = "123",
            heartRate = "80",
            timestamp = "2022-03-01T10:15:30Z"
        )

        val medicationTestData = Medication(
            patientId = "456",
            medicationName = "Aspirin",
            dosage = "1",
            dosageUnit = "pill",
            frequency = "Once daily",
            startDate = "2022-03-01",
            endDate = "2022-03-15"
        )

        val pressureTestData = Pressure(
            patientId = "789",
            systolicPressure = "120",
            diastolicPressure = "80",
            timestamp = "2022-03-01T12:30:45Z"
        )

        val surgeryTestData = Surgery(
            patientId = "101112",
            surgeryName = "Appendectomy",
            surgeryDate = "2022-02-28",
            surgeryOutcome = "Successful",
            surgeryDescription = "Appendix removal surgery"
        )

    }

}