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
import com.naver.maps.map.overlay.CircleOverlay

internal class CircleOverlayNode(
    val overlay: CircleOverlay,
    var onCircleOverlayClick: (CircleOverlay) -> Boolean,
    var density: Density
) : MapNode {
    override fun onRemoved() {
        overlay.map = null
    }
}

public object CircleOverlayDefaults {
    public const val GlobalZIndex: Int = CircleOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * A composable for a circle overlay on the map.
 *
 * @param center the [LatLng] to use for the center of this circle
 * @param color the fill color of the circle
 * @param radius the radius of the circle in meters.
 * @param outlineColor the outline color of the circle
 * @param tag optional tag to be associated with the circle
 * @param outlineWidth the width of the circle's outline in screen pixels
 * @param visible the visibility of the circle
 * @param zIndex the z-index of the circle
 * @param onClick a lambda invoked when the circle is clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun CircleOverlay(
    center: LatLng,
    color: Color = Color.Transparent,
    radius: Double = 0.0,
    outlineColor: Color = Color.Black,
    outlineWidth: Dp = 10.dp,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapDefaults.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapDefaults.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = CircleOverlayDefaults.GlobalZIndex,
    onClick: (CircleOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as? MapApplier
    val density = LocalDensity.current
    ComposeNode<CircleOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding CircleOverlay")
            val overlay = CircleOverlay().apply {
                this.center = center
                this.color = color.toArgb()
                this.radius = radius
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
                    .nodeForCircleOverlay(overlay)
                    ?.onCircleOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            CircleOverlayNode(overlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onCircleOverlayClick = it }

            set(center) { this.overlay.center = it }
            set(color) { this.overlay.color = it.toArgb() }
            set(radius) { this.overlay.radius = it }
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
