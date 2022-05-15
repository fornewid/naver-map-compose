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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.overlay.PolylineOverlay.LineCap
import com.naver.maps.map.overlay.PolylineOverlay.LineJoin

internal class PolylineOverlayNode(
    val polylineOverlay: PolylineOverlay,
    var onPolylineOverlayClick: (PolylineOverlay) -> Boolean
) : MapNode {
    override fun onRemoved() {
        polylineOverlay.remove()
    }
}

/**
 * A composable for a polyline on the map.
 *
 * @param points the points comprising the polyline
 * @param color the color of the polyline
 * @param capType the cap type for all vertices of the polyline
 * @param joinType the join type for all vertices of the polyline except the start and end vertices
 * @param pattern the pattern for the polyline
 * @param visible the visibility of the polyline
 * @param width the width of the polyline in screen pixels
 * @param zIndex the z-index of the polyline
 * @param onClick a lambda invoked when the polyline is clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun PolylineOverlay(
    points: List<LatLng>,
    color: Color = Color.Black,
    capType: LineCap = LineCap.Round,
    joinType: LineJoin = LineJoin.Miter,
    pattern: IntArray = intArrayOf(),
    tag: Any? = null,
    visible: Boolean = true,
    width: Int = 10,
    zIndex: Int = 0,
    onClick: (PolylineOverlay) -> Boolean = { false }
) {
    val mapApplier = currentComposer.applier as MapApplier?
    ComposeNode<PolylineOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding Polyline")
            val polylineOverlay = PolylineOverlay().apply {
                this.coords = points
                this.color = color.toArgb()
                this.capType = capType
                this.joinType = joinType
                this.setPattern(*pattern)
                this.isVisible = visible
                this.width = width
                this.zIndex = zIndex
            }
            polylineOverlay.tag = tag
            polylineOverlay.map = map
            polylineOverlay.setOnClickListener {
                mapApplier
                    .nodeForPolylineOverlay(polylineOverlay)
                    ?.onPolylineOverlayClick
                    ?.invoke(polylineOverlay)
                    ?: false
            }
            PolylineOverlayNode(polylineOverlay, onClick)
        },
        update = {
            update(onClick) { this.onPolylineOverlayClick = it }

            set(points) { this.polylineOverlay.coords = it }
            set(color) { this.polylineOverlay.color = it.toArgb() }
            set(capType) { this.polylineOverlay.capType = it }
            set(joinType) { this.polylineOverlay.joinType = it }
            set(pattern) { this.polylineOverlay.setPattern(*it) }
            set(tag) { this.polylineOverlay.tag = it }
            set(visible) { this.polylineOverlay.isVisible = it }
            set(width) { this.polylineOverlay.width = it }
            set(zIndex) { this.polylineOverlay.zIndex = it }
        }
    )
}
