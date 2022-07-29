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
package net.daum.mf.map.api.compose.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import net.daum.mf.map.api.CameraUpdateFactory
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPointBounds
import net.daum.mf.map.api.compose.ExperimentalKakaoMapApi
import net.daum.mf.map.api.compose.KakaoMap
import net.daum.mf.map.api.compose.rememberCameraPositionState
import soup.korea.map.compose.demo.R
import soup.korea.map.compose.demo.common.DefaultTopAppBar

@OptIn(ExperimentalKakaoMapApi::class)
@Composable
fun MarkerScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.marker_demo_title),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val cameraPositionState = rememberCameraPositionState {
                val padding = 20
                val minZoomLevel = 7f
                val maxZoomLevel = 10f
                val bounds = MapPointBounds(CUSTOM_MARKER_POINT, DEFAULT_MARKER_POINT)
                move(
                    CameraUpdateFactory
                        .newMapPointBounds(bounds, padding, minZoomLevel, maxZoomLevel)
                )
            }
            KakaoMap(
                cameraPositionState = cameraPositionState,
            ) {
//                Marker(
//                    mapPoint = DEFAULT_MARKER_POINT,
//                    itemName = "Default Marker",
//                    tag = 0,
//                    markerType = MapPOIItem.MarkerType.BluePin,
//                    selectedMarkerType = MapPOIItem.MarkerType.RedPin,
//                )
//                Marker(
//                    mapPoint = CUSTOM_MARKER_POINT,
//                    itemName = "Custom Marker",
//                    tag = 1,
//                    markerType = MapPOIItem.MarkerType.CustomImage,
//                    customImageResourceId = R.drawable.custom_marker_red,
//                    customImageAutoscale = false,
//                    customImageAnchor = Offset(0.5f, 1.0f),
//                )
//                val bm = BitmapFactory.decodeResource(getResources(), R.drawable.custom_marker_star)
//                Marker(
//                    mapPoint = CUSTOM_MARKER_POINT2,
//                    itemName = "Custom Bitmap Marker",
//                    tag = 2,
//                    markerType = MapPOIItem.MarkerType.BluePin,
//                    customImageBitmap = bm,
//                    customImageAutoscale = false,
//                    customImageAnchor = Offset(0.5f, 1.0f),
//                )
            }
        }
    }
}

private val CUSTOM_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.537229, 127.005515)
private val CUSTOM_MARKER_POINT2 = MapPoint.mapPointWithGeoCoord(37.447229, 127.015515)
private val DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.4020737, 127.1086766)
