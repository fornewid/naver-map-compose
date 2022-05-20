/*
 * Copyright 2022 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.naver.maps.map.compose

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.NaverMap

internal val DefaultMapProperties = MapProperties()

public object MapPropertiesDefaults {

    public const val MaxZoom: Double = NaverMap.MAXIMUM_ZOOM.toDouble()

    public const val MinZoom: Double = NaverMap.MINIMUM_ZOOM.toDouble()

    public const val MaxTilt: Double = NaverMap.MAXIMUM_TILT.toDouble()

    public const val CameraAnimationDuration: Int =
        NaverMap.DEFAULT_DEFAULT_CAMERA_ANIMATION_DURATION

    public val IndoorFocusRadius: Dp = NaverMap.DEFAULT_INDOOR_FOCUS_RADIUS_DP.dp

    public val BackgroundColorLight: Color = Color(NaverMap.DEFAULT_BACKGROUND_COLOR_LIGHT)

    public val BackgroundColorDark: Color = Color(NaverMap.DEFAULT_BACKGROUND_COLOR_DARK)

    @DrawableRes
    public val BackgroundDrawableLight: Int = NaverMap.DEFAULT_BACKGROUND_DRWABLE_LIGHT

    @DrawableRes
    public val BackgroundDrawableDark: Int = NaverMap.DEFAULT_BACKGROUND_DRWABLE_DARK
}

/**
 * Data class for properties that can be modified on the map.
 *
 * Note: This is intentionally a class and not a data class for binary
 * compatibility on future changes.
 * See: https://jakewharton.com/public-api-challenges-in-kotlin/
 */
public data class MapProperties(
    public val mapType: MapType = MapType.Basic,
    public val extent: LatLngBounds? = null,
    public val minZoom: Double = MapPropertiesDefaults.MinZoom,
    public val maxZoom: Double = MapPropertiesDefaults.MaxZoom,
    public val maxTilt: Double = MapPropertiesDefaults.MaxTilt,
    public val defaultCameraAnimationDuration: Int = MapPropertiesDefaults.CameraAnimationDuration,
    public val fpsLimit: Int = 0,
    public val enabledLayerGroupSet: Set<LayerGroup> = hashSetOf(LayerGroup.Building),
    public val isLiteModeEnabled: Boolean = false,
    public val isNightModeEnabled: Boolean = false,
    public val isIndoorEnabled: Boolean = false,
    public val indoorFocusRadius: Dp = MapPropertiesDefaults.IndoorFocusRadius,
    public val buildingHeight: Float = 1f,
    public val lightness: Float = 0f,
    public val symbolScale: Float = 1f,
    public val symbolPerspectiveRatio: Float = 1f,
    public val backgroundColor: Color = MapPropertiesDefaults.BackgroundColorLight,
    @DrawableRes public val backgroundResource: Int = MapPropertiesDefaults.BackgroundDrawableLight,
    public val locationTrackingMode: LocationTrackingMode = LocationTrackingMode.None,
) {
    init {
        require(fpsLimit >= 0) { "fpsLimit must be greater than 0." }
        require(buildingHeight in 0f..1f) { "buildingHeight must be between 0f and 1f." }
        require(lightness in -1f..1f) { "lightness must be between -1f and 1f." }
        require(symbolScale in 0f..2f) { "symbolScale must be between 0f and 2f." }
        require(symbolPerspectiveRatio in 0f..1f) { "symbolPerspectiveRatio must be between 0f and 1f." }
    }
}
