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

import kotlin.math.max
import kotlin.math.min

public data class LatLngBounds(
    public val southWest: LatLng,
    public val northEast: LatLng,
) {

    private constructor(
        northLatitude: Double,
        eastLongitude: Double,
        southLatitude: Double,
        westLongitude: Double,
    ) : this(
        southWest = LatLng(latitude = southLatitude, longitude = westLongitude),
        northEast = LatLng(latitude = northLatitude, longitude = eastLongitude),
    )

    public val southEast: LatLng get() = LatLng(southLatitude, eastLongitude)
    public val northWest: LatLng get() = LatLng(northLatitude, westLongitude)

    public val southLatitude: Double get() = southWest.latitude
    public val westLongitude: Double get() = southWest.longitude
    public val northLatitude: Double get() = northEast.latitude
    public val eastLongitude: Double get() = northEast.longitude

    public val vertexes: Array<LatLng>
        get() = arrayOf(southWest, northWest, northEast, southEast)

    public fun toPolygon(): Array<LatLng> {
        return arrayOf(southWest, northWest, northEast, southEast, southWest)
    }

    public val isValid: Boolean
        get() = southWest.isValid && northWest.isValid

    public val isEmpty: Boolean
        get() {
            return !isValid ||
                southLatitude >= northLatitude ||
                westLongitude >= eastLongitude
        }

    public val center: LatLng
        get() = LatLng(
            latitude = (southLatitude + northLatitude) / 2,
            longitude = (westLongitude + eastLongitude) / 2,
        )

    public operator fun contains(latLng: LatLng): Boolean {
        return southLatitude <= latLng.latitude &&
            westLongitude <= latLng.longitude &&
            northLatitude >= latLng.latitude &&
            eastLongitude >= latLng.longitude
    }

    public operator fun contains(bounds: LatLngBounds): Boolean {
        return southLatitude <= bounds.southLatitude &&
            westLongitude <= bounds.westLongitude &&
            northLatitude >= bounds.northLatitude &&
            eastLongitude >= bounds.eastLongitude
    }

    public fun intersects(bounds: LatLngBounds): Boolean {
        return southLatitude <= bounds.northLatitude &&
            westLongitude <= bounds.eastLongitude &&
            northLatitude >= bounds.southLatitude &&
            eastLongitude >= bounds.westLongitude
    }

    public fun intersection(bounds: LatLngBounds): LatLngBounds? {
        val maxWest = max(westLongitude, bounds.westLongitude)
        val minEast = min(eastLongitude, bounds.eastLongitude)
        if (minEast < maxWest) {
            return null
        } else {
            val maxSouth = max(southLatitude, bounds.southLatitude)
            val minNorth = min(northLatitude, bounds.northLatitude)
            return if (minNorth < maxSouth) {
                null
            } else {
                LatLngBounds(
                    northLatitude = minNorth,
                    eastLongitude = minEast,
                    southLatitude = maxSouth,
                    westLongitude = maxWest,
                )
            }
        }
    }

    public fun expand(latLng: LatLng): LatLngBounds {
        return if (contains(latLng)) {
            this
        } else {
            LatLngBounds(
                northLatitude = max(northLatitude, latLng.latitude),
                eastLongitude = max(eastLongitude, latLng.longitude),
                southLatitude = min(southLatitude, latLng.latitude),
                westLongitude = min(westLongitude, latLng.longitude),
            )
        }
    }

    public fun buffer(meter: Double): LatLngBounds {
        return LatLngBounds(
            southWest = southWest.offset(-meter, -meter),
            northEast = northEast.offset(meter, meter),
        )
    }

    public fun union(bounds: LatLngBounds): LatLngBounds {
        return if (contains(bounds)) {
            this
        } else {
            LatLngBounds(
                northLatitude = max(northLatitude, bounds.northLatitude),
                eastLongitude = max(eastLongitude, bounds.eastLongitude),
                southLatitude = min(southLatitude, bounds.southLatitude),
                westLongitude = min(westLongitude, bounds.westLongitude),
            )
        }
    }

    public class Builder {
        private val coords = mutableListOf<LatLng>()

        public fun include(latLng: LatLng?): Builder {
            if (latLng != null && latLng.isValid) {
                coords.add(latLng)
            }
            return this
        }

        public fun include(vararg latLngs: LatLng): Builder {
            return include(latLngs.toList())
        }

        public fun include(latLngs: Collection<LatLng>): Builder {
            latLngs.forEach { latLng ->
                include(latLng)
            }
            return this
        }

        public fun build(): LatLngBounds {
            val result: LatLngBounds? = buildOrNull()
            requireNotNull(result) { "coordinates are empty; call include() first" }
            return result
        }

        public fun buildOrNull(): LatLngBounds? {
            if (coords.isEmpty()) {
                return null
            } else {
                return LatLngBounds(
                    northLatitude = coords.maxOf { it.latitude },
                    eastLongitude = coords.maxOf { it.longitude },
                    southLatitude = coords.minOf { it.latitude },
                    westLongitude = coords.minOf { it.longitude },
                )
            }
        }
    }

    public companion object {
        public val INVALID: LatLngBounds = LatLngBounds(LatLng.INVALID, LatLng.INVALID)
        public val WORLD: LatLngBounds = LatLngBounds(LatLng(-90.0, -180.0), LatLng(90.0, 180.0))

        public fun from(vararg latLngs: LatLng): LatLngBounds {
            return from(latLngs.toList())
        }

        public fun fromOrNull(vararg latLngs: LatLng): LatLngBounds? {
            return fromOrNull(latLngs.toList())
        }

        public fun from(latLngs: Collection<LatLng>): LatLngBounds {
            val result = fromOrNull(latLngs)
            requireNotNull(result) { "latLngs are empty" }
            return result
        }

        public fun fromOrNull(latLngs: Collection<LatLng>): LatLngBounds? {
            return (Builder()).include(latLngs).buildOrNull()
        }
    }
}
