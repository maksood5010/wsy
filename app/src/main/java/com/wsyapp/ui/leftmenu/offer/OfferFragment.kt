package com.wsyapp.ui.leftmenu.offer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beautybirds.base.BaseFragment
import com.wsyapp.R

class OfferFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_offer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity().showToolBar()
        getMainActivity().showBackOnToolBar()

        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.Offer))
        getMainActivity().lockDrawer()
    }

}