package com.wsyapp.ui.home.carservice.accuracy

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beautybirds.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wsyapp.R
import kotlinx.android.synthetic.main.fragment_accuracy.*

class AccuracyFragment() : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accuracy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity().hideToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().hideTitleOnToolBar()
        getMainActivity().lockDrawer()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.home), View.GONE)

        setData()

        tv_agree.setOnClickListener(this)
    }

    private fun setData() {


        Glide.with(requireContext()).load(resources.getDrawable(R.drawable.accuracy))
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            }).into(iv_accuracy)


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_agree -> onAgree()
        }
    }

    private fun onAgree() {
        getMainActivity().onBackPressed()
    }


}