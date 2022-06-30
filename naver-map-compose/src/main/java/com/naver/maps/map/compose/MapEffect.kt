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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentComposer
import com.naver.maps.map.NaverMap
import kotlinx.coroutines.CoroutineScope

/**
 * [NaverMap] 객체를 제공하는 side-effect입니다.
 * 이 effect는 [key1]가 달라지면 다시 시작됩니다.
 *
 * [NaverMap]의 속성은 [com.naver.maps.map.compose.NaverMap] composable 함수에서 관리하므로
 * 이 effect를 주의해서 사용해야 합니다. 그러나 확장성을 고려하면 맵을 직접 참조해야 하는 경우가 있습니다.
 * (예: 클러스터링을 위한 유틸리티 라이브러리 사용)
 */
@Composable
@NaverMapComposable
@ExperimentalNaverMapApi
public fun MapEffect(
    key1: Any?,
    block: suspend CoroutineScope.(NaverMap) -> Unit,
) {
    val map = (currentComposer.applier as MapApplier).map
    LaunchedEffect(key1 = key1) {
        block(map)
    }
}

/**
 * [NaverMap] 객체를 제공하는 side-effect입니다.
 * 이 effect는 [key1] 또는 [key2]가 달라지면 다시 시작됩니다.
 *
 * [NaverMap]의 속성은 [com.naver.maps.map.compose.NaverMap] composable 함수에서 관리하므로
 * 이 effect를 주의해서 사용해야 합니다. 그러나 확장성을 고려하면 맵을 직접 참조해야 하는 경우가 있습니다.
 * (예: 클러스터링을 위한 유틸리티 라이브러리 사용)
 */
@Composable
@NaverMapComposable
@ExperimentalNaverMapApi
public fun MapEffect(
    key1: Any?,
    key2: Any?,
    block: suspend CoroutineScope.(NaverMap) -> Unit,
) {
    val map = (currentComposer.applier as MapApplier).map
    LaunchedEffect(key1 = key1, key2 = key2) {
        block(map)
    }
}

/**
 * [NaverMap] 객체를 제공하는 side-effect입니다.
 * 이 effect는 [key1], [key2] 또는 [key3]가 달라지면 다시 시작됩니다.
 *
 * [NaverMap]의 속성은 [com.naver.maps.map.compose.NaverMap] composable 함수에서 관리하므로
 * 이 effect를 주의해서 사용해야 합니다. 그러나 확장성을 고려하면 맵을 직접 참조해야 하는 경우가 있습니다.
 * (예: 클러스터링을 위한 유틸리티 라이브러리 사용)
 */
@Composable
@NaverMapComposable
@ExperimentalNaverMapApi
public fun MapEffect(
    key1: Any?,
    key2: Any?,
    key3: Any?,
    block: suspend CoroutineScope.(NaverMap) -> Unit,
) {
    val map = (currentComposer.applier as MapApplier).map
    LaunchedEffect(key1 = key1, key2 = key2, key3 = key3) {
        block(map)
    }
}

/**
 * [NaverMap] 객체를 제공하는 side-effect입니다.
 * 이 effect는 [keys] 중 어떤 것이 달라지면 다시 시작됩니다.
 *
 * [NaverMap]의 속성은 [com.naver.maps.map.compose.NaverMap] composable 함수에서 관리하므로
 * 이 effect를 주의해서 사용해야 합니다. 그러나 확장성을 고려하면 맵을 직접 참조해야 하는 경우가 있습니다.
 * (예: 클러스터링을 위한 유틸리티 라이브러리 사용)
 */
@Composable
@NaverMapComposable
@ExperimentalNaverMapApi
public fun MapEffect(
    vararg keys: Any?,
    block: suspend CoroutineScope.(NaverMap) -> Unit
) {
    val map = (currentComposer.applier as MapApplier).map
    LaunchedEffect(keys = keys) {
        block(map)
    }
}
