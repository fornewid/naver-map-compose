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

import androidx.compose.runtime.Applier

@PublishedApi
internal class MapApplier(val root: MapUiNode) : Applier<MapUiNode> {
  private val stack = mutableListOf<MapUiNode>()
  override var current = root
    private set

  override fun down(node: MapUiNode) {
    stack.add(current)
    current = node
  }

  override fun up() {
    check(stack.isNotEmpty()) { "empty stack" }
    current = stack.removeAt(stack.size - 1)
  }

  override fun insertTopDown(index: Int, instance: MapUiNode) {
    // Ignored. Insert is performed in [insertBottomUp] to build the tree bottom-up to avoid
    // duplicate notification when the child nodes enter the tree
  }

  override fun insertBottomUp(index: Int, instance: MapUiNode) {
    current.insertAt(index, instance)
  }

  override fun move(from: Int, to: Int, count: Int) {
    current.move(from, to, count)
  }

  override fun remove(index: Int, count: Int) {
    current.removeAt(index, count)
  }

  override fun clear() {
    stack.forEach(MapUiNode::removeAll)
    stack.clear()
    current = root.also(MapUiNode::removeAll)
  }
}
