# NAVER Map Compose 🗺

<a href="https://github.com/fornewid/naver-map-compose/actions/workflows/build.yaml"><img src="https://github.com/fornewid/naver-map-compose/actions/workflows/build.yaml/badge.svg"/></a>
<a href="https://opensource.org/licenses/Apache-2.0"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href='https://developer.android.com'><img src='http://img.shields.io/badge/platform-android-green.svg'/></a>
<a href='https://developer.android.com/studio/releases/platforms#5.0'><img src="https://img.shields.io/badge/API%20Level-21+-green.svg"/></a>

이 라이브러리는 [Jetpack Compose][compose]에서 사용할 수 있는 [네이버 지도][naver-map] API를 제공합니다.

## Sample App

이 저장소에는 샘플 앱이 포함되어 있습니다.
실행하려면 다음을 수행해야 합니다.

1. [NAVER Map Android SDK Demo](https://github.com/navermaps/android-map-sdk) 레포지터리의 [How To Run](https://github.com/navermaps/android-map-sdk#how-to-run)을 참고하여, 클라이언트 ID를 발급받습니다.
2. [`client_id.xml`](app/src/main/res/values/client_id.xml)의 `naver_map_sdk_client_id`에 발급받은 클라이언트 ID를 입력합니다.
3. 빌드하고 실행합니다.

## Download

<table>
 <tr>
  <td>Compose 1.1 (1.1.x)</td><td><img alt="Maven Central" src="https://img.shields.io/maven-central/v/io.github.fornewid/naver-map-compose?versionPrefix=1.0"/></td>
 </tr>
 <tr>
  <td>Compose 1.2 (1.2.x)</td><td><img alt="Maven Central" src="https://img.shields.io/maven-central/v/io.github.fornewid/naver-map-compose"></td>
 </tr>
</table>

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.fornewid:naver-map-compose:<version>'
    
    // NAVER Map Android SDK 최신 버전도 포함해야 합니다.
    implementation 'com.naver.maps:map-sdk:3.15.0'

    // FusedLocationSource를 사용하려면 play-services-location 의존성도 추가해야 합니다.
    implementation 'com.google.android.gms:play-services-location:16.0.0'
}
```

## Usage

### 지도 추가하기

```kotlin
NaverMap(
    modifier = Modifier.fillMaxSize()
)
```

### 지도 구성하기

지도는 `MapProperties`와 `MapUiSettings` 객체를 `NaverMap` composable에 전달하여 구성할 수 있습니다.

```kotlin
var mapProperties by remember {
    mutableStateOf(
        MapProperties(maxZoom = 10.0, minZoom = 5.0)
    )
}
var mapUiSettings by remember {
    mutableStateOf(
        MapUiSettings(isLocationButtonEnabled = false)
    )
}
Box(Modifier.fillMaxSize()) {
    NaverMap(properties = mapProperties, uiSettings = mapUiSettings)
    Column {
        Button(onClick = {
            mapProperties = mapProperties.copy(
                isBuildingLayerGroupEnabled = !mapProperties.isBuildingLayerGroupEnabled
            )
        }) {
            Text(text = "Toggle isBuildingLayerGroupEnabled")
        }
        Button(onClick = {
            mapUiSettings = mapUiSettings.copy(
                isLocationButtonEnabled = !mapUiSettings.isLocationButtonEnabled
            )
        }) {
            Text(text = "Toggle isLocationButtonEnabled")
        }
    }
}
```

### 지도의 카메라 제어하기

`CameraPositionState`를 통해 카메라 변경을 관찰하고 제어할 수 있습니다.

```kotlin
val seoul = LatLng(37.532600, 127.024612)
val cameraPositionState: CameraPositionState = rememberCameraPositionState {
    // 카메라 초기 위치를 설정합니다.
    position = CameraPosition(seoul, 11.0)
}
Box(Modifier.fillMaxSize()) {
    NaverMap(cameraPositionState = cameraPositionState)
    Button(onClick = {
        // 카메라를 새로운 줌 레벨로 이동합니다.
        cameraPositionState.move(CameraUpdate.zoomIn())
    }) {
        Text(text = "Zoom In")
    }
}
```

### 지도에 그리기

지도에 `Marker`처럼 Overlay를 추가하려면, `NaverMap`의 content에 child composable을 추가하면 됩니다.

```kotlin
NaverMap {
    Marker(
        state = MarkerState(position = LatLng(37.532600, 127.024612)),
        captionText = "Marker in Seoul"
    )
    Marker(
        state = MarkerState(position = LatLng(37.390791, 127.096306)),
        captionText = "Marker in Pangyo"
    )
}
```

제공되는 Overlay는 다음과 같습니다.

- `ArrowheadPathOverlay`
- `CircleOverlay`
- `GroundOverlay`
- `Marker`
- `MultipartPathOverlay`
- `PathOverlay`
- `PolygonOverlay`
- `PolylineOverlay`

## Snapshots

현재 개발 중인 버전을 확인하고 싶다면 [SNAPSHOT 버전](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/fornewid/naver-map-compose/)을 사용할 수 있습니다.

Snapshot은 `main` branch에 커밋이 추가될 때마다 업데이트됩니다.

```groovy
repositories {
    maven { url 'https://s01.oss.sonatype.org/content/repositories/snapshots' }
}

dependencies {
    // 위 링크에서 최신 SNAPSHOT 버전을 확인하세요.
    classpath 'io.github.fornewid:naver-map-compose:XXX-SNAPSHOT'
}
```

## Contributions

- 오류를 발견하거나 궁금한 점이 있다면 [이슈를 등록해주세요.](https://github.com/fornewid/naver-map-compose/issues/new)
- 새로운 기능을 추가하고 싶다면 [GitHub Issues](https://github.com/fornewid/naver-map-compose/issues)를 통해 지원 가능 여부를 확인해야 합니다.

## License

```
Copyright 2022 SOUP

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements. See the NOTICE file distributed with this work for
additional information regarding copyright ownership. The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```

[naver-map]: https://navermaps.github.io/android-map-sdk/guide-ko/
[compose]: https://developer.android.com/jetpack/compose
[api-key]: https://navermaps.github.io/android-map-sdk/guide-ko/1.html
