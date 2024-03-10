package fi.solita.hrnd.feature.details.model

import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.lifecycle.JavaSerializable

data class ChartData(
    val data: List<Double>,
    val label: String,
    val color: Color
) : JavaSerializable