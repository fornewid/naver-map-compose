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
package com.naver.maps.map.compose.demo.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.R
import com.naver.maps.map.compose.demo.common.CheckedText
import com.naver.maps.map.compose.demo.common.DefaultTopAppBar
import com.naver.maps.map.compose.demo.common.showToast

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ZoomGesturesEventScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_zoom_gestures_event),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            val consumeDoubleTap = remember { mutableStateOf(false) }
            val consumeTwoFingerTap = remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.label_consume_event),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .requiredWidth(100.dp)
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                )
                CheckedText(
                    text = stringResource(R.string.double_tap),
                    checked = consumeDoubleTap.value,
                    onCheckedChange = { consumeDoubleTap.value = it },
                    modifier = Modifier.wrapContentWidth()
                )
                CheckedText(
                    text = stringResource(R.string.two_finger_tap),
                    checked = consumeTwoFingerTap.value,
                    onCheckedChange = { consumeTwoFingerTap.value = it },
                    modifier = Modifier.wrapContentWidth()
                )
            }

            val context = LocalContext.current
            NaverMap(
                onMapDoubleTab = { _, coord ->
                    context.showToast(
                        R.string.format_map_double_tap,
                        coord.latitude,
                        coord.longitude
                    )
                    consumeDoubleTap.value
                },
                onMapTwoFingerTap = { _, coord ->
                    context.showToast(
                        R.string.format_map_two_finger_tap,
                        coord.latitude,
                        coord.longitude
                    )
                    consumeTwoFingerTap.value
                },
            )
        }
    }
}
