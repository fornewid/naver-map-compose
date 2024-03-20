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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import land.sungbin.navermap.compose.MapOverlay
import land.sungbin.navermap.compose.NaverMap
import land.sungbin.navermap.compose.modifier.MapModifier
import land.sungbin.navermap.compose.modifier.offset
import land.sungbin.navermap.compose.options.rememberCameraPositionState
import land.sungbin.navermap.token.overlay.MarkerOverlay

class SampleActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val position = LatLng(/* latitude = */ 36.019184, /* longitude = */ 129.343357)

    setContent {
      val cameraPositionState = rememberCameraPositionState {
        this.position = CameraPosition(position, 10.0)
      }

      NaverMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
      ) {
        val density = LocalDensity.current
        MapOverlay<MarkerOverlay>(modifier = MapModifier.offset(position)) {
          width = with(density) { 50.dp.roundToPx() }
          height = with(density) { 80.dp.roundToPx() }
        }
      }
    }
  }
}
