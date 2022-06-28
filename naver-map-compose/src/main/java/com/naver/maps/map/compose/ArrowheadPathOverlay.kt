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
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.ArrowheadPathOverlay
import com.naver.maps.map.overlay.Overlay.InvalidCoordinateException

internal class ArrowheadPathOverlayNode(
    val overlay: ArrowheadPathOverlay,
    var onArrowheadPathOverlayClick: (ArrowheadPathOverlay) -> Boolean,
    var density: Density,
) : MapNode {
    override fun onRemoved() {
        overlay.map = null
    }
}

public object ArrowheadPathOverlayDefaults {

    /**
     * 기본 전역 Z 인덱스.
     */
    public const val GlobalZIndex: Int = ArrowheadPathOverlay.DEFAULT_GLOBAL_Z_INDEX
}

/**
 * 지도 상의 [ArrowheadPathOverlay]에 대한 [Composable]입니다.
 *
 * @param coords 좌표열을 지정합니다. 만약 coords의 크기가 2 미만이면 [IllegalArgumentException]이 발생합니다.
 * coords에 null이거나 유효하지 않은([LatLng.isValid]가 false인) 좌표가 있다면 [InvalidCoordinateException]이
 * 발생합니다.
 * @param width 두께를 지정합니다. 기본값은 10입니다.
 * @param headSizeRatio 머리 크기의 배율을 반환합니다. 두께에 배율을 곱한 값이 머리의 크기가 됩니다. 기본값은 2.5입니다.
 * @param color 색상을 지정합니다. 기본값은 [Color.White]입니다.
 * @param outlineWidth 외곽선의 두께를 지정합니다. 기본값은 2입니다.
 * @param outlineColor 외곽선의 색상을 지정합니다. 외곽선의 색상은 반투명일 수 없으며, 완전히 투명하거나 완전히 불투명해야만
 * 합니다. 색상의 알파가 0이 아닌 경우 완전히 불투명한 것으로 간주됩니다. 기본값은 [Color.Black]입니다.
 * @param elevation 엘리베이션을 지정합니다. 기본값은 0입니다.
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
public fun ArrowheadPathOverlay(
    coords: List<LatLng>,
    width: Dp = 10.dp,
    headSizeRatio: Float = 2.5f,
    color: Color = Color.Black,
    outlineWidth: Dp = 2.dp,
    outlineColor: Color = Color.Black,
    elevation: Dp = 0.dp,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapConstants.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapConstants.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = ArrowheadPathOverlayDefaults.GlobalZIndex,
    onClick: (ArrowheadPathOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as MapApplier?
    val density = LocalDensity.current
    ComposeNode<ArrowheadPathOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding ArrowheadPathOverlay")
            val overlay = ArrowheadPathOverlay().apply {
                this.coords = coords
                this.width = with(density) { width.roundToPx() }
                this.headSizeRatio = headSizeRatio
                this.color = color.toArgb()
                this.outlineWidth = with(density) { outlineWidth.roundToPx() }
                this.outlineColor = outlineColor.toArgb()
                this.elevation = with(density) { elevation.roundToPx() }

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
                    .nodeForArrowheadPathOverlay(overlay)
                    ?.onArrowheadPathOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            ArrowheadPathOverlayNode(overlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onArrowheadPathOverlayClick = it }

            set(coords) { this.overlay.coords = it }
            set(width) {
                this.overlay.width = with(this.density) { it.roundToPx() }
            }
            set(headSizeRatio) { this.overlay.headSizeRatio = it }
            set(color) { this.overlay.color = it.toArgb() }
            set(outlineWidth) {
                this.overlay.outlineWidth = with(this.density) { it.roundToPx() }
            }
            set(outlineColor) { this.overlay.outlineColor = it.toArgb() }
            set(elevation) {
                this.overlay.elevation = with(this.density) { it.roundToPx() }
            }

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
