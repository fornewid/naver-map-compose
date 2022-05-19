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

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R

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
                    onCheckedChange = { consumeDoubleTap.value = it }
                )
                CheckedText(
                    text = stringResource(R.string.two_finger_tap),
                    checked = consumeTwoFingerTap.value,
                    onCheckedChange = { consumeTwoFingerTap.value = it }
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

@Composable
private fun CheckedText(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        Modifier
            .wrapContentWidth()
            .height(42.dp)
            .toggleable(
                value = checked,
                onValueChange = { onCheckedChange(it) },
                role = Role.Checkbox
            )
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1.merge(),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

private fun Context.showToast(@StringRes resId: Int, vararg formatArgs: Any) {
    Toast.makeText(this, getString(resId, *formatArgs), Toast.LENGTH_SHORT).show()
}
