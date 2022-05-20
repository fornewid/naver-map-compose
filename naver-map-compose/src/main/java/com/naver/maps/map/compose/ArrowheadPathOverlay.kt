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
import com.naver.maps.map.overlay.ArrowheadPathOverlay

internal class ArrowheadPathOverlayNode(
    val overlay: ArrowheadPathOverlay,
    var onArrowheadPathOverlayClick: (ArrowheadPathOverlay) -> Boolean,
    var density: Density,
) : MapNode {
    override fun onRemoved() {
        overlay.map = null
    }
}

public object ArrowheadPathOverlayDefaults {
    public const val GlobalZIndex: Int = ArrowheadPathOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * A composable for a multipart path overlay on the map.
 *
 * @param coords the points comprising the path
 * @param color the color of the path
 * TODO: (sungyong.an) 설명 추가
 * @param visible the visibility of the path
 * @param width the width of the path in screen pixels
 * @param zIndex the z-index of the path
 * @param onClick a lambda invoked when the path is clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun ArrowheadPathOverlay(
    coords: List<LatLng>,
    color: Color = Color.Black,
    outlineColor: Color = Color.Black,
    outlineWidth: Dp = 10.dp,
    headSizeRatio: Float = 1f,
    elevation: Dp = 0.dp,
    width: Dp = 10.dp,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapDefaults.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapDefaults.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = ArrowheadPathOverlayDefaults.GlobalZIndex,
    onClick: (ArrowheadPathOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<ArrowheadPathOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding ArrowheadPathOverlay")
            val overlay = ArrowheadPathOverlay().apply {
                this.coords = coords
                this.color = color.toArgb()
                this.outlineColor = outlineColor.toArgb()
                this.outlineWidth = with(density) { outlineWidth.roundToPx() }
                this.headSizeRatio = headSizeRatio
                this.elevation = with(density) { elevation.roundToPx() }
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
                    .nodeForArrowheadPathOverlay(overlay)
                    ?.onArrowheadPathOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            ArrowheadPathOverlayNode(overlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onArrowheadPathOverlayClick = it }

            set(coords) { this.overlay.coords = it }
            set(color) { this.overlay.color = it.toArgb() }
            set(outlineColor) { this.overlay.outlineColor = it.toArgb() }
            set(outlineWidth) {
                this.overlay.outlineWidth = with(this.density) { it.roundToPx() }
            }
            set(headSizeRatio) { this.overlay.headSizeRatio = it }
            set(elevation) {
                this.overlay.elevation = with(this.density) { it.roundToPx() }
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
