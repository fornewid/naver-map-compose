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

import androidx.compose.runtime.collection.mutableVectorOf
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Overlay
import land.sungbin.navermap.compose.lifecycle.NaverMapContentLifecycleCallback
import land.sungbin.navermap.compose.modifier.MapModifier
import land.sungbin.navermap.compose.updater.OverlayUpdater
import land.sungbin.navermap.token.overlay.OverlayFactory

@PublishedApi
internal open class MapUiNode(map: NaverMap? = null) : MapOwner {
  private val components = mutableVectorOf<MapUiNode>()

  override var map: NaverMap? = map
    set(value) {
      field = value
      if (value != null) callback.onAttached()
    }

  override var callback = NaverMapContentLifecycleCallback.Empty
    set(value) {
      if (field != value) {
        field = value
        if (map != null) value.onAttached()
      }
    }

  private var factory: OverlayFactory<*>? = null
  private var overlay: Overlay? = null

  private fun updateOverlay(factory: Class<out OverlayFactory<*>>) {
    val overlay = factory.getDeclaredConstructor().newInstance()
    val mapOverlay = overlay.createOverlay()

    mapOverlay.map = requireMap()

    this.factory = overlay
    this.overlay = mapOverlay
  }

  private fun invalidateOverlay(block: OverlayFactory<*>.() -> Unit) {
    block.invoke(factory!!)
    OverlayUpdater.fromFactory(factory = factory!!, overlay = overlay!!)
  }

  private fun invalidateModifier(modifier: MapModifier) {
    OverlayUpdater.fromModifier(factory = factory!!, overlay = overlay!!, modifier = modifier)
  }

  private fun detach() {
    overlay?.map = null
    callback.onDetached()
    map = null
  }

  fun insertAt(index: Int, instance: MapUiNode) {
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
    val Constructor: () -> MapUiNode = { MapUiNode() }
    val SetOverlay: MapUiNode.(Class<out OverlayFactory<*>>) -> Unit = MapUiNode::updateOverlay
    val InvalidateOverlay: MapUiNode.(OverlayFactory<*>.() -> Unit) -> Unit = MapUiNode::invalidateOverlay
    val SetModifier: MapUiNode.(MapModifier) -> Unit = MapUiNode::invalidateModifier
  }
}
