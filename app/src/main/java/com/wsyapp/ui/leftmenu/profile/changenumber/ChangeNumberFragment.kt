package com.wsyapp.ui.leftmenu.profile.changenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import kotlinx.android.synthetic.main.fragment_change_number.*

class ChangeNumberFragment : BaseFragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        iv_save.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        tv_change_number.setOnClickListener(this)

    }

    fun initToolBar() {
        getMainActivity().hideToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().hideTitleOnToolBar()
        getMainActivity().lockDrawer()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().hideRightAction()

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_back -> getMainActivity().onBackPressed()
            R.id.iv_save -> getMainActivity().onBackPressed()
            R.id.tv_change_number -> getMainActivity().onBackPressed()
        }
    }
}