package com.wsyapp.ui.home.garage.details.fullimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beautybirds.base.BaseFragment
import com.wsyapp.R

class ImageViewerFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
    }

    fun initToolBar() {
        getMainActivity().hideToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().lockDrawer()
        getMainActivity().showBackOnToolBar()
        getMainActivity().hideRightAction()


    }

}