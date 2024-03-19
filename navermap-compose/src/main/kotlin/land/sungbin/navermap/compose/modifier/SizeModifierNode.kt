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

import androidx.annotation.Px
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntSize

@Stable
public fun MapModifier.size(size: IntSize): MapModifier =
  this then SizeModifierNode(width = size.width, height = size.height, head = this)

@Stable
public fun MapModifier.size(@Px width: Int? = null, @Px height: Int? = null): MapModifier =
  this then SizeModifierNode(width = width, height = height, head = this)

@Immutable
internal data class SizeModifierNode(
  @Px val width: Int?,
  @Px val height: Int?,
  override val head: MapModifier?,
) : MapModifier
