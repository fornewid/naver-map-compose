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

import androidx.annotation.UiThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.Projection
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Integer.MAX_VALUE
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * [CameraPositionState]를 만들고 [CameraPositionState.Saver]를 사용하여 [rememberSaveable]합니다.
 * [init]은 초기 상태를 구성하기 위해 [CameraPositionState]가 처음 생성될 때 호출됩니다.
 */
@Composable
public inline fun rememberCameraPositionState(
    key: String? = null,
    crossinline init: CameraPositionState.() -> Unit = {},
): CameraPositionState = rememberSaveable(key = key, saver = CameraPositionState.Saver) {
    CameraPositionState().apply(init)
}

/**
 * 지도의 카메라 상태를 제어하고 관찰할 수 있는 상태 개체입니다.
 * [CameraPositionState]는 단일 [MapView]의 상태를 반영하므로 한 번에 하나의 [NaverMap] composable에서만 사용할 수 있습니다.
 *
 * @param position 초기 좌표를 지정합니다.
 */
public class CameraPositionState(
    position: CameraPosition = NaverMap.DEFAULT_CAMERA_POSITION,
) {
    /**
     * 카메라가 현재 움직이고 있는지 여부입니다. 여기에는 이동, 확대/축소 또는 회전과 같은 모든 종류의 이동이 포함됩니다.
     */
    public var isMoving: Boolean by mutableStateOf(false)
        internal set

    /**
     * 이 지도의 화면 좌표와 위도/경도 간의 변환에 사용할 [Projection] 객체를 반환합니다. 항상 같은 객체가 반환됩니다.
     */
    public val projection: Projection?
        get() = map?.projection

    /**
     * Local source of truth for the current camera position.
     * While [map] is non-null this reflects the current position of [map] as it changes.
     * While [map] is null it reflects the last known map position, or the last value set by
     * explicitly setting [position].
     */
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
                    map.moveCamera(CameraUpdate.toCameraPosition(value))
                }
            }
        }

    /**
     * 지도의 콘텐츠 영역에 대한 [LatLngBounds]를 반환합니다.
     * 콘텐츠 패딩이 모두 0이면 [coveringBounds]와 동일한 영역이,
     * 콘텐츠 패딩이 지정되어 있으면 [coveringBounds]에서 콘텐츠 패딩을 제외한 영역이 반환됩니다.
     */
    public val contentBounds: LatLngBounds?
        get() = map?.contentBounds

    /**
     * 지도의 콘텐츠 영역에 대한 좌표열을 반환합니다. 좌표열은 네 개의 좌표로 구성된 사각형으로 표현됩니다.
     * 단, 반환되는 배열의 크기는 5이며, 첫 번째 원소와 마지막 원소가 동일한 지점을 가리킵니다.
     * 콘텐츠 패딩이 모두 0이면 [coveringRegion]과 동일한 사각형이,
     * 콘텐츠 패딩이 지정되어 있으면 [coveringRegion]에서 콘텐츠 패딩을 제외한 사각형이 반환됩니다.
     */
    public val contentRegion: Array<LatLng>?
        get() = map?.contentRegion

    /**
     * 콘텐츠 패딩을 포함한 지도의 뷰 전체 영역에 대한 [LatLngBounds]를 반환합니다.
     */
    public val coveringBounds: LatLngBounds?
        get() = map?.coveringBounds

    /**
     * 콘텐츠 패딩을 포함한 지도의 뷰 전체 영역에 대한 좌표열을 반환합니다. 좌표열은 네 개의 좌표로 구성된 사각형으로 표현됩니다.
     * 단, 반환되는 배열의 크기는 5이며, 첫 번째 원소와 마지막 원소가 동일한 지점을 가리킵니다.
     */
    public val coveringRegion: Array<LatLng>?
        get() = map?.coveringRegion

    /**
     * 콘텐츠 패딩을 포함한 지도의 뷰 전체를 완전히 덮는 타일 ID의 목록을 반환합니다.
     */
    public val coveringTileIds: LongArray?
        get() = map?.coveringTileIds

    /**
     * 콘텐츠 패딩을 포함한 지도의 뷰 전체를 완전히 덮는 zoom 레벨 타일 ID의 목록을 반환합니다.
     */
    public fun getCoveringTileIdsAtZoom(zoom: Int): LongArray? {
        return map?.getCoveringTileIdsAtZoom(zoom)
    }

    /**
     * 위치 추적 모드를 반환합니다. 기본값은 [LocationTrackingMode.None]입니다.
     */
    public val locationTrackingMode: LocationTrackingMode?
        get() = when (map?.locationTrackingMode) {
            com.naver.maps.map.LocationTrackingMode.None -> LocationTrackingMode.None
            com.naver.maps.map.LocationTrackingMode.NoFollow -> LocationTrackingMode.NoFollow
            com.naver.maps.map.LocationTrackingMode.Follow -> LocationTrackingMode.Follow
            com.naver.maps.map.LocationTrackingMode.Face -> LocationTrackingMode.Face
            null -> null
        }

    // Used to perform side effects thread-safely.
    // Guards all mutable properties that are not `by mutableStateOf`.
    private val lock = Any()

    // The map currently associated with this CameraPositionState.
    // Guarded by `lock`.
    private var map: NaverMap? = null

    // An action to run when the map becomes available or unavailable.
    // represents a mutually exclusive mutation to perform while holding `lock`.
    // Guarded by `lock`.
    private var onMapChanged: OnMapChangedCallback? = null

    /**
     * Set [onMapChanged] to [callback], invoking the current callback's
     * [OnMapChangedCallback.onCancelLocked] if one is present.
     */
    private fun doOnMapChangedLocked(callback: OnMapChangedCallback) {
        onMapChanged?.onCancelLocked()
        onMapChanged = callback
    }

    // A token representing the current owner of any ongoing motion in progress.
    // Used to determine if map animation should stop when calls to animate end.
    // Guarded by `lock`.
    private var movementOwner: Any? = null

    /**
     * Used with [onMapChangedLocked] to execute one-time actions when a map becomes available
     * or is made unavailable. Cancellation is provided in order to resume suspended coroutines
     * that are awaiting the execution of one of these callbacks that will never come.
     */
    private fun interface OnMapChangedCallback {
        fun onMapChangedLocked(newMap: NaverMap?)
        fun onCancelLocked() {}
    }

    // The current map is set and cleared by side effect.
    // There can be only one associated at a time.
    internal fun setMap(map: NaverMap?) {
        synchronized(lock) {
            if (this.map == null && map == null) return
            if (this.map != null && map != null) {
                error("CameraPositionState may only be associated with one NaverMap at a time")
            }
            this.map = map
            if (map == null) {
                isMoving = false
            } else {
                map.moveCamera(CameraUpdate.toCameraPosition(position))
            }
            onMapChanged?.let {
                // Clear this first since the callback itself might set it again for later
                onMapChanged = null
                it.onMapChangedLocked(map)
            }
        }
    }

    /**
     * Animate the camera position as specified by [update], returning once the animation has
     * completed. [position] will reflect the position of the camera as the animation proceeds.
     *
     * [animate] will throw [CancellationException] if the animation does not fully complete.
     * This can happen if:
     *
     * * The user manipulates the map directly
     * * [position] is set explicitly, e.g. `state.position = CameraPosition(...)`
     * * [animate] is called again before an earlier call to [animate] returns
     * * [move] is called
     * * The calling job is [cancelled][kotlinx.coroutines.Job.cancel] externally
     *
     * If this [CameraPositionState] is not currently bound to a [NaverMap] this call will
     * suspend until a map is bound and animation will begin.
     *
     * This method should only be called from a dispatcher bound to the map's UI thread.
     *
     * @param update the change that should be applied to the camera
     * @param animation
     * @param durationMs The duration of the animation in milliseconds. If [Int.MAX_VALUE] is
     * provided, the default animation duration will be used. Otherwise, the value provided must be
     * strictly positive, otherwise an [IllegalArgumentException] will be thrown.
     */
    @ExperimentalNaverMapApi
    @UiThread
    public suspend fun animate(
        update: CameraUpdate,
        animation: CameraAnimation = CameraAnimation.Easing,
        durationMs: Int = MAX_VALUE,
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
                            override fun onMapChangedLocked(newMap: NaverMap?) {
                                if (newMap == null) {
                                    // Cancel the animate caller and crash the map setter
                                    @Suppress("ThrowableNotThrown")
                                    continuation.resumeWithException(
                                        CancellationException("internal error; no NaverMap available")
                                    )
                                    error("internal error; no NaverMap available to animate position")
                                }
                                performAnimateCameraLocked(
                                    newMap,
                                    update,
                                    animation,
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
                        performAnimateCameraLocked(map, update, animation, durationMs, continuation)
                    }
                }
            }
        } finally {
            // continuation.invokeOnCancellation might be called from any thread, so stop the
            // animation in progress here where we're guaranteed to be back on the right dispatcher.
            synchronized(lock) {
                if (myJob != null && movementOwner === myJob) {
                    movementOwner = null
                    map?.cancelTransitions()
                }
            }
        }
    }

    private fun performAnimateCameraLocked(
        map: NaverMap,
        update: CameraUpdate,
        animation: CameraAnimation,
        durationMs: Int,
        continuation: CancellableContinuation<Unit>,
    ) {
        val cancelableCallback = object : CameraUpdate.CancelCallback, CameraUpdate.FinishCallback {
            override fun onCameraUpdateCancel() {
                continuation.resumeWithException(CancellationException("Animation cancelled"))
            }

            override fun onCameraUpdateFinish() {
                continuation.resume(Unit)
            }
        }
        if (durationMs == MAX_VALUE) {
            map.moveCamera(
                update.animate(animation)
                    .cancelCallback(cancelableCallback)
                    .finishCallback(cancelableCallback)
            )
        } else {
            map.moveCamera(
                update.animate(animation, durationMs.toLong())
                    .cancelCallback(cancelableCallback)
                    .finishCallback(cancelableCallback)
            )
        }
        doOnMapChangedLocked {
            check(it == null) {
                "New NaverMap unexpectedly set while an animation was still running"
            }
            map.cancelTransitions()
        }
    }

    /**
     * Move the camera instantaneously as specified by [update]. Any calls to [animate] in progress
     * will be cancelled. [position] will be updated when the bound map's position has been updated,
     * or if the map is currently unbound, [update] will be applied when a map is next bound.
     * Other calls to [move], [animate], or setting [position] will override an earlier pending
     * call to [move].
     *
     * This method must be called from the map's UI thread.
     */
    @ExperimentalNaverMapApi
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

    @ExperimentalNaverMapApi
    @UiThread
    public fun stop() {
        synchronized(lock) {
            val map = map
            movementOwner = null
            if (map == null) {
                // Do it when we have a map available
                doOnMapChangedLocked { it?.cancelTransitions() }
            } else {
                map.cancelTransitions()
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
