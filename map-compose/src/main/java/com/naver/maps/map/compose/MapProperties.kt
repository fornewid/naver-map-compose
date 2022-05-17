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

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.NaverMap

internal val DefaultMapProperties = MapProperties()

internal object MapPropertiesDefaults {
    const val DefaultMaxZoom: Double = NaverMap.MAXIMUM_ZOOM.toDouble()
    const val DefaultMinZoom: Double = NaverMap.MINIMUM_ZOOM.toDouble()
    const val DefaultMaxTilt: Double = NaverMap.MAXIMUM_TILT.toDouble()
    const val DefaultCameraAnimationDuration: Int =
        NaverMap.DEFAULT_DEFAULT_CAMERA_ANIMATION_DURATION
    val DefaultIndoorFocusRadius: Dp = NaverMap.DEFAULT_INDOOR_FOCUS_RADIUS_DP.dp
    val DefaultLocationTrackingMode: LocationTrackingMode = LocationTrackingMode.None
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
    public val minZoom: Double = MapPropertiesDefaults.DefaultMinZoom,
    public val maxZoom: Double = MapPropertiesDefaults.DefaultMaxZoom,
    public val maxTilt: Double = MapPropertiesDefaults.DefaultMaxTilt,
    public val defaultCameraAnimationDuration: Int = MapPropertiesDefaults.DefaultCameraAnimationDuration,
    public val fpsLimit: Int = 0,
    public val isLiteModeEnabled: Boolean = false,
    public val isNightModeEnabled: Boolean = false,
    public val isIndoorEnabled: Boolean = false,
    public val indoorFocusRadius: Dp = MapPropertiesDefaults.DefaultIndoorFocusRadius,
    public val buildingHeight: Float = 1f,
    public val lightness: Float = 0f,
    public val symbolScale: Float = 1f,
    public val symbolPerspectiveRatio: Float = 1f,
    public val locationTrackingMode: LocationTrackingMode = MapPropertiesDefaults.DefaultLocationTrackingMode,
) {
    init {
        require(fpsLimit >= 0) { "fpsLimit must be greater than 0." }
        require(buildingHeight in 0f..1f) { "buildingHeight must be between 0f and 1f." }
        require(lightness in -1f..1f) { "lightness must be between -1f and 1f." }
        require(symbolScale in 0f..2f) { "symbolScale must be between 0f and 2f." }
        require(symbolPerspectiveRatio in 0f..1f) { "symbolPerspectiveRatio must be between 0f and 1f." }
    }
}
