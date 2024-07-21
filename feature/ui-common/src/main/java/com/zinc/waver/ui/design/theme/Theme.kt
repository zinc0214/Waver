package com.zinc.waver.ui.design.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.zinc.waver.ui_common.R

private val LightColorPalette = Colors(
    primary = Main1,
    primaryVariant = Main3,
    secondary = Sub_D3,
    secondaryVariant = Sub_D2,
    background = Gray1,
    surface = Gray1,
    error = Error2,
    onPrimary = Main4,
    onSecondary = Gray1,
    onBackground = Gray10,
    onSurface = Gray10,
    onError = Gray1,
    isLight = true
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
fun BaseTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography(defaultFontFamily = notoSansKr),
        shapes = Shapes,
        content = content
    )
}