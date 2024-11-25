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
 * 하나의 실내지도 구역을 나타내는 불변 클래스.
 * 하나의 실내지도 구역에는 한 개 이상의 층이 있습니다.
 * 이 클래스의 인스턴스는 직접 생성할 수 없고 [IndoorRegion]을 이용해서 가져올 수 있습니다.
 *
 * @property zoneId 구역 ID를 반환합니다.
 * @property defaultLevelIndex 대표 층의 인덱스를 반환합니다.
 * @property levels 층 목록을 반환합니다.
 */
public class IndoorZone(
    public val zoneId: String,
    public val defaultLevelIndex: Int,
    public val levels: List<IndoorLevel>,
) {

    /**
     * 대표 층을 반환합니다.
     */
    public val defaultLevel: IndoorLevel
        get() = levels[defaultLevelIndex]

    /**
     * 구역에 속한 층 중 ID가 levelId인 층의 인덱스를 반환합니다.
     *
     * @param levelId 층 ID.
     * @return 층의 인덱스. 구역 내에 ID가 levelId인 층이 없을 경우 -1.
     */
    public fun getLevelIndex(levelId: String): Int {
        return levels.indexOfFirst { level ->
            level.indoorView.levelId == levelId
        }
    }

    /**
     * 구역에 속한 층 중 ID가 levelId인 층을 반환합니다.
     *
     * @param levelId 층 ID.
     * @return 층 객체. 구역 내에 ID가 levelId인 층이 없을 경우 null.
     */
    public fun getLevel(levelId: String): IndoorLevel? {
        return levels.getOrNull(getLevelIndex(levelId))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return zoneId == (other as IndoorZone).zoneId
    }

    override fun hashCode(): Int {
        return zoneId.hashCode()
    }
}
