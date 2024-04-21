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
package com.naver.maps.map.compose.demo.clustering

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.clustering.ClusteringKey

class ItemKey(val id: Int, private val latLng: LatLng) : ClusteringKey {
    override fun getPosition() = latLng

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val itemKey = other as ItemKey
        return id == itemKey.id
    }

    override fun hashCode() = id
}
