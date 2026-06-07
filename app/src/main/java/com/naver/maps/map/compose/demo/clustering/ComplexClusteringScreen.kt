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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.clustering.DefaultDistanceStrategy
import com.naver.maps.map.clustering.Node
import com.naver.maps.map.compose.Clustering
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ComplexClusteringScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_complex_clustering),
                upPress = upPress,
            )
        },
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            ComplexClustering()

            Text(
                text = stringResource(id = R.string.seoul_toilet_data_copyright),
                fontSize = 12.sp,
                color = Color(0xFF333333),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(all = 8.dp),
            )
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun ComplexClustering() {
    val toiletLocationList = rememberToiletLocationList()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(NaverMapConstants.DefaultCameraPosition.target, 10.0)
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    NaverMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        val items = toiletLocationList.associate {
            ItemKey(it.id, LatLng(it.latitude, it.longitude)) to ItemData(
                it.name,
                it.gu,
            )
        }
        val defaultDistanceStrategy = remember { DefaultDistanceStrategy() }
        Clustering(
            items = items,
            clusterContent = {
                val backgroundColor = when {
                    it.minZoom <= 10 -> Color.Red
                    it.size < 10 -> Color.Blue
                    else -> Color.Green
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(backgroundColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${it.size}"
                    )
                }
            },
            leafContent = {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.Cyan, CircleShape)
                )
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
                Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show()
                true
            },
            updateClusterMarkerData = { info, marker ->
                if(info.size > 100) {
                    marker.isFlat = true
                    marker.angle = 45f
                } else {
                    marker.isFlat = false
                    marker.angle = 0f
                }
            },
            updateLeafMarkerData = { info, marker ->
                marker.isFlat = false
                marker.angle = 0f
            },
            minClusteringZoom = 9,
            maxClusteringZoom = 16,
            maxScreenDistance = 200.0,
            thresholdStrategy = { zoom ->
                if (zoom <= 11) {
                    0.0
                } else {
                    70.0
                }
            },
            distanceStrategy = { zoom: Int, node1: Node, node2: Node ->
                if (zoom <= 9) {
                    -1.0
                } else if ((node1.tag as ItemData).gu == (node2.tag as ItemData).gu) {
                    if (zoom <= 11) {
                        -1.0
                    } else {
                        defaultDistanceStrategy.getDistance(zoom, node1, node2)
                    }
                } else {
                    10000.0
                }
            },
            tagMergeStrategy = { cluster ->
                if (cluster.maxZoom <= 9) {
                    null
                } else {
                    ItemData("", (cluster.children.first().tag as ItemData).gu)
                }
            }
        )
    }
}

private class ItemData(val name: String, val gu: String)

@Composable
private fun rememberToiletLocationList(): List<ToiletLocation> {
    var toiletLocationList by remember { mutableStateOf<List<ToiletLocation>>(emptyList()) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        toiletLocationList = withContext(Dispatchers.IO) {
            buildList {
                context.assets.open(CSV_ASSET_PATH).bufferedReader().use { reader ->
                    var i = 0
                    reader.forEachLine { line ->
                        val split = line.split(",")
                        add(
                            ToiletLocation(
                                id = i++,
                                name = split[0],
                                gu = split[1],
                                longitude = split[2].toDouble(),
                                latitude = split[3].toDouble(),
                            ),
                        )
                    }
                }
            }
        }
    }
    return toiletLocationList
}

private class ToiletLocation(
    val id: Int,
    val name: String,
    val gu: String,
    val longitude: Double,
    val latitude: Double,
)

// 데이터 © 서울특별시 (CC BY)
// 서울 열린데이터 광장 - 서울시 공중화장실 위치정보
// http://data.seoul.go.kr/dataList/OA-1370/S/1/datasetView.do
// 2023.04.19.
private const val CSV_ASSET_PATH = "seoul_toilet.csv"
