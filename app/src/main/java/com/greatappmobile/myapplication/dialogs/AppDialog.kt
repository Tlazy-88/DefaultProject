package com.greatappmobile.myapplication.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.View
import android.view.Window
import androidx.core.graphics.drawable.toDrawable

class AppDialog internal constructor(
    private val context: Context,
    private val layoutId: Int,
    private val onBindView: ((View, Dialog) -> Unit),
    private val onDismissCallback: (() -> Unit)? = null,
    private val cancelable: Boolean = true,
    private val widthPercent: Float = 0.85f,
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (layoutId == 0) throw IllegalStateException("layoutId must be non-zero")

        val view = LayoutInflater.from(context).inflate(layoutId, null)


        val dialog = this
        dialog.apply {

            requestWindowFeature(Window.FEATURE_NO_TITLE)

        }

        setContentView(view)

        setCancelable(cancelable)


        window?.apply {

            setLayout(
                (context.resources.displayMetrics.widthPixels * widthPercent).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            attributes = attributes.apply {
                dimAmount = 0f
            }


            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }

        animation(true){

        }


        onBindView.invoke(view, this)
    }

    override fun dismiss() {

        // Manual animation close
        animation(false){
            super.dismiss()
            onDismissCallback?.invoke()
        }

    }

    private fun animation(isOpen: Boolean, finish: () -> Unit){
        window?.decorView?.findViewById<View>(android.R.id.content)?.let{view->
            val start= if(isOpen) 0f else 1f
            val end= if(isOpen) 1f else 0f

            view.alpha = start
            view.scaleX = start
            view.scaleY = start
            view.animate()
                .alpha(end)
                .scaleX(end)
                .scaleY(end)
                .setDuration(500)
                .withEndAction {
                    finish()
                }
                .start()
        } ?: run {
            finish()
        }
    }
}