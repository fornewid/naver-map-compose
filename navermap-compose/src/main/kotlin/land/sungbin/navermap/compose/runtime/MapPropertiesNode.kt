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

package land.sungbin.navermap.compose.runtime

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.naver.maps.map.NaverMap
import land.sungbin.navermap.compose.lifecycle.NaverMapContentLifecycleCallback
import land.sungbin.navermap.compose.options.CameraPositionState
import land.sungbin.navermap.compose.options.CameraUpdateReason
import land.sungbin.navermap.compose.options.MapClickListeners

internal class MapPropertiesNode(
  override var map: NaverMap?,
  cameraPositionState: CameraPositionState,
  var clickListeners: MapClickListeners,
  var density: Density,
  var layoutDirection: LayoutDirection,
) : MapUiNode(map) {
  val safeMap get() = requireMap()

  init {
    cameraPositionState.setMap(safeMap)
  }

  var cameraPositionState: CameraPositionState = cameraPositionState
    set(value) {
      if (value == field) return
      field.setMap(null)
      field = value
      value.setMap(map)
    }

  private val cameraIdleListener = NaverMap.OnCameraIdleListener {
    this.cameraPositionState.isMoving = false
    // setOnCameraMoveListener is only invoked when the camera position
    // is changed via .animate(). To handle updating state when .move()
    // is used, it's necessary to set the camera's position here as well
    this.cameraPositionState.rawPosition = safeMap.cameraPosition
  }
  private val cameraChangeListener = NaverMap.OnCameraChangeListener { reason, _ ->
    cameraPositionState.cameraUpdateReason = CameraUpdateReason.fromInt(reason)
    cameraPositionState.isMoving = true
    cameraPositionState.rawPosition = safeMap.cameraPosition
  }
  private val loadListener = NaverMap.OnLoadListener {
    clickListeners.onMapLoaded()
  }
  private val locationChangeListener = NaverMap.OnLocationChangeListener {
    clickListeners.onLocationChange(it)
  }
  private val optionChangeListener = NaverMap.OnOptionChangeListener {
    clickListeners.onOptionChange()
  }
  private val indoorSelectionChangeListener = NaverMap.OnIndoorSelectionChangeListener {
    clickListeners.onIndoorSelectionChange(it)
  }

  override var callback: NaverMapContentLifecycleCallback = object : NaverMapContentLifecycleCallback {
    override fun onAttached() {
      safeMap.setOnMapClickListener { point, coord ->
        clickListeners.onMapClick(point, coord)
      }
      safeMap.setOnMapLongClickListener { point, coord ->
        clickListeners.onMapLongClick(point, coord)
      }
      safeMap.setOnMapDoubleTapListener { point, coord ->
        clickListeners.onMapDoubleTab(point, coord)
      }
      safeMap.setOnMapTwoFingerTapListener { point, coord ->
        clickListeners.onMapTwoFingerTap(point, coord)
      }
      safeMap.setOnSymbolClickListener {
        clickListeners.onSymbolClick(it)
      }
      safeMap.addOnLoadListener(loadListener)
      safeMap.addOnCameraIdleListener(cameraIdleListener)
      safeMap.addOnCameraChangeListener(cameraChangeListener)
      safeMap.addOnLocationChangeListener(locationChangeListener)
      safeMap.addOnOptionChangeListener(optionChangeListener)
      safeMap.addOnIndoorSelectionChangeListener(indoorSelectionChangeListener)
    }

    override fun onDetached() {
      safeMap.removeOnLoadListener(loadListener)
      safeMap.removeOnCameraIdleListener(cameraIdleListener)
      safeMap.removeOnCameraChangeListener(cameraChangeListener)
      safeMap.removeOnLocationChangeListener(locationChangeListener)
      safeMap.removeOnOptionChangeListener(optionChangeListener)
      safeMap.removeOnIndoorSelectionChangeListener(indoorSelectionChangeListener)

      cameraPositionState.setMap(null)
    }
  }
}
