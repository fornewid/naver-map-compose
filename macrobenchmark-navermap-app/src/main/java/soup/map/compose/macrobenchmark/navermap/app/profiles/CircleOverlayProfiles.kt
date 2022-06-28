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
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.CircleOverlayDefaults
import com.naver.maps.map.compose.NaverMapComposable
import com.naver.maps.map.compose.NaverMapConstants

@Composable
@NaverMapComposable
fun CircleOverlayProfiles() {
    CircleOverlay(
        center = LatLng(37.5666102, 126.9783881),
        color = Color.Transparent,
        radius = 1000.0,
        outlineWidth = 0.dp,
        outlineColor = Color.Black,
        tag = null,
        visible = true,
        minZoom = NaverMapConstants.MinZoom,
        minZoomInclusive = true,
        maxZoom = NaverMapConstants.MaxZoom,
        maxZoomInclusive = true,
        zIndex = 0,
        globalZIndex = CircleOverlayDefaults.GlobalZIndex,
        onClick = { false },
    )
}
