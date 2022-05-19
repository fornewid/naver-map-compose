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
package com.naver.maps.map.compose.demo.option

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.rememberCameraPositionState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ControlSettingsScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_control_settings),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            val isCompassEnabled = remember { mutableStateOf(true) }
            val isScaleBarEnabled = remember { mutableStateOf(true) }
            val isZoomControlEnabled = remember { mutableStateOf(true) }
            val isIndoorLevelPickerEnabled = remember { mutableStateOf(true) }
            val isLocationButtonEnabled = remember { mutableStateOf(true) }
            val isLogoClickEnabled = remember { mutableStateOf(true) }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                CheckedText(
                    text = stringResource(R.string.compass),
                    checked = isCompassEnabled.value,
                    onCheckedChange = { isCompassEnabled.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.scale_bar),
                    checked = isScaleBarEnabled.value,
                    onCheckedChange = { isScaleBarEnabled.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.zoom_control),
                    checked = isZoomControlEnabled.value,
                    onCheckedChange = { isZoomControlEnabled.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.indoor_level_picker),
                    checked = isIndoorLevelPickerEnabled.value,
                    onCheckedChange = { isIndoorLevelPickerEnabled.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.location_button),
                    checked = isLocationButtonEnabled.value,
                    onCheckedChange = { isLocationButtonEnabled.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.logo_click),
                    checked = isLogoClickEnabled.value,
                    onCheckedChange = { isLogoClickEnabled.value = it }
                )
            }

            val cameraPositionState = rememberCameraPositionState {
                this.position = CameraPosition(LatLng(37.5116620, 127.0594274), 16.0, 0.0, 90.0)
            }
            NaverMap(
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isIndoorEnabled = true),
                uiSettings = MapUiSettings(
                    compassEnabled = isCompassEnabled.value,
                    scaleBarEnabled = isScaleBarEnabled.value,
                    zoomControlEnabled = isZoomControlEnabled.value,
                    indoorLevelPickerEnabled = isIndoorLevelPickerEnabled.value,
                    locationButtonEnabled = isLocationButtonEnabled.value,
                    logoClickEnabled = isLogoClickEnabled.value,
                )
            )
        }
    }
}

@Composable
private fun CheckedText(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        Modifier
            .wrapContentWidth()
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
