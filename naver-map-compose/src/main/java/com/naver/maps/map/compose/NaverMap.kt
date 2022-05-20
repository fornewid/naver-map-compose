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

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationSource
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.Symbol
import com.naver.maps.map.indoor.IndoorSelection
import kotlinx.coroutines.awaitCancellation
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

public object NaverMapDefaults {
    public val DefaultCameraPosition: CameraPosition = NaverMap.DEFAULT_CAMERA_POSITION

    public const val DefaultMinZoom: Double = NaverMap.MINIMUM_ZOOM.toDouble()
    public const val DefaultMaxZoom: Double = NaverMap.MAXIMUM_ZOOM.toDouble()
}

@ExperimentalNaverMapApi
@Composable
public fun NaverMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    properties: MapProperties = DefaultMapProperties,
    locationSource: LocationSource? = null,
    locale: Locale? = null,
    uiSettings: MapUiSettings = DefaultMapUiSettings,
    onMapClick: (PointF, LatLng) -> Unit = { _, _ -> },
    onMapLongClick: (PointF, LatLng) -> Unit = { _, _ -> },
    onMapDoubleTab: (point: PointF, coord: LatLng) -> Boolean = { _, _ -> false },
    onMapTwoFingerTap: (point: PointF, coord: LatLng) -> Boolean = { _, _ -> false },
    onMapLoaded: () -> Unit = {},
    onLocationChange: (Location) -> Unit = {},
    onOptionChange: () -> Unit = {},
    onSymbolClick: (Symbol) -> Boolean = { false },
    onIndoorSelectionChange: (IndoorSelection?) -> Unit = {},
    contentPadding: PaddingValues = NoPadding,
    content: (@Composable () -> Unit)? = null,
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context, NaverMapOptions()) }

    AndroidView(modifier = modifier, factory = { unwrapAppCompat(mapView) })
    MapLifecycle(mapView)

    // rememberUpdatedState and friends are used here to make these values observable to
    // the subcomposition without providing a new content function each recomposition
    val mapClickListeners = remember { MapClickListeners() }.also {
        it.onMapClick = onMapClick
        it.onMapLongClick = onMapLongClick
        it.onMapDoubleTab = onMapDoubleTab
        it.onMapTwoFingerTap = onMapTwoFingerTap
        it.onMapLoaded = onMapLoaded
        it.onLocationChange = onLocationChange
        it.onOptionChange = onOptionChange
        it.onSymbolClick = onSymbolClick
        it.onIndoorSelectionChange = onIndoorSelectionChange
    }
    val currentLocationSource by rememberUpdatedState(locationSource)
    val currentLocale by rememberUpdatedState(locale)
    val currentCameraPositionState by rememberUpdatedState(cameraPositionState)
    val currentContentPadding by rememberUpdatedState(contentPadding)
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
                    contentPadding = currentContentPadding,
                    locationSource = currentLocationSource,
                    locale = currentLocale,
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
    val map = awaitMap()
    return Composition(
        MapApplier(map), parent
    ).apply {
        setContent(content)
    }
}

/**
 * A suspending function that provides an instance of [NaverMap] from this [MapView]. This is
 * an alternative to [MapView.getMapAsync] by using coroutines to obtain the [NaverMap].
 *
 * @return the [NaverMap] instance
 */
private suspend inline fun MapView.awaitMap(): NaverMap {
    return suspendCoroutine { continuation ->
        getMapAsync {
            continuation.resume(it)
        }
    }
}

/**
 * Registers lifecycle observers to the local [MapView].
 */
@Composable
private fun MapLifecycle(mapView: MapView) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val savedInstanceState = rememberSavedInstanceState()
    DisposableEffect(context, lifecycle, mapView, savedInstanceState) {
        val mapLifecycleObserver = mapView.lifecycleObserver(
            savedInstanceState.takeUnless { it.isEmpty }
        )
        val callbacks = mapView.componentCallbacks()

        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)

        onDispose {
            mapView.onSaveInstanceState(savedInstanceState)
            lifecycle.removeObserver(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)
        }
    }
}

@Composable
private fun rememberSavedInstanceState(): Bundle {
    return rememberSaveable { Bundle() }
}

private fun MapView.lifecycleObserver(
    savedInstanceState: Bundle?,
): LifecycleEventObserver {
    return LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> this.onCreate(savedInstanceState)
            Lifecycle.Event.ON_START -> this.onStart()
            Lifecycle.Event.ON_RESUME -> this.onResume()
            Lifecycle.Event.ON_PAUSE -> this.onPause()
            Lifecycle.Event.ON_STOP -> this.onStop()
            Lifecycle.Event.ON_DESTROY -> this.onDestroy()
            else -> throw IllegalStateException()
        }
    }
}

private fun MapView.componentCallbacks(): ComponentCallbacks {
    return object : ComponentCallbacks {
        override fun onConfigurationChanged(config: Configuration) {}

        override fun onLowMemory() {
            this@componentCallbacks.onLowMemory()
        }
    }
}

/**
 * 네이버지도 SDK 내부에는 AppCompat의 [srcCompat] 속성으로 이미지를 설정하는 부분이 있다.
 * 이 부분이 있는 Widget은 AppCompat Widget으로 생성되어야 해서 반드시 AppCompat Theme를 사용해야 한다.
 *
 * Jetpack Compose는 minSdk가 21이므로 AppCompat 없이도 이미지를 참조하는데 문제가 없다.
 * AppCompat Theme를 사용하지 않더라도 아이콘이 적절히 보여질 수 있도록
 * 네이버지도 SDK 내부에서 생성되는 Widget 중 [srcCompat] 속성으로 이미지가 설정되는 부분을 직접 설정한다.
 */
private fun unwrapAppCompat(mapView: MapView): MapView = mapView.apply {
    val compassIcon: ImageView = findViewById(com.naver.maps.map.R.id.navermap_compass_icon)
    compassIcon.setImageResource(com.naver.maps.map.R.drawable.navermap_compass)

    val locationIcon: ImageView = findViewById(com.naver.maps.map.R.id.navermap_location_icon)
    locationIcon.setImageResource(com.naver.maps.map.R.drawable.navermap_location_none_normal)

    val zoomInIcon: ImageView = findViewById(com.naver.maps.map.R.id.navermap_zoom_in)
    zoomInIcon.setImageResource(com.naver.maps.map.R.drawable.navermap_zoom_in)

    val zoomOutIcon: ImageView = findViewById(com.naver.maps.map.R.id.navermap_zoom_out)
    zoomOutIcon.setImageResource(com.naver.maps.map.R.drawable.navermap_zoom_out)
}
