package fi.solita.hrnd.presentation.designSystem

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EmptyContent(modifier: Modifier = Modifier) {
  Box(modifier, contentAlignment = Alignment.Center) {
    Text("MR.base.no_data_available")
  }
}