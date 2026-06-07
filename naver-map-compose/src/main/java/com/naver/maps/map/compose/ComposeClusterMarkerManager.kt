package com.naver.maps.map.compose

import androidx.core.graphics.createBitmap
import com.naver.maps.map.clustering.MarkerInfo
import com.naver.maps.map.clustering.MarkerManager
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import java.io.Closeable

internal class ComposeClusterMarkerManager(
    poolSize: Int = DEFAULT_POOL_SIZE,
) : MarkerManager {
    private val markerPool = ArrayDeque<Marker>(poolSize)
    private val DEFAULT_BITMAP = createBitmap(10, 10)

    override fun retainMarker(markerInfo: MarkerInfo): Marker? {
        val marker =
            markerPool.removeFirstOrNull() ?: Marker(OverlayImage.fromBitmap(DEFAULT_BITMAP))
        return marker
    }

    override fun releaseMarker(
        markerInfo: MarkerInfo,
        marker: Marker,
    ) {
        val closeable = marker.tag as? Closeable
        closeable?.close()
        marker.tag = null
        marker.onClickListener = null

        markerPool.addLast(marker)
    }

    companion object {
        private const val DEFAULT_POOL_SIZE = 100
    }
}