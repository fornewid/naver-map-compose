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
package com.naver.maps.map.compose.demo.location

import android.graphics.PointF
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationSource
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.demo.common.showToast

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun CustomLocationSourceScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_custom_location_source),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val context = LocalContext.current
            val locationSource = remember { CustomLocationSource() }
            NaverMap(
                locationSource = locationSource,
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.NoFollow
                ),
                onMapClick = locationSource::onMapClick,
                onLocationChange = {
                    context.showToast(
                        R.string.format_location_changed,
                        it.latitude,
                        it.longitude
                    )
                }
            )
        }
    }
}

private class CustomLocationSource : LocationSource {
    private var listener: LocationSource.OnLocationChangedListener? = null

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        this.listener = listener
    }

    override fun deactivate() {
        listener = null
    }

    fun onMapClick(point: PointF, coord: LatLng) {
        listener?.onLocationChanged(
            Location("CustomLocationSource").apply {
                latitude = coord.latitude
                longitude = coord.longitude
                accuracy = 100f
            }
        )
    }
}
