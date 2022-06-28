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
package soup.map.compose.macrobenchmark.navermap.app.profiles

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.NaverMapComposable
import com.naver.maps.map.compose.NaverMapConstants
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.PathOverlayDefaults

@Composable
@NaverMapComposable
fun PathOverlaySample() {
    PathOverlay(
        coords = COORDS,
        progress = 0.0,
        width = 10.dp,
        outlineWidth = 2.dp,
        color = Color.Black,
        outlineColor = Color.White,
        passedColor = Color.Gray,
        passedOutlineColor = Color.White,
        patternImage = null,
        patternInterval = 50.dp,
        isHideCollidedSymbols = false,
        isHideCollidedMarkers = false,
        isHideCollidedCaptions = false,
        tag = null,
        visible = true,
        minZoom = NaverMapConstants.MinZoom,
        minZoomInclusive = true,
        maxZoom = NaverMapConstants.MaxZoom,
        maxZoomInclusive = true,
        zIndex = 0,
        globalZIndex = PathOverlayDefaults.GlobalZIndex,
        onClick = { false },
    )
}

private val COORDS = listOf(
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
