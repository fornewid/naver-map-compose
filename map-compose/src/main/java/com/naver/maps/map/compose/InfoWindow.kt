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

import android.graphics.PointF
import androidx.annotation.UiThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker

internal class InfoWindowNode(
    val infoWindow: InfoWindow,
    var onInfoWindowClick: (InfoWindow) -> Boolean,
) : MapNode {
    override fun onRemoved() {
        infoWindow.remove()
    }
}

public object InfoWindowDefaults {
    public const val DefaultGlobalZIndex: Int = InfoWindow.DEFAULT_GLOBAL_Z_INDEX
}

@ExperimentalNaverMapApi
@Composable
public inline fun rememberInfoWindowState(
    key: Any? = null,
    crossinline init: InfoWindowState.() -> Unit = {}
): InfoWindowState = remember(key) {
    InfoWindowState().apply(init)
}

@ExperimentalNaverMapApi
public class InfoWindowState(
    position: InfoWindowPosition = InfoWindowPosition.close()
) {

    internal var position by mutableStateOf(position)

    @UiThread
    public fun open(marker: Marker) {
        this.position = InfoWindowPosition.open(marker)
    }

    @UiThread
    public fun open(marker: Marker, align: Align) {
        this.position = InfoWindowPosition.open(marker, align)
    }

    @UiThread
    public fun open(position: LatLng) {
        this.position = InfoWindowPosition.open(position)
    }

    @UiThread
    public fun close() {
        this.position = InfoWindowPosition.close()
    }
}

/**
 * The position of a [InfoWindow].
 *
 * Use one of the [open] or [close] methods to construct an instance of this class.
 */
public class InfoWindowPosition private constructor(
    public val marker: Marker? = null,
    public val align: Align? = null,
    public val position: LatLng? = null,
) {

    public companion object {
        public fun open(marker: Marker): InfoWindowPosition {
            return InfoWindowPosition(marker = marker)
        }

        public fun open(marker: Marker, align: Align): InfoWindowPosition {
            return InfoWindowPosition(marker = marker, align = align)
        }

        public fun open(position: LatLng): InfoWindowPosition {
            return InfoWindowPosition(position = position)
        }

        public fun close(): InfoWindowPosition {
            return InfoWindowPosition()
        }
    }
}

/**
 * A composable for a info window on the map.
 *
 * TODO: (sungyong.an) 설명 추가
 * @param visible the visibility of the path
 * @param zIndex the z-index of the path
 * @param onClick a lambda invoked when the path is clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun InfoWindow(
    state: InfoWindowState,
    adapter: InfoWindow.Adapter = InfoWindow.DEFAULT_ADAPTER,
    anchor: PointF = InfoWindow.DEFAULT_ANCHOR,
    offsetX: Int = 0,
    offsetY: Int = 0,
    alpha: Float = 1f,
    tag: Any? = null,
    visible: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = InfoWindowDefaults.DefaultGlobalZIndex,
    onClick: (InfoWindow) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    ComposeNode<InfoWindowNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding InfoWindow")
            val infoWindow = InfoWindow().apply {
                this.adapter = adapter
                this.anchor = anchor
                this.alpha = alpha
                this.offsetX = offsetX
                this.offsetY = offsetY
                this.isVisible = visible
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
            }
            infoWindow.tag = tag
            infoWindow.position(state.position, map)
            infoWindow.setOnClickListener {
                mapApplier
                    .nodeForInfoWindow(infoWindow)
                    ?.onInfoWindowClick
                    ?.invoke(infoWindow)
                    ?: false
            }
            InfoWindowNode(infoWindow, onClick)
        },
        update = {
            update(onClick) { this.onInfoWindowClick = it }

            set(state.position) { this.infoWindow.position(it, mapApplier?.map) }
            update(adapter) { this.infoWindow.adapter = it }
            set(anchor) { this.infoWindow.anchor = it }
            set(alpha) { this.infoWindow.alpha = it }
            set(offsetX) { this.infoWindow.offsetX = it }
            set(offsetY) { this.infoWindow.offsetY = it }
            set(tag) { this.infoWindow.tag = it }
            set(visible) { this.infoWindow.isVisible = it }
            set(zIndex) { this.infoWindow.zIndex = it }
            set(globalZIndex) { this.infoWindow.globalZIndex = it }
        }
    )
}

private fun InfoWindow.position(position: InfoWindowPosition, map: NaverMap?) {
    if (position.marker != null) {
        if (position.align != null) {
            open(position.marker, position.align.value)
        } else {
            open(position.marker)
        }
    } else if (position.position != null && map != null) {
        this.position = position.position
        open(map)
    } else {
        close()
    }
}
