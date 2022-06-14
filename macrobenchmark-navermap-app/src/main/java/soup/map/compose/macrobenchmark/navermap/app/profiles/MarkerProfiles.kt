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
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerDefaults
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.rememberMarkerState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MarkerProfiles() {
    Marker(
        state = rememberMarkerState(
            key = null,
            position = LatLng(37.57207, 126.97917)
        ),
        icon = MarkerDefaults.Icon,
        iconTintColor = Color.Transparent,
        width = MarkerDefaults.SizeAuto,
        height = MarkerDefaults.SizeAuto,
        anchor = MarkerDefaults.Anchor,
        captionText = null,
        captionTextSize = MarkerDefaults.CaptionTextSize,
        captionColor = Color.Black,
        captionHaloColor = Color.White,
        captionRequestedWidth = 0.dp,
        captionMinZoom = NaverMapConstants.MinZoom,
        captionMaxZoom = NaverMapConstants.MaxZoom,
        subCaptionText = null,
        subCaptionTextSize = MarkerDefaults.CaptionTextSize,
        subCaptionColor = Color.Black,
        subCaptionHaloColor = Color.White,
        subCaptionRequestedWidth = 0.dp,
        subCaptionMinZoom = NaverMapConstants.MinZoom,
        subCaptionMaxZoom = NaverMapConstants.MaxZoom,
        captionAligns = MarkerDefaults.CaptionAligns,
        captionOffset = 0.dp,
        alpha = 1.0f,
        angle = 0.0f,
        isFlat = false,
        isHideCollidedSymbols = false,
        isHideCollidedMarkers = false,
        isHideCollidedCaptions = false,
        isForceShowIcon = false,
        isForceShowCaption = false,
        isIconPerspectiveEnabled = false,
        isCaptionPerspectiveEnabled = false,
        tag = null,
        visible = true,
        minZoom = NaverMapConstants.MinZoom,
        minZoomInclusive = true,
        maxZoom = NaverMapConstants.MaxZoom,
        maxZoomInclusive = true,
        zIndex = 0,
        globalZIndex = MarkerDefaults.GlobalZIndex,
        onClick = { false },
    )
}
