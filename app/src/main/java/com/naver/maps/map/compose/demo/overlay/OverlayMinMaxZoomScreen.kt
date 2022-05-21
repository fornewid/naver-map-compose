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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun OverlayMinMaxZoomScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_overlay_min_max_zoom),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val cameraPositionState = rememberCameraPositionState {
                this.position = DEFAULT_CAMERA_POSITION
            }
            NaverMap(
                cameraPositionState = cameraPositionState,
            ) {
                Marker(
                    icon = MarkerIcons.GREEN,
                    state = rememberMarkerState(
                        position = LatLng(37.56713851901027, 126.97891430703686)
                    ),
                    captionText = stringResource(R.string.caption_marker_1),
                    minZoom = 15.0,
                    minZoomInclusive = true,
                    maxZoom = 17.0,
                    maxZoomInclusive = true,
                )

                Marker(
                    icon = MarkerIcons.BLUE,
                    state = rememberMarkerState(
                        position = LatLng(37.56713851901027, 126.97786189296272)
                    ),
                    captionText = stringResource(R.string.caption_marker_2),
                    minZoom = 15.0,
                    minZoomInclusive = false,
                    maxZoom = 17.0,
                    maxZoomInclusive = true,
                )

                Marker(
                    icon = MarkerIcons.RED,
                    state = rememberMarkerState(
                        position = LatLng(37.566081877242425, 126.97891430703686)
                    ),
                    captionText = stringResource(R.string.caption_marker_3),
                    minZoom = 15.0,
                    minZoomInclusive = true,
                    maxZoom = 17.0,
                    maxZoomInclusive = false,
                )

                Marker(
                    icon = MarkerIcons.YELLOW,
                    state = rememberMarkerState(
                        position = LatLng(37.566081877242425, 126.97786189296272)
                    ),
                    captionText = stringResource(R.string.caption_marker_4),
                    minZoom = 15.0,
                    maxZoom = 17.0,
                    minZoomInclusive = false,
                    maxZoomInclusive = false,
                )
            }

            Text(
                text = stringResource(
                    R.string.format_double,
                    cameraPositionState.position.zoom
                ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .requiredSize(width = 64.dp, height = 36.dp)
                    .wrapContentHeight(),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            val coroutineScope = rememberCoroutineScope()
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                backgroundColor = MaterialTheme.colors.primary,
                onClick = {
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdate.toCameraPosition(DEFAULT_CAMERA_POSITION),
                            animation = CameraAnimation.Easing
                        )
                    }
                }
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.content_description_reset),
                    tint = Color.White
                )
            }
        }
    }
}

private val DEFAULT_CAMERA_POSITION = CameraPosition(
    NaverMapConstants.DefaultCameraPosition.target,
    16.0
)
