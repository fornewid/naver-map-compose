package com.naver.maps.map.compose

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView

@SuppressLint("ViewConstructor")
internal class InvalidatingComposeView(
    context: Context,
    private val content: @Composable () -> Unit,
) : AbstractComposeView(context) {

    var onInvalidate: (() -> Unit)? = null

    @Composable
    override fun Content() = content()

    override fun onDescendantInvalidated(child: View, target: View) {
        super.onDescendantInvalidated(child, target)
        onInvalidate?.invoke()
    }
}