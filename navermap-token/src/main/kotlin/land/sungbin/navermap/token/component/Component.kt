package land.sungbin.navermap.token.component

import com.naver.maps.map.overlay.Overlay

public interface Component<O : Overlay> {
  public val type: Class<Overlay>
  public val tag: Any? get() = null
  public val clickListener: Overlay.OnClickListener? get() = null
}
