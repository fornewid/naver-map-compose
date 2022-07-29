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
package net.daum.mf.map.api.compose.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Destination(val route: String) {
    Home("Home"),

    Map("Map"),
    Marker("Marker"),
    Polygon("Polygon"),
    Location("Location"),
    Camera("Camera"),
    Events("Events"),
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

        composable(Destination.Map.route) {
            MapScreen(upPress = upPress)
        }
        composable(Destination.Marker.route) {
            MarkerScreen(upPress = upPress)
        }
        composable(Destination.Polygon.route) {
            PolygonScreen(upPress = upPress)
        }
        composable(Destination.Location.route) {
            LocationScreen(upPress = upPress)
        }
        composable(Destination.Camera.route) {
            CameraScreen(upPress = upPress)
        }
        composable(Destination.Events.route) {
            EventsScreen(upPress = upPress)
        }
    }
}
