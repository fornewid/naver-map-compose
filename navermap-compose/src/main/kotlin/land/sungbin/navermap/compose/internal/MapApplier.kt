package land.sungbin.navermap.compose.internal

import androidx.compose.runtime.Applier
import com.naver.maps.map.NaverMap

internal class MapApplier(
  val map: NaverMap,
  private val root: MapNode,
) : Applier<MapNode> {
  private val stack = mutableListOf<MapNode>()
  override var current = root
    private set

  override fun down(node: MapNode) {
    stack.add(current)
    current = node
  }

  override fun up() {
    check(stack.isNotEmpty()) { "empty stack" }
    current = stack.removeAt(stack.size - 1)
  }

  override fun insertTopDown(index: Int, instance: MapNode) {
    // Ignored. Insert is performed in [insertBottomUp] to build the tree bottom-up to avoid
    // duplicate notification when the child nodes enter the tree
  }

  override fun insertBottomUp(index: Int, instance: MapNode) {
    current.insertAt(index, instance)
  }

  override fun move(from: Int, to: Int, count: Int) {
    current.move(from, to, count)
  }

  override fun remove(index: Int, count: Int) {
    current.removeAt(index, count)
  }

  override fun clear() {
    stack.forEach(MapNode::removeAll)
    stack.clear()
    current = root.also { it.removeAll() }
  }
}

