/*
 * Copyright 2022 SOUP
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
package soup.map.compose.macrobenchmark.navermap.app.profiles

import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMapConstants

fun MapUiSettingsProfiles() {
    MapUiSettings(
        pickTolerance = NaverMapConstants.DefaultPickTolerance,
        isScrollGesturesEnabled = true,
        isZoomGesturesEnabled = true,
        isTiltGesturesEnabled = true,
        isRotateGesturesEnabled = true,
        isStopGesturesEnabled = true,
        scrollGesturesFriction = NaverMapConstants.DefaultScrollGesturesFriction,
        zoomGesturesFriction = NaverMapConstants.DefaultZoomGesturesFriction,
        rotateGesturesFriction = NaverMapConstants.DefaultRotateGesturesFriction,
        isCompassEnabled = true,
        isScaleBarEnabled = true,
        isZoomControlEnabled = true,
        isIndoorLevelPickerEnabled = false,
        isLocationButtonEnabled = false,
        isLogoClickEnabled = true,
        logoGravity = Gravity.BOTTOM or Gravity.START,
        logoMargin = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
    )
}
