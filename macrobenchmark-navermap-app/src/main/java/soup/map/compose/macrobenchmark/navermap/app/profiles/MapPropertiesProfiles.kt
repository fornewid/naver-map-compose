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
package soup.map.compose.macrobenchmark.navermap.app.profiles

import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMapConstants

fun MapPropertiesProfiles() {
    MapProperties(
        mapType = MapType.Basic,
        extent = null,
        minZoom = NaverMapConstants.MinZoom,
        maxZoom = NaverMapConstants.MaxZoom,
        maxTilt = NaverMapConstants.DefaultMaxTilt,
        defaultCameraAnimationDuration = NaverMapConstants.DefaultCameraAnimationDuration,
        fpsLimit = 0,
        isBuildingLayerGroupEnabled = true,
        isTransitLayerGroupEnabled = false,
        isBicycleLayerGroupEnabled = false,
        isTrafficLayerGroupEnabled = false,
        isCadastralLayerGroupEnabled = false,
        isMountainLayerGroupEnabled = false,
        isLiteModeEnabled = false,
        isNightModeEnabled = false,
        isIndoorEnabled = false,
        indoorFocusRadius = NaverMapConstants.DefaultIndoorFocusRadius,
        buildingHeight = 1f,
        lightness = 0f,
        symbolScale = 1f,
        symbolPerspectiveRatio = 1f,
        backgroundColor = NaverMapConstants.DefaultBackgroundColorLight,
        backgroundResource = NaverMapConstants.DefaultBackgroundDrawableLight,
        locationTrackingMode = LocationTrackingMode.None,
    )

    MapType.Basic
    MapType.Navi
    MapType.Satellite
    MapType.Hybrid
    MapType.Terrain
    MapType.None

    LocationTrackingMode.None
    LocationTrackingMode.NoFollow
    LocationTrackingMode.Follow
    LocationTrackingMode.Face
}
