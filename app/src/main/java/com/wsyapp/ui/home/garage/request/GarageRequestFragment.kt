package com.wsyapp.ui.home.garage.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.ui.dialog.selectcar.SelectCarDialog
import com.wsyapp.ui.dialog.selectcar.adapter.SelectCarAdapter
import kotlinx.android.synthetic.main.fragment_garage_request.*

class GarageRequestFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_garage_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()

        initRv()
        tv_submit.setOnClickListener(this)
        iv_select_address.setOnClickListener(this)
        tv_select_car.setOnClickListener(this)

    }

    fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.Make_Request))
        getMainActivity().lockDrawer()
        getMainActivity().showBackOnToolBar()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.Make_Request), View.VISIBLE)


    }
    private fun initRv() {
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_view.adapter =
            SelectCarAdapter(requireContext(), object : SelectCarAdapter.AppClickListener {
                override fun onClickListener(view: View, position: Int) {
                    //dismiss()
                }
            })
    }
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_submit -> getMainActivity().onBackPressed()
            R.id.iv_select_address -> openSelectAddressFragment()
            R.id.tv_select_car -> openSelectCarDialog()
        }
    }

    private fun openSelectCarDialog() {
        val dialog = SelectCarDialog()
        dialog.show(childFragmentManager, "TAG")
    }

    private fun openSelectAddressFragment() {
        findNavController().navigate(R.id.action_garageRequestFragment_to_selectAddressFragment)
    }
}