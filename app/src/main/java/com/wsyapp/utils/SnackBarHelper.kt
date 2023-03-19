package com.wsyapp.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.wsyapp.R

class SnackBarHelper {

    private  var snackbar: Snackbar?=null
    private var listener: AppSnackBarListener? = null

    fun addAppSnackBarListener(listener: AppSnackBarListener?) {
        this.listener = listener
    }

    fun removeAppSnackBarListener() {
        this.listener = null
    }

    fun getAppSnackBarListener(): AppSnackBarListener? {
        return listener
    }

    fun getSnackbar(): Snackbar? {
        return snackbar
    }

    fun showSnackBarWithListenner(
        view: View,
        action: String?,
        msg: String, context: Context
    ): Snackbar? {
        snackbar = Snackbar
            .make(view, msg, Snackbar.LENGTH_LONG)
        var isEmpty = action?.isEmpty() ?: false
        if (action != null || isEmpty)
            snackbar?.setAction(action) {
            }

        snackbar?.setActionTextColor(Color.WHITE)
        snackbar?.setBackgroundTint(context.resources.getColor(R.color.green))
        val sbView = snackbar?.view
        val textView =
            sbView?.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)

        if (listener != null) {
            snackbar?.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (listener != null) {
                        listener?.onDissmissListener(transientBottomBar) ?: return
                    }
                    //  super.onDismissed(transientBottomBar, event)
                }

                override fun onShown(sb: Snackbar?) {
                    super.onShown(sb)
                }
            })
        }

        snackbar?.show()
        return snackbar
    }

    interface AppSnackBarListener {
        fun onDissmissListener(snackbar: Snackbar?)
    }
}