package fi.solita.hrnd.domain

import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.lifecycle.JavaSerializable

enum class ChartDataType{
    HEART_RATE,
    SYSTOLIC_PRESSURE,
    DIASTOLIC_PRESSURE
}

data class ChartDataEntry(
    val timeStampEpochSeconds: Long,
    val value : Double,
): JavaSerializable

/**
 * Represents data visualized in chart.
 * */
data class ChartData(
    val type: ChartDataType,
    val entries: List<ChartDataEntry>?,
    val color: Color? = null,
) : JavaSerializable