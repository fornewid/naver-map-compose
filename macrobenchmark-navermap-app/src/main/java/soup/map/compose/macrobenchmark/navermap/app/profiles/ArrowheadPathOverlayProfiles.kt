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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ArrowheadPathOverlay
import com.naver.maps.map.compose.ArrowheadPathOverlayDefaults
import com.naver.maps.map.compose.NaverMapConstants

@Composable
fun ArrowheadPathOverlayProfiles() {
    ArrowheadPathOverlay(
        coords = COORDS,
        width = 10.dp,
        headSizeRatio = 2.5f,
        color = Color.Black,
        outlineWidth = 2.dp,
        outlineColor = Color.Black,
        elevation = 0.dp,
        tag = null,
        visible = true,
        minZoom = NaverMapConstants.MinZoom,
        minZoomInclusive = true,
        maxZoom = NaverMapConstants.MaxZoom,
        maxZoomInclusive = true,
        zIndex = 0,
        globalZIndex = ArrowheadPathOverlayDefaults.GlobalZIndex,
        onClick = { false },
    )
}

private val COORDS = listOf(
    LatLng(37.568003, 126.9772503),
    LatLng(37.5701573, 126.9772503),
    LatLng(37.5701573, 126.9751261)
)
