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
import com.naver.maps.map.compose.PolygonOverlay
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun PolygonOverlayScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_polygon_overlay),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        NaverMap(
            contentPadding = contentPadding,
        ) {
            PolygonOverlay(
                coords = COORDS_1,
                color = MaterialTheme.colors.primary.copy(alpha = 0.12f),
                outlineColor = MaterialTheme.colors.primary,
                outlineWidth = dimensionResource(R.dimen.overlay_line_width),
            )
            PolygonOverlay(
                coords = COORDS_2,
                holes = HOLES,
                color = Color.Gray.copy(alpha = 0.5f)
            )
        }
    }
}

private val COORDS_1 = listOf(
    LatLng(37.5734571, 126.975335),
    LatLng(37.5738912, 126.9825649),
    LatLng(37.5678124, 126.9812127),
    LatLng(37.5694007, 126.9739434),
)

private val COORDS_2 = listOf(
    LatLng(37.5640984, 126.9712268),
    LatLng(37.5651279, 126.9767904),
    LatLng(37.5625365, 126.9832241),
    LatLng(37.5585305, 126.9809297),
    LatLng(37.5590777, 126.974617),
)

private val HOLES = listOf(
    listOf(
        LatLng(37.5612243, 126.9768938),
        LatLng(37.5627692, 126.9795502),
        LatLng(37.5628377, 126.976066),
    ),
)
