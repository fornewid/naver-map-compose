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
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

internal class MarkerNode(
    val marker: Marker,
    val markerState: MarkerState,
    var density: Density,
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

public object MarkerDefaults {
    public const val DefaultGlobalZIndex: Int = Marker.DEFAULT_GLOBAL_Z_INDEX
    public val SizeAuto: Dp = Marker.SIZE_AUTO.dp
    public val DefaultIcon: OverlayImage = Marker.DEFAULT_ICON
    public val DefaultAnchor: PointF = Marker.DEFAULT_ANCHOR
    public val DefaultCaptionTextSize: TextUnit = Marker.DEFAULT_CAPTION_TEXT_SIZE.sp
    public val DefaultCaptionAligns: Array<Align> = arrayOf(Align.Bottom)
    public const val DefaultCaptionMinZoom: Double = NaverMap.MINIMUM_ZOOM.toDouble()
    public const val DefaultCaptionMaxZoom: Double = NaverMap.MAXIMUM_ZOOM.toDouble()
}

/**
 * A state object that can be hoisted to control and observe the marker state.
 *
 * @param position the initial marker position
 */
public class MarkerState(
    position: LatLng = LatLng(0.0, 0.0),
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
    position: LatLng = LatLng(0.0, 0.0),
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
 * @param isFlat sets if the marker should be flat against the map
 * @param icon sets the icon for the marker
 * @param infoWindowAnchor the anchor point of the info window on the marker image
 * @param angle the angle of the marker in degrees clockwise about the marker's anchor point
 * @param subCaptionText the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param captionText the title for the marker
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
    width: Dp = MarkerDefaults.SizeAuto,
    height: Dp = MarkerDefaults.SizeAuto,
    alpha: Float = 1.0f,
    anchor: Offset = Offset(0.5f, 1.0f),
    isFlat: Boolean = false,
    icon: OverlayImage = MarkerDefaults.DefaultIcon,
    iconTintColor: Color = Color.Transparent,
    infoWindowAnchor: Offset = Offset(0.5f, 0.0f),
    angle: Float = 0.0f,
    minZoom: Double = OverlayDefaults.DefaultMinZoom,
    isMinZoomInclusive: Boolean = true,
    maxZoom: Double = OverlayDefaults.DefaultMaxZoom,
    isMaxZoomInclusive: Boolean = true,
    subCaptionText: String? = null,
    subCaptionColor: Color = Color.Black,
    subCaptionTextSize: TextUnit = MarkerDefaults.DefaultCaptionTextSize,
    subCaptionMinZoom: Double = MarkerDefaults.DefaultCaptionMinZoom,
    subCaptionMaxZoom: Double = MarkerDefaults.DefaultCaptionMaxZoom,
    subCaptionHaloColor: Color = Color.White,
    subCaptionRequestedWidth: Dp = 0.dp,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    isForceShowIcon: Boolean = false,
    isForceShowCaption: Boolean = false,
    tag: Any? = null,
    captionText: String? = null,
    captionColor: Color = Color.Black,
    captionTextSize: TextUnit = MarkerDefaults.DefaultCaptionTextSize,
    captionMinZoom: Double = MarkerDefaults.DefaultCaptionMinZoom,
    captionMaxZoom: Double = MarkerDefaults.DefaultCaptionMaxZoom,
    captionAligns: Array<Align> = MarkerDefaults.DefaultCaptionAligns,
    visible: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = MarkerDefaults.DefaultGlobalZIndex,
    onClick: (Marker) -> Boolean = { false },
    onInfoWindowClick: (Marker) -> Unit = {},
    onInfoWindowClose: (Marker) -> Unit = {},
    onInfoWindowLongClick: (Marker) -> Unit = {},
) {
    MarkerImpl(
        state = state,
        width = width,
        height = height,
        alpha = alpha,
        anchor = anchor,
        isFlat = isFlat,
        icon = icon,
        iconTintColor = iconTintColor,
        infoWindowAnchor = infoWindowAnchor,
        angle = angle,
        minZoom = minZoom,
        isMinZoomInclusive = isMinZoomInclusive,
        maxZoom = maxZoom,
        isMaxZoomInclusive = isMaxZoomInclusive,
        subCaptionText = subCaptionText,
        subCaptionColor = subCaptionColor,
        subCaptionTextSize = subCaptionTextSize,
        subCaptionMinZoom = subCaptionMinZoom,
        subCaptionMaxZoom = subCaptionMaxZoom,
        subCaptionHaloColor = subCaptionHaloColor,
        subCaptionRequestedWidth = subCaptionRequestedWidth,
        isHideCollidedSymbols = isHideCollidedSymbols,
        isHideCollidedMarkers = isHideCollidedMarkers,
        isHideCollidedCaptions = isHideCollidedCaptions,
        isForceShowIcon = isForceShowIcon,
        isForceShowCaption = isForceShowCaption,
        tag = tag,
        captionText = captionText,
        captionColor = captionColor,
        captionTextSize = captionTextSize,
        captionMinZoom = captionMinZoom,
        captionMaxZoom = captionMaxZoom,
        captionAligns = captionAligns,
        visible = visible,
        zIndex = zIndex,
        globalZIndex = globalZIndex,
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
 * @param isFlat sets if the marker should be flat against the map
 * @param icon sets the icon for the marker
 * @param infoWindowAnchor the anchor point of the info window on the marker image
 * @param angle the angle of the marker in degrees clockwise about the marker's anchor point
 * @param subCaptionText the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param captionText the title for the marker
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
    width: Dp = MarkerDefaults.SizeAuto,
    height: Dp = MarkerDefaults.SizeAuto,
    alpha: Float = 1.0f,
    anchor: Offset = Offset(0.5f, 1.0f),
    isFlat: Boolean = false,
    icon: OverlayImage = MarkerDefaults.DefaultIcon,
    iconTintColor: Color = Color.Transparent,
    infoWindowAnchor: Offset = Offset(0.5f, 0.0f),
    angle: Float = 0.0f,
    minZoom: Double = OverlayDefaults.DefaultMinZoom,
    isMinZoomInclusive: Boolean = true,
    maxZoom: Double = OverlayDefaults.DefaultMaxZoom,
    isMaxZoomInclusive: Boolean = true,
    subCaptionText: String? = null,
    subCaptionColor: Color = Color.Black,
    subCaptionTextSize: TextUnit = MarkerDefaults.DefaultCaptionTextSize,
    subCaptionMinZoom: Double = MarkerDefaults.DefaultCaptionMinZoom,
    subCaptionMaxZoom: Double = MarkerDefaults.DefaultCaptionMaxZoom,
    subCaptionHaloColor: Color = Color.White,
    subCaptionRequestedWidth: Dp = 0.dp,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    isForceShowIcon: Boolean = false,
    isForceShowCaption: Boolean = false,
    tag: Any? = null,
    captionText: String? = null,
    captionColor: Color = Color.Black,
    captionTextSize: TextUnit = MarkerDefaults.DefaultCaptionTextSize,
    captionMinZoom: Double = MarkerDefaults.DefaultCaptionMinZoom,
    captionMaxZoom: Double = MarkerDefaults.DefaultCaptionMaxZoom,
    captionAligns: Array<Align> = MarkerDefaults.DefaultCaptionAligns,
    visible: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = MarkerDefaults.DefaultGlobalZIndex,
    onClick: (Marker) -> Boolean = { false },
    onInfoWindowClick: (Marker) -> Unit = {},
    onInfoWindowClose: (Marker) -> Unit = {},
    onInfoWindowLongClick: (Marker) -> Unit = {},
    content: @Composable() ((Marker) -> Unit)? = null,
) {
    MarkerImpl(
        state = state,
        width = width,
        height = height,
        alpha = alpha,
        anchor = anchor,
        isFlat = isFlat,
        icon = icon,
        iconTintColor = iconTintColor,
        infoWindowAnchor = infoWindowAnchor,
        angle = angle,
        minZoom = minZoom,
        isMinZoomInclusive = isMinZoomInclusive,
        maxZoom = maxZoom,
        isMaxZoomInclusive = isMaxZoomInclusive,
        subCaptionText = subCaptionText,
        subCaptionColor = subCaptionColor,
        subCaptionTextSize = subCaptionTextSize,
        subCaptionMinZoom = subCaptionMinZoom,
        subCaptionMaxZoom = subCaptionMaxZoom,
        subCaptionHaloColor = subCaptionHaloColor,
        subCaptionRequestedWidth = subCaptionRequestedWidth,
        isHideCollidedSymbols = isHideCollidedSymbols,
        isHideCollidedMarkers = isHideCollidedMarkers,
        isHideCollidedCaptions = isHideCollidedCaptions,
        isForceShowIcon = isForceShowIcon,
        isForceShowCaption = isForceShowCaption,
        tag = tag,
        captionText = captionText,
        captionColor = captionColor,
        captionTextSize = captionTextSize,
        captionMinZoom = captionMinZoom,
        captionMaxZoom = captionMaxZoom,
        captionAligns = captionAligns,
        visible = visible,
        zIndex = zIndex,
        globalZIndex = globalZIndex,
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
 * @param isFlat sets if the marker should be flat against the map
 * @param icon sets the icon for the marker
 * @param infoWindowAnchor the anchor point of the info window on the marker image
 * @param angle the angle of the marker in degrees clockwise about the marker's anchor point
 * @param subCaptionText the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param captionText the title for the marker
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
    width: Dp = MarkerDefaults.SizeAuto,
    height: Dp = MarkerDefaults.SizeAuto,
    alpha: Float = 1.0f,
    anchor: Offset = Offset(0.5f, 1.0f),
    isFlat: Boolean = false,
    icon: OverlayImage = MarkerDefaults.DefaultIcon,
    iconTintColor: Color = Color.Transparent,
    infoWindowAnchor: Offset = Offset(0.5f, 0.0f),
    angle: Float = 0.0f,
    minZoom: Double = OverlayDefaults.DefaultMinZoom,
    isMinZoomInclusive: Boolean = true,
    maxZoom: Double = OverlayDefaults.DefaultMaxZoom,
    isMaxZoomInclusive: Boolean = true,
    subCaptionText: String? = null,
    subCaptionColor: Color = Color.Black,
    subCaptionTextSize: TextUnit = MarkerDefaults.DefaultCaptionTextSize,
    subCaptionMinZoom: Double = MarkerDefaults.DefaultCaptionMinZoom,
    subCaptionMaxZoom: Double = MarkerDefaults.DefaultCaptionMaxZoom,
    subCaptionHaloColor: Color = Color.White,
    subCaptionRequestedWidth: Dp = 0.dp,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    isForceShowIcon: Boolean = false,
    isForceShowCaption: Boolean = false,
    tag: Any? = null,
    captionText: String? = null,
    captionColor: Color = Color.Black,
    captionTextSize: TextUnit = MarkerDefaults.DefaultCaptionTextSize,
    captionMinZoom: Double = MarkerDefaults.DefaultCaptionMinZoom,
    captionMaxZoom: Double = MarkerDefaults.DefaultCaptionMaxZoom,
    captionAligns: Array<Align> = MarkerDefaults.DefaultCaptionAligns,
    visible: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = MarkerDefaults.DefaultGlobalZIndex,
    onClick: (Marker) -> Boolean = { false },
    onInfoWindowClick: (Marker) -> Unit = {},
    onInfoWindowClose: (Marker) -> Unit = {},
    onInfoWindowLongClick: (Marker) -> Unit = {},
    content: @Composable() ((Marker) -> Unit)? = null,
) {
    MarkerImpl(
        state = state,
        width = width,
        height = height,
        alpha = alpha,
        anchor = anchor,
        isFlat = isFlat,
        icon = icon,
        iconTintColor = iconTintColor,
        infoWindowAnchor = infoWindowAnchor,
        angle = angle,
        minZoom = minZoom,
        isMinZoomInclusive = isMinZoomInclusive,
        maxZoom = maxZoom,
        isMaxZoomInclusive = isMaxZoomInclusive,
        subCaptionText = subCaptionText,
        subCaptionColor = subCaptionColor,
        subCaptionTextSize = subCaptionTextSize,
        subCaptionMinZoom = subCaptionMinZoom,
        subCaptionMaxZoom = subCaptionMaxZoom,
        subCaptionHaloColor = subCaptionHaloColor,
        subCaptionRequestedWidth = subCaptionRequestedWidth,
        isHideCollidedSymbols = isHideCollidedSymbols,
        isHideCollidedMarkers = isHideCollidedMarkers,
        isHideCollidedCaptions = isHideCollidedCaptions,
        isForceShowIcon = isForceShowIcon,
        isForceShowCaption = isForceShowCaption,
        tag = tag,
        captionText = captionText,
        captionColor = captionColor,
        captionTextSize = captionTextSize,
        captionMinZoom = captionMinZoom,
        captionMaxZoom = captionMaxZoom,
        captionAligns = captionAligns,
        visible = visible,
        zIndex = zIndex,
        globalZIndex = globalZIndex,
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
 * @param isFlat sets if the marker should be flat against the map
 * @param icon sets the icon for the marker
 * @param infoWindowAnchor the anchor point of the info window on the marker image
 * @param angle the angle of the marker in degrees clockwise about the marker's anchor point
 * @param subCaptionText the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param captionText the title for the marker
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
    width: Dp = MarkerDefaults.SizeAuto,
    height: Dp = MarkerDefaults.SizeAuto,
    alpha: Float = 1.0f,
    anchor: Offset = Offset(0.5f, 1.0f),
    isFlat: Boolean = false,
    icon: OverlayImage = MarkerDefaults.DefaultIcon,
    iconTintColor: Color = Color.Transparent,
    infoWindowAnchor: Offset = Offset(0.5f, 0.0f),
    angle: Float = 0.0f,
    minZoom: Double = OverlayDefaults.DefaultMinZoom,
    isMinZoomInclusive: Boolean = true,
    maxZoom: Double = OverlayDefaults.DefaultMaxZoom,
    isMaxZoomInclusive: Boolean = true,
    subCaptionText: String? = null,
    subCaptionColor: Color = Color.Black,
    subCaptionTextSize: TextUnit = MarkerDefaults.DefaultCaptionTextSize,
    subCaptionMinZoom: Double = MarkerDefaults.DefaultCaptionMinZoom,
    subCaptionMaxZoom: Double = MarkerDefaults.DefaultCaptionMaxZoom,
    subCaptionHaloColor: Color = Color.White,
    subCaptionRequestedWidth: Dp = 0.dp,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    isForceShowIcon: Boolean = false,
    isForceShowCaption: Boolean = false,
    tag: Any? = null,
    captionText: String? = null,
    captionColor: Color = Color.Black,
    captionTextSize: TextUnit = MarkerDefaults.DefaultCaptionTextSize,
    captionMinZoom: Double = MarkerDefaults.DefaultCaptionMinZoom,
    captionMaxZoom: Double = MarkerDefaults.DefaultCaptionMaxZoom,
    captionAligns: Array<Align> = MarkerDefaults.DefaultCaptionAligns,
    visible: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = MarkerDefaults.DefaultGlobalZIndex,
    onClick: (Marker) -> Boolean = { false },
    onInfoWindowClick: (Marker) -> Unit = {},
    onInfoWindowClose: (Marker) -> Unit = {},
    onInfoWindowLongClick: (Marker) -> Unit = {},
    infoWindow: @Composable() ((Marker) -> Unit)? = null,
    infoContent: @Composable() ((Marker) -> Unit)? = null,
) {
    val mapApplier = currentComposer.applier as? MapApplier
    val density = LocalDensity.current
    ComposeNode<MarkerNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding marker")
            val marker: Marker = Marker().apply {
                this.width = with(density) { width.roundToPx() }
                this.height = with(density) { height.roundToPx() }
                this.alpha = alpha
                this.anchor = PointF(anchor.x, anchor.y)
                this.isFlat = isFlat
                this.icon = icon
                this.iconTintColor = iconTintColor.toArgb()
                this.infoWindow?.anchor = PointF(infoWindowAnchor.x, infoWindowAnchor.y)
                this.position = state.position
                this.angle = angle
                this.minZoom = minZoom
                this.isMinZoomInclusive = isMinZoomInclusive
                this.maxZoom = maxZoom
                this.isMaxZoomInclusive = isMaxZoomInclusive
                this.subCaptionText = subCaptionText.orEmpty()
                this.subCaptionColor = subCaptionColor.toArgb()
                this.subCaptionTextSize = subCaptionTextSize.value
                this.subCaptionMinZoom = subCaptionMinZoom
                this.subCaptionMaxZoom = subCaptionMaxZoom
                this.subCaptionHaloColor = subCaptionHaloColor.toArgb()
                this.subCaptionRequestedWidth = with(density) {
                    subCaptionRequestedWidth.roundToPx()
                }
                this.isHideCollidedSymbols = isHideCollidedSymbols
                this.isHideCollidedMarkers = isHideCollidedMarkers
                this.isHideCollidedCaptions = isHideCollidedCaptions
                this.isForceShowIcon = isForceShowIcon
                this.isForceShowCaption = isForceShowCaption
                this.captionText = captionText.orEmpty()
                this.captionColor = captionColor.toArgb()
                this.captionTextSize = captionTextSize.value
                this.captionMinZoom = captionMinZoom
                this.captionMaxZoom = captionMaxZoom
                this.setCaptionAligns(*captionAligns.map { it.value }.toTypedArray())
                this.isVisible = visible
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
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
                density = density,
                onMarkerClick = onClick,
                onInfoWindowClick = onInfoWindowClick,
                onInfoWindowClose = onInfoWindowClose,
                onInfoWindowLongClick = onInfoWindowLongClick,
                infoWindow = infoWindow,
                infoContent = infoContent,
            )
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onMarkerClick = it }
            update(onInfoWindowClick) { this.onInfoWindowClick = it }
            update(onInfoWindowClose) { this.onInfoWindowClose = it }
            update(onInfoWindowLongClick) { this.onInfoWindowLongClick = it }
            update(infoContent) { this.infoContent = it }
            update(infoWindow) { this.infoWindow = it }

            set(width) {
                this.marker.width = with(this.density) { it.roundToPx() }
            }
            set(height) {
                this.marker.height = with(this.density) { it.roundToPx() }
            }
            set(alpha) { this.marker.alpha = it }
            set(anchor) { this.marker.anchor = PointF(it.x, it.y) }
            set(isFlat) { this.marker.isFlat = it }
            set(icon) { this.marker.icon = it }
            set(iconTintColor) { this.marker.iconTintColor = it.toArgb() }
            set(infoWindowAnchor) { this.marker.infoWindow?.anchor = PointF(it.x, it.y) }
            set(state.position) { this.marker.position = it }
            set(angle) { this.marker.angle = it }
            set(minZoom) { this.marker.minZoom = it }
            set(isMinZoomInclusive) { this.marker.isMinZoomInclusive = it }
            set(maxZoom) { this.marker.maxZoom = it }
            set(isMaxZoomInclusive) { this.marker.isMaxZoomInclusive = it }
            set(subCaptionText) {
                this.marker.subCaptionText = it.orEmpty()
                if (this.marker.isInfoWindowShown) {
                    this.marker.showInfoWindow()
                }
            }
            set(subCaptionColor) { this.marker.subCaptionColor = it.toArgb() }
            set(subCaptionTextSize) { this.marker.subCaptionTextSize = it.value }
            set(subCaptionMinZoom) { this.marker.subCaptionMinZoom = it }
            set(subCaptionMaxZoom) { this.marker.subCaptionMaxZoom = it }
            set(subCaptionHaloColor) { this.marker.subCaptionHaloColor = it.toArgb() }
            set(subCaptionRequestedWidth) {
                this.marker.subCaptionRequestedWidth = with(this.density) { it.roundToPx() }
            }
            set(isHideCollidedSymbols) { this.marker.isHideCollidedSymbols = it }
            set(isHideCollidedMarkers) { this.marker.isHideCollidedMarkers = it }
            set(isHideCollidedCaptions) { this.marker.isHideCollidedCaptions = it }
            set(isForceShowIcon) { this.marker.isForceShowIcon = it }
            set(isForceShowCaption) { this.marker.isForceShowCaption = it }
            set(tag) { this.marker.tag = it }
            set(captionText) {
                this.marker.captionText = it.orEmpty()
                if (this.marker.isInfoWindowShown) {
                    this.marker.showInfoWindow()
                }
            }
            set(captionColor) { this.marker.captionColor = it.toArgb() }
            set(captionTextSize) { this.marker.captionTextSize = it.value }
            set(captionMinZoom) { this.marker.captionMinZoom = it }
            set(captionMaxZoom) { this.marker.captionMaxZoom = it }
            set(captionAligns) {
                this.marker.setCaptionAligns(*it.map { it.value }.toTypedArray())
            }
            set(visible) { this.marker.isVisible = it }
            set(zIndex) { this.marker.zIndex = it }
            set(globalZIndex) { this.marker.globalZIndex = it }
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
