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
 * 하나의 실내지도를 나타내는 불변 클래스.
 *
 * @property zoneId 구역 ID를 반환합니다.
 * @property levelId 층 ID를 반환합니다.
 */
public data class IndoorView(
    public val zoneId: String,
    public val levelId: String,
)
