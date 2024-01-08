/*
 * Copyright 2023 SOUP
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
package com.naver.maps.map.compose

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.naver.maps.map.LocationSource
import com.naver.maps.map.location.FusedLocationSource

@ExperimentalNaverMapApi
@OptIn(ExperimentalPermissionsApi::class)
@Composable
public fun rememberFusedLocationSource(
    isCompassEnabled: Boolean = false,
): LocationSource {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    val context = LocalContext.current
    val locationSource = remember {
        object : FusedLocationSource(context) {

            override fun hasPermissions(): Boolean {
                return permissionsState.allPermissionsGranted
            }

            override fun onPermissionRequest() {
                permissionsState.launchMultiplePermissionRequest()
            }
        }
    }

    val allGranted = permissionsState.allPermissionsGranted
    LaunchedEffect(allGranted) {
        if (allGranted) {
            locationSource.onPermissionGranted()
        }
    }
    LaunchedEffect(isCompassEnabled) {
        locationSource.setCompassEnabled(enabled = isCompassEnabled)
    }
    return locationSource
}
