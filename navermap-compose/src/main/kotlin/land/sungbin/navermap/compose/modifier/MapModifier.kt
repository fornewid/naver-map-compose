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

package land.sungbin.navermap.compose.modifier

import androidx.compose.runtime.Stable

@Stable
public interface MapModifier {
  public val head: MapModifier?

  public infix fun then(other: MapModifier): MapModifier =
    if (other === MapModifier) this else CombinedMapModifier(head = this, tail = other)

  public companion object : MapModifier {
    override val head: MapModifier? = null
  }
}

internal inline fun <reified T : MapModifier> MapModifier.findNodeByType(): T? {
  var current: MapModifier? = this
  while (current != null) {
    if (current is CombinedMapModifier) {
      val tail = current.tail
      if (tail is T) return tail else current = current.head
    } else {
      return current as? T
    }
  }

  return null
}

@Stable
internal class CombinedMapModifier(
  override val head: MapModifier,
  val tail: MapModifier,
) : MapModifier {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is CombinedMapModifier) return false

    if (head != other.head) return false
    if (tail != other.tail) return false

    return true
  }

  override fun hashCode(): Int {
    var result = head.hashCode()
    result = 31 * result + tail.hashCode()
    return result
  }
}
