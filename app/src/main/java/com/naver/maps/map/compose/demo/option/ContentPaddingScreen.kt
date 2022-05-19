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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapDefaults
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ContentPaddingScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_content_padding),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {

            val cameraPositionState = rememberCameraPositionState {
                this.position = CameraPosition(
                    COORD_1,
                    NaverMapDefaults.DefaultCameraPosition.zoom
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.label_content_bounds),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .requiredWidth(100.dp)
                )
                Text(
                    text = cameraPositionState.contentBounds
                        ?.let { content ->
                            stringResource(
                                R.string.format_bounds,
                                content.southLatitude,
                                content.westLongitude,
                                content.northLatitude,
                                content.eastLongitude
                            )
                        }
                        .orEmpty(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .fillMaxWidth()
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.label_covering_bounds),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .requiredWidth(100.dp)
                )
                Text(
                    text = cameraPositionState.coveringBounds
                        ?.let { covering ->
                            stringResource(
                                R.string.format_bounds,
                                covering.southLatitude,
                                covering.westLongitude,
                                covering.northLatitude,
                                covering.eastLongitude
                            )
                        }
                        .orEmpty(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .fillMaxWidth()
                )
            }

            Box {
                NaverMap(
                    cameraPositionState = cameraPositionState,
                    contentPadding = PaddingValues(
                        start = dimensionResource(R.dimen.map_padding_left),
                        top = dimensionResource(R.dimen.map_padding_top),
                        end = dimensionResource(R.dimen.map_padding_right),
                        bottom = dimensionResource(R.dimen.map_padding_bottom)
                    )
                ) {
                    Marker(
                        state = rememberMarkerState(position = COORD_1)
                    )
                    Marker(
                        state = rememberMarkerState(position = COORD_2)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .requiredHeight(dimensionResource(R.dimen.map_padding_top))
                        .background(Color(0x30000000))
                )
                Spacer(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .requiredHeight(dimensionResource(R.dimen.map_padding_left))
                        .background(Color(0x30000000))
                )
                Spacer(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .requiredHeight(dimensionResource(R.dimen.map_padding_bottom))
                        .background(Color(0x30000000))
                )
                Spacer(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxWidth()
                        .requiredHeight(dimensionResource(R.dimen.map_padding_right))
                        .background(Color(0x30000000))
                )

                val coroutineScope = rememberCoroutineScope()
                var flag by remember { mutableStateOf(false) }
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    backgroundColor = MaterialTheme.colors.primary,
                    onClick = {
                        val coord = if (flag) COORD_1 else COORD_2
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdate.scrollTo(coord),
                                animation = CameraAnimation.Fly,
                                durationMs = 3000
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
}

private val COORD_1 = LatLng(37.5666102, 126.9783881)
private val COORD_2 = LatLng(35.1798159, 129.0750222)
