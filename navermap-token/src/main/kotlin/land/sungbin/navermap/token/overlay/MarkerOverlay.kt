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

package land.sungbin.navermap.token.overlay

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import land.sungbin.navermap.token.HasOverlayImage
import land.sungbin.navermap.token.HasSize

public open class MarkerOverlay : HasOverlayImage, HasSize, OverlayFactory<Marker> {
  override var overlayImage: OverlayImage? = null
  override var width: Int? = null
  override var height: Int? = null

  public override fun createOverlay(): Marker = Marker(LatLng(0.0, 0.0))
}
