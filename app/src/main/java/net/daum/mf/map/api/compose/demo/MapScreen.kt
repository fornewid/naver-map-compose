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
package net.daum.mf.map.api.compose.demo

import android.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.daum.mf.map.api.MapPoint.GeoCoordinate
import net.daum.mf.map.api.compose.ExperimentalKakaoMapApi
import net.daum.mf.map.api.compose.KakaoMap
import net.daum.mf.map.api.compose.MapProperties
import net.daum.mf.map.api.compose.MapTileMode
import net.daum.mf.map.api.compose.MapType
import soup.korea.map.compose.demo.R
import soup.korea.map.compose.demo.common.DefaultTopAppBar

@OptIn(ExperimentalKakaoMapApi::class)
@Composable
fun MapScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.map_view_demo_title),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            val mapTypes = MapType.values()
            var selectedMapTypePosition by remember { mutableStateOf(0) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Map Type:",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .requiredWidth(80.dp)
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                )

                var expanded by remember { mutableStateOf(false) }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Row(
                        Modifier
                            .clickable { expanded = !expanded }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mapTypes[selectedMapTypePosition].name,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 8.dp).weight(1f)
                        )
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            mapTypes.forEachIndexed { index, mapType ->
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        selectedMapTypePosition = index
                                    }
                                ) {
                                    Text(text = mapType.name)
                                }
                            }
                        }
                    }
                }
            }

            val mapTileModes = MapTileMode.values()
            var selectedMapTileModePosition by remember { mutableStateOf(0) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Map Tile Mode:",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .requiredWidth(80.dp)
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                )

                var expanded by remember { mutableStateOf(false) }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Row(
                        Modifier
                            .clickable { expanded = !expanded }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mapTileModes[selectedMapTileModePosition].name,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 8.dp).weight(1f)
                        )
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            mapTileModes.forEachIndexed { index, mapTileMode ->
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        selectedMapTileModePosition = index
                                    }
                                ) {
                                    Text(text = mapTileMode.name)
                                }
                            }
                        }
                    }
                }
            }

            val context = LocalContext.current
            KakaoMap(
                properties = MapProperties(
                    mapType = mapTypes[selectedMapTypePosition],
                    mapTileMode = mapTileModes[selectedMapTileModePosition],
                ),
                onMapDoubleTapped = { mapPoint ->
                    val mapPointGeo = mapPoint.mapPointGeoCoord
                    AlertDialog.Builder(context)
                        .setTitle("DaumMapLibrarySample")
                        .setMessage(
                            String.format(
                                "Double-Tap on (%f,%f)",
                                mapPointGeo.latitude,
                                mapPointGeo.longitude
                            )
                        )
                        .setPositiveButton("OK", null)
                        .show()
                },
                onMapLongPressed = { mapPoint ->
                    val mapPointGeo: GeoCoordinate = mapPoint.mapPointGeoCoord
                    AlertDialog.Builder(context)
                        .setTitle("DaumMapLibrarySample")
                        .setMessage(
                            String.format(
                                "Long-Press on (%f,%f)",
                                mapPointGeo.latitude,
                                mapPointGeo.longitude
                            )
                        )
                        .setPositiveButton("OK", null)
                        .show()
                }
            )
        }
    }
}
