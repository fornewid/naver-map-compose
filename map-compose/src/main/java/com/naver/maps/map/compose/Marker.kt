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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

internal class MarkerNode(
    val marker: Marker,
    val markerState: MarkerState,
    var onMarkerClick: (Marker) -> Boolean,
    var onInfoWindowClick: (Marker) -> Unit,
    var onInfoWindowClose: (Marker) -> Unit,
    var onInfoWindowLongClick: (Marker) -> Unit,
    var infoWindow: @Composable() ((Marker) -> Unit)?,
    var infoContent: @Composable() ((Marker) -> Unit)?,
) : MapNode {

    override fun onAttached() {
        markerState.marker = marker
    }

    override fun onRemoved() {
        markerState.marker = null
        marker.remove()
    }

    override fun onCleared() {
        markerState.marker = null
        marker.remove()
    }
}

@Immutable
public enum class DragState {
    START, DRAG, END
}

/**
 * A state object that can be hoisted to control and observe the marker state.
 *
 * @param position the initial marker position
 */
public class MarkerState(
    position: LatLng = LatLng(0.0, 0.0)
) {
    /**
     * Current position of the marker.
     */
    public var position: LatLng by mutableStateOf(position)

    // The marker associated with this MarkerState.
    internal var marker: Marker? = null
        set(value) {
            if (field == null && value == null) return
            if (field != null && value != null) {
                error("MarkerState may only be associated with one Marker at a time.")
            }
            field = value
        }

    /**
     * Shows the info window for the underlying marker
     */
    public fun showInfoWindow() {
        marker?.showInfoWindow()
    }

    /**
     * Hides the info window for the underlying marker
     */
    public fun hideInfoWindow() {
        marker?.hideInfoWindow()
    }

    public companion object {
        /**
         * The default saver implementation for [MarkerState]
         */
        public val Saver: Saver<MarkerState, LatLng> = Saver(
            save = { it.position },
            restore = { MarkerState(it) }
        )
    }
}

@ExperimentalNaverMapApi
@Composable
public fun rememberMarkerState(
    key: String? = null,
    position: LatLng = LatLng(0.0, 0.0)
): MarkerState = rememberSaveable(key = key, saver = MarkerState.Saver) {
    MarkerState(position)
}

/**
 * A composable for a marker on the map.
 *
 * @param state the [MarkerState] to be used to control or observe the marker
 * state such as its position and info window
 * @param alpha the alpha (opacity) of the marker
 * @param anchor the anchor for the marker image
 * @param flat sets if the marker should be flat against the map
 * @param icon sets the icon for the marker
 * @param infoWindowAnchor the anchor point of the info window on the marker image
 * @param rotation the rotation of the marker in degrees clockwise about the marker's anchor point
 * @param snippet the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param title the title for the marker
 * @param visible the visibility of the marker
 * @param zIndex the z-index of the marker
 * @param onClick a lambda invoked when the marker is clicked
 * @param onInfoWindowClick a lambda invoked when the marker's info window is clicked
 * @param onInfoWindowClose a lambda invoked when the marker's info window is closed
 * @param onInfoWindowLongClick a lambda invoked when the marker's info window is long clicked
 */
@ExperimentalNaverMapApi
@Composable
public fun Marker(
    state: MarkerState = rememberMarkerState(),
    alpha: Float = 1.0f,
    anchor: Offset = Offset(0.5f, 1.0f),
    flat: Boolean = false,
    icon: OverlayImage = Marker.DEFAULT_ICON,
    infoWindowAnchor: Offset = Offset(0.5f, 0.0f),
    rotation: Float = 0.0f,
    snippet: String? = null,
    tag: Any? = null,
    title: String? = null,
    visible: Boolean = true,
    zIndex: Int = 0,
    onClick: (Marker) -> Boolean = { false },
    onInfoWindowClick: (Marker) -> Unit = {},
    onInfoWindowClose: (Marker) -> Unit = {},
    onInfoWindowLongClick: (Marker) -> Unit = {},
) {
    MarkerImpl(
        state = state,
        alpha = alpha,
        anchor = anchor,
        flat = flat,
        icon = icon,
        infoWindowAnchor = infoWindowAnchor,
        rotation = rotation,
        snippet = snippet,
        tag = tag,
        title = title,
        visible = visible,
        zIndex = zIndex,
        onClick = onClick,
        onInfoWindowClick = onInfoWindowClick,
        onInfoWindowClose = onInfoWindowClose,
        onInfoWindowLongClick = onInfoWindowLongClick,
    )
}

/**
 * A composable for a marker on the map wherein its entire info window can be
 * customized. If this customization is not required, use
 * [com.naver.maps.map.compose.Marker].
 *
 * @param state the [MarkerState] to be used to control or observe the marker
 * state such as its position and info window
 * @param alpha the alpha (opacity) of the marker
 * @param anchor the anchor for the marker image
 * @param flat sets if the marker should be flat against the map
 * @param icon sets the icon for the marker
 * @param infoWindowAnchor the anchor point of the info window on the marker image
 * @param rotation the rotation of the marker in degrees clockwise about the marker's anchor point
 * @param snippet the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param title the title for the marker
 * @param visible the visibility of the marker
 * @param zIndex the z-index of the marker
 * @param onClick a lambda invoked when the marker is clicked
 * @param onInfoWindowClick a lambda invoked when the marker's info window is clicked
 * @param onInfoWindowClose a lambda invoked when the marker's info window is closed
 * @param onInfoWindowLongClick a lambda invoked when the marker's info window is long clicked
 * @param content optional composable lambda expression for customizing the
 * info window's content
 */
@ExperimentalNaverMapApi
@Composable
public fun MarkerInfoWindow(
    state: MarkerState = rememberMarkerState(),
    alpha: Float = 1.0f,
    anchor: Offset = Offset(0.5f, 1.0f),
    flat: Boolean = false,
    icon: OverlayImage = Marker.DEFAULT_ICON,
    infoWindowAnchor: Offset = Offset(0.5f, 0.0f),
    rotation: Float = 0.0f,
    snippet: String? = null,
    tag: Any? = null,
    title: String? = null,
    visible: Boolean = true,
    zIndex: Int = 0,
    onClick: (Marker) -> Boolean = { false },
    onInfoWindowClick: (Marker) -> Unit = {},
    onInfoWindowClose: (Marker) -> Unit = {},
    onInfoWindowLongClick: (Marker) -> Unit = {},
    content: @Composable() ((Marker) -> Unit)? = null
) {
    MarkerImpl(
        state = state,
        alpha = alpha,
        anchor = anchor,
        flat = flat,
        icon = icon,
        infoWindowAnchor = infoWindowAnchor,
        rotation = rotation,
        snippet = snippet,
        tag = tag,
        title = title,
        visible = visible,
        zIndex = zIndex,
        onClick = onClick,
        onInfoWindowClick = onInfoWindowClick,
        onInfoWindowClose = onInfoWindowClose,
        onInfoWindowLongClick = onInfoWindowLongClick,
        infoWindow = content,
    )
}

/**
 * A composable for a marker on the map wherein its info window contents can be
 * customized. If this customization is not required, use
 * [com.naver.maps.map.compose.Marker].
 *
 * @param state the [MarkerState] to be used to control or observe the marker
 * state such as its position and info window
 * @param alpha the alpha (opacity) of the marker
 * @param anchor the anchor for the marker image
 * @param flat sets if the marker should be flat against the map
 * @param icon sets the icon for the marker
 * @param infoWindowAnchor the anchor point of the info window on the marker image
 * @param rotation the rotation of the marker in degrees clockwise about the marker's anchor point
 * @param snippet the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param title the title for the marker
 * @param visible the visibility of the marker
 * @param zIndex the z-index of the marker
 * @param onClick a lambda invoked when the marker is clicked
 * @param onInfoWindowClick a lambda invoked when the marker's info window is clicked
 * @param onInfoWindowClose a lambda invoked when the marker's info window is closed
 * @param onInfoWindowLongClick a lambda invoked when the marker's info window is long clicked
 * @param content optional composable lambda expression for customizing the
 * info window's content
 */
@ExperimentalNaverMapApi
@Composable
public fun MarkerInfoWindowContent(
    state: MarkerState = rememberMarkerState(),
    alpha: Float = 1.0f,
    anchor: Offset = Offset(0.5f, 1.0f),
    flat: Boolean = false,
    icon: OverlayImage = Marker.DEFAULT_ICON,
    infoWindowAnchor: Offset = Offset(0.5f, 0.0f),
    rotation: Float = 0.0f,
    snippet: String? = null,
    tag: Any? = null,
    title: String? = null,
    visible: Boolean = true,
    zIndex: Int = 0,
    onClick: (Marker) -> Boolean = { false },
    onInfoWindowClick: (Marker) -> Unit = {},
    onInfoWindowClose: (Marker) -> Unit = {},
    onInfoWindowLongClick: (Marker) -> Unit = {},
    content: @Composable() ((Marker) -> Unit)? = null
) {
    MarkerImpl(
        state = state,
        alpha = alpha,
        anchor = anchor,
        flat = flat,
        icon = icon,
        infoWindowAnchor = infoWindowAnchor,
        rotation = rotation,
        snippet = snippet,
        tag = tag,
        title = title,
        visible = visible,
        zIndex = zIndex,
        onClick = onClick,
        onInfoWindowClick = onInfoWindowClick,
        onInfoWindowClose = onInfoWindowClose,
        onInfoWindowLongClick = onInfoWindowLongClick,
        infoContent = content,
    )
}

/**
 * Internal implementation for a marker on a Naver map.
 *
 * @param state the [MarkerState] to be used to control or observe the marker
 * state such as its position and info window
 * @param alpha the alpha (opacity) of the marker
 * @param anchor the anchor for the marker image
 * @param flat sets if the marker should be flat against the map
 * @param icon sets the icon for the marker
 * @param infoWindowAnchor the anchor point of the info window on the marker image
 * @param rotation the rotation of the marker in degrees clockwise about the marker's anchor point
 * @param snippet the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param title the title for the marker
 * @param visible the visibility of the marker
 * @param zIndex the z-index of the marker
 * @param onClick a lambda invoked when the marker is clicked
 * @param onInfoWindowClick a lambda invoked when the marker's info window is clicked
 * @param onInfoWindowClose a lambda invoked when the marker's info window is closed
 * @param onInfoWindowLongClick a lambda invoked when the marker's info window is long clicked
 * @param infoWindow optional composable lambda expression for customizing
 * the entire info window. If this value is non-null, the value in infoContent]
 * will be ignored.
 * @param infoContent optional composable lambda expression for customizing
 * the info window's content. If this value is non-null, [infoWindow] must be null.
 */
@ExperimentalNaverMapApi
@Composable
private fun MarkerImpl(
    state: MarkerState = rememberMarkerState(),
    alpha: Float = 1.0f,
    anchor: Offset = Offset(0.5f, 1.0f),
    flat: Boolean = false,
    icon: OverlayImage = Marker.DEFAULT_ICON,
    infoWindowAnchor: Offset = Offset(0.5f, 0.0f),
    rotation: Float = 0.0f,
    snippet: String? = null,
    tag: Any? = null,
    title: String? = null,
    visible: Boolean = true,
    zIndex: Int = 0,
    onClick: (Marker) -> Boolean = { false },
    onInfoWindowClick: (Marker) -> Unit = {},
    onInfoWindowClose: (Marker) -> Unit = {},
    onInfoWindowLongClick: (Marker) -> Unit = {},
    infoWindow: @Composable() ((Marker) -> Unit)? = null,
    infoContent: @Composable() ((Marker) -> Unit)? = null,
) {
    val mapApplier = currentComposer.applier as? MapApplier
    ComposeNode<MarkerNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding marker")
            val marker: Marker = Marker().apply {
                this.alpha = alpha
                this.anchor = PointF(anchor.x, anchor.y)
                this.isFlat = flat
                this.icon = icon
                this.infoWindow?.anchor = PointF(infoWindowAnchor.x, infoWindowAnchor.y)
                this.position = state.position
                this.angle = rotation
                this.subCaptionText = snippet.orEmpty()
                this.captionText = title.orEmpty()
                this.isVisible = visible
                this.zIndex = zIndex
            }
            marker.map = map
            marker.tag = tag
            marker.setOnClickListener {
                mapApplier
                    .nodeForMarker(marker)
                    ?.onMarkerClick
                    ?.invoke(marker)
                    ?: false
            }
            MarkerNode(
                marker = marker,
                markerState = state,
                onMarkerClick = onClick,
                onInfoWindowClick = onInfoWindowClick,
                onInfoWindowClose = onInfoWindowClose,
                onInfoWindowLongClick = onInfoWindowLongClick,
                infoWindow = infoWindow,
                infoContent = infoContent,
            )
        },
        update = {
            update(onClick) { this.onMarkerClick = it }
            update(onInfoWindowClick) { this.onInfoWindowClick = it }
            update(onInfoWindowClose) { this.onInfoWindowClose = it }
            update(onInfoWindowLongClick) { this.onInfoWindowLongClick = it }
            update(infoContent) { this.infoContent = it }
            update(infoWindow) { this.infoWindow = it }

            set(alpha) { this.marker.alpha = it }
            set(anchor) { this.marker.anchor = PointF(it.x, it.y) }
            set(flat) { this.marker.isFlat = it }
            set(icon) { this.marker.icon = it }
            set(infoWindowAnchor) { this.marker.infoWindow?.anchor = PointF(it.x, it.y) }
            set(state.position) { this.marker.position = it }
            set(rotation) { this.marker.angle = it }
            set(snippet) {
                this.marker.subCaptionText = it.orEmpty()
                if (this.marker.isInfoWindowShown) {
                    this.marker.showInfoWindow()
                }
            }
            set(tag) { this.marker.tag = it }
            set(title) {
                this.marker.captionText = it.orEmpty()
                if (this.marker.isInfoWindowShown) {
                    this.marker.showInfoWindow()
                }
            }
            set(visible) { this.marker.isVisible = it }
            set(zIndex) { this.marker.zIndex = it }
        }
    )
}

private fun Marker.showInfoWindow() {
    infoWindow?.open(this)
}

private fun Marker.hideInfoWindow() {
    infoWindow?.close()
}

private val Marker.isInfoWindowShown: Boolean
    get() = hasInfoWindow()
