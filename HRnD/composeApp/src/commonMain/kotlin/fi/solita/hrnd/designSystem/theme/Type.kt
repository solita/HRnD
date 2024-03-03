package fi.solita.hrnd.designSystem.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 26.56.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = (27).sp,
        letterSpacing = 2.sp,
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 23.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = (24.0).sp,
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
    ),
    h4 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 24.sp,
        fontWeight = FontWeight.W700,
    ),
    h5 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 22.sp,
        fontWeight = FontWeight.W500,
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
)