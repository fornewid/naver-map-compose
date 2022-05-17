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

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun CameraEventScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_camera_event),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            val cameraPositionState = rememberCameraPositionState()
            val position by remember {
                derivedStateOf {
                    cameraPositionState.position
                }
            }
            var isMovingByAnimation by remember { mutableStateOf(false) }

            // TODO: How to improve this performance?
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.label_camera_change),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .requiredWidth(50.dp)
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                )
                Text(
                    text = stringResource(
                        R.string.format_camera_event,
                        position.target.latitude,
                        position.target.longitude,
                        position.zoom,
                        position.tilt,
                        position.bearing
                    ),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.label_camera_idle),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .requiredWidth(50.dp)
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                )
                var idlePosition by remember { mutableStateOf(position) }
                if (cameraPositionState.isMoving.not()) {
                    idlePosition = position
                }
                Text(
                    text = stringResource(
                        R.string.format_camera_event,
                        idlePosition.target.latitude,
                        idlePosition.target.longitude,
                        idlePosition.zoom,
                        idlePosition.tilt,
                        idlePosition.bearing
                    ),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Box {
                NaverMap(
                    cameraPositionState = cameraPositionState,
                ) {
                    Marker(state = rememberMarkerState(position = COORD_1))
                    Marker(state = rememberMarkerState(position = COORD_2))
                }

                Image(
                    painterResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_blue),
                    contentDescription = stringResource(R.string.content_description_center_marker),
                    modifier = Modifier
                        .padding(bottom = 54.dp)
                        .align(Alignment.Center)
                )
                Image(
                    painterResource(R.drawable.crosshair),
                    contentDescription = stringResource(R.string.content_description_center_crosshair),
                    modifier = Modifier
                        .requiredSize(14.dp, 14.dp)
                        .align(Alignment.Center)
                )

                val context = LocalContext.current
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    backgroundColor = MaterialTheme.colors.primary,
                    onClick = {
                        if (isMovingByAnimation) {
                            cameraPositionState.stop()
                        } else {
                            // TODO: How to improve the chains of camera animations?
                            cameraPositionState.move(
                                CameraUpdate.scrollTo(COORD_1)
                                    .animate(CameraAnimation.Fly, 3000)
                                    .cancelCallback {
                                        isMovingByAnimation = false
                                        context.showToast(R.string.camera_update_cancelled)
                                    }
                                    .finishCallback {
                                        cameraPositionState.move(
                                            CameraUpdate.scrollTo(COORD_2)
                                                .animate(CameraAnimation.Fly, 3000)
                                                .cancelCallback {
                                                    isMovingByAnimation = false
                                                    context.showToast(R.string.camera_update_cancelled)
                                                }
                                                .finishCallback {
                                                    isMovingByAnimation = false
                                                    context.showToast(R.string.camera_update_finished)
                                                }
                                        )
                                    },
                            )
                            isMovingByAnimation = true
                        }
                    }
                ) {
                    Icon(
                        if (isMovingByAnimation) {
                            Icons.Default.Stop
                        } else {
                            Icons.Default.PlayArrow
                        },
                        contentDescription = stringResource(R.string.content_description_move_camera),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

private fun Context.showToast(@StringRes msgId: Int) {
    Toast.makeText(this, msgId, LENGTH_SHORT).show()
}

private val COORD_1 = LatLng(35.1798159, 129.0750222)
private val COORD_2 = LatLng(37.5666102, 126.9783881)
