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
import androidx.compose.runtime.Composer
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.ReusableComposeNode
import androidx.compose.runtime.currentComposer
import land.sungbin.navermap.compose.runtime.MapApplier
import land.sungbin.navermap.compose.runtime.MapUiNode
import land.sungbin.navermap.compose.runtime.requireMap
import land.sungbin.navermap.compose.modifier.MapModifier
import land.sungbin.navermap.token.overlay.OverlayFactory

@Composable
@NaverMapComposable
public inline fun <reified O : OverlayFactory<*>> MapOverlay(
  modifier: MapModifier = MapModifier,
  crossinline block: @DisallowComposableCalls O.() -> Unit = {},
) {
  val applier = currentComposer.mapApplier

  ReusableComposeNode<MapUiNode, MapApplier>(
    factory = MapUiNode.Constructor,
    update = {
      init {
        map = applier.root.requireMap()
        MapUiNode.SetOverlay.invoke(/* receiver = */ this, /* value = */ O::class.java)
      }
      set(value = modifier, block = MapUiNode.SetModifier)
      set(value = { block(this as O) }, block = MapUiNode.InvalidateOverlay)
    },
  )
}

@PublishedApi
internal val Composer.mapApplier: MapApplier
  inline get() = applier as? MapApplier ?: error("Should be used inside 'NaverMap' composable!")
