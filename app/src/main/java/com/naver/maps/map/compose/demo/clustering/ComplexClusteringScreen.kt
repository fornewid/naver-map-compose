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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.DefaultClusterMarkerUpdater
import com.naver.maps.map.clustering.DefaultClusterOnClickListener
import com.naver.maps.map.clustering.DefaultDistanceStrategy
import com.naver.maps.map.clustering.DefaultMarkerManager
import com.naver.maps.map.clustering.DistanceStrategy
import com.naver.maps.map.clustering.Node
import com.naver.maps.map.compose.CameraPosition
import com.naver.maps.map.compose.DisposableMapEffect
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LatLng
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.Dispatchers
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
    NaverMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        var clusterManager by remember { mutableStateOf<Clusterer<ItemKey>?>(null) }
        DisposableMapEffect(toiletLocationList) { map ->
            if (clusterManager == null) {
                clusterManager = Clusterer.ComplexBuilder<ItemKey>()
                    .minClusteringZoom(9)
                    .maxClusteringZoom(16)
                    .maxScreenDistance(200.0)
                    .thresholdStrategy { zoom ->
                        if (zoom <= 11) {
                            0.0
                        } else {
                            70.0
                        }
                    }
                    .distanceStrategy(object : DistanceStrategy {
                        private val defaultDistanceStrategy = DefaultDistanceStrategy()

                        override fun getDistance(zoom: Int, node1: Node, node2: Node): Double {
                            return if (zoom <= 9) {
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
                        }
                    })
                    .tagMergeStrategy { cluster ->
                        if (cluster.maxZoom <= 9) {
                            null
                        } else {
                            ItemData("", (cluster.children.first().tag as ItemData).gu)
                        }
                    }
                    .markerManager(object : DefaultMarkerManager() {
                        override fun createMarker() = super.createMarker().apply {
                            subCaptionTextSize = 10f
                            subCaptionColor = android.graphics.Color.WHITE
                            subCaptionHaloColor = android.graphics.Color.TRANSPARENT
                        }
                    })
                    .clusterMarkerUpdater { info, marker ->
                        val size = info.size
                        marker.icon = when {
                            info.minZoom <= 10 -> MarkerIcons.CLUSTER_HIGH_DENSITY
                            size < 10 -> MarkerIcons.CLUSTER_LOW_DENSITY
                            else -> MarkerIcons.CLUSTER_MEDIUM_DENSITY
                        }
                        marker.subCaptionText =
                            if (info.minZoom == 10) {
                                (info.tag as ItemData).gu
                            } else {
                                ""
                            }
                        marker.anchor = DefaultClusterMarkerUpdater.DEFAULT_CLUSTER_ANCHOR
                        marker.captionText = size.toString()
                        marker.setCaptionAligns(Align.Center)
                        marker.captionColor = android.graphics.Color.WHITE
                        marker.captionHaloColor = android.graphics.Color.TRANSPARENT
                        marker.onClickListener = DefaultClusterOnClickListener(info)
                    }
                    .leafMarkerUpdater { info, marker ->
                        marker.icon = Marker.DEFAULT_ICON
                        marker.anchor = Marker.DEFAULT_ANCHOR
                        marker.captionText = (info.tag as ItemData).name
                        marker.setCaptionAligns(Align.Bottom)
                        marker.captionColor = android.graphics.Color.BLACK
                        marker.captionHaloColor = android.graphics.Color.WHITE
                        marker.subCaptionText = ""
                        marker.onClickListener = null
                    }
                    .build()
                    .apply { this.map = map }
            }
            val keyTagMap = toiletLocationList.associate {
                ItemKey(it.id, LatLng(it.latitude, it.longitude)) to ItemData(
                    it.name,
                    it.gu,
                )
            }
            clusterManager?.addAll(keyTagMap)
            onDispose {
                clusterManager?.clear()
            }
        }
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
