package com.wsyapp.ui.home.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.data.database.entity.Cart
import com.wsyapp.data.model.request.GarageDetailRequestModel
import com.wsyapp.data.model.response.AddressModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.utils.MyConstants
import kotlinx.android.synthetic.main.fragment_add_address.*

class AddAddress : BaseFragment() {
    private var country: String? = null
    private var state: String? = null
    private var street: String? = null
    private var area: String? = null
    private val cartList: MutableList<Cart>? = mutableListOf()
    private lateinit var viewModel: AddAddressViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddAddressViewModel::class.java)
        getAddressFromMap()
        getCartProduct()
        te_city.setText(state)
        te_street.setText(street)
        te_area.setText(area)


        tv_select.setOnClickListener {
            val city = te_city.text.toString()
            val area = te_area.text.toString()
            val phone = te_phone.text.toString()
            val street = te_street.text.toString()
            val building = te_building.text.toString()
            val apartment = te_apartment.text.toString()
            if (city == "") {
                showSnackBar(cl_parent, "", getString(R.string.select_city))
            } else if (area == "") {
                showSnackBar(cl_parent, "", getString(R.string.select_area))
            } else if (phone == "") {
                showSnackBar(cl_parent, "", getString(R.string.enter_number))
            } else if (street == "") {
                showSnackBar(cl_parent, "", getString(R.string.select_street))
            } else if (building == "") {
                showSnackBar(cl_parent, "", getString(R.string.enter_build))
            } else if (apartment == "") {
                showSnackBar(cl_parent, "", getString(R.string.enter_apart))
            } else {

                val bundle = Bundle()
                bundle.putInt("size",cartList!!.size)
                for (i in cartList!!.indices) {
                    bundle.putParcelable(i.toString(), cartList!![i])
                }
                bundle.putString(MyConstants.CITY,city)
                bundle.putString(MyConstants.PHONE,phone)
                bundle.putString(MyConstants.AREA,area)
                bundle.putString(MyConstants.STREET,street)
                bundle.putString(MyConstants.BUILDING,building)
                bundle.putString(MyConstants.APART,apartment)
                findNavController().navigate(R.id.action_addaddress_to_ticketFragment, bundle)
            }

        }
    }
    private fun getCartProduct() {
        val arguments = arguments ?: return
        val size=arguments.getInt("size")?:return
        var i=0
        while (i<size){
            val cartLi=arguments.getParcelable<Cart>(i.toString()) ?: return
            i++
            cartList!!.add(cartLi)
        }
    }


    private fun getAddressFromMap() {
        val arguments = arguments ?: return
        val country1 = arguments.getString(MyConstants.COUNTRY) ?: return
        val state1 = arguments.getString(MyConstants.STATE) ?: return
        val street1 = arguments.getString(MyConstants.STREET) ?: return
        val area1 = arguments.getString(MyConstants.AREA) ?: return
        country = country1
        street = street1
        state = state1
        area = area1
        Log.d("Address", "getAddressFromMap:  $street1")

    }
}