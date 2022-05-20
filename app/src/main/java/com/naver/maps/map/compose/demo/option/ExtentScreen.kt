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
package com.naver.maps.map.compose.demo.option

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PolylineOverlay
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.util.MapConstants

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ExtentScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_extent),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val cameraPositionState = rememberCameraPositionState()
            NaverMap(
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    extent = MapConstants.EXTENT_KOREA,
                )
            ) {
                PolylineOverlay(
                    coords = MapConstants.EXTENT_KOREA.toPolygon().toList(),
                )
            }

            val padding = with(LocalDensity.current) {
                dimensionResource(R.dimen.fit_bounds_padding).roundToPx()
            }
            LaunchedEffect(Unit) {
                cameraPositionState.move(CameraUpdate.fitBounds(MapConstants.EXTENT_KOREA, padding))
            }
        }
    }
}
