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
package com.naver.maps.map.compose.demo.overlay

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.GroundOverlay
import com.naver.maps.map.compose.GroundOverlayPosition
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.overlay.OverlayImage

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun GroundOverlayScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_ground_overlay),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        NaverMap(
            contentPadding = contentPadding,
        ) {
            GroundOverlay(
                position = GroundOverlayPosition.create(
                    bounds = LatLngBounds(
                        LatLng(37.57023, 126.97672),
                        LatLng(37.57545, 126.98323)
                    )
                ),
                alpha = 0.8f
            )

            GroundOverlay(
                position = GroundOverlayPosition.create(
                    bounds = LatLngBounds(
                        LatLng(37.566351, 126.977234),
                        LatLng(37.568528, 126.97998)
                    )
                ),
                image = OverlayImage.fromResource(R.drawable.ground_overlay)
            )
        }
    }
}
