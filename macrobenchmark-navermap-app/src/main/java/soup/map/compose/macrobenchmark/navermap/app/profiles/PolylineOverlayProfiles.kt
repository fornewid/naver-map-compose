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
import com.naver.maps.map.compose.LineCap
import com.naver.maps.map.compose.LineJoin
import com.naver.maps.map.compose.NaverMapComposable
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.PolylineOverlay
import com.naver.maps.map.compose.PolylineOverlayDefaults

@Composable
@NaverMapComposable
fun PolylineOverlaySample() {
    PolylineOverlay(
        coords = COORDS,
        width = 5.dp,
        color = Color.Black,
        pattern = arrayOf(10.dp, 10.dp),
        capType = LineCap.Round,
        joinType = LineJoin.Miter,
        tag = null,
        visible = true,
        minZoom = NaverMapConstants.MinZoom,
        minZoomInclusive = true,
        maxZoom = NaverMapConstants.MaxZoom,
        maxZoomInclusive = true,
        zIndex = 0,
        globalZIndex = PolylineOverlayDefaults.GlobalZIndex,
        onClick = { false },
    )
}

private val COORDS = listOf(
    LatLng(37.57152, 126.97714),
    LatLng(37.56607, 126.98268),
    LatLng(37.56445, 126.97707),
    LatLng(37.55855, 126.97822),
)
