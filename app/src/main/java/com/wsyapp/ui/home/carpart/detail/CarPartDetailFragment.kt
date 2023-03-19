package com.wsyapp.ui.home.carpart.detail

import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.wsyapp.R
import com.wsyapp.base.MyViewPagerAdapter
import com.wsyapp.data.database.entity.Cart
import com.wsyapp.data.model.response.CarPartDetailResponseModel
import com.wsyapp.data.model.response.CarPartSliderModel
import com.wsyapp.data.model.response.ProductModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.ui.home.carpart.detail.pager.PagerFragment
import com.wsyapp.utils.SnackBarHelper
import kotlinx.android.synthetic.main.fragment_car_part_detail.*
import kotlinx.android.synthetic.main.fragment_car_part_detail.cl_parent
import kotlinx.android.synthetic.main.fragment_car_part_detail.view_pager

class CarPartDetailFragment : BaseFragment(), View.OnClickListener {

    private var viewModel: CarPartDetailViewModel? = null
    private var totalPagerItem = 0
    private var currentItem = 0
    private var isAddedToCart = false
    private var isBuyNow = false

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var snackBarHelper: SnackBarHelper? = null

    private var productModel: ProductModel? = null
    private var carttModel: Cart? = null
    var cartList: MutableList<Cart>? = null
    init {
        isAddedToCart=false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_part_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        viewModel = ViewModelProviders.of(this).get(CarPartDetailViewModel::class.java)
        cartList = mutableListOf()

        viewModel?.getCartLiveData()?.observe(viewLifecycleOwner, Observer {
            cartList?.clear()
            cartList?.addAll(it)
        })
        viewModel?.initDatabase(requireContext())
        viewModel?.getLiveData()?.observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                CarPartDetailViewModel.Action.SUCCESS -> setUpdata(it.payload)
                CarPartDetailViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                CarPartDetailViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
        viewModel?.addLiveData()?.observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            if (it!!.compareTo(0L) == 0) {
                showSnackBar(cl_parent, "", getString(R.string.failed_to_add_cart))
            } else {
                if (!isBuyNow) {
                    snackBarHelper = SnackBarHelper()
                    snackBarHelper!!.addAppSnackBarListener(object :
                        SnackBarHelper.AppSnackBarListener {
                        override fun onDissmissListener(snackbar: Snackbar?) {
                            if (snackBarHelper == null) return
                            if (snackbar == null) return
                            snackbar.dismiss()
                            getMainActivity().onBackPressed()
                        }

                    })
                    snackBarHelper!!.showSnackBarWithListenner(
                        cl_parent,
                        "",
                        getString(R.string.success_add_cart), requireContext()
                    )
                }
            }
        })

        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                handler.removeCallbacks(this)
                if (currentItem == totalPagerItem) {
                    currentItem = 0
                } else {
                    currentItem = currentItem + 1
                }
                view_pager.setCurrentItem(currentItem)
                handler.postDelayed(this, 2000)
            }
        }
        viewModel?.getAllProducts()
        getCarPartDetail()

        iv_minus.setOnClickListener(this)
        iv_add.setOnClickListener(this)
        cl_add_cart.setOnClickListener(this)
        cl_buy_now.setOnClickListener(this)


    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    private fun initViewPager(slider: List<CarPartSliderModel>) {
        Log.d("TAG", "initViewPager: called")
        val adapter = MyViewPagerAdapter(childFragmentManager)
        for (item in slider) {
            val pagerFragment = PagerFragment()
            pagerFragment.setSliderModel(item)
            adapter.addFragment(pagerFragment)
        }
        totalPagerItem = slider.size
        view_pager.adapter = adapter
        view_pager.currentItem = 0
        handler.post(runnable)
    }

    private fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showBackOnToolBar()
        // getMainActivity().showTitleOnToolBar(getString(R.string.order_car_parts))
        getMainActivity().lockDrawer()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar("", View.VISIBLE)
    }

    fun setUpdata(payload: CarPartDetailResponseModel?) {
        if (payload == null) {
            showSnackBar(cl_parent, "", getString(R.string.not_found))
            return
        }
        val product = payload.product ?: return
        if (product.size == 0) return
        productModel = product?.get(0) ?: return
        for (item in cartList!!) {
            if (item.id_product == productModel!!.id) {
                carttModel = item
                // tv_quantity.setText("" + item.quantity)
                isAddedToCart = true
                cl_add_cart.setBackgroundColor(requireContext().resources.getColor(R.color.green))
            }
        }

        tv_car_name.setText(productModel?.getName(requireContext()))
        tv_shop_name.setText(productModel?.getPriceText(requireContext()))
        rating_bar.rating = productModel?.rate!!
        Glide.with(requireContext()).load(productModel?.img)
            .placeholder(R.drawable.bg_image_placeholder)
            .into(iv_garage)
        tv_na.setText(Html.fromHtml(productModel?.details))
        getMainActivity().updateToolBar(productModel?.getName(requireContext())!!, View.VISIBLE)
        Log.d("TAG", "setUpdata: images size:  ${payload.product!!.size}")
        val slider1= payload.images!!.toMutableList()
        if (slider1!=null){
            slider1.add(0, CarPartSliderModel("","", productModel!!.img,"",""))
        }

        val list = slider1
        if (list.isEmpty()) return
        initViewPager(list)
    }

    private fun getCarPartDetail() {
        val arguments = arguments ?: return
        val id = arguments.getString("id") ?: return
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, "")
            viewModel?.getCarPartDetail(RepoConstant.API_GET_CAR_PART_DETAIL, id)
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_add -> addQuantity()
            R.id.iv_minus -> minusQuantity()
            R.id.cl_add_cart -> {
                if (!isAddedToCart) {
                    addToCart()
                } else {
                    showSnackBar(cl_parent, "", getString(R.string.alreadyadded))
                }
            }
            R.id.cl_buy_now -> {
                openCartFragment()
            }
        }
    }

    private fun openCartFragment() {
        val bundle = Bundle()
        bundle.putString("id", productModel!!.id)
        if (!isAddedToCart) {
            isBuyNow = true
            addToCart()
        }
        findNavController().navigate(R.id.action_carPartDetailFragment_to_cartFragment, bundle)
    }

    private fun addToCart() {
        displayProgressDialog(false, "")
        if (productModel == null) return
        var cart = Cart()
        cart.bestseller = productModel?.bestseller!!
        cart.details = productModel?.details!!
        cart.disable = productModel?.disable!!
        cart.hide = productModel?.hide!!
        cart.id_product = productModel?.id!!
        cart.img = productModel?.img!!
        cart.likes = productModel?.likes!!
        cart.menu_cat = productModel?.menu_cat!!
        cart.name = productModel?.name!!
        cart.name_ar = productModel?.name_ar!!
        cart.price = productModel?.price!!
        cart.rate = productModel?.rate!!
        cart.shop_id = productModel?.shop_id!!
        cart.time = productModel?.time!!
        cart.weight = productModel?.weight!!
        cart.stock_quantity = productModel?.quantity!!
        cart.quantity = tv_quantity.text.toString().trim().toInt()
        if (carttModel != null) {
            carttModel!!.quantity =
                carttModel!!.quantity + tv_quantity.text.toString().trim().toInt()
            viewModel?.getUpDateProduct(cart = carttModel!!)
        } else {
            viewModel?.addInCart(cart = cart)
        }
    }

    private fun minusQuantity() {
        val trim = tv_quantity.text.toString().trim()
        val total = trim.toInt()
        if (total == 1) return
        val i = total - 1
        tv_quantity.setText("" + i)
    }

    private fun addQuantity() {
        val trim = tv_quantity.text.toString().trim()
        val total = trim.toInt()
        val i = total + 1
        if (i > productModel?.quantity!!) {
            showSnackBar(cl_parent, "", getString(R.string.Stock_not_available))
            return
        }
        tv_quantity.setText("" + i)
    }

    override fun onDestroyView() {
        if (snackBarHelper == null) {
            super.onDestroyView()
            return
        }
        snackBarHelper?.removeAppSnackBarListener()
        val snackbar = snackBarHelper?.getSnackbar() ?: return
        snackbar.dismiss()
        super.onDestroyView()
    }
}