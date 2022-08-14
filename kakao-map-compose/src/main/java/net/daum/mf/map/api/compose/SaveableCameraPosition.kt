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
package net.daum.mf.map.api.compose

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.daum.mf.map.api.CameraPosition
import net.daum.mf.map.api.MapPoint

@Parcelize
public class SaveableCameraPosition(
    public val latitude: Double,
    public val longitude: Double,
    public val zoomLevel: Float,
) : Parcelable {

    public fun asModel(): CameraPosition {
        return CameraPosition(
            MapPoint.mapPointWithGeoCoord(latitude, longitude),
            zoomLevel,
        )
    }
}
