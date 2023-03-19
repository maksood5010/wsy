package com.wsyapp.ui.dialog.selectcar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsyapp.R
import com.wsyapp.ui.dialog.selectcar.adapter.SelectCarAdapter
import kotlinx.android.synthetic.main.dialog_select_car.*

class SelectCarDialog : DialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_select_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()

        iv_back.setOnClickListener(this)
        iv_done.setOnClickListener(this)
    }

    private fun initRv() {
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_view.adapter =
            SelectCarAdapter(requireContext(), object : SelectCarAdapter.AppClickListener {
                override fun onClickListener(view: View, position: Int) {
                    //dismiss()
                }
            })
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_back, R.id.iv_done -> {
                dismiss()
            }
        }
    }
}