package com.naver.maps.map.compose

import android.content.Context
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import com.naver.maps.map.clustering.ClusterMarkerInfo
import com.naver.maps.map.clustering.ClusterMarkerUpdater
import com.naver.maps.map.clustering.LeafMarkerInfo
import com.naver.maps.map.clustering.LeafMarkerUpdater
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Closeable

internal class ComposeClusterMarkerUpdater(
    private val context: Context,
    private val composeUiViewRenderer: ComposeUiViewRenderer,
    private val coroutineScope: CoroutineScope,
    private val clusterContent: State<@Composable (ClusterMarkerInfo) -> Unit>,
    private val onClickCluster: State<(ClusterMarkerInfo, Overlay) -> Boolean>,
    private val leafContent: State<@Composable (LeafMarkerInfo) -> Unit>,
    private val onClickLeaf: State<(LeafMarkerInfo, Overlay) -> Boolean>,
    private val updateClusterMarkerData: State<((ClusterMarkerInfo, Marker) -> Unit)?>,
    private val updateLeafMarkerData: State<((LeafMarkerInfo, Marker) -> Unit)?>,
) : ClusterMarkerUpdater, LeafMarkerUpdater {

    private val fakeCanvas = Canvas()

    override fun updateLeafMarker(
        info: LeafMarkerInfo,
        marker: Marker,
    ) {
        val tag = marker.tag as? Closeable
        tag?.close()

        updateLeafMarkerData.value?.invoke(info, marker)
        val view = InvalidatingComposeView(context, { leafContent.value.invoke(info) })
        val handle = composeUiViewRenderer.startRenderingView(view)
        val job = collectInvalidateAndRerender(view, marker)
        marker.onClickListener = Overlay.OnClickListener { overlay ->
            onClickLeaf.value.invoke(info, overlay)
        }

        marker.tag = Closeable {
            handle.dispose()
            job.cancel()
        }
    }

    override fun updateClusterMarker(
        info: ClusterMarkerInfo,
        marker: Marker,
    ) {
        val tag = marker.tag as? Closeable
        tag?.close()

        updateClusterMarkerData.value?.invoke(info, marker)
        val view = InvalidatingComposeView(context, { clusterContent.value.invoke(info) })
        val handle = composeUiViewRenderer.startRenderingView(view)
        val job = collectInvalidateAndRerender(view, marker)
        marker.onClickListener = Overlay.OnClickListener { overlay ->
            onClickCluster.value.invoke(info, overlay)
        }

        marker.tag = Closeable {
            handle.dispose()
            job.cancel()
        }
    }

    private fun collectInvalidateAndRerender(
        view: InvalidatingComposeView,
        marker: Marker,
    ): Job {
        return coroutineScope.launch {
            callbackFlow {
                var invalidated = false
                view.onInvalidate = {
                    if (!invalidated) {
                        launch {
                            awaitFrame()
                            trySend(Unit)
                            invalidated = false
                        }
                        invalidated = true
                    }
                }
                view.doOnAttach {
                    launch {
                        awaitFrame()
                        trySend(Unit)
                    }
                    view.doOnDetach { close() }
                }
                awaitClose()
            }
                .collectLatest {
                    marker.icon = OverlayImage.fromBitmap(renderViewToBitmap(fakeCanvas, view))
                }
        }
    }
}
