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

package land.sungbin.navermap.compose.updater

import com.naver.maps.map.overlay.Marker
import land.sungbin.navermap.token.overlay.MapOverlay

internal object OverlayUpdaters {
  private val widthMap = mapOf(Marker::class.java to ::markerWidth)
  private val heightMap = mapOf(Marker::class.java to ::markerHeight)

  fun width(overlay: MapOverlay, value: Int) {
    widthMap.forEach { (type, updater) ->
      if (type.isAssignableFrom(overlay::class.java)) {
        return@width updater(overlay, value)
      }
    }
  }

  fun height(overlay: MapOverlay, value: Int) {
    heightMap.forEach { (type, updater) ->
      if (type.isAssignableFrom(overlay::class.java)) {
        return@height updater(overlay, value)
      }
    }
  }

  private fun markerWidth(overlay: MapOverlay, value: Int) {
    (overlay as Marker).width = value
  }

  private fun markerHeight(overlay: MapOverlay, value: Int) {
    (overlay as Marker).height = value
  }
}
