package com.naver.maps.map.compose.demo

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private object Colors {
    val Brand = Color(0xFF00C42E)
}

private val LightColorPalette = lightColors(
    primary = Colors.Brand,
    primaryVariant = Colors.Brand,
    onPrimary = Color.White,
    secondary = Colors.Brand,
    secondaryVariant = Colors.Brand,
    onSecondary = Color.White
)

@Composable
fun NaverMapTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColorPalette,
        content = content
    )
}
