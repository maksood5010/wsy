package com.wsyapp.ui.home.carservice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.ui.home.carservice.adapter.ViewPagerAdapterHome
import com.wsyapp.ui.home.carservice.category.adapter.CategoryItemAdapter
import kotlinx.android.synthetic.main.fragment_car_services.*


class CarServicesFragment : BaseFragment(), View.OnClickListener {

    private var currentPage = 0

    private lateinit var bannerAdapterList: MutableList<Int>
    private lateinit var originalBannerAdapterList: MutableList<Int>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initViewPager()
        iv_back.setOnClickListener(this)
        iv_next.setOnClickListener(this)
        iv_slide.setOnClickListener(this)

    }

    fun initViewPager() {
        bannerAdapterList = mutableListOf()
        originalBannerAdapterList = mutableListOf()
        originalBannerAdapterList.add(R.drawable.car_parts)
        originalBannerAdapterList.add(R.drawable.car_parts)
        originalBannerAdapterList.add(R.drawable.car_parts)
        originalBannerAdapterList.add(R.drawable.car_parts)
        originalBannerAdapterList.add(R.drawable.car_parts)
        originalBannerAdapterList.add(R.drawable.car_parts)
        originalBannerAdapterList.add(R.drawable.car_parts)
        originalBannerAdapterList.add(R.drawable.car_parts)
        originalBannerAdapterList.add(R.drawable.car_parts)
        originalBannerAdapterList.add(R.drawable.car_parts)
        bannerAdapterList.addAll(originalBannerAdapterList)
        val myViewPagerAdapter = ViewPagerAdapterHome(requireContext(), bannerAdapterList)

        //openFragment()
        view_pager.adapter = myViewPagerAdapter
        currentPage = 2
        setCurrentItem(currentPage)
        view_pager.setAnimationEnabled(true)
        view_pager.setFadeEnabled(true)
        view_pager.setFadeFactor(0.6f)

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {
                Log.e("onPageScrolled=", i.toString() + "")
            }

            override fun onPageSelected(i: Int) {
                Log.e("pageSelected=", i.toString() + "")
                /* if (bannerAdapterList.size - 1 == i) {
                     bannerAdapterList.addAll(originalBannerAdapterList)
                     myViewPagerAdapter.notifyDataSetChanged()
                 }*/
                //     openFragment()
                currentPage = i
            }

            override fun onPageScrollStateChanged(i: Int) {
                Log.e("pageScroolStateChanged=", i.toString() + "")
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_back -> {
                setPreviousPage(currentPage)
            }
            R.id.iv_next -> {
                setNextPage(currentPage + 1)
            }
            R.id.iv_slide -> {
                slideBottomView()
            }
        }
    }

    private fun slideBottomView() {
        val visibility = rv_view.visibility
        if (visibility == View.VISIBLE) {
            rv_view.visibility = View.GONE
            iv_slide.setImageResource(R.drawable.up)
        } else {
            rv_view.visibility = View.VISIBLE
            iv_slide.setImageResource(R.drawable.down_arrow)
        }
    }

    fun setPreviousPage(currentPage: Int) {
        if (currentPage <= 0 || currentPage > bannerAdapterList.size - 1) {
            return
        }
        setCurrentItem(currentPage - 1)

    }

    fun setNextPage(currentPage: Int) {
        if (currentPage <= 0 || currentPage > bannerAdapterList.size - 1) {
            return
        }
        setCurrentItem(currentPage + 1)

    }

    private fun setCurrentItem(currentPage: Int) {
        this.currentPage = currentPage
        view_pager.setCurrentItem(currentPage)
    }

    private fun initRv() {
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_view.adapter =
            CategoryItemAdapter(requireContext(), object : CategoryItemAdapter.AppClickListener {
                override fun onClickListener(view: View, position: Int) {
                    openConfirmFragment()
                }

            })
    }

    private fun openConfirmFragment() {
        findNavController().navigate(R.id.action_carServicesFragment_to_confirmFragment)
    }


}