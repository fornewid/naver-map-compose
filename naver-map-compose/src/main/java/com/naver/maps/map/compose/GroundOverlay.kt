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
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.compose.GroundOverlayPosition.Companion.create
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.OverlayImage

internal class GroundOverlayNode(
    val overlay: GroundOverlay,
    var onGroundOverlayClick: (GroundOverlay) -> Boolean
) : MapNode {
    override fun onRemoved() {
        overlay.map = null
    }
}

public object GroundOverlayDefaults {
    public const val GlobalZIndex: Int = GroundOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * The position of a [GroundOverlay].
 *
 * Use one of the [create] methods to construct an instance of this class.
 */
public class GroundOverlayPosition private constructor(
    public val latLngBounds: LatLngBounds? = null
) {
    public companion object {
        public fun create(): GroundOverlayPosition {
            return GroundOverlayPosition()
        }

        public fun create(bounds: LatLngBounds): GroundOverlayPosition {
            return GroundOverlayPosition(latLngBounds = bounds)
        }
    }
}

/**
 * A composable for a ground overlay on the map.
 *
 * @param position the position of the ground overlay
 * @param image the image of the ground overlay
 * @param tag optional tag to associate with the ground overlay
 * @param alpha the alpha of the ground overlay
 * @param visible the visibility of the ground overlay
 * @param zIndex the z-index of the ground overlay
 * @param onClick a lambda invoked when the ground overlay is clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun GroundOverlay(
    position: GroundOverlayPosition = GroundOverlayPosition.create(),
    image: OverlayImage = GroundOverlay.DEFAULT_IMAGE,
    alpha: Float = 1f,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapDefaults.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapDefaults.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = GroundOverlayDefaults.GlobalZIndex,
    onClick: (GroundOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as? MapApplier
    ComposeNode<GroundOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding GroundOverlay")
            val overlay = GroundOverlay().apply {
                this.position(position)
                this.image = image
                this.alpha = alpha

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
                    .nodeForGroundOverlay(overlay)
                    ?.onGroundOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            GroundOverlayNode(overlay, onClick)
        },
        update = {
            update(onClick) { this.onGroundOverlayClick = it }

            set(position) { this.overlay.position(it) }
            set(image) { this.overlay.image = it }
            set(alpha) { this.overlay.alpha = it }

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

private fun GroundOverlay.position(position: GroundOverlayPosition) {
    if (position.latLngBounds != null) {
        bounds = position.latLngBounds
        return
    }
}
