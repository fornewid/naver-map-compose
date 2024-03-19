package land.sungbin.navermap.token

import com.naver.maps.map.overlay.OverlayImage

public interface HasOverlayImage {
  public val overlayImage: OverlayImage

  public companion object {
    public operator fun invoke(image: OverlayImage): HasOverlayImage =
      object : HasOverlayImage {
        override val overlayImage = image
      }
  }
}