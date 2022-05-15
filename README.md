# NAVER Map Android SDK for Jetpack Compose 🗺

<a href="https://opensource.org/licenses/Apache-2.0"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href='https://developer.android.com'><img src='http://img.shields.io/badge/platform-android-green.svg'/></a>

This repository contains [Jetpack Compose](compose) components for the NAVER Map SDK for Android.

## Requirements
* Kotlin-enabled project
* Jetpack Compose-enabled project (see [releases](https://github.com/fornewid/naver-map-compose/releases) for the required version of Jetpack Compose)
* An [API key][api-key]
* API level 21+

## Installation

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.fornewid:naver-map-compose:<version>'
    
    // Make sure to also include the latest version of the NAVER Map SDK for Android 
    implementation 'com.naver.maps:map-sdk:3.15.0'

    // Make sure to also include the latest version of the NAVER Map SDK for Android
    implementation 'com.google.android.gms:play-services-location:16.0.0'
    
    // Also include Compose version `1.2.0-alpha03` or higher - for example:
    implementation "androidx.compose.foundation:foundation:1.2.0-alpha03"
}
```

## Usage

Adding a map to your app looks like the following:

```kotlin
val seoulCityHall = LatLng(37.5666102, 126.9783881)
val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition(seoulCityHall, 14.0)
}
NaverMap(
    modifier = Modifier.fillMaxSize(),
    cameraPositionState = cameraPositionState
)
```

## Contributing

Contributions are welcome and encouraged!

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

[compose]: https://developer.android.com/jetpack/compose
[api-key]: https://navermaps.github.io/android-map-sdk/guide-ko/1.html
