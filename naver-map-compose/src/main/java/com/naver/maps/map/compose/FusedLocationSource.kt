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
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.naver.maps.map.LocationSource

@ExperimentalNaverMapApi
@OptIn(ExperimentalPermissionsApi::class)
@Composable
public fun rememberFusedLocationSource(): LocationSource {
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
    return locationSource
}

private abstract class FusedLocationSource(context: Context) : LocationSource {

    private val callback = object : FusedLocationCallback(context.applicationContext) {
        override fun onLocationChanged(location: Location?) {
            lastLocation = location
        }
    }

    private var listener: LocationSource.OnLocationChangedListener? = null
    private var isListening: Boolean = false
    private var lastLocation: Location? = null
        set(value) {
            field = value
            if (listener != null && value != null) {
                listener?.onLocationChanged(value)
            }
        }

    abstract fun hasPermissions(): Boolean
    abstract fun onPermissionRequest()

    fun onPermissionGranted() {
        setListening(true)
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        this.listener = listener
        if (isListening.not()) {
            if (hasPermissions()) {
                setListening(true)
            } else {
                onPermissionRequest()
            }
        }
    }

    override fun deactivate() {
        if (isListening) {
            setListening(false)
        }
        this.listener = null
    }

    private fun setListening(listening: Boolean) {
        if (listening) {
            callback.startListening()
        } else {
            callback.stopListening()
        }
        isListening = listening
    }

    private abstract class FusedLocationCallback(private val context: Context) {

        private val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                onLocationChanged(locationResult.lastLocation)
            }
        }

        fun startListening() {
            GoogleApiClient.Builder(context)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    @SuppressLint("MissingPermission")
                    override fun onConnected(bundle: Bundle?) {
                        val request = LocationRequest()
                        request.priority = 100
                        request.interval = 1000L
                        request.fastestInterval = 1000L
                        LocationServices.getFusedLocationProviderClient(context)
                            .requestLocationUpdates(request, locationCallback, null)
                    }

                    override fun onConnectionSuspended(i: Int) {}
                })
                .addApi(LocationServices.API)
                .build()
                .connect()
        }

        fun stopListening() {
            LocationServices
                .getFusedLocationProviderClient(context)
                .removeLocationUpdates(locationCallback)
        }

        abstract fun onLocationChanged(location: Location?)
    }
}
