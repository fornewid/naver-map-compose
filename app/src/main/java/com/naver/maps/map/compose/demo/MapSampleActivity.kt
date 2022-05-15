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
package com.naver.maps.map.compose.demo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationSource
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

private const val TAG = "MapSampleActivity"

class MapSampleActivity : ComponentActivity() {

    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen(locationSource)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun MainScreen(locationSource: LocationSource) {
    Box(modifier = Modifier.fillMaxSize()) {
        var isMapLoaded by remember { mutableStateOf(false) }
        val cameraPositionState = rememberCameraPositionState()
        NaverMapView(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            locationSource = locationSource,
            onMapLoaded = {
                isMapLoaded = true
            },
        )
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun NaverMapView(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    locationSource: LocationSource,
    onMapLoaded: () -> Unit,
    content: @Composable () -> Unit = {},
) {
    var uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                scaleBarEnabled = false,
                compassEnabled = true,
                locationButtonEnabled = true
            )
        )
    }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.BASIC,
                isIndoorEnabled = true,
                locationTrackingMode = LocationTrackingMode.FOLLOW
            )
        )
    }

    var mapVisible by remember { mutableStateOf(true) }

    if (mapVisible) {
        NaverMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            locationSource = locationSource,
            onMapLoaded = onMapLoaded,
            onMapClick = { point, coord ->
                Log.d(TAG, "onMapClick: point=$point, coord=$coord")
            }
        ) {
            Marker(
                state = rememberMarkerState(position = LatLng(37.5666102, 126.9783881))
            )
            Marker(
                state = rememberMarkerState(position = LatLng(37.57000, 126.97618)),
                icon = MarkerIcons.BLACK,
                rotation = 315f
            )

            content()
        }
    }
    MapButton(
        text = "Toggle Map",
        onClick = { mapVisible = !mapVisible },
        modifier = Modifier.testTag("toggleMapVisibility"),
    )
}

@Composable
private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier.padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary
        ),
        onClick = onClick
    ) {
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}
