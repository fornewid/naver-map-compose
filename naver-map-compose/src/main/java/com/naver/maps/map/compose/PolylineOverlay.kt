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
package com.naver.maps.map.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Overlay.InvalidCoordinateException
import com.naver.maps.map.overlay.PolylineOverlay

internal class PolylineOverlayNode(
    val overlay: PolylineOverlay,
    var onPolylineOverlayClick: (PolylineOverlay) -> Boolean,
    var density: Density,
) : MapNode {
    override fun onRemoved() {
        overlay.map = null
    }
}

public object PolylineOverlayDefaults {

    /**
     * 기본 전역 Z 인덱스.
     */
    public const val GlobalZIndex: Int = PolylineOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * 지도 상의 [PolylineOverlay]에 대한 [Composable]입니다.
 *
 * @param coords 좌표열을 지정합니다. 만약 coords의 크기가 2 미만이면 [IllegalArgumentException]이 발생합니다.
 * coords에 null이거나 유효하지 않은([LatLng.isValid]가 false인) 좌표가 있다면 [InvalidCoordinateException]이
 * 발생합니다.
 * @param width 두께를 지정합니다. 기본값은 5입니다.
 * @param color 색상을 지정합니다. 기본값은 [Color.Black]입니다.
 * @param pattern 점선 패턴을 지정합니다. 패턴은 픽셀 단위의 배열로 표현되며, 각각 2n번째 요소는 실선의 길이,
 * 2n + 1번째 요소는 공백의 길이를 의미합니다. 빈 배열일 경우 실선이 됩니다. 기본값은 빈 배열입니다.
 * @param capType 끝 지점의 모양을 지정합니다. 기본값은 [LineCap.Butt]입니다.
 * @param joinType 연결점의 모양을 지정합니다. 기본값은 [LineJoin.Miter]입니다.
 * @param tag 태그를 지정합니다. 기본값은 null입니다.
 * @param visible 가시성을 지정합니다. 가시성이 false일 경우 오버레이는 화면에 나타나지 않으며 이벤트도 받지 못합니다.
 * 가시성은 명시적으로 지정하지 않는 한 변하지 않습니다. 즉, 오버레이가 현재 보이는 지도 영역의 바깥쪽으로 나가더라도 가시성이
 * false로 변하지는 않습니다. 기본값은 true입니다.
 * @param minZoom 오버레이가 보이는 최소 줌 레벨을 지정합니다. 기본값은 [NaverMapConstants.MinZoom]입니다.
 * @param minZoomInclusive 지도의 줌 레벨과 오버레이의 최소 줌 레벨이 동일할 때 오버레이를 보일지 여부를 지정합니다.
 * 만약 inclusive가 true이면 오버레이가 나타나고 false이면 나타나지 않습니다. 기본값은 true입니다.
 * @param maxZoom 오버레이가 보이는 최대 줌 레벨을 지정합니다. 기본값은 [NaverMapConstants.MaxZoom]입니다.
 * @param maxZoomInclusive 지도의 줌 레벨과 오버레이의 최대 줌 레벨이 동일할 때 오버레이를 보일지 여부를 지정합니다.
 * 만약 inclusive가 true이면 오버레이가 나타나고 false이면 나타나지 않습니다. 기본값은 true입니다.
 * @param zIndex 보조 Z 인덱스를 지정합니다. 전역 Z 인덱스가 동일한 여러 오버레이가 화면에서 겹쳐지면 보조 Z 인덱스가 큰
 * 오버레이가 작은 오버레이를 덮습니다. 기본값은 0입니다.
 * @param globalZIndex 전역 Z 인덱스를 지정합니다. 여러 오버레이가 화면에서 겹쳐지면 전역 Z 인덱스가 큰 오버레이가 작은
 * 오버레이를 덮습니다. 또한 값이 0 이상이면 오버레이가 심벌 위에, 0 미만이면 심벌 아래에 그려집니다.
 * @param onClick 클릭 이벤트 리스너를 지정합니다. 사용자가 오버레이를 클릭하면 호출됩니다. 오직 클릭 이벤트 리스너가 지정된
 * 오버레이만이 클릭 이벤트를 받을 수 있습니다. 예를 들어 마커와 지상 오버레이가 겹쳐져 있고 지상 오버레이에만 클릭 이벤트
 * 리스너가 지정된 경우, 사용자가 마커를 클릭하더라도 지상 오버레이가 클릭 이벤트를 받습니다.
 */
@Composable
@NaverMapComposable
public fun PolylineOverlay(
    coords: List<LatLng>,
    width: Dp = 5.dp,
    color: Color = Color.Black,
    pattern: Array<Dp> = emptyArray(),
    capType: LineCap = LineCap.Round,
    joinType: LineJoin = LineJoin.Miter,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapConstants.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapConstants.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = PolylineOverlayDefaults.GlobalZIndex,
    onClick: (PolylineOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<PolylineOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding PolylineOverlay")
            val overlay = PolylineOverlay().apply {
                this.coords = coords
                this.width = with(density) { width.roundToPx() }
                this.color = color.toArgb()
                this.setPattern(*pattern.toIntArrayWith(density))
                this.capType = capType.value
                this.joinType = joinType.value

                // Overlay
                this.tag = tag
                this.isVisible = visible
                this.minZoom = minZoom
                this.isMinZoomInclusive = minZoomInclusive
                this.maxZoom = maxZoom
                this.isMaxZoomInclusive = maxZoomInclusive
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
            }
            overlay.map = map
            overlay.setOnClickListener {
                mapApplier
                    .nodeForPolylineOverlay(overlay)
                    ?.onPolylineOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            PolylineOverlayNode(overlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onPolylineOverlayClick = it }

            set(coords) { this.overlay.coords = it }
            set(width) {
                this.overlay.width = with(this.density) { it.roundToPx() }
            }
            set(color) { this.overlay.color = it.toArgb() }
            set(pattern) { this.overlay.setPattern(*it.toIntArrayWith(this.density)) }
            set(capType) { this.overlay.capType = it.value }
            set(joinType) { this.overlay.joinType = it.value }

            // Overlay
            set(tag) { this.overlay.tag = it }
            set(visible) { this.overlay.isVisible = it }
            set(minZoom) { this.overlay.minZoom = it }
            set(minZoomInclusive) { this.overlay.isMinZoomInclusive = it }
            set(maxZoom) { this.overlay.maxZoom = it }
            set(maxZoomInclusive) { this.overlay.isMaxZoomInclusive = it }
            set(zIndex) { this.overlay.zIndex = it }
            set(globalZIndex) { this.overlay.globalZIndex = it }
        }
    )
}

/**
 * 끝 지점의 모양.
 */
@Immutable
public enum class LineCap(public val value: com.naver.maps.map.overlay.PolylineOverlay.LineCap) {
    /**
     * 원형. 끝 지점에 지름이 두께만 한 원이 그려집니다.
     */
    Round(com.naver.maps.map.overlay.PolylineOverlay.LineCap.Round),

    /**
     * 평면. 끝 지점이 좌표에 딱 맞게 잘립니다.
     */
    Butt(com.naver.maps.map.overlay.PolylineOverlay.LineCap.Butt),

    /**
     * 사각형. 끝 지점에 두께의 반만큼의 직사각형이 추가됩니다.
     */
    Square(com.naver.maps.map.overlay.PolylineOverlay.LineCap.Square),
}

/**
 * 연결점의 모양.
 */
@Immutable
public enum class LineJoin(public val value: com.naver.maps.map.overlay.PolylineOverlay.LineJoin) {
    /**
     * 미터. 연결점이 뾰족하게 그려집니다.
     */
    Miter(com.naver.maps.map.overlay.PolylineOverlay.LineJoin.Miter),

    /**
     * 사면. 연결점에서 뾰족하게 튀어나온 부분이 잘려나갑니다.
     */
    Bevel(com.naver.maps.map.overlay.PolylineOverlay.LineJoin.Bevel),

    /**
     * 원형. 연결점이 둥글게 그려집니다.
     */
    Round(com.naver.maps.map.overlay.PolylineOverlay.LineJoin.Round),
}

private fun Array<Dp>.toIntArrayWith(density: Density): IntArray {
    return map {
        with(density) { it.roundToPx() }
    }.toIntArray()
}
