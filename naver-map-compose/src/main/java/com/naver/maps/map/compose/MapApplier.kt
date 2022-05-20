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
package com.naver.maps.map.compose

import androidx.compose.runtime.AbstractApplier
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.ArrowheadPathOverlay
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.MultipartPathOverlay
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.overlay.PolygonOverlay
import com.naver.maps.map.overlay.PolylineOverlay

internal interface MapNode {
    fun onAttached() {}
    fun onRemoved() {}
    fun onCleared() {}
}

private object MapNodeRoot : MapNode

internal class MapApplier(
    val map: NaverMap,
) : AbstractApplier<MapNode>(MapNodeRoot) {

    private val decorations = mutableListOf<MapNode>()

    override fun onClear() {
        decorations.forEach { it.onCleared() }
        decorations.clear()
    }

    override fun insertBottomUp(index: Int, instance: MapNode) {
        decorations.add(index, instance)
        instance.onAttached()
    }

    override fun insertTopDown(index: Int, instance: MapNode) {
        // insertBottomUp is preferred
    }

    override fun move(from: Int, to: Int, count: Int) {
        decorations.move(from, to, count)
    }

    override fun remove(index: Int, count: Int) {
        repeat(count) {
            decorations[index + it].onRemoved()
        }
        decorations.remove(index, count)
    }

    internal fun nodeForCircleOverlay(
        circleOverlay: CircleOverlay,
    ): CircleOverlayNode? {
        return decorations.firstOrNull {
            it is CircleOverlayNode && it.circleOverlay == circleOverlay
        } as? CircleOverlayNode
    }

    internal fun nodeForPolygonOverlay(
        polygonOverlay: PolygonOverlay,
    ): PolygonOverlayNode? {
        return decorations.firstOrNull {
            it is PolygonOverlayNode && it.polygonOverlay == polygonOverlay
        } as? PolygonOverlayNode
    }

    internal fun nodeForPolylineOverlay(
        polylineOverlay: PolylineOverlay,
    ): PolylineOverlayNode? {
        return decorations.firstOrNull {
            it is PolylineOverlayNode && it.polylineOverlay == polylineOverlay
        } as? PolylineOverlayNode
    }

    internal fun nodeForPathOverlay(
        pathOverlay: PathOverlay,
    ): PathOverlayNode? {
        return decorations.firstOrNull {
            it is PathOverlayNode && it.pathOverlay == pathOverlay
        } as? PathOverlayNode
    }

    internal fun nodeForMultipartPathOverlay(
        multipartPathOverlay: MultipartPathOverlay,
    ): MultipartPathOverlayNode? {
        return decorations.firstOrNull {
            it is MultipartPathOverlayNode && it.multipartPathOverlay == multipartPathOverlay
        } as? MultipartPathOverlayNode
    }

    internal fun nodeForArrowheadPathOverlay(
        arrowheadPathOverlay: ArrowheadPathOverlay,
    ): ArrowheadPathOverlayNode? {
        return decorations.firstOrNull {
            it is ArrowheadPathOverlayNode && it.arrowheadPathOverlay == arrowheadPathOverlay
        } as? ArrowheadPathOverlayNode
    }

    internal fun nodeForGroundOverlay(
        groundOverlay: GroundOverlay,
    ): GroundOverlayNode? {
        return decorations.firstOrNull {
            it is GroundOverlayNode && it.groundOverlay == groundOverlay
        } as? GroundOverlayNode
    }

    internal fun nodeForMarker(
        marker: Marker,
    ): MarkerNode? {
        return decorations.firstOrNull {
            it is MarkerNode && it.marker == marker
        } as? MarkerNode
    }
}
