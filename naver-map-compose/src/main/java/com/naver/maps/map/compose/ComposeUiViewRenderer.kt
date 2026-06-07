package com.naver.maps.map.compose

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.platform.AbstractComposeView
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import com.naver.maps.map.MapView
import java.io.Closeable

@Composable
internal fun rememberComposeUiViewRenderer(): ComposeUiViewRenderer {
    val mapView = (currentComposer.applier as MapApplier).mapView
    val compositionContext = rememberCompositionContext()

    return remember(compositionContext) {
        object : ComposeUiViewRenderer {

            override fun startRenderingView(
                view: AbstractComposeView,
            ): ComposeUiViewRenderer.RenderHandle {
                return mapView.startRenderingComposeView(
                    view = view,
                    parentContext = compositionContext,
                )
            }

        }
    }
}

private fun MapView.startRenderingComposeView(
    view: AbstractComposeView,
    parentContext: CompositionContext,
): ComposeUiViewRenderer.RenderHandle {
    val containerView = ensureContainerView()
    containerView.addView(view)
    view.apply {
        setParentCompositionContext(parentContext)
    }
    return object : ComposeUiViewRenderer.RenderHandle {
        override fun dispose() {
            containerView.removeView(view)
        }

    }
}

private fun MapView.ensureContainerView(): NoDrawContainerView {
    return findViewById(R.id.naver_maps_compose_nodraw_container_view)
        ?: NoDrawContainerView(context)
            .apply { id = R.id.naver_maps_compose_nodraw_container_view }
            .also(::addView)
}

/**
 * Compose UI 를 View 에 렌더링 하기 위한 인터페이스
 */
internal interface ComposeUiViewRenderer {

    fun startRenderingView(
        view: AbstractComposeView,
    ): RenderHandle

    interface RenderHandle : Closeable {
        fun dispose()

        override fun close(): Unit = dispose()
    }
}

/**
 * child view 가 layout 및 draw 되지 않도록 하는 ViewGroup.
 * 뷰 계층구조에 영향을 주지 않으면서 MapView 의 하위항목으로 ComposeView 를 추가하기 위함.
 */
private class NoDrawContainerView(context: Context) : ViewGroup(context) {

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    }

    override fun dispatchDraw(canvas: Canvas) {
    }
}

internal fun renderViewToBitmap(fakeCanvas: Canvas, view: AbstractComposeView): Bitmap {
    /* AndroidComposeView triggers LayoutNode's layout phase in the View draw phase,
       so trigger a draw to an empty canvas to force that */
    view.draw(fakeCanvas)
    val viewParent =
        view.parent as? ViewGroup ?: return createBitmap(20, 20)
    view.measure(
        View.MeasureSpec.makeMeasureSpec(viewParent.width, View.MeasureSpec.AT_MOST),
        View.MeasureSpec.makeMeasureSpec(viewParent.height, View.MeasureSpec.AT_MOST),
    )
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    val bitmap = createBitmap(
        view.measuredWidth.takeIf { it > 0 } ?: 1,
        view.measuredHeight.takeIf { it > 0 } ?: 1
    )
    bitmap.applyCanvas {
        view.draw(this)
    }

    return bitmap
}
