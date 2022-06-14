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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapProfiles(
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null
) {
    NaverMap(
        modifier = modifier,
        cameraPositionState = rememberCameraPositionState(),
        properties = MapProperties(),
        uiSettings = MapUiSettings(),
        locationSource = null,
        locale = null,
        onMapClick = { _, _ -> },
        onMapLongClick = { _, _ -> },
        onMapDoubleTab = { _, _ -> false },
        onMapTwoFingerTap = { _, _ -> false },
        onMapLoaded = {},
        onLocationChange = {},
        onOptionChange = {},
        onSymbolClick = { false },
        onIndoorSelectionChange = {},
        contentPadding = PaddingValues(),
        content = content,
    )
}
