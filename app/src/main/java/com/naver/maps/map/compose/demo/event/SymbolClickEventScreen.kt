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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.demo.DefaultTopAppBar
import com.naver.maps.map.compose.demo.R

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun SymbolClickEventScreen(upPress: () -> Unit) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                text = stringResource(R.string.name_symbol_click_event),
                upPress = upPress
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val context = LocalContext.current
            NaverMap(
                onMapClick = { _, coord ->
                    context.showToast(
                        R.string.format_map_click,
                        coord.latitude,
                        coord.longitude
                    )
                },
                onSymbolClick = {
                    context.showToast(
                        R.string.format_symbol_click,
                        it.caption,
                        it.position.latitude,
                        it.position.longitude
                    )
                    true
                }
            )
        }
    }
}

private fun Context.showToast(@StringRes resId: Int, vararg formatArgs: Any) {
    Toast.makeText(this, getString(resId, *formatArgs), Toast.LENGTH_SHORT).show()
}
