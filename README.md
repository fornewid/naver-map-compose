# NAVER Map Compose πΊ

<a href="https://github.com/fornewid/naver-map-compose/actions/workflows/build.yaml"><img src="https://github.com/fornewid/naver-map-compose/actions/workflows/build.yaml/badge.svg"/></a>
<a href="https://opensource.org/licenses/Apache-2.0"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href='https://developer.android.com'><img src='http://img.shields.io/badge/platform-android-green.svg'/></a>
<a href='https://developer.android.com/studio/releases/platforms#5.0'><img src="https://img.shields.io/badge/API%20Level-21+-green.svg"/></a>

μ΄ λΌμ΄λΈλ¬λ¦¬λ [Jetpack Compose][compose]μμ μ¬μ©ν  μ μλ [λ€μ΄λ² μ§λ][naver-map] APIλ₯Ό μ κ³΅ν©λλ€.

## Sample App

μ΄ μ μ₯μμλ μν μ±μ΄ ν¬ν¨λμ΄ μμ΅λλ€.
μ€ννλ €λ©΄ λ€μμ μνν΄μΌ ν©λλ€.

1. [NAVER Map Android SDK Demo](https://github.com/navermaps/android-map-sdk) λ ν¬μ§ν°λ¦¬μ [How To Run](https://github.com/navermaps/android-map-sdk#how-to-run)μ μ°Έκ³ νμ¬, ν΄λΌμ΄μΈνΈ IDλ₯Ό λ°κΈλ°μ΅λλ€.
2. [`client_id.xml`](app/src/main/res/values/client_id.xml)μ `naver_map_sdk_client_id`μ λ°κΈλ°μ ν΄λΌμ΄μΈνΈ IDλ₯Ό μλ ₯ν©λλ€.
3. λΉλνκ³  μ€νν©λλ€.

## Download

<table>
 <tr>
  <td>Compose 1.1 (1.1.x)</td><td><img alt="Maven Central" src="https://img.shields.io/maven-central/v/io.github.fornewid/naver-map-compose?versionPrefix=1.0"/></td>
 </tr>
 <tr>
  <td>Compose 1.2 (1.2.x)</td><td><img alt="Maven Central" src="https://img.shields.io/maven-central/v/io.github.fornewid/naver-map-compose?versionPrefix=1.1"></td>
 </tr>
 <tr>
  <td>Compose 1.3 (1.3.x)</td><td><img alt="Maven Central" src="https://img.shields.io/maven-central/v/io.github.fornewid/naver-map-compose"></td>
 </tr>
</table>

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.fornewid:naver-map-compose:<version>'
    
    // NAVER Map Android SDK μ΅μ  λ²μ λ ν¬ν¨ν΄μΌ ν©λλ€.
    implementation 'com.naver.maps:map-sdk:3.15.0'

    // FusedLocationSourceλ₯Ό μ¬μ©νλ €λ©΄ play-services-location μμ‘΄μ±λ μΆκ°ν΄μΌ ν©λλ€.
    implementation 'com.google.android.gms:play-services-location:16.0.0'
}
```

## Usage

### μ§λ μΆκ°νκΈ°

```kotlin
NaverMap(
    modifier = Modifier.fillMaxSize()
)
```

### μ§λ κ΅¬μ±νκΈ°

μ§λλ `MapProperties`μ `MapUiSettings` κ°μ²΄λ₯Ό `NaverMap` composableμ μ λ¬νμ¬ κ΅¬μ±ν  μ μμ΅λλ€.

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

### μ§λμ μΉ΄λ©λΌ μ μ΄νκΈ°

`CameraPositionState`λ₯Ό ν΅ν΄ μΉ΄λ©λΌ λ³κ²½μ κ΄μ°°νκ³  μ μ΄ν  μ μμ΅λλ€.

```kotlin
val seoul = LatLng(37.532600, 127.024612)
val cameraPositionState: CameraPositionState = rememberCameraPositionState {
    // μΉ΄λ©λΌ μ΄κΈ° μμΉλ₯Ό μ€μ ν©λλ€.
    position = CameraPosition(seoul, 11.0)
}
Box(Modifier.fillMaxSize()) {
    NaverMap(cameraPositionState = cameraPositionState)
    Button(onClick = {
        // μΉ΄λ©λΌλ₯Ό μλ‘μ΄ μ€ λ λ²¨λ‘ μ΄λν©λλ€.
        cameraPositionState.move(CameraUpdate.zoomIn())
    }) {
        Text(text = "Zoom In")
    }
}
```

### μ§λμ κ·Έλ¦¬κΈ°

μ§λμ `Marker`μ²λΌ Overlayλ₯Ό μΆκ°νλ €λ©΄, `NaverMap`μ contentμ child composableμ μΆκ°νλ©΄ λ©λλ€.

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

μ κ³΅λλ Overlayλ λ€μκ³Ό κ°μ΅λλ€.

- `ArrowheadPathOverlay`
- `CircleOverlay`
- `GroundOverlay`
- `Marker`
- `MultipartPathOverlay`
- `PathOverlay`
- `PolygonOverlay`
- `PolylineOverlay`

#### raw NaverMap κ°μ²΄ μ»κΈ° (Experimental)

νΉμ  UseCaseμμλ `NaverMap` κ°μ²΄κ° νμν©λλ€.
μλ₯Ό λ€μ΄, μ΄ λΌμ΄λΈλ¬λ¦¬μμ λ§μ»€ ν΄λ¬μ€ν°λ§μ μμ§ μ§μλμ§ μμ§λ§([Issue #14](https://github.com/fornewid/naver-map-compose/issues/14) μ°Έμ‘°),
[μΈλΆ λΌμ΄λΈλ¬λ¦¬](https://github.com/ParkSangGwon/TedNaverMapClustering) λ₯Ό μ΄μ©νμ¬ ν΄λ¬μ€ν°λ§μ κ΅¬νν  μ μμ΅λλ€.
μ΄λ κ² κ΅¬ννλ €λ©΄ `MapEffect` composableμ μ¬μ©νμ¬ λ€μ΄λ²μ§λ SDKμ raw `NaverMap` κ°μ²΄μ μ κ·Όν΄μΌ ν©λλ€.

```kt
NaverMap(
    // ...
) {
    val context = LocalContext.current
    var clusterManager by remember { mutableStateOf<TedNaverClustering<MyItem>?>(null) }
    MapEffect(items) { map ->
        if (clusterManager == null) {
            clusterManager = TedNaverClustering.with<MyItem>(context, map).make()
        }
        clusterManager?.addItems(items)
    }
}
```

## Snapshots

νμ¬ κ°λ° μ€μΈ λ²μ μ νμΈνκ³  μΆλ€λ©΄ [SNAPSHOT λ²μ ](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/fornewid/naver-map-compose/)μ μ¬μ©ν  μ μμ΅λλ€.

Snapshotμ `main` branchμ μ»€λ°μ΄ μΆκ°λ  λλ§λ€ μλ°μ΄νΈλ©λλ€.

```groovy
repositories {
    maven { url 'https://s01.oss.sonatype.org/content/repositories/snapshots' }
}

dependencies {
    // μ λ§ν¬μμ μ΅μ  SNAPSHOT λ²μ μ νμΈνμΈμ.
    classpath 'io.github.fornewid:naver-map-compose:XXX-SNAPSHOT'
}
```

## Contributions

- μ€λ₯λ₯Ό λ°κ²¬νκ±°λ κΆκΈν μ μ΄ μλ€λ©΄ [μ΄μλ₯Ό λ±λ‘ν΄μ£ΌμΈμ.](https://github.com/fornewid/naver-map-compose/issues/new)
- μλ‘μ΄ κΈ°λ₯μ μΆκ°νκ³  μΆλ€λ©΄ [GitHub Issues](https://github.com/fornewid/naver-map-compose/issues)λ₯Ό ν΅ν΄ μ§μ κ°λ₯ μ¬λΆλ₯Ό νμΈν΄μΌ ν©λλ€.

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
