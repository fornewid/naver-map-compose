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
package net.daum.mf.map.api.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import net.daum.mf.map.api.MapPoint

internal class MapClickListeners {
    var onMapInitialized: () -> Unit by mutableStateOf({})
    var onMapCenterPointMoved: (MapPoint) -> Unit by mutableStateOf({})
    var onMapZoomLevelChanged: (Int) -> Unit by mutableStateOf({})
    var onMapSingleTapped: (MapPoint) -> Unit by mutableStateOf({})
    var onMapDoubleTapped: (MapPoint) -> Unit by mutableStateOf({})
    var onMapLongPressed: (MapPoint) -> Unit by mutableStateOf({})
}
