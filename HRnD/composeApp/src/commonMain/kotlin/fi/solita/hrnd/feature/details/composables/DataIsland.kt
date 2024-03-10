package fi.solita.hrnd.feature.details.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fi.solita.hrnd.designSystem.DecorativeBall
import fi.solita.hrnd.feature.details.model.ChartRead
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DataIsland(chartReads: ImmutableList<ChartRead>, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.surface,
        elevation = 8.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(6.dp))
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center,
                text = chartReads.first().timeStamp.time.toString(),
                style = MaterialTheme.typography.body2.copy(
                    color = MaterialTheme.colors.secondaryVariant,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
                chartReads.forEachIndexed { i, chartRead ->
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DecorativeBall(Modifier.size(8.dp), chartRead.color)
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = chartRead.value.toString(),
                            style = MaterialTheme.typography.body1
                        )
                        if (i < chartReads.size - 1) {
                            Spacer(Modifier.width(8.dp))
                        }
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}