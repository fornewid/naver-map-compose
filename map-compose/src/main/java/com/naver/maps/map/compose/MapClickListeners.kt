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
package com.naver.maps.map.compose

import android.graphics.PointF
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.Symbol
import com.naver.maps.map.indoor.IndoorSelection

/**
 * Holder class for top-level click listeners.
 */
internal class MapClickListeners {
    var onMapClick: (PointF, LatLng) -> Unit by mutableStateOf({ _, _ -> })
    var onMapLongClick: (PointF, LatLng) -> Unit by mutableStateOf({ _, _ -> })
    var onMapDoubleTab: (point: PointF, coord: LatLng) -> Boolean by mutableStateOf({ _, _ -> false })
    var onMapTwoFingerTap: (point: PointF, coord: LatLng) -> Boolean by mutableStateOf({ _, _ -> false })
    var onMapLoaded: () -> Unit by mutableStateOf({})
    var onLocationChange: (Location) -> Unit by mutableStateOf({})
    var onOptionChange: () -> Unit by mutableStateOf({ })
    var onSymbolClick: (Symbol) -> Boolean by mutableStateOf({ false })
    var onIndoorSelectionChange: (IndoorSelection?) -> Unit by mutableStateOf({})
}
