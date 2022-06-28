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
import com.naver.maps.map.compose.NaverMapComposable
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.PolygonOverlay
import com.naver.maps.map.compose.PolygonOverlayDefaults

@Composable
@NaverMapComposable
fun PolygonOverlaySample() {
    PolygonOverlay(
        coords = COORDS,
        holes = HOLES,
        color = Color.Black,
        outlineWidth = 3.dp,
        outlineColor = Color.Black.copy(alpha = 0.5f),
        tag = null,
        visible = true,
        minZoom = NaverMapConstants.MinZoom,
        minZoomInclusive = true,
        maxZoom = NaverMapConstants.MaxZoom,
        maxZoomInclusive = true,
        zIndex = 0,
        globalZIndex = PolygonOverlayDefaults.GlobalZIndex,
        onClick = { false },
    )
}

private val COORDS = listOf(
    LatLng(37.5640984, 126.9712268),
    LatLng(37.5651279, 126.9767904),
    LatLng(37.5625365, 126.9832241),
    LatLng(37.5585305, 126.9809297),
    LatLng(37.5590777, 126.974617),
)

private val HOLES = listOf(
    listOf(
        LatLng(37.5612243, 126.9768938),
        LatLng(37.5627692, 126.9795502),
        LatLng(37.5628377, 126.976066),
    ),
)
