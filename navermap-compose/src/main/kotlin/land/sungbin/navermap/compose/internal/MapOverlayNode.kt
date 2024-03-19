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

package land.sungbin.navermap.compose.internal

import androidx.compose.runtime.collection.mutableVectorOf
import com.naver.maps.map.NaverMap
import land.sungbin.navermap.compose.lifecycle.NaverMapContentLifecycleCallback
import land.sungbin.navermap.compose.modifier.MapModifier
import land.sungbin.navermap.compose.modifier.SizeModifierNode
import land.sungbin.navermap.compose.modifier.findNodeByType
import land.sungbin.navermap.compose.updater.OverlayUpdaters
import land.sungbin.navermap.token.overlay.MapOverlay
import land.sungbin.navermap.token.overlay.Overlay

@PublishedApi
internal class MapOverlayNode(override var map: NaverMap? = null) : MapOwner {
  private val components = mutableVectorOf<MapOverlayNode>()

  override var callback = NaverMapContentLifecycleCallback.Empty
    set(value) {
      if (field != value) {
        field = value
        value.onAttached()
      }
    }

  private var overlay: Overlay<*>? = null
  private var mapOverlay: MapOverlay? = null

  private var modifier: MapModifier = MapModifier
    set(value) {
      if (field != value) {
        field = value
        value.findNodeByType<SizeModifierNode>()?.let { size ->
          size.width?.let { OverlayUpdaters.width(overlay = mapOverlay!!, value = it) }
          size.height?.let { OverlayUpdaters.height(overlay = mapOverlay!!, value = it) }
        }
      }
    }

  private fun updateOverlay(factory: Class<out Overlay<*>>) {
    val overlay = factory.getDeclaredConstructor().newInstance()
    val mapOverlay = overlay.createOverlay()

    mapOverlay.map = requireMap()

    this.overlay = overlay
    this.mapOverlay = mapOverlay
  }

  private fun invalidateOverlay(block: Overlay<*>.() -> Unit) {
    block.invoke(overlay!!)
    // TODO: Run Updaters...
  }

  private fun detach() {
    mapOverlay?.map = null
    callback.onDetached()
    map = null
  }

  fun insertAt(index: Int, instance: MapOverlayNode) {
    components.add(index, instance)
  }

  fun removeAt(index: Int, count: Int) {
    require(count >= 0) { "count ($count) must be greater than 0" }
    for (i in index + count - 1 downTo index) {
      val child = components.removeAt(i)
      child.detach()
    }
  }

  fun move(from: Int, to: Int, count: Int) {
    if (from == to) return

    for (i in 0 until count) {
      val fromIndex = if (from > to) from + i else from
      val toIndex = if (from > to) to + i else to + count - 2
      val child = components.removeAt(fromIndex)
      components.add(toIndex, child)
    }
  }

  fun removeAll() {
    for (i in components.size - 1 downTo 0) {
      val child = components.removeAt(i)
      child.detach()
    }
  }

  companion object {
    val Constructor: () -> MapOverlayNode = { MapOverlayNode() }
    val SetOverlay: MapOverlayNode.(Class<out Overlay<*>>) -> Unit = MapOverlayNode::updateOverlay
    val InvalidateOverlay: MapOverlayNode.(Overlay<*>.() -> Unit) -> Unit = MapOverlayNode::invalidateOverlay
    val SetModifier: MapOverlayNode.(MapModifier) -> Unit = { modifier = it }
  }
}
