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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.daum.mf.map.api.MapView
import soup.korea.map.compose.demo.R

private data class Demo(
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
    val destination: Destination,
) {
    companion object {
        val items: List<Demo> = listOf(
            Demo(
                titleResId = R.string.map_view_demo_title,
                descriptionResId = R.string.map_view_demo_desc,
                destination = Destination.Map
            ),
            Demo(
                titleResId = R.string.marker_demo_title,
                descriptionResId = R.string.marker_demo_desc,
                destination = Destination.Marker
            ),
            Demo(
                titleResId = R.string.polygon_demo_title,
                descriptionResId = R.string.polygon_demo_desc,
                destination = Destination.Polygon
            ),
            Demo(
                titleResId = R.string.location_demo_title,
                descriptionResId = R.string.location_demo_desc,
                destination = Destination.Location
            ),
            Demo(
                titleResId = R.string.camera_demo_title,
                descriptionResId = R.string.camera_demo_desc,
                destination = Destination.Camera
            ),
            Demo(
                titleResId = R.string.events_demo_title,
                descriptionResId = R.string.events_demo_desc,
                destination = Destination.Events
            ),
        )
    }
}

@Composable
fun HomeScreen(onItemClick: (Destination) -> Unit) {
    var openDropdownMenu by remember { mutableStateOf(false) }
    Box {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.daum_map_demo_title)) },
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
                items(
                    items = Demo.items,
                    key = { it.destination.route }
                ) { demo ->
                    HomeDemoItem(demo, onItemClick = onItemClick)
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
                DropdownMenuItem(onClick = {
                    MapView.clearMapTilePersistentCache()
                }) {
                    Text("Clear Map Tile Cache")
                }
            }
        }
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
