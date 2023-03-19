package com.wsyapp.ui.leftmenu.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import kotlinx.android.synthetic.main.fragment_wallet.*

class WalletFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        tv_recharge.setOnClickListener(this)
    }

    fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.wallet))
        getMainActivity().lockDrawer()
        getMainActivity().showBackOnToolBar()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.wallet), View.VISIBLE)


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_recharge -> getMainActivity().onBackPressed()
        }
    }


}