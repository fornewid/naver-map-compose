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
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.OverlayImage

internal class GroundOverlayNode(
    val groundOverlay: GroundOverlay,
    var onGroundOverlayClick: (GroundOverlay) -> Boolean
) : MapNode {
    override fun onRemoved() {
        groundOverlay.map = null
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
    tag: Any? = null,
    alpha: Float = 1f,
    visible: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = GroundOverlayDefaults.GlobalZIndex,
    onClick: (GroundOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as? MapApplier
    ComposeNode<GroundOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding ground overlay")
            val groundOverlay = GroundOverlay().apply {
                this.position(position)
                this.image = image
                this.alpha = alpha
                this.isVisible = visible
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
            }
            groundOverlay.tag = tag
            groundOverlay.map = map
            groundOverlay.setOnClickListener {
                mapApplier
                    .nodeForGroundOverlay(groundOverlay)
                    ?.onGroundOverlayClick
                    ?.invoke(groundOverlay)
                    ?: false
            }
            GroundOverlayNode(groundOverlay, onClick)
        },
        update = {
            update(onClick) { this.onGroundOverlayClick = it }

            set(position) { this.groundOverlay.position(it) }
            set(image) { this.groundOverlay.image = it }
            set(tag) { this.groundOverlay.tag = it }
            set(alpha) { this.groundOverlay.alpha = it }
            set(visible) { this.groundOverlay.isVisible = it }
            set(zIndex) { this.groundOverlay.zIndex = it }
            set(globalZIndex) { this.groundOverlay.globalZIndex = it }
        }
    )
}

private fun GroundOverlay.position(position: GroundOverlayPosition) {
    if (position.latLngBounds != null) {
        bounds = position.latLngBounds
        return
    }
}
