package com.wsyapp.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.wsyapp.BuildConfig
import com.wsyapp.R
import com.wsyapp.base.MyViewPagerAdapter
import com.wsyapp.data.model.request.HomeRequestModel
import com.wsyapp.data.model.response.HomeSliderResponseModel
import com.wsyapp.data.model.response.SliderModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.ui.home.pager.PagerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : BaseFragment(), View.OnClickListener {

    private var totalPagerItem = 0
    private var currentItem = 0

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity().showToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.home))
        getMainActivity().unlockDrawer()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.home), View.GONE)



        hideMenuUi()
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                HomeViewModel.Action.SUCCESS -> updateUi(it.payload)
                HomeViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                HomeViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
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
        ll_garage.setOnClickListener(this)
        ll_car_part.setOnClickListener(this)
        ll_car_service.setOnClickListener(this)
        ll_recovery.setOnClickListener(this)
        fab_menu.setOnClickListener(this)
        fab_close.setOnClickListener(this)
        fab_share.setOnClickListener(this)
        fab_rate.setOnClickListener(this)
        fab_call.setOnClickListener(this)

        getHomeSliderFromServer()
    }

    private fun getHomeSliderFromServer() {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.gethomeSlider(
                HomeRequestModel(
                    RepoConstant.API_ST_HOME_SLIDER,
                    BuildConfig.VERSION_CODE,
                    0
                )
            )
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    private fun updateUi(payload: HomeSliderResponseModel?) {
        if (payload == null) {
            showToast(getString(R.string.not_found))
            return
        }
        if (!payload.success) return

        checkForAppUpdate(payload)
        val slider = payload.slider
        if (slider == null || slider.isEmpty()) {
            showToast(getString(R.string.not_found))
            return
        }
        initViewPager(slider)
    }

    private fun checkForAppUpdate(payload: HomeSliderResponseModel) {
        var api_version = payload.android_version
        val versionCode = BuildConfig.VERSION_CODE
        if (api_version > versionCode) {
            getMainActivity().openAppUpdateDialog(payload)
        }
    }

    private fun openCarServiceFragment() {
        val statusGPSCheck = getMainActivity().statusGPSCheck()
        if (statusGPSCheck)
            findNavController().navigate(R.id.action_homeFragment_to_appMapFragment)
    }


    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ll_garage -> openGarageFragment()
            R.id.ll_car_part -> openCarPartFragment()
            R.id.ll_car_service -> openCarServiceFragment()
            R.id.ll_recovery -> openCarServiceFragment()
            R.id.fab_menu -> showMenuUi()
            R.id.fab_close -> hideMenuUi()
            R.id.fab_share -> share(requireContext().resources.getString(R.string.app_not_on_play_store))
            R.id.fab_rate -> rateApp()
            R.id.fab_call -> openConfirmDialog()
        }
    }


    private fun showMenuUi() {
        cl_fabs.visibility = View.VISIBLE
        fab_menu.visibility = View.GONE


    }



    private fun hideMenuUi() {
        cl_fabs.visibility = View.GONE
        fab_menu.visibility = View.VISIBLE
    }

    private fun openGarageFragment() {
        findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
    }

    private fun openCarPartFragment() {
        findNavController().navigate(R.id.action_homeFragment_to_carPartFragment)
    }

    private fun initViewPager(slider: List<SliderModel>) {
        val adapter = MyViewPagerAdapter(childFragmentManager)

        for (item in slider) {
            val pagerFragment = PagerFragment()
            pagerFragment.setSliderModel(item)
            adapter.addFragment(pagerFragment)
        }
        tabs.setupWithViewPager(view_pager)
        totalPagerItem = slider.size
        view_pager.adapter = adapter
        view_pager.currentItem = 0
        handler.post(runnable)
    }

    open fun share(url: String) {
        getMainActivity().share(url)
    }

    fun rateApp() {
        getMainActivity().rateApp()

    }

    private fun openConfirmDialog() {
        val mainActivity = getMainActivity()

        mainActivity.openConfirmDialog(mainActivity.getString(R.string.msg))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_cart, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cart -> getMainActivity().openCartFragment()
        }
        return true
    }
}