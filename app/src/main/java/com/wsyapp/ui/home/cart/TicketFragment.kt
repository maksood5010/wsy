package com.wsyapp.ui.home.cart

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.data.database.entity.Cart
import com.wsyapp.data.model.request.OrderRequestModel
import com.wsyapp.data.model.request.PlaceOrderRequestModel
import com.wsyapp.data.model.response.AddressForPlaceOrderModel
import com.wsyapp.data.model.response.PlaceOrderResponseModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.ui.home.cart.adapter.PaymentListAdapter
import com.wsyapp.ui.home.cart.adapter.ProductListAdapter
import com.wsyapp.utils.MyConstants
import com.wsyapp.utils.UserPref
import kotlinx.android.synthetic.main.fragment_ticket.*
import kotlinx.android.synthetic.main.fragment_ticket.rv_view
import kotlinx.android.synthetic.main.total_layout.*
import java.text.SimpleDateFormat
import java.util.*

class TicketFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var viewModel: TicketViewModel
    private var city: String? = null
    private var street: String? = null
    private var area: String? = null
    private var phone: String? = null
    private var building: String? = null
    private var apartment: String? = null
    private val cartList: MutableList<Cart>? = mutableListOf()
    private var orderList: MutableList<OrderRequestModel>? = mutableListOf()
    private var address: MutableList<AddressForPlaceOrderModel>? = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCartProduct()
        getAddress()
        initRv()
        viewModel = ViewModelProviders.of(this).get(TicketViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                TicketViewModel.Action.SUCCESS ->updateUi(it.payload)
                TicketViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                TicketViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
        if (city != null) {
            tv_address.setText(city + ", " + street)
            tv_phone.setText(phone)
        } else Log.d("TAG", "onViewCreated: args $city")

        initTotal()


        tv_submit.setOnClickListener {
            onSubmit()

        }


    }

    private fun updateUi(payload: PlaceOrderResponseModel?) {
        Log.d("TAG", "updateUi: ${payload!!.orders}")
        showSnackBar(cl_view, "", requireContext().getString(R.string.orderplaced))
    }

    private fun onSubmit() {
//        val list=MutableList<OrderRequestModel>()
        for (i in cartList!!.indices) {
            val order = OrderRequestModel(
                cartList!![i].shop_id,
                cartList!![i].id_product,
                cartList!![i].price,
                cartList!![i].quantity.toString()
            )
            orderList!!.add(order)
        }
        var total = 0.0
        for (item in cartList!!) {
            total += getTotal(item)
        }
        val number2digits: Double = String.format("%.2f", total).toDouble()
        val grandTotal = number2digits.toString()

        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy MM dd HH:mm:ss")

        val userModel = getMainActivity().getUserModel() ?: return
        Log.d("TAG", "fragment_ticket: userModel.user_id ${userModel.user_id}")

        if (ConnectionDetector.isNetAvailable(requireContext())) {
            val adapterPayment = PaymentListAdapter(requireContext())
            Log.d(
                "TAG", "onSubmit: argumts: st=${RepoConstant.API_PLACE_ORDER} " +
                        "user_id 0 , street : ${address!![0].st} " +
                        "dateInString $dateInString" +
                        "payment ${adapterPayment.getPaymentMethod()}" +
                        "grandTotal :$grandTotal" +
                        "orderList ${orderList!![0].shop_id}" +
                        "user id ${userModel.user_id}"
            )
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.orderTicket(
                PlaceOrderRequestModel(
                    RepoConstant.API_PLACE_ORDER,
                    userModel.user_id,
                    phone!!,
                    address,
                    dateInString,
                    adapterPayment.getPaymentMethod(),
                    "deliver",
                    grandTotal,
                    "1",
                    orderList!!
                )
            )
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    private fun initTotal() {
        if (cartList!!.size != 0) {
            var total = 0.0
            for (item in cartList!!) {
                total += getTotal(item)
            }
            val number2digits: Double = String.format("%.2f", total).toDouble()
            tv_sub_total.text = "" + number2digits + " " + requireContext().getString(R.string.aed)
        } else {
            tv_sub_total.text = "0.0"
        }
        ll_delivery.visibility = View.GONE
        tv_total.setText(grandTotal())

    }

    private fun grandTotal(): String {
        var total = 0.0
        for (item in cartList!!) {
            total += getTotal(item)
        }
        val number2digits: Double = String.format("%.2f", total).toDouble()
        val deliveriCharge = 0
        val grandTotal = number2digits + deliveriCharge
        return grandTotal.toString() + " " + requireContext().getString(R.string.aed)
    }

    private fun getTotal(model: Cart): Double {
        val itemQty = model.quantity
        val itemPrice = model.price.toDouble()
        val total = itemPrice * itemQty
        val number2digits: Double = String.format("%.2f", total).toDouble()
        return (number2digits)
    }

    private fun getAddress() {
        val arguments = arguments ?: return
        city = arguments.getString(MyConstants.CITY) ?: return
        area = arguments.getString(MyConstants.AREA) ?: return
        phone = arguments.getString(MyConstants.PHONE) ?: return
        street = arguments.getString(MyConstants.STREET) ?: return
        building = arguments.getString(MyConstants.BUILDING) ?: return
        apartment = arguments.getString(MyConstants.APART) ?: return
        val addres = AddressForPlaceOrderModel(area!!, city!!, street!!, building!!, apartment!!)
        address!!.add(addres)
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }


    private fun initRv() {
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_view.adapter = ProductListAdapter(requireContext(), cartList)
        rv_payment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapterPayment = PaymentListAdapter(requireContext())
        rv_payment.adapter = adapterPayment
    }

    private fun getCartProduct() {
        val arguments = arguments ?: return
        val size = arguments.getInt("size") ?: return
        var i = 0
        while (i < size) {
            val cartLi = arguments.getParcelable<Cart>(i.toString()) ?: return
            i++
            cartList!!.add(cartLi)
        }
    }
    private fun showDialog(){
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setMessage(requireContext().getString(R.string.orderplaced))
            // if the dialog is cancelable
            .setCancelable(false)
            .setPositiveButton("Ok", DialogInterface.OnClickListener {
                    dialog, id ->
                dialog.dismiss()

            })

        val alert = dialogBuilder.create()
        alert.setTitle("Test")
        alert.show()
    }
}