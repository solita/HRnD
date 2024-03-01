package fi.solita.hrnd.designSystem

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.no_data_available
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EmptyContent(modifier: Modifier = Modifier) {
  Box(modifier, contentAlignment = Alignment.Center) {
    Text(stringResource(Res.string.no_data_available))
  }
}

@Preview
@Composable
fun PreviewEmptyContent(){
  EmptyContent()
}