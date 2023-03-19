package com.wsyapp.ui.home.garage.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.data.model.response.GarageCategoryModel
import com.wsyapp.data.model.response.GarageCategoryResponseModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.ui.home.garage.category.adapter.GarageCategoryAdapter
import com.wsyapp.utils.MyConstants
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : BaseFragment() {

    private lateinit var homeViewModel: CategoryViewModel
    private lateinit var adapter: GarageCategoryAdapter
    private var list: MutableList<GarageCategoryModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()

        noDataFoundView(View.GONE)
        initRv()
        homeViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        homeViewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                CategoryViewModel.Action.SUCCESS -> updateUi(it.payload)
                CategoryViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                CategoryViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })

        getGarageCategories()
    }

    private fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.garage))
        getMainActivity().unlockDrawer()
        getMainActivity().updateToolBar(getString(R.string.garage), View.GONE)


    }

    private fun updateUi(payload: GarageCategoryResponseModel?) {
        if (payload == null) {
            showToast(getString(R.string.not_found))
            noDataFoundView(View.VISIBLE)
            return
        }

        val results = payload.categories
        if (results == null || results.isEmpty()) {
            noDataFoundView(View.GONE)
            return
        }
        list?.clear()
        list?.addAll(results)
        adapter.notifyDataSetChanged()


    }

    private fun getGarageCategories() {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            homeViewModel.getGarageCategories(RepoConstant.API_GET_ALL_CATEGORY_GARAGE)
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    private fun initRv() {
        list = mutableListOf()
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = GarageCategoryAdapter(requireContext(), list!!)
        adapter.setAppClickListener(object : GarageCategoryAdapter.AppClickListener {
            override fun onClickListener(view: View, position: Int) {
                when (view!!.id) {
                    R.id.cl_view -> {
                        val model = list!!.get(position)
                        openCagetogoryById(model)

                    }
                }
            }

        })
        rv_view.adapter = adapter

    }

    private fun noDataFoundView(visibility: Int) {
        tv_no_data_found.visibility = visibility

    }

    private fun openCagetogoryById(model: GarageCategoryModel) {
        if (model == null) return
        val id = model.id
        if (id==null||id.isEmpty())return
        val bundle = Bundle()
        bundle.putString(MyConstants.GARAGE_CATEGORY_ID, id)
        findNavController().navigate(R.id.action_categoryFragment_to_garageFragment, bundle)
    }
}
