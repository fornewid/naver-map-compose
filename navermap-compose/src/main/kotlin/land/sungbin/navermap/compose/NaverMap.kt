package land.sungbin.navermap.compose

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitCancellation
import land.sungbin.navermap.compose.internal.MapApplier
import land.sungbin.navermap.compose.internal.MapNode

@Composable
public fun NaverMap(
  modifier: Modifier = Modifier,
  innerContent: (@Composable () -> Unit)? = null,
) {
  val context = LocalContext.current
  val map = remember { MapView(context, NaverMapOptions()) }

  AndroidView(modifier = modifier, factory = { unwrapAppCompat(map) })
  MapLifecycle(map)

  val parentComposition = rememberCompositionContext()
  val currentInnerContent by rememberUpdatedState(innerContent)

  LaunchedEffect(Unit) {
    disposingComposition {
      map.newComposition(parentComposition) {
        currentInnerContent?.invoke()
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
  val map = CompletableDeferred<NaverMap>()
  getMapAsync(map::complete)

  val root = MapNode()

  return Composition(
    applier = MapApplier(map = map.await(), root = root),
    parent = parent,
  ).apply {
    setContent(content)
  }
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

private fun MapView.componentCallbacks(): ComponentCallbacks {
  return object : ComponentCallbacks {
    override fun onConfigurationChanged(config: Configuration) {}
    override fun onLowMemory() {
      this@componentCallbacks.onLowMemory()
    }
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
