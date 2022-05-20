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
    val pathOverlay: PathOverlay,
    var onPathOverlayClick: (PathOverlay) -> Boolean,
    var density: Density
) : MapNode {
    override fun onRemoved() {
        pathOverlay.map = null
    }
}

public object PathOverlayDefaults {
    public const val DefaultGlobalZIndex: Int = PathOverlay.DEFAULT_GLOBAL_Z_INDEX
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
    tag: Any? = null,
    visible: Boolean = true,
    width: Dp = 10.dp,
    zIndex: Int = 0,
    globalZIndex: Int = PathOverlayDefaults.DefaultGlobalZIndex,
    onClick: (PathOverlay) -> Boolean = { false }
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<PathOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding PathOverlay")
            val pathOverlay = PathOverlay().apply {
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
                this.isVisible = visible
                this.width = with(density) { width.roundToPx() }
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
            }
            pathOverlay.tag = tag
            pathOverlay.map = map
            pathOverlay.setOnClickListener {
                mapApplier
                    .nodeForPathOverlay(pathOverlay)
                    ?.onPathOverlayClick
                    ?.invoke(pathOverlay)
                    ?: false
            }
            PathOverlayNode(pathOverlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onPathOverlayClick = it }

            set(coords) { this.pathOverlay.coords = it }
            set(progress) { this.pathOverlay.progress = it }
            set(color) { this.pathOverlay.color = it.toArgb() }
            set(outlineColor) { this.pathOverlay.outlineColor = it.toArgb() }
            set(outlineWidth) {
                this.pathOverlay.outlineWidth = with(this.density) { it.roundToPx() }
            }
            set(passedColor) { this.pathOverlay.passedColor = it.toArgb() }
            set(passedOutlineColor) { this.pathOverlay.passedOutlineColor = it.toArgb() }
            set(patternImage) { this.pathOverlay.patternImage = it }
            set(patternInterval) {
                this.pathOverlay.patternInterval = with(this.density) { it.roundToPx() }
            }
            set(isHideCollidedSymbols) { this.pathOverlay.isHideCollidedSymbols = it }
            set(isHideCollidedMarkers) { this.pathOverlay.isHideCollidedMarkers = it }
            set(isHideCollidedCaptions) { this.pathOverlay.isHideCollidedCaptions = it }
            set(tag) { this.pathOverlay.tag = it }
            set(visible) { this.pathOverlay.isVisible = it }
            set(width) { this.pathOverlay.width = with(this.density) { it.roundToPx() } }
            set(zIndex) { this.pathOverlay.zIndex = it }
            set(globalZIndex) { this.pathOverlay.globalZIndex = it }
        }
    )
}
