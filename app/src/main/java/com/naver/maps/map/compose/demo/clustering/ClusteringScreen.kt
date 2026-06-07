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
package com.naver.maps.map.compose.demo.clustering

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.Clustering
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MapConstants
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun SimpleClusteringScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_clustering),
                upPress = upPress,
            )
        },
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            SimpleClustering()
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun SimpleClustering() {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(MapConstants.EXTENT_KOREA.center, 4.0)
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    NaverMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        var clusterItems by remember { mutableStateOf(emptyMap<ItemKey, Int>()) }
        LaunchedEffect(Unit) {
            val keyTagMap = buildMap(5000) {
                val south = MapConstants.EXTENT_KOREA.southLatitude
                val west = MapConstants.EXTENT_KOREA.westLongitude
                val height = MapConstants.EXTENT_KOREA.northLatitude - south
                val width = MapConstants.EXTENT_KOREA.eastLongitude - west

                repeat(5000) { i ->
                    put(
                        ItemKey(
                            i,
                            LatLng(height * Math.random() + south, width * Math.random() + west),
                        ),
                        (Math.random() * ICONS.size).toInt(),
                    )
                }
            }
            clusterItems = keyTagMap
        }

        Clustering(
            items = clusterItems,
            clusterContent = {
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.Yellow, RoundedCornerShape(8.dp))
                ) {
                    Text("Cluster(${it.size})")
                }
            },
            leafContent = {
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.Cyan, RoundedCornerShape(8.dp))
                ) {
                    Text("Leaf(${it.tag})")
                }
            },
            onClickCluster = { info, _ ->
                scope.launch {
                    cameraPositionState.animate(
                        CameraUpdate.toCameraPosition(
                            CameraPosition(
                                info.position,
                                cameraPositionState.position.zoom + 2.0
                            )
                        )
                    )
                }
                true
            },
            onClickLeaf = { info, _ ->
                Toast.makeText(context, "Clicked! ${info.tag}", Toast.LENGTH_SHORT).show()
                true
            }
        )
    }
}

private val ICONS =
    arrayOf(Marker.DEFAULT_ICON, MarkerIcons.BLUE, MarkerIcons.RED, MarkerIcons.YELLOW)
