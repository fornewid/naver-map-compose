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

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.Align
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapDefaults
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MarkerScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = "MarkerScreen",
                upPress = upPress
            )
        }
    ) { contentPadding ->
        val cameraPositionState = rememberCameraPositionState {
            this.position = CameraPosition(
                NaverMapDefaults.DefaultCameraPosition.target,
                NaverMapDefaults.DefaultCameraPosition.zoom,
                30.0,
                45.0
            )
        }
        NaverMap(
            cameraPositionState = cameraPositionState,
            contentPadding = contentPadding,
        ) {
            Marker(
                state = rememberMarkerState(
                    position = LatLng(37.5666102, 126.9783881)
                ),
            )
            Marker(
                state = rememberMarkerState(
                    position = LatLng(37.57000, 126.97618)
                ),
                icon = MarkerIcons.BLACK,
                angle = 315f,
            )
            Marker(
                state = rememberMarkerState(
                    position = LatLng(37.57145, 126.98191)
                ),
                icon = OverlayImage.fromResource(R.drawable.ic_info_black_24dp),
                width = dimensionResource(R.dimen.marker_size),
                height = dimensionResource(R.dimen.marker_size),
                isFlat = true,
                angle = 90f,
            )
            Marker(
                state = rememberMarkerState(
                    position = LatLng(37.56768, 126.98602)
                ),
                icon = OverlayImage.fromResource(R.drawable.marker_right_bottom),
                anchor = Offset(1f, 1f),
                angle = 90f,
            )
            Marker(
                state = rememberMarkerState(
                    position = LatLng(37.56436, 126.97499)
                ),
                icon = MarkerIcons.YELLOW,
                captionMinZoom = 12.0,
                captionAligns = arrayOf(Align.Left),
                captionText = stringResource(R.string.marker_caption_1),
            )
            Marker(
                state = rememberMarkerState(
                    position = LatLng(37.56138, 126.97970)
                ),
                icon = MarkerIcons.PINK,
                captionTextSize = 14.sp,
                captionText = stringResource(R.string.marker_caption_2),
                captionMinZoom = 12.0,
                subCaptionTextSize = 10.sp,
                subCaptionColor = Color.Gray,
                subCaptionText = stringResource(R.string.marker_sub_caption_2),
                subCaptionMinZoom = 13.0,
            )
            Marker(
                state = rememberMarkerState(
                    position = LatLng(37.56500, 126.9783881)
                ),
                icon = MarkerIcons.BLACK,
                iconTintColor = Color.Red,
                alpha = 0.5f
            )
        }
    }
}
