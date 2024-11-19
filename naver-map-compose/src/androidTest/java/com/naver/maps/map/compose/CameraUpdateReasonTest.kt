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
import com.naver.maps.map.CameraUpdate
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
internal class CameraUpdateReasonTest {

    @Test
    fun equals_count_of_CameraUpdateReason() {
        assertEquals(
            expected = 5,
            actual = CameraUpdate::class.publicStaticFields.count {
                it.name.startsWith(prefix = "REASON_")
            },
            message = "The number of static fields CameraUpdate.REASON_* in NaverMap SDK is not exactly 5.",
        )
    }

    @Test
    fun equals_value_of_CameraUpdateReason() {
        val naverReasons = listOf(
            CameraUpdate.REASON_DEVELOPER,
            CameraUpdate.REASON_GESTURE,
            CameraUpdate.REASON_CONTROL,
            CameraUpdate.REASON_LOCATION,
            CameraUpdate.REASON_CONTENT_PADDING,
        )
        val composeReasons = listOf(
            CameraUpdateReason.DEVELOPER,
            CameraUpdateReason.GESTURE,
            CameraUpdateReason.CONTROL,
            CameraUpdateReason.LOCATION,
            CameraUpdateReason.CONTENT_PADDING,
        )
        assertEquals(
            expected = composeReasons,
            actual = naverReasons.map { reason ->
                CameraUpdateReason.fromInt(reason = reason)
            },
        )
    }
}
