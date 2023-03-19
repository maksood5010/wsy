package com.wsyapp.ui.dialog.appupdate

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.wsyapp.R
import com.wsyapp.data.model.response.HomeSliderResponseModel

class AppUpdateDialog(context: Context) : Dialog(context), View.OnClickListener {

    private var model: HomeSliderResponseModel? = null
    private lateinit var tv_message: TextView
    private lateinit var tv_update: TextView
    private lateinit var tv_cancel: TextView
    private lateinit var confirmClickListener: OnClickListener

    fun setOnClickListener(listener: OnClickListener) {
        confirmClickListener = listener
    }

    fun setModel(data: HomeSliderResponseModel) {
        model = data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.dialog_app_update)

        tv_message = findViewById(R.id.tv_message)
        tv_cancel = findViewById(R.id.tv_cancel)
        tv_update = findViewById(R.id.tv_update)

        updateUi()

        tv_update.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)

    }

    private fun updateUi() {
        if (model == null) return
        val forceUpdate = model?.fupdateandroid ?: false
        if (forceUpdate) {
            tv_cancel.visibility = View.GONE
        } else {
            tv_cancel.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_cancel -> {
                dismiss()
            }
            R.id.tv_update -> {
                confirmClickListener?.onClick() ?: dismiss()
            }
        }
    }

    interface OnClickListener {
        fun onClick()
    }

}