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

package land.sungbin.navermap.token.intercept

import land.sungbin.navermap.token.overlay.MapOverlay
import land.sungbin.navermap.token.overlay.Overlay

public interface OverlayInterceptor<O : MapOverlay> {
  public fun request(overlay: Overlay<O>): O? = null
  public fun built(overlay: O): O = overlay

  public companion object {
    public inline operator fun <O : MapOverlay> invoke(
      crossinline request: (Overlay<O>) -> O? = { null },
      crossinline built: (O) -> O = { it },
    ): OverlayInterceptor<O> = object : OverlayInterceptor<O> {
      override fun request(overlay: Overlay<O>) = request(overlay)
      override fun built(overlay: O) = built(overlay)
    }
  }
}
