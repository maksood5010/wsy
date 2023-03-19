package com.wsyapp.ui.leftmenu.myorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.ui.home.garage.adapter.MyOrderAdapter
import kotlinx.android.synthetic.main.fragment_my_order.*

class MyOrderFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!getMainActivity().isUserLogin()) {
            findNavController().popBackStack()
            getMainActivity().openLoginFragment()
            return
        }

        hideNoDataView()
        initRv()
        initToolBar()

    }

    fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.my_orders))
        getMainActivity().unlockDrawer()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().hideRightAction()

        getMainActivity().updateToolBar(getString(R.string.my_orders), View.GONE)

    }
    private fun initRv() {
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_view.adapter =
            MyOrderAdapter(requireContext(), object : MyOrderAdapter.AppClickListener {
                override fun onClickListener(view: View, position: Int) {

                    when (view!!.id) {
                        R.id.cl_view -> {
                            openOrderDetailFragment()
                        }
                    }
                }
            })
    }

    private fun openOrderDetailFragment() {
        findNavController().navigate(R.id.action_myOrderFragment_to_orderDetailFragment)
    }

    fun hideNoDataView() {
        tv_no_data.visibility = View.GONE

    }

}