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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.OverlayImage

internal class LocationOverlayNode(
    val locationOverlay: LocationOverlay,
    var onLocationOverlayClick: (LocationOverlay) -> Boolean,
    var density: Density,
) : MapNode

public object LocationOverlayDefaults {
    public const val DefaultGlobalZIndex: Int = LocationOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * A composable for a location overlay on the map.
 *
 * TODO: (sungyong.an) 설명 추가
 * @param visible the visibility of the polyline
 * @param zIndex the z-index of the polyline
 */
@ExperimentalNaverMapApi
@Composable
public fun LocationOverlay(
    position: LatLng,
    bearing: Float = 0f,
    icon: OverlayImage = LocationOverlay.DEFAULT_ICON,
    iconWidth: Int = LocationOverlay.SIZE_AUTO,
    iconHeight: Int = LocationOverlay.SIZE_AUTO,
    anchor: PointF = LocationOverlay.DEFAULT_ANCHOR,
    subIcon: OverlayImage? = null,
    subIconWidth: Int = LocationOverlay.SIZE_AUTO,
    subIconHeight: Int = LocationOverlay.SIZE_AUTO,
    subAnchor: PointF = LocationOverlay.DEFAULT_ANCHOR,
    circleRadius: Dp = LocationOverlay.DEFAULT_CIRCLE_RADIUS_DP.dp,
    circleColor: Color = Color(LocationOverlay.DEFAULT_CIRCLE_COLOR),
    circleOutlineWidth: Int = 0,
    circleOutlineColor: Color = Color.Transparent,
    tag: Any? = null,
    visible: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = LocationOverlayDefaults.DefaultGlobalZIndex,
    onClick: (LocationOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<LocationOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding Polyline")
            val locationOverlay = map.locationOverlay.apply {
                this.position = position
                this.bearing = bearing
                this.icon = icon
                this.iconWidth = iconWidth
                this.iconHeight = iconHeight
                this.anchor = anchor
                this.subIcon = subIcon
                this.subIconWidth = iconWidth
                this.subIconHeight = iconHeight
                this.subAnchor = subAnchor
                this.circleRadius = with(density) { circleRadius.roundToPx() }
                this.circleColor = circleColor.toArgb()
                this.circleOutlineWidth = circleOutlineWidth
                this.circleOutlineColor = circleOutlineColor.toArgb()
                this.isVisible = visible
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
            }
            locationOverlay.tag = tag
            locationOverlay.setOnClickListener {
                mapApplier
                    .nodeForLocationOverlay(locationOverlay)
                    ?.onLocationOverlayClick
                    ?.invoke(locationOverlay)
                    ?: false
            }
            LocationOverlayNode(locationOverlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onLocationOverlayClick = it }

            set(position) { this.locationOverlay.position = it }
            set(bearing) { this.locationOverlay.bearing = it }
            set(icon) { this.locationOverlay.icon = it }
            set(iconWidth) { this.locationOverlay.iconWidth = it }
            set(iconHeight) { this.locationOverlay.iconHeight = it }
            set(anchor) { this.locationOverlay.anchor = it }
            set(subIcon) { this.locationOverlay.subIcon = it }
            set(subIconWidth) { this.locationOverlay.subIconWidth = it }
            set(subIconHeight) { this.locationOverlay.subIconHeight = it }
            set(subAnchor) { this.locationOverlay.subAnchor = it }
            set(circleRadius) {
                this.locationOverlay.circleRadius = with(this.density) { it.roundToPx() }
            }
            set(circleColor) { this.locationOverlay.circleColor = it.toArgb() }
            set(circleOutlineWidth) { this.locationOverlay.circleOutlineWidth = it }
            set(circleOutlineColor) { this.locationOverlay.circleOutlineColor = it.toArgb() }
            set(visible) { this.locationOverlay.isVisible = it }
            set(tag) { this.locationOverlay.tag = it }
            set(zIndex) { this.locationOverlay.zIndex = it }
            set(globalZIndex) { this.locationOverlay.globalZIndex = it }
        }
    )
}
