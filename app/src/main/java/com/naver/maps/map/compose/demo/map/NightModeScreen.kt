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
package com.naver.maps.map.compose.demo.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapPropertiesDefaults
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerDefaults
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.rememberMarkerState
import com.naver.maps.map.util.MarkerIcons

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NightModeScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_night_mode),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            var isNightModeEnabled by remember { mutableStateOf(true) }
            CheckedTextView(
                text = stringResource(R.string.night_mode),
                checked = isNightModeEnabled,
                onCheckedChange = { isNightModeEnabled = it }
            )

            val mapProperties by remember {
                derivedStateOf {
                    MapProperties(
                        isNightModeEnabled = isNightModeEnabled,
                        mapType = MapType.Navi,
                        minZoom = 4.0,
                        backgroundColor = if (isNightModeEnabled) {
                            MapPropertiesDefaults.DefaultBackgroundColorDark
                        } else {
                            MapPropertiesDefaults.DefaultBackgroundColorLight
                        },
                        backgroundResource = if (isNightModeEnabled) {
                            MapPropertiesDefaults.DefaultBackgroundDrawableDark
                        } else {
                            MapPropertiesDefaults.DefaultBackgroundDrawableLight
                        },
                    )
                }
            }
            NaverMap(
                properties = mapProperties,
            ) {

                MARKER_COORDS.forEach {
                    Marker(
                        state = rememberMarkerState(position = it),
                        icon = if (isNightModeEnabled) {
                            MarkerIcons.GRAY
                        } else {
                            MarkerDefaults.DefaultIcon
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckedTextView(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .toggleable(
                value = checked,
                onValueChange = { onCheckedChange(it) },
                role = Role.Checkbox
            )
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1.merge(),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

private val MARKER_COORDS = arrayOf(
    LatLng(37.5666102, 126.9783881),
    LatLng(37.57000, 126.97618),
    LatLng(37.56138, 126.97970),
)
