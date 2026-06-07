package com.naver.maps.map.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.UiComposable
import androidx.compose.ui.platform.LocalContext
import com.naver.maps.geometry.WebMercatorCoord
import com.naver.maps.map.clustering.Cluster
import com.naver.maps.map.clustering.ClusterMarkerInfo
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.ClusteringKey
import com.naver.maps.map.clustering.LeafMarkerInfo
import com.naver.maps.map.clustering.Node
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay

@Composable
internal fun <T : ClusteringKey> rememberClusterer(
    clusterContent: @Composable (ClusterMarkerInfo) -> Unit,
    onClickCluster: (ClusterMarkerInfo, Overlay) -> Boolean,
    leafContent: @Composable (LeafMarkerInfo) -> Unit,
    onClickLeaf: (LeafMarkerInfo, Overlay) -> Boolean,
    updateClusterMarkerData: ((ClusterMarkerInfo, Marker) -> Unit)? = null,
    updateLeafMarkerData: ((LeafMarkerInfo, Marker) -> Unit)? = null,
    thresholdStrategy: ((zoom: Int) -> Double)? = null,
    distanceStrategy: ((zoom: Int, node1: Node, node2: Node) -> Double)? = null,
    tagMergeStrategy: ((Cluster) -> Any?)? = null,
    positionStrategy: ((Cluster) -> WebMercatorCoord)? = null,
    maxScreenDistance: Double? = null,
    animationDuration: Int? = null,
    maxClusteringZoom: Int? = null,
    minClusteringZoom: Int? = null,
    maxIndexingZoom: Int? = null,
    minIndexingZoom: Int? = null,
    updateOnChange: Boolean = false,
): Clusterer<T> {
    val context = LocalContext.current
    val composeUiRenderer = rememberComposeUiViewRenderer()
    val coroutineScope = rememberCoroutineScope()
    val clusterContent = rememberUpdatedState(clusterContent)
    val onClickCluster = rememberUpdatedState(onClickCluster)
    val leafContent = rememberUpdatedState(leafContent)
    val onClickLeaf = rememberUpdatedState(onClickLeaf)
    val updateClusterMarkerData = rememberUpdatedState(updateClusterMarkerData)
    val updateLeafMarkerData = rememberUpdatedState(updateLeafMarkerData)
    val thresholdStrategyState by rememberUpdatedState(thresholdStrategy)
    val distanceStrategyState by rememberUpdatedState(distanceStrategy)
    val tagMergeStrategyState by rememberUpdatedState(tagMergeStrategy)
    val positionStrategyState by rememberUpdatedState(positionStrategy)
    val composeClusterMarkerUpdater = remember(
        context,
        composeUiRenderer,
        clusterContent,
        onClickCluster,
        leafContent,
        onClickLeaf,
        updateClusterMarkerData,
        updateLeafMarkerData
    ) {
        ComposeClusterMarkerUpdater(
            context,
            composeUiRenderer,
            coroutineScope,
            clusterContent,
            onClickCluster,
            leafContent,
            onClickLeaf,
            updateClusterMarkerData,
            updateLeafMarkerData
        )
    }
    val markerManager = remember { ComposeClusterMarkerManager() }
    return remember(
        composeClusterMarkerUpdater,
        markerManager,
        thresholdStrategyState,
        distanceStrategyState,
        maxScreenDistance,
        tagMergeStrategyState,
        positionStrategyState,
        animationDuration,
        maxClusteringZoom,
        minClusteringZoom,
        maxIndexingZoom,
        minIndexingZoom,
        updateOnChange,
    ) {
        Clusterer.ComplexBuilder<T>()
            .clusterMarkerUpdater(composeClusterMarkerUpdater)
            .leafMarkerUpdater(composeClusterMarkerUpdater)
            .markerManager(markerManager)
            .apply {
                if (thresholdStrategyState != null) {
                    thresholdStrategy(thresholdStrategyState!!)
                }
                if (distanceStrategyState != null) {
                    distanceStrategy(distanceStrategyState!!)
                }
                if (tagMergeStrategyState != null) {
                    tagMergeStrategy(tagMergeStrategyState!!)
                }
                if (positionStrategyState != null) {
                    positioningStrategy(positionStrategyState!!)
                }
                if (maxScreenDistance != null) {
                    this.maxScreenDistance(maxScreenDistance)
                }
                if (animationDuration != null) {
                    this.animationDuration(animationDuration)
                }
                if (maxClusteringZoom != null) {
                    this.maxClusteringZoom(maxClusteringZoom)
                }
                if (minClusteringZoom != null) {
                    this.minClusteringZoom(minClusteringZoom)
                }
                if (maxIndexingZoom != null) {
                    this.maxIndexingZoom(maxIndexingZoom)
                }
                if (minIndexingZoom != null) {
                    this.minIndexingZoom(minIndexingZoom)
                }
            }
            .updateOnChange(updateOnChange)
            .build()
    }
}

/**
 * 네이버맵에 클러스터링 기능을 추가합니다.
 *
 * @param items 클러스터링할 아이템들. key 는 [ClusteringKey]인터페이스를 구현해야 하며 value 는 각 마커 위치에 대한 태그값.
 * @param clusterContent 클러스터 마커의 UI 컴포저블.
 * @param onClickCluster 클러스터 마커 클릭 시 호출되는 콜백.
 * @param leafContent 리프 마커의 UI 컴포저블.
 * @param onClickLeaf 리프 마커 클릭 시 호출되는 콜백.
 * @param updateClusterMarkerData 클러스터 마커가 생성될 때 호출되는 콜백. 이 시점에 마커의 속성을 수정할 수 있다. 마커의 클릭 리스너는 이미
 * 설정되어 있으니 클릭리스너를 덮어쓰지 않도록 주의한다. 마커는 재사용되기 때문에 [updateLeafMarkerData] 에서 수정한 내용이 남아있을 수 있으니 필요한
 * 속성은 모두 설정해야 한다.
 * @param updateLeafMarkerData 리프 마커가 생성될 때 호출되는 콜백. 이 시점에 마커의 속성을 수정할 수 있다. 마커의 클릭 리스너는 이미
 * 설정되어 있으니 클릭리스너를 덮어쓰지 않도록 주의한다. 마커는 재사용되기 때문에 [updateClusterMarkerData] 에서 수정한 내용이 남아있을 수 있으니 필요한
 * 속성은 모두 설정해야 한다.
 * @param thresholdStrategy 두 Node를 클러스터링할 기준 거리를 구하는 전략을 지정합니다.
 * @param distanceStrategy 각 Node간의 거리를 측정하는 전략을 지정합니다.
 * @param tagMergeStrategy 부모 Cluster의 자식 Node들의 태그를 병합하는 전략을 지정합니다.
 * @param positionStrategy 한 Cluster의 좌표를 정하는 전략을 지정합니다.
 * @param maxScreenDistance 클러스터링할 최대 화면 거리를 지정합니다.
 * @param animationDuration 카메라 확대/축소시 클러스터가 펼쳐지는/합쳐지는 애니메이션의 지속 시간을 지정합니다.
 * @param maxClusteringZoom 클러스터링할 최대 줌 레벨을 반환합니다.
 * @param minClusteringZoom 클러스터링할 최소 줌 레벨을 지정합니다.
 * @param maxIndexingZoom 인덱싱할 최대 줌 레벨을 지정합니다.
 * @param minIndexingZoom 인덱싱할 최소 줌 레벨을 지정합니다.
 * @param updateOnChange 화면상 마커를 갱신할 때 [com.naver.maps.map.NaverMap.OnCameraIdleListener] 대신
 * [com.naver.maps.map.NaverMap.OnCameraChangeListener]를 사용할지 여부를 지정합니다.
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
@NaverMapComposable
public fun <T : ClusteringKey> Clustering(
    items: Map<T, Any?>,
    clusterContent: @[UiComposable Composable] (ClusterMarkerInfo) -> Unit,
    onClickCluster: (ClusterMarkerInfo, Overlay) -> Boolean,
    leafContent: @[UiComposable Composable] (LeafMarkerInfo) -> Unit,
    onClickLeaf: (LeafMarkerInfo, Overlay) -> Boolean,
    updateClusterMarkerData: ((ClusterMarkerInfo, Marker) -> Unit)? = null,
    updateLeafMarkerData: ((LeafMarkerInfo, Marker) -> Unit)? = null,
    thresholdStrategy: ((zoom: Int) -> Double)? = null,
    distanceStrategy: ((zoom: Int, node1: Node, node2: Node) -> Double)? = null,
    tagMergeStrategy: ((Cluster) -> Any?)? = null,
    positionStrategy: ((Cluster) -> WebMercatorCoord)? = null,
    maxScreenDistance: Double? = null,
    animationDuration: Int? = null,
    maxClusteringZoom: Int? = null,
    minClusteringZoom: Int? = null,
    maxIndexingZoom: Int? = null,
    minIndexingZoom: Int? = null,
    updateOnChange: Boolean = false,
) {
    val clusterer = rememberClusterer<T>(
        clusterContent,
        onClickCluster,
        leafContent,
        onClickLeaf,
        updateClusterMarkerData,
        updateLeafMarkerData,
        thresholdStrategy,
        distanceStrategy,
        tagMergeStrategy,
        positionStrategy,
        maxScreenDistance,
        animationDuration,
        maxClusteringZoom,
        minClusteringZoom,
        maxIndexingZoom,
        minIndexingZoom,
        updateOnChange,
    )
    val itemsState = rememberUpdatedState(items)

    LaunchedEffect(items) {
        snapshotFlow { itemsState.value }
            .collect {
                clusterer.clear()
                clusterer.addAll(it)
            }
    }
    MapEffect(clusterer) {
        clusterer.map = it
    }
    DisposableEffect(itemsState) {
        onDispose {
            clusterer.clear()
        }
    }
}