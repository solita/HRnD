package fi.solita.hrnd.core.designSystem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DecorativeBall(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier.fillMaxSize()){
        drawCircle(color)
    }
}