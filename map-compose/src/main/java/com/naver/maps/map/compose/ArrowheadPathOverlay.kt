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
import com.naver.maps.map.overlay.ArrowheadPathOverlay

internal class ArrowheadPathOverlayNode(
    val arrowheadPathOverlay: ArrowheadPathOverlay,
    var onArrowheadPathOverlayClick: (ArrowheadPathOverlay) -> Boolean
) : MapNode {
    override fun onRemoved() {
        arrowheadPathOverlay.remove()
    }
}

/**
 * A composable for a multipart path overlay on the map.
 *
 * @param points the points comprising the path
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
    points: List<LatLng>,
    color: Color = Color.Black,
    strokeColor: Color = Color.Black,
    strokeWidth: Int = 10,
    headSizeRatio: Float = 1f,
    elevation: Int = 0,
    tag: Any? = null,
    visible: Boolean = true,
    width: Int = 10,
    zIndex: Int = 0,
    onClick: (ArrowheadPathOverlay) -> Boolean = { false }
) {
    val mapApplier = currentComposer.applier as MapApplier?
    ComposeNode<ArrowheadPathOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding ArrowheadPathOverlay")
            val arrowheadPathOverlay = ArrowheadPathOverlay().apply {
                this.coords = points
                this.color = color.toArgb()
                this.outlineColor = strokeColor.toArgb()
                this.outlineWidth = strokeWidth
                this.headSizeRatio = headSizeRatio
                this.elevation = elevation
                this.isVisible = visible
                this.width = width
                this.zIndex = zIndex
            }
            arrowheadPathOverlay.tag = tag
            arrowheadPathOverlay.map = map
            arrowheadPathOverlay.setOnClickListener {
                mapApplier
                    .nodeForArrowheadPathOverlay(arrowheadPathOverlay)
                    ?.onArrowheadPathOverlayClick
                    ?.invoke(arrowheadPathOverlay)
                    ?: false
            }
            ArrowheadPathOverlayNode(arrowheadPathOverlay, onClick)
        },
        update = {
            update(onClick) { this.onArrowheadPathOverlayClick = it }

            set(points) { this.arrowheadPathOverlay.coords = it }
            set(color) { this.arrowheadPathOverlay.color = it.toArgb() }
            set(strokeColor) { this.arrowheadPathOverlay.outlineColor = it.toArgb() }
            set(strokeWidth) { this.arrowheadPathOverlay.outlineWidth = it }
            set(headSizeRatio) { this.arrowheadPathOverlay.headSizeRatio = it }
            set(elevation) { this.arrowheadPathOverlay.elevation = it }
            set(tag) { this.arrowheadPathOverlay.tag = it }
            set(visible) { this.arrowheadPathOverlay.isVisible = it }
            set(width) { this.arrowheadPathOverlay.width = it }
            set(zIndex) { this.arrowheadPathOverlay.zIndex = it }
        }
    )
}
