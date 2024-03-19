package land.sungbin.navermap.token.intercept

import com.naver.maps.map.overlay.Overlay
import land.sungbin.navermap.token.component.Component

public interface ComponentInterceptor<O : Overlay> {
  public fun request(component: Component<O>): O? = null
  public fun built(overlay: O): O = overlay

  public companion object {
    public inline operator fun <O : Overlay> invoke(
      crossinline request: (Component<O>) -> O? = { null },
      crossinline built: (O) -> O = { it },
    ): ComponentInterceptor<O> =
      object : ComponentInterceptor<O> {
        override fun request(component: Component<O>) = request(component)
        override fun built(overlay: O) = built(overlay)
      }
  }
}
