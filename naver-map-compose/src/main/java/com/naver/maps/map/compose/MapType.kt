package com.naver.maps.map.compose

import androidx.compose.runtime.Immutable

/**
 * 지도의 유형을 나타내는 열거형.
 */
@Immutable
public sealed interface MapType {
    /**
     * 일반 지도.
     */
    public data object Basic : MapType

    /**
     * 내비게이션 지도.
     */
    public data object Navi : MapType

    /**
     * 위성 지도.
     */
    public data object Satellite : MapType

    /**
     * 위성 지도(겹쳐보기).
     */
    public data object Hybrid : MapType

    /**
     * 내비게이션용 위성 지도(겹쳐보기).
     */
    public data object NaviHybrid : MapType

    /**
     * 지형도.
     */
    public data object Terrain : MapType

    /**
     * 없음. 지도는 나타나지 않고 오버레이만이 나타납니다.
     */
    public data object None : MapType
}
