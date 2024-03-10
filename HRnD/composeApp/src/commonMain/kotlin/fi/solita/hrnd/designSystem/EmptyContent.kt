package fi.solita.hrnd.designSystem

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.no_data_available
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NoDataAvailable(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(stringResource(Res.string.no_data_available))
    }
}

@Composable
fun EmptyContent(modifier: Modifier = Modifier) {

    val rememberShimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = defaultShimmerTheme.copy(animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = LinearEasing,
                delayMillis = 1_500,
            ),
            initialStartOffset = StartOffset(1300, StartOffsetType.FastForward) ,
            repeatMode = RepeatMode.Restart,
        ),),

    )
    Box(
        modifier = modifier
            .shimmer(customShimmer = rememberShimmer)
            .background(MaterialTheme.colors.background),
    )
}

@Preview
@Composable
fun PreviewEmptyContent() {
    EmptyContent(Modifier.size(200.dp))
}