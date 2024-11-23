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
import com.naver.maps.map.CameraUpdate as NaverCameraUpdate

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
@Suppress("ClassName")
@Immutable
public sealed interface CameraUpdateReason {
    public data object UNKNOWN : CameraUpdateReason
    public data object NO_MOVEMENT_YET : CameraUpdateReason
    public data object DEVELOPER : CameraUpdateReason
    public data object GESTURE : CameraUpdateReason
    public data object CONTROL : CameraUpdateReason
    public data object LOCATION : CameraUpdateReason
    public data object CONTENT_PADDING : CameraUpdateReason

    public companion object {
        public fun fromInt(reason: Int): CameraUpdateReason {
            return when (reason) {
                NaverCameraUpdate.REASON_DEVELOPER -> DEVELOPER
                NaverCameraUpdate.REASON_GESTURE -> GESTURE
                NaverCameraUpdate.REASON_CONTROL -> CONTROL
                NaverCameraUpdate.REASON_LOCATION -> LOCATION
                NaverCameraUpdate.REASON_CONTENT_PADDING -> CONTENT_PADDING
                else -> UNKNOWN
            }
        }
    }
}
