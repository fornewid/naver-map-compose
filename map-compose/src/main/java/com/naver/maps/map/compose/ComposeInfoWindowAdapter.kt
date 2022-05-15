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

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.platform.ComposeView
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

// TODO: (sungyong.an) InfoWindow 작업 필요
/**
 * An InfoWindowAdapter that returns a [ComposeView] for drawing a marker's
 * info window.
 *
 * Note: As of version 18.0.2 of the Maps SDK, info windows are drawn by
 * creating a bitmap of the [View]s returned in the [GoogleMap.InfoWindowAdapter]
 * interface methods. The returned views are never attached to a window,
 * instead, they are drawn to a bitmap canvas. This breaks the assumption
 * [ComposeView] makes where it must eventually be attached to a window. As a
 * workaround, the contained window is temporarily attached to the MapView so
 * that the contents of the ComposeViews are rendered.
 *
 * Eventually when info windows are no longer implemented this way, this
 * implementation should be updated.
 */
// internal class ComposeInfoWindowAdapter(
//    private val mapView: MapView,
//    private val markerNodeFinder: (Marker) -> MarkerNode?
// ) : NaverMap.InfoWindowAdapter {
//
//    private val infoWindowView: ComposeView
//        get() = ComposeView(mapView.context).apply {
//            mapView.addView(this)
//        }
//
//    override fun getInfoContents(marker: Marker): View? {
//        val markerNode = markerNodeFinder(marker) ?: return null
//        val content  = markerNode.infoContent
//        if (content == null) {
//            return null
//        }
//        return infoWindowView.applyAndRemove(markerNode.compositionContext) {
//            content(marker)
//        }
//    }
//
//    override fun getInfoWindow(marker: Marker): View? {
//        val markerNode = markerNodeFinder(marker) ?: return null
//        val infoWindow  = markerNode.infoWindow
//        if (infoWindow == null) {
//            return null
//        }
//        return infoWindowView.applyAndRemove(markerNode.compositionContext) {
//            infoWindow(marker)
//        }
//    }
//
//    private fun ComposeView.applyAndRemove(
//        parentContext: CompositionContext,
//        content: @Composable () -> Unit
//    ): ComposeView {
//        val result = this.apply {
//            setParentCompositionContext(parentContext)
//            setContent(content)
//        }
//        (this.parent as? MapView)?.removeView(this)
//        return result
//    }
// }
