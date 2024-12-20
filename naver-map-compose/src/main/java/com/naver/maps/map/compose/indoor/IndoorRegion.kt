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
package com.naver.maps.map.compose.indoor

/**
 * 실내지도가 존재하는 영역을 나타내는 불변 클래스.
 * 하나의 실내지도 영역은 서로 겹쳐진 한 개 이상의 구역으로 이루어집니다.
 * 이 클래스의 인스턴스는 직접 생성할 수 없습니다.
 *
 * @property zones 영역에 속해 있는 구역 목록.
 */
public class IndoorRegion(
    public val zones: List<IndoorZone>,
) {

    /**
     * 영역에 속해 있는 구역 중 ID가 zoneId인 구역의 인덱스를 반환합니다.
     *
     * @param zoneId 구역 ID.
     * @return 구역의 인덱스. 영역 내에 ID가 zoneId인 구역이 없을 경우 -1.
     */
    public fun getZoneIndex(zoneId: String): Int {
        return zones.indexOfFirst { zone ->
            zone.zoneId == zoneId
        }
    }

    /**
     * 영역에 속해 있는 구역 중 ID가 zoneId인 구역을 반환합니다.
     *
     * @param zoneId 구역 ID.
     * @return 구역 객체. 영역 내에 ID가 zoneId인 구역이 없을 경우 null.
     */
    public fun getZone(zoneId: String): IndoorZone? {
        return zones.getOrNull(getZoneIndex(zoneId))
    }
}
