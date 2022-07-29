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

import androidx.compose.runtime.Immutable

internal val DefaultMapProperties = MapProperties()

public data class MapProperties(
    public val mapType: MapType = MapType.Standard,
    public val mapTileMode: MapTileMode = MapTileMode.Standard,
)

/**
 * 지도의 유형을 나타내는 열거형.
 */
@Immutable
public enum class MapType(public val value: net.daum.mf.map.api.MapView.MapType) {

    /**
     * 일반 지도.
     */
    Standard(net.daum.mf.map.api.MapView.MapType.Standard),

    /**
     * 위성 지도.
     */
    Satellite(net.daum.mf.map.api.MapView.MapType.Satellite),

    /**
     * 위성 지도(겹쳐보기).
     */
    Hybrid(net.daum.mf.map.api.MapView.MapType.Hybrid),
}

/**
 * 지도의 유형을 나타내는 열거형.
 */
@Immutable
public enum class MapTileMode(public val value: net.daum.mf.map.api.MapView.MapTileMode) {

    /**
     * 일반 지도.
     */
    Standard(net.daum.mf.map.api.MapView.MapTileMode.Standard),

    /**
     * 위성 지도.
     */
    HD(net.daum.mf.map.api.MapView.MapTileMode.HD),

    /**
     * 위성 지도(겹쳐보기).
     */
    HD2X(net.daum.mf.map.api.MapView.MapTileMode.HD2X),
}
