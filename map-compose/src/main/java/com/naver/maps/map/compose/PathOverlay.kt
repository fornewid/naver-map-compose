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
import com.naver.maps.map.overlay.PathOverlay

internal class PathOverlayNode(
    val pathOverlay: PathOverlay,
    var onPathOverlayClick: (PathOverlay) -> Boolean
) : MapNode {
    override fun onRemoved() {
        pathOverlay.remove()
    }
}

/**
 * A composable for a path overlay on the map.
 *
 * @param points the points comprising the path
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
    points: List<LatLng>,
    progress: Double = 0.0,
    color: Color = Color.Black,
    strokeColor: Color = Color.Black,
    strokeWidth: Int = 10,
    passedColor: Color = Color.Black,
    passedOutlineColor: Color = Color.Black,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    tag: Any? = null,
    visible: Boolean = true,
    width: Int = 10,
    zIndex: Int = 0,
    onClick: (PathOverlay) -> Boolean = { false }
) {
    val mapApplier = currentComposer.applier as MapApplier?
    ComposeNode<PathOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding PathOverlay")
            val pathOverlay = PathOverlay().apply {
                this.coords = points
                this.progress = progress
                this.color = color.toArgb()
                this.outlineColor = strokeColor.toArgb()
                this.outlineWidth = strokeWidth
                this.passedColor = passedColor.toArgb()
                this.passedOutlineColor = passedOutlineColor.toArgb()
                this.isHideCollidedSymbols = isHideCollidedSymbols
                this.isHideCollidedMarkers = isHideCollidedMarkers
                this.isHideCollidedCaptions = isHideCollidedCaptions
                this.isVisible = visible
                this.width = width
                this.zIndex = zIndex
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
            PathOverlayNode(pathOverlay, onClick)
        },
        update = {
            update(onClick) { this.onPathOverlayClick = it }

            set(points) { this.pathOverlay.coords = it }
            set(progress) { this.pathOverlay.progress = it }
            set(color) { this.pathOverlay.color = it.toArgb() }
            set(strokeColor) { this.pathOverlay.outlineColor = it.toArgb() }
            set(strokeWidth) { this.pathOverlay.outlineWidth = it }
            set(passedColor) { this.pathOverlay.passedColor = it.toArgb() }
            set(passedOutlineColor) { this.pathOverlay.passedOutlineColor = it.toArgb() }
            set(isHideCollidedSymbols) { this.pathOverlay.isHideCollidedSymbols = it }
            set(isHideCollidedMarkers) { this.pathOverlay.isHideCollidedMarkers = it }
            set(isHideCollidedCaptions) { this.pathOverlay.isHideCollidedCaptions = it }
            set(tag) { this.pathOverlay.tag = it }
            set(visible) { this.pathOverlay.isVisible = it }
            set(width) { this.pathOverlay.width = it }
            set(zIndex) { this.pathOverlay.zIndex = it }
        }
    )
}
