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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PolygonOverlay

internal class PolygonOverlayNode(
    val overlay: PolygonOverlay,
    var onPolygonOverlayClick: (PolygonOverlay) -> Boolean,
    var density: Density,
) : MapNode {
    override fun onRemoved() {
        overlay.map = null
    }
}

public object PolygonOverlayDefaults {
    public const val GlobalZIndex: Int = PolygonOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * A composable for a polygon overlay on the map.
 *
 * @param coords the points comprising the vertices of the polygon
 * @param color the fill color of the polygon
 * @param holes the holes for the polygon
 * @param outlineColor the stroke color of the polygon
 * @param outlineWidth specifies the polygon's stroke width, in display pixels
 * @param tag optional tag to associate wiht the polygon
 * @param visible the visibility of the polygon
 * @param zIndex the z-index of the polygon
 * @param onClick a lambda invoked when the polygon is clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun PolygonOverlay(
    coords: List<LatLng>,
    color: Color = Color.Black,
    holes: List<List<LatLng>> = emptyList(),
    outlineColor: Color = Color.Black,
    outlineWidth: Dp = 10.dp,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapDefaults.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapDefaults.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = PolygonOverlayDefaults.GlobalZIndex,
    onClick: (PolygonOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<PolygonOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding PolygonOverlay")
            val overlay = PolygonOverlay().apply {
                this.coords = coords
                this.color = color.toArgb()
                this.holes = holes
                this.outlineColor = outlineColor.toArgb()
                this.outlineWidth = with(density) { outlineWidth.roundToPx() }

                // Overlay
                this.tag = tag
                this.isVisible = visible
                this.minZoom = minZoom
                this.isMinZoomInclusive = minZoomInclusive
                this.maxZoom = maxZoom
                this.isMaxZoomInclusive = maxZoomInclusive
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
            }
            overlay.map = map
            overlay.setOnClickListener {
                mapApplier
                    .nodeForPolygonOverlay(overlay)
                    ?.onPolygonOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            PolygonOverlayNode(overlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onPolygonOverlayClick = it }

            set(coords) { this.overlay.coords = it }
            set(color) { this.overlay.color = it.toArgb() }
            set(holes) { this.overlay.holes = it }
            set(outlineColor) { this.overlay.outlineColor = it.toArgb() }
            set(outlineWidth) {
                this.overlay.outlineWidth = with(this.density) { it.roundToPx() }
            }

            // Overlay
            set(tag) { this.overlay.tag = it }
            set(visible) { this.overlay.isVisible = it }
            set(minZoom) { this.overlay.minZoom = it }
            set(minZoomInclusive) { this.overlay.isMinZoomInclusive = it }
            set(maxZoom) { this.overlay.maxZoom = it }
            set(maxZoomInclusive) { this.overlay.isMaxZoomInclusive = it }
            set(zIndex) { this.overlay.zIndex = it }
            set(globalZIndex) { this.overlay.globalZIndex = it }
        }
    )
}
