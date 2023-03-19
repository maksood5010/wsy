package com.wsyapp.ui.right_menu.complains

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import kotlinx.android.synthetic.main.fragment_complain.*

class ComplainFragment : BaseFragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!getMainActivity().isUserLogin()) {
            findNavController().popBackStack()
            getMainActivity().openLoginFragment()
            return
        }
        tv_submit.setOnClickListener(this)
        initToolBar()


    }

    private fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.complains))
        getMainActivity().unlockDrawer()
        getMainActivity().hideRightAction()

        getMainActivity().updateToolBar(getString(R.string.complains), View.GONE)


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_submit -> {
                getMainActivity().onBackPressed()

            }
        }
    }

}