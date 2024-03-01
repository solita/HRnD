package fi.solita.hrnd.designSystem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DecorativeBall(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier.fillMaxSize()){
        drawCircle(color)
    }
}

@Preview
@Composable
fun PreviewDecorativeBall(){
    DecorativeBall(Modifier,Color.Black)
}