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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun CircleOverlayScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_circle_overlay),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        NaverMap(
            contentPadding = contentPadding,
        ) {
            CircleOverlay(
                center = LatLng(37.5666102, 126.9783881),
                color = MaterialTheme.colors.primary.copy(alpha = 0.12f),
                radius = 500.0,
                outlineColor = MaterialTheme.colors.primary,
                outlineWidth = dimensionResource(R.dimen.overlay_line_width)
            )
        }
    }
}
