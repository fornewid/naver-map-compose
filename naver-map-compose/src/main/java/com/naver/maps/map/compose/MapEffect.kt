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
