package com.wsyapp.ui.home.cart

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wsyapp.R
import com.wsyapp.data.database.entity.Cart
import com.wsyapp.data.model.response.AddressModel
import com.wsyapp.data.model.response.CartItemModel
import com.wsyapp.data.model.response.CartItemsResponseModel
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.helper.sorthelper.MyListComparator
import com.wsyapp.helper.sorthelper.SecondComparator
import com.wsyapp.ui.home.cart.adapter.AdressListAdapter
import com.wsyapp.ui.home.cart.adapter.CartAdapter
import com.wsyapp.utils.MyConstants
import kotlinx.android.synthetic.main.address_bottomsheet.view.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.cl_parent
import java.util.*

class CartFragment : BaseFragment() {

    private var add: MutableList<AddressModel>? = null
    private lateinit var viewModel: CartViewModel
    var cartList: MutableList<Cart>? = null
    var adapter: CartAdapter? = null
    private var products: MutableList<CartItemModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar()
        initRv()
        viewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)

        viewModel.initDatabase(requireContext())

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            cartList?.clear()
            cartList?.addAll(it)
            Collections.sort(cartList!!, MyListComparator())
            if (cartList!!.size == 0) {
                Handler().postDelayed(Runnable {
                    if (tv_no_data_fount != null) tv_no_data_fount.visibility = View.VISIBLE
                }, 500)

            } else {
                Log.d("TAG", "onClickListener: cartList!!.size ${cartList!!.size} ")
                tv_no_data_fount.visibility = View.GONE
            }
            getHomeSliderFromServer()
        })

        viewModel.getCartResponseLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                CartViewModel.Action.SUCCESS -> updateUi(it.payload)
                CartViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                CartViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
        viewModel.getAllProducts()
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.address_bottomsheet, null)
        bottomSheetDialog.setContentView(view)
        view.rv_address.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        view.rv_address.adapter = AdressListAdapter(requireContext(), bottomSheetDialog)
        view.tv_select.setOnClickListener {
        }
        tv_check_out.setOnClickListener {
            if (cartList != null) {
                val bundle = Bundle()
                bundle.putInt("size",cartList!!.size)
                for (i in cartList!!.indices)
                bundle.putParcelable(i.toString(), cartList!![i])


                if (add != null) {
                    bottomSheetDialog.show()
                } else {

                    findNavController().navigate(R.id.action_cartFragment_to_markAddress,bundle)
                }
            }else{
//                findNavController().navigate(R.id.action_cartFragment_to_markAddress)
            }
        }
        changeTextButton()

    }

    private fun updateUi(payload: CartItemsResponseModel?) {
        if (payload == null) {
            showToast(getString(R.string.not_found))
            return
        }

        products = payload.products ?: return
        if (payload.address != null) {
            add = payload.address
        }

        Collections.sort(products!!, SecondComparator())
        var temp = mutableListOf<Cart>()

        for (i in products!!.indices) {
            val cartItem = cartList?.get(i) ?: return//error ind
            for (j in products!!.indices) {
                val productItem = products!!.get(j)
                if (cartItem.id_product == productItem.id) {
                    if (cartItem!!.quantity > productItem.quantity.toInt()) {
                        cartItem.quantity = productItem.quantity.toInt()
                    }
                    temp.add(cartItem)
//                    if (temp[i].quantity > products!![j].quantity.toInt()){
//                        temp[i].quantity= products!![j].quantity.toInt()
//                    }
                }
            }
        }
        Log.d(
            "Cart",
            "updateUi: temp size :${temp!!.size}products: size ${products!!.size} "
        )


//            if (item?.id_product == cartItem.id) {
//                temp.add(item)
//                Log.d("Cart", "updateUi: temp.add ${item?.id_product}")
//            }
        cartList?.clear()
        for (item in temp) {
            cartList?.add(item)
        }

        Collections.sort(cartList!!, MyListComparator())
        changeTextButton()
        adapter?.notifyDataSetChanged()
    }

    private fun changeTextButton() {
        if (cartList!!.size != 0) {
            var total = 0.0
            for (item in cartList!!) {
                total += getGrandTotal(item)
            }
            val number2digits: Double = String.format("%.2f", total).toDouble()
            tv_check_out.text = "Buy " + cartList!!.size + " Product on " + number2digits
        } else {
            tv_check_out.text = requireContext().getString(R.string.buy_now)
        }
    }

    fun getGrandTotal(model: Cart): Double {
        val itemQty = model.quantity
        val itemPrice = model.price.toDouble()
        val total = itemPrice * itemQty
        val number2digits: Double = String.format("%.2f", total).toDouble()
        return (number2digits)
    }

    private fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showBackOnToolBar()
        //getMainActivity().showTitleOnToolBar(getString(R.string.order_car_parts))
        getMainActivity().lockDrawer()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.cart), View.VISIBLE)

    }

    private fun initRv() {
        cartList = mutableListOf()
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        Collections.sort(cartList!!, MyListComparator())
        adapter = CartAdapter(requireContext(), cartList)
        adapter?.appClickListener = object : CartAdapter.AppClickListener {
            override fun onClickListener(view: View, position: Int) {
                val model = cartList?.get(position) ?: return
                val model2 = products?.get(position)
                when (view!!.id) {
                    R.id.cl_view -> {
                        openCarPartDetailFragment(model)
                    }
                    R.id.iv_delete -> {
                        cartList?.remove(model)
                        products?.remove(model2)
                        changeTextButton()
                        adapter?.notifyDataSetChanged()
                        viewModel.deleteProduct(cart = model)
                        if (cartList!!.size == 0) {
                            tv_no_data_fount.visibility = View.VISIBLE
                            Log.d("TAG", "onClickListener: cartList!!.size ")
                        } else {
                            Log.d("TAG", "onClickListener: cartList!!.size ${cartList!!.size} ")
                            tv_no_data_fount.visibility = View.GONE
                        }
                        showSnackBar(cl_parent, "", getString(R.string.deleted_success))
                    }
                }
            }

            override fun onAddedQuantity(view: TextView, position: Int, model: Cart) {

                if (products != null && cartList!!.size == products!!.size) {

                    val cartQty: Int = cartList!![position].quantity
                    val productQty: Int = products!![position].quantity.toInt()
                    Log.d("Cart", "onAddedQuantity: product qty: ${products!![position].quantity}")
                    Log.d("Cart", "onAddedQuantity: cart qty: ${cartList!![position].quantity}")

                    if (cartQty > productQty) {
                        showSnackBar(cl_parent, "", getString(R.string.Stock_not_available))
                        return
                    } else {
                        Log.d("Cart", "cartList: ${cartList!!.size} products: ${products!!.size} ")
                        Log.d("Cart", "cartQty: $cartQty productQty: $productQty ")
                        Log.d(
                            "cart",
                            "onAddedQuantity: model.price : ${model.price.toDouble()} model qty:${model.quantity} "
                        )
//                        cartList!![position].quantity=cartQty+1
//                        model.price=((model.price).toDouble()*model.quantity).toString()
                        cartList?.set(position, model)
                        Log.d("Cart", "model : ${model.price}")
                        view.setText("" + cartList!![position].quantity)
                        changeTextButton()
                        adapter?.notifyDataSetChanged()
                        viewModel.getUpDateProduct(cart = model)
                    }
                }
            }

            override fun onMinusQuantity(view: View, position: Int, model: Cart) {
                Log.d("Cart", "onMinusQuantity: minus")
                cartList?.set(position, model)
                changeTextButton()
                adapter?.notifyDataSetChanged()
                viewModel.getUpDateProduct(cart = model)
            }

        }

//        for (position in cartList!!.indices){
//            val cartQ = cartList!![position]
//            for (pos in products!!.indices){
//                val productQ = products!![pos]
//                if (cartQ.id_product== productQ.id){
//                    if (cartQ.quantity>productQ.quantity.toInt()){
//                        cartList!![position].quantity=products!![pos].quantity.toInt()
//                    }
//                }
//            }
//        }
        rv_view.adapter = adapter
    }

    private fun openCarPartDetailFragment(model: Cart) {
        val bundle = Bundle()
        bundle.putString("id", model.id_product)
        findNavController().navigate(R.id.action_cartFragment_to_carPartDetailFragment, bundle)
    }

    private fun getHomeSliderFromServer() {
        val allIds = getAllIds() ?: return
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.getCartItems(allIds.toString())
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    private fun getAllIds(): String? {
        if (cartList == null) return null
        if (cartList!!.isEmpty()) return null
        var ids: String? = null
        for (item in cartList!!.indices) {
            val model = cartList?.get(item)
            val idProduct = model?.id_product
            if (item == 0) {
                ids = idProduct
                continue
            }
            ids = ids + "," + idProduct
        }
        Log.d("TAG", "getAllIds: $ids")
        return ids

    }
}