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
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.MultipartPathOverlay
import com.naver.maps.map.overlay.OverlayImage

internal class MultipartPathOverlayNode(
    val multipartPathOverlay: MultipartPathOverlay,
    var onMultipartPathOverlayClick: (MultipartPathOverlay) -> Boolean
) : MapNode {
    override fun onRemoved() {
        multipartPathOverlay.remove()
    }
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
    strokeWidth: Int = 10,
    patternImage: OverlayImage? = null,
    patternInterval: Int = 0,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    tag: Any? = null,
    visible: Boolean = true,
    width: Int = 10,
    zIndex: Int = 0,
    onClick: (MultipartPathOverlay) -> Boolean = { false }
) {
    val mapApplier = currentComposer.applier as MapApplier?
    ComposeNode<MultipartPathOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding MultipartPathOverlay")
            val multipartPathOverlay = MultipartPathOverlay().apply {
                this.coordParts = coordParts
                this.colorParts = colorParts
                this.progress = progress
                this.outlineWidth = strokeWidth
                this.patternImage = patternImage
                this.patternInterval = patternInterval
                this.isHideCollidedSymbols = isHideCollidedSymbols
                this.isHideCollidedMarkers = isHideCollidedMarkers
                this.isHideCollidedCaptions = isHideCollidedCaptions
                this.isVisible = visible
                this.width = width
                this.zIndex = zIndex
            }
            multipartPathOverlay.tag = tag
            multipartPathOverlay.map = map
            multipartPathOverlay.setOnClickListener {
                mapApplier
                    .nodeForMultipartPathOverlay(multipartPathOverlay)
                    ?.onMultipartPathOverlayClick
                    ?.invoke(multipartPathOverlay)
                    ?: false
            }
            MultipartPathOverlayNode(multipartPathOverlay, onClick)
        },
        update = {
            update(onClick) { this.onMultipartPathOverlayClick = it }

            set(coordParts) { this.multipartPathOverlay.coordParts = it }
            set(colorParts) { this.multipartPathOverlay.colorParts = it }
            set(progress) { this.multipartPathOverlay.progress = it }
            set(strokeWidth) { this.multipartPathOverlay.outlineWidth = it }
            set(patternImage) { this.multipartPathOverlay.patternImage = it }
            set(patternInterval) { this.multipartPathOverlay.patternInterval = it }
            set(isHideCollidedSymbols) { this.multipartPathOverlay.isHideCollidedSymbols = it }
            set(isHideCollidedMarkers) { this.multipartPathOverlay.isHideCollidedMarkers = it }
            set(isHideCollidedCaptions) { this.multipartPathOverlay.isHideCollidedCaptions = it }
            set(tag) { this.multipartPathOverlay.tag = it }
            set(visible) { this.multipartPathOverlay.isVisible = it }
            set(width) { this.multipartPathOverlay.width = it }
            set(zIndex) { this.multipartPathOverlay.zIndex = it }
        }
    )
}
