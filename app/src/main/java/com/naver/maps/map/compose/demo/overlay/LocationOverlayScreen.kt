/*
 * Copyright 2023 SOUP
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
package com.naver.maps.map.compose.demo.overlay

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LatLng
import com.naver.maps.map.compose.LocationOverlay
import com.naver.maps.map.compose.LocationOverlayDefaults
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun LocationOverlayScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_location_overlay),
                upPress = upPress,
            )
        },
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            var currentBearing: Float by remember { mutableStateOf(0f) }
            Row(
                modifier = Modifier.requiredHeight(40.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.label_bearing),
                    modifier = Modifier.padding(start = 8.dp),
                )
                Slider(
                    value = currentBearing,
                    onValueChange = { progress ->
                        currentBearing = progress
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    valueRange = 0f..360f,
                )
                Text(
                    text = stringResource(R.string.format_bearing, currentBearing.toInt()),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .requiredWidth(40.dp),
                )
            }

            val cameraPositionState = rememberCameraPositionState()
            var currentPosition: LatLng by remember {
                mutableStateOf(cameraPositionState.position.target)
            }
            val circleAnimationState = remember { CircleAnimationState() }
            val coroutineScope = rememberCoroutineScope()
            NaverMap(
                contentPadding = contentPadding,
                onMapClick = { _, coord ->
                    currentPosition = coord
                    coroutineScope.launch {
                        circleAnimationState.animateCircle()
                    }
                },
            ) {
                LocationOverlay(
                    position = currentPosition,
                    bearing = currentBearing,
                    subIcon = LocationOverlayDefaults.DefaultSubIconArrow,
                    circleOutlineWidth = 0.dp,
                    circleRadius = circleAnimationState.circleRadius,
                    circleColor = circleAnimationState.circleColor,
                    onClick = {
                        coroutineScope.launch {
                            circleAnimationState.animateCircle()
                        }
                        true
                    },
                )
            }
            LaunchedEffect(Unit) {
                circleAnimationState.animateCircle()
            }
        }
    }
}

@Stable
private class CircleAnimationState {

    private val fraction = Animatable(0f)

    val circleRadius: Dp by derivedStateOf {
        lerp(start = 0.dp, stop = 100.dp, fraction = fraction.value)
    }

    val circleColor: Color by derivedStateOf {
        lerp(
            start = Color(red = 148, green = 186, blue = 250, alpha = 127),
            stop = Color(red = 148, green = 186, blue = 250, alpha = 0),
            fraction = fraction.value,
        )
    }

    suspend fun animateCircle() {
        fraction.snapTo(0f)
        fraction.animateTo(
            targetValue = 1f,
            animationSpec = repeatable(iterations = 2, tween(durationMillis = 1000)),
        )
    }
}
