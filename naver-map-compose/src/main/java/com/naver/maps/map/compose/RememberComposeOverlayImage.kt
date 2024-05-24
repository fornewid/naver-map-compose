/*
 * Copyright 2024 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.naver.maps.map.compose

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.applyCanvas
import com.naver.maps.map.overlay.OverlayImage

@Composable
internal fun rememberComposeOverlayImage(
    vararg keys: Any,
    content: @Composable () -> Unit,
): OverlayImage {
    val parent = LocalView.current as ViewGroup
    val compositionContext = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)

    return remember(parent, compositionContext, currentContent, *keys) {
        renderComposableToOverlayImage(parent, compositionContext, currentContent)
    }
}

private val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

private fun renderComposableToOverlayImage(
    parent: ViewGroup,
    compositionContext: CompositionContext,
    content: @Composable () -> Unit,
): OverlayImage {
    val fakeCanvas = Canvas()
    val composeView = ComposeView(parent.context)
        .apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            setParentCompositionContext(compositionContext)
            setContent(content)
        }
        .also(parent::addView)

    composeView.draw(fakeCanvas)

    composeView.measure(measureSpec, measureSpec)

    if (composeView.measuredWidth == 0 || composeView.measuredHeight == 0) {
        throw IllegalStateException(
            "The ComposeView was measured to have a width or height of " +
                "zero. Make sure that the content has a non-zero size."
        )
    }

    composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)

    val bitmap = Bitmap.createBitmap(
        composeView.measuredWidth,
        composeView.measuredHeight,
        Bitmap.Config.ARGB_8888,
    )

    bitmap.applyCanvas { composeView.draw(this) }

    parent.removeView(composeView)

    return OverlayImage.fromBitmap(bitmap)
}
