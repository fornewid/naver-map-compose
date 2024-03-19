package land.sungbin.navermap.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import com.naver.maps.map.overlay.Overlay
import land.sungbin.navermap.compose.internal.MapApplier
import land.sungbin.navermap.token.component.Component
import land.sungbin.navermap.token.component.Component as ComponentToken

public interface MapComponentScope {
  public fun <O : Overlay> component(token: ComponentToken<O>)
  public fun <O : Overlay> component(token: ComponentToken<O>, block: O.() -> Unit)
}

@Suppress("ComposableNaming")
@Composable
internal fun MapComponentScope(): MapComponentScope {
  val map = (currentComposer.applier as MapApplier).map

  fun <O : Overlay> buildComponent(token: Component<O>): O {
    token::class.java.genericSuperclass
  }

  return object : MapComponentScope {
    override fun <O : Overlay> component(token: ComponentToken<O>) {
    }

    override fun <O : Overlay> component(token: ComponentToken<O>, block: O.() -> Unit) {
      buildComponent(token).apply(block)
    }
  }
}
