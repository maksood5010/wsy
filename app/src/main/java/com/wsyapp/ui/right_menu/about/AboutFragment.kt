package com.wsyapp.ui.right_menu.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beautybirds.base.BaseFragment
import com.wsyapp.R

class AboutFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity().showToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.About_us))
        getMainActivity().unlockDrawer()
        getMainActivity().hideRightAction()

        getMainActivity().updateToolBar(getString(R.string.About_us), View.GONE)



    }

}