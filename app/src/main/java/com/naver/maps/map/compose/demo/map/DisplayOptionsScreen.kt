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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LayerGroup
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapDefaults
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun DisplayOptionsScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_display_options),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            var lightnessProgress by remember { mutableStateOf(100f) }
            var buildingHeightProgress by remember { mutableStateOf(100f) }
            var symbolScaleProgress by remember { mutableStateOf(100f) }
            var symbolPerspectiveRatioProgress by remember { mutableStateOf(100f) }
            SliderItem(
                text = stringResource(R.string.label_map_lightness),
                value = lightnessProgress,
                onValueChange = { progress ->
                    lightnessProgress = progress
                },
                valueRange = 0f..200f,
                valuePercent = (lightnessProgress - 100) / 100,
            )
            SliderItem(
                text = stringResource(R.string.label_building_height),
                value = buildingHeightProgress,
                onValueChange = { progress ->
                    buildingHeightProgress = progress
                },
                valueRange = 0f..100f,
                valuePercent = buildingHeightProgress / 100,
            )
            SliderItem(
                text = stringResource(R.string.label_symbol_scale),
                value = symbolScaleProgress,
                onValueChange = { progress ->
                    symbolScaleProgress = progress
                },
                valueRange = 0f..200f,
                valuePercent = symbolScaleProgress / 100,
            )
            SliderItem(
                text = stringResource(R.string.label_symbol_perspective_ratio),
                value = symbolPerspectiveRatioProgress,
                onValueChange = { progress ->
                    symbolPerspectiveRatioProgress = progress
                },
                valueRange = 0f..100f,
                valuePercent = symbolPerspectiveRatioProgress / 100,
            )

            val mapProperties by remember {
                derivedStateOf {
                    MapProperties(
                        lightness = (lightnessProgress - 100) / 100,
                        buildingHeight = buildingHeightProgress / 100,
                        symbolScale = symbolScaleProgress / 100,
                        symbolPerspectiveRatio = symbolPerspectiveRatioProgress / 100,
                        enabledLayerGroupSet = hashSetOf(LayerGroup.Building)
                    )
                }
            }
            val cameraPositionState = rememberCameraPositionState {
                this.position = CameraPosition(
                    NaverMapDefaults.DefaultCameraPosition.target,
                    16.0,
                    40.0,
                    0.0
                )
            }
            NaverMap(
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SliderItem(
    text: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    valuePercent: Float,
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .requiredWidth(100.dp)
            )
            Slider(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                valueRange = valueRange
            )
            Text(
                text = stringResource(
                    R.string.format_display_option_value,
                    valuePercent
                ),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .requiredWidth(60.dp)
            )
        }
    }
}
