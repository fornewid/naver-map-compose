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

import android.graphics.PointF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay.InvalidCoordinateException
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

internal class MarkerNode(
    val overlay: Marker,
    val markerState: MarkerState,
    var density: Density,
    var onMarkerClick: (Marker) -> Boolean,
) : MapNode {

    override fun onAttached() {
        markerState.marker = overlay
    }

    override fun onRemoved() {
        markerState.marker = null
        overlay.map = null
    }

    override fun onCleared() {
        markerState.marker = null
        overlay.map = null
    }
}

public object MarkerDefaults {

    /**
     * 기본 전역 Z 인덱스.
     */
    public const val GlobalZIndex: Int = Marker.DEFAULT_GLOBAL_Z_INDEX

    /**
     * 너비 또는 높이가 자동임을 나타내는 상수. 너비 또는 높이가 자동일 경우 아이콘 이미지의 크기에 맞춰집니다.
     */
    public val SizeAuto: Dp = Marker.SIZE_AUTO.dp

    /**
     * 기본 아이콘. [MarkerIcons.GREEN]과 동일합니다.
     */
    public val Icon: OverlayImage = Marker.DEFAULT_ICON

    /**
     * 기본 앵커. 가운데 아래를 가리킵니다.
     */
    public val Anchor: Offset = Offset(Marker.DEFAULT_ANCHOR.x, Marker.DEFAULT_ANCHOR.y)

    /**
     * 기본 캡션 크기. DP 단위.
     */
    public val CaptionTextSize: TextUnit = Marker.DEFAULT_CAPTION_TEXT_SIZE.sp

    /**
     * 기본 캡션 정렬 방향. [Align.Bottom].
     */
    public val CaptionAligns: Array<Align> = arrayOf(Align.Bottom)
}

/**
 * [Marker] 상태를 제어하고 관찰할 수 있는 상태 개체입니다.
 *
 * @param position 초기 좌표를 지정합니다. 만약 position이 유효하지 않은([LatLng.isValid]가 false인) 좌표라면
 * [InvalidCoordinateException]이 발생합니다.
 */
public class MarkerState(
    position: LatLng = LatLng(0.0, 0.0),
) {
    /**
     * Current position of the marker.
     */
    public var position: LatLng by mutableStateOf(position)

    // The marker associated with this MarkerState.
    internal var marker: Marker? = null
        set(value) {
            if (field == null && value == null) return
            if (field != null && value != null) {
                error("MarkerState may only be associated with one Marker at a time.")
            }
            field = value
        }

    public companion object {
        /**
         * The default saver implementation for [MarkerState]
         */
        public val Saver: Saver<MarkerState, LatLng> = Saver(
            save = { it.position },
            restore = { MarkerState(it) }
        )
    }
}

@ExperimentalNaverMapApi
@Composable
@NaverMapComposable
public fun rememberMarkerState(
    key: String? = null,
    position: LatLng = LatLng(0.0, 0.0),
): MarkerState = rememberSaveable(key = key, saver = MarkerState.Saver) {
    MarkerState(position)
}

/**
 * 지도 상의 [Marker]에 대한 [Composable]입니다.
 *
 * @param state position 및 정보 창과 같은 마커 상태를 제어하거나 관찰하는 데 사용되는 [MarkerState]입니다.
 * @param icon 아이콘을 지정합니다.
 * @param iconTintColor 아이콘에 덧입힐 색상을 지정합니다. 덧입힐 색상을 지정하면 덧입힐 색상이 아이콘 이미지의 색상과 가산
 * 혼합됩니다. 단, 덧입힐 색상의 알파는 무시됩니다. 기본값은 [Color.Transparent]입니다.
 * @param width 아이콘의 너비를 지정합니다. [MarkerDefaults.SizeAuto]일 경우 이미지의 너비를 따릅니다. 기본값은
 * [MarkerDefaults.SizeAuto]입니다.
 * @param height 아이콘의 높이를 지정합니다. [MarkerDefaults.SizeAuto]일 경우 이미지의 높이를 따릅니다. 기본값은
 * [MarkerDefaults.SizeAuto]입니다.
 * @param anchor 앵커를 지정합니다. 앵커는 아이콘 이미지에서 기준이 되는 지점을 의미합니다. 앵커로 지정된 지점이 마커의 좌표에
 * 위치합니다. 값의 범위는 (0, 0)~(1, 1)이며, (0, 0)일 경우 이미지의 왼쪽 위, (1, 1)일 경우 이미지의 오른쪽 아래를
 * 의미합니다. 기본값은 [MarkerDefaults.Anchor]입니다.
 * @param captionText 캡션의 텍스트를 지정합니다. 빈 문자열일 경우 캡션이 그려지지 않습니다. 기본값은 빈 문자열입니다.
 * @param captionTextSize 캡션의 텍스트 크기를 지정합니다. 기본값은 [MarkerDefaults.CaptionTextSize]입니다.
 * @param captionColor 캡션의 텍스트 색상을 지정합니다. 기본값은 [Color.Black]입니다.
 * @param captionHaloColor 캡션의 외곽 색상을 지정합니다. 기본값은 [Color.White]입니다.
 * @param captionRequestedWidth 캡션의 희망 너비를 지정합니다. 지정할 경우 한 줄의 너비가 희망 너비를 초과하는 캡션
 * 텍스트가 자동으로 개행됩니다. 자동 개행은 어절 단위로 이루어지므로, 하나의 어절이 길 경우 캡션의 너비가 희망 너비를 초과할 수
 * 있습니다. 0일 경우 너비를 제한하지 않습니다. 기본값은 0입니다.
 * @param captionMinZoom 캡션이 보이는 최소 줌 레벨을 지정합니다. 지도의 줌 레벨이 캡션의 최소 줌 레벨보다 작을 경우
 * 아이콘만 나타나고 주 캡션 및 보조 캡션은 나타나지 않습니다. 기본값은 [NaverMapConstants.MinZoom]입니다.
 * @param captionMaxZoom 캡션이 보이는 최대 줌 레벨을 지정합니다. 지도의 줌 레벨이 캡션의 최대 줌 레벨보다 클 경우 아이콘만
 * 나타나고 주 캡션 및 보조 캡션은 나타나지 않습니다. 기본값은 [NaverMapConstants.MaxZoom]입니다.
 * @param subCaptionText 보조 캡션의 텍스트를 지정합니다. 보조 캡션은 주 캡션의 하단에 나타납니다. 빈 문자열일 경우 보조
 * 캡션이 그려지지 않습니다. 기본값은 빈 문자열입니다.
 * @param subCaptionTextSize 보조 캡션의 텍스트 크기를 지정합니다. 기본값은 [MarkerDefaults.CaptionTextSize]입니다.
 * @param subCaptionColor 보조 캡션의 텍스트 색상을 지정합니다. 기본값은 [Color.Black]입니다.
 * @param subCaptionHaloColor 보조 캡션의 외곽 색상을 지정합니다. 기본값은 [Color.White]입니다.
 * @param subCaptionRequestedWidth 보조 캡션의 너비를 지정합니다. 지정할 경우 한 줄의 너비가 희망 너비를 초과하는 캡션
 * 텍스트는 자동으로 개행됩니다. 자동 개행은 어절 단위로 이루어지므로, 하나의 어절이 길 경우 캡션의 너비가 희망 너비를 초과할 수
 * 있습니다. 0일 경우 너비를 제한하지 않습니다. 기본값은 0입니다.
 * @param subCaptionMinZoom 보조 캡션이 보이는 최소 줌 레벨을 지정합니다. 지도의 줌 레벨이 보조 캡션의 최소 줌 레벨보다
 * 작을 경우 아이콘 및 주 캡션만 나타나고 보조 캡션은 나타나지 않습니다. 기본값은 [NaverMapConstants.MinZoom]입니다.
 * @param subCaptionMaxZoom 보조 캡션이 보이는 최대 줌 레벨을 지정합니다. 지도의 줌 레벨이 보조 캡션의 최대 줌 레벨보다
 * 클 경우 아이콘 및 주 캡션만 나타나고 보조 캡션은 나타나지 않습니다. 기본값은 [NaverMapConstants.MaxZoom]입니다.
 * @param captionAligns 캡션을 아이콘의 어느 방향에 위치시킬지를 지정합니다. 캡션은 aligns에 지정된 순서대로 우선적으로
 * 위치합니다. 만약 캡션이 다른 마커와 겹치지 않거나 겹치더라도 해당 마커의 [isHideCollidedCaptions]가 false라면 캡션은
 * 반드시 0번에 위치합니다. 그렇지 않을 경우 겹치지 않은 다음 방향에 위치하며, 어느 방향으로 위치시켜도 다른 마커와 겹칠 경우
 * 캡션이 숨겨집니다. 만약 파라메터 없이 메서드를 호출하거나 aligns의 크기가 0이면 [IllegalArgumentException]이
 * 발생합니다. aligns에 null이 있다면 [NullPointerException]이 발생합니다. 기본값은
 * [MarkerDefaults.CaptionAligns]입니다.
 * @param captionOffset 아이콘과 캡션 간의 여백을 지정합니다. 기본값은 0입니다.
 * @param alpha 불투명도를 0~1로 지정합니다. 0일 경우 완전히 투명, 1일 경우 완전히 불투명함을 의미합니다. 아이콘과 캡션
 * 모두에 적용됩니다. 기본값은 1입니다.
 * @param angle 아이콘의 각도를 지정합니다. 각도를 지정하면 아이콘이 해당 각도만큼 시계 방향으로 회전합니다. 기본값은 0입니다.
 * @param isFlat 마커를 평평하게 설정할지 여부를 지정합니다. 마커가 평평할 경우 지도가 회전하거나 기울어지면 마커 이미지도 함께
 * 회전하거나 기울어집니다. 단, 마커가 평평하더라도 이미지의 크기는 항상 동일하게 유지됩니다. 기본값은 false입니다.
 * @param isHideCollidedSymbols 마커와 지도 심벌이 겹칠 경우 지도 심벌을 숨길지 여부를 지정합니다. 기본값은 false입니다.
 * @param isHideCollidedMarkers 마커와 다른 마커가 겹칠 경우 다른 마커를 숨길지 여부를 지정합니다. 기본값은 false입니다.
 * @param isHideCollidedCaptions 마커와 다른 마커의 캡션이 겹칠 경우 다른 마커의 캡션을 숨길지 여부를 지정합니다.
 * 기본값은 false입니다.
 * @param isForceShowIcon 마커가 [isHideCollidedMarkers]이 true인 다른 마커와 겹치더라도 아이콘을 무조건 표시할지
 * 여부를 지정합니다. 기본값은 false입니다.
 * @param isForceShowCaption 마커가 [isHideCollidedCaptions]이 true인 다른 마커와 겹치더라도 캡션을 무조건
 * 표시할지 여부를 지정합니다. 기본값은 false입니다.
 * @param isIconPerspectiveEnabled 아이콘에 원근 효과를 적용할지 여부를 지정합니다. 원근 효과를 적용할 경우 가까운
 * 아이콘은 크게, 먼 아이콘은 작게 표시됩니다. 기본값은 false입니다.
 * @param isCaptionPerspectiveEnabled 캡션에 원근 효과를 적용할지 여부를 지정합니다. 원근 효과를 적용할 경우 가까운
 * 캡션은 크게, 먼 캡션은 작게 표시됩니다. 기본값은 false입니다.
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
@ExperimentalNaverMapApi
@Composable
@NaverMapComposable
public fun Marker(
    state: MarkerState = rememberMarkerState(),
    icon: OverlayImage = MarkerDefaults.Icon,
    iconTintColor: Color = Color.Transparent,
    width: Dp = MarkerDefaults.SizeAuto,
    height: Dp = MarkerDefaults.SizeAuto,
    anchor: Offset = MarkerDefaults.Anchor,
    captionText: String? = null,
    captionTextSize: TextUnit = MarkerDefaults.CaptionTextSize,
    captionColor: Color = Color.Black,
    captionHaloColor: Color = Color.White,
    captionRequestedWidth: Dp = 0.dp,
    captionMinZoom: Double = NaverMapConstants.MinZoom,
    captionMaxZoom: Double = NaverMapConstants.MaxZoom,
    subCaptionText: String? = null,
    subCaptionTextSize: TextUnit = MarkerDefaults.CaptionTextSize,
    subCaptionColor: Color = Color.Black,
    subCaptionHaloColor: Color = Color.White,
    subCaptionRequestedWidth: Dp = 0.dp,
    subCaptionMinZoom: Double = NaverMapConstants.MinZoom,
    subCaptionMaxZoom: Double = NaverMapConstants.MaxZoom,
    captionAligns: Array<Align> = MarkerDefaults.CaptionAligns,
    captionOffset: Dp = 0.dp,
    alpha: Float = 1.0f,
    angle: Float = 0.0f,
    isFlat: Boolean = false,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    isForceShowIcon: Boolean = false,
    isForceShowCaption: Boolean = false,
    isIconPerspectiveEnabled: Boolean = false,
    isCaptionPerspectiveEnabled: Boolean = false,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapConstants.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapConstants.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = MarkerDefaults.GlobalZIndex,
    onClick: (Marker) -> Boolean = { false },
) {
    MarkerImpl(
        state = state,
        width = width,
        height = height,
        alpha = alpha,
        anchor = anchor,
        isFlat = isFlat,
        icon = icon,
        iconTintColor = iconTintColor,
        angle = angle,
        subCaptionText = subCaptionText,
        subCaptionColor = subCaptionColor,
        subCaptionTextSize = subCaptionTextSize,
        subCaptionMinZoom = subCaptionMinZoom,
        subCaptionMaxZoom = subCaptionMaxZoom,
        subCaptionHaloColor = subCaptionHaloColor,
        subCaptionRequestedWidth = subCaptionRequestedWidth,
        isHideCollidedSymbols = isHideCollidedSymbols,
        isHideCollidedMarkers = isHideCollidedMarkers,
        isHideCollidedCaptions = isHideCollidedCaptions,
        isForceShowIcon = isForceShowIcon,
        isForceShowCaption = isForceShowCaption,
        isIconPerspectiveEnabled = isIconPerspectiveEnabled,
        isCaptionPerspectiveEnabled = isCaptionPerspectiveEnabled,
        captionText = captionText,
        captionColor = captionColor,
        captionHaloColor = captionHaloColor,
        captionTextSize = captionTextSize,
        captionRequestedWidth = captionRequestedWidth,
        captionMinZoom = captionMinZoom,
        captionMaxZoom = captionMaxZoom,
        captionAligns = captionAligns,
        captionOffset = captionOffset,
        tag = tag,
        visible = visible,
        minZoom = minZoom,
        minZoomInclusive = minZoomInclusive,
        maxZoom = maxZoom,
        maxZoomInclusive = maxZoomInclusive,
        zIndex = zIndex,
        globalZIndex = globalZIndex,
        onClick = onClick,
    )
}

/**
 * 지도 상의 [Marker]에 대한 내부 구현입니다.
 *
 * @param state position 및 정보 창과 같은 마커 상태를 제어하거나 관찰하는 데 사용되는 [MarkerState]입니다.
 * @param icon 아이콘을 지정합니다.
 * @param iconTintColor 아이콘에 덧입힐 색상을 지정합니다. 덧입힐 색상을 지정하면 덧입힐 색상이 아이콘 이미지의 색상과 가산
 * 혼합됩니다. 단, 덧입힐 색상의 알파는 무시됩니다. 기본값은 [Color.Transparent]입니다.
 * @param width 아이콘의 너비를 지정합니다. [MarkerDefaults.SizeAuto]일 경우 이미지의 너비를 따릅니다. 기본값은
 * [MarkerDefaults.SizeAuto]입니다.
 * @param height 아이콘의 높이를 지정합니다. [MarkerDefaults.SizeAuto]일 경우 이미지의 높이를 따릅니다. 기본값은
 * [MarkerDefaults.SizeAuto]입니다.
 * @param anchor 앵커를 지정합니다. 앵커는 아이콘 이미지에서 기준이 되는 지점을 의미합니다. 앵커로 지정된 지점이 마커의 좌표에
 * 위치합니다. 값의 범위는 (0, 0)~(1, 1)이며, (0, 0)일 경우 이미지의 왼쪽 위, (1, 1)일 경우 이미지의 오른쪽 아래를
 * 의미합니다. 기본값은 [MarkerDefaults.Anchor]입니다.
 * @param captionText 캡션의 텍스트를 지정합니다. 빈 문자열일 경우 캡션이 그려지지 않습니다. 기본값은 빈 문자열입니다.
 * @param captionTextSize 캡션의 텍스트 크기를 지정합니다. 기본값은 [MarkerDefaults.CaptionTextSize]입니다.
 * @param captionColor 캡션의 텍스트 색상을 지정합니다. 기본값은 [Color.Black]입니다.
 * @param captionHaloColor 캡션의 외곽 색상을 지정합니다. 기본값은 [Color.White]입니다.
 * @param captionRequestedWidth 캡션의 희망 너비를 지정합니다. 지정할 경우 한 줄의 너비가 희망 너비를 초과하는 캡션
 * 텍스트가 자동으로 개행됩니다. 자동 개행은 어절 단위로 이루어지므로, 하나의 어절이 길 경우 캡션의 너비가 희망 너비를 초과할 수
 * 있습니다. 0일 경우 너비를 제한하지 않습니다. 기본값은 0입니다.
 * @param captionMinZoom 캡션이 보이는 최소 줌 레벨을 지정합니다. 지도의 줌 레벨이 캡션의 최소 줌 레벨보다 작을 경우
 * 아이콘만 나타나고 주 캡션 및 보조 캡션은 나타나지 않습니다. 기본값은 [NaverMapConstants.MinZoom]입니다.
 * @param captionMaxZoom 캡션이 보이는 최대 줌 레벨을 지정합니다. 지도의 줌 레벨이 캡션의 최대 줌 레벨보다 클 경우 아이콘만
 * 나타나고 주 캡션 및 보조 캡션은 나타나지 않습니다. 기본값은 [NaverMapConstants.MaxZoom]입니다.
 * @param subCaptionText 보조 캡션의 텍스트를 지정합니다. 보조 캡션은 주 캡션의 하단에 나타납니다. 빈 문자열일 경우 보조
 * 캡션이 그려지지 않습니다. 기본값은 빈 문자열입니다.
 * @param subCaptionTextSize 보조 캡션의 텍스트 크기를 지정합니다. 기본값은 [MarkerDefaults.CaptionTextSize]입니다.
 * @param subCaptionColor 보조 캡션의 텍스트 색상을 지정합니다. 기본값은 [Color.Black]입니다.
 * @param subCaptionHaloColor 보조 캡션의 외곽 색상을 지정합니다. 기본값은 [Color.White]입니다.
 * @param subCaptionRequestedWidth 보조 캡션의 너비를 지정합니다. 지정할 경우 한 줄의 너비가 희망 너비를 초과하는 캡션
 * 텍스트는 자동으로 개행됩니다. 자동 개행은 어절 단위로 이루어지므로, 하나의 어절이 길 경우 캡션의 너비가 희망 너비를 초과할 수
 * 있습니다. 0일 경우 너비를 제한하지 않습니다. 기본값은 0입니다.
 * @param subCaptionMinZoom 보조 캡션이 보이는 최소 줌 레벨을 지정합니다. 지도의 줌 레벨이 보조 캡션의 최소 줌 레벨보다
 * 작을 경우 아이콘 및 주 캡션만 나타나고 보조 캡션은 나타나지 않습니다. 기본값은 [NaverMapConstants.MinZoom]입니다.
 * @param subCaptionMaxZoom 보조 캡션이 보이는 최대 줌 레벨을 지정합니다. 지도의 줌 레벨이 보조 캡션의 최대 줌 레벨보다
 * 클 경우 아이콘 및 주 캡션만 나타나고 보조 캡션은 나타나지 않습니다. 기본값은 [NaverMapConstants.MaxZoom]입니다.
 * @param captionAligns 캡션을 아이콘의 어느 방향에 위치시킬지를 지정합니다. 캡션은 aligns에 지정된 순서대로 우선적으로
 * 위치합니다. 만약 캡션이 다른 마커와 겹치지 않거나 겹치더라도 해당 마커의 [isHideCollidedCaptions]가 false라면 캡션은
 * 반드시 0번에 위치합니다. 그렇지 않을 경우 겹치지 않은 다음 방향에 위치하며, 어느 방향으로 위치시켜도 다른 마커와 겹칠 경우
 * 캡션이 숨겨집니다. 만약 파라메터 없이 메서드를 호출하거나 aligns의 크기가 0이면 [IllegalArgumentException]이
 * 발생합니다. aligns에 null이 있다면 [NullPointerException]이 발생합니다. 기본값은
 * [MarkerDefaults.CaptionAligns]입니다.
 * @param captionOffset 아이콘과 캡션 간의 여백을 지정합니다. 기본값은 0입니다.
 * @param alpha 불투명도를 0~1로 지정합니다. 0일 경우 완전히 투명, 1일 경우 완전히 불투명함을 의미합니다. 아이콘과 캡션
 * 모두에 적용됩니다. 기본값은 1입니다.
 * @param angle 아이콘의 각도를 지정합니다. 각도를 지정하면 아이콘이 해당 각도만큼 시계 방향으로 회전합니다. 기본값은 0입니다.
 * @param isFlat 마커를 평평하게 설정할지 여부를 지정합니다. 마커가 평평할 경우 지도가 회전하거나 기울어지면 마커 이미지도 함께
 * 회전하거나 기울어집니다. 단, 마커가 평평하더라도 이미지의 크기는 항상 동일하게 유지됩니다. 기본값은 false입니다.
 * @param isHideCollidedSymbols 마커와 지도 심벌이 겹칠 경우 지도 심벌을 숨길지 여부를 지정합니다. 기본값은 false입니다.
 * @param isHideCollidedMarkers 마커와 다른 마커가 겹칠 경우 다른 마커를 숨길지 여부를 지정합니다. 기본값은 false입니다.
 * @param isHideCollidedCaptions 마커와 다른 마커의 캡션이 겹칠 경우 다른 마커의 캡션을 숨길지 여부를 지정합니다.
 * 기본값은 false입니다.
 * @param isForceShowIcon 마커가 [isHideCollidedMarkers]이 true인 다른 마커와 겹치더라도 아이콘을 무조건 표시할지
 * 여부를 지정합니다. 기본값은 false입니다.
 * @param isForceShowCaption 마커가 [isHideCollidedCaptions]이 true인 다른 마커와 겹치더라도 캡션을 무조건
 * 표시할지 여부를 지정합니다. 기본값은 false입니다.
 * @param isIconPerspectiveEnabled 아이콘에 원근 효과를 적용할지 여부를 지정합니다. 원근 효과를 적용할 경우 가까운
 * 아이콘은 크게, 먼 아이콘은 작게 표시됩니다. 기본값은 false입니다.
 * @param isCaptionPerspectiveEnabled 캡션에 원근 효과를 적용할지 여부를 지정합니다. 원근 효과를 적용할 경우 가까운
 * 캡션은 크게, 먼 캡션은 작게 표시됩니다. 기본값은 false입니다.
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
@ExperimentalNaverMapApi
@Composable
@NaverMapComposable
private fun MarkerImpl(
    state: MarkerState = rememberMarkerState(),
    icon: OverlayImage = MarkerDefaults.Icon,
    iconTintColor: Color = Color.Transparent,
    width: Dp = MarkerDefaults.SizeAuto,
    height: Dp = MarkerDefaults.SizeAuto,
    anchor: Offset = Offset(0.5f, 1.0f),
    captionText: String? = null,
    captionTextSize: TextUnit = MarkerDefaults.CaptionTextSize,
    captionColor: Color = Color.Black,
    captionHaloColor: Color = Color.White,
    captionRequestedWidth: Dp = 0.dp,
    captionMinZoom: Double = NaverMapConstants.MinZoom,
    captionMaxZoom: Double = NaverMapConstants.MaxZoom,
    subCaptionText: String? = null,
    subCaptionTextSize: TextUnit = MarkerDefaults.CaptionTextSize,
    subCaptionColor: Color = Color.Black,
    subCaptionHaloColor: Color = Color.White,
    subCaptionRequestedWidth: Dp = 0.dp,
    subCaptionMinZoom: Double = NaverMapConstants.MinZoom,
    subCaptionMaxZoom: Double = NaverMapConstants.MaxZoom,
    captionAligns: Array<Align> = MarkerDefaults.CaptionAligns,
    captionOffset: Dp = 0.dp,
    alpha: Float = 1.0f,
    angle: Float = 0.0f,
    isFlat: Boolean = false,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    isForceShowIcon: Boolean = false,
    isForceShowCaption: Boolean = false,
    isIconPerspectiveEnabled: Boolean = false,
    isCaptionPerspectiveEnabled: Boolean = false,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = NaverMapConstants.MinZoom,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = NaverMapConstants.MaxZoom,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = MarkerDefaults.GlobalZIndex,
    onClick: (Marker) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as? MapApplier
    val density = LocalDensity.current
    ComposeNode<MarkerNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding Marker")
            val overlay: Marker = Marker().apply {
                this.position = state.position
                this.icon = icon
                this.iconTintColor = iconTintColor.toArgb()
                this.width = with(density) { width.roundToPx() }
                this.height = with(density) { height.roundToPx() }
                this.anchor = PointF(anchor.x, anchor.y)
                this.captionText = captionText.orEmpty()
                this.captionTextSize = captionTextSize.value
                this.captionColor = captionColor.toArgb()
                this.captionHaloColor = captionHaloColor.toArgb()
                this.captionRequestedWidth = with(density) {
                    captionRequestedWidth.roundToPx()
                }
                this.captionMinZoom = captionMinZoom
                this.captionMaxZoom = captionMaxZoom
                this.subCaptionText = subCaptionText.orEmpty()
                this.subCaptionTextSize = subCaptionTextSize.value
                this.subCaptionColor = subCaptionColor.toArgb()
                this.subCaptionHaloColor = subCaptionHaloColor.toArgb()
                this.subCaptionRequestedWidth = with(density) {
                    subCaptionRequestedWidth.roundToPx()
                }
                this.subCaptionMinZoom = subCaptionMinZoom
                this.subCaptionMaxZoom = subCaptionMaxZoom
                this.setCaptionAligns(*captionAligns.map { it.value }.toTypedArray())
                this.captionOffset = with(density) {
                    captionOffset.roundToPx()
                }
                this.alpha = alpha
                this.angle = angle
                this.isFlat = isFlat
                this.isHideCollidedSymbols = isHideCollidedSymbols
                this.isHideCollidedMarkers = isHideCollidedMarkers
                this.isHideCollidedCaptions = isHideCollidedCaptions
                this.isForceShowIcon = isForceShowIcon
                this.isForceShowCaption = isForceShowCaption
                this.isIconPerspectiveEnabled = isIconPerspectiveEnabled
                this.isCaptionPerspectiveEnabled = isCaptionPerspectiveEnabled

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
                    .nodeForMarker(overlay)
                    ?.onMarkerClick
                    ?.invoke(overlay)
                    ?: false
            }
            MarkerNode(
                overlay = overlay,
                markerState = state,
                density = density,
                onMarkerClick = onClick,
            )
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            update(density) { this.density = it }
            update(onClick) { this.onMarkerClick = it }

            set(state.position) { this.overlay.position = it }
            set(icon) { this.overlay.icon = it }
            set(iconTintColor) { this.overlay.iconTintColor = it.toArgb() }
            set(width) {
                this.overlay.width = with(this.density) { it.roundToPx() }
            }
            set(height) {
                this.overlay.height = with(this.density) { it.roundToPx() }
            }
            set(anchor) { this.overlay.anchor = PointF(it.x, it.y) }
            set(captionText) {
                this.overlay.captionText = it.orEmpty()
            }
            set(captionTextSize) { this.overlay.captionTextSize = it.value }
            set(captionColor) { this.overlay.captionColor = it.toArgb() }
            set(captionHaloColor) { this.overlay.captionHaloColor = it.toArgb() }
            set(captionRequestedWidth) {
                this.overlay.captionRequestedWidth = with(this.density) { it.roundToPx() }
            }
            set(captionMinZoom) { this.overlay.captionMinZoom = it }
            set(captionMaxZoom) { this.overlay.captionMaxZoom = it }
            set(subCaptionText) {
                this.overlay.subCaptionText = it.orEmpty()
            }
            set(subCaptionTextSize) { this.overlay.subCaptionTextSize = it.value }
            set(subCaptionColor) { this.overlay.subCaptionColor = it.toArgb() }
            set(subCaptionHaloColor) { this.overlay.subCaptionHaloColor = it.toArgb() }
            set(subCaptionRequestedWidth) {
                this.overlay.subCaptionRequestedWidth = with(this.density) { it.roundToPx() }
            }
            set(subCaptionMinZoom) { this.overlay.subCaptionMinZoom = it }
            set(subCaptionMaxZoom) { this.overlay.subCaptionMaxZoom = it }
            set(captionAligns) { aligns ->
                this.overlay.setCaptionAligns(*aligns.map { it.value }.toTypedArray())
            }
            set(captionOffset) {
                this.overlay.captionOffset = with(this.density) { it.roundToPx() }
            }
            set(alpha) { this.overlay.alpha = it }
            set(angle) { this.overlay.angle = it }
            set(isFlat) { this.overlay.isFlat = it }
            set(isHideCollidedSymbols) { this.overlay.isHideCollidedSymbols = it }
            set(isHideCollidedMarkers) { this.overlay.isHideCollidedMarkers = it }
            set(isHideCollidedCaptions) { this.overlay.isHideCollidedCaptions = it }
            set(isForceShowIcon) { this.overlay.isForceShowIcon = it }
            set(isForceShowCaption) { this.overlay.isForceShowCaption = it }
            set(isIconPerspectiveEnabled) { this.overlay.isIconPerspectiveEnabled = it }
            set(isCaptionPerspectiveEnabled) { this.overlay.isCaptionPerspectiveEnabled = it }

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
 * 오버레이를 어느 방향으로 정렬할지를 나타내는 열거형.
 */
@Immutable
public enum class Align(public val value: com.naver.maps.map.overlay.Align) {
    /**
     * 가운데.
     */
    Center(com.naver.maps.map.overlay.Align.Center),

    /**
     * 왼쪽.
     */
    Left(com.naver.maps.map.overlay.Align.Left),

    /**
     * 오른쪽.
     */
    Right(com.naver.maps.map.overlay.Align.Right),

    /**
     * 위.
     */
    Top(com.naver.maps.map.overlay.Align.Top),

    /**
     * 아래.
     */
    Bottom(com.naver.maps.map.overlay.Align.Bottom),

    /**
     * 왼쪽 위.
     */
    TopLeft(com.naver.maps.map.overlay.Align.TopLeft),

    /**
     * 오른쪽 위.
     */
    TopRight(com.naver.maps.map.overlay.Align.TopRight),

    /**
     * 오른쪽 아래.
     */
    BottomRight(com.naver.maps.map.overlay.Align.BottomRight),

    /**
     * 왼쪽 아래.
     */
    BottomLeft(com.naver.maps.map.overlay.Align.BottomLeft),
    ;

    public companion object {
        /**
         * 모서리 방향.

         */
        public val EDGES: Array<Align> = arrayOf(Bottom, Right, Left, Top)

        /**
         * 꼭짓점 방향.
         */
        public val APEXES: Array<Align> = arrayOf(BottomRight, BottomLeft, TopRight, TopLeft)

        /**
         * 모서리와 꼭짓점 방향.
         */
        public val OUTSIDES: Array<Align> = arrayOf(
            Bottom, Right, Left, Top, BottomRight, BottomLeft, TopRight, TopLeft
        )
    }
}
