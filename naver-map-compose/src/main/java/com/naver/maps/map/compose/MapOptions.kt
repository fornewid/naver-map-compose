/*
 * Copyright 2025 SOUP
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

internal val DefaultMapOptions = MapOptions()

/**
 * 지도의 초기 옵션을 지정하는 클래스.
 *
 * @param preserveEGLContextOnPause GLSurfaceView의 preserveEGLContextOnPause를 활성화할지 여부를 지정합니다.
 * GLSurfaceView를 사용하지 않고 TextureView를 사용하는 경우에는 무시됩니다.
 * 기본값은 true입니다.
 * @param useTextureView 지도 렌더링을 위해 GLSurfaceView대신 TextureView를 사용할지 여부를 지정합니다.
 * 기본값은 false입니다.
 * @param translucentTextureSurface TextureView를 투명하게 만들지 여부를 지정합니다.
 * TextureView를 사용하지 않고 GLSurfaceView를 사용하는 경우에는 무시됩니다.
 * 기본값은 false입니다.
 * @param msaaEnabled 4x MSAA를 적용할지 여부를 지정합니다. 디바이스가 4x MSAA를 지원하지 않으면 무시됩니다.
 * 기본값은 false입니다.
 */
public data class MapOptions(
    val preserveEGLContextOnPause: Boolean = true,
    val useTextureView: Boolean = false,
    val translucentTextureSurface: Boolean = false,
    val msaaEnabled: Boolean = false,
)
