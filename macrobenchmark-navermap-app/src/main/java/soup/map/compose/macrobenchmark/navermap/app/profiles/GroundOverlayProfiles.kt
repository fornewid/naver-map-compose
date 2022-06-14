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

import androidx.compose.runtime.Composable
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.compose.GroundOverlay
import com.naver.maps.map.compose.GroundOverlayDefaults
import com.naver.maps.map.compose.NaverMapConstants

@Composable
fun GroundOverlaySample() {
    GroundOverlay(
        bounds = BOUNDS,
        image = GroundOverlayDefaults.Image,
        alpha = 1f,
        tag = null,
        visible = true,
        minZoom = NaverMapConstants.MinZoom,
        minZoomInclusive = true,
        maxZoom = NaverMapConstants.MaxZoom,
        maxZoomInclusive = true,
        zIndex = 0,
        globalZIndex = GroundOverlayDefaults.GlobalZIndex,
        onClick = { false },
    )
}

private val BOUNDS = LatLngBounds(
    LatLng(37.57023, 126.97672),
    LatLng(37.57545, 126.98323)
)
