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
 * 지도 카메라가 움직였음을 나타내는 값들.
 * https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/CameraUpdate.html#field.summary
 *
 * [NO_MOVEMENT_YET]는 지도 이동이 관찰되기 전의 초기 상태로 사용됩니다.
 *
 * [UNKNOWN] is used to represent when an unsupported integer value is provided to [fromInt] - this
 * may be a new constant value from the Maps SDK that isn't supported by maps-compose yet, in which
 * case this library should be updated to include a new enum value for that constant.
 *
 * [UNKNOWN]은 지원되지 않는 정수 값을 나타내는 데 사용됩니다.
 * 이는 naver-map-compose에서 아직 지원하지 않는 Maps SDK의 새 상수 값일 수 있습니다.
 * 이 경우 라이브러리가 새 상수를 포함하도록 업데이트되어야 합니다.
 */
@Immutable
public enum class CameraUpdateReason(public val value: Int) {
    UNKNOWN(2),
    NO_MOVEMENT_YET(1),
    DEVELOPER(com.naver.maps.map.CameraUpdate.REASON_DEVELOPER),
    GESTURE(com.naver.maps.map.CameraUpdate.REASON_GESTURE),
    CONTROL(com.naver.maps.map.CameraUpdate.REASON_CONTROL),
    LOCATION(com.naver.maps.map.CameraUpdate.REASON_LOCATION),
    ;

    public companion object {
        public fun fromInt(reason: Int): CameraUpdateReason {
            return values().firstOrNull { it.value == reason } ?: return UNKNOWN
        }
    }
}
