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
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.overlay.PolylineOverlay.LineCap
import com.naver.maps.map.overlay.PolylineOverlay.LineJoin

internal class PolylineOverlayNode(
    val overlay: PolylineOverlay,
    var onPolylineOverlayClick: (PolylineOverlay) -> Boolean,
    var density: Density,
) : MapNode {
    override fun onRemoved() {
        overlay.map = null
    }
}

public object PolylineOverlayDefaults {
    public const val GlobalZIndex: Int = PolylineOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * A composable for a polyline on the map.
 *
 * @param coords the points comprising the polyline
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
    coords: List<LatLng>,
    color: Color = Color.Black,
    capType: LineCap = LineCap.Round,
    joinType: LineJoin = LineJoin.Miter,
    pattern: Array<Dp> = emptyArray(),
    width: Dp = 10.dp,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapDefaults.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapDefaults.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = PolygonOverlayDefaults.GlobalZIndex,
    onClick: (PolylineOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<PolylineOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding PolylineOverlay")
            val overlay = PolylineOverlay().apply {
                this.coords = coords
                this.color = color.toArgb()
                this.capType = capType
                this.joinType = joinType
                this.setPattern(*pattern.map { with(density) { it.roundToPx() } }.toIntArray())
                this.width = with(density) { width.roundToPx() }

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
                    .nodeForPolylineOverlay(overlay)
                    ?.onPolylineOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            PolylineOverlayNode(overlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onPolylineOverlayClick = it }

            set(coords) { this.overlay.coords = it }
            set(color) { this.overlay.color = it.toArgb() }
            set(capType) { this.overlay.capType = it }
            set(joinType) { this.overlay.joinType = it }
            set(pattern) {
                this.overlay.setPattern(
                    *it.map { with(this.density) { it.roundToPx() } }.toIntArray()
                )
            }
            set(width) {
                this.overlay.width = with(this.density) { it.roundToPx() }
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
