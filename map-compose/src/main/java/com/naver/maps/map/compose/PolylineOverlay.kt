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
    val polylineOverlay: PolylineOverlay,
    var onPolylineOverlayClick: (PolylineOverlay) -> Boolean,
    var density: Density,
) : MapNode {
    override fun onRemoved() {
        polylineOverlay.remove()
    }
}

public object PolylineOverlayDefaults {
    public const val DefaultGlobalZIndex: Int = PolylineOverlay.DEFAULT_GLOBAL_Z_INDEX
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
    tag: Any? = null,
    visible: Boolean = true,
    width: Dp = 10.dp,
    zIndex: Int = 0,
    globalZIndex: Int = PolygonOverlayDefaults.DefaultGlobalZIndex,
    onClick: (PolylineOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<PolylineOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding Polyline")
            val polylineOverlay = PolylineOverlay().apply {
                this.coords = coords
                this.color = color.toArgb()
                this.capType = capType
                this.joinType = joinType
                this.setPattern(*pattern.map { with(density) { it.roundToPx() } }.toIntArray())
                this.isVisible = visible
                this.width = with(density) { width.roundToPx() }
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
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
            PolylineOverlayNode(polylineOverlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onPolylineOverlayClick = it }

            set(coords) { this.polylineOverlay.coords = it }
            set(color) { this.polylineOverlay.color = it.toArgb() }
            set(capType) { this.polylineOverlay.capType = it }
            set(joinType) { this.polylineOverlay.joinType = it }
            set(pattern) {
                this.polylineOverlay.setPattern(
                    *it.map { with(density) { it.roundToPx() } }.toIntArray()
                )
            }
            set(tag) { this.polylineOverlay.tag = it }
            set(visible) { this.polylineOverlay.isVisible = it }
            set(width) {
                this.polylineOverlay.width = with(this.density) { it.roundToPx() }
            }
            set(zIndex) { this.polylineOverlay.zIndex = it }
            set(globalZIndex) { this.polylineOverlay.globalZIndex = it }
        }
    )
}
