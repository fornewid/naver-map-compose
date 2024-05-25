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

import android.graphics.PointF
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMap.OnIndoorSelectionChangeListener
import com.naver.maps.map.NaverMap.OnLoadListener
import com.naver.maps.map.NaverMap.OnLocationChangeListener
import com.naver.maps.map.NaverMap.OnMapClickListener
import com.naver.maps.map.NaverMap.OnMapDoubleTapListener
import com.naver.maps.map.NaverMap.OnMapLongClickListener
import com.naver.maps.map.NaverMap.OnMapTwoFingerTapListener
import com.naver.maps.map.NaverMap.OnOptionChangeListener
import com.naver.maps.map.NaverMap.OnSymbolClickListener
import com.naver.maps.map.Symbol
import com.naver.maps.map.indoor.IndoorSelection

internal class MapClickListeners {
    var onMapClick: (PointF, LatLng) -> Unit by mutableStateOf({ _, _ -> })
    var onMapLongClick: (PointF, LatLng) -> Unit by mutableStateOf({ _, _ -> })
    var onMapDoubleTab: (point: PointF, coord: LatLng) -> Boolean by mutableStateOf({ _, _ -> false })
    var onMapTwoFingerTap: (point: PointF, coord: LatLng) -> Boolean by mutableStateOf({ _, _ -> false })
    var onMapLoaded: () -> Unit by mutableStateOf({})
    var onLocationChange: (Location) -> Unit by mutableStateOf({})
    var onOptionChange: () -> Unit by mutableStateOf({ })
    var onSymbolClick: (Symbol) -> Boolean by mutableStateOf({ false })
    var onIndoorSelectionChange: (IndoorSelection?) -> Unit by mutableStateOf({})
}

/**
 * @param L NaverMap click listener type, e.g. [OnMapClickListener]
 */
internal class MapClickListenerNode<L : Any>(
    private val map: NaverMap,
    private val setter: NaverMap.(L?) -> Unit,
    private val listener: L
) : MapNode {
    override fun onAttached() = setListener(listener)
    override fun onRemoved() = setListener(null)
    override fun onCleared() = setListener(null)

    private fun setListener(listenerOrNull: L?) = map.setter(listenerOrNull)
}

/**
 * @param L NaverMap change listener type, e.g. [OnLoadListener]
 */
internal class MapChangeListenerNode<L : Any>(
    private val map: NaverMap,
    private val add: NaverMap.(L) -> Unit,
    private val remove: NaverMap.(L) -> Unit,
    private val listener: L
) : MapNode {
    override fun onAttached() = map.add(listener)
    override fun onRemoved() = Unit
    override fun onCleared() = map.remove(listener)
}

@Suppress("ComplexRedundantLet")
@Composable
internal fun MapClickListenerUpdater() {
    // The mapClickListeners container object is not allowed to ever change
    val mapClickListeners = (currentComposer.applier as MapApplier).mapClickListeners

    with(mapClickListeners) {
        ::onMapClick.let { callback ->
            MapClickListenerComposeNode(
                callback,
                NaverMap::setOnMapClickListener,
                OnMapClickListener { point, coord -> callback().invoke(point, coord) }
            )
        }

        ::onMapLongClick.let { callback ->
            MapClickListenerComposeNode(
                callback,
                NaverMap::setOnMapLongClickListener,
                OnMapLongClickListener { point, coord -> callback().invoke(point, coord) }
            )
        }

        ::onMapDoubleTab.let { callback ->
            MapClickListenerComposeNode(
                callback,
                NaverMap::setOnMapDoubleTapListener,
                OnMapDoubleTapListener { point, coord -> callback().invoke(point, coord) }
            )
        }

        ::onMapTwoFingerTap.let { callback ->
            MapClickListenerComposeNode(
                callback,
                NaverMap::setOnMapTwoFingerTapListener,
                OnMapTwoFingerTapListener { point, coord -> callback().invoke(point, coord) }
            )
        }

        ::onMapLoaded.let { callback ->
            MapChangeListenerComposeNode(
                callback,
                NaverMap::addOnLoadListener,
                NaverMap::removeOnLoadListener,
                OnLoadListener { callback().invoke() }
            )
        }

        ::onLocationChange.let { callback ->
            MapChangeListenerComposeNode(
                callback,
                NaverMap::addOnLocationChangeListener,
                NaverMap::removeOnLocationChangeListener,
                OnLocationChangeListener { callback().invoke(it) }
            )
        }

        ::onOptionChange.let { callback ->
            MapChangeListenerComposeNode(
                callback,
                NaverMap::addOnOptionChangeListener,
                NaverMap::removeOnOptionChangeListener,
                OnOptionChangeListener { callback().invoke() }
            )
        }

        ::onSymbolClick.let { callback ->
            MapClickListenerComposeNode(
                callback,
                NaverMap::setOnSymbolClickListener,
                OnSymbolClickListener { callback().invoke(it) }
            )
        }

        ::onIndoorSelectionChange.let { callback ->
            MapChangeListenerComposeNode(
                callback,
                NaverMap::addOnIndoorSelectionChangeListener,
                NaverMap::removeOnIndoorSelectionChangeListener,
                OnIndoorSelectionChangeListener { callback().invoke(it) }
            )
        }
    }
}

/**
 * Encapsulates the ComposeNode factory lambda as a recomposition optimization.
 *
 * @param L NaverMap click listener type, e.g. [OnMapClickListener]
 * @param callback a property reference to the callback lambda, i.e.
 * invoking it returns the callback lambda
 * @param setter a reference to a NaverMap setter method, e.g. `setOnMapClickListener()`
 * @param listener must include a call to `callback()` inside the listener
 * to use the most up-to-date recomposed version of the callback lambda;
 * However, the resulting callback reference might actually be null due to races;
 * the caller must guard against this case.
 *
 */
@Composable
@NonRestartableComposable
private fun <L : Any> MapClickListenerComposeNode(
    callback: () -> Any?,
    setter: NaverMap.(L?) -> Unit,
    listener: L
) {
    val mapApplier = currentComposer.applier as MapApplier

    MapClickListenerComposeNode(callback) { MapClickListenerNode(mapApplier.map, setter, listener) }
}

@Composable
@NaverMapComposable
private fun MapClickListenerComposeNode(
    callback: () -> Any?,
    factory: () -> MapClickListenerNode<*>
) {
    if (callback() != null) ComposeNode<MapClickListenerNode<*>, MapApplier>(factory) {}
}

@Composable
@NonRestartableComposable
private fun <L : Any> MapChangeListenerComposeNode(
    callback: () -> Any?,
    add: NaverMap.(L) -> Unit,
    remove: NaverMap.(L) -> Unit,
    listener: L
) {
    val mapApplier = currentComposer.applier as MapApplier

    MapChangeListenerComposeNode(callback) {
        MapChangeListenerNode(mapApplier.map, add, remove, listener)
    }
}

@Composable
@NaverMapComposable
private fun MapChangeListenerComposeNode(
    callback: () -> Any?,
    factory: () -> MapChangeListenerNode<*>
) {
    if (callback() != null) ComposeNode<MapChangeListenerNode<*>, MapApplier>(factory) {}
}
