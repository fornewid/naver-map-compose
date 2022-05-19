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
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun GestureSettingsScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_gesture_settings),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            val isScrollGesturesEnabled = remember { mutableStateOf(true) }
            val isZoomGesturesEnabled = remember { mutableStateOf(true) }
            val isTiltGesturesEnabled = remember { mutableStateOf(true) }
            val isRotateGesturesEnabled = remember { mutableStateOf(true) }
            val isStopGesturesEnabled = remember { mutableStateOf(true) }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                CheckedText(
                    text = stringResource(R.string.scroll),
                    checked = isScrollGesturesEnabled.value,
                    onCheckedChange = { isScrollGesturesEnabled.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.zoom),
                    checked = isZoomGesturesEnabled.value,
                    onCheckedChange = { isZoomGesturesEnabled.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.tilt),
                    checked = isTiltGesturesEnabled.value,
                    onCheckedChange = { isTiltGesturesEnabled.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.rotate),
                    checked = isRotateGesturesEnabled.value,
                    onCheckedChange = { isRotateGesturesEnabled.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.stop),
                    checked = isStopGesturesEnabled.value,
                    onCheckedChange = { isStopGesturesEnabled.value = it }
                )
            }

            NaverMap(
                properties = MapProperties(isIndoorEnabled = true),
                uiSettings = MapUiSettings(
                    scrollGesturesEnabled = isScrollGesturesEnabled.value,
                    zoomGesturesEnabled = isZoomGesturesEnabled.value,
                    tiltGesturesEnabled = isTiltGesturesEnabled.value,
                    rotateGesturesEnabled = isRotateGesturesEnabled.value,
                    stopGesturesEnabled = isStopGesturesEnabled.value,
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
