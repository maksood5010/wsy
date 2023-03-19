package com.wsyapp.ui.home.garage.details

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.bumptech.glide.Glide
import com.wsyapp.R
import com.wsyapp.base.MyViewPagerAdapter
import com.wsyapp.data.model.request.GarageDetailRequestModel
import com.wsyapp.data.model.response.GarageDetailResponseModel
import com.wsyapp.data.model.response.GarageServiceModel
import com.wsyapp.data.model.response.GarageSliderModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.ui.home.garage.details.adapter.GarageServiceAdapter
import com.wsyapp.ui.home.garage.details.pager.GaragePagerFragment
import com.wsyapp.utils.MyConstants
import kotlinx.android.synthetic.main.fragment_garage_detail.*

class GarageDetailFragment : BaseFragment(), View.OnClickListener {

    private var totalPagerItem = 0
    private var currentItem = 0

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private lateinit var viewModel: GarageDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garage_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        viewModel = ViewModelProviders.of(this).get(GarageDetailViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                GarageDetailViewModel.Action.SUCCESS -> updateUi(it.payload)
                GarageDetailViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                GarageDetailViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
        tv_request.setOnClickListener(this)

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
        getGarageDetail()
    }

    private fun initViewPager(slider: List<GarageSliderModel>) {
        val adapter = MyViewPagerAdapter(childFragmentManager)

        for (item in slider) {
            val pagerFragment = GaragePagerFragment()
            pagerFragment.setSliderModel(item)
            adapter.addFragment(pagerFragment)
        }
        tabs.setupWithViewPager(view_pager)
        totalPagerItem = slider.size
        view_pager.adapter = adapter
        view_pager.currentItem = 0
        handler.post(runnable)
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    private fun updateUi(payload: GarageDetailResponseModel?) {
        if (payload == null) {
            showToast(getString(R.string.not_found))
            return
        }

        val garage = payload.garage ?: return
        if (garage == null || garage.isEmpty()) return
        val model = garage.get(0)
        Glide.with(requireContext()).load(model.cover).placeholder(R.drawable.bg_image_placeholder)
            .into(iv_garage)
        tv_title.setText(model.getName(requireContext()))
        rating_bar.rating = (model.rate)
        tv_address.setText(model.address)
        tv_working_hour.setText(model.hours)
        tv_title.setText(model.getName(requireContext()))
        tv_title.setText(model.getName(requireContext()))
        getMainActivity().showTitleOnToolBar(model.getName(requireContext())!!)

        var slider = payload.slider
        if (slider == null || slider.isEmpty()) {
            slider = mutableListOf()
            val garageSliderModel = GarageSliderModel("1", "0", "1", model.cover, "1")
            slider.add(garageSliderModel)
        }
        initViewPager(slider)

        val services = payload.services ?: return
        if (services == null || services.isEmpty()) return
        initRv(services)
    }

    private fun getGarageDetail() {
        val arguments = arguments ?: return
        val id = arguments.getString(MyConstants.GARAGE_DETAIL_ID) ?: return

        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.getGarageDetail(
                GarageDetailRequestModel(
                    RepoConstant.API_GARAGE_DETAIL,
                    "1",
                    id
                )
            )
        } else {
            showToast(getString(R.string.network_error))
        }
    }


    private fun openImageViewerFragment() {
        findNavController().navigate(R.id.action_garageDetailFragment_to_imageViewerFragment)
    }

    private fun openGarageRequestFragment() {
        findNavController().navigate(R.id.action_garageDetailFragment_to_garageRequestFragment)
    }

    //Edes$202019
    fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().lockDrawer()
        getMainActivity().showBackOnToolBar()
        getMainActivity().hideRightAction()

        getMainActivity().updateToolBar(getString(R.string.garage), View.VISIBLE)


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_request -> openGarageRequestFragment()
        }
    }

    private fun initRv(services: List<GarageServiceModel>) {
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_view.adapter =
            GarageServiceAdapter(
                requireContext(), services
            )
    }
}