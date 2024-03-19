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

package land.sungbin.navermap.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.ReusableComposeNode
import androidx.compose.runtime.currentComposer
import land.sungbin.navermap.compose.internal.MapApplier
import land.sungbin.navermap.compose.internal.MapOverlayNode
import land.sungbin.navermap.compose.modifier.MapModifier
import land.sungbin.navermap.token.overlay.Overlay

@Composable
@NaverMapComposable
public inline fun <reified O : Overlay<*>> MapOverlay(
  modifier: MapModifier = MapModifier,
  crossinline block: @DisallowComposableCalls O.() -> Unit = {},
) {
  val map = (currentComposer.applier as MapApplier).map

  ReusableComposeNode<MapOverlayNode, MapApplier>(
    factory = { MapOverlayNode(map = map) },
    update = {
      init { MapOverlayNode.SetOverlay.invoke(/* receiver = */ this, /* value = */ O::class.java) }
      set(value = modifier, block = MapOverlayNode.SetModifier)
      set(value = { block(this as O) }, block = MapOverlayNode.InvalidateOverlay)
    },
  )
}
