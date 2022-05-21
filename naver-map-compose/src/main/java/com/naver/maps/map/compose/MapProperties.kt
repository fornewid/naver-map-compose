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

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.naver.maps.geometry.LatLngBounds

internal val DefaultMapProperties = MapProperties()

/**
 * 지도에서 수정할 수 있는 속성을 관리하는 데이터 클래스.
 *
 * Note: This is intentionally a class and not a data class for binary compatibility on future changes.
 * See: https://jakewharton.com/public-api-challenges-in-kotlin/
 *
 * @param mapType 지도의 유형을 지정합니다. 기본값은 [MapType.Basic]입니다.
 * @param extent 지도의 제한 영역을 지정합니다. 기본값은 제한이 없음을 의미하는 null입니다.
 * @param minZoom 지도의 최소 줌 레벨을 지정합니다. 기본값은 [NaverMapConstants.MinZoom]입니다.
 * @param maxZoom 지도의 최대 줌 레벨을 지정합니다. 기본값은 [NaverMapConstants.MaxZoom]입니다.
 * @param maxTilt 지도의 최대 기울기 각도를 지정합니다. 기본값은 [NaverMapConstants.DefaultMaxTilt]입니다.
 * @param defaultCameraAnimationDuration 카메라 이동 애니메이션의 기본 지속 시간을 지정합니다.
 * 기본값은 [NaverMapConstants.DefaultCameraAnimationDuration]입니다.
 * @param fpsLimit 지도의 최대 초당 프레임 수(FPS, frames per second)를 지정합니다.
 * 기본값은 제한을 두지 않음을 의미하는 0입니다.
 * @param isBuildingLayerGroupEnabled 건물 레이어 그룹을 활성화할지 여부를 지정합니다. 기본값은 true입니다.
 * @param isTransitLayerGroupEnabled 대중교통 레이어 그룹을 활성화할지 여부를 지정합니다. 기본값은 false입니다.
 * @param isBicycleLayerGroupEnabled 자전거 도로 레이어 그룹을 활성화할지 여부를 지정합니다. 기본값은 false입니다.
 * @param isTrafficLayerGroupEnabled 실시간 교통정보 레이어 그룹을 활성화할지 여부를 지정합니다. 기본값은 false입니다.
 * @param isCadastralLayerGroupEnabled 지적편집도 레이어 그룹을 활성화할지 여부를 지정합니다. 기본값은 false입니다.
 * @param isMountainLayerGroupEnabled 등산로 레이어 그룹을 활성화할지 여부를 지정합니다. 기본값은 false입니다.
 * @param isLiteModeEnabled 라이트 모드를 활성화할지 여부를 지정합니다.
 * 라이트 모드가 활성화되면 지도의 로딩이 빨라지고 메모리 소모가 감소합니다. 그러나 다음과 같은 제약이 생깁니다.
 * 지도의 전반적인 화질이 하락합니다.
 * 카메라가 회전하거나 기울어지면 지도 심벌도 함께 회전하거나 기울어집니다.
 * 줌 레벨이 커지거나 작아지면 지도 심벌도 일정 정도 함께 커지거나 작아집니다.
 * [MapType.Navi] 지도 유형을 사용할 수 없습니다.
 * [isBuildingLayerGroupEnabled], [isTransitLayerGroupEnabled], [isBicycleLayerGroupEnabled],
 * [isTrafficLayerGroupEnabled], [isCadastralLayerGroupEnabled], [isMountainLayerGroupEnabled],
 * [isIndoorEnabled], [isNightModeEnabled], [lightness], [buildingHeight], [symbolScale],
 * [symbolPerspectiveRatio]가 동작하지 않습니다.
 * [NaverMap]의 onSymbolClick이 호출되지 않습니다.
 * [Marker]의 isHideCollidedSymbols가 동작하지 않습니다.
 * 기본값은 false입니다.
 * @param isNightModeEnabled 야간 모드를 활성화할지 여부를 지정합니다.
 * 야간 모드가 활성화되면 지도 스타일이 어둡게 바뀝니다.
 * 지도 유형이 야간 모드를 지원하지 않으면 활성화하더라도 아무 변화가 일어나지 않습니다.
 * 기본값은 false입니다.
 * @param isIndoorEnabled 실내지도를 활성화할지 여부를 지정합니다.
 * 활성화하면 카메라가 일정 이상 확대되고 실내지도가 있는 영역에 포커스될 경우 자동으로 해당 영역의 실내지도가 나타납니다.
 * 기본값은 false입니다.
 * @param indoorFocusRadius 실내지도 영역의 포커스 유지 반경을 지정합니다.
 * 지정할 경우 카메라의 위치가 포커스 유지 반경을 완전히 벗어날 때까지 영역에 대한 포커스가 유지됩니다.
 * 기본값은 [NaverMapConstants.DefaultIndoorFocusRadius]입니다.
 * @param buildingHeight 건물의 3D 높이 배율을 지정합니다. 배율이 0일 경우 지도를 기울이더라도 건물이 2D로 나타납니다.
 * 기본값은 1입니다.
 * @param lightness 배경의 명도 계수를 지정합니다.
 * 계수가 -1일 경우 명도 최소치인 검정색으로, 1일 경우 명도 최대치인 흰색으로 표시됩니다. 오버레이에는 적용되지 않습니다.
 * 기본값은 0입니다.
 * @param symbolScale 심벌의 크기 배율을 지정합니다. 배율이 0.5일 경우 절반, 2일 경우 두 배의 크기로 표시됩니다.
 * 기본값은 1입니다.
 * @param symbolPerspectiveRatio 지도를 기울일 때 적용되는 심벌의 원근 계수를 지정합니다.
 * 계수가 1일 경우 배경 지도와 동일한 비율로 멀리 있는 심벌은 작아지고 가까이 있는 심벌은 커지며, 0에 가까울수록 원근 효과가 감소합니다.
 * 기본값은 1입니다.
 * @param backgroundColor 지도의 배경색을 지정합니다. 배경은 해당 지역의 지도 데이터가 없거나 로딩 중일 때 나타납니다.
 * 기본값은 [NaverMapConstants.DefaultBackgroundColorLight]입니다.
 * @param backgroundResource 지도의 배경 리소스를 지정합니다. 배경은 해당 지역의 지도 데이터가 없거나 로딩 중일 때 나타납니다.
 * resId가 올바르지 않을 경우 backgroundColor(int)를 이용해 지정된 배경색이 사용됩니다.
 * 기본값은 [NaverMapConstants.DefaultBackgroundDrawableLight]입니다.
 * @param locationTrackingMode
 */
public data class MapProperties(
    public val mapType: MapType = MapType.Basic,
    public val extent: LatLngBounds? = null,
    public val minZoom: Double = NaverMapConstants.MinZoom,
    public val maxZoom: Double = NaverMapConstants.MaxZoom,
    public val maxTilt: Double = NaverMapConstants.DefaultMaxTilt,
    public val defaultCameraAnimationDuration: Int = NaverMapConstants.DefaultCameraAnimationDuration,
    public val fpsLimit: Int = 0,
    public val isBuildingLayerGroupEnabled: Boolean = true,
    public val isTransitLayerGroupEnabled: Boolean = false,
    public val isBicycleLayerGroupEnabled: Boolean = false,
    public val isTrafficLayerGroupEnabled: Boolean = false,
    public val isCadastralLayerGroupEnabled: Boolean = false,
    public val isMountainLayerGroupEnabled: Boolean = false,
    public val isLiteModeEnabled: Boolean = false,
    public val isNightModeEnabled: Boolean = false,
    public val isIndoorEnabled: Boolean = false,
    public val indoorFocusRadius: Dp = NaverMapConstants.DefaultIndoorFocusRadius,
    public val buildingHeight: Float = 1f,
    public val lightness: Float = 0f,
    public val symbolScale: Float = 1f,
    public val symbolPerspectiveRatio: Float = 1f,
    public val backgroundColor: Color = NaverMapConstants.DefaultBackgroundColorLight,
    @DrawableRes
    public val backgroundResource: Int = NaverMapConstants.DefaultBackgroundDrawableLight,
    public val locationTrackingMode: LocationTrackingMode = LocationTrackingMode.None,
) {
    init {
        require(fpsLimit >= 0) { "fpsLimit must be greater than 0." }
        require(buildingHeight in 0f..1f) { "buildingHeight must be between 0f and 1f." }
        require(lightness in -1f..1f) { "lightness must be between -1f and 1f." }
        require(symbolScale in 0f..2f) { "symbolScale must be between 0f and 2f." }
        require(symbolPerspectiveRatio in 0f..1f) { "symbolPerspectiveRatio must be between 0f and 1f." }
    }
}

/**
 * 지도의 유형을 나타내는 열거형.
 */
@Immutable
public enum class MapType(public val value: com.naver.maps.map.NaverMap.MapType) {
    /**
     * 일반 지도.
     */
    Basic(com.naver.maps.map.NaverMap.MapType.Basic),

    /**
     * 내비게이션 지도.
     */
    Navi(com.naver.maps.map.NaverMap.MapType.Navi),

    /**
     * 위성 지도.
     */
    Satellite(com.naver.maps.map.NaverMap.MapType.Satellite),

    /**
     * 위성 지도(겹쳐보기).
     */
    Hybrid(com.naver.maps.map.NaverMap.MapType.Hybrid),

    /**
     * 지형도.
     */
    Terrain(com.naver.maps.map.NaverMap.MapType.Terrain),

    /**
     * 없음. 지도는 나타나지 않고 오버레이만이 나타납니다.
     */
    None(com.naver.maps.map.NaverMap.MapType.None)
}

/**
 * 위치 추적 모드를 나타내는 열거형.
 */
@Immutable
public enum class LocationTrackingMode(public val value: com.naver.maps.map.LocationTrackingMode) {
    /**
     * 위치 추적을 사용하지 않는 모드. [com.naver.maps.map.overlay.LocationOverlay]는 움직이지 않습니다.
     */
    None(com.naver.maps.map.LocationTrackingMode.None),

    /**
     * 위치는 추적하지만 지도는 움직이지 않는 모드. [com.naver.maps.map.overlay.LocationOverlay]가 사용자의 위치를
     * 따라 움직이나 지도는 움직이지 않습니다.
     */
    NoFollow(com.naver.maps.map.LocationTrackingMode.NoFollow),

    /**
     * 위치를 추적하면서 카메라도 따라 움직이는 모드. [com.naver.maps.map.overlay.LocationOverlay]와 카메라의
     * 좌표가 사용자의 위치를 따라 움직입니다. API나 제스처를 사용해 지도를 임의로 움직일 경우 모드가 [NoFollow]로 바뀝니다.
     */
    Follow(com.naver.maps.map.LocationTrackingMode.Follow),

    /**
     * 위치를 추적하면서 카메라의 좌표와 베어링도 따라 움직이는 모드.
     * [com.naver.maps.map.overlay.LocationOverlay]와 카메라의 좌표, 베어링이 사용자의 위치, 사용자가 바라보고 있는
     * 방향을 따라 움직입니다. API나 제스처를 사용해 지도를 임의로 움직일 경우 모드가 [NoFollow]로 바뀝니다.
     */
    Face(com.naver.maps.map.LocationTrackingMode.Face)
}
