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

import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.map.NaverMap

internal val DefaultMapUiSettings = MapUiSettings()

internal object MapUiSettingsDefaults {
    val PickTolerance: Dp = NaverMap.DEFAULT_PICK_TOLERANCE_DP.dp
    const val ScrollGesturesFriction: Float = 0.08799999952316284f
    const val ZoomGesturesFriction: Float = 0.1237500011920929f
    const val RotateGesturesFriction: Float = 0.19333000481128693f
    const val LogoGravity: Int = Gravity.BOTTOM or Gravity.START
    val LogoMargin: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 16.dp)
}

/**
 * Data class for UI-related settings on the map.
 *
 * Note: This is intentionally a class and not a data class for binary
 * compatibility on future changes.
 * See: https://jakewharton.com/public-api-challenges-in-kotlin/
 */
public data class MapUiSettings(
    public val pickTolerance: Dp = MapUiSettingsDefaults.PickTolerance,
    public val scrollGesturesEnabled: Boolean = true,
    public val zoomGesturesEnabled: Boolean = true,
    public val tiltGesturesEnabled: Boolean = true,
    public val rotateGesturesEnabled: Boolean = true,
    public val stopGesturesEnabled: Boolean = true,
    public val scrollGesturesFriction: Float = MapUiSettingsDefaults.ScrollGesturesFriction,
    public val zoomGesturesFriction: Float = MapUiSettingsDefaults.ZoomGesturesFriction,
    public val rotateGesturesFriction: Float = MapUiSettingsDefaults.RotateGesturesFriction,
    public val compassEnabled: Boolean = true,
    public val scaleBarEnabled: Boolean = true,
    public val zoomControlEnabled: Boolean = true,
    public val indoorLevelPickerEnabled: Boolean = false,
    public val locationButtonEnabled: Boolean = false,
    public val logoClickEnabled: Boolean = true,
    public val logoGravity: Int = MapUiSettingsDefaults.LogoGravity,
    public val logoMargin: PaddingValues = MapUiSettingsDefaults.LogoMargin,
) {
    init {
        require(scrollGesturesFriction in 0f..1f) { "scrollGesturesFriction must be between 0f and 1f." }
        require(zoomGesturesFriction in 0f..1f) { "zoomGesturesFriction must be between 0f and 1f." }
        require(rotateGesturesFriction in 0f..1f) { "rotateGesturesFriction must be between 0f and 1f." }
    }
}
