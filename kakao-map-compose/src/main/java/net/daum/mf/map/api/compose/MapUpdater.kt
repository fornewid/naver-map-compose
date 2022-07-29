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
package net.daum.mf.map.api.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import net.daum.mf.map.api.CameraPosition
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

internal class MapPropertiesNode(
    val map: MapView,
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

    override fun onAttached() {
        map.setMapViewEventListener(
            object : MapView.MapViewEventListener {
                override fun onMapViewInitialized(mapView: MapView) {
                    clickListeners.onMapInitialized()
                }

                override fun onMapViewCenterPointMoved(mapView: MapView, mapCenterPoint: MapPoint) {
                    clickListeners.onMapCenterPointMoved(mapCenterPoint)
                }

                override fun onMapViewZoomLevelChanged(mapView: MapView, zoomLevel: Int) {
                    clickListeners.onMapZoomLevelChanged(zoomLevel)
                }

                override fun onMapViewSingleTapped(mapView: MapView, mapPoint: MapPoint) {
                    clickListeners.onMapSingleTapped(mapPoint)
                }

                override fun onMapViewDoubleTapped(mapView: MapView, mapPoint: MapPoint) {
                    clickListeners.onMapDoubleTapped(mapPoint)
                }

                override fun onMapViewLongPressed(mapView: MapView, mapPoint: MapPoint) {
                    clickListeners.onMapLongPressed(mapPoint)
                }

                override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {
                    cameraPositionState.isMoving = true
                    cameraPositionState.rawPosition =
                        CameraPosition(mapPoint, mapView.zoomLevel.toFloat())
                }

                override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {
                    cameraPositionState.isMoving = false
                    cameraPositionState.rawPosition =
                        CameraPosition(mapPoint, mapView.zoomLevel.toFloat())
                }

                override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
                    cameraPositionState.isMoving = false
                    cameraPositionState.rawPosition =
                        CameraPosition(mapPoint, mapView.zoomLevel.toFloat())
                }
            }
        )
        map.setCurrentLocationEventListener(object : MapView.CurrentLocationEventListener {

            override fun onCurrentLocationUpdate(
                mapView: MapView,
                currentLocation: MapPoint,
                accuracyInMeters: Float,
            ) {
                // TODO:
            }

            override fun onCurrentLocationDeviceHeadingUpdate(
                mapView: MapView,
                headingAngle: Float,
            ) {
                // TODO:
            }

            override fun onCurrentLocationUpdateFailed(mapView: MapView) {
                // TODO:
            }

            override fun onCurrentLocationUpdateCancelled(mapView: MapView) {
                // TODO:
            }
        })
        map.setPOIItemEventListener(object : MapView.POIItemEventListener {
            override fun onPOIItemSelected(mapView: MapView, poiItem: MapPOIItem?) {
                // TODO:
            }

            @Deprecated("deprecated")
            override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView, poiItem: MapPOIItem) {
            }

            override fun onCalloutBalloonOfPOIItemTouched(
                mapView: MapView,
                poiItem: MapPOIItem,
                buttonType: MapPOIItem.CalloutBalloonButtonType,
            ) {
                // TODO:
            }

            override fun onDraggablePOIItemMoved(
                mapView: MapView,
                poiItem: MapPOIItem,
                newMapPoint: MapPoint?,
            ) {
                // TODO:
            }
        })
    }

    override fun onRemoved() {
        cameraPositionState.setMap(null)
    }

    override fun onCleared() {
        cameraPositionState.setMap(null)
    }
}

internal val NoPadding = PaddingValues()

@Suppress("NOTHING_TO_INLINE")
@Composable
internal inline fun MapUpdater(
    cameraPositionState: CameraPositionState,
    clickListeners: MapClickListeners,
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

        update(cameraPositionState) { this.cameraPositionState = it }

//        set(mapUiSettings.isScrollGesturesEnabled) { map.uiSettings.isScrollGesturesEnabled = it }
        set(mapProperties.mapType) { map.mapType = it.value }
        set(mapProperties.mapTileMode) { map.mapTileMode = it.value }

        update(clickListeners) { this.clickListeners = it }
    }
}
