package com.wsyapp.ui.home.carservice.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import kotlinx.android.synthetic.main.fragment_confirm.*

class ConfirmFragment : BaseFragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_close.setOnClickListener(this)
        tv_confirm.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        getMainActivity().onBackPressed()
    }

}