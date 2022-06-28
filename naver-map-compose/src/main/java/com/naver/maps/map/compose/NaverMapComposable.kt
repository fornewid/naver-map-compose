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

import androidx.compose.runtime.ComposableTargetMarker

/**
 * [NaverMapComposable] composable 함수에서 사용할 것으로 예상되는 composable 함수를 표시하는데 사용합니다.
 *
 * [NaverMapComposable] content lambda 외부에서 [NaverMapComposable] composable 함수를 사용하거나
 * 그 반대의 경우에도 빌드 경고를 생성합니다.
 */
@Retention(AnnotationRetention.BINARY)
@ComposableTargetMarker(description = "Naver Map Composable")
@Target(
    AnnotationTarget.FILE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPE_PARAMETER,
)
public annotation class NaverMapComposable
