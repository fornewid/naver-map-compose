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
) : MapNode {

    override fun onAttached() {
        markerState.marker = marker
    }

    override fun onRemoved() {
        markerState.marker = null
        marker.map = null
    }

    override fun onCleared() {
        markerState.marker = null
        marker.map = null
    }
}

public object MarkerDefaults {
    public const val GlobalZIndex: Int = Marker.DEFAULT_GLOBAL_Z_INDEX
    public val SizeAuto: Dp = Marker.SIZE_AUTO.dp
    public val Icon: OverlayImage = Marker.DEFAULT_ICON
    public val Anchor: PointF = Marker.DEFAULT_ANCHOR
    public val CaptionTextSize: TextUnit = Marker.DEFAULT_CAPTION_TEXT_SIZE.sp
    public val CaptionAligns: Array<Align> = arrayOf(Align.Bottom)
    public const val CaptionMinZoom: Double = NaverMap.MINIMUM_ZOOM.toDouble()
    public const val CaptionMaxZoom: Double = NaverMap.MAXIMUM_ZOOM.toDouble()
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
 * @param angle the angle of the marker in degrees clockwise about the marker's anchor point
 * @param subCaptionText the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param captionText the title for the marker
 * @param visible the visibility of the marker
 * @param zIndex the z-index of the marker
 * @param onClick a lambda invoked when the marker is clicked
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
    icon: OverlayImage = MarkerDefaults.Icon,
    iconTintColor: Color = Color.Transparent,
    angle: Float = 0.0f,
    minZoom: Double = NaverMapDefaults.MinZoom,
    isMinZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapDefaults.MaxZoom,
    isMaxZoomInclusive: Boolean = true,
    subCaptionText: String? = null,
    subCaptionColor: Color = Color.Black,
    subCaptionTextSize: TextUnit = MarkerDefaults.CaptionTextSize,
    subCaptionMinZoom: Double = MarkerDefaults.CaptionMinZoom,
    subCaptionMaxZoom: Double = MarkerDefaults.CaptionMaxZoom,
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
    captionTextSize: TextUnit = MarkerDefaults.CaptionTextSize,
    captionMinZoom: Double = MarkerDefaults.CaptionMinZoom,
    captionMaxZoom: Double = MarkerDefaults.CaptionMaxZoom,
    captionAligns: Array<Align> = MarkerDefaults.CaptionAligns,
    visible: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = MarkerDefaults.GlobalZIndex,
    onClick: (Marker) -> Boolean = { false },
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
 * @param angle the angle of the marker in degrees clockwise about the marker's anchor point
 * @param subCaptionText the snippet for the marker
 * @param tag optional tag to associate with the marker
 * @param captionText the title for the marker
 * @param visible the visibility of the marker
 * @param zIndex the z-index of the marker
 * @param onClick a lambda invoked when the marker is clicked
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
    icon: OverlayImage = MarkerDefaults.Icon,
    iconTintColor: Color = Color.Transparent,
    angle: Float = 0.0f,
    minZoom: Double = NaverMapDefaults.MinZoom,
    isMinZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapDefaults.MaxZoom,
    isMaxZoomInclusive: Boolean = true,
    subCaptionText: String? = null,
    subCaptionColor: Color = Color.Black,
    subCaptionTextSize: TextUnit = MarkerDefaults.CaptionTextSize,
    subCaptionMinZoom: Double = MarkerDefaults.CaptionMinZoom,
    subCaptionMaxZoom: Double = MarkerDefaults.CaptionMaxZoom,
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
    captionTextSize: TextUnit = MarkerDefaults.CaptionTextSize,
    captionMinZoom: Double = MarkerDefaults.CaptionMinZoom,
    captionMaxZoom: Double = MarkerDefaults.CaptionMaxZoom,
    captionAligns: Array<Align> = MarkerDefaults.CaptionAligns,
    visible: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = MarkerDefaults.GlobalZIndex,
    onClick: (Marker) -> Boolean = { false },
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
            )
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onMarkerClick = it }

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
            set(state.position) { this.marker.position = it }
            set(angle) { this.marker.angle = it }
            set(minZoom) { this.marker.minZoom = it }
            set(isMinZoomInclusive) { this.marker.isMinZoomInclusive = it }
            set(maxZoom) { this.marker.maxZoom = it }
            set(isMaxZoomInclusive) { this.marker.isMaxZoomInclusive = it }
            set(subCaptionText) {
                this.marker.subCaptionText = it.orEmpty()
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
            }
            set(captionColor) { this.marker.captionColor = it.toArgb() }
            set(captionTextSize) { this.marker.captionTextSize = it.value }
            set(captionMinZoom) { this.marker.captionMinZoom = it }
            set(captionMaxZoom) { this.marker.captionMaxZoom = it }
            set(captionAligns) { aligns ->
                this.marker.setCaptionAligns(*aligns.map { it.value }.toTypedArray())
            }
            set(visible) { this.marker.isVisible = it }
            set(zIndex) { this.marker.zIndex = it }
            set(globalZIndex) { this.marker.globalZIndex = it }
        }
    )
}
