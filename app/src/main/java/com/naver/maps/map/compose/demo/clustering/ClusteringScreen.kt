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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.ClusteringKey
import com.naver.maps.map.clustering.DefaultLeafMarkerUpdater
import com.naver.maps.map.clustering.LeafMarkerInfo
import com.naver.maps.map.compose.DisposableMapEffect
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.MapConstants
import com.naver.maps.map.util.MarkerIcons

@Composable
fun ClusteringScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_clustering),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Clustering()
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun Clustering() {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(MapConstants.EXTENT_KOREA.center, 4.0)
    }
    NaverMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        var clusterManager by remember { mutableStateOf<Clusterer<ItemKey>?>(null) }
        DisposableMapEffect(Unit) { map ->
            if (clusterManager == null) {
                clusterManager = Clusterer.Builder<ItemKey>()
                    .leafMarkerUpdater(object : DefaultLeafMarkerUpdater() {
                        override fun updateLeafMarker(info: LeafMarkerInfo, marker: Marker) {
                            super.updateLeafMarker(info, marker)
                            marker.icon = ICONS[info.tag as Int]
                            marker.onClickListener = Overlay.OnClickListener {
                                clusterManager?.remove(info.key as ItemKey)
                                true
                            }
                        }
                    })
                    .build()
                    .apply { this.map = map }
            }
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
            clusterManager?.addAll(keyTagMap)
            onDispose {
                clusterManager?.clear()
            }
        }
    }
}

private class ItemKey(
    val id: Int,
    private val position: LatLng,
) : ClusteringKey {

    override fun getPosition() = position

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val itemKey = other as ItemKey
        return id == itemKey.id
    }

    override fun hashCode() = id
}

private val ICONS = arrayOf(Marker.DEFAULT_ICON, MarkerIcons.BLUE, MarkerIcons.RED, MarkerIcons.YELLOW)
