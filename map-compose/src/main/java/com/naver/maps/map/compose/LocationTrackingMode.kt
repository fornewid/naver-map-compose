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

import androidx.compose.runtime.Immutable

/**
 * Enumerates the different types of location tracking modes.
 */
@Immutable
public enum class LocationTrackingMode(public val value: com.naver.maps.map.LocationTrackingMode) {
    NONE(com.naver.maps.map.LocationTrackingMode.None),
    NO_FOLLOW(com.naver.maps.map.LocationTrackingMode.NoFollow),
    FOLLOW(com.naver.maps.map.LocationTrackingMode.Follow),
    FACE(com.naver.maps.map.LocationTrackingMode.Face)
}
