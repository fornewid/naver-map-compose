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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState

@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterialApi::class)
@Composable
fun MapTypesAndLayerGroupsScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_map_types_and_layer_groups),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            val mapTypes = stringArrayResource(R.array.map_types)
            var selectedMapTypePosition by remember { mutableStateOf(0) }

            var isBuildingLayerGroupEnabled by remember { mutableStateOf(true) }
            var isTransitLayerGroupEnabled by remember { mutableStateOf(false) }
            var isBicycleLayerGroupEnabled by remember { mutableStateOf(false) }
            var isTrafficLayerGroupEnabled by remember { mutableStateOf(false) }
            var isCadastralLayerGroupEnabled by remember { mutableStateOf(false) }
            var isMountainLayerGroupEnabled by remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.label_map_type),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .requiredWidth(80.dp)
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                )

                var mapTypeExpanded by remember { mutableStateOf(false) }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Row(
                        Modifier
                            .clickable { mapTypeExpanded = !mapTypeExpanded }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mapTypes[selectedMapTypePosition],
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 8.dp).weight(1f)
                        )
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )

                        DropdownMenu(
                            expanded = mapTypeExpanded,
                            onDismissRequest = { mapTypeExpanded = false }
                        ) {
                            mapTypes.forEachIndexed { index, mapType ->
                                DropdownMenuItem(
                                    onClick = {
                                        mapTypeExpanded = false
                                        selectedMapTypePosition = index
                                    }
                                ) {
                                    Text(text = mapType)
                                }
                            }
                        }
                    }
                }

                var layerGroupExpanded by remember { mutableStateOf(false) }
                Box(Modifier.wrapContentWidth(), contentAlignment = Alignment.Center) {
                    Row(
                        Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                            Button(
                                onClick = {
                                    layerGroupExpanded = !layerGroupExpanded
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.layer_groups),
                                    fontSize = 14.sp,
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = layerGroupExpanded,
                            onDismissRequest = { layerGroupExpanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    layerGroupExpanded = false
                                    isBuildingLayerGroupEnabled = !isBuildingLayerGroupEnabled
                                }
                            ) {
                                Text(text = stringResource(R.string.building))
                                Checkbox(
                                    checked = isBuildingLayerGroupEnabled,
                                    onCheckedChange = null
                                )
                            }
                            DropdownMenuItem(
                                onClick = {
                                    layerGroupExpanded = false
                                    isTransitLayerGroupEnabled = !isTransitLayerGroupEnabled
                                }
                            ) {
                                Text(text = stringResource(R.string.transit))
                                Checkbox(
                                    checked = isTransitLayerGroupEnabled,
                                    onCheckedChange = null
                                )
                            }
                            DropdownMenuItem(
                                onClick = {
                                    layerGroupExpanded = false
                                    isBicycleLayerGroupEnabled = !isBicycleLayerGroupEnabled
                                }
                            ) {
                                Text(text = stringResource(R.string.bicycle))
                                Checkbox(
                                    checked = isBicycleLayerGroupEnabled,
                                    onCheckedChange = null
                                )
                            }
                            DropdownMenuItem(
                                onClick = {
                                    layerGroupExpanded = false
                                    isTrafficLayerGroupEnabled = !isTrafficLayerGroupEnabled
                                }
                            ) {
                                Text(text = stringResource(R.string.traffic))
                                Checkbox(
                                    checked = isTrafficLayerGroupEnabled,
                                    onCheckedChange = null
                                )
                            }
                            DropdownMenuItem(
                                onClick = {
                                    layerGroupExpanded = false
                                    isCadastralLayerGroupEnabled = !isCadastralLayerGroupEnabled
                                }
                            ) {
                                Text(text = stringResource(R.string.cadastral))
                                Checkbox(
                                    checked = isCadastralLayerGroupEnabled,
                                    onCheckedChange = null
                                )
                            }
                            DropdownMenuItem(
                                onClick = {
                                    layerGroupExpanded = false
                                    isMountainLayerGroupEnabled = !isMountainLayerGroupEnabled
                                }
                            ) {
                                Text(text = stringResource(R.string.mountain))
                                Checkbox(
                                    checked = isMountainLayerGroupEnabled,
                                    onCheckedChange = null
                                )
                            }
                        }
                    }
                }
            }
            val cameraPositionState = rememberCameraPositionState {
                this.position = CameraPosition(
                    NaverMapConstants.DefaultCameraPosition.target,
                    16.0,
                    40.0,
                    0.0
                )
            }
            NaverMap(
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = MapType.valueOf(mapTypes[selectedMapTypePosition]),
                    isBuildingLayerGroupEnabled = isBuildingLayerGroupEnabled,
                    isTransitLayerGroupEnabled = isTransitLayerGroupEnabled,
                    isBicycleLayerGroupEnabled = isBicycleLayerGroupEnabled,
                    isTrafficLayerGroupEnabled = isTrafficLayerGroupEnabled,
                    isCadastralLayerGroupEnabled = isCadastralLayerGroupEnabled,
                    isMountainLayerGroupEnabled = isMountainLayerGroupEnabled,
                ),
            )
        }
    }
}
