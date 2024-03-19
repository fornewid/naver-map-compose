package land.sungbin.navermap.token.component

import com.naver.maps.map.overlay.Marker
import land.sungbin.navermap.token.HasCoord
import land.sungbin.navermap.token.HasOverlayImage

public interface MarkerComponent : HasOverlayImage, HasCoord, Component<Marker>