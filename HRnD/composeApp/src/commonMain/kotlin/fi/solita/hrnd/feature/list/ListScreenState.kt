package fi.solita.hrnd.feature.list

import fi.solita.hrnd.core.data.model.PatientInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ListScreenState(
    val patients: ImmutableList<PatientInfo> = persistentListOf(),
    val patientSearchKeyWord: String = ""
)