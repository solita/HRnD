package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MiniChart(modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier.aspectRatio(1f),
        color = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium
    ) {

    }
}