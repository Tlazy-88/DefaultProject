package com.greatappmobile.myapplication.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.graphics.toColorInt


class AppDialogBuilder(private val context: Context) {
    private var layoutId: Int = 0
    private lateinit var bindView: ((View, Dialog) -> Unit)
    private var onDismissCallback: (() -> Unit)? = null
    private var cancelable: Boolean = true
    private var widthPercent: Float = 0.85f

    fun setLayout(layoutId: Int): AppDialogBuilder {
        this.layoutId = layoutId
        return this
    }

    fun onBindView(bindView: (View, Dialog) -> Unit): AppDialogBuilder {
        this.bindView = bindView
        return this
    }

    fun onDismiss(callback: () -> Unit): AppDialogBuilder {
        this.onDismissCallback = callback
        return this
    }

    fun setCancelable(value: Boolean): AppDialogBuilder {
        this.cancelable = value
        return this
    }

    fun setWidthPercent(percent: Float): AppDialogBuilder {
        this.widthPercent = percent
        return this
    }

    fun build(): AppDialog {
        if (layoutId == 0)throw IllegalStateException("LayoutId not set")

        return AppDialog(
            context,
            layoutId= layoutId,
            onBindView= bindView,
            onDismissCallback= onDismissCallback,
            cancelable= cancelable,
            widthPercent= widthPercent,
        )
    }

    fun show(): AppDialog {
        return build().apply { show() }
    }
}

fun createGradientDrawableBackground(
    colors: IntArray = intArrayOf(
        "#B6BFB4".toColorInt(),
        "#25C825".toColorInt()
    ),
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.BOTTOM_TOP,
    topLeftRadius: Float = 24f,
    topRightRadius: Float = 24f,
    bottomRightRadius: Float = 24f,
    bottomLeftRadius: Float = 24f
): GradientDrawable {
    return GradientDrawable(orientation, colors).apply {

        //màu
        shape = GradientDrawable.RECTANGLE

        //corner
        cornerRadii = floatArrayOf(
            topLeftRadius, topLeftRadius,
            topRightRadius, topRightRadius,
            bottomRightRadius, bottomRightRadius,
            bottomLeftRadius, bottomLeftRadius
        )

    }
}