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
 * 하나의 실내지도 영역 내에서 선택된 구역 및 층을 나타내는 불변 클래스.
 *
 * @property region 영역을 반환합니다.
 * @property zoneIndex 선택된 구역의 인덱스를 반환합니다.
 * @property levelIndex 선택된 층의 인덱스를 반환합니다.
 */
public class IndoorSelection(
    public val region: IndoorRegion,
    public val zoneIndex: Int,
    public val levelIndex: Int,
) {

    /**
     * 선택된 구역을 반환합니다.
     */
    public val zone: IndoorZone
        get() = region.zones[zoneIndex]

    /**
     * 선택된 층을 반환합니다.
     */
    public val level: IndoorLevel
        get() = zone.levels[levelIndex]
}
