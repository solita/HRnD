package fi.solita.hrnd.presentation.designSystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColors(
    primary = LightPrimary,
    primaryVariant = LightPrimaryVariant,
    secondary = LigthSecondary,
    secondaryVariant = LightSecondaryVariant,
    surface = LightSurface,
    background = LightBackground,
    onBackground = LightOnBackground,
    error = Error,
    onSecondary = LightSurface
)
private val DarkColorScheme = darkColors(
    primary = DarkPrimary,
    primaryVariant = DarkPrimaryVariant,
    secondary = DarkSecondary,
    secondaryVariant = DarkSecondaryVariant,
    surface = DarkSurface,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    error = Error,
    onSecondary = DarkBackground
    )

@Composable
fun HrndTheme(
    darkTheme : Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
    ) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        ){
        content()
    }
}