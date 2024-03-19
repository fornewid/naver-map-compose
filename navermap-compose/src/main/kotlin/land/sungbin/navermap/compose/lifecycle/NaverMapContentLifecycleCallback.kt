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

package land.sungbin.navermap.compose.lifecycle

public interface NaverMapContentLifecycleCallback {
  public fun onAttached() {}
  public fun onDetached() {}

  public companion object {
    internal val Empty = NaverMapContentLifecycleCallback()

    public inline operator fun invoke(
      crossinline onAttached: () -> Unit = {},
      crossinline onDetached: () -> Unit = {},
    ): NaverMapContentLifecycleCallback = object : NaverMapContentLifecycleCallback {
      override fun onAttached() = onAttached()
      override fun onDetached() = onDetached()
    }
  }
}
