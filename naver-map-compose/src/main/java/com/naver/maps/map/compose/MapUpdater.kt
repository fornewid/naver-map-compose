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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.naver.maps.map.LocationSource
import com.naver.maps.map.NaverMap
import java.util.Locale

internal class MapPropertiesNode(
    val map: NaverMap,
    cameraPositionState: CameraPositionState,
    var clickListeners: MapClickListeners,
    var density: Density,
    var layoutDirection: LayoutDirection,
) : MapNode {

    init {
        cameraPositionState.setMap(map)
    }

    var cameraPositionState: CameraPositionState = cameraPositionState
        set(value) {
            if (value == field) return
            field.setMap(null)
            field = value
            value.setMap(map)
        }

    private val cameraIdleListener = NaverMap.OnCameraIdleListener {
        this.cameraPositionState.isMoving = false
        // setOnCameraMoveListener is only invoked when the camera position
        // is changed via .animate(). To handle updating state when .move()
        // is used, it's necessary to set the camera's position here as well
        this.cameraPositionState.rawPosition = map.cameraPosition
    }
    private val cameraChangeListener = NaverMap.OnCameraChangeListener { reason, animated ->
        cameraPositionState.cameraUpdateReason = CameraUpdateReason.fromInt(reason)
        cameraPositionState.isMoving = true
        cameraPositionState.rawPosition = map.cameraPosition
    }
    private val loadListener = NaverMap.OnLoadListener {
        clickListeners.onMapLoaded()
    }
    private val locationChangeListener = NaverMap.OnLocationChangeListener {
        clickListeners.onLocationChange(it)
    }
    private val optionChangeListener = NaverMap.OnOptionChangeListener {
        clickListeners.onOptionChange()
    }
    private val indoorSelectionChangeListener = NaverMap.OnIndoorSelectionChangeListener {
        clickListeners.onIndoorSelectionChange(it)
    }

    override fun onAttached() {
        map.setOnMapClickListener { point, coord ->
            clickListeners.onMapClick(point, coord)
        }
        map.setOnMapLongClickListener { point, coord ->
            clickListeners.onMapLongClick(point, coord)
        }
        map.setOnMapDoubleTapListener { point, coord ->
            clickListeners.onMapDoubleTab(point, coord)
        }
        map.setOnMapTwoFingerTapListener { point, coord ->
            clickListeners.onMapTwoFingerTap(point, coord)
        }
        map.setOnSymbolClickListener {
            clickListeners.onSymbolClick(it)
        }
        map.addOnLoadListener(loadListener)
        map.addOnCameraIdleListener(cameraIdleListener)
        map.addOnCameraChangeListener(cameraChangeListener)
        map.addOnLocationChangeListener(locationChangeListener)
        map.addOnOptionChangeListener(optionChangeListener)
        map.addOnIndoorSelectionChangeListener(indoorSelectionChangeListener)
    }

    override fun onRemoved() {
        cameraPositionState.setMap(null)
    }

    override fun onCleared() {
        map.removeOnLoadListener(loadListener)
        map.removeOnCameraIdleListener(cameraIdleListener)
        map.removeOnCameraChangeListener(cameraChangeListener)
        map.removeOnLocationChangeListener(locationChangeListener)
        map.removeOnOptionChangeListener(optionChangeListener)
        map.removeOnIndoorSelectionChangeListener(indoorSelectionChangeListener)

        cameraPositionState.setMap(null)
    }
}

internal val NoPadding = PaddingValues()

/**
 * Used to keep the primary map properties up to date. This should never leave the map composition.
 */
@Suppress("NOTHING_TO_INLINE")
@Composable
internal inline fun MapUpdater(
    cameraPositionState: CameraPositionState,
    clickListeners: MapClickListeners,
    contentPadding: PaddingValues = NoPadding,
    locationSource: LocationSource?,
    locale: Locale? = null,
    mapProperties: MapProperties,
    mapUiSettings: MapUiSettings,
) {
    val map = (currentComposer.applier as MapApplier).map
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    ComposeNode<MapPropertiesNode, MapApplier>(
        factory = {
            MapPropertiesNode(
                map = map,
                cameraPositionState = cameraPositionState,
                clickListeners = clickListeners,
                density = density,
                layoutDirection = layoutDirection,
            )
        }
    ) {
        // The node holds density and layoutDirection so that the updater blocks can be
        // non-capturing, allowing the compiler to turn them into singletons
        update(density) { this.density = it }
        update(layoutDirection) { this.layoutDirection = it }
        set(locale) { map.locale = it }

        set(mapProperties.extent) { map.extent = it }
        set(mapProperties.minZoom) { map.minZoom = it }
        set(mapProperties.maxZoom) { map.maxZoom = it }
        set(mapProperties.maxTilt) { map.maxTilt = it }
        set(contentPadding) {
            val node = this
            with(this.density) {
                map.setContentPadding(
                    it.calculateLeftPadding(node.layoutDirection).roundToPx(),
                    it.calculateTopPadding().roundToPx(),
                    it.calculateRightPadding(node.layoutDirection).roundToPx(),
                    it.calculateBottomPadding().roundToPx()
                )
            }
        }
        set(mapProperties.defaultCameraAnimationDuration) {
            map.defaultCameraAnimationDuration = it
        }
        update(cameraPositionState) { this.cameraPositionState = it }

        set(mapUiSettings.pickTolerance) {
            with(this.density) {
                map.uiSettings.pickTolerance = it.roundToPx()
            }
        }
        set(mapUiSettings.isScrollGesturesEnabled) { map.uiSettings.isScrollGesturesEnabled = it }
        set(mapUiSettings.isZoomGesturesEnabled) { map.uiSettings.isZoomGesturesEnabled = it }
        set(mapUiSettings.isTiltGesturesEnabled) { map.uiSettings.isTiltGesturesEnabled = it }
        set(mapUiSettings.isRotateGesturesEnabled) { map.uiSettings.isRotateGesturesEnabled = it }
        set(mapUiSettings.isStopGesturesEnabled) { map.uiSettings.isStopGesturesEnabled = it }
        set(mapUiSettings.scrollGesturesFriction) { map.uiSettings.scrollGesturesFriction = it }
        set(mapUiSettings.zoomGesturesFriction) { map.uiSettings.zoomGesturesFriction = it }
        set(mapUiSettings.rotateGesturesFriction) { map.uiSettings.rotateGesturesFriction = it }
        set(mapUiSettings.isCompassEnabled) { map.uiSettings.isCompassEnabled = it }
        set(mapUiSettings.isScaleBarEnabled) { map.uiSettings.isScaleBarEnabled = it }
        set(mapUiSettings.isZoomControlEnabled) { map.uiSettings.isZoomControlEnabled = it }
        set(mapUiSettings.isIndoorLevelPickerEnabled) {
            map.uiSettings.isIndoorLevelPickerEnabled = it
        }
        set(mapUiSettings.isLocationButtonEnabled) { map.uiSettings.isLocationButtonEnabled = it }
        set(mapUiSettings.isLogoClickEnabled) { map.uiSettings.isLogoClickEnabled = it }
        set(mapUiSettings.logoGravity) { map.uiSettings.logoGravity = it }
        set(mapUiSettings.logoMargin) {
            val node = this
            with(this.density) {
                map.uiSettings.setLogoMargin(
                    it.calculateLeftPadding(node.layoutDirection).roundToPx(),
                    it.calculateTopPadding().roundToPx(),
                    it.calculateRightPadding(node.layoutDirection).roundToPx(),
                    it.calculateBottomPadding().roundToPx()
                )
            }
        }
        set(mapProperties.fpsLimit) { map.fpsLimit = it }

        set(mapProperties.isIndoorEnabled) { map.isIndoorEnabled = it }
        set(mapProperties.mapType) { map.mapType = it.value }
        set(mapProperties.isBuildingLayerGroupEnabled) {
            map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, it)
        }
        set(mapProperties.isTransitLayerGroupEnabled) {
            map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, it)
        }
        set(mapProperties.isBicycleLayerGroupEnabled) {
            map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, it)
        }
        set(mapProperties.isTrafficLayerGroupEnabled) {
            map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, it)
        }
        set(mapProperties.isCadastralLayerGroupEnabled) {
            map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, it)
        }
        set(mapProperties.isMountainLayerGroupEnabled) {
            map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, it)
        }
        set(mapProperties.isLiteModeEnabled) { map.isLiteModeEnabled = it }
        set(mapProperties.isNightModeEnabled) { map.isNightModeEnabled = it }
        set(mapProperties.buildingHeight) { map.buildingHeight = it }
        set(mapProperties.lightness) { map.lightness = it }
        set(mapProperties.symbolScale) { map.symbolScale = it }
        set(mapProperties.symbolPerspectiveRatio) { map.symbolPerspectiveRatio = it }
        set(mapProperties.indoorFocusRadius) {
            with(this.density) {
                map.indoorFocusRadius = it.roundToPx()
            }
        }
        set(mapProperties.backgroundColor) { map.backgroundColor = it.toArgb() }
        set(mapProperties.backgroundResource) { map.setBackgroundResource(it) }
        set(locationSource) { map.locationSource = it }
        set(mapProperties.locationTrackingMode) { map.locationTrackingMode = it.value }

        update(clickListeners) { this.clickListeners = it }
    }
}
