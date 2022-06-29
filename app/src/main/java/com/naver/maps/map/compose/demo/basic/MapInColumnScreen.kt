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
package com.naver.maps.map.compose.demo.basic

import android.view.MotionEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState

@Composable
fun MapInColumnScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_map_in_column),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val cameraPositionState = rememberCameraPositionState()
            var columnScrollingEnabled by remember { mutableStateOf(true) }

            LaunchedEffect(cameraPositionState.isMoving) {
                if (!cameraPositionState.isMoving) {
                    columnScrollingEnabled = true
                }
            }

            MapInColumn(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                columnScrollingEnabled = columnScrollingEnabled,
                onMapTouched = {
                    columnScrollingEnabled = false
                },
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MapInColumn(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    columnScrollingEnabled: Boolean,
    onMapTouched: () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = rememberScrollState(),
                    enabled = columnScrollingEnabled
                ),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.padding(5.dp))
            for (i in 1..20) {
                Text(
                    text = "Item $i",
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                NaverMapInColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInteropFilter(
                            onTouchEvent = {
                                if (it.action == MotionEvent.ACTION_DOWN) {
                                    onMapTouched()
                                    false
                                } else {
                                    true
                                }
                            }
                        ),
                    cameraPositionState = cameraPositionState,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            for (i in 21..40) {
                Text(
                    text = "Item $i",
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun NaverMapInColumn(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
) {
    NaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(mapType = MapType.None),
        uiSettings = MapUiSettings(isCompassEnabled = false),
    ) {
        Marker(state = rememberMarkerState(position = POSITION.target))
    }
}

private val POSITION = CameraPosition(LatLng(37.5666102, 126.9783881), 6.0)
