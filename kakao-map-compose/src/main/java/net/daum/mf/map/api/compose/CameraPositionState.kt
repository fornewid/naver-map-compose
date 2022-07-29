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

import androidx.annotation.UiThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.suspendCancellableCoroutine
import net.daum.mf.map.api.CameraPosition
import net.daum.mf.map.api.CameraUpdate
import net.daum.mf.map.api.CameraUpdateFactory
import net.daum.mf.map.api.CancelableCallback
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Composable
public inline fun rememberCameraPositionState(
    key: String? = null,
    crossinline init: CameraPositionState.() -> Unit = {},
): CameraPositionState = remember(key1 = key) {
    CameraPositionState().apply(init)
}

public class CameraPositionState(
    position: CameraPosition = CameraPosition(
        MapPoint.mapPointWithGeoCoord(37.5666102, 126.9783881),
        14f
    ),
) {
    public var isMoving: Boolean by mutableStateOf(false)
        internal set

    internal var rawPosition by mutableStateOf(position)

    /**
     * 지도에서 카메라의 현재 위치입니다.
     */
    public var position: CameraPosition
        get() = rawPosition
        set(value) {
            synchronized(lock) {
                val map = map
                if (map == null) {
                    rawPosition = value
                } else {
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(value))
                }
            }
        }

    private val lock = Any()

    private var map: MapView? = null

    private var onMapChanged: OnMapChangedCallback? = null

    private fun doOnMapChangedLocked(callback: OnMapChangedCallback) {
        onMapChanged?.onCancelLocked()
        onMapChanged = callback
    }

    private var movementOwner: Any? = null

    private fun interface OnMapChangedCallback {
        fun onMapChangedLocked(newMap: MapView?)
        fun onCancelLocked() {}
    }

    internal fun setMap(map: MapView?) {
        synchronized(lock) {
            if (this.map == null && map == null) return
            if (this.map != null && map != null) {
                error("CameraPositionState may only be associated with one DaumMap at a time")
            }
            this.map = map
            if (map == null) {
                isMoving = false
            } else {
                map.moveCamera(CameraUpdateFactory.newCameraPosition(position))
            }
            onMapChanged?.let {
                // Clear this first since the callback itself might set it again for later
                onMapChanged = null
                it.onMapChangedLocked(map)
            }
        }
    }

    @ExperimentalKakaoMapApi
    @UiThread
    public suspend fun animate(
        update: CameraUpdate,
        durationMs: Int = Integer.MAX_VALUE,
    ) {
        val myJob = currentCoroutineContext()[Job]
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                synchronized(lock) {
                    movementOwner = myJob
                    val map = map
                    if (map == null) {
                        // Do it later
                        val animateOnMapAvailable = object : OnMapChangedCallback {
                            override fun onMapChangedLocked(newMap: MapView?) {
                                if (newMap == null) {
                                    // Cancel the animate caller and crash the map setter
                                    @Suppress("ThrowableNotThrown")
                                    continuation.resumeWithException(
                                        CancellationException("internal error; no DaumMap available")
                                    )
                                    error("internal error; no NaverMap available to animate position")
                                }
                                performAnimateCameraLocked(
                                    newMap,
                                    update,
                                    durationMs,
                                    continuation
                                )
                            }

                            override fun onCancelLocked() {
                                continuation.resumeWithException(
                                    CancellationException("Animation cancelled")
                                )
                            }
                        }
                        doOnMapChangedLocked(animateOnMapAvailable)
                        continuation.invokeOnCancellation {
                            synchronized(lock) {
                                if (onMapChanged === animateOnMapAvailable) {
                                    // External cancellation shouldn't invoke onCancel
                                    // so we set this to null directly instead of going through
                                    // doOnMapChangedLocked(null).
                                    onMapChanged = null
                                }
                            }
                        }
                    } else {
                        performAnimateCameraLocked(map, update, durationMs, continuation)
                    }
                }
            }
        } finally {
            // continuation.invokeOnCancellation might be called from any thread, so stop the
            // animation in progress here where we're guaranteed to be back on the right dispatcher.
            synchronized(lock) {
                if (myJob != null && movementOwner === myJob) {
                    movementOwner = null
                    map?.stopAnimation()
                }
            }
        }
    }

    private fun performAnimateCameraLocked(
        map: MapView,
        update: CameraUpdate,
        durationMs: Int,
        continuation: CancellableContinuation<Unit>,
    ) {
        val cancelableCallback = object : CancelableCallback {
            override fun onCancel() {
                continuation.resumeWithException(CancellationException("Animation cancelled"))
            }

            override fun onFinish() {
                continuation.resume(Unit)
            }
        }
        if (durationMs == Integer.MAX_VALUE) {
            map.animateCamera(
                update,
                cancelableCallback
            )
        } else {
            map.animateCamera(
                update,
                durationMs.toFloat(),
                cancelableCallback
            )
        }
        doOnMapChangedLocked {
            check(it == null) {
                "New DaumMap unexpectedly set while an animation was still running"
            }
            map.stopAnimation()
        }
    }

    @ExperimentalKakaoMapApi
    @UiThread
    public fun move(update: CameraUpdate) {
        synchronized(lock) {
            val map = map
            movementOwner = null
            if (map == null) {
                // Do it when we have a map available
                doOnMapChangedLocked { it?.moveCamera(update) }
            } else {
                map.moveCamera(update)
            }
        }
    }

    @ExperimentalKakaoMapApi
    @UiThread
    public fun stop() {
        synchronized(lock) {
            val map = map
            movementOwner = null
            if (map == null) {
                // Do it when we have a map available
                doOnMapChangedLocked { it?.stopAnimation() }
            } else {
                map.stopAnimation()
            }
        }
    }

    public companion object {
        /**
         * [CameraPositionState]의 기본 [Saver]입니다.
         */
        public val Saver: Saver<CameraPositionState, CameraPosition> = Saver(
            save = { it.position },
            restore = { CameraPositionState(it) }
        )
    }
}
