package com.wsyapp.ui.home.carpart

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.data.model.response.CarPartCategoryModel
import com.wsyapp.data.model.response.CarPartProductModel
import com.wsyapp.data.model.response.CarPartResponseModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.ui.home.carpart.adapter.CarPartCategoryAdapter
import com.wsyapp.ui.home.carpart.adapter.CarPartItemAdapter
import kotlinx.android.synthetic.main.fragment_car_part.*

class CarPartFragment : BaseFragment() {

    private var searchView: SearchView? = null
    private var viewModel: CarPartViewModel? = null
    private var adapterProduct: CarPartItemAdapter? = null
    private var QueryText: String? = ""
    private var adapterCategory: CarPartCategoryAdapter? = null
    private var categoryList: MutableList<CarPartCategoryModel>? = null
    private var productList: MutableList<CarPartProductModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_car_part, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        // if (viewModel == null) {
        updateNoDataViewVisibility(View.GONE)
        initRvCat()
        initRv()
        viewModel = ViewModelProviders.of(this).get(CarPartViewModel::class.java)
        viewModel?.getLiveData()?.observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                CarPartViewModel.Action.SUCCESS -> updateUi(it.payload)
                CarPartViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                CarPartViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
        getCarPartProducs()
        // }

    }

    private fun updateUi(payload: CarPartResponseModel?) {
        if (payload == null) {
            showSnackBar(cl_parent, "", getString(R.string.not_found))
            return
        }

        updateCategories(payload.categories)

        val products = payload.products ?: return
        updateProducts(products)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_rent_search, menu)
        val searchManager =
            getMainActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val findItem = menu.findItem(R.id.menu_search) ?: return
        searchView = findItem.actionView as SearchView
        searchView?.setSearchableInfo(
            searchManager
                .getSearchableInfo(getMainActivity().componentName)
        )
        searchView?.setMaxWidth(Integer.MAX_VALUE);
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // carsdapter?.searchByName(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter(query)
                adapterProduct?.searchByName(newText)
                QueryText=newText
                return false
            }

        })

        findItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
//                ?.searchByName("")
                adapterProduct?.searchByName("")
                QueryText=""
                return true
            }

        })
    }

    private fun updateProducts(products: List<CarPartProductModel>?) {
        if (products == null) {
            showSnackBar(cl_parent, "", getString(R.string.not_found))
            updateNoDataViewVisibility(View.VISIBLE)
            return
        }
        if (adapterProduct == null) return
        productList?.clear()
        productList?.addAll(products)
        adapterProduct?.notifyDataSetChanged()
    }

    private fun updateNoDataViewVisibility(visibility: Int) {
        if (tv_no_data_fount!=null)
        tv_no_data_fount.visibility = visibility
    }

    private fun updateCategories(categories: List<CarPartCategoryModel>?) {
        if (categories == null) return
        if (adapterCategory == null) return
        categoryList?.clear()
        categoryList?.add(0, CarPartCategoryModel("0", "All", "All", true))
        categoryList?.addAll(categories)
        adapterCategory?.notifyDataSetChanged()
    }

    private fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.order_car_parts))
        getMainActivity().unlockDrawer()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.order_car_parts), View.GONE)


    }

    private fun initRvCat() {
        categoryList = mutableListOf()
        rv_view_cat.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterCategory = CarPartCategoryAdapter(requireContext(), categoryList)
        adapterCategory?.setAppClickListener(object : CarPartCategoryAdapter.AppClickListener {
            override fun onClickListener(view: View, position: Int, model: CarPartCategoryModel) {
                if (adapterProduct == null || productList == null) return
                Log.d("TAG", "onClickListener:adapterCategory clicked Query $QueryText")
//                adapterProduct?.searchByName(QueryText)
                val filter = adapterProduct?.filter ?: return
                filter.filter(model.id)
            }
        })
        rv_view_cat.adapter = adapterCategory

    }

    private fun initRv() {
        productList = mutableListOf()
        rv_view.layoutManager =
            GridLayoutManager(requireContext(), 2)
        adapterProduct = CarPartItemAdapter(requireContext(), productList)
        adapterProduct?.setAppClickListener(object : CarPartItemAdapter.AppClickListener {
            override fun onClickListener(view: View, position: Int, model: CarPartProductModel) {
                openCarPartDetailFragment(model)
            }

            override fun onNoData() {
                Log.d("TAG", "onNoData: View VISIBLE")
                Handler().postDelayed(Runnable { updateNoDataViewVisibility(View.VISIBLE) }, 500)
            }

            override fun onDataFound() {
                updateNoDataViewVisibility(View.GONE)
            }
        })
        rv_view.adapter = adapterProduct

    }

    private fun openCarPartDetailFragment(model: CarPartProductModel) {
        val bundle = Bundle()
        bundle.putString("id", model.id)
        findNavController().navigate(R.id.action_carPartFragment_to_carPartDetailFragment, bundle)
    }

    private fun getCarPartProducs() {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, "")
            viewModel?.getCarPartProducs(RepoConstant.API_GET_CAR_PART)
        } else {
            showToast(getString(R.string.network_error))
        }
    }
}