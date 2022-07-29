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
package net.daum.mf.map.api.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.awaitCancellation
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@ExperimentalKakaoMapApi
@Composable
public fun KakaoMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    properties: MapProperties = DefaultMapProperties,
    uiSettings: MapUiSettings = DefaultMapUiSettings,
    onMapInitialized: () -> Unit = {},
    onMapCenterPointMoved: (MapPoint) -> Unit = {},
    onMapZoomLevelChanged: (Int) -> Unit = {},
    onMapSingleTapped: (MapPoint) -> Unit = {},
    onMapDoubleTapped: (MapPoint) -> Unit = {},
    onMapLongPressed: (MapPoint) -> Unit = {},
    content: (@Composable () -> Unit)? = null,
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    AndroidView(modifier = modifier, factory = { mapView })

    // rememberUpdatedState and friends are used here to make these values observable to
    // the subcomposition without providing a new content function each recomposition
    val mapClickListeners = remember { MapClickListeners() }.also {
        it.onMapInitialized = onMapInitialized
        it.onMapCenterPointMoved = onMapCenterPointMoved
        it.onMapZoomLevelChanged = onMapZoomLevelChanged
        it.onMapSingleTapped = onMapSingleTapped
        it.onMapDoubleTapped = onMapDoubleTapped
        it.onMapLongPressed = onMapLongPressed
    }
    val currentCameraPositionState by rememberUpdatedState(cameraPositionState)
    val currentUiSettings by rememberUpdatedState(uiSettings)
    val currentMapProperties by rememberUpdatedState(properties)

    val parentComposition = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)

    LaunchedEffect(Unit) {
        disposingComposition {
            mapView.newComposition(parentComposition) {
                MapUpdater(
                    cameraPositionState = currentCameraPositionState,
                    clickListeners = mapClickListeners,
                    mapProperties = currentMapProperties,
                    mapUiSettings = currentUiSettings,
                )

                currentContent?.invoke()
            }
        }
    }
}

private suspend inline fun disposingComposition(factory: () -> Composition) {
    val composition = factory()
    try {
        awaitCancellation()
    } finally {
        composition.dispose()
    }
}

private suspend inline fun MapView.newComposition(
    parent: CompositionContext,
    noinline content: @Composable () -> Unit,
): Composition {
    val map: MapView = this
    return Composition(
        MapApplier(map), parent
    ).apply {
        setContent(content)
    }
}
