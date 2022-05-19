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
import androidx.compose.ui.res.stringResource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PolylineOverlay
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun PolylineOverlayScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_polyline_overlay),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        NaverMap(
            contentPadding = contentPadding,
        ) {
            val width = dimensionResource(R.dimen.overlay_line_width)
            val patternInterval = dimensionResource(R.dimen.overlay_pattern_interval)

            PolylineOverlay(
                width = width,
                coords = COORDS_1,
                color = MaterialTheme.colors.primary
            )

            PolylineOverlay(
                width = width,
                coords = COORDS_2,
                pattern = arrayOf(patternInterval, patternInterval),
                color = Color.Gray
            )
        }
    }
}

private val COORDS_1 = listOf(
    LatLng(37.57152, 126.97714),
    LatLng(37.56607, 126.98268),
    LatLng(37.56445, 126.97707),
    LatLng(37.55855, 126.97822),
)

private val COORDS_2 = listOf(
    LatLng(37.57152, 126.97714),
    LatLng(37.5744287, 126.982625),
)
