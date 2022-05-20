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
package com.naver.maps.map.compose.demo.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun FitBoundsScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_fit_bounds),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val cameraPositionState = rememberCameraPositionState()
            NaverMap(
                cameraPositionState = cameraPositionState,
            ) {
                Marker(state = rememberMarkerState(position = BOUNDS_1.northEast))
                Marker(state = rememberMarkerState(position = BOUNDS_1.southWest))
                Marker(state = rememberMarkerState(position = BOUNDS_2.northEast))
                Marker(state = rememberMarkerState(position = BOUNDS_2.southWest))
            }

            val coroutineScope = rememberCoroutineScope()
            val padding = with(LocalDensity.current) {
                dimensionResource(R.dimen.fit_bounds_padding).roundToPx()
            }
            var flag by remember { mutableStateOf(false) }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                backgroundColor = MaterialTheme.colors.primary,
                onClick = {
                    val bounds = if (flag) BOUNDS_2 else BOUNDS_1
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdate.fitBounds(bounds, padding),
                            animation = CameraAnimation.Fly,
                            durationMs = 5000
                        )
                    }
                    flag = !flag
                }
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.content_description_run),
                    tint = Color.White
                )
            }
        }
    }
}

private val BOUNDS_1 = LatLngBounds(
    LatLng(37.4282975, 126.7644840),
    LatLng(37.7014553, 127.1837949)
)
private val BOUNDS_2 = LatLngBounds(
    LatLng(34.8357234, 128.7614072),
    LatLng(35.3890374, 129.3055979)
)
