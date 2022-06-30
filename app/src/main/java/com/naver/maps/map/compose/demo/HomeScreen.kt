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
package com.naver.maps.map.compose.demo

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.compose.demo.common.showToast

private data class Demo(
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
    val destination: Destination,
)

private data class Category(
    @StringRes
    val titleResId: Int,
    val items: List<Demo>,
) {
    companion object {
        val items: List<Category> = listOf(
            Category(
                titleResId = R.string.category_basic,
                items = listOf(
                    Demo(
                        titleResId = R.string.name_map,
                        descriptionResId = R.string.description_map,
                        destination = Destination.Map
                    ),
                    Demo(
                        titleResId = R.string.name_map_in_column,
                        descriptionResId = R.string.description_map_in_column,
                        destination = Destination.MapInColumn
                    ),
                    Demo(
                        titleResId = R.string.name_map_clustering,
                        descriptionResId = R.string.description_map_clustering,
                        destination = Destination.MapClustering
                    )
                )
            ),
            Category(
                titleResId = R.string.category_overlay,
                items = listOf(
                    Demo(
                        titleResId = R.string.name_marker,
                        descriptionResId = R.string.description_marker,
                        destination = Destination.Marker
                    ),
                    Demo(
                        titleResId = R.string.name_polygon_overlay,
                        descriptionResId = R.string.description_polygon_overlay,
                        destination = Destination.PolygonOverlay
                    ),
                    Demo(
                        titleResId = R.string.name_polyline_overlay,
                        descriptionResId = R.string.description_polyline_overlay,
                        destination = Destination.PolylineOverlay
                    ),
                    Demo(
                        titleResId = R.string.name_circle_overlay,
                        descriptionResId = R.string.description_circle_overlay,
                        destination = Destination.CircleOverlay
                    ),
                    Demo(
                        titleResId = R.string.name_ground_overlay,
                        descriptionResId = R.string.description_ground_overlay,
                        destination = Destination.GroundOverlay
                    ),
                    Demo(
                        titleResId = R.string.name_path_overlay,
                        descriptionResId = R.string.description_path_overlay,
                        destination = Destination.PathOverlay
                    ),
                    Demo(
                        titleResId = R.string.name_multipart_path_overlay,
                        descriptionResId = R.string.description_multipart_path_overlay,
                        destination = Destination.MultipartPathOverlay
                    ),
                    Demo(
                        titleResId = R.string.name_arrowhead_path_overlay,
                        descriptionResId = R.string.description_arrowhead_path_overlay,
                        destination = Destination.ArrowheadPathOverlay
                    ),
                    Demo(
                        titleResId = R.string.name_overlay_min_max_zoom,
                        descriptionResId = R.string.description_overlay_min_max_zoom,
                        destination = Destination.OverlayMinMaxZoom
                    ),
                    Demo(
                        titleResId = R.string.name_global_z_index,
                        descriptionResId = R.string.description_global_z_index,
                        destination = Destination.GlobalZIndex
                    ),
                    Demo(
                        titleResId = R.string.name_overlay_collision,
                        descriptionResId = R.string.description_overlay_collision,
                        destination = Destination.OverlayCollision
                    ),
                )
            ),
            Category(
                titleResId = R.string.category_camera,
                items = listOf(
                    Demo(
                        titleResId = R.string.name_camera_move,
                        descriptionResId = R.string.description_camera_move,
                        destination = Destination.CameraMove
                    ),
                    Demo(
                        titleResId = R.string.name_camera_animation,
                        descriptionResId = R.string.description_camera_animation,
                        destination = Destination.CameraAnimation
                    ),
                    Demo(
                        titleResId = R.string.name_camera_update_params,
                        descriptionResId = R.string.description_camera_update_params,
                        destination = Destination.CameraUpdateParams
                    ),
                    Demo(
                        titleResId = R.string.name_fit_bounds,
                        descriptionResId = R.string.description_fit_bounds,
                        destination = Destination.FitBounds
                    ),
                    Demo(
                        titleResId = R.string.name_pivot,
                        descriptionResId = R.string.description_pivot,
                        destination = Destination.Pivot
                    ),
                    Demo(
                        titleResId = R.string.name_camera_event,
                        descriptionResId = R.string.description_camera_event,
                        destination = Destination.CameraEvent
                    ),
                )
            ),
            Category(
                titleResId = R.string.category_map,
                items = listOf(
                    Demo(
                        titleResId = R.string.name_map_types_and_layer_groups,
                        descriptionResId = R.string.description_map_types_and_layer_groups,
                        destination = Destination.MapTypesAndLayerGroups
                    ),
                    Demo(
                        titleResId = R.string.name_display_options,
                        descriptionResId = R.string.description_display_options,
                        destination = Destination.DisplayOptions
                    ),
                    Demo(
                        titleResId = R.string.name_indoor_map,
                        descriptionResId = R.string.description_indoor_map,
                        destination = Destination.IndoorMap
                    ),
                    Demo(
                        titleResId = R.string.name_lite_mode,
                        descriptionResId = R.string.description_lite_mode,
                        destination = Destination.LiteMode
                    ),
                    Demo(
                        titleResId = R.string.name_night_mode,
                        descriptionResId = R.string.description_night_mode,
                        destination = Destination.NightMode
                    ),
                    Demo(
                        titleResId = R.string.name_locale,
                        descriptionResId = R.string.description_locale,
                        destination = Destination.Locale
                    ),
                )
            ),
            Category(
                titleResId = R.string.category_option,
                items = listOf(
                    Demo(
                        titleResId = R.string.name_min_max_zoom,
                        descriptionResId = R.string.description_min_max_zoom,
                        destination = Destination.MinMaxZoom
                    ),
                    Demo(
                        titleResId = R.string.name_max_tilt,
                        descriptionResId = R.string.description_max_tilt,
                        destination = Destination.MaxTilt
                    ),
                    Demo(
                        titleResId = R.string.name_extent,
                        descriptionResId = R.string.description_extent,
                        destination = Destination.Extent
                    ),
                    Demo(
                        titleResId = R.string.name_content_padding,
                        descriptionResId = R.string.description_content_padding,
                        destination = Destination.ContentPadding
                    ),
                    Demo(
                        titleResId = R.string.name_control_settings,
                        descriptionResId = R.string.description_control_settings,
                        destination = Destination.ControlSettings
                    ),
                    Demo(
                        titleResId = R.string.name_gesture_settings,
                        descriptionResId = R.string.description_gesture_settings,
                        destination = Destination.GestureSettings
                    ),
                )
            ),
            Category(
                titleResId = R.string.category_event,
                items = listOf(
                    Demo(
                        titleResId = R.string.name_map_click_event,
                        descriptionResId = R.string.description_map_click_event,
                        destination = Destination.MapClickEvent
                    ),
                    Demo(
                        titleResId = R.string.name_overlay_click_event,
                        descriptionResId = R.string.description_overlay_click_event,
                        destination = Destination.OverlayClickEvent
                    ),
                    Demo(
                        titleResId = R.string.name_symbol_click_event,
                        descriptionResId = R.string.description_symbol_click_event,
                        destination = Destination.SymbolClickEvent
                    ),
                    Demo(
                        titleResId = R.string.name_zoom_gestures_event,
                        descriptionResId = R.string.description_zoom_gestures_event,
                        destination = Destination.ZoomGesturesEvent
                    ),
                )
            ),
            Category(
                titleResId = R.string.category_location,
                items = listOf(
                    Demo(
                        titleResId = R.string.name_location_tracking,
                        descriptionResId = R.string.description_location_tracking,
                        destination = Destination.LocationTracking
                    ),
                    Demo(
                        titleResId = R.string.name_custom_location_source,
                        descriptionResId = R.string.description_custom_location_source,
                        destination = Destination.CustomLocationSource
                    ),
                )
            ),
        )
    }
}

@Composable
fun HomeScreen(onItemClick: (Destination) -> Unit) {
    var openDropdownMenu by remember { mutableStateOf(false) }
    var openInfoDialog by remember { mutableStateOf(false) }
    Box {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.app_name)) },
                    actions = {
                        IconButton(onClick = { openDropdownMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = null)
                        }
                    }
                )
            }
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding
            ) {
                Category.items.forEachIndexed { index, category ->
                    item(
                        key = index,
                        contentType = { "Category" }
                    ) {
                        HomeCategoryItem(title = stringResource(category.titleResId))
                    }
                    items(
                        items = category.items,
                        key = { it.destination.route },
                        contentType = { "Demo" }
                    ) { demo ->
                        HomeDemoItem(demo, onItemClick = onItemClick)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopEnd)
                .padding(end = 4.dp)
        ) {
            DropdownMenu(
                expanded = openDropdownMenu,
                onDismissRequest = { openDropdownMenu = false }
            ) {
                val context = LocalContext.current
                DropdownMenuItem(onClick = {
                    NaverMapSdk.getInstance(context).flushCache {
                        context.showToast(R.string.cache_flushed)
                    }
                }) {
                    Text(stringResource(R.string.flush_cache))
                }
                DropdownMenuItem(onClick = {
                    openDropdownMenu = false
                    openInfoDialog = true
                }) {
                    Text(stringResource(R.string.info))
                }
            }
        }

        if (openInfoDialog) {
            AlertDialog(
                onDismissRequest = {
                    openInfoDialog = false
                },
                title = {
                    Text(text = stringResource(R.string.sdk_info_title))
                },
                text = {
                    Text(
                        text = stringResource(
                            R.string.sdk_info_body_format,
                            BuildConfig.VERSION_NAME
                        )
                    )
                },
                buttons = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colors.onSurface
                            ),
                            onClick = {
                                openInfoDialog = false
                            }
                        ) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeCategoryItem(
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Divider(color = Color(0xFFE0E0E0))
        Text(
            text = title.uppercase(),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color(0xFF606060),
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun HomeDemoItem(
    demo: Demo,
    onItemClick: (Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable {
            onItemClick(demo.destination)
        }
    ) {
        Text(
            text = stringResource(demo.titleResId),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.Black,
            fontSize = 16.sp,
        )
        Text(
            text = stringResource(demo.descriptionResId),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.Gray,
            fontSize = 14.sp,
        )
    }
}
