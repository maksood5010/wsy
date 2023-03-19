package com.wsyapp.ui.home.garage.subcategory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import com.wsyapp.R
import com.wsyapp.data.model.response.GarageSubCategoryModel
import com.wsyapp.data.model.response.GarageSubCategoryResponseModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.ui.home.garage.adapter.GarageItemAdapter
import com.wsyapp.utils.MyConstants
import kotlinx.android.synthetic.main.fragment_garage.*

private const val TAG = "GarageFragment"

class GarageFragment : BaseFragment(), View.OnClickListener {

    lateinit var sp_emirates: MaterialBetterSpinner
    private lateinit var viewModel: GarageSubcategoryViewModel

    private var adapter: GarageItemAdapter? = null
    private var list: MutableList<GarageSubCategoryModel>? = null
    private var selectedList: MutableList<GarageSubCategoryModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPresentView(inflater, container, savedInstanceState, R.layout.fragment_garage)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            sp_emirates = view.findViewById(R.id.sp_emirates)
            Log.e(TAG, "onViewCreated")

            initToolBar()
            initRv()
            initSpinner()
            tv_req_quotion.setOnClickListener(this)
            chk_select_all.setOnCheckedChangeListener { p0, p1 ->
                if (p1) {
                    if (adapter != null)
                        adapter?.selectAllSelection() ?: return@setOnCheckedChangeListener
                } else {
                    if (adapter != null)
                        adapter?.removeAllSelection() ?: return@setOnCheckedChangeListener
                }
            }

            viewModel = ViewModelProviders.of(this).get(GarageSubcategoryViewModel::class.java)

            viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
                dismissProgressDialog()
                Log.e(TAG, "getLiveData")

                when (it.action) {
                    GarageSubcategoryViewModel.Action.SUCCESS -> updateUi(it.payload)
                    GarageSubcategoryViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                    GarageSubcategoryViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                    else -> showToast(getString(R.string.Something_went_wrong))
                }
            })
            noDataFoundView(View.GONE)
            getGarageCategories()
        }

    }

    private fun getGarageCategories() {
        Log.e(TAG, "getGarageCategories: 1")

        val arguments = arguments ?: return
        val id = arguments.getString(MyConstants.GARAGE_CATEGORY_ID) ?: return

        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            Log.e(TAG, "getGarageCategories: id: ${id.toInt()}")

            viewModel.getSubGarageCategories(RepoConstant.API_GARAGE_BY_CATEGORY, id.toInt())
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    private fun updateUi(payload: GarageSubCategoryResponseModel?) {
        Log.e(TAG, "updateUi: 1")

        if (payload == null) {
            showToast(getString(R.string.not_found))
            noDataFoundView(View.VISIBLE)
            return
        }

        val results = payload.garages
        if (results == null || results.isEmpty()) {
            noDataFoundView(View.GONE)
            return
        }
        list?.clear()
        list?.addAll(results)
        val tag = sp_emirates.getTag()
        if (tag != null) {
            tag as String
            if (adapter == null || adapter?.filter == null) return
            adapter!!.filter?.filter(tag as String)
            return
        }

        adapter!!.notifyDataSetChanged()
        Log.e(TAG, "updateUi")


    }

    private fun noDataFoundView(visibility: Int) {
        tv_no_data_found.visibility = visibility

    }

    private fun initSpinner() {
        val emirates = resources.getStringArray(R.array.emirates)
        val emirates_id = resources.getStringArray(R.array.emirates_id)
        val adapter2 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            emirates
        )
        sp_emirates.setAdapter(adapter2)
        sp_emirates.setText(emirates[0])
        Log.e(TAG, "initSpinner")

        sp_emirates.setOnItemClickListener { adapterView, view, position, l ->

            if (adapter == null || adapter?.filter == null) return@setOnItemClickListener

            sp_emirates.setTag(emirates_id[position])
            adapter!!.filter.filter(emirates_id[position])
        }

    }


    private fun initRv() {
        Log.e(TAG, "initRv 1")
        list = mutableListOf()
        selectedList = mutableListOf()
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter =
            GarageItemAdapter(requireContext(), list, object : GarageItemAdapter.AppClickListener {
                override fun onClickListener(
                    view: View,
                    position: Int,
                    model: GarageSubCategoryModel?
                ) {
                    when (view!!.id) {
                        R.id.cl_view -> {
                            if (model == null)
                                openGarageDetailsFragment(list!!.get(position))
                            else
                                openGarageDetailsFragment(model)
                        }
                    }
                }

                override fun onItemCheckedListener(
                    view: View,
                    position: Int,
                    model: GarageSubCategoryModel?
                ) {
                    if (model != null)
                        selectedList?.add(model)
                }

                override fun onItemRemovedListener(
                    view: View,
                    position: Int,
                    model: GarageSubCategoryModel?
                ) {
                    if (model != null) {
                        if (selectedList == null) return
                        for (item in selectedList!!.indices) {
                            val get = selectedList?.get(item) ?: return
                            if (get.id == model.id) {
                                selectedList?.removeAt(item)
                                break
                            }

                        }


                    }
                }

                override fun onAllItemCheckedListener(list: MutableList<GarageSubCategoryModel>?) {
                    if (list != null) {
                        selectedList?.clear() ?: return
                        selectedList?.addAll(list!!) ?: return
                    }

                }

                override fun onAllItemRemovedListener(list: MutableList<GarageSubCategoryModel>?) {
                    if (list != null) {
                        selectedList?.clear()
                    }
                }

            })
        rv_view.adapter = adapter
        Log.e(TAG, "initRv 2")
    }

    private fun openGarageDetailsFragment(model: GarageSubCategoryModel) {
        val bundle = Bundle()
        bundle.putString(MyConstants.GARAGE_DETAIL_ID, model.id)
        findNavController().navigate(R.id.action_garageFragment_to_garageDetailFragment, bundle)
    }

    private fun openGarageRequestFragment() {
        if (selectedList == null || selectedList?.size == 0) {
            showSnackBar(cl_parent, "", getString(R.string.no_item_selected))
            return
        }
        findNavController().navigate(R.id.action_garageFragment_to_garageRequestFragment)
    }

    private fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showBackOnToolBar()
        //getMainActivity().hideTitleOnToolBar()
        getMainActivity().lockDrawer()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.garage), View.VISIBLE)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_req_quotion -> {
                openGarageRequestFragment()
            }
        }
    }
}