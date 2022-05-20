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
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay

internal class PathOverlayNode(
    val overlay: PathOverlay,
    var onPathOverlayClick: (PathOverlay) -> Boolean,
    var density: Density,
) : MapNode {
    override fun onRemoved() {
        overlay.map = null
    }
}

public object PathOverlayDefaults {
    public const val GlobalZIndex: Int = PathOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * A composable for a path overlay on the map.
 *
 * @param coords the coordinates comprising the path
 * @param progress the progress of the path
 * @param color the color of the path
 * TODO: (sungyong.an) 설명 추가
 * @param visible the visibility of the path
 * @param width the width of the path in screen pixels
 * @param zIndex the z-index of the path
 * @param onClick a lambda invoked when the path is clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun PathOverlay(
    coords: List<LatLng>,
    progress: Double = 0.0,
    color: Color = Color.Black,
    outlineColor: Color = Color.Black,
    outlineWidth: Dp = 10.dp,
    passedColor: Color = Color.Black,
    passedOutlineColor: Color = Color.Black,
    patternImage: OverlayImage? = null,
    patternInterval: Dp = 50.dp,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    width: Dp = 10.dp,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapDefaults.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapDefaults.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = PathOverlayDefaults.GlobalZIndex,
    onClick: (PathOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<PathOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding PathOverlay")
            val overlay = PathOverlay().apply {
                this.coords = coords
                this.progress = progress
                this.color = color.toArgb()
                this.outlineColor = outlineColor.toArgb()
                this.outlineWidth = with(density) { outlineWidth.roundToPx() }
                this.passedColor = passedColor.toArgb()
                this.passedOutlineColor = passedOutlineColor.toArgb()
                this.patternImage = patternImage
                this.patternInterval = with(density) { patternInterval.roundToPx() }
                this.isHideCollidedSymbols = isHideCollidedSymbols
                this.isHideCollidedMarkers = isHideCollidedMarkers
                this.isHideCollidedCaptions = isHideCollidedCaptions
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
                    .nodeForPathOverlay(overlay)
                    ?.onPathOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            PathOverlayNode(overlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onPathOverlayClick = it }

            set(coords) { this.overlay.coords = it }
            set(progress) { this.overlay.progress = it }
            set(color) { this.overlay.color = it.toArgb() }
            set(outlineColor) { this.overlay.outlineColor = it.toArgb() }
            set(outlineWidth) {
                this.overlay.outlineWidth = with(this.density) { it.roundToPx() }
            }
            set(passedColor) { this.overlay.passedColor = it.toArgb() }
            set(passedOutlineColor) { this.overlay.passedOutlineColor = it.toArgb() }
            set(patternImage) { this.overlay.patternImage = it }
            set(patternInterval) {
                this.overlay.patternInterval = with(this.density) { it.roundToPx() }
            }
            set(isHideCollidedSymbols) { this.overlay.isHideCollidedSymbols = it }
            set(isHideCollidedMarkers) { this.overlay.isHideCollidedMarkers = it }
            set(isHideCollidedCaptions) { this.overlay.isHideCollidedCaptions = it }
            set(width) { this.overlay.width = with(this.density) { it.roundToPx() } }

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
