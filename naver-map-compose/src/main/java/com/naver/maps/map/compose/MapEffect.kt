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
