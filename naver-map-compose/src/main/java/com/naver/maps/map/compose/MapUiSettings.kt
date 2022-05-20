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

import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.map.app.LegalNoticeActivity
import com.naver.maps.map.app.OpenSourceLicenseActivity

internal val DefaultMapUiSettings = MapUiSettings()

/**
 * 제스처 사용, 컨트롤 노출 등 사용자 인터페이스와 관련된 설정을 관리하는 데이터 클래스.
 * 설정으로 제스처나 컨트롤을 비활성화하더라도 API를 호출하면 여전히 카메라를 움직일 수 있습니다.
 *
 * Note: This is intentionally a class and not a data class for binary compatibility on future changes.
 * See: https://jakewharton.com/public-api-challenges-in-kotlin/
 *
 * @param pickTolerance 지도 클릭 시 피킹되는 Pickable의 클릭 허용 반경을 지정합니다. 사용자가 지도를 클릭했을 때,
 * 클릭된 지점이 Pickable의 영역 내에 존재하지 않더라도 허용 반경 내에 있다면 해당 요소가 클릭된 것으로 간주됩니다.
 * 기본값은 [NaverMapConstants.DefaultPickTolerance]입니다.
 * @param isScrollGesturesEnabled 스크롤 제스처를 활성화할지 여부를 지정합니다. 활성화하면 지도를 스와이프해 카메라의
 * 좌표를 변경할 수 있습니다. 기본값은 true입니다.
 * @param isZoomGesturesEnabled 줌 제스처를 활성화할지 여부를 지정합니다. 활성화하면 지도를 더블 탭, 두 손가락 탭, 핀치해
 * 카메라의 줌 레벨을 변경할 수 있습니다. 기본값은 true입니다.
 * @param isTiltGesturesEnabled 틸트 제스처를 활성화할지 여부를 지정합니다. 활성화하면 지도를 두 손가락으로 세로로
 * 스와이프해 카메라의 기울기 각도를 변경할 수 있습니다. 기본값은 true입니다.
 * @param isRotateGesturesEnabled 회전 제스처를 활성화할지 여부를 지정합니다. 활성화하면 두 손가락으로 지도를 돌려
 * 카메라의 베어링 각도를 변경할 수 있습니다. 기본값은 true입니다.
 * @param isStopGesturesEnabled 스톱 제스처를 활성화할지 여부를 지정합니다. 활성화하면 지도 애니메이션 진행 중 탭으로
 * 지도 애니메이션을 중지할 수 있습니다. 기본값은 true입니다.
 * @param scrollGesturesFriction 스크롤 제스처의 마찰 계수를 0~1로 지정합니다. 계수가 클수록 마찰이 강해집니다.
 * 기본값은 [NaverMapConstants.DefaultScrollGesturesFriction]입니다.
 * @param zoomGesturesFriction 줌 제스처의 마찰 계수를 0~1로 지정합니다. 계수가 클수록 마찰이 강해집니다.
 * 기본값은 [NaverMapConstants.DefaultZoomGesturesFriction]입니다.
 * @param rotateGesturesFriction 회전 제스처의 마찰 계수를 0~1로 지정합니다. 계수가 클수록 마찰이 강해집니다.
 * 기본값은 [NaverMapConstants.DefaultRotateGesturesFriction]입니다.
 * @param isCompassEnabled 나침반을 활성화할지 여부를 지정합니다. 활성화하면 나침반이 노출됩니다.
 * 기본값은 true입니다.
 * @param isScaleBarEnabled 축척 바를 활성화할지 여부를 지정합니다. 활성화하면 축척 바가 노출됩니다.
 * 기본값은 true입니다.
 * @param isZoomControlEnabled 줌 컨트롤을 활성화할지 여부를 지정합니다. 활성화하면 줌 컨트롤이 노출됩니다.
 * 기본값은 true입니다.
 * @param isIndoorLevelPickerEnabled 실내지도 층 피커를 활성화할지 여부를 지정합니다. 활성화하면 실내지도 패널이 노출됩니다.
 * 기본값은 false입니다.
 * @param isLocationButtonEnabled 현위치 버튼을 활성화할지 여부를 지정합니다. 활성화하면 현위치 버튼이 노출됩니다.
 * 기본값은 false입니다.
 * @param isLogoClickEnabled 네이버 로고 클릭을 활성화할지 여부를 지정합니다.
 * 활성화하면 네이버 로고 클릭시 범례, 법적 공지, 오픈소스 라이선스를 보여주는 다이얼로그가 열립니다.
 * 이 옵션을 false로 지정하는 앱은 반드시 앱 내에 네이버 지도 SDK의 법적 공지 액티비티([LegalNoticeActivity]) 및
 * 오픈소스 라이선스 액티비티([OpenSourceLicenseActivity])로 연결되는 메뉴를 만들어야 합니다.
 * 기본값은 true입니다.
 * @param logoGravity 네이버 로고의 그래비티를 지정합니다.
 * @param logoMargin 네이버 로고의 마진을 지정합니다.
 */
public data class MapUiSettings(
    public val pickTolerance: Dp = NaverMapConstants.DefaultPickTolerance,
    public val isScrollGesturesEnabled: Boolean = true,
    public val isZoomGesturesEnabled: Boolean = true,
    public val isTiltGesturesEnabled: Boolean = true,
    public val isRotateGesturesEnabled: Boolean = true,
    public val isStopGesturesEnabled: Boolean = true,
    public val scrollGesturesFriction: Float = NaverMapConstants.DefaultScrollGesturesFriction,
    public val zoomGesturesFriction: Float = NaverMapConstants.DefaultZoomGesturesFriction,
    public val rotateGesturesFriction: Float = NaverMapConstants.DefaultRotateGesturesFriction,
    public val isCompassEnabled: Boolean = true,
    public val isScaleBarEnabled: Boolean = true,
    public val isZoomControlEnabled: Boolean = true,
    public val isIndoorLevelPickerEnabled: Boolean = false,
    public val isLocationButtonEnabled: Boolean = false,
    public val isLogoClickEnabled: Boolean = true,
    public val logoGravity: Int = Gravity.BOTTOM or Gravity.START,
    public val logoMargin: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
) {
    init {
        require(scrollGesturesFriction in 0f..1f) { "scrollGesturesFriction must be between 0f and 1f." }
        require(zoomGesturesFriction in 0f..1f) { "zoomGesturesFriction must be between 0f and 1f." }
        require(rotateGesturesFriction in 0f..1f) { "rotateGesturesFriction must be between 0f and 1f." }
    }
}
