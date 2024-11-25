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
 *
 * 하나의 실내지도 층을 나타내는 불변 클래스.
 * 하나의 실내지도 층은 다른 층과 연결될 수 있습니다.
 * 이 클래스의 인스턴스는 직접 생성할 수 없고 [IndoorZone]을 이용해서 가져올 수 있습니다.
 *
 * @property name 층 명칭.
 * @property indoorView 층에 해당하는 실내지도 뷰.
 * @property connections 연결된 실내지도 뷰 목록.
 */
public class IndoorLevel(
    public val name: String,
    public val indoorView: IndoorView,
    public val connections: List<IndoorView>,
) {

    /**
     * 연결된 층 중 구역 ID가 zoneId인 실내지도 뷰의 인덱스를 반환합니다.
     *
     * @param zoneId 구역 ID.
     * @return 실내지도 뷰의 인덱스. 연결된 층 중에 ID가 zoneId인 층이 없을 경우 -1.
     */
    public fun getConnectionIndex(zoneId: String): Int {
        return connections.indexOfFirst { connection ->
            connection.zoneId == zoneId
        }
    }

    /**
     * 연결된 층 중 구역 ID가 zoneId인 실내지도 뷰를 반환합니다.
     *
     * @param zoneId 구역 ID.
     * @return 실내지도 뷰. 연결된 층 중에 ID가 zoneId인 층이 없을 경우 null.
     */
    public fun getConnection(zoneId: String): IndoorView? {
        return connections.getOrNull(getConnectionIndex(zoneId))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return indoorView == (other as IndoorLevel).indoorView
    }

    override fun hashCode(): Int {
        return indoorView.hashCode()
    }
}
