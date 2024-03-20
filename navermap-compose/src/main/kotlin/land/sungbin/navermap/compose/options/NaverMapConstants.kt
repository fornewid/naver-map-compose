/*
 * Copyright 2024 SOUP, Ji Sungbin
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

@file:Suppress("FloatingPointLiteralPrecision", "ConstPropertyName")

package land.sungbin.navermap.compose.options

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap

public object NaverMapConstants {
  /** 지도에서 표현할 수 있는 최소 줌 레벨 */
  public const val MinZoom: Double = NaverMap.MINIMUM_ZOOM.toDouble()

  /** 지도에서 표현할 수 있는 최대 줌 레벨 */
  public const val MaxZoom: Double = NaverMap.MAXIMUM_ZOOM.toDouble()

  /** 지도에서 표현할 수 있는 최소 기울기 각도 */
  public const val MinTilt: Double = NaverMap.MINIMUM_TILT.toDouble()

  /** 지도에서 표현할 수 있는 최대 기울기 각도 */
  public const val MaxTilt: Double = NaverMap.MAXIMUM_TILT.toDouble()

  /** 기본 최대 기울기 각도 */
  public const val DefaultMaxTilt: Double = NaverMap.DEFAULT_MAXIMUM_TILT.toDouble()

  /** 지도에서 표현할 수 있는 최소 베어링 각도 */
  public const val MinBearing: Double = NaverMap.MINIMUM_BEARING.toDouble()

  /** 지도에서 표현할 수 있는 최대 베어링 각도 */
  public const val MaxBearing: Double = NaverMap.MAXIMUM_BEARING.toDouble()

  /** 카메라 애니메이션의 기본 지속 시간 (밀리초 단위) */
  public const val DefaultCameraAnimationDuration: Int = NaverMap.DEFAULT_DEFAULT_CAMERA_ANIMATION_DURATION

  /** 기본 실내지도 영역 포커스 반경 (DP 단위) */
  public val DefaultIndoorFocusRadius: Dp = NaverMap.DEFAULT_INDOOR_FOCUS_RADIUS_DP.dp

  /** 지도 클릭 시 피킹되는 Pickable의 기본 클릭 허용 반경 (DP 단위) */
  public val DefaultPickTolerance: Dp = NaverMap.DEFAULT_PICK_TOLERANCE_DP.dp

  /** 기본 스크롤 제스처 마찰 계수 */
  public const val DefaultScrollGesturesFriction: Float = 0.08799999952316284f

  /** 기본 줌 제스처 마찰 계수 */
  public const val DefaultZoomGesturesFriction: Float = 0.1237500011920929f

  /** 기본 회전 제스처 마찰 계수 */
  public const val DefaultRotateGesturesFriction: Float = 0.19333000481128693f

  /** 지도의 기본 카메라 위치 */
  public val DefaultCameraPosition: CameraPosition = NaverMap.DEFAULT_CAMERA_POSITION

  /** 기본 밝은 배경색 */
  public val DefaultBackgroundColorLight: Color = Color(NaverMap.DEFAULT_BACKGROUND_COLOR_LIGHT)

  /** 기본 어두운 배경색 */
  public val DefaultBackgroundColorDark: Color = Color(NaverMap.DEFAULT_BACKGROUND_COLOR_DARK)

  /** 기본 밝은 배경 이미지 */
  @DrawableRes
  public val DefaultBackgroundDrawableLight: Int = NaverMap.DEFAULT_BACKGROUND_DRWABLE_LIGHT

  /** 기본 어두운 배경 이미지 */
  @DrawableRes
  public val DefaultBackgroundDrawableDark: Int = NaverMap.DEFAULT_BACKGROUND_DRWABLE_DARK
}
