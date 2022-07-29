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
package soup.korea.map.compose.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.compose.demo.NaverMapSampleActivity
import net.daum.mf.map.api.compose.demo.DaumMapSampleActivity

class KoreaMapSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = stringResource(R.string.app_name))
                        },
                    )
                }
            ) { contentPadding ->
                val context = LocalContext.current
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = contentPadding
                ) {
                    item {
                        HomeDemoItem(
                            titleResId = R.string.naver_map_demo_title,
                            descriptionResId = R.string.naver_map_demo_desc,
                            onItemClick = {
                                context.startActivity(
                                    Intent(context, NaverMapSampleActivity::class.java)
                                )
                            }
                        )
                    }
                    item {
                        HomeDemoItem(
                            titleResId = R.string.daum_map_demo_title,
                            descriptionResId = R.string.daum_map_demo_desc,
                            onItemClick = {
                                context.startActivity(
                                    Intent(context, DaumMapSampleActivity::class.java)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeDemoItem(
    @StringRes titleResId: Int,
    @StringRes descriptionResId: Int,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable {
            onItemClick()
        }
    ) {
        Text(
            text = stringResource(titleResId),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.Black,
            fontSize = 16.sp,
        )
        Text(
            text = stringResource(descriptionResId),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.Gray,
            fontSize = 14.sp,
        )
    }
}
