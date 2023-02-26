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
package com.naver.maps.map.compose

import android.graphics.PointF
import androidx.annotation.FloatRange
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
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Overlay.InvalidCoordinateException
import com.naver.maps.map.overlay.OverlayImage

internal class LocationOverlayNode(
    val overlay: LocationOverlay,
    var onLocationOverlayClick: (LocationOverlay) -> Boolean,
    var density: Density,
) : MapNode {

    override fun onAttached() {
        this.overlay.isVisible = true
    }

    override fun onRemoved() {
        this.overlay.isVisible = false
    }
}

public object LocationOverlayDefaults {

    /**
     * 기본 전역 Z 인덱스.
     */
    public const val GlobalZIndex: Int = LocationOverlay.DEFAULT_GLOBAL_Z_INDEX

    /**
     * 너비 또는 높이가 자동임을 나타내는 상수.
     */
    public const val SizeAuto: Int = LocationOverlay.SIZE_AUTO

    /**
     * 기본 아이콘.
     */
    public val DefaultIcon: OverlayImage = LocationOverlay.DEFAULT_ICON

    /**
     * 화살표가 있는 서브 아이콘.
     */
    public val DefaultSubIconArrow: OverlayImage = LocationOverlay.DEFAULT_SUB_ICON_ARROW

    /**
     * 콘이 있는 서브 아이콘.
     */
    public val DefaultSubIconCone: OverlayImage = LocationOverlay.DEFAULT_SUB_ICON_CONE

    /**
     * 기본 원 색상.
     */
    public val DefaultCircleColor: Color = Color(LocationOverlay.DEFAULT_CIRCLE_COLOR)

    /**
     * 기본 원 반경.
     */
    public val DefaultCircleRadius: Dp = LocationOverlay.DEFAULT_CIRCLE_RADIUS_DP.dp

    /**
     * 기본 앵커.
     */
    public val DefaultAnchor: PointF = LocationOverlay.DEFAULT_ANCHOR

    /**
     * 기본 서브 앵커.
     */
    public val DefaultSubAnchor: PointF = LocationOverlay.DEFAULT_SUB_ANCHOR
}

/**
 * 지도 상의 [LocationOverlay]에 대한 [Composable]입니다.
 *
 * @param position 좌표를 지정합니다. 만약 position이 유효하지 않은([LatLng.isValid]가 false인) 좌표라면
 * [InvalidCoordinateException]이 발생합니다.
 * @param bearing 방위를 지정합니다. 방위가 북쪽일 경우 0도이며, 시계 방향으로 증가합니다.
 * @param icon 아이콘을 지정합니다.
 * @param iconWidth 아이콘의 너비를 지정합니다. [LocationOverlayDefaults.SizeAuto]일 경우 이미지의 너비를 따릅니다.
 * @param iconHeight 아이콘의 높이를 지정합니다. [LocationOverlayDefaults.SizeAuto]일 경우 이미지의 높이를 따릅니다.
 * @param anchor 아이콘의 앵커를 지정합니다. 앵커는 아이콘 이미지에서 기준이 되는 지점을 의미합니다. 앵커로 지정된 지점이
 * 오버레이의 좌표에 위치합니다. 값의 범위는 (0, 0)~(1, 1)이며, (0, 0)일 경우 이미지의 왼쪽 위, (1, 1)일 경우 이미지의
 * 오른쪽 아래를 의미합니다.
 * @param subIcon 보조 아이콘을 지정합니다. null일 경우 보조 아이콘이 그려지지 않습니다.
 * @param subIconWidth 보조 아이콘의 너비를 지정합니다. [LocationOverlayDefaults.SizeAuto]일 경우 이미지의 너비를 따릅니다.
 * @param subIconHeight 보조 아이콘의 높이를 지정합니다. [LocationOverlayDefaults.SizeAuto]일 경우 이미지의 높이를 따릅니다.
 * @param subAnchor 보조 아이콘의 앵커를 지정합니다. 앵커는 보조 아이콘 이미지에서 기준이 되는 지점을 의미합니다. 앵커로
 * 지정된 지점이 오버레이의 좌표에 위치합니다. 값의 범위는 (0, 0)~(1, 1)이며, (0, 0)일 경우 이미지의 왼쪽 위, (1, 1)일
 * 경우 이미지의 오른쪽 아래를 의미합니다.
 * @param circleRadius 원의 반경을 지정합니다. 반경이 0일 경우 원이 그려지지 않습니다.
 * @param circleColor 원의 색상을 반환합니다.
 * @param circleOutlineWidth 원의 테두리 두께를 지정합니다. 0일 경우 테두리가 그려지지 않습니다.
 * @param circleOutlineColor 원의 테두리 색상을 지정합니다.
 * @param tag 태그를 지정합니다. 기본값은 null입니다.
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
public fun LocationOverlay(
    position: LatLng,
    @FloatRange(from = 0.0, to = 360.0) bearing: Float = 0f,
    icon: OverlayImage = LocationOverlayDefaults.DefaultIcon,
    iconWidth: Int = LocationOverlayDefaults.SizeAuto,
    iconHeight: Int = LocationOverlayDefaults.SizeAuto,
    anchor: PointF = LocationOverlayDefaults.DefaultAnchor,
    subIcon: OverlayImage? = null,
    subIconWidth: Int = LocationOverlayDefaults.SizeAuto,
    subIconHeight: Int = LocationOverlayDefaults.SizeAuto,
    subAnchor: PointF = LocationOverlayDefaults.DefaultSubAnchor,
    circleRadius: Dp = LocationOverlayDefaults.DefaultCircleRadius,
    circleColor: Color = LocationOverlayDefaults.DefaultCircleColor,
    circleOutlineWidth: Dp = 0.dp,
    circleOutlineColor: Color = Color.Transparent,
    tag: Any? = null,
    minZoom: Double = NaverMapConstants.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapConstants.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = LocationOverlayDefaults.GlobalZIndex,
    onClick: (LocationOverlay) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as? MapApplier
    val density = LocalDensity.current
    ComposeNode<LocationOverlayNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding LocationOverlay")
            val overlay = map.locationOverlay.apply {
                this.position = position
                this.bearing = bearing
                this.icon = icon
                this.iconWidth = iconWidth
                this.iconHeight = iconHeight
                this.anchor = anchor
                this.subIcon = subIcon
                this.subIconWidth = subIconWidth
                this.subIconHeight = subIconHeight
                this.subAnchor = subAnchor
                this.circleRadius = with(density) { circleRadius.roundToPx() }
                this.circleColor = circleColor.toArgb()
                this.circleOutlineWidth = with(density) { circleOutlineWidth.roundToPx() }
                this.circleOutlineColor = circleOutlineColor.toArgb()

                // Overlay
                this.tag = tag
                this.minZoom = minZoom
                this.isMinZoomInclusive = minZoomInclusive
                this.maxZoom = maxZoom
                this.isMaxZoomInclusive = maxZoomInclusive
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
            }
            overlay.setOnClickListener {
                mapApplier
                    .nodeForLocationOverlay(overlay)
                    ?.onLocationOverlayClick
                    ?.invoke(overlay)
                    ?: false
            }
            LocationOverlayNode(overlay, onClick, density)
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onLocationOverlayClick = it }

            set(position) { this.overlay.position = it }
            set(bearing) { this.overlay.bearing = it }
            set(icon) { this.overlay.icon = it }
            set(iconWidth) { this.overlay.iconWidth = it }
            set(iconHeight) { this.overlay.iconHeight = it }
            set(anchor) { this.overlay.anchor = it }
            set(subIcon) { this.overlay.subIcon = it }
            set(subIconWidth) { this.overlay.subIconWidth = it }
            set(subIconHeight) { this.overlay.subIconHeight = it }
            set(subAnchor) { this.overlay.subAnchor = it }
            set(circleRadius) {
                this.overlay.circleRadius = with(this.density) { it.roundToPx() }
            }
            set(circleColor) { this.overlay.circleColor = it.toArgb() }
            set(circleOutlineWidth) {
                this.overlay.circleOutlineWidth = with(this.density) { it.roundToPx() }
            }
            set(circleOutlineColor) { this.overlay.circleOutlineColor = it.toArgb() }

            // Overlay
            set(tag) { this.overlay.tag = it }
            set(minZoom) { this.overlay.minZoom = it }
            set(minZoomInclusive) { this.overlay.isMinZoomInclusive = it }
            set(maxZoom) { this.overlay.maxZoom = it }
            set(maxZoomInclusive) { this.overlay.isMaxZoomInclusive = it }
            set(zIndex) { this.overlay.zIndex = it }
            set(globalZIndex) { this.overlay.globalZIndex = it }
        }
    )
}
