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

package land.sungbin.navermap.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import land.sungbin.navermap.compose.MapOverlay
import land.sungbin.navermap.compose.NaverMap
import land.sungbin.navermap.compose.modifier.MapModifier
import land.sungbin.navermap.compose.modifier.size
import land.sungbin.navermap.token.overlay.MarkerOverlay

class SampleActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      NaverMap(modifier = Modifier.fillMaxSize()) {
        MapOverlay<MarkerOverlay>(modifier = MapModifier.size(width = 500))
      }
    }
  }
}
