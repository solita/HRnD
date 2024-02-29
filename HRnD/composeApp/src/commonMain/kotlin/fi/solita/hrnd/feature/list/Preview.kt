package fi.solita.hrnd.feature.list

import fi.solita.hrnd.core.data.model.PatientInfo
import kotlinx.collections.immutable.persistentListOf

/**
 * Contains preview tools
 * */


val listScreenMockState = ListScreenState(
    patients = persistentListOf(
        PatientInfo("1","Michal","Guspiel","9/18/1997", currentRoom = null),
        PatientInfo("1","Johh","Doe","1/29/1979", currentRoom = "12"),
    ),
    patientSearchKeyWord = ""
)