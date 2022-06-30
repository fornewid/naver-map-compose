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
package com.naver.maps.map.compose.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.naver.maps.map.compose.demo.basic.MapClusteringScreen
import com.naver.maps.map.compose.demo.basic.MapInColumnScreen
import com.naver.maps.map.compose.demo.basic.MapScreen
import com.naver.maps.map.compose.demo.camera.CameraAnimationScreen
import com.naver.maps.map.compose.demo.camera.CameraEventScreen
import com.naver.maps.map.compose.demo.camera.CameraMoveScreen
import com.naver.maps.map.compose.demo.camera.CameraUpdateParamsScreen
import com.naver.maps.map.compose.demo.camera.FitBoundsScreen
import com.naver.maps.map.compose.demo.camera.PivotScreen
import com.naver.maps.map.compose.demo.event.MapClickEventScreen
import com.naver.maps.map.compose.demo.event.OverlayClickEventScreen
import com.naver.maps.map.compose.demo.event.SymbolClickEventScreen
import com.naver.maps.map.compose.demo.event.ZoomGesturesEventScreen
import com.naver.maps.map.compose.demo.location.CustomLocationSourceScreen
import com.naver.maps.map.compose.demo.location.LocationTrackingScreen
import com.naver.maps.map.compose.demo.map.DisplayOptionsScreen
import com.naver.maps.map.compose.demo.map.IndoorMapScreen
import com.naver.maps.map.compose.demo.map.LiteModeScreen
import com.naver.maps.map.compose.demo.map.LocaleScreen
import com.naver.maps.map.compose.demo.map.MapTypesAndLayerGroupsScreen
import com.naver.maps.map.compose.demo.map.NightModeScreen
import com.naver.maps.map.compose.demo.option.ContentPaddingScreen
import com.naver.maps.map.compose.demo.option.ControlSettingsScreen
import com.naver.maps.map.compose.demo.option.ExtentScreen
import com.naver.maps.map.compose.demo.option.GestureSettingsScreen
import com.naver.maps.map.compose.demo.option.MaxTiltScreen
import com.naver.maps.map.compose.demo.option.MinMaxZoomScreen
import com.naver.maps.map.compose.demo.overlay.ArrowheadPathOverlayScreen
import com.naver.maps.map.compose.demo.overlay.CircleOverlayScreen
import com.naver.maps.map.compose.demo.overlay.GlobalZIndexScreen
import com.naver.maps.map.compose.demo.overlay.GroundOverlayScreen
import com.naver.maps.map.compose.demo.overlay.MarkerScreen
import com.naver.maps.map.compose.demo.overlay.MultipartPathOverlayScreen
import com.naver.maps.map.compose.demo.overlay.OverlayCollisionScreen
import com.naver.maps.map.compose.demo.overlay.OverlayMinMaxZoomScreen
import com.naver.maps.map.compose.demo.overlay.PathOverlayScreen
import com.naver.maps.map.compose.demo.overlay.PolygonOverlayScreen
import com.naver.maps.map.compose.demo.overlay.PolylineOverlayScreen

enum class Destination(val route: String) {
    Home("Home"),

    // BASIC
    Map("Map"),
    MapInColumn("MapInColumn"),
    MapClustering("MapClustering"),

    // OVERLAY
    Marker("Marker"),
    PolygonOverlay("PolygonOverlay"),
    PolylineOverlay("PolylineOverlay"),
    CircleOverlay("CircleOverlay"),
    GroundOverlay("GroundOverlay"),
    PathOverlay("PathOverlay"),
    MultipartPathOverlay("MultipartPathOverlay"),
    ArrowheadPathOverlay("ArrowheadPathOverlay"),
    OverlayMinMaxZoom("OverlayMinMaxZoom"),
    GlobalZIndex("GlobalZIndex"),
    OverlayCollision("OverlayCollision"),

    // CAMERA
    CameraMove("CameraMove"),
    CameraAnimation("CameraAnimation"),
    CameraUpdateParams("CameraUpdateParams"),
    FitBounds("FitBounds"),
    Pivot("Pivot"),
    CameraEvent("CameraEvent"),

    // MAP
    MapTypesAndLayerGroups("MapTypesAndLayerGroups"),
    DisplayOptions("DisplayOptions"),
    IndoorMap("IndoorMap"),
    LiteMode("LiteMode"),
    NightMode("NightMode"),
    Locale("Locale"),

    // MAP OPTIONS
    MinMaxZoom("MinMaxZoom"),
    MaxTilt("MaxTilt"),
    Extent("Extent"),
    ContentPadding("ContentPadding"),
    ControlSettings("ControlSettings"),
    GestureSettings("GestureSettings"),

    // MAP EVENT
    MapClickEvent("MapClickEvent"),
    OverlayClickEvent("OverlayClickEvent"),
    SymbolClickEvent("SymbolClickEvent"),
    ZoomGesturesEvent("ZoomGesturesEvent"),

    // LOCATION
    LocationTracking("LocationTracking"),
    CustomLocationSource("CustomLocationSource"),
}

@Composable
fun NavGraph(
    startDestination: String = Destination.Home.route,
) {
    val navController = rememberNavController()
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Destination.Home.route) {
            HomeScreen(
                onItemClick = {
                    navController.navigate(route = it.route)
                }
            )
        }

        // BASIC
        composable(Destination.Map.route) {
            MapScreen(upPress = upPress)
        }
        composable(Destination.MapInColumn.route) {
            MapInColumnScreen(upPress = upPress)
        }
        composable(Destination.MapClustering.route) {
            MapClusteringScreen(upPress = upPress)
        }

        // OVERLAY
        composable(Destination.Marker.route) {
            MarkerScreen(upPress = upPress)
        }
        composable(Destination.PolygonOverlay.route) {
            PolygonOverlayScreen(upPress = upPress)
        }
        composable(Destination.PolylineOverlay.route) {
            PolylineOverlayScreen(upPress = upPress)
        }
        composable(Destination.CircleOverlay.route) {
            CircleOverlayScreen(upPress = upPress)
        }
        composable(Destination.GroundOverlay.route) {
            GroundOverlayScreen(upPress = upPress)
        }
        composable(Destination.PathOverlay.route) {
            PathOverlayScreen(upPress = upPress)
        }
        composable(Destination.MultipartPathOverlay.route) {
            MultipartPathOverlayScreen(upPress = upPress)
        }
        composable(Destination.ArrowheadPathOverlay.route) {
            ArrowheadPathOverlayScreen(upPress = upPress)
        }
        composable(Destination.OverlayMinMaxZoom.route) {
            OverlayMinMaxZoomScreen(upPress = upPress)
        }
        composable(Destination.GlobalZIndex.route) {
            GlobalZIndexScreen(upPress = upPress)
        }
        composable(Destination.OverlayCollision.route) {
            OverlayCollisionScreen(upPress = upPress)
        }

        // CAMERA
        composable(Destination.CameraMove.route) {
            CameraMoveScreen(upPress = upPress)
        }
        composable(Destination.CameraAnimation.route) {
            CameraAnimationScreen(upPress = upPress)
        }
        composable(Destination.CameraUpdateParams.route) {
            CameraUpdateParamsScreen(upPress = upPress)
        }
        composable(Destination.FitBounds.route) {
            FitBoundsScreen(upPress = upPress)
        }
        composable(Destination.Pivot.route) {
            PivotScreen(upPress = upPress)
        }
        composable(Destination.CameraEvent.route) {
            CameraEventScreen(upPress = upPress)
        }

        // MAP
        composable(Destination.MapTypesAndLayerGroups.route) {
            MapTypesAndLayerGroupsScreen(upPress = upPress)
        }
        composable(Destination.DisplayOptions.route) {
            DisplayOptionsScreen(upPress = upPress)
        }
        composable(Destination.IndoorMap.route) {
            IndoorMapScreen(upPress = upPress)
        }
        composable(Destination.LiteMode.route) {
            LiteModeScreen(upPress = upPress)
        }
        composable(Destination.NightMode.route) {
            NightModeScreen(upPress = upPress)
        }
        composable(Destination.Locale.route) {
            LocaleScreen(upPress = upPress)
        }

        // MAP OPTIONS
        composable(Destination.MinMaxZoom.route) {
            MinMaxZoomScreen(upPress = upPress)
        }
        composable(Destination.MaxTilt.route) {
            MaxTiltScreen(upPress = upPress)
        }
        composable(Destination.Extent.route) {
            ExtentScreen(upPress = upPress)
        }
        composable(Destination.ContentPadding.route) {
            ContentPaddingScreen(upPress = upPress)
        }
        composable(Destination.ControlSettings.route) {
            ControlSettingsScreen(upPress = upPress)
        }
        composable(Destination.GestureSettings.route) {
            GestureSettingsScreen(upPress = upPress)
        }

        // MAP EVENT
        composable(Destination.MapClickEvent.route) {
            MapClickEventScreen(upPress = upPress)
        }
        composable(Destination.OverlayClickEvent.route) {
            OverlayClickEventScreen(upPress = upPress)
        }
        composable(Destination.SymbolClickEvent.route) {
            SymbolClickEventScreen(upPress = upPress)
        }
        composable(Destination.ZoomGesturesEvent.route) {
            ZoomGesturesEventScreen(upPress = upPress)
        }

        // LOCATION
        composable(Destination.LocationTracking.route) {
            LocationTrackingScreen(upPress = upPress)
        }
        composable(Destination.CustomLocationSource.route) {
            CustomLocationSourceScreen(upPress = upPress)
        }
    }
}
