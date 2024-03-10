package fi.solita.hrnd.feature.details.model

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDateTime

data class ChartRead(
    val color: Color,
    val timeStamp: LocalDateTime,
    val value: Double
)