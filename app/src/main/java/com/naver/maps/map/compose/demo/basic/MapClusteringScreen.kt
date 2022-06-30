package com.naver.maps.map.compose.demo.basic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapEffect
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng
import ted.gun0912.clustering.naver.TedNaverClustering
import kotlin.random.Random

@Composable
fun MapClusteringScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_map),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            NaverMapClustering()
        }
    }
}

@Composable
private fun NaverMapClustering() {
    val items = remember { mutableStateListOf<MyItem>() }
    LaunchedEffect(Unit) {
        repeat(100) {
            val position = LatLng(
                POSITION.latitude + Random.nextFloat(),
                POSITION.longitude + Random.nextFloat(),
            )
            items.add(MyItem(position, "Marker", "Snippet"))
        }
    }
    NaverMapClustering(items = items)
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun NaverMapClustering(items: List<MyItem>) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(POSITION, 6.0)
    }
    NaverMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        val context = LocalContext.current
        var clusterManager by remember { mutableStateOf<TedNaverClustering<MyItem>?>(null) }
        MapEffect(items) { map ->
            if (clusterManager == null) {
                clusterManager = TedNaverClustering.with<MyItem>(context, map).make()
            }
            clusterManager?.addItems(items)
        }
    }
}

private data class MyItem(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
) : TedClusterItem {

    override fun getTedLatLng(): TedLatLng {
        return TedLatLng(
            latitude = itemPosition.latitude,
            longitude = itemPosition.longitude,
        )
    }
}

private val POSITION = LatLng(37.5666102, 126.9783881)
