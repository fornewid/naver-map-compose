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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.MultipartPathOverlay
import com.naver.maps.map.overlay.OverlayImage

internal class MultipartPathOverlayNode(
    val overlay: MultipartPathOverlay,
    var onMultipartPathOverlayClick: (MultipartPathOverlay) -> Boolean,
    var density: Density,
) : MapNode {
    override fun onRemoved() {
        overlay.map = null
    }
}

public object MultipartPathOverlayDefaults {
    public const val GlobalZIndex: Int = MultipartPathOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * A composable for a multipart path overlay on the map.
 *
 * TODO: (sungyong.an) 설명 추가
 * @param progress the progress of the path
 * TODO: (sungyong.an) 설명 추가
 * @param visible the visibility of the path
 * @param width the width of the path in screen pixels
 * @param zIndex the z-index of the path
 * @param onClick a lambda invoked when the path is clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun MultipartPathOverlay(
    coordParts: List<List<LatLng>>,
    colorParts: List<MultipartPathOverlay.ColorPart>,
    progress: Double = 0.0,
    outlineWidth: Dp = 10.dp,
    patternImage: OverlayImage? = null,
    patternInterval: Dp = 0.dp,
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
    globalZIndex: Int = MultipartPathOverlayDefaults.GlobalZIndex,
    onClick: (MultipartPathOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<MultipartPathOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding MultipartPathOverlay")
            val overlay = MultipartPathOverlay().apply {
                this.coordParts = coordParts
                this.colorParts = colorParts
                this.progress = progress
                this.outlineWidth = with(density) { outlineWidth.roundToPx() }
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
                    .nodeForMultipartPathOverlay(overlay)
                    ?.onMultipartPathOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            MultipartPathOverlayNode(overlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onMultipartPathOverlayClick = it }

            set(coordParts) { this.overlay.coordParts = it }
            set(colorParts) { this.overlay.colorParts = it }
            set(progress) { this.overlay.progress = it }
            set(outlineWidth) {
                this.overlay.outlineWidth = with(this.density) { it.roundToPx() }
            }
            set(patternImage) { this.overlay.patternImage = it }
            set(patternInterval) {
                this.overlay.patternInterval = with(this.density) { it.roundToPx() }
            }
            set(isHideCollidedSymbols) { this.overlay.isHideCollidedSymbols = it }
            set(isHideCollidedMarkers) { this.overlay.isHideCollidedMarkers = it }
            set(isHideCollidedCaptions) { this.overlay.isHideCollidedCaptions = it }
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
