/*
 * Copyright 2024 SOUP
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

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import com.naver.maps.map.NaverMap.MapType as NaverMapType
import com.naver.maps.map.compose.MapType as ComposeMapType

@RunWith(AndroidJUnit4::class)
internal class MapTypeTest {

    @Test
    fun equals() {
        val naverMapTypes = NaverMapType.entries.map { it.name }.sorted()
        val composeMapTypes = sealedSubclasses<ComposeMapType>().map { it.toString() }.sorted()
        assertEquals(
            expected = naverMapTypes,
            actual = composeMapTypes,
            message = "The MapType class of NaverMap SDK and naver-map-compose do not match exactly.",
        )
    }
}
