/*
 * Copyright 2024 SOUP
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

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

public data class LatLng(
    val latitude: Double,
    val longitude: Double,
) {

    public val isValid: Boolean
        get() = !latitude.isNaN() &&
            !longitude.isNaN() &&
            !latitude.isInfinite() &&
            !longitude.isInfinite()

    public val isWithinCoverage: Boolean
        get() = isValid && this in COVERAGE

    public fun wrap(): LatLng {
        return if (!(longitude < MINIMUM_LONGITUDE) && !(longitude > MAXIMUM_LONGITUDE)) {
            this
        } else {
            LatLng(
                latitude = latitude,
                longitude = longitude.coerceIn(MINIMUM_LONGITUDE, MAXIMUM_LONGITUDE),
            )
        }
    }

    public fun distanceTo(other: LatLng): Double {
        if (this.latitude == other.latitude && this.longitude == other.longitude) {
            return 0.0
        } else {
            val lat1 = Math.toRadians(this.latitude)
            val lng1 = Math.toRadians(this.longitude)
            val lat2 = Math.toRadians(other.latitude)
            val lng2 = Math.toRadians(other.longitude)
            return 1.2756274E7 * asin(sqrt(sin((lat1 - lat2) / 2.0).pow(2.0) + cos(lat1) * cos(lat2) * sin((lng1 - lng2) / 2.0).pow(2.0)))
        }
    }

    public fun offset(northMeter: Double, eastMeter: Double): LatLng {
        return LatLng(
            latitude = this.latitude + Math.toDegrees(northMeter / 6378137.0),
            longitude = this.longitude + Math.toDegrees(eastMeter / (6378137.0 * cos(Math.toRadians(latitude)))),
        )
    }

    // TODO: remove this
    public fun asOriginal(): com.naver.maps.geometry.LatLng {
        return com.naver.maps.geometry.LatLng(latitude, longitude)
    }

    public companion object {
        public val INVALID: LatLng = LatLng(Double.NaN, Double.NaN)
        public const val MINIMUM_LATITUDE: Double = -90.0
        public const val MAXIMUM_LATITUDE: Double = 90.0
        public const val MINIMUM_LONGITUDE: Double = -180.0
        public const val MAXIMUM_LONGITUDE: Double = 180.0
        public val COVERAGE: LatLngBounds = LatLngBounds(
            southWest = LatLng(latitude = -90.0, longitude = -180.0),
            northEast = LatLng(latitude = 90.0, longitude = 180.0),
        )

        // TODO: remove this
        public fun fromOriginal(original: com.naver.maps.geometry.LatLng): LatLng {
            return LatLng(
                latitude = original.latitude,
                longitude = original.longitude,
            )
        }
    }
}
