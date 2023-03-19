package com.wsyapp.ui.leftmenu.myorder.orderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beautybirds.base.BaseFragment
import com.wsyapp.R

class OrderDetailFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
    }

    fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.Order_Detail))
        getMainActivity().lockDrawer()
        getMainActivity().showBackOnToolBar()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.order_details), View.VISIBLE)


    }
}