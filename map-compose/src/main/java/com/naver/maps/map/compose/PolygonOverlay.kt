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
import com.naver.maps.map.overlay.PolygonOverlay

internal class PolygonOverlayNode(
    val polygonOverlay: PolygonOverlay,
    var onPolygonOverlayClick: (PolygonOverlay) -> Boolean
) : MapNode {
    override fun onRemoved() {
        polygonOverlay.remove()
    }
}

/**
 * A composable for a polygon overlay on the map.
 *
 * @param points the points comprising the vertices of the polygon
 * @param fillColor the fill color of the polygon
 * @param holes the holes for the polygon
 * @param strokeColor the stroke color of the polygon
 * @param strokeWidth specifies the polygon's stroke width, in display pixels
 * @param tag optional tag to associate wiht the polygon
 * @param visible the visibility of the polygon
 * @param zIndex the z-index of the polygon
 * @param onClick a lambda invoked when the polygon is clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun PolygonOverlay(
    points: List<LatLng>,
    fillColor: Color = Color.Black,
    holes: List<List<LatLng>> = emptyList(),
    strokeColor: Color = Color.Black,
    strokeWidth: Int = 10,
    tag: Any? = null,
    visible: Boolean = true,
    zIndex: Int = 0,
    onClick: (PolygonOverlay) -> Boolean = { false }
) {
    val mapApplier = currentComposer.applier as MapApplier?
    ComposeNode<PolygonOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding polygon")
            val polygonOverlay = PolygonOverlay().apply {
                this.coords = points
                this.color = fillColor.toArgb()
                this.holes = holes
                this.outlineColor = strokeColor.toArgb()
                this.outlineWidth = strokeWidth
                this.isVisible = visible
                this.zIndex = zIndex
            }
            polygonOverlay.tag = tag
            polygonOverlay.map = map
            polygonOverlay.setOnClickListener {
                mapApplier
                    .nodeForPolygonOverlay(polygonOverlay)
                    ?.onPolygonOverlayClick
                    ?.invoke(polygonOverlay)
                    ?: false
            }
            PolygonOverlayNode(polygonOverlay, onClick)
        },
        update = {
            update(onClick) { this.onPolygonOverlayClick = it }

            set(points) { this.polygonOverlay.coords = it }
            set(fillColor) { this.polygonOverlay.color = it.toArgb() }
            set(holes) { this.polygonOverlay.holes = it }
            set(strokeColor) { this.polygonOverlay.outlineColor = it.toArgb() }
            set(strokeWidth) { this.polygonOverlay.outlineWidth = it }
            set(tag) { this.polygonOverlay.tag = it }
            set(visible) { this.polygonOverlay.isVisible = it }
            set(zIndex) { this.polygonOverlay.zIndex = it }
        }
    )
}
