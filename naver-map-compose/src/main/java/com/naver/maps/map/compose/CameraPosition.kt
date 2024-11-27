/*
 * Copyright 2024 SOUP
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

/**
 * 카메라의 위치 관련 정보를 나타내는 불변 클래스.
 * 카메라의 위치는 좌표, 줌 레벨, 기울기 각도, 베어링 각도로 구성됩니다.
 *
 * @property target 카메라의 좌표.
 * @property zoom 줌 레벨. 이 값이 증가할수록 축척이 증가합니다.
 * @property tilt 기울기 각도. 도 단위. 카메라가 지면을 내려다보는 각도를 나타냅니다. 천정에서 지면을 수직으로 내려다보는 경우 0도이며, 비스듬해질수록 값이 증가합니다.
 * @property bearing 베어링 각도. 도 단위. 카메라가 바라보는 방위를 나타냅니다. 방위가 북쪽일 경우 0도이며, 시계 방향으로 값이 증가합니다.
 */
public data class CameraPosition(
    public val target: LatLng,
    public val zoom: Double,
    public val tilt: Double,
    public val bearing: Double,
) {
    /**
     * 좌표와 줌 레벨로부터 객체를 생성합니다.
     *
     * @property target 카메라의 좌표.
     * @property zoom 줌 레벨.
     */
    public constructor(target: LatLng, zoom: Double) : this(
        target = target,
        zoom = zoom,
        tilt = 0.0,
        bearing = 0.0,
    )

    init {
        require(zoom in 0.0..21.0) { "zoom range should be between 0.0 and 21.0." }
        require(tilt in 0.0..63.0) { "tilt range should be between 0.0 and 63.0." }
        require(bearing in 0.0..360.0) { "bearing range should be between 0.0 and 360.0." }
    }

    // TODO: remove this
    public fun asOriginal(): com.naver.maps.map.CameraPosition {
        return com.naver.maps.map.CameraPosition(
            target.asOriginal(),
            zoom,
            tilt,
            bearing,
        )
    }

    public companion object {
        /**
         * 유효하지 않은 카메라 위치를 나타내는 상수.
         */
        public val INVALID: CameraPosition = CameraPosition(
            target = LatLng.INVALID,
            zoom = Double.NaN,
            tilt = Double.NaN,
            bearing = Double.NaN,
        )

        // TODO: remove this
        public fun fromOriginal(original: com.naver.maps.map.CameraPosition): CameraPosition {
            return CameraPosition(
                target = LatLng.fromOriginal(original.target),
                zoom = original.zoom,
                tilt = original.tilt,
                bearing = original.bearing,
            )
        }
    }
}
