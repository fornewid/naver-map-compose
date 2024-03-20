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

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import kotlin.reflect.full.createType
import land.sungbin.navermap.compose.modifier.MapModifier
import land.sungbin.navermap.compose.modifier.OffsetModifierNode
import land.sungbin.navermap.compose.modifier.SizeModifierNode
import land.sungbin.navermap.compose.modifier.findNodeByType
import land.sungbin.navermap.token.HasSize
import land.sungbin.navermap.token.overlay.OverlayFactory

private object Types {
  val Marker = Marker::class.createType()
  val HasSize = HasSize::class.createType()
}

internal object OverlayUpdater {
  private val widthSupported = mapOf(Types.Marker to Unit, Types.HasSize to Unit)
  private val heightSupported = mapOf(Types.Marker to Unit, Types.HasSize to Unit)
  private val offsetSupported = mapOf(Types.Marker to Unit)

  fun fromFactory(factory: OverlayFactory<*>, overlay: Overlay) {
    val types = factory::class.supertypes.filter { it.arguments.isEmpty() }

    for (type in types) {
      if (widthSupported.containsKey(type) || heightSupported.containsKey(type)) {
        val size = factory as HasSize

        if (widthSupported.containsKey(type))
          size.width?.let { markerWidth(overlay = overlay, value = it) }

        if (heightSupported.containsKey(type))
          size.height?.let { markerHeight(overlay = overlay, value = it) }
      }
    }
  }

  fun fromModifier(factory: OverlayFactory<*>, overlay: Overlay, modifier: MapModifier) {
    val type = factory::class.supertypes.last { it.arguments.size == 1 }.arguments.first().type

    if (widthSupported.containsKey(type) || heightSupported.containsKey(type)) {
      modifier.findNodeByType<SizeModifierNode>()?.let { size ->
        if (widthSupported.containsKey(type))
          size.width?.let { markerWidth(overlay = overlay, value = it) }

        if (heightSupported.containsKey(type))
          size.height?.let { markerHeight(overlay = overlay, value = it) }
      }
    }

    if (offsetSupported.containsKey(type)) {
      modifier.findNodeByType<OffsetModifierNode>()?.let { offset ->
        markerOffset(overlay = overlay, position = offset.position)
      }
    }
  }

  private fun markerWidth(overlay: Overlay, value: Int) {
    (overlay as Marker).width = value
  }

  private fun markerHeight(overlay: Overlay, value: Int) {
    (overlay as Marker).height = value
  }

  private fun markerOffset(overlay: Overlay, position: LatLng) {
    (overlay as Marker).position = position
  }
}
