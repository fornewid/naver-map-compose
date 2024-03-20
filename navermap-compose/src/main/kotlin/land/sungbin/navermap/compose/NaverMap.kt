/*
 * Copyright 2024 SOUP, Ji Sungbin
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

package land.sungbin.navermap.compose

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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.naver.maps.map.LocationSource
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.Symbol
import com.naver.maps.map.indoor.IndoorSelection
import java.util.Locale
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitCancellation
import land.sungbin.navermap.compose.options.CameraPositionState
import land.sungbin.navermap.compose.options.DefaultMapProperties
import land.sungbin.navermap.compose.options.DefaultMapUiSettings
import land.sungbin.navermap.compose.options.LocalCameraPositionState
import land.sungbin.navermap.compose.options.MapClickListeners
import land.sungbin.navermap.compose.options.MapProperties
import land.sungbin.navermap.compose.options.MapUiSettings
import land.sungbin.navermap.compose.options.rememberCameraPositionState
import land.sungbin.navermap.compose.runtime.MapApplier
import land.sungbin.navermap.compose.runtime.MapUiNode
import land.sungbin.navermap.compose.updater.MapOptionUpdater
import land.sungbin.navermap.compose.updater.NoPadding

private val NoContent: @Composable @NaverMapComposable () -> Unit = {}

@Composable
public fun NaverMap(
  modifier: Modifier = Modifier,
  cameraPositionState: CameraPositionState = rememberCameraPositionState(),
  properties: MapProperties = DefaultMapProperties,
  uiSettings: MapUiSettings = DefaultMapUiSettings,
  locationSource: LocationSource? = null,
  locale: Locale? = null,
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
  content: @Composable @NaverMapComposable () -> Unit = NoContent,
) {
  val context = LocalContext.current
  val map = remember { MapView(context, NaverMapOptions()) }

  AndroidView(modifier = modifier, factory = { unwrapAppCompat(map) })
  MapLifecycle(map)

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
  val currentContent by rememberUpdatedState(content)

  val parentComposition = rememberCompositionContext()
  val compositionLocalContext = currentCompositionLocalContext

  LaunchedEffect(map) {
    disposingComposition {
      map.newComposition(parentComposition) {
        CompositionLocalProvider(context = compositionLocalContext) {
          CompositionLocalProvider(LocalCameraPositionState provides cameraPositionState) {
            MapOptionUpdater(
              cameraPositionState = currentCameraPositionState,
              clickListeners = mapClickListeners,
              contentPadding = currentContentPadding,
              locationSource = currentLocationSource,
              locale = currentLocale,
              mapProperties = currentMapProperties,
              mapUiSettings = currentUiSettings,
            )
            currentContent()
          }
        }
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
  val deferredMap = CompletableDeferred<NaverMap>()
  getMapAsync(deferredMap::complete)

  val root = MapUiNode(map = deferredMap.await())
  val composition = Composition(applier = MapApplier(root = root), parent = parent)

  return composition.apply { setContent(content) }
}

@Composable
private fun MapLifecycle(map: MapView) {
  val context = LocalContext.current
  val lifecycle = LocalLifecycleOwner.current.lifecycle
  val previousState = remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
  val savedInstanceState = rememberSaveable { Bundle() }

  DisposableEffect(context, lifecycle, map, savedInstanceState) {
    val mapLifecycleObserver = map.lifecycleObserver(
      savedInstanceState = savedInstanceState.takeUnless(Bundle::isEmpty),
      previousState = previousState,
    )
    val callbacks = map.componentCallbacks()

    lifecycle.addObserver(mapLifecycleObserver)
    context.registerComponentCallbacks(callbacks)

    onDispose {
      map.onSaveInstanceState(savedInstanceState)
      lifecycle.removeObserver(mapLifecycleObserver)
      context.unregisterComponentCallbacks(callbacks)

      when (previousState.value) {
        Lifecycle.Event.ON_CREATE, Lifecycle.Event.ON_STOP -> {
          map.onDestroy()
        }
        Lifecycle.Event.ON_START, Lifecycle.Event.ON_PAUSE -> {
          map.onStop()
          map.onDestroy()
        }
        Lifecycle.Event.ON_RESUME -> {
          map.onPause()
          map.onStop()
          map.onDestroy()
        }
        else -> {}
      }
    }
  }
}

private fun MapView.lifecycleObserver(
  savedInstanceState: Bundle?,
  previousState: MutableState<Lifecycle.Event>,
) = LifecycleEventObserver { _, event ->
  when (event) {
    Lifecycle.Event.ON_CREATE -> onCreate(savedInstanceState)
    Lifecycle.Event.ON_START -> onStart()
    Lifecycle.Event.ON_RESUME -> onResume()
    Lifecycle.Event.ON_PAUSE -> onPause()
    Lifecycle.Event.ON_STOP -> onStop()
    Lifecycle.Event.ON_DESTROY -> onDestroy()
    else -> error("Unhandled lifecycle event: $event")
  }
  previousState.value = event
}

private fun MapView.componentCallbacks() = object : ComponentCallbacks {
  override fun onConfigurationChanged(config: Configuration) {}
  override fun onLowMemory() {
    this@componentCallbacks.onLowMemory()
  }
}

private fun unwrapAppCompat(map: MapView) = map.apply {
  val compassIcon: ImageView = findViewById(com.naver.maps.map.R.id.navermap_compass_icon)
  compassIcon.setImageResource(com.naver.maps.map.R.drawable.navermap_compass)

  val locationIcon: ImageView = findViewById(com.naver.maps.map.R.id.navermap_location_icon)
  locationIcon.setImageResource(com.naver.maps.map.R.drawable.navermap_location_none_normal)

  val zoomInIcon: ImageView = findViewById(com.naver.maps.map.R.id.navermap_zoom_in)
  zoomInIcon.setImageResource(com.naver.maps.map.R.drawable.navermap_zoom_in)

  val zoomOutIcon: ImageView = findViewById(com.naver.maps.map.R.id.navermap_zoom_out)
  zoomOutIcon.setImageResource(com.naver.maps.map.R.drawable.navermap_zoom_out)
}
