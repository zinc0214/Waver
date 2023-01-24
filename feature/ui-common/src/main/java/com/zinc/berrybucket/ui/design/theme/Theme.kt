package com.zinc.berrybucket.ui.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.zinc.berrybucket.ui_common.R

private val DarkColorPalette = darkColors(
    primary = Main3,
    primaryVariant = Main4,
    secondary = Gray2
)

private val LightColorPalette = lightColors(
    primary = Main1,
    primaryVariant = Main2,
    secondary = Gray1

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

val notoSansKr = FontFamily(
    Font(R.font.notosans_kr_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.notosans_kr_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.notosans_kr_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.notosans_kr_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.notosans_kr_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.notosans_kr_thin, FontWeight.Thin, FontStyle.Normal)
)

@Composable
fun BaseTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography(defaultFontFamily = notoSansKr),
        shapes = Shapes,
        content = content
    )
}