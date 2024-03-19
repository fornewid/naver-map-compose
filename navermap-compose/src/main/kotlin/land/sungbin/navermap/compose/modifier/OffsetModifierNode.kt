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
public fun MapModifier.offset(coord: LatLng): MapModifier =
  this then OffsetModifierNode(lat = coord.latitude, lng = coord.longitude, head = this)

@Stable
public fun MapModifier.offset(lat: Double, lng: Double): MapModifier =
  this then OffsetModifierNode(lat = lat, lng = lng, head = this)

@Immutable
internal data class OffsetModifierNode(
  val lat: Double,
  val lng: Double,
  override val head: MapModifier?,
) : MapModifier
