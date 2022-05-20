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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.CheckedText
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar

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
                    onCheckedChange = { isScrollGesturesEnabled.value = it },
                    modifier = Modifier.wrapContentWidth()
                )
                CheckedText(
                    text = stringResource(R.string.zoom),
                    checked = isZoomGesturesEnabled.value,
                    onCheckedChange = { isZoomGesturesEnabled.value = it },
                    modifier = Modifier.wrapContentWidth()
                )
                CheckedText(
                    text = stringResource(R.string.tilt),
                    checked = isTiltGesturesEnabled.value,
                    onCheckedChange = { isTiltGesturesEnabled.value = it },
                    modifier = Modifier.wrapContentWidth()
                )
                CheckedText(
                    text = stringResource(R.string.rotate),
                    checked = isRotateGesturesEnabled.value,
                    onCheckedChange = { isRotateGesturesEnabled.value = it },
                    modifier = Modifier.wrapContentWidth()
                )
                CheckedText(
                    text = stringResource(R.string.stop),
                    checked = isStopGesturesEnabled.value,
                    onCheckedChange = { isStopGesturesEnabled.value = it },
                    modifier = Modifier.wrapContentWidth()
                )
            }

            NaverMap(
                properties = MapProperties(isIndoorEnabled = true),
                uiSettings = MapUiSettings(
                    isScrollGesturesEnabled = isScrollGesturesEnabled.value,
                    isZoomGesturesEnabled = isZoomGesturesEnabled.value,
                    isTiltGesturesEnabled = isTiltGesturesEnabled.value,
                    isRotateGesturesEnabled = isRotateGesturesEnabled.value,
                    isStopGesturesEnabled = isStopGesturesEnabled.value,
                )
            )
        }
    }
}
