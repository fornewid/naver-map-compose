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
package com.naver.maps.map.compose.demo.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerDefaults
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.demo.common.showToast
import com.naver.maps.map.compose.rememberMarkerState
import com.naver.maps.map.util.MarkerIcons

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun OverlayClickEventScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_overlay_click_event),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val context = LocalContext.current
            NaverMap(
                onMapClick = { _, coord ->
                    context.showToast(
                        R.string.format_map_click,
                        coord.latitude,
                        coord.longitude
                    )
                }
            ) {
                var enabled1 by remember { mutableStateOf(true) }
                Marker(
                    state = rememberMarkerState(
                        position = LatLng(37.57207, 126.97917)
                    ),
                    captionText = stringResource(R.string.consume_event),
                    icon = if (enabled1) {
                        MarkerDefaults.DefaultIcon
                    } else {
                        MarkerIcons.GRAY
                    },
                    onClick = {
                        enabled1 = !enabled1
                        true
                    }
                )

                var enabled2 by remember { mutableStateOf(true) }
                Marker(
                    state = rememberMarkerState(
                        position = LatLng(37.56361, 126.97439)
                    ),
                    captionText = stringResource(R.string.propagate_event),
                    icon = if (enabled2) {
                        MarkerDefaults.DefaultIcon
                    } else {
                        MarkerIcons.GRAY
                    },
                    onClick = {
                        enabled2 = !enabled2
                        false
                    }
                )

                Marker(
                    state = rememberMarkerState(
                        position = LatLng(37.56671, 126.98260)
                    ),
                    captionText = stringResource(R.string.no_event_listener)
                )
            }
        }
    }
}
