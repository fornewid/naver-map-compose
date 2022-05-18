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

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.LocationSource
import com.naver.maps.map.compose.Align
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.GroundOverlay
import com.naver.maps.map.compose.GroundOverlayPosition
import com.naver.maps.map.compose.InfoWindow
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.demo.basic.MapScreen
import com.naver.maps.map.compose.demo.camera.CameraAnimationScreen
import com.naver.maps.map.compose.demo.camera.CameraEventScreen
import com.naver.maps.map.compose.demo.camera.CameraMoveScreen
import com.naver.maps.map.compose.demo.camera.CameraUpdateParamsScreen
import com.naver.maps.map.compose.demo.camera.FitBoundsScreen
import com.naver.maps.map.compose.demo.camera.PivotScreen
import com.naver.maps.map.compose.demo.map.DisplayOptionsScreen
import com.naver.maps.map.compose.demo.map.IndoorMapScreen
import com.naver.maps.map.compose.demo.map.LiteModeScreen
import com.naver.maps.map.compose.demo.map.LocaleScreen
import com.naver.maps.map.compose.demo.map.MapTypesAndLayerGroupsScreen
import com.naver.maps.map.compose.demo.map.NightModeScreen
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
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberInfoWindowState
import com.naver.maps.map.compose.rememberMarkerState
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

enum class Destination(val route: String) {
    Home("Home"),

    // BASIC
    Map("Map"),

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

    Demo("Demo"),
    ;

    companion object {
        fun additionalExampleList(): List<Destination> = values().toList()
    }
}

private const val TAG = "MapSampleActivity"

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
        composable(Destination.Demo.route) {
            MainScreen()
        }

        // MAP EVENT
        composable(Destination.Demo.route) {
            MainScreen()
        }

        // LOCATION
        composable(Destination.Demo.route) {
            MainScreen()
        }

        // MISC
        composable(Destination.Demo.route) {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MainScreen() {
    val locationSource = LocalLocationSource.current
    Box(modifier = Modifier.fillMaxSize()) {
        var isMapLoaded by remember { mutableStateOf(false) }
        val cameraPositionState = rememberCameraPositionState()
        NaverMapView(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            locationSource = locationSource,
            onMapLoaded = {
                isMapLoaded = true
            },
        )
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun NaverMapView(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    locationSource: LocationSource,
    onMapLoaded: () -> Unit,
    content: @Composable () -> Unit = {},
) {
    var uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                scaleBarEnabled = false,
                compassEnabled = true,
                locationButtonEnabled = true
            )
        )
    }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.Basic,
                isIndoorEnabled = true,
                locationTrackingMode = LocationTrackingMode.Follow
            )
        )
    }
    val infoWindowState = rememberInfoWindowState {
        open(position = LatLng(37.5666102, 126.9783881))
    }

    var mapVisible by remember { mutableStateOf(true) }
    if (mapVisible) {
        NaverMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            locationSource = locationSource,
            onMapLoaded = onMapLoaded,
            onMapClick = { point, coord ->
                Log.d(TAG, "onMapClick: point=$point, coord=$coord")
                infoWindowState.open(position = coord)
            }
        ) {

            val context = LocalContext.current
            InfoWindow(
                state = infoWindowState,
                adapter = InfoWindowAdapter(context),
                onClick = {
                    infoWindowState.close()
                    true
                }
            )
            Marker(
                state = rememberMarkerState(position = LatLng(37.5666102, 126.9783881)),
            )
            Marker(
                state = rememberMarkerState(position = LatLng(37.57000, 126.97618)),
                icon = MarkerIcons.BLACK,
//                rotation = 315f,
                tag = "Marker 1",
                onClick = {
                    infoWindowState.open(it)
                    true
                }
            )
            Marker(
                state = rememberMarkerState(position = LatLng(37.56138, 126.97970)),
                icon = MarkerIcons.BLACK,
                angle = 270f,
                tag = "Marker 2",
                onClick = {
                    infoWindowState.open(it, Align.Left)
                    true
                }
            )
            GroundOverlay(
                position = GroundOverlayPosition.create(
                    bounds = LatLngBounds(
                        LatLng(37.57023, 126.97672),
                        LatLng(37.57545, 126.98323)
                    )
                ),
                alpha = 0.8f,
            )
            GroundOverlay(
                position = GroundOverlayPosition.create(
                    bounds = LatLngBounds(
                        LatLng(37.566351, 126.977234),
                        LatLng(37.568528, 126.97998)
                    )
                ),
                image = OverlayImage.fromResource(R.drawable.ground_overlay)
            )
            PathOverlay(
                coords = COORDS_1,
                width = dimensionResource(R.dimen.path_overlay_width),
                outlineWidth = dimensionResource(R.dimen.path_overlay_outline_width),
                color = MaterialTheme.colors.primary,
                outlineColor = Color.White,
                passedColor = Color.Gray,
                passedOutlineColor = Color.White,
                progress = 0.3,
            )
            PathOverlay(
                coords = COORDS_2,
                width = dimensionResource(R.dimen.path_overlay_width),
                outlineWidth = 0.dp,
                color = Color.Blue,
                patternImage = OverlayImage.fromResource(R.drawable.path_pattern),
                patternInterval = dimensionResource(R.dimen.overlay_pattern_interval),
            )

            content()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        MapButton(
            text = "Toggle Map",
            onClick = { mapVisible = !mapVisible },
            modifier = Modifier.testTag("toggleMapVisibility"),
        )
    }
}

@Composable
private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier.padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary
        ),
        onClick = onClick
    ) {
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}

private class InfoWindowAdapter(context: Context) : InfoWindow.DefaultTextAdapter(context) {
    override fun getText(infoWindow: InfoWindow): String {
        val marker = infoWindow.marker
        return if (marker != null) {
            context.getString(R.string.format_info_window_on_marker, marker.tag)
        } else {
            context.getString(
                R.string.format_info_window_on_map,
                infoWindow.position.latitude, infoWindow.position.longitude
            )
        }
    }
}

private val COORDS_1 = listOf(
    LatLng(37.5594084, 126.9745830),
    LatLng(37.5599980, 126.9748245),
    LatLng(37.5601083, 126.9748951),
    LatLng(37.5601980, 126.9749873),
    LatLng(37.5601998, 126.9749896),
    LatLng(37.5602478, 126.9750492),
    LatLng(37.5603158, 126.9751371),
    LatLng(37.5604241, 126.9753616),
    LatLng(37.5604853, 126.9755401),
    LatLng(37.5605225, 126.9756157),
    LatLng(37.5605353, 126.9756405),
    LatLng(37.5605652, 126.9756924),
    LatLng(37.5606143, 126.9757679),
    LatLng(37.5606903, 126.9758432),
    LatLng(37.5608510, 126.9758919),
    LatLng(37.5611353, 126.9759964),
    LatLng(37.5611949, 126.9760186),
    LatLng(37.5612383, 126.9760364),
    LatLng(37.5615796, 126.9761721),
    LatLng(37.5619326, 126.9763123),
    LatLng(37.5621502, 126.9763991),
    LatLng(37.5622776, 126.9764492),
    LatLng(37.5624374, 126.9765137),
    LatLng(37.5630911, 126.9767753),
    LatLng(37.5631345, 126.9767931),
    LatLng(37.5635163, 126.9769240),
    LatLng(37.5635506, 126.9769351),
    LatLng(37.5638061, 126.9770239),
    LatLng(37.5639153, 126.9770605),
    LatLng(37.5639577, 126.9770749),
    LatLng(37.5640074, 126.9770927),
    LatLng(37.5644783, 126.9771755),
    LatLng(37.5649229, 126.9772482),
    LatLng(37.5650330, 126.9772667),
    LatLng(37.5652152, 126.9772971),
    LatLng(37.5654569, 126.9773170),
    LatLng(37.5655173, 126.9773222),
    LatLng(37.5656534, 126.9773258),
    LatLng(37.5660418, 126.9773004),
    LatLng(37.5661985, 126.9772914),
    LatLng(37.5664663, 126.9772952),
    LatLng(37.5668827, 126.9773047),
    LatLng(37.5669467, 126.9773054),
    LatLng(37.5670567, 126.9773080),
    LatLng(37.5671360, 126.9773097),
    LatLng(37.5671910, 126.9773116),
    LatLng(37.5672785, 126.9773122),
    LatLng(37.5674668, 126.9773120),
    LatLng(37.5677264, 126.9773124),
    LatLng(37.5680410, 126.9773068),
    LatLng(37.5689242, 126.9772871),
    LatLng(37.5692829, 126.9772698),
    LatLng(37.5693829, 126.9772669),
    LatLng(37.5696659, 126.9772615),
    LatLng(37.5697524, 126.9772575),
    LatLng(37.5698659, 126.9772499),
    LatLng(37.5699671, 126.9773070),
    LatLng(37.5700151, 126.9773395),
    LatLng(37.5700748, 126.9773866),
    LatLng(37.5701164, 126.9774373),
    LatLng(37.5701903, 126.9776225),
    LatLng(37.5701905, 126.9776723),
    LatLng(37.5701897, 126.9777006),
    LatLng(37.5701869, 126.9784990),
    LatLng(37.5701813, 126.9788591),
    LatLng(37.5701770, 126.9791139),
    LatLng(37.5701741, 126.9792702),
    LatLng(37.5701743, 126.9793098),
    LatLng(37.5701752, 126.9795182),
    LatLng(37.5701761, 126.9799315),
    LatLng(37.5701775, 126.9800380),
    LatLng(37.5701800, 126.9804048),
    LatLng(37.5701832, 126.9809189),
    LatLng(37.5701845, 126.9810197),
    LatLng(37.5701862, 126.9811986),
    LatLng(37.5701882, 126.9814375),
    LatLng(37.5701955, 126.9820897),
    LatLng(37.5701996, 126.9821860),
)

private val COORDS_2 = listOf(
    LatLng(37.5660645, 126.9826732),
    LatLng(37.5660294, 126.9826723),
    LatLng(37.5658526, 126.9826611),
    LatLng(37.5658040, 126.9826580),
    LatLng(37.5657697, 126.9826560),
    LatLng(37.5654413, 126.9825880),
    LatLng(37.5652157, 126.9825273),
    LatLng(37.5650560, 126.9824843),
    LatLng(37.5647789, 126.9824114),
    LatLng(37.5646788, 126.9823861),
    LatLng(37.5644062, 126.9822963),
    LatLng(37.5642519, 126.9822566),
    LatLng(37.5641517, 126.9822312),
    LatLng(37.5639965, 126.9821915),
    LatLng(37.5636536, 126.9820920),
    LatLng(37.5634424, 126.9820244),
    LatLng(37.5633241, 126.9819890),
    LatLng(37.5632772, 126.9819712),
    LatLng(37.5629404, 126.9818433),
    LatLng(37.5627733, 126.9817584),
    LatLng(37.5626694, 126.9816980),
    LatLng(37.5624588, 126.9815738),
    LatLng(37.5620376, 126.9813140),
    LatLng(37.5619426, 126.9812252),
    LatLng(37.5613227, 126.9814831),
    LatLng(37.5611995, 126.9815372),
    LatLng(37.5609414, 126.9816749),
    LatLng(37.5606785, 126.9817390),
    LatLng(37.5605659, 126.9817499),
    LatLng(37.5604892, 126.9817459),
    LatLng(37.5604540, 126.9817360),
    LatLng(37.5603484, 126.9816993),
    LatLng(37.5602092, 126.9816097),
    LatLng(37.5600048, 126.9814390),
    LatLng(37.5599702, 126.9813612),
    LatLng(37.5599401, 126.9812923),
    LatLng(37.5597114, 126.9807346),
    LatLng(37.5596905, 126.9806826),
    LatLng(37.5596467, 126.9805663),
    LatLng(37.5595203, 126.9801199),
    LatLng(37.5594901, 126.9800149),
    LatLng(37.5594544, 126.9798883),
    LatLng(37.5594186, 126.9797436),
    LatLng(37.5593948, 126.9796634),
    LatLng(37.5593132, 126.9793526),
    LatLng(37.5592831, 126.9792622),
    LatLng(37.5590904, 126.9788854),
    LatLng(37.5589081, 126.9786365),
    LatLng(37.5587088, 126.9784125),
    LatLng(37.5586699, 126.9783698),
)
