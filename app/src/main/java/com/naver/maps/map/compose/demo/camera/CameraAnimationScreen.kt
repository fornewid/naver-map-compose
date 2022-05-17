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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun CameraAnimationScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_camera_animation),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val cameraPositionState = rememberCameraPositionState()
            NaverMap(
                cameraPositionState = cameraPositionState
            ) {
                Marker(state = rememberMarkerState(position = POSITION_1.target))
                Marker(state = rememberMarkerState(position = POSITION_2.target))
            }

            val coroutineScope = rememberCoroutineScope()
            var flag by remember { mutableStateOf(false) }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                backgroundColor = MaterialTheme.colors.primary,
                onClick = {
                    val position = if (flag) POSITION_2 else POSITION_1
                    val animation = if (flag) CameraAnimation.Linear else CameraAnimation.Easing
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdate.toCameraPosition(position),
                            animation = animation,
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

private val POSITION_1 = CameraPosition(LatLng(37.5666102, 126.9783881), 6.0)
private val POSITION_2 = CameraPosition(LatLng(35.1798159, 129.0750222), 8.0)
