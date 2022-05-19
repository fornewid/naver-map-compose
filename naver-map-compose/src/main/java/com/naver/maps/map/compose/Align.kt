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

import androidx.compose.runtime.Immutable

@Immutable
public enum class Align(public val value: com.naver.maps.map.overlay.Align) {
    Center(com.naver.maps.map.overlay.Align.Center),
    Left(com.naver.maps.map.overlay.Align.Left),
    Right(com.naver.maps.map.overlay.Align.Right),
    Top(com.naver.maps.map.overlay.Align.Top),
    Bottom(com.naver.maps.map.overlay.Align.Bottom),
    TopLeft(com.naver.maps.map.overlay.Align.TopLeft),
    TopRight(com.naver.maps.map.overlay.Align.TopRight),
    BottomRight(com.naver.maps.map.overlay.Align.BottomRight),
    BottomLeft(com.naver.maps.map.overlay.Align.BottomLeft),
    ;

    public companion object {
        public val EDGES: Array<Align> = arrayOf(Bottom, Right, Left, Top)
        public val APEXES: Array<Align> = arrayOf(BottomRight, BottomLeft, TopRight, TopLeft)
        public val OUTSIDES: Array<Align> = arrayOf(
            Bottom, Right, Left, Top, BottomRight, BottomLeft, TopRight, TopLeft
        )
    }
}
