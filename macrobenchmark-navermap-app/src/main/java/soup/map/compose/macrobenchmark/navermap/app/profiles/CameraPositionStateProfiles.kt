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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.util.MapConstants
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun CameraPositionStateProfiles() {
    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.isMoving
    cameraPositionState.projection
    cameraPositionState.position
    cameraPositionState.contentBounds
    cameraPositionState.contentRegion
    cameraPositionState.coveringBounds
    cameraPositionState.coveringRegion
    cameraPositionState.coveringTileIds
    cameraPositionState.getCoveringTileIdsAtZoom(NaverMapConstants.MinZoom.toInt())
    cameraPositionState.locationTrackingMode
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            cameraPositionState.animate(CameraUpdate.fitBounds(MapConstants.EXTENT_KOREA))
        }
    }
    cameraPositionState.move(CameraUpdate.fitBounds(MapConstants.EXTENT_KOREA))
    cameraPositionState.stop()
}
