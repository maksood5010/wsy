package com.wsyapp.ui.dialog.confirm_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.wsyapp.R

class ConfirmDialog(context: Context) : Dialog(context), View.OnClickListener {

    lateinit var txt: String
    lateinit var tv_message: TextView
    lateinit var tv_ok: TextView
    lateinit var tv_cancel: TextView
    lateinit var confirmClickListener: OnClickListener

    fun setOnClickListener(listener: OnClickListener){
        confirmClickListener=listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.custom_dailog)

        tv_message = findViewById(R.id.tv_message)
        tv_cancel = findViewById(R.id.tv_cancel)
        tv_ok = findViewById(R.id.tv_ok)
        tv_message.setText(txt)
        tv_ok.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_cancel -> {
                 dismiss()
            }
            R.id.tv_ok -> {
                confirmClickListener?.onClick() ?: dismiss()
            }
        }
    }

    fun setMessage(s: String) {
        txt = s
    }


    interface OnClickListener {
        fun onClick()
    }

}