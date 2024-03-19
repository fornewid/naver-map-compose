package land.sungbin.navermap.compose.internal

import androidx.compose.runtime.collection.mutableVectorOf
import com.naver.maps.map.overlay.Overlay
import land.sungbin.navermap.token.component.Component

internal open class MapNode : Component<Overlay> {
  private val _components = mutableVectorOf<MapNode>()

  @Suppress("unused")
  val components: List<MapNode> get() = _components.asMutableList()

  open fun onAttached() {}
  open fun onRemoved() {}

  fun insertAt(index: Int, instance: MapNode) {
    _components.add(index, instance)
    instance.onAttached()
  }

  fun removeAt(index: Int, count: Int) {
    require(count >= 0) { "count ($count) must be greater than 0" }
    for (i in index + count - 1 downTo index) {
      val child = _components.removeAt(i)
      child.onRemoved()
    }
  }

  fun move(from: Int, to: Int, count: Int) {
    if (from == to) return // nothing to do

    for (i in 0 until count) {
      // if "from" is after "to," the from index moves because we're inserting before it
      val fromIndex = if (from > to) from + i else from
      val toIndex = if (from > to) to + i else to + count - 2
      val child = _components.removeAt(fromIndex)

      _components.add(toIndex, child)
    }
  }

  fun removeAll() {
    for (i in _components.size - 1 downTo 0) {
      val child = _components.removeAt(i)
      child.onRemoved()
    }
  }
}
