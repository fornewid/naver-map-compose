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

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.naver.maps.geometry.LatLng

@Stable
public fun MapModifier.offset(position: LatLng): MapModifier =
  this then OffsetModifierNode(position = position, head = this)

@Stable
public fun MapModifier.offset(lat: Double, lng: Double): MapModifier =
  this then OffsetModifierNode(position = LatLng(/* latitude = */ lat, /* longitude = */ lng), head = this)

@Immutable
internal data class OffsetModifierNode(
  val position: LatLng,
  override val head: MapModifier?,
) : MapModifier
