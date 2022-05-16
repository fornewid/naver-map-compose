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

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ArrowheadPathOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.rememberCameraPositionState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ArrowheadPathOverlayScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = "ArrowheadPathOverlayScreen",
                upPress = upPress
            )
        }
    ) { contentPadding ->
        val cameraPositionState = rememberCameraPositionState {
            this.position = CameraPosition(
                LatLng(37.5701573, 126.9777503),
                14.0,
                50.0,
                0.0
            )
        }
        NaverMap(
            cameraPositionState = cameraPositionState,
            contentPadding = contentPadding,
        ) {
            ArrowheadPathOverlay(
                coords = COORDS_1,
                width = dimensionResource(R.dimen.arrowhead_path_overlay_width),
                color = Color.White,
                outlineWidth = dimensionResource(R.dimen.arrowhead_path_overlay_outline_width),
                outlineColor = MaterialTheme.colors.primary,
            )

            ArrowheadPathOverlay(
                coords = COORDS_2,
                width = dimensionResource(R.dimen.arrowhead_path_overlay_width),
                color = Color(0x80000000.toInt()),
                outlineWidth = dimensionResource(R.dimen.arrowhead_path_overlay_outline_width),
                outlineColor = Color.Black,
            )

            ArrowheadPathOverlay(
                coords = COORDS_2,
                width = dimensionResource(R.dimen.arrowhead_path_overlay_width),
                color = Color.White,
                outlineWidth = dimensionResource(R.dimen.arrowhead_path_overlay_outline_width),
                outlineColor = MaterialTheme.colors.primary,
                elevation = dimensionResource(R.dimen.arrowhead_path_overlay_elevation),
            )
        }
    }
}

private val COORDS_1 = listOf(
    LatLng(37.568003, 126.9782503),
    LatLng(37.5701573, 126.9782503),
    LatLng(37.5701573, 126.9803745)
)

private val COORDS_2 = listOf(
    LatLng(37.568003, 126.9772503),
    LatLng(37.5701573, 126.9772503),
    LatLng(37.5701573, 126.9751261)
)
